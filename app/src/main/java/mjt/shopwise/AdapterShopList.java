package mjt.shopwise;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * AdapterShopList - Custom Cusror Adapter for List of Shops
 *                      Note has been written for ListView and Spinner
 *                      i.e. includes overridden getDropDownView method.
 */
class AdapterShopList extends CursorAdapter {

    private Intent callerintent;
    private Context ctxt;
    private boolean fromspinner;
    private Cursor cursor;

    private int shops_shopid_offset = -1;
    private int shops_shopname_offest = 0;
    private int shops_shopstreet_offset = 0;
    private int shops_shopcity_offset = 0;
    private int shops_shopstate_offset = 0;
    private int shops_shopnotes_offset = 0;
    private int shops_shoporder_offset = 0;


    /**
     * AdapterShopList Constructor - shortform for ListView only
     *
     * @param context     the context
     * @param csr         the csr
     * @param flags       the flags
     * @param intent      the intent
     */
    AdapterShopList(Context context, Cursor csr, int flags, Intent intent) {
        super(context, csr, 0);
        this.ctxt = context;
        this.cursor = csr;
        this.callerintent = intent;
        this.fromspinner = false;
        setShopOffsets(csr);

    }

    /**
     * AdapterShopList Constructor - longform for Spinner and ListView
     *
     * @param context       the context
     * @param csr           the SQLite cursor containg the rows
     * @param flags         ignored but required
     * @param intent        the intent, used for
     * @param fromspinner   true if used by a spinner
     */
    AdapterShopList(Context context,
                    Cursor csr,
                    int flags,
                    Intent intent,
                    boolean fromspinner) {
        super(context, csr, 0);
        ctxt = context;
        callerintent = intent;
        this.fromspinner = fromspinner;
        this.cursor = csr;
        setShopOffsets(csr);
    }

    /**
     * newView - inflates the layout used for the selector
     * @param context   The context from the invoking activity
     * @param csr       The cusros containing the data rows
     * @param parent    The parent Viewgroup
     * @return          The modified/inflated view
     */
    @Override
    public View newView(Context context, Cursor csr, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(
                R.layout.shoplist,
                parent,
                false
        );
    }

    /**
     * bindView - Used to tie the data to the current view
     * Note!        see initView
     *
     * @param view      The inflated view
     * @param context   The context from the invoking activity
     * @param csr       The SQLite cursor containing the data
     */
    @Override
    public void bindView(View view, Context context, Cursor csr) {
        int position = csr.getPosition();
        view = initView(view, csr);
    }

    /**
     * getDropDownView - invoked when building the dropdown view of a Spinner
     * @param position      the position within the list
     * @param convertview   the view to be inflated
     * @param parent        the owning ViewGroup
     * @return              the inflated and modified view
     */
    @Override
    public View getDropDownView(int position, View convertview, ViewGroup parent) {
        super.getDropDownView(position, convertview, parent);
        View view = convertview;
        if (fromspinner) {
            view = View.inflate(ctxt,R.layout.shoplist,null);
            view.setBackgroundResource(R.drawable.textviewborder);
            int evenrow = ActionColorCoding.setHeadingColor(ctxt,
                    callerintent,
                    ActionColorCoding.getColorsPerGroup() - 1) &
                    ActionColorCoding.transparency_evenrow;
            int oddrow = evenrow & ActionColorCoding.transparency_oddrow;
            if (position % 2 == 0) {
                ActionColorCoding.setActionButtonColor(view,evenrow);
            } else {
                ActionColorCoding.setActionButtonColor(view,oddrow);
            }
        }
        bindView(view, ctxt, this.cursor);
        return view;
    }

    /**
     * getView - invoked when build the ListView i.e. not for spinner
     * @param position      the position of the element in the list
     * @param convertview   the view to be converted/inflated/moodified
     * @param parent        the owning ViewGroup
     * @return              the modified view
     */
    @Override
    public View getView(int position, View convertview, ViewGroup parent) {
        View view = super.getView(position, convertview, parent);

        /**
         * Alternate row backgrounds based upon the respective colorcode
         * using the last shade for that primary color but then applying
         * opacity.
         */
        int evenrow = ActionColorCoding.setHeadingColor(ctxt,
                callerintent,
                ActionColorCoding.getColorsPerGroup() - 1
                ) & ActionColorCoding.transparency_evenrow;
        int oddrow = evenrow & ActionColorCoding.transparency_oddrow;
        if (position % 2 == 0) {
            view.setBackgroundColor(evenrow);
        } else {
            view.setBackgroundColor(oddrow);
        }
        return view;
    }

    /**
     * initView - bind the data to the views
     * @param view      The view to which the data is to be bound to
     * @param cursor    The SQlite cursor that contains the data as rows
     * @return          The modifed view
     */
    private View initView(View view, Cursor cursor) {

        TextView nametv = (TextView) view.findViewById(R.id.shoplist_name);
        TextView citytv = (TextView) view.findViewById(R.id.shoplist_city);
        TextView ordertv = (TextView) view.findViewById(R.id.shoplist_order);

        String shopname = cursor.getString(shops_shopname_offest);
        String shopcity = cursor.getString(shops_shopcity_offset);
        String shopstreet = cursor.getString(shops_shopstreet_offset);
        String shopstate = cursor.getString(shops_shopstate_offset);
        String shopnotes = cursor.getString(shops_shopnotes_offset);
        String shoporder = cursor.getString(shops_shoporder_offset);

        nametv.setText(shopname);
        citytv.setText(shopcity);
        ordertv.setText(shoporder);

        return view;
    }

    /**
     * setShopOffsets - get the cursor's column offsets just one
     * @param csr       The SQLite cursor that contains the data as rows
     */
    private void setShopOffsets(Cursor csr) {
        shops_shopid_offset = csr.getColumnIndex(
                DBShopsTableConstants.SHOPS_ID_COL
        );
        shops_shopname_offest = csr.getColumnIndex(
                DBShopsTableConstants.SHOPS_NAME_COL
        );
        shops_shopstreet_offset = csr.getColumnIndex(
                DBShopsTableConstants.SHOPS_STREET_COL
        );
        shops_shopcity_offset = csr.getColumnIndex(
                DBShopsTableConstants.SHOPS_CITY_COL
        );
        shops_shopstate_offset = csr.getColumnIndex(
                DBShopsTableConstants.SHOPS_STATE_COL
        );
        shops_shopnotes_offset = csr.getColumnIndex(
                DBShopsTableConstants.SHOPS_NOTES_COL
        );
        shops_shoporder_offset = csr.getColumnIndex(
                DBShopsTableConstants.SHOPS_ORDER_COL
        );
    }
}
