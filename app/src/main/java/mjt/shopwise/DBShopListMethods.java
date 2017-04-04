package mjt.shopwise;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;

import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Shoplist Database Methods
 */

@SuppressWarnings({"FieldCanBeLocal", "WeakerAccess", "CanBeFinal"})
public class DBShopListMethods {

    private static final String LOGTAG = "SW-DBSLM";
    private Context context;
    private DBDAO dbdao;
    private static SQLiteDatabase db;
    private static TRPLLONG lastshoplistadded = new TRPLLONG(0,0,0);
    private static boolean lastshoplistaddedok = false;
    private static boolean lastshoplistupdatedok = false;
    public static final String THISCLASS = DBShopListMethods.class.getSimpleName();


    /**************************************************************************
     * Instantiates a new DBShopListMethods
     * @param ctxt  the Context to be used
     */
    DBShopListMethods(Context ctxt) {
        String msg = "Constructing";
        String methodname = "Construct";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        context = ctxt;
        dbdao = new DBDAO(context);
        db = dbdao.db;
    }

    /**************************************************************************
     * return the number of rows in the ShopLIst table
     * @return  number of rows on the ShopList table
     */
    int getShopListCount() {
        return dbdao.getTableRowCount(
                DBShopListTableConstants.SHOPLIST_TABLE
        );
    }

    /**************************************************************************
     * get the last ShopList entry that was added
     * @return  TRPLLONG (3 longs) id,  aisleid and productid
     */
    @SuppressWarnings("unused")
    TRPLLONG getLastShopListEntryAdded() { return lastshoplistadded; }

    /**************************************************************************
     * return true of the last shoplistentry was added ok, esle false
     * @return true is the shoplistentry was added, esle false
     */
    @SuppressWarnings("unused")
    boolean ifShopListEntryAdded() { return lastshoplistaddedok; }

    /**************************************************************************
     *
     * @param shoplistid the id of the shoplistentry
     * @return the cursor containing the shoplist data
     */
    Cursor getShopListEntry(long shoplistid) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        Cursor rv;
        String filter = DBShopListTableConstants.SHOPLIST_ID_COL_FULL +
                " = " + Long.toString(shoplistid) +
                DBConstants.SQLENDSTATEMENT;
        rv =  DBCommonMethods.getTableRows(db,
                DBShopListTableConstants.SHOPLIST_TABLE,
                filter,
                ""
        );
        msg = "Returning Cursor for ShoplistID=" +
                Long.toString(shoplistid) +
                " rows=" + Integer.toString(rv.getCount());
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        return rv;
    }

    /**************************************************************************
     *
     * @param shoplistid the id of the shoplistentry to check
     * @return the id of the shoplistentry
     */
    boolean doesShopListEntryExist(long shoplistid) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        boolean rv = false;
        Cursor csr = getShopListEntry(shoplistid);
        if (csr.getCount() > 0 ) {
            rv = true;
        }
        csr.close();
        msg = "ShoplistID=" + Long.toString(shoplistid) +
                "Exists=" + Boolean.toString(rv);
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        return rv;
    }

    /**************************************************************************
     *
     * @return the value of the shoplistupdated flag
     */
    boolean ifShopListEntryUpdated() { return lastshoplistupdatedok; }

    /**************************************************************************
     *
     * @return the value of the shoplistadded flag
     */
    @SuppressWarnings("unused")
    boolean ifShopListentryAdded() { return lastshoplistaddedok; }

    /**************************************************************************
     *
     * @param aisleid   the id of the aisle to be checked for
     * @param productid the id of the product to be checked for
     * @return          true if the shoplist entry exists, esle false
     */
    boolean doesShopListEntryExist(long aisleid, long productid) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        boolean rv = false;
        String whereargs[] = new String[] {Long.toString(aisleid),
                Long.toString(productid)};
        String whereclause = DBShopListTableConstants.SHOPLIST_AISLEREF_COL +
                " = ? AND " +
                DBShopListTableConstants.SHOPLIST_PRODUCTREF_COL +
                " = ? ";
        Cursor csr = db.query(DBShopListTableConstants.SHOPLIST_TABLE,null,
                whereclause,whereargs,
                null,null,null,null);
        if (csr.getCount() > 0 ) {
            rv = true;
        }
        csr.close();
        msg = "ShoppingList Entry for AisleID=" + Long.toString(aisleid) +
                " ProductID=" + Long.toString(productid) +
                " Exists=" + Boolean.toString(rv);
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        return rv;
    }


    /**************************************************************************
     * Tidy Shopping List - remove shoppinglist rows that are complete
     *                      i.e. the numbertoget is 0. Effectively this
     *                      removes items that appear as checked-off.
     */
    void tidyShoppingList() {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        int cleancount;
        String whereclause = DBShopListTableConstants.SHOPLIST_NUMBERTOGET_COL +
                " - " +
                DBShopListTableConstants.SHOPLIST_DONE_COL +
                " < 1";
        cleancount = db.delete(DBShopListTableConstants.SHOPLIST_TABLE,
                whereclause,null);
        Cursor csr = db.query(DBShopListTableConstants.SHOPLIST_TABLE,null,null,null,null,null,null);
        while (csr.moveToNext()) {
            long shopid = csr.getLong(csr.getColumnIndex(
                    DBShopListTableConstants.SHOPLIST_ID_COL
            ));
            int newnumbertoget = csr.getInt(csr.getColumnIndex(
                    DBShopListTableConstants.SHOPLIST_NUMBERTOGET_COL)) -
                    csr.getInt(csr.getColumnIndex(
                            DBShopListTableConstants.SHOPLIST_DONE_COL
                    ));
            ContentValues cv = new ContentValues();
            String[] whereargs = new String[] {Long.toString(shopid)};
            whereclause = DBShopListTableConstants.SHOPLIST_ID_COL + " = ?";
            cv.put(DBShopListTableConstants.SHOPLIST_NUMBERTOGET_COL,newnumbertoget);
            cv.put(DBShopListTableConstants.SHOPLIST_DONE_COL,0);
            db.update(DBShopListTableConstants.SHOPLIST_TABLE,cv,whereclause,whereargs);

        }
        csr.close();
        msg = "Tidied " + Integer.toString(cleancount) +
                " ShoppingList Entries.";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
    }

    /**************************************************************************
     * getTotals - return the total cost of the items to be purchased,
     *              the amount remaining and the amount spent
     * @param filter    filter if any
     * @return          returns a TRPLDBL (3 doubles) with totalcost,
     *                  remaining cost and amount spent
     */
    TRPLDBL getTotals(@SuppressWarnings("SameParameterValue") String filter) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);

        TRPLDBL rv = new TRPLDBL();
        rv.setTRPLDBL(-1,-1,-1);
        String sql = DBConstants.SQLSELECT +
                DBConstants.SQLSUM +
                DBShopListTableConstants.TOTALCOST +
                DBConstants.SQLSUMCLOSE +
                " AS " + DBShopListTableConstants.TOTALCOST +  ", " +

                DBConstants.SQLSUM +
                DBShopListTableConstants.TOTALREMAINING +
                DBConstants.SQLSUMCLOSE +
                " AS " + DBShopListTableConstants.TOTALREMAINING + ", " +

                DBConstants.SQLSUM +
                DBShopListTableConstants.TOTALSPENT +
                DBConstants.SQLSUMCLOSE +
                " AS " + DBShopListTableConstants.TOTALSPENT +
                DBConstants.SQLFROM + "(" +
                DBConstants.SQLSELECT +
                "("+
                // (shoplistnumbertoget * shoplistcost) AS totalcost,
                DBShopListTableConstants.SHOPLIST_NUMBERTOGET_COL_FULL + " *+ " +
                DBProductusageTableConstants.PRODUCTUSAGE_COST_FULL +
                ") AS " + DBShopListTableConstants.TOTALCOST + ", " +

                // ((shoplistnumbertoget - shoplistdone) * shoplistcost) AS remainingcost,
                "((" +
                DBShopListTableConstants.SHOPLIST_NUMBERTOGET_COL_FULL + " - " +
                DBShopListTableConstants.SHOPLIST_DONE_COL +
                ") * " +
                DBProductusageTableConstants.PRODUCTUSAGE_COST_FULL +
                ") AS " + DBShopListTableConstants.TOTALREMAINING + ", " +

                //(shoplistdone * shoplistcost) AS spent
                "(" +
                DBShopListTableConstants.SHOPLIST_DONE_COL_FULL + " * " +
                DBProductusageTableConstants.PRODUCTUSAGE_COST_FULL +
                ") AS " + DBShopListTableConstants.TOTALSPENT +
                DBConstants.SQLFROM + DBShopListTableConstants.SHOPLIST_TABLE +

                DBConstants.SQLLEFTJOIN +
                DBAislesTableConstants.AISLES_TABLE +
                DBConstants.SQLON +
                DBShopListTableConstants.SHOPLIST_AISLEREF_COL_FULL + " = " +
                DBAislesTableConstants.AISLES_ID_COL_FULL +

                DBConstants.SQLLEFTJOIN +
                DBShopsTableConstants.SHOPS_TABLE +
                DBConstants.SQLON +
                DBAislesTableConstants.AISLES_SHOPREF_COL_FULL + " = " +
                DBShopsTableConstants.SHOPS_ID_COL_FULL +

                DBConstants.SQLJOIN +
                DBProductusageTableConstants.PRODUCTUSAGE_TABLE +
                DBConstants.SQLON +
                DBShopListTableConstants.SHOPLIST_AISLEREF_COL_FULL + " = " +
                DBProductusageTableConstants.PRODUCTUSAGE_AISLEREF_COL_FULL +
                DBConstants.SQLAND +
                DBShopListTableConstants.SHOPLIST_PRODUCTREF_COL_FULL + " = " +
                DBProductusageTableConstants.PRODUCTUSAGE_PRODUCTREF_COL_FULL;
        if (filter.length() > 0 ) {
            sql = sql + DBConstants.SQLWHERE + filter;
        }
        sql = sql + ")" + DBConstants.SQLENDSTATEMENT;
        Cursor totcsr = db.rawQuery(sql,null);
        if (totcsr.getCount() > 0 ) {
            totcsr.moveToFirst();
            rv.setTRPLDBL(
                    totcsr.getDouble(totcsr.getColumnIndex(DBShopListTableConstants.TOTALCOST)),
                    totcsr.getDouble(totcsr.getColumnIndex(DBShopListTableConstants.TOTALREMAINING)),
                    totcsr.getDouble(totcsr.getColumnIndex(DBShopListTableConstants.TOTALSPENT))
            );
        }
        msg = "Returning Totals as :-" +
                "\n\tTotal Cost=" +
                NumberFormat.getCurrencyInstance().format(rv.getdbl1()) +
                "\tRemaining Cost=" +
                NumberFormat.getCurrencyInstance().format(rv.getdbl2()) +
                "\tSpent=" +
                NumberFormat.getCurrencyInstance().format(rv.getdbl3());
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        totcsr.close();
        return rv;
    }

    /**************************************************************************
     *
     * @param filter    the where clause less WHERE
     * @param orderby   the order clause less ORDER BY
     * @return          the cursor with the shopping list entries
     */
    Cursor getShopListEntries(String filter, @SuppressWarnings("SameParameterValue") String orderby) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        Cursor rv =  dbdao.getTableRows(
                DBShopListTableConstants.SHOPLIST_TABLE,
                "", filter, orderby);
        msg = "Returning Cursor with " + Integer.toString(rv.getCount()) +
                " ShoppingList Entries";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        return rv;
    }

    /**************************************************************************
     *
     * @param filter    filter to be used "" otherwise
     * @return          the extracted cursor
     */
    Cursor getExpandedShopListEntries(String filter) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        Cursor rv;
        String sql = DBConstants.SQLSELECT +
                DBShopListTableConstants.SHOPLIST_ID_COL_FULL + ", " +
                DBShopListTableConstants.SHOPLIST_AISLEREF_COL_FULL + ", " +
                DBShopListTableConstants.SHOPLIST_PRODUCTREF_COL_FULL + ", " +
                DBShopListTableConstants.SHOPLIST_DATEADDED_COL_FULL + ", " +
                DBShopListTableConstants.SHOPLIST_NUMBERTOGET_COL_FULL + ", " +
                DBShopListTableConstants.SHOPLIST_DONE_COL_FULL + ", " +
                DBShopListTableConstants.SHOPLIST_DATEGOT_COL_FULL + ", " +
                DBShopListTableConstants.SHOPLIST_COST_COL_FULL + ", " +

                DBShopsTableConstants.SHOPS_NAME_COL_FULL + ", " +
                DBShopsTableConstants.SHOPS_CITY_COL_FULL + ", " +
                DBShopsTableConstants.SHOPS_ORDER_COL_FULL + ", " +
                DBShopsTableConstants.SHOPS_STREET_COL_FULL + ", " +
                DBShopsTableConstants.SHOPS_STATE_COL_FULL + ", " +
                DBShopsTableConstants.SHOPS_NOTES_COL_FULL + ", " +

                DBAislesTableConstants.AISLES_SHOPREF_COL_FULL + ", " +
                DBAislesTableConstants.AISLES_NAME_COL_FULL + ", " +
                DBAislesTableConstants.AISLES_ORDER_COL_FULL + ", " +

                DBProductsTableConstants.PRODUCTS_NAME_COL_FULL + ", " +
                DBProductsTableConstants.PRODUCTS_NOTES_COL_FULL + ", " +

                DBProductusageTableConstants.PRODUCTUSAGE_COST_FULL + ", " +
                DBProductusageTableConstants.PRODUCTUSAGE_BUYCOUNT_COL_FULL + ", " +
                DBProductusageTableConstants.PRODUCTUSAGE_FIRSTBUYDATE_COL_FULL + ", " +
                DBProductusageTableConstants.PRODUCTUSAGE_LATESTBUYDATE_COL_FULL + ", " +
                DBProductusageTableConstants.PRODUCTUSAGE_ORDER_COL_FULL + ", " +
                DBProductusageTableConstants.PRODUCTUSAGE_RULESUGGESTFLAG_COL_FULL + ", " +
                DBProductusageTableConstants.PRODUCTUSAGE_CHECKLISTFLAG_COL_FULL + ", " +
                DBProductusageTableConstants.PRODUCTUSAGE_CHECKLISTCOUNT_COL_FULL + ", " +

                DBShopListTableConstants.SHOPLIST_NUMBERTOGET_COL_FULL + " * " +
                DBProductusageTableConstants.PRODUCTUSAGE_COST_FULL + DBConstants.SQLAS +
                DBConstants.CALCULATED_TOTALCOST +

                DBConstants.SQLFROM + DBShopListTableConstants.SHOPLIST_TABLE +

                DBConstants.SQLLEFTJOIN + DBAislesTableConstants.AISLES_TABLE +
                DBConstants.SQLON +
                DBShopListTableConstants.SHOPLIST_AISLEREF_COL_FULL + " = " +
                DBAislesTableConstants.AISLES_ID_COL_FULL +

                DBConstants.SQLLEFTJOIN + DBProductsTableConstants.PRODUCTS_TABLE +
                DBConstants.SQLON +
                DBShopListTableConstants.SHOPLIST_PRODUCTREF_COL_FULL + " = " +
                DBProductsTableConstants.PRODUCTS_ID_COL_FULL +

                DBConstants.SQLLEFTJOIN +
                DBShopsTableConstants.SHOPS_TABLE +
                DBConstants.SQLON +
                DBAislesTableConstants.AISLES_SHOPREF_COL_FULL + " = " +
                DBShopsTableConstants.SHOPS_ID_COL_FULL +

                DBConstants.SQLJOIN +
                DBProductusageTableConstants.PRODUCTUSAGE_TABLE +
                DBConstants.SQLON +
                DBShopListTableConstants.SHOPLIST_AISLEREF_COL_FULL + " = " +
                DBProductusageTableConstants.PRODUCTUSAGE_AISLEREF_COL_FULL +
                DBConstants.SQLAND +
                DBShopListTableConstants.SHOPLIST_PRODUCTREF_COL_FULL + " = " +
                DBProductusageTableConstants.PRODUCTUSAGE_PRODUCTREF_COL_FULL;

        if (filter.length() > 0) {
            sql = sql + DBConstants.SQLWHERE + filter;
        }

        sql = sql + DBConstants.SQLORDERBY +
                DBShopsTableConstants.SHOPS_ORDER_COL_FULL + ", " +
                DBAislesTableConstants.AISLES_ORDER_COL_FULL + ", " +
                DBProductusageTableConstants.PRODUCTUSAGE_ORDER_COL_FULL
                + DBConstants.SQLENDSTATEMENT;

        rv = db.rawQuery(sql,null);
        msg = "returning Cursor with " + Integer.toString(rv.getCount()) +
                " ShoppingList Entries.";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        return rv;
    }

    /**************************************************************************
     *
     * @param aisleid               The id of the aisle.
     * @param productid             The id of the product.
     *                                  ailseid and productid together define
     *                                  a unique instance.
     * @param numbertoget           The number to get, positive increases
     *                                  the number to purchase, negative will
     *                                  decrease (see adjust done).
     * @param incrementifexists     If false then can only add a new entry
     *                                  but only if it will not be a duplicate.
     * @param adjustdone            Controls wether or not the done column
     *                                  is altered to relfect the number purchased.
     *                                  note the numbertoget is subtracted from 0
     *                                  so a negative value (as when buying) will
         *                              result in the DB numbertoget being reduced
     *                                  whilst done will be incremented.
     *                              Sholud only be true when invoking from the
     *                              shopping list.
     */
    void addOrUpdateShopListEntry(long aisleid,
                                  long productid,
                                  int numbertoget,
                                  @SuppressWarnings("SameParameterValue") boolean incrementifexists,
                                  boolean adjustdone) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        boolean exists = false;
        int newnumbertoget = 0;         // Number to purchase
        int newshoplistdone = 0;        // Number purchased
        ContentValues cv = new ContentValues();
        lastshoplistupdatedok = false;
        if (doesShopListEntryExist(aisleid, productid)) {
            exists = true;
        }
        // Shoplist Entry already exists so either increment the numbertoget or
        //  if the number to get passed is negative then increment the
        // shoplistdone (number purchased)
        if (exists && incrementifexists) {
            String whereargs[] = new String[] {
                    Long.toString(aisleid),
                    Long.toString(productid)
            };
            String whereclause = DBShopListTableConstants.SHOPLIST_AISLEREF_COL +
                    " = ? AND " +
                    DBShopListTableConstants.SHOPLIST_PRODUCTREF_COL +
                    " = ? ";
            Cursor csr = db.query(DBShopListTableConstants.SHOPLIST_TABLE,
                    null,
                    whereclause,
                    whereargs,
                    null,null,null,null);
            // If the Shoplist entry was found then update the ShopList entry
            // Should exist but just in case check
            boolean updatetodo = false;
            if (csr.getCount() > 0) {
                csr.moveToFirst();
                // Calculate the new number to get if positive and greater than 0
                //      i.e. need to increment the number to get
                if (numbertoget > 0) {
                    newnumbertoget = numbertoget +
                            csr.getInt(csr.getColumnIndex(
                                    DBShopListTableConstants.SHOPLIST_NUMBERTOGET_COL
                            ));
                    cv.put(DBShopListTableConstants.SHOPLIST_NUMBERTOGET_COL,newnumbertoget);
                    updatetodo = true;
                }
                if ((numbertoget < 0) && adjustdone) {
                    newshoplistdone = (0 - numbertoget) +
                            csr.getInt(csr.getColumnIndex(
                                    DBShopListTableConstants.SHOPLIST_DONE_COL
                            ));
                    cv.put(DBShopListTableConstants.SHOPLIST_DONE_COL,newshoplistdone);
                    updatetodo = true;
                }
                if ((numbertoget < 0) && !adjustdone) {
                    newnumbertoget = numbertoget +
                            csr.getInt(csr.getColumnIndex(
                                    DBShopListTableConstants.SHOPLIST_NUMBERTOGET_COL
                            ));
                    if (newnumbertoget >= 0) {
                        cv.put(DBShopListTableConstants.SHOPLIST_NUMBERTOGET_COL, newnumbertoget);
                        updatetodo = true;
                    }
                }
            }
            // Done with the cursor so close it
            csr.close();
            if (updatetodo) {
                lastshoplistupdatedok = db.update(
                        DBShopListTableConstants.SHOPLIST_TABLE,
                        cv, whereclause, whereargs) > 0;
                msg = "ShoppingList Entry AisleID=" + Long.toString(aisleid) +
                        " ProductID=" + Long.toString(productid) +
                        " Updated=" + Boolean.toString(lastshoplistupdatedok);
                LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL, LOGTAG, msg, THISCLASS, methodname);
            }
        }
        // If the Shoplist Entry does not exist then need to add a new entry
        // but only if the numbertoget is positive and greater than 0
        if ((!exists) && numbertoget > 0 ) {
            lastshoplistaddedok = false;
            cv.put(DBShopListTableConstants.SHOPLIST_AISLEREF_COL,aisleid);
            cv.put(DBShopListTableConstants.SHOPLIST_PRODUCTREF_COL,productid);
            cv.put(DBShopListTableConstants.SHOPLIST_DATEADDED_COL,
                    System.currentTimeMillis());
            cv.put(DBShopListTableConstants.SHOPLIST_DONE_COL,0);
            cv.put(DBShopListTableConstants.SHOPLIST_DATEGOT_COL,0);
            cv.put(DBShopListTableConstants.SHOPLIST_COST_COL,0);
            cv.put(DBShopListTableConstants.SHOPLIST_NUMBERTOGET_COL,numbertoget);
            lastshoplistadded.setTRPLLONG(
                    db.insert(DBShopListTableConstants.SHOPLIST_TABLE,null,cv),
                    aisleid, productid);
            if (lastshoplistadded.getLong1() > 0) {
                lastshoplistaddedok = true;
            }
            msg = "ShoppingList Entry AisleID=" + Long.toString(aisleid) +
                    " ProductID=" + Long.toString(productid) +
                    "Added=" + Boolean.toString(lastshoplistaddedok);
            LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        }
    }

    /**************************************************************************
     *
     * @param aisleid       The id of the aisle
     * @param productid     The id of the product
     * @param purchased     The new value for how many have been purchased
     */
    void adjustPurchased(long aisleid, long productid, int purchased) {
        String msg = "Invoked";
        String methodname =
                new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                LOGTAG,
                msg,
                THISCLASS,
                methodname
        );
        long shopref = new DBAisleMethods(context).getOwningShop(aisleid);
        String shopname = new DBShopMethods(context).getShopName(shopref);
        String aislename = new DBAisleMethods(context).getAisleName(aisleid);
        String productname = new DBProductMethods(context).getProductName(productid);
        String[] whereargs = new String[]{
                Long.toString(aisleid),
                Long.toString(productid)};
        String whereclause = DBShopListTableConstants.SHOPLIST_AISLEREF_COL +
                " = ? AND " +
                DBShopListTableConstants.SHOPLIST_PRODUCTREF_COL + " = ? ";
        ContentValues cv = new ContentValues();
        cv.put(DBShopListTableConstants.SHOPLIST_DONE_COL,Integer.toString(purchased));
        lastshoplistupdatedok = false;
        int updatecount = db.update(DBShopListTableConstants.SHOPLIST_TABLE,cv,whereclause,whereargs);
        if (updatecount > 0) {
            lastshoplistupdatedok = true;
        }
        String logmsg = "Update of shoplistdone (number purchased) for Shop" +
                shopname +
                " (ID=" +
                Long.toString(shopref) +
                ") Aisle " +
                aislename +
                " (ID=" +
                Long.toString(aisleid) +
                ") Product " +
                productname +
                " (ID=" + Long.toString(productid) +
                ") was " + Boolean.toString(lastshoplistupdatedok);
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                LOGTAG,
                logmsg,
                THISCLASS,
                methodname
        );
    }

    /**************************************************************************
     * shopListEntryDeleteImpact
     *      report on the impact of what rows would be deleted based upon
     *      a stcked item (productusage) being deleted.
     *      i.e. an aisle/product combination (which would be unique)
     *      Note! there may be 0-many ShopLIst entries (currently assuming
     *      that entries are not deleted when done).
     *
     * @param aisleid   id (long) of the aisle
     * @param productid id (long) of the product
     * @return          an arraylist of messages regarding the ShopList entries
     *                  that would be deleted.
     *
     */
    ArrayList<String> shopListEntryDeleteImpact(long aisleid, long productid) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);

        ArrayList<String> rv = new ArrayList<>();
        String sllfilter = DBShopListTableConstants.SHOPLIST_AISLEREF_COL_FULL +
                " = " + Long.toString(aisleid) +
                " AND " +
                DBProductsTableConstants.PRODUCTS_ID_COL_FULL +
                " = " + Long.toString(productid);
        Cursor sllcsr = getExpandedShopListEntries(sllfilter);
        if (sllcsr.getCount() > 0 ) {
            while (sllcsr.moveToNext()) {
                String aislename = sllcsr.getString(sllcsr.getColumnIndex(
                        DBAislesTableConstants.AISLES_NAME_COL
                ));
                String productname = sllcsr.getString(sllcsr.getColumnIndex(
                        DBProductsTableConstants.PRODUCTS_NAME_COL
                ));
                String shopname = sllcsr.getString(sllcsr.getColumnIndex(
                        DBShopsTableConstants.SHOPS_NAME_COL
                ));
                rv.add("ShoppingList entry for Product " + productname +
                        " in Aisle " + aislename +
                        ", Shop " + shopname +
                        " would be deleted."
                );
            }
        }
        return rv;
    }

    /**************************************************************************
     *
     * @param shoplistid id of the Shoplist entry to be deleted
     */
    void deleteShopListEntry(long shoplistid) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        int deletedcount = 0;
        if (doesShopListEntryExist(shoplistid)) {
            String whereargs[] = {Long.toString(shoplistid)};
            deletedcount = db.delete(
                    DBShopListTableConstants.SHOPLIST_TABLE,
                    DBShopListTableConstants.SHOPLIST_ID_COL + " = ?",
                    whereargs
            );
        }
        msg = "ShopList Entry for ShopID=" + Long.toString(shoplistid) +
                " Deleted=" + Boolean.toString(deletedcount > 0);
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
    }

    /**************************************************************************
     *
     * @param aisleid       id of the aisle
     * @param productid     id of the product
     */
    @SuppressWarnings("unused")
    void deleteShopListEntry(long aisleid, long productid) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        int deletedcount = 0;
        if (doesShopListEntryExist(aisleid, productid)) {
            String whereargs[] = new String[] {
                    Long.toString(aisleid),
                    Long.toString(productid)
            };
            String whereclause = DBShopListTableConstants.SHOPLIST_AISLEREF_COL +
                    " = ? AND " +
                    DBShopListTableConstants.SHOPLIST_PRODUCTREF_COL +
                    " = ? ";
            deletedcount = db.delete(
                    DBShopListTableConstants.SHOPLIST_TABLE,
                    whereclause,whereargs
            );
        }
        msg = "ShopList Entry for AisleID=" + Long.toString(aisleid) +
                " ProductID=" + Long.toString(productid) +
                " Deleted=" + Boolean.toString(deletedcount > 0);
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
    }
}
