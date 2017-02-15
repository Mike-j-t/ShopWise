package mjt.shopwise;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * DBProductUsageMethods - Database methods sepcific to ProductUsage handling
 * <p>
 * A ProductUsage represents a product within an aisle (location) so is
 * a shop specific representation of a product. (This is a generalisation, as
 * it is possible to have a product in multiple aisles within a store).
 * <p>
 * Although a ProductUsage represents the link between product and store
 * (a many-many relationship) other values that are store/aisle specfifc
 * are held in this table. e.g. cost and order.
 */
class DBProductUsageMethods {
    private Context context;
    private DBDAO dbdao;
    private static SQLiteDatabase db;
    private static long lastproductusageadded;
    private static boolean lastproductusageaddok = false;
    private static boolean lastproductusageduplicate = false;
    private static boolean lastprdductusageupdateok = false;
    public static final String THISCLASS = DBProductUsageMethods.class.getSimpleName();
    private static final String LOGTAG = "SW_DBPUM";
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat sdf = new SimpleDateFormat(StandardAppConstants.EXTENDED_DATE_FORMAT);

    /**
     * Instantiates a new Db product usage methods.
     *
     * @param ctxt the ctxt
     */
    DBProductUsageMethods(Context ctxt) {
        String msg = "Constructing";
        String methodname = "Construct";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        context = ctxt;
        dbdao = new DBDAO(context);
        db = dbdao.db;
    }

    /**************************************************************************
     * @return the number of ProductUsage rows
     */
    int getProductUsageCount() {
        return dbdao.getTableRowCount(
                DBProductusageTableConstants.PRODUCTUSAGE_TABLE
        );
    }

    /**************************************************************************
     * @return ProudctUsage id of the last added
     */
    long getLastProductusageAdded() { return lastproductusageadded; }

    /**************************************************************************
     * @return true if last ProductUsage was add OK, else false
     */
    boolean ifProductUsageAdded() { return lastproductusageaddok; }

    /**************************************************************************
     *
     * @return true if lastupdate attempt updated, esel false
     */
    boolean ifProductUsageUpdated() { return lastprdductusageupdateok; }

    /**************************************************************************
     * If product usage was duplicate boolean.
     *
     * @return true if attempt to insert productusage failed because the
     * ProductUsage (combination of AisleRef and ProductRef) already
     * existed, otherwisee false.
     */
    boolean ifProductUsageWasDuplicate() { return lastproductusageduplicate; }

    /**************************************************************************
     *
     * @param aisleid   id of the aisle
     * @return          the highest productusage order
     */
    int getHighestProductUsageOrderPerAisle(long aisleid) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);

        int rv = 0;

        String columns[] = new String[] {
                DBConstants.SQLMAX +
                        DBProductusageTableConstants.PRODUCTUSAGE_ORDER_COL +
                        DBConstants.SQLMAXCLOSE +
                        DBConstants.SQLAS +
                        DBProductusageTableConstants.PRODUCTUSAGEMAXORDERCOLUMN
        };
        String whereclause = DBProductusageTableConstants.PRODUCTUSAGE_AISLEREF_COL +
                " = ? ";
        String whereargs[] = new String[] {
                Long.toString(aisleid)
        };
        Cursor csr = db.query(
                DBProductusageTableConstants.PRODUCTUSAGE_TABLE,
                columns,
                whereclause,
                whereargs,
                null,null,null
        );
        if (csr.getCount() > 0 ) {
            csr.moveToFirst();
            rv = csr.getInt(csr.getColumnIndex(
                    DBProductusageTableConstants.PRODUCTUSAGEMAXORDERCOLUMN
            ));
        }
        csr.close();
        msg = "Highhest ProductUsage Order=" + Integer.toString(rv);
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        return rv;
    }


    /**************************************************************************
     * @param aisleref   id of the Aisle
     * @param productref id of the product
     * @return true if found, else false
     */
    boolean doesProductUsageExist(long aisleref, long productref) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        boolean rv = false;
        String filter = DBProductusageTableConstants.PRODUCTUSAGE_AISLEREF_COL_FULL +
                " = " +
                Long.toString(aisleref) +
                DBConstants.SQLAND +
                DBProductusageTableConstants.PRODUCTUSAGE_PRODUCTREF_COL_FULL +
                " = " +
                Long.toString(productref);
        Cursor csr = DBCommonMethods.getTableRows(db,
                DBProductusageTableConstants.PRODUCTUSAGE_TABLE,
                filter,
                ""
        );
        if(csr.getCount() > 0 ) {
            rv = true;
        }
        csr.close();
        msg = "Product Usage for AisleID=" + Long.toString(aisleref) +
                " ProductID=" + Long.toString(productref);
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        return rv;
    }

    /**************************************************************************
     *
     * @param aisleref      id of the aisle
     * @param productref    id of the product
     */
    void setChecklistCheckedStatus(long aisleref, long productref) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        int currentcheckstatus = 0;
        int toggledcheckstatus = 2;
        int updatecount = 0;
        String filter = DBProductusageTableConstants.PRODUCTUSAGE_AISLEREF_COL_FULL +
                " = " +
                Long.toString(aisleref) +
                DBConstants.SQLAND +
                DBProductusageTableConstants.PRODUCTUSAGE_PRODUCTREF_COL_FULL +
                " = " +
                Long.toString(productref);
        Cursor csr = DBCommonMethods.getTableRows(db,
                DBProductusageTableConstants.PRODUCTUSAGE_TABLE,
                filter,
                "");
        if (csr.getCount() > 0 ) {
            csr.moveToFirst();
            currentcheckstatus = csr.getInt(csr.getColumnIndex(
                    DBProductusageTableConstants.PRODUCTUSAGE_CHECKLISTFLAG_COL
            ));
            if (currentcheckstatus > 1) {
                toggledcheckstatus =1;
            }
        }
        csr.close();
        ContentValues cv = new ContentValues();
        String[] whereargs = {Long.toString(aisleref), Long.toString(productref)};
        String whereclause = DBProductusageTableConstants.PRODUCTUSAGE_AISLEREF_COL + " = ? " +
                DBConstants.SQLAND +
                DBProductusageTableConstants.PRODUCTUSAGE_PRODUCTREF_COL + " = ?";
        cv.put(DBProductusageTableConstants.PRODUCTUSAGE_CHECKLISTFLAG_COL,toggledcheckstatus);
        updatecount = db.update(
                DBProductusageTableConstants.PRODUCTUSAGE_TABLE,
                cv,
                whereclause,
                whereargs);
        msg = "Set Checklist Checked Status UpdateOK=" +
                Boolean.toString(updatecount > 0);
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
    }

    /**************************************************************************
     *
     */
    void resetChecklistCheckedStatus() {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        int updatecount = 0;
        String whereargs[] = { Integer.toString(1)};
        String whereclause = DBProductusageTableConstants.PRODUCTUSAGE_CHECKLISTFLAG_COL + " >= ?";
        ContentValues cv = new ContentValues();
        cv.put(DBProductusageTableConstants.PRODUCTUSAGE_CHECKLISTFLAG_COL,1);
        updatecount = db.update(
                DBProductusageTableConstants.PRODUCTUSAGE_TABLE,
                cv,
                whereclause,
                whereargs);
        msg = "RESET Checklist Updated " + Integer.toString(updatecount) + " Product Usages";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
    }

    /**************************************************************************
     * getProductUsages - get ProductUsages as a Cursor
     *
     * @param filter filter  clause, if required, less WHERE keyword
     * @param order  order clause, if required, LESS ORDER BY keywords
     * @return product usages
     */
    Cursor getProductUsages(String filter, String order) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        return dbdao.getTableRows(DBProductusageTableConstants.PRODUCTUSAGE_TABLE,
                "",
                filter,
                order);
    }

    /**************************************************************************
     * Get ProductUsages that have been set to appear in the checklist
     * Note! adds 0 in the Shopping List if there is no current Shopping list entry
     * for the productUsage SQL generated for this is (brackets included):-
     * (CASE WHEN shoplist.numbertoget IS NULL THEN 0 ELSE shoplist.numbertoget END)
     *
     *
     * @param filter    SQL WHERE clause less WHERE Noting that
     *                  productusage.productusagechecklistflag > 0 WILL ALWAYS
     *                  be the first item of the WHERE clause.
     * @param order     SQL ORDER clause (none by default)
     * @return          The generated SQLIte DB Cursor
     */
    Cursor getCheckList(String filter, String order) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        Cursor rv;
        String sql = DBConstants.SQLSELECT +
                " ABS(RANDOM()) AS " + DBConstants.STD_ID + ", " +
                DBProductusageTableConstants.PRODUCTUSAGE_AISLEREF_COL_FULL + ", " +
                DBProductusageTableConstants.PRODUCTUSAGE_PRODUCTREF_COL_FULL + ", " +
                DBProductusageTableConstants.PRODUCTUSAGE_COST_FULL + ", " +
                DBProductusageTableConstants.PRODUCTUSAGE_BUYCOUNT_COL_FULL + ", " +
                DBProductusageTableConstants.PRODUCTUSAGE_FIRSTBUYDATE_COL_FULL + ", " +
                DBProductusageTableConstants.PRODUCTUSAGE_LATESTBUYDATE_COL_FULL + ", " +
                DBProductusageTableConstants.PRODUCTUSAGE_ORDER_COL_FULL + ", " +
                DBProductusageTableConstants.PRODUCTUSAGE_RULESUGGESTFLAG_COL_FULL + ", " +
                DBProductusageTableConstants.PRODUCTUSAGE_CHECKLISTFLAG_COL_FULL + ", " +
                DBProductusageTableConstants.PRODUCTUSAGE_CHECKLISTCOUNT_COL_FULL + ", " +

                "(CASE WHEN " +
                DBShopListTableConstants.SHOPLIST_NUMBERTOGET_COL_FULL +
                " IS NULL THEN 0 ELSE " +
                DBShopListTableConstants.SHOPLIST_NUMBERTOGET_COL_FULL +
                " END ) AS " +
                DBConstants.CALCULATED_PRODUTSORDERED_NAME
                + ", " +

                DBProductsTableConstants.PRODUCTS_NAME_COL_FULL + ", " +

                DBAislesTableConstants.AISLES_SHOPREF_COL_FULL + ", " +
                DBAislesTableConstants.AISLES_NAME_COL_FULL + ", " +
                DBAislesTableConstants.AISLES_ORDER_COL_FULL + ", " +

                DBShopsTableConstants.SHOPS_NAME_COL_FULL + ", " +
                DBShopsTableConstants.SHOPS_CITY_COL_FULL + ", " +
                DBShopsTableConstants.SHOPS_ORDER_COL_FULL + " " +

                DBConstants.SQLFROM + DBProductusageTableConstants.PRODUCTUSAGE_TABLE +

                DBConstants.SQLLEFTJOIN + DBProductsTableConstants.PRODUCTS_TABLE +
                DBConstants.SQLON +
                DBProductusageTableConstants.PRODUCTUSAGE_PRODUCTREF_COL_FULL + " = " +
                DBProductsTableConstants.PRODUCTS_ID_COL_FULL +

                DBConstants.SQLLEFTJOIN +
                DBAislesTableConstants.AISLES_TABLE +
                DBConstants.SQLON +
                DBProductusageTableConstants.PRODUCTUSAGE_AISLEREF_COL_FULL + " = " +
                DBAislesTableConstants.AISLES_ID_COL_FULL +

                DBConstants.SQLLEFTJOIN +
                DBShopsTableConstants.SHOPS_TABLE +
                DBConstants.SQLON +
                DBAislesTableConstants.AISLES_SHOPREF_COL_FULL + " = " +
                DBShopsTableConstants.SHOPS_ID_COL_FULL +

                DBConstants.SQLLEFTJOIN +
                DBShopListTableConstants.SHOPLIST_TABLE +
                DBConstants.SQLON +
                DBProductusageTableConstants.PRODUCTUSAGE_PRODUCTREF_COL_FULL + " = " +
                DBShopListTableConstants.SHOPLIST_PRODUCTREF_COL_FULL +
                DBConstants.SQLAND +
                DBProductusageTableConstants.PRODUCTUSAGE_AISLEREF_COL_FULL + " = " +
                DBShopListTableConstants.SHOPLIST_AISLEREF_COL_FULL +
                DBConstants.SQLWHERE +
                DBProductusageTableConstants.PRODUCTUSAGE_CHECKLISTFLAG_COL_FULL + " > 0 ";
        if (filter.length() > 0 ) {
            sql = sql + DBConstants.SQLAND + filter;
        }
        if (order.length() > 0 ) {
            sql = sql + DBConstants.SQLORDERBY + order;
        }
        sql = sql + DBConstants.SQLENDSTATEMENT;

        rv = db.rawQuery(sql,null);
        msg = "Returned Cursor with " + Integer.toString(rv.getCount()) + " rows." +
                "\n\t SQL used=" + sql;
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        return rv;

    }

    /**************************************************************************
     *
     * @param filter    SQL filter clause less WHERE
     * @param order     SQL ORDER clause less ORDER BY
     * @return          Cursor containing expanded product usage
     *                  expanded means joined to referenced tables
     *                  i.e. respective Aisle, Product Shop (from aisle)
     *                  and ShopList
     */
    Cursor getExpandedProductUsages(String filter, String order) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        Cursor rv;
        String sql = DBConstants.SQLSELECT +
                " ABS(RANDOM()) AS " + DBConstants.STD_ID + ", " +
                DBProductusageTableConstants.PRODUCTUSAGE_AISLEREF_COL_FULL + ", " +
                DBProductusageTableConstants.PRODUCTUSAGE_PRODUCTREF_COL_FULL + ", " +
                DBProductusageTableConstants.PRODUCTUSAGE_COST_FULL + ", " +
                DBProductusageTableConstants.PRODUCTUSAGE_BUYCOUNT_COL_FULL + ", " +
                DBProductusageTableConstants.PRODUCTUSAGE_FIRSTBUYDATE_COL_FULL + ", " +
                DBProductusageTableConstants.PRODUCTUSAGE_LATESTBUYDATE_COL_FULL + ", " +
                DBProductusageTableConstants.PRODUCTUSAGE_ORDER_COL_FULL + ", " +
                DBProductusageTableConstants.PRODUCTUSAGE_RULESUGGESTFLAG_COL_FULL + ", " +
                DBProductusageTableConstants.PRODUCTUSAGE_CHECKLISTFLAG_COL_FULL + ", " +
                DBProductusageTableConstants.PRODUCTUSAGE_CHECKLISTCOUNT_COL_FULL + ", " +

                "(CASE WHEN " +
                DBShopListTableConstants.SHOPLIST_NUMBERTOGET_COL_FULL +
                " IS NULL THEN 0 ELSE " +
                DBShopListTableConstants.SHOPLIST_NUMBERTOGET_COL_FULL +
                " END ) AS " +
                DBConstants.CALCULATED_PRODUTSORDERED_NAME
                + ", " +

                DBProductsTableConstants.PRODUCTS_NAME_COL_FULL + ", " +

                DBAislesTableConstants.AISLES_SHOPREF_COL_FULL + ", " +
                DBAislesTableConstants.AISLES_NAME_COL_FULL + ", " +
                DBAislesTableConstants.AISLES_ORDER_COL_FULL + ", " +

                DBShopsTableConstants.SHOPS_NAME_COL_FULL + ", " +
                DBShopsTableConstants.SHOPS_CITY_COL_FULL + ", " +
                DBShopsTableConstants.SHOPS_ORDER_COL_FULL + " " +

                DBConstants.SQLFROM + DBProductusageTableConstants.PRODUCTUSAGE_TABLE +

                DBConstants.SQLLEFTJOIN + DBProductsTableConstants.PRODUCTS_TABLE +
                DBConstants.SQLON +
                DBProductusageTableConstants.PRODUCTUSAGE_PRODUCTREF_COL_FULL + " = " +
                DBProductsTableConstants.PRODUCTS_ID_COL_FULL +

                DBConstants.SQLLEFTJOIN +
                DBAislesTableConstants.AISLES_TABLE +
                DBConstants.SQLON +
                DBProductusageTableConstants.PRODUCTUSAGE_AISLEREF_COL_FULL + " = " +
                DBAislesTableConstants.AISLES_ID_COL_FULL +

                DBConstants.SQLLEFTJOIN +
                DBShopsTableConstants.SHOPS_TABLE +
                DBConstants.SQLON +
                DBAislesTableConstants.AISLES_SHOPREF_COL_FULL + " = " +
                DBShopsTableConstants.SHOPS_ID_COL_FULL +

                DBConstants.SQLLEFTJOIN +
                DBShopListTableConstants.SHOPLIST_TABLE +
                DBConstants.SQLON +
                DBProductusageTableConstants.PRODUCTUSAGE_PRODUCTREF_COL_FULL + " = " +
                DBShopListTableConstants.SHOPLIST_PRODUCTREF_COL_FULL +
                DBConstants.SQLAND +
                DBProductusageTableConstants.PRODUCTUSAGE_AISLEREF_COL_FULL + " = " +
                DBShopListTableConstants.SHOPLIST_AISLEREF_COL_FULL;
        if (filter.length() > 0 ) {
            sql = sql + DBConstants.SQLWHERE + filter;
        }
        if (order.length() > 0 ) {
            sql = sql + DBConstants.SQLORDERBY + order;
        }
        sql = sql + DBConstants.SQLENDSTATEMENT;

        rv = db.rawQuery(sql,null);
        msg = "Returned Cursor with " + Integer.toString(rv.getCount()) + " rows." +
                "\n\t SQL used=" + sql;
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        return rv;
    }

    /**************************************************************************
     *
     * @param aisleid
     * @param productid
     * @return
     */
    ArrayList<String> stockDeleteImapct(long aisleid, long productid) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        ArrayList<String> rv = new ArrayList<>();

        String pufilter = DBProductusageTableConstants.PRODUCTUSAGE_AISLEREF_COL_FULL +
                " = " + Long.toString(aisleid) +
                " AND " +
                DBProductsTableConstants.PRODUCTS_ID_COL_FULL +
                " = " + Long.toString(productid);
        Cursor pucsr = getExpandedProductUsages(pufilter,"");
        if (pucsr.getCount() > 0 ) {
            while (pucsr.moveToNext()) {
                String aislename = pucsr.getString(pucsr.getColumnIndex(
                        DBAislesTableConstants.AISLES_NAME_COL
                ));
                String shopname = pucsr.getString(pucsr.getColumnIndex(
                        DBShopsTableConstants.SHOPS_NAME_COL
                ));
                String productname = pucsr.getString(pucsr.getColumnIndex(
                        DBProductsTableConstants.PRODUCTS_NAME_COL
                ));
                rv.add("Stocked Item " + productname +
                " in Aisle " + aislename +
                ", Shop " + shopname +
                " would be deleted.");
                rv.addAll(
                        new DBShopListMethods(context).shopListEntryDeleteImpact(
                                aisleid,
                                productid
                        )
                );
                rv.addAll(
                        new DBRuleMethods(context).ruleDeleteImpact(
                                aisleid,
                                productid
                        )
                );
            }
        }
        return rv;
    }

    /**************************************************************************
     *
     * @param aisled
     * @param productid
     * @param intransaction
     * @return
     */
    int deleteStock(long aisled, long productid, boolean intransaction) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);

        int rv = 0;

        String slfilter = DBShopListTableConstants.SHOPLIST_AISLEREF_COL_FULL +
                " = " + Long.toString(aisled) +
                " AND " +
                DBShopListTableConstants.SHOPLIST_PRODUCTREF_COL_FULL +
                " = " + Long.toString(productid);
        Cursor sllcsr = new DBShopListMethods(context)
                .getShopListEntries(slfilter,"");
        String rlfilter = DBRulesTableConstants.RULES_AISLEREF_COL_FULL +
                " = " + Long.toString(aisled) +
                " AND " +
                DBRulesTableConstants.RULES_PRODUCTREF_COL_FULL +
                " = " + Long.toString(productid);
        Cursor rlcsr = new DBRuleMethods(context).getRules(rlfilter,"");

        if(!intransaction) {
            db.beginTransaction();
            msg = "Starting DB Transaction";
            LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        }

        while (sllcsr.moveToNext()) {
            long shoplistid = sllcsr.getLong(sllcsr.getColumnIndex(
                    DBShopListTableConstants.SHOPLIST_ID_COL
            ));
            new DBShopListMethods(context).deleteShopListEntry(shoplistid);
        }
        sllcsr.close();

        while (rlcsr.moveToNext()) {
            long ruleid = rlcsr.getLong(rlcsr.getColumnIndex(
                    DBRulesTableConstants.RULES_ID_COL
            ));
            new DBRuleMethods(context).deleteRule(ruleid);
        }
        rlcsr.close();

        String dlt_shoplistentry_whereclause =
                DBProductusageTableConstants.PRODUCTUSAGE_AISLEREF_COL +
                        " = ? AND " +
                DBProductusageTableConstants.PRODUCTUSAGE_PRODUCTREF_COL +
                        " = ?";
        String dlt_shoplistentry_whereargs[] = {
                Long.toString(aisled),
                Long.toString(productid)
        };
        rv = db.delete(DBProductusageTableConstants.PRODUCTUSAGE_TABLE,
                dlt_shoplistentry_whereclause,
                dlt_shoplistentry_whereargs);
        if(!intransaction) {
            db.setTransactionSuccessful();
            db.endTransaction();
            msg = "SET AND ENDED DB Transaction";
            LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        }
        return rv;
    }

    /**************************************************************************
     *
     * @param aisleid
     * @param productid
     * @param newcost
     */
    void amendProductUsageCost(long aisleid,
                               long productid,
                               double newcost) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        int updatecount = 0;
        ContentValues cv = new ContentValues();
        lastprdductusageupdateok = false;
        String whereclause =
                DBProductusageTableConstants.PRODUCTUSAGE_AISLEREF_COL +
                        "= ? AND " +
                        DBProductusageTableConstants.PRODUCTUSAGE_PRODUCTREF_COL +
                        " = ? ";
        String whereargs[] = new String[] {
                Long.toString(aisleid),
                Long.toString(productid)
        };
        cv.put(DBProductusageTableConstants.PRODUCTUSAGE_COST_COL,newcost);
        if (doesProductUsageExist(aisleid,productid)) {
            updatecount = db.update(DBProductusageTableConstants.PRODUCTUSAGE_TABLE,
                    cv,whereclause,whereargs);
            if (updatecount > 0) {
                lastprdductusageupdateok = true;
            }
        }
        msg = "Update Cost=" + Boolean.toString(updatecount > 0) +
                " for ProductUsage AisleID=" + Long.toString(aisleid) +
                " ProductID=" + Long.toString(productid);
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
    }

    /**************************************************************************
     *
     * @param aisleid           id of the aisle
     * @param productid         id of the product
     * @param numberpurchased   number of the items purchased
     */
    void amendPurchasedProductUsage(long aisleid,
                                    long productid,
                                    int numberpurchased) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);

        long currentfirstbuydate = -1;
        int currentbuycount = 0;
        int updatecount = 0;
        lastprdductusageupdateok = false;
        ContentValues cv = new ContentValues();
        String whereclause =
                DBProductusageTableConstants.PRODUCTUSAGE_AISLEREF_COL +
                        " = ? AND " +
                        DBProductusageTableConstants.PRODUCTUSAGE_PRODUCTREF_COL +
                        " = ? ";
        String whereargs[] = new String[] {
                Long.toString(aisleid),
                Long.toString(productid)
        };
        Cursor csr = db.query(DBProductusageTableConstants.PRODUCTUSAGE_TABLE,
                null,whereclause,whereargs,null,null,null
        );

        if (csr.getCount() > 0) {
            csr.moveToFirst();
            currentfirstbuydate =  csr.getLong(csr.getColumnIndex(
                    DBProductusageTableConstants.PRODUCTUSAGE_FIRSTBUYDATE_COL
            ));
            if (currentfirstbuydate == 0) {
                cv.put(DBProductusageTableConstants.PRODUCTUSAGE_FIRSTBUYDATE_COL,
                        System.currentTimeMillis());
            }
            cv.put(DBProductusageTableConstants.PRODUCTUSAGE_LATESTBUYDATE_COL,
                    System.currentTimeMillis());
            currentbuycount = csr.getInt(csr.getColumnIndex(
                    DBProductusageTableConstants.PRODUCTUSAGE_BUYCOUNT_COL
            ));
            cv.put(DBProductusageTableConstants.PRODUCTUSAGE_BUYCOUNT_COL,
                    currentbuycount + numberpurchased);
            csr.close();
            updatecount = db.update(DBProductusageTableConstants.PRODUCTUSAGE_TABLE,
                    cv,
                    whereclause,
                    whereargs
            );
            if (updatecount > 0 ) {
                lastprdductusageupdateok = true;
            }
        } else {
            csr.close();
        }
        msg = "Productusage AisleID=" + Long.toString(aisleid) +
                " ProductID=" + Long.toString(productid) +
                " Purchased Update=" + Boolean.toString(updatecount > 0);
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
    }

    /**************************************************************************
     *
     * @param productref
     * @param aisleref
     * @param cost
     * @param order
     * @param checklistflag
     * @param checklistcount
     */
    void modifyProductUsage(long productref,
                            long aisleref,
                            double cost,
                            int order,
                            boolean checklistflag,
                            int checklistcount) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);

        lastprdductusageupdateok = false;
        int zero = 0;
        long lzero = 0;
        int modified = 0;
        String whereclause =
                DBProductusageTableConstants.PRODUCTUSAGE_AISLEREF_COL +
                " = ? AND " +
                DBProductusageTableConstants.PRODUCTUSAGE_PRODUCTREF_COL +
                " = ?";
        String whereargs[] = {Long.toString(aisleref),Long.toString(productref)};
        if (doesProductUsageExist(aisleref,productref)) {
            lastproductusageduplicate = false;
            ContentValues cv = new ContentValues();
            cv.put(DBProductusageTableConstants.PRODUCTUSAGE_COST_COL, cost);
            cv.put(DBProductusageTableConstants.PRODUCTUSAGE_ORDER_COL, order);
            cv.put(DBProductusageTableConstants.PRODUCTUSAGE_CHECKLISTFLAG_COL,checklistflag);
            cv.put(DBProductusageTableConstants.PRODUCTUSAGE_CHECKLISTCOUNT_COL,checklistcount);
            modified = db.update(DBProductusageTableConstants.PRODUCTUSAGE_TABLE,
                    cv,whereclause,whereargs);
            if (modified > 0 ) {
                lastprdductusageupdateok = true;
            }

        }
        msg = "ProductUsage for AisleID=" + Long.toString(aisleref) +
                " ProductID=" + Long.toString(productref) +
                " Modified=" + Boolean.toString(lastprdductusageupdateok);
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
    }

    /**************************************************************************
     * insertProductUsage       Insert/Add a ProductUsage entry
     *
     * @param productref The id of the Product
     * @param aisleref   The id of the Aisle
     * @param cost       The cost to be associated with this
     *                   ProductUsage
     * @param order      The order within the aisle
     *                   NOTE!! combination of productref and aisleref
     *                   has to be unqiue for the insertion to work.
     *                   If not then lastproductusageduplicate will
     *                   be set to true
     */
    void insertProductUsage(long productref,
                            long aisleref,
                            double cost,
                            int order,
                            boolean checklistflag,
                            int checklistcount) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);

        int zero = 0;
        long lzero = 0;
        long addedid;
        if (!doesProductUsageExist(aisleref, productref)) {
            lastproductusageduplicate = false;
            ContentValues cv = new ContentValues();
            cv.put(DBProductusageTableConstants.PRODUCTUSAGE_PRODUCTREF_COL,
                    productref);
            cv.put(DBProductusageTableConstants.PRODUCTUSAGE_AISLEREF_COL,
                    aisleref);
            cv.put(DBProductusageTableConstants.PRODUCTUSAGE_BUYCOUNT_COL, zero);
            cv.put(DBProductusageTableConstants.PRODUCTUSAGE_COST_COL, cost);
            cv.put(DBProductusageTableConstants.PRODUCTUSAGE_FIRSTBUYDATE_COL, lzero);
            cv.put(DBProductusageTableConstants.PRODUCTUSAGE_LATESTBUYDATE_COL, lzero);
            cv.put(DBProductusageTableConstants.PRODUCTUSAGE_ORDER_COL, order);
            cv.put(DBProductusageTableConstants.PRODUCTUSAGE_RULESUGGESTFLAG_COL, zero);
            cv.put(DBProductusageTableConstants.PRODUCTUSAGE_CHECKLISTFLAG_COL,checklistflag);
            cv.put(DBProductusageTableConstants.PRODUCTUSAGE_CHECKLISTCOUNT_COL,checklistcount);
            addedid = db.insert(DBProductusageTableConstants.PRODUCTUSAGE_TABLE,
                    null,
                    cv);
            if (addedid > -1) {
                lastproductusageadded = addedid;
                lastproductusageaddok = true;
            } else {
                lastproductusageaddok = false;
            }
        } else {
            lastproductusageduplicate = true;
            lastproductusageaddok = false;
        }
        msg = "ProductUsage AisleID=" + Long.toString(aisleref) +
                " ProductID=" + Long.toString(productref) +
                " Inserted=" + Boolean.toString(lastproductusageaddok) +
                " Duplicate=" + Boolean.toString(lastproductusageduplicate);
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
    }

    /**************************************************************************
     * getBuyCount          return the current Productusage BuyCount as an
     * integer, if not found then returns -1
     * Buycount should never be -1.
     *
     * @param productref id of the product
     * @param aisleref   id of the aisle
     * @return a MixTripleLongTripleInt (3 longs and 3 ints) :-                          long1 the firstbutdate                          long2 the latestbuydate                          long3 unused                          int1 the buycount                          int2 unused                          int3 unused
     */
    MixTripleLongTripleInt getBuyCount(long productref, long aisleref) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        int buycount = -1;
        long firstbuydate = 0;
        long lastbuydate = 0;
        MixTripleLongTripleInt rv = new MixTripleLongTripleInt();
        if (doesProductUsageExist(aisleref, productref)) {
            String filter = buildSingleProductUsageFilter(productref, aisleref);
            Cursor csr = getProductUsages(filter, "");
            if (csr.moveToFirst()) {
                buycount = csr.getInt(
                        csr.getColumnIndex(
                                DBProductusageTableConstants.PRODUCTUSAGE_BUYCOUNT_COL));
                firstbuydate = csr.getLong(
                        csr.getColumnIndex(
                                DBProductusageTableConstants.PRODUCTUSAGE_FIRSTBUYDATE_COL));
                lastbuydate = csr.getLong(
                        csr.getColumnIndex(
                                DBProductusageTableConstants.PRODUCTUSAGE_LATESTBUYDATE_COL));
            }
            csr.close();
        }
        rv.setMIXTRPPLONGINT(firstbuydate,lastbuydate,0,buycount,0,0);
        msg = "Returning for ProductUsage AisleID=" + Long.toString(aisleref) +
                " ProductID=" + Long.toString(productref) +
                " :- \n\tBought=" + Integer.toString(buycount) +
                " First Bought=" + sdf.format(firstbuydate) +
                " Last Bought=" + sdf.format(lastbuydate);
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        return rv;
    }

    /**************************************************************************
     * incrementBuyCount        increment the buycount, the firstbuydate
     * and the lastbuydate to reflect that the
     * productusage has been purchased.
     * Note! firstbuydate is only updated if
     * it is 0 (as initialised).
     *
     * @param productref product id referenced by this productusage
     * @param aisleref   aisle id referenced by this productusage
     */
    void incrementBuyCount(Long productref, Long aisleref) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        lastprdductusageupdateok = false;

        // Get the buycount, firstbuydate and latestbuydate
        MixTripleLongTripleInt mixvalues = getBuyCount(productref, aisleref);
        // increment the buycount
        int buycount = mixvalues.getint1() + 1;
        long firstbuydate = mixvalues.getlong1();
        long lastbuydate = new Date().getTime();
        if (firstbuydate == 0) { firstbuydate = lastbuydate; }
        if (buycount < 1 ) { return; }
        ContentValues cv = new ContentValues();
        cv.put(DBProductusageTableConstants.PRODUCTUSAGE_BUYCOUNT_COL,
                buycount);
        cv.put(DBProductusageTableConstants.PRODUCTUSAGE_FIRSTBUYDATE_COL,
                firstbuydate);
        cv.put(DBProductusageTableConstants.PRODUCTUSAGE_LATESTBUYDATE_COL,
                lastbuydate);
        String whereargs[] = {Long.toString(productref), Long.toString(aisleref)};
        String whereclause = buildCVWhereClause();
        int updatecount = db.update(DBProductusageTableConstants.PRODUCTUSAGE_TABLE,
                cv,
                whereclause,
                whereargs);
        if (updatecount > 0 ) {
            lastprdductusageupdateok = true;
        }
        msg = "ProductUsage AisleID=" + Long.toString(aisleref) +
                " ProductID=" + Long.toString(productref) +
                " BuyCount Incremented=" + Boolean.toString(lastprdductusageupdateok);
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
    }

    /**************************************************************************
     * setProductusageOrder     set the order of a specific ProductUsage
     *
     * @param productref product id referenced by the ProductUsage
     * @param aisleref   aisle id referenced by the ProductUsage
     * @param neworder   the new order to be used
     */
    void setProductUsageOrder(long productref, long aisleref, int neworder) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);

        lastprdductusageupdateok = false;
        if (doesProductUsageExist(productref, aisleref)) {
            String whereargs[] = {Long.toString(productref), Long.toString(aisleref)};
            ContentValues cv = new ContentValues();
            cv.put(DBProductusageTableConstants.PRODUCTUSAGE_ORDER_COL,neworder);
            int updatecount = db.update(DBProductusageTableConstants.PRODUCTUSAGE_TABLE,
                    cv,
                    buildCVWhereClause(),
                    whereargs);
            if (updatecount > 0 ) {
                lastprdductusageupdateok = true;
            }
        }
        msg = "ProductUsage AisleID=" + Long.toString(aisleref) +
                " ProductID=" + Long.toString(productref) +
                " Order Updated=" + Boolean.toString(lastprdductusageupdateok);
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
    }

    /**************************************************************************
     * setRuleSuggestFlag   set the RuleSuggestFlag
     *
     * @param productref product id referenced by this productusage
     * @param aisleref   aisle id referenced by this productusage
     * @param flag       flag (0=clear, 1=to skip, 2=disable)
     */
    void setRuleSuggestFlag(long productref, long aisleref, int flag) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);

        lastprdductusageupdateok = false;
        if (doesProductUsageExist(aisleref,productref)) {
            ContentValues cv = new ContentValues();
            cv.put(DBProductusageTableConstants.PRODUCTUSAGE_RULESUGGESTFLAG_COL, flag);
            String whereargs[] = {Long.toString(productref), Long.toString(aisleref)};
            int updatecount = db.update(DBProductusageTableConstants.PRODUCTUSAGE_TABLE,
                    cv,
                    buildCVWhereClause(),
                    whereargs);
            if (updatecount > 0 ) {
                lastprdductusageupdateok = true;
            }
        }
        msg = "ProductUsage AisleID=" + Long.toString(aisleref) +
                " ProductID=" + Long.toString(productref) +
                " RuleSuggestFlag Updated=" + Boolean.toString(lastprdductusageupdateok);
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
    }

    /**************************************************************************
     * enableSkipped    change ruleSggestFlags from skipped to clear
     */
    void enableSkipped() {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);

        lastprdductusageupdateok = false;
        int updatecount = 0;
        String whereargs[] = {Integer.toString(DBProductusageTableConstants.RULESUGGESTFLAG_SKIP)};
        String whereclause = DBProductusageTableConstants.PRODUCTUSAGE_RULESUGGESTFLAG_COL +
                " = ?";
        ContentValues cv = new ContentValues();
        cv.put(DBProductusageTableConstants.PRODUCTUSAGE_RULESUGGESTFLAG_COL,
                DBProductusageTableConstants.RULESUGGESTFLAG_CLEAR);
        updatecount = db.update(DBProductusageTableConstants.PRODUCTUSAGE_TABLE,
                cv,whereclause,
                whereargs);
        if (updatecount > 0 ) {
            lastprdductusageupdateok = true;
        }
        msg = "RuleSuggestFlags Cleared=" + Long.toString(updatecount);
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
    }

    /**************************************************************************
     * buildSingleProductUsageFilter    Build a filter string for a specific
     *                                  Productusage
     * @param productref    product id referenced by the ProductUsage
     * @param aislref       aisle id referenced by the ProductUsage
     * @return              Filter String
     */
    private String buildSingleProductUsageFilter(long productref, long aislref) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        return
                DBProductusageTableConstants.PRODUCTUSAGE_PRODUCTREF_COL_FULL +
                        " = " +
                        Long.toString(productref) +
                        DBConstants.SQLAND +
                        DBProductusageTableConstants.PRODUCTUSAGE_AISLEREF_COL_FULL +
                        " = " +
                        Long.toString(aislref) +
                        " ";
    }

    /**************************************************************************
     * buildCVWhereClause   build the ContentValues clause for a single
     *                      Productusage (albiet it generic and driven
     *                      by the whereagrs)
     * @return              ContentValues Where Clause
     */
    private String buildCVWhereClause() {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        return DBProductusageTableConstants.PRODUCTUSAGE_PRODUCTREF_COL +
                " = ? " + DBConstants.SQLAND +
                DBProductusageTableConstants.PRODUCTUSAGE_AISLEREF_COL +
                " = ?";
    }
}
