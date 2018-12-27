package mjt.shopwise;

import android.annotation.SuppressLint;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import mjt.displayhelp.DisplayHelp;

import static mjt.shopwise.ActionColorCoding.transparency_optional;
import static mjt.shopwise.ActionColorCoding.transparency_required;

import static mjt.sqlwords.SQLKWORD.*;

/**
 * ShopAddEditAcitivty - Add or Edit shops
 */
@SuppressWarnings({"FieldCanBeLocal", "WeakerAccess", "unused"})
public class ShopsAddEditActivity extends AppCompatActivity {

    @SuppressWarnings("unused")
    private static final String THIS_ACTIVITY = "ShopsAddEditActivity";
    @SuppressWarnings("unused")
    private static String caller;
    private static  int calledmode;
    /**
     * Sorting Shoplist columns
     */
    private static final int BYSHOP = 0;
    private static final int BYCITY = 1;
    private static final int BYORDER = 2;
    private static final String SORTASCENDING = SQLORDERASCENDING;
    private static final String SORTDESCENDING = SQLORDERDESCENDING;
    /**
     * The Context.
     */
    Context context;
    /**
     * The Actionbar.
     */
    ActionBar actionbar;

    /**
     * Colours
     */
    private static int h1;
    private static int h2;
    @SuppressWarnings("unused")
    private static int h3;
    private static int h4;
    private static int primary_color;
    public static final String THISCLASS = ShopsAddEditActivity.class.getSimpleName();
    private static final String LOGTAG = "SW_SAEA";


    /**
     * View objects
     */
    @SuppressWarnings("unused")
    LinearLayout inputshopname_linearlayout;
    TextView inputshopname_label;
    EditText inputshopname;
    @SuppressWarnings("unused")
    LinearLayout inputshopcity_lineralayout;
    TextView inputshopcity_label;
    EditText inputshopcity;
    @SuppressWarnings("unused")
    LinearLayout inputshoporder_linearlayout;
    TextView inputshoporder_label;
    EditText inputshoporder;
    @SuppressWarnings("unused")
    LinearLayout shopaddedit_buttons_linearlayout;
    TextView savebutton;
    TextView donebutton;
    LinearLayout shoplist_heading;
    @SuppressWarnings("unused")
    TextView shoplist_shopname;
    @SuppressWarnings("unused")
    TextView shoplist_shopcity;
    @SuppressWarnings("unused")
    TextView shoplist_shoporder;
    ListView shoplist;
    AdapterShopList  shoplistadapter;
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

    long passedshopid = 0;
    String passedshopname = "";
    String passedshopcity = "";
    int passedshoporder = 0;

    /**
     * The Showdetails.
     */
    @SuppressWarnings("unused")
    boolean showdetails = false;
    @SuppressWarnings("unused")
    private static final String SHOPID_COLUMN = DBShopsTableConstants.SHOPS_ID_COL;
    @SuppressWarnings("unused")
    private static final String SHOPNAME_COLUMN = DBShopsTableConstants.SHOPS_NAME_COL;
    @SuppressWarnings("unused")
    private static final String SHOPCITY_COLUMN = DBShopsTableConstants.SHOPS_CITY_COL;
    @SuppressWarnings("unused")
    private static final String SHOPORDER_COLUMN = DBShopsTableConstants.SHOPS_ORDER_COL;
    @SuppressWarnings("unused")
    private static final String SHOPID_FULLCOLUMN = DBShopsTableConstants.SHOPS_ID_COL_FULL;
    private static final String SHOPNAME_FULLCOLUMN = DBShopsTableConstants.SHOPS_NAME_COL_FULL;
    private static final String SHOPCITY_FULLCOLUMN = DBShopsTableConstants.SHOPS_CITY_COL_FULL;
    private static final String SHOPORDER_FULLCOLUMN = DBShopsTableConstants.SHOPS_ORDER_COL_FULL;

    /**
     * The Orderby.
     */
    static String orderby = SHOPNAME_FULLCOLUMN + SORTASCENDING;
    static int orderfld = BYSHOP;
    static boolean ordertype = true;
    static boolean sortchanged = false;
    static String lastmessage = "";
    @SuppressWarnings("unused")
    static int shopcount = 0;
    @SuppressWarnings("unused")
    static int aislecount = 0;
    @SuppressWarnings("unused")
    static int productcount = 0;

    private int resumestate = StandardAppConstants.RESUMSTATE_NORMAL;
    @SuppressWarnings("unused")
    private Activity thisactivity;

    @SuppressLint("SetTextI18n")
    protected void onCreate(Bundle savedInstanceState) {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopsaddedit);
        context = this;
        thisactivity = (Activity)context;

        logmsg = "Preparing ColorCoding";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        inputshopname_linearlayout = findViewById(R.id.inputshopname_linearlayout);
        inputshopname_label = findViewById(R.id.inputshopname_label);
        inputshopname = findViewById(R.id.inputshopname);
        inputshopcity_lineralayout = findViewById(R.id.inputshopcity_linearlayout);
        inputshopcity_label = findViewById(R.id.inputshopcity_label);
        inputshopcity = findViewById(R.id.inputshopcity);
        inputshoporder_linearlayout = findViewById(R.id.inputshoporder_linearlayout);
        inputshoporder_label = findViewById(R.id.inputshoporder_label);
        inputshoporder = findViewById(R.id.inputshoporder);

        savebutton = findViewById(R.id.shopaddedit_savebutton);
        donebutton = findViewById(R.id.shopaddedit_donebutton);

        shoplist_heading = findViewById(R.id.shopaddedit_shoplist_heading);
        shoplist_shopname = findViewById(R.id.shopaddedit_shoplist_heading_shopname);
        shoplist_shopcity = findViewById(R.id.shopaddedit_shoplist_heading_shopcity);
        shoplist_shoporder = findViewById(R.id.shopaddedit_shoplist_heading_shoporder);
        shoplist = findViewById(R.id.shopaddedit_shoplist);

        sortable = findViewById(R.id.sortable);
        clickable = findViewById(R.id.clickable);
        longclickable = findViewById(R.id.longclickable);

        // Apply Color Coding
        actionbar = getSupportActionBar();
        ActionColorCoding.setActionBarColor(this,getIntent(),actionbar);
        primary_color = ActionColorCoding.setHeadingColor(this,getIntent(),0);
        h1 = ActionColorCoding.setHeadingColor(this,getIntent(),1);
        h2 = ActionColorCoding.setHeadingColor(this,getIntent(),2);
        h3 = ActionColorCoding.setHeadingColor(this,getIntent(),3);
        h4 = ActionColorCoding.setHeadingColor(this,getIntent(),4);
        ActionColorCoding.setActionButtonColor(donebutton, primary_color);
        ActionColorCoding.setActionButtonColor(savebutton, primary_color);
        ActionColorCoding.setActionButtonColor(inputshopname,h2 & transparency_required);
        ActionColorCoding.setActionButtonColor(inputshopcity,h4 & transparency_optional);
        ActionColorCoding.setActionButtonColor(inputshoporder,h2 & transparency_required);
        inputshopname_label.setTextColor(primary_color);
        inputshopcity_label.setTextColor(h1);
        inputshoporder_label.setTextColor(h1);
        shoplist_heading.setBackgroundColor(h1);

        sortable.setTextColor(primary_color);
        clickable.setVisibility(View.INVISIBLE);
        longclickable.setVisibility(View.INVISIBLE);

        logmsg = "Preparing Database";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        //dbdao = new DBDAO(this);
        dbshopmethods = new DBShopMethods(this);
        dbaislemethods = new DBAisleMethods(this);
        dbproductmethods = new DBProductMethods(this);

        logmsg = "Retrieving ShopList";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        slcsr = dbshopmethods.getShops("",orderby);
        shoplistadapter = new AdapterShopList(this,
                slcsr,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER,
                this.getIntent(),
                false, false, false);
        shoplist.setAdapter(shoplistadapter);

        logmsg = "Retrieving IntentExtras";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        caller = getIntent().getStringExtra(
                StandardAppConstants.INTENTKEY_CALLINGACTIVITY
        );
        calledmode = getIntent().getIntExtra(
                StandardAppConstants.INTENTKEY_CALLINGMODE,
                StandardAppConstants.CM_CLEAR
        );
        logmsg = "Determening ShopOrder for a new Shop (used for ADD mode)";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        setNewOrder(inputshoporder);
        if (calledmode == StandardAppConstants.CM_EDIT) {
            logmsg = "Populating values, (EDIT mode)";
            LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
            passedshopid = getIntent().getLongExtra(
                    StandardAppConstants.INTENTKEY_SHOPID,0);
            passedshopname = getIntent().getStringExtra(
                    StandardAppConstants.INTENTKEY_SHOPNAME);
            passedshopcity = getIntent().getStringExtra(
                    StandardAppConstants.INTENTKEY_SHOPCITY);
            passedshoporder = getIntent().getIntExtra(
                    StandardAppConstants.INTENTKEY_SHOPORDER,0);

            inputshopname.setText(passedshopname);
            inputshopcity.setText(passedshopcity);
            inputshoporder.setText(Integer.toString(passedshoporder));
            this.setTitle(getResources().getString(R.string.shopslabel) + " - Edit");
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
                break;
        }
        logmsg = "Refreshing ShopList";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        slcsr = dbshopmethods.getShops("",orderby);
        shoplistadapter.swapCursor(slcsr);
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
                                R.string.title_help_shopsaddedit_activity),
                        R.array.help_shopsaddedit_activty,
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
            case R.id.shopaddedit_donebutton:
                logmsg = "Finishing";
                LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
                this.finish();
                break;
            case R.id.shopaddedit_savebutton:
                shopsave();
                break;
            default:
                break;
        }
    }

    /**************************************************************************
     *
     */
    @SuppressWarnings("ConstantConditions")
    public void shopsave() {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        boolean notdoneok = true;
        String shoporder_str = inputshoporder.getText().toString();
        String shopname = inputshopname.getText().toString();

        String shopnamelabel = getResources().getString(R.string.shopnamelabel) + " ";
        String shoplabel = getResources().getString(R.string.shoplabel) + " " ;
        String shoporderlabel = getResources().getString(R.string.orderlabel) + " ";
        String notsaved = getResources().getString(R.string.notsaved);
        String msg = "";

        // Shop Name cannot be blank
        if (shopname.length() < 1) {
            msg = shopnamelabel + getResources().getString(R.string.inputblank) +
                    " " + shoplabel +
                    " " + notsaved;
            setMessage(this,msg,notdoneok);
            logmsg = "Cannot Save as ShopName is blank";
            LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
            return;
        }

        //Order cannot be blank
        if (shoporder_str.length() < 1) {
            msg = shoporderlabel + getResources().getString(R.string.inputblank) +
                    " " + shoplabel + " " + notsaved;
            setMessage(this,msg,notdoneok);
            setNewOrder(inputshoporder);
            logmsg = "Cannot Save as ShopOrder is blanks";
            LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
            return;
        }


        int shoporder = Integer.parseInt(inputshoporder.getText().toString());
        msg = shoplabel + " " + shopname + " was ";
        switch (calledmode) {
            case StandardAppConstants.CM_ADD:
                logmsg = "ADD Mode so Adding Shop=" + shopname +
                        " City=" + inputshopcity.getText().toString() +
                        " Order=" + Integer.toString(shoporder);
                LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
                dbshopmethods.insertShop(inputshopname.getText().toString(),
                        shoporder,"",
                        inputshopcity.getText().toString(),"",""
                );
                if (dbshopmethods.ifShopAdded() ) {
                    msg = msg + getResources().getString(R.string.addedok);
                    inputshopname.setText("");
                    setNewOrder(inputshoporder);
                    inputshopcity.setText("");
                    notdoneok = false;
                } else {
                    msg = msg + notsaved;
                }
                break;
            case StandardAppConstants.CM_EDIT:
                logmsg = "EDIT Mode Shop=" + shopname +
                        " City=" + inputshopcity.getText() +
                        " Order=" + Integer.toString(shoporder);
                LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
                dbshopmethods.modifyShop(passedshopid,
                        shoporder,
                        inputshopname.getText().toString(),"",
                        inputshopcity.getText().toString(),
                        "",""
                );
                if (dbshopmethods.ifShopUpdated()) {
                    msg = msg + getResources().getString(R.string.editedok);
                    notdoneok = false;
                } else {
                    msg = msg + notsaved;
                }
                break;
        }
        logmsg = "Refreshing ShopList";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        slcsr = dbshopmethods.getShops("",orderby);
        shoplistadapter.swapCursor(slcsr);
        setMessage(this,msg,notdoneok);
        logmsg = "Shop=" + shopname +
                " City=" + inputshopcity.getText() +
                " Order=" + Integer.toString(shoporder) +
                " Add=" + Boolean.toString(!notdoneok);
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
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
        lastmessage = "List of Shops sorted by ";
        switch (view.getId()) {
            case R.id.shopaddedit_shoplist_heading_shopname:
                getOrderBy(SHOPNAME_FULLCOLUMN,BYSHOP);
                lastmessage = lastmessage + " SHOP NAME in ";
                break;
            case R.id.shopaddedit_shoplist_heading_shopcity:
                getOrderBy(SHOPCITY_FULLCOLUMN,BYCITY);
                lastmessage = lastmessage + "  CITY in ";
                break;
            case R.id.shopaddedit_shoplist_heading_shoporder:
                getOrderBy(SHOPORDER_FULLCOLUMN,BYORDER);
                lastmessage = lastmessage + " SHOP ORDER in";
            default:
                break;
        }
        if (sortchanged) {
            slcsr = dbshopmethods.getShops("",orderby);
            shoplistadapter.swapCursor(slcsr);
            if (ordertype) {
                lastmessage = lastmessage + " ascending order.";
            } else {
                lastmessage = lastmessage + " descending order.";
            }
            setMessage(this,lastmessage,false);
        }
    }

    /**************************************************************************
     * setDBCounts - extract the row counts from the database for relevant
     *                  tables.
     */
    @SuppressWarnings("unused")
    private void setDBCounts() {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        shopcount = dbshopmethods.getShopCount();
        aislecount = dbaislemethods.getAisleCount();
        productcount = dbproductmethods.getProductCount();
    }

    /**************************************************************************
     *
     * @param edittext      the EditText holding the Shop's Order
     */
    @SuppressLint("SetTextI18n")
    private void setNewOrder(EditText edittext) {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        int highorder = dbshopmethods.getHighestShopOrder() + 100;
        if (highorder < 1000 ) {
            highorder = 1000;
        }
        if (highorder > 9999) {
            highorder = highorder - 100 + 1;
            if (highorder > 9999) {
                highorder = 9999;
            }
        }
        edittext.setText(Integer.toString(highorder));
        logmsg = "New Highorder=" + Integer.toString(highorder);
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
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
    public void setMessage(ShopsAddEditActivity sa, String msg, boolean flag) {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);

        TextView messagebar = sa.findViewById(R.id.shopsaddedit_messagebar);
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
}
