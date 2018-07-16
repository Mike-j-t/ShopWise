package mjt.shopwise;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import mjt.displayhelp.DisplayHelp;

import static mjt.sqlwords.SQLKWORD.*;

/******************************************************************************
 * ShopsActivity - List Shops allowing them to be edited, stocked, or deleted
 * List can also be sorted by clicking on a label. Clicking
 * a label will toggle between descending and ascending order.
 *
 * Stock means to add a product to an aisle, this can be
 * achieved from a shop, aisle or product. The Stock button
 * will only be available when at least 1 aisle and 1 product
 * exists.
 */
@SuppressWarnings({"FieldCanBeLocal", "WeakerAccess", "unused"})
public class ShopsActivity extends AppCompatActivity {

    private static final String THIS_ACTIVITY = "ShopsActivity";
    @SuppressWarnings("unused")
    private static String caller;
    @SuppressWarnings("unused")
    private static int calledmode;
    public static final String THISCLASS = ShopsActivity.class.getSimpleName();
    private static final String LOGTAG = "SW_ShopsA";

    /**
     * Sorting Shoplist columns
     */
    private static final int BYSHOP = 0;
    private static final int BYCITY = 1;
    private static final int BYORDER = 2;
    private static final String SORTASCENDING = SQLORDERASCENDING;
    private static final String SORTDESCENDING = SQLORDERDESCENDING;

    Context context;
    ActionBar actionbar;

    /**
     * Colours
     */
    private static int h1;
    @SuppressWarnings("unused")
    private static int h2;
    @SuppressWarnings("unused")
    private static int h3;
    @SuppressWarnings("unused")
    private static int h4;
    private static int primary_color;
    private String menucolorcode;
    int passedmenucolorcode;

    /**
     * View objects
     */
    TextView donebutton;
    TextView newbutton;
    TextView messagebar;
    ListView shoplist;
    LinearLayout shoplistheading;
    AdapterShopList shoplistadapter;
    TextView sortable;
    TextView clickable;
    TextView longclickable;

    /**
     * Database objects
     */
    @SuppressWarnings("unused")
    DBDAO dbdao;
    DBShopMethods dbshopmethods;
    DBAisleMethods dbaislemethods;
    DBProductMethods dbproductmethods;
    Cursor slcsr;

    private static final String SHOPID_COLUMN =
            DBShopsTableConstants.SHOPS_ID_COL;
    private static final String SHOPNAME_COLUMN =
            DBShopsTableConstants.SHOPS_NAME_COL;
    private static final String SHOPCITY_COLUMN =
            DBShopsTableConstants.SHOPS_CITY_COL;
    private static final String SHOPORDER_COLUMN =
            DBShopsTableConstants.SHOPS_ORDER_COL;
    @SuppressWarnings("unused")
    private static final String SHOPID_FULLCOLUMN =
            DBShopsTableConstants.SHOPS_ID_COL_FULL;
    private static final String SHOPNAME_FULLCOLUMN =
            DBShopsTableConstants.SHOPS_NAME_COL_FULL;
    private static final String SHOPCITY_FULLCOLUMN =
            DBShopsTableConstants.SHOPS_CITY_COL_FULL;
    private static final String SHOPORDER_FULLCOLUMN =
            DBShopsTableConstants.SHOPS_ORDER_COL_FULL;

    static String orderby = SHOPNAME_FULLCOLUMN + SORTASCENDING;
    static int orderfld = BYSHOP;
    static boolean ordertype = true;
    static boolean sortchanged = false;
    static String lastmessage = "";
    static String currentshopname = "";
    @SuppressWarnings("unused")
    static int shopcount = 0;
    static int aislecount = 0;
    static int productcount = 0;

    private int resumestate = StandardAppConstants.RESUMSTATE_NORMAL;
    private Activity thisactivity;


    @SuppressWarnings("DanglingJavadoc")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shops);
        context = this;
        thisactivity = (Activity)context;
        logmsg = "Retrieving IntentExtras";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        caller = getIntent().getStringExtra(
                StandardAppConstants.INTENTKEY_CALLINGACTIVITY);
        calledmode = getIntent().getIntExtra(
                StandardAppConstants.INTENTKEY_CALLINGMODE,0);

        logmsg = "Preparing ColorCoding";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        menucolorcode = StandardAppConstants.INTENTKEY_MENUCOLORCODE;
        passedmenucolorcode = getIntent().getIntExtra(menucolorcode,0);

        donebutton = (TextView) findViewById(R.id.shops_donebutton);
        newbutton = (TextView) findViewById(R.id.shops_newshopbutton);
        shoplist  = (ListView) findViewById(R.id.shops_shoplist);
        shoplistheading = (LinearLayout) findViewById(R.id.shops_shoplist_heading);
        messagebar = (TextView) findViewById(R.id.shops_messagebar);
        sortable = (TextView) findViewById(R.id.sortable);
        clickable = (TextView) findViewById(R.id.clickable);
        longclickable = (TextView) findViewById(R.id.longclickable);


        // Apply Color Coding
        actionbar = getSupportActionBar();
        ActionColorCoding.setActionBarColor(this,getIntent(),actionbar);
        primary_color = ActionColorCoding.setHeadingColor(this,getIntent(),0);
        h1 = ActionColorCoding.setHeadingColor(this,getIntent(),1);
        h2 = ActionColorCoding.setHeadingColor(this,getIntent(),2);
        h3 = ActionColorCoding.setHeadingColor(this,getIntent(),3);
        h4 = ActionColorCoding.setHeadingColor(this,getIntent(),4);
        ActionColorCoding.setActionButtonColor(donebutton, primary_color);
        ActionColorCoding.setActionButtonColor(newbutton, primary_color);
        shoplistheading.setBackgroundColor(h1);

        sortable.setTextColor(primary_color);
        clickable.setTextColor(primary_color);
        longclickable.setTextColor(primary_color);

        ActionColorCoding.setSwatches(findViewById(android.R.id.content),this.getIntent());

        //dbdao = new DBDAO(this);
        logmsg = "Preparing Database";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        dbshopmethods = new DBShopMethods(this);
        dbaislemethods = new DBAisleMethods(this);
        dbproductmethods = new DBProductMethods(this);
        setDBCounts();
        this.setTitle(getResources().getString(R.string.shopslabel));

        logmsg = "Retrieving ShopList";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        slcsr = dbshopmethods.getShops("",orderby);
        shoplistadapter = new AdapterShopList(
                this,
                slcsr,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER,
                getIntent(),
                false, false, false);
        shoplist.setAdapter(shoplistadapter);
        logmsg = "Adding ShopList OnItemLongClick Listener";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        shoplist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView,
                                           View view,
                                           int position,
                                           long id) {
                listItemLongClick(view, position, id);
                return true;
            }
        });
        logmsg = "Adding ShopList OnItemClick Listener";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        shoplist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView,
                                    View view,
                                    int position,
                                    long id) {
                listItemClick(view, position, id);
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
        logmsg = "Refreshing ShopList";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        slcsr = dbshopmethods.getShops("",orderby);
        shoplistadapter.swapCursor(slcsr);
        resumestate = StandardAppConstants.RESUMSTATE_NORMAL;
        this.setTitle(getResources().getString(R.string.shopslabel));
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
                                R.string.title_help_shops_activity),
                        R.array.help_shops_activity,
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
        slcsr.close();
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
            case R.id.shops_donebutton:
                logmsg = "Finishing";
                LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
                this.finish();
                break;
            case R.id.shops_newshopbutton:
                addShop();
                break;
            default:
                break;
        }
    }

    /**************************************************************************
     * addShop - Invoke ShopAddEditActivity passing
     *              the respective intent extras
     *              menucolorcode as per this activity
     *              this Activity's name
     *              the mode i.e. Add as opposed to edit
     *
     */
    public void addShop() {
        String logmsg = "Invoked";
        String methodname =
                new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        Intent intent = new Intent(this,ShopsAddEditActivity.class);
        intent.putExtra(menucolorcode,passedmenucolorcode);
        intent.putExtra(
                StandardAppConstants.INTENTKEY_CALLINGACTIVITY,
                THIS_ACTIVITY
        );
        intent.putExtra(
                StandardAppConstants.INTENTKEY_CALLINGMODE,
                StandardAppConstants.CM_ADD);
        logmsg = "Starting " + ShopsAddEditActivity.class.getSimpleName() +
                " in ADD mode.";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        startActivity(intent);
    }

    /**************************************************************************
     * Shop edit.
     *
     * @param values the values
     */
    public void shopEdit(@SuppressWarnings("SameParameterValue") RequestDialogParameters values) {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        //long shopid = values.getLong1();
        Intent intent = new Intent(this,ShopsAddEditActivity.class);
        intent.putExtra(
                StandardAppConstants.INTENTKEY_CALLINGACTIVITY,
                THIS_ACTIVITY
        );
        intent.putExtra(
                StandardAppConstants.INTENTKEY_CALLINGMODE,
                StandardAppConstants.CM_EDIT
        );
        intent.putExtra(
                StandardAppConstants.INTENTKEY_SHOPID,
                slcsr.getLong(
                        slcsr.getColumnIndex(SHOPID_COLUMN)
                )
        );
        intent.putExtra(
                StandardAppConstants.INTENTKEY_SHOPNAME,
                slcsr.getString(
                        slcsr.getColumnIndex(SHOPNAME_COLUMN)
                )
        );
        intent.putExtra(
                StandardAppConstants.INTENTKEY_SHOPCITY,
                slcsr.getString(
                        slcsr.getColumnIndex(SHOPCITY_COLUMN)
                )
        );
        intent.putExtra(
                StandardAppConstants.INTENTKEY_SHOPORDER,
                slcsr.getInt(
                        slcsr.getColumnIndex(SHOPORDER_COLUMN)
                )
        );
        intent.putExtra(menucolorcode,passedmenucolorcode);
        logmsg = "Starting " + ShopsAddEditActivity.class.getSimpleName() +
                " in EDIT mode for Shop=" +
                slcsr.getString(
                        slcsr.getColumnIndex(SHOPNAME_COLUMN)) + "" +
                " City=" +
                slcsr.getString(
                        slcsr.getColumnIndex(SHOPCITY_COLUMN));
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        startActivity(intent);
    }

    /**************************************************************************
     * shopDelete - Delete the respective Shop as extracted
     *              from the RequestDialogParameters passed to
     *              this method via the RequestDialog.
     *
     * @param values a RequestDialogParameters instance
     */
    public void shopDelete(@SuppressWarnings("SameParameterValue") RequestDialogParameters values) {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        Activity activity = (values.getPassedactivity());
        ShopsActivity sa = (ShopsActivity) activity;
        sa.dbshopmethods.deleteShop(values.getLong1(),false);
        sa.slcsr = dbshopmethods.getShops("",ShopsActivity.orderby);
        sa.shoplistadapter.swapCursor(sa.slcsr);
        sa.setMessage(sa,"Shop " +
                currentshopname +
                " Deleted.",
                false
        );
    }

    /**************************************************************************
     *
     * @param values    a RequestDialogParameters instance
     */
    @SuppressWarnings("unused")
    public void shopStock(RequestDialogParameters values) {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        long shopid = values.getLong1();
        Intent intent = new Intent(this,StockAddActivity.class);
        intent.putExtra(
                StandardAppConstants.INTENTKEY_CALLINGACTIVITY,
                THIS_ACTIVITY
        );
        intent.putExtra(
                StandardAppConstants.INTENTKEY_CALLINGMODE,
                StandardAppConstants.CM_STOCKFROMSSHOP
        );
        intent.putExtra(
                StandardAppConstants.INTENTKEY_SHOPID,
                shopid
        );
        intent.putExtra(
                StandardAppConstants.INTENTKEY_SHOPNAME,
                currentshopname
        );
        intent.putExtra(menucolorcode,passedmenucolorcode);
        logmsg = "Starting " + StockAddActivity.class.getSimpleName() +
                " passing Shop=" +
                slcsr.getString(
                        slcsr.getColumnIndex(SHOPNAME_COLUMN)) +
                " City=" +
                slcsr.getString(
                        slcsr.getColumnIndex(SHOPCITY_COLUMN));
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
        lastmessage = getResources().getString(R.string.shopslabel) +
                " sorted by ";
        switch (view.getId()) {
            case R.id.shops_shoplist_heading_shopname:
                getOrderBy(SHOPNAME_FULLCOLUMN,BYSHOP);
                lastmessage = lastmessage + " SHOP NAME (";
                break;
            case R.id.shops_shoplist_heading_shopcity:
                getOrderBy(SHOPCITY_FULLCOLUMN,BYCITY);
                lastmessage = lastmessage + "  CITY (";
                break;
            case R.id.shops_shoplist_heading_shoporder:
                getOrderBy(SHOPORDER_FULLCOLUMN,BYORDER);
                lastmessage = lastmessage + " SHOP ORDER (";
            default:
                break;
        }
        if (sortchanged) {
            slcsr = dbshopmethods.getShops("",orderby);
            shoplistadapter.notifyDataSetChanged();
            shoplistadapter.swapCursor(slcsr);
            if (ordertype) {
                lastmessage = lastmessage + "ascending)";
            } else {
                lastmessage = lastmessage + "descending)";
            }
            setMessage(this,lastmessage,false);
        }
    }

    /**************************************************************************
     * listItemClick - Handle Clicking an Item
     *
     * @param view     The view that was clicked.
     * @param position The position of the clicked item in the list
     * @param id       The row id of the item that was clicked.
     */
    public void listItemClick(View view, int position, long id) {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);

        long shopid = slcsr.getLong(
                slcsr.getColumnIndex(SHOPID_COLUMN)
        );
        currentshopname = slcsr.getString(
                slcsr.getColumnIndex(SHOPNAME_COLUMN)
        );
        Class cls = null;
        String classname = this.getClass().getCanonicalName();
        String positivebuttontext = getResources().getString(R.string.editbutton);
        String positiveaction = "shopEdit";
        String negativebuttontext = getResources().getString(R.string.stockbutton);
        String negativeaction = "shopStock";
        String neutralbuttontext = getResources().getString(R.string.cancelbutton);
        String neutralaction = "";
        String title = "Edit or Stock Shop = " + currentshopname;
        String message = "Avaiable Options are CANCEL, EDIT and STOCK.\n\n" +
                "\tCANCEL returns to Shops, doing nothing.\n\n" +
                "\tEDIT allows you to edit the Shop.\n\n" +
                "\tSTOCK allows you to assign Products to Aisles in the Shop " +
                "(or othershops).";
        if ((aislecount < 1) || (productcount < 1) || (dbaislemethods.getAislesPerShop(shopid) < 1)) {
            negativebuttontext = "";
        }
        MixTripleLongTripleInt values = new MixTripleLongTripleInt();
        values.setMIXTRPPLONGINT(shopid, 0, 0, 0, 0, 0);
        logmsg = "Presenting OnClick RequestDialog for Shop=" +
                slcsr.getString(
                        slcsr.getColumnIndex(SHOPNAME_COLUMN)) +
                " City=" +
                slcsr.getString(
                        slcsr.getColumnIndex(SHOPCITY_COLUMN));
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        //Activity activitytopass = (Activity)this;
        new RequestDialog().requestDialog(thisactivity,
                classname,
                title,
                message,
                positivebuttontext, negativebuttontext, neutralbuttontext,
                positiveaction, negativeaction, neutralaction,
                values                          // parameters to be used
        );
    }

    /**************************************************************************
     * listItemLongClick - Handle long Clicking an Item
     *
     * @param view     The view that was long clicked.
     * @param position The position of the clicked item in the list
     * @param id       The row id of the item that was clicked.
     */
    public void listItemLongClick(View view, int position, long id) {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);

        long shopid = slcsr.getLong(
                slcsr.getColumnIndex(SHOPID_COLUMN));
        currentshopname = slcsr.getString(
                slcsr.getColumnIndex(SHOPNAME_COLUMN));
        String title = "Delete Shop - " +currentshopname;

        String positivebuttontext = getResources().getString(R.string.deletebutton);
        String negativebuttontext = "";
        String neutralbuttontext = getResources().getString(R.string.cancelbutton);
        String positiveaction = "shopDelete";
        String negativeaction = "";
        String neutralaction = "";

        // Get impact of deleting the shop (i.e other entitiies to be deleted)
        // e.g. Ailses witht the shop as a parent, product usages (products
        // assigned to (aka stocked_ to such an aisle. Shopping list entries
        // and Rules for such product usages. Notes, products are not
        // to be deleted.
        ArrayList<String> impact = dbshopmethods.shopDeletedImpact(shopid);
        StringBuilder sb = new StringBuilder("\n");
        for (String msg: impact) {
            sb.append(msg).append("\n");
    }
        String message = "Deleting Shop - " + currentshopname + sb;
         // Prepare for potential deletion i.e. pass the shopid via values
        MixTripleLongTripleInt values = new MixTripleLongTripleInt();
        values.setMIXTRPPLONGINT(shopid,0,0,0,0,0);

        // Need the class that the methods belong to
        // (note restricted to all being in this class)
        String classname = this.getClass().getCanonicalName();
        logmsg = "Presenting OnLongClick RequestDialog for Shop=" +
                slcsr.getString(
                        slcsr.getColumnIndex(SHOPNAME_COLUMN)) +
                " City=" +
                slcsr.getString(
                        slcsr.getColumnIndex(SHOPCITY_COLUMN));
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        new RequestDialog().requestDialog(thisactivity, classname,
                title, message,
                positivebuttontext, negativebuttontext, neutralbuttontext ,
                positiveaction, negativeaction, neutralaction,
                values);
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
     * setMessage - set the Message regarding the lat action performed
     * Note as an action may have altered the database getDBcounts
     * is called and the title changed
     *
     * @param sa   the sa
     * @param msg  The message to be displayed.
     * @param flag Message imnportant, if true Yellow text, esle green
     */
    public void setMessage(ShopsActivity sa,
                           String msg,
                           @SuppressWarnings("SameParameterValue") boolean flag) {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);

        TextView messagebar = (TextView) sa.findViewById(R.id.shops_messagebar);
        messagebar.setText(context.getResources().getString(
                R.string.messagebar_prefix_lastaction,msg));
        if (flag) {
            messagebar.setTextColor(Color.YELLOW);
        } else {
            messagebar.setTextColor(Color.GREEN);
        }
        messagebar.setVisibility(View.VISIBLE);
        sa.actionbar.setTitle(getResources().getString(R.string.shopslabel));
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
    @SuppressWarnings({"EmptyMethod", "unused"})
    public void doNothing(RequestDialogParameters values) {
    }

    /**************************************************************************
     * Usecalledmethods.
     */
    @SuppressWarnings("unused")
    protected void usecalledmethods() {
        shopDelete(null);
        shopEdit(null);
    }
}
