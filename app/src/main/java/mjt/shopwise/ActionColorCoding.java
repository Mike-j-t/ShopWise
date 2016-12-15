package mjt.shopwise;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

/**
 * ActionColorCoding - Methods to Apply Color Coding of to Activities
 * NOTE! Singleton
 * <p>
 * Color Coding is coding views/elements such as the action bar.
 * <p>
 * Colors by default are in groups each based upon a primary color with the
 * subsequent colors shades of that color.
 * <p>
 * The number of primary colors is flexible, thus it is possible to add or
 * remove a color group. the number of shades per primary color is flexible
 * to an extent. That is the number of shades per primary color, overall,
 * may be changed but all primary colors must have the same number of shades.
 * <p>
 * The colors are stored in two int arrays, primarycolors and all colors. The
 * former consisting solely of the primary colors, whilst allcolors contains
 * all the shades as well as the primary colors. The order/sequence of the
 * colors should be consistent between the two arrays, which are limited to
 * just storing the actual color.
 * <p>
 * More specifically assuming 5 primary colors with 4 shades per primary color.
 * i.e. 5 colors in total per a color and thus 25 (5 * 5) colors in total.
 * The first entry in the primarycolors array (offset 0) is related to the
 * first 5 * colors (offsets 0-4) in the allcolors array.
 * The second primarycolor (offset 1) is related to the second 5 colors
 * (offsets 5-9) and so on.
 * <p>
 * To cater for user customisation the colors are stored in the Appvalues table;
 * these being used if they exist. If they don't exist then they are created
 * according to the default values and then stored in the Appvalues table
 * this is done when constructing an ActionColorCoding instance.
 */
class ActionColorCoding {

    private static ActionColorCoding instance = null;
    private static final String  COLORCODENAME = "COLORCODES";
    private static final String PRIMARYCOLORCODENAME = "PRIMARYCOLORCODES";
    private static int colorspergroup;
    private static int[] primarycolors;
    private static int[] allcolors;
    private static boolean colorset = false;
    private static boolean colorsloaded = false;
    /**
     * The constant transparency_requied.
     */
    public static final int transparency_requied = 0x44ffffff;
    public static final int transparency_optional = 0x22ffffff;
    public static final int transparency_evenrow = 0x3fffffff;
    public static final int transparency_oddrow = 0x1fffffff;


    private ActionColorCoding(Context context) {
        loadColors(context);
        if (!colorsloaded) {
            setDefaultColors();
            storeColors(context,allcolors,primarycolors);
        }
        colorspergroup = allcolors.length / primarycolors.length;
    }

    /**
     * Gets instance.
     *
     * @param context the context
     * @return the instance
     */
    static ActionColorCoding getInstance(Context context) {
        if (instance == null) {
            instance = new ActionColorCoding(context);
        }
        return instance;
    }

    /**
     * Gets colors per group.
     *
     * @return the colors per group
     */
    static int getColorsPerGroup() {
        return colorspergroup;
    }


    /**************************************************************************
     * setActionBarColor - Change the Action Bar background color to
     * the respetive primary color code.
     *
     * @param context   The context from the invoking activity
     * @param intent    The intent from the invoking activity
     * @param actionbar The ActionBar from the invoking activity
     */
    static void setActionBarColor(
            Context context,
            Intent intent,
            ActionBar actionbar
    ) {
        setDefaultColors();
        int passedoption = intent.getIntExtra(
                StandardAppConstants.INTENTKEY_MENUCOLORCODE,
                -1
        );
        //int colorlist[] = context.getResources().getIntArray(R.array.colorList);
        int actionbarcolor = primarycolors[passedoption % primarycolors.length];
        ColorDrawable cd = new ColorDrawable();
        cd.setColor(actionbarcolor);
        actionbar.setBackgroundDrawable(cd);
    }

    /**************************************************************************
     * setHeadingColor Sets the heading to the respective shade according to
     * the Color Code.
     * The respective Color code being based upon the
     * intentextra passed to the activity from which this
     * is called.
     *
     * @param context The context from the invoking activity
     * @param intent  The intent from the invoking activity
     * @param offset  The offset into the shades (0 = primary)
     * @return respective color as int
     */
    public static int setHeadingColor(
            Context context,
            Intent intent,
            int offset) {

        /**
         * Get the color code from the passed intent
         */
        int passedoption = intent.getIntExtra(
                StandardAppConstants.INTENTKEY_MENUCOLORCODE,
                -1
        );
        /**
         * Calculate how many shades per color
         */
        int colorspergroup = allcolors.length / primarycolors.length;
        /**
         * offset can be a maximum og the number of colors per group
         *  (less 1 as it is an offest).
         */
        if (offset > (colorspergroup -1)) {
            offset = colorspergroup -1;
        }
        /**
         * return the respective color
         */
        return allcolors[
                (
                        (passedoption * colorspergroup)
                                % allcolors.length
                ) +
                        offset
                ];
    }

    /**
     * setActionButtonColor - Change the color of the buttons drawable
     * Used to ensure that rounded corners etc
     * are changed.
     *
     * @param view  id of the button to be changed
     * @param color color to change the backkground to
     */
    public static void setActionButtonColor(View view, int color) {
        ((GradientDrawable) view.getBackground()).setColor(color);
    }
    private static void setDefaultColors() {

        if (colorset) { return; }

        // All Colors (first per set is primary/base color)
        int[] R = {0xffff0000, 0xffff3333, 0xffff6666, 0xffff9999, 0xffffbbbb };
        int[] Y = {0xffffb000, 0xffffc333, 0xffffd666, 0xffffe999, 0xfffffbbb };
        int[] G = {0xff32bd32, 0xff42cd42, 0xff52dd52, 0xff62ed62, 0xff72fd72 };
        int[] B = {0xff0000ff, 0xff5555ff, 0xff7777ff, 0xff9999ff, 0xffbbbbff };
        int[] P = {0xffb010f0, 0xffc020f0, 0xffd030f0, 0xffe040f0, 0xfff050f0 };
        int[] DR = {0xffdd0000, 0xffdd3333, 0xffdd6666, 0xffdd9999, 0xffddbbbb };
        int[] DY = {0xffff8800, 0xffff8833, 0xffff9966, 0xffffaa99, 0xffffbbaa};
        int[] DG = {0xff228b22, 0xff449b44, 0xff66ab66, 0xff88bb88, 0xffaacbaa };
        int[] DB = {0xff0000aa, 0xff0033bb, 0xff0066cc, 0xff0099dd, 0xff00bbee };
        int[] DP = {0xff5010b0, 0xff6020c0, 0xff7030d0, 0xff8040e0, 0xff9050f0 };

        //Build an array of primary/base colors
        int[] pc = {R[0], Y[0], G[0], B[0], P[0],
                DR[0], DY[0], DG[0], DB[0], DP[0]};
        //Build an array of all colors
        int[] ac = {R[0], R[1], R[2], R[3], R[4],
                Y[0], Y[1], Y[2], Y[3], Y[4],
                G[0], G[1], G[2], G[3], G[4],
                B[0], B[1], B[2], B[3], B[4],
                P[0], P[1], P[2], P[3], P[4],
                DR[0], DR[1], DR[2], DR[3], DR[4],
                DY[0], DY[1], DY[2], DY[3], DY[4],
                DG[0], DG[1], DG[2], DG[3], DG[4],
                DB[0], DB[1], DB[2], DB[3], DB[4],
                DP[0], DP[1], DP[2], DP[3], DP[4]
        };
        primarycolors = pc;
        allcolors = ac;
        colorset = true;
    }

    /**
     * Store colors.
     *
     * @param context       the context
     * @param allcolors     the allcolors
     * @param primarycolors the primarycolors
     */
    public static void storeColors(Context context, int[] allcolors, int[] primarycolors) {
        setDefaultColors();
        DBAppvaluesMethods dbAppvaluesmethods = new DBAppvaluesMethods(context);
        for (int i = 0; i < allcolors.length; i++) {
            dbAppvaluesmethods.insertAppvalue(COLORCODENAME,allcolors[i],true,false,Integer.toString(i));
        }
        for (int i = 0; i < primarycolors.length; i++) {
            dbAppvaluesmethods.insertAppvalue(PRIMARYCOLORCODENAME,primarycolors[i],true,true,Integer.toString(i));
        }
    }

    /**
     * Load colors.
     *
     * @param context the context
     */
    public static void loadColors(Context context) {

        DBDAO dbdao = new DBDAO(context);

        Cursor pccsr = dbdao.getTableRows(
                DBAppvaluesTableConstants.APPVALUES_TABLE,
                "",
                DBAppvaluesTableConstants.APPVALUES_NAME_COL +
                        " = '" +
                        PRIMARYCOLORCODENAME +
                        "' ",
                DBAppvaluesTableConstants.APPVALUES_SETTINGSINFO_COL +
                        " ASC "
        );
        int pccount = pccsr.getCount();
        pccsr.close();

        Cursor accsr = dbdao.getTableRows(
                DBAppvaluesTableConstants.APPVALUES_TABLE,
                "",
                DBAppvaluesTableConstants.APPVALUES_NAME_COL +
                        " = '" +
                        COLORCODENAME +
                        "' ",
                DBAppvaluesTableConstants.APPVALUES_SETTINGSINFO_COL +
                        " ASC "
        );
        int account = accsr.getCount();
        accsr.close();
        if (account < 1 || pccount < 1) { return; }

        DBAppvaluesMethods dbappvaluesmethods = new DBAppvaluesMethods(context);
        ArrayList<Long> pc = dbappvaluesmethods.getLongAppvalues(PRIMARYCOLORCODENAME);
        ArrayList<Long> ac = dbappvaluesmethods.getLongAppvalues(COLORCODENAME);

        int[] ipc = new int[pc.size()];
        for (int i = 0; i < pc.size(); i++) {
            ipc[i] =  pc.get(i).intValue();
        }
        int[] iac = new int[ac.size()];
        for (int i = 0; i < ac.size(); i++) {
            iac[i] = ac.get(i).intValue();
        }
        primarycolors = ipc;
        allcolors = iac;
        colorsloaded = true;
    }

}
