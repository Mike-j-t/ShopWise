package mjt.shopwise;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

/**
 * DBShopMethods - Database Methods specific to Shop handling
 */
class DBShopMethods {

    private static final String LOGTAG = "SW-DBSM";
    private Context context;        // Context as passed
    private DBDAO dbdao;            // Get db data access object
    private static SQLiteDatabase db;      // get db
    private static long lastshopadded = -1;            // id of the last shop added
    private static boolean lastshopaddok = false;   // state of last insert

    DBShopMethods(Context ctxt) {
        context = ctxt;
        dbdao = new DBDAO(context);
        db = dbdao.db;
    }

    /**************************************************************************
     * getShopCount - get the number of shops
     * @return          number of Shops
     */
    int getShopCount() {
        String sqlstr = "SELECT * FROM " +
                DBShopsTableConstants.SHOPSTABLE.getDBTableName() +
                " ;";
        Cursor csr = db.rawQuery(sqlstr,null);
        int rv = csr.getCount();
        csr.close();
        return rv;
    }

    /**************************************************************************
     * getLastShopAdded - return the id of the last shop added
     * @return shopid
     */
    long getLastShopAdded() {
        return lastshopadded;
    }

    /**************************************************************************
     * ifShopAdded - returns status of last shop insert
     * @return true if added ok, else false
     */
    boolean ifShopAdded() {
        return lastshopaddok;
    }

    /**************************************************************************
     * getShops - get shops as a cursor
     * @param filter sql filter string less WHERE
     * @param order sql sort string less ORDER BY
     * @return cursor containing selected shops
     */
    Cursor getShops(String filter, String order) {
        String sql = " SELECT * FROM " +
                DBShopsTableConstants.SHOPS_TABLE;
        if(filter.length() > 0) {
            sql = sql + " WHERE " + filter;
        }
        if(order.length() > 0) {
            sql = sql + " ORDER BY " + order;
        }
        sql = sql + " ;";
        return db.rawQuery(sql,null);
    }

    /**************************************************************************
     * insertShop add a new Shop
     * @param shopname      name of the shop
     * @param shoporder     order of the shop (lowest first)
     * @param shopstreet    street part of the address of the shop
     * @param shopcity      city/location of the shop
     * @param shopstate     state/county of the shop
     * @param shopnotes     motes about the shop
     */
    void insertShop(String shopname,
                           int shoporder,
                           String shopstreet,
                           String shopcity,
                           String shopstate,
                           String shopnotes) {

        long addedid;

        lastshopaddok = false;

        ContentValues cv = new ContentValues();
        cv.put(DBShopsTableConstants.SHOPS_NAME_COL,shopname);
        cv.put(DBShopsTableConstants.SHOPS_ORDER_COL,shoporder);
        cv.put(DBShopsTableConstants.SHOPS_STREET_COL,shopstreet);
        cv.put(DBShopsTableConstants.SHOPS_CITY_COL,shopcity);
        cv.put(DBShopsTableConstants.SHOPS_STATE_COL,shopstate);
        cv.put(DBShopsTableConstants.SHOPS_NOTES_COL,shopnotes);
        addedid =  db.insert(DBShopsTableConstants.SHOPS_TABLE,
                null,
                cv);
        if (addedid >= 0) {
            lastshopadded = addedid;
            lastshopaddok = true;
        }
    }

    /**------------------------------------------------------------------------
     * alternative insertShop method (notes blank)
     * @param shopname      name of the shop
     * @param shoporder     order of the shop (lowest first)
     * @param shopstreet    street part of the address of the shop
     * @param shopcity      city/location of the shop
     * @param shopstate     state/county of the shop
     */
    void insertShop(String shopname,
                           int shoporder,
                           String shopstreet,
                           String shopcity,
                           String shopstate) {
        insertShop(shopname, shoporder, shopstreet, shopcity, shopstate,"");
    }

    /**------------------------------------------------------------------------
     * alternative insertShop method (notes and shopstate blank)
     * @param shopname      name of the shop
     * @param shoporder     order of the shop (lowest first)
     * @param shopstreet    street part of the address of the shop
     * @param shopcity      city/location of the shop
     */
    void insertShop(String shopname,
                           int shoporder,
                           String shopstreet,
                           String shopcity) {
        insertShop(shopname, shoporder, shopstreet, shopcity, "");
    }
    /**------------------------------------------------------------------------
     * alternative insertShop method (notes, shopstate and hhopcity blank)
     * @param shopname      name of the shop
     * @param shoporder     order of the shop (lowest first)
     * @param shopstreet    street part of the address of the shop
     */
    void insertShop(String shopname,
                           int shoporder,
                           String shopstreet) {
        insertShop(shopname, shoporder, shopstreet, "");
    }
    /**------------------------------------------------------------------------
     * alternative insertShop method (notes, shopstate, shopcity and
     * shopstreet blank)
     * @param shopname      name of the shop
     * @param shoporder     order of the shop (lowest first)
     */
    void insertShop(String shopname,
                           int shoporder) {
        insertShop(shopname, shoporder, "");
    }
    /**------------------------------------------------------------------------
     * alternative insertShop method (notes, shopstate, shopcity and
     * shopstreet  blank, shoporder set to default)
     * @param shopname      name of the shop
     */
    void insertShop(String shopname) {
        String orderdflt = DBConstants.DEFAULTORDER;
        insertShop(shopname, Integer.parseInt(DBConstants.DEFAULTORDER));
    }

    /**************************************************************************
     * doesShopExist
     * @param shopid shopid
     * @return true if shopid exists, false if not
     */
    boolean doesShopExist(long shopid) {
        boolean rv = false;
        String sql = "SELECT " +
                    DBShopsTableConstants.SHOPS_ID_COL_FULL +
                " FROM " + DBShopsTableConstants.SHOPS_TABLE +
                " WHERE " + DBShopsTableConstants.SHOPS_ID_COL_FULL +
                    " = " + Long.toString(shopid) +
                " ;";
        Cursor csr = db.rawQuery(sql,null);
        if (csr.getCount() > 0) {
            rv = true;
        }
        csr.close();
        return rv;
    }

    /**************************************************************************
     * modifyShop Update a shop
     * @param shopid        shopid of the shop to be updated
     * @param shoporder     order of the shop 0 if to skip
     * @param shopname      new shopname, "" to skip
     * @param shopstreet    new street, "" to skip
     * @param shopcity      new city, "" skip
     * @param shopstate     new state, "" to skip
     * @param shopnotes new notes, "" to skip
     */
    void modifyShop(long shopid,
                    int shoporder,
                    String shopname,
                    String shopstreet,
                    String shopcity,
                    String shopstate,
                    String shopnotes) {
        /**
         * If the shop doesn't exist then return
         */
        if(!doesShopExist(shopid)) {
            return;
        }
        int updatecount = 0;
        ContentValues cv = new ContentValues();
        if (shoporder > 0) {
            cv.put(DBShopsTableConstants.SHOPS_ORDER_COL,shoporder);
            updatecount++;
        }
        if (shopname.length() > 0) {
            cv.put(DBShopsTableConstants.SHOPS_NAME_COL,shopname);
            updatecount++;
        }
        if (shopstreet.length() > 0) {
            cv.put(DBShopsTableConstants.SHOPS_STREET_COL,shopstreet);
            updatecount++;
        }
        if (shopcity.length() > 0) {
            cv.put(DBShopsTableConstants.SHOPS_CITY_COL,shopcity);
            updatecount++;
        }
        if(shopstate.length() > 0) {
            cv.put(DBShopsTableConstants.SHOPS_STATE_COL,shopstate);
            updatecount++;
        }
        if(shopnotes.length() > 0) {
            cv.put(DBShopsTableConstants.SHOPS_NOTES_COL,shopnotes);
            updatecount++;
        }
        /**
         * if nothing to do then return
         */
        if (updatecount < 1 ) {
            return;
        }
        String whereargs[] = {Long.toString(shopid )};
        String whereclause = DBShopsTableConstants.SHOPS_ID_COL + " = ?";
        db.update(DBShopsTableConstants.SHOPS_TABLE,cv,whereclause,whereargs);
    }

    /**************************************************************************
     * deleteShop = Delete a specified shop from the database along with all
     * the underlying "owned" components.
     *      aisles are directly owned by a shop
     *      aisles own productusages, rules and shoppinglist entries
     *
     * @param shopid id of the shop to be deleted
     * @return number of shops deleted
     */
    int deleteShop(long shopid) {
        int rv = 0;
        int deleted_productusages = 0;
        int deleted_rules = 0;
        int deleted_shoplists = 0;
        int deleted_aisles = 0;

        String sqlstr = "SELECT * FROM " +
                DBAislesTableConstants.AISLES_TABLE +
                " WHERE " +
                DBAislesTableConstants.AISLES_SHOPREF_COL +
                " = " + Long.toString(shopid) +
                " ;";
        Cursor aislescursor = db.rawQuery(sqlstr,null);
        db.beginTransaction();
        while(aislescursor.moveToNext()) {

            /**
             * Aisle ID required for all 3 sets of deletions so set whereargs to aisleid
             */
            String whereargs[] = {
                    aislescursor.getString(
                            aislescursor.getColumnIndex(
                                    DBAislesTableConstants.AISLES_ID_COL
                            )
                    )
            };

            /**
             * Ready for and action deletion of referenced productusages
             */
            String dlt_pu_whereclause =
                    DBProductusageTableConstants.PRODUCTUSAGE_AISLEREF_COL + " = ?";
            deleted_productusages = db.delete(
                    DBProductusageTableConstants.PRODUCTUSAGE_TABLE,
                    dlt_pu_whereclause,
                    whereargs
            );

            /**
             * Ready for and action deletion of referenced shoplist entries
             */
            String dlt_sl_whereclause =
                DBShopListTableConstants.SHOPLIST_AISLEREF_COL + " = ? ";
            deleted_shoplists = db.delete(
                    DBShopListTableConstants.SHOPLIST_TABLE,
                    dlt_sl_whereclause,
                    whereargs
            );

            /**
             * Ready for and action deleteion of referenced rules
             */
            String dlt_rules_whereclause =
                    DBRulesTableConstants.RULES_AISLEREF_COL + " = ?";
            deleted_rules = db.delete(DBRulesTableConstants.RULES_TABLE,
                    dlt_rules_whereclause,
                    whereargs
            );
        }
        aislescursor.close();
        /**
         * Ready for and action deletion of referenced Aisles
         */
        String dlt_aisles_whereclause =
                DBAislesTableConstants.AISLES_SHOPREF_COL + " = ?";
        String dlt_ailses_whereargs[] = { Long.toString(shopid)};
        deleted_aisles = db.delete(
                DBAislesTableConstants.AISLES_TABLE,
                dlt_aisles_whereclause,
                dlt_ailses_whereargs
        );

        /**
         * Ready for action the deletion of the Shop (should only ever be 1)
         */
        String dlt_shop_whereclause = DBShopsTableConstants.SHOPS_ID_COL + " = ?";
        String dlt_shop_whereargs[] = { Long.toString(shopid) };
        rv =  db.delete(DBShopsTableConstants.SHOPS_TABLE,
                dlt_shop_whereclause,
                dlt_shop_whereargs);
        db.setTransactionSuccessful();
        db.endTransaction();
        Log.i(LOGTAG,
                Long.toString(rv) + " Shops Deleted for shopid " +
                Long.toString(shopid) +
                "\n\t" +
                Long.toString(deleted_aisles) +
                " Referenced Aisles Deleted (by shop)." +
                "\n\t" +
                Long.toString(deleted_productusages) +
                " Referenced Productusages Deleted (by aisles)" +
                "\n\t" +
                Long.toString(deleted_shoplists) +
                " Referenced ShopList Entries Deleted (by aisles)" +
                "\n\t" +
                Long.toString(deleted_rules) +
                " Referenced Rules Deleted (by aisles)");

        return rv;
    }

    /**************************************************************************
     * shopDeleteImpact
     * @param shopid id of the shop to report on
     * @return String ArrayList of table rows that will be deleted
     */
    ArrayList<String> shopDeletedImpact(long shopid) {
        ArrayList<String> rv = new ArrayList<>();

        String shopsql = "SELECT * FROM " +
                DBShopsTableConstants.SHOPS_TABLE +
                " WHERE " +
                DBShopsTableConstants.SHOPS_ID_COL_FULL +
                " = " +
                Long.toString(shopid) +
                " ;";
        String aislesql = "SELECT * FROM " + DBAislesTableConstants.AISLES_TABLE +
                " WHERE " +
                DBAislesTableConstants.AISLES_SHOPREF_COL_FULL +
                " = " +
                Long.toString(shopid) +
                " ;";
        Cursor shopstodelete = db.rawQuery(shopsql,null);
        while (shopstodelete.moveToNext()) {
            Cursor aislestodelete = db.rawQuery(aislesql,null);
            rv.add("SHOP " +
                    shopstodelete.getString(
                            shopstodelete.getColumnIndex(
                                    DBShopsTableConstants.SHOPS_NAME_COL)) +
                    "-" + shopstodelete.getString(
                    shopstodelete.getColumnIndex(
                            DBShopsTableConstants.SHOPS_CITY_COL)) +
                    "-" + shopstodelete.getString(
                    shopstodelete.getColumnIndex(
                            DBShopsTableConstants.SHOPS_STREET_COL)) +
                    " would be deleted.");
            while (aislestodelete.moveToNext()) {
                String current_aisleid = Long.toString(
                        aislestodelete.getLong(
                                aislestodelete.getColumnIndex(
                                        DBAislesTableConstants.AISLES_SHOPREF_COL
                                )
                        )
                );
                /**
                 * Add this aisle as one to be deleted
                 */
                rv.add("AISLE " +
                        aislestodelete.getString(
                                aislestodelete.getColumnIndex(
                                        DBAislesTableConstants.AISLES_NAME_COL
                                )
                        ) +
                        " would be deleted."
                );

                /**
                 * Determine productusages that would be deleted due to being
                 * referenced by this aisle
                 */
                String pusql = " SELECT * FROM " +
                        DBProductusageTableConstants.PRODUCTUSAGE_TABLE +
                        " LEFT JOIN " +
                            DBProductsTableConstants.PRODUCTS_TABLE +
                            " ON " + DBProductsTableConstants.PRODUCTS_ID_COL_FULL +
                                " = " +
                                DBProductusageTableConstants.PRODUCTUSAGE_PRODUCTREF_COL_FULL +
                        " WHERE " +
                        DBProductusageTableConstants.PRODUCTUSAGE_AISLEREF_COL_FULL +
                        " = " + current_aisleid +
                        " ;";
                Cursor pucursor = db.rawQuery(pusql,null);
                while (pucursor.moveToNext()) {
                    //TODO add stuff here

                }
                pucursor.close();

                /**
                 * Determine ShopList entries that would be deleted due to being
                 * referenced by this aisle
                 */
                String slsql = " SELECT * FROM " +
                        DBShopListTableConstants.SHOPLIST_TABLE +
                        " LEFT JOIN " +
                            DBProductsTableConstants.PRODUCTS_TABLE +
                            " ON " +
                                DBProductsTableConstants.PRODUCTS_ID_COL_FULL +
                                " = " +
                                DBShopListTableConstants.SHOPLIST_PRODUCTREF_COL_FULL +
                        " WHERE " +
                            DBShopListTableConstants.SHOPLIST_AISLEREF_COL_FULL +
                            " = " + current_aisleid +
                        " ;";
                Cursor slcursor = db.rawQuery(slsql,null);
                while (slcursor.moveToNext()) {
                    //TODO add stuff here
                }
                slcursor.close();

                String rulesql = " SELECT * FROM " +
                        DBRulesTableConstants.RULES_TABLE +
                        " LEFT JOIN " +
                            DBProductsTableConstants.PRODUCTS_TABLE +
                            " ON " +
                                DBProductsTableConstants.PRODUCTS_ID_COL_FULL +
                                " = " +
                                DBRulesTableConstants.RULES_PRODUCTREF_COL_FULL +
                        " WHERE " +
                            DBRulesTableConstants.RULES_AISLEREF_COL_FULL +
                            " = " + current_aisleid +
                        " ;";
                Cursor rulescursor = db.rawQuery(rulesql,null);
                while (rulescursor.moveToNext()) {
                    //TODO stuff here
                }
                rulescursor.close();
            }
            aislestodelete.close();
        }
        shopstodelete.close();
        return  rv;
    }
}
