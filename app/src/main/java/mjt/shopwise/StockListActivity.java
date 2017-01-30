package mjt.shopwise;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * StockListActivity - i.e. the STOCK Option
 *
 *  Displays a list of all stock allowing stock to be added, edited or deleted.
 *
 *  Note! all of these options are avaiable from the stock option of Shops,
 *  Aisles and Products but the list of all stocked products can be easier than
 *  having to remeber the shop/aisle in which a product is stocked.
 */

public class StockListActivity extends AppCompatActivity {

    private static final String THIS_ACTIVITY = "StockListActivity";
    private static final String LOGTAG = "SW_SLA";
    private static String caller;
    private static int calledmode;
    private int resumestate = StandardAppConstants.RESUMSTATE_NORMAL;
    private Activity thisactivity;
    public static final String THISCLASS = StockListActivity.class.getSimpleName();

    //DBDAO dbdao;
    DBShopMethods dbshopmethods;
    DBAisleMethods dbaislemethods;
    DBProductMethods dbproductmethods;
    DBProductUsageMethods dbpumethods;
    DBShopListMethods dbshoplistmethods;
    DBRuleMethods dbrulemethods;

    Cursor stockedcursor;

    private static final String SHOPID_COLUMN = DBShopsTableConstants.SHOPS_ID_COL;
    private static final String SHOPNAME_COLUMN = DBShopsTableConstants.SHOPS_NAME_COL;
    private static final String SHOPCITY_COLUMN = DBShopsTableConstants.SHOPS_CITY_COL;
    private static final String SHOPORDER_COLUMN = DBShopsTableConstants.SHOPS_ORDER_COL;
    private static final String SHOPID_FULLCOLUMN = DBShopsTableConstants.SHOPS_ID_COL_FULL;
    private static final String SHOPNAME_FULLCOLUMN = DBShopsTableConstants.SHOPS_NAME_COL_FULL;
    private static final String SHOPCITY_FULLCOLUMN = DBShopsTableConstants.SHOPS_CITY_COL_FULL;
    private static final String SHOPORDER_FULLCOLUMN = DBShopsTableConstants.SHOPS_ORDER_COL_FULL;

    private static final String AISLEID_COLUMN = DBAislesTableConstants.AISLES_ID_COL;
    private static final String AILSEID_FULLCOLUMN = DBAislesTableConstants.AISLES_ID_COL_FULL;
    private static final String AISLENAME_COLUMN = DBAislesTableConstants.AISLES_NAME_COL;
    private static final String AISLENAME_FULLCOLUMN = DBAislesTableConstants.AISLES_NAME_COL_FULL;
    private static final String AISLEORDER_COLUMN = DBAislesTableConstants.AISLES_ORDER_COL;
    private static final String AISLEORDER_FULLCOLUMN = DBAislesTableConstants.AISLES_ORDER_COL_FULL;
    private static final String AISLESHOPREF_COLUMN = DBAislesTableConstants.AISLES_SHOPREF_COL;
    private static final String AISLESHOPREF_FULLCOLUMN = DBAislesTableConstants.AISLES_SHOPREF_COL_FULL;

    private static final String PRODUCTID_COLUMN = DBProductsTableConstants.PRODUCTS_ID_COL;
    private static final String PRODUCTNAME_COLUMN = DBProductsTableConstants.PRODUCTS_NAME_COL;
    private static final String PRODUCTID_FULLCOLUMN = DBProductsTableConstants.PRODUCTS_ID_COL_FULL;
    private static final String PRODUCTNAME_FULLCOLUMN = DBProductsTableConstants.PRODUCTS_NAME_COL_FULL;

    private static final String PRODUCTREF_COLUMN = DBProductusageTableConstants.PRODUCTUSAGE_PRODUCTREF_COL;
    private static final String PRODUCTREF_FULLCOLUMN = DBProductusageTableConstants.PRODUCTUSAGE_PRODUCTREF_COL_FULL;
    private static final String AISLEREF_COLUMN = DBProductusageTableConstants.PRODUCTUSAGE_AISLEREF_COL;
    private static final String AISLEREF_FULLCOLUMN = DBProductusageTableConstants.PRODUCTUSAGE_AISLEREF_COL_FULL;
    private static final String COST_COLUMN = DBProductusageTableConstants.PRODUCTUSAGE_COST_COL;
    private static final String COST_FULLCOLUMN = DBProductusageTableConstants.PRODUCTUSAGE_COST_FULL;
    private static final String BUYCOUNT_COLUMN = DBProductusageTableConstants.PRODUCTUSAGE_BUYCOUNT_COL;
    private static final String BUYCOUNT_FULLCOLUMN = DBProductusageTableConstants.PRODUCTUSAGE_BUYCOUNT_COL_FULL;
    private static final String FIRSTBUY_COLUMN = DBProductusageTableConstants.PRODUCTUSAGE_FIRSTBUYDATE_COL;
    private static final String FIRSTBUY_FULLCOLUMN = DBProductusageTableConstants.PRODUCTUSAGE_FIRSTBUYDATE_COL_FULL;
    private static final String LASTBUY_COLUMN = DBProductusageTableConstants.PRODUCTUSAGE_LATESTBUYDATE_COL;
    private static final String LASTBUY_FULLCOLUMN = DBProductusageTableConstants.PRODUCTUSAGE_LATESTBUYDATE_COL_FULL;
    private static final String PRODUCTUSAGEORDER_COLUMN = DBProductusageTableConstants.PRODUCTUSAGE_ORDER_COL;
    private static final String PRODUCTUSAGEORDER_FULLCOLUMN = DBProductusageTableConstants.PRODUCTUSAGE_ORDER_COL_FULL;
    private static final String CHECKLISTFLAG_COLUMN = DBProductusageTableConstants.PRODUCTUSAGE_CHECKLISTFLAG_COL;
    private static final String CHECKLISTFLAG_FULLCOLUMN = DBProductusageTableConstants.PRODUCTUSAGE_CHECKLISTFLAG_COL_FULL;
    private static final String CHECKLISTCOUNT_COLUMN = DBProductusageTableConstants.PRODUCTUSAGE_CHECKLISTCOUNT_COL;
    private static final String CHECKLISTCOUNT_FULLCOLUMN = DBProductusageTableConstants.PRODUCTUSAGE_CHECKLISTCOUNT_COL_FULL;

    /**
     * Sorting Productlist columns
     */
    private static final int BYPRODUCT = 0;
    private static final int BYCOST = 1;
    private static final int BYORDER = 2;
    private static final int BYSHOPNAME = 3;
    private static final int BYAISLENAME = 4;
    private static final String SORTASCENDING = DBConstants.SQLORDERASCENDING;
    private static final String SORTDESCENDING = DBConstants.SQLORDERDESCENDING;
    static String orderby = PRODUCTNAME_FULLCOLUMN + SORTASCENDING;
    static int orderfld = BYPRODUCT;
    static boolean ordertype = true;
    static boolean sortchanged = false;
    static String lastmessage = "";
    static String currentproductname = "";
    static String productfilter = "";
    static String productorderby = PRODUCTNAME_FULLCOLUMN;
    static String shopfilter = "";
    static String shoporderby = SHOPNAME_FULLCOLUMN;
    static String aislefilter = "";
    static String aisleorderby = AISLENAME_COLUMN;
    static String stockfilter = "";
    static String stockorderby = PRODUCTNAME_FULLCOLUMN;
    static int shopcount = 0;
    static int aislecount = 0;
    static int productcount = 0;

    Context context;
    ActionBar actionbar;

    /**
     * Colours
     */
    private static int h1;
    private static int h2;
    private static int h3;
    private static int h4;
    private static int primary_color;
    private String menucolorcode;
    int passedmenucolorcode;

    AdapterStockListList stocklistadapter;

    TextView messagebar;
    TextView donebutton;
    TextView addbutton;
    LinearLayout stocklistheading;
    TextView inputproductfilterlabel;
    EditText inputproductfilter;
    ListView stocklist;

    String stocklabel;


    protected void onCreate(Bundle savedInstanceState) {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        Log.i(LOGTAG, "OnCreate method entered.");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stocklist);
        context = this;
        thisactivity = (Activity) context;
        logmsg = "Retrieving IntentExtras";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        caller = getIntent().getStringExtra(
                StandardAppConstants.INTENTKEY_CALLINGACTIVITY);
        calledmode = getIntent().getIntExtra(
                StandardAppConstants.INTENTKEY_CALLINGMODE,0);
        menucolorcode = StandardAppConstants.INTENTKEY_MENUCOLORCODE;
        passedmenucolorcode = getIntent().getIntExtra(menucolorcode,0);

        logmsg = "Preparing Color Coding";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        stockfilter = "";

        messagebar = (TextView) findViewById(R.id.stocklist_messagebar);
        donebutton = (TextView) findViewById(R.id.stocklist_donebutton);
        addbutton = (TextView) findViewById(R.id.stocklist_addbutton);

        stocklabel = getResources().getString(R.string.stocklabel);
        inputproductfilterlabel = (TextView) findViewById(R.id.products_inputfilterlabel);
        inputproductfilter = (EditText) findViewById(R.id.products_inputfilter);
        stocklistheading = (LinearLayout) findViewById(R.id.stocklist_stocklist_heading);
        stocklist = (ListView) findViewById(R.id.stocklist_stocklist);


        actionbar = getSupportActionBar();
        ActionColorCoding.setActionBarColor(this,getIntent(),actionbar);
        primary_color = ActionColorCoding.setHeadingColor(this,getIntent(),0);
        h1 = ActionColorCoding.setHeadingColor(this,getIntent(),1);
        h2 = ActionColorCoding.setHeadingColor(this,getIntent(),2);
        h3 = ActionColorCoding.setHeadingColor(this,getIntent(),3);
        h4 = ActionColorCoding.setHeadingColor(this,getIntent(),4);
        ActionColorCoding.setActionButtonColor(donebutton,primary_color);
        ActionColorCoding.setActionButtonColor(addbutton,primary_color);
        ActionColorCoding.setActionButtonColor(inputproductfilter,
                h2 & ActionColorCoding.transparency_optional);
        stocklistheading.setBackgroundColor(h1);
        inputproductfilterlabel.setTextColor(h2);

        this.setTitle(getResources().getString(R.string.stocklabel));
        ActionColorCoding.setSwatches(findViewById(android.R.id.content), this.getIntent());


        //dbdao = new DBDAO(this);
        logmsg = "Preparing Database";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        dbshopmethods = new DBShopMethods(this);
        dbaislemethods = new DBAisleMethods(this);
        dbproductmethods = new DBProductMethods(this);
        dbpumethods = new DBProductUsageMethods(this);
        dbshoplistmethods = new DBShopListMethods(this);
        dbrulemethods = new DBRuleMethods(this);


        logmsg = "Retrieving StockList";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        stockedcursor = dbpumethods.getExpandedProductUsages(stockfilter,stockorderby);
        stocklistadapter = new AdapterStockListList(this,stockedcursor,0,getIntent(),false);
        stocklist.setAdapter(stocklistadapter);
        addProductFilterListener();

        logmsg = "Adding StockList OnItemClick Listener";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        stocklist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long rowid) {
                listItemClick(view, position, rowid);

            }
        });

        logmsg = "Adding StockList OnItemLongClick Listener";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        stocklist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long rowid) {
                listItemLongClick(view, position, rowid);
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
        setDBCounts();
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
        logmsg = "Refreshing StockList";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        resumestate = StandardAppConstants.RESUMSTATE_NORMAL;
        stockedcursor = dbpumethods.getExpandedProductUsages(stockfilter,stockorderby);
        stocklistadapter.swapCursor(stockedcursor);
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
    public void actionButtonClick(View view) {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        switch (view.getId()) {
            case R.id.stocklist_donebutton:
                logmsg = "Finishing";
                LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
                this.finish();
                break;
            case R.id.stocklist_addbutton:
                stockAdd();
            default:
                break;
        }
    }

    public void stockAdd() {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        Intent intent = new Intent(this,StockActivity.class);
        intent.putExtra(StandardAppConstants.INTENTKEY_CALLINGACTIVITY,THIS_ACTIVITY);
        intent.putExtra(StandardAppConstants.INTENTKEY_CALLINGMODE,
                StandardAppConstants.CM_STOCKFROMSTOCKLIST);
        intent.putExtra(menucolorcode,passedmenucolorcode);
        logmsg = "Starting " + StockActivity.class.getSimpleName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        startActivity(intent);
    }

    /**************************************************************************
     *
     * @param values
     */
    public void stockDelete(RequestDialogParameters values) {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        Activity activity = values.getPassedactivity();
        StockListActivity sla = (StockListActivity) activity;
        sla.dbpumethods.deleteStock(values.getLong1(),values.getLong2(), false);
        stockedcursor = dbpumethods.getExpandedProductUsages(stockfilter,stockorderby);
        stocklistadapter.swapCursor(stockedcursor);
        sla.setMessage(sla,"Stocked Product " +
                currentproductname +
                " Deleted from this Shop/Aisle.",
                false
        );
        logmsg = "Deleted STOCK item AisleID=" +
                Long.toString(values.getLong1()) +
                " ProductID=" + Long.toString(values.getLong2());
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
    }

    /**************************************************************************
     * sortClick - Handle list heading clicks (i.e. sort by that field)
     *
     * @param view the view that was clicked
     */
    public void sortClick(View view) {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        lastmessage = getResources().getString(R.string.productslabel) + " in Shop/Aisle sorted by ";
        switch (view.getId()) {
            case R.id.stocklist_stocklist_heading_productname:
                getOrderBy(PRODUCTNAME_FULLCOLUMN,BYPRODUCT);
                lastmessage = lastmessage + " PRODUCT NAME (";
                break;
            case R.id.stocklist_stocklist_heading_productcost:
                getOrderBy(COST_FULLCOLUMN,BYCOST);
                lastmessage = lastmessage + "  COST/PRICE (";
                break;
            case R.id.stocklist_stocklist_heading_shopname:
                getOrderBy(SHOPNAME_FULLCOLUMN,BYSHOPNAME);
                lastmessage = lastmessage + " SHOP NAME (";
                break;
            case R.id.stocklist_stocklist_heading_aislename:
                getOrderBy(AISLENAME_FULLCOLUMN,BYAISLENAME);
                lastmessage = lastmessage + " AISLE NAME (";
                break;
            case R.id.stocklist_stocklist_heading_productorder:
                getOrderBy(PRODUCTUSAGEORDER_FULLCOLUMN,BYORDER);
                lastmessage = lastmessage + " PRODUCT ORDER (";
                break;
            default:
                break;
        }
        if (sortchanged) {
            stockorderby = orderby;
            stockedcursor = dbpumethods.getExpandedProductUsages(stockfilter,stockorderby);
            stocklistadapter.swapCursor(stockedcursor);
            if (ordertype) {
                lastmessage = lastmessage + "ascending)";
            } else {
                lastmessage = lastmessage + "descending)";
            }
            setMessage(this,lastmessage,false);
        }
    }

    /**************************************************************************
     *  addProductFilterListener - Add a Listener for whenever anything is
     *                              typed in the filter field. This is then
     *                              used to filter the products that are
     *                              available in the product selection spinner
     */
    public void addProductFilterListener() {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        inputproductfilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                stockfilter = DBProductsTableConstants.PRODUCTS_NAME_COL_FULL +
                        DBConstants.SQLLIKECHARSTART +
                        inputproductfilter.getText().toString() +
                        DBConstants.SQLLIKECHAREND;
                stockedcursor = dbpumethods.getExpandedProductUsages(stockfilter,stockorderby);
                stocklistadapter.swapCursor(stockedcursor);
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    /**************************************************************************
     * listItemClickView - Handle normal click of an item in the List
     *                      Click allows editing of the item, however just in
     *                      case the use inadvertently clicked an item. An
     *                      Alertdialog is displayed allowing the user to
     *                      cancel or continue to edit.
     *
     *                      Edit performed by the stockListEdit activity which
     *                      is invoked by the editStockList method (called by
     *                      from the alertdialog's edit option i.e. the
     *                      positiveaction).
     *
     * @param view      view that was click on (stocked product)
     * @param position  position of the clicked item
     * @param id        id of the clikced row i.e. the _id column
     */
    public void listItemClick(View view, int position, long id) {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        long aisled = stockedcursor.getLong(
                stockedcursor.getColumnIndex(AISLEREF_COLUMN)
        );
        String aislename = stockedcursor.getString(
                stockedcursor.getColumnIndex(AISLENAME_COLUMN)
        );
        long productid = stockedcursor.getLong(
                stockedcursor.getColumnIndex(PRODUCTREF_COLUMN)
        );
        String productname = stockedcursor.getString(
                stockedcursor.getColumnIndex(PRODUCTNAME_COLUMN)
        );
        long shopid = stockedcursor.getLong(
                stockedcursor.getColumnIndex(AISLESHOPREF_COLUMN)
        );
        String shopname = stockedcursor.getString(
                stockedcursor.getColumnIndex(SHOPNAME_COLUMN)
        );
        double cost = stockedcursor.getDouble(
                stockedcursor.getColumnIndex(COST_COLUMN)
        );
        int order = stockedcursor.getInt(
                stockedcursor.getColumnIndex(PRODUCTUSAGEORDER_COLUMN)
        );
        boolean checklistflag = false;
        int t_chklistflag = stockedcursor.getInt(
                stockedcursor.getColumnIndex(CHECKLISTFLAG_COLUMN)
        );
        if (t_chklistflag > 0) {
            checklistflag = true;
        }
        int checklistcount = stockedcursor.getInt(
                stockedcursor.getColumnIndex(CHECKLISTCOUNT_COLUMN)
        );

        Class cls = null;
        String classname = this.getClass().getCanonicalName();
        String positivebuttontext = getResources().getString(R.string.editbutton);
        String positiveaction = "editStockList";
        String negativebuttontext = "";
        String negativeaction = "";
        String neutralbuttontext = getResources().getString(R.string.cancelbutton);
        String neutralactioon = "";
        String title = getResources().getString(R.string.editbutton) + " " +
                getResources().getString(R.string.productlabel) + " in " +
                productname +
                getResources().getString(R.string.aislelabel) + " " +
                aislename + " in " +
                getResources().getString(R.string.shoplabel) + " " +
                shopname;
        String message = getResources().getString(R.string.availableoptions) +
                getResources().getString(R.string.cancelbutton) +
                getResources().getString(R.string.and) +
                getResources().getString(R.string.editbutton) +
                getResources().getString(R.string.doublenewline) +
                getResources().getString(R.string.tab1) +
                getResources().getString(R.string.cancelbutton) +
                getResources().getString(R.string.canceldoes) +
                getResources().getString(R.string.editbutton) +
                " allows the stocked item to be edited.";
        MixTripleLongTripleInt values = new MixTripleLongTripleInt();
        values.setMIXTRPPLONGINT(aisled,productid,shopid,0,0,0);

        logmsg = "Presenting RequestDialog for Product=" + productname +
                " Shop=" + shopname +
                " City=" + stockedcursor.getString(
                stockedcursor.getColumnIndex(SHOPCITY_COLUMN)) +
                " Aisle=" + aislename;
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        new RequestDialog().requestDialog(thisactivity,
                classname,
                title,
                message,
                positivebuttontext,negativebuttontext,neutralbuttontext,
                positiveaction,negativeaction,neutralactioon,
                values);
    }

    /**************************************************************************
     * listItemLongClick - handle Long Clicking and item (delete item)
     * @param view      the invoking view
     * @param position  the position of the item in the list
     * @param rowid     the cursor's rowdid value
     */
    public void listItemLongClick(View view, int position, long rowid) {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        long aisleid = stockedcursor.getLong(stockedcursor.getColumnIndex(
                AISLEREF_COLUMN
        ));
        long productid = stockedcursor.getLong(stockedcursor.getColumnIndex(
                PRODUCTREF_COLUMN
        ));
        String aislename = stockedcursor.getString(stockedcursor.getColumnIndex(
                AISLENAME_COLUMN
        ));
        String productname = stockedcursor.getString(stockedcursor.getColumnIndex(
                PRODUCTNAME_COLUMN
        ));
        String shopname = stockedcursor.getString(stockedcursor.getColumnIndex(
                SHOPNAME_COLUMN
        ));

        Class cls = null;
        String classname = this.getClass().getCanonicalName();
        String positivebuttontext = getResources().getString(R.string.deletebutton);
        String positiveaction = "stockDelete";
        String negativebuttontext = "";
        String negativeaction = "";
        String neutralbuttontext = getResources().getString(R.string.cancelbutton);
        String neutralaction = "";
        String title = getResources().getString(R.string.deletebutton) + " " +
                getResources().getString(R.string.productlabel) + " " +
                productname + "  in " +
                getResources().getString(R.string.aislelabel) + " " +
                aislename + " in " +
                getResources().getString(R.string.shoplabel) + " " +
                shopname;
        String message = getResources().getString(R.string.availableoptions) +
                getResources().getString(R.string.cancelbutton) +
                getResources().getString(R.string.and) +
                getResources().getString(R.string.editbutton) +
                getResources().getString(R.string.doublenewline) +
                getResources().getString(R.string.tab1) +
                getResources().getString(R.string.cancelbutton) +
                getResources().getString(R.string.canceldoes) +
                getResources().getString(R.string.doublenewline) +
                getResources().getString(R.string.editbutton) +
                " allows the stocked item to be deleted." +
                getResources().getString(R.string.doublenewline);
        String tmsg = "";
        for (String msg: dbpumethods.stockDeleteImapct(aisleid,productid)) {
            tmsg = tmsg + msg + "\n";
        }
        message = message + tmsg;
        MixTripleLongTripleInt values = new MixTripleLongTripleInt();
        values.setMIXTRPPLONGINT(aisleid,productid,0,0,0,0);

        logmsg = "Presenting RequestDialog for Product=" + productname +
                " Shop=" + shopname +
                " City=" + stockedcursor.getString(
                stockedcursor.getColumnIndex(SHOPCITY_COLUMN)) +
                " Aisle=" + aislename;
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        new RequestDialog().requestDialog(thisactivity,
                classname,
                title,
                message,
                positivebuttontext,negativebuttontext,neutralbuttontext,
                positiveaction,negativeaction,neutralaction,
                values);
    }

    /**
     * editStockList - Invoke StockListEditActivity to edit the stocked item
     *                  after setting the Intent and Intent Extras.
     *
     *                  Intent extras setup are :-
     *                  menucolorcode = colorcode passed to this activity
     *                  calling mode  = indicator to StockListEditActivity
     *                  that it was called to perform this type of processing
     *                  callingactivity = the name of this activity
     *                  shopid = the id of the shop
     *                  puasielref = the aisle id
     *                  puproductref = the product id
     *
     *                  Note keys are defined in StandardAppConstants
     *
     * @param values
     */
    public void editStockList(RequestDialogParameters values) {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        Intent intent = new Intent(this,StockLisEditActivity.class);
        intent.putExtra(menucolorcode,passedmenucolorcode);
        intent.putExtra(StandardAppConstants.INTENTKEY_CALLINGMODE,
                StandardAppConstants.CM_STOCKFROMSTOCKLIST);
        intent.putExtra(StandardAppConstants.INTENTKEY_CALLINGACTIVITY,
                THIS_ACTIVITY);
        intent.putExtra(StandardAppConstants.INTENTKEY_SHOPID,
                values.getLong3()
        );
        intent.putExtra(StandardAppConstants.INTENTKEY_PUAISLEREF,
                values.getLong1()
        );
        intent.putExtra(StandardAppConstants.INTENTKEY_PUPRODUCTREF,
                values.getLong2()
        );
        logmsg = "Starting " + StockLisEditActivity.class.getSimpleName() +
                " for ShopID=" + Long.toString(values.getLong3()) +
                " AisleID=" + Long.toString(values.getLong1()) +
                " ProductID=" + Long.toString(values.getLong2());
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        startActivity(intent);
    }

    /**************************************************************************
     * setMessage - set the Message regarding the lat action performed
     * Note as an action may have altered the database getDBcounts
     * is called and the title changed
     *
     * @param sla   the sa
     * @param msg  The message to be displayed.
     * @param flag Message imnportant, if true Yellow text, esle green
     */
    public void setMessage(StockListActivity sla, String msg, boolean flag) {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);

        TextView messagebar = (TextView) sla.findViewById(R.id.stocklist_messagebar);
        messagebar.setText(context.getResources().getString(
                R.string.messagebar_prefix_lastaction) + " " + msg);
        if (flag) {
            messagebar.setTextColor(Color.YELLOW);
        } else {
            messagebar.setTextColor(Color.GREEN);
        }
        messagebar.setVisibility(View.VISIBLE);
        //sla.actionbar.setTitle(getResources().getString(R.string.stocklabel));
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
     * setDBCounts - extract the row counts from the database for relevant
     *                  tables.
     */
    private void setDBCounts() {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        shopcount = dbshopmethods.getShopCount();
        aislecount = dbaislemethods.getAisleCount();
        productcount = dbproductmethods.getProductCount();
    }

    /**************************************************************************
     * Do nothing.
     *
     * @param values the values
     */
    public void doNothing(RequestDialogParameters values) {
    }
}
