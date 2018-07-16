package mjt.shopwise;

import android.os.Build;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

/**
 * StoreData is a Class for Storing and retrieving data
 *
 * It specifically uses the devices Download folder/directory located
 * in the device's PRIMARY PUBLIC EXTERNAL STORAGE. This may be
 * internal storage allocated as such.
 *
 * The class is provided primarily for the creation of App backup data.
 * the members of an instantiated object holds data relevant to the
 * creation and use of files.
 *
 * Usage
 * Instantiating (constructing)
 * ============================================================================
 *
 * StoreData <objectname> = new StoreData(<subdirectory>,
 *  <filename>,
 *  <existcheck>(true/false))
 * ----------------------------------------------------------------------------
 *  <subdirectory> is the directory with the Download directory, which will be
 *      created if need be. Note Download directory is as per the device so
 *      it may not necessarily be called Download.
 *  <filename> is as specified.
 *  <existcheck> if true will check if the file exists.
 *      If false it will not. However, it will actually try to create and then
 *      delete the file.
 *      Typically true would be used when reading a file (i.e. it should not be
 *          created/deleted), whilst false would typically be used when writing
 *          a new file.
 *  Other Class methods
 *  ===========================================================================
 *  writeData(<data to write>)
 *  ---------------------------------------------------------------------------
 *      Will write data to the file. <data to write> is a String Arraylist
 *      ex. obj.writeData(mydata) if it is ok to do so, it is suggested to use
 *      obj.isOKandExists() prior to using writeData. However, isOkandexists()
 *      method is called within writeData method.
 *
 *  data = obj.readData()
 *  ---------------------------------------------------------------------------
 *      Will populate the String ArrayList from the file if it is OK to do so.
 *      it is suggested that obj.isOK() is used prior to calling readData
 *      method. However, isOK() method is called within readData method.
 *
 *  flag = obj.isOK()
 *  ---------------------------------------------------------------------------
 *      Returns true if there is no non-zero errorcode and the storage device
 *      is mounted and it is not set as being inerror.
 *      Else it returns false.
 *
 *  flag = obj.isOKandExists()
 *  ---------------------------------------------------------------------------
 *      returns true if OK AND the file actually exists. OK is as per isOK
 *      method.
 *
 *  String = obj.Display
 *  ----------------------------------------------------------------------------
 *      Returns a string listing the objects member values, 1 per line.
 *
 *  String = obj.getErrorMessages()
 *      Returns a string list the current error messages, 1 per line.
 */
@SuppressWarnings({"WeakerAccess", "CanBeFinal"})
class StoreData {

    private String directory; //Note built internally and includes subdirectory
    private String subdirectory;
    private String filename;
    private boolean mounted;
    private boolean inerror;
    private boolean fileexists;
    private boolean direxists;
    private long errorcode;
    private ArrayList<String> errorlist = new ArrayList<>();
    private ArrayList<File> otherfilesindirectory = new ArrayList<>();

    // Need to be aware of the API
    @SuppressWarnings("unused")
    public static final int API_VERSION = Build.VERSION.SDK_INT;
    private static final long UNMOUNTED = 1;
    private static final long FILEIOERR = 2;
    private static final long READERR = 4;
    private static final String NEWLINE = "\r\n";

    /**
     * Sole Constructor for a StoreData object
     * Note instantiating creates but the deletes a file, assuming that
     * no prior errors left the instance in an unusable state (as initially set)
     * Note instantiating, if existcheck (3rd param) is true, does not create
     * and delete the file, rather it checks that the file exists
     *     typically for reading an existing file.
     *
     * @param subdirectory - Sub directory in which to create file
     * @param filename - the file name where actual data will be stored
     * @param existcheck - whether or not to check for the existence of the file
     *
     *  Note!! existcheck, if true, will not try to create the file
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public StoreData(String subdirectory, @SuppressWarnings("SameParameterValue") String filename, boolean existcheck) {
        fileexists = false;
        direxists = false;
        mounted = false;
        inerror = false;
        errorcode = 0;

        this.directory = "";
        this.subdirectory = subdirectory;
        this.filename = filename;

        // External Storage must be mounted.
        if(!(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))) {
            switch (Environment.getExternalStorageState()) {
                case Environment.MEDIA_SHARED : {
                    errorlist.add(
                            "Although External Storage is present." +
                                    " It cannot be used as it's in use via USB." +
                                    "\nDisconnect the USB cable and then try again."
                    );
                    break;
                }
                case Environment.MEDIA_REMOVED : {
                    errorlist.add(
                            "External Storage is not present." +
                                    "\nInsert an SD Card."
                    );
                    break;
                }
                case Environment.MEDIA_EJECTING : {
                    errorlist.add(
                            "External Storage is being ejected." +
                                    "\nRe-insert the SD Card."
                    );
                    break;
                }
                case Environment.MEDIA_NOFS : {
                    errorlist.add(
                            "External Storage is blank or does not have the correct" +
                                    " filesystem present." +
                                    "\nUse a valid SDCard."
                    );
                    break;
                }
                case Environment.MEDIA_BAD_REMOVAL : {
                    errorlist.add(
                            "External Storage was removed incorrectly." +
                                    "\nRe-insert the SD Card, if this fails then" +
                                    " try restarting the device."
                    );
                    break;
                }
                case Environment.MEDIA_CHECKING : {
                    errorlist.add(
                            "External Storage is unavailable as it is being checked." +
                                    "\nTry again."
                    );
                }
                case Environment.MEDIA_MOUNTED_READ_ONLY : {
                    errorlist.add(
                            "External Storage is READ ONLY." +
                                    "\nInsert an SD card that is not protected."
                    );
                }
                case Environment.MEDIA_UNKNOWN : {
                    errorlist.add(
                            "External Storage state is UNKNOWN." +
                                    "\ntry a different SD Card."
                    );
                }
                case Environment.MEDIA_UNMOUNTABLE : {
                    errorlist.add(
                            "External Storage cannot be mounted." +
                                    "\nTry re-inserting the SD Card or using a different SD Card."
                    );
                }
                case Environment.MEDIA_UNMOUNTED : {
                    errorlist.add(
                            "External Storage is not mounted." +
                                    "\nTry re-inserting the SD Card or using a different SD Card."
                    );

                }
                default: {
                    errorlist.add(
                            "Undefined Error"
                    );
                }
            }
            this.errorcode = UNMOUNTED;
            return;
        } else {
            this.mounted = true;
        }

        // Get the required directory and specified sub directory
        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),subdirectory);
        this.directory = dir.getPath();

        // If existcheck is true check that the directories exist
        if (existcheck && dir.exists()) {
            direxists = true;

        }
        // If the directories do not exist try to create them and redo check
        // Note! existcheck is more for file level so always try to create
        // directories
        else {
            //noinspection ResultOfMethodCallIgnored
            dir.mkdirs();
            if(dir.exists()) {
                direxists = true;
            }
        }
        if(direxists) {
            refreshOtherFilesInDirectory();
        }

        // File level
        File f = new File(directory,filename);
        // Check if the file exists if requested and return if it does
        if (existcheck) {
            if (f.exists()) {
                fileexists = true;
            }
            return;
        }

        try {
            f.createNewFile();
        }
        catch (IOException e) {
            e.printStackTrace();
            this.errorcode = FILEIOERR ;
            errorlist.add(
                    "File Error " + e.getMessage()
            );
            return;
        }
        f.delete();
    }

    @SuppressWarnings({"ConstantConditions", "UnusedReturnValue"})
    public boolean refreshOtherFilesInDirectory() {
        boolean rv = true;
        File dir = new File(directory);
        File[] dirlist = dir.listFiles();
        if((dirlist.length) > 0) {
            // Sort the list
            Arrays.sort(dirlist, new Comparator<File>() {
                @Override
                public int compare(File object1, File object2) {
                    return object1.getName().compareTo(object2.getName());
                }
            });
            otherfilesindirectory.clear();
            for (File aDirlist : dirlist) {
                if (!(aDirlist.getName().equals(this.filename))) {
                    otherfilesindirectory.add(aDirlist);
                }
            }
        }
        return rv;
    }

    /**
     * writeData - Write data to the file from String Arraylist passed
     * Note!! a linefeed is added to each string
     * @param datatowrite - strng ArrayList holding data to write
     * @return result flag
     */
    @SuppressWarnings("unused")
    public boolean writeData(ArrayList<String> datatowrite) {
        // Check that this instance is OK
        if (!this.isOK()) {
            this.errorlist.add(
                    "\nError prior to call to writeData method."
            );
            return false;
        }
        // Prepare to write
        this.errorlist.clear();
        File f = new File(this.directory,File.separator + this.filename);
        try {
            boolean x =  f.createNewFile();
            FileOutputStream fos = new FileOutputStream(f);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            for (int i = 0; i < datatowrite.size(); i++) {
                osw.write(datatowrite.get(i) + NEWLINE);
            }
            osw.flush();
            osw.close();
            fos.flush();
            fos.close();
            this.fileexists = true;
        }
        catch (IOException e) {
            e.printStackTrace();
            this.errorcode = FILEIOERR;
            errorlist.add(
                    "File Error " + e.getMessage()
            );
            return false;
        }
        return true;
    }

    /**
     * readData - Populate a String ArrayList from the data in the file
     * Note! Assumes linefeeds in the file separate strings of data
     * @return - result flag
     */
    @SuppressWarnings("unused")
    public ArrayList<String> readData() {
        ArrayList<String> rv = new ArrayList<>();

        if(!this.isOKandExists()) {
            this.errorlist.add(
                    "\nError prior to call to readData method or the file doesn't exist."
            );
            this.errorcode = READERR;
            return rv;
        }

        this.errorlist.clear();
        File f = new File(this.directory,File.separator + this.filename);
        try {
            FileInputStream fis = new FileInputStream(f);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while((line = br.readLine()) != null) {
                rv.add(line);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            this.errorcode = READERR;
            errorlist.add(
                    "File Read Error" + e.getMessage()
            );
            return rv;
        }
        return rv;
    }

    /**
     * isOK - Check if object is usable
     * @return true if OK else false
     */
    public boolean isOK() {
        return !(errorcode != 0 || !mounted || inerror);
    }

    /**
     * exists = Check if the file exists
     * @return - Result of check
     */
    @SuppressWarnings("unused")
    public boolean exists() {
        return this.fileexists;
    }

    public boolean isOKandExists() {
        return this.isOK() && this.fileexists;
    }

    /**
     * Return a string displaying the instances details
     * @return string displaying object's members
     */
    public String Display() {
        String rv;

        rv = "Directory path=" + directory + "\n" +
                "SubDirectory=" + subdirectory + "\n" +
                "Filename=" + filename + "\n" +
                "Mounted =" + Boolean.toString(mounted) + "\n" +
                "Directory Exists=" + Boolean.toString(this.direxists) + "\n" +
                "File Exists=" + Boolean.toString(this.fileexists) + "\n" +
                "In Error=" + Boolean.toString(inerror) + "\n" +
                "Last Error Code=" + Long.toString(errorcode);
        return rv;
    }

    @SuppressWarnings("unused")
    public String DisplayWithOtherFiles() {
        String rv;
        rv = this.Display() + "\nOther Files in Directory (" + this.directory + ") ";

        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < otherfilesindirectory.size(); i++) {
            sb.append("\n\t").append(otherfilesindirectory.get(i).getName());
        }
        return sb.toString();
    }

    /**
     * Retrieve generated error messages. if any
     * @return sting comprised of all error messages generated
     */
    @SuppressWarnings("unused")
    public String getErrorMessages() {
        String rv = "";
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < errorlist.size(); i++) {
            sb.append(errorlist.get(i));
        }
        return sb.toString();
    }

    /**
     * Method: getDirectory - get the backup directory as a String
     * @return Directory as a String
     */
    public String getDirectory() {
        return this.directory;
    }

    /**
     * Method: getFilename - get the filename of the object as a String
     * @return Filename as a String
     */
    @SuppressWarnings("unused")
    public String getFilename() {
        return this.filename;
    }

    /**
     * Method: getSubDirectory - get the sub-directory as a string
     * @return Sub-Directory as a String
     */
    @SuppressWarnings("unused")
    public String getSubDirectory() {
        return this.subdirectory;
    }

    /**
     * Method: getFilesInDirectory - return an ArrayList of type File
     * @return List of files in the directory as an ArrayList<File>
     */
    public ArrayList<File> getFilesInDirectory() {
        return this.otherfilesindirectory;
    }
}
