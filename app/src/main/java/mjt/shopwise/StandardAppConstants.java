package mjt.shopwise;

import android.graphics.Color;

import java.text.SimpleDateFormat;

/**
 * Standard activity constants
 */
public class StandardAppConstants {

    /**
     * Intent keys
     */
    public static final String INTENTKEY_MENUCOLORCODE = "ik_menucolorcode";
    public static final String INTENTKEY_CALLINGACTIVITY = "ik_caller";
    public static final String INTENTKEY_CALLINGMODE = "ik_callermode";
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


    /**
     * Standard Resume States
     */
    public static final int RESUMSTATE_NORMAL = 0;
    public static final int RESUMESTATE_ALT1 = 1;
    public static final int RESUMESTATE_ALT2 = 2;
    public static final int RESUMESTATE_ALT3 = 3;
    public static final int RESUMESTATE_ALT4 = 4;


    public static String STANDARD_DDMMYYY_FORMAT = "dd/MM/yyyy";
    public static String EXTENDED__DATE_DORMAT = "EEE MMM d, yyyy";
    public static String COMPARE_DATE_FORMAT = "yyyyMMdd";
    public static final SimpleDateFormat sdf =
            new SimpleDateFormat(STANDARD_DDMMYYY_FORMAT);
    public static final SimpleDateFormat sdf_extended =
            new SimpleDateFormat(EXTENDED__DATE_DORMAT);
    public static final SimpleDateFormat sdf_compare =
            new SimpleDateFormat(COMPARE_DATE_FORMAT);

    /**
     * The Colorlist.
     */
    public Color colorlist[];   // colours for Main option colour-coding
                                // colours defined in resources.
                                // colorlist built in main activity

    /**************************************************************************
     * Main Menu Options
     * Note menuoptions and menuoptions_info should always have the same
     * number of elements.
     * MENUOPTIONS is the name of the key used to store values in the
     * Appvalues table.
     * menuoptions String Array = button text (TextViews acting as buttons)
     * menuoptions_info String array = associated notes for the option.
     */
    public static final String MENUOPTIONS = "MAMENUOPTIONS";
    public static final String SHOPSAPPVALNAME = "SHOPS";
    public static final String AISLESAPPVALNAME = "AISLES";
    public static final String PRODUCTSAPPVALNAME = "PRODUCTS";
    public static final String ORDERAPPVALNAME = "ORDER";
    public static final String SHOPPINGAPPVALNAME = "SHOPPING";
    public static final String RULESAPPVALNAME = "RULES";
    /**
     * The constant MAINACTIVITYOPTIONLIST.
     */
    public static final ActivityMenuOption[] MAINACTIVITYOPTIONLIST = {
            new ActivityMenuOption(SHOPSAPPVALNAME, "Add, Delete and Edit Shop, also assign Products.",1),
            new ActivityMenuOption(AISLESAPPVALNAME,"Add, Delete and Edit Shop, also assign Products.",2),
            new ActivityMenuOption(PRODUCTSAPPVALNAME,"Add, Delete and Edit Shop, also assign Products.",3),
            new ActivityMenuOption(ORDERAPPVALNAME,"Add to the Shopping List.",4),
            new ActivityMenuOption(SHOPPINGAPPVALNAME,"Do the Shopping.",5),
            new ActivityMenuOption(RULESAPPVALNAME,"Manage Rules for Automated/Prompted Ordering.",6)
    };
}
