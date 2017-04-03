package mjt.shopwise;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.NumberFormat;

/**
 * Shopping Activity
 */

@SuppressWarnings({"FieldCanBeLocal", "WeakerAccess", "CanBeFinal", "unused"})
public class ShoppingActivity extends AppCompatActivity {

    private final String THIS_ACTIVITY = "ShoppingActivity";
    private final String LOGTAG = "SW-SHPPINGA";
    @SuppressWarnings("unused")
    private String caller;
    private int resumestate = StandardAppConstants.RESUMSTATE_NORMAL;
    @SuppressWarnings("unused")
    private Activity thisactivity;

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
    DBProductUsageMethods dbpumethods;
    DBShopListMethods dbshoplistmethods;
    DBRuleMethods dbrulemethods;

    private static final String SHOPLISTID_COLUMN = DBShopListTableConstants.SHOPLIST_ID_COL;
    @SuppressWarnings("unused")
    private static final String SHOPLISTID_FULLCOLUMN = DBShopListTableConstants.SHOPLIST_ID_COL_FULL;
    private static final String SHOPLISTPRODUCTREF_COLUMN = DBShopListTableConstants.SHOPLIST_PRODUCTREF_COL;
    @SuppressWarnings("unused")
    private static final String SHOPLISTPRODUCTREF_FULLCOLUMN = DBShopListTableConstants.SHOPLIST_PRODUCTREF_COL_FULL;
    private static final String SHOPLISTAISLREF_COLUMN = DBShopListTableConstants.SHOPLIST_AISLEREF_COL;
    @SuppressWarnings("unused")
    private static final String SHOPLISTAISLREF_FULLCOLUMN = DBShopListTableConstants.SHOPLIST_AISLEREF_COL_FULL;
    @SuppressWarnings("unused")
    private static final String SHOPLISTDATEADDED_COLUMN = DBShopListTableConstants.SHOPLIST_DATEADDED_COL;
    @SuppressWarnings("unused")
    private static final String SHOPLISTDATEADDED_FULLCOLUMN = DBShopListTableConstants.SHOPLIST_DATEADDED_COL_FULL;
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

    //private static final String filter = SHOPLISTNUMBERTOGET_FULLCOLUMN + " > 0 ";
    private static final String filter = "";
    public static final String THISCLASS = ShoppingActivity.class.getSimpleName();

    Cursor slcsr;
    TRPLDBL totals;
    boolean firstrefreshdisplaydone = false;

    private static String lastmessage = "";

    @SuppressWarnings("unused")
    TextView messagebar;
    TextView donebutton;
    TextView tidybutton;
    ListView shoppinglist;
    LinearLayout shoppinglistheading;
    AdapterShoppingList shoppinglistadapter;
    TextView totalcost;
    TextView remaining;
    TextView spent;

    @SuppressWarnings("unused")
    Dialog adjustdialog;
    @SuppressWarnings("unused")
    Window adw;
    @SuppressWarnings("unused")
    TextView adj_donebutton;
    @SuppressWarnings("unused")
    TextView adj_savebutton;
    @SuppressWarnings("unused")
    TextView adj_undobutton;
    @SuppressWarnings("unused")
    TextView adj_morebutton;
    @SuppressWarnings("unused")
    TextView adj_lessbutton;
    @SuppressWarnings("unused")
    TextView adj_orig_productname_tv;
    @SuppressWarnings("unused")
    TextView adj_orig_cost_tv;
    @SuppressWarnings("unused")
    TextView adj_orig_quantity_tv;
    @SuppressWarnings("unused")
    TextView adj_orig_total_tv;

    @SuppressWarnings("unused")
    long adj_aisleid;
    @SuppressWarnings("unused")
    long adj_productid;
    @SuppressWarnings("unused")
    double adj_orig_cost;
    @SuppressWarnings("unused")
    int adj_orig_quantity;
    @SuppressWarnings("unused")
    String adj_orig_productname;
    @SuppressWarnings("unused")
    double adj_new_cost;
    @SuppressWarnings("unused")
    int adj_new_quantity;
    @SuppressWarnings("unused")
    String adj_new_productname;

    protected void onCreate(Bundle savedInstanceState) {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);
        context = this;
        thisactivity = (Activity) context;
        logmsg = "Retrieving IntentExtras";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        caller = getIntent().getStringExtra(
                StandardAppConstants.INTENTKEY_CALLINGACTIVITY
        );
        logmsg = "Preparing ColorCoding";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        menucolorcode = StandardAppConstants.INTENTKEY_MENUCOLORCODE;
        passedmenucolorcode = getIntent().getIntExtra(menucolorcode,0);

        messagebar = (TextView) findViewById(R.id.shopping_messagebar);
        donebutton = (TextView) findViewById(R.id.shopping_donebutton);
        tidybutton = (TextView) findViewById(R.id.shopping_tidybutton);
        shoppinglist = (ListView) findViewById(R.id.shopping_shoppinglist);
        shoppinglistheading = (LinearLayout) findViewById(R.id.shopping_shoppinglistinfo_heading);
        totalcost = (TextView) findViewById(R.id.shopping_shoppinglist_totalcost);
        remaining = (TextView) findViewById(R.id.shopping_shoppinglist_remainingcost);
        spent = (TextView) findViewById(R.id.shopping_shoppinglist_spent);

        actionbar = getSupportActionBar();
        ActionColorCoding.setActionBarColor(this,getIntent(),actionbar);
        primary_color = ActionColorCoding.setHeadingColor(this,getIntent(),0);
        h1 = ActionColorCoding.setHeadingColor(this,getIntent(),1);
        h2 = ActionColorCoding.setHeadingColor(this,getIntent(),2);
        h3 = ActionColorCoding.setHeadingColor(this,getIntent(),3);
        h4 = ActionColorCoding.setHeadingColor(this,getIntent(),4);
        ActionColorCoding.setActionButtonColor(donebutton, primary_color);
        ActionColorCoding.setActionButtonColor(tidybutton,primary_color);
        shoppinglistheading.setBackgroundColor(h1);

        ActionColorCoding.setSwatches(findViewById(android.R.id.content), this.getIntent());

        logmsg = "Preparing Database";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        dbdao = new DBDAO(this);
        dbshopmethods = new DBShopMethods(this);
        dbaislemethods = new DBAisleMethods(this);
        dbproductmethods = new DBProductMethods(this);
        dbpumethods = new DBProductUsageMethods(this);
        dbshoplistmethods = new DBShopListMethods(this);
        dbrulemethods = new DBRuleMethods(this);

        logmsg = "Applying Automated Rule Additions";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        refreshDisplay();
        int autoadded = dbrulemethods.applyAutoAddRules();
        String msg = Integer.toString(autoadded) +
                " Shopping List Entries Added from Rules.";
        //Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
        setMessage(this,msg,false);
        logmsg = "Invoking Prompted Rules";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        doPromptedRules();


        /**
        slcsr = dbshoplistmethods.getExpandedShopListEntries(filter);
        shoppinglistadapter = new AdapterShoppingList(this,slcsr,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER,
                getIntent());
        shoppinglist.setAdapter(shoppinglistadapter);
        totals = dbshoplistmethods.getTotals("");
        totalcost.setText(Double.toString(totals.getdbl1()));
        remaining.setText(Double.toString(totals.getdbl2()));
        spent.setText(Double.toString(totals.getdbl3()));
         **/

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
        logmsg = "Refreshing ShoppingList";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        slcsr = dbshoplistmethods.getExpandedShopListEntries(filter);
        shoppinglistadapter.swapCursor(slcsr);
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
            case R.id.shopping_donebutton:
                logmsg = "Finishing";
                LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
                this.finish();
                break;
            case R.id.shoppinglist_boughtbutton:
               doBought((int) view.getTag());
                break;
            case R.id.shopping_tidybutton:
                tidyShoppingList();
                break;
            case R.id.shoppinglist_adjustbutton:
                doAdjustShoppingEntry((int)view.getTag());
                //doAdjust((int) view.getTag());
                break;
            case R.id.shoppingadjust_donebutton:
                adjustdialog.dismiss();
                refreshDisplay();
                break;
            case R.id.shoppinglist_deletetbutton:
                doDelete((int) view.getTag());
                break;
            default:
                break;
        }
    }

    /**
     * Check for Prompted Rules, if any invoked PromptedRulesActivity
     */
    public void doPromptedRules() {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        Cursor palcsr = dbrulemethods.getPromptedRules("","");
        if (palcsr.getCount() < 1 ) {
            palcsr.close();
            logmsg = "No prompted Rules so returning";
            LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
            return;
        }
        logmsg = "Preparing to invoke" +
                PromptedRulesActivity.class.getSimpleName() +
                " to process " + Integer.toString(palcsr.getCount()) +
                " Prompted Rules" +
                "\n\tNote!!!! may process more as dates are adjusted.";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        palcsr.close();
        Intent intent = new Intent(this,PromptedRulesActivity.class);
        intent.putExtra(StandardAppConstants.INTENTKEY_CALLINGACTIVITY,THIS_ACTIVITY);
        intent.putExtra(StandardAppConstants.INTENTKEY_CALLINGMODE,StandardAppConstants.CM_CLEAR);
        intent.putExtra(menucolorcode,passedmenucolorcode);
        startActivity(intent);
    }

    /**************************************************************************
     *
     * @param position
     */
    public void doAdjustShoppingEntry(int position) {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        Intent intent = new Intent(this,ShoppingEntryAdjustActivity.class);
        intent.putExtra(StandardAppConstants.INTENTKEY_MENUCOLORCODE,passedmenucolorcode);
        intent.putExtra(StandardAppConstants.INTENTKEY_CALLINGACTIVITY,THIS_ACTIVITY);
        intent.putExtra(StandardAppConstants.INTENTKEY_CALLINGMODE,0);
        int cpos = slcsr.getPosition();
        slcsr.moveToPosition(position);
        intent.putExtra(StandardAppConstants.INTENTKEY_AISLEID,
                slcsr.getLong(
                        slcsr.getColumnIndex(
                                SHOPLISTAISLREF_COLUMN
                        )
                ));
        intent.putExtra(StandardAppConstants.INTENTKEY_PRODUCTID,
                slcsr.getLong(
                        slcsr.getColumnIndex(
                                SHOPLISTPRODUCTREF_COLUMN
                        )
                ));
        intent.putExtra(StandardAppConstants.INTENTKEY_PUCOST,
                slcsr.getDouble(
                        slcsr.getColumnIndex(PRODUCTUSAGECOST_COLUMN)
                ));
        intent.putExtra(StandardAppConstants.INTENTKEY_SHOPLISTQUANTITY,
                slcsr.getInt(
                        slcsr.getColumnIndex(SHOPLISTNUMBERTOGET_COLUMN)
                ));
        intent.putExtra(StandardAppConstants.INTENTKEY_PRODUCTNAME,
                slcsr.getString(
                        slcsr.getColumnIndex(PRODUCTNAME_COLUMN)
                ));
        slcsr.moveToPosition(cpos);
        logmsg = "Starting Activity " +
                ShoppingEntryAdjustActivity.class.getSimpleName() +
                " for Product=" +
                slcsr.getString(
                        slcsr.getColumnIndex(PRODUCTNAME_COLUMN)) +
                " Shop=" +
                slcsr.getString(
                        slcsr.getColumnIndex(SHOPNAME_COLUMN)) +
                " City=" +
                slcsr.getString(
                        slcsr.getColumnIndex(SHOPCITY_COLUMN)) +
                " Aisle=" +
                slcsr.getString(
                        slcsr.getColumnIndex(AISLENAME_COLUMN));
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        startActivity(intent);
    }

    /**************************************************************************
     * doBought i.e. buy a product
     * @param position      the position as per the button's tag of the
     *                      respective item being bought.
     */
    public void doBought(int position) {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        slcsr.moveToPosition(position);
        long aisleid = slcsr.getLong(
                slcsr.getColumnIndex(
                        SHOPLISTAISLREF_COLUMN
                ));
        long productid = slcsr.getLong(
                slcsr.getColumnIndex(
                        SHOPLISTPRODUCTREF_COLUMN
                ));
        double cost = slcsr.getDouble(
                slcsr.getColumnIndex(
                        SHOPLISTCOST_COLUMN
                ));
        lastmessage = "Bought 1 " +
                slcsr.getString(slcsr.getColumnIndex(PRODUCTNAME_COLUMN)) +
                " from " +
                slcsr.getString(slcsr.getColumnIndex(SHOPNAME_COLUMN)) +
                " in aisle " +
                slcsr.getString(slcsr.getColumnIndex(AISLENAME_COLUMN));
        setMessage(this,lastmessage,false);
        dbshoplistmethods.addOrUpdateShopListEntry(aisleid,
                productid,
                -1,
                true, true);
        dbpumethods.amendPurchasedProductUsage(aisleid,productid,1);
        refreshDisplay();
    }

    /**************************************************************************
     *
     */
    public void tidyShoppingList() {
        String logmsg = "Invoked";
        String methodname =
                new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                LOGTAG,logmsg,THISCLASS,methodname);
        dbshoplistmethods.tidyShoppingList();
        refreshDisplay();
    }

    /**************************************************************************
     * doDelete Delete a ShoppingList Entry
     * (invoked when Delete button is clicked)
     * @param position  position of the ShoppingList Entry
     */
    public void doDelete(int position) {
        String logmsg = "Invoked";
        String methodname =
                new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                LOGTAG,logmsg,THISCLASS,methodname);
        slcsr.moveToPosition(position);
        long shoplistid = slcsr.getLong(
                slcsr.getColumnIndex(SHOPLISTID_COLUMN));
        dbshoplistmethods.deleteShopListEntry(shoplistid);
        slcsr.close();
        refreshDisplay();
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
    public void setMessage(ShoppingActivity sa,
                           String msg,
                           @SuppressWarnings("SameParameterValue") boolean flag) {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);

        TextView messagebar = (TextView) sa.findViewById(
                R.id.shopping_messagebar);
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
     * refresh the display i.e. the listview and the listview heading
     */
    public void refreshDisplay() {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        slcsr = dbshoplistmethods.getExpandedShopListEntries(filter);
        if (!firstrefreshdisplaydone) {
            shoppinglistadapter = new AdapterShoppingList(this, slcsr,
                    CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER,
                    getIntent());
            shoppinglist.setAdapter(shoppinglistadapter);
            firstrefreshdisplaydone = true;
        } else {
            shoppinglistadapter.swapCursor(slcsr);
        }
        totals = dbshoplistmethods.getTotals("");
        totalcost.setText(NumberFormat.getCurrencyInstance().format(totals.getdbl1()));
        remaining.setText(NumberFormat.getCurrencyInstance().format(totals.getdbl2()));
        spent.setText(NumberFormat.getCurrencyInstance().format(totals.getdbl3()));
    }

    /**************************************************************************
     * sortClick - needed as listview headers invoke this method
     * However the shopping list should be not be sorted other than as
     * setup in order to maintain shoppong order.
     */
     @SuppressWarnings("unused")
     public void sortClick(View view) {
         String logmsg = "Invoked (not shoppinglist sort does nothing)";
         String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
         LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
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
