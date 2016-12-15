package mjt.shopwise;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Date;

/**
 * DBProductUsageMethods - Database methods sepcific to ProductUsage handling
 * <p>
 * A ProductUsage represents a product within an aisle (location) so is
 * a shop specific representation a product. (This is a generalisation, as
 * it is possible to have a product in multiple aisles within a store).
 * <p>
 * Although a ProductUsage represents the link between product and store
 * (a many-many relationship) other values that are store/aisle specfifc
 * are held in this table. e.g. cost and order.
 */
class DBProductUsageMethods {
    private static final String LOGTAG = "DB-PUM";
    private Context context;
    private DBDAO dbdao;
    private static SQLiteDatabase db;
    private static long lastproductusageadded;
    private static boolean lastproductusageaddok = false;
    private static boolean lastproductusageduplicate = false;

    /**
     * Instantiates a new Db product usage methods.
     *
     * @param ctxt the ctxt
     */
    DBProductUsageMethods(Context ctxt) {
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
    long getLastproductusageadded() { return lastproductusageadded; }

    /**************************************************************************
     * @return true if last ProductUsage was add OK, else false
     */
    boolean ifProductUsageAdded() { return lastproductusageaddok; }

    /**
     * If product usage was duplicate boolean.
     *
     * @return true if attempt to insert productusage failed because the          ProductUsage (combination of AisleRef and ProductRef) already          existed, otherwisee false.
     */
    boolean ifProductUsageWasDuplicate() { return lastproductusageduplicate; }

    /**************************************************************************
     * @param aisleref   id of the Aisle
     * @param productref id of the product
     * @return true if found, else false
     */
    boolean doesProductUsageExist(long aisleref, long productref) {
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
        return rv;
    }

    /**************************************************************************
     * getProductUsages - get ProductUsages as a Cursor
     *
     * @param filter filter  clause, if required, less WHERE keyword
     * @param order  order clause, if required, LESS ORDER BY keywords
     * @return product usages
     */
    Cursor getProductUsages(String filter, String order) {
        return dbdao.getTableRows(DBProductusageTableConstants.PRODUCTUSAGE_TABLE,
                "",
                filter,
                order);
    }

    /**************************************************************************
     * insertProductUsage       Insert/Add a ProductUsage entry
     *
     * @param productref The id of the Product
     * @param aisleref   The id of the Aisle
     * @param cost       The cost to be associated with this                              ProductUsage
     * @param order      The order within the aisle                          NOTE!! combination of productref and aisleref                          has to be unqiue for the insertion to work.                          If not then lastproductusageduplicate will                          be set to true
     */
    void insertProductUsage(long productref,
                            long aisleref,
                            double cost,
                            int order) {
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
        db.update(DBProductusageTableConstants.PRODUCTUSAGE_TABLE,
                cv,
                whereclause,
                whereargs);
    }

    /**************************************************************************
     * setProductusageOrder     set the order of a specific ProductUsage
     *
     * @param productref product id referenced by the ProductUsage
     * @param aisleref   aisle id referenced by the ProductUsage
     * @param neworder   the new order to be used
     */
    void setProductUsageOrder(long productref, long aisleref, int neworder) {
        if (doesProductUsageExist(productref, aisleref)) {
            String whereargs[] = {Long.toString(productref), Long.toString(aisleref)};
            ContentValues cv = new ContentValues();
            cv.put(DBProductusageTableConstants.PRODUCTUSAGE_ORDER_COL,neworder);
            db.update(DBProductusageTableConstants.PRODUCTUSAGE_TABLE,
                    cv,
                    buildCVWhereClause(),
                    whereargs);
        }
    }

    /**************************************************************************
     * setRuleSuggestFlag   set the RuleSuggestFlag
     *
     * @param productref product id referenced by this productusage
     * @param aisleref   aisle id referenced by this productusage
     * @param flag       flag (0=clear, 1=to skip, 2=disable)
     */
    void setRuleSuggestFlag(long productref, long aisleref, int flag) {
        if (doesProductUsageExist(aisleref,productref)) {
            ContentValues cv = new ContentValues();
            cv.put(DBProductusageTableConstants.PRODUCTUSAGE_RULESUGGESTFLAG_COL, flag);
            String whereargs[] = {Long.toString(productref), Long.toString(aisleref)};
            db.update(DBProductusageTableConstants.PRODUCTUSAGE_TABLE,
                    cv,
                    buildCVWhereClause(),
                    whereargs);
        }
    }

    /**************************************************************************
     * enableSkipped    change ruleSggestFlags from skipped to clear
     */
    void enableSkipped() {
        String whereargs[] = {Integer.toString(DBProductusageTableConstants.RULESUGGESTFLAG_SKIP)};
        String whereclause = DBProductusageTableConstants.PRODUCTUSAGE_RULESUGGESTFLAG_COL +
                " = ?";
        ContentValues cv = new ContentValues();
        cv.put(DBProductusageTableConstants.PRODUCTUSAGE_RULESUGGESTFLAG_COL,
                DBProductusageTableConstants.RULESUGGESTFLAG_CLEAR);
        db.update(DBProductusageTableConstants.PRODUCTUSAGE_TABLE,
                cv,whereclause,
                whereargs);
    }

    /**************************************************************************
     * buildSingleProductUsageFilter    Build a filter string for a specific
     *                                  Productusage
     * @param productref    product id referenced by the ProductUsage
     * @param aislref       aisle id referenced by the ProductUsage
     * @return              Filter String
     */
    private String buildSingleProductUsageFilter(long productref, long aislref) {
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
        return DBProductusageTableConstants.PRODUCTUSAGE_PRODUCTREF_COL +
                " = ? " + DBConstants.SQLAND +
                DBProductusageTableConstants.PRODUCTUSAGE_AISLEREF_COL +
                " = ?";
    }
}
