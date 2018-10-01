package mjt.shopwise;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

/**
 * Listview adpater for RuleTools (Rule Suggestion and Rule Accuracy)
 */

@SuppressWarnings({"FieldCanBeLocal", "WeakerAccess"})
public class AdapterRuleToolList extends CursorAdapter{

    private final Intent callerintent;
    private final Context ctxt;
    @SuppressWarnings("unused")
    private final boolean fromspinner;
    @SuppressWarnings("unused")
    private Cursor cursor;
    private boolean suggestmode = false;
    private boolean acccheckmode = false;
    private boolean disabledmode = false;
    @SuppressWarnings("unused")
    private Resources res;

    private int productusage_productref_offset = -1;
    @SuppressWarnings("unused")
    private int productusage_aisleref_offset = 0;
    @SuppressWarnings("unused")
    private int productusage_cost_offset = 0;
    private int productusage_buycount_offset = 0;
    @SuppressWarnings("unused")
    private int productusage_firstbuydate_offset = 0;
    @SuppressWarnings("unused")
    private int productusage_latestbuydate_offset = 0;
    @SuppressWarnings("unused")
    private int productusage_order_offset = 0;
    @SuppressWarnings("unused")
    private int productusage_rulesuggestflag_offset = 0;
    @SuppressWarnings("unused")
    private int productusage_checklistflag_offset = 0;
    @SuppressWarnings("unused")
    private int productusage_checklistcount_offset = 0;

    private int calculated_ruleperiod_offset = 0;

    private int product_name_offset = 0;

    private int aisle_name_offset = 0;
    @SuppressWarnings("unused")
    private int aisle_order_offset = 0;
    @SuppressWarnings("unused")
    private int aisle_shopref_offset = 0;

    private int shop_name_offset = 0;
    @SuppressWarnings("unused")
    private int shop_city_offset = 0;
    @SuppressWarnings("unused")
    private int shop_order_offset = 0;

    @SuppressWarnings("unused")
    private int rule_id_offset = 0;
    @SuppressWarnings("unused")
    private int rule_aisleref_offset = 0;
    @SuppressWarnings("unused")
    private int rule_productref_offset = 0;
    private int rule_name_offset = 0;
    @SuppressWarnings("unused")
    private int rule_uses_offset = 0;
    @SuppressWarnings("unused")
    private int rule_prompt_offset = 0;
    @SuppressWarnings("unused")
    private int rule_acton_offset = 0;
    @SuppressWarnings("unused")
    private int rule_period_offset = 0;
    @SuppressWarnings("unused")
    private int rule_multiplier_offset = 0;

    public static final DecimalFormat df = new DecimalFormat("#.###");
    public static final String THISCLASS = AdapterRuleToolList.class.getSimpleName();
    public static final String LOGTAG = "SW_ARTL(CsrAdptr)";

    @SuppressWarnings("unused")
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat sdf =
            new SimpleDateFormat(StandardAppConstants.EXTENDED_DATE_FORMAT);

    AdapterRuleToolList(Context context,
                        Cursor csr,
                        @SuppressWarnings({"SameParameterValue", "UnusedParameters"}) int flags,
                        Intent intent,
                        @SuppressWarnings("SameParameterValue") boolean fromspinner,
                        int mode) {
        super(context, csr, 0);
        String logmsg = "Constructing";
        String methodname = "Construct";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        this.ctxt = context;
        this.callerintent = intent;
        this.fromspinner = fromspinner;
        switch (mode) {
            case StandardAppConstants.CM_RULESUGGEST:
                suggestmode = true;
                break;
            case StandardAppConstants.CM_RULEACCURACY:
                acccheckmode = true;
                break;
            case StandardAppConstants.CM_RULEDISABLED:
                disabledmode = true;
                break;
        }
        setRuleToolOffsets(csr);
    }

    public View newView(Context context, Cursor csr, ViewGroup parent) {
        String logmssg = "Inflating Layout";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmssg,THISCLASS,methodname);
        this.cursor = csr;
        return LayoutInflater.from(context).inflate(
                R.layout.ruletoollist,
                parent,
                false
        );
    }
    public View getView(int position, View convertview, ViewGroup parent) {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        View view = super.getView(position, convertview, parent);
        int evenrow = ActionColorCoding.setHeadingColor(ctxt, callerintent,
                ActionColorCoding.getColorsPerGroup() -1 ) &
                ActionColorCoding.transparency_evenrow;
        int oddrow = evenrow & ActionColorCoding.transparency_oddrow;
        if (position % 2 == 0) {
            view.setBackgroundColor(evenrow);
        } else {
            view.setBackgroundColor(oddrow);
        }
        LinearLayout accuracybar =
                view.findViewById(R.id.ruletoollist_accuracybar);
        TextView addbutton =
                view.findViewById(R.id.ruletoollist_addbutton);
        TextView skipbutton =
                view.findViewById(R.id.ruletoollist_skipbutton);
        TextView disablebutton =
                view.findViewById(R.id.ruletoollist_disablebutton);
        if (acccheckmode) {
            accuracybar.setVisibility(View.VISIBLE);
            addbutton.setVisibility(View.INVISIBLE);
            skipbutton.setVisibility(View.INVISIBLE);
            disablebutton.setText(ctxt.getResources().getString(R.string.modifybutton));
        } else {
            accuracybar.setVisibility(View.GONE);
            addbutton.setVisibility(View.VISIBLE);
            skipbutton.setVisibility(View.VISIBLE);
            disablebutton.setText(ctxt.getResources().getString(R.string.disablebutton));
        }
        if (disabledmode) {
            addbutton.setVisibility(View.INVISIBLE);
            skipbutton.setVisibility(View.INVISIBLE);
            disablebutton.setText(ctxt.getResources().getString(
                    R.string.enablebutton
            ));
        }
        return view;
    }

    public void bindView(View view, Context ccontext, Cursor csr) {
        initView(view, csr);
    }

    @SuppressWarnings("UnusedReturnValue")
    private View initView(View view, Cursor csr) {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);

        TextView rulename = view.findViewById(R.id.ruletoollist_rulename);
        TextView productname = view.findViewById(R.id.ruletoollist_productname);
        TextView shopname = view.findViewById(R.id.ruletoollist_shopname);
        TextView aislename = view.findViewById(R.id.ruletoollist_aislename);
        TextView ruletext = view.findViewById(R.id.ruletoollist_ruleastext);
        TextView currentrule = view.findViewById(R.id.ruletoollist_currentrule);
        TextView addbutton = view.findViewById(R.id.ruletoollist_addbutton);
        TextView skipbutton = view.findViewById(R.id.ruletoollist_skipbutton);
        TextView disablebutton = view.findViewById(R.id.ruletoollist_disablebutton);
        ProgressBar accuracylow = view.findViewById(R.id.ruletoollist_accuracylow);
        ProgressBar accuracyhigh = view.findViewById(R.id.ruletoollist_accuracyhigh);

        addbutton.setTag(csr.getPosition());
        skipbutton.setTag(csr.getPosition());
        disablebutton.setTag(csr.getPosition());
        double buycount;
        double overdays;
        int ruleperiodasint;
        double ruleperiod;
        double rulemultiplier;
        int rulemultiplierasint;
        double rulecount;
        double rulequantityperday;
        double realquantityperday;
        double ruleaccuracy;

        buycount = csr.getDouble(productusage_buycount_offset);
        overdays = csr.getDouble(calculated_ruleperiod_offset);
        realquantityperday = overdays / buycount;

        if (suggestmode || disabledmode) {
            rulename.setText(ctxt.getResources().getString(
                    R.string.rulenametext,
                    csr.getString(product_name_offset)));
            currentrule.setText("");
        } else {
            rulename.setText(csr.getString(rule_name_offset));
            ruleperiod = RulePeriodAsDays(csr.getInt(csr.getColumnIndex(
                    DBRulesTableConstants.RULES_PERIOD_COL
            )));
            ruleperiodasint = csr.getInt(csr.getColumnIndex(
                    DBRulesTableConstants.RULES_PERIOD_COL
            ));
            rulemultiplier = csr.getDouble(csr.getColumnIndex(
                    DBRulesTableConstants.RULES_MULTIPLIER_COL
            ));
            rulemultiplierasint = csr.getInt(csr.getColumnIndex(
                    DBRulesTableConstants.RULES_MULTIPLIER_COL
            ));
            rulecount = csr.getDouble(csr.getColumnIndex(
                    DBRulesTableConstants.RULES_USES_COL
            ));
            rulequantityperday = (ruleperiod * rulemultiplier) / rulecount;
            ruleaccuracy = ((realquantityperday / rulequantityperday) * 100);

            currentrule.setText(ctxt.getResources().getString(
                    R.string.rulestats,
                    csr.getInt(csr.getColumnIndex(
                            DBRulesTableConstants.RULES_USES_COL)),
                    RulePeriodAsString(ruleperiodasint,rulemultiplierasint),
                    df.format(rulequantityperday),
                    df.format(ruleaccuracy)));
            accuracylow.setProgress(100);
            accuracyhigh.setProgress(0);
            if (ruleaccuracy < 100) {
                accuracylow.setProgress((int) ruleaccuracy);
                accuracyhigh.setProgress(0);
            }
            if (ruleaccuracy > 100) {
                accuracyhigh.setProgress((int)((ruleaccuracy - 100)));
                accuracylow.setProgress(100);
            }
        }

        productname.setText(csr.getString(product_name_offset));
        shopname.setText(csr.getString(shop_name_offset));
        aislename.setText(ctxt.getResources().getString(
                R.string.aislenametext,
                csr.getString(aisle_name_offset)));

        double quantitytoget = 1;
        double periodfor1 = realquantityperday;
        if (periodfor1 < 1) {
            periodfor1 = periodfor1 * ((double)1 / periodfor1);
            quantitytoget = ((double)1 / realquantityperday);
        }
        Double cnvpf1 = periodfor1;
        Double cnvqtytoget = quantitytoget + 0.5;
        ruletext.setText(ctxt.getResources().getString(
                R.string.ruleastext,
                cnvqtytoget.intValue(),
                cnvpf1.intValue(),
                csr.getInt(productusage_buycount_offset),
                csr.getInt(calculated_ruleperiod_offset),
                df.format(realquantityperday)
        ));
        return view;
    }


    private void setRuleToolOffsets(Cursor csr) {
        if (productusage_productref_offset != -1) {
            return;
        }
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                LOGTAG,logmsg,THISCLASS,methodname);
        productusage_productref_offset = csr.getColumnIndex(
                DBProductusageTableConstants.PRODUCTUSAGE_PRODUCTREF_COL
        );
        productusage_aisleref_offset = csr.getColumnIndex(
                DBProductusageTableConstants.PRODUCTUSAGE_AISLEREF_COL
        );
        productusage_cost_offset = csr.getColumnIndex(
                DBProductusageTableConstants.PRODUCTUSAGE_COST_COL
        );
        productusage_buycount_offset = csr.getColumnIndex(
                DBProductusageTableConstants.PRODUCTUSAGE_BUYCOUNT_COL
        );
        productusage_firstbuydate_offset = csr.getColumnIndex(
                DBProductusageTableConstants.PRODUCTUSAGE_FIRSTBUYDATE_COL
        );
        productusage_latestbuydate_offset = csr.getColumnIndex(
                DBProductusageTableConstants.PRODUCTUSAGE_LATESTBUYDATE_COL
        );
        productusage_order_offset = csr.getColumnIndex(
                DBProductusageTableConstants.PRODUCTUSAGE_ORDER_COL
        );
        productusage_rulesuggestflag_offset = csr.getColumnIndex(
                DBProductusageTableConstants.PRODUCTUSAGE_RULESUGGESTFLAG_COL
        );
        productusage_checklistflag_offset = csr.getColumnIndex(
                DBProductusageTableConstants.PRODUCTUSAGE_CHECKLISTFLAG_COL
        );
        productusage_checklistcount_offset = csr.getColumnIndex(
                DBProductusageTableConstants.PRODUCTUSAGE_CHECKLISTCOUNT_COL
        );
        calculated_ruleperiod_offset = csr.getColumnIndex(
                DBConstants.CALCULATED_RULEPERIODINDAYS
        );
        product_name_offset = csr.getColumnIndex(
                DBProductsTableConstants.PRODUCTS_NAME_COL
        );
        aisle_name_offset = csr.getColumnIndex(
                DBAislesTableConstants.AISLES_NAME_COL
        );
        aisle_order_offset = csr.getColumnIndex(
                DBAislesTableConstants.AISLES_ORDER_COL
        );
        aisle_shopref_offset = csr.getColumnIndex(
                DBAislesTableConstants.AISLES_SHOPREF_COL
        );
        shop_name_offset = csr.getColumnIndex(
                DBShopsTableConstants.SHOPS_NAME_COL
        );
        shop_city_offset = csr.getColumnIndex(
                DBShopsTableConstants.SHOPS_CITY_COL
        );
        shop_order_offset = csr.getColumnIndex(
                DBShopsTableConstants.SHOPS_ORDER_COL
        );
        rule_id_offset = csr.getColumnIndex(
                DBRulesTableConstants.RULES_ALTID_COL
        );
        rule_aisleref_offset = csr.getColumnIndex(
                DBRulesTableConstants.RULES_AISLEREF_COL
        );
        rule_productref_offset = csr.getColumnIndex(
                DBRulesTableConstants.RULES_PRODUCTREF_COL
        );
        rule_name_offset = csr.getColumnIndex(
                DBRulesTableConstants.RULES_NAME_COL
        );
        rule_uses_offset = csr.getColumnIndex(
                DBRulesTableConstants.RULES_USES_COL
        );
        rule_prompt_offset = csr.getColumnIndex(
                DBRulesTableConstants.RULES_PROMPT_COL
        );
        rule_acton_offset = csr.getColumnIndex(
                DBRulesTableConstants.RULES_ACTON_COL
        );
        rule_period_offset = csr.getColumnIndex(
                DBRulesTableConstants.RULES_PERIOD_COL
        );
        rule_multiplier_offset = csr.getColumnIndex(
                DBRulesTableConstants.RULES_MULTIPLIER_COL
        );
    }

    /**
     * Convert the ruleperiod into the number of days
     * @param   rp  The rule period to be converted
     * @return      The number of days the period eaustes to
     */
    private double RulePeriodAsDays(int rp) {
        int rv;
        switch (rp) {
            case DBRulesTableConstants.PERIOD_DAYSASINT:
                rv = 1;
                break;
            case DBRulesTableConstants.PERIOD_WEEKSASINT:
                rv =  7;
                break;
            case DBRulesTableConstants.PERIOD_FORTNIGHTSASINT:
                rv =  14;
                break;
            case DBRulesTableConstants.PERIOD_MONTHSASINT:
                rv =  30;
                break;
            case DBRulesTableConstants.PERIOD_QUARTERSASINT:
                rv =  90;
                break;
            case DBRulesTableConstants.PERIOD_YEARSASINT:
                rv =  365;
                break;
            default:
                rv = 1;
        }
        return rv;
    }

    private String RulePeriodAsString(int rp, int multiplier) {
        String rv;
        switch (rp) {
            case DBRulesTableConstants.PERIOD_DAYSASINT:
                if (multiplier > 1) {
                    rv = DBRulesTableConstants.PERIOD_DAYS;
                } else {
                    rv = DBRulesTableConstants.PERIOD_DAYS_SINGULAR;
                }
                break;
            case DBRulesTableConstants.PERIOD_WEEKSASINT:
                if (multiplier > 1) {
                    rv = DBRulesTableConstants.PERIOD_WEEKS;
                } else {
                    rv = DBRulesTableConstants.PERIOD_WEEKS_SINGULAR;
                }
                break;
            case DBRulesTableConstants.PERIOD_FORTNIGHTSASINT:
                if (multiplier > 1) {
                    rv = DBRulesTableConstants.PERIOD_FORTNIGHTS;
                } else {
                    rv = DBRulesTableConstants.PERIOD_FORTNIGHTS_SINGULAR;
                }
                break;
            case DBRulesTableConstants.PERIOD_MONTHSASINT:
                if (multiplier > 1) {
                    rv = DBRulesTableConstants.PERIOD_MONTHS;
                } else {
                    rv = DBRulesTableConstants.PERIOD_MONTHS_SINGULAR;
                }
                break;
            case DBRulesTableConstants.PERIOD_QUARTERSASINT:
                if (multiplier > 1) {
                    rv = DBRulesTableConstants.PERIOD_QUARTERS;
                } else {
                    rv = DBRulesTableConstants.PERIOD_QUARTERS_SINGULAR;
                }
                break;
            case DBRulesTableConstants.PERIOD_YEARSASINT:
                if (multiplier > 1) {
                    rv = DBRulesTableConstants.PERIOD_YEARS;
                } else {
                    rv = DBRulesTableConstants.PERIOD_YEARS_SINGULAR;
                }
                break;
            default:
                rv = "";

        }
        if (multiplier > 1) {
            rv = Integer.toString(multiplier) + " " + rv;
        }
        return rv;
    }

}
