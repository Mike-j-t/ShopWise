package mjt.shopwise;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Mike092015 on 17/11/2016.
 */

public class DBDAO {

    protected SQLiteDatabase db;
    private DBHelper dbhelper;
    private Context mContext;

    /**
     * DBAO Constructor
     * @param context Context of the invoking method
     */
    public DBDAO(Context context) {
        this.mContext = context;
        dbhelper = DBHelper.getHelper(context);
        db = dbhelper.getWritableDatabase();
    }

    /**
     *
     * @param tablename table to inspect
     * @return number of rows
     */
    private int getTableRowCount(String tablename) {
        String sql = " SELECT * FROM " + tablename + " ;";
        Cursor csr = db.rawQuery(sql,null);
        int rv = csr.getCount();
        csr.close();
        return rv;
    }

    /**
     * getShopCount
     * @return number of Shops
     */
    public int getShopCount() {
        return getTableRowCount(DBShopsTableConstants.SHOPS_TABLE);
    }

    /**
     * getAisleCount
     * @return number of Aisles
     */
    public int getAisleCount() {
        return getTableRowCount(DBAislesTableConstants.AISLES_TABLE);
    }

    /**
     * getProductCount
     * @return number of Products
     */
    public int getProductCount() {
        return getTableRowCount(DBProductsTableConstants.PRODUCTS_TABLE);
    }

    /**
     * getProductUsageCount
     * @return number of Productusages
     */
    public int getProductUsageCount() {
        return getTableRowCount(DBProductusageTableConstants.PRODUCTUSAGE_TABLE);
    }

    /**
     * getShopListCount
     * @return number of ShopList entries
     */
    public int getShoplistCount() {
        return getTableRowCount(DBShopListTableConstants.SHOPLIST_TABLE);
    }

    /**
     * getRuleCount
     * @return number of Rules
     */
    public int getRuleCount() {
        return getTableRowCount(DBRulesTableConstants.RULES_TABLE);
    }

    /**
     * getAppvalueCount
     * @return number of Appvalue entries
     */
    public int getAppvalueCount() {
        return getTableRowCount(DBAppvaluesTableConstants.APPVALUES_TABLE);
    }
}
