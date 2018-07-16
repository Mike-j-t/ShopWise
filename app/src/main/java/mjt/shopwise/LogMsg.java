package mjt.shopwise;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

/**
 * Statndard Message Logging with supression dependent upon mode.
 * Mode being a two tier aspect.
 * That is, DEVMODE (according to StandardAppConstants.DEVMODE) if false
 * will suppress ALL. Whilst, DEVMODE_?????, if false will supress at
 * class level.
 * If DEVMODE_????? is true AND if DEVMODE is true then logging for that
 * class will be turned on.
 *
 * Log will be :-
 *  <datetime>/<app> <T>/<TAG>/   Class=<class> Method=<method> MSG=<message>
 *      where   <datetime> is the date and time MM-DD HH:MM:SS.TTT
 *              <app> is the application/package name
 *              <T> is the log type E,W,D,I,V (default V)
 *              <TAG> is the supplied TAG
 *              <class> is the ClassName (supplied or determined)
 *                  note less . prefix when determined.
 *              <method> is the supplied methodname
 *              <message> is the supplied message
 *
 */
@SuppressWarnings({"WeakerAccess", "MethodNameSameAsClassName"})
public class LogMsg{

    /**
     * TAG default and LOGTYPES
     */
    public static final String LOGTAG_DEFAULT = "SW_DFLT";
    public static final int LOGTYPE_ERROR = 16;
    public static final int LOGTYPE_WARNING = 8;
    public static final int LOGTYPE_DEBUG = 4;
    public static final int LOGTYPE_INFORMATIONAL = 2;
    public static final int LOGTYPE_VERBOSE = 32;

    /**
     * Disallow construction thus only allow methods to be invoked
     **/
    private LogMsg() {}


    /**************************************************************************
     * Log with a provided classname
     * @param logtype       Type of log message
     * @param logtag        The TAG (first part of log entry)
     * @param msg           The Message to be logged
     * @param classname     The classname
     * @param methodname    The name of the Method
     */
    public static void LogMsg(int logtype,
                              String logtag,
                              String msg,
                              String classname,
                              String methodname
                              ) {
        LogMsg(logtype, logtag, msg, null, classname, methodname);
    }

    /**************************************************************************
     * Log with provided classname and default TAG
     * @param logtype       Type of log message
     * @param msg           The message to be logged
     * @param classname     The provided ClassName
     * @param methodname    The provided MethodName
     */
    public static void LogMsg(@SuppressWarnings("SameParameterValue") int logtype,
                              String msg,
                              @SuppressWarnings("SameParameterValue") String classname,
                              String methodname
    ) {
        LogMsg(logtype, LOGTAG_DEFAULT, msg, null, classname, methodname);
    }

    /**************************************************************************
     * Log with provided TAG and context from which the ClassName is extracted
     * @param logtype       Type of log message
     * @param logtag        The provided TAG
     * @param msg           The Message to be logged
     * @param context       The context from which the ClassName is extracted
     * @param methodname    The provided MethodName
     */
    public static void LogMsg(@SuppressWarnings("SameParameterValue") int logtype,
                              String logtag,
                              String msg,
                              Context context,
                              String methodname) {
        LogMsg(logtype, logtag, msg, context, "", methodname);

    }

    /**************************************************************************
     * Log with default TAG, ClassName derived from provided Context
     * @param logtype       The type of the Log Message
     * @param msg           The message
     * @param context       The context
     * @param methodname    The method name
     */
    @SuppressWarnings("unused")
    public static void LogMsg(int logtype,
                              String msg,
                              Context context,
                              String methodname){
        LogMsg(logtype, LOGTAG_DEFAULT, msg, context, "", methodname);
    }

    /**************************************************************************
     * The MASTER method which can only be called by the other methods
     * @param logtype       The Type of log Message
     * @param logtag        The TAG
     * @param msg           The Message
     * @param context       The context (note null handling)
     * @param callingclass  The Class name used in the absence of Context
     * @param methodname    The Methodname
     *
     *
     */
    private static void LogMsg(int logtype,
                              String logtag,
                              String msg,
                              Context context,
                              String callingclass,
                              String methodname){

        // IF not in DEVMODE then return without logging message
        if (!StandardAppConstants.DEVMODE) {
            return;
        }

        if (context != null) {
            callingclass = ((Activity) context).getComponentName().getShortClassName();
            if (callingclass.length() > 1 && callingclass.charAt(0) == '.')  {
                callingclass = callingclass.substring(1);

            }
        }

        // Only allow logging if the calling class has it's devmode set to true
        // Note set thses in StandardAppsConstants
        if ( !
                (
                        (callingclass.equals(MainActivity.THISCLASS) &&
                                StandardAppConstants.DEVMODE_MAINACTIVITY) ||
                        (callingclass.equals(ActionColorCoding.THISCLASS) &&
                                StandardAppConstants.DEVMODE_ACTIONCOLORCODING) ||
                        (callingclass.equals(ActivityMenuOption.THISCLASS) &&
                                StandardAppConstants.DEVMODE_ACTIVYMENUOPTION) ||
                        (callingclass.equals(AdapterAisleList.THISCLASS) &&
                                StandardAppConstants.DEVMODE_ADAPTERAISLELIST) ||
                        (callingclass.equals(AdapterChecklist.THISCLASS) &&
                                StandardAppConstants.DEVMODE_ADAPTERCHECKLIST) ||
                        (callingclass.equals(AdapterFileList.THISCLASS) &&
                                StandardAppConstants.DEVMODE_ADAPTERFILELIST) ||
                        (callingclass.equals(AdapterMainActivityOptionsMenu.THISCLASS) &&
                                StandardAppConstants.DEVMODE_ADAPTERMAINACTIVITYOPTIONSMENU) ||
                        (callingclass.equals(AdapterOrderList.THISCLASS) &&
                                StandardAppConstants.DEVMODE_ADAPTERORDERLIST) ||
                        (callingclass.equals(AdapterProductList.THISCLASS) &&
                                StandardAppConstants.DEVMODE_ADAPTERPRODUCTLIST) ||
                        (callingclass.equals(AdapterPromptedRuleList.THISCLASS) &&
                                StandardAppConstants.DEVMODE_ADAPATERPROMPTEDRULELIST) ||
                        (callingclass.equals(AdapterRuleList.THISCLASS) &&
                                StandardAppConstants.DEVMODE_ADAPTERRULELIST) ||
                        (callingclass.equals(AdapterRulePeriodList.THISCLASS)
                                && StandardAppConstants.DEVMODE_ADAPTERRULEPERIODLIST) ||
                        (callingclass.equals(AdapterShopList.THISCLASS) &&
                                StandardAppConstants.DEVMODE_ADAPTERSHOPLIST) ||
                        (callingclass.equals(AdapterShoppingList.THISCLASS) &&
                                StandardAppConstants.DEVMODE_ADAPATERSHOPPINGLIST) ||
                        (callingclass.equals(AdapterStockList.THISCLASS) &&
                                StandardAppConstants.DEVMODE_ADAPTERSTOCKLIST) ||
                        (callingclass.equals(AdapterStockListList.THISCLASS) &&
                                StandardAppConstants.DEVMODE_ADPATERSTOCKLISTLIST) ||
                        (callingclass.equals(AislesActivity.THISCLASS) &&
                                StandardAppConstants.DEVMODE_AISLESACTIVITY) ||
                        (callingclass.equals(BackupActivity.THISCLASS) &&
                                StandardAppConstants.DEVMODE_BACKUPACTIVITY) ||
                        (callingclass.equals(DBAppvaluesMethods.THISCLASS) &&
                                StandardAppConstants.DEVMODE_DBAPPVALUESMETHODS) ||
                        (callingclass.equals(DBHelper.THISCLASS) &&
                                StandardAppConstants.DEVMODE_DBHELPER) ||
                        (callingclass.equals(CheckListActivity.THISCLASS) &&
                                StandardAppConstants.DEVMODE_CHECKLISTACTIVITY) ||
                        (callingclass.equals(DBAisleMethods.THISCLASS) &&
                                StandardAppConstants.DEVMODE_DBAISLEMETHODS) ||
                        (callingclass.equals(DBProductMethods.THISCLASS) &&
                                StandardAppConstants.DEVMODE_DBPRODUCTMETHODS) ||
                        (callingclass.equals(DBDAO.THISCLASS) &&
                                StandardAppConstants.DEVMODE_DBDAO) ||
                        (callingclass.equals(DBProductUsageMethods.THISCLASS) &&
                                StandardAppConstants.DEVMODE_DBPRODUCTUSAGEMETHODS) ||
                        (callingclass.equals(DBRuleMethods.THISCLASS) &&
                                StandardAppConstants.DEVMODE_DBRULEMETHODS) ||
                        (callingclass.equals(DBShopListMethods.THISCLASS) &&
                                StandardAppConstants.DEVMODE_DBSHOPLISTMETHODS) ||
                        (callingclass.equals(DBShopMethods.THISCLASS) &&
                                StandardAppConstants.DEVMODE_DBSHOPMETHODS) ||
                        (callingclass.equals(ExternalStoragePermissions.THISCLASS) &&
                                StandardAppConstants.DEVMODE_EXTERNALSTORAGEPERMISSIONS) ||
                        (callingclass.equals(IntegrityCheckDBHelper.THISCLASS) &&
                                StandardAppConstants.DEVMODE_INTEGRITYCHECKDBHELPER) ||
                        (callingclass.equals(OrderActivity.THISCLASS) &&
                                StandardAppConstants.DEVMODE_ORDERACTIVITY) ||
                        (callingclass.equals(ProductsActivity.THISCLASS) &&
                                StandardAppConstants.DEVMODE_PRODUCTSACTIVITY) ||
                        (callingclass.equals(ProductsAddEditActivity.THISCLASS) &&
                                StandardAppConstants.DEVMODE_PRODUCTSADDEDITACTIVITY) ||
                        (callingclass.equals(PromptedRulesActivity.THISCLASS) &&
                                StandardAppConstants.DEVMODE_PROMPTEDRULESACTIVITY) ||
                        (callingclass.equals(RequestDialog.THISCLASS) &&
                                StandardAppConstants.DEVMODE_REQUESTDIALOG) ||
                        (callingclass.equals(RequestDialogParameters.THISCLASS) &&
                                StandardAppConstants.DEVMODE_REQUESTDIALOGPARAMETERS) ||
                        (callingclass.equals(RulesActivity.THISCLASS) &&
                                StandardAppConstants.DEVMODE_RULESACTIVITY) ||
                        (callingclass.equals(RulesAddEditActivity.THISCLASS) &&
                                StandardAppConstants.DEVMODE_RULESADDEDITACTIVITY) ||
                        (callingclass.equals(RuleToolsActivity.THISCLASS) &&
                                StandardAppConstants.DEVMODE_RULETOOLSACTIVITY) ||
                        (callingclass.equals(ShoppingActivity.THISCLASS) &&
                                StandardAppConstants.DEVMODE_SHOPPINGACTIVITY) ||
                        (callingclass.equals(ShoppingEntryAdjustActivity.THISCLASS) &&
                                StandardAppConstants.DEVMODE_SHOPPINGENTRYADJUSTACTIVITY) ||
                        (callingclass.equals(ShopsActivity.THISCLASS) &&
                                StandardAppConstants.DEVMODE_SHOPSACTIVITY) ||
                        (callingclass.equals(ShopsAddEditActivity.THISCLASS)
                                && StandardAppConstants.DEVMODE_SHOPSADDEDITACTIVITY) ||
                        (callingclass.equals(SpinnerMove.THISCLASS) &&
                                StandardAppConstants.DEVMODE_SPINNERMOVE) ||
                        (callingclass.equals(StockAddActivity.THISCLASS) &&
                                StandardAppConstants.DEVMODE_STOCKADDACTIVITY) ||
                        (callingclass.equals(StockEditActivity.THISCLASS) &&
                                StandardAppConstants.DEVMODE_STOCKEDITACTIVITY) ||
                        (callingclass.equals(StockListActivity.THISCLASS) &&
                                StandardAppConstants.DEVMODE_STOCKLISTACTIVITY) ||
                        (callingclass.equals(ToolsActivity.THISCLASS) &&
                                StandardAppConstants.DEVMODE_TOOLSACTIVITY) ||
                        (callingclass.equals(RuleSuggestCheckActivity.THISCLASS) &&
                                StandardAppConstants.DEVMODE_RULESUGGESTCHECKACTIVITY) ||
                        (callingclass.equals(AdapterStorageList.THISCLASS) &&
                                StandardAppConstants.DEVMODE_ADAPTERSTORAGELIST) ||
                        (callingclass.equals(DBStorageMethods.THISCLASS) &&
                                StandardAppConstants.DEVMODE_DBSTORAGEMETHODS) ||
                                (callingclass.equals(StorageActivity.THISCLASS) &&
                                StandardAppConstants.DEVMODE_STORAGEACTIVITY)
                )

                )
        {
            return;
        }

        msg =   " Class=" + callingclass +
                " Method=" + methodname +
                " MSG="+ msg;

        switch (logtype) {
            case LOGTYPE_ERROR:
                Log.e(logtag,msg);
                break;
            case LOGTYPE_WARNING:
                Log.w(logtag,msg);
                break;
            case LOGTYPE_INFORMATIONAL:
                Log.i(logtag,msg);
                break;
            case LOGTYPE_DEBUG:
                Log.d(logtag,msg);
                break;
            case LOGTYPE_VERBOSE:
                Log.v(logtag,msg);
                break;
            default:
                Log.v(logtag,msg);
        }
    }
}
