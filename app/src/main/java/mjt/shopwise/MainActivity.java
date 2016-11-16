package mjt.shopwise;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * Main/Start Activity for ShopWise
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String sql = DBConstants.SHOPWISE.generateExportSchemaSQL();
        int stophere = 0;
    }
}
