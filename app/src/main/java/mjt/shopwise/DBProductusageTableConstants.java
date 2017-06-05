package mjt.shopwise;

import java.util.ArrayList;
import java.util.Arrays;

import mjt.dbcolumn.DBColumn;
import mjt.dbtable.DBTable;

import static mjt.shopwise.DBConstants.DEFAULTORDER;
import static mjt.sqlwords.SQLKWORD.*;

/**
 * ProductUsage Table and Columns
 * A productusage is when a product has been assigned (stocked) to an aisle
 * and thus the product is purchasable. Distinct from a product entry which is
 * basically the name of the product.
 * <p>
 * productusageproductref       reference to the product
 * productusageaisleref         reference to the aisle
 * productusagecost             cost of the product
 * productusagebuycount         number purchased over time
 * productusagefirstbuydate     first time purchased
 * productusagelatestbuydate    last time the product was purchased
 * productusageorder            order of product within the aisle
 * productusagerulesuggestflag  flag used by rule suggestion to allow use,
 *                                  temporarily disable (skip) or permanently
 *                                  disable (can be reveresed) Rule Suggestion
 *                                  for this ProductUsage.
 * productusagechecklistflag    flag to indicate if this productusage should
 *                                  appear in the checlist.
 * productusagechecklistcount   The number of items to check for in the
 *                                  checklist.
 */
@SuppressWarnings("WeakerAccess")
public class DBProductusageTableConstants {
    /**
     * productusage table - Note many to many between aisles and products
     */
    public static final String PRODUCTUSAGE_TABLE = "productusage";
    /**
     * The constant RULESUGGESTFLAG_CLEAR.
     */
    public static final int RULESUGGESTFLAG_CLEAR = 0;
    /**
     * The constant RULESUGGESTFLAG_SKIP.
     */
    public static final int RULESUGGESTFLAG_SKIP = 1;
    /**
     * The constant RULESUGGESTFLAG_DISABLE.
     */
    public static final int RULESUGGESTFLAG_DISABLE = 2;

    /**
     * productusageproductref - reference to the product being used.
     */
    public static final String PRODUCTUSAGE_PRODUCTREF_COL = "productusageproductref";
    /**
     * The constant PRODUCTUSAGE_PRODUCTREF_COL_FULL.
     */
    public static final String PRODUCTUSAGE_PRODUCTREF_COL_FULL = PRODUCTUSAGE_TABLE +
            SQLPERIOD +
            PRODUCTUSAGE_PRODUCTREF_COL;
    /**
     * The constant PRODUCTUSAGE_PRODUCTREF_TYPE.
     */
    public static final String PRODUCTUSAGE_PRODUCTREF_TYPE = SQLINTEGER;
    /**
     * The constant PRODUCTUSAGE_PRODUCTREF_PRIMARY_INDEX.
     */
    public static final boolean PRODUCTUSAGE_PRODUCTREF_PRIMARY_INDEX = true;
    /**
     * The constant PRODUCTUSAGEPRODUCTREFCOL.
     */
    public static final DBColumn PRODUCTUSAGEPRODUCTREFCOL = new DBColumn(PRODUCTUSAGE_PRODUCTREF_COL,
            PRODUCTUSAGE_PRODUCTREF_TYPE,
            PRODUCTUSAGE_PRODUCTREF_PRIMARY_INDEX,
            ""
    );


    /**
     * productusageaisleref - reference to the aisle where the product is
     * assigned.
     */
    public static final String PRODUCTUSAGE_AISLEREF_COL = "productusageaisleref";
    /**
     * The constant PRODUCTUSAGE_AISLEREF_COL_FULL.
     */
    public static final String PRODUCTUSAGE_AISLEREF_COL_FULL = PRODUCTUSAGE_TABLE +
            SQLPERIOD +
            PRODUCTUSAGE_AISLEREF_COL;
    /**
     * The constant PRODUCTUSAGE_AISLEREF_TYPE.
     */
    public static final String PRODUCTUSAGE_AISLEREF_TYPE = SQLINTEGER;
    /**
     * The constant PRODUCTUSAGE_AISLEREF_PRIMARY_INDEX.
     */
    public static final boolean PRODUCTUSAGE_AISLEREF_PRIMARY_INDEX = true;
    /**
     * The constant PRODUCTUSAGEAISLEREFCOL.
     */
    public static final DBColumn PRODUCTUSAGEAISLEREFCOL = new DBColumn(PRODUCTUSAGE_AISLEREF_COL,
            PRODUCTUSAGE_AISLEREF_TYPE,
            PRODUCTUSAGE_AISLEREF_PRIMARY_INDEX,
            ""
    );
    /**
     * productusagecost - cost of the product unique to this assignment
     */
    public static final String PRODUCTUSAGE_COST_COL = "productusagecost";
    /**
     * The constant PRODUCTUSAGE_COST_FULL.
     */
    public static final String PRODUCTUSAGE_COST_FULL = PRODUCTUSAGE_TABLE +
            SQLPERIOD +
            PRODUCTUSAGE_COST_COL;
    /**
     * The constant PRODUCTUSAGE_COST_TYPE.
     */
    public static final String PRODUCTUSAGE_COST_TYPE = SQLREAL;
    /**
     * The constant PRODUCTUSAGE_COST_PRIMARY_INDEX.
     */
    public static final boolean PRODUCTUSAGE_COST_PRIMARY_INDEX = false;
    /**
     * The constant PRODUCTUSAGECOSTCOL.
     */
    public static final DBColumn PRODUCTUSAGECOSTCOL = new DBColumn(PRODUCTUSAGE_COST_COL,
            PRODUCTUSAGE_COST_TYPE,
            PRODUCTUSAGE_COST_PRIMARY_INDEX,
            "0"
    );


    /**
     * productusage buycount - number purchases
     */
    public static final String PRODUCTUSAGE_BUYCOUNT_COL = "productusagebuycount";
    /**
     * The constant PRODUCTUSAGE_BUYCOUNT_COL_FULL.
     */
    public static final String PRODUCTUSAGE_BUYCOUNT_COL_FULL = PRODUCTUSAGE_TABLE +
            SQLPERIOD +
            PRODUCTUSAGE_BUYCOUNT_COL;
    /**
     * The constant PRODUCTUSAGE_BUYCOUNT_TYPE.
     */
    public static final String PRODUCTUSAGE_BUYCOUNT_TYPE = SQLINTEGER;
    /**
     * The constant PRODUCTUSAGE_BUYCOUNT_PRIMARY_INDEX.
     */
    public static final boolean PRODUCTUSAGE_BUYCOUNT_PRIMARY_INDEX = false;
    /**
     * The constant PRODUCTUSAGEBUYCOUNTCOL.
     */
    public static final DBColumn PRODUCTUSAGEBUYCOUNTCOL = new DBColumn(PRODUCTUSAGE_BUYCOUNT_COL,
            PRODUCTUSAGE_BUYCOUNT_TYPE,
            PRODUCTUSAGE_BUYCOUNT_PRIMARY_INDEX,
            "0");


    /**
     * productusagefirstbuydate - date first purchased
     */
    public static final String PRODUCTUSAGE_FIRSTBUYDATE_COL = "productusagefirstbuydate";
    /**
     * The constant PRODUCTUSAGE_FIRSTBUYDATE_COL_FULL.
     */
    public static final String PRODUCTUSAGE_FIRSTBUYDATE_COL_FULL = PRODUCTUSAGE_TABLE +
            SQLPERIOD +
            PRODUCTUSAGE_FIRSTBUYDATE_COL;
    /**
     * The constant PRODUCTUSAGE_FIRSTBUYDATE_TYPE.
     */
    public static final String PRODUCTUSAGE_FIRSTBUYDATE_TYPE = SQLINTEGER;
    /**
     * The constant PRODUCTUSAGE_FIRSTBUYDATE_PRIMARY_INDEX.
     */
    public static final boolean PRODUCTUSAGE_FIRSTBUYDATE_PRIMARY_INDEX = false;
    /**
     * The constant PRODUCTUSAGEFIRSTBUYDATECOL.
     */
    public static final DBColumn PRODUCTUSAGEFIRSTBUYDATECOL = new DBColumn(PRODUCTUSAGE_FIRSTBUYDATE_COL,
            PRODUCTUSAGE_FIRSTBUYDATE_TYPE,
            PRODUCTUSAGE_FIRSTBUYDATE_PRIMARY_INDEX,
            "0");


    /**
     * productusagelatestbuydate - date last purchased
     */
    public static final String PRODUCTUSAGE_LATESTBUYDATE_COL = "productusagelatestbuydate";
    /**
     * The constant PRODUCTUSAGE_LATESTBUYDATE_COL_FULL.
     */
    public static final String PRODUCTUSAGE_LATESTBUYDATE_COL_FULL = PRODUCTUSAGE_TABLE +
            SQLPERIOD +
            PRODUCTUSAGE_LATESTBUYDATE_COL;
    /**
     * The constant PRODUCTUSAGE_LATESTBUYDATE_TYPE.
     */
    public static final String PRODUCTUSAGE_LATESTBUYDATE_TYPE = SQLINTEGER;
    /**
     * The constant PRODUCTUSAGE_LATESTBUYDATE_PRIMARY_INDEX.
     */
    public static final boolean PRODUCTUSAGE_LATESTBUYDATE_PRIMARY_INDEX = false;
    /**
     * The constant PRODUCTUSAGESLATESTBUYDATECOL.
     */
    public static final DBColumn PRODUCTUSAGESLATESTBUYDATECOL = new DBColumn(PRODUCTUSAGE_LATESTBUYDATE_COL,
            PRODUCTUSAGE_LATESTBUYDATE_TYPE,
            PRODUCTUSAGE_LATESTBUYDATE_PRIMARY_INDEX,
            "0");


    /**
     * productusageorder - order in the aisle
     */
    public static final String PRODUCTUSAGE_ORDER_COL = "productusageorder";
    /**
     * The constant PRODUCTUSAGE_ORDER_COL_FULL.
     */
    public static final String PRODUCTUSAGE_ORDER_COL_FULL = PRODUCTUSAGE_TABLE +
            SQLPERIOD +
            PRODUCTUSAGE_ORDER_COL;
    /**
     * The constant PRODUCTUSAGE_ORDER_TYPE.
     */
    public static final String PRODUCTUSAGE_ORDER_TYPE = SQLINTEGER;
    /**
     * The constant PRODUCTUSAGE_ORDER_PRIMARY_INDEX.
     */
    public static final boolean PRODUCTUSAGE_ORDER_PRIMARY_INDEX = false;
    /**
     * The constant PRODUCTUSAGEORDERCOL.
     */
    public static final DBColumn PRODUCTUSAGEORDERCOL = new DBColumn(PRODUCTUSAGE_ORDER_COL,
            PRODUCTUSAGE_ORDER_TYPE,
            PRODUCTUSAGE_ORDER_PRIMARY_INDEX,
            DEFAULTORDER);


    /**
     * productusagerulesuggestflag - flag for rule suggestions
     */
    public static final String PRODUCTUSAGE_RULESUGGESTFLAG_COL = "productusagerulesuggestflag";
    /**
     * The constant PRODUCTUSAGE_RULESUGGESTFLAG_COL_FULL.
     */
    public static final String PRODUCTUSAGE_RULESUGGESTFLAG_COL_FULL = PRODUCTUSAGE_TABLE +
            SQLPERIOD +
            PRODUCTUSAGE_RULESUGGESTFLAG_COL;
    /**
     * The constant PRODUCTUSAGE_RULESUGGESTFLAG_TYPE.
     */
    public static final String PRODUCTUSAGE_RULESUGGESTFLAG_TYPE = SQLINTEGER;
    /**
     * The constant PRODUCTUSAGE_RULESUGGESTFLAG_PRIMARY_INDEX.
     */
    public static final boolean PRODUCTUSAGE_RULESUGGESTFLAG_PRIMARY_INDEX = false;
    /**
     * The constant PRODUCTUSAGERULESUGGESTFLAGCOL.
     */
    public static final DBColumn PRODUCTUSAGERULESUGGESTFLAGCOL = new DBColumn(PRODUCTUSAGE_RULESUGGESTFLAG_COL,
            PRODUCTUSAGE_RULESUGGESTFLAG_TYPE,
            PRODUCTUSAGE_RULESUGGESTFLAG_PRIMARY_INDEX,
            "0");


    public static final String PRODUCTUSAGE_CHECKLISTFLAG_COL = "productusagechecklistflag";
    public static final String PRODUCTUSAGE_CHECKLISTFLAG_COL_FULL =
            PRODUCTUSAGE_TABLE + SQLPERIOD + PRODUCTUSAGE_CHECKLISTFLAG_COL;
    public static final String PRODUCTUSAGE_CHECKLISTFLAG_TYPE = SQLINTEGER;
    public static final boolean PRODUCTUSAGE_CHECKLISTFLAG_PRIMARY_INDEX = false;
    public static final DBColumn PRODUCTUSAGECHECKLISTFLAGCOL = new DBColumn(
            PRODUCTUSAGE_CHECKLISTFLAG_COL,
            PRODUCTUSAGE_CHECKLISTFLAG_TYPE,
            PRODUCTUSAGE_CHECKLISTFLAG_PRIMARY_INDEX,
            "0");


    public static final String PRODUCTUSAGE_CHECKLISTCOUNT_COL = "productusagechecklistcount";
    public static final String PRODUCTUSAGE_CHECKLISTCOUNT_COL_FULL =
            PRODUCTUSAGE_TABLE + SQLPERIOD + PRODUCTUSAGE_CHECKLISTCOUNT_COL;
    public static final String PRODUCTUSAGE_CHECKLISTCOUNT_TYPE = SQLINTEGER;
    public static final boolean PRODUCTUSAGE_CHECKLISTCOUNT_PRIMARY_INDEX = false;
    public static final DBColumn PRODUCTUSAGECHECKLISTCOUNTCOL = new DBColumn(
            PRODUCTUSAGE_CHECKLISTCOUNT_COL,
            PRODUCTUSAGE_CHECKLISTCOUNT_TYPE,
            PRODUCTUSAGE_CHECKLISTCOUNT_PRIMARY_INDEX,
            "1");


    /**
     * The constant PRODUCTUSAGECOLS.
     */
    public static final ArrayList<DBColumn> PRODUCTUSAGECOLS = new ArrayList<>(Arrays.asList(
            PRODUCTUSAGEPRODUCTREFCOL,
            PRODUCTUSAGEAISLEREFCOL,
            PRODUCTUSAGECOSTCOL,
            PRODUCTUSAGEBUYCOUNTCOL,
            PRODUCTUSAGEFIRSTBUYDATECOL,
            PRODUCTUSAGESLATESTBUYDATECOL,
            PRODUCTUSAGEORDERCOL,
            PRODUCTUSAGERULESUGGESTFLAGCOL,
            PRODUCTUSAGECHECKLISTFLAGCOL,
            PRODUCTUSAGECHECKLISTCOUNTCOL));
    /**
     * The constant PRODUCTUSAGETABLE.
     */
    public static final DBTable PRODUCTUSAGETABLE = new DBTable(PRODUCTUSAGE_TABLE,
            PRODUCTUSAGECOLS);
    public static final String PRODUCTUSAGEMAXORDERCOLUMN = "maxorder";
}
