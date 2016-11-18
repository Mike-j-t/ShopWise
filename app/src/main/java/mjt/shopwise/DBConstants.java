package mjt.shopwise;

import java.util.ArrayList;
import java.util.Arrays;

import static mjt.shopwise.DBAislesTableConstants.AISLESTABLE;
import static mjt.shopwise.DBProductsTableConstants.PRODUCTSTABLE;

import static mjt.shopwise.DBProductusageTableConstants.PRODUCTUSAGETABLE;
import static mjt.shopwise.DBRulesTableConstants.RULESTABLE;
import static mjt.shopwise.DBShopListTableConstants.SHOPLISTTABLE;
import static mjt.shopwise.DBShopsTableConstants.SHOPSTABLE;
import static mjt.shopwise.DBAppvaluesTableConstants.APPVALUESTABLE;

/**
 * Constans used for DB construction and usage. Note that many values are
 * obtained from the underlying DB????TableConstants interfaces
 */

final class DBConstants {

    static final String DATABASE_NAME = "ShopWise";
    static final String STD_ID = "_id";
    static final String PERIOD = ".";
    static final String INT = "INTEGER";
    static final String TXT = "TEXT";
    static final String IDTYPE = INT;
    static final String REAL = "REAL";
    static final String DEFAULTORDER = "1000";

    static final ArrayList<DBTable> SHOPWISETABLES = new ArrayList<>(Arrays.asList(SHOPSTABLE,
            AISLESTABLE,
            PRODUCTSTABLE,
            PRODUCTUSAGETABLE,
            SHOPLISTTABLE,
            RULESTABLE,
            APPVALUESTABLE));
    static final DBDatabase SHOPWISE = new DBDatabase(DATABASE_NAME,SHOPWISETABLES);
}