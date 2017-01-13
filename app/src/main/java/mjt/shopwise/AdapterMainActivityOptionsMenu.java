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
import android.widget.LinearLayout;
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
class AdapterMainActivityOptionsMenu extends CursorAdapter {

    private int[] colorlist;
    private DBShopMethods dbshops;
    private DBAisleMethods dbaisles;
    private DBProductMethods dbproducts;
    private DBProductUsageMethods dbprodusages;
    private DBShopListMethods dbshoplist;
    private DBRuleMethods dbrules;
    private Context ctxt;
    private Intent callerintent;


    /**
     * Instantiates a new Adapter main activity options menu.
     *
     * @param context the context
     * @param csr     the csr
     * @param flags   the flags
     */
    AdapterMainActivityOptionsMenu(Context context, Cursor csr, int flags, Intent intent) {
        super(context, csr, 0);
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
        return LayoutInflater.from(context).inflate(
                R.layout.activity_main_optionsmenu,
                parent,
                false);
    }
    @Override
    public void bindView(View view, Context context, Cursor csr) {
        int position = csr.getPosition();
        boolean showthis = true;

        TextView option = (TextView) view.findViewById(
                R.id.activity_main_OptionsMenu_option
        );
        TextView label = (TextView) view.findViewById(
                R.id.activity_main_OptionsMenu_label
        );
        TextView option_rowcount = (TextView) view.findViewById(
                R.id.activity_main_OptionsMenu_rowcount
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
        ActionColorCoding acc = ActionColorCoding.getInstance(ctxt);
        View view = super.getView(position,convertview, parent);
        TextView option_tv = (TextView) view.findViewById(
                R.id.activity_main_OptionsMenu_option
        );
        TextView option_info_tv = (TextView) view.findViewById(
                R.id.activity_main_OptionsMenu_label
        );
        option_tv.setTag(position);
        option_tv.setTextColor(Color.WHITE);
        ((GradientDrawable) option_tv.getBackground()).setColor(
                acc.getPrimaryColor(position));
                //colorlist[position % colorlist.length]
        return view;
    }
}
