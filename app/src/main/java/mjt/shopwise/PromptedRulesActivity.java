package mjt.shopwise;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import mjt.displayhelp.DisplayHelp;

import static mjt.sqlwords.SQLKWORD.*;

/**
 * Prompted Rules Activity
 */

@SuppressWarnings({"FieldCanBeLocal", "WeakerAccess", "unused"})
public class PromptedRulesActivity extends AppCompatActivity {

    @SuppressWarnings("unused")
    private final String THIS_ACTIVITY = "PromptedRulesActivity";
    private final String LOGTAG = "SW-PRA";
    @SuppressWarnings("unused")
    private String caller;
    private int resumestate = StandardAppConstants.RESUMSTATE_NORMAL;
    private Activity thisactivity;
    public static final String THISCLASS = PromptedRulesActivity.class.getSimpleName();

    Context context;
    ActionBar actionbar;

    /**
     * Colours
     */
    private int h1;
    @SuppressWarnings("unused")
    private int h2;
    @SuppressWarnings("unused")
    private int h3;
    @SuppressWarnings("unused")
    private int h4;
    private int primary_color;
    private String menucolorcode;
    /**
     * The Passedmenucolorcode.
     */
    @SuppressWarnings("unused")
    int passedmenucolorcode;

    @SuppressWarnings("unused")
    DBDAO dbdao;
    @SuppressWarnings("unused")
    DBShopMethods dbshopmethods;
    @SuppressWarnings("unused")
    DBAisleMethods dbaislemethods;
    @SuppressWarnings("unused")
    DBProductMethods dbproductmethods;
    @SuppressWarnings("unused")
    DBProductUsageMethods dbpumethods;
    @SuppressWarnings("unused")
    DBShopListMethods dbshoplistmethods;
    DBRuleMethods dbrulemethods;

    @SuppressWarnings("unused")
    private static final String SHOPLISTID_COLUMN = DBShopListTableConstants.SHOPLIST_ID_COL;
    @SuppressWarnings("unused")
    private static final String SHOPLISTID_FULLCOLUMN = DBShopListTableConstants.SHOPLIST_ID_COL_FULL;
    @SuppressWarnings("unused")
    private static final String SHOPLISTPRODUCTREF_COLUMN = DBShopListTableConstants.SHOPLIST_PRODUCTREF_COL;
    @SuppressWarnings("unused")
    private static final String SHOPLISTPRODUCTREF_FULLCOLUMN = DBShopListTableConstants.SHOPLIST_PRODUCTREF_COL_FULL;
    @SuppressWarnings("unused")
    private static final String SHOPLISTAISLREF_COLUMN = DBShopListTableConstants.SHOPLIST_AISLEREF_COL;
    @SuppressWarnings("unused")
    private static final String SHOPLISTAISLREF_FULLCOLUMN = DBShopListTableConstants.SHOPLIST_AISLEREF_COL_FULL;
    @SuppressWarnings("unused")
    private static final String SHOPLISTDATEADDED_COLUMN = DBShopListTableConstants.SHOPLIST_DATEADDED_COL;
    @SuppressWarnings("unused")
    private static final String SHOPLISTDATEADDED_FULLCOLUMN = DBShopListTableConstants.SHOPLIST_DATEADDED_COL_FULL;
    @SuppressWarnings("unused")
    private static final String SHOPLISTNUMBERTOGET_COLUMN = DBShopListTableConstants.SHOPLIST_NUMBERTOGET_COL;
    @SuppressWarnings("unused")
    private static final String SHOPLISTNUMBERTOGET_FULLCOLUMN = DBShopListTableConstants.SHOPLIST_NUMBERTOGET_COL_FULL;
    @SuppressWarnings("unused")
    private static final String SHOPLISTDONE_COLUMN = DBShopListTableConstants.SHOPLIST_DONE_COL;
    @SuppressWarnings("unused")
    private static final String SHOPLISTDONE_FULLCOLUMN = DBShopListTableConstants.SHOPLIST_DONE_COL_FULL;
    @SuppressWarnings("unused")
    private static final String SHOPLISTDATEGOT_COLUMN = DBShopListTableConstants.SHOPLIST_DATEGOT_COL;
    @SuppressWarnings("unused")
    private static final String SHOPLISTDATEGOT_FULLCOLUMN = DBShopListTableConstants.SHOPLIST_DATEGOT_COL_FULL;
    @SuppressWarnings("unused")
    private static final String SHOPLISTCOST_COLUMN = DBShopListTableConstants.SHOPLIST_COST_COL;
    @SuppressWarnings("unused")
    private static final String SHOPLISTCOST_FULLCOLUMN = DBShopListTableConstants.SHOPLIST_COST_COL_FULL;

    @SuppressWarnings("unused")
    private static final String SHOPID_COLUMN = DBShopsTableConstants.SHOPS_ID_COL;
    private static final String SHOPNAME_COLUMN = DBShopsTableConstants.SHOPS_NAME_COL;
    private static final String SHOPCITY_COLUMN = DBShopsTableConstants.SHOPS_CITY_COL;
    @SuppressWarnings("unused")
    private static final String SHOPORDER_COLUMN = DBShopsTableConstants.SHOPS_ORDER_COL;
    @SuppressWarnings("unused")
    private static final String SHOPID_FULLCOLUMN = DBShopsTableConstants.SHOPS_ID_COL_FULL;
    @SuppressWarnings("unused")
    private static final String SHOPNAME_FULLCOLUMN = DBShopsTableConstants.SHOPS_NAME_COL_FULL;
    @SuppressWarnings("unused")
    private static final String SHOPCITY_FULLCOLUMN = DBShopsTableConstants.SHOPS_CITY_COL_FULL;
    @SuppressWarnings("unused")
    private static final String SHOPORDER_FULLCOLUMN = DBShopsTableConstants.SHOPS_ORDER_COL_FULL;

    @SuppressWarnings("unused")
    private static final String AISLEID_COLUMN = DBAislesTableConstants.AISLES_ID_COL;
    @SuppressWarnings("unused")
    private static final String AILSEID_FULLCOLUMN = DBAislesTableConstants.AISLES_ID_COL_FULL;
    private static final String AISLENAME_COLUMN = DBAislesTableConstants.AISLES_NAME_COL;
    @SuppressWarnings("unused")
    private static final String AISLENAME_FULLCOLUMN = DBAislesTableConstants.AISLES_NAME_COL_FULL;
    @SuppressWarnings("unused")
    private static final String AISLEORDER_COLUMN = DBAislesTableConstants.AISLES_ORDER_COL;
    @SuppressWarnings("unused")
    private static final String AISLEORDER_FULLCOLUMN = DBAislesTableConstants.AISLES_ORDER_COL_FULL;
    @SuppressWarnings("unused")
    private static final String AISLESHOPREF_COLUMN = DBAislesTableConstants.AISLES_SHOPREF_COL;
    @SuppressWarnings("unused")
    private static final String AISLESHOPREF_FULLCOLUMN = DBAislesTableConstants.AISLES_SHOPREF_COL_FULL;

    @SuppressWarnings("unused")
    private static final String PRODUCTID_COLUMN = DBProductsTableConstants.PRODUCTS_ID_COL;
    private static final String PRODUCTNAME_COLUMN = DBProductsTableConstants.PRODUCTS_NAME_COL;
    @SuppressWarnings("unused")
    private static final String PRODUCTID_FULLCOLUMN = DBProductsTableConstants.PRODUCTS_ID_COL_FULL;
    private static final String PRODUCTNAME_FULLCOLUMN = DBProductsTableConstants.PRODUCTS_NAME_COL_FULL;

    @SuppressWarnings("unused")
    private static final String PRODUCTREF_COLUMN = DBProductusageTableConstants.PRODUCTUSAGE_PRODUCTREF_COL;
    @SuppressWarnings("unused")
    private static final String PRODUCTREF_FULLCOLUMN = DBProductusageTableConstants.PRODUCTUSAGE_PRODUCTREF_COL_FULL;
    @SuppressWarnings("unused")
    private static final String AISLEREF_COLUMN = DBProductusageTableConstants.PRODUCTUSAGE_AISLEREF_COL;
    @SuppressWarnings("unused")
    private static final String AISLEREF_FULLCOLUMN = DBProductusageTableConstants.PRODUCTUSAGE_AISLEREF_COL_FULL;
    @SuppressWarnings("unused")
    private static final String COST_COLUMN = DBProductusageTableConstants.PRODUCTUSAGE_COST_COL;
    @SuppressWarnings("unused")
    private static final String COST_FULLCOLUMN = DBProductusageTableConstants.PRODUCTUSAGE_COST_FULL;
    @SuppressWarnings("unused")
    private static final String BUYCOUNT_COLUMN = DBProductusageTableConstants.PRODUCTUSAGE_BUYCOUNT_COL;
    @SuppressWarnings("unused")
    private static final String BUYCOUNT_FULLCOLUMN = DBProductusageTableConstants.PRODUCTUSAGE_BUYCOUNT_COL_FULL;
    @SuppressWarnings("unused")
    private static final String FIRSTBUY_COLUMN = DBProductusageTableConstants.PRODUCTUSAGE_FIRSTBUYDATE_COL;
    @SuppressWarnings("unused")
    private static final String FIRSTBUY_FULLCOLUMN = DBProductusageTableConstants.PRODUCTUSAGE_FIRSTBUYDATE_COL_FULL;
    @SuppressWarnings("unused")
    private static final String LASTBUY_COLUMN = DBProductusageTableConstants.PRODUCTUSAGE_LATESTBUYDATE_COL;
    @SuppressWarnings("unused")
    private static final String LASTBUY_FULLCOLUMN = DBProductusageTableConstants.PRODUCTUSAGE_LATESTBUYDATE_COL_FULL;
    @SuppressWarnings("unused")
    private static final String PRODUCTUSAGEORDER_COLUMN = DBProductusageTableConstants.PRODUCTUSAGE_ORDER_COL;
    @SuppressWarnings("unused")
    private static final String PRODUCTUSAGEORDER_FULLCOLUMN = DBProductusageTableConstants.PRODUCTUSAGE_ORDER_COL_FULL;
    @SuppressWarnings("unused")
    private static final String PRODUCTUSAGECOST_COLUMN = DBProductusageTableConstants.PRODUCTUSAGE_COST_COL;
    @SuppressWarnings("unused")
    private static final String PRODUCTUSAGECOST_FULLCOLUMN = DBProductusageTableConstants.PRODUCTUSAGE_AISLEREF_COL_FULL;
    @SuppressWarnings("unused")
    private static final String CHECKLISTFLAG_COLUMN = DBProductusageTableConstants.PRODUCTUSAGE_CHECKLISTFLAG_COL;
    @SuppressWarnings("unused")
    private static final String CHECKLISTFLAG_FULLCOLUMN = DBProductusageTableConstants.PRODUCTUSAGE_CHECKLISTFLAG_COL_FULL;
    @SuppressWarnings("unused")
    private static final String CHECKLISTCOUNT_COLUMN = DBProductusageTableConstants.PRODUCTUSAGE_CHECKLISTCOUNT_COL;
    @SuppressWarnings("unused")
    private static final String CHECKLISTCOUNT_FULLCOLUMN = DBProductusageTableConstants.PRODUCTUSAGE_CHECKLISTCOUNT_COL_FULL;

    @SuppressWarnings("unused")
    private static final String RULEID_COLUMN = DBRulesTableConstants.RULES_ID_COL;
    @SuppressWarnings("unused")
    private static final String RULEID_FULLCOLUMN = DBRulesTableConstants.RULES_ID_COL_FULL;
    private static final String RULENAME_COLUMN = DBRulesTableConstants.RULES_NAME_COL;
    private static final String RULENAME_FULLCOLUMN = DBRulesTableConstants.RULES_NAME_COL_FULL;
    @SuppressWarnings("unused")
    private static final String RULEAISLEREF_COLUMN = DBRulesTableConstants.RULES_AISLEREF_COL;
    @SuppressWarnings("unused")
    private static final String RULEAISLEREF_FULLCOLUMN = DBRulesTableConstants.RULES_AISLEREF_COL_FULL;
    @SuppressWarnings("unused")
    private static final String RULEPRODUCTREF_COLUMN = DBRulesTableConstants.RULES_PRODUCTREF_COL;
    @SuppressWarnings("unused")
    private static final String RULEPRODUCTREF_FULLCOLUMN = DBRulesTableConstants.RULES_PRODUCTREF_COL_FULL;
    @SuppressWarnings("unused")
    private static final String RULEUSES_COLUMN = DBRulesTableConstants.RULES_USES_COL;
    @SuppressWarnings("unused")
    private static final String RULEUSES_FULLCOLUMN = DBRulesTableConstants.RULES_USES_COL_FULL;
    @SuppressWarnings("unused")
    private static final String RULEPROMPT_COLUMN = DBRulesTableConstants.RULES_PROMPT_COL;
    @SuppressWarnings("unused")
    private static final String RULEPROMPT_FULLCOLUMN = DBRulesTableConstants.RULES_PROMPT_COL_FULL;
    @SuppressWarnings("unused")
    private static final String RULEACTON_COLUMN  = DBRulesTableConstants.RULES_ACTON_COL;
    private static final String RULEACTON_FULLCOLUMN = DBRulesTableConstants.RULES_ACTOON_COL_FULL;
    @SuppressWarnings("unused")
    private static final String RULEPERIOD_COLUMN = DBRulesTableConstants.RULES_PERIOD_COL;
    @SuppressWarnings("unused")
    private static final String RULEPERIOD_FULLCOLUMN = DBRulesTableConstants.RULES_PERIOD_COL_FULL;
    @SuppressWarnings("unused")
    private static final String RULEMULTIPLIER_COLUMN = DBRulesTableConstants.RULES_MULTIPLIER_COL;
    @SuppressWarnings("unused")
    private static final String RULEMULTIPLIER_FULLCOLUMN = DBRulesTableConstants.RULES_MULTIPLIER_COL_FULL;

    Cursor rlcsr;
    @SuppressWarnings("unused")
    private String currentrulename = "";

    /**
     * Sorting Productlist columns
     */
    private static final int BYRULENAME = 0;
    private static final int BYPRODUCTNAME = 1;
    private static final int BYSHOPNAME = 2;
    private static final String SORTASCENDING = SQLORDERASCENDING;
    private static final String SORTDESCENDING = SQLORDERDESCENDING;
    static String orderby = RULENAME_FULLCOLUMN + SORTASCENDING;
    static int orderfld = BYRULENAME;
    static boolean ordertype = true;
    static boolean sortchanged = false;
    private static String lastmessage = "";

    TextView messagebar;
    TextView donebutton;
    ListView rulelist;
    TextView sortable;
    TextView clickable;
    TextView longclickable;
    LinearLayout rulelistheading;
    AdapterPromptedRuleList rulelistadpater;

    protected void onCreate(Bundle savedInstanceState) {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promptedrules);
        context = this;
        thisactivity = (Activity) context;
        caller = getIntent().getStringExtra(
                StandardAppConstants.INTENTKEY_CALLINGACTIVITY
        );

        logmsg = "Preparing ColorCoding";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        menucolorcode = StandardAppConstants.INTENTKEY_MENUCOLORCODE;
        passedmenucolorcode = getIntent().getIntExtra(menucolorcode, 0);
        messagebar = (TextView) findViewById(R.id.promptedrules_messagebar);
        donebutton = (TextView) findViewById(R.id.promptedrules_donebutton);
        rulelistheading = (LinearLayout) findViewById(R.id.promptedrules_rulelist_heading);
        rulelist = (ListView) findViewById(R.id.promptedrules_rulelist);
        sortable = (TextView) findViewById(R.id.sortable);
        clickable = (TextView) findViewById(R.id.clickable);
        longclickable = (TextView) findViewById(R.id.longclickable);

        actionbar = getSupportActionBar();
        ActionColorCoding.setActionBarColor(this,getIntent(),actionbar);
        primary_color = ActionColorCoding.setHeadingColor(this,getIntent(),0);
        h1 = ActionColorCoding.setHeadingColor(this,getIntent(),1);
        h2 = ActionColorCoding.setHeadingColor(this,getIntent(),2);
        h3 = ActionColorCoding.setHeadingColor(this,getIntent(),3);
        h4 = ActionColorCoding.setHeadingColor(this,getIntent(),4);
        ActionColorCoding.setActionButtonColor(donebutton, primary_color);
        rulelistheading.setBackgroundColor(h1);
        sortable.setTextColor(primary_color);
        clickable.setVisibility(View.INVISIBLE);
        longclickable.setVisibility(View.INVISIBLE);

        logmsg = "Preparing Databases";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        dbdao = new DBDAO(this);
        dbshopmethods = new DBShopMethods(this);
        dbaislemethods = new DBAisleMethods(this);
        dbproductmethods = new DBProductMethods(this);
        dbpumethods = new DBProductUsageMethods(this);
        dbshoplistmethods = new DBShopListMethods(this);
        dbrulemethods = new DBRuleMethods(this);

        logmsg = "Preparing RulesList";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        rlcsr = dbrulemethods.getPromptedRules("",orderby);
        rulelistadpater = new AdapterPromptedRuleList(this,rlcsr,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER,
                this.getIntent());
        rulelist.setAdapter(rulelistadpater);

        logmsg = "Displaying Dialog Explaining Prompted Rules";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        String classname = this.getClass().getCanonicalName();
        String title = "Prompted Rules Exist";
        String positivebuttontext = "";
        String positiveaction = "";
        String negativebuttontext = "";
        String negativeaction = "";
        String neutralbuttontext = getResources().getString(R.string.proceedbutton);
        String neutralaction = "";
        String message = getResources().getString(R.string.availableoptions) +
                neutralbuttontext + ".\n\n" +
                "This screen is displayed to make you aware that Prompted " +
                "Rules exist and that the next screen will display them. " +
                "\n\nThe list allows you to Add them to the Shopping List or to " +
                " Skip adding them to the shopping list." +
                "\n\nYou can also skip all by clicking the DONE button." +
                "\n\nNOTE using the DONE button, does not result in the " +
                " date of the rule being altered. Whilst, using the SKIP " +
                "button does alter the date, ready for the next occurence. " +
                "\n\nThus, using DONE, will only temporaily skip the rule." +
                "Using SKIP permenantly skips the occurence." +
                "\n\nNOTE it may appear that after using SKIP or ADD, that " +
                "the same rule reappears. This would be due to the next " +
                "occurence of the rule then becomming valid. The Rule Date " +
                "would be different.";
        MixTripleLongTripleInt values = new MixTripleLongTripleInt();
        values.setMIXTRPPLONGINT(0,0,0,0,0,0);
        new RequestDialog().requestDialog(thisactivity,
                classname,
                title,message,
                positivebuttontext,negativebuttontext,neutralbuttontext,
                positiveaction,negativeaction,neutralaction,
                values);
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
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
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
        logmsg = "Refreshing RulesList";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        rlcsr = dbrulemethods.getPromptedRules("",orderby);
        rulelistadpater.swapCursor(rlcsr);
        resumestate = StandardAppConstants.RESUMSTATE_NORMAL;
    }

    /**
     * Add the help option to the Activity's menu bar.
     * @param menu  The menu xml
     * @return  true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.common_help, menu);
        return true;
    }

    /**
     * Action the respective option when the menu is selected
     * @param menuitem  The menuitem that was selected
     * @return true to indicate actioned.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem menuitem) {
        int menuitemid = menuitem.getItemId();
        switch (menuitemid) {
            case R.id.actionhelp:
                //new DisplayHelp(this,"ALt Title",R.array.help_main_activity,80,true,0xffff0000, 0xbbffffff,20f,16f,12);
                new DisplayHelp(this,
                        getResources().getString(
                                R.string.title_help_promptedrules_activity),
                        R.array.help_promptedrules_activty,
                        85,
                        true,
                        primary_color,
                        0xbbffffff,
                        22f,
                        16f,
                        12
                );
                return true;
            default:
                break;
        }
        return  onOptionsItemSelected(menuitem);
    }

    /**************************************************************************
     * onDestroy - do any clean up before th application is destroyed
     * e.g. close any open cursors and then close the database
     */
    @Override
    protected void onDestroy() {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        super.onDestroy();
        rlcsr.close();
    }

    /**************************************************************************
     * sortClick - Handle list heading clicks (i.e. sort by that field)
     *
     * @param view the view that was clicked
     */
    @SuppressWarnings("unused")
    public void sortClick(View view) {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        lastmessage = getResources().getString(R.string.ruleslabel) +
                " sorted by ";
        switch (view.getId()) {
            case R.id.promptedrules_rulelist_heading_rulename:
                getOrderBy(RULENAME_FULLCOLUMN,BYRULENAME);
                lastmessage = lastmessage + " RULE NAME (";
                break;
            case R.id.promptedrules_rulelist_heading_productname:
                getOrderBy(PRODUCTNAME_FULLCOLUMN,BYPRODUCTNAME);
                lastmessage = lastmessage + " PRODUCT NAME (";
                break;
            case R.id.promptedrules_rulelist_heading_shopname:
                getOrderBy(RULEACTON_FULLCOLUMN,BYSHOPNAME);
                lastmessage = lastmessage + " NEXT DATE (";
                break;
            default:
                break;
        }
        if (sortchanged) {
            rlcsr = dbrulemethods.getPromptedRules("",orderby);
            rulelistadpater.swapCursor(rlcsr);
            if (ordertype) {
                lastmessage = lastmessage + "ascending)";
            } else {
                lastmessage = lastmessage + "descending)";
            }
            setMessage(this,lastmessage,false);
        }
    }

    /**************************************************************************
     * setMessage - set the Message regarding the lat action performed
     * Note as an action may have altered the database getDBcounts
     * is called and the title changed
     *
     * @param pra   the pra
     * @param msg  The message to be displayed.
     * @param flag Message imnportant, if true Yellow text, esle green
     */
    public void setMessage(PromptedRulesActivity pra,
                           String msg,
                           @SuppressWarnings("SameParameterValue") boolean flag) {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);

        TextView messagebar = (TextView) pra.findViewById(
                R.id.promptedrules_messagebar);
        messagebar.setText(context.getResources().getString(
                R.string.messagebar_prefix_lastaction) + " " + msg);
        if (flag) {
            messagebar.setTextColor(Color.YELLOW);
        } else {
            messagebar.setTextColor(Color.GREEN);
        }
        messagebar.setVisibility(View.VISIBLE);
    }
    /**************************************************************************
     * getOrderBy - Generate the new ORDEY BY sql (ORDER BY already exists)
     * @param newcolumn     the DB column to sort by
     * @param neworderfld   the column as an integer as per constants
     */
    private void getOrderBy(String newcolumn, int neworderfld) {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
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
    }

    /**************************************************************************
     * Button Click Handler
     *
     * @param view The view (i.e the TextView that was clicked)
     */
    @SuppressWarnings("unused")
    public void actionButtonClick(View view) {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);

        int tag, cpos;

        switch (view.getId()) {
            case R.id.promptedrules_donebutton:
                logmsg = "Finishing";
                LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
                this.finish();
                break;
            case R.id.promptedruleslist_skipbutton:
                tag = Integer.parseInt(view.getTag().toString());
                cpos = rlcsr.getPosition();
                rlcsr.moveToPosition(tag);
                logmsg = "Skipping PromptedRule for " +
                        rlcsr.getString(
                                rlcsr.getColumnIndex(RULENAME_COLUMN));
                LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
                dbrulemethods.applyPromptedRule(rlcsr.getLong(
                        rlcsr.getColumnIndex(
                                DBRulesTableConstants.RULES_ID_COL
                        )), true);
                rlcsr = dbrulemethods.getPromptedRules("",orderby);
                if (rlcsr.getCount() < 1) {
                    logmsg = "No More Prompted Rules left. Finishing and going to ShoppingList";
                    LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
                    this.finish();
                }
                logmsg = "Refreshing PromptedRulesList";
                LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
                rulelistadpater.swapCursor(rlcsr);
                break;
            case R.id.promptedruleslist_addbutton:
                tag = Integer.parseInt(view.getTag().toString());
                cpos = rlcsr.getPosition();
                rlcsr.moveToPosition(tag);
                logmsg = "Adding Product=" +
                        rlcsr.getString(
                                rlcsr.getColumnIndex(PRODUCTNAME_COLUMN)) +
                        " Shop=" +
                        rlcsr.getString(
                                rlcsr.getColumnIndex(SHOPNAME_COLUMN)) +
                        " City=" +
                        rlcsr.getString(
                                rlcsr.getColumnIndex(SHOPCITY_COLUMN)) +
                        " Ailse=" +
                        rlcsr.getString(
                                rlcsr.getColumnIndex(AISLENAME_COLUMN));
                LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
                dbrulemethods.applyPromptedRule(rlcsr.getLong(
                        rlcsr.getColumnIndex(
                                DBRulesTableConstants.RULES_ID_COL
                        )), false);
                rlcsr.moveToPosition(cpos);
                rlcsr = dbrulemethods.getPromptedRules("",orderby);
                if (rlcsr.getCount() < 1) {
                    logmsg = "No More Prompted Rules left. Finishing and going to ShoppingList";
                    LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
                    this.finish();
                }
                logmsg = "Refreshing PromptedRulesList";
                LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
                rulelistadpater.swapCursor(rlcsr);
                break;
            default:
                break;
        }
    }
}
