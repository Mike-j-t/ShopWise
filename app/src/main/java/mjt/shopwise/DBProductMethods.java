package mjt.shopwise;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * DBProductMethods - Dtabase methods specific to Product Handling
 */
class DBProductMethods {

    private Context context;
    private DBDAO dbdao;
    private static SQLiteDatabase db;
    private static long lastproductadded;
    private static boolean lastproductaddok = false;
    private static boolean lastproductupdatedok = false;
    public static final String THISCLASS = DBProductMethods.class.getSimpleName();
    private static final String LOGTAG = "SW_DBPM";

    /**
     * Instantiates a new Db product methods.
     *
     * @param ctxt the ctxt
     */
    DBProductMethods(Context ctxt) {
        String msg = "Constructing";
        String methodname = "Construct";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        context = ctxt;
        dbdao = new DBDAO(context);
        db = dbdao.db;
    }

    /**************************************************************************
     * getProductCount - get the number of products
     *
     * @return the number of rows in the product table
     */
    int getProductCount() {
        return DBCommonMethods.getTableRowCount(db,
                DBProductsTableConstants.PRODUCTS_TABLE
        );
    }

    /**************************************************************************
     * getLastproductAdded - Get the last product that was added
     *
     * @return the if of the last product that was added
     */
    long getLastProductAdded() {
        return lastproductadded;
    }

    /**************************************************************************
     * ifProductAdded - true if the last attempt to add a product was ok
     * otherwise false
     *
     * @return true if added ok, otherwise false
     */
    boolean ifProductAdded() {
        return lastproductaddok;
    }

    boolean ifProductUpdated() { return lastproductupdatedok; }

    /**************************************************************************
     * doesProductExist - check to see if a product id exists
     *
     * @param productid id of the product to check
     * @return true if the product row was found otherwise false
     */
    boolean doesProductExist(long productid) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        boolean rv = false;
        Cursor csr;

        csr = db.query(DBProductsTableConstants.PRODUCTS_TABLE,
                new String[] {DBProductsTableConstants.PRODUCTS_ID_COL_FULL},
                DBProductsTableConstants.PRODUCTS_ID_COL_FULL + " =?",
                new String[] {Long.toString(productid)},
                null,null,null);
        if (csr.getCount() > 0) {
            rv = true;
        }
        csr.close();
        msg = "Returning " + Boolean.toString(rv);
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        return rv;
    }

    /**************************************************************************
     * getProducts - get products as a cursor
     *
     * @param filter filter string less WHERE keyword
     * @param order  order string less ORDER and BY keywords
     * @return cursor products
     */
    Cursor getProducts(String filter, String order) {
        return DBCommonMethods.getTableRows(db,
                DBProductsTableConstants.PRODUCTS_TABLE,
                filter,
                order
        );
    }

    /**************************************************************************
     *
     * @param aisleid
     * @param filter
     * @param order
     * @return
     */
    Cursor getProductsInAisle(long aisleid, String filter, String order) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        //TODO Remove commented code (rawquery replaced with query)
        String sql = DBConstants.SQLSELECTDISTINCT  +
                DBProductsTableConstants.PRODUCTS_ID_COL_FULL + ", " +
                DBProductsTableConstants.PRODUCTS_NAME_COL_FULL + ", " +
                DBProductsTableConstants.PRODUCTS_NOTES_COL_FULL + " " +
                DBConstants.SQLFROM +
                DBProductsTableConstants.PRODUCTS_TABLE +
                DBConstants.SQLWHERE +
                DBProductsTableConstants.PRODUCTS_ID_COL_FULL +
                DBConstants.SQLIN + "(" +
                DBConstants.SQLSELECT +
                DBProductusageTableConstants.PRODUCTUSAGE_PRODUCTREF_COL_FULL +
                DBConstants.SQLFROM +
                DBProductusageTableConstants.PRODUCTUSAGE_TABLE +
                DBConstants.SQLWHERE +
                DBProductsTableConstants.PRODUCTS_ID_COL_FULL +
                " = " +
                DBProductusageTableConstants.PRODUCTUSAGE_PRODUCTREF_COL_FULL +
                " AND " +
                DBProductusageTableConstants.PRODUCTUSAGE_AISLEREF_COL_FULL +
                " = " +
                Long.toString(aisleid) +
                ") ";

        if (filter.length() > 0 ) {

            sql = sql + DBConstants.SQLAND + filter;
        }
        if (order.length() > 0 ) {
            sql = sql + DBConstants.SQLORDERBY + order;
        }
        return db.rawQuery(sql,null);
    }

    /**************************************************************************
     * getproductsNotInAIsle - get products that are not in the specified aisle
     * @param aisleid   the id of the aisle
     * @param filter    filter string (do not include WHERE)
     * @param order     order string (do not include ORDER BY)
     * @return          a cursor containing the products
     */
    Cursor getProductsNotInAisle(long aisleid, String filter, String order) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        //TODO remove commented out code (coverted rawquery to query)
        /**
        String sql = DBConstants.SQLSELECTDISTINCT  +
                DBProductsTableConstants.PRODUCTS_ID_COL_FULL + ", " +
                DBProductsTableConstants.PRODUCTS_NAME_COL_FULL + ", " +
                DBProductsTableConstants.PRODUCTS_NOTES_COL_FULL + " " +
                DBConstants.SQLFROM +
                DBProductsTableConstants.PRODUCTS_TABLE +
                DBConstants.SQLWHERE +
                DBProductsTableConstants.PRODUCTS_ID_COL_FULL +
                DBConstants.SQLNOTIN + "(" +
                DBConstants.SQLSELECT +
                DBProductusageTableConstants.PRODUCTUSAGE_PRODUCTREF_COL_FULL +
                DBConstants.SQLFROM +
                DBProductusageTableConstants.PRODUCTUSAGE_TABLE +
                DBConstants.SQLWHERE +
                DBProductsTableConstants.PRODUCTS_ID_COL_FULL +
                " = " +
                DBProductusageTableConstants.PRODUCTUSAGE_PRODUCTREF_COL_FULL +
                " AND " +
                DBProductusageTableConstants.PRODUCTUSAGE_AISLEREF_COL_FULL +
                " = " +
                Long.toString(aisleid) +
                ") ";

        if (filter.length() > 0 ) {

            sql = sql + DBConstants.SQLAND + filter;
        }
        if (order.length() > 0 ) {
            sql = sql + DBConstants.SQLORDERBY + order;
        }
        return db.rawQuery(sql,null);
        **/
        String whereclause = DBProductsTableConstants.PRODUCTS_ID_COL_FULL +
                DBConstants.SQLNOTIN  + "(" +
                DBConstants.SQLSELECT +
                DBProductusageTableConstants.PRODUCTUSAGE_PRODUCTREF_COL_FULL +
                DBConstants.SQLFROM +
                DBProductusageTableConstants.PRODUCTUSAGE_TABLE +
                DBConstants.SQLWHERE +
                DBProductusageTableConstants.PRODUCTUSAGE_PRODUCTREF_COL_FULL + " = " +
                DBProductsTableConstants.PRODUCTS_ID_COL_FULL +
                DBConstants.SQLAND +
                DBProductusageTableConstants.PRODUCTUSAGE_AISLEREF_COL_FULL + " = ?" +
                " ) ";
        if (filter.length()> 0) {
            whereclause = whereclause + DBConstants.SQLAND + filter;
        }
        String orderclause = null;
        if (order.length() > 0 ) {
            orderclause = order;
        }
        return db.query(true,DBProductsTableConstants.PRODUCTS_TABLE,
                new String[] {DBProductsTableConstants.PRODUCTS_ID_COL_FULL,
                        DBProductsTableConstants.PRODUCTS_NAME_COL_FULL,
                        DBProductsTableConstants.PRODUCTS_NOTES_COL_FULL},
                whereclause,
                new String[] { Long.toString(aisleid)},
                null,null,
                orderclause,
                null, null);
    }

    /**************************************************************************
     * getProductname - get the name of the Product
     *
     * @param productid the id of the product
     * @return the product name as a string
     */
    String getProductName(long productid) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        String rv = "NOTAPRODUCT";
        if(doesProductExist(productid)) {
            String filter = DBProductsTableConstants.PRODUCTS_ID_COL_FULL +
                    " = " +
                    Long.toString(productid);
            Cursor csr = DBCommonMethods.getTableRows(db,
                    DBProductsTableConstants.PRODUCTS_TABLE,
                    filter,
                    ""
            );
            if(csr.getCount() > 0) {
                rv = csr.getString(
                        csr.getColumnIndex(
                        DBProductsTableConstants.PRODUCTS_NAME_COL
                ));
            }
            csr.close();
        }
        msg = "Returning " + rv;
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        return rv;
    }

    /**************************************************************************
     * insertProduct - Add a new Product
     *
     * @param productname  name of the Product
     * @param productnotes notes about the Product
     */
    void insertProduct(String productname, String productnotes) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        long addedid;
        ContentValues cv = new ContentValues();
        cv.put(DBProductsTableConstants.PRODUCTS_NAME_COL,productname);
        cv.put(DBProductsTableConstants.PRODUCTS_NOTES_COL,productnotes);
        addedid = db.insert(DBProductsTableConstants.PRODUCTS_TABLE,
                null,
                cv);
        if (addedid > -1) {
            lastproductadded = addedid;
            lastproductaddok = true;
            msg = "Added Product=" + productname;
            LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        } else {
            lastproductaddok = false;
            msg = "Failed to add Product=" + productname;
            LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        }
    }

    /**************************************************************************
     * modifyProduct - Update a Product's Name and Notes
     *
     * @param productid    id of the Product to be updated
     * @param productname  New name (if blank then not changed)
     * @param productnotes New notes (always changed to allow blank)
     */
    void modifyProduct(long productid, String productname, String productnotes) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        if (!doesProductExist(productid)) {
            return;
        }
        ContentValues cv = new ContentValues();
        if (productname.length() > 0 ) {
            cv.put(DBProductsTableConstants.PRODUCTS_NAME_COL,productname);
        }
        cv.put(DBProductsTableConstants.PRODUCTS_NOTES_COL,productnotes);
        String whereargs[] = { Long.toString(productid)};
        String whereclause = DBProductsTableConstants.PRODUCTS_ID_COL + " = ?";
        lastproductupdatedok = db.update(DBProductsTableConstants.PRODUCTS_TABLE,
                cv,
                whereclause,
                whereargs)
                > 0;
        msg = "Product Modify=" + Boolean.toString(lastproductupdatedok) +
                " for Product=" + productname;
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
    }

    /**
     * deleteProduct - Delete a product along with ProductUsages,
     * ShopList entries and Rules that use the product
     *
     * @param productid     id of the product
     * @param intransaction true if being called from within an existing db
     *                      transaction.
     */
    void deleteProduct(long productid, boolean intransaction) {
        String msg = "Invoked for Product ID=" + productid;
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        String sql;
        int pudeletes = 0;
        int sldeletes = 0;
        int rdeletes = 0;
        int pdeletes = 0;

        if(doesProductExist(productid)) {

            /**
             * if not in a transaction then begin a transaction
             */
            if(!intransaction) {
                db.beginTransaction();
                msg = "Starting DB Transaction";
                LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
            }

            String whereargs[] = { Long.toString(productid)};

            /**
             * Delete ProductUsage rows that use this product
             */
            pudeletes = db.delete(
                    DBProductusageTableConstants.PRODUCTUSAGE_TABLE,
                    DBProductusageTableConstants.PRODUCTUSAGE_PRODUCTREF_COL +
                            " = ?",
                    whereargs
            );
            msg = "Deleted " + Integer.toString(pudeletes) +
                    " ProductUsage Rows that reference Product ID=" + productid;
            LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);

            /**
             * Delete ShopList rows that use this product
             */
            sldeletes = db.delete(
                    DBShopListTableConstants.SHOPLIST_TABLE,
                    DBShopListTableConstants.SHOPLIST_PRODUCTREF_COL +
                            " = ?",
                    whereargs
            );
            msg = "Deleted " + Integer.toString(sldeletes) +
                    " ShoppingList Rows that reference Product ID=" + productid;
            LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);

            /**
             * Delete Rules rows that use this product
             */
            rdeletes = db.delete(
                    DBRulesTableConstants.RULES_TABLE,
                    DBRulesTableConstants.RULES_PRODUCTREF_COL +
                            " = ?",
                    whereargs
            );
            msg = "Deleted " + Integer.toString(rdeletes) +
                    " Rule Rows that reference Product ID=" + productid;
            LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);

            /**
             * Delete the Product
             */
            pdeletes = db.delete(
                    DBProductsTableConstants.PRODUCTS_TABLE,
                    DBProductsTableConstants.PRODUCTS_ID_COL +
                            " = ?",
                    whereargs
            );
            msg = "Deleted " + Integer.toString(pdeletes) +
                    " Products with Product ID=" + productid;
            LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);

            /**
             * if originally not in a transaction then as one was started
             * complete and end the transaction
             */
            if(!intransaction) {
                db.setTransactionSuccessful();
                db.endTransaction();
                msg = "Transaction SET and ENDED for Product ID=" + productid;
                LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
            }
        }
    }

    /**
     * productDeleteImpact - return a string arraylist of  the product usages,
     * shoplist entries and rules that will have to be
     * removed if deleting the product
     *
     * @param productid the id of the product
     * @return a String ArrayList
     */
    ArrayList<String> productDeleteImpact(long productid) {
        String msg = "Invoked for Product ID=" + productid;
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        ArrayList<String> rv = new ArrayList<>();

        if (doesProductExist(productid)) {
            String filter = DBProductsTableConstants.PRODUCTS_ID_COL_FULL +
                    " = " +
                    Long.toString(productid);
            Cursor productcsr = DBCommonMethods.getTableRows(db,
                    DBProductsTableConstants.PRODUCTS_TABLE,
                    filter,
                    ""
            );
            if(productcsr.getCount() > 0) {
                while (productcsr.moveToNext()) {
                    String productname = productcsr.getString(
                            productcsr.getColumnIndex(
                                    DBProductsTableConstants.PRODUCTS_NAME_COL
                            )
                    );

                    rv.add("Delete PRODUCT - " +
                            productname
                    );

                    Cursor pucsr = db.query(DBProductusageTableConstants.PRODUCTUSAGE_TABLE +
                            DBConstants.SQLLEFTJOIN +
                            DBAislesTableConstants.AISLES_TABLE +
                            DBConstants.SQLON +
                            DBProductusageTableConstants.PRODUCTUSAGE_AISLEREF_COL_FULL + " = " +
                            DBAislesTableConstants.AISLES_ID_COL_FULL,null,
                            DBProductusageTableConstants.PRODUCTUSAGE_PRODUCTREF_COL_FULL + " =?",
                            new String[] { Long.toString(productid)},
                            null,null,null);

                    //TODO Remove commented out code (cover rawquery to query)
                    /**
                    String pusql = DBConstants.SQLSELECTALLFROM +
                            DBProductusageTableConstants.PRODUCTUSAGE_TABLE +
                            " LEFT JOIN " +
                                DBAislesTableConstants.AISLES_TABLE +
                                " ON " +
                                    DBAislesTableConstants.AISLES_ID_COL_FULL +
                                    " = " +
                                    DBProductusageTableConstants.PRODUCTUSAGE_AISLEREF_COL_FULL +
                            DBConstants.SQLWHERE +
                                DBProductusageTableConstants.PRODUCTUSAGE_PRODUCTREF_COL_FULL +
                            " = " +
                            Long.toString(productid) +
                            DBConstants.SQLENDSTATEMENT;
                    Cursor pucsr = db.rawQuery(pusql,null);
                     **/
                    int text = pucsr.getCount();
                    while(pucsr.moveToNext()) {
                        rv.add("Delete STOCK row for " +
                                productname +
                                " Aisle " +
                                pucsr.getString(
                                        pucsr.getColumnIndex(
                                                DBAislesTableConstants.AISLES_NAME_COL))
                        );
                    }
                    pucsr.close();
                    Cursor slcsr = db.query(DBShopListTableConstants.SHOPLIST_TABLE +
                            DBConstants.SQLLEFTJOIN + DBAislesTableConstants.AISLES_TABLE +
                            DBConstants.SQLON + DBShopListTableConstants.SHOPLIST_AISLEREF_COL_FULL + " = " +
                            DBAislesTableConstants.AISLES_ID_COL_FULL,
                            null,
                            DBShopListTableConstants.SHOPLIST_PRODUCTREF_COL_FULL + " = ?",
                            new String[] { Long.toString(productid)},
                            null,null,null);

                    //TODO remove commented out code (rawquery converted to query)
                    /**
                    String slsql = DBConstants.SQLSELECTALLFROM +
                            DBShopListTableConstants.SHOPLIST_TABLE +
                            " LEFT JOIN " + DBAislesTableConstants.AISLES_TABLE +
                                " ON " +
                                    DBAislesTableConstants.AISLES_ID_COL_FULL +
                                " = " +
                                    DBShopListTableConstants.SHOPLIST_AISLEREF_COL_FULL +
                            DBConstants.SQLWHERE +
                                DBShopListTableConstants.SHOPLIST_PRODUCTREF_COL_FULL +
                            " = " +
                            Long.toString(productid) +
                            DBConstants.SQLENDSTATEMENT;
                    Cursor slcsr = db.rawQuery(slsql,null);
                     **/
                    while (slcsr.moveToNext()) {
                        rv.add("Delete SHOPPING row for " +
                                productname +
                                " Aisle " +
                                slcsr.getString(
                                        slcsr.getColumnIndex(
                                                DBAislesTableConstants.AISLES_NAME_COL))
                        );
                    }
                    slcsr.close();

                    Cursor rulecsr = db.query(DBRulesTableConstants.RULES_TABLE +
                            DBConstants.SQLLEFTJOIN +
                            DBAislesTableConstants.AISLES_TABLE +
                            DBConstants.SQLON +
                            DBRulesTableConstants.RULES_AISLEREF_COL_FULL + " = " +
                            DBAislesTableConstants.AISLES_ID_COL_FULL, null,
                            DBRulesTableConstants.RULES_PRODUCTREF_COL_FULL + " =?",
                            new String[] { Long.toString(productid)},
                            null, null, null);

                    //TODO Remove ccommented out code (rawquery converted to query)
                    /**
                    String rulesql = DBConstants.SQLSELECTALLFROM +
                            DBRulesTableConstants.RULES_TABLE +
                            " LEFT JOIN " +
                                DBAislesTableConstants.AISLES_TABLE +
                                " ON " +
                                    DBAislesTableConstants.AISLES_ID_COL_FULL +
                                    " = " +
                                    DBRulesTableConstants.RULES_AISLEREF_COL_FULL +
                            DBConstants.SQLWHERE +
                                DBRulesTableConstants.RULES_PRODUCTREF_COL_FULL +
                                " = " +
                                Long.toString(productid) +
                            DBConstants.SQLENDSTATEMENT;
                    Cursor rulecsr = db.rawQuery(rulesql,null);
                     **/
                    while (rulecsr.moveToNext()) {
                        rv.add(
                                "Delete Rule " +
                                        rulecsr.getString(
                                                rulecsr.getColumnIndex(
                                                        DBRulesTableConstants.RULES_NAME_COL
                                                )
                                        )
                        );
                    }
                    rulecsr.close();
                }
            }
            productcsr.close();
        }
        return rv;
    }
}
