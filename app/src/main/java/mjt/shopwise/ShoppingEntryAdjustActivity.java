package mjt.shopwise;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Shopping List Entry Adjustment Activity Invoke when clicking on a
 * Adjust button on the Shopping List
 */

@SuppressWarnings({"FieldCanBeLocal", "WeakerAccess", "unused"})
public class ShoppingEntryAdjustActivity extends AppCompatActivity {

    @SuppressWarnings("unused")
    private final String THIS_ACTIVITY = "ShoppingEntryAdjustActivity";
    private final String LOGTAG = "SW-SHPPINGEAA";
    @SuppressWarnings("unused")
    private String caller;
    private int resumestate = StandardAppConstants.RESUMSTATE_NORMAL;
    @SuppressWarnings("unused")
    private Activity thisactivity;

    Context context;
    ActionBar actionbar;

    /**
     * Colours
     */
    private int h1;
    @SuppressWarnings("unused")
    private int h2;
    @SuppressWarnings("unused")
    private int h3;
    @SuppressWarnings("unused")
    private int h4;
    private int primary_color;
    private String menucolorcode;
    /**
     * The Passedmenucolorcode.
     */
    int passedmenucolorcode;

    @SuppressWarnings("unused")
    DBDAO dbdao;
    @SuppressWarnings("unused")
    DBShopMethods dbshopmethods;
    @SuppressWarnings("unused")
    DBAisleMethods dbaislemethods;
    DBProductMethods dbproductmethods;
    DBProductUsageMethods dbpumethods;
    DBShopListMethods dbshoplistmethods;
    @SuppressWarnings("unused")
    DBRuleMethods dbrulemethods;

    LinearLayout originalvaluesheading;
    @SuppressWarnings("unused")
    TextView messagebar;
    TextView donebutton;
    TextView savebutton;
    TextView undobutton;
    TextView morebutton;
    TextView lessbutton;
    TextView orig_productname_tv;
    TextView orig_quantity_tv;
    TextView orig_cost_tv;
    TextView orig_total_tv;
    EditText new_cost_et;
    EditText new_quantity_et;
    EditText new_productname_et;
    TextView new_productname_lbl;
    TextView new_cost_lbl;
    TextView new_quantity_lbl;
    TextView orig_productname_lbl;
    TextView orig_quantity_lbl;
    TextView orig_cost_lbl;
    TextView orig_total_lbl;


    long orig_aisleid = 0;
    long orig_productid = 0;
    String orig_productname;
    double orig_cost;
    int orig_quantity;
    String saved_productname = "";
    double saved_cost = -1;
    int saved_quantity = -1;



    @SuppressWarnings("unused")
    private static final String SHOPLISTID_COLUMN = DBShopListTableConstants.SHOPLIST_ID_COL;
    @SuppressWarnings("unused")
    private static final String SHOPLISTID_FULLCOLUMN = DBShopListTableConstants.SHOPLIST_ID_COL_FULL;
    @SuppressWarnings("unused")
    private static final String SHOPLISTPRODUCTREF_COLUMN = DBShopListTableConstants.SHOPLIST_PRODUCTREF_COL;
    @SuppressWarnings("unused")
    private static final String SHOPLISTPRODUCTREF_FULLCOLUMN = DBShopListTableConstants.SHOPLIST_PRODUCTREF_COL_FULL;
    @SuppressWarnings("unused")
    private static final String SHOPLISTAISLREF_COLUMN = DBShopListTableConstants.SHOPLIST_AISLEREF_COL;
    @SuppressWarnings("unused")
    private static final String SHOPLISTAISLREF_FULLCOLUMN = DBShopListTableConstants.SHOPLIST_AISLEREF_COL_FULL;
    @SuppressWarnings("unused")
    private static final String SHOPLISTDATEADDED_COLUMN = DBShopListTableConstants.SHOPLIST_DATEADDED_COL;
    @SuppressWarnings("unused")
    private static final String SHOPLISTDATEADDED_FULLCOLUMN = DBShopListTableConstants.SHOPLIST_DATEADDED_COL_FULL;
    @SuppressWarnings("unused")
    private static final String SHOPLISTNUMBERTOGET_COLUMN = DBShopListTableConstants.SHOPLIST_NUMBERTOGET_COL;
    @SuppressWarnings("unused")
    private static final String SHOPLISTNUMBERTOGET_FULLCOLUMN = DBShopListTableConstants.SHOPLIST_NUMBERTOGET_COL_FULL;
    @SuppressWarnings("unused")
    private static final String SHOPLISTDONE_COLUMN = DBShopListTableConstants.SHOPLIST_DONE_COL;
    @SuppressWarnings("unused")
    private static final String SHOPLISTDONE_FULLCOLUMN = DBShopListTableConstants.SHOPLIST_DONE_COL_FULL;
    @SuppressWarnings("unused")
    private static final String SHOPLISTDATEGOT_COLUMN = DBShopListTableConstants.SHOPLIST_DATEGOT_COL;
    @SuppressWarnings("unused")
    private static final String SHOPLISTDATEGOT_FULLCOLUMN = DBShopListTableConstants.SHOPLIST_DATEGOT_COL_FULL;
    @SuppressWarnings("unused")
    private static final String SHOPLISTCOST_COLUMN = DBShopListTableConstants.SHOPLIST_COST_COL;
    @SuppressWarnings("unused")
    private static final String SHOPLISTCOST_FULLCOLUMN = DBShopListTableConstants.SHOPLIST_COST_COL_FULL;

    @SuppressWarnings("unused")
    private static final String SHOPID_COLUMN = DBShopsTableConstants.SHOPS_ID_COL;
    @SuppressWarnings("unused")
    private static final String SHOPNAME_COLUMN = DBShopsTableConstants.SHOPS_NAME_COL;
    @SuppressWarnings("unused")
    private static final String SHOPCITY_COLUMN = DBShopsTableConstants.SHOPS_CITY_COL;
    @SuppressWarnings("unused")
    private static final String SHOPORDER_COLUMN = DBShopsTableConstants.SHOPS_ORDER_COL;
    @SuppressWarnings("unused")
    private static final String SHOPID_FULLCOLUMN = DBShopsTableConstants.SHOPS_ID_COL_FULL;
    @SuppressWarnings("unused")
    private static final String SHOPNAME_FULLCOLUMN = DBShopsTableConstants.SHOPS_NAME_COL_FULL;
    @SuppressWarnings("unused")
    private static final String SHOPCITY_FULLCOLUMN = DBShopsTableConstants.SHOPS_CITY_COL_FULL;
    @SuppressWarnings("unused")
    private static final String SHOPORDER_FULLCOLUMN = DBShopsTableConstants.SHOPS_ORDER_COL_FULL;

    @SuppressWarnings("unused")
    private static final String AISLEID_COLUMN = DBAislesTableConstants.AISLES_ID_COL;
    @SuppressWarnings("unused")
    private static final String AILSEID_FULLCOLUMN = DBAislesTableConstants.AISLES_ID_COL_FULL;
    @SuppressWarnings("unused")
    private static final String AISLENAME_COLUMN = DBAislesTableConstants.AISLES_NAME_COL;
    @SuppressWarnings("unused")
    private static final String AISLENAME_FULLCOLUMN = DBAislesTableConstants.AISLES_NAME_COL_FULL;
    @SuppressWarnings("unused")
    private static final String AISLEORDER_COLUMN = DBAislesTableConstants.AISLES_ORDER_COL;
    @SuppressWarnings("unused")
    private static final String AISLEORDER_FULLCOLUMN = DBAislesTableConstants.AISLES_ORDER_COL_FULL;
    @SuppressWarnings("unused")
    private static final String AISLESHOPREF_COLUMN = DBAislesTableConstants.AISLES_SHOPREF_COL;
    @SuppressWarnings("unused")
    private static final String AISLESHOPREF_FULLCOLUMN = DBAislesTableConstants.AISLES_SHOPREF_COL_FULL;

    @SuppressWarnings("unused")
    private static final String PRODUCTID_COLUMN = DBProductsTableConstants.PRODUCTS_ID_COL;
    @SuppressWarnings("unused")
    private static final String PRODUCTNAME_COLUMN = DBProductsTableConstants.PRODUCTS_NAME_COL;
    @SuppressWarnings("unused")
    private static final String PRODUCTID_FULLCOLUMN = DBProductsTableConstants.PRODUCTS_ID_COL_FULL;
    @SuppressWarnings("unused")
    private static final String PRODUCTNAME_FULLCOLUMN = DBProductsTableConstants.PRODUCTS_NAME_COL_FULL;

    @SuppressWarnings("unused")
    private static final String PRODUCTREF_COLUMN = DBProductusageTableConstants.PRODUCTUSAGE_PRODUCTREF_COL;
    @SuppressWarnings("unused")
    private static final String PRODUCTREF_FULLCOLUMN = DBProductusageTableConstants.PRODUCTUSAGE_PRODUCTREF_COL_FULL;
    @SuppressWarnings("unused")
    private static final String AISLEREF_COLUMN = DBProductusageTableConstants.PRODUCTUSAGE_AISLEREF_COL;
    @SuppressWarnings("unused")
    private static final String AISLEREF_FULLCOLUMN = DBProductusageTableConstants.PRODUCTUSAGE_AISLEREF_COL_FULL;
    @SuppressWarnings("unused")
    private static final String COST_COLUMN = DBProductusageTableConstants.PRODUCTUSAGE_COST_COL;
    @SuppressWarnings("unused")
    private static final String COST_FULLCOLUMN = DBProductusageTableConstants.PRODUCTUSAGE_COST_FULL;
    @SuppressWarnings("unused")
    private static final String BUYCOUNT_COLUMN = DBProductusageTableConstants.PRODUCTUSAGE_BUYCOUNT_COL;
    @SuppressWarnings("unused")
    private static final String BUYCOUNT_FULLCOLUMN = DBProductusageTableConstants.PRODUCTUSAGE_BUYCOUNT_COL_FULL;
    @SuppressWarnings("unused")
    private static final String FIRSTBUY_COLUMN = DBProductusageTableConstants.PRODUCTUSAGE_FIRSTBUYDATE_COL;
    @SuppressWarnings("unused")
    private static final String FIRSTBUY_FULLCOLUMN = DBProductusageTableConstants.PRODUCTUSAGE_FIRSTBUYDATE_COL_FULL;
    @SuppressWarnings("unused")
    private static final String LASTBUY_COLUMN = DBProductusageTableConstants.PRODUCTUSAGE_LATESTBUYDATE_COL;
    @SuppressWarnings("unused")
    private static final String LASTBUY_FULLCOLUMN = DBProductusageTableConstants.PRODUCTUSAGE_LATESTBUYDATE_COL_FULL;
    @SuppressWarnings("unused")
    private static final String PRODUCTUSAGEORDER_COLUMN = DBProductusageTableConstants.PRODUCTUSAGE_ORDER_COL;
    @SuppressWarnings("unused")
    private static final String PRODUCTUSAGEORDER_FULLCOLUMN = DBProductusageTableConstants.PRODUCTUSAGE_ORDER_COL_FULL;
    @SuppressWarnings("unused")
    private static final String PRODUCTUSAGECOST_COLUMN = DBProductusageTableConstants.PRODUCTUSAGE_COST_COL;
    @SuppressWarnings("unused")
    private static final String PRODUCTUSAGECOST_FULLCOLUMN = DBProductusageTableConstants.PRODUCTUSAGE_AISLEREF_COL_FULL;
    @SuppressWarnings("unused")
    private static final String CHECKLISTFLAG_COLUMN = DBProductusageTableConstants.PRODUCTUSAGE_CHECKLISTFLAG_COL;
    @SuppressWarnings("unused")
    private static final String CHECKLISTFLAG_FULLCOLUMN = DBProductusageTableConstants.PRODUCTUSAGE_CHECKLISTFLAG_COL_FULL;
    @SuppressWarnings("unused")
    private static final String CHECKLISTCOUNT_COLUMN = DBProductusageTableConstants.PRODUCTUSAGE_CHECKLISTCOUNT_COL;
    @SuppressWarnings("unused")
    private static final String CHECKLISTCOUNT_FULLCOLUMN = DBProductusageTableConstants.PRODUCTUSAGE_CHECKLISTCOUNT_COL_FULL;

    //private static final String filter = SHOPLISTNUMBERTOGET_FULLCOLUMN + " > 0 ";
    @SuppressWarnings("unused")
    private static final String filter = "";
    public static final String THISCLASS = ShoppingEntryAdjustActivity.class.getSimpleName();

    @SuppressLint("SetTextI18n")
    protected void onCreate(Bundle savedInstanceState) {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoppingadjust);
        context = this;
        thisactivity = (Activity) context;
        logmsg = "retrieving IntentExtras";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        caller = getIntent().getStringExtra(
                StandardAppConstants.INTENTKEY_CALLINGACTIVITY
        );
        orig_aisleid = getIntent().getLongExtra(
                StandardAppConstants.INTENTKEY_AISLEID,0
        );
        orig_productid = getIntent().getLongExtra(
                StandardAppConstants.INTENTKEY_PRODUCTID,0
        );
        orig_productname = getIntent().getStringExtra(
                StandardAppConstants.INTENTKEY_PRODUCTNAME
        );
        orig_cost = getIntent().getDoubleExtra(
                StandardAppConstants.INTENTKEY_PUCOST,0
        );
        orig_quantity = getIntent().getIntExtra(
                StandardAppConstants.INTENTKEY_SHOPLISTQUANTITY,0
        );

        logmsg = "Preparing Database";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        dbdao = new DBDAO(this);
        dbshopmethods = new DBShopMethods(this);
        dbaislemethods = new DBAisleMethods(this);
        dbproductmethods = new DBProductMethods(this);
        dbshoplistmethods = new DBShopListMethods(this);
        dbpumethods = new DBProductUsageMethods(this);
        dbrulemethods = new DBRuleMethods(this);

        logmsg = "Preparing ColorCoding";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        originalvaluesheading = (LinearLayout) findViewById(R.id.shoppingadjust_orginalvalues_heading);
        donebutton = (TextView) findViewById(R.id.shoppingadjust_donebutton);
        savebutton = (TextView) findViewById(R.id.shoppingadjust_savebutton);
        undobutton = (TextView) findViewById(R.id.shoppingadjust_undobutton);
        morebutton = (TextView) findViewById(R.id.shoppingadjust_morebutton);
        lessbutton = (TextView) findViewById(R.id.shoppingadjust_lessbutton);
        messagebar = (TextView) findViewById(R.id.shoppingadjust_messagebar);

        orig_productname_tv = (TextView) findViewById(R.id.shoppingadjust_orig_productname);
        orig_quantity_tv = (TextView) findViewById(R.id.shoppingadjust_orig_quantity);
        orig_cost_tv = (TextView) findViewById(R.id.shoppingadjust_orig_cost);
        orig_total_tv = (TextView) findViewById(R.id.shoppingadjust_orig_total);

        orig_productname_lbl = (TextView) findViewById(R.id.shoppingadjust_orig_productname_lbl);
        orig_cost_lbl = (TextView) findViewById(R.id.shoppingadjust_orig_cost_lbl);
        orig_total_lbl = (TextView) findViewById(R.id.shoppingadjust_orig_total_lbl);
        orig_quantity_lbl = (TextView) findViewById(R.id.shoppingadjust_orig_quantity_lbl);

        new_productname_et = (EditText) findViewById(R.id.shoppingadjust_inputproductname);
        new_cost_et = (EditText) findViewById(R.id.shoppingadjust_inputcost);
        new_quantity_et = (EditText) findViewById(R.id.shoppingadjust_inputquantity);

        new_productname_lbl = (TextView) findViewById(R.id.shoppingadjust_inputproductname_label);
        new_cost_lbl = (TextView) findViewById(R.id.shoppingadjust_inputcost_label);
        new_quantity_lbl = (TextView) findViewById(R.id.shoppingadjust_inputquantity_label);

        menucolorcode = StandardAppConstants.INTENTKEY_MENUCOLORCODE;
        passedmenucolorcode = getIntent().getIntExtra(menucolorcode,0);

        actionbar = getSupportActionBar();
        ActionColorCoding.setActionBarColor(this,getIntent(),actionbar);
        primary_color = ActionColorCoding.setHeadingColor(this,getIntent(),0);
        h1 = ActionColorCoding.setHeadingColor(this,getIntent(),1);
        h2 = ActionColorCoding.setHeadingColor(this,getIntent(),2);
        h3 = ActionColorCoding.setHeadingColor(this,getIntent(),3);
        h4 = ActionColorCoding.setHeadingColor(this,getIntent(),4);
        ActionColorCoding.setActionButtonColor(morebutton,primary_color);
        ActionColorCoding.setActionButtonColor(lessbutton,primary_color);
        ActionColorCoding.setActionButtonColor(donebutton,primary_color);
        ActionColorCoding.setActionButtonColor(savebutton,primary_color);
        ActionColorCoding.setActionButtonColor(undobutton,primary_color);
        originalvaluesheading.setBackgroundColor(h1);
        int etbgcol = ActionColorCoding.getGroupColor(passedmenucolorcode,3)
                & ActionColorCoding.transparency_requied;
        int bgcol = ActionColorCoding.getGroupColor(passedmenucolorcode,3) &
                ActionColorCoding.transparency_optional;
        orig_productname_tv.setTextColor(primary_color);
        orig_productname_tv.setText(orig_productname);
        orig_quantity_tv.setTextColor(primary_color);
        orig_quantity_tv.setText(Integer.toString(orig_quantity));
        orig_cost_tv.setTextColor(primary_color);
        orig_cost_tv.setText(Double.toString(orig_cost));
        orig_total_tv.setTextColor(primary_color);
        orig_total_tv.setText(Double.toString(
                orig_cost * orig_quantity
        ));
        orig_productname_lbl.setTextColor(primary_color);
        orig_cost_lbl.setTextColor(primary_color);
        orig_total_lbl.setTextColor(primary_color);
        orig_quantity_lbl.setTextColor(primary_color);
        saved_productname = orig_productname;
        saved_cost = orig_cost;
        saved_quantity = orig_quantity;

        new_productname_lbl.setTextColor(primary_color);
        ActionColorCoding.setActionButtonColor(new_productname_et,etbgcol);
        new_productname_et.setText(orig_productname);
        new_quantity_lbl.setTextColor(primary_color);
        ActionColorCoding.setActionButtonColor(new_quantity_et,etbgcol);
        new_quantity_et.setText(Integer.toString(orig_quantity));
        new_cost_lbl.setTextColor(primary_color);
        ActionColorCoding.setActionButtonColor(new_cost_et,etbgcol);
        new_cost_et.setText(Double.toString(orig_cost));
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
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
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
                break;
        }
        resumestate = StandardAppConstants.RESUMSTATE_NORMAL;
    }

    /**************************************************************************
     * onDestroy - do any clean up before th application is destroyed
     * e.g. close any open cursors and then close the database
     */
    @Override
    protected void onDestroy() {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        super.onDestroy();
    }

    /**************************************************************************
     * Button Click Handler
     *
     * @param view The view (i.e the TextView that was clicked)
     */
    @SuppressWarnings("unused")
    @SuppressLint("SetTextI18n")
    public void actionButtonClick(View view) {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        String msg = "";
        switch (view.getId()) {
            case R.id.shoppingadjust_donebutton:
                logmsg = "Finishing";
                LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
                this.finish();
                break;
            case R.id.shoppingadjust_morebutton:
                new_quantity_et.setText(Integer.toString(
                        Integer.parseInt(
                                new_quantity_et.getText().toString()) + 1)
                );
                msg = "Number to purchase increased by 1";
                setMessage(this,msg,false);
                logmsg = "Increased Quantity by 1";
                LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
                break;
            case R.id.shoppingadjust_lessbutton:
                int tcost = Integer.parseInt(new_quantity_et.getText().toString());
                if (tcost > 0) {
                    tcost = tcost - 1;
                    new_quantity_et.setText(Integer.toString(tcost));
                    msg = "Number to get reduced by 1";
                    setMessage(this,msg,false);

                } else {
                    msg = "Sorry, Number to get cannot be changed to less than 0";
                    setMessage(this,msg,true);
                }
                logmsg = "Decreased Quantity by 1";
                LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
                break;
            case R.id.shoppingadjust_undobutton:
                new_productname_et.setText(orig_productname);
                new_quantity_et.setText(Integer.toString(orig_quantity));
                new_cost_et.setText(Double.toString(orig_cost));
                msg = "Values reset to their original values.";
                setMessage(this,msg,false);
                logmsg = "Reset values to their original values";
                LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
                break;
            case R.id.shoppingadjust_savebutton:
                doSave();
                break;
            default:
                break;
        }
    }

    /**************************************************************************
     *
     */
    public void doSave() {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        boolean updateallok = true;
        String msg = "";
        Emsg emsg;
        if (new_productname_et.getText().toString().length() < 1) {
            msg = "Product name cannot be blank.";
            setMessage(this,msg,true);
            new_productname_et.requestFocus();
            logmsg = "Cannot save ShoppingList entry as the Product Name is blank";
            LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
            return;
        }
        emsg = ValidateInput.validateMonetary(new_cost_et.getText().toString());
        if (emsg.getErrorIndicator()) {
            msg = "Cost " + emsg.getErrorMessage();
            setMessage(this,msg,true);
            new_cost_et.requestFocus();
            logmsg = "Cannot save ShoppingList entry as " + msg;
            LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
            return;
        }
        emsg = ValidateInput.validateInteger(new_quantity_et.getText().toString());
        if (emsg.getErrorIndicator()) {
            msg = "Quantity " + emsg.getErrorMessage();
            setMessage(this,msg,true);
            new_quantity_et.requestFocus();
            logmsg = "Cannot save ShoppingList entry as " + msg;
            LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
            return;
            }
        if (saved_quantity != Integer.parseInt(new_quantity_et.getText().toString())) {
            msg = "Shoplist Entry";
            dbshoplistmethods.addOrUpdateShopListEntry(orig_aisleid, orig_productid,
                    Integer.parseInt(new_quantity_et.getText().toString()) - orig_quantity,
                    true,
                    true);
            if (dbshoplistmethods.ifShopListEntryUpdated()) {
                msg = msg + "Updated OK.";
                saved_quantity = Integer.parseInt(new_quantity_et.getText().toString());
            } else {
                msg = msg + "NOT Updated.";
                updateallok = false;
            }
        }
        if (saved_cost != Double.parseDouble(new_cost_et.getText().toString())) {
            if (msg.length() > 0) {
                msg = msg + " ";
            }
            msg = msg + "Stock cost. ";
            dbpumethods.amendProductUsageCost(orig_aisleid, orig_productid,
                    Double.parseDouble(new_cost_et.getText().toString()));
            if (dbpumethods.ifProductUsageUpdated()) {
                msg = msg + "Updated OK.";
                saved_cost = Double.parseDouble(new_cost_et.getText().toString());
            } else {
                msg = msg + "NOT Updated.";
                updateallok = false;
            }
        }
        if (!saved_productname.equals(new_productname_et.getText().toString())) {
            if (msg.length() > 0 ) {
                msg = msg + " ";
            }
            msg = msg + "Prodcut Name. ";
            dbproductmethods.modifyProduct(orig_productid,
                    new_productname_et.getText().toString(),
                    "",
                    0,
                    0);
            if (dbproductmethods.ifProductUpdated()) {
                msg = msg + "Updated OK.";
                saved_productname = new_productname_et.getText().toString();
            } else {
                msg = msg + "NOT Updated.";
                updateallok = false;
            }
        }
        if (msg.length() > 0 ) {
            setMessage(this,msg,!updateallok);
        } else {
            setMessage(this,"Nothing changed, so nothing was done",true);
        }
        logmsg = "ShopingList entry updated=" + Boolean.toString(updateallok);
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
    }

    /**************************************************************************
     * setMessage - set the Message regarding the lat action performed
     * Note as an action may have altered the database getDBcounts
     * is called and the title changed
     *
     * @param seaa This activity
     * @param msg  The message to be displayed.
     * @param flag Message imnportant, if true Yellow text, esle green
     */
    public void setMessage(ShoppingEntryAdjustActivity seaa, String msg, boolean flag) {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);

        TextView messagebar = (TextView) seaa.findViewById(
                R.id.shoppingadjust_messagebar);
        messagebar.setText(context.getResources().getString(
                R.string.messagebar_prefix_lastaction) + " " + msg);
        if (flag) {
            messagebar.setTextColor(Color.YELLOW);
        } else {
            messagebar.setTextColor(Color.GREEN);
        }
        messagebar.setVisibility(View.VISIBLE);
    }
}
