package mjt.shopwise;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;

/**
 * Prompted Rule ListView/Spinner Cursor Adapater
 */

@SuppressWarnings({"FieldCanBeLocal", "WeakerAccess"})
public class AdapterPromptedRuleList extends CursorAdapter {

    private final Intent callerintent;
    private final Context ctxt;
    private final boolean fromspinner;
    private Cursor cursor;

    private int rules_ruleid_offset = -1;
    @SuppressWarnings("unused")
    private int rules_ruleproductref_offset = 0;
    @SuppressWarnings("unused")
    private int rules_aisleid_offset = 0;
    private int rules_rulename_offset = 0;
    private int rules_ruleuses_offset = 0;
    @SuppressWarnings("unused")
    private int rules_ruleprompt_offset = 0;
    private int rules_ruleacton_offset = 0;
    private int rules_ruleperiod_offset = 0;
    private int rules_rulemultiplier_offset = 0;

    @SuppressWarnings("unused")
    private int aisles_aisle_shopref_offset = 0;
    private int aisles_aislename_offset = 0;
    @SuppressWarnings("unused")
    private int aisles_aisleorder_offset = 0;

    private int shops_shopname_offset = 0;
    private int shops_shopcity_offset = 0;
    @SuppressWarnings("unused")
    private int shops_shoporder_offset = 0;
    @SuppressWarnings("unused")
    private int shops_shopstreet_offset = 0;
    @SuppressWarnings("unused")
    private int shops_shopstate_offset = 0;
    @SuppressWarnings("unused")
    private int shops_shopnotes_offset = 0;

    private int products_productname_offset = 0;
    @SuppressWarnings("unused")
    private int products_productnotes_offset = 0;
    public static final String THISCLASS = AdapterPromptedRuleList.class.getSimpleName();
    private static final String LOGTAG = "SW_APRL(CsrAdptr)";

    @SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat sdf = new SimpleDateFormat(StandardAppConstants.EXTENDED_DATE_FORMAT);

    AdapterPromptedRuleList(Context context, Cursor csr, @SuppressWarnings({"SameParameterValue", "UnusedParameters"}) int flags, Intent intent) {
        super(context, csr, 0);
        String msg = "Constructing";
        String methodname = "Construct";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        this.ctxt = context;
        this.callerintent = intent;
        this.fromspinner = false;
        setRuleOffsets(csr);
    }

    AdapterPromptedRuleList(Context context, Cursor csr, @SuppressWarnings("UnusedParameters") int flags, Intent intent,
                            boolean fromspinner) {
        super(context, csr, 0);
        String msg = "Constructing";
        String methodname = "Construct";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        this.ctxt = context;
        this.callerintent = intent;
        this.fromspinner = fromspinner;
        setRuleOffsets(csr);

    }

    @Override
    public void bindView(View view, Context context, Cursor csr) {
        view = initView(view, csr);
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
        return LayoutInflater.from(context).inflate(
                R.layout.promptedrulelist,
                parent,
                false
        );
    }

    //TODO Not complete as yet
    @Override
    public View getDropDownView(int position, View convertview, ViewGroup parent) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        super.getDropDownView(position, convertview, parent);
        @SuppressWarnings("UnnecessaryLocalVariable") View view = convertview;
        //noinspection StatementWithEmptyBody
        if (fromspinner) {
            // Never invoked from a Spinner
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
        int evevrow = ActionColorCoding.setHeadingColor(ctxt,
                callerintent,
                ActionColorCoding.getColorsPerGroup() - 1) &
                ActionColorCoding.transparency_evenrow;
        int oddrow = evevrow & ActionColorCoding.transparency_oddrow;
        if (position % 2 == 0) {
            view.setBackgroundColor(evevrow);
        } else {
            view.setBackgroundColor(oddrow);
        }

        return  view;
    }

    private View initView(View view, Cursor csr) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);

        int position = csr.getPosition();

        TextView rulename = view.findViewById(R.id.promptedrulelist_rulename);
        TextView productname = view.findViewById(R.id.promptedrulelist_productname);
        TextView shopname = view.findViewById(R.id.promptedrulelist_shopname);
        TextView textrule = view.findViewById(R.id.promptedrulelist_textrule);
        TextView ruledate = view.findViewById(R.id.promptedruleslist_date);
        TextView addbutton = view.findViewById(R.id.promptedruleslist_addbutton);
        TextView skipbutton = view.findViewById(R.id.promptedruleslist_skipbutton);

        rulename.setText(csr.getString(rules_rulename_offset));
        productname.setText(csr.getString(products_productname_offset));
        shopname.setText(csr.getString(shops_shopname_offset));
        ruledate.setText(sdf.format(csr.getLong(rules_ruleacton_offset)));

        addbutton.setTag(position);
        skipbutton.setTag(position);

        String period_str = "";
        String period_str1 = "";
        switch (Integer.parseInt(csr.getString(rules_ruleperiod_offset))) {
            case DBRulesTableConstants.PERIOD_DAYSASINT:
                period_str = DBRulesTableConstants.PERIOD_DAYS;
                period_str1 = DBRulesTableConstants.PERIOD_DAYS_SINGULAR;
                break;
            case DBRulesTableConstants.PERIOD_WEEKSASINT:
                period_str = DBRulesTableConstants.PERIOD_WEEKS;
                period_str1 = DBRulesTableConstants.PERIOD_WEEKS_SINGULAR;
                break;
            case DBRulesTableConstants.PERIOD_FORTNIGHTSASINT:
                period_str = DBRulesTableConstants.PERIOD_FORTNIGHTS;
                period_str1 = DBRulesTableConstants.PERIOD_FORTNIGHTS_SINGULAR;
                break;
            case DBRulesTableConstants.PERIOD_MONTHSASINT:
                period_str = DBRulesTableConstants.PERIOD_MONTHS;
                period_str1 = DBRulesTableConstants.PERIOD_MONTHS_SINGULAR;
                break;
            case DBRulesTableConstants.PERIOD_QUARTERSASINT:
                period_str = DBRulesTableConstants.PERIOD_QUARTERS;
                period_str1 = DBRulesTableConstants.PERIOD_QUARTERS_SINGULAR;
                break;
            case DBRulesTableConstants.PERIOD_YEARSASINT:
                period_str = DBRulesTableConstants.PERIOD_YEARS;
                period_str1 = DBRulesTableConstants.PERIOD_YEARS_SINGULAR;
                break;
            default:
                break;
        }
        int multiplier = csr.getInt(rules_rulemultiplier_offset);
        String ruleastext = "";
        String startadj_plural = "Every ";
        String startadj_single = "Each ";

        int span01_start = 0;
        int span01_end = 0;

        if (multiplier > 1) {
            span01_start = startadj_plural.length() - 1;
            span01_end = Integer.toString(multiplier).length() + 1 + period_str.length() + 1;
            ruleastext = ruleastext + startadj_plural +
                    Integer.toString(multiplier) +
                    " " + period_str;
        } else {
            span01_start = startadj_single.length() -1;
            span01_end = period_str1.length() + 1;
            ruleastext = ruleastext + startadj_single + period_str1;
        }
        ruleastext = ruleastext + " add ";
        String numbertoget = csr.getString(rules_ruleuses_offset);
        int span02_start = ruleastext.length() -1;
        int span02_end = numbertoget.length() + 1;
        ruleastext = ruleastext + numbertoget;

        ruleastext = ruleastext + " from ";

        int span03_start = ruleastext.length();
        int span03_end = span03_start + csr.getString(shops_shopname_offset). length();
        ruleastext = ruleastext + csr.getString(shops_shopname_offset) + " (";

        int span04_start = ruleastext.length();
        int span04_end = span04_start + csr.getString(shops_shopcity_offset).length();
        ruleastext = ruleastext + csr.getString(shops_shopcity_offset) + ") in Aisle ";

        int span05_start = ruleastext.length();
        int span05_end = span05_start + csr.getString(aisles_aislename_offset).length();
        ruleastext = ruleastext + csr.getString(aisles_aislename_offset);

        //wts = new SpannableString(shopdata);
        Spannable wts = new SpannableString(ruleastext);
        wts.setSpan(
                new ForegroundColorSpan(ActionColorCoding.setHeadingColor(ctxt,callerintent,0)),
                span01_start,span01_start + span01_end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        wts.setSpan(
                new ForegroundColorSpan(ActionColorCoding.setHeadingColor(ctxt,callerintent,0)),
                span02_start,span02_start + span02_end,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        wts.setSpan(
                new ForegroundColorSpan(ActionColorCoding.setHeadingColor(
                        ctxt,callerintent,0)),span03_start,span03_end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        wts.setSpan(
                new ForegroundColorSpan(ActionColorCoding.setHeadingColor(
                        ctxt,callerintent,2)),span04_start,span04_end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        wts.setSpan(
                new ForegroundColorSpan(ActionColorCoding.setHeadingColor(
                        ctxt,callerintent,0)),span05_start,span05_end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        textrule.setText(wts);
        msg = "SET RuleName=" + rulename.getText().toString() +
                " Product=" + productname.getText().toString() +
                " Shop=" + shopname.getText().toString() +
                "\n\tDate=" + ruledate.getText().toString() +
                "\n\tRule=" + ruleastext;
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);

        return view;
    }

    private void setRuleOffsets(Cursor csr) {
        if (rules_ruleid_offset != -1 ) {
            return;
        }
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        rules_ruleid_offset = csr.getColumnIndex(
                DBRulesTableConstants.RULES_ID_COL
        );
        rules_ruleproductref_offset = csr.getColumnIndex(
                DBRulesTableConstants.RULES_PRODUCTREF_COL
        );
        rules_aisleid_offset = csr.getColumnIndex(
                DBRulesTableConstants.RULES_AISLEREF_COL
        );
        rules_rulename_offset = csr.getColumnIndex(
                DBRulesTableConstants.RULES_NAME_COL
        );
        rules_ruleuses_offset = csr.getColumnIndex(
                DBRulesTableConstants.RULES_USES_COL
        );
        rules_ruleprompt_offset = csr.getColumnIndex(
                DBRulesTableConstants.RULES_PROMPT_COL
        );
        rules_ruleacton_offset = csr.getColumnIndex(
                DBRulesTableConstants.RULES_ACTON_COL
        );
        rules_ruleperiod_offset = csr.getColumnIndex(
                DBRulesTableConstants.RULES_PERIOD_COL
        );
        rules_rulemultiplier_offset = csr.getColumnIndex(
                DBRulesTableConstants.RULES_MULTIPLIER_COL
        );
        aisles_aisle_shopref_offset = csr.getColumnIndex(
                DBAislesTableConstants.AISLES_SHOPREF_COL
        );
        aisles_aislename_offset = csr.getColumnIndex(
                DBAislesTableConstants.AISLES_NAME_COL
        );
        aisles_aisleorder_offset = csr.getColumnIndex(
                DBAislesTableConstants.AISLES_ORDER_COL
        );
        shops_shopname_offset = csr.getColumnIndex(
                DBShopsTableConstants.SHOPS_NAME_COL
        );
        shops_shoporder_offset = csr.getColumnIndex(
                DBShopsTableConstants.SHOPS_ORDER_COL
        );
        shops_shopcity_offset = csr.getColumnIndex(
                DBShopsTableConstants.SHOPS_CITY_COL
        );
        shops_shopstreet_offset = csr.getColumnIndex(
                DBShopsTableConstants.SHOPS_STREET_COL
        );
        shops_shopstate_offset = csr.getColumnIndex(
                DBShopsTableConstants.SHOPS_STATE_COL
        );
        shops_shopnotes_offset = csr.getColumnIndex(
                DBShopsTableConstants.SHOPS_STATE_COL
        );
        products_productname_offset = csr.getColumnIndex(
                DBProductsTableConstants.PRODUCTS_NAME_COL
        );
    }
}
