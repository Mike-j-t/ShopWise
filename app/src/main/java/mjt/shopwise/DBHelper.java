package mjt.shopwise;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * DBHelper
 */
@SuppressWarnings("WeakerAccess")
class DBHelper extends SQLiteOpenHelper {

    private static final String LOGTAG = "SW-DBHelper";
    private static final String DBNAME = DBConstants.DATABASE_NAME;
    private static final String dbcreated =
            "001I Database " + DBNAME + " created.";
    private static final String dbunusable =
            "002E Database " + DBNAME +
            " has been set as unusable (according to schema).";
    private   static final String dbexpanded =
            "003I Database " + DBNAME + " expanded.";
    private static final String dbexpandskipped =
            "004I Database " + DBNAME + " expand skipped - nothing to alter.";
    private static final String dbbuildskipped =
            "005I Database" + DBNAME + " build skipped - no tables to add";
    public static final String THISCLASS = DBHelper.class.getSimpleName();

    /**
     * Consrtuctor
     *
     * @param context activity context
     * @param name    database name
     * @param factory cursorfactory
     * @param version database version
     */
    DBHelper(Context context, @SuppressWarnings("SameParameterValue") String name, @SuppressWarnings("SameParameterValue") SQLiteDatabase.CursorFactory factory, @SuppressWarnings("SameParameterValue") int version) {
        super(context, name, factory, version);
    }

    /**
     * Instantiates a new Db helper.
     *
     * @param context the context
     */
    DBHelper(Context context) {
        super(context, DBConstants.DATABASE_NAME, null, 1);
    }

    private static DBHelper instance;

    /**
     * Gets helper.
     *
     * @param context the context
     * @return the helper
     */
    static synchronized DBHelper getHelper(Context context) {
        if(instance == null) {
            instance = new DBHelper(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        expand(db, false);
    }
    

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion) {

    }

    /**
     * expand create database tables
     *
     * @param db             SQLIte Database, if null then instance is used
     * @param buildandexpand to attempt both create and expand
     */
    void expand(SQLiteDatabase db, boolean buildandexpand) {

        String mode = "Create Mode.";
        if (buildandexpand) {
            mode = "Expand Mode.";
        }
        String msg = mode;
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        // if no database has been passed then get the database
        if(db == null) {
            db = instance.getWritableDatabase();
        }
        // Build Tables to reflect schema (SHOPWISEBASESCHEMA) only if schema is usable
        if(DBConstants.SHOPWISEBASESCHEMA.isDBDatabaseUsable()) {
            // Check to see if any tables need to be added
            ArrayList<String> buildsql = DBConstants.SHOPWISEBASESCHEMA.generateDBBuildSQL(db);
            if (!buildsql.isEmpty()) {
                DBConstants.SHOPWISEBASESCHEMA.actionDBBuildSQL(db);
                msg = dbcreated + buildsql.size() + " tables added.";
                LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
            } else {
                msg = dbbuildskipped;
                LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
            }
            if(buildandexpand) {
                ArrayList<String> altersql = DBConstants.SHOPWISEBASESCHEMA.generateDBAlterSQL(db);
                if(!altersql.isEmpty()) {
                    msg = dbexpanded + altersql.size() + " columns added.";
                    LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
                    DBConstants.SHOPWISEBASESCHEMA.actionDBAlterSQL(db);
                }  else {
                    msg = dbexpandskipped;
                    LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
                }
            }
        } else {
            msg = dbunusable + "\n" +
                    DBConstants.SHOPWISEBASESCHEMA.getAllDBDatabaseProblemMsgs();
            LogMsg.LogMsg(LogMsg.LOGTYPE_ERROR,LOGTAG,msg,THISCLASS,methodname);
        }
    }
    public static void reopen(Context context) {
        instance = new DBHelper(context);
    }
}
