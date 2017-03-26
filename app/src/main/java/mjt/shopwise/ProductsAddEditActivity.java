package mjt.shopwise;

import android.annotation.SuppressLint;
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
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import static mjt.shopwise.ActionColorCoding.transparency_optional;
import static mjt.shopwise.ActionColorCoding.transparency_requied;

/**
 * Add or Edit a Product
 */

@SuppressWarnings({"FieldCanBeLocal", "WeakerAccess", "CanBeFinal", "unused"})
public class ProductsAddEditActivity  extends AppCompatActivity {

    @SuppressWarnings("unused")
    private static final String THIS_ACTIVITY = "ProductsAddEditActivity";
    @SuppressWarnings("unused")
    private static String caller;
    private static int calledmode;
    //private static String defaultorder = "1000";

    private static final int BYPRODUCT = 0;
    private static final int BYSTORAGE = 1;
    private static final int BYORDER = 2;
    private static final String SORTASCENDING = DBConstants.SQLORDERASCENDING;
    private static final String SORTDESCENDING = DBConstants.SQLORDERDESCENDING;
    private String productfilter = "";

    Context context;
    ActionBar actionbar;

    /**
     * Colours
     */
    private static int h1;
    private static int h2;
    @SuppressWarnings("unused")
    private static int h3;
    private static int h4;
    private static int primary_color;

    @SuppressWarnings("unused")
    LinearLayout inputproductname_linearlayout;
    TextView inputproductname_label;
    EditText inputproductname;
    @SuppressWarnings("unused")
    LinearLayout inputproductstorage_linearlayout;
    @SuppressWarnings("unused")
    TextView inputproductstorage_label;
    Spinner inputproductstorage_spinner;
    AdapterStorageList storagelistadapter;
    @SuppressWarnings("unused")
    LinearLayout inputproductorder_linearlayout;
    TextView inputproductorder_label;
    EditText inputproductorder;
    @SuppressWarnings("unused")
    LinearLayout productaddeditbuttons_linearlayout;
    TextView savebutton;
    TextView donebutton;
    @SuppressWarnings("unused")
    LinearLayout productfilterheading;
    TextView productfilterlabel;
    EditText inputproductfilter;
    LinearLayout productlistheading;
    @SuppressWarnings("unused")
    TextView productlistproductname;
    ListView productlist;
    AdapterProductList productlistadapter;

    @SuppressWarnings("unused")
    DBDAO dbdao;
    DBShopMethods dbshopmethods;
    DBAisleMethods dbaislemethods;
    DBProductMethods dbproductmethods;
    DBStorageMethods dbstoragemethods;
    Cursor plcsr;
    Cursor stcsr;

    long passedproductid;
    String passedproductname;
    long passedstorageid;
    @SuppressWarnings("unused")
    String passedstoragname;
    int passedorder;
    long currentstoragref;
    @SuppressWarnings("unused")
    int currenthigheststorageorder;

    @SuppressWarnings("unused")
    private static final String PRODUCTID_COLUMN =
            DBProductsTableConstants.PRODUCTS_ID_COL;
    @SuppressWarnings("unused")
    private static final String PRODUCTNAME_COLUMN =
            DBProductsTableConstants.PRODUCTS_NAME_COL;
    @SuppressWarnings("unused")
    private static final String PRODUCTID_FULLCOLUMN =
            DBProductsTableConstants.PRODUCTS_ID_COL_FULL;
    private static final String PRODUCTNAME_FULLCOLUMN =
            DBProductsTableConstants.PRODUCTS_NAME_COL_FULL;
    @SuppressWarnings("unused")
    private static final  String PRODUCTORDER_COLUMN =
            DBProductsTableConstants.PRODUCTS_STORAGEORDER_COL;
    private static final String PRODUCTORDER_FULLCOLUMN =
            DBProductsTableConstants.PRODUCTS_STORAGEORDER_COL_FULL;
    @SuppressWarnings("unused")
    private static final String PRODUCTSTORAGEREF_COLUMN =
            DBProductsTableConstants.PRODUCTS_STORAGEREF_COL;
    @SuppressWarnings("unused")
    private static final String PRODUCTSTORAGEREF_FULLCOLUMN =
            DBProductsTableConstants.PRODUCTS_STORAGEREF_COL_FULL;
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
    @SuppressWarnings("unused")
    private static final String STORAGEORDER_FULLCOLUMN =
            DBStorageTableConstants.STORAGE_ORDER_COL_FULL;

    static String orderby = PRODUCTNAME_FULLCOLUMN + SORTASCENDING;
    static String storageorderby = STORAGENAME_FULLCOLUMN + SORTASCENDING;
    static int orderfld = BYPRODUCT;
    static boolean ordertype = true;
    static boolean sortchanged = false;
    static String storagefilter = "";

    static String lastmessage = "";
    @SuppressWarnings("unused")
    static String currentproductname = "";

    @SuppressWarnings("unused")
    static int shopcount = 0;
    @SuppressWarnings("unused")
    static int aislecount = 0;
    @SuppressWarnings("unused")
    static int productcount = 0;

    private int resumestate = StandardAppConstants.RESUMSTATE_NORMAL;
    @SuppressWarnings("unused")
    private Activity thisactivity;
    public static final String THISCLASS = ProductsAddEditActivity.class.getSimpleName();
    private static final String LOGTAG = "SW_PAEA";

    @SuppressLint("SetTextI18n")
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
        inputproductstorage_linearlayout = (LinearLayout) findViewById(R.id.inputproductstorage_linearlayout);
        inputproductstorage_label = (TextView) findViewById(R.id.inputproductstorage_label);
        inputproductstorage_spinner = (Spinner) findViewById(R.id.inputproductstorage_spinner);
        inputproductorder_linearlayout = (LinearLayout) findViewById(R.id.inputproductorder_linearlayout);
        inputproductorder_label = (TextView) findViewById(R.id.inputproductorder_label);
        inputproductorder = (EditText) findViewById(R.id.inputproductorder);
        addFilterListener(inputproductname);
        inputproductfilter = (EditText) findViewById(R.id.productaddedit_inputfilter);
        addFilterListener(inputproductfilter);
        savebutton = (TextView) findViewById(R.id.productaddedit_savebutton);
        donebutton = (TextView) findViewById(R.id.productaddedit_donebutton);
        productfilterlabel = (TextView) findViewById(R.id.productaddedit_productfilterlabel);
        productlistheading = (LinearLayout) findViewById(R.id.productaddedit_productlist_heading);
        productlistproductname = (TextView) findViewById(R.id.productaddedit_productlist_heading_productname);
        productlist = (ListView) findViewById(R.id.productaddedit_productlist);

         //Apply Color Coding
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
        ActionColorCoding.setActionButtonColor(inputproductstorage_spinner,h2);
        ActionColorCoding.setActionButtonColor(inputproductorder,h2 & transparency_requied);
        ActionColorCoding.setActionButtonColor(inputproductfilter,h4 & transparency_optional);
        productlistheading.setBackgroundColor(h1);
        inputproductname_label.setTextColor(primary_color);
        inputproductstorage_label.setTextColor(primary_color);
        inputproductorder_label.setTextColor(primary_color);
        productfilterlabel.setTextColor(h2);


        msg = "Preparing DataBases";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        dbdao = new DBDAO(this);
        dbshopmethods = new DBShopMethods(this);
        dbaislemethods = new DBAisleMethods(this);
        dbproductmethods = new DBProductMethods(this);
        dbstoragemethods = new DBStorageMethods(this);
        setDBCounts();
        this.setTitle(getResources().getString(R.string.productsaddlabel));

        msg = "Preparing ProductList";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        plcsr = dbproductmethods.getExpandedProducts(productfilter,orderby);
        productlistadapter = new AdapterProductList(this,
                plcsr,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER,
                this.getIntent(),
                false
        );
        productlist.setAdapter(productlistadapter);

        stcsr = dbstoragemethods.getStorage(storagefilter,storageorderby);
        storagelistadapter = new AdapterStorageList(this,
                stcsr,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER,
                this.getIntent(),
                true);
        inputproductstorage_spinner.setAdapter(storagelistadapter);

        msg = "Extracting and applying Intent Extras";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        caller = getIntent().getStringExtra(
                StandardAppConstants.INTENTKEY_CALLINGACTIVITY
        );
        calledmode = getIntent().getIntExtra(
                StandardAppConstants.INTENTKEY_CALLINGMODE,
                StandardAppConstants.CM_CLEAR
        );
        passedstorageid = 0;
        if (calledmode == StandardAppConstants.CM_EDIT) {
            passedproductid = getIntent().getLongExtra(
                    StandardAppConstants.INTENTKEY_PRODUCTID,
                    0
            );
            passedproductname = getIntent().getStringExtra(
                    StandardAppConstants.INTENTKEY_PRODUCTNAME
            );
            inputproductname.setText(passedproductname);
            passedorder = getIntent().getIntExtra(
                    StandardAppConstants.INTENTKEY_STORAGEORDER,0
            );
            inputproductorder.setText(Integer.toString(passedorder));
            passedstorageid = getIntent().getLongExtra(
                    StandardAppConstants.INTENTKEY_STORAGEID,0
            );
            passedstoragname = getIntent().getStringExtra(
                    StandardAppConstants.INTENTKEY_STORAGENAME
            );
            inputproductfilter.setText("");
            this.setTitle(getResources().getString(R.string.productseditlabel));
        }
        stcsr.moveToPosition(-1);
        while (stcsr.moveToNext()) {
            if (stcsr.getLong(stcsr.getColumnIndex(
                    STORAGEID_COLUMN
            )) == passedstorageid) {
                inputproductstorage_spinner.setSelection(stcsr.getPosition());
                break;
            }
        }
        setSelectStorageListener();

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
        plcsr = dbproductmethods.getExpandedProducts(productfilter,orderby);
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
    @SuppressWarnings("unused")
    public void actionButtonClick(View view) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        switch (view.getId()) {
            case R.id.productaddedit_donebutton:
                inputproductfilter.setText("");
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
    @SuppressWarnings("ConstantConditions")
    public void productSave() {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        boolean notdoneok = true;
        String productname = inputproductname.getText().toString();
        String productorder = inputproductorder.getText().toString();
        String productnamelabel = getResources().getString(
                R.string.productnamelabel) + " ";
        String notsaved = getResources().getString(R.string.notsaved);
        String msg;

        // Product name cannot be blank
        if (productname.length() < 1) {
            msg = productnamelabel + getResources().getString(R.string.inputblank) +
                    " " + productnamelabel + " " + notsaved;
            setMessage(this,msg,notdoneok);
            logmsg = "Found that Product Name is blank. Not Saving";
            LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
            return;
        }

        if (productorder.length() < 1) {
            msg = inputproductorder_label.getText().toString() +
                    getResources().getString(R.string.inputblank) +
                    productname + " " + notsaved;
            setMessage(this,msg,notdoneok);
            return;
        }

        stcsr.moveToPosition(inputproductstorage_spinner.getSelectedItemPosition());
        long productstorageref = stcsr.getLong(stcsr.getColumnIndex(
                STORAGEID_COLUMN
        ));

        msg = getResources().getString(R.string.productlabel) + " " +
                inputproductname.getText().toString() + " was ";
        switch (calledmode) {
            case StandardAppConstants.CM_ADD:
                logmsg = "Adding Product=" + productname;
                LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
                dbproductmethods.insertProduct(
                        inputproductname.getText().toString(),
                        "",
                        productstorageref,
                        Integer.valueOf(productorder)
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
                        "",
                        productstorageref,
                        Integer.valueOf(productorder)
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
        plcsr = dbproductmethods.getExpandedProducts(productfilter,orderby);
        productlistadapter.swapCursor(plcsr);
        setMessage(this,msg,notdoneok);
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
        lastmessage = "List of Products sorted by ";
        switch (view.getId()) {
            case R.id.productaddedit_productlist_heading_productname:
                getOrderBy(PRODUCTNAME_FULLCOLUMN,BYPRODUCT);
                lastmessage = lastmessage + " PRODUCT NAME in ";
                break;
            case R.id.productaddedit_productlist_heading_productstorage:
                getOrderBy(STORAGENAME_FULLCOLUMN,BYSTORAGE);
                lastmessage = lastmessage + " STORAGE in ";
                break;
            case R.id.productaddedit_productlist_heading_productorder:
                getOrderBy(PRODUCTORDER_FULLCOLUMN,BYORDER);
                lastmessage = lastmessage + " ORDER in ";
                break;
            default:
                break;
        }
        if (sortchanged) {
            plcsr = dbproductmethods.getExpandedProducts(productfilter,orderby);
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
                plcsr = dbproductmethods.getExpandedProducts(productfilter,orderby);
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

    @SuppressLint("SetTextI18n")
    private void setSelectStorageListener() {
        inputproductstorage_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view,
                                       int position,
                                       long id) {
                currentstoragref = stcsr.getLong(stcsr.getColumnIndex(
                        STORAGEID_COLUMN
                ));
                int highorder =
                        dbproductmethods.getHighestProductOrderPerStorage(
                                currentstoragref
                        ) + 100;
                if(highorder < 1000) {
                    highorder = 1000;
                }
                if (highorder > 9999) {
                    highorder = highorder - 100 + 1;
                    if (highorder > 9999) {
                        highorder = 9999;
                    }
                }
                if (calledmode == StandardAppConstants.CM_ADD) {
                    inputproductorder.setText(Integer.toString(highorder));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
