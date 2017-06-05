package mjt.shopwise;

import java.util.ArrayList;
import java.util.Arrays;

import mjt.dbindex.DBIndex;
import mjt.dbtable.DBTable;
import mjt.dbdatabase.DBDatabase;

import static mjt.shopwise.DBAislesTableConstants.AISLESSHOPREFINDEX;
import static mjt.shopwise.DBAislesTableConstants.AISLESTABLE;
import static mjt.shopwise.DBProductsTableConstants.PRODUCTSTABLE;
import static mjt.shopwise.DBProductusageTableConstants.PRODUCTUSAGETABLE;
import static mjt.shopwise.DBRulesTableConstants.RULESTABLE;
import static mjt.shopwise.DBShopListTableConstants.SHOPLISTAISLEREFINDEX;
import static mjt.shopwise.DBShopListTableConstants.SHOPLISTTABLE;
import static mjt.shopwise.DBShopsTableConstants.SHOPSTABLE;
import static mjt.shopwise.DBAppvaluesTableConstants.APPVALUESTABLE;
import static mjt.shopwise.DBStorageTableConstants.STORAGETABLE;

/**
 * Constans used for DB construction and usage. Note that many values are
 * obtained from the underlying DB????TableConstants interfaces
 */
@SuppressWarnings("WeakerAccess")
final class DBConstants {

    /**
     * The Database name.
     */
    static final String DATABASE_NAME = "ShopWise";
    /**
     * The Defaultorder appled to ordered items; Shops, Aisles,
     * Storage, Products etc.
     */
    static final String DEFAULTORDER = "1000";

    /*
    Cursor row names for Calculated columns
     */
    static final String CALCULATED_PRODUCTSORDERED_NAME = "orderedcount";
    static final String CALCULATED_TOTALCOST = "calculated_totalcost";
    static final String CALCULATED_RULEPERIODINDAYS = "periodindays";

    /**************************************************************************
     * DEFINITION OF THE SHOPWISEBASESCHEMA DATABASE
     */
    /*
    Create a DBTable ArrayList of the database tables.
     */
    static final ArrayList<DBTable> SHOPWISETABLES = new ArrayList<>(
            Arrays.asList(
                    SHOPSTABLE,
                    AISLESTABLE,
                    PRODUCTSTABLE,
                    PRODUCTUSAGETABLE,
                    SHOPLISTTABLE,
                    RULESTABLE,
                    APPVALUESTABLE,
                    STORAGETABLE
            )
    );
    /*
    Create a DBIndex ArrayList of the indexes.
     */
    static final ArrayList<DBIndex> SHOPWISEINDXES = new ArrayList<>(
            Arrays.asList(
                    SHOPLISTAISLEREFINDEX,
                    AISLESSHOPREFINDEX
            )
    );
    /**************************************************************************
     * Create the base/pseudo database schema.
     * The schema is the desired structure of databsase. It is compared against
     * the actual structure allowing the addition of tables, indexes and columns.
     */
    static final DBDatabase SHOPWISEBASESCHEMA = new DBDatabase(DATABASE_NAME,
            SHOPWISETABLES,
            SHOPWISEINDXES
    );
}