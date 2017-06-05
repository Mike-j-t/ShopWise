package mjt.shopwise;

import java.util.ArrayList;
import java.util.Arrays;

import mjt.dbcolumn.DBColumn;
import mjt.dbindex.DBIndex;
import mjt.dbtable.DBTable;

import static mjt.sqlwords.SQLKWORD.*;

/**
 * Shoplist table - List of products to be purchased or have been purchased
 */
@SuppressWarnings("WeakerAccess")
public class DBShopListTableConstants {

    /**
     * shoplist table
     */
    public static final String SHOPLIST_TABLE = "shoplist";

    /**
     * _id (aka shoplist_id) - index/primary key
     */
    public static final String SHOPLIST_ID_COL = SQLSTD_ID;
    /**
     * The constant SHOPLIST_ID_COL_FULL.
     */
    public static final String SHOPLIST_ID_COL_FULL = SHOPLIST_TABLE +
            SQLPERIOD +
            SHOPLIST_ID_COL;
    /**
     * The constant SHOPLIST_ALTID_COL.
     */
    public static final String SHOPLIST_ALTID_COL = SHOPLIST_TABLE +
            SHOPLIST_ID_COL;
    /**
     * The constant SHOPLIST_ALTID_COL_FULL.
     */
    @SuppressWarnings("unused")
    public static final String SHOPLIST_ALTID_COL_FULL = SHOPLIST_TABLE +
            SQLPERIOD +
            SHOPLIST_ALTID_COL;
    /**
     * The constant SHOPLISTIDCOL.
     */
    public static final DBColumn SHOPLISTIDCOL = new DBColumn(true);

    /**
     * shoplistproductref - reference to the product and thus part reference
     * to the productusage (with shoplistaisleref)
     */
    public static final String SHOPLIST_PRODUCTREF_COL = "shoplistproductref";
    /**
     * The constant SHOPLIST_PRODUCTREF_COL_FULL.
     */
    public static final String SHOPLIST_PRODUCTREF_COL_FULL = SHOPLIST_TABLE +
            SQLPERIOD +
            SHOPLIST_PRODUCTREF_COL;
    /**
     * The constant SHOPLIST_PRODUCTREF_TYPE.
     */
    public static final String SHOPLIST_PRODUCTREF_TYPE = SQLINTEGER;
    /**
     * The constant SHOPLIST_PRODUCTREF_PRIMARY_INDEX.
     */
    public static final boolean SHOPLIST_PRODUCTREF_PRIMARY_INDEX = false;
    /**
     * The constant SHOPLISTPRODUCTREFCOL.
     */
    public static final DBColumn SHOPLISTPRODUCTREFCOL = new DBColumn(SHOPLIST_PRODUCTREF_COL,
            SHOPLIST_PRODUCTREF_TYPE,
            SHOPLIST_PRODUCTREF_PRIMARY_INDEX,
            "");


    /**
     * shoplistaisleref reference to the aisle and thus part of the reference
     * to the productusage (with shoplistproductref
     */
    public static final String SHOPLIST_AISLEREF_COL = "shoplistaisleref";
    /**
     * The constant SHOPLIST_AISLEREF_COL_FULL.
     */
    public static final String SHOPLIST_AISLEREF_COL_FULL = SHOPLIST_TABLE +
            SQLPERIOD +
            SHOPLIST_AISLEREF_COL;
    /**
     * The constant SHOPLIST_AISLEREF_TYPE.
     */
    public static final String SHOPLIST_AISLEREF_TYPE = SQLINTEGER;
    /**
     * The constant SHOPLIST_AISLEREF_PRIMARY_INDEX.
     */
    public static final boolean SHOPLIST_AISLEREF_PRIMARY_INDEX = false;
    /**
     * The constant SHOPLISTAISLEREFCOL.
     */
    public static final DBColumn SHOPLISTAISLEREFCOL = new DBColumn(SHOPLIST_AISLEREF_COL,
            SHOPLIST_AISLEREF_TYPE,
            SHOPLIST_AISLEREF_PRIMARY_INDEX,
            "");


    /**
     * shoplistdateadded - date this entry was added to the shopping list
     */
    public static final String SHOPLIST_DATEADDED_COL = "shoplistdateadded";
    /**
     * The constant SHOPLIST_DATEADDED_COL_FULL.
     */
    public static final String SHOPLIST_DATEADDED_COL_FULL = SHOPLIST_TABLE +
            SQLPERIOD +
            SHOPLIST_DATEADDED_COL;
    /**
     * The constant SHOPLIST_DATEADDED_TYPE.
     */
    public static final String SHOPLIST_DATEADDED_TYPE = SQLINTEGER;
    /**
     * The constant SHOPLIST_DATEADDED_PRIMARY_INDEX.
     */
    public static final boolean SHOPLIST_DATEADDED_PRIMARY_INDEX = false;
    /**
     * The constant SHOPLISTDATEADDEDCOL.
     */
    public static final DBColumn SHOPLISTDATEADDEDCOL = new DBColumn(SHOPLIST_DATEADDED_COL,
            SHOPLIST_DATEADDED_TYPE,
            SHOPLIST_DATEADDED_PRIMARY_INDEX,
            "0");


    /**
     * shoplistnumbertoget - number to purchase
     */
    public static final String SHOPLIST_NUMBERTOGET_COL = "shoplistnumbertoget";
    /**
     * The constant SHOPLIST_NUMBERTOGET_COL_FULL.
     */
    public static final String SHOPLIST_NUMBERTOGET_COL_FULL = SHOPLIST_TABLE +
            SQLPERIOD +
            SHOPLIST_NUMBERTOGET_COL;
    /**
     * The constant SHOPLIST_NUMBERTOGET_TYPE.
     */
    public static final String SHOPLIST_NUMBERTOGET_TYPE = SQLINTEGER;
    /**
     * The constant SHOPLIST_NUMBERTOGET_PRIMARY_INDEX.
     */
    public static final boolean SHOPLIST_NUMBERTOGET_PRIMARY_INDEX = false;
    /**
     * The constant SHOPLISTNUMBERTOGETCOL.
     */
    public static final DBColumn SHOPLISTNUMBERTOGETCOL = new DBColumn(SHOPLIST_NUMBERTOGET_COL,
            SHOPLIST_NUMBERTOGET_TYPE,
            SHOPLIST_NUMBERTOGET_PRIMARY_INDEX,
            "0");


    /**
     * shoplistdone - boolean 0 if not done, 1 if done (purchased)
     */
    public static final String SHOPLIST_DONE_COL = "shoplistdone";
    /**
     * The constant SHOPLIST_DONE_COL_FULL.
     */
    public static final String SHOPLIST_DONE_COL_FULL = SHOPLIST_TABLE +
            SQLPERIOD +
            SHOPLIST_DONE_COL;
    /**
     * The constant SHOPLIST_DONE_TYPE.
     */
    public static final String SHOPLIST_DONE_TYPE = SQLINTEGER;
    /**
     * The constant SHOPLIST_DONE_PRIMARY_INDEX.
     */
    public static final boolean SHOPLIST_DONE_PRIMARY_INDEX = false;
    /**
     * The constant SHOPLISTDONECOL.
     */
    public static final DBColumn SHOPLISTDONECOL = new DBColumn(SHOPLIST_DONE_COL,
            SHOPLIST_DONE_TYPE,
            SHOPLIST_DONE_PRIMARY_INDEX,
            "0");


    /**
     * shoplistdategot - date purchased
     */
    public static final String SHOPLIST_DATEGOT_COL = "shoplistdategot";
    /**
     * The constant SHOPLIST_DATEGOT_COL_FULL.
     */
    public static final String SHOPLIST_DATEGOT_COL_FULL = SHOPLIST_TABLE +
            SQLPERIOD +
            SHOPLIST_DATEGOT_COL;
    /**
     * The constant SHOPLIST_DATEGOT_TYPE.
     */
    public static final String SHOPLIST_DATEGOT_TYPE = SQLINTEGER;
    /**
     * The constant SHOPLIST_DATEGOT_PRIMARY_INDEX.
     */
    public static final boolean SHOPLIST_DATEGOT_PRIMARY_INDEX = false;
    /**
     * The constant SHOPLISTDATEGOTCOL.
     */
    public static final DBColumn SHOPLISTDATEGOTCOL = new DBColumn(SHOPLIST_DATEGOT_COL,
            SHOPLIST_DATEGOT_TYPE,
            SHOPLIST_DATEGOT_PRIMARY_INDEX,
            "0");


    /**
     * shoplistcost - purchase cost
     */
    public static final String SHOPLIST_COST_COL = "shoplistcost";
    /**
     * The constant SHOPLIST_COST_COL_FULL.
     */
    public static final String SHOPLIST_COST_COL_FULL = SHOPLIST_TABLE +
            SQLPERIOD +
            SHOPLIST_COST_COL;
    /**
     * The constant SHOPLIST_COST_TYPE.
     */
    public static final String SHOPLIST_COST_TYPE = SQLREAL;
    /**
     * The constant SHOPLIST_COST_PRIMARY_INDEX.
     */
    public static final boolean SHOPLIST_COST_PRIMARY_INDEX = false;
    /**
     * The constant SHOPLISTCOSTCOL.
     */
    public static final DBColumn SHOPLISTCOSTCOL = new DBColumn(SHOPLIST_COST_COL,
            SHOPLIST_COST_TYPE,
            SHOPLIST_COST_PRIMARY_INDEX,
            "0");


    /**
     * The constant SHOPLISTCOLS.
     */
    public static final ArrayList<DBColumn> SHOPLISTCOLS = new ArrayList<>(Arrays.asList(SHOPLISTIDCOL,
            SHOPLISTPRODUCTREFCOL,
            SHOPLISTAISLEREFCOL,
            SHOPLISTDATEADDEDCOL,
            SHOPLISTNUMBERTOGETCOL,
            SHOPLISTDONECOL,
            SHOPLISTDATEGOTCOL,
            SHOPLISTCOSTCOL));
    /**
     * The constant SHOPLISTTABLE.
     */
    public static final DBTable SHOPLISTTABLE = new DBTable( SHOPLIST_TABLE,SHOPLISTCOLS);

    public static final DBIndex SHOPLISTAISLEREFINDEX = new DBIndex(SHOPLIST_TABLE+SHOPLIST_AISLEREF_COL+"index",
            SHOPLISTTABLE,SHOPLISTAISLEREFCOL,true,false);

    public static final String TOTALCOST = "totalcost";
    public static final String TOTALREMAINING = "totremaining";
    public static final String TOTALSPENT = "totalspent";
}
