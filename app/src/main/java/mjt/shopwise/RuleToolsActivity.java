package mjt.shopwise;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Mike092015 on 19/01/2017.
 */

public class RuleToolsActivity extends AppCompatActivity{

    private static final String THIS_ACTIVITY = "RuleToolsActivity";
    private static String caller;
    private static int calledmode;

    Context context;
    ActionBar actionbar;

    /**
     * Colours
     */
    private static int h1;
    private static int h2;
    private static int h3;
    private static int h4;
    private static int primary_color;
    private String menucolorcode;
    int passedmenucolorcode;

    TextView messagebar;
    TextView donebutton;
    TextView suggestbutton;
    TextView checkbutton;
    TextView suggestoverview;
    TextView checkoverview;

    private int resumestate = StandardAppConstants.RESUMSTATE_NORMAL;
    private Activity thisactivity;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruletools);
        context = this;
        thisactivity = (Activity)context;
        caller = getIntent().getStringExtra(
                StandardAppConstants.INTENTKEY_CALLINGACTIVITY);
        calledmode = getIntent().getIntExtra(
                StandardAppConstants.INTENTKEY_CALLINGMODE,0);
        menucolorcode = StandardAppConstants.INTENTKEY_MENUCOLORCODE;
        passedmenucolorcode = getIntent().getIntExtra(menucolorcode,0);

        messagebar = (TextView) findViewById(R.id.ruletools_messagebar);
        donebutton = (TextView) findViewById(R.id.ruletools_donebutton);
        suggestbutton = (TextView) findViewById(R.id.ruletools_rulesuggestionbutton);
        checkbutton = (TextView) findViewById(R.id.ruletools_checkbutton);
        suggestoverview = (TextView) findViewById(R.id.ruletools_suggestoverview);
        checkoverview = (TextView) findViewById(R.id.ruletools_checkoverview);

        /**
         * Apply Color Coding
         */
        actionbar = getSupportActionBar();
        ActionColorCoding.setActionBarColor(this,getIntent(),actionbar);
        primary_color = ActionColorCoding.setHeadingColor(this,getIntent(),0);
        h1 = ActionColorCoding.setHeadingColor(this,getIntent(),1);
        h2 = ActionColorCoding.setHeadingColor(this,getIntent(),2);
        h3 = ActionColorCoding.setHeadingColor(this,getIntent(),3);
        h4 = ActionColorCoding.setHeadingColor(this,getIntent(),4);
        ActionColorCoding.setActionButtonColor(suggestbutton,primary_color);
        ActionColorCoding.setActionButtonColor(checkbutton,primary_color);
        suggestoverview.setTextColor(primary_color);
        checkoverview.setTextColor(primary_color);

    }

    /**************************************************************************
     * onResume do any processing upon resume of the activity
     * e.g. refresh any listviews etc.
     * RESUMESTATES would be set when starting another activity if that
     * activity could alter the contents to be displayed.
     * Should always set the resumestate to RESUMESTATE_NORMAL
     */
    @Override
    protected void onResume() {
        super.onResume();
        switch (resumestate) {
            case StandardAppConstants.RESUMESTATE_ALT1:
                break;
            case StandardAppConstants.RESUMESTATE_ALT2:
                break;
            case StandardAppConstants.RESUMESTATE_ALT3:
                break;
            case StandardAppConstants.RESUMESTATE_ALT4:
                break;
            default:
                messagebar.setVisibility(View.INVISIBLE);
                break;
        }
    }

    /**************************************************************************
     * onDestroy - do any clean up before th application is destroyed
     * e.g. close any open cursors and then close the database
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**************************************************************************
     * Button Click Handler
     *
     * @param view The view (i.e the TextView that was clicked)
     */
    public void actionButtonClick(View view) {

        Intent intent = null;
        switch (view.getId()) {
            case R.id.ruletools_donebutton:
                this.finish();
                break;
            case R.id.tools_backupbutton:
                intent = new Intent(this,BackupActivity.class);
                break;
            default:
                break;
        }
        if (intent != null) {
            intent.putExtra(StandardAppConstants.INTENTKEY_CALLINGACTIVITY,THIS_ACTIVITY);
            intent.putExtra(StandardAppConstants.INTENTKEY_CALLINGMODE,StandardAppConstants.CM_CLEAR);
            intent.putExtra(menucolorcode,passedmenucolorcode + 1);
            startActivity(intent);
        }
    }
}
