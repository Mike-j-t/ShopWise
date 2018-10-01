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
 * Rule Period ListView/Spinner Cursor Adapter
 */

@SuppressWarnings("WeakerAccess")
public class AdapterRulePeriodList extends CursorAdapter {

    private final Intent callerintent;
    private final Context ctxt;
    private final boolean fromspinner;
    private Cursor cursor;

    private int appvalues_name_offset = -1;
    private int appvalues_text_offset = 0;
    public static final String THISCLASS = AdapterRulePeriodList.class.getSimpleName();
    private static final String LOGTAG = "SW_ARPL(CsrAdptr)";

    AdapterRulePeriodList(Context context, Cursor csr, @SuppressWarnings("UnusedParameters") int flags, Intent intent) {
        super(context, csr, 0);
        String msg = "Constructing";
        String methodname = "Construct";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        this.ctxt = context;
        this.callerintent = intent;
        this.fromspinner = false;
        setRulePeriodOffsets(csr);
    }

    AdapterRulePeriodList(Context context, Cursor csr, @SuppressWarnings({"SameParameterValue", "UnusedParameters"}) int flags, Intent intent,
                          @SuppressWarnings("SameParameterValue") boolean fromspinner) {
        super(context, csr, 0);
        String msg = "Constructing";
        String methodname = "Construct";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        this.ctxt = context;
        this.callerintent = intent;
        this.fromspinner = fromspinner;
        setRulePeriodOffsets(csr);

    }

    @Override
    public View newView(Context context, Cursor csr, ViewGroup parent) {
        String msg = "Inflating Layout";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        this.cursor = csr;
        return LayoutInflater.from(context).inflate(
                R.layout.ruleperiodlist,
                parent,
                false
        );
    }

    @Override
    public void bindView(View view, Context context, Cursor csr) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        initView(view,csr);

    }

    @Override
    public View getDropDownView(int position, View convertview, ViewGroup parent) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        super.getDropDownView(position, convertview, parent);
        View view = convertview;
        if (fromspinner) {
            view = View.inflate(ctxt,R.layout.ruleperiodlist,null);
            view.setBackgroundResource(R.drawable.textviewborder);
            int evenrow = ActionColorCoding.setHeadingColor(ctxt,
                    callerintent,
                    ActionColorCoding.getColorsPerGroup() -1) &
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

    @Override
    public View getView(int position, View convertview, ViewGroup parent) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        View view = super.getView(position, convertview, parent);

        int evenrow = ActionColorCoding.setHeadingColor(ctxt,
                callerintent,
                ActionColorCoding.getColorsPerGroup() -1) &
                ActionColorCoding.transparency_evenrow;
        int oddrow = evenrow & ActionColorCoding.transparency_oddrow;
        if (position % 2 == 0 ) {
            view.setBackgroundColor(evenrow);
        } else {
            view.setBackgroundColor(oddrow);
        }
        return view;
    }

    @SuppressWarnings("UnusedReturnValue")
    private View initView(View view, Cursor csr) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        TextView ruleperiod = view.findViewById(R.id.ruleperiod);
        ruleperiod.setText(csr.getString(appvalues_text_offset));
        msg = "Set Period=" + ruleperiod.getText().toString();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        return view;
    }

    private void setRulePeriodOffsets(Cursor csr) {
        if (appvalues_name_offset != -1) {
            return;
        }
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        appvalues_name_offset = csr.getColumnIndex(
                DBAppvaluesTableConstants.APPVALUES_NAME_COL
        );
        appvalues_text_offset = csr.getColumnIndex(
                DBAppvaluesTableConstants.APPVALUES_TEXT_COL
        );
    }
}
