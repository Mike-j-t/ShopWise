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
 * Created by Mike092015 on 29/12/2016.
 */

public class ShoppingActivity extends AppCompatActivity {

    private final String THIS_ACTIVITY = "ShoppingActivity";
    private final String LOGTAG = "SW-SHPPINGA";
    private String caller;
    private int resumestate = StandardAppConstants.RESUMSTATE_NORMAL;
    private Activity thisactivity;

    Context context;
    ActionBar actionbar;

    /**
     * Colours
     */
    private int h1;
    private int h2;
    private int h3;
    private int h4;
    private int primary_color;
    private String menucolorcode;
    /**
     * The Passedmenucolorcode.
     */
    int passedmenucolorcode;

    DBDAO dbdao;
    DBShopMethods dbshopmethods;
    DBAisleMethods dbaislemethods;
    DBProductMethods dbproductmethods;
    DBProductUsageMethods dbpumethods;
    DBShopListMethods dbshoplistmethods;
    DBRuleMethods dbrulemethods;

    private static final String SHOPLISTID_COLUMN = DBShopListTableConstants.SHOPLIST_ID_COL;
    private static final String SHOPLISTID_FULLCOLUMN = DBShopListTableConstants.SHOPLIST_ID_COL_FULL;
    private static final String SHOPLISTPRODUCTREF_COLUMN = DBShopListTableConstants.SHOPLIST_PRODUCTREF_COL;
    private static final String SHOPLISTPRODUCTREF_FULLCOLUMN = DBShopListTableConstants.SHOPLIST_PRODUCTREF_COL_FULL;
    private static final String SHOPLISTAISLREF_COLUMN = DBShopListTableConstants.SHOPLIST_AISLEREF_COL;
    private static final String SHOPLISTAISLREF_FULLCOLUMN = DBShopListTableConstants.SHOPLIST_AISLEREF_COL_FULL;
    private static final String SHOPLISTDATEADDED_COLUMN = DBShopListTableConstants.SHOPLIST_DATEADDED_COL;
    private static final String SHOPLISTDATEADDED_FULLCOLUMN = DBShopListTableConstants.SHOPLIST_DATEADDED_COL_FULL;
    private static final String SHOPLISTNUMBERTOGET_COLUMN = DBShopListTableConstants.SHOPLIST_NUMBERTOGET_COL;
    private static final String SHOPLISTNUMBERTOGET_FULLCOLUMN = DBShopListTableConstants.SHOPLIST_NUMBERTOGET_COL_FULL;
    private static final String SHOPLISTDONE_COLUMN = DBShopListTableConstants.SHOPLIST_DONE_COL;
    private static final String SHOPLISTDONE_FULLCOLUMN = DBShopListTableConstants.SHOPLIST_DONE_COL_FULL;
    private static final String SHOPLISTDATEGOT_COLUMN = DBShopListTableConstants.SHOPLIST_DATEGOT_COL;
    private static final String SHOPLISTDATEGOT_FULLCOLUMN = DBShopListTableConstants.SHOPLIST_DATEGOT_COL_FULL;
    private static final String SHOPLISTCOST_COLUMN = DBShopListTableConstants.SHOPLIST_COST_COL;
    private static final String SHOPLISTCOST_FULLCOLUMN = DBShopListTableConstants.SHOPLIST_COST_COL_FULL;

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
    private static final String PRODUCTUSAGECOST_COLUMN = DBProductusageTableConstants.PRODUCTUSAGE_COST_COL;
    private static final String PRODUCTUSAGECOST_FULLCOLUMN = DBProductusageTableConstants.PRODUCTUSAGE_AISLEREF_COL_FULL;
    private static final String CHECKLISTFLAG_COLUMN = DBProductusageTableConstants.PRODUCTUSAGE_CHECKLISTFLAG_COL;
    private static final String CHECKLISTFLAG_FULLCOLUMN = DBProductusageTableConstants.PRODUCTUSAGE_CHECKLISTFLAG_COL_FULL;
    private static final String CHECKLISTCOUNT_COLUMN = DBProductusageTableConstants.PRODUCTUSAGE_CHECKLISTCOUNT_COL;
    private static final String CHECKLISTCOUNT_FULLCOLUMN = DBProductusageTableConstants.PRODUCTUSAGE_CHECKLISTCOUNT_COL_FULL;

    //private static final String filter = SHOPLISTNUMBERTOGET_FULLCOLUMN + " > 0 ";
    private static final String filter = "";
    public static final String THISCLASS = ShoppingActivity.class.getSimpleName();

    Cursor slcsr;
    TRPLDBL totals;
    boolean firstrefreshdisplaydone = false;

    private static String lastmessage = "";

    TextView messagebar;
    TextView donebutton;
    TextView tidybutton;
    ListView shoppinglist;
    LinearLayout shoppinglistheading;
    AdapterShoppingList shoppinglistadapter;
    TextView totalcost;
    TextView remaining;
    TextView spent;

    Dialog adjustdialog;
    Window adw;
    TextView adj_donebutton;
    TextView adj_savebutton;
    TextView adj_undobutton;
    TextView adj_morebutton;
    TextView adj_lessbutton;
    TextView adj_orig_productname_tv;
    TextView adj_orig_cost_tv;
    TextView adj_orig_quantity_tv;
    TextView adj_orig_total_tv;

    long adj_aisleid;
    long adj_productid;
    double adj_orig_cost;
    int adj_orig_quantity;
    String adj_orig_productname;
    double adj_new_cost;
    int adj_new_quantity;
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
            default:
                break;
        }
    }

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
        intent.putExtra(StandardAppConstants.INTENTKEY_CALLINGMODE,(int)0);
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

    //TODO doAdjust should be removed
    /**************************************************************************
     *
     * @param position
     */
    /**
    @SuppressLint("SetTextI18n")
    public void doAdjust(int position) {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        slcsr.moveToPosition(position);
        adj_aisleid = slcsr.getLong(
                slcsr.getColumnIndex(
                        SHOPLISTAISLREF_COLUMN
                )
        );
        adj_productid = slcsr.getLong(
                slcsr.getColumnIndex(
                        SHOPLISTPRODUCTREF_COLUMN
                )
        );
        adj_orig_cost = slcsr.getDouble(
                slcsr.getColumnIndex(
                        PRODUCTUSAGECOST_COLUMN
                )
        );
        adj_orig_quantity = slcsr.getInt(
                slcsr.getColumnIndex(
                        SHOPLISTNUMBERTOGET_COLUMN
                )
        );
        adj_orig_productname = slcsr.getString(
                slcsr.getColumnIndex(
                        PRODUCTNAME_COLUMN
                )
        );

        adjustdialog = new Dialog(this);
        adjustdialog.setContentView(R.layout.activity_shoppingadjust);

        adj_donebutton = (TextView) adjustdialog.findViewById(R.id.shoppingadjust_donebutton);
        adj_savebutton = (TextView) adjustdialog.findViewById(R.id.shoppingadjust_savebutton);
        adj_undobutton = (TextView) adjustdialog.findViewById(R.id.shoppingadjust_undobutton);
        adj_lessbutton = (TextView) adjustdialog.findViewById(R.id.shoppingadjust_lessbutton);
        adj_morebutton = (TextView) adjustdialog.findViewById(R.id.shoppingadjust_morebutton);

        adj_orig_productname_tv = (TextView) adjustdialog.findViewById(R.id.shoppingadjust_orig_productname);
        adj_orig_quantity_tv = (TextView) adjustdialog.findViewById(R.id.shoppingadjust_orig_quantity);
        adj_orig_cost_tv = (TextView) adjustdialog.findViewById(R.id.shoppingadjust_orig_cost);
        adj_orig_quantity_tv = (TextView) adjustdialog.findViewById(R.id.shoppingadjust_orig_quantity);
        adj_orig_total_tv = (TextView) adjustdialog.findViewById(R.id.shoppingadjust_orig_total);

        ActionColorCoding.setActionButtonColor(adj_donebutton,primary_color);
        ActionColorCoding.setActionButtonColor(adj_savebutton,primary_color);
        ActionColorCoding.setActionButtonColor(adj_undobutton,primary_color);
        ActionColorCoding.setActionButtonColor(adj_morebutton,primary_color);
        ActionColorCoding.setActionButtonColor(adj_lessbutton,primary_color);
        int bgcol = ActionColorCoding.getGroupColor(passedmenucolorcode,3) &
                ActionColorCoding.transparency_optional;

        adj_orig_productname_tv.setBackgroundColor(bgcol);
        adj_orig_quantity_tv.setBackgroundColor(bgcol);
        adj_orig_cost_tv.setBackgroundColor(bgcol);
        adj_orig_total_tv.setBackgroundColor(bgcol);

        adj_orig_productname_tv.setText(adj_orig_productname);
        adj_orig_quantity_tv.setText(Integer.toString(adj_orig_quantity));
        adj_orig_cost_tv.setText(Double.toString(adj_orig_cost));

        int titleLayoutId = adjustdialog.getContext().getResources().
                getIdentifier("topPanel", "id", "mjt.shopwise");

        LinearLayout layout = (LinearLayout) adjustdialog.findViewById(titleLayoutId);
        if (layout != null) {
            layout.setBackgroundColor(primary_color);
        }


        adjustdialog.setTitle("Adjust Shopping List Entry");

        adw  = adjustdialog.getWindow();

        //adw.setTitleColor(primary_color);
        adjustdialog.show();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.width = (WindowManager.LayoutParams.MATCH_PARENT);
        lp.height = (WindowManager.LayoutParams.WRAP_CONTENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            adw.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            adw.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            adw.setStatusBarColor(primary_color);
            adw.setTitle("Adjust Shopping List Entry");
        }
        adjustdialog.show();
        adw.setAttributes(lp);
    }
    **/

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
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        dbshoplistmethods.tidyShoppingList();
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
    public void setMessage(ShoppingActivity sa, String msg, boolean flag) {
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
    public void doNothing(RequestDialogParameters values) {
    }
}
