package mjt.shopwise;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

/**
 * Main/Start Activity for ShopWise
 */
public class MainActivity extends AppCompatActivity {

    public DBHelper db;
    private DBDAO dbdao;
    private DBShopMethods dbshopmethods;
    private DBAisleMethods dbaislemethods;
    private static final String THIS_ACTIVITY = "MainActivity";
    private static final String LOGTAG = "SW-MA";
    private int shopcount;
    private int aislecount;
    private int productcount;
    private int productusagecount;
    private int shoplistcount;
    private int rulecount;
    private int appvaluecount;

    private int resumestate = StandardActivityConstants.RESUMSTATE_NORMAL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DBHelper(this,DBConstants.DATABASE_NAME,null,1);
        dbdao = new DBDAO(this);
        dbshopmethods = new DBShopMethods(this);
        dbaislemethods = new DBAisleMethods(this);
        getDBCounts();
        setContentView(R.layout.activity_main);
        dbshopmethods.insertShop("TEST3");
        long lastshop = dbshopmethods.getLastShopAdded();
        dbaislemethods.insertAisle("Test Aisle1",1000,dbshopmethods.getLastShopAdded());
        dbshopmethods.insertShop("Test4");
        dbshopmethods.modifyShop(4,1100,"MODIFIED","","","","");
        dbshopmethods.modifyShop(2,1200,"Altered","1 New Street","Cityville","Stateshire","Nothing much here");
        lastshop = dbshopmethods.getLastShopAdded();
        dbaislemethods.insertAisle("Test Aisle2",1000,dbshopmethods.getLastShopAdded());
        dbshopmethods.modifyShop(6,1600,"Altered","1 New Street","Cityville","Stateshire","Nothing much here");
        dbshopmethods.modifyShop(8,1800,"Altered","1 New Street","Cityville","Stateshire","Nothing much here");
        dbshopmethods.modifyShop(10,2200,"Altered","1 New Street","Cityville","Stateshire","Nothing much here");
        Cursor csr = dbshopmethods.getShops("","");
        while(csr.moveToNext()) {
            Log.i(LOGTAG," Shop=" +
                    csr.getString(csr.getColumnIndex(DBShopsTableConstants.SHOPS_NAME_COL)) +
                    " ID=" + csr.getString(csr.getColumnIndex(DBShopsTableConstants.SHOPS_ID_COL)) +
                    " ORDER=" + csr.getString(csr.getColumnIndex(DBShopsTableConstants.SHOPS_ORDER_COL))
            );
        }
        csr.close();
        Cursor aislecsr = dbaislemethods.getAisles("","");
        while (aislecsr.moveToNext()) {
            long idoffset = aislecsr.getColumnIndex(DBAislesTableConstants.AISLES_ID_COL);
            long aisleid = aislecsr.getLong(aislecsr.getColumnIndex(DBAislesTableConstants.AISLES_ID_COL));
            Log.i(LOGTAG,"Aisle=" +
                    aislecsr.getString(aislecsr.getColumnIndex(DBAislesTableConstants.AISLES_NAME_COL)) +
                    " ID=" + aislecsr.getString(aislecsr.getColumnIndex(DBAislesTableConstants.AISLES_ID_COL)) +
                    " ORDER=" + aislecsr.getString(aislecsr.getColumnIndex(DBAislesTableConstants.AISLES_ORDER_COL)) +
                    " SHOPREF=" + aislecsr.getString(aislecsr.getColumnIndex(DBAislesTableConstants.AISLES_SHOPREF_COL)) +
                    " ;"
            );
        }
        aislecsr.close();
        dbshopmethods.shopDeletedImpact(4);
        dbshopmethods.deleteShop(4);


        db.expand(null, true);
        int stophere = 0;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    @Override
    protected void onResume() {
        super.onResume();

        getDBCounts();
        /**
         * Resume state handling
         */
        switch (resumestate) {
             case StandardActivityConstants.RESUMESTATE_ALT1:
                 break;
             case StandardActivityConstants.RESUMESTATE_ALT2:
                 break;
             case StandardActivityConstants.RESUMESTATE_ALT3:
                 break;
             case StandardActivityConstants.RESUMESTATE_ALT4:
                 break;
             default:
                 break;
        }
        resumestate = StandardActivityConstants.RESUMSTATE_NORMAL;
    }

    private void getDBCounts() {
        shopcount = dbshopmethods.getShopCount();
        aislecount = dbdao.getAisleCount();
        productcount = dbdao.getProductCount();
        productusagecount = dbdao.getProductUsageCount();
        shoplistcount = dbdao.getShoplistCount();
        rulecount = dbdao.getRuleCount();
        appvaluecount = dbdao.getAppvalueCount();
    }
}
