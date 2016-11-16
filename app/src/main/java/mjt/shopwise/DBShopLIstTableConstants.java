package mjt.shopwise;

import java.util.ArrayList;
import java.util.Arrays;

import static mjt.shopwise.DBConstants.IDTYPE;
import static mjt.shopwise.DBConstants.INT;
import static mjt.shopwise.DBConstants.PERIOD;
import static mjt.shopwise.DBConstants.REAL;
import static mjt.shopwise.DBConstants.STD_ID;

/**
 * Shoplist table - List of products to be purchased or have been purchased
 */

public class DBShopListTableConstants {

    /**
     * shoplist table
     */
    public static final String SHOPLIST_TABLE = "shoplist";

    /**
     * _id (aka shoplist_id) - index/primary key
     */
    public static final String SHOPLIST_ID_COL = STD_ID;
    public static final String SHOPLIST_ID_COL_FULL = SHOPLIST_TABLE +
            PERIOD +
            SHOPLIST_ID_COL;
    public static final String SHOPLIST_ALTID_COL = SHOPLIST_TABLE +
            SHOPLIST_ID_COL;
    public static final String SHOPLIST_ALTID_COL_FULL = SHOPLIST_TABLE +
            PERIOD +
            SHOPLIST_ALTID_COL;
    public static final String SHOPLIST_ID_TYPE = IDTYPE;
    public static final boolean SHOPLIST_ID_PRIMARY_INDEX = true;
    public static final DBColumn SHOPLISTIDCOL = new DBColumn(SHOPLIST_ID_COL,
            SHOPLIST_ID_TYPE,
            SHOPLIST_ID_PRIMARY_INDEX,
            "");

    /**
     * shoplistproductref - reference to the product and thus part reference
     * to the productusage (with shoplistaisleref)
     */
    public static final String SHOPLIST_PRODUCTREF_COL = "shoplistproductref";
    public static final String SHOPLIST_PRODUCTREF_COL_FULL = SHOPLIST_TABLE +
            PERIOD +
            SHOPLIST_PRODUCTREF_COL;
    public static final String SHOPLIST_PRODUCTREF_TYPE = INT;
    public static final boolean SHOPLIST_PRODUCTREF_PRIMARY_INDEX = false;
    public static final DBColumn SHOPLISTPRODUCTREFCOL = new DBColumn(SHOPLIST_PRODUCTREF_COL,
            SHOPLIST_PRODUCTREF_TYPE,
            SHOPLIST_PRODUCTREF_PRIMARY_INDEX,
            "");

    /**
     * shoplistaisleref reference to the aisle and thus part of the reference
     * to the productusage (with shoplistproductref
     */
    public static final String SHOPLIST_AISLEREF_COL = "shoplistaisleref";
    public static final String SHOPLIST_AISLEREF_COL_FULL = SHOPLIST_TABLE +
            PERIOD +
            SHOPLIST_AISLEREF_COL;
    public static final String SHOPLIST_AISLEREF_TYPE = INT;
    public static final boolean SHOPLIST_AISLEREF_PRIMARY_INDEX = false;
    public static final DBColumn SHOPLISTAISLEREFCOL = new DBColumn(SHOPLIST_AISLEREF_COL,
            SHOPLIST_AISLEREF_TYPE,
            SHOPLIST_AISLEREF_PRIMARY_INDEX,
            "");

    /**
     * shoplistdateadded - date this entry was added to the shopping list
     */
    public static final String SHOPLIST_DATEADDED_COL = "shoplistdateadded";
    public static final String SHOPLIST_DATEADDED_COL_FULL = SHOPLIST_TABLE +
            PERIOD +
            SHOPLIST_DATEADDED_COL;
    public static final String SHOPLIST_DATEADDED_TYPE = INT;
    public static final boolean SHOPLIST_DATEADDED_PRIMARY_INDEX = false;
    public static final DBColumn SHOPLISTDATEADDEDCOL = new DBColumn(SHOPLIST_DATEADDED_COL,
            SHOPLIST_DATEADDED_TYPE,
            SHOPLIST_DATEADDED_PRIMARY_INDEX,
            "0");

    /**
     * shoplistnumbertoget - number to purchase
     */
    public static final String SHOPLIST_NUMBERTOGET_COL = "shoplistnumbertoget";
    public static final String SHOPLIST_NUMBERTOGET_COL_FULL = SHOPLIST_TABLE +
            PERIOD +
            SHOPLIST_NUMBERTOGET_COL;
    public static final String SHOPLIST_NUMBERTOGET_TYPE = INT;
    public static final boolean SHOPLIST_NUMBERTOGET_PRIMARY_INDEX = false;
    public static final DBColumn SHOPLISTNUMBERTOGETCOL = new DBColumn(SHOPLIST_NUMBERTOGET_COL,
            SHOPLIST_NUMBERTOGET_TYPE,
            SHOPLIST_NUMBERTOGET_PRIMARY_INDEX,
            "0");

    /**
     * shoplistdone - boolean 0 if not done, 1 if done (purchased)
     */
    public static final String SHOPLIST_DONE_COL = "shoplistdone";
    public static final String SHOPLIST_DONE_COL_FULL = SHOPLIST_TABLE +
            PERIOD +
            SHOPLIST_DONE_COL;
    public static final String SHOPLIST_DONE_TYPE = INT;
    public static final boolean SHOPLIST_DONE_PRIMARY_INDEX = false;
    public static final DBColumn SHOPLISTDONECOL = new DBColumn(SHOPLIST_DONE_COL,
            SHOPLIST_DONE_TYPE,
            SHOPLIST_DONE_PRIMARY_INDEX,
            "0");

    /**
     * shoplistdategot - date purchased
     */
    public static final String SHOPLIST_DATEGOT_COL = "shoplistdategot";
    public static final String SHOPLIST_DATEGOT_COL_FULL = SHOPLIST_TABLE +
            PERIOD +
            SHOPLIST_DATEGOT_COL;
    public static final String SHOPLIST_DATEGOT_TYPE = INT;
    public static final boolean SHOPLIST_DATEGOT_PRIMARY_INDEX = false;
    public static final DBColumn SHOPLISTDATEGOTCOL = new DBColumn(SHOPLIST_DATEGOT_COL,
            SHOPLIST_DATEGOT_TYPE,
            SHOPLIST_DATEGOT_PRIMARY_INDEX,
            "0");

    /**
     * shoplistcost - purchase cost
     */
    public static final String SHOPLIST_COST_COL = "shoplistcost";
    public static final String SHOPLIST_COST_COL_FULL = SHOPLIST_TABLE +
            PERIOD +
            SHOPLIST_COST_COL;
    public static final String SHOPLIST_COST_TYPE = REAL;
    public static final boolean SHOPLIST_COST_PRIMARY_INDEX = false;
    public static final DBColumn SHOPLISTCOSTCOL = new DBColumn(SHOPLIST_COST_COL,
            SHOPLIST_COST_TYPE,
            SHOPLIST_COST_PRIMARY_INDEX,
            "0");

    public static final ArrayList<DBColumn> SHOPLISTCOLS = new ArrayList<>(Arrays.asList(SHOPLISTIDCOL,
            SHOPLISTPRODUCTREFCOL,
            SHOPLISTAISLEREFCOL,
            SHOPLISTDATEADDEDCOL,
            SHOPLISTNUMBERTOGETCOL,
            SHOPLISTDONECOL,
            SHOPLISTDATEGOTCOL,
            SHOPLISTCOSTCOL));
    public static final DBTable SHOPLISTTABLE = new DBTable( SHOPLIST_TABLE,SHOPLISTCOLS);
}
