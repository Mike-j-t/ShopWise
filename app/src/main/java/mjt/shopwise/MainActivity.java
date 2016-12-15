package mjt.shopwise;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import static mjt.shopwise.StandardAppConstants.AISLESAPPVALNAME;
import static mjt.shopwise.StandardAppConstants.INTENTKEY_CALLINGACTIVITY;
import static mjt.shopwise.StandardAppConstants.INTENTKEY_CALLINGMODE;
import static mjt.shopwise.StandardAppConstants.MAINACTIVITYOPTIONLIST;
import static mjt.shopwise.StandardAppConstants.MENUOPTIONS;
import static mjt.shopwise.StandardAppConstants.ORDERAPPVALNAME;
import static mjt.shopwise.StandardAppConstants.PRODUCTSAPPVALNAME;
import static mjt.shopwise.StandardAppConstants.RULESAPPVALNAME;
import static mjt.shopwise.StandardAppConstants.SHOPPINGAPPVALNAME;
import static mjt.shopwise.StandardAppConstants.SHOPSAPPVALNAME;

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
         *  Appvalues table.
         */
        ActionColorCoding acc = ActionColorCoding.getInstance(this);

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

        dbappvaluesmethods.insertAppvalue("RULEPERIODS","DAYS",true,true,"");
        dbappvaluesmethods.insertAppvalue("RULEPERIODS","WEEKS",true,true,"");
        dbappvaluesmethods.insertAppvalue("RULEPERIODS","FORTNIGHTS",true,true,"");
        dbappvaluesmethods.insertAppvalue("RULEPERIODS","MONTHS",true,true,"");
        dbappvaluesmethods.insertAppvalue("RULEPERIODS","QUARTERS",true,true,"");
        dbappvaluesmethods.insertAppvalue("RULEPERIODS","YEARS",true,true,"");


        //dbshopmethods.insertShop("TestShop",1000,"1 Main Street","Maintown","County Main","blah");
        //dbproductmethods.insertProduct("Baked beans","Small tin");
        //dbaislemethods.insertAisle("Fruit & Veg",1000,1);
        //dbaislemethods.insertAisle("Veg & Fruit",1100,1);
        //dbaislemethods.insertAisle("Bakery",1200,1);
        //dbproductusagemethods.insertProductUsage(1,1,0.65,1000);


        //TODO Remove block, just for testing/development
        ArrayList<String> ruleperiods = dbappvaluesmethods.getStringAppvalues("RULEPERIODS");
        for (int i = 0; i < ruleperiods.size(); i++) {
            Log.i(LOGTAG,"\t\tThis RULEPERIOD=" + ruleperiods.get(i));
        }

        //TODO remove block, just for testing/development
        Cursor shopscsr = dbshopmethods.getShops("","");
        while(shopscsr.moveToNext()) {
            Log.i(LOGTAG," Shop=" +
                    shopscsr.getString(shopscsr.getColumnIndex(DBShopsTableConstants.SHOPS_NAME_COL)) +
                    " ID=" + shopscsr.getString(shopscsr.getColumnIndex(DBShopsTableConstants.SHOPS_ID_COL)) +
                    " ORDER=" + shopscsr.getString(shopscsr.getColumnIndex(DBShopsTableConstants.SHOPS_ORDER_COL))
            );
        }
        shopscsr.close();

        //TODO remove block, just for testing/development
        Cursor aislecsr = dbaislemethods.getAisles("","");
        while (aislecsr.moveToNext()) {
            long idoffset = aislecsr.getColumnIndex(DBAislesTableConstants.AISLES_ID_COL);
            long aisleid = aislecsr.getLong(aislecsr.getColumnIndex(DBAislesTableConstants.AISLES_ID_COL));
            Log.i(LOGTAG,"Aisle=" +
                    aislecsr.getString(aislecsr.getColumnIndex(DBAislesTableConstants.AISLES_NAME_COL)) +
                    " ID=" + aislecsr.getString(aislecsr.getColumnIndex(DBAislesTableConstants.AISLES_ID_COL)) +
                    " ORDER=" + aislecsr.getString(aislecsr.getColumnIndex(DBAislesTableConstants.AISLES_ORDER_COL)) +
                    " SHOPREF=" + aislecsr.getString(aislecsr.getColumnIndex(DBAislesTableConstants.AISLES_SHOPREF_COL))
            );
        }
        aislecsr.close();


        //TODO remove block, just for testing/development
        Cursor productcsr = dbproductmethods.getProducts("","");
        while(productcsr.moveToNext()) {
            Log.i(LOGTAG,"Product=" +
                    productcsr.getString(productcsr.getColumnIndex(DBProductsTableConstants.PRODUCTS_NAME_COL)) +
                    " ID=" + productcsr.getString(productcsr.getColumnIndex(DBProductsTableConstants.PRODUCTS_ID_COL)) +
                    " Notes=" + productcsr.getString(productcsr.getColumnIndex(DBProductsTableConstants.PRODUCTS_NOTES_COL))
            );
        }
        productcsr.close();

        //TODO remove block, just for testing/development
        Cursor pucsr = dbproductusagemethods.getProductUsages("","");
        while(pucsr.moveToNext()) {
            Log.i(LOGTAG,"PUARef=" +
                    pucsr.getString(pucsr.getColumnIndex(DBProductusageTableConstants.PRODUCTUSAGE_AISLEREF_COL)) +
                    " PUPRef=" +
                    pucsr.getString(pucsr.getColumnIndex(DBProductusageTableConstants.PRODUCTUSAGE_PRODUCTREF_COL)) +
                    " PUCost=" +
                    pucsr.getString(pucsr.getColumnIndex(DBProductusageTableConstants.PRODUCTUSAGE_COST_COL)) +
                    " PUCNT=" +
                    pucsr.getString(pucsr.getColumnIndex(DBProductusageTableConstants.PRODUCTUSAGE_BUYCOUNT_COL)) +
                    " PUFRST=" +
                    pucsr.getString(pucsr.getColumnIndex(DBProductusageTableConstants.PRODUCTUSAGE_FIRSTBUYDATE_COL)) +
                    " PULAST=" +
                    pucsr.getString(pucsr.getColumnIndex(DBProductusageTableConstants.PRODUCTUSAGE_LATESTBUYDATE_COL)) +
                    " PUORD=" +
                    pucsr.getString(pucsr.getColumnIndex(DBProductusageTableConstants.PRODUCTUSAGE_ORDER_COL)) +
                    " PURSF=" +
                    pucsr.getString(pucsr.getColumnIndex(DBProductusageTableConstants.PRODUCTUSAGE_RULESUGGESTFLAG_COL))
            );
        }
        pucsr.close();


        //TODO remove block, just for testing/development
        Cursor appvaluescsr = dbappvaluesmethods.getAppvalues("","");
        while (appvaluescsr.moveToNext()) {
            Log.i(LOGTAG, "Appvalue =" +
                    appvaluescsr.getString(appvaluescsr.getColumnIndex(DBAppvaluesTableConstants.APPVALUES_NAME_COL)) +
                    " Type=" +
                    appvaluescsr.getString(appvaluescsr.getColumnIndex(DBAppvaluesTableConstants.APPVALUES_TYPE_COL)) +
                    " TEXT Value=" +
                    appvaluescsr.getString(appvaluescsr.getColumnIndex(DBAppvaluesTableConstants.APPVALUES_TEXT_COL)) +
                    " INT Value=" +
                    appvaluescsr.getString(appvaluescsr.getColumnIndex(DBAppvaluesTableConstants.APPVALUES_INT_COL)) +
                    " REAL Value=" +
                    appvaluescsr.getString(appvaluescsr.getColumnIndex(DBAppvaluesTableConstants.APPVALUES_REAL_COL)) +
                    " INCL FLAG=" +
                    appvaluescsr.getString(appvaluescsr.getColumnIndex(DBAppvaluesTableConstants.APPVALUES_INCLUDEINSETTINGS_COL)) +
                    " INFO Value=" +
                    appvaluescsr.getString(appvaluescsr.getColumnIndex(DBAppvaluesTableConstants.APPVALUES_SETTINGSINFO_COL)) +
                    " ;"
            );
        }
        appvaluescsr.close();


        //TODO remove block, just for testing/development
        Cursor rcsr = dbrulemethods.getRules("","");
        while (rcsr.moveToNext()) {
            Log.i(LOGTAG,"RuleName=" +
                    rcsr.getString(rcsr.getColumnIndex(DBRulesTableConstants.RULES_NAME_COL)) +
                    " ProdRef=" + Long.toString(rcsr.getLong(rcsr.getColumnIndex(DBRulesTableConstants.RULES_PRODUCTREF_COL))) +
                    " Aisleref=" + Long.toString(rcsr.getLong(rcsr.getColumnIndex(DBRulesTableConstants.RULES_AISLEREF_COL))) +
                    " Period=" + Integer.toString(rcsr.getInt(rcsr.getColumnIndex(DBRulesTableConstants.RULES_PERIOD_COL))) +
                    " Multiplier =" + rcsr.getString(rcsr.getColumnIndex(DBRulesTableConstants.RULES_MULTIPLIER_COL))
            );
        }
         rcsr.close();


        int stophere = 0;

        /**********************************************************************
         *  END OF TESTING STUFF.
         *********************************************************************/

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

        String include_shops = "(" +
                DBAppvaluesTableConstants.APPVALUES_TEXT_COL_FULL +
                " = '" + SHOPSAPPVALNAME + "') ";
        String include_products = "(" +
                DBAppvaluesTableConstants.APPVALUES_TEXT_COL_FULL +
                " = '" + PRODUCTSAPPVALNAME + "') ";
        String include_aisles = "(" +
                DBAppvaluesTableConstants.APPVALUES_TEXT_COL_FULL +
                " = '" + AISLESAPPVALNAME + "') ";
        String include_order = "(" +
                DBAppvaluesTableConstants.APPVALUES_TEXT_COL_FULL +
                " = '" + ORDERAPPVALNAME + "') ";
        String include_shopping = "( " +
                DBAppvaluesTableConstants.APPVALUES_TEXT_COL_FULL +
                " = '" + SHOPPINGAPPVALNAME + "') ";
        String include_rules = "( " +
                DBAppvaluesTableConstants.APPVALUES_TEXT_COL_FULL +
                " = '" + RULESAPPVALNAME + "') ";
        String filter =
                DBAppvaluesTableConstants.APPVALUES_NAME_COL_FULL +
                        " = '" + MENUOPTIONS + "' " +
                        DBConstants.SQLAND;
        if (shopcount < 1) {
            filter = filter + "(" +
                    include_shops + DBConstants.SQLOR + include_products +
                    ")";
        }
        if (shopcount > 0) {
            filter = filter + "(" +
                    include_shops + DBConstants.SQLOR +
                    include_products + DBConstants.SQLOR +
                    include_aisles;
            if (productusagecount > 0) {
                filter = filter + DBConstants.SQLOR + include_order +
                        DBConstants.SQLOR + include_shopping +
                        DBConstants.SQLOR + include_rules;
                }
            filter = filter + ")";
        }
        //filter = filter + DBConstants.SQLENDSTATEMENT;
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
            if ((optionmatch && notesmatch) || (!optionmatch && notesmatch)) {
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
                String whereargs[] = { MENUOPTIONS,
                        MAINACTIVITYOPTIONLIST[i].getMenuOptionName()};
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
        );
        options_listview.setAdapter(options_adapter);

    }

    /**
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
                break;
            case 1:
                //TODO Implement Aisles
                intent = new Intent(this,AislesActivity.class);
                break;
            case 2:
                //TODO Implement Products
                intent = new Intent(this,ProductsActivity.class);
                break;
            case 3:
                //TODO Implement Ordering
                intent = new Intent(this,TestAction.class);
                break;
            case 4:
                //TODO Implement Shopping
                intent = new Intent(this,TestAction.class);
                break;
            case 5:
                //TODO Implement Rules
                intent = new Intent(this,TestAction.class);
                break;
            case 6:
                //TODO Not needed as of yet
                intent = new Intent(this,TestAction.class);
                break;
            case 7:
                //TODO Not needed as yet
                intent = new Intent(this,TestAction.class);
                break;
            default:
                intent = new Intent(this, TestAction.class);
        }
        intent.putExtra(StandardAppConstants.INTENTKEY_MENUCOLORCODE,tag);
        intent.putExtra(INTENTKEY_CALLINGACTIVITY,THIS_ACTIVITY);
        intent.putExtra(INTENTKEY_CALLINGMODE,(int)0);
        startActivity(intent);
    }
}
