package mjt.shopwise;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * DBAisleMethods - Databse Methods for Aisle handling
 */
class DBAisleMethods {

    private static final String LOGTAG = "SW_DBAM";
    private Context context;
    private DBDAO dbdao;
    private static SQLiteDatabase db;
    private static long lastaisleadded;
    private static boolean lastaisleaddok = false;
    private static boolean lastaisleupdateok = false;
    private DBShopMethods dbshopmethods;
    public static final String THISCLASS = DBAisleMethods.class.getSimpleName();

    private final String[] dummyrowcolumns = new String[] {
            DBAislesTableConstants.AISLES_ID_COL,
            DBAislesTableConstants.AISLES_SHOPREF_COL,
            DBAislesTableConstants.AISLES_ORDER_COL,
            DBAislesTableConstants.AISLES_NAME_COL
    };
    private final MatrixCursor dummyrow = new MatrixCursor(dummyrowcolumns);

    /**
     * Instantiates a new Db aisle methods.
     *
     * @param ctxt the ctxt
     */
    DBAisleMethods(Context ctxt) {
        String msg = "Constructing";
        String methodname = "Construct";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        context = ctxt;
        dbdao = new DBDAO(context);
        db = dbdao.db;
        dbshopmethods = new DBShopMethods(context);
    }

    /**************************************************************************
     * getAisleCount - get the number of aisles
     *
     * @return number of Aisles
     */
    int getAisleCount() {
        return DBCommonMethods.getTableRowCount(db,
                DBAislesTableConstants.AISLES_TABLE
        );
    }

    /**************************************************************************
     * getAisles - get Aisles as a cursor
     *
     * @param filter sql filter string less WHERE
     * @param order  sql order string less ORDER BY
     * @return cursor containing selected Aisles
     */
    Cursor getAisles(String filter, String order, boolean adddummyrowifempty) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        Cursor rv;
        rv =  DBCommonMethods.getTableRows(db,
                DBAislesTableConstants.AISLES_TABLE,
                filter,
                order
        );
        if (rv.getCount() == 0 && adddummyrowifempty) {
            ContentValues cv = new ContentValues();
            Cursor[] mergecursors = new Cursor[2];
            mergecursors[0] = rv;
            dummyrow.addRow(new String[] {"0","0","0","No Aisles"});
            mergecursors[1] = dummyrow;
            rv = new MergeCursor(mergecursors);
        }
        msg = "Returned " + Integer.toString(rv.getCount()) + " Aisle rows." ;
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        return rv;
    }

    long getOwningShop(long aisleid) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        long rv = 0;
        String filter = DBAislesTableConstants.AISLES_ID_COL_FULL +
                " = " + Long.toString(aisleid);
        String order = "";
        Cursor csr = DBCommonMethods.getTableRows(db,
                DBAislesTableConstants.AISLES_TABLE,
                filter,
                order);
        if (csr.getCount() > 0 ) {
            csr.moveToFirst();
            rv = csr.getLong(csr.getColumnIndex(
                    DBAislesTableConstants.AISLES_SHOPREF_COL
            ));
        }
        msg = "Found OwningShop=" +
                Boolean.toString(csr.getCount() > 0);
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        return rv;
    }

    /**************************************************************************
     * getAislesPerShop - Get the number of Aisles owned by a Shop
     *
     * @param shopid id of the shop
     * @return number of Aisles owned by the Shop
     */
    int getAislesPerShop(long shopid) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        int rv = 0;
        String filter = DBAislesTableConstants.AISLES_SHOPREF_COL_FULL +
                " = " + Long.toString(shopid);
        rv = DBCommonMethods.getTableRowCount(db,
                DBAislesTableConstants.AISLES_TABLE,
                filter, "");
        msg = "Found " + Integer.toString(rv) + " Aisles";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        return rv;
    }

    /**************************************************************************
     * getLastAisleAdded - return the is of the last Aisle added
     *
     * @return id of the Aisle that was last added
     */
    long getLastAisleAdded() {
        return lastaisleadded;
    }

    /**************************************************************************
     * ifAisleAdded - returns status of the last Aisle insert
     *
     * @return true if the last Aisle insert added the Aisle, else false
     */
    boolean ifAisleAdded() {
        return lastaisleaddok;
    }

    /**
     *  ifAsileUpdated
     * @return          the status of the last modifyAisle
     */
    boolean ifAisleUpdated() { return  lastaisleupdateok; }

    /**
     * getHighhesteAisleOrderPerShop
     * @param shopid    id of the shop
     * @return          the highest aisle number in the shop
     */
    int getHighestAisleOrderPerShop(long shopid) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        int rv = 0;
        String columns[] = {
                DBConstants.SQLMAX +
                        DBAislesTableConstants.AISLES_ORDER_COL +
                        DBConstants.SQLMAXCLOSE +
                        DBConstants.SQLAS +
                        DBAislesTableConstants.AISLESMAXORDERCOLUMN
        };
        String whereclause = DBAislesTableConstants.AISLES_SHOPREF_COL +
                " = ? ";
        String whereargs[] = new String[] {
                Long.toString(shopid)
        };
        Cursor csr = db.query(
                DBAislesTableConstants.AISLES_TABLE,
                columns,
                whereclause,
                whereargs,
                null,null,null
        );
        if (csr.getCount() > 0 ) {
            csr.moveToFirst();
            rv = csr.getInt(csr.getColumnIndex(
                    DBAislesTableConstants.AISLESMAXORDERCOLUMN
            ));
        }
        csr.close();
        msg = "Highest Aisle Order=" + Integer.toString(rv);
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        return rv;
    }

    /**************************************************************************
     * doesAisleExist
     *
     * @param aisleid id of the aisle to check
     * @return true if the Aisle exists esle false
     */
    boolean doesAisleExist(long aisleid) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        boolean rv = false;
        String filter = DBAislesTableConstants.AISLES_ID_COL_FULL +
                " = " + Long.toString(aisleid);
        Cursor csr = DBCommonMethods.getTableRows(db,
                DBAislesTableConstants.AISLES_TABLE,
                filter,
                "");
        if (csr.getCount() > 0) {
            rv = true;
        }
        csr.close();
        msg = "Aisle found=" + Boolean.toString(rv);
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        return rv;
    }

    /**************************************************************************
     * doesAisleExistInShop
     *
     * @param aisleid id of the aisle to check
     * @param shopid  id of the shop the Aisle should be owned by
     * @return true if the Aisle exists and is owned by the specified shop, else false
     */
    boolean doesAisleExistInShop(long aisleid, long shopid) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        boolean rv = false;
        if (dbshopmethods.doesShopExist(shopid)) {
            String filter = DBAislesTableConstants.AISLES_ID_COL_FULL +
                    " = " + Long.toString(aisleid) +
                    DBConstants.SQLAND +
                    DBAislesTableConstants.AISLES_SHOPREF_COL_FULL +
                    " = " + Long.toString(shopid);
            Cursor csr = DBCommonMethods.getTableRows(db,
                    DBAislesTableConstants.AISLES_TABLE,
                    filter,
                    "");
            if (csr.getCount() > 0) {
                rv = true;
            }
            csr.close();
        }
        msg = "Aisle in Shop=" + Boolean.toString(rv);
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        return rv;
    }

    /**************************************************************************
     * getAisleName get the name of the Aisle
     *
     * @param aisleid id of the Aisle
     * @return Name of the Aisle
     */
    String getAisleName(long aisleid) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        String rv = "NOTANAISLE";
        if (doesAisleExist(aisleid)) {
            String filter = DBAislesTableConstants.AISLES_ID_COL_FULL +
                    " = " +
                    Long.toString(aisleid);
            Cursor csr = DBCommonMethods.getTableRows(db,
                    DBAislesTableConstants.AISLES_TABLE,
                    filter,
                    "");
            if (csr.getCount() > 0) {
                rv = csr.getString(
                        csr.getColumnIndex(
                                DBAislesTableConstants.AISLES_NAME_COL
                        ));
            }
            csr.close();
        }
        msg = "Aisles Name=" + rv;
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        return rv;
    }

    /**************************************************************************
     * insertAisle - add a new Aisle
     *
     * @param aislename name of the aisle
     * @param aislorder order of the aisle (within shop)
     * @param shopref   reference to the parent shop
     */
    void insertAisle(String aislename, int aislorder, long shopref) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);

        long addedid;
        if (dbshopmethods.doesShopExist(shopref)) {
            ContentValues cv = new ContentValues();
            cv.put(DBAislesTableConstants.AISLES_NAME_COL, aislename);
            cv.put(DBAislesTableConstants.AISLES_ORDER_COL, aislorder);
            cv.put(DBAislesTableConstants.AISLES_SHOPREF_COL, shopref);
            addedid = db.insert(DBAislesTableConstants.AISLES_TABLE,
                    null,
                    cv);
            if (addedid > -1) {
                lastaisleadded = addedid;
                lastaisleaddok = true;

            }
        } else {
            lastaisleaddok = false;
        }
        msg = "Aisle=" + aislename + " Inserted=" + Boolean.toString(lastaisleaddok);
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
    }


    /**************************************************************************
     * @param aisleid    ID of the Aisle to modify
     * @param aisleorder new Aisle Order value (0 skips)
     * @param aislename  new Aisle Name value (blank skips)
     */
    void modifyAisle(long aisleid, int aisleorder, String aislename) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        if (!doesAisleExist(aisleid)) {
            msg = "Aisle=" + aislename + " ID=" + Long.toString(aisleid) +
                    " does not exist. Not Updated.";
            LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
            return;
        }
        int updatecount = 0;
        ContentValues cv = new ContentValues();
        if (aisleorder > 0) {
            cv.put(DBAislesTableConstants.AISLES_ORDER_COL, aisleorder);
            updatecount++;
        }
        if (aislename.length() > 0) {
            cv.put(DBAislesTableConstants.AISLES_NAME_COL, aislename);
            updatecount++;
        }
        if (updatecount < 1) {
            msg = "Aisle=" + aislename + " ID=" + Long.toString(aisleid) +
                    " Nothing to update. Not Updated.";
            LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
            return;
        }
        String whereargs[] = {Long.toString(aisleid)};
        String whereclause = DBAislesTableConstants.AISLES_ID_COL + " = ?";
        lastaisleupdateok = db.update(DBAislesTableConstants.AISLES_TABLE, cv, whereclause, whereargs) > 0;
        msg = "Aisle=" + aislename + " ID=" + Long.toString(aisleid) +
                " Updated=" + Boolean.toString(lastaisleupdateok);
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
    }

    /**************************************************************************
     * deleteAisle - Delete an Aisle and any children the aisle has
     * children could be :- productusage rows
     * shoplist rows
     * rule rows
     *
     * @param aisleid       id of the aisle to be deleted
     * @param intransaction true if already in a transaction
     */
    void deleteAisle(long aisleid, boolean intransaction) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);

        String sql;
        int pudeletes = 0;
        int sldeletes = 0;
        int rdeletes = 0;
        int adelete = 0;

        if (doesAisleExist(aisleid)) {
            if (!intransaction) {
                db.beginTransaction();
                msg = "Not in another DB Transacion so Transaction Started";
                LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
            }

            /**
             *  Set whereargs string array to the aisleid as a string
             */
            String whereargs[] = {Long.toString(aisleid)};
            /**
             * Delete ProductUsage rows that have Aisle as a parent
             */
            pudeletes = db.delete(
                    DBProductusageTableConstants.PRODUCTUSAGE_TABLE,
                    DBProductusageTableConstants.PRODUCTUSAGE_AISLEREF_COL +
                            " = ?",
                    whereargs
            );
            msg = "Deleted " + Integer.toString(pudeletes) +
                    " ProductUsage Rows that reference Aisle ID=" + aisleid;
            LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
            /**
             * Delete Shopping List rows with Aisle as a parent
             */
            sldeletes = db.delete(
                    DBShopListTableConstants.SHOPLIST_TABLE,
                    DBShopListTableConstants.SHOPLIST_AISLEREF_COL +
                            " = ?",
                    whereargs
            );
            msg = "Deleted " + Integer.toString(pudeletes) +
                    " ShoppingList Rows that reference Aisle ID=" + aisleid;
            LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
            /**
             * Delete Rule rows with Aisle as a parent
             */
            rdeletes = db.delete(
                    DBRulesTableConstants.RULES_TABLE,
                    DBRulesTableConstants.RULES_AISLEREF_COL +
                            " = ?"
                    ,
                    whereargs);
            msg = "Deleted " + Integer.toString(pudeletes) +
                    " Rule Rows that reference Aisle ID=" + aisleid;
            LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
            /**
             * Finally Delete the Aisle itself
             */
            adelete = db.delete(
                    DBAislesTableConstants.AISLES_TABLE,
                    DBAislesTableConstants.AISLES_ID_COL + " = ?",
                    whereargs);
            msg = "Deleted " + Integer.toString(pudeletes) +
                    " Aisle Rows with Aisle ID=" + aisleid;
            LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
            if (!intransaction) {
                db.setTransactionSuccessful();
                db.endTransaction();
                msg = "DB Transacion SET and ENDED for Aisle ID=" + Long.toString(aisleid);
                LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
            }
        }
    }

    /**************************************************************************
     * aisleDeleteImpact
     *
     * @param aisleid The id of the Aisle being checked
     * @return A String ArrayList of the impacts impacts being the Aisle thar would be deleted, any productusages that would be deleted as they reference the Aisle, any Shoplist entries that reference the Aisle and any Rules that reference the Aisle.
     */
    ArrayList<String> aisleDeleteImpact(long aisleid) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        ArrayList<String> rv = new ArrayList<>();
        Cursor aislecsr;

        aislecsr = db.query(DBAislesTableConstants.AISLES_TABLE,
                null,
                DBAislesTableConstants.AISLES_ID_COL_FULL + "=?",
                new String[]{Long.toString(aisleid)},
                null,null,null);
        if (aislecsr.getCount() > 0) {
            Cursor pucsr, slcsr, rulecsr;
            while (aislecsr.moveToNext()) {
                String aislename = aislecsr.getString(
                        aislecsr.getColumnIndex(
                                DBAislesTableConstants.AISLES_NAME_COL
                        )
                );
                rv.add("AISLE - " +
                        aislename +
                        " would be deleted."
                );

                pucsr = db.query(DBProductusageTableConstants.PRODUCTUSAGE_TABLE +
                        DBConstants.SQLLEFTJOIN +
                        DBProductsTableConstants.PRODUCTS_TABLE +
                        DBConstants.SQLON +
                        DBProductusageTableConstants.PRODUCTUSAGE_PRODUCTREF_COL_FULL + " = " +
                        DBProductsTableConstants.PRODUCTS_ID_COL_FULL,
                        null,
                        DBProductusageTableConstants.PRODUCTUSAGE_AISLEREF_COL_FULL + " =?",
                        new String[] {Long.toString(aisleid)},
                        null,null,null);

                while (pucsr.moveToNext()) {
                    rv.add("TO GET for product -" +
                            pucsr.getString(
                                    pucsr.getColumnIndex(
                                            DBProductsTableConstants.PRODUCTS_NAME_COL
                                    )) +
                            " from Aisle " +
                            aislename +
                            " will be removed."
                    );
                }
                pucsr.close();

                slcsr = db.query(DBShopListTableConstants.SHOPLIST_TABLE +
                        DBConstants.SQLLEFTJOIN +
                        DBProductsTableConstants.PRODUCTS_TABLE +
                        DBConstants.SQLON +
                        DBShopListTableConstants.SHOPLIST_PRODUCTREF_COL_FULL + " = " +
                        DBProductsTableConstants.PRODUCTS_ID_COL_FULL,
                        null,DBShopListTableConstants.SHOPLIST_AISLEREF_COL_FULL + " =?",new String[] {Long.toString(aisleid)},
                        null,null,null);
                while (slcsr.moveToNext()) {
                    rv.add(
                            "ShopList row for product " +
                                    slcsr.getString(
                                            slcsr.getColumnIndex(
                                                    DBProductsTableConstants.PRODUCTS_NAME_COL)) +
                                    " will be removed."
                    );
                }
                slcsr.close();

                rulecsr = db.query(DBRulesTableConstants.RULES_TABLE +
                        DBConstants.SQLLEFTJOIN +
                        DBProductsTableConstants.PRODUCTS_TABLE +
                        DBConstants.SQLON +
                        DBRulesTableConstants.RULES_PRODUCTREF_COL_FULL + " = " +
                        DBProductsTableConstants.PRODUCTS_ID_COL_FULL,
                        null,
                        DBRulesTableConstants.RULES_AISLEREF_COL_FULL + " =?",
                        new String[]{ Long.toString(aisleid)},
                        null,null,null);
                while (rulecsr.moveToNext()) {
                    rv.add(
                            "Rule - " +
                                    rulecsr.getString(
                                            rulecsr.getColumnIndex(
                                                    DBRulesTableConstants.RULES_NAME_COL
                                            )
                                    ) +
                                    " will be removed."
                    );
                }
                rulecsr.close();
            }
        }
        aislecsr.close();
        return rv;
    }
}
