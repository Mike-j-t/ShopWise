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
 * AdapterAisleList - Custom Cusror Adapter for List of Shops
 *                      Note has been written for ListView and Spinner
 *                      i.e. includes overridden getDropDownView method.
 */
@SuppressWarnings({"WeakerAccess", "FieldCanBeLocal", "CanBeFinal"})
public class AdapterAisleList extends CursorAdapter{

    private Intent callerintent;
    private final Context ctxt;
    private boolean fromspinner;
    private boolean clickable;
    private boolean longclickable;
    private Cursor cursor;

    private int aisles_aisleid_offset = -1;
    private int aisles_aislename_offset = 0;
    private int aisles_aisleorder_offset = 0;
    @SuppressWarnings("unused")
    private int aisles_aisleshopref_offset = 0;
    @SuppressWarnings("unused")
    private int aisles_aislenotes_offset = 0;
    public static final String THISCLASS = AdapterAisleList.class.getSimpleName();
    public static final String LOGTAG = "SW_AAL(CsrAdpt)";


    /**
     * AdapterSAisleList Constructor - longform for Spinner and ListView
     *
     * @param context       the context
     * @param csr           the SQLite cursor containg the rows
     * @param flags         ignored but required
     * @param intent        the intent, used for
     * @param fromspinner   true if used in a spinner
     */
    @SuppressWarnings({"UnusedParameters", "SameParameterValue"})
    AdapterAisleList(Context context,
                     Cursor csr,
                     int flags,
                     Intent intent,
                     @SuppressWarnings("SameParameterValue") boolean fromspinner,
                     boolean clickable,
                     boolean longclickable) {
        super(context, csr, 0);
        String msg = "Constructing";
        String methodname = "Construct";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        ctxt = context;
        callerintent = intent;
        this.fromspinner = fromspinner;
        this.cursor = csr;
        this.clickable = clickable;
        this.longclickable = longclickable;
        setAisleOffsets(csr);
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
        String msg = "Inflating Layout";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        this.cursor = csr;
        View rv = LayoutInflater.from(context).inflate(
                R.layout.aislelist,
                parent,
                false
        );
        if (fromspinner) {
            ((TextView) rv.findViewById(R.id.rowind_click)).setText("");
            ((TextView) rv.findViewById(R.id.rowind_longclick)).setText("");
        }
        return rv;
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
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        initView(view, csr);

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
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        View view = convertview;
        if (fromspinner) {
            view = View.inflate(ctxt,R.layout.aislelist,null);
            view.setBackgroundResource(R.drawable.textviewborder);
            int evenrow = ActionColorCoding.setHeadingColor(ctxt,
                    callerintent,
                    ActionColorCoding.getColorsPerGroup() - 1) &
                    ActionColorCoding.transparency_evenrow;
            int oddrow = evenrow & ActionColorCoding.transparency_oddrow;
            if (position % 2 == 0) {
                ActionColorCoding.setActionButtonColor(view, evenrow);
            } else {
                ActionColorCoding.setActionButtonColor(view, oddrow);
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
    @Override
    public View getView(int position, View convertview, ViewGroup parent) {
        View view = super.getView(position, convertview, parent);

        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        int evenrow = ActionColorCoding.setHeadingColor(ctxt,
                callerintent,
                ActionColorCoding.getColorsPerGroup() -1 ) &
                ActionColorCoding.transparency_evenrow;
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
    @SuppressWarnings("UnusedReturnValue")
    private View initView(View view, Cursor cursor) {

        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        String clickablerowindictor =
                ctxt.getResources().getString(R.string.clickrowindicator);
        String longclickablerowindicator =
                ctxt.getResources().getString(R.string.longclickrowindicator);
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);

        TextView nametv = view.findViewById(R.id.aislelist_name);
        TextView ordertv = view.findViewById(R.id.aislelist_order);

        String aislename = cursor.getString(aisles_aislename_offset);
        String aisleorder = cursor.getString(aisles_aisleorder_offset);

        nametv.setText(aislename);
        ordertv.setText(aisleorder);

        if (!clickable || fromspinner) {
            clickablerowindictor = "";
        }
        if (!longclickable || fromspinner) {
            longclickablerowindicator = "";
        }

        ((TextView) view.findViewById(R.id.rowind_click)).setText(clickablerowindictor);
        ((TextView) view.findViewById(R.id.rowind_longclick)).setText(longclickablerowindicator);
        msg = "Set AisleName=" + aislename + " Order=" + aisleorder;
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        return view;
    }

    /**
     * setShopOffsets - get the cursor's column offsets just one
     * @param csr       The SQLite cursor that contains the data as rows
     */
    private void setAisleOffsets(Cursor csr) {
        if (aisles_aisleid_offset != -1) {
            return;
        }
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        aisles_aisleid_offset = csr.getColumnIndex(
                DBAislesTableConstants.AISLES_ID_COL
        );
        aisles_aislename_offset = csr.getColumnIndex(
                DBAislesTableConstants.AISLES_NAME_COL
        );
        aisles_aisleorder_offset = csr.getColumnIndex(
                DBAislesTableConstants.AISLES_ORDER_COL
        );
        aisles_aisleshopref_offset = csr.getColumnIndex(
                DBAislesTableConstants.AISLES_SHOPREF_COL
        );
    }
}
