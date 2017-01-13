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

    /**
     * The Database name.
     */
    static final String DATABASE_NAME = "ShopWise";
    /**
     * The Std id.
     */
    static final String STD_ID = "_id";
    /**
     * The Period.
     */
    static final String PERIOD = ".";
    /**
     * The Int.
     */
    static final String INT = "INTEGER";
    /**
     * The Txt.
     */
    static final String TXT = "TEXT";
    /**
     * The Idtype.
     */
    static final String IDTYPE = INT;
    /**
     * The Real.
     */
    static final String REAL = "REAL";
    /**
     * The Defaultorder.
     */
    static final String DEFAULTORDER = "1000";

    static final String CALCULATED_PRODUTSORDERED_NAME = "orderedcount";
    static final String CALCULATED_TOTALCOST = "calculated_totalcost";

    /**
     * The Sqlselect.
     */
    static final String SQLSELECT = " SELECT ";
    static final String SQLSELECTDISTINCT = SQLSELECT + "DISTINCT ";
    /**
     * The Sqlfrom.
     */
    static final String SQLFROM = " FROM ";
    /**
     * The Sqlgroup.
     */
    static final String SQLGROUP = " GROUP BY ";
    /**
     * The Sqlwhere.
     */
    static final String SQLWHERE = " WHERE ";
    /**
     * The Sqlorderby.
     */
    static final String SQLORDERBY = " ORDER BY ";
    /**
     * The Sqlorderascending.
     */
    static final String SQLORDERASCENDING = " ASC ";
    /**
     * The Sqlorderdescending.
     */
    static final String SQLORDERDESCENDING = " DESC ";
    /**
     * The Sqland.
     */
    static final String SQLAND = " AND ";
    /**
     * The Sqlor.
     */
    static final String SQLOR = " OR ";
    /**
     * The Sqlon.
     */
    static final String SQLON = " ON ";
    static final String SQLAS = " AS ";
    /**
     * The Sqlendstatement.
     */
    static final String SQLENDSTATEMENT = " ;";
    /**
     * The Sqljoin.
     */
    static final String SQLJOIN = " JOIN ";
    /**
     * The Sqlleftjoin.
     */
    static final String SQLLEFTJOIN = " LEFT JOIN ";
    /**
     * The Sqllimit.
     */
    static final String SQLNOT = " NOT ";
    static final String SQLIN = " IN ";
    static final String SQLNOTIN = SQLNOT + SQLIN;
    static final String SQLNOTEQUALS = " <> ";
    static final String SQLLIMIT = " LIMIT ";
    static final String SQLLIKECHARSTART = " LIKE '%";
    static final String SQLLIKECHAREND = "%' ";

    static final String SQLSUM = " SUM(";
    static final String SQLSUMCLOSE = ") ";

    static final String SQLMAX = "MAX(";
    static final String SQLMAXCLOSE = ") ";

    /**
     * The Sqlselectallfrom.
     */
    static final String SQLSELECTALLFROM = SQLSELECT + "*" + SQLFROM;
    static final String SQLSELECTDISTINCTALLFROM =
            SQLSELECTDISTINCT + "*" + SQLFROM;


    /**************************************************************************
     * DEFINITION OF THE SHOPWISE DATABASE
     * <p>
     * Derived from the respective DBTables
     * (see respective DB<table></table>Constants.java)
     * (note DBTable objects incorporate DBColumn objects where the columns
     * are defined)
     */
    static final ArrayList<DBTable> SHOPWISETABLES = new ArrayList<>(
            Arrays.asList(
                    SHOPSTABLE,
                    AISLESTABLE,
                    PRODUCTSTABLE,
                    PRODUCTUSAGETABLE,
                    SHOPLISTTABLE,
                    RULESTABLE,
                    APPVALUESTABLE
            ));
    /**
     * The Shopwise.
     */
    static final DBDatabase SHOPWISE = new DBDatabase(DATABASE_NAME,SHOPWISETABLES);
}