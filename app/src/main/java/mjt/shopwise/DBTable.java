package mjt.shopwise;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;

/**
 * The type Db table.
 */
/*==================================================================================================
//==================================================================================================
// DBTable class - Database Table Class has table name and holds list of the columns in the table
// Note!! Table name is converted to lowercase
//
// If default constructor used then the table will be flagged as unusable. It will require:-
//        1) A table name via the setDBTable
//        2) One or more columns via methods :-
//            AddDBColumnToDBTable (adds a single column via a DBColumn object)
//            AddDBColumnsToDBTable (adds multiple columns (1 or more) via an ArrayList of DBCOlumn
//                Objects.
//            AddMultipleColumnstoDBTable just a psuedonym for AddColumnsToDBTable, that is easier
//                to differentiate between AddDBColumnToDBTAble and AddColumnsToDBTable (plural)
//
// If Intermediate Constructor used (sets the table name but not any columns). The table will be
// flagged as unusable. However, the table name would be set (unless "" given as the table name).
// Assuming the name is not "" then a column or columns will have to be added as per 2) above.
// If "" given as name then 1) above would have to be applied.
//
// If HigherIntermediate Constructor (sets the table name and 1 column) then the table would be
// usable. However 2) above would be required if further columns were needed.
//
// If Full constructor used then (sets table name and multiple columns as per and ArrayList of
// DBColumn objects) table should be usable.
// NOTE!!! In all cases the assumption is that all DBColumns applied are themselves usable. If any
//         are not then the table will be flagged as unusable. Refer to DBColumns for their
//         usability criteria.
//==================================================================================================
//================================================================================================*/
@SuppressWarnings("WeakerAccess")
class DBTable {
    private boolean usable;                         //* Flag to denote if this object can be used
    private String table_name;                      // The table name
    private ArrayList<DBColumn> table_columns;      // The list of columns in the table
    private String problem_msg;                     //* Holds error/warning message

    //* signifies internally managed property

    /**
     * Instantiates a new Db table.
     */
//==============================================================================================
    // Default DBTable Object Constructor
    public DBTable() {
        this.usable = false;
        this.table_name = "";
        this.table_columns = new ArrayList<>();
        this.problem_msg = "WDBT0004 - Uninstantiated - " +
                "Use addDBColumnToDBTable to add at least 1 usable DBColumn or " +
                "Use addDBColumnsToDBTable to add at least 1 usable DBColumn or " +
                "Use AddMultipleColumnstoDBTable to add at least 1 usable DBColumn. " +
                "Note any unusable DBcolumn will render table unusable. " +
                "Also use setDBTableTableName to set the Table Name. " +
                "Caller=DBTable (Default Constructor)";
    }

    /**
     * Instantiates a new Db table.
     *
     * @param table_name the table name
     */
//==============================================================================================
    // Intermediate DBTable Object Constructor - Just the table name
    @SuppressWarnings("unused")
    public DBTable(String table_name) {
        this.usable = false;
        this.table_name = table_name.toLowerCase();
        this.table_columns = new ArrayList<>();
        this.problem_msg = "WDBT0005 - Partially Instantiated - " +
                "Use addDBColumnToDBTable to add at least 1 usable DBColumns or " +
                "Use addDBColumnsToDBTable to add at least 1 usable DBColumn or " +
                "Use AddMultipleColumnstoDBTable to add at least 1 usable DBColumn. " +
                "Note any unusable DBColumn will render table unusable. " +
                "Caller=DBTable (Table Name only Constructor)";
        if(table_name.length() < 1) {
            this.problem_msg = this.problem_msg + "WDTB0006 - " +
                    "Invalid Table Name - Must be at least 1 character in length. " +
                    "Caller=(DBTable (table_name))";
        }
    }

    /**
     * Instantiates a new Db table.
     *
     * @param table_name   the table name
     * @param table_column the table column
     */
//==============================================================================================
    // HigherIntermediate DBTable Object Constructor - Table with 1 column
    @SuppressWarnings("unused")
    public DBTable(String table_name, DBColumn table_column) {
        this();
        this.table_name = table_name;
        this.table_columns.add(table_column);
        this.problem_msg = "";
        this.checkDBTableIsUsable("DBTable (table_name, table_column (singular))");
    }

    /**
     * Instantiates a new Db table.
     *
     * @param table_name    the table name
     * @param table_columns the table columns
     */
//==============================================================================================
    // Full DBTable Object Constructor
    public DBTable(String table_name, ArrayList<DBColumn> table_columns) {
        this();
        this.problem_msg = "";
        this.table_name = table_name;
        this.table_columns = table_columns;
        this.checkDBTableIsUsable("DBTable Full Constructor");
    }

    /**
     * Add db column to db table.
     *
     * @param dbcolumn the dbcolumn
     */
//==============================================================================================
    // Add a DBCOlumn Object to the table
    @SuppressWarnings("unused")
    public void AddDBColumnToDBTable(DBColumn dbcolumn) {
        this.table_columns.add(dbcolumn);
        this.problem_msg = "";
        this.checkDBTableIsUsable("AddDBColumnToDBTable");
    }

    /**
     * Add db columnsto db table.
     *
     * @param dbcolumns the dbcolumns
     */
//==============================================================================================
    // Add a list of DBCOlumn Objects, as held in a DBColumn ArrayList, to the table
    public void AddDBColumnstoDBTable(ArrayList<DBColumn> dbcolumns) {
        this.table_columns.addAll(dbcolumns);
        this.problem_msg = "";
        this.checkDBTableIsUsable("AddDBColumnsToDBtable");
    }

    /**
     * Add multiple columnsto db table.
     *
     * @param dbcolumns the dbcolumns
     */
//==============================================================================================
    // psuedonym for AddDBColumnsToDBTable (easier to differentiate from AddDBColumnToDBTable)
    @SuppressWarnings("unused")
    public void AddMultipleColumnstoDBTable(ArrayList<DBColumn> dbcolumns) {
        this.AddDBColumnstoDBTable(dbcolumns);
    }

    /**
     * Sets db table name.
     *
     * @param table_name the table name
     */
//==============================================================================================
    // Set the name of the table.
    // Note table name will be converetd to lowercase.
    // The table usability will be rechecked.
    @SuppressWarnings("unused")
    public void setDBTableName(String table_name) {
        this.table_name = table_name.toLowerCase();
        this.problem_msg = "";
        this.checkDBTableIsUsable("setDBTableName");
    }

    /**
     * Gets db table name.
     *
     * @return the db table name
     */
// Retrieve the DBTable's name
    //==============================================================================================
    public String getDBTableName() { return this.table_name; }

    /**
     * Is db table usable boolean.
     *
     * @return the boolean
     */
//==============================================================================================
    public boolean isDBTableUsable() { return this.usable; }

    /**
     * Number of columns in table int.
     *
     * @return the int
     */
    @SuppressWarnings("unused")
    public int numberOfColumnsInTable() { return this.table_columns.size(); }

    /**
     * Gets table db columns.
     *
     * @return the table db columns
     */
//Retrieve the DBTable's DBColumn list as and ArrayList of DBCOlumn objects
    //==============================================================================================
    public ArrayList<DBColumn> getTableDBColumns() { return this.table_columns; }

    /**
     * Gets db table problem msg.
     *
     * @return the db table problem msg
     */
// Retrieve the DBTable's Problem Message
    //==============================================================================================
    public String getDBTableProblemMsg() { return this.problem_msg; }

    /**
     * Gets all db table problem msgs.
     *
     * @return the all db table problem msgs
     */
//==============================================================================================
    // Retrieve the DBTable's Problem Message along with all the Problem Messages for the
    // DBColumns in the DBTable
    public String getAllDBTableProblemMsgs() {
        String problem_messages = this.getDBTableProblemMsg();
        for(DBColumn tc : this.table_columns) {
            problem_messages = problem_messages + tc.getDBColumnProblemMsg();
        }
        return  problem_messages;
    }

    /**
     * Check db table is usable boolean.
     *
     * @param caller the caller
     * @return the boolean
     */
//==============================================================================================
    // Check if the DBTable is usable, setting usability state. Includes underlying DBColumns
    @SuppressWarnings("UnusedReturnValue")
    public boolean checkDBTableIsUsable(String caller) {
        this.usable = false;
        if(this.anyEmptyDBColumnsInDBTable(caller)) {
            this.usable = true;
            if (this.table_name.length() < 1) {
                this.problem_msg = this.problem_msg + " EDBT0009 - Inavlid Table Name - " +
                        "Must be at least 1 character in length. " +
                        "Caller=(" + caller + ")";
                this.usable = false;
            }
        }
        return this.usable;
    }

    /**
     * Any empty db columns in db table boolean.
     *
     * @param caller the caller
     * @return the boolean
     */
//==============================================================================================
    // Check to see if the DBtable has 0 DBColumns. If not then checks the DBColumns for usability.
    // Note!! does not reset DBColumn usability, rather this is the DBTable's view of the DBColumns
    // usability.
    public boolean anyEmptyDBColumnsInDBTable(String caller) {
        boolean rc = true;
        if(this.table_columns.isEmpty()) {
            this.problem_msg = this.problem_msg + "EDBT0007 - No Columns - " +
                    "Must have at least 1 Column in the Table. " +
                    "Caller=(" + caller + ")";
            this.usable = false;
            return false;
        }
        for(DBColumn tc : this.table_columns) {
            if(!tc.isDBColumnUsable()) {
                this.problem_msg = this.problem_msg + "EDBT0008 - Column " + tc.getDBColumnName() +
                        " is unusable. Must be usable. " +
                        "Caller=(" + caller + ")";
                rc = false;
            }
        }
        return rc;
    }

    /**
     * Gets sql create string.
     *
     * @param db the db
     * @return the sql create string
     */
//==============================================================================================
    public String getSQLCreateString(SQLiteDatabase db) {

        // Check to see if this table exists, if not then skip CREATE
        String sqlstr_mstr = "SELECT name FROM sqlite_master WHERE type = 'table' AND name!='android_metadata' ORDER by name;";
        Cursor csr_mstr = db.rawQuery(sqlstr_mstr,null);
        boolean table_exists = false;

        while(csr_mstr.moveToNext()) {
            if(this.table_name.equals(csr_mstr.getString(0))) { table_exists = true; }
        }
        // Finished with master
        csr_mstr.close();
        if(table_exists) {
            return "";
        }

        // Extract Columns that are flagged as PRIMARY INDEXES so we have a count
        // More than one has to be handled differently
        ArrayList<String> indexes = new ArrayList<>();
        for(DBColumn dc : this.table_columns) {
            if(dc.getDBColumnIsPrimaryIndex()) {
                indexes.add(dc.getDBColumnName());
            }
        }
        // Build the CREATE SQL
        String part1 = " CREATE  TABLE IF NOT EXISTS " + this.table_name + " (";
        int dccount = 0;
        // Main Loop through the columns
        for(DBColumn dc : this.table_columns) {
            part1 = part1 + dc.getDBColumnName() + " " + dc.getDBColumnType() + " ";
            // Apply the default value if required
            if(dc.getDBColumnDefaultValue().length() > 0 ) {
                part1 = part1 + " DEFAULT " + dc.getDBColumnDefaultValue() + " ";
            }
            // if only 1 PRIMARY INDEX and this is it then add it
            if(dc.getDBColumnIsPrimaryIndex() & indexes.size() == 1) {
                part1 = part1 + " PRIMARY KEY ";
            }
            // If more to do then include comma separator
            dccount++;
            if (dccount < this.table_columns.size()) {
                part1 = part1 + ", ";
            }
        }
        // Handle multiple PRIMARY INDEXES ie add PRIMARY KEY (<col>, <col> .....)
        int ixcount = 1;
        if(indexes.size() > 1 ) {
            part1 = part1 + ", PRIMARY KEY (";
            for(String ix : indexes) {
                part1 = part1 + ix;
                if(ixcount < (indexes.size() ) ) {
                    part1 = part1 + ", ";
                }
                ixcount++;
            }
            part1 = part1 + ")";
        }
        part1 = part1 + ") ;";
        return part1;
    }

    /**
     * Gets sql table create as string.
     *
     * @param doasmysql the doasmysql
     * @return the sql table create as string
     */
//==============================================================================================
    public String getSQLTableCreateAsString(@SuppressWarnings("SameParameterValue") Boolean doasmysql) {

        // Extract Columns that are flagged as PRIMARY INDEXES so we have a count
        // More than one has to be handled differently
        ArrayList<String> indexes = new ArrayList<>();
        for(DBColumn dc : this.table_columns) {
            if(dc.getDBColumnIsPrimaryIndex()) {
                indexes.add(dc.getDBColumnName());
            }
        }
        // Build the CREATE SQL
        String part1 = " CREATE  TABLE IF NOT EXISTS `" + this.table_name + "` (";
        int dccount = 0;
        // Main Loop through the columns
        for(DBColumn dc : this.table_columns) {
            // FOR mysql export need to use BIGINT(20) instead of INTEGER
            if(doasmysql && dc.getDBColumnType().equals("INTEGER")) {
                part1 = part1 + "`" + dc.getDBColumnName() + "` BIGINT(20) NOT NULL ";
            } else {
                part1 = part1 + "`" + dc.getDBColumnName() + "` " + dc.getDBColumnType() + " ";
            }

            // Apply the default value if required
            if(dc.getDBColumnDefaultValue().length() > 0 ) {
                part1 = part1 + " DEFAULT " + dc.getDBColumnDefaultValue() + " ";
            }
            // if only 1 PRIMARY INDEX and this is it then add it
            if(dc.getDBColumnIsPrimaryIndex() & indexes.size() == 1) {
                part1 = part1 + " PRIMARY KEY ";
            }
            // If more to do then include comma separator
            dccount++;
            if (dccount < this.table_columns.size()) {
                part1 = part1 + ", ";
            }
        }
        // Handle multiple PRIMARY INDEXES ie add PRIMARY KEY (<col>, <col> .....)
        int ixcount = 1;
        if(indexes.size() > 1 ) {
            part1 = part1 + ", PRIMARY KEY (";
            for(String ix : indexes) {
                part1 = part1 + ix;
                if(ixcount < (indexes.size() ) ) {
                    part1 = part1 + ", ";
                }
                ixcount++;
            }
            part1 = part1 + ")";
        }
        part1 = part1 + ") ;";
        return part1;
    }

    /**
     * Gets sql alter to add new columns.
     *
     * @param db the db
     * @return the sql alter to add new columns
     */
//==============================================================================================
    public ArrayList<String> getSQLAlterToAddNewColumns(SQLiteDatabase db) {
        // Have to return an array (arraylist) as ALTER statements can only Add 1 column at a time.
        ArrayList<String> result = new ArrayList<>();

        // Prepare to get the current database table information PRAGMA
        String sqlstr = " PRAGMA table_info (" + this.table_name + ")";
        Cursor csr = db.rawQuery(sqlstr, null);

        // Check to see if this table exists, if not then cannot ALTER anything
        // Should never happen if the method actionDBAlterSQL (method that invokes this method)
        // is preceeded by actionDBCreateSQL, as that should create any tables that don't exist.
        String sqlstr_mstr = "SELECT name FROM sqlite_master WHERE type = 'table' AND name!='android_metadata' ORDER by name;";
        Cursor csr_mstr = db.rawQuery(sqlstr_mstr,null);
        boolean table_exists = false;

        while(csr_mstr.moveToNext()) {
            String cmix0 = csr_mstr.getString(0);
            if(this.table_name.equals(csr_mstr.getString(0))) { table_exists = true; }
        }
        csr_mstr.close();
        if(!table_exists) {
            csr.close();
            return result;
        }

        // Loop through all the columns of the potentially new columns
        for(DBColumn dc : this.table_columns) {
            String columntofind = dc.getDBColumnName();
            boolean columnmatch = false;

            csr.moveToPosition(-1);
            while(csr.moveToNext()) {
                if(csr.getString(1).equals(columntofind)) {
                    columnmatch = true;
                }
            }

            if(!columnmatch) {
                String altersql = " ALTER TABLE " + this.table_name + " ADD COLUMN " +
                        dc.getDBColumnName() + " " +
                        dc.getDBColumnType() + " ";
                if(dc.isDBColumnPrimaryIndex()) {
                    altersql = altersql + " PRIMARY INDEX ";
                }
                if(dc.getDBColumnDefaultValue().length() > 0 ) {
                    altersql = altersql + " DEFAULT " + dc.getDBColumnDefaultValue() + " ";
                }
                altersql = altersql + " ; ";
                result.add(altersql);
            }
        }
        csr.close();
        return result;
    }
}
