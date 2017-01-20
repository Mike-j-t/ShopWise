package mjt.shopwise;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

/******************************************************************************
 * AislessActivity - List Aisles allowing them to be edited, stocked, or deleted
 * List can also be sorted by clicking on a label. Clicking
 * a label will toggle between descending and ascending order.
 *
 * Stock means to add a product to an aisle, this can be
 * achieved from a shop, aisle or product. The Stock button
 * will only be available when at least 1 aisle and 1 product
 * exists.
 */

public class AislesActivity extends AppCompatActivity{

    private static final String THIS_ACTIVITY = "AislesActivity";
    private static String caller;
    private static int calledmode;

    private static final int BYAISLE = 0;
    private static final int BYORDER = 1;
    private static final String SORTASCENDING = DBConstants.SQLORDERASCENDING;
    private static final String SORTDESCENDING = DBConstants.SQLORDERDESCENDING;
    private static String shopfilter = "";


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
    /**
     * The Passedmenucolorcode.
     */
    int passedmenucolorcode;

    TextView messagebar;
    TextView donebutton;
    TextView newbutton;
    ListView aiselist;
    Spinner selectshoplist;
    LinearLayout aislelistheading;
    AdapterAisleList aislelistadapter;
    AdapterShopList selectshoplistadapter;
    TextView selectshoplabel;


    DBDAO dbdao;
    DBShopMethods dbshopmethods;
    DBAisleMethods dbaislemethods;
    DBProductMethods dbproductmethods;

    Cursor alcsr;
    Cursor slcsr;

    boolean showdetails = false;
    private static final String AISLEID_COLUMN = DBAislesTableConstants.AISLES_ID_COL;
    private static final String AILSEID_FULLCOLUMN = DBAislesTableConstants.AISLES_ID_COL_FULL;
    private static final String AISLENAME_COLUMN = DBAislesTableConstants.AISLES_NAME_COL;
    private static final String AISLENAME_FULLCOLUMN = DBAislesTableConstants.AISLES_NAME_COL_FULL;
    private static final String AISLEORDER_COLUMN = DBAislesTableConstants.AISLES_ORDER_COL;
    private static final String AISLEORDER_FULLCOLUMN = DBAislesTableConstants.AISLES_ORDER_COL_FULL;
    private static final String AISLESHOPREF_COLUMN = DBAislesTableConstants.AISLES_SHOPREF_COL;
    private static final String AISLESHOPREF_FULLCOLUMN = DBAislesTableConstants.AISLES_SHOPREF_COL_FULL;
    private static final String SHOPID_COLUMN = DBShopsTableConstants.SHOPS_ID_COL;
    private static final String SHOPNAME_COLUMN = DBShopsTableConstants.SHOPS_NAME_COL;
    private static final String SHOPCITY_COLUMN = DBShopsTableConstants.SHOPS_CITY_COL;
    private static final String SHOPORDER_COLUMN = DBShopsTableConstants.SHOPS_ORDER_COL;
    private static final String SHOPID_FULLCOLUMN = DBShopsTableConstants.SHOPS_ID_COL_FULL;
    private static final String SHOPNAME_FULLCOLUMN = DBShopsTableConstants.SHOPS_NAME_COL_FULL;
    private static final String SHOPCITY_FULLCOLUMN = DBShopsTableConstants.SHOPS_CITY_COL_FULL;
    private static final String SHOPORDER_FULLCOLUMN = DBShopsTableConstants.SHOPS_ORDER_COL_FULL;

    static String orderby = AISLENAME_FULLCOLUMN + DBConstants.SQLORDERASCENDING;
    static String shopsorderby = SHOPNAME_FULLCOLUMN + DBConstants.SQLORDERASCENDING;
    static int orderfld = BYAISLE;
    static boolean ordertype = true;
    static boolean sortchanged = false;
    static String lastmessage = "";
    static String currentaislename = "";
    static String currentshopname = "";
    static long currentshopid;
    static boolean aislesadapterset = false;

    static int shopcount = 0;
    static int aislecount = 0;
    static int productcount = 0;

    private int resumestate = StandardAppConstants.RESUMSTATE_NORMAL;
    private Activity thisactivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aisles);
        context = this;
        thisactivity = (Activity) context;
        caller = getIntent().getStringExtra(
                StandardAppConstants.INTENTKEY_CALLINGACTIVITY
        );
        calledmode = getIntent().getIntExtra(
                StandardAppConstants.INTENTKEY_CALLINGMODE,0);
        menucolorcode = StandardAppConstants.INTENTKEY_MENUCOLORCODE;
        passedmenucolorcode = getIntent().getIntExtra(menucolorcode,0);
        donebutton = (TextView) findViewById(R.id.aisles_donebutton);
        newbutton = (TextView) findViewById(R.id.aisles_newaislebutton);
        aiselist = (ListView) findViewById(R.id.aisles_aislelist);
        aislelistheading = (LinearLayout) findViewById(R.id.aisles_aislelist_heading);
        messagebar = (TextView) findViewById(R.id.aisles_messagebar);
        selectshoplist = (Spinner) findViewById(R.id.aisles_shopspinner);
        selectshoplabel = (TextView) findViewById(R.id.aisles_shopspinner_shopname_label);

        /**
         * Apply Color Coding
         */
        actionbar = getSupportActionBar();
        ActionColorCoding.setActionBarColor(this,getIntent(),actionbar);
        primary_color = ActionColorCoding.setHeadingColor(this,getIntent(),0);
        h1 = ActionColorCoding.setHeadingColor(this,getIntent(),1);
        h2 = ActionColorCoding.setHeadingColor(this,getIntent(),2);
        h3 = ActionColorCoding.setHeadingColor(this,getIntent(),3);
        h4 = ActionColorCoding.setHeadingColor(this,getIntent(),4);
        ActionColorCoding.setActionButtonColor(donebutton, primary_color);
        ActionColorCoding.setActionButtonColor(newbutton, primary_color);
        aislelistheading.setBackgroundColor(h1);
        ActionColorCoding.setActionButtonColor(selectshoplist,h2);
        selectshoplabel.setTextColor(primary_color);

        dbdao = new DBDAO(this);
        dbshopmethods = new DBShopMethods(this);
        dbaislemethods = new DBAisleMethods(this);
        dbproductmethods = new DBProductMethods(this);
        setDBCounts();

        slcsr = dbshopmethods.getShops("", shopsorderby);
        selectshoplistadapter = new AdapterShopList(this,
                slcsr,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER,
                getIntent(),
                true);

        selectshoplist.setAdapter(selectshoplistadapter);
        setSelectShopListener();

        alcsr = dbaislemethods.getAisles(shopfilter,orderby);
        aislelistadapter = new AdapterAisleList(
                this,
                alcsr,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER,
                getIntent()
        );
        aiselist.setAdapter(aislelistadapter);
        aislesadapterset = true;

        aiselist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView,
                                    View view,
                                    int position,
                                    long id) {
                listItemClick(view, position, id);
            }
        });
        aiselist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view,
                                           int position,
                                           long id) {
                listItemLongClick(view, position,id);
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
        alcsr = dbaislemethods.getAisles(shopfilter,orderby);
        aislelistadapter.swapCursor(alcsr);
        resumestate = StandardAppConstants.RESUMSTATE_NORMAL;
        setDBCounts();
    }

    /**************************************************************************
     * onDestroy - do any clean up before th application is destroyed
     * e.g. close any open cursors and then close the database
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        slcsr.close();
    }

    /**************************************************************************
     * Button Click Handler
     *
     * @param view The view (i.e the TextView that was clicked)
     */
    public void actionButtonClick(View view) {
        switch (view.getId()) {
            case R.id.aisles_donebutton:
                this.finish();
                break;
            case R.id.aisles_newaislebutton:
                addAisle();
                break;
            default:
                break;
        }
    }

    /**************************************************************************
     * addAisle - Invoke AisleAddEditActivity passing the respective intent
     *              extras :-
     *                  menucolorcode as was sent to this Activity
     *                  this Activity's name
     *                  the mode i.e. Add as opposed to edit
     */
    public void addAisle() {
        Intent intent = new Intent(this,AislesAddEditActivity.class);
        intent.putExtra(menucolorcode,passedmenucolorcode);
        intent.putExtra(StandardAppConstants.INTENTKEY_AISLESHOPREF,
                slcsr.getLong(slcsr.getColumnIndex(
                        DBShopsTableConstants.SHOPS_ID_COL)));
        intent.putExtra(StandardAppConstants.INTENTKEY_CALLINGACTIVITY,
                THIS_ACTIVITY);
        intent.putExtra(StandardAppConstants.INTENTKEY_CALLINGMODE,
                StandardAppConstants.CM_ADD);
        startActivity(intent);
    }

    /**************************************************************************
     *
     * @param values
     */
    public void aisleEdit(RequestDialogParameters values) {
        AislesActivity activity = (AislesActivity) values.getPassedactivity();
        long aisleid = values.getLong1();
        Intent intent = new Intent(this,AislesAddEditActivity.class);
        intent.putExtra(
                StandardAppConstants.INTENTKEY_CALLINGACTIVITY,
                THIS_ACTIVITY
        );
        intent.putExtra(
                StandardAppConstants.INTENTKEY_CALLINGMODE,
                StandardAppConstants.CM_EDIT
        );
        intent.putExtra(
                StandardAppConstants.INTENTKEY_AISLEID,
                alcsr.getLong(
                        slcsr.getColumnIndex(AISLEID_COLUMN)));
        intent.putExtra(
                StandardAppConstants.INTENTKEY_AISLENAME,
                alcsr.getString(
                        alcsr.getColumnIndex(AISLENAME_COLUMN)));
        intent.putExtra(
                StandardAppConstants.INTENTKEY_AISLEORDER,
                alcsr.getInt(
                        alcsr.getColumnIndex(AISLEORDER_COLUMN)));
        intent.putExtra(
                StandardAppConstants.INTENTKEY_AISLESHOPREF,
                alcsr.getLong(
                        alcsr.getColumnIndex(AISLESHOPREF_COLUMN)));
        intent.putExtra(menucolorcode,passedmenucolorcode);
        startActivity(intent);
    }

    /**************************************************************************
     *
     * @param values
     */
    public void aisleDelete(RequestDialogParameters values) {
        Activity activity = (values.getPassedactivity());
        AislesActivity  aa = (AislesActivity) activity;
        aa.dbaislemethods.deleteAisle(values.getLong1(),false);
        aa.alcsr = dbaislemethods.getAisles(shopfilter,orderby);
        aa.aislelistadapter.swapCursor(alcsr);
        aa.setMessage(aa,"Aisle " +
                currentaislename +
                " Deleted.",
                true);
        setDBCounts();
    }

    /**************************************************************************
     *
     * @param values    a RequestDialogParameters instance
     */
    public void aisleStock(RequestDialogParameters values) {
        long aisleid = values.getLong1();
        Intent intent = new Intent(this,StockActivity.class);
        intent.putExtra(
                StandardAppConstants.INTENTKEY_CALLINGACTIVITY,
                THIS_ACTIVITY
        );
        intent.putExtra(
                StandardAppConstants.INTENTKEY_CALLINGMODE,
                StandardAppConstants.CM_STOCKFROMAISLE
        );
        intent.putExtra(
                StandardAppConstants.INTENTKEY_AISLEID,
                alcsr.getLong(
                        slcsr.getColumnIndex(AISLEID_COLUMN)));
        intent.putExtra(
                StandardAppConstants.INTENTKEY_AISLENAME,
                alcsr.getString(
                        alcsr.getColumnIndex(AISLENAME_COLUMN)));
        intent.putExtra(
                StandardAppConstants.INTENTKEY_AISLEORDER,
                alcsr.getInt(
                        alcsr.getColumnIndex(AISLEORDER_COLUMN)));
        intent.putExtra(
                StandardAppConstants.INTENTKEY_AISLESHOPREF,
                alcsr.getLong(
                        alcsr.getColumnIndex(AISLESHOPREF_COLUMN)));
        intent.putExtra(menucolorcode,passedmenucolorcode);
        startActivity(intent);
    }

    /**************************************************************************
     * sortClick - Handle list heading clicks (i.e. sort by that field)
     *
     * @param view the view that was clicked
     */
    public void sortClick(View view) {
        lastmessage = getResources().getString(R.string.aisleslabel) +
                " sorted by ";
        switch (view.getId()) {
            case R.id.aisles_aislelist_heading_aislename:
                getOrderBy(AISLENAME_FULLCOLUMN,BYAISLE);
                lastmessage = lastmessage + " AISLE NAME (";
                break;
            case R.id.aisles_aislelist_heading_aisleorder:
                getOrderBy(AISLEORDER_FULLCOLUMN,BYORDER);
                lastmessage = lastmessage + " AISLE ORDER (";
            default:
                break;
        }
        if (sortchanged) {
            alcsr = dbaislemethods.getAisles(shopfilter,orderby);
            aislelistadapter.swapCursor(alcsr);
            if (ordertype) {
                lastmessage = lastmessage + "ascending)";
            } else {
                lastmessage = lastmessage + "descending)";
            }
            setMessage(this,lastmessage,false);
        }
    }

    /**************************************************************************
     * listItemClick - Handle Clicking a List Item
     *
     * @param view     The view that was long clicked.
     * @param position The position of the clicked item in the list
     * @param id       The row id of the item that was clicked.
     */
    public void listItemClick(View view, int position, long id) {
        setDBCounts();
        long aisleid = alcsr.getLong(
                alcsr.getColumnIndex(AISLEID_COLUMN));
        currentaislename = alcsr.getString(
                alcsr.getColumnIndex(AISLENAME_COLUMN));
        //Class cls = null;
        String classname = this.getClass().getCanonicalName();
        String positivebuttontext = getResources().getString(R.string.editbutton);
        String positiveaction = "aisleEdit";
        String negativebuttontext = getResources().getString(R.string.stockbutton);
        String negativeaction = "aisleStock";
        String neutralbuttontext = getResources().getString(R.string.cancelbutton);
        String neutralaction = "";
        String title = "Edit or Stock Aisle = " + currentaislename;
        String message = "Available Options are " + neutralbuttontext + "," +
                positivebuttontext + " AND " + negativebuttontext + "\n\n" +
                "\t" + neutralbuttontext + " returns to Aisles, doing nothing." +
                "\t" + positivebuttontext + " allows the Aisle to be changed." +
                "\t" + negativebuttontext + " allows assignment of products to " +
                "Aisles.";
        if ((aislecount < 1) || (productcount < 1)) {
            negativebuttontext = "";
        }
        MixTripleLongTripleInt values = new MixTripleLongTripleInt();
        values.setMIXTRPPLONGINT(aisleid,0,0,0,0,0);
        new RequestDialog().requestDialog(thisactivity,
                classname,
                title, message,
                positivebuttontext,negativebuttontext,neutralbuttontext,
                positiveaction,negativeaction,neutralaction,
                values);
    }

    /**************************************************************************
     *
     * @param view
     * @param position
     * @param id
     */
    public void listItemLongClick(View view, int position, long id) {
        long aisleid = alcsr.getLong(
                alcsr.getColumnIndex(AISLEID_COLUMN));
        currentaislename = alcsr.getString(
                alcsr.getColumnIndex(AISLENAME_COLUMN));
        String title = "Delete Aisle - " + currentaislename;
        String positivebuttontext = getResources().getString(R.string.deletebutton);
        String positiveaction = "aisleDelete";
        String negativebuttontext = "";
        String negativeaction = "";
        String neutralbuttontext = getResources().getString(R.string.cancelbutton);
        String neutralaction = "";

        ArrayList<String> impact = dbaislemethods.aisleDeleteImpact(aisleid);
        String tmsg = "";
        for (String msg: impact) {
            tmsg = tmsg + msg + "\n";
        }
        String message = "Deleting Aisle = " + currentaislename + tmsg;
        MixTripleLongTripleInt values = new MixTripleLongTripleInt();
        values.setMIXTRPPLONGINT(aisleid,0,0,0,0,0);

        String classname = this.getClass().getCanonicalName();
        new RequestDialog().requestDialog(thisactivity, classname,
                title,message,
                positivebuttontext,negativebuttontext,neutralbuttontext,
                positiveaction,negativeaction, neutralaction,
                values);
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
     * setMessage - set the Message regarding the lat action performed
     * Note as an action may have altered the database getDBcounts
     * is called and the title changed
     *
     * @param aa   the aa
     * @param msg  The message to be displayed.
     * @param flag Message imnportant, if true Yellow text, esle green
     */
    public void setMessage(AislesActivity aa, String msg, boolean flag) {

        TextView messagebar = (TextView) aa.findViewById(
                R.id.aisles_messagebar
        );
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
     * setDBCounts - extract the row counts from the database for relevant
     *                  tables.
     */
    private void setDBCounts() {
        shopcount = dbshopmethods.getShopCount();
        aislecount = dbaislemethods.getAisleCount();
        productcount = dbproductmethods.getProductCount();
    }

    /**************************************************************************
     *
     */
    void setSelectShopListener() {
        selectshoplist.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView,
                                       View view,
                                       int i,
                                       long l) {
                currentshopid = slcsr.getLong(slcsr.getColumnIndex(
                        SHOPID_COLUMN
                ));
                currentshopname = slcsr.getString(slcsr.getColumnIndex(
                        SHOPNAME_COLUMN
                ));
                shopfilter = DBAislesTableConstants.AISLES_SHOPREF_COL_FULL +
                        " = " +
                        Long.toString(currentshopid);
                alcsr = dbaislemethods.getAisles(shopfilter,orderby);
                if (aislesadapterset) {
                    aislelistadapter.swapCursor(alcsr);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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
        aisleDelete(null);
        aisleEdit(null);
    }
}
