package mjt.shopwise;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
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

@SuppressWarnings("WeakerAccess")
public class AdapterProductList extends CursorAdapter {

    private final Intent callerintent;
    private final Context ctxt;
    private final boolean fromspinner;
    private Cursor cursor;
    private String mode="";

    private static int products_productid_offset = -1;
    private static int products_productname_offset = 0;
    private static int products_storage_offset = 0;
    private static int products_order_offset = 0;
    private static int products_storageorder_offset = 0;
    public static final String THISCLASS = AdapterProductList.class.getSimpleName();
    private static final String LOGTAG = "SW_APL(CsrAdptr)";

    /**************************************************************************
     * AdapterProductList Constructor - shortform for ListView only
     *
     * @param context     the context
     * @param csr         the csr
     * @param flags       the flags
     * @param intent      the intent
     */
    AdapterProductList(Context context, Cursor csr, int flags, Intent intent) {
        super(context, csr, 0);
        String msg = "Constructing";
        String methodname = "Construct";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
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
        String msg = "Constructing";
        String methodname = "Construct";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        ctxt = context;
        callerintent = intent;
        this.fromspinner = fromspinner;
        this.cursor = csr;
        setProductOffsets(csr);
    }

    /*************************************************************************
     * newView - inflates the layout used for the selector
     * @param context   The context from the invoking activity
     * @param csr       The cusros containing the data rows
     * @param parent    The parent Viewgroup
     * @return          The modified/inflated view
     */
    @Override
    public View newView(Context context, Cursor csr, ViewGroup parent) {
        //products_productid_offset = -1;
        //setProductOffsets(csr);
        String msg = "Inflating Layout";
        String methodname =
                new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                LOGTAG,
                msg,
                THISCLASS,
                methodname
        );

        mode = "newView";
        this.cursor = csr;
        return LayoutInflater.from(context).inflate(
                R.layout.productlist,
                parent,
                false
        );
    }

    /**************************************************************************
     * bindView - Used to tie the data to the current view
     * Note!        see initView
     *
     * @param view      The inflated view
     * @param context   The context from the invoking activity
     * @param csr       The SQLite cursor containing the data
     */
    @Override
    public void bindView(View view, Context context, Cursor csr) {
        String msg = "Invoked";
        String methodname =
                new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                LOGTAG,
                msg,
                THISCLASS,
                methodname
        );
        initView(view, csr);
    }

    /**************************************************************************
     * getDropDownView - invoked when building the dropdown view of a Spinner
     * @param position      the position within the list
     * @param convertview   the view to be inflated
     * @param parent        the owning ViewGroup
     * @return              the inflated and modified view
     */
    @Override
    public View getDropDownView(int position,
                                View convertview,
                                ViewGroup parent) {
        String msg = "Invoked";
        String methodname =
                new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                LOGTAG,
                msg,
                THISCLASS,
                methodname
        );
        //super.getDropDownView(position, convertview, parent);
        mode = "getDropDownView";

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
        //this.cursor.moveToPosition(position);
        bindView(view, ctxt, this.getCursor());
        return view;
    }

    /**************************************************************************
     * getView - invoked when build the ListView i.e. not for spinner
     * @param position      the position of the element in the list
     * @param convertview   the view to be converted/inflated/moodified
     * @param parent        the owning ViewGroup
     * @return              the modified view
     */
    public View getView(int position,
                        View convertview,
                        ViewGroup parent) {
        String msg = "Invoked";
        String methodname =
                new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                LOGTAG,
                msg,
                THISCLASS,
                methodname
        );
        View view = super.getView(position, convertview, parent);
        mode = "getView";

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



    /**************************************************************************
     * initView - bind the data to the views
     * @param view      The view to which the data is to be bound to
     * @param csr       The SQlite cursor that contains the data as rows
     * @return          The modifed view
     */
    private View initView(View view, Cursor csr) {
        String msg = "Invoked";
        String methodname =
                new Object(){}.getClass().getEnclosingMethod().getName();
        String productname;
        String storagename;
        String ordertext;
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                LOGTAG,
                msg,
                THISCLASS,methodname
        );

        TextView nametv =
                (TextView) view.findViewById(R.id.productlist_name);
        TextView storagetv =
                (TextView) view.findViewById(R.id.productlist_storage);
        TextView ordertv =
                (TextView) view.findViewById(R.id.productlist_order);

        productname = csr.getString(products_productname_offset);
        try {
            storagename = csr.getString(products_storage_offset);
        } catch (IllegalStateException e) {
            storagename = "Ooops";
        }
        try {
            int pooffset = products_order_offset;
            String otext = csr.getColumnName(pooffset);
            ordertext = csr.getString(products_order_offset);
        } catch (IllegalStateException e) {
            ordertext = "????";
        }
        nametv.setText(productname);
        storagetv.setText(storagename);
        ordertv.setText(ordertext);
        msg = "Set Product=" + productname;
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                LOGTAG,
                msg,
                THISCLASS,methodname
        );
        return view;
    }

    /**************************************************************************
     * setProductOffsets
     */
    private void setProductOffsets(Cursor csr) {
        if (products_productid_offset != -1) {
            return;
        }
        String msg = "Invoked";
        String methodname =
                new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                LOGTAG,
                msg,
                THISCLASS,
                methodname
        );
        products_productid_offset = csr.getColumnIndex(
                DBProductsTableConstants.PRODUCTS_ID_COL
        );
        products_productname_offset = csr.getColumnIndex(
                DBProductsTableConstants.PRODUCTS_NAME_COL
        );
        products_storage_offset = csr.getColumnIndex(
                DBStorageTableConstants.STORAGE_NAME_COL
        );
        products_order_offset = csr.getColumnIndex(
                DBProductsTableConstants.PRODUCTS_STORAGEORDER_COL
        );
    }
}



