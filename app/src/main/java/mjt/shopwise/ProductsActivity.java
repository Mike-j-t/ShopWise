package mjt.shopwise;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import mjt.displayhelp.DisplayHelp;

import static mjt.sqlwords.SQLKWORD.*;

/******************************************************************************
 * ProductsActivity - List Products allowing them to be edited, stocked,
 * or deleted.
 * List can also be sorted by clicking on a label. Clicking
 * a label will toggle between descending and ascending order.
 *
 * Stock means to add a product to an aisle, this can be
 * achieved from a shop, aisle or product. The Stock button
 * will only be available when at least 1 aisle and 1 product
 * exists.
 */

@SuppressWarnings({"FieldCanBeLocal", "WeakerAccess", "unused"})
public class ProductsActivity extends AppCompatActivity {

    private static final String THIS_ACTIVITY = "ShopsActivity";
    @SuppressWarnings("unused")
    private static String caller;
    @SuppressWarnings("unused")
    private static int calledmode;

    /**
     * Sorting Productlist columns
     */
    private static final int BYPRODUCT = 0;
    private static final int BYSTORAGE = 1;
    private static final int BYORDER = 2;
    private static final String SORTASCENDING = SQLORDERASCENDING;
    private static final String SORTDESCENDING = SQLORDERDESCENDING;

    Context context;
    ActionBar actionbar;

    /**
     * Colours
     */
    private static int h1;
    @SuppressWarnings("unused")
    private static int h2;
    @SuppressWarnings("unused")
    private static int h3;
    private static int h4;
    private static int primary_color;
    private String menucolorcode;
    int passedmenucolorcode;

    /**
     * View objects
     */
    TextView donebutton;
    TextView newbutton;
    TextView messagebar;
    ListView productlist;
    LinearLayout productlistheading;
    TextView inputproductfilterlabel;
    EditText inputproductfilter;
    TextView sortable;
    TextView clickable;
    TextView longclickable;
    /**
     * The ProductListAdapter.
     */
    AdapterProductList productlistadapter;

    /**
     * Database objects
     */
    @SuppressWarnings("unused")
    DBDAO dbdao;
    DBShopMethods dbshopmethods;
    DBAisleMethods dbaislemethods;
    DBProductMethods dbproductmethods;
    Cursor plcsr;

    private static final String PRODUCTID_COLUMN =
            DBProductsTableConstants.PRODUCTS_ID_COL;
    private static final String PRODUCTNAME_COLUMN =
            DBProductsTableConstants.PRODUCTS_NAME_COL;
    @SuppressWarnings("unused")
    private static final String PRODUCTID_FULLCOLUMN =
            DBProductsTableConstants.PRODUCTS_ID_COL_FULL;
    private static final String PRODUCTNAME_FULLCOLUMN =
            DBProductsTableConstants.PRODUCTS_NAME_COL_FULL;
    private static final String PRODUCTSTORAGEREF_COLUMN =
            DBProductsTableConstants.PRODUCTS_STORAGEREF_COL;
    @SuppressWarnings("unused")
    private static final String PRODUCTSTORAGEREF_FULLCOLUMN =
            DBProductsTableConstants.PRODUCTS_STORAGEREF_COL_FULL;
    private static final String PRODUCTSTORAGEORDER_COLUMN =
            DBProductsTableConstants.PRODUCTS_STORAGEORDER_COL;
    private static final String PRODUCTSTORAGEORDER_FULLCOLUMN =
            DBProductsTableConstants.PRODUCTS_STORAGEORDER_COL_FULL;
    @SuppressWarnings("unused")
    private static final String STORAGENAME_COLUMN =
            DBStorageTableConstants.STORAGE_NAME_COL;
    private static final String STORAGENAME_FULLCOLUMN =
            DBStorageTableConstants.STORAGE_NAME_COL_FULL;
    @SuppressWarnings("unused")
    private static final String STORAGEORDER_COLUMN =
            DBStorageTableConstants.STORAGE_ORDER_COL;
    @SuppressWarnings("unused")
    private static final String STORAGEORDER_FULLCOLUMN =
            DBStorageTableConstants.STORAGE_ORDER_COL_FULL;

    static String orderby = PRODUCTNAME_FULLCOLUMN + SORTASCENDING;
    static int orderfld = BYPRODUCT;
    static boolean ordertype = true;
    static boolean sortchanged = false;
    static String lastmessage = "";
    static String currentproductname = "";
    static String productfilter = "";
    @SuppressWarnings("unused")
    static int shopcount = 0;
    static int aislecount = 0;
    static int productcount = 0;

    private int resumestate = StandardAppConstants.RESUMSTATE_NORMAL;
    private Activity thisactivity;
    public static final String THISCLASS = ProductsActivity.class.getSimpleName();
    private static final String LOGTAG = "SW_PA";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        context = this;
        thisactivity = (Activity) context;
        caller = getIntent().getStringExtra(
                StandardAppConstants.INTENTKEY_CALLINGACTIVITY);
        calledmode = getIntent().getIntExtra(
                StandardAppConstants.INTENTKEY_CALLINGMODE,0);
        msg = "Preparing Color Coding";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        menucolorcode = StandardAppConstants.INTENTKEY_MENUCOLORCODE;
        passedmenucolorcode = getIntent().getIntExtra(menucolorcode,0);
        productfilter = "";

        donebutton = (TextView) findViewById(R.id.products_donebutton);
        newbutton = (TextView) findViewById(R.id.products_newproductbutton);
        productlist = (ListView) findViewById(R.id.products_productlist);
        productlistheading = (LinearLayout) findViewById(R.id.products_productlist_heading);
        inputproductfilterlabel = (TextView) findViewById(R.id.products_productfilterlabel);
        inputproductfilter = (EditText) findViewById(R.id.products_inputfilter);
        addFilterListener();
        messagebar = (TextView) findViewById(R.id.products_messagebar);
        sortable = (TextView) findViewById(R.id.sortable);
        clickable = (TextView) findViewById(R.id.clickable);
        longclickable = (TextView) findViewById(R.id.longclickable);

        //ActionColorCoding.setSwatches(findViewById(android.R.id.content),this.getIntent());

        // Apply Color Coding
        actionbar = getSupportActionBar();
        ActionColorCoding.setActionBarColor(this,getIntent(),actionbar);
        primary_color = ActionColorCoding.setHeadingColor(this,getIntent(),0);
        h1 = ActionColorCoding.setHeadingColor(this,getIntent(),1);
        h2 = ActionColorCoding.setHeadingColor(this,getIntent(),2);
        h3 = ActionColorCoding.setHeadingColor(this,getIntent(),3);
        h4 = ActionColorCoding.setHeadingColor(this,getIntent(),4);
        ActionColorCoding.setActionButtonColor(donebutton, primary_color);
        ActionColorCoding.setActionButtonColor(newbutton, primary_color);
        productlistheading.setBackgroundColor(h1);
        ActionColorCoding.setActionButtonColor(inputproductfilter,h4 &
        ActionColorCoding.transparency_optional);
        inputproductfilterlabel.setTextColor(h1);
        sortable.setTextColor(primary_color);
        clickable.setTextColor(primary_color);
        longclickable.setTextColor(primary_color);


        msg = "Preparing Databases";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        dbdao = new DBDAO(this);
        dbshopmethods = new DBShopMethods(this);
        dbaislemethods = new DBAisleMethods(this);
        dbproductmethods = new DBProductMethods(this);

        setDBCounts();
        this.setTitle(getResources().getString(R.string.productslabel));

        msg = "Preparing ProductList";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        plcsr = dbproductmethods.getExpandedProducts(productfilter,orderby );
        productlistadapter = new AdapterProductList(
                this,
                plcsr,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER,
                getIntent(),
                false,
                false,
                false
        );
        productlist.setAdapter(productlistadapter);
        productlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                listItemClick(view, position, id);
            }
        });
        productlist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                listItemLongClick(view, position, id);
                return true;
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
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        super.onResume();
        setDBCounts();
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
        msg = "Refreshing ProductList";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        plcsr = dbproductmethods.getExpandedProducts(productfilter,orderby);
        productlistadapter.swapCursor(plcsr);
        resumestate = StandardAppConstants.RESUMSTATE_NORMAL;
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
                                R.string.title_help_products_activity),
                        R.array.help_products_activty,
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
            case R.id.products_donebutton:
                msg = "Finishing";
                LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
                this.finish();
                break;
            case R.id.products_newproductbutton:
                addProduct();
                break;
            default:
                break;
        }
    }

    /**************************************************************************
     * productEdit - Invoke the productAddEdit activity after
     *                  setting up the intent with the respective data
     *                  to be passed via the intent extras.
     */
    public void addProduct() {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        Intent intent = new Intent(this,ProductsAddEditActivity.class);
        intent.putExtra(menucolorcode,passedmenucolorcode);
        intent.putExtra(
                StandardAppConstants.INTENTKEY_CALLINGACTIVITY,
                THIS_ACTIVITY
        );
        intent.putExtra(
                StandardAppConstants.INTENTKEY_CALLINGMODE,
                StandardAppConstants.CM_ADD
        );
        msg = "Starting Activity" + ProductsAddEditActivity.class.getSimpleName() +
                " in ADD mode.";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        startActivity(intent);
    }

    /**************************************************************************
     * productEdit - method invoked from the request dialog to then invoke
     *                  then invoke the productAddEdit activity after
     *                  setting up the intent with the respective data
     *                  to be passed via the intent extras.
     */
    @SuppressWarnings("unused")
    public void productEdit(RequestDialogParameters values) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        ProductsActivity pa = (ProductsActivity) values.getPassedactivity();
        Intent intent = new Intent(this,ProductsAddEditActivity.class);
        intent.putExtra(
                StandardAppConstants.INTENTKEY_CALLINGACTIVITY,
                THIS_ACTIVITY
        );
        intent.putExtra(
                StandardAppConstants.INTENTKEY_CALLINGMODE,
                StandardAppConstants.CM_EDIT
        );
        intent.putExtra(StandardAppConstants.INTENTKEY_PRODUCTID,
                plcsr.getLong(
                        plcsr.getColumnIndex(PRODUCTID_COLUMN)
                ));
        intent.putExtra(StandardAppConstants.INTENTKEY_PRODUCTNAME,
                plcsr.getString(
                        plcsr.getColumnIndex(PRODUCTNAME_COLUMN
                        )
                ));
        intent.putExtra(StandardAppConstants.INTENTKEY_STORAGEID,
                plcsr.getLong(
                        plcsr.getColumnIndex(PRODUCTSTORAGEREF_COLUMN)
                ));
        intent.putExtra(StandardAppConstants.INTENTKEY_STORAGEORDER,
                plcsr.getInt(
                        plcsr.getColumnIndex(PRODUCTSTORAGEORDER_COLUMN)
                ));
        intent.putExtra(menucolorcode,passedmenucolorcode);
        msg = "Starting Activity" + ProductsAddEditActivity.class.getSimpleName() +
                " in EDIT mode. For Product=" +
                plcsr.getString(
                        plcsr.getColumnIndex(PRODUCTNAME_COLUMN)) +
                " ID=" +
                Long.toString(
                        plcsr.getLong(
                                plcsr.getColumnIndex(PRODUCTID_COLUMN)
                        )
                );
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        startActivity(intent);
    }

    /**************************************************************************
     * productDelete - method invoked from the request dialog to delete a
     *                  product along with any linked/refrenced rows.
     *                  Linked are rows in other tables that reference the
     *                  product. These can be Product Usages, ShoppingList
     *                  entries and rules.
     *                  Note the request dialog lists these and asks for
     *                  confirmation. It is upon confirmation to perform
     *                  the delete that this method is invoked. The request
     *                  dialog is called on the ListView's item onLongClick
     *                  handler.
     */
    @SuppressWarnings("unused")
    public void productDelete(RequestDialogParameters values) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        Activity activity = (values.getPassedactivity());
        ProductsActivity pa = (ProductsActivity) activity;
        pa.dbproductmethods.deleteProduct(values.getLong1(),false);
        pa.plcsr = dbproductmethods.getExpandedProducts(productfilter,orderby);
        pa.productlistadapter.swapCursor(plcsr);
        pa.setMessage(pa,"Product" +
                currentproductname +
                " Deleted.",
                true
        );
        msg = "ProductID=" + Long.toString(values.getLong1()) +
                " Deleted.";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
    }

    /**************************************************************************
     *
     * @param values        values passed getLong1 = productref
     */
    @SuppressWarnings("unused")
    public void productStock(RequestDialogParameters values) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        long productid = values.getLong1();
        Intent intent = new Intent(this,StockAddActivity.class);
        intent.putExtra(
                StandardAppConstants.INTENTKEY_CALLINGACTIVITY,
                THIS_ACTIVITY
        );
        intent.putExtra(
                StandardAppConstants.INTENTKEY_CALLINGMODE,
                StandardAppConstants.CM_STOCKFROMPRODUCT
        );
        intent.putExtra(StandardAppConstants.INTENTKEY_PRODUCTID,
                plcsr.getLong(
                        plcsr.getColumnIndex(PRODUCTID_COLUMN)
                ));
        intent.putExtra(StandardAppConstants.INTENTKEY_PRODUCTNAME,
                plcsr.getString(
                        plcsr.getColumnIndex(PRODUCTNAME_COLUMN
                        )
                ));
        intent.putExtra(menucolorcode,passedmenucolorcode);
        msg = "Starting Activity " + StockAddActivity.class.getSimpleName() +
                " for Product=" +
                plcsr.getString(plcsr.getColumnIndex(PRODUCTNAME_COLUMN)) +
                " ID=" +
                Long.toString(
                        plcsr.getLong(
                                plcsr.getColumnIndex(PRODUCTID_COLUMN))
                );
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        startActivity(intent);
    }

    /**************************************************************************
     * sortClick - Handle list heading clicks (i.e. sort by that field)
     *
     * @param view the view that was clicked
     */
    @SuppressWarnings("unused")
    public void sortClick(View view) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        lastmessage = getResources().getString(R.string.productslabel) +
                " sorted by ";
        switch (view.getId()) {
            case R.id.products_productlist_heading_productname:
                getOrderBy(PRODUCTNAME_FULLCOLUMN,BYPRODUCT);
                lastmessage = lastmessage + " PRODUCT NAME (";
                break;
            case R.id.products_productlist_heading_productstorage:
                getOrderBy(STORAGENAME_FULLCOLUMN,BYSTORAGE);
                lastmessage = lastmessage + " STORAGE NAME (";
                break;
            case R.id.products_productlist_heading_productorder:
                getOrderBy(PRODUCTSTORAGEORDER_FULLCOLUMN,BYORDER);
                lastmessage = lastmessage + " ORDER (";
                break;
            default:
                break;
        }
        if (sortchanged) {
            plcsr = dbproductmethods.getExpandedProducts(productfilter,orderby);
            productlistadapter.swapCursor(plcsr);
            if (ordertype) {
                lastmessage = lastmessage + "ascending)";
            } else {
                lastmessage = lastmessage + "descending)";
            }
            setMessage(this,lastmessage,false);
        }
    }

    /**************************************************************************
     * listItemClick - Handle Clicking an Item
     *
     * @param view      The view that was clicked.
     * @param position  The position of the clicked item in the list.
     * @param id        The row id of the item that was clicked.
     */
    public void listItemClick(View view, int position, long id) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);

        long productid = plcsr.getLong(plcsr.getColumnIndex(PRODUCTID_COLUMN));
        currentproductname = plcsr.getString(
                plcsr.getColumnIndex(PRODUCTNAME_COLUMN)
        );
        Class cls = null;
        String classname = this.getClass().getCanonicalName();
        String positivebuttontext = getResources().getString(R.string.editbutton);
        String positiveaction = "productEdit";
        String negativebuttontext = getResources().getString(R.string.stockbutton);
        String negativeaction = "productStock";
        String neutralbuttontext = getResources().getString(R.string.cancelbutton);
        String neutralaction = "";
        String title = "Edit or Stock Product = " + currentproductname;
        String message = "Avaiable Options are CANCEL, EDIT and STOCK.\n\n" +
                "\tCANCEL returns to Products, doing nothing.\n\n" +
                "\tEDIT allows you to edit the Product.\n\n" +
                "\tSTOCK allows you to assign Products to Aisles in the Shop " +
                "(or othershops).";
        if ((aislecount < 1) || (productcount < 1)) {
            negativebuttontext = "";
        }
        MixTripleLongTripleInt values = new MixTripleLongTripleInt();
        values.setMIXTRPPLONGINT(productid,0,0,0,0,0);
        new RequestDialog().requestDialog(thisactivity,
                classname,
                title,
                message,
                positivebuttontext,negativebuttontext,neutralbuttontext,
                positiveaction,negativeaction,neutralaction,
                values
        );
    }

    /**************************************************************************
     *  handle list item long click - Delete a product or Cancel to not Delete
     * @param view          The view that was clicked
     * @param position      The position of the item in the list
     * @param id            The id (_id value obtained from the cursor)
     *
     *                      Note invokes a dialog which will then invoke
     *                      the productDelete method is Delete is Clicked
     */
    public void listItemLongClick(View view, int position, long id) {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);

        long productid = plcsr.getLong(
                plcsr.getColumnIndex(PRODUCTID_COLUMN)
        );
        currentproductname = plcsr.getString(
                plcsr.getColumnIndex(PRODUCTNAME_COLUMN)
        );
        String title = "Delete Product - " + currentproductname;

        String positivebuttontext = getResources().getString(R.string.deletebutton);
        String positiveaction = "productDelete";    //<== The method invoked
        String negativebuttontext = "";
        String negativeaction = "";
        String neutralbuttontext = getResources().getString(R.string.cancelbutton);
        String neutralaction = "";

        // Get the impacy of deleting the product.
        // i.e items that reference the product, such as,
        //          product usages, shopping list entries, rules

        ArrayList<String> impact = dbproductmethods.productDeleteImpact(productid);
        StringBuilder sb = new StringBuilder();
        String tmsg = "";
        for (String msg: impact) {
            sb.append(msg).append("\n");
        }
        String message = "Deleting product - " + currentproductname + " will:\n\n" + sb.toString();
        MixTripleLongTripleInt values = new MixTripleLongTripleInt();
        values.setMIXTRPPLONGINT(productid,0,0,0,0,0);

        String classname = this.getClass().getCanonicalName();
        new RequestDialog().requestDialog(thisactivity,
                classname,
                title, message,
                positivebuttontext,negativebuttontext,neutralbuttontext,
                positiveaction, negativeaction, neutralaction,
                values);
    }

    /**************************************************************************
     * addFilterListener - Add a Listener to filter the ListView
     *                      i.e. as characters are typed then the ListView
     *                      is filtered according to the characters input
     *                      This is done by using LIKE '%<characters>%'
     *
     */
    public void addFilterListener() {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        inputproductfilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                productfilter = DBProductsTableConstants.PRODUCTS_NAME_COL_FULL +
                        SQLLIKECHARSTART +
                        inputproductfilter.getText().toString() +
                        SQLLIKECHAREND;
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
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        shopcount = dbshopmethods.getShopCount();
        aislecount = dbaislemethods.getAisleCount();
        productcount = dbproductmethods.getProductCount();
    }

    /**************************************************************************
     * setMessage - set the Message regarding the lat action performed
     * Note as an action may have altered the database getDBcounts
     * is called and the title changed
     *
     * @param activity   this activity
     * @param msg  The message to be displayed.
     * @param flag Message imnportant, if true Yellow text, esle green
     */
    public void setMessage(ProductsActivity activity, String msg, boolean flag) {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);

        TextView messagebar = (TextView) activity.findViewById(R.id.products_messagebar);
        messagebar.setText(context.getResources().getString(
                R.string.messagebar_prefix_lastaction,msg));
        if (flag) {
            messagebar.setTextColor(Color.YELLOW);
        } else {
            messagebar.setTextColor(Color.GREEN);
        }
        messagebar.setVisibility(View.VISIBLE);
    }

    /**************************************************************************
     * getOrderBy - Generate the new ORDEY BY sql (ORDER BY already exists)
     * @param newcolumn     the DB column to sort by
     * @param neworderfld   the column as an integer as per constants
     */
    private void getOrderBy(String newcolumn, int neworderfld) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
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
     * Do nothing.
     *
     * @param values the values
     */
    @SuppressWarnings({"EmptyMethod", "unused"})
    public void doNothing(RequestDialogParameters values) {
    }

}
