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
 * AdapterStorageList
 */
@SuppressWarnings("WeakerAccess")
public class AdapterStorageList extends CursorAdapter {

    private final Intent callerintent;
    private final Context ctxt;
    private final boolean fromspinner;
    @SuppressWarnings("CanBeFinal")
    private boolean clickable;
    @SuppressWarnings("CanBeFinal")
    private boolean longclickable;
    private Cursor cursor;

    private int storage_storageid_offset = -1;
    private int storage_storagename_offset = 0;
    private int storage_storageorder_offset = 0;

    public static final String THISCLASS =
            AdapterStorageList.class.getSimpleName();
    private static final String LOGTAG = "SW_STL(CsrAdptr)";

    @SuppressWarnings("SameParameterValue")
    AdapterStorageList(Context context,
                       Cursor csr,
                       @SuppressWarnings({"SameParameterValue", "UnusedParameters"}) int flags,
                       Intent intent,
                       boolean fromspinner,
                       boolean clickable,
                       boolean longclickable) {
        super(context, csr, 0);
        String logmsg = "Constructing";
        String methodname = "Construct";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                LOGTAG,
                logmsg,
                THISCLASS,
                methodname);
        ctxt = context;
        callerintent = intent;
        this.fromspinner = fromspinner;
        this.clickable = clickable;
        this.longclickable = longclickable;
        this.cursor = csr;
        setStorageOffsets(csr);
    }

    /**
     *
     * @param context   The context from the invoking activity
     * @param csr       The SQLite cursor with the data
     * @param parent    The parent ViewGroup
     * @return          The modified View
     */
    public View newView(Context context, Cursor csr, ViewGroup parent) {
        String logmsg = "Inflating Layout";
        String methodname =
                new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                LOGTAG,
                logmsg,
                THISCLASS,
                methodname
        );
        this.cursor = csr;
        View rv = LayoutInflater.from(context).inflate(
                R.layout.storagelist,
                parent,
                false
        );
        if (fromspinner) {
            ((TextView) rv.findViewById(R.id.rowind_click)).setText("");
            ((TextView) rv.findViewById(R.id.rowind_longclick)).setText("");
        }
        return rv;
    }

    /**************************************************************************
     * bindView         Bind date from the SQlite cursor
     * @param view      The view to have the data bound to it
     * @param context   The context from the invoking activity
     * @param csr       The SQLite cursor holding the data
     */
    @Override
    public void bindView(View view, Context context, Cursor csr) {

        view = initView(view, csr);
    }

    /**************************************************************************
     * getDropDownView      Invoked when building the dropdownview of a Spinner
     * @param position      The current position within the list
     * @param convertview   The view to be inflated/modified
     * @param parent        The owning ViewGroup
     * @return              The inflated and modified View
     */
    @Override
    public View getDropDownView(int position, View convertview, ViewGroup parent) {
        String logmsg = "Invoked";
        String methodname =
                new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                LOGTAG,
                logmsg,
                THISCLASS,
                methodname
        );
        super.getDropDownView(position, convertview, parent);
        View view = convertview;
        if (fromspinner) {
            view = View.inflate(ctxt, R.layout.storagelist,null);
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
        this.cursor.moveToPosition(position);
        bindView(view, ctxt, this.cursor);
        return view;
    }

    /**************************************************************************
     * getView              invoked by ListView when the view is retrieved
     * @param position      The position within the ListView
     * @param convertview   The view to be converted/inflated/modified
     * @param parent        The owning ViewGroup
     * @return              The modified/inflated/converted view
     */
    @Override
    public View getView(int position, View convertview, ViewGroup parent) {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                LOGTAG,
                logmsg,
                THISCLASS,
                methodname
        );
        View view = super.getView(position, convertview, parent);
        int evenrow = ActionColorCoding.setHeadingColor(ctxt,
                callerintent,
                ActionColorCoding.getColorsPerGroup() -1) &
                ActionColorCoding.transparency_evenrow;
        int oddrow = evenrow & ActionColorCoding.transparency_oddrow;
        if (position % 2 == 0) {
            view.setBackgroundColor(evenrow);
        } else {
            view.setBackgroundColor(oddrow);
        }
        return view;
    }

    /**************************************************************************
     * initView         bind the data to the views
     * @param view      The view to which the data is to be bound
     * @param cursor       The SQLite cursor that contains the data
     * @return          The modified view
     */
    private View initView(View view, Cursor cursor) {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        String clickablerowindicator =
                ctxt.getResources().getString(R.string.clickrowindicator);
        String longclickablerowindicator =
                ctxt.getResources().getString(R.string.longclickrowindicator);

        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                LOGTAG,
                logmsg,
                THISCLASS,
                methodname
        );

        TextView nametv =
                view.findViewById(R.id.storagelist_name);
        TextView ordertv =
                view.findViewById(R.id.storagelist_order);

        String ivstoragename = cursor.getString(storage_storagename_offset);
        String ivstorageorder = cursor.getString(storage_storageorder_offset);

        nametv.setText(ivstoragename);
        ordertv.setText(ivstorageorder);

        if (!clickable || fromspinner) {
            clickablerowindicator = "";
        }
        if (!longclickable || fromspinner) {
            longclickablerowindicator = "";
        }

        ((TextView) view.findViewById(
                R.id.rowind_click)).setText(
                        clickablerowindicator
        );
        ((TextView) view.findViewById(
                R.id.rowind_longclick)).setText(
                        longclickablerowindicator
        );

        logmsg = "SET StorageName=" +
                nametv.getText().toString() +
                " Order=" +
                ordertv.getText().toString();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                LOGTAG,
                logmsg,
                THISCLASS,
                methodname
        );
        return view;

    }

    /**************************************************************************
     * setStorageOffsets    1 time get of cursor offsets
     * @param csr The SQLite Cursor passed to the Adapter
     */
    private void setStorageOffsets(Cursor csr) {
        if (storage_storageid_offset != -1) {
            return;
        }
        String logmsg = "Invoked";
        String methodname =
                new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                LOGTAG,
                logmsg,
                THISCLASS,
                methodname);
        storage_storageid_offset = csr.getColumnIndex(
                DBStorageTableConstants.STORAGE_ID_COL
        );
        storage_storagename_offset = csr.getColumnIndex(
                DBStorageTableConstants.STORAGE_NAME_COL
        );
        storage_storageorder_offset = csr.getColumnIndex(
                DBStorageTableConstants.STORAGE_ORDER_COL
        );
    }
}
