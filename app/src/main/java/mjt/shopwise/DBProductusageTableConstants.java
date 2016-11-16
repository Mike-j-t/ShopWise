package mjt.shopwise;

import java.util.ArrayList;
import java.util.Arrays;

import static mjt.shopwise.DBConstants.DEFAULTORDER;
import static mjt.shopwise.DBConstants.INT;
import static mjt.shopwise.DBConstants.PERIOD;
import static mjt.shopwise.DBConstants.REAL;

/**
 * ProductUsage Table and Columns
 * A productusage is when a product has been assigned (stocked) to an aisle
 * and thus the product is purchasable. Distinct from a product entry which is
 * basically the name of the product.
 */

public class DBProductusageTableConstants {
    /**
     * productusage table - Note many to many between aisles and products
     */
    public static final String PRODUCTUSAGE_TABLE = "productusage";

    /**
     * productusageproductref - reference to the product being used.
     */
    public static final String PRODUCTUSAGE_PRODUCTREF_COL = "productusageproductref";
    public static final String PRODUCTUSAGE_PRODUCTREF_COL_FULL = PRODUCTUSAGE_TABLE +
            PERIOD +
            PRODUCTUSAGE_PRODUCTREF_COL;
    public static final String PRODUCTUSAGE_PRODUCTREF_TYPE = INT;
    public static final boolean PRODUCTUSAGE_PRODUCTREF_PRIMARY_INDEX = true;
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
    public static final String PRODUCTUSAGE_AISLEREF_COL_FULL = PRODUCTUSAGE_TABLE +
            PERIOD +
            PRODUCTUSAGE_AISLEREF_COL;
    public static final String PRODUCTUSAGE_AISLEREF_TYPE = INT;
    public static final boolean PRODUCTUSAGE_AISLEREF_PRIMARY_INDEX = true;
    public static final DBColumn PRODUCTUSAGEAISLEREFCOL = new DBColumn(PRODUCTUSAGE_AISLEREF_COL,
            PRODUCTUSAGE_AISLEREF_TYPE,
            PRODUCTUSAGE_AISLEREF_PRIMARY_INDEX,
            ""
    );
    /**
     * productusagecost - cost of the product unique to this assignment
     */
    public static final String PRODUCTUSAGE_COST_COL = "productusagecost";
    public static final String PRODUCTUSAGE_COST_FULL = PRODUCTUSAGE_TABLE +
            PERIOD +
            PRODUCTUSAGE_COST_COL;
    public static final String PRODUCTUSAGE_COST_TYPE = REAL;
    public static final boolean PRODUCTUSAGE_COST_PRIMARY_INDEX = false;
    public static final DBColumn PRODUCTUSAGECOSTCOL = new DBColumn(PRODUCTUSAGE_COST_COL,
            PRODUCTUSAGE_COST_TYPE,
            PRODUCTUSAGE_COST_PRIMARY_INDEX,
            "0"
    );

    /**
     * productusage buycount - number purchases
     */
    public static final String PRODUCTUSAGE_BUYCOUNT_COL = "productusagebuycount";
    public static final String PRODUCTUSAGE_BUYCOUNT_COL_FULL = PRODUCTUSAGE_TABLE +
            PERIOD +
            PRODUCTUSAGE_BUYCOUNT_COL;
    public static final String PRODUCTUSAGE_BUYCOUNT_TYPE = INT;
    public static final boolean PRODUCTUSAGE_BUYCOUNT_PRIMARY_INDEX = false;
    public static final DBColumn PRODUCTUSAGEBUYCOUNTCOL = new DBColumn(PRODUCTUSAGE_BUYCOUNT_COL,
            PRODUCTUSAGE_BUYCOUNT_TYPE,
            PRODUCTUSAGE_BUYCOUNT_PRIMARY_INDEX,
            "0");

    /**
     * productusagefirstbuydate - date first purchased
     */
    public static final String PRODUCTUSAGE_FIRSTBUYDATE_COL = "productusagefirstbuydate";
    public static final String PRODUCTUSAGE_FIRSTBUYDATE_COL_FULL = PRODUCTUSAGE_TABLE +
            PERIOD +
            PRODUCTUSAGE_FIRSTBUYDATE_COL;
    public static final String PRODUCTUSAGE_FIRSTBUYDATE_TYPE = INT;
    public static final boolean PRODUCTUSAGE_FIRSTBUYDATE_PRIMARY_INDEX = false;
    public static final DBColumn PRODUCTUSAGEFIRSTBUYDATECOL = new DBColumn(PRODUCTUSAGE_FIRSTBUYDATE_COL,
            PRODUCTUSAGE_FIRSTBUYDATE_TYPE,
            PRODUCTUSAGE_FIRSTBUYDATE_PRIMARY_INDEX,
            "0");

    /**
     * productusagelatestbuydate - date last purchased
     */
    public static final String PRODUCTUSAGE_LATESTBUYDATE_COL = "productusagelatestbuydate";
    public static final String PRODUCTUSAGE_LATESTBUYDATE_COL_FULL = PRODUCTUSAGE_TABLE +
            PERIOD +
            PRODUCTUSAGE_LATESTBUYDATE_COL;
    public static final String PRODUCTUSAGE_LATESTBUYDATE_TYPE = INT;
    public static final boolean PRODUCTUSAGE_LATESTBUYDATE_PRIMARY_INDEX = false;
    public static final DBColumn PRODUCTUSAGESLATESTBUYDATECOL = new DBColumn(PRODUCTUSAGE_LATESTBUYDATE_COL,
            PRODUCTUSAGE_LATESTBUYDATE_TYPE,
            PRODUCTUSAGE_LATESTBUYDATE_PRIMARY_INDEX,
            "0");

    /**
     * productusageorder - order in the aisle
     */
    public static final String PRODUCTUSAGE_ORDER_COL = "productusageorder";
    public static final String PRODUCTUSAGE_ORDER_COL_FULL = PRODUCTUSAGE_TABLE +
            PERIOD +
            PRODUCTUSAGE_ORDER_COL;
    public static final String PRODUCTUSAGE_ORDER_TYPE = INT;
    public static final boolean PRODUCTUSAGE_ORDER_PRIMARY_INDEX = false;
    public static final DBColumn PRODUCTUSAGEORDERCOL = new DBColumn(PRODUCTUSAGE_ORDER_COL,
            PRODUCTUSAGE_ORDER_TYPE,
            PRODUCTUSAGE_ORDER_PRIMARY_INDEX,
            DEFAULTORDER);

    /**
     * productusagerulesuggestflag - flag for rule suggestions
     */
    public static final String PRODUCTUSAGE_RULESUGGESTFLAG_COL = "productusagerulesuggestflag";
    public static final String PRODUCTUSAGE_RULESUGGESTFLAG_COL_FULL = PRODUCTUSAGE_TABLE +
            PERIOD +
            PRODUCTUSAGE_RULESUGGESTFLAG_COL;
    public static final String PRODUCTUSAGE_RULESUGGESTFLAG_TYPE = INT;
    public static final boolean PRODUCTUSAGE_RULESUGGESTFLAG_PRIMARY_INDEX = false;
    public static final DBColumn PRODUCTUSAGERULESUGGESTFLAGCOL = new DBColumn(PRODUCTUSAGE_RULESUGGESTFLAG_COL,
            PRODUCTUSAGE_RULESUGGESTFLAG_TYPE,
            PRODUCTUSAGE_RULESUGGESTFLAG_PRIMARY_INDEX,
            "0");

    public static final ArrayList<DBColumn> PRODUCTUSAGECOLS = new ArrayList<>(Arrays.asList(PRODUCTUSAGEPRODUCTREFCOL,
            PRODUCTUSAGEAISLEREFCOL,
            PRODUCTUSAGECOSTCOL,
            PRODUCTUSAGEBUYCOUNTCOL,
            PRODUCTUSAGEFIRSTBUYDATECOL,
            PRODUCTUSAGESLATESTBUYDATECOL,
            PRODUCTUSAGEORDERCOL,
            PRODUCTUSAGERULESUGGESTFLAGCOL));
    public static final DBTable PRODUCTUSAGETABLE = new DBTable(PRODUCTUSAGE_TABLE,
            PRODUCTUSAGECOLS);
}
