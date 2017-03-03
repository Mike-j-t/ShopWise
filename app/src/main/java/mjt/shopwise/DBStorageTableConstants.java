package mjt.shopwise;

import java.util.ArrayList;
import java.util.Arrays;

import static mjt.shopwise.DBConstants.DEFAULTORDER;
import static mjt.shopwise.DBConstants.IDTYPE;
import static mjt.shopwise.DBConstants.INT;
import static mjt.shopwise.DBConstants.PERIOD;
import static mjt.shopwise.DBConstants.STD_ID;
import static mjt.shopwise.DBConstants.TXT;

/**
 * DBStorageTableConstants - Constant Values for the Storage Table
 */

@SuppressWarnings("WeakerAccess")
public class DBStorageTableConstants {
    /**
     * STORAGE Table
     * ID (LONG) KEY/PRIMARY INDEX
     * STOREAGENAME (TEXT) Name of the storage location
     * STOREAGEORDER (INT) Order of the storage location
     */
    public static final String STORAGE_TABLE = "storage";

    public  static final String STORAGE_ID_COL = STD_ID;
    public static final String  STORAGE_ID_COL_FULL = STORAGE_TABLE +
            PERIOD +
            STORAGE_ID_COL;
    public static final String STORAGE_ALTID_COL = STORAGE_TABLE + STD_ID;
    public static final String STORAGE_ALTID_COL_FULL = STORAGE_TABLE +
            PERIOD +
            STORAGE_ALTID_COL;
    public static final String STORAGE_ID_TYPE = IDTYPE;

    public static final Boolean STORAGE_ID_PRIMARY_INDEX = true;
    public static final DBColumn STORAGEIDCOL = new DBColumn(
            STORAGE_ID_COL,
            STORAGE_ID_TYPE,
            STORAGE_ID_PRIMARY_INDEX,
            ""
    );
    public static final String STORAGE_ORDER_COL = "storageorder";
    public static final String STORAGE_ORDER_COL_FULL = STORAGE_TABLE +
            PERIOD +
            STORAGE_ORDER_COL;
    public static final String STORAGE_ORDER_TYPE = INT;
    public static final Boolean STORAGE_ORDER_PRIMARY_INDEX = false;
    public static final DBColumn STORAGEORDERCOL = new DBColumn(
            STORAGE_ORDER_COL,
            STORAGE_ORDER_TYPE,
            STORAGE_ORDER_PRIMARY_INDEX,
            DEFAULTORDER
    );
    public static final String STORAGE_NAME_COL = "storagename";
    public static final String STORAGE_NAME_COL_FULL = STORAGE_TABLE +
            PERIOD +
            STORAGE_NAME_COL;
    public static final String STORAGE_NAME_TYPE = TXT;
    public static final Boolean STORAGE_NAME_PRIMARY_INDEX = false;


    public static final DBColumn STORAGENAMECOL = new DBColumn(
            STORAGE_NAME_COL,
            STORAGE_NAME_TYPE,
            STORAGE_NAME_PRIMARY_INDEX,
            ""
    );
    // Create as DBColumn Arraylist of columns in the table
    public static final ArrayList<DBColumn> STORAGECOLS = new ArrayList<>(
            Arrays.asList(
                    STORAGEIDCOL,
                    STORAGEORDERCOL,
                    STORAGENAMECOL
            )
    );
    // Creaet a DBTable instance of the table using the ArrayLIst of DBColumns
    public static final DBTable STORAGETABLE = new DBTable(
            STORAGE_TABLE,
            STORAGECOLS
    );
    public static final String STORAGEMAXORDERCOLUMN = "maxorder";

}
