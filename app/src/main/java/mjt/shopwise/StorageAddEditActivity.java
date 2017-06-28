package mjt.shopwise;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import static mjt.shopwise.ActionColorCoding.transparency_requied;

import static mjt.sqlwords.SQLKWORD.*;

/**
 * Add or Edit Storage
 */

@SuppressWarnings({"FieldCanBeLocal", "WeakerAccess", "unused"})
public class StorageAddEditActivity extends AppCompatActivity {

    @SuppressWarnings("unused")
    private static final String THIS_ACTIVITY = "StorageAddEditActivity";
    @SuppressWarnings("unused")
    private static String caller;
    private static int calledmode;
    /**
     * Sorting Shoplist columns
     */
    private static final int BYSTORAGE = 0;
    private static final int BYORDER = 2;
    private static final String SORTASCENDING = SQLORDERASCENDING;
    private static final String SORTDESCENDING = SQLORDERDESCENDING;
    /**
     * The Context.
     */
    Context context;
    /**
     * The Actionbar.
     */
    ActionBar actionbar;

    /**
     * Colours
     */
    private static int h1;
    private static int h2;
    @SuppressWarnings("unused")
    private static int h3;
    @SuppressWarnings("unused")
    private static int h4;
    private static int primary_color;
    public static final String THISCLASS = StorageAddEditActivity.class.getSimpleName();
    private static final String LOGTAG = "SW_STAEA";

    @SuppressWarnings("unused")
    LinearLayout inputstoragename_linearlayout;
    TextView inputstoragename_label;
    EditText inputstoragename;

    @SuppressWarnings("unused")
    LinearLayout inputstorageorder_linearlayout;
    TextView inputstorageorder_label;
    EditText inputstorageorder;

    @SuppressWarnings("unused")
    LinearLayout storageaddedit_buttons_linearlayout;
    TextView savebutton;
    TextView donebutton;

    LinearLayout storagelist_heading;
    @SuppressWarnings("unused")
    TextView storagelist_storagename;
    @SuppressWarnings("unused")
    TextView storagelist_storageorder;
    ListView storagelist;
    AdapterStorageList storagelistadapter;
    TextView sortable;
    TextView clickable;
    TextView longclickable;

    @SuppressWarnings("unused")
    DBDAO dbdao;
    DBStorageMethods dbstoragemethods;
    Cursor stcsr;

    @SuppressWarnings("unused")
    private static final String STORAGEID_COLUMN =
            DBStorageTableConstants.STORAGE_ID_COL;
    @SuppressWarnings("unused")
    private static final String STORAGENAME_COLUMN =
            DBStorageTableConstants.STORAGE_NAME_COL;
    @SuppressWarnings("unused")
    private static final String STORAGEORDER_COLUMN =
            DBStorageTableConstants.STORAGE_ORDER_COL;
    @SuppressWarnings("unused")
    private static final String STORAGEID_FULLCOLUMN =
            DBStorageTableConstants.STORAGE_ID_COL_FULL;
    private static final String STORAGENAME_FULLCOLUMN =
            DBStorageTableConstants.STORAGE_NAME_COL_FULL;
    private static final String STORAGEORDER_FULLCOLUMN =
            DBStorageTableConstants.STORAGE_ORDER_COL_FULL;

    static String orderby = STORAGENAME_FULLCOLUMN + SORTASCENDING;
    static int orderfld = BYSTORAGE;
    static boolean ordertype = true;
    static boolean sortchanged = false;
    static String lastmessage = "";

    long passedstorageid;
    String passedstoragename;
    int passedstorageorder;

    private int resumestate = StandardAppConstants.RESUMSTATE_NORMAL;
    @SuppressWarnings("unused")
    private Activity thisactivity;


    /**************************************************************************
     *
     * @param savedInstanceState    The Bundle holding the savedInstanceState
     */
    @SuppressLint("SetTextI18n")
    protected void onCreate(Bundle savedInstanceState) {
        String logmsg = "Invoked";
        String methodname = new Object() {
        }.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                LOGTAG,
                logmsg,
                THISCLASS,
                methodname
        );
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storeageaddedit);
        context = this;
        thisactivity = (Activity) context;

        logmsg = "Preparing ColorCoding";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                LOGTAG,
                logmsg,
                THISCLASS,
                methodname);

        inputstoragename_linearlayout = (LinearLayout) findViewById(
                R.id.inputstoragename_linearlayout);
        inputstoragename_label = (TextView) findViewById(
                R.id.inputstoragename_label);
        inputstoragename = (EditText) findViewById(R.id.inputstoragename);
        inputstorageorder_linearlayout = (LinearLayout) findViewById(
                R.id.inputstorageorder_linearlayout);
        inputstorageorder_label = (TextView) findViewById(
                R.id.inputstorageorder_label);
        inputstorageorder = (EditText) findViewById(R.id.inputstorageorder);

        savebutton = (TextView) findViewById(R.id.storageaddedit_savebutton);
        donebutton = (TextView) findViewById(R.id.storageaddedit_donebutton);

        storagelist_heading =
                (LinearLayout) findViewById(
                        R.id.storageaddedit_storagelist_heading);
        storagelist_storagename =
                (TextView) findViewById(
                        R.id.storageaddedit_storagelist_heading_storagename
                );
        storagelist_storageorder =
                (TextView) findViewById(
                        R.id.storageaddedit_storagelist_heading_storageorder
                );
        storagelist = (ListView) findViewById(R.id.storageaddedit_storagelist);
        sortable = (TextView) findViewById(R.id.sortable);
        clickable = (TextView) findViewById(R.id.clickable);
        longclickable = (TextView) findViewById(R.id.longclickable);

        actionbar = getSupportActionBar();
        ActionColorCoding.setActionBarColor(this, getIntent(), actionbar);
        primary_color = ActionColorCoding.setHeadingColor(this, getIntent(), 0);
        h1 = ActionColorCoding.setHeadingColor(this, getIntent(), 1);
        h2 = ActionColorCoding.setHeadingColor(this, getIntent(), 2);
        h3 = ActionColorCoding.setHeadingColor(this, getIntent(), 3);
        h4 = ActionColorCoding.setHeadingColor(this, getIntent(), 4);
        ActionColorCoding.setActionButtonColor(donebutton, primary_color);
        ActionColorCoding.setActionButtonColor(savebutton, primary_color);
        ActionColorCoding.setActionButtonColor(inputstoragename,
                h2 & transparency_requied);
        ActionColorCoding.setActionButtonColor(inputstorageorder,
                h2 & transparency_requied);
        inputstoragename_label.setTextColor(h1);
        inputstorageorder_label.setTextColor(h1);
        storagelist_heading.setBackgroundColor(h1);
        sortable.setTextColor(primary_color);
        clickable.setVisibility(View.INVISIBLE);
        longclickable.setVisibility(View.INVISIBLE);

        logmsg = "Preparing Database";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                LOGTAG,
                logmsg,
                THISCLASS,
                methodname
        );

        dbstoragemethods = new DBStorageMethods(this);

        logmsg = "Retrieving StorageList";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                LOGTAG,
                logmsg,
                THISCLASS,
                methodname);
        stcsr = dbstoragemethods.getStorage("", orderby);
        storagelistadapter = new AdapterStorageList(this,
                stcsr,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER,
                this.getIntent(),
                false,
                false,
                false);
        storagelist.setAdapter(storagelistadapter);

        logmsg = "Retrieving Intent Extras";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                LOGTAG,
                logmsg,
                THISCLASS,
                methodname
        );
        caller = getIntent().getStringExtra(
                StandardAppConstants.INTENTKEY_CALLINGACTIVITY
        );
        calledmode = getIntent().getIntExtra(
                StandardAppConstants.INTENTKEY_CALLINGMODE, 0
        );
        switch (calledmode) {
            case StandardAppConstants.CM_ADD:
                setNewOrder(inputstorageorder);
                actionbar.setTitle(getResources().getString(R.string.storageaddlabel));
                break;
            case StandardAppConstants.CM_EDIT:
                passedstoragename = getIntent().getStringExtra(
                        StandardAppConstants.INTENTKEY_STORAGENAME
                );
                passedstorageid = getIntent().getLongExtra(
                        StandardAppConstants.INTENTKEY_STORAGEID,0
                );
                passedstorageorder = getIntent().getIntExtra(
                        StandardAppConstants.INTENTKEY_STORAGEORDER,0
                );
                inputstoragename.setText(passedstoragename);
                inputstorageorder.setText(Integer.toString(passedstorageorder));
                actionbar.setTitle(getResources().getString(R.string.storageeditlabel));
                break;
        }
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
        String methodname = new Object() {
        }.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL, LOGTAG, logmsg, THISCLASS, methodname);
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
        logmsg = "Refreshing ShopList";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL, LOGTAG, logmsg, THISCLASS, methodname);
        stcsr = dbstoragemethods.getStorage("", orderby);
        storagelistadapter.swapCursor(stcsr);
        resumestate = StandardAppConstants.RESUMSTATE_NORMAL;
    }

    /**************************************************************************
     * onDestroy - do any clean up before th application is destroyed
     * e.g. close any open cursors and then close the database
     */
    @Override
    protected void onDestroy() {
        String logmsg = "Invoked";
        String methodname = new Object() {
        }.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL, LOGTAG, logmsg, THISCLASS, methodname);
        super.onDestroy();
        stcsr.close();
    }

    /**************************************************************************
     * Button Click Handler
     *
     * @param view The view (i.e the TextView that was clicked)
     */
    @SuppressWarnings("unused")
    public void actionButtonClick(View view) {
        String logmsg = "Invoked";
        String methodname =
                new Object() {}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                LOGTAG,
                logmsg,
                THISCLASS,
                methodname
        );
        switch (view.getId()) {
            case R.id.storageaddedit_donebutton:
                logmsg = "Finishing";
                LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                        LOGTAG,
                        logmsg,
                        THISCLASS,
                        methodname);
                this.finish();
                break;
            case R.id.storageaddedit_savebutton:
                switch (calledmode) {
                    case StandardAppConstants.CM_ADD:
                        addStorage();
                        break;
                    case StandardAppConstants.CM_EDIT:
                        updateStorage(passedstorageid,
                                passedstoragename,
                                passedstorageorder
                        );
                        break;
                }
                break;
            default:
                break;
        }
    }

    /**************************************************************************
     *
     * @param storageid         ID of the Storage Item to update
     * @param originalname      The original name passed
     * @param originalorder     The original order passed
     */
    private void updateStorage(long storageid,
                               String originalname,
                               int originalorder) {

        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                LOGTAG,
                logmsg,
                THISCLASS,
                methodname
        );
        boolean updateattempted = false;
        String newstoragename = inputstoragename.getText().toString();
        int newstorageorder = Integer.valueOf(
                inputstorageorder.getText().toString()
        );
        if ((newstorageorder != originalorder) && !(newstoragename.equals(originalname))) {
            updateattempted = true;
            dbstoragemethods.modifyStorage(storageid, newstorageorder, newstoragename);
        } else {
            if (newstorageorder != originalorder) {
                updateattempted = true;
                dbstoragemethods.modifyStorage(storageid,newstorageorder,"");
            }
            if (!newstoragename.equals(originalname)) {
                updateattempted = true;
                dbstoragemethods.modifyStorage(storageid,0,newstoragename);
            }
        }
        if (updateattempted) {
            if (dbstoragemethods.ifStorageUpdate()) {
                setMessage(this,"Storage Updated",false);
                passedstorageorder = newstorageorder;
                passedstoragename = newstoragename;
                stcsr = dbstoragemethods.getStorage("",orderby);
                storagelistadapter.swapCursor(stcsr);
            } else {
                setMessage(this,"Storage NOT Updated", true);
            }
        } else {
            setMessage(this,"Storage NOT Updated, nothing has changed.", false);
        }
    }

    /**************************************************************************
     *
     */
    private void addStorage() {
        String logmsg = "Invoked";
        String methodname =
                new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                LOGTAG,
                logmsg,
                THISCLASS,
                methodname
        );
        String storagename = inputstoragename.getText().toString();
        String storageorder = inputstorageorder.getText().toString();
        if (storagename.length() < 1) {
            setMessage(this,"Storage Name cannot be blank.",true);
            inputstoragename.requestFocus();
            return;
        }
        Emsg sovalid = ValidateInput.validateInteger(storageorder);
        if (sovalid.getErrorIndicator()) {
            setMessage(this,sovalid.getErrorMessage(),true);
            inputstorageorder.requestFocus();
            return;
        }
        logmsg = "Validation passed. Attempting addition of " +storagename +
                " Order=" + storageorder;
        dbstoragemethods.insertStorage(storagename,Integer.valueOf(storageorder));
        if (dbstoragemethods.ifStorageAdded()) {
            setMessage(this,storagename + " Added OK.",false);
        } else {
            setMessage(this,storagename + " Not Added", true);
        }
        stcsr = dbstoragemethods.getStorage("",orderby);
        storagelistadapter.swapCursor(stcsr);
        setNewOrder(inputstorageorder);
    }

    /**************************************************************************
     * sortClick - Handle list heading clicks (i.e. sort by that field)
     *
     * @param view the view that was clicked
     */
    @SuppressWarnings("unused")
    public void sortClick(View view) {
        String logmsg = "Invoked";
        String methodname =
                new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                LOGTAG,
                logmsg,
                THISCLASS,
                methodname
        );
        lastmessage = "List of Storage sorted by ";
        switch (view.getId()) {
            case R.id.storageaddedit_storagelist_heading_storagename:
                getOrderBy(STORAGENAME_FULLCOLUMN,BYSTORAGE);
                lastmessage = lastmessage + " STORAGE NAME in ";
                break;
            case R.id.storageaddedit_storagelist_heading_storageorder:
                getOrderBy(STORAGEORDER_FULLCOLUMN,BYORDER);
                lastmessage = lastmessage + " STORAGE ORDER in";
            default:
                break;
        }
        if (sortchanged) {
            stcsr = dbstoragemethods.getStorage("",orderby);
            storagelistadapter.swapCursor(stcsr);
            if (ordertype) {
                lastmessage = lastmessage + " ascending order.";
            } else {
                lastmessage = lastmessage + " descending order.";
            }
            setMessage(this,lastmessage,false);
        }
    }

    /**************************************************************************
     * setMessage - set the Message regarding the lat action performed
     * Note as an action may have altered the database getDBcounts
     * is called and the title changed
     *
     * @param sae  the sae
     * @param msg  The message to be displayed.
     * @param flag Message imnportant, if true Yellow text, esle green
     */
    public void setMessage(StorageAddEditActivity sae, String msg, boolean flag) {
        String logmsg = "Invoked";
        String methodname = new Object() {
        }.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL, LOGTAG, logmsg, THISCLASS, methodname);

        TextView messagebar = (TextView) sae.findViewById(R.id.storageaddedit_messagebar);
        messagebar.setText(context.getResources().getString(
                R.string.messagebar_prefix_lastaction) + " " + msg);
        if (flag) {
            messagebar.setTextColor(Color.YELLOW);
        } else {
            messagebar.setTextColor(Color.GREEN);
        }
        messagebar.setVisibility(View.VISIBLE);
    }

    /**************************************************************************
     * getOrderBy - Generate the new ORDEY BY sql (ORDER BY already exists)
     *
     * @param newcolumn   the DB column to sort by
     * @param neworderfld the column as an integer as per constants
     */
    private void getOrderBy(String newcolumn, int neworderfld) {
        String logmsg = "Invoked";
        String methodname = new Object() {
        }.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL, LOGTAG, logmsg, THISCLASS, methodname);

        orderby = newcolumn;
        // If already sorted by this column then toggle between ascedning and
        // descending.
        // If not then default to ascending
        if (orderfld == neworderfld) {
            if (ordertype) {
                orderby = orderby + SORTDESCENDING;
                ordertype = false;
            } else {
                orderby = orderby + SORTASCENDING;
                ordertype = true;
            }
        } else {
            orderby = orderby + SORTASCENDING;
            ordertype = true;
        }
        orderfld = neworderfld;
        sortchanged = true;
    }

    /**************************************************************************
     *
     * @param edittext      the EditText holding the storage locations Order
     */
    @SuppressLint("SetTextI18n")
    private void setNewOrder(EditText edittext) {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        int highorder = dbstoragemethods.getHighestStorageOrder() + 100;
        if (highorder < 1000 ) {
            highorder = 1000;
        }
        if (highorder > 9999) {
            highorder = highorder - 100 + 1;
            if (highorder > 9999) {
                highorder = 9999;
            }
        }
        edittext.setText(Integer.toString(highorder));
        logmsg = "New Highorder=" + Integer.toString(highorder);
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
    }
}
