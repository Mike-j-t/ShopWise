package mjt.shopwise;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * DBRuleMethods - Database Methods for Rule Handling
 */
public class DBRuleMethods {
    private static final  String LOGTAG = "SW-DBRM";
    private Context context;
    private DBDAO dbdao;
    private static SQLiteDatabase db;
    private static long lastruleadded = -1;
    private static boolean lastruleaddok = false;
    private static boolean lastruleupdatedok = false;

    /**
     * Instantiates a new Db rule methods.
     *
     * @param ctxt the ctxt
     */
    DBRuleMethods(Context ctxt) {
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
        return DBCommonMethods.getTableRowCount(db,
                DBRulesTableConstants.RULES_TABLE
        );
    }

    /**************************************************************************
     * getLastRuleAdded - returns id of the last rule added
     *
     * @return id of the last rule added
     */
    long getLastRuleAdded() { return lastruleadded; }

    /**************************************************************************
     * ifRuleAdded - returns the status of the last rule insert
     *
     * @return true if ok, esle false
     */
    boolean ifRuleAdded() { return lastruleaddok; }

    /**************************************************************************
     * getRules - getRules as a cursor
     *
     * @param filter sql filter string less WHERE
     * @param order  sql sort string less ORDER BY
     * @return cursor containing selected rules
     */
    Cursor getRules(String filter, String order) {
        return DBCommonMethods.getTableRows(db,
                DBRulesTableConstants.RULES_TABLE,
                filter,
                order
        );
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
    }

    /**************************************************************************
     * getRule          Get a rule, as a cursor
     *
     * @param ruleid id of the rule
     * @return cursor containg the respective rows (1 only)
     */
    Cursor getRule(long ruleid) {
        String filter = DBRulesTableConstants.RULES_ID_COL_FULL +
                " = " + Long.toString(ruleid) +
                DBConstants.SQLENDSTATEMENT;
         return DBCommonMethods.getTableRows(db,
                DBRulesTableConstants.RULES_TABLE,
                filter,
                ""
        );
    }

    /**************************************************************************
     * doesRuleExistbyRuleid
     *
     * @param ruleid id of the rule to be checked
     * @return true if rule exists, esle false
     */
    boolean doesRuleExistbyRuleid(long ruleid) {
        boolean rv = false;
         Cursor csr = getRule(ruleid);
        if (csr.getCount() > 0 ) {
            rv = true;
        }
        csr.close();
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

        String existing_rulename = "";
        int existing_uses = 0;
        boolean existing_promptflag = false;
        long exisiting_actondate = 0;
        int existing_period = -1;
        int existing_multiplier = 0;

        Cursor csr = getRule(ruleid);
        if (csr.getCount() < 1) {
            csr.close();
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
            return;
        }
        String[] whereargs = {Long.toString(ruleid)};
        String whereclause = DBRulesTableConstants.RULES_ID_COL + " = ?";
        db.update(DBRulesTableConstants.RULES_TABLE,cv,whereclause,whereargs);
    }

    /**************************************************************************
     *
     * @param filter
     * @param orderby
     * @return
     */
    Cursor getExpandedRuleList(String filter, String orderby) {
        String sql = DBConstants.SQLSELECT +
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

                DBConstants.SQLFROM + DBRulesTableConstants.RULES_TABLE +

                DBConstants.SQLLEFTJOIN + DBAislesTableConstants.AISLES_TABLE +
                DBConstants.SQLON +
                DBRulesTableConstants.RULES_AISLEREF_COL_FULL + " = " +
                DBAislesTableConstants.AISLES_ID_COL_FULL +

                DBConstants.SQLLEFTJOIN + DBProductsTableConstants.PRODUCTS_TABLE +
                DBConstants.SQLON +
                DBRulesTableConstants.RULES_PRODUCTREF_COL_FULL + " = " +
                DBProductsTableConstants.PRODUCTS_ID_COL_FULL +

                DBConstants.SQLLEFTJOIN +
                DBShopsTableConstants.SHOPS_TABLE +
                DBConstants.SQLON +
                DBAislesTableConstants.AISLES_SHOPREF_COL_FULL + " = " +
                DBShopsTableConstants.SHOPS_ID_COL_FULL;

        if (filter.length() > 0 ) {
            sql = sql + DBConstants.SQLWHERE + filter;
        }
        if (orderby.length() > 0) {
            sql = sql + DBConstants.SQLORDERBY + orderby;
        }
        sql = sql + DBConstants.SQLENDSTATEMENT;
        return db.rawQuery(sql,null);
    }

    /**************************************************************************
     *
     * @param filter
     * @param orderby
     * @return  cursor consisting of rules that will be prompted for
     *          (according to the current day)
     */
    public Cursor getPromptedRules(String filter,String orderby) {


        filter = DBRulesTableConstants.RULES_ACTON_COL +
                " <= " +
                Long.toString(getDateTimeOfAllofToday()) +
                DBConstants.SQLAND +
                DBRulesTableConstants.RULES_PROMPT_COL +
                " >= 1 ";

        return getExpandedRuleList(filter,orderby);
    }

    /**************************************************************************
     * applyPromptedRule i.e. add the shopping list entry according to the rule
     * @param ruleid
     * @param skipapply
     */
    public void applyPromptedRule(long ruleid, boolean skipapply) {

        if (!doesRuleExistbyRuleid(ruleid)) {
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
    }

    /**************************************************************************
     *
     * @return  number of rules applied to the shopignlist
     */
    public int applyAutoAddRules() {

        int rulesaddedcount = 0;
        DBShopListMethods dbShopListMethods = new DBShopListMethods(context);

        String filter = DBRulesTableConstants.RULES_ACTON_COL +
                " <= " +
                Long.toString(getDateTimeOfAllofToday()) +
                DBConstants.SQLAND +
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
        return rulesaddedcount;
    }

    //TODO Working on this
    public Cursor getToolRules(boolean rulesexist) {
        Cursor rv = null;

        return rv;
    }

    /**
     *
     * @return  1 millsecond before midnight today
     */
    private long getDateTimeOfAllofToday() {
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
        db.update(DBRulesTableConstants.RULES_TABLE,cv,whereclause,whereargs);
    }
    /**************************************************************************
     *
     * @param aisleid       id (long) of the aisle
     * @param productid     id (long) of the product
     * @return              an arraylist of messages regarding the Rules rows
     *                      that would be deleted.
     */
    ArrayList<String> ruleDeleteImpact(long aisleid, long productid) {
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
        if (doesRuleExistbyRuleid(ruleid)) {
            String whereargs[] = {Long.toString(ruleid)};
            db.delete(
                    DBRulesTableConstants.RULES_TABLE,
                    DBRulesTableConstants.RULES_ID_COL + " = ?",whereargs
            );
        }
    }
}
