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
 * Created by Mike092015 on 8/01/2017.
 */

public class AdapterRulePeriodList extends CursorAdapter {

    private Intent callerintent;
    private Context ctxt;
    private boolean fromspinner;
    private Cursor cursor;

    private int appvalues_name_offset = -1;
    private int appvalues_text_offset = 0;

    AdapterRulePeriodList(Context context, Cursor csr, int flags, Intent intent) {
        super(context, csr, 0);
        this.ctxt = context;
        this.callerintent = intent;
        this.fromspinner = false;
        setRulePeriodOffsets(csr);
    }

    AdapterRulePeriodList(Context context, Cursor csr, int flags, Intent intent,
                          boolean fromspinner) {
        super(context, csr, 0);
        this.ctxt = context;
        this.callerintent = intent;
        this.fromspinner = fromspinner;
        setRulePeriodOffsets(csr);

    }

    @Override
    public View newView(Context context, Cursor csr, ViewGroup parent) {
        this.cursor = csr;
        return LayoutInflater.from(context).inflate(
                R.layout.ruleperiodlist,
                parent,
                false
        );
    }

    @Override
    public void bindView(View view, Context context, Cursor csr) {
        initView(view,csr);

    }

    @Override
    public View getDropDownView(int position, View convertview, ViewGroup parent) {
        super.getDropDownView(position, convertview, parent);
        View view = convertview;
        if (fromspinner) {
            int cpos = this.cursor.getPosition();
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

    private View initView(View view, Cursor csr) {
        TextView ruleperiod = (TextView) view.findViewById(R.id.ruleperiod);
        ruleperiod.setText(csr.getString(appvalues_text_offset));
        return view;
    }

    private void setRulePeriodOffsets(Cursor csr) {
        if (appvalues_name_offset != -1) {
            return;
        }
        appvalues_name_offset = csr.getColumnIndex(
                DBAppvaluesTableConstants.APPVALUES_NAME_COL
        );
        appvalues_text_offset = csr.getColumnIndex(
                DBAppvaluesTableConstants.APPVALUES_TEXT_COL
        );
    }
}