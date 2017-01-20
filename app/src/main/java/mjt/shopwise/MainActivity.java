package mjt.shopwise;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;

import static mjt.shopwise.StandardAppConstants.AISLESAPPVALNAME;
import static mjt.shopwise.StandardAppConstants.CHECKLISTAPPVALNAME;
import static mjt.shopwise.StandardAppConstants.INTENTKEY_CALLINGACTIVITY;
import static mjt.shopwise.StandardAppConstants.INTENTKEY_CALLINGMODE;
import static mjt.shopwise.StandardAppConstants.MAINACTIVITYOPTIONLIST;
import static mjt.shopwise.StandardAppConstants.MENUOPTIONS;
import static mjt.shopwise.StandardAppConstants.ORDERAPPVALNAME;
import static mjt.shopwise.StandardAppConstants.PRODUCTSAPPVALNAME;
import static mjt.shopwise.StandardAppConstants.RULESAPPVALNAME;
import static mjt.shopwise.StandardAppConstants.SHOPPINGAPPVALNAME;
import static mjt.shopwise.StandardAppConstants.SHOPSAPPVALNAME;
import static mjt.shopwise.StandardAppConstants.STOCKAPPVALNAME;
import static mjt.shopwise.StandardAppConstants.TOOLSAPPVALNAME;

/**
 * Main/Start Activity for ShopWise
 */
public class MainActivity extends AppCompatActivity {

    private static final String THIS_ACTIVITY = "MainActivity";
    private static final String LOGTAG = "SW-MA";

    /**
     * The Db.
     */
    public DBHelper db;
    private DBDAO dbdao;
    private DBShopMethods dbshopmethods;
    private DBAisleMethods dbaislemethods;
    private DBProductMethods dbproductmethods;
    private DBAppvaluesMethods dbappvaluesmethods;
    private DBProductUsageMethods dbproductusagemethods;
    private DBRuleMethods dbrulemethods;

    private ListView options_listview;
    /**
     * The Options adapter.
     */
    AdapterMainActivityOptionsMenu options_adapter;
    private Cursor mocsr;


    /**
     * The Shopcount.
     */
    static int shopcount = 0;
    /**
     * The Aislecount.
     */
    static int aislecount = 0;
    /**
     * The Productcount.
     */
    static int productcount = 0;
    /**
     * The Productusagecount.
     */
    static int productusagecount = 0;
    /**
     * The Shoplistcount.
     */
    static int shoplistcount = 0;
    /**
     * The Rulecount.
     */
    static int rulecount = 0;
    /**
     * The Appvaluecount.
     */
    static int appvaluecount = 0;

    private static int resumestate = StandardAppConstants.RESUMSTATE_NORMAL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= 23) {
            ExternalStoragePermissions.verifyStoragePermissions(this);
        }
        db = new DBHelper(this,DBConstants.DATABASE_NAME,null,1);
        dbdao = new DBDAO(this);
        dbshopmethods = new DBShopMethods(this);
        dbaislemethods = new DBAisleMethods(this);
        dbproductmethods = new DBProductMethods(this);
        dbappvaluesmethods = new DBAppvaluesMethods(this);
        dbproductusagemethods = new DBProductUsageMethods(this);
        dbrulemethods = new DBRuleMethods(this);
        getDBCounts();

        /**
         * Setup Color coding i.e. Load colors from Appvalues Table. If they
         *  do not exist then default colors are genereated and stored in the
         *  Appvalues table. Also force a store of values to enforce any updates.
         */
        ActionColorCoding acc = ActionColorCoding.getInstance(this);
        ActionColorCoding.forceStoreColors(this);

        setContentView(R.layout.activity_main);

        /**
         * Expand the Database if necessary
         * i.e. allows addition of columns and tables. This being based upon
         * a comparison of the DataBase schema against the actual Database
         * (see
         */
        db.expand(null, true);

        /**
         * Prepare to use a ListView as the Main Options Menu and then build
         * the options menu (do twice to fully resolve any menu changes)
         */
        options_listview = (ListView) this.findViewById(R.id.activity_main_OptionsMenu);
        String rp_appvalname = StandardAppConstants.RULEPERIODS_APPVALKEY;

        dbappvaluesmethods.insertAppvalue(rp_appvalname,"DAYS",true,true,"");
        dbappvaluesmethods.insertAppvalue(rp_appvalname,"WEEKS",true,true,"");
        dbappvaluesmethods.insertAppvalue(rp_appvalname,"FORTNIGHTS",true,true,"");
        dbappvaluesmethods.insertAppvalue(rp_appvalname,"MONTHS",true,true,"");
        dbappvaluesmethods.insertAppvalue(rp_appvalname,"QUARTERS",true,true,"");
        dbappvaluesmethods.insertAppvalue(rp_appvalname,"YEARS",true,true,"");

        //Build the menu (do a second time to correct any missed insertions
        // due to deletes)
        buildMenu();
        buildMenu();
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

        getDBCounts();
        /**
         * Resume state handling
         */
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
        buildMenu();
        resumestate = StandardAppConstants.RESUMSTATE_NORMAL;
    }

    /**************************************************************************
     * onDestroy - do any clean up before th application is destroyed
     * e.g. close any open cursors and then close the database
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mocsr.close();
        db.close();
    }

    /**************************************************************************
     * getDBCounts - get Database table counts. Used for display and logic for
     * for which Options are available.
     * e.g. Aisles Option should only be visible when Shops
     * exist etc.
     */
    void getDBCounts() {
        shopcount = dbshopmethods.getShopCount();
        aislecount = dbaislemethods.getAisleCount();
        productcount = dbproductmethods.getProductCount();
        productusagecount = dbproductusagemethods.getProductUsageCount();
        shoplistcount = dbdao.getShoplistCount();
        rulecount = dbrulemethods.getRuleCount();
        appvaluecount = dbappvaluesmethods.getAppvalueCount();
    }

    /**************************************************************************
     * buildMenu - Build the Main Activity Options Menu
     *              This menu is built dynamically, as a ListView, from rows
     *              in the Appvalues Table.
     *              Rows used are those with a Name as defined in MENUOPTIONS.
     *              The TEXT column has the Text shown by the button (TextView)
     *              The settingssinfo column contains the notes for the option.
     *              The respective database rows are generated, deleted or
     *              updated according to data in String Arrays menuoptions and
     *              menuoptions_info (defined as MainActivity Class Members
     *              above.)
     *
     *              This method will perform the database operations and then
     *              build the ListView thus catering for a relatively simple
     *              means of updating the menus as available features change.
     *
     *
     */
    private void buildMenu() {
        getDBCounts();

        /**
         * Menu Options String definitions
         */

        String include_shops = "(" +
                DBAppvaluesTableConstants.APPVALUES_TEXT_COL_FULL +
                " = '" + SHOPSAPPVALNAME + "') ";
        String include_products = "(" +
                DBAppvaluesTableConstants.APPVALUES_TEXT_COL_FULL +
                " = '" + PRODUCTSAPPVALNAME + "') ";
        String include_aisles = "(" +
                DBAppvaluesTableConstants.APPVALUES_TEXT_COL_FULL +
                " = '" + AISLESAPPVALNAME + "') ";
        String include_stock = "(" +
                DBAppvaluesTableConstants.APPVALUES_TEXT_COL_FULL +
                " = '" + STOCKAPPVALNAME + "') ";
        String include_order = "(" +
                DBAppvaluesTableConstants.APPVALUES_TEXT_COL_FULL +
                " = '" + ORDERAPPVALNAME + "') ";
        String include_checklist = "(" +
                DBAppvaluesTableConstants.APPVALUES_TEXT_COL_FULL +
                " = '" + CHECKLISTAPPVALNAME + "') ";
        String include_shopping = "( " +
                DBAppvaluesTableConstants.APPVALUES_TEXT_COL_FULL +
                " = '" + SHOPPINGAPPVALNAME + "') ";
        String include_rules = "( " +
                DBAppvaluesTableConstants.APPVALUES_TEXT_COL_FULL +
                " = '" + RULESAPPVALNAME + "') ";
        String include_tools = "( " +
                DBAppvaluesTableConstants.APPVALUES_TEXT_COL_FULL +
                " = '" + TOOLSAPPVALNAME + "') ";

        String filter =
                DBAppvaluesTableConstants.APPVALUES_NAME_COL_FULL +
                        " = '" + MENUOPTIONS + "' " +
                        DBConstants.SQLAND;

        /**
         * Inclusion/visiblity of menuoption logic
         * If no data at all then show Shops and Products only
         * If 1 or more Shops then add Aisles
         * If at least 1 Shop, Product and Aisle then add Stock
         * It at least 1 stocked product (i.e. product assigned to an aisle)
         *      stocked product aka productusage
         * then show all others (even though they may show nothing)
         */
        if (shopcount < 1) {
            filter = filter + "(" +
                    include_shops +
                    DBConstants.SQLOR + include_products +
                    DBConstants.SQLOR + include_tools +
                    ")";
        }
        if (shopcount > 0) {
            filter = filter + "(" +
                    include_shops +
                    DBConstants.SQLOR + include_products +
                    DBConstants.SQLOR + include_aisles +
                    DBConstants.SQLOR + include_tools;
            if (productcount > 0 && aislecount > 0)
                filter = filter +
                    DBConstants.SQLOR + include_stock;
            if (productusagecount > 0) {
                filter = filter +
                        DBConstants.SQLOR + include_order +
                        DBConstants.SQLOR + include_checklist +
                        DBConstants.SQLOR + include_shopping +
                        DBConstants.SQLOR + include_rules;
                }
            filter = filter + ")";
        }

        // Get a cursor with the relevant rows according to the filter
        Cursor csr =  dbdao.getTableRows(
                DBAppvaluesTableConstants.APPVALUES_TABLE,
                "",
                filter,
                ""
        );
        SQLiteDatabase modb = DBHelper.getHelper(this).getWritableDatabase();
        //Phase 1 - determine if there are any rows that need to be added or
        // updated.
        //      i.e. loop through menuoptions array looking for rows that do
        //      not exist (add/insert new row required) or do not match (
        //      update required) and perform respective inserts or updates.
        boolean optionmatch;
        boolean notesmatch;
        boolean ordermatch;
        for (int i = 0; i < MAINACTIVITYOPTIONLIST.length; i++) {
            csr.moveToPosition(-1);     //position before first row
            optionmatch = false;
            notesmatch = false;
            ordermatch = false;
            while (csr.moveToNext()) {
                // compare cursor values and array equivalents
                if (csr.getString(csr.getColumnIndex(
                        DBAppvaluesTableConstants.APPVALUES_TEXT_COL
                )).equals(
                        MAINACTIVITYOPTIONLIST[i].getMenuOptionName()
                )) {
                    optionmatch = true;
                }

                if (csr.getString(csr.getColumnIndex(
                        DBAppvaluesTableConstants.APPVALUES_SETTINGSINFO_COL
                )).equals(
                        MAINACTIVITYOPTIONLIST[i].getmMenuOptionInfo()
                )) {
                    notesmatch = true;
                }
                if (csr.getInt(csr.getColumnIndex(
                        DBAppvaluesTableConstants.APPVALUES_INT_COL
                )) ==
                        MAINACTIVITYOPTIONLIST[i].getmMenuOptionOrder()
                        ) {
                    ordermatch = true;
                }
            }
            // comparisons done so act accordingly
            // skip to next if both match or only the option mismatches
            // (latter cases means the notes are for another option))
            if ((optionmatch && notesmatch && ordermatch) || (!optionmatch && notesmatch &&ordermatch)) {
                continue;
            }
            ContentValues cv = new ContentValues();
            // insert required as no matches
            if ((!optionmatch)) {
                dbappvaluesmethods.insertStringAndIntAppvalue(
                        MENUOPTIONS,
                        MAINACTIVITYOPTIONLIST[i].getMenuOptionName(),
                        MAINACTIVITYOPTIONLIST[i].getmMenuOptionOrder(),
                        true,
                        false,
                        MAINACTIVITYOPTIONLIST[i].getmMenuOptionInfo());
            }
            // update required as only notes mismatch
            if(optionmatch) {
                cv.put(DBAppvaluesTableConstants.APPVALUES_SETTINGSINFO_COL,
                        MAINACTIVITYOPTIONLIST[i].getmMenuOptionInfo());
                cv.put(DBAppvaluesTableConstants.APPVALUES_INT_COL,
                        MAINACTIVITYOPTIONLIST[i].getmMenuOptionOrder());
                String whereargs[] = { MENUOPTIONS,
                        MAINACTIVITYOPTIONLIST[i].getMenuOptionName()
                };
                String whereclause = DBAppvaluesTableConstants.APPVALUES_NAME_COL +
                        " = ? AND " +
                        DBAppvaluesTableConstants.APPVALUES_TEXT_COL +
                        " = ?";
                modb.update(
                        DBAppvaluesTableConstants.APPVALUES_TABLE,
                        cv,
                        whereclause,
                        whereargs);
            }
        }
        // Phase 2 - Determine if and then delete, any rows that have no matching
        //              array entry.
        csr.moveToPosition(-1);
        while (csr.moveToNext()) {
            optionmatch = false;
            for (int i = 0; i < MAINACTIVITYOPTIONLIST.length; i++) {
                if (csr.getString(
                        csr.getColumnIndex(
                                DBAppvaluesTableConstants.APPVALUES_TEXT_COL
                        )).equals(
                        MAINACTIVITYOPTIONLIST[i].getMenuOptionName())
                        ) {
                    optionmatch = true;
                }

            }
            if (!optionmatch) {
                String sql = "DELETE FROM " +
                        DBAppvaluesTableConstants.APPVALUES_TABLE +
                        DBConstants.SQLWHERE +
                        DBAppvaluesTableConstants.APPVALUES_NAME_COL_FULL +
                        " = '" +
                        MENUOPTIONS +
                        "' AND " +
                        DBAppvaluesTableConstants.APPVALUES_TEXT_COL_FULL +
                        " = '" +
                        csr.getString(
                                csr.getColumnIndex(
                                        DBAppvaluesTableConstants.APPVALUES_TEXT_COL_FULL
                                )) +
                        "' " + DBConstants.SQLENDSTATEMENT;
                modb.execSQL(sql);
            }
        }
        csr.close();
        // Phase 3 build the actual Listview that is displayed
        String order = DBAppvaluesTableConstants.APPVALUES_INT_COL_FULL + DBConstants.SQLORDERASCENDING;
        mocsr = dbdao.getTableRows(
                DBAppvaluesTableConstants.APPVALUES_TABLE,
                "",
                filter,
                order
        );
        options_adapter = new AdapterMainActivityOptionsMenu(
                this,
                mocsr,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
                ,this.getIntent()
        );
        options_listview.setAdapter(options_adapter);

    }

    /**************************************************************************
     * actionButtonClick - Take the appropriate action when an action button is
     * touched/clicked
     * Curenttly the same activity is invoked however the position is passed
     *
     * @param view the view (TextView acting as Button)that was clicked
     */
    public void actionButtonClick(View view) {
        Integer tag = (Integer) view.getTag();
        Intent intent;
        switch (tag) {
            case 0:
                intent = new Intent(this,ShopsActivity.class);
                intent.putExtra(
                        StandardAppConstants.INTENTKEY_MENUCOLORCODE,
                        StandardAppConstants.SHOPSAPPVALORDER);
                break;
            case 1:
                intent = new Intent(this,AislesActivity.class);
                intent.putExtra(
                        StandardAppConstants.INTENTKEY_MENUCOLORCODE,
                        StandardAppConstants.AISLESAPPVALORDER);
                break;
            case 2:
                intent = new Intent(this,ProductsActivity.class);
                intent.putExtra(
                        StandardAppConstants.INTENTKEY_MENUCOLORCODE,
                        StandardAppConstants.PRODUCTSAPPVALORDER);
                break;
            case 3:
                intent = new Intent(this,StockListActivity.class);
                intent.putExtra(
                        StandardAppConstants.INTENTKEY_MENUCOLORCODE,
                        StandardAppConstants.STOCKAPPVALORDER);
                break;
            case 4:
                intent = new Intent(this,OrderActivity.class);
                intent.putExtra(
                        StandardAppConstants.INTENTKEY_MENUCOLORCODE,
                        StandardAppConstants.ORDERAPPVALORDER);
                break;
            case 5:
                intent = new Intent(this,CheckListActivity.class);
                intent.putExtra(
                        StandardAppConstants.INTENTKEY_MENUCOLORCODE,
                        StandardAppConstants.CHECKLISTAPPVALORDER);
                break;
            case 6:
                intent = new Intent(this,ShoppingActivity.class);
                intent.putExtra(
                        StandardAppConstants.INTENTKEY_MENUCOLORCODE,
                        StandardAppConstants.SHOPPINGAPPVALORDER);
                break;
            case 7:
                intent = new Intent(this,RulesActivity.class);
                intent.putExtra(
                        StandardAppConstants.INTENTKEY_MENUCOLORCODE,
                        StandardAppConstants.RULESAPPVALORDER);
                break;
            case 8:
                intent = new Intent(this,ToolsActivity.class);
                intent.putExtra(
                        StandardAppConstants.INTENTKEY_MENUCOLORCODE,
                        StandardAppConstants.TOOLSAPPVALORDER);
                break;
            default:
                intent = new Intent(this, TestAction.class);
        }
        intent.putExtra(INTENTKEY_CALLINGACTIVITY,THIS_ACTIVITY);
        intent.putExtra(INTENTKEY_CALLINGMODE,(int)0);
        startActivity(intent);
    }
}
