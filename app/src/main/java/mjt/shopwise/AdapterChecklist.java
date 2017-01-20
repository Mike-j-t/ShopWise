package mjt.shopwise;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.text.NumberFormat;

/**
 * Created by Mike092015 on 27/12/2016.
 */

public class AdapterChecklist extends CursorAdapter {

    private Intent callerintent;
    private Context ctxt;
    private boolean fromspinner;
    private Cursor cursor;

    private int productusage_productref_offset = -1;
    private int productusage_aisleref_offset = 0;
    private int productusage_cost_offest = 0;
    private int productusage_buycount_offset = 0;
    private int productusage_firstbuydate_offset = 0;
    private int productusage_latestbuydate_offset = 0;
    private int productusage_order_offset = 0;
    private int productusage_rulesuggestflag_offset = 0;
    private int productusage_checklistflag_offset = 0;
    private int productusage_checklistcount_offset = 0;

    private int calculated_orderedcount_offset = 0;

    private int product_name_offset = 0;

    private int aisle_name_offset = 0;
    private int aisle_order_offset = 0;
    private int aisle_shopref_offset = 0;

    private int shop_name_offset = 0;
    private int shop_city_offset = 0;
    private int shop_order_offset = 0;

    AdapterChecklist(Context context, Cursor csr, int flags, Intent intent) {
        super(context, csr, flags);
        ctxt = context;
        callerintent = intent;
        this.fromspinner = false;
        this.cursor = csr;
        setChecklistOffsets();
    }

    AdapterChecklist(Context context, Cursor csr, int flags, Intent intent,
                     boolean fromspinner) {
        super(context, csr, flags);
        ctxt = context;
        callerintent = intent;
        this.fromspinner = fromspinner;
        this.cursor = csr;
        setChecklistOffsets();
    }

    @Override
    public View newView(Context context, Cursor csr, ViewGroup parent) {
        this.cursor = csr;
        return LayoutInflater.from(context).inflate(
                R.layout.checklistlist, parent, false
        );
    }

    @Override
    public void bindView(View view, Context context, Cursor csr) {
        view = initView(view, csr);
    }

    @Override
    public View getView(int position, View convertview, ViewGroup parent) {
        View view = super.getView(position, convertview, parent);

        TextView order1 = (TextView) view.findViewById(R.id.checklist_order1_button);
        TextView less1 = (TextView) view.findViewById(R.id.checklist_less1_button);
        TextView checkoff = (TextView) view.findViewById(R.id.checklist_checkoff_button);
        int primary_color = ActionColorCoding.setHeadingColor(ctxt,callerintent,0);

        int evenrow = ActionColorCoding.setHeadingColor(ctxt,
                callerintent,
                ActionColorCoding.getColorsPerGroup() - 1) &
                ActionColorCoding.transparency_evenrow;
        int oddrow = evenrow &
                ActionColorCoding.transparency_oddrow;
        if (position % 2 == 0) {
            view.setBackgroundColor(evenrow);
        } else {
            view.setBackgroundColor(oddrow);
        }
        ActionColorCoding.setActionButtonColor(order1,primary_color);
        ActionColorCoding.setActionButtonColor(checkoff,primary_color);
        ActionColorCoding.setActionButtonColor(less1,primary_color);
        return view;
    }


    private View initView(View view, Cursor csr) {
        TextView productname = (TextView) view.findViewById(R.id.checklist_productname);
        TextView shopname = (TextView) view.findViewById(R.id.checklist_shopname);
        TextView aislename = (TextView) view.findViewById(R.id.checklist_aislename);
        TextView price = (TextView) view.findViewById(R.id.checklist_price);
        TextView stocklevel = (TextView) view.findViewById(R.id.checklist_stocklevel);
        TextView orderlevel = (TextView) view.findViewById(R.id.checklist_orderlevel);
        TextView order1 = (TextView) view.findViewById(R.id.checklist_order1_button);
        TextView less1 = (TextView) view.findViewById(R.id.checklist_less1_button);
        TextView checkoff = (TextView) view.findViewById(R.id.checklist_checkoff_button);

        productname.setText(csr.getString(product_name_offset));
        shopname.setText(csr.getString(shop_name_offset));
        aislename.setText(csr.getString(aisle_name_offset));
        //price.setText(csr.getString(productusage_cost_offest));
        price.setText(NumberFormat.getCurrencyInstance().format(csr.getDouble(productusage_cost_offest)));
        stocklevel.setText(csr.getString(productusage_checklistcount_offset));
        orderlevel.setText(csr.getString(calculated_orderedcount_offset));
        order1.setTag(csr.getPosition());
        less1.setTag(csr.getPosition());
        checkoff.setTag(csr.getPosition());
        if (csr.getInt(productusage_checklistflag_offset) > 1) {
            int checkedcolor = ContextCompat.getColor(ctxt,R.color.colorWhite);
            checkoff.setText(ctxt.getResources().getString(R.string.uncheckbutton));
            productname.setTextColor(checkedcolor);
            shopname.setTextColor(checkedcolor);
            aislename.setTextColor(checkedcolor);
            price.setTextColor(checkedcolor);
            stocklevel.setTextColor(checkedcolor);
            orderlevel.setTextColor(checkedcolor);
            less1.setVisibility(View.INVISIBLE);
            order1.setVisibility(View.INVISIBLE);

        } else {
            TextView dummy = new TextView(ctxt);
            ColorStateList defaultcolor = dummy.getTextColors();
            int uncheckedcolor = ContextCompat.getColor(ctxt,R.color.colorBlack);
            checkoff.setText(ctxt.getResources().getString(R.string.checkoffbutton));
            productname.setTextColor(uncheckedcolor);
            shopname.setTextColor(defaultcolor);
            aislename.setTextColor(defaultcolor);
            price.setTextColor(defaultcolor);
            stocklevel.setTextColor(defaultcolor);
            orderlevel.setTextColor(defaultcolor);
            less1.setVisibility(View.VISIBLE);
            order1.setVisibility(View.VISIBLE);
        }

        return view;

    }

    /**
     *
     */
    private void setChecklistOffsets() {
        if (productusage_aisleref_offset < 0) {
            return;
        }
        productusage_aisleref_offset = cursor.getColumnIndex(DBProductusageTableConstants.PRODUCTUSAGE_AISLEREF_COL);
        productusage_productref_offset = cursor.getColumnIndex(DBProductusageTableConstants.PRODUCTUSAGE_PRODUCTREF_COL);
        productusage_cost_offest = cursor.getColumnIndex(DBProductusageTableConstants.PRODUCTUSAGE_COST_COL);
        productusage_buycount_offset = cursor.getColumnIndex(DBProductusageTableConstants.PRODUCTUSAGE_BUYCOUNT_COL);
        productusage_firstbuydate_offset = cursor.getColumnIndex(DBProductusageTableConstants.PRODUCTUSAGE_FIRSTBUYDATE_COL);
        productusage_latestbuydate_offset = cursor.getColumnIndex(DBProductusageTableConstants.PRODUCTUSAGE_LATESTBUYDATE_COL);
        productusage_order_offset = cursor.getColumnIndex(DBProductusageTableConstants.PRODUCTUSAGE_ORDER_COL);
        productusage_rulesuggestflag_offset = cursor.getColumnIndex(DBProductusageTableConstants.PRODUCTUSAGE_RULESUGGESTFLAG_COL);
        productusage_checklistflag_offset = cursor.getColumnIndex(DBProductusageTableConstants.PRODUCTUSAGE_CHECKLISTFLAG_COL);
        productusage_checklistcount_offset = cursor.getColumnIndex(DBProductusageTableConstants.PRODUCTUSAGE_CHECKLISTCOUNT_COL);

        calculated_orderedcount_offset = cursor.getColumnIndex(DBConstants.CALCULATED_PRODUTSORDERED_NAME);

        product_name_offset = cursor.getColumnIndex(DBProductsTableConstants.PRODUCTS_NAME_COL);

        aisle_name_offset = cursor.getColumnIndex(DBAislesTableConstants.AISLES_NAME_COL);
        aisle_order_offset = cursor.getColumnIndex(DBAislesTableConstants.AISLES_ORDER_COL);
        aisle_shopref_offset = cursor.getColumnIndex(DBAislesTableConstants.AISLES_SHOPREF_COL);

        shop_name_offset = cursor.getColumnIndex(DBShopsTableConstants.SHOPS_NAME_COL);
        shop_city_offset = cursor.getColumnIndex(DBShopsTableConstants.SHOPS_CITY_COL);
        shop_order_offset = cursor.getColumnIndex(DBShopsTableConstants.SHOPS_ORDER_COL);

    }
}
