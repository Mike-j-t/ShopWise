package mjt.shopwise;

import java.util.ArrayList;
import java.util.Arrays;

import static mjt.shopwise.DBConstants.IDTYPE;
import static mjt.shopwise.DBConstants.INT;
import static mjt.shopwise.DBConstants.PERIOD;
import static mjt.shopwise.DBConstants.REAL;
import static mjt.shopwise.DBConstants.STD_ID;
import static mjt.shopwise.DBConstants.TXT;

/**
 * DBValuesTableConstants - Values Table (stores values)
 */

public class DBValuesTableConstants {

    /**
     * Values Table - Store Values a little like shared preferences
     */
    public static final String VALUES_TABLE = "values";

    /**
     * _id (aka values_id) primary key/ index
     */
    public static final String VALUES_ID_COL = STD_ID;
    public static final String VALUES_ID_COL_FULL = VALUES_TABLE +
            PERIOD +
            VALUES_ID_COL;
    public static final String VALUES_ALTID_COL = VALUES_TABLE +
            VALUES_ID_COL;
    public static final String VALUES_ALTID_COL_FULL = VALUES_TABLE +
            PERIOD +
            VALUES_ALTID_COL;
    public static final String VALUES_ID_TYPE = IDTYPE;
    public static final boolean VALUES_ID_PRIMARY_INDEX = true;
    public static final DBColumn VALUESIDCOL = new DBColumn(VALUES_ID_COL,
            VALUES_ID_TYPE,
            VALUES_ID_PRIMARY_INDEX,
            ""
    );

    /**
     * valuename - name of the value note! need not necessairly be unique
     */
    public static final String VALUES_NAME_COL = "valuename";
    public static final String VALUES_NAME_COL_FULL = VALUES_TABLE +
            PERIOD +
            VALUES_NAME_COL;
    public static final String VALUES_NAME_TYPE = TXT;
    public static final boolean VALUES_NAME_PRIMARY_INDEX = false;
    public static final DBColumn VALUESNAMECOL = new DBColumn(VALUES_NAME_COL,
            VALUES_NAME_TYPE,
            VALUES_NAME_PRIMARY_INDEX,
            "");

    /**
     * valuetype - type of value stored
     */
    public static final String VALUES_TYPE_COL = "valuetype";
    public static final String VALUES_TYPE_COL_FULL = VALUES_TABLE +
            PERIOD +
            VALUES_TYPE_COL;
    public static final String VALUES_TYPE_TYPE = TXT;
    public static final boolean VALUES_TYPE_PRIMARY_INDEX = false;
    public static final DBColumn VALUESTYPECOL = new DBColumn(VALUES_TYPE_COL,
            VALUES_TYPE_TYPE,
            VALUES_TYPE_PRIMARY_INDEX,
            "");

    /**
     * valueint - store of respective held value if type is integer
     */
    public static final String VALUES_INT_COL = "valueint";
    public static final String VALUES_INT_COL_FULL = VALUES_TABLE +
            PERIOD +
            VALUES_INT_COL;
    public static final String VALUES_INT_TYPE = INT;
    public static final boolean VALUES_INT_PRIMARY_INDEX = false;
    public static final DBColumn VALUESINTCOL = new DBColumn(VALUES_INT_COL,
            VALUES_INT_TYPE,
            VALUES_INT_PRIMARY_INDEX,
            "0");

    /**
     * valuereal - store of respective held value if type is real
     */
    public static final String VALUES_REAL_COL = "valuereal";
    public static final String VALUES_REAL_COl_FULL = VALUES_TABLE +
            PERIOD +
            VALUES_REAL_COL;
    public static final String VALUES_REAL_TYPE = REAL;
    public static final boolean VALUES_REAL_PRIMARY_INDEX = false;
    public static final DBColumn VALUESREALCOL = new DBColumn(VALUES_REAL_COL,
            VALUES_REAL_TYPE,
            VALUES_REAL_PRIMARY_INDEX,
            "0");

    /**
     * valuetext - store of respective held value if type is text
     */
    public static final String VALUES_TEXT_COL = "valuetext";
    public static final String VALUES_TEXT_COL_FULL = VALUES_TABLE +
            PERIOD +
            VALUES_TEXT_COL;
    public static final String VALUES_TEXT_TYPE = TXT;
    public static final boolean VALUES_TEXT_PRIMARY_INDEX = false;
    public static final DBColumn VALUESTEXTCOL = new DBColumn(VALUES_TEXT_COL,
            VALUES_TYPE_TYPE,
            VALUES_TEXT_PRIMARY_INDEX,
            "");
    /**
     * valueincludeinsettings
     */
    public static final String VALUES_INCLUDEINSETTINGS_COL = "valueincludeinsettings";
    public static final String VALUES_INCLUDEINSETTINGS_COL_FULL = VALUES_TABLE +
            PERIOD +
            VALUES_INCLUDEINSETTINGS_COL;
    public static final String VALUES_INCLUDEINSETTINGS_TYPE = INT;
    public static final boolean VALUES_INCLUDEINSETTINGS_PRIMARY_INDEX = false;
    public static final DBColumn VALUESINCLUDEINSETTINGSCOL = new DBColumn(VALUES_INCLUDEINSETTINGS_COL,
            VALUES_INCLUDEINSETTINGS_TYPE,
            VALUES_INCLUDEINSETTINGS_PRIMARY_INDEX,
            "0");

    /**
     * valuesettingsinfo - Information to be displayed in settings
     */
    public static final String VALUES_SETTINGSINFO_COL = "valuesettingsinfo";
    public static final String VALUES_SETTINGSINFO_COL_FULL = VALUES_TABLE +
            PERIOD +
            VALUES_SETTINGSINFO_COL;
    public static final String VALUES_SETTINGSINFO_TYPE = TXT;
    public static final boolean VALUES_SETTINGSINFO_PRIMARY_INDEX = false;
    public static final DBColumn VALUESSETTINGSINFOCOL = new DBColumn(VALUES_SETTINGSINFO_COL,
            VALUES_SETTINGSINFO_TYPE,
            VALUES_SETTINGSINFO_PRIMARY_INDEX,
            "");

    public static final ArrayList<DBColumn> VALUESCOLS = new ArrayList<>(Arrays.asList(VALUESIDCOL,
            VALUESNAMECOL,
            VALUESTYPECOL,
            VALUESINTCOL,
            VALUESREALCOL,
            VALUESTEXTCOL,
            VALUESINCLUDEINSETTINGSCOL,
            VALUESSETTINGSINFOCOL));

    public static final DBTable VALUESTABLE = new DBTable(VALUES_TABLE, VALUESCOLS);

}
