package mjt.shopwise;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * OOTAD out_of-the-actual-database classes/methods/schema
 * AKA relatively simple database changes
 * ============================================================================
 * Also includes complementary Database classes DBDatabase, DBTable & DBColumn
 * and associated methods.
 * This allowing an out-of-the-actual-database (OOTAD) schema that can be used
 * for relatively simple table and column definition, thier use in the SQLite
 * onCreate, onUpgrade and onDowngrade overidden.
 * Methods actionDBAlterSQL and actionDBBuildSQL can be used to implment and
 * alter actual schema based upon the OOTAD.
 * <p>
 * More specifically actionDBBuilSQL generates and actions CREATE ??? IF NOT EXISTS ... SQL
 * so if a new table is added to the respective OOTAB (DBColumn objects fed into a DBTable object)
 * that table will automatically be created.
 * Similarily actionDBAlterSQL generates ALTER SQL to ADD columns. However, the underlying process
 * compares the actual schema with the proposed new schema (no changes = nothing done)
 * <p>
 * In brief create a String Arraylist of DBColumn objects eg :-
 * <p>
 * ArrayList<DBColumn> <my_table_columns> = new ArrayList<DBColumn>();
 * <p>
 * <my_table_columns>.add(DBColumn(<my_column_name>,     // String
 * <my_column_type>,                                // String according to SQLite Datatypes  #1
 * <primary_index_boolean>,                         // true = this will be primary index col #2
 * <my_default_vale_if_one_else_empty_string>););   // String ("" to not specify default)
 * ........
 * ArrayList<DBTable> <my_list_of_tables> = new ArrayList<DBTable>();
 * <p>
 * <my_list_of_tables>.add(DBTable(<my_table_name>,<my_table_columns>);
 * ........
 * DBDatabase <my_database_schema> = new DBDatabase(<my_database_name>,<>)                        #3
 * <p>
 * <my_database_schema>.actionDBBuildSQL(<real_database>); // Build or add new tables
 * <my_database_schema>.actionDBALterSQL(<real_database>); // Add non existing columns to existing
 * would have actionDBBuildSQL in onCreate or it's equivalent
 * would have both in onUpgrade or it's equivalent
 * <p>
 * NOTE!!! there is a flag that is propogated  and from DBColumn through DBTable to DBDatabase
 * this should be checked as it can indicate potential failures such as specifying an invalid
 * DATATYPE, no table name. (note will be set to false throught the levels)
 * DBDatabase method isDBDatabaseUsable() returns boolean true if useable
 * e.g if(<mydatabase>.isDBDatabaseUsable()) {<mydatabase>.actionDBBuildSQL(<real_database>); }
 * <p>
 * Reason(s) for the unusable state can be obtained from problemmsg property via
 * get<class>ProblemMessage() for the message(s) stored at that class/object level or via
 * get<class>AllProblemMessages() for traversal of the underlying objects within the DBDatabase
 * DBTable, DBColumn hierarchy (no ALL version for DBColumn class as it's the lowest level).
 * <p>
 * <p>
 * #1 All types should be catered for. However, they are then converted to thier more primitive
 * types INTEGER, TEXT, REAL or NUMERIC.
 * #2 Supports multiple primary index columns
 * #3 At present the database name is not used externally as a comparison with the actual, so it
 * need not be the same.
 * <p>
 * Both actionDBBuildSQL and actionDBAlterSQL have respective underlying generate...SQLStatements
 * methods. These can be used when devloping to then grab and check the generated SQL they can
 * then be copied and pasted to check/confirm that the results are as expected (eg copy and paste
 * them into SQLiteManger for Firefox from Debug).
 * <p>
 * WARNING!!! if you were to add a table and then use actionDBAlterSQL that was not preceeded
 * by actionDBBuildSQLyou would get an exception as the ALTER statements for the new table would
 * generate even though the table did not exist
 * NOTE!!! ALTER statements should now not be generated if the table to which they relate does
 * not exist in the actual database
 */
class DBDatabase {
    private boolean usable;
    private String database_name;
    private ArrayList<DBTable> database_tables;
    private String problem_msg;

    /**
     * Instantiates a new Db database.
     */
    public DBDatabase() {
        this.usable = false;
        this.database_name = "";
        this.database_tables = new ArrayList<>();
        this.problem_msg = "WDBD0100 - Uninstantiated - " +
                "Use setDBDatabaseName to set the Database Name. " +
                "Use addDBTableToDBDatabase to add at least 1 Table or " +
                "Use addDBTablesToDBDatabase to add at least 1 Table or " +
                "Use addMultipleTablesToDBDatabase to add at least 1 Table";
    }

    /**
     * Instantiates a new Db database.
     *
     * @param database_name the database name
     */
    public DBDatabase(String database_name) {
        this();
        this.database_name = database_name;
        this.problem_msg = "WDBD0101 - Partially Instantiated -  " +
                "Use addDBTableToDBDatabase to add at least 1 Table or " +
                "Use addDBTablesToDBDatabase to add at least 1 Table or " +
                "Use addMultipleTablesToDBDatabase to add at least 1 Table";
    }

    /**
     * Instantiates a new Db database.
     *
     * @param database_name  the database name
     * @param database_table the database table
     */
    public DBDatabase(String database_name, DBTable database_table) {
        this();
        this.database_name = database_name;
        this.problem_msg = "";
        this.database_tables.add(database_table);
        this.checkDBDatabaseIsUsable("DBDatabase (database_name, database_table (singular))");
    }

    /**
     * Instantiates a new Db database.
     *
     * @param database_name   the database name
     * @param database_tables the database tables
     */
    public DBDatabase(String database_name, ArrayList<DBTable> database_tables) {
        this();
        this.database_name = database_name;
        this.problem_msg = "";
        this.database_tables.addAll(database_tables);
        this.checkDBDatabaseIsUsable("DBDatabse Full Contructor");
    }

    /**
     * Is db database usable boolean.
     *
     * @return the boolean
     */
    public boolean isDBDatabaseUsable() { return this.usable; }

    /**
     * Number of tablesin db database long.
     *
     * @return the long
     */
    public long numberOfTablesinDBDatabase() { return this.database_tables.size(); }

    /**
     * Sets db database name.
     *
     * @param database_name the database name
     */
    public void setDBDatabaseName(String database_name) {
        this.database_name = database_name;
    }

    /**
     * Gets db database name.
     *
     * @return the db database name
     */
    public String getDBDatabaseName() { return this.database_name; }

    /**
     * Gets db database problem msg.
     *
     * @return the db database problem msg
     */
    public String getDBDatabaseProblemMsg() { return this.problem_msg; }

    /**
     * Gets all db database problem msgs.
     *
     * @return the all db database problem msgs
     */
    public String getAllDBDatabaseProblemMsgs() {
        String problem_messages = this.getDBDatabaseProblemMsg();
        for(DBTable dt : this.database_tables) {
            problem_messages = problem_messages + dt.getAllDBTableProblemMsgs();
        }
        return  problem_messages;
    }

    /**
     * Add db table to db database.
     *
     * @param database_table the database table
     */
    public void addDBTableToDBDatabase(DBTable database_table) {
        this.database_tables.add(database_table);
        this.problem_msg = "";
        this.checkDBDatabaseIsUsable("AddDBTableToDBDatabase");
    }

    /**
     * Add db tables to db database.
     *
     * @param database_tables the database tables
     */
    public void addDBTablesToDBDatabase(ArrayList<DBTable> database_tables) {
        this.database_tables.addAll(database_tables);
        this.problem_msg = "";
        this.checkDBDatabaseIsUsable("AddDBTablesToDBDatabase");
    }

    /**
     * Check db database is usable boolean.
     *
     * @param caller the caller
     * @return the boolean
     */
    public boolean checkDBDatabaseIsUsable(String caller) {
        this.usable = false;
        if(this.anyEmptyDBTablesInDBDatabase(caller)) {
            this.usable = true;
            if (this.database_name.length() < 1) {
                this.problem_msg = this.problem_msg + " EDBT0105 - Inavlid Table Name - " +
                        "Must be at least 1 character in length. " +
                        "Caller=(" + caller + ")";
                this.usable = false;
            }
        }
        return this.usable;
    }

    /**
     * Any empty db tables in db database boolean.
     *
     * @param caller the caller
     * @return the boolean
     */
    public boolean anyEmptyDBTablesInDBDatabase(String caller) {
        boolean rc = true;
        if(this.database_tables.isEmpty()) {
            this.problem_msg = this.problem_msg + "EDBD0103 - No Tables - " +
                    "Must have at least 1 Table in the Database. " +
                    "Caller=(" + caller + ")";
            this.usable = false;
            return false;
        }
        for(DBTable dt : this.database_tables) {
            if(!dt.isDBTableUsable()) {
                this.problem_msg = this.problem_msg + "EDBT0104 - Table " + dt.getDBTableName() +
                        " is unusable. Must be usable. " +
                        "Caller=(" + caller + ")";
                rc = false;
            }

        }
        return rc;
    }

    /**
     * Generate db build sql array list.
     *
     * @param db the db
     * @return the array list
     */
// Generate all of the SQL to build the defined database
    public ArrayList<String> generateDBBuildSQL(SQLiteDatabase db) {
        ArrayList<String> generatedSQLStatements = new ArrayList<>();
        for(DBTable dbt : this.database_tables) {
            String current_create_string = dbt.getSQLCreateString(db);
            if(current_create_string.length() > 0) {
                generatedSQLStatements.add(current_create_string);
            }
            //generatedSQLStatements.add(dbt.getSQLCreateString(db));
        }
        return generatedSQLStatements;
    }

    /**
     * Generate export schema sql string.
     *
     * @return the string
     */
// Generate SQL that could be used to Build Database and Tables elsewhere (part 1 of export)
    public String generateExportSchemaSQL() {
        String sql = "";
        String tablesql = "";
        if(this.usable) {
            //sql = "--CRTDB_START\n";
            //sql = sql + " CREATE DATABASE IF NOT EXISTS `" + this.database_name + "` ;\n" +
            //        "--CRTDB_USESTART\n" +
            //        " USE `" + this.database_name + "`;" + "\n" ;
            //sql = sql + "--CRTDB_USEFINISH\n";
            for(DBTable dbt : this.database_tables) {
                tablesql = dbt.getSQLTableCreateAsString(true);
                if(tablesql.length() > 0) {
                    sql = sql + "--CRTTB_START\n" + tablesql + "\n--CRTTB_FINISH\n";
                }
            }
            //sql = sql + "--CRTDB_FINISH\n";
        }
        return sql;
    }

    /**
     * Generate export data sql string.
     *
     * @param db the db
     * @return the string
     */
// Export All Table Data (not expect to work as no escaping as yet)
    //TODO 1 Need to do equiv to MYSQL_REAL_ESCAPE otherwise OK load
    public String generateExportDataSQL(SQLiteDatabase db) {
        String sql = "";
        String sqlcols = "";
        Cursor csr;
        String coldata = "";
        if(!this.usable) { return sql; }
        //sql =
        //sql = "--TDL_USESTART\n USE `" + this.database_name + "`;\n--TDL_USEFINISH\n";
        for(DBTable dbt : this.database_tables) {
            // Select all rows from the respective table into Cursor csr.
            String sqlstr = " SELECT * FROM " + dbt.getDBTableName() + ";";
            csr = db.rawQuery(sqlstr,null);
            // Skip if no rows
            if(csr.getCount() > 0 ){
                int coli = 0;
                // Build Column list
                ArrayList<Integer> coltype = new ArrayList<>();
                sqlcols = "";
                for(DBColumn dbtc : dbt.getTableDBColumns()) {
                    if(coli++ > 0) {
                        sqlcols = sqlcols + ", ";
                    }
                    if(dbtc.getDBColumnType().equals("TEXT")) {
                        coltype.add(1);
                    } else {
                        coltype.add(0);
                    }
                    sqlcols = sqlcols + "`" + dbtc.getDBColumnName() + "` ";
                }
                // Process each row from the table
                csr.moveToPosition(-1);
                while(csr.moveToNext()) {
                    sql = sql + "--TBL_INSERTSTART\nINSERT INTO " + dbt.getDBTableName() + " ( " + sqlcols + ") VALUES(";
                    for(int i=0; i < coli;i++) {
                        coldata = csr.getString(i);
                        if(coldata == null) {coldata = ""; }
                        if(coltype.get(i) == 1) {
                            if(coldata.length() > 0) {
                                try {
                                    coldata = coldata.replaceAll("\'", "#@APOST@#")
                                            .replaceAll("\"", "#@QUOTE@#");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            sql = sql + "'" + coldata + "'";
                        } else {
                            sql = sql + csr.getString(i);
                        }
                        if(i < (coltype.size()-1)) {
                            sql = sql + ", ";
                        }
                    }
                    sql = sql + " );\n--TBL_INSERTFINISH\n";
                }
            } else {
                sql = sql + "-- ERROR - TABLE " + dbt.getDBTableName() + " IS EMPTY SKIPPED \n";
            }
            csr.close();
        }
        return sql;
    }

    /**
     * Action db build sql.
     *
     * @param db the db
     */
//==============================================================================================
    // Actually perform the Table create statments (note CREATE IF NOT EXISTS) so will
    // only create tables that don't exist.
    // As such there is no need to compare what actually exists with the coded schema/design
    public void actionDBBuildSQL(SQLiteDatabase db) {
        ArrayList<String> actionsql = new ArrayList<>();
        actionsql.addAll(generateDBBuildSQL(db));
        for(String currentsql : actionsql) {
            db.execSQL(currentsql);
        }
    }

    /**
     * Generate db alter sql array list.
     *
     * @param db the db
     * @return the array list
     */
//==============================================================================================
    // Generate all of the SQL ALter statements, these determined by comparing the DBDatabase
    // schema against the actual database (data extracted via PRAGMA).
    // NOTE!!! this is limited to basically just comparing the databse column names
    // NOTE!!! This only considers adding columns that don't exist.
    public ArrayList<String> generateDBAlterSQL(SQLiteDatabase db) {
        ArrayList<String> generatedSQLStatements = new ArrayList<>();
        for(DBTable dbt: this.database_tables) {
            generatedSQLStatements.addAll(dbt.getSQLAlterToAddNewColumns(db));
        }
        return generatedSQLStatements;
    }

    /**
     * Action db alter sql.
     *
     * @param db the db
     */
//==============================================================================================
    // Actually perform the automatically generated SQL ALTERs (Add columns)
    // See generateDBAlterSQL
    public void actionDBAlterSQL(SQLiteDatabase db) {
        ArrayList<String> actionsql = new ArrayList<>();
        actionsql.addAll(generateDBAlterSQL(db));
        for(String currentsql : actionsql) {
            db.execSQL(currentsql);
        }
    }
}
