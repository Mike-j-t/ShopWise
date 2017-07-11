package mjt.shopwise;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static mjt.sqlwords.SQLKWORD.*;

/**
 * DBRuleMethods - Database Methods for Rule Handling
 */
@SuppressWarnings({"FieldCanBeLocal", "WeakerAccess", "CanBeFinal"})
public class DBRuleMethods {
    private static final  String LOGTAG = "SW-DBRM";
    private Context context;
    private DBDAO dbdao;
    private static SQLiteDatabase db;
    private static long lastruleadded = -1;
    private static boolean lastruleaddok = false;
    @SuppressWarnings("unused")
    private static boolean lastruleupdatedok = false;
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat sdf = new SimpleDateFormat(
            StandardAppConstants.EXTENDED_DATE_FORMAT);
    public static final String THISCLASS = DBRuleMethods.class.getSimpleName();

    /**
     * Instantiates a new Db rule methods.
     *
     * @param ctxt the ctxt
     */
    DBRuleMethods(Context ctxt) {
        String logmsg = "Constructing";
        String methodname = "Construct";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                LOGTAG,logmsg,THISCLASS,methodname);
        this.context = ctxt;
        this.dbdao = new DBDAO(context);
        db = dbdao.db;
    }

    /**************************************************************************
     * getRuleCount - return the number of Rules
     *
     * @return number of rules
     */
    int getRuleCount() {
        String logmsg = "Invoked";
        String methodname =
                new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                LOGTAG,logmsg,THISCLASS,methodname);
        return DBCommonMethods.getTableRowCount(db,
                DBRulesTableConstants.RULES_TABLE
        );
    }

    /**************************************************************************
     * getLastRuleAdded - returns id of the last rule added
     *
     * @return id of the last rule added
     */
    @SuppressWarnings("unused")
    long getLastRuleAdded() { return lastruleadded; }

    /**************************************************************************
     * ifRuleAdded - returns the status of the last rule insert
     *
     * @return true if ok, esle false
     */
    @SuppressWarnings("unused")
    boolean ifRuleAdded() { return lastruleaddok; }

    /**************************************************************************
     * getRules - getRules as a cursor
     *
     * @param filter sql filter string less WHERE
     * @param order  sql sort string less ORDER BY
     * @return cursor containing selected rules
     */
    Cursor getRules(String filter, @SuppressWarnings("SameParameterValue") String order) {
        String logmsg = "Invoked";
        String methodname =
                new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                LOGTAG,logmsg,THISCLASS,methodname);
        Cursor rv = DBCommonMethods.getTableRows(db,
                DBRulesTableConstants.RULES_TABLE,
                filter,
                order
        );
        logmsg = "Returnig Cursor with " + Integer.toString(rv.getCount()) + " rows.";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                LOGTAG,logmsg,THISCLASS,methodname);
        return rv;
    }

    /**************************************************************************
     * insertRule
     *
     * @param productid  key/id of the product
     * @param aisleid    key/id of the aisle
     * @param rulename   name of the rule
     * @param promptflag whether to prompt when applying the rule
     * @param actondate  date rule will be activated
     * @param period     the base period/frequency of rule application
     *                   0 = Days, 1 = weeks, 2 = fortnights, 3 = months,
     *                   4 = quarters, 5 = years. Use defined constants e.g.
     *                   PERIOD_DAYSASINT, PERDIOD_WEEKSASINT etc.
     * @param multiplier multiplier/factor used in conjucntion with period
     *                   to determine the frequency at which the rule will
     *                   be applied (basically added to actondate)
     */
    void insertRule(long productid,
                    long aisleid,
                    String rulename,
                    int numbertoget,
                    boolean promptflag,
                    long actondate,
                    int period,
                    int multiplier) {
        String msg = "Invoked";
        String methodname =
                new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                LOGTAG,msg,THISCLASS,methodname);

        long addedid;
        lastruleaddok = false;

        ContentValues cv = new ContentValues();
        cv.put(DBRulesTableConstants.RULES_PRODUCTREF_COL,productid);
        cv.put(DBRulesTableConstants.RULES_AISLEREF_COL,aisleid);
        cv.put(DBRulesTableConstants.RULES_NAME_COL,rulename);
        cv.put(DBRulesTableConstants.RULES_USES_COL,numbertoget);
        cv.put(DBRulesTableConstants.RULES_PROMPT_COL,promptflag);
        cv.put(DBRulesTableConstants.RULES_ACTON_COL,actondate);
        cv.put(DBRulesTableConstants.RULES_PERIOD_COL,period);
        cv.put(DBRulesTableConstants.RULES_MULTIPLIER_COL,multiplier);
        addedid = db.insert(DBRulesTableConstants.RULES_TABLE,
                null,
                cv);
        if (addedid >= 0) {
            lastruleadded = addedid;
            lastruleaddok = true;
        }
        msg = "Rule " + rulename + " Update=" + Boolean.toString(lastruleaddok);
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                LOGTAG,msg,THISCLASS,methodname);
    }

    /**************************************************************************
     * getRule          Get a rule, as a cursor
     *
     * @param ruleid id of the rule
     * @return cursor containg the respective rows (1 only)
     */
    Cursor getRule(long ruleid) {
        String msg = "Invoked";
        String methodname =
                new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                LOGTAG,msg,THISCLASS,methodname);
        Cursor rv;
        String filter = DBRulesTableConstants.RULES_ID_COL_FULL +
                " = " + Long.toString(ruleid) +
                SQLENDSTATEMENT;
         rv = DBCommonMethods.getTableRows(db,
                DBRulesTableConstants.RULES_TABLE,
                filter,
                ""
        );
        msg = "Return Cursor with" + Integer.toString(rv.getCount()) + "rowws.";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                LOGTAG,msg,THISCLASS,methodname);
        return rv;
    }

    /**************************************************************************
     * doesRuleExistbyRuleid
     *
     * @param ruleid id of the rule to be checked
     * @return true if rule exists, esle false
     */
    boolean doesRuleExistbyRuleid(long ruleid) {
        String msg = "Invoked";
        String methodname =
                new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                LOGTAG,msg,THISCLASS,methodname);
        boolean rv = false;
         Cursor csr = getRule(ruleid);
        if (csr.getCount() > 0 ) {
            rv = true;
        }
        csr.close();
        msg = "Rule ID=" + Long.toString(ruleid) +
                " Found=" + Boolean.toString(rv);
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                LOGTAG,msg,THISCLASS,methodname);
        return rv;
    }

    /**************************************************************************
     * modifyRuleAllValues - Modify all (normally permissable) values of a rule
     * Note! normally permissable excludes changing the
     * product and aisle references and of course the
     * ruleid itself.
     * Note! each passed value is checked against the
     * existing data and only changed data will be
     * applied.
     *
     * @param ruleid     the ruleid
     * @param rulename   the rulename
     * @param promptflag the promptflag
     * @param actondate  the actondate
     * @param period     the period
     * @param multiplier the multiplier
     */
    void modifyRuleAllValues(long ruleid,
                    String rulename,
                             int uses,
                    boolean promptflag,
                    long actondate,
                    int period,
                    int multiplier) {
        String msg = "Invoked";
        String methodname =
                new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                LOGTAG,msg,THISCLASS,methodname);

        String existing_rulename;
        int existing_uses;
        boolean existing_promptflag = false;
        long exisiting_actondate;
        int existing_period;
        int existing_multiplier;
        int updatedcount;

        Cursor csr = getRule(ruleid);
        if (csr.getCount() < 1) {
            csr.close();
            msg = "Rule=" + rulename + " ID=" + Long.toString(ruleid) + " not found.";
            LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                    LOGTAG,msg,THISCLASS,methodname);
            return;
        }

        if (csr.moveToFirst()) {
            existing_rulename = csr.getString(csr.getColumnIndex(
                    DBRulesTableConstants.RULES_NAME_COL
            ));
            existing_uses = csr.getShort(csr.getColumnIndex(
                    DBRulesTableConstants.RULES_USES_COL
            ));
            int retrieved_promptflag = csr.getInt(csr.getColumnIndex(
                    DBRulesTableConstants.RULES_PROMPT_COL
            ));
            if (retrieved_promptflag != 0) {
                existing_promptflag = true;
            }
            exisiting_actondate = csr.getLong(csr.getColumnIndex(
                    DBRulesTableConstants.RULES_ACTON_COL
            ));
            existing_period = csr.getInt(csr.getColumnIndex(
                    DBRulesTableConstants.RULES_PERIOD_COL
            ));
            existing_multiplier = csr.getInt(csr.getColumnIndex(
                    DBRulesTableConstants.RULES_MULTIPLIER_COL
            ));
            csr.close();
        } else {
            csr.close();
            msg = "Rule=" + rulename + " ID=" + Long.toString(ruleid) +
                    " not found. Not Updated.";
            LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                    LOGTAG,msg,THISCLASS,methodname);
            return;
        }

        int updatecount = 0;
        ContentValues cv = new ContentValues();
        if (!(rulename.equals(existing_rulename))) {
            cv.put(DBRulesTableConstants.RULES_NAME_COL,rulename);
            updatecount++;
        }
        if (!(uses == existing_uses)) {
            cv.put(DBRulesTableConstants.RULES_USES_COL,uses);
            updatecount++;
        }
        if (!(promptflag == existing_promptflag)) {
            cv.put(DBRulesTableConstants.RULES_PROMPT_COL,promptflag);
            updatecount++;
        }
        if (!(actondate == exisiting_actondate)) {
            cv.put(DBRulesTableConstants.RULES_ACTON_COL,actondate);
            updatecount++;
        }
        if (!(period == existing_period)) {
            cv.put(DBRulesTableConstants.RULES_PERIOD_COL,period);
            updatecount++;
        }
        if (!(multiplier == existing_multiplier)) {
            cv.put(DBRulesTableConstants.RULES_MULTIPLIER_COL,multiplier);
            updatecount++;
        }

        if (updatecount < 1) {
            msg = "Rule=" + rulename + " ID=" + Long.toString(ruleid) +
                    " no updates to do. Not Updated.";
            LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                    LOGTAG,msg,THISCLASS,methodname);
            return;
        }
        String[] whereargs = {Long.toString(ruleid)};
        String whereclause = DBRulesTableConstants.RULES_ID_COL + " = ?";
        updatedcount = db.update(DBRulesTableConstants.RULES_TABLE,
                cv,whereclause,whereargs);
        msg = "Rule=" + rulename + " ID=" + Long.toString(ruleid) +
                " Updated=" + Boolean.toString(updatedcount > 0);
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                LOGTAG,msg,THISCLASS,methodname);
    }

    /**************************************************************************
     *
     * @param filter        String containing SQL filter less WHERE
     * @param orderby       String contaning SQL ORDER less ORDER BY
     * @return              Cursor containing the Expanded Rule List
     */
    Cursor getExpandedRuleList(String filter, String orderby) {
        String msg = "Invoked";
        String methodname =
                new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                LOGTAG,msg,THISCLASS,methodname);
        Cursor rv;
        String sql = SQLSELECT +
                DBRulesTableConstants.RULES_ID_COL_FULL + ", " +
                DBRulesTableConstants.RULES_AISLEREF_COL_FULL + ", " +
                DBRulesTableConstants.RULES_PRODUCTREF_COL_FULL + ", " +
                DBRulesTableConstants.RULES_NAME_COL_FULL + ", " +
                DBRulesTableConstants.RULES_USES_COL_FULL + ", " +
                DBRulesTableConstants.RULES_PROMPT_COL_FULL + ", " +
                DBRulesTableConstants.RULES_ACTOON_COL_FULL + ", " +
                DBRulesTableConstants.RULES_PERIOD_COL_FULL + ", " +
                DBRulesTableConstants.RULES_MULTIPLIER_COL_FULL + ", " +

                DBShopsTableConstants.SHOPS_NAME_COL_FULL + ", " +
                DBShopsTableConstants.SHOPS_CITY_COL_FULL + ", " +
                DBShopsTableConstants.SHOPS_ORDER_COL_FULL + ", " +
                DBShopsTableConstants.SHOPS_STREET_COL_FULL + ", " +
                DBShopsTableConstants.SHOPS_STATE_COL_FULL + ", " +
                DBShopsTableConstants.SHOPS_NOTES_COL_FULL + ", " +

                DBAislesTableConstants.AISLES_SHOPREF_COL_FULL + ", " +
                DBAislesTableConstants.AISLES_NAME_COL_FULL + ", " +
                DBAislesTableConstants.AISLES_ORDER_COL_FULL + ", " +

                DBProductsTableConstants.PRODUCTS_NAME_COL_FULL + ", " +
                DBProductsTableConstants.PRODUCTS_NOTES_COL_FULL + " " +

                SQLFROM + DBRulesTableConstants.RULES_TABLE +

                SQLLEFTJOIN + DBAislesTableConstants.AISLES_TABLE +
                SQLON +
                DBRulesTableConstants.RULES_AISLEREF_COL_FULL + " = " +
                DBAislesTableConstants.AISLES_ID_COL_FULL +

                SQLLEFTJOIN +
                DBProductsTableConstants.PRODUCTS_TABLE +
                SQLON +
                DBRulesTableConstants.RULES_PRODUCTREF_COL_FULL + " = " +
                DBProductsTableConstants.PRODUCTS_ID_COL_FULL +

                SQLLEFTJOIN +
                DBShopsTableConstants.SHOPS_TABLE +
                SQLON +
                DBAislesTableConstants.AISLES_SHOPREF_COL_FULL + " = " +
                DBShopsTableConstants.SHOPS_ID_COL_FULL;

        if (filter.length() > 0 ) {
            sql = sql + SQLWHERE + filter;
        }
        if (orderby.length() > 0) {
            sql = sql + SQLORDERBY + orderby;
        }
        sql = sql + SQLENDSTATEMENT;
        rv =  db.rawQuery(sql,null);
        msg = "Rules Extracted=" + Integer.toString(rv.getCount());
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                LOGTAG,msg,THISCLASS,methodname);
        return rv;
    }

    /**************************************************************************
     *
     * @param filter        String containing SQL filter less WHERE
     * @param orderby       String containing SQL ORDER less ORDER BY
     * @return  cursor consisting of rules that will be prompted for
     *          (according to the current day)
     */
    public Cursor getPromptedRules(@SuppressWarnings({"ParameterCanBeLocal", "SameParameterValue"}) String filter, String orderby) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                LOGTAG,msg,THISCLASS,methodname);

        Cursor rv;
        filter = DBRulesTableConstants.RULES_ACTON_COL +
                " <= " +
                Long.toString(getDateTimeOfAllofToday()) +
                SQLAND +
                DBRulesTableConstants.RULES_PROMPT_COL +
                " >= 1 ";

        rv = getExpandedRuleList(filter,orderby);
        msg = "Prompted Rules Extracted=" + Integer.toString(rv.getCount());
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                LOGTAG,msg,THISCLASS,methodname);
        return rv;
    }

    /**************************************************************************
     * applyPromptedRule i.e. add the shopping list entry according to the rule
     * @param ruleid        Id of the Rule
     * @param skipapply     true to skip the application of the prompted rule
     */
    public void applyPromptedRule(long ruleid, boolean skipapply) {
        String msg = "Invoked";
        String methodname =
                new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                LOGTAG,msg,THISCLASS,methodname);

        if (!doesRuleExistbyRuleid(ruleid)) {
            msg = "Prompted Rule ID=" +
                    Long.toString(ruleid) + " Not Found. Not Updated.";
            LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                    LOGTAG,msg,THISCLASS,methodname);
            return;
        }

        Cursor csr = getRule(ruleid);
        csr.moveToFirst();
        long aisleid = csr.getLong(csr.getColumnIndex(
                DBRulesTableConstants.RULES_AISLEREF_COL
        ));
        long productid = csr.getLong(csr.getColumnIndex(
                DBRulesTableConstants.RULES_PRODUCTREF_COL
        ));
        int numbertoget = csr.getInt(csr.getColumnIndex(
                DBRulesTableConstants.RULES_USES_COL
        ));
        long actondate = csr.getLong(csr.getColumnIndex(
                DBRulesTableConstants.RULES_ACTON_COL
        ));
        int period = csr.getInt(csr.getColumnIndex(
                DBRulesTableConstants.RULES_PERIOD_COL
        ));
        int multiplier = csr.getInt(csr.getColumnIndex(
                DBRulesTableConstants.RULES_MULTIPLIER_COL
        ));
        csr.close();

        // Don't apply Rule if number to get is 0 or if
        // multiplier is 0
        if (numbertoget == 0 || multiplier ==0) {
            return;
        }

        if (!skipapply) {
            new DBShopListMethods(context).addOrUpdateShopListEntry(
                    aisleid,
                    productid,
                    numbertoget,
                    true,
                    false
            );

        }
        adjustActOnDate(ruleid, actondate, period, multiplier);
        msg = "Prompted Rule ID+" + Long.toString(ruleid) +
                "Applied and Adjusted";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                LOGTAG,msg,THISCLASS,methodname);
    }

    /**************************************************************************
     *
     * @return  number of rules applied to the shoppinglist
     */
    public int applyAutoAddRules() {
        String msg = "Invoked";
        String methodname =
                new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                LOGTAG,msg,THISCLASS,methodname);

        int rulesaddedcount = 0;
        DBShopListMethods dbShopListMethods = new DBShopListMethods(context);

        String filter = DBRulesTableConstants.RULES_ACTON_COL +
                " <= " +
                Long.toString(getDateTimeOfAllofToday()) +
                SQLAND +
                DBRulesTableConstants.RULES_PROMPT_COL +
                " = 0 ";
        // Loop through the rules until none are left to do.
        // Note actioning a rule may result in that rule being set with a new
        // date, that is still needing to be actioned, thus the assumed count
        // of 9999
        int pseudocount = 9999;
        while (pseudocount > 0) {
            Cursor csr = getExpandedRuleList(filter, "");
            pseudocount = csr.getCount();
            while (csr.moveToNext()) {
                rulesaddedcount++;
                long ruleid = csr.getLong(csr.getColumnIndex(
                        DBRulesTableConstants.RULES_ID_COL));
                long aisleid = csr.getLong(csr.getColumnIndex(
                        DBRulesTableConstants.RULES_AISLEREF_COL));
                long productid = csr.getLong(csr.getColumnIndex(
                        DBRulesTableConstants.RULES_PRODUCTREF_COL));
                int numbertoget = csr.getInt(csr.getColumnIndex(
                        DBRulesTableConstants.RULES_USES_COL));
                long actondate = csr.getLong(csr.getColumnIndex(
                        DBRulesTableConstants.RULES_ACTON_COL));
                int period = csr.getInt(csr.getColumnIndex(
                        DBRulesTableConstants.RULES_PERIOD_COL));
                int multiplier = csr.getInt(csr.getColumnIndex(
                        DBRulesTableConstants.RULES_MULTIPLIER_COL));

                // Skip Adding to shopping List and thus determining
                // next application date if numbertoget is 0 or if
                // multiplier is 0
                if (numbertoget == 0 || multiplier == 0) {
                    continue;
                }

                dbShopListMethods.addOrUpdateShopListEntry(
                        aisleid, productid,
                        numbertoget,
                        true,
                        false
                );
                adjustActOnDate(ruleid, actondate, period, multiplier);
            }
            csr.close();
        }
        msg = "Rule applied to the ShoppingList=" +
                Integer.toString(rulesaddedcount);
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                LOGTAG,msg,THISCLASS,methodname);
        return rulesaddedcount;
    }

    /**************************************************************************
     *
     * @param rulesexist                true if to find existing rules, else
     *                                  only find non-existing rules
     * @param minimumruleperiodindays   Minimum number of days of shopping
     *                                  history for a stocked item to be
     *                                  included.
     * @param minimumbuycount           Minumum number of purchases of stocked
     *                                  item for it to be included.
     * @return                          Cursor containing rules
     */
    public Cursor getToolRules(boolean rulesexist,
                               int minimumruleperiodindays,
                               int minimumbuycount,String filter, String orderby) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);

        Cursor rv;
        String columns[] = new String[] {
                "0 " + SQLAS + SQLSTD_ID,
                DBProductusageTableConstants.PRODUCTUSAGE_PRODUCTREF_COL,
                DBProductusageTableConstants.PRODUCTUSAGE_AISLEREF_COL,
                DBProductusageTableConstants.PRODUCTUSAGE_COST_COL,
                DBProductusageTableConstants.PRODUCTUSAGE_BUYCOUNT_COL,
                DBProductusageTableConstants.PRODUCTUSAGE_FIRSTBUYDATE_COL,
                DBProductusageTableConstants.PRODUCTUSAGE_LATESTBUYDATE_COL,
                DBProductusageTableConstants.PRODUCTUSAGE_ORDER_COL,
                DBProductusageTableConstants.PRODUCTUSAGE_RULESUGGESTFLAG_COL,
                DBProductusageTableConstants.PRODUCTUSAGE_CHECKLISTFLAG_COL,
                DBProductusageTableConstants.PRODUCTUSAGE_CHECKLISTCOUNT_COL,

                "((" +
                        DBProductusageTableConstants.PRODUCTUSAGE_LATESTBUYDATE_COL +
                        " - " +
                        DBProductusageTableConstants.PRODUCTUSAGE_FIRSTBUYDATE_COL +
                        ") / (86400000)" +
                        ") " + SQLAS + DBConstants.CALCULATED_RULEPERIODINDAYS,

                DBProductsTableConstants.PRODUCTS_NAME_COL,

                DBAislesTableConstants.AISLES_NAME_COL,
                DBAislesTableConstants.AISLES_ORDER_COL,
                DBAislesTableConstants.AISLES_SHOPREF_COL,

                DBShopsTableConstants.SHOPS_NAME_COL,
                DBShopsTableConstants.SHOPS_CITY_COL,
                DBShopsTableConstants.SHOPS_ORDER_COL,

                DBRulesTableConstants.RULES_ID_COL_FULL +
                        SQLAS + DBRulesTableConstants.RULES_ALTID_COL,
                DBRulesTableConstants.RULES_AISLEREF_COL,
                DBRulesTableConstants.RULES_PRODUCTREF_COL,
                DBRulesTableConstants.RULES_NAME_COL,
                DBRulesTableConstants.RULES_USES_COL,
                DBRulesTableConstants.RULES_PROMPT_COL,
                DBRulesTableConstants.RULES_ACTON_COL,
                DBRulesTableConstants.RULES_PERIOD_COL,
                DBRulesTableConstants.RULES_MULTIPLIER_COL

        };
        String joinclauses = SQLLEFTJOIN +
                DBProductsTableConstants.PRODUCTS_TABLE +
                SQLON +
                DBProductusageTableConstants.PRODUCTUSAGE_PRODUCTREF_COL + " = " +
                DBProductsTableConstants.PRODUCTS_ID_COL_FULL + " " +

                SQLLEFTJOIN +
                DBAislesTableConstants.AISLES_TABLE +
                SQLON +
                DBProductusageTableConstants.PRODUCTUSAGE_AISLEREF_COL + " = " +
                DBAislesTableConstants.AISLES_ID_COL_FULL +

                SQLLEFTJOIN +
                DBShopsTableConstants.SHOPS_TABLE +
                SQLON +
                DBAislesTableConstants.AISLES_SHOPREF_COL + " = " +
                DBShopsTableConstants.SHOPS_ID_COL_FULL +

                SQLLEFTJOIN +
                DBRulesTableConstants.RULES_TABLE +
                SQLON +
                DBProductusageTableConstants.PRODUCTUSAGE_PRODUCTREF_COL + " =  " +
                DBRulesTableConstants.RULES_PRODUCTREF_COL +
                SQLAND +
                DBProductusageTableConstants.PRODUCTUSAGE_AISLEREF_COL + " = " +
                DBRulesTableConstants.RULES_AISLEREF_COL
                ;
        String ruleexistoption = DBRulesTableConstants.RULES_ID_COL_FULL;
        if (rulesexist) {
            ruleexistoption = ruleexistoption + SQLISNOTNULL;
        } else {
            ruleexistoption = ruleexistoption + SQLISNULL;
        }


        String whereclause = ruleexistoption +
                SQLAND +
                "(" + DBConstants.CALCULATED_RULEPERIODINDAYS + " - " +
                Integer.toString(minimumruleperiodindays) +
                ") >= 0" +
                SQLAND +
                DBProductusageTableConstants.PRODUCTUSAGE_BUYCOUNT_COL + " >= " + Integer.toString(minimumbuycount);
        if (filter.length() > 0) {
            whereclause = whereclause + SQLAND + filter;
        }
        rv =  db.query(DBProductusageTableConstants.PRODUCTUSAGE_TABLE + joinclauses,
                columns,whereclause,null,null,null,orderby);
        msg = "Returning Cursor Rules=" + Integer.toString(rv.getCount());
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        return rv;
    }

    public Cursor getDisabledRules(String orderby) {
        Cursor rv;
        String filter =
                DBProductusageTableConstants.PRODUCTUSAGE_RULESUGGESTFLAG_COL_FULL +
                " >= " +
                DBProductusageTableConstants.RULESUGGESTFLAG_DISABLE;
        rv = getToolRules(false, 0,0,filter,orderby);
        return rv;
    }

    /**
     *
     * @return  1 millsecond before midnight today
     */
    private long getDateTimeOfAllofToday() {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH,1); // tomorrow
        cal.set(Calendar.MILLISECOND,0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.HOUR_OF_DAY,0);
        cal.add(Calendar.MILLISECOND,-1);
        return cal.getTimeInMillis();
    }



    /**************************************************************************
     * Adjust the acton date (date the rule is applied to the shopping list)
     * according to the period (DAYS, WEEKS..) and the period multiplier.
     *
     * @param ruleid    the id of the rule to be adjusted
     * @param olddate   the actondate from the rule
     * @param period    the period used by the rule
     * @param multiplier    the period multiplier
     */
    private void adjustActOnDate(long ruleid,
                                 long olddate,
                                 int period,
                                 int multiplier) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);

        int updatecount;
        Calendar adjustcal = Calendar.getInstance();
        adjustcal.setTimeInMillis(olddate);
        switch (period) {
            case DBRulesTableConstants.PERIOD_DAYSASINT:
                adjustcal.add(Calendar.DAY_OF_MONTH,multiplier);
                break;
            case DBRulesTableConstants.PERIOD_WEEKSASINT:
                adjustcal.add(Calendar.WEEK_OF_MONTH,multiplier);
                break;
            case DBRulesTableConstants.PERIOD_FORTNIGHTSASINT:
                adjustcal.add(Calendar.WEEK_OF_MONTH, (multiplier * 2));
                break;
            case DBRulesTableConstants.PERIOD_MONTHSASINT:
                adjustcal.add(Calendar.MONTH,multiplier);
                break;
            case DBRulesTableConstants.PERIOD_QUARTERSASINT:
                adjustcal.add(Calendar.MONTH,(multiplier * 4));
                break;
            case DBRulesTableConstants.PERIOD_YEARSASINT:
                adjustcal.add(Calendar.YEAR,multiplier);
                break;
        }
        long newdate = adjustcal.getTimeInMillis();
        ContentValues cv = new ContentValues();
        cv.put(DBRulesTableConstants.RULES_ACTON_COL,newdate);
        String whereclause = DBRulesTableConstants.RULES_ID_COL_FULL + " = ?";
        String whereargs[] = new String[] { Long.toString(ruleid)};
        updatecount = db.update(DBRulesTableConstants.RULES_TABLE,cv,whereclause,whereargs);
        msg = "Rule ID=" + Long.toString(ruleid) +
                " Update=" + Boolean.toString(updatecount > 0) +
                " New Date=" + sdf.format(newdate) +
                " as long=" + Long.toString(newdate) ;
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
    }

    /**************************************************************************
     *
     * @param aisleid       id (long) of the aisle
     * @param productid     id (long) of the product
     * @return              an arraylist of messages regarding the Rules rows
     *                      that would be deleted.
     */
    ArrayList<String> ruleDeleteImpact(long aisleid, long productid) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        ArrayList<String> rv = new ArrayList<>();

        String rlfilter = DBRulesTableConstants.RULES_AISLEREF_COL_FULL +
                " = " + Long.toString(aisleid) +
                " AND " +
                DBRulesTableConstants.RULES_PRODUCTREF_COL_FULL +
                " = " + Long.toString(productid);
        Cursor rlcsr = getExpandedRuleList(rlfilter,"");
        if (rlcsr.getCount() > 0 ) {
            while (rlcsr.moveToNext()) {
                String rulename = rlcsr.getString(rlcsr.getColumnIndex(
                        DBRulesTableConstants.RULES_NAME_COL
                ));
                String aislename = rlcsr.getString(rlcsr.getColumnIndex(
                        DBAislesTableConstants.AISLES_NAME_COL
                ));
                String productname = rlcsr.getString(rlcsr.getColumnIndex(
                        DBProductsTableConstants.PRODUCTS_NAME_COL
                ));
                String shopname = rlcsr.getString(rlcsr.getColumnIndex(
                        DBShopsTableConstants.SHOPS_NAME_COL
                ));
                rv.add("Rule " + rulename +
                        " for Product " + productname +
                        " in Aisle" + aislename +
                        ", Shop " +shopname +
                        " would be deleted."

                );
            }
        }
        return rv;
    }

    /**************************************************************************
     * deleteRule           Delete a rule
     *
     * @param ruleid the id of the rule to delete
     */
    void deleteRule(long ruleid) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);

        int deletecount = 0;
        if (doesRuleExistbyRuleid(ruleid)) {
            String whereargs[] = {Long.toString(ruleid)};
            deletecount = db.delete(
                    DBRulesTableConstants.RULES_TABLE,
                    DBRulesTableConstants.RULES_ID_COL + " = ?",whereargs
            );
        }
        msg = "Rule ID=" + Long.toString(ruleid) +
                " Deleted=" + Boolean.toString(deletecount > 0);
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
    }
}
