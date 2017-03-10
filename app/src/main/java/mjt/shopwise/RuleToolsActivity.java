package mjt.shopwise;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Mike092015 on 19/01/2017.
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

        messagebar = (TextView) findViewById(R.id.ruletools_messagebar);
        donebutton = (TextView) findViewById(R.id.ruletools_donebutton);
        suggestbutton = (TextView) findViewById(R.id.ruletools_rulesuggestionbutton);
        checkbutton = (TextView) findViewById(R.id.ruletools_checkbutton);
        disabledbutton = (TextView) findViewById(R.id.ruletools_disabledrulesbutton);
        suggestoverview = (TextView) findViewById(R.id.ruletools_suggestoverview);
        checkoverview = (TextView) findViewById(R.id.ruletools_checkoverview);
        minbuyinfo = (TextView) findViewById(R.id.ruletools_minpbuy_info);
        minperiodinfo = (TextView) findViewById(R.id.ruletools_minperiod_info);
        minbuylabel = (TextView) findViewById(R.id.ruletools_minbuy_label);
        minperiodlabel = (TextView) findViewById(R.id.ruletools_minperiod_label);
        minbuy = (EditText) findViewById(R.id.ruletools_minbuy);
        minperiod = (EditText) findViewById(R.id.ruletools_minperiod);
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
        ActionColorCoding.setActionButtonColor(suggestbutton,primary_color);
        ActionColorCoding.setActionButtonColor(checkbutton,primary_color);
        ActionColorCoding.setActionButtonColor(minbuy,
                h2 & ActionColorCoding.transparency_requied);
        ActionColorCoding.setActionButtonColor(minperiod,
                h2 & ActionColorCoding.transparency_requied);
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
    //TODO working on this to add Suggest and Check, also remove case for backup
    @SuppressWarnings("unused")
    public void actionButtonClick(View view) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);

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
}
