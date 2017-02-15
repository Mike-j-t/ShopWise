package mjt.shopwise;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import static mjt.shopwise.ActionColorCoding.transparency_requied;

/**
 * Created by Mike092015 on 12/12/2016.
 */

public class AislesAddEditActivity extends AppCompatActivity {

    private static final String THIS_ACTIVITY = "AislesAddEditActivity";
    private static String caller;
    private static int calledmode;
    private static String defaultorder = "1000";

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

    /**
     * Views
     */
    TextView messagebar;
    LinearLayout input_aisleshop_linearlayout;
    TextView input_aisleshop_label;
    Spinner input_aisleshop_select;
    AdapterShopList shopselectadapter;
    LinearLayout input_aislename_linearlayout;
    TextView input_aislename_label;
    EditText input_aislename;
    LinearLayout input_aisleorder_linearlayout;
    TextView input_aisleorder_label;
    EditText input_aisleorder;
    LinearLayout aislelist_heading_linearlayout;
    TextView aislelist_heading_aislename;
    TextView aislelist_heading_aisleorder;
    ListView aislelist;
    AdapterAisleList aislelistadapter;
    TextView savebutton;
    TextView donebutton;

    DBDAO dbdao;
    DBShopMethods dbshopmethods;
    DBAisleMethods dbaislemethods;
    DBProductMethods dbproductmethods;
    Cursor slcsr;
    Cursor alcsr;

    private static final String SHOPID_COLUMN = DBShopsTableConstants.SHOPS_ID_COL;
    private static final String SHOPNAME_COLUMN = DBShopsTableConstants.SHOPS_NAME_COL;
    private static final String SHOPCITY_COLUMN = DBShopsTableConstants.SHOPS_CITY_COL;
    private static final String SHOPORDER_COLUMN = DBShopsTableConstants.SHOPS_ORDER_COL;
    private static final String SHOPID_FULLCOLUMN = DBShopsTableConstants.SHOPS_ID_COL_FULL;
    private static final String SHOPNAME_FULLCOLUMN = DBShopsTableConstants.SHOPS_NAME_COL_FULL;
    private static final String SHOPCITY_FULLCOLUMN = DBShopsTableConstants.SHOPS_CITY_COL_FULL;
    private static final String SHOPORDER_FULLCOLUMN = DBShopsTableConstants.SHOPS_ORDER_COL_FULL;

    private static final String AISLEID_COLUMN = DBAislesTableConstants.AISLES_ID_COL;
    private static final String AISLENAME_COLUMN = DBAislesTableConstants.AISLES_NAME_COL;
    private static final String AISLEORDER_COLUMN = DBAislesTableConstants.AISLES_ORDER_COL;
    private static final String AISLESHOPREF_COLUMN = DBAislesTableConstants.AISLES_SHOPREF_COL;
    private static final String AISLEID_FULLCOLUMN = DBAislesTableConstants.AISLES_ID_COL_FULL;
    private static final String AISLENAME_FULLCOLUMN = DBAislesTableConstants.AISLES_NAME_COL_FULL;
    private static final String AISLEORDER_FULLCOLUMN = DBAislesTableConstants.AISLES_ORDER_COL_FULL;
    private static final String AISLESHOPREF_FULLCOLUMN = DBAislesTableConstants.AISLES_SHOPREF_COL_FULL;

    long passedshopid = 0;
    long passaisleid = 0;
    String passedaislename = "";
    int passedaisleorder = 0;
    long passedshopref = 0;

    static String orderby = AISLENAME_FULLCOLUMN + SORTASCENDING;
    static String shopsorderby = SHOPNAME_FULLCOLUMN + SORTASCENDING;
    static int orderfld = BYAISLE;
    static boolean ordertype = true;
    static boolean sortchanged = false;
    static String lastmessage = "";
    static String currentaislename = "";
    static String currentshopname = "";
    static long currentshopid;
    static boolean aislesadapterset = false;
    static String currenttitle = "";

    static int shopcount = 0;
    static int aislecount = 0;
    static int productcount = 0;

    private int resumestate = StandardAppConstants.RESUMSTATE_NORMAL;
    private Activity thisactivity;

    @SuppressLint("SetTextI18n")
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aislesaddedit);
        context = this;
        thisactivity = (Activity) context;

        messagebar = (TextView) findViewById(R.id.aislesaddedit_messagebar);
        input_aisleshop_linearlayout = (LinearLayout) findViewById(R.id.inputaisleshop_linearlayout);
        input_aisleshop_label = (TextView) findViewById(R.id.inputaisleshop_label);
        input_aisleshop_select = (Spinner) findViewById(R.id.selectaisleshop);
        input_aislename_linearlayout = (LinearLayout) findViewById(R.id.inputaislename_linearlayout);
        input_aislename_label = (TextView) findViewById(R.id.inputaislename_label);
        input_aislename = (EditText) findViewById(R.id.inputaislename);
        input_aisleorder_linearlayout = (LinearLayout) findViewById(R.id.inputaisleorder_linearlayout);
        input_aisleorder_label = (TextView) findViewById(R.id.inputaisleorder_label);
        input_aisleorder = (EditText) findViewById(R.id.inputaisleorder);
        aislelist_heading_linearlayout = (LinearLayout) findViewById(R.id.aisleaddedit_aislelist_heading);
        aislelist_heading_aislename = (TextView) findViewById(R.id.aisleaddedit_aislelist_heading_aislename);
        aislelist_heading_aisleorder = (TextView) findViewById(R.id.aisleaddedit_aislelist_heading_aisleorder);
        aislelist = (ListView) findViewById(R.id.aisleaddedit_aislelist);
        savebutton = (TextView) findViewById(R.id.aisleaddedit_savebutton);
        donebutton = (TextView) findViewById(R.id.aisleaddedit_donebutton);
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
        ActionColorCoding.setActionButtonColor(savebutton, primary_color);
        ActionColorCoding.setActionButtonColor(input_aislename,h2 & transparency_requied);
        ActionColorCoding.setActionButtonColor(input_aisleorder,h2 & transparency_requied);
        aislelist_heading_linearlayout.setBackgroundColor(h1);
        ActionColorCoding.setActionButtonColor(input_aisleshop_select,h2);
        input_aislename_label.setTextColor(primary_color);
        input_aisleshop_label.setTextColor(primary_color);
        input_aisleorder_label.setTextColor(primary_color);

        dbdao = new DBDAO(this);
        dbshopmethods = new DBShopMethods(this);
        dbaislemethods = new DBAisleMethods(this);
        dbproductmethods = new DBProductMethods(this);

        // Prepare Shop Spinner and setup listener
        slcsr = dbshopmethods.getShops("",shopsorderby);
        shopselectadapter = new AdapterShopList(this,
                slcsr,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER,
                this.getIntent(),
                true
        );
        input_aisleshop_select.setAdapter(shopselectadapter);
        setSelectShopListener();

        // Prepare ListView of aisles currently linked to the current shop
        alcsr = dbaislemethods.getAisles(shopfilter,orderby, false);
        aislelistadapter = new AdapterAisleList(this,
                alcsr,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER,
                this.getIntent()
        );
        aislelist.setAdapter(aislelistadapter);
        aislesadapterset = true;

        // Get the data/values from the invoking activity
        // especially the processing mode, ADD or EDIT
        caller = getIntent().getStringExtra(
                StandardAppConstants.INTENTKEY_CALLINGACTIVITY
        );
        calledmode = getIntent().getIntExtra(
                StandardAppConstants.INTENTKEY_CALLINGMODE,
                StandardAppConstants.CM_CLEAR);

        // Assume ADD mode
        currenttitle = "AISLES - ADD";
        passedshopref = getIntent().getLongExtra(
                StandardAppConstants.INTENTKEY_AISLESHOPREF,0);
        setNewOrder(input_aisleorder,
                slcsr.getLong(
                        slcsr.getColumnIndex(
                                DBShopsTableConstants.SHOPS_ID_COL
                        )
                ));
        //input_aisleorder.setText(defaultorder);

        // However if EDIT mode then get the extra data
        if(calledmode == StandardAppConstants.CM_EDIT) {
            passedshopid = getIntent().getLongExtra(
                    StandardAppConstants.INTENTKEY_SHOPID,0);
            passedaislename = getIntent().getStringExtra(
                    StandardAppConstants.INTENTKEY_AISLENAME);
            passaisleid = getIntent().getLongExtra(
                    StandardAppConstants.INTENTKEY_AISLEID,0);
            passedaisleorder = getIntent().getIntExtra(
                    StandardAppConstants.INTENTKEY_AISLEORDER,0);
            input_aislename.setText(passedaislename);
            input_aisleorder.setText(Integer.toString(passedaisleorder));
            currentaislename = passedaislename;
            currenttitle = "AISLES - EDIT " + currentaislename;
            this.setTitle(currenttitle);
        }

        // irrespective of mode the Shop Selection Spinner needs to be
        // posaitioned to the shop selected according to the invocation
        slcsr.moveToPosition(-1);
        while (slcsr.moveToNext()) {
            if (slcsr.getLong(slcsr.getColumnIndex(
                    DBShopsTableConstants.SHOPS_ID_COL)) == passedshopref) {
                break;
            }
        }
        input_aisleshop_select.setSelection(slcsr.getPosition());
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
        //setDBCounts();
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
        alcsr = dbaislemethods.getAisles(shopfilter,orderby, false);
        aislelistadapter.swapCursor(alcsr);
        resumestate = StandardAppConstants.RESUMSTATE_NORMAL;
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
    }

    /**************************************************************************
     *
     * @param view
     */
    public void actionButtonClick(View view) {
        switch (view.getId()) {
            case R.id.aisleaddedit_donebutton:
                this.finish();
                break;
            case R.id.aisleaddedit_savebutton:
                aisleSave();
                break;
            default:
                break;
        }
    }

    /**************************************************************************
     * aisleSave - Save the Aisle being added or edited.
     *              Note will not save blank aisle
     */
    public void aisleSave() {
        boolean notdoneok = true;
        String aisleorder_str = input_aisleorder.getText().toString();
        String aislename = input_aislename.getText().toString();

        String aislenamelabel = getResources().getString(
                R.string.aislenamelabel) + " ";
        String aislelabel = getResources().getString(R.string.aislelabel) + " ";
        String aisleorderlabel = getResources().getString(R.string.orderlabel) + " ";
        String notsaved = getResources().getString(R.string.notsaved);
        String msg = "";

        // Aisle Name cannot be blank
        if (aislename.length() < 1) {
            msg = aislenamelabel + getResources().getString(R.string.inputblank) +
                    " " + aislelabel + " " + notsaved;
            setMessage(this,msg,notdoneok);
            return;
        }

        // Order cannot be blank
        if (aisleorder_str.length() < 1) {
            msg = aisleorderlabel + getResources().getString(R.string.inputblank) +
                    " " + aislelabel + " " + notsaved;
            setMessage(this,msg,notdoneok);
            input_aisleorder.setText(defaultorder);
            setNewOrder(input_aisleorder,
                    slcsr.getLong(
                            slcsr.getColumnIndex(
                                    DBShopsTableConstants.SHOPS_ID_COL
                            )
                    ));
            return;
        }

        int aisleorder = Integer.parseInt(aisleorder_str);
        msg = aislelabel + " " + aislename + " was ";
        switch (calledmode) {
            case StandardAppConstants.CM_ADD:
                dbaislemethods.insertAisle(aislename,
                        aisleorder,
                        slcsr.getLong(
                                slcsr.getColumnIndex(
                                        DBShopsTableConstants.SHOPS_ID_COL))
                );
                if (dbaislemethods.ifAisleAdded()) {
                    msg = msg + getResources().getString(R.string.addedok);
                    input_aislename.setText("");
                    setNewOrder(input_aisleorder,
                            slcsr.getLong(
                                    slcsr.getColumnIndex(
                                            DBShopsTableConstants.SHOPS_ID_COL
                                    )
                            ));
                    //input_aisleorder.setText(defaultorder);
                    notdoneok = false;
                } else {
                    msg = msg + notsaved;
                }
                break;
            case StandardAppConstants.CM_EDIT:
                dbaislemethods.modifyAisle(passaisleid,
                        aisleorder,
                        aislename);
                if (dbaislemethods.ifAisleUpdated()) {
                    msg = msg + getResources().getString(R.string.editedok);
                    notdoneok = false;
                } else {
                    msg = msg + notsaved;
                }
        }
        alcsr = dbaislemethods.getAisles(shopfilter,orderby,false);
        aislelistadapter.swapCursor(alcsr);
        setMessage(this,msg,notdoneok);
    }

    /**************************************************************************
     * sortClick - Handle list heading clicks (i.e. sort by that field)
     *
     * @param view the view that was clicked
     */
    public void sortClick(View view) {
        lastmessage = "List of Aisles sorted by ";
        switch (view.getId()) {
            case R.id.aisleaddedit_aislelist_heading_aislename:
                getOrderBy(AISLENAME_FULLCOLUMN,BYAISLE);
                lastmessage = lastmessage + " AISLE NAME in ";
                break;
            case R.id.aisleaddedit_aislelist_heading_aisleorder:
                getOrderBy(AISLEORDER_FULLCOLUMN,BYORDER);
                lastmessage = lastmessage + " AISLE ORDER in";
            default:
                break;
        }
        if (sortchanged) {
            alcsr = dbaislemethods.getAisles(shopfilter,orderby, false);
            aislelistadapter.swapCursor(alcsr);
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
    /**
    private void setDBCounts() {
        shopcount = dbshopmethods.getShopCount();
        aislecount = dbaislemethods.getAisleCount();
        productcount = dbproductmethods.getProductCount();
    }
     **/

    /**************************************************************************
     *
     * @param edittext  the EditText into which the new (next proposed) order
     *                  is to be placed.
     * @param shopid    the shopid that this is relevant for
     */
    @SuppressLint("SetTextI18n")
    private void setNewOrder(EditText edittext, Long shopid) {
        int highorder = dbaislemethods.getHighestAisleOrderPerShop(shopid) + 100;
        if (highorder < 1000) {
            highorder = 1000;
        }
        edittext.setText(Integer.toString(highorder));
    }

    /**************************************************************************
     * setMessage - set the Message regarding the lat action performed
     * Note as an action may have altered the database getDBcounts
     * is called and the title changed
     *
     * @param activity      the invoking activity
     * @param msg           The message to be displayed.
     * @param flag          Message importance, if true Yellow text, esle green
     */
    public void setMessage(AislesAddEditActivity activity, String msg, boolean flag) {

        TextView messagebar = (TextView) activity.findViewById(R.id.aislesaddedit_messagebar);
        messagebar.setText(context.getResources().getString(
                R.string.messagebar_prefix_lastaction) + " " + msg);
        if (flag) {
            messagebar.setTextColor(Color.YELLOW);
        } else {
            messagebar.setTextColor(Color.GREEN);
        }
        messagebar.setVisibility(View.VISIBLE);
        //activity.setDBCounts();
        String shopslabel = activity.getResources().getString(R.string.aisleslabel) +
                " (number of Aisles = " +
                Integer.toString(aislecount) +
                ")";
        activity.actionbar.setTitle(currenttitle);
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
     *
     */
    void setSelectShopListener() {
        input_aisleshop_select.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                currentshopid = slcsr.getLong(slcsr.getColumnIndex(
                        SHOPID_COLUMN
                ));
                currentshopname = slcsr.getString(slcsr.getColumnIndex(
                        SHOPNAME_COLUMN
                ));
                shopfilter = DBAislesTableConstants.AISLES_SHOPREF_COL_FULL +
                        " = " +
                        Long.toString(currentshopid);
                alcsr = dbaislemethods.getAisles(shopfilter,orderby, false);
                if (aislesadapterset) {
                    aislelistadapter.swapCursor(alcsr);
                }
                if (calledmode == StandardAppConstants.CM_ADD) {
                    input_aislename.setText("");
                    setNewOrder(input_aisleorder, currentshopid);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
}
