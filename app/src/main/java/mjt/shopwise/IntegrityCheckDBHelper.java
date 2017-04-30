package mjt.shopwise;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * SQLite DB Openhelper specifically for the purpose of an integrity check
 *  The database is used solely for this purpose.
 *  Note! Dependant upon the main DB as it's name is used as the suffix of the
 *  IC database name i.e. IC is the prefix, first part of the database name.
 */
@SuppressWarnings({"FieldCanBeLocal", "CanBeFinal"})
class IntegrityCheckDBHelper extends SQLiteOpenHelper implements DatabaseErrorHandler {

    private static final String DATABASE_NAME = "IC"+DBConstants.DATABASE_NAME;
    private static final int DATABASE_CORRUPTED = 1;
    private static int databasestate = 0;
    @SuppressWarnings("unused")
    private Context context;
    public static final String THISCLASS = IntegrityCheckDBHelper.class.getSimpleName();
    private static final String LOGTAG = "SW_ICDBH";

    IntegrityCheckDBHelper(Context context,
                           @SuppressWarnings({"SameParameterValue", "UnusedParameters"}) String name,
                           @SuppressWarnings("SameParameterValue") SQLiteDatabase.CursorFactory factory,
                           @SuppressWarnings({"SameParameterValue", "UnusedParameters"}) int version, @SuppressWarnings("SameParameterValue") DatabaseErrorHandler errorHandler) {
        super(context, DATABASE_NAME,factory,1,errorHandler);
        String msg = "Constructing";
        String methodname = "Construct";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        this.context = context;
    }

    public void onCreate(SQLiteDatabase db) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
    }
    public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
    }
    public void onCorruption(SQLiteDatabase db) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        databasestate = 1;
    }
    @SuppressWarnings({"SameReturnValue", "unused"})
    public boolean checkDB() {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        SQLiteDatabase icdb = this.getReadableDatabase();
        String icsqlstr = " PRAGMA quick_check";
        Cursor iccsr;

        iccsr = icdb.rawQuery(icsqlstr,null);
        iccsr.close();
        return false;
    }
    @SuppressWarnings("unused")
    public static void setDatabaseCorrupted() {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        databasestate = DATABASE_CORRUPTED;
    }
    @SuppressWarnings("unused")
    public boolean isDatabaseCouurpted() {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        return databasestate == 0;
    }
}
