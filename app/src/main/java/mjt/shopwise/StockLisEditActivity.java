package mjt.shopwise;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import static mjt.sqlwords.SQLKWORD.*;

/**
 * StockList Add or Edit a Stocked Item from the StockList (note alternative)
 */

@SuppressWarnings({"FieldCanBeLocal", "WeakerAccess", "CanBeFinal", "unused"})
public class StockLisEditActivity extends AppCompatActivity {

    @SuppressWarnings("unused")
    private static final String THIS_ACTIVITY = "StockListEditActivity";
    private static final String LOGTAG = "SW_SLEA";
    @SuppressWarnings("unused")
    private static String caller;
    @SuppressWarnings("unused")
    private static int calledmode;
    private int resumestate = StandardAppConstants.RESUMSTATE_NORMAL;
    @SuppressWarnings("unused")
    private Activity thisactivity;
    public static final String THISCLASS = StockLisEditActivity.class.getSimpleName();

    //DBDAO dbdao;
    @SuppressWarnings("unused")
    DBShopMethods dbshopmethods;
    @SuppressWarnings("unused")
    DBAisleMethods dbaislemethods;
    DBProductMethods dbproductmethods;
    DBProductUsageMethods dbpumethods;
    @SuppressWarnings("unused")
    DBShopListMethods dbshoplistmethods;
    @SuppressWarnings("unused")
    DBRuleMethods dbrulemethods;

    Cursor stockedcursor;

    @SuppressWarnings("unused")
    private static final String SHOPID_COLUMN = DBShopsTableConstants.SHOPS_ID_COL;
    private static final String SHOPNAME_COLUMN = DBShopsTableConstants.SHOPS_NAME_COL;
    @SuppressWarnings("unused")
    private static final String SHOPCITY_COLUMN = DBShopsTableConstants.SHOPS_CITY_COL;
    @SuppressWarnings("unused")
    private static final String SHOPORDER_COLUMN = DBShopsTableConstants.SHOPS_ORDER_COL;
    @SuppressWarnings("unused")
    private static final String SHOPID_FULLCOLUMN = DBShopsTableConstants.SHOPS_ID_COL_FULL;
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

    private static final String PRODUCTREF_COLUMN = DBProductusageTableConstants.PRODUCTUSAGE_PRODUCTREF_COL;
    @SuppressWarnings("unused")
    private static final String PRODUCTREF_FULLCOLUMN = DBProductusageTableConstants.PRODUCTUSAGE_PRODUCTREF_COL_FULL;
    private static final String AISLEREF_COLUMN = DBProductusageTableConstants.PRODUCTUSAGE_AISLEREF_COL;
    @SuppressWarnings("unused")
    private static final String AISLEREF_FULLCOLUMN = DBProductusageTableConstants.PRODUCTUSAGE_AISLEREF_COL_FULL;
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
    private static final String PRODUCTUSAGEORDER_COLUMN = DBProductusageTableConstants.PRODUCTUSAGE_ORDER_COL;
    @SuppressWarnings("unused")
    private static final String PRODUCTUSAGEORDER_FULLCOLUMN = DBProductusageTableConstants.PRODUCTUSAGE_ORDER_COL_FULL;
    private static final String CHECKLISTFLAG_COLUMN = DBProductusageTableConstants.PRODUCTUSAGE_CHECKLISTFLAG_COL;
    @SuppressWarnings("unused")
    private static final String CHECKLISTFLAG_FULLCOLUMN = DBProductusageTableConstants.PRODUCTUSAGE_CHECKLISTFLAG_COL_FULL;
    private static final String CHECKLISTCOUNT_COLUMN = DBProductusageTableConstants.PRODUCTUSAGE_CHECKLISTCOUNT_COL;
    @SuppressWarnings("unused")
    private static final String CHECKLISTCOUNT_FULLCOLUMN = DBProductusageTableConstants.PRODUCTUSAGE_CHECKLISTCOUNT_COL_FULL;

    /**
     * Sorting Productlist columns
     */
    private static final int BYPRODUCT = 0;
    @SuppressWarnings("unused")
    private static final int BYCOST = 1;
    @SuppressWarnings("unused")
    private static final int BYORDER = 2;
    @SuppressWarnings("unused")
    private static final int BYSHOPNAME = 3;
    @SuppressWarnings("unused")
    private static final int BYAISLENAME = 4;
    private static final String SORTASCENDING = SQLORDERASCENDING;
    @SuppressWarnings("unused")
    private static final String SORTDESCENDING = SQLORDERDESCENDING;
    @SuppressWarnings("unused")
    static String orderby = PRODUCTNAME_FULLCOLUMN + SORTASCENDING;
    @SuppressWarnings("unused")
    static int orderfld = BYPRODUCT;
    @SuppressWarnings("unused")
    static boolean ordertype = true;
    @SuppressWarnings("unused")
    static boolean sortchanged = false;
    @SuppressWarnings("unused")
    static String lastmessage = "";
    @SuppressWarnings("unused")
    static String currentproductname = "";
    @SuppressWarnings("unused")
    static String productfilter = "";
    @SuppressWarnings("unused")
    static String productorderby = PRODUCTNAME_FULLCOLUMN;
    @SuppressWarnings("unused")
    static String shopfilter = "";
    @SuppressWarnings("unused")
    static String shoporderby = SHOPNAME_FULLCOLUMN;
    @SuppressWarnings("unused")
    static String aislefilter = "";
    @SuppressWarnings("unused")
    static String aisleorderby = AISLENAME_COLUMN;
    static String stockfilter = "";
    static String stockorderby = PRODUCTNAME_FULLCOLUMN;
    @SuppressWarnings("unused")
    static int shopcount = 0;
    @SuppressWarnings("unused")
    static int aislecount = 0;
    @SuppressWarnings("unused")
    static int productcount = 0;


    /**
     * Colours
     */
    @SuppressWarnings("unused")
    private static int h1;
    private static int h2;
    @SuppressWarnings("unused")
    private static int h3;
    @SuppressWarnings("unused")
    private static int h4;
    private static int primary_color;
    private String menucolorcode;
    @SuppressWarnings("unused")
    int passedmenucolorcode;

    Context context;
    ActionBar actionbar;

    TextView messagebar;
    TextView donebutton;
    TextView savebutton;
    TextView shopnamelabel;
    TextView shopname;
    TextView aislenamelabel;
    TextView aislename;
    TextView productnamelabel;
    EditText productname;
    TextView inputstockcostlabel;
    EditText inputstockcost;
    TextView inputstockorderlabel;
    EditText inputstockorder;
    TextView inputchecklistflaglabel;
    CheckBox inputchecklistflag;
    TextView inputchecklistcountlabel;
    EditText inputchecklistcount;

    @SuppressWarnings("unused")
    long currentshopid = 0;
    long currentaisleid = 0;
    long currentproductid = 0;

    protected void onCreate(Bundle savedInstanceState) {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stocklistedit);
        context = this;
        thisactivity = (Activity) context;
        logmsg = "Retrieving IntentExtras";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        caller = getIntent().getStringExtra(
                StandardAppConstants.INTENTKEY_CALLINGACTIVITY);
        calledmode = getIntent().getIntExtra(
                StandardAppConstants.INTENTKEY_CALLINGMODE, 0);
        menucolorcode = StandardAppConstants.INTENTKEY_MENUCOLORCODE;
        passedmenucolorcode = getIntent().getIntExtra(menucolorcode, 0);

        currentshopid = getIntent().getLongExtra(
                StandardAppConstants.INTENTKEY_SHOPID,0);
        currentaisleid = getIntent().getLongExtra(
                StandardAppConstants.INTENTKEY_PUAISLEREF,0);
        currentproductid = getIntent().getLongExtra(
                StandardAppConstants.INTENTKEY_PUPRODUCTREF,0);


        logmsg = "Preparing Color Coding";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        actionbar = getSupportActionBar();
        //noinspection ConstantConditions,ConstantConditions
        actionbar.setTitle(actionbar.getTitle().toString() + " - " + THISCLASS);
        messagebar = (TextView) findViewById(R.id.stocklistedit_messagebar);
        donebutton = (TextView) findViewById(R.id.stocklistedit_donebutton);
        savebutton = (TextView) findViewById(R.id.stocklistedit_savebutton);
        shopnamelabel = (TextView) findViewById(R.id.stocklistlistedit_shopnamelabel);
        shopname = (TextView) findViewById(R.id.stocklistlistedit_shopname);
        aislenamelabel = (TextView) findViewById(R.id.stocklistlistedit_aisleamelabel);
        aislename = (TextView) findViewById(R.id.stocklistlistedit_aislename);
        productnamelabel = (TextView) findViewById(R.id.stocklistlistedit_productnamelabel);
        productname = (EditText) findViewById(R.id.stocklistlistedit_productname);
        inputstockcostlabel = (TextView) findViewById(R.id.stocklistlistedit_costlabel);
        inputstockcost = (EditText) findViewById(R.id.inputstockcost);
        inputstockorderlabel = (TextView) findViewById(R.id.inputstockorder_label);
        inputstockorder = (EditText) findViewById(R.id.inputstockorder);
        inputchecklistflaglabel = (TextView) findViewById(R.id.inputchecklistflag_label);
        inputchecklistflag = (CheckBox) findViewById(R.id.inputchecklistflag);
        inputchecklistcountlabel = (TextView) findViewById(R.id.inputchecklistcount_label);
        inputchecklistcount = (EditText) findViewById(R.id.inputchecklistcount);

        ActionColorCoding.setActionBarColor(this,getIntent(),actionbar);
        primary_color = ActionColorCoding.setHeadingColor(this,getIntent(),0);
        h1 = ActionColorCoding.setHeadingColor(this,getIntent(),1);
        h2 = ActionColorCoding.setHeadingColor(this,getIntent(),2);
        h3 = ActionColorCoding.setHeadingColor(this,getIntent(),3);
        h4 = ActionColorCoding.setHeadingColor(this,getIntent(),4);
        ActionColorCoding.setActionButtonColor(donebutton,primary_color);
        ActionColorCoding.setActionButtonColor(savebutton,primary_color);
        ActionColorCoding.setActionButtonColor(productname,
                h2 & ActionColorCoding.transparency_requied);
        ActionColorCoding.setActionButtonColor(inputstockcost,
                h2 & ActionColorCoding.transparency_requied);
        ActionColorCoding.setActionButtonColor(inputstockorder,
                h2 & ActionColorCoding.transparency_requied);
        ActionColorCoding.setActionButtonColor(inputchecklistcount,
                h2 & ActionColorCoding.transparency_optional);
        ActionColorCoding.setActionButtonColor(inputchecklistflag,
                h2 & ActionColorCoding.transparency_optional);
        ActionColorCoding.setCheckBoxAccent(this,getIntent(),inputchecklistflag);
        shopnamelabel.setTextColor(primary_color);
        shopname.setTextColor(Color.BLACK);
        aislenamelabel.setTextColor(primary_color);
        aislename.setTextColor(Color.BLACK);
        productnamelabel.setTextColor(primary_color);
        productname.setTextColor(Color.BLACK);
        inputstockcostlabel.setTextColor(primary_color);
        inputstockorderlabel.setTextColor(primary_color);
        inputchecklistflaglabel.setTextColor(primary_color);
        inputchecklistcountlabel.setTextColor(primary_color);

        logmsg = "Preparing Database";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        //dbdao = new DBDAO(this);
        dbshopmethods = new DBShopMethods(this);
        dbaislemethods = new DBAisleMethods(this);
        dbpumethods = new DBProductUsageMethods(this);
        dbproductmethods = new DBProductMethods(this);

        logmsg = "Populating Lists";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        populateFromDB();
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
        resumestate = StandardAppConstants.RESUMSTATE_NORMAL;
        logmsg = "Refreshing Lists";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        populateFromDB();
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
        stockedcursor.close();
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
            case R.id.stocklistedit_donebutton:
                logmsg = "Finishing";
                LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
                this.finish();
                break;
            case R.id.stocklistedit_savebutton:
                stockListEditSave();
                break;
            default:
                break;
        }
    }

    /**************************************************************************
     *
     */
    public void stockListEditSave() {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        Emsg emsg = new Emsg();
        double newcost;
        String newcost_str;
        int neworder;
        String neworder_str;
        int newchklistcount;
        String newchklistcount_str;

        newcost_str = inputstockcost.getText().toString();
        emsg = ValidateInput.validateMonetary(newcost_str);
        if (!emsg.getErrorIndicator()) {
            newcost = Double.parseDouble(newcost_str);
        } else {
            setMessage(this,emsg.getErrorMessage(),true);
            inputstockcost.requestFocus();
            logmsg = "Unable to Save as Price is Inavlid error=" +
                    emsg.getErrorMessage();
            LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
            return;
        }

        neworder_str = inputstockorder.getText().toString();
        emsg = ValidateInput.validateInteger(neworder_str);
        if (!emsg.getErrorIndicator()) {
            neworder = Integer.parseInt(neworder_str);
        } else {
            setMessage(this,emsg.getErrorMessage(),true);
            inputstockorder.requestFocus();
            logmsg = "Unable to Save as Order is Invalid error=" +
                    emsg.getErrorMessage();
            LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
            return;
        }
        newchklistcount_str = inputchecklistcount.getText().toString();
        emsg = ValidateInput.validateInteger(newchklistcount_str);
        if (!emsg.getErrorIndicator()) {
            newchklistcount = Integer.parseInt(newchklistcount_str);
        } else {
            setMessage(this, emsg.getErrorMessage(),true);
            inputchecklistcount.requestFocus();
            logmsg = "Unable to Save as Count is Invalid error=" +
                    emsg.getErrorMessage();
            LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
            return;
        }
        if ((productname.getText().toString()).length() < 1) {
            setMessage(this,getResources().getString(R.string.inputblank),true);
            productname.requestFocus();
            logmsg = "Unable to save as ProductName is blank";
            LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
            return;
        }
        dbpumethods.modifyProductUsage(currentproductid,currentaisleid,
                newcost,neworder,inputchecklistflag.isChecked(),newchklistcount);
        dbproductmethods.modifyProduct(currentproductid,
                productname.getText().toString(),"",0,0);
        setMessage(this,getResources().getString(R.string.editedok),false);
        logmsg = "Stock and underlying Product have been modifed. " +
                "Lists have been refreshed";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        populateFromDB();

    }

    /**************************************************************************
     *
     */
    public void populateFromDB() {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        stockfilter = AISLEREF_COLUMN + " = " + Long.toString(currentaisleid) +
                " AND " +
                PRODUCTREF_COLUMN + " = " + Long.toString(currentproductid);
        stockedcursor = dbpumethods.getExpandedProductUsages(stockfilter,stockorderby);
        if (stockedcursor.getCount() > 0) {
            stockedcursor.moveToFirst();
            shopname.setText(stockedcursor.getString(stockedcursor.getColumnIndex(
                    SHOPNAME_COLUMN
            )));
            aislename.setText(stockedcursor.getString(stockedcursor.getColumnIndex(
                    AISLENAME_COLUMN
            )));
            productname.setText(stockedcursor.getString(stockedcursor.getColumnIndex(
                    PRODUCTNAME_COLUMN
            )));
            inputstockcost.setText(stockedcursor.getString(stockedcursor.getColumnIndex(
                    COST_COLUMN
            )));
            inputstockorder.setText(stockedcursor.getString(stockedcursor.getColumnIndex(
                    PRODUCTUSAGEORDER_COLUMN
            )));
            int t_chklstflg = stockedcursor.getInt(stockedcursor.getColumnIndex(
                    CHECKLISTFLAG_COLUMN
            ));
            inputchecklistflag.setChecked((t_chklstflg > 0));
            inputchecklistcount.setText(stockedcursor.getString(stockedcursor.getColumnIndex(
                    CHECKLISTCOUNT_COLUMN
            )));
        }
    }

    /**************************************************************************
     * setMessage - set the Message regarding the lat action performed
     * Note as an action may have altered the database getDBcounts
     * is called and the title changed
     *
     * @param slea   the sa
     * @param msg  The message to be displayed.
     * @param flag Message imnportant, if true Yellow text, esle green
     */
    public void setMessage(StockLisEditActivity slea, String msg, boolean flag) {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);

        TextView messagebar = (TextView) slea.findViewById(R.id.stocklistedit_messagebar);
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
     * Do nothing.
     *
     * @param values the values
     */
    @SuppressWarnings({"EmptyMethod", "unused"})
    public void doNothing(RequestDialogParameters values) {
    }
}
