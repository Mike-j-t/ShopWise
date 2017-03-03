package mjt.shopwise;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * RuleSuggestCheckActivity - List either potential rules for suggestion
 *                              or existing rules with an indicator of their
 *                              accuracy.
 *                              Allowing rules to be added (suggestion) or
 *                              modified (accuracy check)
 */

@SuppressWarnings({"FieldCanBeLocal", "WeakerAccess"})
public class RuleSuggestCheckActivity extends AppCompatActivity {

    private static final String THIS_ACTIVITY = "RuleSuggestCheckActivity";
    private static String caller;
    private static int calledmode;

    private static final int BYRULE = 0;
    private static final int BYPRODUCT = 1;
    private static final int BYSHOP = 2;
    private static final String SORTASCENDING = DBConstants.SQLORDERASCENDING;
    private static final String SORTDESCENDING = DBConstants.SQLORDERDESCENDING;
    static String orderby = DBProductsTableConstants.PRODUCTS_NAME_COL_FULL;
    static int orderfld = BYPRODUCT;
    static boolean ordertype = true;
    static boolean sortchanged = false;
    static String lastmessage = "";
    static String filter = "";

    Context context;
    ActionBar actionbar;

    private static int h1;
    private static int h2;
    private static int h3;
    private static int h4;
    private static int primary_color;
    private String menucolorcode;
    int passedmenucolorcode;
    boolean rulesexist = false;
    private boolean suggestmode = false;
    private boolean disabledmode = false;
    private boolean acccheckmode = false;
    private int minbuy;
    private int minprd;

    DBDAO dbdao;
    DBShopMethods dbshopmethods;
    DBAisleMethods dbaislemethods;
    DBProductMethods dbproductmethods;
    DBProductUsageMethods dbpumethods;
    DBShopListMethods dbshoplistmethods;
    DBRuleMethods dbrulemethods;

    Cursor rlcsr;

    TextView messagebar;
    TextView donebutton;
    ListView rulelist;
    LinearLayout rulelistheading;
    AdapterRuleToolList ruletoollistadapter;
    TextView addbutton;
    TextView skipbutton;
    TextView disablebutton;

    private int resumestate = StandardAppConstants.RESUMSTATE_NORMAL;
    private Activity thisactivity;
    public static final String THISCLASS =
            RuleSuggestCheckActivity.class.getSimpleName();
    private static final String LOGTAG = "SW_RSA";

    protected void onCreate(Bundle savedInstanceState) {
        String logmsg = "Invoked";
        String methodname =
                new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                LOGTAG,logmsg,THISCLASS,methodname);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rulesuggest);
        context = this;
        thisactivity = (Activity)context;
        actionbar = getSupportActionBar();
        logmsg = "Retrieving IntentExtras";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                LOGTAG,logmsg,THISCLASS,methodname);
        caller = getIntent().getStringExtra(
                StandardAppConstants.INTENTKEY_CALLINGACTIVITY);
        calledmode = getIntent().getIntExtra(
                StandardAppConstants.INTENTKEY_CALLINGMODE,0);
        minbuy = getIntent().getIntExtra(
                StandardAppConstants.INTENTKEY_RULETOOLMINBUY,5);
        minprd = getIntent().getIntExtra(
                StandardAppConstants.INTENTKEY_RULETOOLMINPERIOD,30);
        switch (calledmode) {
            case StandardAppConstants.CM_RULEACCURACY:
                rulesexist = true;
                suggestmode = false;
                acccheckmode = true;
                disabledmode = false;
                actionbar.setTitle(this.getResources().getString(
                        R.string.rulechecklist
                ));
                filter = "";
                break;
            case StandardAppConstants.CM_RULESUGGEST:
                rulesexist = false;
                suggestmode = true;
                acccheckmode = false;
                disabledmode = false;
                filter = DBProductusageTableConstants.PRODUCTUSAGE_RULESUGGESTFLAG_COL_FULL +
                        " = " +
                        DBProductusageTableConstants.RULESUGGESTFLAG_CLEAR;
                actionbar.setTitle(this.getResources().getString(
                        R.string.rulesuggestlist
                ));
                break;
            case StandardAppConstants.CM_RULEDISABLED:
                suggestmode = false;
                acccheckmode = false;
                disabledmode = true;
                filter = "";
                actionbar.setTitle(this.getResources().getString(
                        R.string.ruledisabledlist
                ));
        }

        logmsg = "Preparing ColorCoding";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                LOGTAG,logmsg,THISCLASS,methodname);
        menucolorcode = StandardAppConstants.INTENTKEY_MENUCOLORCODE;
        passedmenucolorcode = getIntent().getIntExtra(menucolorcode,0);

        messagebar = (TextView) findViewById(R.id.rulesuggest_messagebar);
        donebutton = (TextView) findViewById(R.id.rulesuggest_donebutton);
        rulelistheading = (LinearLayout) findViewById(R.id.rulesuggest_heading);
        rulelist = (ListView) findViewById(R.id.rulesuggest_rulelist);
        addbutton = (TextView) findViewById(R.id.ruletoollist_addbutton);
        skipbutton = (TextView) findViewById(R.id.ruletoollist_skipbutton);
        disablebutton = (TextView) findViewById(R.id.ruletoollist_disablebutton);

        /**
         * Apply Color Coding
         */

        ActionColorCoding.setActionBarColor(this,getIntent(),actionbar);
        primary_color = ActionColorCoding.setHeadingColor(this,getIntent(),0);
        h1 = ActionColorCoding.setHeadingColor(this,getIntent(),1);
        h2 = ActionColorCoding.setHeadingColor(this,getIntent(),2);
        h3 = ActionColorCoding.setHeadingColor(this,getIntent(),3);
        h4 = ActionColorCoding.setHeadingColor(this,getIntent(),4);
        ActionColorCoding.setActionButtonColor(donebutton,primary_color);
        rulelistheading.setBackgroundColor(h1);

        ActionColorCoding.setSwatches(findViewById(android.R.id.content), this.getIntent());

        logmsg = "Prepapring Databases";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,logmsg,THISCLASS,methodname);

        dbdao = new DBDAO(this);
        dbshopmethods = new DBShopMethods(this);
        dbaislemethods = new DBAisleMethods(this);
        dbproductmethods = new DBProductMethods(this);
        dbpumethods = new DBProductUsageMethods(this);
        dbshoplistmethods = new DBShopListMethods(this);
        dbrulemethods = new DBRuleMethods(this);

        logmsg = "Preparing Rulelist";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,logmsg,THISCLASS,methodname);
        if (suggestmode) {
            rlcsr = dbrulemethods.getToolRules(rulesexist,minprd,minbuy,filter,orderby);
        }
        if (acccheckmode) {
            rlcsr = dbrulemethods.getToolRules(rulesexist,minprd,minbuy,filter,orderby);
        }
        if (disabledmode) {
            rlcsr = dbrulemethods.getDisabledRules(orderby);
        }

        ruletoollistadapter = new AdapterRuleToolList(this, rlcsr,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER,
                this.getIntent(),false,calledmode);
        rulelist.setAdapter(ruletoollistadapter);
    }

    /**************************************************************************
     * onResume do any processing upon resume of the activity
     * e.g. refresh any listviews etc.
     * RESUMESTATES would be set when starting another activity if that
     * activity could alter the contents to be displayed.
     * Should always set the resumestate to RESUMESTATE_NORMAL
     */
    @Override
    protected void onResume() {
        String msg = "Invoked";
        String methodname =
                new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                LOGTAG,msg,THISCLASS,methodname);
        super.onResume();
        switch (resumestate) {
            case StandardAppConstants.RESUMESTATE_ALT1:
                break;
            case StandardAppConstants.RESUMESTATE_ALT2:
                break;
            case StandardAppConstants.RESUMESTATE_ALT3:
                break;
            case StandardAppConstants.RESUMESTATE_ALT4:
                break;
            default:
                messagebar.setVisibility(View.INVISIBLE);
                break;
        }
        if (suggestmode) {
            rlcsr = dbrulemethods.getToolRules(rulesexist,minprd,minbuy,filter,orderby);
        }
        if (acccheckmode) {
            rlcsr = dbrulemethods.getToolRules(rulesexist,minprd,minbuy,filter,orderby);
        }
        if (disabledmode) {
            rlcsr = dbrulemethods.getDisabledRules(orderby);
        }
        ruletoollistadapter.swapCursor(rlcsr);
    }

    /**************************************************************************
     * onDestroy - do any clean up before th application is destroyed
     * e.g. close any open cursors and then close the database
     */
    @Override
    protected void onDestroy() {
        String msg = "Invoked";
        String methodname =
                new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                LOGTAG,msg,THISCLASS,methodname);
        super.onDestroy();
        rlcsr.close();
        dbpumethods.enableSkipped();
    }

    /**************************************************************************
     * Button Click Handler
     *
     * @param view The view (i.e the TextView that was clicked)
     */
    public void actionButtonClick(View view) {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);

        Intent intent = null;
        int viewtag;
        switch (view.getId()) {
            case R.id.rulesuggest_donebutton:
                logmsg = "Finishing";
                LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
                this.finish();
                break;
            case R.id.ruletoollist_addbutton:
                logmsg = "Adding Suggested Rule";
                LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
                rlcsr.moveToPosition((int) view.getTag());
                intent = new Intent(this,RulesAddEditActivity.class);
                buildAddRuleIntent(intent);
                break;
            case R.id.ruletoollist_skipbutton:
                logmsg = "Skipping Rule Suggestion";
                LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
                setRuleSkipperOrDisabled((int) view.getTag(),
                        DBProductusageTableConstants.RULESUGGESTFLAG_SKIP);
                break;
            case R.id.ruletoollist_disablebutton:
                TextView disablebutton = (TextView) findViewById(view.getId());
                if (disablebutton.getText().equals(this.getResources().getString(R.string.disablebutton))) {
                    logmsg = "Disabling Rule";
                    LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
                    setRuleSkipperOrDisabled((int) view.getTag(),
                            DBProductusageTableConstants.RULESUGGESTFLAG_DISABLE);
                }
                if (disablebutton.getText().equals(
                        getResources().getString(
                                R.string.modifybutton))) {
                    logmsg = "Modifying Rule from Accuracy Check";
                    LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
                    rlcsr.moveToPosition((int) view.getTag());
                    intent = new Intent(this,RulesAddEditActivity.class);
                    buildModifyRuleIntent(intent);
                }
                if (disablebutton.getText().equals(
                        getResources().getString(R.string.enablebutton)
                )) {
                    logmsg = "Enabling Disabled Rule";
                    LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
                    setRuleSkipperOrDisabled((int) view.getTag(),
                            DBProductusageTableConstants.RULESUGGESTFLAG_CLEAR
                            );
                }
                break;
            default:
                break;
        }
        if (intent != null) {
            intent.putExtra(StandardAppConstants.INTENTKEY_CALLINGACTIVITY,THIS_ACTIVITY);
            //intent.putExtra(StandardAppConstants.INTENTKEY_CALLINGMODE,StandardAppConstants.CM_CLEAR);
            intent.putExtra(menucolorcode,passedmenucolorcode);
            logmsg = "Starting Activty " + intent.getComponent().getShortClassName();
            LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
            startActivity(intent);
        }
    }
    private void buildModifyRuleIntent(Intent intent) {
        intent.putExtra(StandardAppConstants.INTENTKEY_CALLINGMODE,
                StandardAppConstants.CM_EDIT);
        intent.putExtra(StandardAppConstants.INTENTKEY_RULEID,
                rlcsr.getLong(rlcsr.getColumnIndex(
                        DBRulesTableConstants.RULES_ALTID_COL
                )));
        intent.putExtra(StandardAppConstants.INTENTKEY_RULEAISLEID,
                rlcsr.getLong(rlcsr.getColumnIndex(
                        DBRulesTableConstants.RULES_AISLEREF_COL
                )));
        intent.putExtra(StandardAppConstants.INTENTKEY_RULEPRODUCTREF,
                rlcsr.getLong(rlcsr.getColumnIndex(
                        DBRulesTableConstants.RULES_PRODUCTREF_COL
                )));
        intent.putExtra(StandardAppConstants.INTENTKEY_RULENAME,
                rlcsr.getString(rlcsr.getColumnIndex(
                        DBRulesTableConstants.RULES_NAME_COL
                )));
        double overdays = rlcsr.getDouble(rlcsr.getColumnIndex(
                DBConstants.CALCULATED_RULEPERIODINDAYS
        ));
        double buycount = rlcsr.getDouble(rlcsr.getColumnIndex(
                DBProductusageTableConstants.PRODUCTUSAGE_BUYCOUNT_COL
        ));
        intent.putExtra(StandardAppConstants.INTENTKEY_RULEUSES,1);
        intent.putExtra(StandardAppConstants.INTENTKEY_RULEMULTIPLIER,
                (int) (overdays / buycount));
        intent.putExtra(StandardAppConstants.INTENTKEY_RULEPROMPT,
                rlcsr.getInt(rlcsr.getColumnIndex(
                        DBRulesTableConstants.RULES_PROMPT_COL
                )));
        intent.putExtra(StandardAppConstants.INTENTKEY_RULEACTON,
                rlcsr.getLong(rlcsr.getColumnIndex(
                        DBRulesTableConstants.RULES_ACTON_COL
                )));
        intent.putExtra(StandardAppConstants.INTENTKEY_RULEPERIOD,
                DBRulesTableConstants.PERIOD_DAYSASINT);
    }

    private void buildAddRuleIntent(Intent intent) {
        intent.putExtra(StandardAppConstants.INTENTKEY_CALLINGMODE,
                StandardAppConstants.CM_ADD);
        intent.putExtra(StandardAppConstants.INTENTKEY_PRODUCTNAME,
                rlcsr.getString(rlcsr.getColumnIndex(
                        DBProductsTableConstants.PRODUCTS_NAME_COL
                )));
        intent.putExtra(StandardAppConstants.INTENTKEY_PRODUCTID,
                rlcsr.getLong(rlcsr.getColumnIndex(
                        DBProductusageTableConstants.PRODUCTUSAGE_PRODUCTREF_COL
                )));
        intent.putExtra(StandardAppConstants.INTENTKEY_AISLENAME,
                rlcsr.getString(rlcsr.getColumnIndex(
                        DBAislesTableConstants.AISLES_NAME_COL
                )));
        intent.putExtra(StandardAppConstants.INTENTKEY_AISLEID,
                rlcsr.getLong(rlcsr.getColumnIndex(
                        DBProductusageTableConstants.PRODUCTUSAGE_AISLEREF_COL
                )));
        intent.putExtra(StandardAppConstants.INTENTKEY_SHOPNAME,
                rlcsr.getString(rlcsr.getColumnIndex(
                        DBShopsTableConstants.SHOPS_NAME_COL
                )));
        intent.putExtra(StandardAppConstants.INTENTKEY_SHOPID,
                rlcsr.getLong(rlcsr.getColumnIndex(
                        DBAislesTableConstants.AISLES_SHOPREF_COL
                )));
        intent.putExtra(StandardAppConstants.INTENTKEY_RULENAME,
                "#" + rlcsr.getString(rlcsr.getColumnIndex(
                        DBProductsTableConstants.PRODUCTS_NAME_COL
                )));
        intent.putExtra(StandardAppConstants.INTENTKEY_RULEUSES,1);
        intent.putExtra(StandardAppConstants.INTENTKEY_RULEPERIOD,
                DBRulesTableConstants.PERIOD_DAYSASINT);
        intent.putExtra(StandardAppConstants.INTENTKEY_RULEMULTIPLIER,
                rlcsr.getInt(rlcsr.getColumnIndex(
                        DBConstants.CALCULATED_RULEPERIODINDAYS)) /
                        rlcsr.getInt(rlcsr.getColumnIndex(
                                DBProductusageTableConstants.PRODUCTUSAGE_BUYCOUNT_COL
                        ))
        );
    }


    /**************************************************************************
     * setRuleSkippedOrDisabled
     * @param position  position within the list and hence cursor
     * @param mode      whether to skip or disable the rule
     */
    private void setRuleSkipperOrDisabled(int position, int mode) {
        rlcsr.moveToPosition(position);
        long productref = rlcsr.getLong(
                rlcsr.getColumnIndex(
                        DBProductusageTableConstants.PRODUCTUSAGE_PRODUCTREF_COL
                )
        );
        long aisleref = rlcsr.getLong(
                rlcsr.getColumnIndex(
                        DBProductusageTableConstants.PRODUCTUSAGE_AISLEREF_COL
                )
        );
        // Update the resepctive row in the productusage table
        dbpumethods.setRuleSuggestFlag(productref, aisleref, mode);
        // Refresh the cursor and then swap to the new cursor
        if (suggestmode) {
            rlcsr = dbrulemethods.getToolRules(rulesexist,minprd,minbuy,filter,orderby);
        }
        if (acccheckmode) {
            rlcsr = dbrulemethods.getToolRules(rulesexist,minprd,minbuy,filter,orderby);
        }
        if (disabledmode) {
            rlcsr = dbrulemethods.getDisabledRules(orderby);
            if (rlcsr.getCount() < 1) {
                this.finish();
            }
        }
        ruletoollistadapter.swapCursor(rlcsr);
    }

    /**************************************************************************
     * sortClick - Handle list heading clicks (i.e. sort by that field)
     *
     * @param view the view that was clicked
     */
    public void sortClick(View view) {
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,"Invoked",this,methodname);
        lastmessage = getResources().getString(R.string.ruleslabel) +
                " sorted by ";
        sortchanged = false;
        switch (view.getId()) {
            case R.id.rulesuggest_rulename:
                if (!suggestmode) {
                    getOrderBy(DBRulesTableConstants.RULES_NAME_COL_FULL,BYRULE);
                    lastmessage = lastmessage + " RULE NAME (";
                }
                break;
            case R.id.rulesuggest_productname:
                getOrderBy(DBProductsTableConstants.PRODUCTS_NAME_COL_FULL,BYPRODUCT);
                lastmessage = lastmessage + " PRODUCT NAME (";
                break;
            case R.id.rulesuggest_shopname:
                getOrderBy(DBShopsTableConstants.SHOPS_NAME_COL_FULL,BYSHOP);
                lastmessage = lastmessage + " SHOP NAME (";
            default:
                break;
        }
        if (sortchanged) {
            LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,"Sorting",this,methodname);
            if (suggestmode) {
                rlcsr = dbrulemethods.getToolRules(rulesexist,minprd,minbuy,filter,orderby);
            }
            if (acccheckmode) {
                rlcsr = dbrulemethods.getToolRules(rulesexist,minprd,minbuy,filter,orderby);
            }
            if (disabledmode) {
                rlcsr = dbrulemethods.getDisabledRules(orderby);
            }
            ruletoollistadapter.swapCursor(rlcsr);
            if (ordertype) {
                lastmessage = lastmessage + "ascending)";
            } else {
                lastmessage = lastmessage + "descending)";
            }
            setMessage(this,lastmessage,false);
            LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,
                    "Sorted by " + orderby,
                    this,methodname);
        }
    }

    /**************************************************************************
     * getOrderBy - Generate the new ORDEY BY sql (ORDER BY already exists)
     * @param newcolumn     the DB column to sort by
     * @param neworderfld   the column as an integer as per constants
     */
    @SuppressWarnings("ConstantConditions")
    private void getOrderBy(String newcolumn, int neworderfld) {
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,"Starting",this,methodname);
        orderby = newcolumn;
        // If already sorted by this column then toggle between ascedning and
        // descending.
        // If not then default to ascending
        if (orderfld == neworderfld) {
            if (ordertype) {
                orderby = orderby + SORTDESCENDING;
                ordertype = false;
            } else {
                orderby = orderby + SORTASCENDING;
                ordertype = true;
            }
        } else {
            orderby = orderby + SORTASCENDING;
            ordertype = true;
        }
        orderfld = neworderfld;
        sortchanged = true;
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,
                "Orderby field to be changed=" +  Boolean.toString(sortchanged) +
                        " will be " + orderby,
                this,methodname);
    }

    /**************************************************************************
     * setMessage - set the Message regarding the lat action performed
     * Note as an action may have altered the database getDBcounts
     * is called and the title changed
     *
     * @param rsca This class
     * @param msg  The message to be displayed.
     * @param flag Message imnportant, if true Yellow text, esle green
     */
    public void setMessage(RuleSuggestCheckActivity rsca, String msg, boolean flag) {
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,"Invoked",this,methodname);

        TextView messagebar = (TextView) rsca.findViewById(
                R.id.rulesuggest_messagebar);
        messagebar.setText(context.getResources().getString(
                R.string.messagebar_prefix_lastaction) + " " + msg);
        if (flag) {
            messagebar.setTextColor(Color.YELLOW);
        } else {
            messagebar.setTextColor(Color.GREEN);
        }
        messagebar.setVisibility(View.VISIBLE);
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,
                "Set Message=" + messagebar.getText().toString(),
                this,methodname);
    }

}
