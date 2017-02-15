package mjt.shopwise;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by Mike092015 on 27/12/2016.
 */

public class CheckListActivity extends AppCompatActivity {

    private final String THIS_ACTIVITY = "CheckListActivity";
    private final String LOGTAG = "SW_CLA";
    public static final String THISCLASS = CheckListActivity.class.getSimpleName();
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


    private final String SHOPID_COLUMN = DBShopsTableConstants.SHOPS_ID_COL;
    private final String SHOPNAME_COLUMN = DBShopsTableConstants.SHOPS_NAME_COL;
    private final String SHOPCITY_COLUMN = DBShopsTableConstants.SHOPS_CITY_COL;
    private final String SHOPORDER_COLUMN = DBShopsTableConstants.SHOPS_ORDER_COL;
    private final String SHOPID_FULLCOLUMN = DBShopsTableConstants.SHOPS_ID_COL_FULL;
    private final String SHOPNAME_FULLCOLUMN = DBShopsTableConstants.SHOPS_NAME_COL_FULL;
    private final String SHOPCITY_FULLCOLUMN = DBShopsTableConstants.SHOPS_CITY_COL_FULL;
    private final String SHOPORDER_FULLCOLUMN = DBShopsTableConstants.SHOPS_ORDER_COL_FULL;

    private final String AISLEID_COLUMN = DBAislesTableConstants.AISLES_ID_COL;
    private final String AILSEID_FULLCOLUMN = DBAislesTableConstants.AISLES_ID_COL_FULL;
    private final String AISLENAME_COLUMN = DBAislesTableConstants.AISLES_NAME_COL;
    private final String AISLENAME_FULLCOLUMN = DBAislesTableConstants.AISLES_NAME_COL_FULL;
    private final String AISLEORDER_COLUMN = DBAislesTableConstants.AISLES_ORDER_COL;
    private final String AISLEORDER_FULLCOLUMN = DBAislesTableConstants.AISLES_ORDER_COL_FULL;
    private final String AISLESHOPREF_COLUMN = DBAislesTableConstants.AISLES_SHOPREF_COL;
    private final String AISLESHOPREF_FULLCOLUMN = DBAislesTableConstants.AISLES_SHOPREF_COL_FULL;

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
    private static final String PRODUCTUSAGECOST_FULLCOLUMN = DBProductusageTableConstants.PRODUCTUSAGE_COST_FULL;
    private static final String CHECKLISTFLAG_COLUMN = DBProductusageTableConstants.PRODUCTUSAGE_CHECKLISTFLAG_COL;
    private static final String CHECKLISTFLAG_FULLCOLUMN = DBProductusageTableConstants.PRODUCTUSAGE_CHECKLISTFLAG_COL_FULL;
    private static final String CHECKLISTCOUNT_COLUMN = DBProductusageTableConstants.PRODUCTUSAGE_CHECKLISTCOUNT_COL;
    private static final String CHECKLISTCOUNT_FULLCOLUMN = DBProductusageTableConstants.PRODUCTUSAGE_CHECKLISTCOUNT_COL_FULL;

    Cursor clcsr;

    private static final String ORDERED_PRODUCTS = DBConstants.CALCULATED_PRODUTSORDERED_NAME;

    private static final int BYPRODUCT = 0;
    private static final int BYSHOP = 1;
    private static final int BYAISLE = 2;
    private static final int BYPRICE = 3;
    private static final String SORTASCENDING = DBConstants.SQLORDERASCENDING;
    private static final String SORTDESCENDING = DBConstants.SQLORDERDESCENDING;
    private static String filter = "";
    private static String orderby = PRODUCTNAME_FULLCOLUMN + SORTASCENDING;
    private static int orderfld = BYPRODUCT;
    private static boolean ordertype = true;
    private static boolean sortchanged = false;
    private static String lastmessage = "";

    private static String currentproductname = "";
    private static String currentshopname = "";
    private static String currentaislename = "";

    long currentproductid = 0;
    long currentshopid = 0;
    long currentaisleid = 0;

    TextView messagebar;
    TextView donebutton;
    TextView resetbutton;
    ListView checklist;
    LinearLayout checklistheading;

    AdapterChecklist checklistadapater;


    protected void onCreate(Bundle savedInstanceState) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist);
        context = this;
        thisactivity = (Activity) context;
        caller = getIntent().getStringExtra(
                StandardAppConstants.INTENTKEY_CALLINGACTIVITY
        );

        menucolorcode = StandardAppConstants.INTENTKEY_MENUCOLORCODE;
        passedmenucolorcode = getIntent().getIntExtra(menucolorcode,0);

        messagebar = (TextView) findViewById(R.id.checklist_messagebar);
        donebutton = (TextView) findViewById(R.id.checklist_donebutton);
        resetbutton = (TextView) findViewById(R.id.checklist_resetbutton);
        checklist = (ListView) findViewById(R.id.checklist_checklist);
        checklistheading = (LinearLayout) findViewById(R.id.checklist_checklist_heading);

        msg = "Preparing ColorCoding";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        actionbar = getSupportActionBar();
        ActionColorCoding.setActionBarColor(this,getIntent(),actionbar);
        primary_color = ActionColorCoding.setHeadingColor(this,getIntent(),0);
        h1 = ActionColorCoding.setHeadingColor(this,getIntent(),1);
        h2 = ActionColorCoding.setHeadingColor(this,getIntent(),2);
        h3 = ActionColorCoding.setHeadingColor(this,getIntent(),3);
        h4 = ActionColorCoding.setHeadingColor(this,getIntent(),4);
        ActionColorCoding.setActionButtonColor(donebutton, primary_color);
        ActionColorCoding.setActionButtonColor(resetbutton,primary_color);
        checklistheading.setBackgroundColor(h1);

        ActionColorCoding.setSwatches(findViewById(android.R.id.content),this.getIntent());

        /**
         * Perpare to use the database and the underlying methods
         */
        msg = "Preparing Database Access";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        dbdao = new DBDAO(this);
        dbshopmethods = new DBShopMethods(this);
        dbaislemethods = new DBAisleMethods(this);
        dbproductmethods = new DBProductMethods(this);
        dbpumethods = new DBProductUsageMethods(this);
        dbshoplistmethods = new DBShopListMethods(this);
        dbrulemethods = new DBRuleMethods(this);

        clcsr = dbpumethods.getCheckList(filter,orderby);
        checklistadapater = new AdapterChecklist(this,clcsr,0,getIntent());
        checklist.setAdapter(checklistadapater);

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
        resumestate = StandardAppConstants.RESUMSTATE_NORMAL;
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
        clcsr.close();
    }

    /**************************************************************************
     * Button Click Handler
     *
     * @param view The view (i.e the TextView that was clicked)
     */
    public void actionButtonClick(View view) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        int position;
        switch (view.getId()) {
            case R.id.checklist_donebutton:
                msg = "Finishing";
                LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
                this.finish();
                break;
            case R.id.checklist_resetbutton:
                msg = "Restting Checklist Items";
                LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
                dbpumethods.resetChecklistCheckedStatus();
                clcsr = dbpumethods.getCheckList(filter,orderby);
                checklistadapater.swapCursor(clcsr);
                break;
            case R.id.checklist_order1_button:
                position = (int) view.getTag();
                clcsr.moveToPosition(position);
                msg = "Ordering 1 Product=" + clcsr.getString(
                        clcsr.getColumnIndex(PRODUCTNAME_COLUMN)
                );
                LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
                dbshoplistmethods.addOrUpdateShopListEntry(
                        clcsr.getLong(clcsr.getColumnIndex(AISLEREF_COLUMN)),
                        clcsr.getLong(clcsr.getColumnIndex(PRODUCTREF_COLUMN)),
                        1,
                        true, false
                );
                clcsr = dbpumethods.getCheckList(filter,orderby);
                checklistadapater.swapCursor(clcsr);
                msg = "Ordered 1 Product=" +
                        clcsr.getString(clcsr.getColumnIndex(PRODUCTNAME_COLUMN)) +
                        " and Resetting List (swapping cursor for adapter)";
                LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
                break;
            case R.id.checklist_less1_button:
                position = (int) view.getTag();
                clcsr.moveToPosition(position);
                msg = "Recalling 1 Product=" + clcsr.getString(
                        clcsr.getColumnIndex(PRODUCTNAME_COLUMN)
                );
                LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
                dbshoplistmethods.addOrUpdateShopListEntry(
                        clcsr.getLong(clcsr.getColumnIndex(AISLEREF_COLUMN)),
                        clcsr.getLong(clcsr.getColumnIndex(PRODUCTREF_COLUMN)),
                        -1,
                        true, false
                );
                clcsr = dbpumethods.getCheckList(filter,orderby);
                checklistadapater.swapCursor(clcsr);
                msg = "Recalled 1 Product=" +
                        clcsr.getString(clcsr.getColumnIndex(PRODUCTNAME_COLUMN)) +
                        " and Resetting List (swapping cursor for adapter)";
                LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
                break;
            case R.id.checklist_checkoff_button:
                position = (int) view.getTag();
                clcsr.moveToPosition(position);
                msg = "Toggling Check-off for Product=" + clcsr.getString(
                        clcsr.getColumnIndex(PRODUCTNAME_COLUMN)
                );
                LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
                dbpumethods.setChecklistCheckedStatus(
                        clcsr.getLong(clcsr.getColumnIndex(AISLEREF_COLUMN)),
                        clcsr.getLong(clcsr.getColumnIndex(PRODUCTREF_COLUMN))

                );
                clcsr = dbpumethods.getCheckList(filter,orderby);
                checklistadapater.swapCursor(clcsr);
                msg = "Toggled Check-off for Product=" +
                        clcsr.getString(clcsr.getColumnIndex(PRODUCTNAME_COLUMN)) +
                        " Check-off status =" + Boolean.toString(
                        clcsr.getInt(clcsr.getColumnIndex(CHECKLISTFLAG_COLUMN)) > 1) +
                        " and Resetting List (swapping cursor for adapter)";
                LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
                break;
            default:
                break;
        }
    }

    /**************************************************************************
     * setMessage - set the Message regarding the lat action performed
     * Note as an action may have altered the database getDBcounts
     * is called and the title changed
     *
     * @param cla   the sa
     * @param msg  The message to be displayed.
     * @param flag Message imnportant, if true Yellow text, esle green
     */
    public void setMessage(CheckListActivity cla, String msg, boolean flag) {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);

        TextView messagebar = (TextView) cla.findViewById(R.id.checklist_messagebar);
        messagebar.setText(context.getResources().getString(
                R.string.messagebar_prefix_lastaction) + " " + msg);
        if (flag) {
            messagebar.setTextColor(Color.YELLOW);
        } else {
            messagebar.setTextColor(Color.GREEN);
        }
        messagebar.setVisibility(View.VISIBLE);
        cla.actionbar.setTitle(getResources().getString(R.string.stocklabel));
    }

    /**************************************************************************
     * sortClick - Handle list heading clicks (i.e. sort by that field)
     *
     * @param view the view that was clicked
     */
    public void sortClick(View view) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        lastmessage = getResources().getString(R.string.stocklabel) +
                " in Shop/Aisle sorted by ";
        switch (view.getId()) {
            case R.id.checklist_checklist_heading_productname:
                getOrderBy(PRODUCTNAME_FULLCOLUMN,BYPRODUCT);
                lastmessage = lastmessage + " PRODUCT NAME (";
                break;
            case R.id.checklist_checklist_heading_shopname:
                getOrderBy(SHOPNAME_FULLCOLUMN,BYSHOP);
                lastmessage = lastmessage + "  SHOP NAME (";
                break;
            case R.id.checklist_checklist_heading_aislename:
                getOrderBy(AISLENAME_FULLCOLUMN,BYAISLE);
                lastmessage = lastmessage + " AISLE (";
                break;
            case R.id.checklist_checklist_heading_cost:
                getOrderBy(PRODUCTUSAGECOST_FULLCOLUMN,BYPRICE);
                lastmessage = lastmessage + " PRICE (";
                break;
            default:
                break;
        }
        if (sortchanged) {
            clcsr = dbpumethods.getCheckList(filter,orderby);
            checklistadapater.swapCursor(clcsr);
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
    public void doNothing(RequestDialogParameters values) {
    }
}
