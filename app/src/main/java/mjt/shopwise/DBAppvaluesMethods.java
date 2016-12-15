package mjt.shopwise;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * DBAppvaluesMethods - Database methods for the Appvalues Table handling
 * AppValues table is for holding values in a similar viewn to shared
 * preferences.
 * An entry is basically a key/value pair. Only types of String, int, long
 * double and float are catared for.
 * Multiple values for the same key (AppvalueName) can be specified by
 * setting the allowmultiple parameter, of the insert method, to true.
 * If allowmultiple has been used and duplicate key names (AppvalueNames),
 * then the values should be retrieved into an ArrayList via the
 * get????Appvalues(AppvaleName)
 * (i.e. plural as opposed to singular) methods. Using the singular
 * get????Appvalue(AppvalueNam, defaultvalue)
 * methods to retrieve values from duplicated names will result in only
 * 1 value being returned.
 */
class DBAppvaluesMethods {

    /**
     * The constant APPVALUE_RC_OK.
     */
    public static final int APPVALUE_RC_OK = 0;
    /**
     * The constant APPVALUE_RC_DUPLICATEDNAME.
     */
    public static final int APPVALUE_RC_DUPLICATEDNAME = -2;
    /**
     * The constant APPVALUE_RC_DUPLICATEDNAMEANDVAL.
     */
    public static final int APPVALUE_RC_DUPLICATEDNAMEANDVAL = -3;

    /**
     * The constant APPVALUE_INCLUDEINSETTINGS.
     */
    public static final int APPVALUE_INCLUDEINSETTINGS = 1;
    /**
     * The constant APPVALUE_DONOTINCLUDEINSETTINGS.
     */
    public static final int APPVALUE_DONOTINCLUDEINSETTINGS = 0;

    private static final String LOGTAG = "DB-AVM";
    private Context context;
    private DBDAO dbdao;
    private static SQLiteDatabase db;
    private static long lastappvalueadded;
    private static boolean lastappvalueaddok = false;
    private static int lastappvaleaddrc = APPVALUE_RC_OK;


    /**
     * Instantiates a new Db appvalues methods.
     *
     * @param ctxt the ctxt
     */
    DBAppvaluesMethods(Context ctxt) {
        context = ctxt;
        dbdao = new DBDAO(context);
        db = dbdao.db;
    }

    /**************************************************************************
     * getAppvalueCount - return the number of rows in the Appvalues table
     *
     * @return appvalue count
     */
    int getAppvalueCount() {
        String sql = "SELECT * FROM " +
                DBAppvaluesTableConstants.APPVALUES_TABLE +
                " ;";
        Cursor csr = db.rawQuery(sql,null);
        int rv = csr.getCount();
        csr.close();
        return rv;
    }

    /**
     * Gets appvalues.
     *
     * @param filter the filter
     * @param order  the order
     * @return the appvalues
     */
    Cursor getAppvalues(String filter, String order) {
        return dbdao.getTableRows(
                DBAppvaluesTableConstants.APPVALUES_TABLE,
                "",
                filter,
                order
        );
    }

    /**************************************************************************
     * getLastAppvalueAdded - return the last Appvalue row id that was added
     *
     * @return the id of the last Appvalue that was added
     */
    long getLastAppvalueAdded() {
        return lastappvalueadded;
    }

    /**************************************************************************
     * ifAppvalueAddedOK - true if the last attempt to add an Appvalue row
     * was OK, otherwise false
     *
     * @return true if added ok else false
     */
    boolean ifAppvalueAddedOK() {
        return lastappvalueaddok;
    }

    /**************************************************************************
     * doesAppValueExist - check to see if an Appvalue id exists
     *
     * @param appvalueid id of the Appvalue to check
     * @return true if it does exist, otherwise false
     */
    boolean doesAppValueExist(long appvalueid) {
        boolean rv = false;
        String sql = "SELECT " +
                    DBAppvaluesTableConstants.APPVALUES_ID_COL_FULL +
                " FROM " +
                    DBAppvaluesTableConstants.APPVALUES_TABLE +
                " WHERE " +
                    DBAppvaluesTableConstants.APPVALUES_ID_COL_FULL +
                    " = " +
                    Long.toString(appvalueid) +
                " ;";
        Cursor csr = db.rawQuery(sql,null);
        if (csr.getCount() > 0 ) {
            rv = true;
        }
        csr.close();
        return rv;
    }


    /**************************************************************************
     * insertAppvalue - add an App value entry of SQL type TEXT
     *
     * @param appvaluename      Name of the Appvalue
     * @param appvalue          value to be held
     * @param allowmultiple     true if duplicate key name is allowed
     * @param includeinsettings true, if can be included in settings
     * @param settingsinfo      Text to be displayed along with the setting
     */
    void insertAppvalue(String appvaluename,
                        String appvalue,
                        boolean allowmultiple,
                        boolean includeinsettings,
                        String settingsinfo) {

        lastappvaleaddrc = APPVALUE_RC_OK;

        /**
         * If non-unique name not allowed then reject duplicated names
         */
        if (ifAppvalueDuplicated(appvaluename, appvalue, allowmultiple)) {
            return;
        }

        int sincl = APPVALUE_DONOTINCLUDEINSETTINGS;
        if (includeinsettings) {
            sincl = APPVALUE_INCLUDEINSETTINGS;
        }
        ContentValues cv = new ContentValues();
        cv.put(DBAppvaluesTableConstants.APPVALUES_NAME_COL,appvaluename);
        cv.put(DBAppvaluesTableConstants.APPVALUES_TYPE_COL,DBConstants.TXT);
        cv.put(DBAppvaluesTableConstants.APPVALUES_TEXT_COL,appvalue);
        cv.put(DBAppvaluesTableConstants.APPVALUES_INT_COL,0);
        cv.put(DBAppvaluesTableConstants.APPVALUES_REAL_COL,0.0);
        cv.put(DBAppvaluesTableConstants.APPVALUES_INCLUDEINSETTINGS_COL,sincl);
        cv.put(DBAppvaluesTableConstants.APPVALUES_SETTINGSINFO_COL,settingsinfo);
        db.insert(DBAppvaluesTableConstants.APPVALUES_TABLE,null,cv);
    }

    /**************************************************************************
     * insertAppvalue - add an App value entry of SQL type INTEGER
     *
     * @param appvaluename      Name of the Appvalue
     * @param appvalue          value to be held
     * @param allowmultiple     true if duplicate key name is allowed
     * @param includeinsettings true, if can be included in settings
     * @param settingsinfo      Text to be displayed along with the setting
     */
    void insertAppvalue(String appvaluename,
                        long appvalue,
                        boolean allowmultiple,
                        boolean includeinsettings,
                        String settingsinfo) {

        lastappvaleaddrc = APPVALUE_RC_OK;

        /**
         * If non-unique name not allowed then reject duplicated names
         */
        if (ifAppvalueDuplicated(appvaluename, appvalue, allowmultiple)) {
            return;
        }

        int sincl = APPVALUE_DONOTINCLUDEINSETTINGS;
        if (includeinsettings) {
            sincl = APPVALUE_INCLUDEINSETTINGS;
        }
        ContentValues cv = new ContentValues();
        cv.put(DBAppvaluesTableConstants.APPVALUES_NAME_COL,appvaluename);
        cv.put(DBAppvaluesTableConstants.APPVALUES_TYPE_COL,DBConstants.INT);
        cv.put(DBAppvaluesTableConstants.APPVALUES_TEXT_COL,"");
        cv.put(DBAppvaluesTableConstants.APPVALUES_INT_COL,appvalue);
        cv.put(DBAppvaluesTableConstants.APPVALUES_REAL_COL,0.0);
        cv.put(DBAppvaluesTableConstants.APPVALUES_INCLUDEINSETTINGS_COL,sincl);
        cv.put(DBAppvaluesTableConstants.APPVALUES_SETTINGSINFO_COL,settingsinfo);
        db.insert(DBAppvaluesTableConstants.APPVALUES_TABLE,null,cv);
    }

    /**************************************************************************
     * insertAppvalue - add an App value entry of SQL type INTEGER from int
     * i.e converts int to long and calls insertAppvale
     * with long.
     *
     * @param appvaluename      Name of the Appvalue
     * @param appvalue          value to be held
     * @param allowmultiple     true if duplicate key name is allowed
     * @param includeinsettings true, if can be included in settings
     * @param settingsinfo      Text to be displayed along with the setting
     */
    void insertAppvalue(String appvaluename,
                        int appvalue,
                        boolean allowmultiple,
                        boolean includeinsettings,
                        String settingsinfo) {
        insertAppvalue(appvaluename,
                (long) appvalue,
                allowmultiple,
                includeinsettings,
                settingsinfo);
    }

    /**************************************************************************
     * insertAppvalue - add an App value entry of SQL type REAL
     *
     * @param appvaluename      Name of the Appvalue
     * @param appvalue          value to be held
     * @param allowmultiple     true if duplicate key name is allowed
     * @param includeinsettings true, if can be included in settings
     * @param settingsinfo      Text to be displayed along with the setting
     */
    void insertAppvalue(String appvaluename,
                        double appvalue,
                        boolean allowmultiple,
                        boolean includeinsettings,
                        String settingsinfo) {

        lastappvaleaddrc = APPVALUE_RC_OK;

        /**
         * If non-unique name not allowed then reject duplicated names
         */
        if (ifAppvalueDuplicated(appvaluename, appvalue, allowmultiple)) {
            return;
        }

        int sincl = APPVALUE_DONOTINCLUDEINSETTINGS;
        if (includeinsettings) {
            sincl = APPVALUE_INCLUDEINSETTINGS;
        }
        ContentValues cv = new ContentValues();
        cv.put(DBAppvaluesTableConstants.APPVALUES_NAME_COL,appvaluename);
        cv.put(DBAppvaluesTableConstants.APPVALUES_TYPE_COL,DBConstants.REAL);
        cv.put(DBAppvaluesTableConstants.APPVALUES_REAL_COL,appvalue);
        cv.put(DBAppvaluesTableConstants.APPVALUES_INCLUDEINSETTINGS_COL,sincl);
        cv.put(DBAppvaluesTableConstants.APPVALUES_SETTINGSINFO_COL,settingsinfo);
        db.insert(DBAppvaluesTableConstants.APPVALUES_TABLE,null,cv);
    }

    /**************************************************************************
     * insertAppvalue - add an App value entry of SQL type REAL from double
     * i.e converts double to float and calls insertAppvale
     * with float.
     *
     * @param appvaluename      Name of the Appvalue
     * @param appvalue          value to be held
     * @param allowmultiple     true if duplicate key name is allowed
     * @param includeinsettings true, if can be included in settings
     * @param settingsinfo      Text to be displayed along with the setting
     */
    void insertAppvalue(String appvaluename,
                        float appvalue,
                        boolean allowmultiple,
                        boolean includeinsettings,
                        String settingsinfo) {

        double dappvalue = (double) appvalue;
        insertAppvalue(appvaluename,
                dappvalue,
                allowmultiple,
                includeinsettings,
                settingsinfo)
        ;
    }

    /**
     * Insert string and int appvalue.
     *
     * @param appvaluename      the appvaluename
     * @param appvalue          the appvalue
     * @param extraintvalue     the extraintvalue
     * @param allowmultiple     the allowmultiple
     * @param includeinsettings the includeinsettings
     * @param settingsinfo      the settingsinfo
     */
    void insertStringAndIntAppvalue(String appvaluename,
                               String appvalue,
                               int extraintvalue,
                               boolean allowmultiple,
                               boolean includeinsettings,
                                    String settingsinfo) {

        lastappvaleaddrc = APPVALUE_RC_OK;

        /**
         * If non-unique name not allowed then reject duplicated names
         */
        if (ifAppvalueDuplicated(appvaluename, appvalue, allowmultiple)) {
            return;
        }

        int sincl = APPVALUE_DONOTINCLUDEINSETTINGS;
        if (includeinsettings) {
            sincl = APPVALUE_INCLUDEINSETTINGS;
        }
        ContentValues cv = new ContentValues();
        cv.put(DBAppvaluesTableConstants.APPVALUES_NAME_COL,appvaluename);
        cv.put(DBAppvaluesTableConstants.APPVALUES_TYPE_COL,DBConstants.INT);
        cv.put(DBAppvaluesTableConstants.APPVALUES_TEXT_COL,appvalue);
        cv.put(DBAppvaluesTableConstants.APPVALUES_INT_COL,extraintvalue);
        cv.put(DBAppvaluesTableConstants.APPVALUES_REAL_COL,0.0);
        cv.put(DBAppvaluesTableConstants.APPVALUES_INCLUDEINSETTINGS_COL,sincl);
        cv.put(DBAppvaluesTableConstants.APPVALUES_SETTINGSINFO_COL,settingsinfo);
        db.insert(DBAppvaluesTableConstants.APPVALUES_TABLE,null,cv);

    }


    /**
     * ifAppvalueDuplicated - invoke checkDuplicated for TEXT
     * @param appvaluename  Appvalue Name to be checked
     * @param appvalue      value to be checked
     * @param multipleallowed   true if multiples of the key name are allowed
     * @return              true if duplicated, otherwise false
     */
    private boolean ifAppvalueDuplicated(String appvaluename, String appvalue, boolean multipleallowed) {

        if (!multipleallowed && checkDuplicateName(appvaluename)) {
            return true;
        }
        return checkDuplicated(appvaluename,
                DBAppvaluesTableConstants.APPVALUES_TEXT_COL_FULL,
                appvalue);
    }

    /**
     * ifAppvalueDuplicated - invoke checkDuplicated for INTEGER
     * @param appvaluename  Appvalue Name to be checked
     * @param appvalue      value to be checked
     * @param multipleallowed   true if multiples of the key name are allowed
     * @return              true if duplicated, otherwise false
     */
    private boolean ifAppvalueDuplicated(String appvaluename, long appvalue, boolean multipleallowed) {
        boolean rv = true;
        if (!multipleallowed && checkDuplicateName(appvaluename)) {
            return true;
        }
        return checkDuplicated(appvaluename,
                DBAppvaluesTableConstants.APPVALUES_INT_COL_FULL,
                Long.toString(appvalue));
    }

    /**************************************************************************
     * ifAppvalueDuplicated - invoke checkDuplicated for REAL
     * @param appvaluename  Appvalue Name to be checked
     * @param appvalue      value to be checked
     * @param multipleallowed  true if multiples of the key name are allowed
     * @return              true if duplicated, otherwise false
     */
    private boolean ifAppvalueDuplicated(String appvaluename,
                                         double appvalue,
                                         boolean multipleallowed) {
        boolean rv = true;
        if (!multipleallowed && checkDuplicateName(appvaluename)) {
            return true;
        }
        return checkDuplicated(appvaluename,
                DBAppvaluesTableConstants.APPVALUES_REAL_COL_FULL,
                Double.toString(appvalue));
    }

    /**************************************************************************
     * checkDuplicated - Check if an Appvalue would be a full duplicate
     *                      i.e. name and value are identical
     * @param appvaluename  Appvalue Name to be checked
     * @param valuecolumn   column that holds the value
     * @param appvalue      value
     * @return              true if Name and Value exist in a row, otherwise
     *                      false
     */
    private boolean checkDuplicated(String appvaluename,
                                    String valuecolumn,
                                    String appvalue) {
        String filter =  DBAppvaluesTableConstants.APPVALUES_NAME_COL_FULL +
                " = '" +
                appvaluename +
                "'" + DBConstants.SQLAND +
                valuecolumn +
                " = '" +
                appvalue +
                "' " + DBConstants.SQLENDSTATEMENT;
        Cursor csr = DBCommonMethods.getTableRows(db,
                DBAppvaluesTableConstants.APPVALUES_TABLE,
                filter,
                ""
        );
        if (csr.getCount() > 0) {
            csr.close();
            lastappvalueaddok = false;
            lastappvaleaddrc = APPVALUE_RC_DUPLICATEDNAMEANDVAL;
            return true;
        }
        csr.close();
        return false;
    }

    /**************************************************************************
     * checkDuplicateName - checks id the Appvalue Name already exists
     * @param appvaluename  name of the Appvalue to check
     * @return              true if it exists, otherwsie false
     */
    private boolean checkDuplicateName(String appvaluename) {
        String filter = DBAppvaluesTableConstants.APPVALUES_NAME_COL_FULL +
                " = '" +
                appvaluename +
                "'" + DBConstants.SQLENDSTATEMENT;
        Cursor csr = DBCommonMethods.getTableRows(db,
                DBAppvaluesTableConstants.APPVALUES_TABLE,
                filter,
                ""
        );
        if (csr.getCount() > 0 ) {
            csr.close();
            lastappvalueaddok = false;
            lastappvaleaddrc = APPVALUE_RC_DUPLICATEDNAME;
            return true;
        }
        csr.close();
        return false;
    }

    /**************************************************************************
     * getLongAppvalue
     *
     * @param appvaluename       Appvalue Name
     * @param defaultreturnvalue default value if not found
     * @return extracted value or the default vale
     */
    long getLongAppvalue(String appvaluename, long defaultreturnvalue) {
        long rv = defaultreturnvalue;
        String filter = DBAppvaluesTableConstants.APPVALUES_NAME_COL_FULL +
                " = '" +
                appvaluename +
                "'" + DBConstants.SQLAND +
                DBAppvaluesTableConstants.APPVALUES_TYPE_COL_FULL +
                " = '" +
                DBConstants.INT +
                "'";
        Cursor csr = DBCommonMethods.getTableRows(db,
                DBAppvaluesTableConstants.APPVALUES_TABLE,
                filter,
                ""
        );
        if (csr.getCount() == 1) {
            rv = csr.getLong(csr.getColumnIndex(
                    DBAppvaluesTableConstants.APPVALUES_INT_COL
            ));
        }
        csr.close();
        return rv;
    }

    /**************************************************************************
     * getDoubleAppvalue
     *
     * @param appvaluename       Appvalue Name
     * @param defaultreturnvalue default value if not found
     * @return extracted value or the default vale
     */
    double getDoubleAppvalue(String appvaluename, double defaultreturnvalue) {
        double rv = defaultreturnvalue;
        String filter = DBAppvaluesTableConstants.APPVALUES_NAME_COL_FULL +
                " = '" +
                appvaluename +
                "'" + DBConstants.SQLAND +
                DBAppvaluesTableConstants.APPVALUES_TYPE_COL_FULL +
                " = '" +
                DBConstants.REAL +
                "'";
        Cursor csr = DBCommonMethods.getTableRows(db,
                DBAppvaluesTableConstants.APPVALUES_TABLE,
                filter,
                ""
        );
        if (csr.getCount() == 1) {
            rv = csr.getLong(csr.getColumnIndex(
                    DBAppvaluesTableConstants.APPVALUES_INT_COL
            ));
        }
        csr.close();
        return rv;
    }

    /**************************************************************************
     * getStringAppvalue
     *
     * @param appvaluename       Appvalue Name
     * @param defaultreturnvalue default value if not found
     * @return extracted value or the default vale
     */
    String getStringAppvalue(String appvaluename, String defaultreturnvalue) {
        String rv = defaultreturnvalue;
        String filter = DBAppvaluesTableConstants.APPVALUES_NAME_COL_FULL +
                " = '" +
                appvaluename +
                "'" + DBConstants.SQLAND +
                DBAppvaluesTableConstants.APPVALUES_TYPE_COL_FULL +
                " = '" +
                DBConstants.REAL +
                "'";
        Cursor csr = DBCommonMethods.getTableRows(db,
                DBAppvaluesTableConstants.APPVALUES_TABLE,
                filter,
                ""
        );
        if (csr.getCount() == 1) {
            rv = csr.getString(csr.getColumnIndex(
                    DBAppvaluesTableConstants.APPVALUES_TEXT_COL
            ));
        }
        csr.close();
        return rv;
    }

    /**************************************************************************
     * getLongAppvalues - get an ArrayList of Long Appvalues where the AppValue
     * name is the same. For the same Appvalue Names to
     * exist multipeallowed must be set to true when
     * the AppValue is added (inserted).
     *
     * @param appvaluename Appvalue Name
     * @return extracted values
     */
    ArrayList<Long> getLongAppvalues(String appvaluename) {
        ArrayList<Long> rv = new ArrayList<>();
        String filter = DBAppvaluesTableConstants.APPVALUES_NAME_COL_FULL +
                " = '" +
                appvaluename +
                "'" + DBConstants.SQLAND +
                DBAppvaluesTableConstants.APPVALUES_TYPE_COL_FULL +
                " = '" +
                DBConstants.INT +
                "'";
        Cursor csr = DBCommonMethods.getTableRows(db,
                DBAppvaluesTableConstants.APPVALUES_TABLE,
                filter,
                ""
        );
        while (csr.moveToNext()) {
            rv.add(
                    csr.getLong(csr.getColumnIndex(
                            DBAppvaluesTableConstants.APPVALUES_INT_COL
                    ))
            );
        }
        return rv;
    }

    /**************************************************************************
     * getDoubleAppvalues - get an ArrayList of Double Appvalues where the
     * AppValue name is the same.
     * For the same Appvalue Names to exist multipeallowed
     * must be set to true when the AppValue is added
     * (inserted).
     *
     * @param appvaluename Appvalue Name
     * @return extracted values
     */
    ArrayList<Double> getDoubleAppvalues(String appvaluename) {
        ArrayList<Double> rv = new ArrayList<>();
        String filter = DBAppvaluesTableConstants.APPVALUES_NAME_COL_FULL +
                " = '" +
                appvaluename +
                "'" + DBConstants.SQLAND +
                DBAppvaluesTableConstants.APPVALUES_TYPE_COL_FULL +
                " = '" +
                DBConstants.REAL +
                "'";
        Cursor csr = DBCommonMethods.getTableRows(db,
                DBAppvaluesTableConstants.APPVALUES_TABLE,
                filter,
                ""
        );
        while (csr.moveToNext()) {
            rv.add(
                    csr.getDouble(csr.getColumnIndex(
                            DBAppvaluesTableConstants.APPVALUES_REAL_COL
                    ))
            );
        }
        return rv;
    }

    /**************************************************************************
     * getStringAppvalues - get an ArrayList of String Appvalues where the
     * AppValue name is the same.
     * For the same Appvalue Names to exist multipeallowed
     * must be set to true when the AppValue is added
     * (inserted).
     *
     * @param appvaluename Appvalue Name
     * @return extracted values
     */
    ArrayList<String> getStringAppvalues(String appvaluename) {
        ArrayList<String> rv = new ArrayList<>();
        String filter = DBAppvaluesTableConstants.APPVALUES_NAME_COL_FULL +
                " = '" +
                appvaluename +
                "'" + DBConstants.SQLAND +
                DBAppvaluesTableConstants.APPVALUES_TYPE_COL_FULL +
                " = '" +
                DBConstants.TXT +
                "'";
        Cursor csr = DBCommonMethods.getTableRows(db,
                DBAppvaluesTableConstants.APPVALUES_TABLE,
                filter,
                ""
        );
        while (csr.moveToNext()) {
            rv.add(
                    csr.getString(csr.getColumnIndex(
                            DBAppvaluesTableConstants.APPVALUES_TEXT_COL
                    ))
            );
        }
        return rv;
    }
}
