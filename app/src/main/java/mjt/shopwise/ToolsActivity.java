package mjt.shopwise;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

/**
 * Tools Activity - List of available tools with buttons and decsription
 * thus allowing the respective tool activities to be started/used.
 */

public class ToolsActivity extends AppCompatActivity {

    private static final String THIS_ACTIVITY = "ToolsActivity";
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
    TextView backupbutton;
    TextView backupoverview;
    TextView rulesbutton;
    TextView rulesoverview;


    private int resumestate = StandardAppConstants.RESUMSTATE_NORMAL;
    private Activity thisactivity;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools);
        context = this;
        thisactivity = (Activity)context;
        caller = getIntent().getStringExtra(
                StandardAppConstants.INTENTKEY_CALLINGACTIVITY);
        calledmode = getIntent().getIntExtra(
                StandardAppConstants.INTENTKEY_CALLINGMODE,0);
        menucolorcode = StandardAppConstants.INTENTKEY_MENUCOLORCODE;
        passedmenucolorcode = getIntent().getIntExtra(menucolorcode,0);

        messagebar = (TextView) findViewById(R.id.tools_messagebar);
        donebutton = (TextView) findViewById(R.id.tools_donebutton);
        backupbutton = (TextView) findViewById(R.id.tools_backupbutton);
        backupoverview = (TextView) findViewById(R.id.tools_backupoverview);
        rulesbutton = (TextView) findViewById(R.id.tools_rulesbutton);
        rulesoverview = (TextView) findViewById(R.id.tools_rulesoverview);

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
        ActionColorCoding.setActionButtonColor(donebutton,primary_color);
        ActionColorCoding.setActionButtonColor(backupbutton,primary_color);
        backupoverview.setTextColor(primary_color);
        ActionColorCoding.setActionButtonColor(rulesbutton,primary_color);
        rulesoverview.setTextColor(primary_color);

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
            case R.id.tools_donebutton:
                this.finish();
                break;
            case R.id.tools_backupbutton:
                intent = new Intent(this,BackupActivity.class);
                break;
            case R.id.tools_rulesbutton:
                intent = new Intent(this,RuleToolsActivity.class);
                break;
            default:
                break;
        }
        if (intent != null) {
            intent.putExtra(StandardAppConstants.INTENTKEY_CALLINGACTIVITY,THIS_ACTIVITY);
            intent.putExtra(StandardAppConstants.INTENTKEY_CALLINGMODE,StandardAppConstants.CM_CLEAR);
            intent.putExtra(menucolorcode,passedmenucolorcode);
            startActivity(intent);
        }
    }

    /**************************************************************************
     * setMessage - set the Message regarding the lat action performed
     * Note as an action may have altered the database getDBcounts
     * is called and the title changed
     *
     * @param ta   the sa
     * @param msg  The message to be displayed.
     * @param flag Message imnportant, if true Yellow text, esle green
     */
    public void setMessage(ToolsActivity ta, String msg, boolean flag) {

        TextView messagebar = (TextView) ta.findViewById(R.id.tools_messagebar);
        messagebar.setText(context.getResources().getString(
                R.string.messagebar_prefix_lastaction) + " " + msg);
        if (flag) {
            messagebar.setTextColor(Color.YELLOW);
        } else {
            messagebar.setTextColor(Color.GREEN);
        }
        messagebar.setVisibility(View.VISIBLE);
        ta.actionbar.setTitle(getResources().getString(R.string.shopslabel));
    }
}
