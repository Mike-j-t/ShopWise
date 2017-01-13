package mjt.shopwise;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

/**
 * DBAisleMethods - Databse Methods specific to Aisle handling
 */
class DBAisleMethods {

    private static final String LOGTAG = "DB-AM";
    private Context context;
    private DBDAO dbdao;
    private static SQLiteDatabase db;
    private static long lastaisleadded;
    private static boolean lastaisleaddok = false;
    private static boolean lastaisleupdateok = false;
    private DBShopMethods dbshopmethods;

    /**
     * Instantiates a new Db aisle methods.
     *
     * @param ctxt the ctxt
     */
    DBAisleMethods(Context ctxt) {
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
    Cursor getAisles(String filter, String order) {
        return DBCommonMethods.getTableRows(db,
                DBAislesTableConstants.AISLES_TABLE,
                filter,
                order
        );
    }

    long getOwningShop(long aisleid) {
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
        return rv;
    }

    /**************************************************************************
     * getAislesPerShop - Get the number of Aisles owned by a Shop
     *
     * @param shopid id of the shop
     * @return number of Aisles owned by the Shop
     */
    int getAislesPerShop(long shopid) {
        String filter = DBAislesTableConstants.AISLES_SHOPREF_COL_FULL +
                " = " + Long.toString(shopid);
        return DBCommonMethods.getTableRowCount(db,
                DBAislesTableConstants.AISLES_TABLE,
                filter, "");
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
        return rv;
    }

    /**************************************************************************
     * doesAisleExist
     *
     * @param aisleid id of the aisle to check
     * @return true if the Aisle exists esle false
     */
    boolean doesAisleExist(long aisleid) {
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
        return rv;
    }

    /**************************************************************************
     * getAisleName get the name of the Aisle
     *
     * @param aisleid id of the Aisle
     * @return Name of the Aisle
     */
    String getAisleName(long aisleid) {
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
    }


    /**************************************************************************
     * @param aisleid    ID of the Aisle to modify
     * @param aisleorder new Aisle Order value (0 skips)
     * @param aislename  new Aisle Name value (blank skips)
     */
    void modifyAisle(long aisleid, int aisleorder, String aislename) {
        if (!doesAisleExist(aisleid)) {
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
            return;
        }
        String whereargs[] = {Long.toString(aisleid)};
        String whereclause = DBAislesTableConstants.AISLES_ID_COL + " = ?";
        lastaisleupdateok = false;
        if (db.update(DBAislesTableConstants.AISLES_TABLE, cv, whereclause, whereargs) > 0) {
            lastaisleupdateok = true;
        }
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

        String sql;
        int pudeletes = 0;
        int sldeletes = 0;
        int rdeletes = 0;
        int adelete = 0;

        if (doesAisleExist(aisleid)) {
            if (!intransaction) {
                db.beginTransaction();
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
            /**
             * Delete Shopping List rows with Aisle as a parent
             */
            sldeletes = db.delete(
                    DBShopListTableConstants.SHOPLIST_TABLE,
                    DBShopListTableConstants.SHOPLIST_AISLEREF_COL +
                            " = ?",
                    whereargs
            );
            /**
             * Delete Rule rows with Aisle as a parent
             */
            rdeletes = db.delete(
                    DBRulesTableConstants.RULES_TABLE,
                    DBRulesTableConstants.RULES_AISLEREF_COL +
                            " = ?"
                    ,
                    whereargs);
            /**
             * Finally Delete the Aisle itself
             */
            adelete = db.delete(
                    DBAislesTableConstants.AISLES_TABLE,
                    DBAislesTableConstants.AISLES_ID_COL + " = ?",
                    whereargs);
            if (!intransaction) {
                db.setTransactionSuccessful();
                db.endTransaction();
            }

            Log.i(LOGTAG,
                    "\tProductUsage Rows deleted = " +
                            Integer.toString(pudeletes) +
                            "\n\tShopList Rows deleted = " +
                            Integer.toString(sldeletes) +
                            "\n\tRule Rows deleted = " +
                            Integer.toString(rdeletes) +
                            "\n\tAisle Rows deleted = " +
                            Integer.toString(adelete)
            );
        }
    }

    /**************************************************************************
     * aisleDeleteImpact
     *
     * @param aisleid The id of the Aisle being checked
     * @return A String ArrayList of the impacts impacts being the Aisle thar would be deleted, any productusages that would be deleted as they reference the Aisle, any Shoplist entries that reference the Aisle and any Rules that reference the Aisle.
     */
    ArrayList<String> aisleDeleteImpact(long aisleid) {
        ArrayList<String> rv = new ArrayList<>();

        String aislesql = DBConstants.SQLSELECTALLFROM +
                DBAislesTableConstants.AISLES_TABLE +
                DBConstants.SQLWHERE +
                DBAislesTableConstants.AISLES_ID_COL_FULL +
                " = " +
                Long.toString(aisleid) +
                DBConstants.SQLENDSTATEMENT;
        Cursor aislecsr = db.rawQuery(aislesql, null);
        if (aislecsr.getCount() > 0) {
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

                String pusql = DBConstants.SQLSELECTALLFROM +
                        DBProductusageTableConstants.PRODUCTUSAGE_TABLE +
                        " LEFT JOIN " +
                        DBProductsTableConstants.PRODUCTS_TABLE +
                        " ON " +
                        DBProductsTableConstants.PRODUCTS_ID_COL_FULL +
                        " = " +
                        DBProductusageTableConstants.PRODUCTUSAGE_PRODUCTREF_COL_FULL +
                        DBConstants.SQLWHERE +
                        DBProductusageTableConstants.PRODUCTUSAGE_AISLEREF_COL_FULL +
                        " = " +
                        Long.toString(aisleid) +
                        DBConstants.SQLENDSTATEMENT;

                Cursor pucsr = db.rawQuery(pusql, null);
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

                String slsql = DBConstants.SQLSELECTALLFROM +
                        DBShopListTableConstants.SHOPLIST_TABLE +
                        " LEFT JOIN " + DBProductsTableConstants.PRODUCTS_TABLE +
                        " ON " +
                        DBProductsTableConstants.PRODUCTS_ID_COL_FULL +
                        " = " +
                        DBShopListTableConstants.SHOPLIST_PRODUCTREF_COL_FULL +
                        DBConstants.SQLWHERE +
                        DBShopListTableConstants.SHOPLIST_AISLEREF_COL_FULL +
                        " = " +
                        Long.toString(aisleid) +
                        DBConstants.SQLENDSTATEMENT;
                Cursor slcsr = db.rawQuery(slsql, null);
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

                String rulesql = DBConstants.SQLSELECTALLFROM +
                        DBRulesTableConstants.RULES_TABLE +
                        " LEFT JOIN " + DBProductsTableConstants.PRODUCTS_TABLE +
                        " ON " +
                        DBProductsTableConstants.PRODUCTS_ID_COL_FULL +
                        " = " +
                        DBRulesTableConstants.RULES_PRODUCTREF_COL_FULL +
                        DBConstants.SQLWHERE +
                        DBRulesTableConstants.RULES_AISLEREF_COL_FULL +
                        " = " +
                        Long.toString(aisleid) +
                        DBConstants.SQLENDSTATEMENT;
                Cursor rulecsr = db.rawQuery(rulesql, null);
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
