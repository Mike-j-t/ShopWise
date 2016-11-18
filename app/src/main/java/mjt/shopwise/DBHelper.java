package mjt.shopwise;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * DBHelper
 */

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

    /**
     * Consrtuctor
     * @param context activity context
     * @param name      database name
     * @param factory   cursorfactory
     * @param version   database version
     */
    DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    DBHelper(Context context) {
        super(context, DBConstants.DATABASE_NAME, null, 1);
    }

    private static DBHelper instance;

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
     * @param db SQLIte Database, if null then instance is used
     * @param buildandexpand to attempt both create and expand
     *
     */
    void expand(SQLiteDatabase db, boolean buildandexpand) {
        String mode = "Create Mode.";
        if (buildandexpand) {
            mode = "Expand Mode.";
        }
        Log.i(LOGTAG,mode);
        // if no database as been passed then get the database
        if(db == null) {
            db = instance.getWritableDatabase();
        }
        // Build Tables to reflect schema (SHOPWISE) only if schema is usable
        if(DBConstants.SHOPWISE.isDBDatabaseUsable()) {
            // Check to see if any tables need to be added
            ArrayList<String> buildsql = DBConstants.SHOPWISE.generateDBBuildSQL(db);
            if (!buildsql.isEmpty()) {
                DBConstants.SHOPWISE.actionDBBuildSQL(db);
                Log.i(LOGTAG, dbcreated + buildsql.size() + " tables added.");
            } else {
                Log.i(LOGTAG, dbbuildskipped);
            }
            if(buildandexpand) {
                ArrayList<String> altersql = DBConstants.SHOPWISE.generateDBAlterSQL(db);
                if(!altersql.isEmpty()) {
                    Log.i(LOGTAG,dbexpanded + altersql.size() + " columns added.");
                    DBConstants.SHOPWISE.actionDBAlterSQL(db);
                }  else {
                    Log.i(LOGTAG,dbexpandskipped);
                }
            }
        } else {
            Log.e(LOGTAG,dbunusable +
                    "\n" +
                    DBConstants.SHOPWISE.getAllDBDatabaseProblemMsgs());
        }
    }
}
