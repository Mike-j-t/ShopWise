package mjt.shopwise;

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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import mjt.displayhelp.DisplayHelp;

import static mjt.sqlwords.SQLKWORD.*;

/**
 * CheckList Activity - Display the Checklist allowing Shopping List
 * entries to be added adjusted
 *
 * The CheckTist displays Stock that has been marked as appearing in the
 * checklist.
 * If the item is the first in the Storage location then the Storage
 * location is showsn as a header (i.e. once per location).
 * Each row will then display
 *      the Product, the Shop, the Aisle and the Price (1st row per item)
 *      the Level, the # in the Shopping List and buttons (last row per item)
 *      Buttons will be Check-off, ADD and LESS unless checked-off when only
 *      the UNCHECK button is shown.
 *      Check-off markes the item as checked-off
 *      ADD will add 1 of the item to the Shopping list.
 *      LESS wil subtract 1 of the item from the Shopping List unless the
 *          quantity is 0 (i.e. Shopping List can't have negative quantity).
 *      UNCHECK will revert a checked-off item to not being checked-off.
 *
 */

@SuppressWarnings({"FieldCanBeLocal", "WeakerAccess", "unused"})
public class CheckListActivity extends AppCompatActivity {

    @SuppressWarnings("unused")
    private final String THIS_ACTIVITY = "CheckListActivity";
    private final String LOGTAG = "SW_CLA";
    public static final String THISCLASS = CheckListActivity.class.getSimpleName();
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
    DBStorageMethods dbstoragemethods;

    @SuppressWarnings("unused")
    private final String SHOPID_COLUMN = DBShopsTableConstants.SHOPS_ID_COL;
    @SuppressWarnings("unused")
    private final String SHOPNAME_COLUMN = DBShopsTableConstants.SHOPS_NAME_COL;
    @SuppressWarnings("unused")
    private final String SHOPCITY_COLUMN = DBShopsTableConstants.SHOPS_CITY_COL;
    @SuppressWarnings("unused")
    private final String SHOPORDER_COLUMN = DBShopsTableConstants.SHOPS_ORDER_COL;
    @SuppressWarnings("unused")
    private final String SHOPID_FULLCOLUMN = DBShopsTableConstants.SHOPS_ID_COL_FULL;
    private final String SHOPNAME_FULLCOLUMN = DBShopsTableConstants.SHOPS_NAME_COL_FULL;
    @SuppressWarnings("unused")
    private final String SHOPCITY_FULLCOLUMN = DBShopsTableConstants.SHOPS_CITY_COL_FULL;
    @SuppressWarnings("unused")
    private final String SHOPORDER_FULLCOLUMN = DBShopsTableConstants.SHOPS_ORDER_COL_FULL;

    @SuppressWarnings("unused")
    private final String AISLEID_COLUMN = DBAislesTableConstants.AISLES_ID_COL;
    @SuppressWarnings("unused")
    private final String AILSEID_FULLCOLUMN = DBAislesTableConstants.AISLES_ID_COL_FULL;
    @SuppressWarnings("unused")
    private final String AISLENAME_COLUMN = DBAislesTableConstants.AISLES_NAME_COL;
    private final String AISLENAME_FULLCOLUMN = DBAislesTableConstants.AISLES_NAME_COL_FULL;
    @SuppressWarnings("unused")
    private final String AISLEORDER_COLUMN = DBAislesTableConstants.AISLES_ORDER_COL;
    @SuppressWarnings("unused")
    private final String AISLEORDER_FULLCOLUMN = DBAislesTableConstants.AISLES_ORDER_COL_FULL;
    @SuppressWarnings("unused")
    private final String AISLESHOPREF_COLUMN = DBAislesTableConstants.AISLES_SHOPREF_COL;
    @SuppressWarnings("unused")
    private final String AISLESHOPREF_FULLCOLUMN = DBAislesTableConstants.AISLES_SHOPREF_COL_FULL;

    @SuppressWarnings("unused")
    private static final String PRODUCTID_COLUMN = DBProductsTableConstants.PRODUCTS_ID_COL;
    private static final String PRODUCTNAME_COLUMN = DBProductsTableConstants.PRODUCTS_NAME_COL;
    @SuppressWarnings("unused")
    private static final String PRODUCTID_FULLCOLUMN = DBProductsTableConstants.PRODUCTS_ID_COL_FULL;
    private static final String PRODUCTNAME_FULLCOLUMN = DBProductsTableConstants.PRODUCTS_NAME_COL_FULL;
    @SuppressWarnings("unused")
    private static final String PRODUCTSTORAGEREF_COLUMH = DBProductsTableConstants.PRODUCTS_STORAGEREF_COL;
    @SuppressWarnings("unused")
    private static final String PRODUCTSTORAGEREF_FULLCOLUMN = DBProductsTableConstants.PRODUCTS_STORAGEREF_COL_FULL;
    @SuppressWarnings("unused")
    private static final String PRODUCTSTORAGEORDER_COLUMN = DBProductsTableConstants.PRODUCTS_STORAGEORDER_COL;
    @SuppressWarnings("unused")
    private static final String PRODUCTSTORAGEORDER_FULLCOLUMN = DBProductsTableConstants.PRODUCTS_STORAGEORDER_COL_FULL;

    @SuppressWarnings("unused")
    private static final String STORAGEID_COLUMN = DBStorageTableConstants.STORAGE_ID_COL;
    @SuppressWarnings("unused")
    private static final String STORAGEDID_FULLCOLUMN = DBStorageTableConstants.STORAGE_ID_COL_FULL;
    @SuppressWarnings("unused")
    private static final String STORAGEID_ALTCOLUMN = DBStorageTableConstants.STORAGE_ALTID_COL;
    @SuppressWarnings("unused")
    private static final String STORAGEID_ALTFULLCOLUMN = DBStorageTableConstants.STORAGE_ALTID_COL_FULL;
    @SuppressWarnings("unused")
    private static final String STORAGENAME_COLUMN = DBStorageTableConstants.STORAGE_NAME_COL;
    @SuppressWarnings("unused")
    private static final String STORAGENAME_FULLCOLUMN = DBStorageTableConstants.STORAGE_NAME_COL_FULL;
    @SuppressWarnings("unused")
    private static final String STORAGEORDER_COLUMN = DBStorageTableConstants.STORAGE_ORDER_COL;
    @SuppressWarnings("unused")
    private static final String STORAGEORDER_FULLCOLUMN = DBStorageTableConstants.STORAGE_ORDER_COL_FULL;

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
    private static final String CHECKLISTFLAG_COLUMN = DBProductusageTableConstants.PRODUCTUSAGE_CHECKLISTFLAG_COL;
    @SuppressWarnings("unused")
    private static final String CHECKLISTFLAG_FULLCOLUMN = DBProductusageTableConstants.PRODUCTUSAGE_CHECKLISTFLAG_COL_FULL;
    @SuppressWarnings("unused")
    private static final String CHECKLISTCOUNT_COLUMN = DBProductusageTableConstants.PRODUCTUSAGE_CHECKLISTCOUNT_COL;
    @SuppressWarnings("unused")
    private static final String CHECKLISTCOUNT_FULLCOLUMN = DBProductusageTableConstants.PRODUCTUSAGE_CHECKLISTCOUNT_COL_FULL;

    Cursor clcsr;

    @SuppressWarnings("unused")
    private static final String ORDERED_PRODUCTS = DBConstants.CALCULATED_PRODUCTSORDERED_NAME;

    private static final int BYPRODUCT = 0;
    private static final int BYSHOP = 1;
    private static final int BYAISLE = 2;
    private static final int BYPRICE = 3;
    @SuppressWarnings("unused")
    private static final int BYSTORAGENAME = 4;
    @SuppressWarnings("unused")
    private static final int BYSTORAGEORDER = 5;
    @SuppressWarnings("unused")
    private static final int BYPRODUCTSTORAGEORDER = 6;
    private static final String SORTASCENDING = SQLORDERASCENDING;
    private static final String SORTDESCENDING = SQLORDERDESCENDING;
    @SuppressWarnings("CanBeFinal")
    private static String filter = "";
    private static String orderby = PRODUCTNAME_FULLCOLUMN + SORTASCENDING;
    private static int orderfld = BYPRODUCT;
    private static boolean ordertype = true;
    private static boolean sortchanged = false;
    private static String lastmessage = "";

    TextView messagebar;
    TextView donebutton;
    TextView resetbutton;
    ListView checklist;
    LinearLayout checklistheading;
    TextView sortable;
    TextView clickable;
    TextView longclickable;

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

        messagebar = findViewById(R.id.checklist_messagebar);
        donebutton = findViewById(R.id.checklist_donebutton);
        resetbutton = findViewById(R.id.checklist_resetbutton);
        checklist = findViewById(R.id.checklist_checklist);
        checklistheading = findViewById(R.id.checklist_checklist_heading);
        sortable = findViewById(R.id.sortable);
        clickable = findViewById(R.id.clickable);
        longclickable = findViewById(R.id.longclickable);

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
        sortable.setText(getResources().getString(R.string.notsortable));
        sortable.setTextColor(primary_color);
        clickable.setVisibility(View.INVISIBLE);
        longclickable.setVisibility(View.INVISIBLE);

        ActionColorCoding.setSwatches(findViewById(android.R.id.content),this.getIntent());

        // Perpare to use the database and the underlying methods
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
                                R.string.title_help_checklist_activity),
                        R.array.help_checklist_activty,
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
        clcsr.close();
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
    public void setMessage(CheckListActivity cla,
                           String msg,
                           @SuppressWarnings("SameParameterValue") boolean flag) {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);

        TextView messagebar = cla.findViewById(R.id.checklist_messagebar);
        messagebar.setText(context.getResources().getString(
                R.string.messagebar_prefix_lastaction,msg));
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
    @SuppressWarnings({"unused", "EmptyMethod"})
    public void sortClick(View view) {
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
