package mjt.shopwise;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.text.NumberFormat;

/**
 * Order ListView/Spinner Cursor Adapter
 */

@SuppressWarnings({"FieldCanBeLocal", "WeakerAccess"})
public class AdapterOrderList extends CursorAdapter {

    private final Intent callerintent;
    private final Context ctxt;
    @SuppressWarnings("unused")
    private final boolean fromspinner;
    private Cursor cursor;

    @SuppressWarnings("unused")
    private int productusage_productref_offset = -1;
    private int productusage_aisleref_offset = 0;
    private int productusage_cost_offest = 0;
    @SuppressWarnings("unused")
    private int productusage_buycount_offset = 0;
    @SuppressWarnings("unused")
    private int productusage_firstbuydate_offset = 0;
    @SuppressWarnings("unused")
    private int productusage_latestbuydate_offset = 0;
    @SuppressWarnings("unused")
    private int productusage_order_offset = 0;
    @SuppressWarnings("unused")
    private int productusage_rulesuggestflag_offset = 0;
    @SuppressWarnings("unused")
    private int productusage_checklistflag_offset = 0;
    @SuppressWarnings("unused")
    private int productusage_checklistcount_offset = 0;

    private int calculated_orderedcount_offset = 0;

    private int product_name_offset = 0;

    private int aisle_name_offset = 0;
    @SuppressWarnings("unused")
    private int aisle_order_offset = 0;
    @SuppressWarnings("unused")
    private int aisle_shopref_offset = 0;

    private int shop_name_offset = 0;
    @SuppressWarnings("unused")
    private int shop_city_offset = 0;
    @SuppressWarnings("unused")
    private int shop_order_offset = 0;

    public static final String THISCLASS = AdapterOrderList.class.getSimpleName();
    private static final String LOGTAG = "SW_AOL(CsrAdptr)";

    AdapterOrderList(Context context, Cursor csr, int flags, Intent intent) {
        super(context, csr, flags);
        String msg = "Constructing";
        String methodname = "Construct";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        ctxt = context;
        callerintent = intent;
        this.fromspinner = false;
        this.cursor = csr;
        setOrderlistOffsets();
    }

    AdapterOrderList(Context context, Cursor csr, @SuppressWarnings("SameParameterValue") int flags, Intent intent,
                     @SuppressWarnings("SameParameterValue") boolean fromspinner) {
        super(context, csr, flags);
        String msg = "Constructing";
        String methodname = "Construct";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        ctxt = context;
        callerintent = intent;
        this.fromspinner = fromspinner;
        this.cursor = csr;
        setOrderlistOffsets();

    }

    @Override
    public View newView(Context context, Cursor csr, ViewGroup parent) {
        String msg = "Inflating Layout";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        this.cursor = csr;
        return LayoutInflater.from(context).inflate(
                R.layout.orderlist, parent, false
        );
    }

    @Override
    public void bindView(View view, Context context, Cursor csr) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        view = initView(view, csr);
    }

    @Override
    public View getView(int position, View convertview, ViewGroup parent) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        View view = super.getView(position, convertview, parent);

        TextView order1 = view.findViewById(R.id.orderlist_order1_button);
        TextView less1 = view.findViewById(R.id.orderlist_less1_button);

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
        ActionColorCoding.setActionButtonColor(less1,primary_color);

        return view;
    }

    private View initView(View view, Cursor csr) {

        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);

        TextView productname = view.findViewById(R.id.orderlist_productname);
        TextView shopname = view.findViewById(R.id.orderlist_shopname);
        TextView aislename = view.findViewById(R.id.orderlist_aislename);
        TextView price = view.findViewById(R.id.orderlist_price);
        TextView numberinshop = view.findViewById(R.id.orderlist_numberinshop);
        TextView order1 = view.findViewById(R.id.orderlist_order1_button);
        TextView less1 = view.findViewById(R.id.orderlist_less1_button);

        productname.setText(csr.getString(product_name_offset));
        shopname.setText(csr.getString(shop_name_offset));
        aislename.setText(csr.getString(aisle_name_offset));
        price.setText(NumberFormat.getCurrencyInstance().format(
                csr.getDouble(productusage_cost_offest)));
        numberinshop.setText(csr.getString(calculated_orderedcount_offset));
        order1.setTag(csr.getPosition());
        less1.setTag(csr.getPosition());
        msg = "Set Product=" + productname.getText().toString() +
                " Shop=" + shopname.getText().toString() +
                " Aisle=" + aislename.getText().toString() +
                "\n\tPrice=" + price.getText().toString() +
                " in Shop=" + numberinshop.getText().toString();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        return view;
    }

    private void setOrderlistOffsets() {
        if (productusage_aisleref_offset < 0) {
            return;
        }
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
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

        calculated_orderedcount_offset = cursor.getColumnIndex(DBConstants.CALCULATED_PRODUCTSORDERED_NAME);

        product_name_offset = cursor.getColumnIndex(DBProductsTableConstants.PRODUCTS_NAME_COL);

        aisle_name_offset = cursor.getColumnIndex(DBAislesTableConstants.AISLES_NAME_COL);
        aisle_order_offset = cursor.getColumnIndex(DBAislesTableConstants.AISLES_ORDER_COL);
        aisle_shopref_offset = cursor.getColumnIndex(DBAislesTableConstants.AISLES_SHOPREF_COL);

        shop_name_offset = cursor.getColumnIndex(DBShopsTableConstants.SHOPS_NAME_COL);
        shop_city_offset = cursor.getColumnIndex(DBShopsTableConstants.SHOPS_CITY_COL);
        shop_order_offset = cursor.getColumnIndex(DBShopsTableConstants.SHOPS_ORDER_COL);

    }
}
