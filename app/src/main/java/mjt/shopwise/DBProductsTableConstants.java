package mjt.shopwise;

import java.util.ArrayList;
import java.util.Arrays;

import mjt.dbcolumn.DBColumn;
import mjt.dbtable.DBTable;

import static mjt.sqlwords.SQLKWORD.*;

/**
 * DBProductsTableConstants - Constants for the Products Table
 */
@SuppressWarnings("WeakerAccess")
public class DBProductsTableConstants {
    /**************************************************************************
     * PRODUCTS TABLE
     * _ID(INT) Key/Primary Index
     */
    public static final String PRODUCTS_TABLE = "products";
    public static final String PRODUCTS_ID_COL = SQLSTD_ID;
    public static final String PRODUCTS_ID_COL_FULL = PRODUCTS_TABLE +
            SQLPERIOD +
            PRODUCTS_ID_COL;
    public static final String PRODUCTS_ALTID_COL = PRODUCTS_TABLE +
            PRODUCTS_ID_COL;
    @SuppressWarnings("unused")
    public static final String PRODUCTS_ALTID_COL_FULL = PRODUCTS_TABLE + SQLPERIOD + PRODUCTS_ALTID_COL;
    public static final DBColumn PRODUCTSIDCOL = new DBColumn(true);


    public static final String PRODUCTS_NAME_COL = "productname";
    public static final String PRODUCTS_NAME_COL_FULL = PRODUCTS_TABLE +
            SQLPERIOD +
            PRODUCTS_NAME_COL;
    public static final String PRODUCTS_NAME_TYPE = SQLTEXT;
    public static final boolean PRODUCTS_NAME_PRIMARY_INDEX = false;
    public static final DBColumn PRODUCTSNAMECOL = new DBColumn(PRODUCTS_NAME_COL,
            PRODUCTS_NAME_TYPE,
            PRODUCTS_NAME_PRIMARY_INDEX,
            ""
    );


    public static final String PRODUCTS_NOTES_COL = "productnotes";
    public static final String PRODUCTS_NOTES_COL_FULL = PRODUCTS_TABLE +
            SQLPERIOD +
            PRODUCTS_NOTES_COL;
    public static final String PRODUCTS_NOTES_TYPE = SQLTEXT;
    public static final boolean PRODUCTS_NOTES_PRIMARY_INDEX = false;
    public static final DBColumn PRODUCTSNOTESCOL = new DBColumn(PRODUCTS_NOTES_COL,
            PRODUCTS_NOTES_TYPE,
            PRODUCTS_NOTES_PRIMARY_INDEX,
            ""
    );


    public static final String PRODUCTS_STORAGEREF_COL = "productstorageref";
    public static final String PRODUCTS_STORAGEREF_COL_FULL = PRODUCTS_TABLE +
            SQLPERIOD +
            PRODUCTS_STORAGEREF_COL;
    public static final String PRODUCTS_STORAGEREF_TYPE = SQLINTEGER;
    public static final boolean PRODUCTS_STORAGEREF_PRIMARY_INDEX = false;
    public static final DBColumn PRODUCTSSTORAGEREFCOL = new DBColumn(
            PRODUCTS_STORAGEREF_COL,
            PRODUCTS_STORAGEREF_TYPE,
            PRODUCTS_STORAGEREF_PRIMARY_INDEX,
            "1"
    );


    public static final String PRODUCTS_STORAGEORDER_COL = "productstorageorder";
    public static final String PRODUCTS_STORAGEORDER_COL_FULL = PRODUCTS_TABLE +
            SQLPERIOD +
            PRODUCTS_STORAGEORDER_COL;
    public static final String PRODUCTS_STORAGEORDER_TYPE = SQLINTEGER;
    public static final boolean PRODUCTS_STORAGEORDER_PRIMARY_INDEX = false;
    public static final DBColumn PRODUCTSTORAGEORDERCOL = new DBColumn(
            PRODUCTS_STORAGEORDER_COL,
            PRODUCTS_STORAGEORDER_TYPE,
            PRODUCTS_STORAGEORDER_PRIMARY_INDEX,
            "1000"
    );


    public static final ArrayList<DBColumn> PRODUCTSCOLS = new ArrayList<>(Arrays.asList(PRODUCTSIDCOL,
            PRODUCTSNAMECOL,
            PRODUCTSNOTESCOL,
            PRODUCTSSTORAGEREFCOL,
            PRODUCTSTORAGEORDERCOL
    ));
    public static final DBTable PRODUCTSTABLE = new DBTable(PRODUCTS_TABLE,
            PRODUCTSCOLS);
    public static final String PRODUCTSSMAXORDERCOLUMN = "maxorder";
}
