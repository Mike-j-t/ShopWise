package mjt.shopwise;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import static mjt.shopwise.ActionColorCoding.transparency_optional;
import static mjt.shopwise.ActionColorCoding.transparency_requied;

/**
 * Created by Mike092015 on 13/12/2016.
 */

public class ProductsAddEditActivity  extends AppCompatActivity {

    private static final String THIS_ACTIVITY = "ProductsAddEditActivity";
    private static String caller;
    private static int calledmode;
    //private static String defaultorder = "1000";

    private static final int BYPRODUCT = 0;
    private static final String SORTASCENDING = DBConstants.SQLORDERASCENDING;
    private static final String SORTDESCENDING = DBConstants.SQLORDERDESCENDING;
    private static String productfilter = "";

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

    LinearLayout inputproductname_linearlayout;
    TextView inputproductname_label;
    EditText inputproductname;
    LinearLayout productaddeditbuttons_linearlayout;
    TextView savebutton;
    TextView donebutton;
    LinearLayout productfilterheading;
    TextView productfilterlabel;
    EditText inputproductfilter;
    LinearLayout productlistheading;
    TextView productlistproductname;
    ListView productlist;
    AdapterProductList productlistadapter;

    DBDAO dbdao;
    DBShopMethods dbshopmethods;
    DBAisleMethods dbaislemethods;
    DBProductMethods dbproductmethods;
    Cursor plcsr;

    long passedproductid;
    String passedproductname;

    private static final String PRODUCTID_COLUMN = DBProductsTableConstants.PRODUCTS_ID_COL;
    private static final String PRODUCTNAME_COLUMN = DBProductsTableConstants.PRODUCTS_NAME_COL;
    private static final String PRODUCTID_FULLCOLUMN = DBProductsTableConstants.PRODUCTS_ID_COL_FULL;
    private static final String PRODUCTNAME_FULLCOLUMN = DBProductsTableConstants.PRODUCTS_NAME_COL_FULL;

    static String orderby = PRODUCTNAME_FULLCOLUMN + SORTASCENDING;
    static int orderfld = BYPRODUCT;
    static boolean ordertype = true;
    static boolean sortchanged = false;

    static String lastmessage = "";
    static String currentproductname = "";

    static int shopcount = 0;
    static int aislecount = 0;
    static int productcount = 0;

    private int resumestate = StandardAppConstants.RESUMSTATE_NORMAL;
    private Activity thisactivity;
    public static final String THISCLASS = ProductsAddEditActivity.class.getSimpleName();
    private static final String LOGTAG = "SW_PAEA";

    protected void onCreate(Bundle savedInstanceState) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productsaddedit);
        context = this;
        thisactivity = (Activity) context;

        msg = "Preparing ColorCoding";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        inputproductname_linearlayout = (LinearLayout) findViewById(R.id.inputproductname_linearlayout);
        inputproductname_label = (TextView) findViewById(R.id.inputproductname_label);
        inputproductname = (EditText) findViewById(R.id.inputproductname);
        addFilterListener(inputproductname);
        inputproductfilter = (EditText) findViewById(R.id.productaddedit_inputfilter);
        addFilterListener(inputproductfilter);
        savebutton = (TextView) findViewById(R.id.productaddedit_savebutton);
        donebutton = (TextView) findViewById(R.id.productaddedit_donebutton);
        productfilterlabel = (TextView) findViewById(R.id.productaddedit_productfilterlabel);
        productlistheading = (LinearLayout) findViewById(R.id.productaddedit_productlist_heading);
        productlistproductname = (TextView) findViewById(R.id.productaddedit_productlist_heading_productname);
        productlist = (ListView) findViewById(R.id.productaddedit_productlist);

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
        ActionColorCoding.setActionButtonColor(donebutton, primary_color);
        ActionColorCoding.setActionButtonColor(savebutton, primary_color);
        ActionColorCoding.setActionButtonColor(inputproductname,h2 & transparency_requied);
        ActionColorCoding.setActionButtonColor(inputproductfilter,h4 & transparency_optional);
        productlistheading.setBackgroundColor(h1);
        inputproductname_label.setTextColor(primary_color);
        productfilterlabel.setTextColor(h2);

        msg = "Preparing DataBases";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        dbdao = new DBDAO(this);
        dbshopmethods = new DBShopMethods(this);
        dbaislemethods = new DBAisleMethods(this);
        dbproductmethods = new DBProductMethods(this);
        setDBCounts();
        this.setTitle(getResources().getString(R.string.productslabel));

        msg = "Preparing ProductList";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        plcsr = dbproductmethods.getProducts(productfilter,orderby);
        productlistadapter = new AdapterProductList(this,
                plcsr,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER,
                this.getIntent(),
                false
        );
        productlist.setAdapter(productlistadapter);

        msg = "Extracting and applying Intent Extras";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        caller = getIntent().getStringExtra(
                StandardAppConstants.INTENTKEY_CALLINGACTIVITY
        );
        calledmode = getIntent().getIntExtra(
                StandardAppConstants.INTENTKEY_CALLINGMODE,
                StandardAppConstants.CM_CLEAR
        );
        if (calledmode == StandardAppConstants.CM_EDIT) {
            passedproductid = getIntent().getLongExtra(
                    StandardAppConstants.INTENTKEY_PRODUCTID,
                    0
            );
            passedproductname = getIntent().getStringExtra(
                    StandardAppConstants.INTENTKEY_PRODUCTNAME
            );
            inputproductname.setText(passedproductname);
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
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        super.onResume();
        //setDBCounts();
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
                //messagebar.setVisibility(View.GONE);
                break;
        }
        msg = "Refreshing ProductList";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        plcsr = dbproductmethods.getProducts(productfilter,orderby);
        productlistadapter.swapCursor(plcsr);
        resumestate = StandardAppConstants.RESUMSTATE_NORMAL;
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
        plcsr.close();
    }

    /**************************************************************************
     * Button Click Handler
     *
     * @param view The view (i.e the TextView that was clicked)
     */
    public void actionButtonClick(View view) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        switch (view.getId()) {
            case R.id.productaddedit_donebutton:
                msg = "Finishing";
                LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
                this.finish();
                break;
            case R.id.productaddedit_savebutton:
                msg = "Saving Data";
                LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
                productSave();
                break;
            default:
                break;
        }
    }

    /**************************************************************************
     * productSave - Save the Product being added or edited
     */
    public void productSave() {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        boolean notdoneok = true;
        String productname = inputproductname.getText().toString();
        String productnamelabel = getResources().getString(
                R.string.productnamelabel) + " ";
        String notsaved = getResources().getString(R.string.notsaved);
        String msg = "";

        // Product name cannot be blank
        if (productname.length() < 1) {
            msg = productnamelabel + getResources().getString(R.string.inputblank) +
                    " " + productnamelabel + " " + notsaved;
            setMessage(this,msg,notdoneok);
            logmsg = "Found that Product Name is blank. Not Saving";
            LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
            return;
        }

        msg = getResources().getString(R.string.productlabel) + " " +
                inputproductname.getText().toString() + " was ";
        switch (calledmode) {
            case StandardAppConstants.CM_ADD:
                logmsg = "Adding Product=" + productname;
                LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
                dbproductmethods.insertProduct(
                        inputproductname.getText().toString(),
                        ""
                );

                if (dbproductmethods.ifProductAdded()) {
                    msg = msg + getResources().getString(R.string.addedok);
                    inputproductname.setText("");
                    notdoneok = false;
                } else {
                    msg = msg + notsaved;
                }
                logmsg = "Product=" + productname +
                        " Added=" + Boolean.toString(!notdoneok);
                LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
                break;
            case StandardAppConstants.CM_EDIT:
                logmsg = "Updating Product=" + productname;
                LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
                dbproductmethods.modifyProduct(passedproductid,
                        inputproductname.getText().toString(),
                        ""
                );
                if (dbproductmethods.ifProductUpdated()) {
                    msg = msg + getResources().getString(R.string.editedok);
                    notdoneok = false;
                } else {
                    msg = msg + notsaved;
                }
                logmsg = "Product=" + productname +
                        " Updated=" + Boolean.toString(!notdoneok);
                LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
                break;
        }
        logmsg = "Refreshing ProductList";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        plcsr = dbproductmethods.getProducts(productfilter,orderby);
        productlistadapter.swapCursor(plcsr);
        setMessage(this,msg,notdoneok);
    }

    /**************************************************************************
     * sortClick - Handle list heading clicks (i.e. sort by that field)
     *
     * @param view the view that was clicked
     */
    public void sortClick(View view) {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        lastmessage = "List of Shops sorted by ";
        switch (view.getId()) {
            case R.id.productaddedit_productlist_heading_productname:
                getOrderBy(PRODUCTNAME_FULLCOLUMN,BYPRODUCT);
                lastmessage = lastmessage + " PRODUCT NAME in ";
                break;
            default:
                break;
        }
        if (sortchanged) {
            plcsr = dbproductmethods.getProducts(productfilter,orderby);
            productlistadapter.swapCursor(plcsr);
            if (ordertype) {
                lastmessage = lastmessage + " ascending order.";
            } else {
                lastmessage = lastmessage + " descending order.";
            }
            setMessage(this,lastmessage,false);
        }
    }

    /**************************************************************************
     * addFilterListener - Add a Listener to filter the ListView
     *                      i.e. as characters are typed then the ListView
     *                      is filtered according to the characters input
     *                      This is done by using LIKE '%<characters>%'
     *
     */
    public void addFilterListener(final EditText edittext) {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                productfilter = DBProductsTableConstants.PRODUCTS_NAME_COL_FULL +
                        DBConstants.SQLLIKECHARSTART +
                        edittext.getText().toString() +
                        DBConstants.SQLLIKECHAREND;
                plcsr = dbproductmethods.getProducts(productfilter,orderby);
                productlistadapter.swapCursor(plcsr);
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    /**************************************************************************
     * setDBCounts - extract the row counts from the database for relevant
     *                  tables.
     */
    private void setDBCounts() {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        shopcount = dbshopmethods.getShopCount();
        aislecount = dbaislemethods.getAisleCount();
        productcount = dbproductmethods.getProductCount();
    }

    /**************************************************************************
     * setMessage - set the Message regarding the lat action performed
     * Note as an action may have altered the database getDBcounts
     * is called and the title changed
     *
     * @param pa   the sa
     * @param msg  The message to be displayed.
     * @param flag Message imnportant, if true Yellow text, esle green
     */
    public void setMessage(ProductsAddEditActivity pa, String msg, boolean flag) {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);

        TextView messagebar = (TextView) pa.findViewById(R.id.productssaddedit_messagebar);
        messagebar.setText(context.getResources().getString(
                R.string.messagebar_prefix_lastaction) + " " + msg);
        if (flag) {
            messagebar.setTextColor(Color.YELLOW);
        } else {
            messagebar.setTextColor(Color.GREEN);
        }
        messagebar.setVisibility(View.VISIBLE);
        pa.setDBCounts();
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
