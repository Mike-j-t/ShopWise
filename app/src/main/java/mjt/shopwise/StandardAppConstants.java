package mjt.shopwise;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;

/**
 * Standard Application Constants
 */
public class StandardAppConstants{

    /**
     * Intent keys (application based)
     *
     * menucolorcode is used for passing a parent's colorcode to it's children
     * or in the case of MainActivity being the parent the respective option's
     * color code
     *
     * callingactivity is used for passing an indentifier from a parent
     * application to the invoked activity (best of the same as the parent
     * activity's name)
     *
     * callingmode allows an activity to pass a mode of operation from a parent
     * activity to a child activity e.g. CM_ADD indicates add perhaps as
     * opposed to CM_EDIT that would indicate edit (i.e. populate input and
     * then update rather than insert DB).
     * Note!Calling Mode constants defined below.
     */
    public static final String INTENTKEY_MENUCOLORCODE = "ik_menucolorcode";
    public static final String INTENTKEY_CALLINGACTIVITY = "ik_caller";
    public static final String INTENTKEY_CALLINGMODE = "ik_callermode";

    /**
     * DEVMODE Constants are used to control what log messages are output
     *
     * DEVMODE itself is ultimate for any logging at all it must be true.
     * DEVMODE_???? constants control the respective class
     *
     * NOTE!!!! These aren't FINAL to potentially allow them to be dynamically
     *          changed in order to allow debugging production app.
     *          Not yet implemented.
     */
    public static boolean DEVMODE = true;
    public static boolean DEVMODE_MAINACTIVITY = false;
    public static boolean DEVMODE_ACTIONCOLORCODING = false;
    public static boolean DEVMODE_ACTIVYMENUOPTION = false;
    public static boolean DEVMODE_ADAPTERAISLELIST = false;
    public static boolean DEVMODE_ADAPTERCHECKLIST = false;
    public static boolean DEVMODE_ADAPTERFILELIST = false;
    public static boolean DEVMODE_ADAPTERMAINACTIVITYOPTIONSMENU = false;
    public static boolean DEVMODE_ADAPTERORDERLIST = false;
    public static boolean DEVMODE_ADAPTERPRODUCTLIST = false;
    public static boolean DEVMODE_ADAPATERPROMPTEDRULELIST = false;
    public static boolean DEVMODE_ADAPTERRULELIST = false;
    public static boolean DEVMODE_ADAPTERRULEPERIODLIST = false;
    public static boolean DEVMODE_ADAPTERSHOPLIST = false;
    public static boolean DEVMODE_ADAPATERSHOPPINGLIST = false;
    public static boolean DEVMODE_ADAPTERSTOCKLIST = false;
    public static boolean DEVMODE_ADPATERSTOCKLISTLIST = false;
    public static boolean DEVMODE_AISLESACTIVITY = false;
    public static boolean DEVMODE_BACKUPACTIVITY = false;
    public static boolean DEVMODE_DBAPPVALUESMETHODS = false;
    public static boolean DEVMODE_DBHELPER = false;
    public static boolean DEVMODE_DBPRODUCTMETHODS = true;
    public static boolean DEVMODE_CHECKLISTACTIVITY = false;
    public static boolean DEVMODE_DBAISLEMETHODS = false;
    public static boolean DEVMODE_DBDAO = false;
    public static boolean DEVMODE_DBPRODUCTUSAGEMETHODS = false;
    public static boolean DEVMODE_DBRULEMETHODS = false;
    public static boolean DEVMODE_DBSHOPLISTMETHODS = false;
    public static boolean DEVMODE_DBSHOPMETHODS = false;
    public static boolean DEVMODE_EXTERNALSTORAGEPERMISSIONS = false;
    public static boolean DEVMODE_INTEGRITYCHECKDBHELPER = false;
    public static boolean DEVMODE_ORDERACTIVITY = false;
    public static boolean DEVMODE_PRODUCTSACTIVITY = true;
    public static boolean DEVMODE_PRODUCTSADDEDITACTIVITY = false;
    public static boolean DEVMODE_PROMPTEDRULESACTIVITY = false;
    public static boolean DEVMODE_REQUESTDIALOG = false;
    public static boolean DEVMODE_REQUESTDIALOGPARAMETERS = false;
    public static boolean DEVMODE_RULESACTIVITY = false;
    public static boolean DEVMODE_RULESADDEDITACTIVITY = false;
    public static boolean DEVMODE_RULETOOLSACTIVITY = false;
    public static boolean DEVMODE_SHOPPINGACTIVITY = false;
    public static boolean DEVMODE_SHOPPINGENTRYADJUSTACTIVITY = false;
    public static boolean DEVMODE_SHOPSACTIVITY = false;
    public static boolean DEVMODE_SHOPSADDEDITACTIVITY = false;
    public static boolean DEVMODE_SPINNERMOVE = false;
    public static boolean DEVMODE_STOCKACTIVITY = false;
    public static boolean DEVMODE_STOCKLISTEDITACTIVITY = false;
    public static boolean DEVMODE_STOCKLISTACTIVITY = false;
    public static boolean DEVMODE_TOOLSACTIVITY = true;

    /**
     * Intent keys (DB based i.e. used for passing DB derived values)
     */
    public static final String INTENTKEY_SHOPID = "ik_shopid";
    public static final String INTENTKEY_SHOPNAME = "ik_shopname";
    public static final String INTENTKEY_SHOPCITY = "ik_shopcity";
    public static final String INTENTKEY_SHOPORDER = "ik_shoporder";
    public static final String INTENTKEY_AISLEID = "ik_aisleid";
    public static final String INTENTKEY_AISLENAME = "ik_aislename";
    public static final String INTENTKEY_AISLEORDER = "ik_aisleorder";
    public static final String INTENTKEY_AISLESHOPREF = "ik+aisleshopref";
    public static final String INTENTKEY_PRODUCTID = "ik_productid";
    public static final String INTENTKEY_PRODUCTNAME = "ik_productname";
    public static final String INTENTKEY_PRODUCTORDER = "ik_productorder";
    public static final String INTENTKEY_PUPRODUCTREF = "ik_puproductref";
    public static final String INTENTKEY_PUAISLEREF = "ik_aisleref";
    public static final String INTENTKEY_PUCOST = "ik_pucost";
    public static final String INTENTKEY_PUCHECKLISTFLAG = "ik_puchecklistflag";
    public static final String INTENTKEY_PUCHECKLISTCOUNT = "ik_puchecklistcount";
    public static final String INTENTKEY_SHOPLISTQUANTITY = "ik_slquantity";
    public static final String INTENTKEY_RULEID = "ik_ruleid";
    public static final String INTENTKEY_RULENAME = "ik_rulename";
    public static final String INTENTKEY_RULEPRODUCTREF = "ik_ruleprodictref";
    public static final String INTENTKEY_RULEAISLEID = "ik_ruleaisleid";
    public static final String INTENTKEY_RULEUSES = "ik_ruleuses";
    public static final String INTENTKEY_RULEPROMPT = "ik_ruleprompt";
    public static final String INTENTKEY_RULEACTON = "ik_ruleacton";
    public static final String INTENKEY_RULEPERIOD = "ik_ruleperiod";
    public static final String INTENTKEY_RULEMULTIPLIER = "ik_rulemulriplier";

    /**
     * Calling Modes as passed via intents.
     * Used to indicate how the called activity should react
     */
    public static final int CM_CLEAR = 0;
    public static final int CM_ADD = 1;
    public static final int CM_EDIT = 2;
    public static final int CM_UPDATE = 3;
    public static final int CM_REPLACE = 4;
    public static final int CM_DELETE = 5;
    public static final int CM_STOCKFROMSSHOP = 6;
    public static final int CM_STOCKFROMAISLE = 7;
    public static final int CM_STOCKFROMPRODUCT = 8;
    public static final int CM_STOCKFROMSTOCKLIST = 9;


    /**
     * Standard Resume States
     */
    public static final int RESUMSTATE_NORMAL = 0;
    public static final int RESUMESTATE_ALT1 = 1;
    public static final int RESUMESTATE_ALT2 = 2;
    public static final int RESUMESTATE_ALT3 = 3;
    public static final int RESUMESTATE_ALT4 = 4;

    public static final String RULEPERIODS_APPVALKEY = "RULEPERIODS";


    /**
     * Date formatting for where SimpleDateFormats are used.
     */
    public static String STANDARD_DDMMYYY_FORMAT = "dd/MM/yyyy";
    public static String EXTENDED_DATE_FORMAT = "EEE MMM d, yyyy";
    public static String COMPARE_DATE_FORMAT = "yyyyMMdd";
    @SuppressLint("SimpleDateFormat")
    public static final SimpleDateFormat sdf =
            new SimpleDateFormat(STANDARD_DDMMYYY_FORMAT);
    @SuppressLint("SimpleDateFormat")
    public static final SimpleDateFormat sdf_extended =
            new SimpleDateFormat(EXTENDED_DATE_FORMAT);
    @SuppressLint("SimpleDateFormat")
    public static final SimpleDateFormat sdf_compare =
            new SimpleDateFormat(COMPARE_DATE_FORMAT);

    /**************************************************************************
     * Main Menu Options
     * Note menuoptions and menuoptions_info should always have the same
     * number of elements.
     * MENUOPTIONS is the name of the key used to store values in the
     * Appvalues table.
     * menuoptions String Array = button text (TextViews acting as buttons)
     * menuoptions_info String array = associated notes for the option.
     *
     * Note to ADD a menu option do:-
     *      1) Add a new declaration here e.g.
     *          public final String variablename = "buttontext"
     *          where variable name should describe the option and
     *          where buttontext should be what appears on the button
     *          max of 8 characters (else button will grow in height)
     *
     *       1a) Add a like named/associated ORDER entry as an integer
     *
     *       2) Add a corresponding array entry for the ActivityMenuOption array e.g.
     *          new ActivityMenuOption(variablename,"descriptivetext",order)
     *          where variablename is as the one created in 1) and
     *          descriptive text is a breif description of the functionality.
     *          NOTE! if it is not the last entry then add a trailing comma,
     *          if it is the last entry then ansure that the previous entry
     *          has a trailing comma.
     *          Note! order is an integer that signifies the order in which the
     *          menu option appears. You should ensure that this integer is
     *          unique otherwise the order of the menuoptions that do not have
     *          a unique order will be as determined by the Database Manager.
     *
     *       3) In the buildMenu method of the MainActivity class (MainActivity.java)
     *          Add a menuoption string definition e.g.
     *              String include_???????? = "("
     *                  DBAppvaluesTableConstants.APPVALUES_TEXT_COL_FULL +
     *                  " = '" + variablename + "') ";
     *              where variablename is as defined in 1) and ???????? should be
     *              descriptive e.g. buttontext as per 1)
     *
     *       4) In the buildMenu method of the MainActivity class (MainActivity.java)
     *              Locate the Inclusion/visiblity of menuoption logic section and
     *              study the logic determining in which if construct the new option
     *              should be made visible and then add a line like :-
     *                  DBConstants.SQLOR + include_????????
     *              where ???????? is as per 3)
     *              Note! the last occurence should have a trailing semi-colon
     *              all prior occurences should have a trailing +
     *                  (string concatenation), within that logic section.
     *
     *  Note! Although defined as constants, the values are stored in the AppValues table.
     *
     */
    public static final String MENUOPTIONS = "NAMENUOPTIONS";

    public static final String SHOPSAPPVALNAME = "SHOPS";
    public static final int SHOPSAPPVALORDER = 0;
    public static final String AISLESAPPVALNAME = "AISLES";
    public static final int AISLESAPPVALORDER = 1;
    public static final String PRODUCTSAPPVALNAME = "PRODUCTS";
    public static final int PRODUCTSAPPVALORDER = 2;
    public static final String STOCKAPPVALNAME = "STOCK";
    public static final int STOCKAPPVALORDER = 3;
    public static final String ORDERAPPVALNAME = "ORDER";
    public static final int ORDERAPPVALORDER = 4;
    public static final String CHECKLISTAPPVALNAME = "CHECKLIST";
    public static final int CHECKLISTAPPVALORDER = 5;
    public static final String SHOPPINGAPPVALNAME = "SHOPPING";
    public static final int SHOPPINGAPPVALORDER = 6;
    public static final String RULESAPPVALNAME = "RULES";
    public static final int RULESAPPVALORDER = 7;
    public static final String TOOLSAPPVALNAME = "TOOLS";
    public static final int TOOLSAPPVALORDER = 8;
    /**
     * The constant MAINACTIVITYOPTIONLIST.
     */
    public static final ActivityMenuOption[] MAINACTIVITYOPTIONLIST = {
            new ActivityMenuOption(SHOPSAPPVALNAME,
                    "ADD, DELETE and EDIT Shops, also STOCK Products.",
                    SHOPSAPPVALORDER),
            new ActivityMenuOption(AISLESAPPVALNAME,
                    "ADD, DELETE and EDIT Aisles, also STOCK Products.",
                    AISLESAPPVALORDER),
            new ActivityMenuOption(PRODUCTSAPPVALNAME,
                    "ADD, DELETE and EDIT products, also STOCK Products.",
                    PRODUCTSAPPVALORDER),
            new ActivityMenuOption(STOCKAPPVALNAME,
                    "ADD, EDIT or DELETE STOCK",
                    STOCKAPPVALORDER),
            new ActivityMenuOption(CHECKLISTAPPVALNAME,
                    "Check potential Shopping List additions.",
                    CHECKLISTAPPVALORDER),
            new ActivityMenuOption(ORDERAPPVALNAME,
                    "Add to the Shopping List.",
                    ORDERAPPVALORDER),
            new ActivityMenuOption(SHOPPINGAPPVALNAME,"Do the Shopping.",
                    SHOPPINGAPPVALORDER),
            new ActivityMenuOption(RULESAPPVALNAME,
                    "Manage Rules for Automated/Prompted Ordering.",
                    RULESAPPVALORDER),
            new ActivityMenuOption(TOOLSAPPVALNAME,"Utilities e.g. BACKUP",TOOLSAPPVALORDER)
    };
}
