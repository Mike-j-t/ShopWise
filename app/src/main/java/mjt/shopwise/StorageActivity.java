package mjt.shopwise;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Storage Activity
 */

@SuppressWarnings({"FieldCanBeLocal", "WeakerAccess", "unused"})
public class StorageActivity extends AppCompatActivity {

    private static final String THIS_ACTIVITY = "StorageActivity";
    @SuppressWarnings("unused")
    private static String caller;
    @SuppressWarnings("unused")
    private static int calledmode;
    public static final String THISCLASS = StorageActivity.class.getSimpleName();
    private static final String LOGTAG = "SW_STA";

    private static final int BYNAME = 0;
    private static final int BYORDER = 1;
    private static final String SORTASCENDING = DBConstants.SQLORDERASCENDING;
    private static final String SORTDESCENDING = DBConstants.SQLORDERDESCENDING;

    Context context;
    ActionBar actionbar;

    private static int h1;
    @SuppressWarnings("unused")
    private static int h2;
    @SuppressWarnings("unused")
    private static int h3;
    @SuppressWarnings("unused")
    private static int h4;
    private static int primary_color;
    private String menucolorcode;
    int passedmenucolorcode;

    TextView donebutton;
    TextView newbutton;
    TextView messagebar;
    ListView storagelist;
    LinearLayout storagelistheading;
    AdapterStorageList storagelistadapter;

    @SuppressWarnings("unused")
    DBDAO dbdao;
    DBStorageMethods dbstoragemethods;
    Cursor stcsr;

    private static final String STORAGEID_COLUMN =
            DBStorageTableConstants.STORAGE_ID_COL;
    private static final String STORAGENAME_COLUMN =
            DBStorageTableConstants.STORAGE_NAME_COL;
    private static final String STORAGEORDER_COLUMN =
            DBStorageTableConstants.STORAGE_ORDER_COL;

    static String orderby = STORAGENAME_COLUMN + SORTASCENDING;
    static int orderfld = BYNAME;
    static boolean ordertype = true;
    static boolean sortchanged = false;
    static String lastmessage = "";

    private int resumestate = StandardAppConstants.RESUMSTATE_NORMAL;
    private Activity thisactivity;

    @Override
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
        setContentView(R.layout.activity_storage);
        context = this;
        thisactivity = (Activity) context;

        logmsg = "Retrieving IntentExtras";
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

        logmsg = "Preparing ColorCoding";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                LOGTAG,
                logmsg,
                THISCLASS,
                methodname
        );
        menucolorcode = StandardAppConstants.INTENTKEY_MENUCOLORCODE;
        passedmenucolorcode = getIntent().getIntExtra(menucolorcode, 0);

        messagebar = (TextView) findViewById(R.id.storage_messagebar);
        donebutton = (TextView) findViewById(R.id.storage_donebutton);
        newbutton = (TextView) findViewById(R.id.storage_newstorgaebutton);
        storagelistheading =
                (LinearLayout) findViewById(R.id.storage_storagelist_heading);
        storagelist = (ListView) findViewById(R.id.storage_storagelist);


        actionbar = getSupportActionBar();
        ActionColorCoding.setActionBarColor(this, getIntent(), actionbar);
        primary_color = ActionColorCoding.setHeadingColor(this, getIntent(), 0);
        h1 = ActionColorCoding.setHeadingColor(this, getIntent(), 1);
        h2 = ActionColorCoding.setHeadingColor(this, getIntent(), 2);
        h3 = ActionColorCoding.setHeadingColor(this, getIntent(), 3);
        h4 = ActionColorCoding.setHeadingColor(this, getIntent(), 4);
        ActionColorCoding.setActionButtonColor(donebutton, primary_color);
        ActionColorCoding.setActionButtonColor(newbutton, primary_color);
        storagelistheading.setBackgroundColor(h1);

        logmsg = "Preparing Database";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                LOGTAG,
                logmsg,
                THISCLASS,
                methodname
        );

        dbstoragemethods = new DBStorageMethods(this);
        stcsr = dbstoragemethods.getStorage("", orderby);
        storagelistadapter = new AdapterStorageList(this,
                stcsr,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER,
                getIntent(),
                false);
        storagelist.setAdapter(storagelistadapter);

        storagelist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(
                    AdapterView<?> parent,
                    View view,
                    int position,
                    long id) {
                listItemLongClick(view, position, id);
                return true;
            }
        });
        storagelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view,
                                    int position,
                                    long id) {
                listItemClick(view, position, id);
            }
        });
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
                messagebar.setVisibility(View.INVISIBLE);
                break;
        }
        logmsg = "Refreshing StorageList";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL, LOGTAG, logmsg, THISCLASS, methodname);
        stcsr = dbstoragemethods.getStorage("",orderby);
        storagelistadapter.swapCursor(stcsr);
        resumestate = StandardAppConstants.RESUMSTATE_NORMAL;
        this.setTitle(getResources().getString(R.string.storagelabel));
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
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        switch (view.getId()) {
            case R.id.storage_donebutton:
                logmsg = "Finishing";
                LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
                this.finish();
                break;
            case R.id.storage_newstorgaebutton:
                addStorage();
                break;
            default:
                break;
        }
    }

    /**************************************************************************
     *
     */
    public void addStorage() {
        String logmsg = "Invoked";
        String methodname =
                new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                LOGTAG,
                logmsg,
                THISCLASS,
                methodname
        );
        Intent intent = new Intent(this,StorageAddEditActivity.class);
        intent.putExtra(menucolorcode,passedmenucolorcode);
        intent.putExtra(StandardAppConstants.INTENTKEY_CALLINGACTIVITY,
                THIS_ACTIVITY);
        intent.putExtra(
                StandardAppConstants.INTENTKEY_CALLINGMODE,
                StandardAppConstants.CM_ADD);
        logmsg = "Starting " + StorageAddEditActivity.class.getSimpleName() +
                "in ADD mode.";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                LOGTAG,
                logmsg,
                THISCLASS,
                methodname
        );
        startActivity(intent);
    }

    /**************************************************************************
     *
     * @param values parameters passed
     */
    @SuppressWarnings("unused")
    public void storageUpdate(RequestDialogParameters values) {
        String logmsg = "Invoked";
        String methodname =
                new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                LOGTAG,
                logmsg,
                THISCLASS,
                methodname
        );

        Intent intent = new Intent(this,StorageAddEditActivity.class);
        intent.putExtra(menucolorcode,passedmenucolorcode);
        intent.putExtra(StandardAppConstants.INTENTKEY_CALLINGACTIVITY,
                THIS_ACTIVITY);
        intent.putExtra(
                StandardAppConstants.INTENTKEY_CALLINGMODE,
                StandardAppConstants.CM_EDIT
        );
        intent.putExtra(
                StandardAppConstants.INTENTKEY_STORAGEID,
                stcsr.getLong(stcsr.getColumnIndex(STORAGEID_COLUMN))
        );
        intent.putExtra(
                StandardAppConstants.INTENTKEY_STORAGENAME,
                stcsr.getString(stcsr.getColumnIndex(STORAGENAME_COLUMN))
        );
        intent.putExtra(
                StandardAppConstants.INTENTKEY_STORAGEORDER,
                stcsr.getInt(stcsr.getColumnIndex(STORAGEORDER_COLUMN))
        );
        logmsg = "Starting " + StorageAddEditActivity.class.getSimpleName() +
                "in ADD mode.";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                LOGTAG,
                logmsg,
                THISCLASS,
                methodname
        );
        startActivity(intent);
    }

    /**************************************************************************
     *
     * @param values    The paaremeters passed from the requestdialog
     */
    @SuppressWarnings("unused")
    public void storageDelete(RequestDialogParameters values) {
        String logmsg = "Invoked";
        String methodname =
                new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                LOGTAG,
                logmsg,
                THISCLASS,
                methodname);
        Activity activity = (values.getPassedactivity());
        StorageActivity sa = (StorageActivity) activity;
        sa.dbstoragemethods.deleteStorage(values.getLong1());
        sa.stcsr = dbstoragemethods.getStorage("",orderby);
        sa.storagelistadapter.swapCursor(stcsr);
        sa.setMessage(sa,"Storage Deleted",true);
    }

    /**************************************************************************
     *
     * @param view      The view that was clicked
     * @param position  The position of the view in the list
     * @param id        The id from the underlying cursor
     */
    private void listItemClick(View view, int position, long id) {
        String logmsg = "Invoked";
        String methodname =
                new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                LOGTAG,
                THISCLASS,
                methodname
        );
        long storageid = stcsr.getLong(
                stcsr.getColumnIndex(STORAGEID_COLUMN));
        String storagename = stcsr.getString(
                stcsr.getColumnIndex(STORAGENAME_COLUMN));
        int storageorder = stcsr.getInt(
                stcsr.getColumnIndex(STORAGEORDER_COLUMN));

        String classname = this.getClass().getCanonicalName();
        String title = "Edit Storage - " + storagename;
        String message = "Editing Storage = " + storagename;
        String positivebuttontext = getResources().getString(R.string.editbutton);
        String positiveaction = "storageUpdate";
        String negativebuttontext = "";
        String negativeaction = "";
        String neutralbuttontext = getResources().getString(R.string.cancelbutton);
        String neutralaction = "";

        MixTripleLongTripleInt values = new MixTripleLongTripleInt();
        values.setMIXTRPPLONGINT(storageid,0,0,0,0,0);

        new RequestDialog().requestDialog(thisactivity, classname,
                title,message,
                positivebuttontext,negativebuttontext,neutralbuttontext,
                positiveaction,negativeaction,neutralaction,
                values);

    }

    /**************************************************************************
     *
     * @param view      The view that was clicked
     * @param position  The position of the view in the list
     * @param id        The id from the underlying cursor
     */
    private void listItemLongClick(View view, int position, long id) {
        String logmsg = "Invoked";
        String methodname =
                new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                LOGTAG,
                THISCLASS,
                methodname
        );
        long storageid = stcsr.getLong(
                stcsr.getColumnIndex(STORAGEID_COLUMN));
        String storagename = stcsr.getString(
                stcsr.getColumnIndex(STORAGENAME_COLUMN));
        int storageorder = stcsr.getInt(
                stcsr.getColumnIndex(STORAGEORDER_COLUMN));
        if (!dbstoragemethods.isStorageEmpty(storageid)) {
            logmsg = "Storage - " + storagename + " is not empty";
            LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                    LOGTAG,
                    logmsg,
                    THISCLASS,
                    methodname
            );
            setMessage(this,"Storage - " + storagename + " not Deleted, it is not empty.",true);
            return;
        }
        String classname = this.getClass().getCanonicalName();
        String title = "Delete Storage - " + storagename;
        String message = "Deleting Storage = " + storagename;
        String positivebuttontext = getResources().getString(R.string.deletebutton);
        String positiveaction = "storageDelete";
        String negativebuttontext = "";
        String negativeaction = "";
        String neutralbuttontext = getResources().getString(R.string.cancelbutton);
        String neutralaction = "";

        MixTripleLongTripleInt values = new MixTripleLongTripleInt();
        values.setMIXTRPPLONGINT(storageid,0,0,0,0,0);

        new RequestDialog().requestDialog(thisactivity, classname,
                title,message,
                positivebuttontext,negativebuttontext,neutralbuttontext,
                positiveaction,negativeaction,neutralaction,
                values);
    }

    /**************************************************************************
     * sortClick - Handle list heading clicks (i.e. sort by that field)
     *
     * @param view the view that was clicked
     */
    @SuppressWarnings("unused")
    public void sortClick(View view) {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        lastmessage = getResources().getString(R.string.storagelabel) +
                " sorted by ";
        switch (view.getId()) {
            case R.id.storage_storagelist_heading_storagename:
                getOrderBy(STORAGENAME_COLUMN,BYNAME);
                lastmessage = lastmessage + " STORAGE NAME (";
                break;
            case R.id.storage_storagelist_heading_storageorder:
                getOrderBy(STORAGEORDER_COLUMN,BYORDER);
                lastmessage = lastmessage + " STORAGE ORDER (";
            default:
                break;
        }
        if (sortchanged) {
            stcsr =  dbstoragemethods.getStorage("",orderby);
            storagelistadapter.swapCursor(stcsr);
            if (ordertype) {
                lastmessage = lastmessage + "ascending)";
            } else {
                lastmessage = lastmessage + "descending)";
            }
            setMessage(this,lastmessage,false);
        }
    }

    /**************************************************************************
     * setMessage - set the Message regarding the lat action performed
     * Note as an action may have altered the database getDBcounts
     * is called and the title changed
     *
     * @param sa   the sa
     * @param msg  The message to be displayed.
     * @param flag Message imnportant, if true Yellow text, esle green
     */
    public void setMessage(StorageActivity sa, String msg, boolean flag) {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);

        TextView messagebar = (TextView) sa.findViewById(R.id.storage_messagebar);
        messagebar.setText(context.getResources().getString(
                R.string.messagebar_prefix_lastaction) + " " + msg);
        if (flag) {
            messagebar.setTextColor(Color.YELLOW);
        } else {
            messagebar.setTextColor(Color.GREEN);
        }
        messagebar.setVisibility(View.VISIBLE);
        sa.actionbar.setTitle(getResources().getString(R.string.shopslabel));
    }

    /**************************************************************************
     * getOrderBy - Generate the new ORDEY BY sql (ORDER BY already exists)
     * @param newcolumn     the DB column to sort by
     * @param neworderfld   the column as an integer as per constants
     */
    private void getOrderBy(String newcolumn, int neworderfld) {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
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

}