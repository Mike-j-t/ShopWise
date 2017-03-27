package mjt.shopwise;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Mike092015 on 7/01/2017.
 */

@SuppressWarnings({"FieldCanBeLocal", "WeakerAccess", "CanBeFinal", "unused"})
public class RulesAddEditActivity extends AppCompatActivity {

    @SuppressWarnings("unused")
    private final String THIS_ACTIVITY = "RulesAddeditActivity";
    private final String LOGTAG = "SW-RAEA";
    private String caller;
    private int calledmode;
    private int resumestate = StandardAppConstants.RESUMSTATE_NORMAL;
    @SuppressWarnings("unused")
    private Activity thisactivity;
    public static final String THISCLASS = RulesAddEditActivity.class.getSimpleName();

    Context context;
    ActionBar actionbar;

    /**
     * Colours
     */
    @SuppressWarnings("unused")
    private int h1;
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
    DBShopMethods dbshopmethods;
    DBAisleMethods dbaislemethods;
    DBProductMethods dbproductmethods;
    DBProductUsageMethods dbpumethods;
    @SuppressWarnings("unused")
    DBShopListMethods dbshoplistmethods;
    DBRuleMethods dbrulemethods;
    DBAppvaluesMethods dbappvaluesmethods;

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

    private static final String SHOPID_COLUMN = DBShopsTableConstants.SHOPS_ID_COL;
    @SuppressWarnings("unused")
    private static final String SHOPNAME_COLUMN = DBShopsTableConstants.SHOPS_NAME_COL;
    @SuppressWarnings("unused")
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

    private static final String AISLEID_COLUMN = DBAislesTableConstants.AISLES_ID_COL;
    @SuppressWarnings("unused")
    private static final String AILSEID_FULLCOLUMN = DBAislesTableConstants.AISLES_ID_COL_FULL;
    @SuppressWarnings("unused")
    private static final String AISLENAME_COLUMN = DBAislesTableConstants.AISLES_NAME_COL;
    @SuppressWarnings("unused")
    private static final String AISLENAME_FULLCOLUMN = DBAislesTableConstants.AISLES_NAME_COL_FULL;
    @SuppressWarnings("unused")
    private static final String AISLEORDER_COLUMN = DBAislesTableConstants.AISLES_ORDER_COL;
    @SuppressWarnings("unused")
    private static final String AISLEORDER_FULLCOLUMN = DBAislesTableConstants.AISLES_ORDER_COL_FULL;
    private static final String AISLESHOPREF_COLUMN = DBAislesTableConstants.AISLES_SHOPREF_COL;
    @SuppressWarnings("unused")
    private static final String AISLESHOPREF_FULLCOLUMN = DBAislesTableConstants.AISLES_SHOPREF_COL_FULL;

    private static final String PRODUCTID_COLUMN = DBProductsTableConstants.PRODUCTS_ID_COL;
    @SuppressWarnings("unused")
    private static final String PRODUCTNAME_COLUMN = DBProductsTableConstants.PRODUCTS_NAME_COL;
    @SuppressWarnings("unused")
    private static final String PRODUCTID_FULLCOLUMN = DBProductsTableConstants.PRODUCTS_ID_COL_FULL;
    @SuppressWarnings("unused")
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
    @SuppressWarnings("unused")
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
    private static final String RULEACTON_COLUMN = DBRulesTableConstants.RULES_ACTON_COL;
    @SuppressWarnings("unused")
    private static final String RULEACTON_FULLCOLUMN = DBRulesTableConstants.RULES_ACTOON_COL_FULL;
    @SuppressWarnings("unused")
    private static final String RULEPERIOD_COLUMN = DBRulesTableConstants.RULES_PERIOD_COL;
    @SuppressWarnings("unused")
    private static final String RULEPERIOD_FULLCOLUMN = DBRulesTableConstants.RULES_PERIOD_COL_FULL;
    @SuppressWarnings("unused")
    private static final String RULEMULTIPLIER_COLUMN = DBRulesTableConstants.RULES_MULTIPLIER_COL;
    @SuppressWarnings("unused")
    private static final String RULEMULTIPLIER_FULLCOLUMN = DBRulesTableConstants.RULES_MULTIPLIER_COL_FULL;

    @SuppressWarnings("unused")
    Cursor rlcsr;
    Cursor slcsr;
    Cursor alcsr;
    Cursor plcsr;
    Cursor rpcsr;

    /**
     * Sorting Productlist columns
     */
    private static final int BYRULENAME = 0;
    @SuppressWarnings("unused")
    private static final int BYPRODUCTNAME = 1;
    @SuppressWarnings("unused")
    private static final int BYACTONDATE = 2;
    private static final String SORTASCENDING = DBConstants.SQLORDERASCENDING;
    private static final String SORTDESCENDING = DBConstants.SQLORDERDESCENDING;
    static String orderby = RULENAME_FULLCOLUMN + SORTASCENDING;
    static int orderfld = BYRULENAME;
    static boolean ordertype = true;
    @SuppressWarnings("unused")
    static boolean sortchanged = false;
    @SuppressWarnings("unused")
    private static String lastmessage = "";
    private String shopfilter = "";
    private String shoporderby = DBShopsTableConstants.SHOPS_NAME_COL_FULL;
    private String aislefilter = "";
    private String aisleorderby = DBAislesTableConstants.AISLES_NAME_COL_FULL;
    private String productfilter = "";
    private String productorderby = DBProductsTableConstants.PRODUCTS_NAME_COL_FULL;
    @SuppressWarnings("unused")
    private String stockfilter = "";
    @SuppressWarnings("unused")
    private String stockorderby = "";

    private String rpfilter = "";
    private String rpoderby = "";

    private long currentshopid = 0;
    private long currentaisleid = 0;
    private long currentproductid = 0;
    @SuppressWarnings("unused")
    private int currentperiod = 0;

    @SuppressWarnings("unused")
    private int currentshopcount;
    @SuppressWarnings("unused")
    private int currentaislecount;
    private int currentproductcount;

    private Date currentdate = new Date(System.currentTimeMillis());
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat sdf = new SimpleDateFormat(StandardAppConstants.EXTENDED_DATE_FORMAT);
    private Date oldate;

    private boolean unabletomakerule = false;

    TextView messagebar;
    TextView donebutton;
    TextView savebutton;
    @SuppressWarnings("unused")
    LinearLayout shopspinner_linearlayout;
    TextView shopnamelabel;
    Spinner shopspinner;
    @SuppressWarnings("unused")
    LinearLayout aislespinner_linearlayout;
    TextView aislenamelabel;
    Spinner aislespinner;
    @SuppressWarnings("unused")
    LinearLayout productspinner_linearlayout;
    TextView productnamelabel;
    Spinner productspinner;
    @SuppressWarnings("unused")
    LinearLayout productfilter_linearlayout;
    TextView productfilterlabel;
    EditText productfilter_input;
    TextView rulenamelabel;
    EditText rulename_input;
    TextView numbertogetlabel;
    EditText numbertoget_input;
    TextView rulepromptlabel;
    CheckBox ruleprompt_input;
    TextView ruledatelabel;
    TextView ruledate_input;
    EditText textruledate_input;
    TextView ruleperiodlabel;
    Spinner ruleperiod_input;
    TextView rulemultiplierlabel;
    EditText rulemultiplier_input;
    AdapterShopList shoplistadapter;
    AdapterAisleList aislelistadapter;
    AdapterProductList productlistadapter;
    @SuppressWarnings("unused")
    AdapterRulePeriodList ruleperiodlistadapter;


    @SuppressLint("SetTextI18n")
    protected void onCreate(Bundle savedInstanceState) {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruleasddedit);
        context = this;
        thisactivity = (Activity) context;
        logmsg = "Retrieving IntentExtras";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        caller = getIntent().getStringExtra(
                StandardAppConstants.INTENTKEY_CALLINGACTIVITY
        );
        calledmode = getIntent().getIntExtra(
                StandardAppConstants.INTENTKEY_CALLINGMODE, 0
        );

        logmsg = "Preparing ColorCoding";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        menucolorcode = StandardAppConstants.INTENTKEY_MENUCOLORCODE;
        passedmenucolorcode = getIntent().getIntExtra(menucolorcode, 0);

        messagebar = (TextView) findViewById(R.id.rulesaddedit_messagebar);
        donebutton = (TextView) findViewById(R.id.rulesaddedit_donebutton);
        savebutton = (TextView) findViewById(R.id.rulesaddedit_savebutton);

        shopspinner_linearlayout = (LinearLayout) findViewById(
                R.id.inputstockshop_linearlayout
        );
        shopnamelabel = (TextView) findViewById(R.id.inputstockshop_label);
        shopspinner = (Spinner) findViewById(R.id.selectstockshop);

        aislespinner_linearlayout = (LinearLayout) findViewById(
                R.id.inputstockaisle_linearlayout
        );
        aislenamelabel = (TextView) findViewById(R.id.inputstockaisle_label);
        aislespinner = (Spinner) findViewById(R.id.selectstockaisle);

        productspinner_linearlayout = (LinearLayout) findViewById(
                R.id.inputstockproduct_linearlayout
        );
        productnamelabel = (TextView) findViewById(R.id.inputstockproduct_label);
        productspinner = (Spinner) findViewById(R.id.selectstockproduct);
        productfilter_linearlayout = (LinearLayout) findViewById(
                R.id.stock_productfilter_linearlayout
        );
        productfilterlabel = (TextView) findViewById(R.id.products_inputfilterlabel);
        productfilter_input = (EditText) findViewById(R.id.products_inputfilter);
        rulenamelabel = (TextView) findViewById(R.id.inputrulename_label);
        rulename_input = (EditText) findViewById(R.id.inputrulename);
        numbertogetlabel = (TextView) findViewById(R.id.inputrulenumbertoget_label);
        numbertoget_input = (EditText) findViewById(R.id.inputrulenumbertoget);
        rulepromptlabel = (TextView) findViewById(R.id.inputprompt_label);
        ruleprompt_input = (CheckBox) findViewById(R.id.inputruleprompt);
        ruledatelabel = (TextView) findViewById(R.id.inputruledatelabel);
        ruledate_input = (TextView) findViewById(R.id.inputruledate);
        textruledate_input = (EditText) findViewById(R.id.inputtextruledate);
        ruleperiodlabel = (TextView) findViewById(R.id.selectruleperiodlabel);
        ruleperiod_input = (Spinner) findViewById(R.id.selectruleperiod);
        rulemultiplierlabel = (TextView) findViewById(R.id.inputrulemultiplierlabel);
        rulemultiplier_input = (EditText) findViewById(R.id.inputrulemultiplier);
        actionbar = getSupportActionBar();
        ActionColorCoding.setActionBarColor(this, getIntent(), actionbar);
        primary_color = ActionColorCoding.setHeadingColor(this, getIntent(), 0);
        h1 = ActionColorCoding.setHeadingColor(this, getIntent(), 1);
        h2 = ActionColorCoding.setHeadingColor(this, getIntent(), 2);
        h3 = ActionColorCoding.setHeadingColor(this, getIntent(), 3);
        h4 = ActionColorCoding.setHeadingColor(this, getIntent(), 4);
        ActionColorCoding.setActionButtonColor(donebutton, primary_color);
        ActionColorCoding.setActionButtonColor(savebutton, primary_color);
        shopnamelabel.setTextColor(primary_color);
        ActionColorCoding.setActionButtonColor(shopspinner, h2);
        aislenamelabel.setTextColor(primary_color);
        ActionColorCoding.setActionButtonColor(aislespinner, h2);
        productnamelabel.setTextColor(primary_color);
        ActionColorCoding.setActionButtonColor(productspinner, h2);
        productfilterlabel.setTextColor(h2);
        ActionColorCoding.setActionButtonColor(productfilter_input,
                h2 & ActionColorCoding.transparency_optional);
        rulenamelabel.setTextColor(primary_color);
        ActionColorCoding.setActionButtonColor(rulename_input,
                h2 & ActionColorCoding.transparency_requied);
        numbertogetlabel.setTextColor(primary_color);
        ActionColorCoding.setActionButtonColor(numbertoget_input,
                h2 & ActionColorCoding.transparency_requied);
        rulepromptlabel.setTextColor(primary_color);
        ActionColorCoding.setActionButtonColor(ruleprompt_input,
                h2 & ActionColorCoding.transparency_optional);
        ActionColorCoding.setCheckBoxAccent(context, getIntent(), ruleprompt_input);
        ruledatelabel.setTextColor(primary_color);
        ActionColorCoding.setActionButtonColor(ruledate_input, primary_color);
        ActionColorCoding.setActionButtonColor(textruledate_input,h2 &
                ActionColorCoding.transparency_optional);
        ruleperiodlabel.setTextColor(primary_color);
        ActionColorCoding.setActionButtonColor(ruleperiod_input,
                h2 & ActionColorCoding.transparency_requied);
        rulemultiplierlabel.setTextColor(primary_color);
        ActionColorCoding.setActionButtonColor(rulemultiplier_input,
                h2 & ActionColorCoding.transparency_requied);
        ActionColorCoding.setActionButtonColor(ruleperiod_input, h2);

        logmsg = "Preparing Database";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        dbdao = new DBDAO(this);
        dbshopmethods = new DBShopMethods(this);
        dbaislemethods = new DBAisleMethods(this);
        dbproductmethods = new DBProductMethods(this);
        dbpumethods = new DBProductUsageMethods(this);
        dbshoplistmethods = new DBShopListMethods(this);
        dbrulemethods = new DBRuleMethods(this);
        dbappvaluesmethods = new DBAppvaluesMethods(this);

        logmsg = "Handling Mode (ADD or EDIT)";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        switch (calledmode) {
            case StandardAppConstants.CM_ADD:
                numbertoget_input.setText("1");
                ruledate_input.setText(sdf.format(currentdate));
                rulemultiplier_input.setText("1");
                textruledate_input.setText("");
                //String testRSCAname = RuleSuggestCheckActivity.class.getSimpleName();
                //boolean textRSCAcompare = caller.equals(testRSCAname);
                if (caller.equals(RuleSuggestCheckActivity.class.getSimpleName())) {
                    currentaisleid = getIntent().getLongExtra(
                            StandardAppConstants.INTENTKEY_AISLEID,0);
                    currentproductid = getIntent().getLongExtra(
                            StandardAppConstants.INTENTKEY_PRODUCTID,0
                    );
                    currentshopid = getIntent().getLongExtra(
                            StandardAppConstants.INTENTKEY_SHOPID,0
                    );
                    rulename_input.setText(
                            getIntent().getStringExtra(
                                    StandardAppConstants.INTENTKEY_RULENAME
                            )
                    );
                    numbertoget_input.setText(
                            Integer.toString(
                                    getIntent().getIntExtra(
                                            StandardAppConstants.INTENTKEY_RULEUSES,1
                                    )
                            )
                    );
                    ruleperiod_input.setSelection(
                            getIntent().getIntExtra(
                                    StandardAppConstants.INTENTKEY_RULEPERIOD,0
                            )
                    );
                    rulemultiplier_input.setText(
                            Integer.toString(
                                    getIntent().getIntExtra(
                                            StandardAppConstants.INTENTKEY_RULEMULTIPLIER,1
                                    )
                            )
                    );
                }
                break;
            case StandardAppConstants.CM_EDIT:
                currentaisleid = getIntent().getLongExtra(
                        StandardAppConstants.INTENTKEY_RULEAISLEID,0);
                currentproductid = getIntent().getLongExtra(
                        StandardAppConstants.INTENTKEY_RULEPRODUCTREF,0);
                currentshopid = dbaislemethods.getOwningShop(currentaisleid);
                rulename_input.setText(
                        getIntent().getStringExtra(
                                StandardAppConstants.INTENTKEY_RULENAME));
                numbertoget_input.setText(
                        Integer.toString(
                        getIntent().getIntExtra(
                                StandardAppConstants.INTENTKEY_RULEUSES,1)));
                int prompt_int = getIntent().getIntExtra(
                        StandardAppConstants.INTENTKEY_RULEPROMPT,0);
                boolean prompt = prompt_int > 0;
                ruleprompt_input.setChecked(prompt);
                //sdf = new SimpleDateFormat(StandardAppConstants.EXTENDED_DATE_FORMAT);
                @SuppressLint("SimpleDateFormat")
                String datestr = new SimpleDateFormat(
                        StandardAppConstants.EXTENDED_DATE_FORMAT).format(new Date(
                        getIntent().getLongExtra(
                                StandardAppConstants.INTENTKEY_RULEACTON,
                                currentdate.getTime()
                        )));
                ruledate_input.setText(datestr);
                rulemultiplier_input.setText(
                        Integer.toString(
                        getIntent().getIntExtra(
                                StandardAppConstants.INTENTKEY_RULEMULTIPLIER,1)));

                break;
            default:
                break;
        }

        logmsg = "Preparing SelectionSpinners";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        slcsr = dbshopmethods.getShopsWithAisles(shopfilter, shoporderby);
        currentshopcount = slcsr.getCount();
        alcsr = dbaislemethods.getAisles(aislefilter, aisleorderby, false);
        plcsr = dbproductmethods.getProductsInAisle(
                currentaisleid, productfilter, productorderby);
        rpfilter = DBAppvaluesTableConstants.APPVALUES_NAME_COL_FULL +
                " = '" + StandardAppConstants.RULEPERIODS_APPVALKEY +
                "' ";
        rpcsr = dbappvaluesmethods.getAppvalues(rpfilter, rpoderby);

        shoplistadapter = setupShopSelectSpinner(currentshopid);
        aislelistadapter = setupAisleSelectSpinner(currentaisleid);
        productlistadapter = setupProductSelectSpinner(currentproductid);
        addProductFilterListener();
        ruleperiodlistadapter = setupRulePeriodSpinner();
        setupTextRuleDateInput();
        String callingactivity = RuleSuggestCheckActivity.class.getSimpleName();

        if (calledmode == StandardAppConstants.CM_EDIT ||
                (calledmode == StandardAppConstants.CM_ADD && caller.equals(
                        RuleSuggestCheckActivity.class.getSimpleName()
                ))) {
            shopspinner.setEnabled(false);
            aislespinner.setEnabled(false);
            productspinner.setEnabled(false);
            productfilter_input.setEnabled(false);
            ruleperiod_input.setSelection(getIntent().getIntExtra(
                    StandardAppConstants.INTENTKEY_RULEPERIOD,0
            ));
        }
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
        logmsg = "Refreshing Display";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
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
        slcsr.close();
        alcsr.close();
        plcsr.close();
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
            case R.id.rulesaddedit_donebutton:
                logmsg = "Finishing";
                LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
                this.finish();
                break;
            case R.id.rulesaddedit_savebutton:
                saveRule();
                break;
            case R.id.inputruledate:
                rulesAddEditDatePick(ruledate_input);
            default:
                break;
        }
    }

    @SuppressLint("SimpleDateFormat")
    public void saveRule() {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        Emsg emsg;
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat(StandardAppConstants.STANDARD_DDMMYYY_FORMAT);
        Date d = new Date(0);
        long ruledate = 0;
        int period = 0;

        String newrulename = rulename_input.getText().toString();
        if (newrulename.length() < 1) {
            setMessage(this,"Cannot add Rule. No RuleName provided.",true);
            return;
        }

        String newruledate_str = textruledate_input.getText().toString();
        if (newruledate_str.length() > 0) {
            emsg = ValidateInput.validateDate(newruledate_str);
            if (emsg.getErrorIndicator()) {
                setMessage(this, "Cannot add Rule. " + emsg.getErrorMessage(), true);
                logmsg = "Cannot Add Rule due to issue with Date" +
                        "\n\tIssue=" + emsg.getErrorMessage();
                LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
                return;
            }
        } else {
            newruledate_str = ruledate_input.getText().toString();
            sdf = new SimpleDateFormat(StandardAppConstants.EXTENDED_DATE_FORMAT);
        }
        try {
            d = sdf.parse(newruledate_str);
        } catch (ParseException e) {
            e.printStackTrace();
            setMessage(this,"Cannot add Rule. Error converting Date to numeric.",true);
            return;
        }
        ruledate = d.getTime();
        logmsg = "Validated Date=" + sdf.format(d.getTime());
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);

        String numbertoget_str = numbertoget_input.getText().toString();
        emsg = ValidateInput.validateInteger(numbertoget_str);
        if (emsg.getErrorIndicator()) {
            setMessage(this,"Cannot add Rule. Invalid Get. " + emsg.getErrorMessage(),true);
            logmsg = "Cannot Add Rule due to issue with Get. " +
                    "\n\tIssue=" + emsg.getErrorMessage();
            LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
            return;
        }

        String newmultiplier_str = rulemultiplier_input.getText().toString();
        emsg = ValidateInput.validateInteger(newmultiplier_str);
        if (emsg.getErrorIndicator()) {
            setMessage(this,"Cannot add Rule. Invalid Multiplier. " + emsg.getErrorMessage(),true);
            logmsg = "Cannot Add Rule due to issue with Multiplier. " +
                    "\n\tIssue=" + emsg.getErrorMessage();
            LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
            return;
        }

        if (!dbpumethods.doesProductUsageExist(currentaisleid,currentproductid)) {
            setMessage(this,"Cannot add Rule. Stock not found", true);
            logmsg = "Cannot Add Rule due to Stock (product/aisle) not found. " +
                    " for AisleID=" + Long.toString(currentaisleid) +
                    " ProductID=" + Long.toString(currentproductid) +
                    "\n\tIssue=" + emsg.getErrorMessage();
            LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
            return;
        }

        logmsg = "Determining index for selected period.";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        Cursor csr = (Cursor) ruleperiod_input.getSelectedItem();
        csr.moveToPosition(ruleperiod_input.getSelectedItemPosition());
        String period_str = csr.getString(csr.getColumnIndex(DBAppvaluesTableConstants.APPVALUES_TEXT_COL));
        switch (period_str) {
            case DBRulesTableConstants.PERIOD_DAYS:
                period = DBRulesTableConstants.PERIOD_DAYSASINT;
                break;
            case DBRulesTableConstants.PERIOD_WEEKS:
                period = DBRulesTableConstants.PERIOD_WEEKSASINT;
                break;
            case DBRulesTableConstants.PERIOD_FORTNIGHTS:
                period = DBRulesTableConstants.PERIOD_FORTNIGHTSASINT;
                break;
            case DBRulesTableConstants.PERIOD_MONTHS:
                period = DBRulesTableConstants.PERIOD_MONTHSASINT;
                break;
            case DBRulesTableConstants.PERIOD_QUARTERS:
                period = DBRulesTableConstants.PERIOD_QUARTERSASINT;
                break;
            case DBRulesTableConstants.PERIOD_YEARS:
                period = DBRulesTableConstants.PERIOD_YEARSASINT;
                break;
            default:
                period = DBRulesTableConstants.PERIOD_DAYSASINT;
        }

        switch (calledmode) {
            case StandardAppConstants.CM_ADD:
                logmsg = "ADD Mode so inserting new Rule.";
                LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
                dbrulemethods.insertRule(currentproductid,currentaisleid,
                        newrulename,
                        Integer.parseInt(numbertoget_input.getText().toString()),
                        ruleprompt_input.isChecked(),
                        ruledate,
                        period,
                        Integer.parseInt(rulemultiplier_input.getText().toString()));
                setMessage(this," Rule " + newrulename + " added",false);
                break;
            case StandardAppConstants.CM_EDIT:
                logmsg = "EDIT Mode so updating existing Rule.";
                LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
                dbrulemethods.modifyRuleAllValues(
                        getIntent().getLongExtra(StandardAppConstants.INTENTKEY_RULEID,0),
                        newrulename,
                        Integer.parseInt(numbertoget_input.getText().toString()),
                        ruleprompt_input.isChecked(),
                        ruledate,
                        period,
                        Integer.parseInt(rulemultiplier_input.getText().toString())
                        );
                setMessage(this,"Rule " + newrulename + " updated.",false);
                break;
            default:
                logmsg = "Default Entered. This SHOULD NOT happen.";
                LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
                break;
        }
    }

    /**************************************************************************
     * @param id
     * @return
     */
    public AdapterShopList setupShopSelectSpinner(long id) {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        AdapterShopList rv = new AdapterShopList(this, slcsr,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER,
                getIntent(), true, false, false);
        shopspinner.setAdapter(rv);
        SpinnerMove.moveToColumn(shopspinner, id, slcsr, SHOPID_COLUMN, true);
        shopspinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view,
                                               int position, long rowid) {
                        currentshopid = rowid;
                        aislefilter = AISLESHOPREF_COLUMN + " = " +
                                Long.toString(currentshopid);
                        alcsr = dbaislemethods.getAisles(aislefilter, aisleorderby, false);
                        aislelistadapter.swapCursor(alcsr);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
        return rv;
    }

    /**************************************************************************
     * @param id
     * @return
     */
    public AdapterAisleList setupAisleSelectSpinner(long id) {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        AdapterAisleList rv = new AdapterAisleList(this, alcsr,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER,
                getIntent(), true, false, false);
        aislespinner.setAdapter(rv);
        if (id > 0) {
            SpinnerMove.moveToColumn(aislespinner, id, alcsr, AISLEID_COLUMN);
            long parentshopid = alcsr.getLong(
                    alcsr.getColumnIndex(
                            AISLESHOPREF_COLUMN
                    )
            );
            SpinnerMove.moveToColumn(shopspinner, parentshopid, slcsr,
                    SHOPID_COLUMN, true);
        }
        aislespinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView,
                                               View view,
                                               int position, long rowid) {
                        currentaisleid = rowid;
                        plcsr = dbproductmethods.getProductsInAisle(
                                currentaisleid, productfilter, productorderby
                        );
                        currentproductcount = plcsr.getCount();
                        if (currentproductcount < 1) {
                            warnUnableToMakeRule();
                        } else {
                            ableToMakeRule();
                        }
                        productlistadapter.swapCursor(plcsr);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                }
        );
        return rv;
    }

    /**************************************************************************
     * @param id
     * @return
     */
    public AdapterProductList setupProductSelectSpinner(long id) {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        AdapterProductList rv = new AdapterProductList(this, plcsr,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER,
                getIntent(), true, false, false);
        productspinner.setAdapter(rv);
        SpinnerMove.moveToColumn(productspinner, id, plcsr, PRODUCTID_COLUMN);
        productspinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view,
                                               int position, long rowid) {
                        currentproductid = rowid;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
        return rv;
    }

    /**************************************************************************
     *
     * @return
     */
    public AdapterRulePeriodList setupRulePeriodSpinner() {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        AdapterRulePeriodList rv = new AdapterRulePeriodList(this, rpcsr,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER,
                getIntent(), true);
        ruleperiod_input.setAdapter(rv);
        return rv;
    }

    /**************************************************************************
     *
     */
    public void addProductFilterListener() {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        productfilter_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                productfilter = DBProductsTableConstants.PRODUCTS_NAME_COL_FULL +
                        DBConstants.SQLLIKECHARSTART +
                        productfilter_input.getText().toString() +
                        DBConstants.SQLLIKECHAREND;
                plcsr = dbproductmethods.getProductsInAisle(currentaisleid,
                        productfilter, productorderby);
                currentproductcount = plcsr.getCount();
                productlistadapter.swapCursor(plcsr);
                if (currentproductcount < 1) {
                    warnUnableToMakeRule();
                } else {
                    ableToMakeRule();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    /**************************************************************************
     *
     */
    public void setupTextRuleDateInput() {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        textruledate_input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!view.hasFocus()) {
                    if (textruledate_input.getText().toString().length() > 0) {
                        Emsg emsg = ValidateInput.validateDate(
                                textruledate_input.getText().toString()
                        );
                        if (emsg.getErrorIndicator()) {
                            setMessage(emsg.getErrorMessage(),true);
                            textruledate_input.setText("");
                            //setTextRuleDateFocus();
                            //((EditText)textruledate_input).requestFocus();
                            //((EditText)textruledate_input).requestFocusFromTouch();
                            // if the above line is active then two focus indicators
                            // appear. One in the EditText, the other is where the fccus
                            // was moved to that triggered the onFocusChange.
                            // The EditText cannot then receive focus and thus cannot
                            // be editted.
                            // The following two lines as per an answer in
                            // http://stackoverflow.com/questions/14788512/how-to-force-focus-to-edit-text
                            // does not resolve the issue nor appear to change
                            //InputMethodManager imm = (InputMethodManager) getSystemService(context.INPUT_METHOD_SERVICE);
                            //imm.showSoftInput(textruledate_input,InputMethodManager.SHOW_IMPLICIT);
                        }
                    }
                }
            }
        });
    }

    /**************************************************************************
     *
     * @param message
     * @param flag
     */
    public void setMessage(String message,
                           @SuppressWarnings("SameParameterValue") boolean flag) {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        setMessage(this, message, flag);
    }

    @SuppressWarnings("unused")
    public void setTextRuleDateFocus() {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        if (!textruledate_input.hasFocus()) {

            try {
                wait(100);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //rulename_input.requestFocus();
            textruledate_input.requestFocus();
        }
    }

    /**************************************************************************
     * If unable to make a rule (no products) issue a message and disable
     * the save button by making it invisible
     */
    public void warnUnableToMakeRule() {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        setMessage(this, "No products - Unable to create Rule.", true);
        savebutton.setVisibility(View.INVISIBLE);
        unabletomakerule = true;
    }

    /**************************************************************************
     * if able to make a rule (there are products stocked in the shop/aisle)
     * then make the save button available if the last message was
     * unabletomakerule then also hide the message
     */
    public void ableToMakeRule() {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        if (unabletomakerule) {
            messagebar.setVisibility(View.INVISIBLE);
        }
        savebutton.setVisibility(View.VISIBLE);
        unabletomakerule = false;
    }

    /**************************************************************************
     * refresh the display i.e. the listview and the listview heading
     */
    public void refreshDisplay() {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        slcsr = dbshopmethods.getShops(shopfilter, shoporderby);
        currentshopcount = slcsr.getCount();
        shoplistadapter.swapCursor(slcsr);
        alcsr = dbaislemethods.getAisles(aislefilter, aisleorderby, false);
        currentaislecount = alcsr.getCount();
        aislelistadapter.swapCursor(alcsr);
        plcsr = dbproductmethods.getProductsInAisle(currentaisleid, productfilter, productorderby);
        currentproductcount = plcsr.getCount();
        productlistadapter.swapCursor(plcsr);
    }

    /**************************************************************************
     *
     * @param view
     */
    public void rulesAddEditDatePick(View view) {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);

        try {
            oldate = sdf.parse(ruledate_input.getText().toString());
        } catch (ParseException e) {
            e.getStackTrace();

        }
        Calendar oldcal = Calendar.getInstance();
        oldcal.setTime(oldate);

        //Setup OnDateSetListener to apply selected date
        DatePickerDialog.OnDateSetListener odsl = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                Calendar newcal = Calendar.getInstance();
                newcal.set(year, monthOfYear, dayOfMonth);
                ruledate_input.setText(sdf.format(newcal.getTime()));
                textruledate_input.setText("");
            }
        };

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        int nw = (width * 100) / 133;
        int nh = (height * 100) / 133;

        DatePickerDialog dpd = new DatePickerDialog(this, odsl, oldcal.get(Calendar.YEAR), oldcal.get(Calendar.MONTH), oldcal.get(Calendar.DAY_OF_MONTH));
        DatePicker dpk = dpd.getDatePicker();
        dpk.setBackgroundColor(Color.WHITE);
        int minh = dpk.getMinimumHeight();
        int minw = dpk.getMinimumWidth();
        dpk.setMinimumHeight(nh);
        dpk.setMinimumWidth(nw);
        dpk.setScaleX((float) 1.1);
        dpk.setScaleY((float) 1.1);
        dpd.setTitle("Rule Start Date");
        dpd.show();
    }

    /**************************************************************************
     * setMessage - set the Message regarding the lat action performed
     * Note as an action may have altered the database getDBcounts
     * is called and the title changed
     *
     * @param raea the sa
     * @param msg  The message to be displayed.
     * @param flag Message imnportant, if true Yellow text, esle green
     */
    public void setMessage(RulesAddEditActivity raea, String msg, boolean flag) {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);

        TextView messagebar = (TextView) raea.findViewById(
                R.id.rulesaddedit_messagebar);
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
     *
     * @param newcolumn   the DB column to sort by
     * @param neworderfld the column as an integer as per constants
     */
    @SuppressWarnings("unused")
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
    @SuppressWarnings({"EmptyMethod", "unused"})
    public void doNothing(RequestDialogParameters values) {
    }
}
