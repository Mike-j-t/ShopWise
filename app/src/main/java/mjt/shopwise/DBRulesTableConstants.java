package mjt.shopwise;

import java.util.ArrayList;
import java.util.Arrays;

import static mjt.shopwise.DBConstants.IDTYPE;
import static mjt.shopwise.DBConstants.INT;
import static mjt.shopwise.DBConstants.PERIOD;
import static mjt.shopwise.DBConstants.STD_ID;
import static mjt.shopwise.DBConstants.TXT;

/**
 * Rules Table - Rules add shopping list entries regularly
 */

public class DBRulesTableConstants {

    public static final String RULES_TABLE = "rules";

    /**
     * _id (aka rules_id) index/primary key
     */
    public static final String RULES_ID_COL = STD_ID;
    public static final String RULES_ID_COL_FULL = RULES_TABLE +
            PERIOD + RULES_ID_COL;
    public static final String RULES_ALTID_COL = RULES_TABLE + RULES_ID_COL;
    public static final String RULES_ALTID_COL_FULL = RULES_TABLE +
            PERIOD +
            RULES_ALTID_COL;
    public static final String RULES_ID_TYPE = IDTYPE;
    public static final boolean RULES_ID_PRIMARY_INDEX = true;
    public static final DBColumn RULESIDCOL = new DBColumn(RULES_ID_COL,
            RULES_ID_TYPE,
            RULES_ID_PRIMARY_INDEX,
            ""
    );

    /**
     * rulesproductref - reference to the product
     */
    public static final String RULES_PRODUCTREF_COL = "ruleproductref";
    public static final String RULES_PRODUCTREF_COL_FULL = RULES_TABLE +
            PERIOD + RULES_PRODUCTREF_COL;
    public static final String RULES_PRODUCTREF_TYPE = INT;
    public static final boolean RULES_PRODUCTREF_PRIMARY_INDEX = false;
    public static final DBColumn RULESPRODUCTREFCOL = new DBColumn(RULES_PRODUCTREF_COL,
            RULES_PRODUCTREF_TYPE,
            RULES_ID_PRIMARY_INDEX,
            "");

    /**
     * rulesaisleref - reference to the aisle
     */
    public static final String RULES_AISLEREF_COL = "ruleaisleref";
    public static final String RULES_AISLEREF_COL_FULL = RULES_TABLE +
            PERIOD +
            RULES_AISLEREF_COL;
    public static final String RULES_AISLEREF_TYPE = INT;
    public static final boolean RULES_AISLEREF_PRIMARY_INDEX = false;
    public static final DBColumn RULESAISLEREFCOL = new DBColumn(RULES_AISLEREF_COL,
            RULES_AISLEREF_TYPE,
            RULES_AISLEREF_PRIMARY_INDEX,
            ""
    );

    /**
     * rulename - name of the rule
     */
    public static final String RULES_NAME_COL = "rulename";
    public static final String RULES_NAME_COL_FULL = RULES_TABLE +
            PERIOD +
            RULES_NAME_COL;
    public static final String RULES_NAME_TYPE = TXT;
    public static final boolean RULES_NAME_PRIMARY_INDEX = false;
    public static final DBColumn RULESNAMECOL = new DBColumn(RULES_NAME_COL,
            RULES_NAME_TYPE,
            RULES_NAME_PRIMARY_INDEX,
            "");

    /**
     * ruleuses - number of times this rule has been used
     */
    public static final String RULES_USES_COL = "ruleuses";
    public static final String RULES_USES_COL_FULL = RULES_TABLE +
            PERIOD +
            RULES_USES_COL;
    public static final String RULES_USES_TYPE = INT;
    public static final boolean RULES_USES_PRIMARY_INDEX = false;
    public static final DBColumn RULESUSESCOL = new DBColumn(RULES_USES_COL,
            RULES_USES_TYPE,
            RULES_USES_PRIMARY_INDEX,
            "0");

    /**
     * ruleprompt - whether or not to prompt when rule is acted on
     */
    public static final String RULES_PROMPT_COL = "ruleprompt";
    public static final String RULES_PROMPT_COL_FULL = RULES_TABLE +
            PERIOD +
            RULES_PROMPT_COL;
    public static final String RULES_PROMPT_TYPE = INT;
    public static final boolean RULES_PROMPT_PRIMARY_INDEX = false;
    public static final DBColumn RULESPROMPTCOL = new DBColumn(RULES_PROMPT_COL,
            RULES_PROMPT_TYPE,
            RULES_PROMPT_PRIMARY_INDEX,
            "0");

    /**
     * ruleacton - date rule is to be applied to the shopping list
     */
    public static final String RULES_ACTON_COL = "ruleacton";
    public static final String RULES_ACTOON_COL_FULL = RULES_TABLE +
            PERIOD +
            RULES_ACTON_COL;
    public static final String RULES_ACTON_TYPE = INT;
    public static final boolean RULES_ACTON_PRIMARY_INDEX = false;
    public static final DBColumn RULESACTONCOL = new DBColumn(RULES_ACTON_COL,
            RULES_ACTON_TYPE,
            RULES_ACTON_PRIMARY_INDEX,
            "0");

    /**
     * ruleperiod - interval type
     * note includes period definitions
     */
    public static final String PERIOD_DAYS = "DAYS";
    public static final String PERIOD_DAYS_SINGULAR = "DAY";
    public static final int PERIOD_DAYSASINT = 0;
    public static final String PERIOD_WEEKS = "WEEKS";
    public static final String PERIOD_WEEKS_SINGULAR = "WEEK";
    public static final int PERIOD_WEEKSASINT = 1;
    public static final String PERIOD_FORTNIGHTS = "FORTNIGHTS";
    public static final String PERIOD_FORTNIGHTS_SINGULAR = "FORTNIGHT";
    public static final int PERIOD_FORTNIGHTSASINT = 2;
    public static final String PERIOD_MONTHS = "MONTHS";
    public static final String PERIOD_MONTHS_SINGULAR = "MONTH";
    public static final int PERIOD_MONTHSASINT = 3;
    public static final String PERIOD_QUARTERS = "QUARTERS";
    public static final String PERIOD_QUARTERS_SINGULAR = "QUARTER";
    public static final int PERIOD_QUARTERSASINT = 4;
    public static final String PERIOD_YEARS = "YEARS";
    public static final String PERIOD_YEARS_SINGULAR = "YEAR";
    public static final int PERIOD_YEARSASINT = 5;

    public static final String RULES_PERIOD_COL = "ruleperiod";
    public static final String RULES_PERIOD_COL_FULL = RULES_TABLE +
            PERIOD + RULES_PERIOD_COL;
    public static final String RULES_PERIOD_TYPE = INT;
    public static final boolean RULES_PERIOD_PRIMARY_INDEX = false;
    public static final DBColumn RULESPERIODCOL = new DBColumn(RULES_PERIOD_COL,
            RULES_PERIOD_TYPE,
            RULES_PERIOD_PRIMARY_INDEX,
            "0");

    /**
     * rulemultiplier - number of ruleperiods per interval
     */
    public static final String RULES_MULTIPLIER_COL = "rulemultiplier";
    public static final String RULES_MULTIPLIER_COL_FULL = RULES_TABLE +
            PERIOD +
            RULES_MULTIPLIER_COL;
    public static final String RULES_MULTIPLIER_TYPE = INT;
    public static final boolean RULES_MULTIPLIER_PRIMARY_INDEX = false;
    public static final DBColumn RULESMULTIPLIERCOL = new DBColumn(RULES_MULTIPLIER_COL,
            RULES_MULTIPLIER_TYPE,
            RULES_MULTIPLIER_PRIMARY_INDEX,
            "1");
    public static final ArrayList<DBColumn> RULESCOLS = new ArrayList<>(Arrays.asList(RULESIDCOL,
            RULESPRODUCTREFCOL,
            RULESAISLEREFCOL,
            RULESNAMECOL,
            RULESUSESCOL,
            RULESPROMPTCOL,
            RULESACTONCOL,
            RULESPERIODCOL,
            RULESMULTIPLIERCOL));
    public static final DBTable RULESTABLE = new DBTable(RULES_TABLE,RULESCOLS);
}
