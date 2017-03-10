package mjt.shopwise;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Mike092015 on 14/01/2017.
 */

@SuppressWarnings({"FieldCanBeLocal", "WeakerAccess", "CanBeFinal", "unused"})
public class BackupActivity extends AppCompatActivity {

    @SuppressWarnings("unused")
    private static final String THIS_ACTIVITY = "BackupActivity";
    @SuppressWarnings("unused")
    private static String caller;
    @SuppressWarnings("unused")
    private static int calledmode;

    Context context;
    ActionBar actionbar;

    /**
     * Colours
     */
    @SuppressWarnings("unused")
    private static int h1;
    private static int h2;
    @SuppressWarnings("unused")
    private static int h3;
    private static int h4;
    private static int primary_color;
    private String menucolorcode;
    @SuppressWarnings("unused")
    int passedmenucolorcode;


    TextView messagebar;
    TextView donebutton;
    TextView directory;
    TextView directorylabel;
    EditText backupdatetimepart;
    TextView backupdatetimepartlabel;
    TextView backupresetbutton;
    EditText backupbasepart;
    TextView backupbasepartlabel;
    EditText backupextension;
    TextView backupextensionlabel;
    TextView backupfullfilename;
    TextView backupfullfilenamelabel;
    TextView backupbutton;
    TextView availablebackups;
    TextView availablebackupslabel;
    Spinner selectrestorefile;
    TextView selectrestorefilelabel;
    TextView restorebutton;

    private ProgressDialog busy;

    private int resumestate = StandardAppConstants.RESUMSTATE_NORMAL;
    @SuppressWarnings("unused")
    private Activity thisactivity;

    private StoreData sdbase;
    private ArrayList<String> errlist = new ArrayList<>();
    private boolean confirmaction = false;
    @SuppressWarnings("unused")
    private boolean refeshspinners = false;
    private File dbfile;
    private String backupfilename = "";
    private boolean strictbackupmode = true;

    private String currentdbfilename;
    private String copydbfilename;
    private String icdbfilename;
    private int copylength = 0;
    private static final int BUFFERSZ = 32768;
    private byte[] buffer = new byte[BUFFERSZ];
    @SuppressWarnings("unused")
    private String logtag;
    private String resulttitle;
    private boolean copytaken;
    private boolean origdeleted;
    private boolean restoredone;
    @SuppressWarnings("unused")
    private boolean rolledback;
    private String finalmessage = "";

    public static final String THISCLASS = BackupActivity.class.getSimpleName();
    private static final String LOGTAG = "SW_BA";

    protected void onCreate(Bundle savedInstanceState) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);
        context = this;
        thisactivity = (Activity)context;
        caller = getIntent().getStringExtra(
                StandardAppConstants.INTENTKEY_CALLINGACTIVITY);
        calledmode = getIntent().getIntExtra(
                StandardAppConstants.INTENTKEY_CALLINGMODE,0);
        menucolorcode = StandardAppConstants.INTENTKEY_MENUCOLORCODE;
        passedmenucolorcode = getIntent().getIntExtra(menucolorcode,0);

        messagebar = (TextView) findViewById(R.id.backup_messagebar);
        donebutton = (TextView) findViewById(R.id.backup_donebutton);
        directory = (TextView) findViewById(R.id.backup_directory);
        directorylabel = (TextView) findViewById(R.id.backup_directorylabel);
        backupdatetimepart = (EditText) findViewById(R.id.backup_datetimepart);
        backupdatetimepartlabel = (TextView) findViewById(R.id.backup_datetimepartlabel);
        backupresetbutton = (TextView) findViewById(R.id.backup_resetbutton);
        backupbasepart = (EditText) findViewById(R.id.backup_basepart);
        backupbasepartlabel = (TextView) findViewById(R.id.backup_basepartlabel);
        backupextension = (EditText) findViewById(R.id.backup_extension);
        backupextensionlabel = (TextView) findViewById(R.id.backup_extensionlabel);
        backupfullfilename = (TextView) findViewById(R.id.backup_fullfilename);
        backupfullfilenamelabel = (TextView) findViewById(R.id.backup_fullfilenamelabel);
        backupbutton = (TextView) findViewById(R.id.backup_backupbutton);
        availablebackups = (TextView) findViewById(R.id.backup_availablebackups);
        availablebackupslabel = (TextView) findViewById(R.id.backup_availablebackupslabel);
        selectrestorefile = (Spinner) findViewById(R.id.selectrestorefile);
        selectrestorefilelabel = (TextView) findViewById(R.id.selectrestorefilelabel);
        restorebutton = (TextView) findViewById(R.id.backup_restorebutton);

        // Prepare Progress Dialog
        busy = new ProgressDialog(this);
        busy.setTitle(getResources().getString(R.string.backupbusydialogtitle));
        busy.setCancelable(true);

        // Note filename xxx is just for ascertaining access
        sdbase = new StoreData(getResources().getString(R.string.backupdirectoryname),
                "xxx",
                false);

        /**
         * Apply Color Coding
         */
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,
                "Preparing Colour Coding", this, methodname);
        actionbar = getSupportActionBar();
        ActionColorCoding.setActionBarColor(this,getIntent(),actionbar);
        primary_color = ActionColorCoding.setHeadingColor(this,getIntent(),0);
        h1 = ActionColorCoding.setHeadingColor(this,getIntent(),1);
        h2 = ActionColorCoding.setHeadingColor(this,getIntent(),2);
        h3 = ActionColorCoding.setHeadingColor(this,getIntent(),3);
        h4 = ActionColorCoding.setHeadingColor(this,getIntent(),4);
        ActionColorCoding.setActionButtonColor(donebutton,primary_color);
        ActionColorCoding.setActionButtonColor(backupdatetimepart,
                h2 & ActionColorCoding.transparency_requied);
        ActionColorCoding.setActionButtonColor(backupresetbutton,primary_color);
        ActionColorCoding.setActionButtonColor(backupbasepart,
                h2 & ActionColorCoding.transparency_requied);
        ActionColorCoding.setActionButtonColor(backupextension,
                h2 & ActionColorCoding.transparency_requied);
        backupfullfilename.setTextColor(Color.BLUE);
        ActionColorCoding.setActionButtonColor(backupbutton,primary_color);
        ActionColorCoding.setActionButtonColor(selectrestorefile,h4);
        ActionColorCoding.setActionButtonColor(restorebutton,primary_color);

        ActionColorCoding.setSwatches(findViewById(android.R.id.content),this.getIntent());

        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,
                "Determining Directory/File names", this, methodname);
        directory.setText(sdbase.getDirectory());
        directory.setTextColor(Color.BLUE);
        directorylabel.setTextColor(primary_color);
        backupdatetimepart.setText(getDateandTimeasYYMMDDhhmm());
        backupdatetimepartlabel.setTextColor(primary_color);
        backupbasepart.setText(getResources().getString(R.string.backupbasefilename));
        backupbasepartlabel.setTextColor(primary_color);
        backupextension.setText(getResources().getString(R.string.backupfileextension));
        backupextensionlabel.setTextColor(primary_color);
        setFullFilename();
        availablebackups.setTextColor(Color.BLUE);
        availablebackupslabel.setTextColor(primary_color);
        backupfullfilenamelabel.setTextColor(primary_color);
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,
                "Populating Spinners", this, methodname);
        populateAllSpinners();
        selectrestorefilelabel.setTextColor(primary_color);
        setEditTextTextChangedListener(backupbasepart);
        setEditTextTextChangedListener(backupdatetimepart);
        setEditTextTextChangedListener(backupextension);
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
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,"Invoked",this,methodname);
        super.onResume();
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
    }
    /**************************************************************************
     * onDestroy - do any clean up before th application is destroyed
     * e.g. close any open cursors and then close the database
     */
    @Override
    protected void onDestroy() {
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,"Invoked",this,methodname);
        super.onDestroy();
    }

    /**************************************************************************
     * Button Click Handler
     *
     * @param view The view (i.e the TextView that was clicked)
     */
    @SuppressWarnings("unused")
    public void actionButtonClick(View view) {
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,"Invoked",this,methodname);
        switch (view.getId()) {
            case R.id.backup_donebutton:
                this.finish();
                break;
            case R.id.backup_resetbutton:
                backupdatetimepart.setText(getDateandTimeasYYMMDDhhmm());
                setMessage(this,"Date/Time Part reset to current date/time.",false);
                setFullFilename();
                populateAllSpinners();
                break;
            case R.id.backup_backupbutton:
                saveDB();
                populateAllSpinners();
                refeshspinners = true;
                break;
            case  R.id.backup_restorebutton:
                restoreDB();
                populateAllSpinners();
                break;
            default:
                break;
        }
    }

    /**
     * Method setEdittextTextChangedListener - Generic Listener
     *      update the filenames when editable fields that affect the
     *      filename are changed.
     * @param et - Edittext to listen to
     */
    private void setEditTextTextChangedListener(EditText et) {
        final String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,"Invoked",this,methodname);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setFullFilename();
                LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,BackupActivity.LOGTAG,
                        "Populating Spinners After Text Change",
                        BackupActivity.class.getSimpleName(),methodname);
                populateAllSpinners();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    /**************************************************************************
     * method saveDB save a file copy of the Database
     */
    private void saveDB() {
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,"Invoked",this,methodname);
        busy.show();
        errlist.clear();
        confirmaction = true;
        String dbfilename = this.getDatabasePath(
                DBConstants.DATABASE_NAME).getPath();
        dbfile = new File(dbfilename);
        backupfilename = directory.getText().toString() +
                "/" +
                backupfullfilename.getText().toString();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,
                "Invoking Runnable/New Thread",
                this,methodname);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    FileInputStream fis = new FileInputStream(dbfile);
                    OutputStream backup = new FileOutputStream(backupfilename);
                    String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
                    LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                            BackupActivity.LOGTAG,
                            "Copying from=" + dbfile.getCanonicalFile() +
                            " to=" + backupfilename,
                            BackupActivity.THISCLASS,methodname);

                    //byte[] buffer = new byte[32768];
                    int length;
                    while((length = fis.read(buffer)) > 0) {
                        backup.write(buffer, 0, length);
                    }
                    backup.flush();
                    backup.close();
                    fis.close();
                    LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                           BackupActivity.LOGTAG,"Copy Completed.",
                            BackupActivity.THISCLASS,methodname);
                }
                catch (IOException e) {
                    String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
                    LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,
                            BackupActivity.LOGTAG,"IO Error Copying File Msg=" + e.getMessage(),
                            BackupActivity.THISCLASS,methodname);
                    e.printStackTrace();
                    errlist.add("Database backup failed with an IO Error. Error Message was " +
                            e.getMessage() +
                            "/n/tFile Name was " +
                            backupfilename);
                    confirmaction = false;
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        busy.dismiss();
                        AlertDialog.Builder dbbackupresult = new AlertDialog.Builder(context);
                        dbbackupresult.setCancelable(true);
                        if(confirmaction) {
                            dbbackupresult.setTitle("DB Data Backed up OK.");
                            dbbackupresult.setMessage("DB Data successfully saved in file \n\t" +
                                    backupfilename );
                        } else {
                            dbbackupresult.setTitle("DB Backup Failed.");
                            String emsg = "";
                            for(int i = 0; i < errlist.size(); i++) {
                                emsg = emsg + errlist.get(i);
                            }
                        }
                        dbbackupresult.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
                    }
                });
            }
        }).start();
    }

    /**
     * method restoreDB - Prepare for and confirm database restore
     */
    private void restoreDB() {
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,"Invoked",this,methodname);

        //Prepare filenames
        //      currentdbfilename = current Shopper DataBase
        //      copydbfilename = filename used when renaming current
        //      icdbfilename = filename of an intermediate/test database used
        //                      to perform an integrity check to see if the
        //                      restore file creates a valid database.
        currentdbfilename = this.getDatabasePath(
                DBConstants.DATABASE_NAME)
                .getPath();
        copydbfilename = currentdbfilename +
                "OLD" +
                getDateandTimeasYYMMDDhhmm();
        icdbfilename = currentdbfilename.substring(0,
                currentdbfilename.lastIndexOf(DBConstants.DATABASE_NAME))
                + "IC" + DBConstants.DATABASE_NAME;

        // Get the backup file, checking that there is one (should always be one
        // as the restore button should be hidden if there are no files
        if(selectrestorefile.getSelectedItemPosition() == AdapterView.INVALID_POSITION) {
            AlertDialog.Builder notokdialog = new AlertDialog.Builder(this);
            notokdialog.setTitle("No DB Restore File.");
            notokdialog.setMessage("There is no file to restore from selected." +
                    "\n\nThe restore request cannot be undertaken" +
                    " and will be cancelled. "
            );
            notokdialog.setCancelable(true);
            notokdialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            }).show();
            return;
        }
        backupfilename = selectrestorefile.getSelectedItem().toString();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,
                "File names Prepared." +
                "\n\tCurrent Database File=" + currentdbfilename +
                "\n\tWorking Copy File    =" + copydbfilename +
                "\n\trestore from File    =" + icdbfilename,
                this,methodname);

        // Confirm restore request is wanted and if so invoke it BUT only
        // if the itegrity check is passed
        // if integrity check is passed the invoke doDBRestore method which does the restore
        AlertDialog.Builder okdialog = new AlertDialog.Builder(this);
        okdialog.setTitle("Database Restore Requested");
        okdialog.setMessage(
                "A database restore has been requested." +
                        "\n\nThe request will use the following file to recover from:" +
                        "\n\t" + backupfilename + " ." +
                        "\n\nIf the standard provided filenames are used then the database will be" +
                        " recovered to the date/time as per the file." +
                        "\n\nAs part of the restore process, a copy of the current database will " +
                        "be created. This will be named:" +
                        "\n\t" + copydbfilename +
                        "\n\n Should the restore fail after the original database has been deleted, " +
                        " the original database will be restored from this copy." +
                        "\n\n" +
                        "Do you wish to continue with the database restore?"

        );
        okdialog.setCancelable(true);
        okdialog.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(dataBaseIntegrityCheck()) {
                    doDBRestore();
                }
            }
        });
        okdialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                confirmaction = false;
            }
        });
        okdialog.show();
    }

    /**************************************************************************
     * method dorestore - Restore Database in 3 stages
     *      1) make a copy of the databasefile
     *      2) delete the database
     *      3) create the database populating by copying from the designated backup
     *      If an IOexception occurs and the database has been deleted revert to the
     *      copy
     */
    private void doDBRestore() {
        final String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,"Invoked",this,methodname);

        confirmaction = true;
        logtag = "DB RESTORE";
        //ArrayList<String> errorlist = new ArrayList<>();
        resulttitle = "Restore Failed.";
        errlist.clear();
        dbfile = new File(currentdbfilename);
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,"In Progress set and dispalyed",this,methodname);
        busy.show();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,"New Thread Started",this,methodname);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Stage 1 Create a copy of the database
                    String msg = "Stage 1 (make Copy of current DB)Starting";
                    LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
                    FileInputStream fis = new FileInputStream(dbfile);
                    OutputStream backup = new FileOutputStream(copydbfilename);
                    while ((copylength = fis.read(buffer)) > 0) {
                        backup.write(buffer, 0, copylength);
                    }
                    backup.flush();
                    backup.close();
                    fis.close();
                    msg = "Stage 1 - Complete. Copy made of current DB.";
                    LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
                    copytaken = true;

                    // Stage 2 - Delete the database file
                    if (dbfile.delete()) {
                        msg = "Stage 2 - Completed. Original DB deleted.";
                        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
                        origdeleted = true;
                    }

                    // Stage 3 copy from the backup to the deleted database file i.e. create it
                    msg = "Stage 3 - (Create new DB from backup) Starting.";
                    LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
                    FileInputStream bkp = new FileInputStream(backupfilename);
                    OutputStream restore = new FileOutputStream(currentdbfilename);
                    copylength = 0;
                    while ((copylength = bkp.read(buffer)) > 0) {
                        restore.write(buffer, 0, copylength);
                    }
                    msg = "Stage 3 - Data Written";
                    LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
                    restore.flush();
                    restore.close();
                    msg = "Stage 3 - New DB file flushed and closed";
                    LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
                    restoredone = true;
                    bkp.close();
                    msg = "Stage 3 - Complete.";
                    LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
                } catch (IOException e) {
                    e.printStackTrace();
                    if(!copytaken) {
                        errlist.add("Restore failed copying current database. Error was " + e.getMessage());
                    } else {
                        if(!origdeleted) {
                            errlist.add("Restore failed to delete current database. Error was " + e.getMessage());
                        }
                        else {
                            if(!restoredone) {
                                errlist.add("Restore failed to recreate the database from the backup. Error was "+ e.getMessage());
                                errlist.add("Restore will attempt to revert to the original database.");
                            }
                        }
                    }
                }
                // Ouch restore not done but DB deleted so recover from
                // copy by renaming copy
                if (copytaken && origdeleted && !restoredone) {

                    String msg = "Restore failed. Recovering DB after failed restore from backup";
                    LogMsg.LogMsg(LogMsg.LOGTYPE_ERROR,LOGTAG,msg,THISCLASS,methodname);
                    File rcvdbfile = new File(copydbfilename);
                    rcvdbfile.renameTo(dbfile);

                    msg = "Restore failed. DB Recovered from backup now in original state.";
                    LogMsg.LogMsg(LogMsg.LOGTYPE_ERROR,LOGTAG,msg,THISCLASS,methodname);
                    rolledback = true;
                    errlist.add("Database reverted to original.");
                }
                if (copytaken && !origdeleted) {
                    String msg = "Restore failed. Original DB not deleted so original\" +\n" +
                            "                            \" is being used.";
                    LogMsg.LogMsg(LogMsg.LOGTYPE_ERROR,LOGTAG,msg,THISCLASS,methodname);
                }
                if(!copytaken) {
                    String msg = "Restore failed. Attempt to Copy original DB failed.\" +\n" +
                            "                            \" Original DB is being used.";
                    LogMsg.LogMsg(LogMsg.LOGTYPE_ERROR,LOGTAG,msg,THISCLASS,methodname);
            }
                if(copytaken && origdeleted && restoredone) {
                    errlist.add("Database successfully restored.");
                    resulttitle = "Restore was successful. Application wil be restarted.";
                    DBHelper.reopen(context);
                    DBHelper.getHelper(context).expand(null,true);
                }
                for(int i = 0; i < errlist.size(); i++){
                    if(i > 0) {
                        finalmessage = finalmessage + "\n\n";
                    }
                    finalmessage = finalmessage + errlist.get(i);
                }


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        busy.dismiss();
                        AlertDialog.Builder resultdialog = new AlertDialog.Builder(context);
                        resultdialog.setTitle(resulttitle);
                        resultdialog.setMessage(finalmessage);
                        resultdialog.setCancelable(true);
                        resultdialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (copytaken && origdeleted && restoredone) {
                                    Intent i = getBaseContext().getPackageManager()
                                            .getLaunchIntentForPackage( getBaseContext().getPackageName() );
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    finish();
                                    startActivity(i);
                                    System.exit(0);
                                }
                            }
                        });
                        resultdialog.show();
                    }
                });
            }
        }).start();
    }

    /**************************************************************************
     *
     * @return false if the backup file is invalid.
     *
     *  determine by creating a differently name database (prefixed with IC),
     *  openeing it with it's own helper (does nothing) and then trying to
     *  check if there are tables in the database.
     *  No tables reflects that file is invalid type.
     *
     *  Note! if an attempt to open an invalid database file then SQLite deletes the file.
     */
    private boolean dataBaseIntegrityCheck() {
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,"Invoked",this,methodname);

        final String THIS_METHOD = "dataBaseIntegrityCheck";
        //String sqlstr_mstr = "SELECT name FROM sqlite_master WHERE type = 'table' AND name!='android_metadata' ORDER by name;";
        Cursor iccsr;
        boolean rv = true;

        //Note no use having the handler as it actualy introduces problems  as SQLite assumes that
        // the handler will restore the database.
        // No need to comment out as handler can be disabled by not not passing it as a parameter
        // of the DBHelper
        DatabaseErrorHandler myerrorhandler = new DatabaseErrorHandler() {
            @Override
            public void onCorruption(SQLiteDatabase sqLiteDatabase) {
            }
        };
        try {
            FileInputStream bkp = new FileInputStream(backupfilename);
            OutputStream ic = new FileOutputStream(icdbfilename);
            while ((copylength = bkp.read(buffer)) > 0) {
                ic.write(buffer, 0, copylength);
            }
            ic.close();
            bkp.close();


            //Note SQLite will actually check for corruption and if so delete the file
            //
            IntegrityCheckDBHelper icdbh = new IntegrityCheckDBHelper(this,null,null,1,null);
            SQLiteDatabase icdb = icdbh.getReadableDatabase();
            iccsr = icdb.query("sqlite_master",
                    new String[]{"name"},
                    "type=? AND name!=?",
                    new String[] {"table", "android_metadata"},
                    null, null,
                    "name"
            );

            //Check to see if there are any tables, if wrong file type shouldn't be any
            //iccsr = icdb.rawQuery(sqlstr_mstr,null);
            if(iccsr.getCount() < 1) {
                errlist.add("Integrity Check extract from sqlite_master returned nothing - Propsoed file is corrupt or not a database file.");
                rv = false;
            }
            iccsr.close();
            icdb.close();

        } catch (IOException e) {
            e.printStackTrace();
            errlist.add("Integrity Check Failed Error Message was " + e.getMessage());
        }

        if(!rv) {
            AlertDialog.Builder notokdialog = new AlertDialog.Builder(this);
            notokdialog.setTitle("Invalid Restore File.");
            notokdialog.setCancelable(true);
            String msg = "File " + backupfilename + " is an invalid file." +
                    "\n\nThe Restore cannot continue and will be canclled. " +
                    "\n\nPlease Use a Valid Database Backup File!";
            notokdialog.setMessage(msg);
            notokdialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            }).show();
        }
        return rv;
    }


    /**************************************************************************
     *
     */
    private void populateAllSpinners() {
        populateRestoreSpinner(selectrestorefile,
                availablebackups,
                backupbasepart.getText().toString(),
                backupextension.getText().toString(),
                sdbase,
                restorebutton
                );
    }

    /**************************************************************************
     * method populateRestoreSpinner - Populate a restore spinner
     *      with the appropriate files for the respective restore
     * @param spn           Spinner
     * @param basefilename  basefilename
     * @param fileext       fileextension
     * @param sd            StoreData object
     */
    @SuppressLint("SetTextI18n")
    @SuppressWarnings("ParameterCanBeLocal")
    private void populateRestoreSpinner(Spinner spn,
                                        TextView tv,
                                        String basefilename,
                                        String fileext,
                                        StoreData sd,
                                        TextView restorebutton) {
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,"Invoked",this,methodname);
        String spnname = "";
        boolean used = false;
        int fcount = 0;
        ArrayList<File> reverseflist = new ArrayList<>();

        //
        sd = new StoreData(getResources().getString(R.string.backupdirectoryname),"xxx",true);
        sd.refreshOtherFilesInDirectory();

        // Build the File ArrayList
        ArrayList<File> flst = new ArrayList<>(sd.getFilesInDirectory());
        if(flst.size() < 1) {
            //Toast.makeText(this,"No Saved Files Found",Toast.LENGTH_SHORT).show();
        }

        // Ascertain the relevant files that are needed for the restore backup
        // file selector
        for(int i = 0; i < flst.size(); i++) {
            used = false;
            boolean endingok = flst.get(i).getName().endsWith(fileext);
            boolean containsok = flst.get(i).getName().contains(basefilename);
            if((strictbackupmode && endingok && containsok)
                    || (!strictbackupmode && (endingok || containsok))) {
                used = true;
                fcount++;
            } else {
                flst.remove(i);
                i--;
            }
        }

        // Reverse the order of the list so most recent backups appear first
        // Also hide/show the Restore button and spinner according to if
        // files exist or not
        // (doing nothing in the case where the is no restore button i.e.
        //  null has been passed)
        if(flst.size() > 0) {
            for (int i = (flst.size() -1); i >= 0; i--) {
                reverseflist.add(flst.get(i));
            }
            if (restorebutton != null) {
                spn.setVisibility(View.VISIBLE);
                restorebutton.setVisibility(View.VISIBLE);
            }
        } else {
            if (restorebutton != null) {
                spn.setVisibility(View.INVISIBLE);
                restorebutton.setVisibility(View.INVISIBLE);
            }
        }

        // Set the available count for display
        //String bcnt = "Available Backups=" + Integer.toString(reverseflist.size());
        tv.setText(Integer.toString(reverseflist.size()));

        // Set the spinner adapter and dropdown layout and then set the
        // spinner's adapter
        AdapterFileList afl = new AdapterFileList(this,
                R.layout.filelist,
                reverseflist,
                getIntent());
        afl.setDropDownViewResource(R.layout.filelist);
        spn.setAdapter(afl);
    }

    /**
     * return the date and time as a string in yyyymmddhhmm format
     * @return String rv as formatted date and time
     */
    private String getDateandTimeasYYMMDDhhmm() {
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,"Invoked",this,methodname);
        Calendar cldr = Calendar.getInstance();
        String rv = "";
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        rv = "_" + sdf.format(cldr.getTime());
        return rv;
    }

    /**************************************************************************
     *
     */
    private void setFullFilename() {
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,"Invoked",this,methodname);
        backupfullfilename.setText(
                backupbasepart.getText().toString() +
                backupdatetimepart.getText().toString() +
                backupextension.getText().toString()
        );
    }

    /**************************************************************************
     * setMessage - set the Message regarding the lat action performed
     * Note as an action may have altered the database getDBcounts
     * is called and the title changed
     *
     * @param ba   the sa
     * @param msg  The message to be displayed.
     * @param flag Message imnportant, if true Yellow text, esle green
     */
    public void setMessage(BackupActivity ba,
                           @SuppressWarnings("SameParameterValue") String msg,
                           @SuppressWarnings("SameParameterValue") boolean flag) {
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,"Invoked",this,methodname);

        TextView messagebar = (TextView) ba.findViewById(R.id.backup_messagebar);
        messagebar.setText(context.getResources().getString(
                R.string.messagebar_prefix_lastaction) + " " + msg);
        if (flag) {
            messagebar.setTextColor(Color.YELLOW);
        } else {
            messagebar.setTextColor(Color.GREEN);
        }
        messagebar.setVisibility(View.VISIBLE);
        ba.actionbar.setTitle(getResources().getString(R.string.shopslabel));
    }

}
