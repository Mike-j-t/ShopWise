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
 * DBAppvaluesTableConstants - Values Table (stores values)
 */

public class DBAppvaluesTableConstants {

    /**
     * Values Table - Store Values a little like shared preferences
     */
    public static final String APPVALUES_TABLE = "appvalues";

    /**
     * _id (aka values_id) primary key/ index
     */
    public static final String APPVALUES_ID_COL = STD_ID;
    public static final String VALUES_ID_COL_FULL = APPVALUES_TABLE +
            PERIOD +
            APPVALUES_ID_COL;
    public static final String APPVALUES_ALTID_COL = APPVALUES_TABLE +
            APPVALUES_ID_COL;
    public static final String VALUES_ALTID_COL_FULL = APPVALUES_TABLE +
            PERIOD +
            APPVALUES_ALTID_COL;
    public static final String APPVALUES_ID_TYPE = IDTYPE;
    public static final boolean APPVALUES_ID_PRIMARY_INDEX = true;
    public static final DBColumn APPVALUESIDCOL = new DBColumn(APPVALUES_ID_COL,
            APPVALUES_ID_TYPE,
            APPVALUES_ID_PRIMARY_INDEX,
            ""
    );

    /**
     * valuename - name of the value note! need not necessairly be unique
     */
    public static final String APPVALUES_NAME_COL = "appvaluename";
    public static final String VALUES_NAME_COL_FULL = APPVALUES_TABLE +
            PERIOD +
            APPVALUES_NAME_COL;
    public static final String APPVALUES_NAME_TYPE = TXT;
    public static final boolean APPVALUES_NAME_PRIMARY_INDEX = false;
    public static final DBColumn APPVALUESNAMECOL = new DBColumn(APPVALUES_NAME_COL,
            APPVALUES_NAME_TYPE,
            APPVALUES_NAME_PRIMARY_INDEX,
            "");

    /**
     * valuetype - type of value stored
     */
    public static final String APPVALUES_TYPE_COL = "appvaluetype";
    public static final String APPVALUES_TYPE_COL_FULL = APPVALUES_TABLE +
            PERIOD +
            APPVALUES_TYPE_COL;
    public static final String APPVALUES_TYPE_TYPE = TXT;
    public static final boolean APPVALUES_TYPE_PRIMARY_INDEX = false;
    public static final DBColumn APPVALUESTYPECOL = new DBColumn(APPVALUES_TYPE_COL,
            APPVALUES_TYPE_TYPE,
            APPVALUES_TYPE_PRIMARY_INDEX,
            "");

    /**
     * valueint - store of respective held value if type is integer
     */
    public static final String APPVALUES_INT_COL = "appvalueint";
    public static final String APPVALUES_INT_COL_FULL = APPVALUES_TABLE +
            PERIOD +
            APPVALUES_INT_COL;
    public static final String APPVALUES_INT_TYPE = INT;
    public static final boolean APPVALUES_INT_PRIMARY_INDEX = false;
    public static final DBColumn APPVALUESINTCOL = new DBColumn(APPVALUES_INT_COL,
            APPVALUES_INT_TYPE,
            APPVALUES_INT_PRIMARY_INDEX,
            "0");

    /**
     * valuereal - store of respective held value if type is real
     */
    public static final String APPVALUES_REAL_COL = "appvaluereal";
    public static final String APPVALUES_REAL_COl_FULL = APPVALUES_TABLE +
            PERIOD +
            APPVALUES_REAL_COL;
    public static final String APPVALUES_REAL_TYPE = REAL;
    public static final boolean APPVALUES_REAL_PRIMARY_INDEX = false;
    public static final DBColumn APPVALUESREALCOL = new DBColumn(APPVALUES_REAL_COL,
            APPVALUES_REAL_TYPE,
            APPVALUES_REAL_PRIMARY_INDEX,
            "0");

    /**
     * valuetext - store of respective held value if type is text
     */
    public static final String APPVALUES_TEXT_COL = "appvaluetext";
    public static final String APPVALUES_TEXT_COL_FULL = APPVALUES_TABLE +
            PERIOD +
            APPVALUES_TEXT_COL;
    public static final String APPVALUES_TEXT_TYPE = TXT;
    public static final boolean APPVALUES_TEXT_PRIMARY_INDEX = false;
    public static final DBColumn APPVALUESTEXTCOL = new DBColumn(APPVALUES_TEXT_COL,
            APPVALUES_TYPE_TYPE,
            APPVALUES_TEXT_PRIMARY_INDEX,
            "");
    /**
     * valueincludeinsettings
     */
    public static final String APPVALUES_INCLUDEINSETTINGS_COL = "appvalueincludeinsettings";
    public static final String APPVALUES_INCLUDEINSETTINGS_COL_FULL = APPVALUES_TABLE +
            PERIOD +
            APPVALUES_INCLUDEINSETTINGS_COL;
    public static final String APPVALUES_INCLUDEINSETTINGS_TYPE = INT;
    public static final boolean APPVALUES_INCLUDEINSETTINGS_PRIMARY_INDEX = false;
    public static final DBColumn APPVALUESINCLUDEINSETTINGSCOL = new DBColumn(APPVALUES_INCLUDEINSETTINGS_COL,
            APPVALUES_INCLUDEINSETTINGS_TYPE,
            APPVALUES_INCLUDEINSETTINGS_PRIMARY_INDEX,
            "0");

    /**
     * valuesettingsinfo - Information to be displayed in settings
     */
    public static final String APPVALUES_SETTINGSINFO_COL = "appvaluesettingsinfo";
    public static final String APPVALUES_SETTINGSINFO_COL_FULL = APPVALUES_TABLE +
            PERIOD +
            APPVALUES_SETTINGSINFO_COL;
    public static final String APPVALUES_SETTINGSINFO_TYPE = TXT;
    public static final boolean APPVALUES_SETTINGSINFO_PRIMARY_INDEX = false;
    public static final DBColumn APPVALUESSETTINGSINFOCOL = new DBColumn(APPVALUES_SETTINGSINFO_COL,
            APPVALUES_SETTINGSINFO_TYPE,
            APPVALUES_SETTINGSINFO_PRIMARY_INDEX,
            "");

    public static final ArrayList<DBColumn> APPVALUESCOLS = new ArrayList<>(Arrays.asList(APPVALUESIDCOL,
            APPVALUESNAMECOL,
            APPVALUESTYPECOL,
            APPVALUESINTCOL,
            APPVALUESREALCOL,
            APPVALUESTEXTCOL,
            APPVALUESINCLUDEINSETTINGSCOL,
            APPVALUESSETTINGSINFOCOL));

    public static final DBTable APPVALUESTABLE = new DBTable(APPVALUES_TABLE, APPVALUESCOLS);

}
