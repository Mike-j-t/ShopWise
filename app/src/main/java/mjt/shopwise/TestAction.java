package mjt.shopwise;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Test Action Activity for use when developing a new invoked activity
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class TestAction extends AppCompatActivity {

    /**
     * The H 1.
     */
    int h1;
    /**
     * The H 2.
     */
    @SuppressWarnings("unused")
    int h2;
    /**
     * The H 3.
     */
    @SuppressWarnings("unused")
    int h3;
    /**
     * The H 4.
     */
    @SuppressWarnings("unused")
    int h4;
    /**
     * The Tv.
     */
    TextView tv;

    @Override
    protected void onCreate(Bundle savedinstanceState) {
        super.onCreate(savedinstanceState);
        setContentView(R.layout.mytest);

        //Prepare and apply respective ActionBar and Heading color coding
        ActionColorCoding.setActionBarColor(this,
                getIntent(),
                getSupportActionBar()
        );
        h1 = ActionColorCoding.setHeadingColor(this,getIntent(),1);
        h2 = ActionColorCoding.setHeadingColor(this,getIntent(),2);
        h3 = ActionColorCoding.setHeadingColor(this,getIntent(),3);
        h4 = ActionColorCoding.setHeadingColor(this,getIntent(),4);

        tv = findViewById(R.id.mytest_textview);
        tv.setBackgroundColor(h1);

    }
}
