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
 * AdapterProductList - Custom Cursor Adapter for Products List
 *                      Note caters for ListView and Spinner
 *                      i.e. getView for ListView and getDropDownView
 *                      for Spinner
 */

public class AdapterProductList extends CursorAdapter {

    private Intent callerintent;
    private Context ctxt;
    private boolean fromspinner;
    private Cursor cursor;

    private static int products_productid_offset = -1;
    private static int products_productname_offset = 0;

    /**
     * AdapterProductList Constructor - shortform for ListView only
     *
     * @param context     the context
     * @param csr         the csr
     * @param flags       the flags
     * @param intent      the intent
     */
    AdapterProductList(Context context, Cursor csr, int flags, Intent intent) {
        super(context, csr, 0);
        this.ctxt = context;
        this.cursor = csr;
        this.callerintent = intent;
        this.fromspinner = false;
        setProductOffsets(this.cursor);

    }

    AdapterProductList(Context context,
                       Cursor csr,
                       int flags,
                       Intent intent,
                       boolean fromspinner) {
        super(context, csr, 0);
        ctxt = context;
        callerintent = intent;
        this.fromspinner = fromspinner;
        this.cursor = csr;
        setProductOffsets(csr);
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
        this.cursor = csr;
        return LayoutInflater.from(context).inflate(
                R.layout.productlist,
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
            view = View.inflate(ctxt, R.layout.productlist,null);
            view.setBackgroundResource(R.drawable.textviewborder);
            int evenrow = ActionColorCoding.setHeadingColor(ctxt,
                    callerintent,
                    ActionColorCoding.getColorsPerGroup() -1) &
                    ActionColorCoding.transparency_evenrow;
            int oddrow = evenrow & ActionColorCoding.transparency_oddrow;
            if (position % 2 == 0) {
                ActionColorCoding.setActionButtonColor(view,evenrow);
            } else {
                ActionColorCoding.setActionButtonColor(view,oddrow);
            }
        }
        this.cursor.moveToPosition(position);
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
    public View getView(int position, View convertview, ViewGroup parent) {
        View view = super.getView(position, convertview, parent);

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

        TextView nametv = (TextView) view.findViewById(R.id.productlist_name);

        String productname = cursor.getString(products_productname_offset);

        nametv.setText(productname);
        return view;
    }


    /**
     * setProductOffsets
     */
    private void setProductOffsets(Cursor csr) {
        if (products_productid_offset != -1) {
            return;
        }
        products_productid_offset = csr.getColumnIndex(
                DBProductsTableConstants.PRODUCTS_ID_COL
        );
        products_productname_offset = csr.getColumnIndex(
                DBProductsTableConstants.PRODUCTS_NAME_COL
        );
    }
}



