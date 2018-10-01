package mjt.shopwise;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;


/**
 * Custom Cursor Adapter for the list (ListView) of Options.
 * <p>
 * Each ListView rows has 3 views (dummy sperators ignored), they are:-
 * 1) The Option, this is TextView that appears and acts as a button
 * (click/touch invokes the appropriate action).
 * 2) Notes providing a brief overview of the options purpose.
 * 3) A count of the number of respective rows in the underlying database.
 * <p>
 * Options are color coded according the the integer array colorlist.
 * The colorlist array is built from the xml array which is obtained
 * from Resources (res/values/colors.xml)
 * The actual color used is determined according to the position of the item.
 * i.e. the first Option uses the first color, the 2nd Option the 2nd color
 * and so on.
 * <p>
 * If more Options exist than there are colors then the colors are reused.
 * This resuse is accomplish by dividing the position by the number of colors
 * and then using the remainder as the array index.
 * i.e. setColor(colortlist[posistion % colorlist.length()])
 */
@SuppressWarnings({"FieldCanBeLocal", "WeakerAccess"})
class AdapterMainActivityOptionsMenu extends CursorAdapter {

    @SuppressWarnings("unused")
    private final int[] colorlist;
    @SuppressWarnings("unused")
    private final DBShopMethods dbshops;
    @SuppressWarnings("unused")
    private final DBAisleMethods dbaisles;
    @SuppressWarnings("unused")
    private final DBProductMethods dbproducts;
    @SuppressWarnings("unused")
    private final DBProductUsageMethods dbprodusages;
    @SuppressWarnings("unused")
    private final DBShopListMethods dbshoplist;
    @SuppressWarnings("unused")
    private final DBRuleMethods dbrules;
    private final Context ctxt;
    @SuppressWarnings("unused")
    private final Intent callerintent;
    public static final String THISCLASS = AdapterMainActivityOptionsMenu.class.getSimpleName();
    public static final String LOGTAG = "SW_AMAOM(CsrAdpt)";


    /**
     * Instantiates a new Adapter main activity options menu.
     *
     * @param context the context
     * @param csr     the csr
     * @param flags   the flags
     */
    AdapterMainActivityOptionsMenu(Context context, Cursor csr, @SuppressWarnings({"SameParameterValue", "UnusedParameters"}) int flags, Intent intent) {
        super(context, csr, 0);
        String msg = "Constructing";
        String methodname = "Construct";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        colorlist =  context.getResources().getIntArray(R.array.colorList);
        dbshops = new DBShopMethods(context);
        dbaisles = new DBAisleMethods(context);
        dbproducts = new DBProductMethods(context);
        dbprodusages = new DBProductUsageMethods(context);
        dbshoplist = new DBShopListMethods(context);
        dbrules = new DBRuleMethods(context);
        this.ctxt = context;
        callerintent = intent;
    }

    @Override
    public View newView(Context context, Cursor csr, ViewGroup parent) {
        String msg = "Inflating Layout";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        return LayoutInflater.from(context).inflate(
                R.layout.activity_main_optionsmenu,
                parent,
                false);
    }
    @Override
    public void bindView(View view, Context context, Cursor csr) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);

        TextView option = view.findViewById(
                R.id.activity_main_OptionsMenu_option
        );
        TextView label = view.findViewById(
                R.id.activity_main_OptionsMenu_label
        );
        String optiontext = csr.getString(
                csr.getColumnIndex(
                        DBAppvaluesTableConstants.APPVALUES_TEXT_COL
                )
        );

        option.setText(optiontext);
        label.setText(csr.getString(
                csr.getColumnIndex(
                        DBAppvaluesTableConstants.APPVALUES_SETTINGSINFO_COL
                )
        ));

    }

    @Override
    public View getView(int position, View convertview, ViewGroup parent) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        //ActionColorCoding acc = ActionColorCoding.getInstance(ctxt);
        View view = super.getView(position,convertview, parent);

        // Get the cursor, store current position, and move to position as
        // passed (this shouldn;t be required as cursor should already be
        //  positioned correctly)
        Cursor csr = getCursor();
        int cpos = csr.getPosition();
        csr.moveToPosition(position);
        // get the menuoption, which may not, directly correlate with position
        // e.g. if not all options are currently available.
        int menuoption = csr.getInt(csr.getColumnIndex(
                DBAppvaluesTableConstants.APPVALUES_INT_COL
        ));
        // Move cursor back to it's starting position
        csr.moveToPosition(cpos);

        //Set view id's
        TextView option_tv = view.findViewById(
                R.id.activity_main_OptionsMenu_option
        );
        TextView option_info_tv = view.findViewById(
                R.id.activity_main_OptionsMenu_label
        );

        // Update views (Tag to indicate menuoption number)
        option_tv.setTag(menuoption);
        // Text White
        option_tv.setTextColor(Color.WHITE);
        // Background colour to the respectibe colour
        ((GradientDrawable) option_tv.getBackground()).setColor(
                ActionColorCoding.getPrimaryColor(menuoption));
        msg = "Setting Option=" + option_tv.getText().toString() +
                " Info=" + option_info_tv.getText().toString();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        return view;
    }
}
