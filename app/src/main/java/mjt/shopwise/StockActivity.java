package mjt.shopwise;

import android.app.Activity;
import android.content.Context;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * StockActivity - Stock Aisles with products
 *
 */
public class StockActivity extends AppCompatActivity {

    private static final String THIS_ACTIVITY = "StockActivity";
    private static final String LOGTAG = "SW-SA";
    private static String caller;
    private static int calledmode;
    private int resumestate = StandardAppConstants.RESUMSTATE_NORMAL;
    private Activity thisactivity;
    private static int internalmode = StandardAppConstants.CM_CLEAR;
    private static final int EDITMODE = 1;
    private static final int ADDMODE = 0;
    private boolean editdisplayed = false;


    DBDAO dbdao;
    DBShopMethods dbshopmethods;
    DBAisleMethods dbaislemethods;
    DBProductMethods dbproductmethods;
    DBProductUsageMethods dbpumethods;
    DBShopListMethods dbshoplistmethods;
    DBRuleMethods dbrulemethods;

    Cursor slcsr;
    Cursor alcsr;
    Cursor plcsr;
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

    AdapterShopList shoplistadapter;
    AdapterAisleList aislelistadapter;
    AdapterProductList productlistadapter;
    AdapterStockList stocklistadapter;

    TextView messagebar;
    LinearLayout selectshop_linearlayout;
    TextView shoplabel;
    Spinner selectshop;
    LinearLayout selectaisle_linearlayout;
    TextView aislelabel;
    Spinner selectaisle;
    LinearLayout productfilter_linearlayout;
    TextView productfilterlabel;
    EditText inputproductfilter;
    LinearLayout selectproduct_linearlayout;
    TextView productlabel;
    Spinner selectproduct;
    TextView donebutton;
    TextView savebutton;
    LinearLayout stocklistheading_linearlayout;
    ListView stocklist;
    EditText inputstockcost;
    TextView inputstockcostlabel;
    EditText inputstockorder;
    TextView inputstockorderlabel;
    CheckBox inputchecklistflag;
    TextView inputchecklistflaglabel;
    EditText inputchecklistcount;
    TextView inputchecklistcountlabel;

    String stocklabel;

    long currentshopid = 0;
    long currentaisleid = 0;
    long currentproductid = 0;

    protected void onCreate(Bundle savedInstanceState) {
        Log.i(LOGTAG,"OnCreate method entered.");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);
        context = this;
        thisactivity = (Activity) context;
        caller = getIntent().getStringExtra(
                StandardAppConstants.INTENTKEY_CALLINGACTIVITY);
        calledmode = getIntent().getIntExtra(
                StandardAppConstants.INTENTKEY_CALLINGMODE,0);
        menucolorcode = StandardAppConstants.INTENTKEY_MENUCOLORCODE;
        passedmenucolorcode = getIntent().getIntExtra(menucolorcode,0);
        productfilter = "";
        stockfilter = "";

        /**
         * Get the UI views from their resource id's
         */
        messagebar = (TextView) findViewById(R.id.stock_messagebar);
        donebutton = (TextView) findViewById(R.id.stock_donebutton);
        savebutton = (TextView) findViewById(R.id.stock_savebutton);
        selectshop_linearlayout = (LinearLayout) findViewById(
                R.id.inputstockshop_linearlayout);
        shoplabel = (TextView) findViewById(R.id.inputstockshop_label);
        selectshop = (Spinner) findViewById(R.id.selectstockshop);
        selectaisle_linearlayout = (LinearLayout) findViewById(
                R.id.inputstockaisle_linearlayout);
        aislelabel = (TextView) findViewById(R.id.inputstockaisle_label);
        selectaisle = (Spinner) findViewById(R.id.selectstockaisle);
        selectproduct_linearlayout = (LinearLayout) findViewById(
                R.id.inputstockproduct_linearlayout);
        productlabel = (TextView) findViewById(R.id.inputproductname_label);
        selectproduct = (Spinner) findViewById(R.id.selectstockproduct);
        productfilter_linearlayout = (LinearLayout) findViewById(
                R.id.stock_productfilter_linearlayout);
        productfilterlabel = (TextView) findViewById(R.id.products_productfilterlabel);
        inputproductfilter = (EditText) findViewById(R.id.products_inputfilter);
        productlabel = (TextView) findViewById(R.id.inputstockproduct_label);
        stocklabel = getResources().getString(R.string.stocklabel);
        stocklistheading_linearlayout = (LinearLayout) findViewById(R.id.stock_stocklist_heading);
        inputstockcost = (EditText) findViewById(R.id.inputstockcost);
        inputstockcostlabel = (TextView) findViewById(R.id.inputstockcost_label);
        inputstockorder = (EditText) findViewById(R.id.inputstockorder);
        inputstockorderlabel = (TextView) findViewById(R.id.inputstockorder_label);
        inputchecklistflag = (CheckBox) findViewById(R.id.inputchecklistflag);
        inputchecklistflaglabel = (TextView) findViewById(R.id.inputchecklistflag_label);
        inputchecklistcount = (EditText) findViewById(R.id.inputchecklistcount);
        inputchecklistcountlabel = (TextView) findViewById(R.id.inputchecklistcount_label);
        stocklist = (ListView) findViewById(R.id.stock_stocklist);

        /**
         * Apply Color Coding
         *  i.e. the colors used will indicate the caller
         */
        actionbar = getSupportActionBar();
        ActionColorCoding.setActionBarColor(this,getIntent(),actionbar);
        primary_color = ActionColorCoding.setHeadingColor(this,getIntent(),0);
        h1 = ActionColorCoding.setHeadingColor(this,getIntent(),1);
        h2 = ActionColorCoding.setHeadingColor(this,getIntent(),2);
        h3 = ActionColorCoding.setHeadingColor(this,getIntent(),3);
        h4 = ActionColorCoding.setHeadingColor(this,getIntent(),4);
        ActionColorCoding.setActionButtonColor(donebutton, primary_color);
        ActionColorCoding.setActionButtonColor(savebutton, primary_color);
        stocklistheading_linearlayout.setBackgroundColor(h1);
        ActionColorCoding.setActionButtonColor(selectshop,h2);
        ActionColorCoding.setActionButtonColor(selectaisle,h2);
        ActionColorCoding.setActionButtonColor(selectproduct,h2);
        ActionColorCoding.setActionButtonColor(inputproductfilter,
                h2 & ActionColorCoding.transparency_optional);
        ActionColorCoding.setActionButtonColor(inputstockcost,
                h2 & ActionColorCoding.transparency_requied);
        ActionColorCoding.setActionButtonColor(inputstockorder,
                h2 & ActionColorCoding.transparency_requied);
        ActionColorCoding.setActionButtonColor(inputchecklistcount,
                h2 & ActionColorCoding.transparency_optional);
        ActionColorCoding.setActionButtonColor(inputchecklistflag,
                h2 & ActionColorCoding.transparency_optional);
        ActionColorCoding.setCheckBoxAccent(this,getIntent(),inputchecklistflag);
        shoplabel.setTextColor(primary_color);
        aislelabel.setTextColor(primary_color);
        productfilterlabel.setTextColor(h2);
        productlabel.setTextColor(primary_color);
        inputstockcostlabel.setTextColor(primary_color);
        inputstockorderlabel.setTextColor(primary_color);
        inputchecklistflaglabel.setTextColor(primary_color);
        inputchecklistcountlabel.setTextColor(primary_color);

        /**
         * Perpare to use the database and the underlying methods
         */
        dbdao = new DBDAO(this);
        dbshopmethods = new DBShopMethods(this);
        dbaislemethods = new DBAisleMethods(this);
        dbproductmethods = new DBProductMethods(this);
        dbpumethods = new DBProductUsageMethods(this);
        dbshoplistmethods = new DBShopListMethods(this);
        dbrulemethods = new DBRuleMethods(this);
        Log.i(LOGTAG,"Databases prepared.");

        /**
         * Get Intent extras according to called mode
         * i.e. :-
         *      if called from shops then we only know the shop
         *      if called from aisles then we know shop and aisle
         *      if called from products then we only know product
         *
         *      if called from shop or aisle then we can set the
         *      aisle filter so that only aisles relevant to the shop
         *      are selected.
         *
         */
        switch (calledmode) {
            case StandardAppConstants.CM_STOCKFROMSSHOP:
                currentshopid = getIntent().getLongExtra(StandardAppConstants.INTENTKEY_SHOPID,0);
                aislefilter = AISLESHOPREF_FULLCOLUMN + " = " + Long.toString(currentshopid);
                break;
            case StandardAppConstants.CM_STOCKFROMAISLE:
                currentaisleid = getIntent().getLongExtra(StandardAppConstants.INTENTKEY_AISLEID,0);
                currentshopid = getIntent().getLongExtra(StandardAppConstants.INTENTKEY_AISLESHOPREF,0);
                aislefilter = AISLESHOPREF_FULLCOLUMN + " = " + Long.toString(currentshopid);
                break;
            case StandardAppConstants.CM_STOCKFROMPRODUCT:
                currentproductid = getIntent().getLongExtra(StandardAppConstants.INTENTKEY_PRODUCTID,0);
                slcsr = dbshopmethods.getShopsWithAisles(shopfilter,shoporderby);
                slcsr.moveToFirst();
                currentshopid = slcsr.getLong(slcsr.getColumnIndex(SHOPID_COLUMN));
                aislefilter = AISLESHOPREF_FULLCOLUMN + " = " + Long.toString(currentshopid);
                alcsr = dbaislemethods.getAisles(aislefilter,aisleorderby);
                alcsr.moveToFirst();
                currentaisleid = alcsr.getLong(alcsr.getColumnIndex(AISLEID_COLUMN));
                break;
            default:
                break;
        }

        /**
         * Extract the  data for the spinners from the DB
         */
        slcsr = dbshopmethods.getShopsWithAisles(shopfilter,shoporderby);
        alcsr = dbaislemethods.getAisles(aislefilter,aisleorderby);
        plcsr = dbproductmethods.getProductsNotInAisle(currentaisleid,productfilter,productorderby);

        shoplistadapter = setupShopSelectSpinner(currentshopid);
        aislelistadapter = setupAisleSelectSpinner(currentaisleid);
        productlistadapter = setupProductSelectSpinner(currentproductid);
        addProductFilterListener();

        /**
         *  Prepare the list of productusages (stock) for the current aisle
         *  Note currentaisleid will be either exist (when called from aisles) or
         *  set when the Shop's spinner is populated
         */
        stockfilter = AISLEREF_FULLCOLUMN + " = " + currentaisleid;
        stockedcursor = dbpumethods.getExpandedProductUsages(stockfilter,stockorderby);
        stocklistadapter = new AdapterStockList(this,stockedcursor,0,this.getIntent(),false);
        stocklist.setAdapter(stocklistadapter);

        setNewInput(inputstockorder, inputstockcost, inputchecklistcount);


        stocklist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView,
                                    View view, int position, long id) {
                listItemClick(view, position, id);
            }
        });
        stocklist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView,
                                           View view, int position, long id) {
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
        this.setTitle(getResources().getString(R.string.stocklabel));
    }

    /**************************************************************************
     * onDestroy - do any clean up before th application is destroyed
     * e.g. close any open cursors and then close the database
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        slcsr.close();
        alcsr.close();
        plcsr.close();
        stockedcursor.close();
    }

    /**************************************************************************
     * Button Click Handler
     *
     * @param view The view (i.e the TextView that was clicked)
     */
    public void actionButtonClick(View view) {
        switch (view.getId()) {
            case R.id.stock_donebutton:
                this.finish();
                break;
            case R.id.stock_savebutton:
                stockProductinAisle((internalmode == EDITMODE));
                break;
            default:
                break;
        }
    }

    /**************************************************************************
     *
     * @param values
     */
    public void stockEdit(RequestDialogParameters values) {

        plcsr = dbproductmethods.getProducts(productfilter,productorderby);
        productlistadapter.swapCursor(plcsr);

        SpinnerMove.moveToColumn(selectproduct,values.getLong2(),plcsr,PRODUCTID_COLUMN,true);
        inputstockcost.setText(stockedcursor.getString(stockedcursor.getColumnIndex(COST_COLUMN)));
        inputstockorder.setText(stockedcursor.getString(stockedcursor.getColumnIndex(PRODUCTUSAGEORDER_COLUMN)));
        if (stockedcursor.getInt(stockedcursor.getColumnIndex(CHECKLISTFLAG_COLUMN)) > 0) {
            inputchecklistflag.setChecked(true);
        } else {
            inputchecklistflag.setChecked(false);
        }
        String t_clcount = stockedcursor.getString(stockedcursor.getColumnIndex(CHECKLISTCOUNT_COLUMN));
        if (t_clcount.length() < 1) {
            t_clcount = "1";
        }
        inputchecklistcount.setText(t_clcount);
        internalmode = EDITMODE;
        editdisplayed = false;
    }

    /**************************************************************************
     *
     * @param values
     */
    public void stockDelete(RequestDialogParameters values) {
        Activity activity = values.getPassedactivity();
        StockActivity sa = (StockActivity) activity;
        sa.dbpumethods.deleteStock(values.getLong1(),values.getLong2(),false);
        stockedcursor = dbpumethods.getExpandedProductUsages(stockfilter,stockorderby);
        stocklistadapter.swapCursor(stockedcursor);
        plcsr = dbproductmethods.getProductsNotInAisle(values.getLong1(),productfilter,productorderby);
        productlistadapter.swapCursor(plcsr);
        sa.setMessage(sa,"Stocked Product " +
                currentproductname +
                " Deleted from this ShopAisle.",
                false
        );
        internalmode = ADDMODE;
    }

    /**************************************************************************
     *
     */
    public void stockProductinAisle(boolean update) {
        Emsg emsg = new Emsg();
        double cost;
        int order;
        int chklstcnt = 1;

        // Get the Product id from the database cursor according to position of
        // the current selection in the Product spinner/dropdown
        int spos = plcsr.getPosition();
        int npos = selectproduct.getSelectedItemPosition();
        plcsr.moveToPosition(npos);
        long productref = plcsr.getLong(plcsr.getColumnIndex(
                PRODUCTID_COLUMN
        ));
        Log.i(LOGTAG,"Product to save, as per spinner is " + Long.toString(productref) + " - " + plcsr.getString(plcsr.getColumnIndex(PRODUCTNAME_COLUMN)));
        plcsr.moveToPosition(spos);

        // Get the Aisle id from the database cursor according to position of
        // the current selection in the Aisle spinner/dropdown
        // Note! Shop is implied via the Aisle
        spos = alcsr.getPosition();
        npos = selectaisle.getSelectedItemPosition();
        alcsr.moveToPosition(npos);
        long aisleref = alcsr.getLong(alcsr.getColumnIndex(
                AISLEID_COLUMN
        ));
        Log.i(LOGTAG,"\tIn Aisle " + Long.toString(aisleref) + " - " + alcsr.getString(alcsr.getColumnIndex(AISLENAME_COLUMN)));
        alcsr.moveToPosition(spos);

        // Get and validate the input Cost/Price
        String costasstr = inputstockcost.getText().toString();
        emsg = ValidateInput.validateMonetary(costasstr);
        if (!emsg.getErrorIndicator()) {
            cost = Double.parseDouble(costasstr);
        } else {
            setMessage(this, emsg.getErrorMessage(),true);
            inputstockcost.requestFocus();
            return;
        }

        // Get and validate the order of the product in the aisle
        String orderasstr = inputstockorder.getText().toString();
        emsg = ValidateInput.validateInteger(orderasstr);
        if (!emsg.getErrorIndicator()) {
            order = Integer.parseInt(orderasstr);
        } else {
            setMessage(this,emsg.getErrorMessage(),true);
            inputstockorder.requestFocus();
            return;
        }

        // Get and validate the checklist count
        String chklstcntasstr = inputchecklistcount.getText().toString();
        if (chklstcntasstr.length() < 1) {
            chklstcntasstr = "1";
        }
        emsg = ValidateInput.validateInteger(chklstcntasstr);
        if (!emsg.getErrorIndicator()) {
            chklstcnt = Integer.parseInt(chklstcntasstr);
        } else {
            setMessage(this,emsg.getErrorMessage(),true);
            inputchecklistcount.requestFocus();
            return;
        }

        Log.i(LOGTAG,"\tCost=" + Double.toString(cost) +
                " Order=" + Integer.toString(order) +
                " ChkFlg=" + Boolean.toString(inputchecklistflag.isChecked()) +
                " Chkcnt=" + Integer.toString(chklstcnt)
        );

        // Reject duplicate (same aisle and product)
        if (dbpumethods.doesProductUsageExist(aisleref,productref) && (!update)) {
            setMessage(this,"Product already exists in this Aisle.",true);
            Log.i(LOGTAG,"\tInsert Rejected as it is a duplicate");
            return;
        }

        if (!update) {
            //Saved the productusage and refresh the stock list
            int addedcnt = 0;
            dbpumethods.insertProductUsage(
                    productref,aisleref,
                    cost,order,
                    inputchecklistflag.isChecked(),chklstcnt);
            Log.i(LOGTAG,"\tResult of insert was " + Boolean.toString(dbpumethods.ifProductUsageAdded()));
        } else {
            dbpumethods.modifyProductUsage(productref,aisleref,cost,order,inputchecklistflag.isChecked(),chklstcnt);
            Log.i(LOGTAG,"\tModify atempted.");
            internalmode = EDITMODE;
        }

        plcsr = dbproductmethods.getProductsNotInAisle(aisleref,productfilter,productorderby);
        productlistadapter.swapCursor(plcsr);
        stockedcursor = dbpumethods.getExpandedProductUsages(stockfilter,stockorderby);
        stocklistadapter.swapCursor(stockedcursor);
        if (internalmode != EDITMODE) {
            setNewInput(inputstockorder, inputstockcost, inputchecklistcount);
        }
    }

    /**************************************************************************
     * sortClick - Handle list heading clicks (i.e. sort by that field)
     *
     * @param view the view that was clicked
     */
    public void sortClick(View view) {
        lastmessage = getResources().getString(R.string.stocklabel) +
                " in Shop/Aisle sorted by ";
        switch (view.getId()) {
            case R.id.stock_stocklist_heading_productname:
                getOrderBy(PRODUCTNAME_FULLCOLUMN,BYPRODUCT);
                lastmessage = lastmessage + " PRODUCT NAME (";
                break;
            case R.id.stock_stocklist_heading_productcost:
                getOrderBy(COST_FULLCOLUMN,BYCOST);
                lastmessage = lastmessage + "  COST/PRICE (";
                break;
            case R.id.stock_stocklist_heading_productorder:
                getOrderBy(PRODUCTUSAGEORDER_FULLCOLUMN,BYORDER);
                lastmessage = lastmessage + " ORDER (";
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
        inputproductfilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                productfilter = DBProductsTableConstants.PRODUCTS_NAME_COL_FULL +
                        DBConstants.SQLLIKECHARSTART +
                        inputproductfilter.getText().toString() +
                        DBConstants.SQLLIKECHAREND;
                plcsr = dbproductmethods.getProductsNotInAisle(currentaisleid, productfilter, productorderby);
                productlistadapter.swapCursor(plcsr);
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    /**************************************************************************
     *  listItemClick - Handle clicking an entry (item) in the list of products
     *                  currently assigned/stocked in the current shop/aisle
     *                  combination.
     *
     * @param view
     * @param position
     * @param id
     */
    public void listItemClick(View view, int position, long id) {
        long aisleid = stockedcursor.getLong(
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
        Class cls = null;
        String classname = this.getClass().getCanonicalName();
        String positivebuttontext = getResources().getString(R.string.editbutton);
        String positiveaction = "stockEdit";
        String negativebuttontext = "";
        String negativeaction = "";
        String neutralbuttontext = getResources().getString(R.string.cancelbutton);
        String neutralaction = "";
        String title = "Edit Product " + productname + " in Aisle " + aislename;
        String message = getResources().getString(R.string.availableoptions) +
                getResources().getString(R.string.cancelbutton) +
                getResources().getString(R.string.and) +
                getResources().getString(R.string.editbutton) +
                getResources().getString(R.string.doublenewline) +
                getResources().getString(R.string.tab1) +
                getResources().getString(R.string.cancelbutton) +
                getResources().getString(R.string.canceldoes) +
                getResources().getString(R.string.doublenewline) +
                getResources().getString(R.string.tab1) +
                getResources().getString(R.string.editbutton) +
                " populates the Cost, Order and the  CheckList checkbox and count inputs." +
                getResources().getString(R.string.newline) +
                getResources().getString(R.string.tab1) +
                " You can then makes changes to these values and then " +
                "Click SAVE to save the changes to the stocked product." +
                getResources().getString(R.string.doublenewline) +
                getResources().getString(R.string.tab1) +
                getResources().getString(R.string.stockedproduct_edit_note1) +
                getResources().getString(R.string.doublenewline) +
                getResources().getString(R.string.tab1) +
                getResources().getText(R.string.stockedproduct_edit_note2);
        MixTripleLongTripleInt values = new MixTripleLongTripleInt();
        values.setMIXTRPPLONGINT(aisleid,productid,0,0,0,0);

        new RequestDialog().requestDialog(thisactivity,
                classname,
                title,
                message,
                positivebuttontext,negativebuttontext,neutralbuttontext,
                positiveaction,negativeaction,neutralaction,
                values
        );
    }

    /**************************************************************************
     *
     * @param view
     * @param position
     * @param id
     */
    public void listItemLongClick(View view, int position, long id) {

        long aisleid = stockedcursor.getLong(
                stockedcursor.getColumnIndex(AISLEREF_COLUMN)
        );
        String aislename = stockedcursor.getString(
                stockedcursor.getColumnIndex(SHOPNAME_COLUMN)
        );
        long productid = stockedcursor.getLong(
                stockedcursor.getColumnIndex(PRODUCTREF_COLUMN)
        );
        String productname = stockedcursor.getString(
                stockedcursor.getColumnIndex(PRODUCTNAME_COLUMN)
        );
        Class cls = null;
        String classname = this.getClass().getCanonicalName();
        String positivebuttontext = getResources().getString(R.string.deletebutton);
        String positiveaction = "stockDelete";
        String negativebuttontext = getResources().getString(R.string.movebutton);
        String negativeaction = "doNothing";
        String neutralbuttontext = getResources().getString(R.string.cancelbutton);
        String neutralaction = "";
        String title = "Delete or Move " + productname + " in Aisle " + aislename;
        String message = "Available Options are CANCEL, DELETE and MOVE.\n\n" +
                "\tCANCEL returns to  STOCK, doing nothing.\n" +
                "\tDELETE allows you to delete the stocked item.\n" +
                "\tMOVE allows you to move the stocked item to another aisle/shop.\n\n";

        ArrayList<String> impact = dbpumethods.stockDeleteImapct(aisleid,productid);
        impact.addAll(dbshoplistmethods.shopListEntryDeleteImpact(aisleid,productid));
        impact.addAll(dbrulemethods.ruleDeleteImpact(aisleid,productid));
        String tmsg = "";
        for (String msg: impact) {
            tmsg = tmsg + msg + "\n";
        }
        message = message + tmsg;

        MixTripleLongTripleInt values = new MixTripleLongTripleInt();
        values.setMIXTRPPLONGINT(aisleid,productid,0,0,0,0);
        new RequestDialog().requestDialog(thisactivity,
                classname,
                title,
                message,
                positivebuttontext,negativebuttontext,neutralbuttontext,
                positiveaction,negativeaction,neutralaction,
                values
        );

    }

    /**************************************************************************
     *  Setup the Shop Spinner
     *
     *  Note, if the shop is changed then the available aisle will change
     *  so the cursor needs to be rebuilt, then the adapter needs to be rebuilt
     *  and then finally the spinner needs to be set to use the new adapter.
     *
     *  Note to avoid initial onItemSelected event (system generated rather than
     *      being due to user input) the listener is added via a new thread
     *      initiated by the spinner's post
     *
     * @param id        The id of the shop
     * @return
     */
    public AdapterShopList setupShopSelectSpinner(long id) {
        Log.i(LOGTAG,"Shop Spinner Setup entered.");
        AdapterShopList rv = new AdapterShopList(
                this,slcsr,0,this.getIntent(),true);
        selectshop.setAdapter(rv);
        //selectshop.setSelection(0,false);
        SpinnerMove.moveToColumn(selectshop,id,slcsr,SHOPID_COLUMN,true);
        Log.i(LOGTAG,"Shop Spinner setup and positioned to shop " + Long.toString(id) + slcsr.getString(slcsr.getColumnIndex(SHOPNAME_COLUMN)));
        selectshop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long rowid) {
                Log.i(LOGTAG,"Shop Spinner Selection Listener entered. Rowid(Shop) " + Long.toString(rowid));
                currentshopid = rowid;
                aislefilter = AISLESHOPREF_FULLCOLUMN + " = " + currentshopid;
                Log.i(LOGTAG,"\tAisle Filter set to :" + aislefilter);
                alcsr = dbaislemethods.getAisles(aislefilter,aisleorderby);
                aislelistadapter.swapCursor(alcsr);
                if (internalmode == EDITMODE) {
                    inputstockorder.setText(DBConstants.DEFAULTORDER);
                    inputstockcost.setText("0");
                    inputchecklistcount.setText("1");
                    inputchecklistflag.setChecked(false);
                    editdisplayed = false;
                } else {
                    setNewInput(inputstockorder, inputstockcost, inputchecklistcount);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.i(LOGTAG,"Shop Spinner Nothing Selected Triggered.");

            }
        });
        return rv;
    }


    /**************************************************************************
     *
     * @param id
     * @return
     */
    public AdapterAisleList setupAisleSelectSpinner(long id) {
        Log.i(LOGTAG,"Aisle Spinner setup entered.");
        AdapterAisleList rv = new AdapterAisleList(
                this,alcsr,0,this.getIntent(),true);
        selectaisle.setAdapter(rv);
        if (id > 0 ) {
            SpinnerMove.moveToColumn(selectaisle,id,alcsr,AISLEID_COLUMN);
            long parentshopid = alcsr.getLong(
                    alcsr.getColumnIndex(AISLESHOPREF_COLUMN));
            SpinnerMove.moveToColumn(selectshop,parentshopid,slcsr,SHOPID_COLUMN,true);
            currentshopid = parentshopid;
        }
        Log.i(LOGTAG,"Aisle Spinner setup and positioned to Aisle " + Long.toString(id));
        selectaisle.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView,
                                       View view,
                                       int position, long rowid) {
                Log.i(LOGTAG,"Aisle Spinner Selection Listener entered. Rowid(Aisle) " + Long.toString(rowid));
                currentaisleid = rowid;
                stockfilter = AISLEREF_FULLCOLUMN + " = " + Long.toString(rowid);
                Log.i(LOGTAG,"\tStock Filter set to :" + stockfilter);
                stockedcursor = dbpumethods.getExpandedProductUsages(stockfilter,
                        stockorderby);
                stocklistadapter.swapCursor(stockedcursor);
                Log.i(LOGTAG,"\tAisle Filter set to :" + aislefilter);
                plcsr = dbproductmethods.getProductsNotInAisle(currentaisleid,productfilter,productorderby);
                productlistadapter.swapCursor(plcsr);

                if (internalmode == EDITMODE) {
                    inputstockorder.setText(DBConstants.DEFAULTORDER);
                    inputstockcost.setText("0");
                    inputchecklistcount.setText("1");
                    inputchecklistflag.setChecked(false);
                    editdisplayed = false;
                } else {
                    setNewInput(inputstockorder, inputstockcost, inputchecklistcount);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        return rv;
    }

    /**************************************************************************
     *
     * @param id
     * @return
     */
    public AdapterProductList setupProductSelectSpinner(long id) {
        Log.i(LOGTAG,"Product Spinner setup entered.");
        AdapterProductList rv = new AdapterProductList(this,
                plcsr,0,this.getIntent(), true);
        selectproduct.setAdapter(rv);
        SpinnerMove.moveToColumn(selectproduct,id,plcsr,PRODUCTID_COLUMN,true);
        //positionSpinnerFromID(selectproduct,
        //        id,plcsr,DBProductsTableConstants.PRODUCTS_ID_COL);
        Log.i(LOGTAG,"Product Spinner setup and positioned to Aisle " + Long.toString(id));
        selectproduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long rowid) {
                Log.i(LOGTAG,"Product Spinner Selection Listener entered. Rowid(Product) " + Long.toString(rowid));
                currentproductid = rowid;
                if (internalmode == EDITMODE && editdisplayed) {
                    inputstockorder.setText(DBConstants.DEFAULTORDER);
                    inputstockcost.setText("0");
                    inputchecklistcount.setText("1");
                    inputchecklistflag.setChecked(false);
                    plcsr = dbproductmethods.getProductsNotInAisle(currentaisleid,productfilter,productorderby);
                    productlistadapter.swapCursor(plcsr);
                }
                if (internalmode == EDITMODE && !editdisplayed) {
                    editdisplayed = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        return rv;
    }

    /**************************************************************************
     * setMessage - set the Message regarding the lat action performed
     * Note as an action may have altered the database getDBcounts
     * is called and the title changed
     *
     * @param sa   the sa
     * @param msg  The message to be displayed.
     * @param flag Message imnportant, if true Yellow text, esle green
     */
    public void setMessage(StockActivity sa, String msg, boolean flag) {

        TextView messagebar = (TextView) sa.findViewById(R.id.stock_messagebar);
        messagebar.setText(context.getResources().getString(
                R.string.messagebar_prefix_lastaction) + " " + msg);
        if (flag) {
            messagebar.setTextColor(Color.YELLOW);
        } else {
            messagebar.setTextColor(Color.GREEN);
        }
        messagebar.setVisibility(View.VISIBLE);
        sa.actionbar.setTitle(getResources().getString(R.string.stocklabel));
    }

    /**
     *
     * @param stockorder
     * @param stockcost
     * @param stockchkcount
     */
    private void setNewInput(EditText stockorder, EditText stockcost, EditText stockchkcount) {
        int highorder = dbpumethods.getHighestProductUsageOrderPerAisle(currentaisleid) + 100;
        if (highorder < 1000) {
            highorder = 1000;
        }
        stockorder.setText(Integer.toString(highorder));
        stockcost.setText("0.00");
        stockchkcount.setText("1");
    }

    /**************************************************************************
     * getOrderBy - Generate the new ORDEY BY sql (ORDER BY already exists)
     * @param newcolumn     the DB column to sort by
     * @param neworderfld   the column as an integer as per constants
     */
    private void getOrderBy(String newcolumn, int neworderfld) {
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
    public void doNothing(RequestDialogParameters values) {
    }

    /**************************************************************************
     * Usecalledmethods.
     */
    protected void usecalledmethods() {
        stockDelete(null);
        stockEdit(null);
    }
}