package mjt.shopwise;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * DBShopMethods - Database Methods specific to Shop handling
 */
@SuppressWarnings({"FieldCanBeLocal", "WeakerAccess", "CanBeFinal"})
class DBShopMethods {

    private static final String LOGTAG = "SW-DBSM";
    private Context context;        // Context as passed
    private DBDAO dbdao;            // Get db data access object
    private static SQLiteDatabase db;      // get db
    private static long lastshopadded = -1;            // id of the last shop added
    private static boolean lastshopaddok = false;   // state of last insert
    private static boolean lastshopupdateok = false;
    public static final String THISCLASS = DBShopMethods.class.getSimpleName();

    /**
     * Instantiates a new Db shop methods.
     *
     * @param ctxt the ctxt
     */
    DBShopMethods(Context ctxt) {
        String msg = "Constructing";
        String methodname = "Construct";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        context = ctxt;
        dbdao = new DBDAO(context);
        db = dbdao.db;
    }

    /**************************************************************************
     * getShopCount - get the number of shops
     *
     * @return number of Shops
     */
    int getShopCount() {
        return DBCommonMethods.getTableRowCount(db,
                DBShopsTableConstants.SHOPS_TABLE
        );
    }

    /**************************************************************************
     * getLastShopAdded - return the id of the last shop added
     *
     * @return shopid last shop added
     */
    @SuppressWarnings("unused")
    long getLastShopAdded() {
        return lastshopadded;
    }

    /**************************************************************************
     * ifShopAdded - returns status of last shop insert
     *
     * @return true if added ok, else false
     */
    boolean ifShopAdded() {
        return lastshopaddok;
    }

    /**
     * If shop updated boolean.
     *
     * @return the boolean
     */
    boolean ifShopUpdated() { return lastshopupdateok; }

    /**
     * getHighestShopOrder
     * @return  the highest order via MAX(shoporder)
     */
    int getHighestShopOrder() {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        int rv = 0;
        String columns[] = {
               DBConstants.SQLMAX +
                       DBShopsTableConstants.SHOPS_ORDER_COL +
                       DBConstants.SQLMAXCLOSE +
                       DBConstants.SQLAS +
                       DBShopsTableConstants.SHOPSMAXORDERCOLUMN
        };
        Cursor csr = db.query(
                DBShopsTableConstants.SHOPS_TABLE,
                columns,
                null,null,null,null,null);
        if (csr.getCount() > 0 ) {
            csr.moveToFirst();
            rv = csr.getInt(csr.getColumnIndex(
                    DBShopsTableConstants.SHOPSMAXORDERCOLUMN
            ));
        }
        csr.close();
        msg = "Hightest Shop Order=" + Integer.toString(rv);
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                LOGTAG,
                msg,
                THISCLASS,
                methodname
        );
        return rv;
    }

    /**************************************************************************
     * getShops - get shops as a cursor
     *
     * @param filter sql filter string less WHERE
     * @param order  sql sort string less ORDER BY
     * @return cursor containing selected shops
     */
    Cursor getShops(String filter, String order) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        Cursor rv;
        rv = DBCommonMethods.getTableRows(db,
                DBShopsTableConstants.SHOPS_TABLE,
                filter,order
        );
        msg = "Returning Shops Cursor with " +
                Integer.toString(rv.getCount()) + " rows.";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        return rv;
    }

    /**************************************************************************
     *
     * @param filter
     * @param order
     * @return
     */
    Cursor getShopsWithAisles(String filter, String order) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);

        Cursor rv;
        String sql = DBConstants.SQLSELECT +
                DBShopsTableConstants.SHOPS_ID_COL_FULL + ", " +
                DBShopsTableConstants.SHOPS_ORDER_COL_FULL + ", " +
                DBShopsTableConstants.SHOPS_NAME_COL_FULL + ", " +
                DBShopsTableConstants.SHOPS_CITY_COL_FULL + ", " +
                DBShopsTableConstants.SHOPS_STREET_COL_FULL + ", " +
                DBShopsTableConstants.SHOPS_STATE_COL_FULL + ", " +
                DBShopsTableConstants.SHOPS_NOTES_COL_FULL +
                DBConstants.SQLFROM +
                DBShopsTableConstants.SHOPS_TABLE +
                DBConstants.SQLLEFTJOIN +
                DBAislesTableConstants.AISLES_TABLE +
                DBConstants.SQLON +
                DBShopsTableConstants.SHOPS_ID_COL_FULL + " = " +
                DBAislesTableConstants.AISLES_SHOPREF_COL_FULL +
                DBConstants.SQLWHERE +
                DBAislesTableConstants.AISLES_SHOPREF_COL_FULL +
                " IS NOT NULL " + filter + " " +
                DBConstants.SQLGROUP +
                DBShopsTableConstants.SHOPS_ID_COL_FULL;

        if (order.length() > 0) {
            sql = sql + DBConstants.SQLORDERBY + order;
        }
        rv = db.rawQuery(sql,null);
        msg = "Returning Shops with Aisles Cursor with " +
                Integer.toString(rv.getCount()) + " rows.";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        return rv;
    }

    /**************************************************************************
     * insertShop add a new Shop
     *
     * @param shopname   name of the shop
     * @param shoporder  order of the shop (lowest first)
     * @param shopstreet street part of the address of the shop
     * @param shopcity   city/location of the shop
     * @param shopstate  state/county of the shop
     * @param shopnotes  motes about the shop
     */
    void insertShop(String shopname,
                    int shoporder,
                    String shopstreet,
                    String shopcity,
                    String shopstate,
                    @SuppressWarnings("SameParameterValue") String shopnotes) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);

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
        msg = "Shop=" + shopname + " City=" + shopcity +
                "Added=" + Boolean.toString(lastshopaddok);
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
    }

    /**
     * ------------------------------------------------------------------------
     * alternative insertShop method (notes blank)
     *
     * @param shopname   name of the shop
     * @param shoporder  order of the shop (lowest first)
     * @param shopstreet street part of the address of the shop
     * @param shopcity   city/location of the shop
     * @param shopstate  state/county of the shop
     */
    void insertShop(String shopname,
                    int shoporder,
                    String shopstreet,
                    String shopcity,
                    @SuppressWarnings("SameParameterValue") String shopstate) {
        insertShop(shopname, shoporder, shopstreet, shopcity, shopstate,"");
    }

    /**
     * ------------------------------------------------------------------------
     * alternative insertShop method (notes and shopstate blank)
     *
     * @param shopname   name of the shop
     * @param shoporder  order of the shop (lowest first)
     * @param shopstreet street part of the address of the shop
     * @param shopcity   city/location of the shop
     */
    void insertShop(String shopname,
                    int shoporder,
                    String shopstreet,
                    @SuppressWarnings("SameParameterValue") String shopcity) {
        insertShop(shopname, shoporder, shopstreet, shopcity, "");
    }

    /**
     * ------------------------------------------------------------------------
     * alternative insertShop method (notes, shopstate and shopcity blank)
     *
     * @param shopname   name of the shop
     * @param shoporder  order of the shop (lowest first)
     * @param shopstreet street part of the address of the shop
     */
    void insertShop(String shopname,
                    int shoporder,
                    @SuppressWarnings("SameParameterValue") String shopstreet) {
        insertShop(shopname, shoporder, shopstreet, "");
    }

    /**
     * ------------------------------------------------------------------------
     * alternative insertShop method (notes, shopstate, shopcity and
     * shopstreet blank)
     *
     * @param shopname  name of the shop
     * @param shoporder order of the shop (lowest first)
     */
    void insertShop(String shopname,
                           int shoporder) {
        insertShop(shopname, shoporder, "");
    }

    /**
     * ------------------------------------------------------------------------
     * alternative insertShop method (notes, shopstate, shopcity and
     * shopstreet  blank, shoporder set to default)
     *
     * @param shopname name of the shop
     */
    @SuppressWarnings("unused")
    void insertShop(String shopname) {
        String orderdflt = DBConstants.DEFAULTORDER;
        insertShop(shopname, Integer.parseInt(DBConstants.DEFAULTORDER));
    }

    /**************************************************************************
     * doesShopExist
     *
     * @param shopid shopid
     * @return true if shopid exists, false if not
     */
    boolean doesShopExist(long shopid) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        boolean rv = false;
        String filter =  DBShopsTableConstants.SHOPS_ID_COL_FULL +
                    " = " + Long.toString(shopid) +
                DBConstants.SQLENDSTATEMENT;
        Cursor csr = DBCommonMethods.getTableRows(db,
                DBShopsTableConstants.SHOPS_TABLE,
                filter,
                ""
        );
        if (csr.getCount() > 0) {
            rv = true;
        }
        csr.close();
        msg = "ShopID=" + Long.toString(shopid) +
                " Exists=" + Boolean.toString(rv);
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        return rv;
    }

    /**************************************************************************
     * modifyShop Update a shop
     *
     * @param shopid     shopid of the shop to be updated
     * @param shoporder  order of the shop 0 if to skip
     * @param shopname   new shopname, "" to skip
     * @param shopstreet new street, "" to skip
     * @param shopcity   new city, "" skip
     * @param shopstate  new state, "" to skip
     * @param shopnotes  new notes, "" to skip
     */
    void modifyShop(long shopid,
                    int shoporder,
                    String shopname,
                    @SuppressWarnings("SameParameterValue") String shopstreet,
                    String shopcity,
                    @SuppressWarnings("SameParameterValue") String shopstate,
                    @SuppressWarnings("SameParameterValue") String shopnotes) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        /**
         * If the shop doesn't exist then return
         */
        if(!doesShopExist(shopid)) {
            msg = "Shop=" + shopname + " ID=" + " does not exist.";
            LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
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
            msg = "Nothing to Update. Not Updated";
            LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
            return;
        }
        String[] whereargs = {Long.toString(shopid )};
        String whereclause = DBShopsTableConstants.SHOPS_ID_COL + " = ?";
        lastshopupdateok = db.update(DBShopsTableConstants.SHOPS_TABLE, cv, whereclause, whereargs) > 0;
        msg = "Shop=" + shopname + " City=" + shopcity +
                " ID=" + Long.toString(shopid) +
                " Updated=" + Boolean.toString(lastshopupdateok);
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
    }

    /**************************************************************************
     * deleteShop = Delete a specified shop from the database along with all
     * the underlying "owned" components.
     * aisles are directly owned by a shop
     * aisles own productusages, rules and shoppinglist entries
     *
     * @param shopid        id of the shop to be deleted
     * @param intransaction the intransaction
     * @return number of shops deleted
     */
    @SuppressWarnings("UnusedReturnValue")
    int deleteShop(long shopid, @SuppressWarnings("SameParameterValue") boolean intransaction) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        int rv = 0;

        String filter = DBAislesTableConstants.AISLES_SHOPREF_COL +
                " = " + Long.toString(shopid) +
                DBConstants.SQLENDSTATEMENT;
        Cursor aislescursor = DBCommonMethods.getTableRows(db,
                DBAislesTableConstants.AISLES_TABLE,
                filter,
                "");
        if(!intransaction) {
            db.beginTransaction();
            msg = "As not in an existing transaction, transaction started.";
            LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        }
        while(aislescursor.moveToNext()) {
            long aisleid = aislescursor.getLong(
                    aislescursor.getColumnIndex(
                            DBAislesTableConstants.AISLES_ID_COL));
            new DBAisleMethods(context).deleteAisle(aisleid,true);
            msg = "Aisle=" + aislescursor.getString(
                    aislescursor.getColumnIndex(
                            DBAislesTableConstants.AISLES_NAME_COL
                    )) + " Deleted, as it references Shop."
            ;
            LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        }
        msg = "Aisles deleted=" + Integer.toString(aislescursor.getCount());
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        aislescursor.close();

        /**
         * Ready for action the deletion of the Shop (should only ever be 1)
         */
        String dlt_shop_whereclause = DBShopsTableConstants.SHOPS_ID_COL + " = ?";
        String dlt_shop_whereargs[] = { Long.toString(shopid) };
        rv =  db.delete(DBShopsTableConstants.SHOPS_TABLE,
                dlt_shop_whereclause,
                dlt_shop_whereargs);
        if(!intransaction) {
            db.setTransactionSuccessful();
            db.endTransaction();
            msg = "Transaction SET and ENDED.";
            LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        }
        msg = "ShopID=" + Long.toString(shopid) +
                " Deleted=" + Boolean.toString(rv > 0);
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        return rv;
    }

    /**************************************************************************
     * shopDeleteImpact
     *
     * @param shopid id of the shop to report on
     * @return String ArrayList of table rows that will be deleted
     */
    ArrayList<String> shopDeletedImpact(long shopid) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        ArrayList<String> rv = new ArrayList<>();

        String shopfilter = DBShopsTableConstants.SHOPS_ID_COL_FULL +
                " = " +
                Long.toString(shopid) +
                DBConstants.SQLENDSTATEMENT;
        String aislefilter = DBAislesTableConstants.AISLES_SHOPREF_COL_FULL +
                " = " +
                Long.toString(shopid) +
                DBConstants.SQLENDSTATEMENT;
        Cursor shopstodelete = DBCommonMethods.getTableRows(db,
                DBShopsTableConstants.SHOPS_TABLE,
                shopfilter,
                ""
        );
        while (shopstodelete.moveToNext()) {
            Cursor aislestodelete = DBCommonMethods.getTableRows(db,
                    DBAislesTableConstants.AISLES_TABLE,
                    aislefilter,
                    "");
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
                long aisleid = aislestodelete.getLong(aislestodelete.getColumnIndex(
                        DBAislesTableConstants.AISLES_ID_COL));

                rv.addAll(new DBAisleMethods(context).aisleDeleteImpact(aisleid));
            }
            aislestodelete.close();
        }
        shopstodelete.close();
        return  rv;
    }
}
