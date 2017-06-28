package mjt.shopwise;

import java.util.ArrayList;
import java.util.Arrays;

import mjt.dbcolumn.DBColumn;
import mjt.dbindex.DBIndex;
import mjt.dbtable.DBTable;

import static mjt.sqlwords.SQLKWORD.*;

/**
 * Rules Table - Rules add shopping list entries regularly
 */
@SuppressWarnings("WeakerAccess")
public class DBRulesTableConstants {

    /**
     * The constant RULES_TABLE.
     */
    public static final String RULES_TABLE = "rules";

    /**
     * _id (aka rules_id) index/primary key
     */
    public static final String RULES_ID_COL = SQLSTD_ID;
    /**
     * The constant RULES_ID_COL_FULL.
     */
    public static final String RULES_ID_COL_FULL = RULES_TABLE +
            SQLPERIOD + RULES_ID_COL;
    /**
     * The constant RULES_ALTID_COL.
     */
    public static final String RULES_ALTID_COL = RULES_TABLE + RULES_ID_COL;
    /**
     * The constant RULES_ALTID_COL_FULL.
     */
    @SuppressWarnings("unused")
    public static final String RULES_ALTID_COL_FULL = RULES_TABLE +
            SQLPERIOD +
            RULES_ALTID_COL;
    /**
     * The constant RULESIDCOL.
     */
    public static final DBColumn RULESIDCOL = new DBColumn(true);


    /**
     * rulesproductref - reference to the product
     */
    public static final String RULES_PRODUCTREF_COL = "ruleproductref";
    /**
     * The constant RULES_PRODUCTREF_COL_FULL.
     */
    public static final String RULES_PRODUCTREF_COL_FULL = RULES_TABLE +
            SQLPERIOD + RULES_PRODUCTREF_COL;
    /**
     * The constant RULES_PRODUCTREF_TYPE.
     */
    public static final String RULES_PRODUCTREF_TYPE = SQLINTEGER;
    /**
     * The constant RULES_PRODUCTREF_PRIMARY_INDEX.
     */
    public static final boolean RULES_PRODUCTREF_PRIMARY_INDEX = false;
    /**
     * The constant RULESPRODUCTREFCOL.
     */
    public static final DBColumn RULESPRODUCTREFCOL = new DBColumn(RULES_PRODUCTREF_COL,
            RULES_PRODUCTREF_TYPE,
            RULES_PRODUCTREF_PRIMARY_INDEX,
            "");


    /**
     * rulesaisleref - reference to the aisle
     */
    public static final String RULES_AISLEREF_COL = "ruleaisleref";
    /**
     * The constant RULES_AISLEREF_COL_FULL.
     */
    public static final String RULES_AISLEREF_COL_FULL = RULES_TABLE +
            SQLPERIOD +
            RULES_AISLEREF_COL;
    /**
     * The constant RULES_AISLEREF_TYPE.
     */
    public static final String RULES_AISLEREF_TYPE = SQLINTEGER;
    /**
     * The constant RULES_AISLEREF_PRIMARY_INDEX.
     */
    public static final boolean RULES_AISLEREF_PRIMARY_INDEX = false;
    /**
     * The constant RULESAISLEREFCOL.
     */
    public static final DBColumn RULESAISLEREFCOL = new DBColumn(RULES_AISLEREF_COL,
            RULES_AISLEREF_TYPE,
            RULES_AISLEREF_PRIMARY_INDEX,
            ""
    );


    /**
     * rulename - name of the rule
     */
    public static final String RULES_NAME_COL = "rulename";
    /**
     * The constant RULES_NAME_COL_FULL.
     */
    public static final String RULES_NAME_COL_FULL = RULES_TABLE +
            SQLPERIOD +
            RULES_NAME_COL;
    /**
     * The constant RULES_NAME_TYPE.
     */
    public static final String RULES_NAME_TYPE = SQLTEXT;
    /**
     * The constant RULES_NAME_PRIMARY_INDEX.
     */
    public static final boolean RULES_NAME_PRIMARY_INDEX = false;
    /**
     * The constant RULESNAMECOL.
     */
    public static final DBColumn RULESNAMECOL = new DBColumn(RULES_NAME_COL,
            RULES_NAME_TYPE,
            RULES_NAME_PRIMARY_INDEX,
            "");


    /**
     * ruleuses - number of times this rule has been used
     */
    public static final String RULES_USES_COL = "ruleuses";
    /**
     * The constant RULES_USES_COL_FULL.
     */
    public static final String RULES_USES_COL_FULL = RULES_TABLE +
            SQLPERIOD +
            RULES_USES_COL;
    /**
     * The constant RULES_USES_TYPE.
     */
    public static final String RULES_USES_TYPE = SQLINTEGER;
    /**
     * The constant RULES_USES_PRIMARY_INDEX.
     */
    public static final boolean RULES_USES_PRIMARY_INDEX = false;
    /**
     * The constant RULESUSESCOL.
     */
    public static final DBColumn RULESUSESCOL = new DBColumn(RULES_USES_COL,
            RULES_USES_TYPE,
            RULES_USES_PRIMARY_INDEX,
            "0");


    /**
     * ruleprompt - whether or not to prompt when rule is acted on
     */
    public static final String RULES_PROMPT_COL = "ruleprompt";
    /**
     * The constant RULES_PROMPT_COL_FULL.
     */
    public static final String RULES_PROMPT_COL_FULL = RULES_TABLE +
            SQLPERIOD +
            RULES_PROMPT_COL;
    /**
     * The constant RULES_PROMPT_TYPE.
     */
    public static final String RULES_PROMPT_TYPE = SQLINTEGER;
    /**
     * The constant RULES_PROMPT_PRIMARY_INDEX.
     */
    public static final boolean RULES_PROMPT_PRIMARY_INDEX = false;
    /**
     * The constant RULESPROMPTCOL.
     */
    public static final DBColumn RULESPROMPTCOL = new DBColumn(RULES_PROMPT_COL,
            RULES_PROMPT_TYPE,
            RULES_PROMPT_PRIMARY_INDEX,
            "0");

    /**
     * ruleacton - date rule is to be applied to the shopping list
     */
    public static final String RULES_ACTON_COL = "ruleacton";
    /**
     * The constant RULES_ACTOON_COL_FULL.
     */
    public static final String RULES_ACTOON_COL_FULL = RULES_TABLE +
            SQLPERIOD +
            RULES_ACTON_COL;
    /**
     * The constant RULES_ACTON_TYPE.
     */
    public static final String RULES_ACTON_TYPE = SQLINTEGER;
    /**
     * The constant RULES_ACTON_PRIMARY_INDEX.
     */
    public static final boolean RULES_ACTON_PRIMARY_INDEX = false;
    /**
     * The constant RULESACTONCOL.
     */
    public static final DBColumn RULESACTONCOL = new DBColumn(RULES_ACTON_COL,
            RULES_ACTON_TYPE,
            RULES_ACTON_PRIMARY_INDEX,
            "0");


    /**
     * ruleperiod - interval type
     * note includes period definitions
     */
    public static final String PERIOD_DAYS = "DAYS";
    /**
     * The constant PERIOD_DAYS_SINGULAR.
     */
    public static final String PERIOD_DAYS_SINGULAR = "DAY";
    /**
     * The constant PERIOD_DAYSASINT.
     */
    public static final int PERIOD_DAYSASINT = 0;
    /**
     * The constant PERIOD_WEEKS.
     */
    public static final String PERIOD_WEEKS = "WEEKS";
    /**
     * The constant PERIOD_WEEKS_SINGULAR.
     */
    public static final String PERIOD_WEEKS_SINGULAR = "WEEK";
    /**
     * The constant PERIOD_WEEKSASINT.
     */
    public static final int PERIOD_WEEKSASINT = 1;
    /**
     * The constant PERIOD_FORTNIGHTS.
     */
    public static final String PERIOD_FORTNIGHTS = "FORTNIGHTS";
    /**
     * The constant PERIOD_FORTNIGHTS_SINGULAR.
     */
    public static final String PERIOD_FORTNIGHTS_SINGULAR = "FORTNIGHT";
    /**
     * The constant PERIOD_FORTNIGHTSASINT.
     */
    public static final int PERIOD_FORTNIGHTSASINT = 2;
    /**
     * The constant PERIOD_MONTHS.
     */
    public static final String PERIOD_MONTHS = "MONTHS";
    /**
     * The constant PERIOD_MONTHS_SINGULAR.
     */
    public static final String PERIOD_MONTHS_SINGULAR = "MONTH";
    /**
     * The constant PERIOD_MONTHSASINT.
     */
    public static final int PERIOD_MONTHSASINT = 3;
    /**
     * The constant PERIOD_QUARTERS.
     */
    public static final String PERIOD_QUARTERS = "QUARTERS";
    /**
     * The constant PERIOD_QUARTERS_SINGULAR.
     */
    public static final String PERIOD_QUARTERS_SINGULAR = "QUARTER";
    /**
     * The constant PERIOD_QUARTERSASINT.
     */
    public static final int PERIOD_QUARTERSASINT = 4;
    /**
     * The constant PERIOD_YEARS.
     */
    public static final String PERIOD_YEARS = "YEARS";
    /**
     * The constant PERIOD_YEARS_SINGULAR.
     */
    public static final String PERIOD_YEARS_SINGULAR = "YEAR";
    /**
     * The constant PERIOD_YEARSASINT.
     */
    public static final int PERIOD_YEARSASINT = 5;

    /**
     * The constant RULES_PERIOD_COL.
     */
    public static final String RULES_PERIOD_COL = "ruleperiod";
    /**
     * The constant RULES_PERIOD_COL_FULL.
     */
    public static final String RULES_PERIOD_COL_FULL = RULES_TABLE +
            SQLPERIOD + RULES_PERIOD_COL;
    /**
     * The constant RULES_PERIOD_TYPE.
     */
    public static final String RULES_PERIOD_TYPE = SQLINTEGER;
    /**
     * The constant RULES_PERIOD_PRIMARY_INDEX.
     */
    public static final boolean RULES_PERIOD_PRIMARY_INDEX = false;
    /**
     * The constant RULESPERIODCOL.
     */
    public static final DBColumn RULESPERIODCOL = new DBColumn(RULES_PERIOD_COL,
            RULES_PERIOD_TYPE,
            RULES_PERIOD_PRIMARY_INDEX,
            "0");


    /**
     * rulemultiplier - number of ruleperiods per interval
     */
    public static final String RULES_MULTIPLIER_COL = "rulemultiplier";
    /**
     * The constant RULES_MULTIPLIER_COL_FULL.
     */
    public static final String RULES_MULTIPLIER_COL_FULL = RULES_TABLE +
            SQLPERIOD +
            RULES_MULTIPLIER_COL;
    /**
     * The constant RULES_MULTIPLIER_TYPE.
     */
    public static final String RULES_MULTIPLIER_TYPE = SQLINTEGER;
    /**
     * The constant RULES_MULTIPLIER_PRIMARY_INDEX.
     */
    public static final boolean RULES_MULTIPLIER_PRIMARY_INDEX = false;
    /**
     * The constant RULESMULTIPLIERCOL.
     */
    public static final DBColumn RULESMULTIPLIERCOL = new DBColumn(RULES_MULTIPLIER_COL,
            RULES_MULTIPLIER_TYPE,
            RULES_MULTIPLIER_PRIMARY_INDEX,
            "1");


    /**
     * The constant RULESCOLS.
     */
    public static final ArrayList<DBColumn> RULESCOLS = new ArrayList<>(
            Arrays.asList(
                    RULESIDCOL,
                    RULESPRODUCTREFCOL,
                    RULESAISLEREFCOL,
                    RULESNAMECOL,
                    RULESUSESCOL,
                    RULESPROMPTCOL,
                    RULESACTONCOL,
                    RULESPERIODCOL,
                    RULESMULTIPLIERCOL
            )
    );
    /**
     * The constant RULESTABLE.
     */
    public static final DBTable RULESTABLE = new DBTable(RULES_TABLE,RULESCOLS);

    /*
    * Index for aisleref
     */
    public static final DBIndex RULESAISLEREFINDEX = new DBIndex(
            RULES_TABLE + RULES_AISLEREF_COL+ "index",
            RULESTABLE,
            RULESAISLEREFCOL,
            true,
            false
    );
}
