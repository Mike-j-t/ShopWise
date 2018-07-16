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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import mjt.displayhelp.DisplayHelp;

import static mjt.sqlwords.SQLKWORD.*;
/**
 * Order Activity
 *
 * List all Stocked Products showing how many currently, to get, in the
 * shopping list. With product filter and single click to add a product.
 */

@SuppressWarnings({"FieldCanBeLocal", "WeakerAccess", "unused"})
public class OrderActivity extends AppCompatActivity {

    @SuppressWarnings("unused")
    private final String THIS_ACTIVITY = "OrderActivity";
    private final String LOGTAG = "SW-OA";
    @SuppressWarnings("unused")
    private String caller;
    private int resumestate = StandardAppConstants.RESUMSTATE_NORMAL;
    @SuppressWarnings("unused")
    private Activity thisactivity;
    public static final String THISCLASS = OrderActivity.class.getSimpleName();

    Context context;
    ActionBar actionbar;

    /**
     * Colours
     */
    private int h1;
    private int h2;
    @SuppressWarnings("unused")
    private int h3;
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
    DBProductUsageMethods dbpumethods;
    DBShopListMethods dbshoplistmethods;
    @SuppressWarnings("unused")
    DBRuleMethods dbrulemethods;

    @SuppressWarnings("unused")
    private static final String SHOPID_COLUMN = DBShopsTableConstants.SHOPS_ID_COL;
    private static final String SHOPNAME_COLUMN = DBShopsTableConstants.SHOPS_NAME_COL;
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
    private static final String PRODUCTUSAGECOST_FULLCOLUMN = DBProductusageTableConstants.PRODUCTUSAGE_COST_FULL;
    @SuppressWarnings("unused")
    private static final String CHECKLISTFLAG_COLUMN = DBProductusageTableConstants.PRODUCTUSAGE_CHECKLISTFLAG_COL;
    @SuppressWarnings("unused")
    private static final String CHECKLISTFLAG_FULLCOLUMN = DBProductusageTableConstants.PRODUCTUSAGE_CHECKLISTFLAG_COL_FULL;
    @SuppressWarnings("unused")
    private static final String CHECKLISTCOUNT_COLUMN = DBProductusageTableConstants.PRODUCTUSAGE_CHECKLISTCOUNT_COL;
    @SuppressWarnings("unused")
    private static final String CHECKLISTCOUNT_FULLCOLUMN = DBProductusageTableConstants.PRODUCTUSAGE_CHECKLISTCOUNT_COL_FULL;

    Cursor olcsr;

    @SuppressWarnings("unused")
    private static final String ORDERED_PRODUCTS = DBConstants.CALCULATED_PRODUCTSORDERED_NAME;

    private static final int BYPRODUCT = 0;
    private static final int BYSHOP = 1;
    private static final int BYAISLE = 2;
    private static final int BYPRICE = 3;
    private static final String SORTASCENDING = SQLORDERASCENDING;
    private static final String SORTDESCENDING = SQLORDERDESCENDING;
    private static String productfilter = "";
    private static String orderby = PRODUCTNAME_FULLCOLUMN + SORTASCENDING;
    private static int orderfld = BYPRODUCT;
    private static boolean ordertype = true;
    private static boolean sortchanged = false;
    private static String lastmessage = "";

    @SuppressWarnings("unused")
    private static String currentproductname = "";
    @SuppressWarnings("unused")
    private static String currentshopname = "";
    @SuppressWarnings("unused")
    private static String currentaislename = "";

    @SuppressWarnings("unused")
    long currentproductid = 0;
    @SuppressWarnings("unused")
    long currentshopid = 0;
    @SuppressWarnings("unused")
    long currentaisleid = 0;

    TextView messagebar;
    TextView donebutton;
    TextView inputproductfilterlabel;
    EditText inputproductfilter;
    ListView orderlist;
    LinearLayout orderlistheading;
    AdapterOrderList orderlisadapter;
    TextView sortable;
    TextView clickable;
    TextView longclickable;

    protected void onCreate(Bundle savedInstanceState) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        context = this;
        thisactivity = (Activity) context;
        caller = getIntent().getStringExtra(
                StandardAppConstants.INTENTKEY_CALLINGACTIVITY
        );

        menucolorcode = StandardAppConstants.INTENTKEY_MENUCOLORCODE;
        passedmenucolorcode = getIntent().getIntExtra(menucolorcode,0);

        productfilter = "";
        messagebar = (TextView) findViewById(R.id.order_messagebar);
        donebutton = (TextView) findViewById(R.id.order_donebutton);
        inputproductfilterlabel = (TextView) findViewById(R.id.products_inputfilterlabel);
        inputproductfilter = (EditText) findViewById(R.id.products_inputfilter);
        orderlist = (ListView) findViewById(R.id.order_list);
        orderlistheading = (LinearLayout) findViewById(R.id.order_list_heading);
        sortable = (TextView) findViewById(R.id.sortable);
        clickable = (TextView) findViewById(R.id.clickable);
        longclickable = (TextView) findViewById(R.id.longclickable);
        addFilterListener();

        msg = "Preparing Color Coding";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        actionbar = getSupportActionBar();
        ActionColorCoding.setActionBarColor(this,getIntent(),actionbar);
        primary_color = ActionColorCoding.setHeadingColor(this,getIntent(),0);
        h1 = ActionColorCoding.setHeadingColor(this,getIntent(),1);
        h2 = ActionColorCoding.setHeadingColor(this,getIntent(),2);
        h3 = ActionColorCoding.setHeadingColor(this,getIntent(),3);
        h4 = ActionColorCoding.setHeadingColor(this,getIntent(),4);
        ActionColorCoding.setActionButtonColor(donebutton, primary_color);
        inputproductfilterlabel.setTextColor(h2);
        ActionColorCoding.setActionButtonColor(inputproductfilter,
                h4 & ActionColorCoding.transparency_optional);
        orderlistheading.setBackgroundColor(h1);
        sortable.setTextColor(primary_color);
        clickable.setVisibility(View.INVISIBLE);
        longclickable.setVisibility(View.INVISIBLE);

        ActionColorCoding.setSwatches(findViewById(android.R.id.content),this.getIntent());

        msg = "Preparing Databases";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        dbdao = new DBDAO(this);
        dbshopmethods = new DBShopMethods(this);
        dbaislemethods = new DBAisleMethods(this);
        dbproductmethods = new DBProductMethods(this);
        dbpumethods = new DBProductUsageMethods(this);
        dbshoplistmethods = new DBShopListMethods(this);
        dbrulemethods = new DBRuleMethods(this);

        msg = "Preparing OrderList";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        olcsr = dbpumethods.getExpandedProductUsages(productfilter,orderby);
        orderlisadapter = new AdapterOrderList(
                this,
                olcsr,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER,
                getIntent(),
                false);
        orderlist.setAdapter(orderlisadapter);
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
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
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
        msg = "Refreshing OrderList";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        olcsr = dbpumethods.getExpandedProductUsages(productfilter,orderby);
        orderlisadapter.swapCursor(olcsr);
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
                                R.string.title_help_order_activity),
                        R.array.help_order_activity,
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
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        super.onDestroy();
        olcsr.close();
    }

    /**************************************************************************
     * Button Click Handler
     *
     * @param view The view (i.e the TextView that was clicked)
     */
    @SuppressWarnings("unused")
    public void actionButtonClick(View view) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        switch (view.getId()) {
            case R.id.order_donebutton:
                msg = "Finishing";
                LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
                this.finish();
                break;
            case R.id.orderlist_order1_button:
                olcsr.moveToPosition((int) view.getTag());
                msg = "Adding 1 to" +
                        " Shop=" + olcsr.getString(
                        olcsr.getColumnIndex(SHOPNAME_COLUMN)) +
                        " City=" +
                        olcsr.getString(
                                olcsr.getColumnIndex(SHOPCITY_COLUMN)) +
                        " Aisle=" +
                        olcsr.getString(
                                olcsr.getColumnIndex(AISLENAME_COLUMN)) +
                        " Product=" +
                        olcsr.getString(
                                olcsr.getColumnIndex(PRODUCTNAME_COLUMN));
                LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
                dbshoplistmethods.addOrUpdateShopListEntry(
                        olcsr.getLong(olcsr.getColumnIndex(AISLEREF_COLUMN)),
                        olcsr.getLong(olcsr.getColumnIndex(PRODUCTREF_COLUMN)),
                        1,
                        true, false
                );
                olcsr = dbpumethods.getExpandedProductUsages(productfilter,orderby);
                orderlisadapter.swapCursor(olcsr);
                break;
            case R.id.orderlist_less1_button:
                olcsr.moveToPosition((int) view.getTag());
                olcsr.moveToPosition((int) view.getTag());
                msg = "Subtracting 1 from" +
                        " Shop=" + olcsr.getString(
                        olcsr.getColumnIndex(SHOPNAME_COLUMN)) +
                        " City=" +
                        olcsr.getString(
                                olcsr.getColumnIndex(SHOPCITY_COLUMN)) +
                        " Aisle=" +
                        olcsr.getString(
                                olcsr.getColumnIndex(AISLENAME_COLUMN)) +
                        " Product=" +
                        olcsr.getString(
                                olcsr.getColumnIndex(PRODUCTNAME_COLUMN));
                LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
                dbshoplistmethods.addOrUpdateShopListEntry(
                        olcsr.getLong(olcsr.getColumnIndex(AISLEREF_COLUMN)),
                        olcsr.getLong(olcsr.getColumnIndex(PRODUCTREF_COLUMN)),
                        -1,
                        true, false
                );
                olcsr = dbpumethods.getExpandedProductUsages(productfilter,orderby);
                orderlisadapter.swapCursor(olcsr);
            default:
                break;
        }
    }

    /**************************************************************************
     * addFilterListener - Add a Listener to filter the ListView
     *                      i.e. as characters are typed then the ListView
     *                      is filtered according to the characters input
     *                      This is done by using LIKE '%<characters>%'
     *
     */
    public void addFilterListener() {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        inputproductfilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                productfilter = DBProductsTableConstants.PRODUCTS_NAME_COL_FULL +
                        SQLLIKECHARSTART +
                        inputproductfilter.getText().toString() +
                        SQLLIKECHAREND;
                olcsr = dbpumethods.getExpandedProductUsages(productfilter,orderby);
                orderlisadapter.swapCursor(olcsr);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    /**************************************************************************
     * setMessage - set the Message regarding the lat action performed
     * Note as an action may have altered the database getDBcounts
     * is called and the title changed
     *
     * @param oa   the sa
     * @param msg  The message to be displayed.
     * @param flag Message imnportant, if true Yellow text, esle green
     */
    public void setMessage(OrderActivity oa,
                           String msg,
                           @SuppressWarnings("SameParameterValue") boolean flag) {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);

        TextView messagebar = (TextView) oa.findViewById(R.id.order_messagebar);
        messagebar.setText(context.getResources().getString(
                R.string.messagebar_prefix_lastaction,msg));
        if (flag) {
            messagebar.setTextColor(Color.YELLOW);
        } else {
            messagebar.setTextColor(Color.GREEN);
        }
        messagebar.setVisibility(View.VISIBLE);
    }

    /**************************************************************************
     * sortClick - Handle list heading clicks (i.e. sort by that field)
     *
     * @param view the view that was clicked
     */
    @SuppressWarnings("unused")
    public void sortClick(View view) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        lastmessage = getResources().getString(R.string.orderlabel) +
                " sorted by ";
        switch (view.getId()) {
            case R.id.order_list_heading_productname:
                getOrderBy(PRODUCTNAME_FULLCOLUMN,BYPRODUCT);
                lastmessage = lastmessage + " PRODUCT NAME (";
                break;
            case R.id.order_list_heading_shopname:
                getOrderBy(SHOPNAME_FULLCOLUMN,BYSHOP);
                lastmessage = lastmessage + "  SHOP NAME (";
                break;
            case R.id.order_list_heading_aislename:
                getOrderBy(AISLENAME_FULLCOLUMN,BYAISLE);
                lastmessage = lastmessage + " AISLE (";
                break;
            case R.id.order_list_heading_cost:
                getOrderBy(PRODUCTUSAGECOST_FULLCOLUMN,BYPRICE);
                lastmessage = lastmessage + " PRICE (";
            default:
                break;
        }
        if (sortchanged) {
            olcsr = dbpumethods.getExpandedProductUsages(productfilter,orderby);
            orderlisadapter.swapCursor(olcsr);
            if (ordertype) {
                lastmessage = lastmessage + "ascending)";
            } else {
                lastmessage = lastmessage + "descending)";
            }
            setMessage(this,lastmessage,false);
        }
    }
    /**************************************************************************
     * getOrderBy - Generate the new ORDEY BY sql (ORDER BY already exists)
     * @param newcolumn     the DB column to sort by
     * @param neworderfld   the column as an integer as per constants
     */
    private void getOrderBy(String newcolumn, int neworderfld) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
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
