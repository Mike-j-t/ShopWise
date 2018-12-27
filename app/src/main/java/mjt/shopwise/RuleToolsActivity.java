package mjt.shopwise;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import mjt.displayhelp.DisplayHelp;

/**
 * Rule Tools Activity (Rule Suggestion and Rule Accuracy Checking)
 */

@SuppressWarnings({"FieldCanBeLocal", "WeakerAccess", "CanBeFinal", "unused"})
public class RuleToolsActivity extends AppCompatActivity{

    private static final String THIS_ACTIVITY = "RuleToolsActivity";
    @SuppressWarnings("unused")
    private static String caller;
    @SuppressWarnings("unused")
    private static int calledmode;

    Context context;
    ActionBar actionbar;

    /**
     * Colours
     */
    @SuppressWarnings("unused")
    private static int h1;
    private static int h2;
    @SuppressWarnings("unused")
    private static int h3;
    @SuppressWarnings("unused")
    private static int h4;
    private static int primary_color;
    private String menucolorcode;
    int passedmenucolorcode;

    TextView messagebar;
    TextView donebutton;
    TextView suggestbutton;
    TextView checkbutton;
    TextView disabledbutton;
    TextView suggestoverview;
    TextView checkoverview;
    TextView minperiodinfo;
    TextView minbuyinfo;
    TextView minperiodlabel;
    TextView minbuylabel;
    EditText minperiod;
    EditText minbuy;
    private DBRuleMethods dbRuleMethods;

    private int resumestate = StandardAppConstants.RESUMSTATE_NORMAL;
    @SuppressWarnings("unused")
    private Activity thisactivity;
    public static final String THISCLASS = RuleToolsActivity.class.getSimpleName();
    private static final String LOGTAG = "SW_RTA";

    protected void onCreate(Bundle savedInstanceState) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruletools);
        context = this;
        thisactivity = (Activity)context;
        msg = "Retrieving IntentExtras";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        caller = getIntent().getStringExtra(
                StandardAppConstants.INTENTKEY_CALLINGACTIVITY);
        calledmode = getIntent().getIntExtra(
                StandardAppConstants.INTENTKEY_CALLINGMODE,0);

        msg = "Preparing ColorCoding";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        menucolorcode = StandardAppConstants.INTENTKEY_MENUCOLORCODE;
        passedmenucolorcode = getIntent().getIntExtra(menucolorcode,0);

        messagebar = findViewById(R.id.ruletools_messagebar);
        donebutton = findViewById(R.id.ruletools_donebutton);
        suggestbutton = findViewById(R.id.ruletools_rulesuggestionbutton);
        checkbutton = findViewById(R.id.ruletools_checkbutton);
        disabledbutton = findViewById(R.id.ruletools_disabledrulesbutton);
        suggestoverview = findViewById(R.id.ruletools_suggestoverview);
        checkoverview = findViewById(R.id.ruletools_checkoverview);
        minbuyinfo = findViewById(R.id.ruletools_minpbuy_info);
        minperiodinfo = findViewById(R.id.ruletools_minperiod_info);
        minbuylabel = findViewById(R.id.ruletools_minbuy_label);
        minperiodlabel = findViewById(R.id.ruletools_minperiod_label);
        minbuy = findViewById(R.id.ruletools_minbuy);
        minperiod = findViewById(R.id.ruletools_minperiod);
        minbuy.setText(context.getResources().getString(R.string.minbuydefault));
        minperiod.setText(context.getResources().getString(R.string.minperioddefault));

        dbRuleMethods = new DBRuleMethods(this);
        Cursor dr = dbRuleMethods.getDisabledRules("");
        int drcount = dr.getCount();
        if (drcount < 1) {
            disabledbutton.setVisibility(View.INVISIBLE);
        } else {
            disabledbutton.setVisibility(View.VISIBLE);
        }
        dr.close();


        // Apply Color Coding
        actionbar = getSupportActionBar();
        ActionColorCoding.setActionBarColor(this,getIntent(),actionbar);
        primary_color = ActionColorCoding.setHeadingColor(this,getIntent(),0);
        h1 = ActionColorCoding.setHeadingColor(this,getIntent(),1);
        h2 = ActionColorCoding.setHeadingColor(this,getIntent(),2);
        h3 = ActionColorCoding.setHeadingColor(this,getIntent(),3);
        h4 = ActionColorCoding.setHeadingColor(this,getIntent(),4);
        ActionColorCoding.setActionButtonColor(donebutton,primary_color);
        ActionColorCoding.setActionButtonColor(suggestbutton,primary_color);
        ActionColorCoding.setActionButtonColor(checkbutton,primary_color);
        ActionColorCoding.setActionButtonColor(minbuy,
                h2 & ActionColorCoding.transparency_required);
        ActionColorCoding.setActionButtonColor(minperiod,
                h2 & ActionColorCoding.transparency_required);
        suggestoverview.setTextColor(h2);
        checkoverview.setTextColor(h2);
        minbuylabel.setTextColor(primary_color);
        minperiodlabel.setTextColor(primary_color);
        minbuyinfo.setTextColor(h2);
        minperiodinfo.setTextColor(h2);

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
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
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
        Cursor dr = dbRuleMethods.getDisabledRules("");
        if (!(dr.getCount() > 0)) {
            disabledbutton.setVisibility(View.INVISIBLE);
        } else {
            disabledbutton.setVisibility(View.VISIBLE);
        }
        dr.close();
    }

    /**
     * Add the help option to the Activity's menu bar.
     * @param menu  The menu xml
     * @return  true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.common_help, menu);
        return true;
    }

    /**
     * Action the respective option when the menu is selected
     * @param menuitem  The menuitem that was selected
     * @return true to indicate actioned.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem menuitem) {
        int menuitemid = menuitem.getItemId();
        switch (menuitemid) {
            case R.id.actionhelp:
                //new DisplayHelp(this,"ALt Title",R.array.help_main_activity,80,true,0xffff0000, 0xbbffffff,20f,16f,12);
                new DisplayHelp(this,
                        getResources().getString(
                                R.string.title_help_ruletools_activity),
                        R.array.help_ruletools_activty,
                        85,
                        true,
                        primary_color,
                        0xbbffffff,
                        22f,
                        16f,
                        12
                );
                return true;
            default:
                break;
        }
        return  onOptionsItemSelected(menuitem);
    }

    /**************************************************************************
     * onDestroy - do any clean up before th application is destroyed
     * e.g. close any open cursors and then close the database
     */
    @Override
    protected void onDestroy() {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        super.onDestroy();
    }

    /**************************************************************************
     * Button Click Handler
     *
     * @param view The view (i.e the TextView that was clicked)
     */
    @SuppressWarnings("unused")
    public void actionButtonClick(View view) {
        String msg = "Invoked";
        Emsg emsg;
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);

        emsg = ValidateInput.validateInteger(minbuy.getText().toString(),0,9999);
        if (emsg.getErrorIndicator()) {
            setMessage(this,emsg.getErrorMessage(),true);
            return;
        }

        emsg = ValidateInput.validateInteger(minperiod.getText().toString(),0,9999);
        if (emsg.getErrorIndicator()) {
            setMessage(this,emsg.getErrorMessage(),true);
            return;
        }

        Intent intent = null;
        //Integer minimumbuy = new Integer(minbuy.getText().toString());
        Integer minimumbuy = Integer.valueOf(minbuy.getText().toString());
        //Integer minimumprd = new Integer(minperiod.getText().toString());
        Integer minimumprd = Integer.valueOf(minperiod.getText().toString());
        boolean callingmodeset = false;
        switch (view.getId()) {
            case R.id.ruletools_donebutton:
                msg = "Finishing";
                LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
                this.finish();
                break;
            case R.id.tools_backupbutton:
                intent = new Intent(this,BackupActivity.class);
                break;
            case R.id.ruletools_rulesuggestionbutton:
                intent = new Intent(this,RuleSuggestCheckActivity.class);
                intent.putExtra(StandardAppConstants.INTENTKEY_CALLINGMODE,
                        StandardAppConstants.CM_RULESUGGEST);
                callingmodeset = true;
                break;
            case R.id.ruletools_checkbutton:
                intent = new Intent(this,RuleSuggestCheckActivity.class);
                intent.putExtra(StandardAppConstants.INTENTKEY_CALLINGMODE,
                        StandardAppConstants.CM_RULEACCURACY);
                callingmodeset = true;
                break;
            case R.id.ruletools_disabledrulesbutton:
                intent = new Intent(this,RuleSuggestCheckActivity.class);
                intent.putExtra(StandardAppConstants.INTENTKEY_CALLINGMODE,
                        StandardAppConstants.CM_RULEDISABLED);
                callingmodeset = true;
            default:
                break;
        }
        if (intent != null) {
            intent.putExtra(StandardAppConstants.INTENTKEY_CALLINGACTIVITY,THIS_ACTIVITY);
            intent.putExtra(StandardAppConstants.INTENTKEY_RULETOOLMINBUY,
                    minimumbuy);
            intent.putExtra(StandardAppConstants.INTENTKEY_RULETOOLMINPERIOD,
                    minimumprd);
            if (!callingmodeset) {
                intent.putExtra(StandardAppConstants.INTENTKEY_CALLINGMODE,
                        StandardAppConstants.CM_CLEAR);
            }
            intent.putExtra(menucolorcode,passedmenucolorcode);
            msg = "Starting Activty " + intent.getComponent().getShortClassName();
            LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
            startActivity(intent);
        }
    }

    /**************************************************************************
     * setMessage - set the Message regarding the lat action performed
     * Note as an action may have altered the database getDBcounts
     * is called and the title changed
     *
     * @param rta   the sa
     * @param msg  The message to be displayed.
     * @param flag Message imnportant, if true Yellow text, esle green
     */
    public void setMessage(RuleToolsActivity rta, String msg, boolean flag) {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);

        TextView messagebar = rta.findViewById(
                R.id.ruletools_messagebar);
        messagebar.setText(context.getResources().getString(
                R.string.messagebar_prefix_lastaction,msg));
        if (flag) {
            messagebar.setTextColor(Color.YELLOW);
        } else {
            messagebar.setTextColor(Color.GREEN);
        }
        messagebar.setVisibility(View.VISIBLE);
        rta.actionbar.setTitle(getResources().getString(R.string.ruleslabel));
    }
}
