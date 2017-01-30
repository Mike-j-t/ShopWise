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

    public static final String THISCLASS = DBAppvaluesMethods.class.getSimpleName();
    private static final String LOGTAG = "DB-AVM";
    private Context context;
    private DBDAO dbdao;
    private static SQLiteDatabase db;
    private static long lastappvalueadded;
    private static boolean lastappvalueaddok = false;
    private static int lastappvaleaddrc = APPVALUE_RC_OK;


    /**************************************************************************
     * Instantiates a new Db appvalues methods.
     *
     * @param ctxt the ctxt
     */
    DBAppvaluesMethods(Context ctxt) {
        String methodname = "Construct";
        String msg = "Constructing";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
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
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        String sql = "SELECT * FROM " +
                DBAppvaluesTableConstants.APPVALUES_TABLE +
                " ;";
        Cursor csr = db.rawQuery(sql,null);
        int rv = csr.getCount();
        csr.close();
        msg = "Returning Count of "+ Integer.toString(rv);
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        return rv;
    }

    /**************************************************************************
     * Gets appvalues.
     *
     * @param filter the filter
     * @param order  the order
     * @return the appvalues
     */
    Cursor getAppvalues(String filter, String order) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        Cursor rv = dbdao.getTableRows(
                DBAppvaluesTableConstants.APPVALUES_TABLE,
                "",
                filter,
                order
        );
        msg = "Returning Cursor with " + Integer.toString(
                rv.getCount()
        ) + " rows.";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        return rv;
    }

    /**************************************************************************
     * getLastAppvalueAdded - return the last Appvalue row id that was added
     *
     * @return the id of the last Appvalue that was added
     */
    long getLastAppvalueAdded() {
        String msg = "Invoked and returning id=" +
                Long.toString(lastappvalueadded) +
                " as last added.";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        return lastappvalueadded;
    }

    /**************************************************************************
     * ifAppvalueAddedOK - true if the last attempt to add an Appvalue row
     * was OK, otherwise false
     *
     * @return true if added ok else false
     */
    boolean ifAppvalueAddedOK() {
        String msg = "Invoked and returning AddedOK=" +
                Boolean.toString(lastappvalueaddok);
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        return lastappvalueaddok;
    }

    /**************************************************************************
     * doesAppValueExist - check to see if an Appvalue id exists
     *
     * @param appvalueid id of the Appvalue to check
     * @return true if it does exist, otherwise false
     */
    boolean doesAppValueExist(long appvalueid) {
        String msg = "Invoked for APPVALUEID=" + Long.toString(appvalueid);
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
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
        msg = "Returning Appvalue with id="
                + Long.toString(appvalueid) +
                " Exists=" + Boolean.toString(rv);
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        return rv;
    }

    /**************************************************************************
     * Generic check for an AppValue row
     *
     * i.e. any fields can be specified (none will obviosuly return false)
     *
     * @param appvaluename                  search arg for APPValue NAME
     * @param appvaluetype                  search arg for Appvalue TYPE
     * @param appvalueint                   search arg for AppValue INT
     * @param appvaluereal                  search arg for AppValue REAL
     * @param appvaluetext                  search arg for AppValue TEXT
     * @param appvalueincludeinsettings     search arg for includeinsettings
     * @param appvaluesettingsinfo          search arg for settingsinfo
     * @return
     */
    boolean doesExtendedAppValueExist(String appvaluename,
                                      String appvaluetype,
                                      String appvalueint,
                                      String appvaluereal,
                                      String appvaluetext,
                                      String appvalueincludeinsettings,
                                      String appvaluesettingsinfo) {

        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);

        boolean rv = false;             // Default return value i.e. not found
        int argcount = 0;               // count for supplied args
        ArrayList<String> whereargs_t = new ArrayList<>(); //temp
        String whereclause = "";        // whereclause base

        // Progressively build the whereclause and an intermediate
        // String ArratList (for building the String Array)
        // 1. NAME(KEY) of the AppValue if supplied
        if (appvaluename != null && (appvaluename.length() > 0)) {
            argcount++;
            whereclause = whereclause +
                    DBAppvaluesTableConstants.APPVALUES_NAME_COL_FULL +
                    " = ? ";
            whereargs_t.add(appvaluename);
        }

        // 2. TYPE (INTEGER,REAL or TEXT) of the Appvalue - if supplied
        if (appvaluetype != null && (appvaluetype.length() > 0)) {
            argcount++;
            if (whereclause.length() > 0) {
                whereclause = whereclause + DBConstants.SQLAND;
            }
            whereclause = whereclause +
                    DBAppvaluesTableConstants.APPVALUES_TYPE_COL_FULL +
                    " = ? ";
            whereargs_t.add(appvaluetype);
        }

        // 3. INTEGER column - if supplied
        if (appvalueint != null && (appvalueint.length() > 0)) {
            argcount++;
            if (whereclause.length() > 0) {
                whereclause = whereclause + DBConstants.SQLAND;
            }
            whereclause = whereclause +
                    DBAppvaluesTableConstants.APPVALUES_INT_COL_FULL +
                    " = ? ";
            whereargs_t.add(appvalueint);
        }

        // 4. REAL (as in numeric) column - if supplied
        if (appvaluereal != null && (appvaluereal.length() > 0)) {
            argcount++;
            if (whereclause.length() > 0) {
                whereclause = whereclause + DBConstants.SQLAND;
            }
            whereclause = whereclause +
                    DBAppvaluesTableConstants.APPVALUES_REAL_COL_FULL +
                    " = ? ";
            whereargs_t.add(appvaluereal);
        }

        // 5. TEXT column - if supplied
        if (appvaluetext != null && (appvaluetext.length() > 0)) {
            argcount++;
            if (whereclause.length() > 0) {
                whereclause = whereclause + DBConstants.SQLAND;
            }
            whereclause = whereclause +
                    DBAppvaluesTableConstants.APPVALUES_TEXT_COL_FULL +
                    " = ? ";
            whereargs_t.add(appvaluetext);
        }

        // 6. Include in settings - if supplied
        if (appvalueincludeinsettings != null &&
                (appvalueincludeinsettings.length() > 0)) {
            argcount++;
            if (whereclause.length() > 0) {
                whereclause = whereclause + DBConstants.SQLAND;
            }
            whereclause = whereclause +
                    DBAppvaluesTableConstants.APPVALUES_INCLUDEINSETTINGS_COL_FULL +
                    " = ? ";
            whereargs_t.add(appvalueincludeinsettings);
        }

        // 7. settings info - if supplied
        if (appvaluesettingsinfo != null &&
                (appvaluesettingsinfo.length() > 0)) {
            argcount++;
            if (whereclause.length() > 0) {
                whereclause = whereclause + DBConstants.SQLAND;
            }
            whereclause = whereclause +
                    DBAppvaluesTableConstants.APPVALUES_SETTINGSINFO_COL_FULL +
                    " = ? ";
            whereargs_t.add(appvaluesettingsinfo);
        }

        // If nothing to do then return as not found
        if (argcount < 1) {
            return rv;
        }
        // Secdondary (overcautious) check for nothing to do
        if (whereclause.length() < 1 ) {
            return rv;
        }
        // Convert the String ArrayList to a String Array
        String[] whereargs = new String[argcount];
        for (int i = 0; i < argcount; i++) {
            whereargs[i] = whereargs_t.get(i);
        }

        // Finally run the query
        Cursor csr = db.query(DBAppvaluesTableConstants.APPVALUES_TABLE,
                null,
                whereclause,
                whereargs,
                null,null,null);
        // get the count
        int rowcount = csr.getCount();
        // tidyup by closing the cursor
        csr.close();
        // return the result
        return (rowcount > 0);
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
        String msg = "Invoked (TEXT) for APPVALUENAME=" +
                " APPVALUE=" + appvalue;
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);

        lastappvaleaddrc = APPVALUE_RC_OK;

        /**
         * If non-unique name not allowed then reject duplicated names
         */
        if (ifAppvalueDuplicated(appvaluename, appvalue, allowmultiple)) {
            msg = "TEXT AppValue Name=" + appvaluename +
                    " Duplicate so not inserted.";
            LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
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
        msg = "Inserted TEXT AppValue Name=" + appvaluename;
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
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

        String msg = "Invoked (INTEGER) for APPVALUENAME=" + appvaluename +
                " APPVALUE=" + Long.toString(appvalue);
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);

        lastappvaleaddrc = APPVALUE_RC_OK;

        /**
         * If non-unique name not allowed then reject duplicated names
         */
        if (ifAppvalueDuplicated(appvaluename, appvalue, allowmultiple)) {
            msg = "INTEGER AppValue Name=" + appvaluename +
                    " Duplicate so not inserted.";
            LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
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
        msg = "Inserted INTEGER AppValue Name=" + appvaluename;
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
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
        String msg = "Invoked (LONG (Note! stored as INTEGER in DB)) for APPVALUENAME=" +
                appvaluename +
                " APPVALUE=" + Integer.toString(appvalue);
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
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
        String msg = "Invoked (REAL (from DOUBLE)) for APPVALUENAME=" +
                appvaluename +
                " APPVALUE=" + Double.toString(appvalue);
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);

        lastappvaleaddrc = APPVALUE_RC_OK;

        /**
         * If non-unique name not allowed then reject duplicated names
         */
        if (ifAppvalueDuplicated(appvaluename, appvalue, allowmultiple)) {
            msg = "REAL AppValue Name=" + appvaluename +
                    " Duplicate so not inserted.";
            LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
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
        msg = "Inserted REAL AppValue Name=" + appvaluename;
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
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
        String msg = "Invoked (REAL (from FLOAT to DOUBLE)) for APPVALUENAME=" +
                appvaluename +
                " APPVALUE=" + appvalue;
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);

        double dappvalue = (double) appvalue;
        insertAppvalue(appvaluename,
                dappvalue,
                allowmultiple,
                includeinsettings,
                settingsinfo)
        ;
    }

    /**************************************************************************
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
        String msg = "Invoked (TEXT and INTEGER specal case) for APPVALUENAME=" +
                appvaluename +
                " APPVALUE(TEXT)=" + appvalue +
                " APPVALUE(INTEGER)=" + Integer.toString(extraintvalue);
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);

        lastappvaleaddrc = APPVALUE_RC_OK;

        /**
         * If non-unique name not allowed then reject duplicated names
         */
        if (ifAppvalueDuplicated(appvaluename, appvalue, allowmultiple)) {
            msg = "TEXT and INTEGER special case, AppValue Name=" + appvaluename +
                    " Duplicate so not inserted.";
            LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
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
        msg = "Inserted TEXT and INTEGER special case, AppValue Name=" + appvaluename;
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);

    }


    /**************************************************************************
     * ifAppvalueDuplicated - invoke checkDuplicated for TEXT
     * @param appvaluename  Appvalue Name to be checked
     * @param appvalue      value to be checked
     * @param multipleallowed   true if multiples of the key name are allowed
     * @return              true if duplicated, otherwise false
     */
    private boolean ifAppvalueDuplicated(String appvaluename,
                                         String appvalue,
                                         boolean multipleallowed) {
        String msg = "Invoked (TEXT) for APPVALUENAME=" + appvaluename +
                "APPVALUE=" + appvalue;
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);

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
    private boolean ifAppvalueDuplicated(String appvaluename,
                                         long appvalue,
                                         boolean multipleallowed) {
        String msg = "Invoked (INTEGER) for APPVALUENAME=" + appvaluename +
                " APPVALUE=" + Long.toString(appvalue);
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
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
        String msg = "Invoked (REAL) for APPVALUENAME=" + appvaluename +
                " APPVALUE=" + Double.toString(appvalue);
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
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
        String msg = "Invoked for APPVALUENAME=" + appvaluename +
                " COLUMN=" + valuecolumn +
                " APPAVLUE=" + appvalue;
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
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
            msg = "Full Duplicate=" + Boolean.toString(true);
            LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
            return true;
        }
        csr.close();
        msg = "Full Duplicate=" + Boolean.toString(false);
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        return false;
    }

    /**************************************************************************
     * checkDuplicateName - checks if the Appvalue Name already exists
     * @param appvaluename  name of the Appvalue to check
     * @return              true if it exists, otherwsie false
     */
    private boolean checkDuplicateName(String appvaluename) {
        String msg = "Invoked for APPVALUENAME=" + appvaluename;
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
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
            msg = "Returning " + Boolean.toString(true);
            LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
            return true;
        }
        csr.close();
        msg = "Returning " + Boolean.toString(false);
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
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
        String msg = "Invoked for APPVALUENAME=" + appvaluename;
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
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
        msg = "Returning " + Long.toString(rv);
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
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
        String msg = "Invoked for APPVALUENAME=" + appvaluename;
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
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
        msg = "Returning " + Double.toString(rv);
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
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
        String msg = "Invoked for APPVALUENAME=" + appvaluename;
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
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
        msg = "Returning " + rv;
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
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
        String msg = "Invoked for APPVALUENAME=" + appvaluename;
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
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
        msg = "Returning LONG ArrayList with " +
                Integer.toString(rv.size()) +
                " Elements for APPVALUENAME=" + appvaluename;
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
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
        String msg = "Invoked for APPVALUENAME=" + appvaluename;
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
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
        msg = "Returning DOUBLE ArrayList with " +
                Integer.toString(rv.size()) +
                " Elements for APPVALUENAME=" + appvaluename;
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
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
        String msg = "Invoked for APPVALUENAME=" + appvaluename;
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
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
        msg = "Returning STRING ArrayList with " +
                Integer.toString(rv.size()) +
                " Elements for APPVALUENAME=" + appvaluename;
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        return rv;
    }
}
