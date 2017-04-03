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
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by Mike092015 on 7/01/2017.
 */

@SuppressWarnings({"FieldCanBeLocal", "WeakerAccess", "unused"})
public class RulesActivity extends AppCompatActivity {

    private final String THIS_ACTIVITY = "RulesActivity";
    private final String LOGTAG = "SW-RA";
    @SuppressWarnings("unused")
    private String caller;
    private int resumestate = StandardAppConstants.RESUMSTATE_NORMAL;
    private Activity thisactivity;
    public static final String THISCLASS = RulesActivity.class.getSimpleName();

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

    private static final String RULEID_COLUMN = DBRulesTableConstants.RULES_ID_COL;
    @SuppressWarnings("unused")
    private static final String RULEID_FULLCOLUMN = DBRulesTableConstants.RULES_ID_COL_FULL;
    private static final String RULENAME_COLUMN = DBRulesTableConstants.RULES_NAME_COL;
    private static final String RULENAME_FULLCOLUMN = DBRulesTableConstants.RULES_NAME_COL_FULL;
    private static final String RULEAISLEREF_COLUMN = DBRulesTableConstants.RULES_AISLEREF_COL;
    @SuppressWarnings("unused")
    private static final String RULEAISLEREF_FULLCOLUMN = DBRulesTableConstants.RULES_AISLEREF_COL_FULL;
    private static final String RULEPRODUCTREF_COLUMN = DBRulesTableConstants.RULES_PRODUCTREF_COL;
    @SuppressWarnings("unused")
    private static final String RULEPRODUCTREF_FULLCOLUMN = DBRulesTableConstants.RULES_PRODUCTREF_COL_FULL;
    private static final String RULEUSES_COLUMN = DBRulesTableConstants.RULES_USES_COL;
    @SuppressWarnings("unused")
    private static final String RULEUSES_FULLCOLUMN = DBRulesTableConstants.RULES_USES_COL_FULL;
    private static final String RULEPROMPT_COLUMN = DBRulesTableConstants.RULES_PROMPT_COL;
    private static final String RULEPROMPT_FULLCOLUMN = DBRulesTableConstants.RULES_PROMPT_COL_FULL;
    private static final String RULEACTON_COLUMN  = DBRulesTableConstants.RULES_ACTON_COL;
    private static final String RULEACTON_FULLCOLUMN = DBRulesTableConstants.RULES_ACTOON_COL_FULL;
    private static final String RULEPERIOD_COLUMN = DBRulesTableConstants.RULES_PERIOD_COL;
    @SuppressWarnings("unused")
    private static final String RULEPERIOD_FULLCOLUMN = DBRulesTableConstants.RULES_PERIOD_COL_FULL;
    private static final String RULEMULTIPLIER_COLUMN = DBRulesTableConstants.RULES_MULTIPLIER_COL;
    @SuppressWarnings("unused")
    private static final String RULEMULTIPLIER_FULLCOLUMN = DBRulesTableConstants.RULES_MULTIPLIER_COL_FULL;

    Cursor rlcsr;
    private String currentrulename = "";

    /**
     * Sorting Productlist columns
     */
    private static final int BYRULENAME = 0;
    private static final int BYPRODUCTNAME = 1;
    private static final int BYACTONDATE = 2;
    private static final int BYPROMPT = 3;
    private static final String SORTASCENDING = DBConstants.SQLORDERASCENDING;
    private static final String SORTDESCENDING = DBConstants.SQLORDERDESCENDING;
    static String orderby = RULENAME_FULLCOLUMN + SORTASCENDING;
    static int orderfld = BYRULENAME;
    static boolean ordertype = true;
    static boolean sortchanged = false;
    private static String lastmessage = "";

    TextView messagebar;
    TextView donebutton;
    TextView addbutton;
    ListView rulelist;
    LinearLayout rulelistheading;
    AdapterRuleList rulelistadpater;


    protected void onCreate(Bundle savedInstanceState) {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);
        context = this;
        thisactivity = (Activity) context;
        logmsg = "Retrieving IntentExtras.";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        caller = getIntent().getStringExtra(
                StandardAppConstants.INTENTKEY_CALLINGACTIVITY
        );

        logmsg = "Preparing ColorCoding and Display";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        menucolorcode = StandardAppConstants.INTENTKEY_MENUCOLORCODE;
        passedmenucolorcode = getIntent().getIntExtra(menucolorcode,0);

        messagebar = (TextView) findViewById(R.id.rules_messagebar);
        donebutton = (TextView) findViewById(R.id.rules_donebutton);
        addbutton = (TextView) findViewById(R.id.rules_addbutton);
        rulelistheading = (LinearLayout) findViewById(R.id.rules_rulelist_heading);
        rulelist = (ListView) findViewById(R.id.rules_rulelist);


        actionbar = getSupportActionBar();
        ActionColorCoding.setActionBarColor(this,getIntent(),actionbar);
        primary_color = ActionColorCoding.setHeadingColor(this,getIntent(),0);
        h1 = ActionColorCoding.setHeadingColor(this,getIntent(),1);
        h2 = ActionColorCoding.setHeadingColor(this,getIntent(),2);
        h3 = ActionColorCoding.setHeadingColor(this,getIntent(),3);
        h4 = ActionColorCoding.setHeadingColor(this,getIntent(),4);
        ActionColorCoding.setActionButtonColor(donebutton, primary_color);
        ActionColorCoding.setActionButtonColor(addbutton,primary_color);
        rulelistheading.setBackgroundColor(h1);

        ActionColorCoding.setSwatches(findViewById(android.R.id.content), this.getIntent());

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
        rlcsr = dbrulemethods.getExpandedRuleList("",orderby);
        rulelistadpater = new AdapterRuleList(this,
                rlcsr,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER,
                this.getIntent(),
                false,
                true,
                true);
        rulelist.setAdapter(rulelistadpater);

        logmsg = "Preparing OnItemClick Listener";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        rulelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position,
                                    long id) {
                listItemClick(view, position, id);
            }
        });

        logmsg = "Preparing OnItemLongClick Listener";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        rulelist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view,
                                           int position,
                                           long id) {
                listItemLongClick(view, position, id);
                return true;
            }
        });
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
        rlcsr = dbrulemethods.getExpandedRuleList("",orderby);
        rulelistadpater.swapCursor(rlcsr);
        refreshDisplay();
        resumestate = StandardAppConstants.RESUMSTATE_NORMAL;
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
     * Button Click Handler
     *
     * @param view The view (i.e the TextView that was clicked)
     */
    @SuppressWarnings("unused")
    public void actionButtonClick(View view) {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        switch (view.getId()) {
            case R.id.rules_donebutton:
                logmsg = "Finishing";
                LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
                this.finish();
                break;
            case R.id.rules_addbutton:
                addRule();
            default:
                break;
        }
    }

    /**************************************************************************
     *
     * @param view
     * @param position
     * @param id
     */
    public void listItemLongClick(View view, int position, long id) {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        int cpos = rlcsr.getPosition();
        rlcsr.moveToPosition(position);
        logmsg = "Preparing RequstDisalog for Rule=" +
                rlcsr.getString(
                        rlcsr.getColumnIndex(RULENAME_COLUMN)) + " " +
                "RuleID=" +
                Long.toString(
                        rlcsr.getLong(
                                rlcsr.getColumnIndex(RULEID_COLUMN)));
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        long ruleid = rlcsr.getLong(
                rlcsr.getColumnIndex(RULEID_COLUMN)
        );
        currentrulename = rlcsr.getString(
                rlcsr.getColumnIndex(RULENAME_COLUMN)
        );
        rlcsr.moveToPosition(cpos);
        Class cls = null;
        String title = "Delete Rule - " + currentrulename;
        String positivebuttontext = getResources().getString(
                R.string.deletebutton);
        String positiveaction = "ruleDelete";
        String negativebuttontext = "";
        String negativeaction = "";
        String neutralbuttontext = getResources().getString(
                R.string.cancelbutton);
        String neutralaction = "";

        String message = "Deleteing Rule = " + currentrulename;
        MixTripleLongTripleInt values = new MixTripleLongTripleInt();
        values.setMIXTRPPLONGINT(ruleid,0,0,0,0,0);

        String classname = this.getClass().getCanonicalName();
        logmsg = "Displaying Prepared RequestDialog";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        new RequestDialog().requestDialog(thisactivity, classname,
                title,message,
                positivebuttontext, negativebuttontext, neutralbuttontext,
                positiveaction, negativeaction, neutralaction,
                values);
    }

    /**************************************************************************
     *
     * @param view
     * @param position
     * @param id
     */
    public void listItemClick(View view, int position, long id) {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        rlcsr.moveToPosition(position);
        logmsg = "Preparing RequstDisalog for Rule=" +
                rlcsr.getString(
                        rlcsr.getColumnIndex(RULENAME_COLUMN)) + " " +
                "RuleID=" +
                Long.toString(
                        rlcsr.getLong(
                                rlcsr.getColumnIndex(RULEID_COLUMN)));
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        long ruleid = rlcsr.getLong(
                rlcsr.getColumnIndex(RULEID_COLUMN)
        );
        currentrulename = rlcsr.getString(
                rlcsr.getColumnIndex(RULENAME_COLUMN)
        );
        //Class cls = null;
        String classname = this.getClass().getCanonicalName();
        String title = "Edit Rule - " + currentrulename;
        String positivebuttontext = getResources().getString(
                R.string.editbutton);
        String positiveaction = "ruleEdit";
        String negativebuttontext = "";
        String negativeaction = "";
        String neutralbuttontext = getResources().getString(
                R.string.cancelbutton);
        String neutralaction = "";
        String message = "Available Options are " + neutralbuttontext +
                " AND " + positivebuttontext + "\n\n" +
                "\t" + neutralbuttontext + " returns to Rules, doing nothing." +
                "\t" + positivebuttontext + " allows the Rule to be changed.";
        MixTripleLongTripleInt values = new MixTripleLongTripleInt();
        values.setMIXTRPPLONGINT(ruleid,0,0,0,0,0);
        logmsg = "Displaying Prepared RequestDialog";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        new RequestDialog().requestDialog(thisactivity,
                classname,
                title,message,
                positivebuttontext,negativebuttontext,neutralbuttontext,
                positiveaction,negativeaction,neutralaction,
                values);
    }

    /**************************************************************************
     *
     * @param values
     */
    @SuppressWarnings("unused")
    public void ruleEdit(RequestDialogParameters values) {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        Activity activity = (values.getPassedactivity());
        RulesActivity ra = (RulesActivity) activity;
        long ruleid = values.getLong1();
        Intent intent = new Intent(this,RulesAddEditActivity.class);
        intent.putExtra(
                StandardAppConstants.INTENTKEY_CALLINGACTIVITY,THIS_ACTIVITY
        );
        intent.putExtra(
                StandardAppConstants.INTENTKEY_CALLINGMODE,
                StandardAppConstants.CM_EDIT
        );
        intent.putExtra(
                StandardAppConstants.INTENTKEY_RULEID,ruleid);
        intent.putExtra(
                StandardAppConstants.INTENTKEY_RULENAME,
                rlcsr.getString(
                        rlcsr.getColumnIndex(RULENAME_COLUMN)));
        intent.putExtra(
                StandardAppConstants.INTENTKEY_RULEAISLEID,
                rlcsr.getLong(
                        rlcsr.getColumnIndex(RULEAISLEREF_COLUMN)));
        intent.putExtra(
                StandardAppConstants.INTENTKEY_RULEPRODUCTREF,
                rlcsr.getLong(
                        rlcsr.getColumnIndex(RULEPRODUCTREF_COLUMN)));
        intent.putExtra(
                StandardAppConstants.INTENTKEY_RULEUSES,
                rlcsr.getInt(
                        rlcsr.getColumnIndex(RULEUSES_COLUMN)));
        intent.putExtra(
                StandardAppConstants.INTENTKEY_RULEPROMPT,
                rlcsr.getInt(
                        rlcsr.getColumnIndex(RULEPROMPT_COLUMN)));
        intent.putExtra(
                StandardAppConstants.INTENTKEY_RULEACTON,
                rlcsr.getLong(
                        rlcsr.getColumnIndex(
                                RULEACTON_COLUMN)));
        intent.putExtra(
                StandardAppConstants.INTENTKEY_RULEPERIOD,
                rlcsr.getInt(
                        rlcsr.getColumnIndex(RULEPERIOD_COLUMN)));
        intent.putExtra(
                StandardAppConstants.INTENTKEY_RULEMULTIPLIER,
                rlcsr.getInt(
                        rlcsr.getColumnIndex(RULEMULTIPLIER_COLUMN)));
        intent.putExtra(menucolorcode,passedmenucolorcode);
        logmsg = "Starting Activity=" +
                RulesAddEditActivity.class.getSimpleName() +
                " for Rule=" +
                rlcsr.getString(
                        rlcsr.getColumnIndex(RULENAME_COLUMN)) +
                " ID=" +
                Long.toString(
                        rlcsr.getLong(
                                rlcsr.getColumnIndex(RULEID_COLUMN))) +
                " Product=" +
                rlcsr.getString(
                        rlcsr.getColumnIndex(PRODUCTNAME_COLUMN)) +
                " Shop=" +
                rlcsr.getString(
                        rlcsr.getColumnIndex(SHOPNAME_COLUMN)) +
                " City=" +
                rlcsr.getString(
                        rlcsr.getColumnIndex(SHOPCITY_COLUMN)) +
                " Aisle=" +
                rlcsr.getString(
                        rlcsr.getColumnIndex(AISLENAME_COLUMN)) +
                " Mode=EDIT";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        startActivity(intent);
    }

    /**************************************************************************
     *
     * @param values
     */
    @SuppressWarnings("unused")
    public void ruleDelete(RequestDialogParameters values) {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        Activity activity = (values.getPassedactivity());
        RulesActivity ra = (RulesActivity) activity;
        ra.dbrulemethods.deleteRule(values.getLong1());
        ra.rlcsr = dbrulemethods.getExpandedRuleList("",orderby);
        ra.rulelistadpater.swapCursor(rlcsr);
        ra.setMessage(ra,"Rule " +
                currentrulename
                + " Deleted.",true);
        logmsg = "Rule " + currentrulename + " Deleted";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
    }
    /**************************************************************************
     *
     */
    public void addRule(){
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        Intent intent = new Intent(this,RulesAddEditActivity.class);
        intent.putExtra(StandardAppConstants.INTENTKEY_CALLINGACTIVITY,
                THIS_ACTIVITY);
        intent.putExtra(StandardAppConstants.INTENTKEY_CALLINGMODE,
                StandardAppConstants.CM_ADD);
        intent.putExtra(menucolorcode,
                passedmenucolorcode);
        logmsg = "Starting Activity=" +
                RulesAddEditActivity.class.getSimpleName() + " Mode=ADD";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        startActivity(intent);
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
            case R.id.rules_rulelist_heading_rulename:
                getOrderBy(RULENAME_FULLCOLUMN,BYRULENAME);
                lastmessage = lastmessage + " RULE NAME (";
                break;
            case R.id.rules_rulelist_heading_productname:
                getOrderBy(PRODUCTNAME_FULLCOLUMN,BYPRODUCTNAME);
                lastmessage = lastmessage + " PRODUCT NAME (";
                break;
            case R.id.rules_rulelist_heading_acton:
                getOrderBy(RULEACTON_FULLCOLUMN,BYACTONDATE);
                lastmessage = lastmessage + " NEXT DATE (";
                break;
            case R.id.rules_rulelist_heading_prompt:
                getOrderBy(RULEPROMPT_FULLCOLUMN,BYPROMPT);
                lastmessage = lastmessage + " PROMPTED (";
            default:
                break;
        }
        if (sortchanged) {
            rlcsr = dbrulemethods.getExpandedRuleList("",orderby);
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
     * refresh the display i.e. the listview and the listview heading
     */
    public void refreshDisplay() {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
    }

    /**************************************************************************
     * setMessage - set the Message regarding the lat action performed
     * Note as an action may have altered the database getDBcounts
     * is called and the title changed
     *
     * @param ra   the sa
     * @param msg  The message to be displayed.
     * @param flag Message imnportant, if true Yellow text, esle green
     */
    public void setMessage(RulesActivity ra, String msg, boolean flag) {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);

        TextView messagebar = (TextView) ra.findViewById(
                R.id.rules_messagebar);
        messagebar.setText(context.getResources().getString(
                R.string.messagebar_prefix_lastaction) + " " + msg);
        if (flag) {
            messagebar.setTextColor(Color.YELLOW);
        } else {
            messagebar.setTextColor(Color.GREEN);
        }
        messagebar.setVisibility(View.VISIBLE);
        ra.actionbar.setTitle(getResources().getString(R.string.ruleslabel));
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
     * Do nothing.
     *
     * @param values the values
     */
    @SuppressWarnings({"unused", "EmptyMethod"})
    public void doNothing(RequestDialogParameters values) {
    }

}
