package mjt.shopwise;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * DBAisleMethods - Databse Methods specific to Aisle handling
 */
public class DBAisleMethods {

    private static final String LOGTAG = "DB-AM";
    private Context context;
    private DBDAO dbdao;
    private static SQLiteDatabase db;
    private static long lastaisleadded;
    private static boolean lastaisleaddok = false;
    private DBShopMethods dbshopmethods;

    DBAisleMethods(Context ctxt) {
        context = ctxt;
        dbdao = new DBDAO(context);
        db = dbdao.db;
        dbshopmethods = new DBShopMethods(context);
    }

    /**************************************************************************
     * getAisleCount - get the number of aisles
     * @return          number of Aisles
     */
    int getAisleCount() {
        String sql = "SELECT * FROM " +
                DBAislesTableConstants.AISLES_TABLE +
                "; ";
        Cursor csr = db.rawQuery(sql,null);
        int rv = csr.getCount();
        csr.close();
        return rv;
    }

    /**************************************************************************
     * getAislesPerShop - Get the number of Aisles owned by a Shop
     * @param shopid    id of the shop
     * @return          number of Aisles owned by the Shop
     */
    int getAislesPerShop(long shopid) {
        String sql = "SELECT * FROM " +
                DBAislesTableConstants.AISLES_TABLE +
                " WHERE " +
                    DBAislesTableConstants.AISLES_SHOPREF_COL_FULL +
                    " = " + Long.toString(shopid);
        Cursor csr = db.rawQuery(sql,null);
        int rv = csr.getCount();
        csr.close();
        return rv;
    }

    /**************************************************************************
     * getLastAisleAdded - return the is of the last Aisle added
     * @return              id of the Aisle that was last added
     */
    long getLastAisleAdded() {
        return lastaisleadded;
    }

    /**************************************************************************
     * ifAisleAdded - returns status of the last Aisle insert
     * @return              true if the last Aisle insert added the Aisle,
     *                      else false
     */
    boolean ifAisleAdded() {
        return lastaisleaddok;
    }

    /**************************************************************************
     * getAisles - get Aisles as a cursor
     * @param filter    sql filter string less WHERE
     * @param order     sql order string less ORDER BY
     * @return cursor containing selected Aisles
     */
    Cursor getAisles(String filter, String order) {
        String sql = "SELECT * FROM " +
                DBAislesTableConstants.AISLES_TABLE;
        if( filter.length() > 0) {
            sql = sql + " WHERE " + filter;
        }
        if (order.length() > 0 ) {
            sql = sql + " ORDER BY " + order;
        }
        sql = sql + "; ";
        return db.rawQuery(sql,null);
    }

    /**************************************************************************
     * insertAisle - add a new Aisle
     * @param aislename     name of the aisle
     * @param aislorder     order of the aisle (within shop)
     * @param shopref       reference to the parent shop
     */
    void insertAisle(String aislename, int aislorder, long shopref) {

        long addedid;
        if (dbshopmethods.doesShopExist(shopref)) {
            ContentValues cv = new ContentValues();
            cv.put(DBAislesTableConstants.AISLES_NAME_COL,aislename);
            cv.put(DBAislesTableConstants.AISLES_ORDER_COL,aislorder);
            cv.put(DBAislesTableConstants.AISLES_SHOPREF_COL,shopref);
            addedid = db.insert(DBAislesTableConstants.AISLES_TABLE,
                    null,
                    cv);
            if(addedid > -1) {
                lastaisleadded = addedid;
            }
        } else {
            lastaisleaddok = false;
        }
    }

    /**************************************************************************
     * doesAisleExist
     * @param aisleid   id of the aisle to check
     * @return  true if the Aisle exists esle false
     */
    boolean doesAisleExist(long aisleid) {
        boolean rv = false;
        String sql = " SELECT " +
                    DBAislesTableConstants.AISLES_ID_COL_FULL +
                " FROM " + DBAislesTableConstants.AISLES_TABLE +
                " WHERE " + DBAislesTableConstants.AISLES_ID_COL_FULL +
                    " = " + Long.toString(aisleid) +
                " ;";
        Cursor csr = db.rawQuery(sql,null);
        if (csr.getCount() > 0 ) {
            rv = true;
        }
        csr.close();
        return rv;
    }

    /**************************************************************************
     * doesAisleExistInShop
     * @param aisleid   id of the aisle to check
     * @param shopid    id of the shop the Aisle should be owned by
     * @return          true if the Aisle exists and is owned by the
     *                  specified shop, else false
     */
    boolean doesAisleExistInShop(long aisleid, long shopid) {
        boolean rv = false;
        if (dbshopmethods.doesShopExist(shopid)) {
            String sql = " SELECT " +
                        DBAislesTableConstants.AISLES_ID_COL_FULL +
                    " FROM " +
                        DBAislesTableConstants.AISLES_TABLE +
                    " WHERE " +
                        DBAislesTableConstants.AISLES_ID_COL_FULL +
                        " = " + Long.toString(aisleid) +
                    " AND " +
                        DBAislesTableConstants.AISLES_SHOPREF_COL_FULL +
                        " = " + Long.toString(shopid) +
                    " ;";
            Cursor csr = db.rawQuery(sql,null);
            if (csr.getCount() > 0) {
                rv = true;
            }
            csr.close();
        }
        return rv;
    }
}
