package mjt.shopwise;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * DBSTorage Methods        Database methods specific to Storage the Table
 */

@SuppressWarnings({"WeakerAccess", "CanBeFinal"})
public class DBStorageMethods {

    private static final String LOGTAG = "SW_DBSTM";
    @SuppressWarnings("FieldCanBeLocal")
    private Context context;
    @SuppressWarnings("FieldCanBeLocal")
    private DBDAO dbdao;
    private static SQLiteDatabase db;
    private static long laststorageadded = -1;
    private static boolean laststorageaddedok = false;
    private static boolean laststorageupdatedok = false;
    public static final String THISCLASS = DBStorageMethods.class.getSimpleName();

    DBStorageMethods(Context ctxt) {
        String logmsg = "Constructing";
        String methodname = "Construct";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                LOGTAG,
                logmsg,
                THISCLASS,
                methodname
        );

        context = ctxt;
        dbdao = new DBDAO(context);
        db = dbdao.db;
    }

    /**************************************************************************
     *
     * @return  The number of rows in the Storage table
     */
    int getStorageCount() {
        return DBCommonMethods.getTableRowCount(db,
                DBStorageTableConstants.STORAGE_TABLE);
    }

    /**************************************************************************
     *
     * @return  The id of the last Storage added
     *              note only temporarily avaialable
     */
    @SuppressWarnings("unused")
    long getLastStorageAdded() {
        return laststorageadded;
    }

    /**************************************************************************
     *
     * @return  True if the last add was OK, else false
     */
    boolean ifStorageAdded() {
        return laststorageaddedok;
    }

    /**************************************************************************
     *
     * @return  True if the last update was OK, else false
     */
    boolean ifStorageUpdate() {
        return  laststorageupdatedok;
    }

    /**************************************************************************
     *
     * @return  Highest order used in the Storage Table
     */
    int getHighestStorageOrder() {
        String logmsg = "Invoked";
        String methodname =
                new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                LOGTAG,
                logmsg,
                THISCLASS,
                methodname
        );
        int rv = 0;
        String columns[] = {
                DBConstants.SQLMAX +
                        DBStorageTableConstants.STORAGE_ORDER_COL +
                        DBConstants.SQLMAXCLOSE +
                        DBConstants.SQLAS +
                        DBStorageTableConstants.STORAGEMAXORDERCOLUMN
        };
        Cursor csr = db.query(
                DBStorageTableConstants.STORAGE_TABLE,
                columns,
                null,null,null,null,null
        );
        if (csr.getCount() > 0) {
            csr.moveToFirst();
            rv = csr.getInt(csr.getColumnIndex(
                    DBStorageTableConstants.STORAGEMAXORDERCOLUMN
            ));
        }
        csr.close();
        logmsg = "Highest Storage Order=" +
                Integer.toString(rv);
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                LOGTAG,
                logmsg,
                THISCLASS,
                methodname
        );
        return rv;
    }

    /**************************************************************************
     *
     * @param filter    Filter clause excluding WHERE
     * @param order     Order clause excluding ORDER BY
     * @return          Cursor containing the selected Storage items
     */
    Cursor getStorage(String filter, String order) {
        String logmsg = "Invoked";
        String methodname =
                new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                LOGTAG,
                logmsg,
                THISCLASS,
                methodname
        );
        Cursor rv = DBCommonMethods.getTableRows(db,
                DBStorageTableConstants.STORAGE_TABLE,
                filter,order);
        logmsg = "Returning Storage Cursor with " +
                Integer.toString(rv.getCount()) + " rows.";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                LOGTAG,
                logmsg,
                THISCLASS,
                methodname
        );
        return rv;
    }

    /**************************************************************************
     *
     * @param storagename   name of the Storage Item
     * @param storageorder  order of the Storage Item
     */
    void insertStorage(String storagename, int storageorder) {
        String logmsg = "Invoked";
        String methodname =
                new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                LOGTAG,
                logmsg,
                THISCLASS,
                methodname
        );

        long addedid;
        laststorageaddedok = false;

        ContentValues cv = new ContentValues();
        cv.put(DBStorageTableConstants.STORAGE_NAME_COL,storagename);
        cv.put(DBStorageTableConstants.STORAGE_ORDER_COL,storageorder);
        addedid = db.insert(DBStorageTableConstants.STORAGE_TABLE,
                null,
                cv);
        if (addedid >= 0) {
            laststorageadded = addedid;
            laststorageaddedok = true;
        }
        logmsg = "Storage=" + storagename +
                " Order=" + Integer.toString(storageorder) +
                "Added=" + Boolean.toString(laststorageaddedok);
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                LOGTAG,
                logmsg,
                THISCLASS,
                methodname
        );
    }

    /**************************************************************************
     *
     * @param storageid id of the storage item to check
     * @return          true if the storage item exists, else false
     */
    boolean doesStorageExist(long storageid) {
        String logmsg = "Invoked";
        String methodname =
                new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                LOGTAG,
                logmsg,
                THISCLASS,
                methodname
        );
        boolean rv = false;
        String filter = DBStorageTableConstants.STORAGE_ID_COL_FULL +
                " = " + Long.toString(storageid) +
                DBConstants.SQLENDSTATEMENT;
        Cursor csr = DBCommonMethods.getTableRows(db,
                DBStorageTableConstants.STORAGE_TABLE,
                filter,
                ""
        );
        if (csr.getCount() > 0) {
            rv = true;
        }
        csr.close();
        logmsg = "StorageID=" + Long.toString(storageid) +
                "Exists=" + Boolean.toString(rv);
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                LOGTAG,
                logmsg,
                THISCLASS,
                methodname
        );
        return rv;
    }

    /**************************************************************************
     *
     * @param storageid     ID of the Storage item to update
     * @param storageorder  Order of the Storage Item to change 0 to not change
     * @param storagename   New name blank if not to change
     */
    void modifyStorage(long storageid, int storageorder, String storagename) {
        String logmsg = "Invoked";
        String methodname =
                new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                LOGTAG,
                logmsg,
                THISCLASS,
                methodname
        );
        laststorageupdatedok = false;
        if (!doesStorageExist(storageid)) {
            logmsg = "StorageID=" + Long.toString(storageid) +
                    " does not exist. Not Updated.";
            LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                    LOGTAG,
                    logmsg,
                    THISCLASS,
                    methodname
            );
            return;
        }
        int updatecount = 0;
        ContentValues cv = new ContentValues();
        if (storageorder > 0) {
            cv.put(DBStorageTableConstants.STORAGE_ORDER_COL,storageorder);
            updatecount++;
        }
        if (storagename.length() > 0) {
            cv.put(DBStorageTableConstants.STORAGE_NAME_COL,storagename);
            updatecount++;
        }
        if (updatecount < 1) {
            logmsg = "Nothing to Update. Not Updated";
            LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                    LOGTAG,
                    logmsg,
                    THISCLASS,
                    methodname
            );
            return;
        }
        String[] whereargs = {Long.toString(storageid)};
        String whereclause = DBStorageTableConstants.STORAGE_ID_COL + "= ?";
        laststorageupdatedok = db.update(DBStorageTableConstants.STORAGE_TABLE,
                cv,
                whereclause,
                whereargs) > 0;
        logmsg = "StorageID=" + Long.toString(storageid) +
                " Updated=" + Boolean.toString(laststorageupdatedok);
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                LOGTAG,
                logmsg,
                THISCLASS,
                methodname
        );
    }

    /**************************************************************************
     *
     * @param storageid     id of the storage item to delete
     * @return              should be 1 if deleted, 0 if not deleted,
     *                      -1 if products are referenced
     */
    @SuppressWarnings("UnusedReturnValue")
    public int deleteStorage(long storageid) {
        String logmsg = "Invoked";
        String methodname =
                new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                LOGTAG,
                logmsg,
                THISCLASS,
                methodname
        );
        int rv = 0;
        String whereclause =
                DBProductsTableConstants.PRODUCTS_STORAGEREF_COL_FULL +
                " = ?" ;
        String[] whereargs = new String[] { Long.toString(storageid)};
        Cursor csr = db.query(DBProductsTableConstants.PRODUCTS_TABLE,
                null,whereclause,whereargs,null,null,null);
        int referencedproducts = csr.getCount();
        csr.close();
        if (referencedproducts == 0) {
            whereclause = DBStorageTableConstants.STORAGE_ID_COL_FULL +
                    " = ?";
            rv = db.delete(DBStorageTableConstants.STORAGE_TABLE,
                    whereclause,whereargs);
        } else {
            rv = -1;
        }
        return rv;
    }

    /**************************************************************************
     *
     * @param storageid     id of the Storage Item being checked
     * @return              true if storeage item exists and is not referenced
     */
    public boolean isStorageEmpty(long storageid) {
        String logmsg = "Invoked";
        String methodname =
                new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                LOGTAG,
                logmsg,
                THISCLASS,
                methodname
        );
        boolean rv = false;
        String whereclause =
                DBProductsTableConstants.PRODUCTS_STORAGEREF_COL_FULL +
                " = ?";
        String[] whereargs = new String[]{Long.toString(storageid)};
        if (doesStorageExist(storageid)) {
            Cursor csr = db.query(DBProductsTableConstants.PRODUCTS_TABLE,
                    null,
                    whereclause,
                    whereargs,
                    null,null,null
            );
            if (csr.getCount() < 1) {
                rv = true;
            }
            csr.close();
        } else {
            logmsg = "Storage (ID=" + Long.toString(storageid) + ") does not exist.";
            LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                    LOGTAG,
                    logmsg,
                    THISCLASS,
                    methodname
            );
        }
        return rv;
    }
}
