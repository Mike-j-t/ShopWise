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
@SuppressWarnings("FieldCanBeLocal")
class IntegrityCheckDBHelper extends SQLiteOpenHelper implements DatabaseErrorHandler {

    private static final String DATABASE_NAME = "IC"+DBConstants.DATABASE_NAME;
    private static final int DATABASE_CORRUPTED = 1;
    private static int databasestate = 0;
    private Context context;

    IntegrityCheckDBHelper(Context context,
                           String name,
                           SQLiteDatabase.CursorFactory factory,
                           int version, DatabaseErrorHandler errorHandler) {
        super(context, DATABASE_NAME,factory,1,errorHandler);
        this.context = context;

    }

    public void onCreate(SQLiteDatabase db) {}
    public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion) {}
    public void onCorruption(SQLiteDatabase db) {
        databasestate = 1;
    }
    public boolean checkDB() {
        SQLiteDatabase icdb = this.getReadableDatabase();
        String icsqlstr = " PRAGMA quick_check";
        Cursor iccsr;

        iccsr = icdb.rawQuery(icsqlstr,null);
        iccsr.close();
        return false;
    }
    public static void setDatabaseCorrupted() {
        databasestate = DATABASE_CORRUPTED;
    }
    public boolean isDatabaseCouurpted() {
        return databasestate == 0;
    }
}
