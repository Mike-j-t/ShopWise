package mjt.shopwise;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * ShopWise Database Common Methods
 */
@SuppressWarnings("WeakerAccess")
public class DBCommonMethods {


    private DBCommonMethods() {
    }


    /**
     * Gets table row count.
     *
     * @param db        the db
     * @param tablename the tablename
     * @param filter    the filter
     * @param order     the order
     * @return the table row count
     */
    static int getTableRowCount(SQLiteDatabase db,
                                String tablename,
                                String filter,
                                @SuppressWarnings("SameParameterValue") String order) {
        Cursor csr = getTableRows(db,tablename,filter,order);
        int rv = csr.getCount();
        csr.close();
        return rv;
    }

    /**
     * Gets table row count.
     *
     * @param db        the db
     * @param tablename the tablename
     * @return the table row count
     */
    static int getTableRowCount(SQLiteDatabase db, String tablename) {
        return getTableRowCount(db,tablename, "", "");
    }

    /**
     * Gets table rows.
     *
     * @param db     the db
     * @param table  the table
     * @param filter the filter
     * @param order  the order
     * @return the table rows
     */
    static Cursor getTableRows(SQLiteDatabase db,
                               String table,
                               String filter,
                               String order) {
        return getTableRows(db,"",table,"",filter,"",order,0);
    }


    /**
     * Gets table rows.
     *
     * @param db          the db
     * @param columns     the columns
     * @param table       the table
     * @param joinclauses the joinclauses
     * @param filter      the filter
     * @param groupclause the groupclause
     * @param order       the order
     * @param limit       the limit
     * @return the table rows
     */
    static Cursor getTableRows(SQLiteDatabase db,
                               @SuppressWarnings("SameParameterValue") String columns,
                               String table,
                               @SuppressWarnings("SameParameterValue") String joinclauses,
                               String filter,
                               @SuppressWarnings("SameParameterValue") String groupclause,
                               String order,
                               @SuppressWarnings("SameParameterValue") int limit) {
        if (columns.length() < 1 ) {
            columns = " * ";
        }
        String sql = DBConstants.SQLSELECT +
                columns + DBConstants.SQLFROM +
                table;
        if (joinclauses.length() > 0 ) {
            sql = sql + joinclauses;
        }
        if (filter.length() > 0 ) {
            sql = sql + DBConstants.SQLWHERE + filter;
        }
        if (groupclause.length() > 0 ) {
            sql = sql + DBConstants.SQLGROUP + groupclause;
        }
        if (order.length() > 0 ) {
            sql = sql + DBConstants.SQLORDERBY + order;
        }
        if (limit > 0 ) {
            sql = sql + DBConstants.SQLLIMIT + Integer.toString(limit);
        }
        sql = sql + DBConstants.SQLENDSTATEMENT;
        return db.rawQuery(sql,null);
    }
}
