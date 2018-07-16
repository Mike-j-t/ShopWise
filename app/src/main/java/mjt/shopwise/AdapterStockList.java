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
 * StockList Listview/Spinner Cursor Adapter
 */

@SuppressWarnings({"FieldCanBeLocal", "WeakerAccess"})
public class AdapterStockList extends CursorAdapter {

    private final Intent callerintent;
    private final Context ctxt;
    @SuppressWarnings("CanBeFinal")
    private boolean fromspinner;
    @SuppressWarnings("CanBeFinal")
    private boolean clickable;
    @SuppressWarnings("CanBeFinal")
    private boolean longclickable;
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
    private int productusage_order_offset = 0;
    @SuppressWarnings("unused")
    private int productusage_rulesuggestflag_offset = 0;

    private int product_name_offset = 0;

    @SuppressWarnings("unused")
    private int aisle_name_offset = 0;
    @SuppressWarnings("unused")
    private int aisle_order_offset = 0;
    @SuppressWarnings("unused")
    private int aisle_shopref_offset = 0;

    @SuppressWarnings("unused")
    private int shop_name_offset = 0;
    @SuppressWarnings("unused")
    private int shop_city_offset = 0;
    @SuppressWarnings("unused")
    private int shop_order_offset = 0;
    public static final String THISCLASS = AdapterStockList.class.getSimpleName();
    private static final String LOGTAG = "SW_ASL(CsrAdptr)";

    AdapterStockList(Context context,
                     Cursor csr,
                     @SuppressWarnings({"SameParameterValue", "UnusedParameters"})
                             int flags,
                     Intent intent,
                     @SuppressWarnings("SameParameterValue") boolean fromspinner,
                     @SuppressWarnings("SameParameterValue") boolean clickable,
                     @SuppressWarnings("SameParameterValue") boolean longclickable) {
        super(context, csr, 0);
        String msg = "Constructing";
        String methodname = "Construct";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        ctxt = context;
        callerintent = intent;
        this.fromspinner = fromspinner;
        this.clickable = clickable;
        this.longclickable = longclickable;
        this.cursor = csr;
        setStockOffsets();
    }

    @Override
    public View newView(Context context, Cursor csr, ViewGroup parent) {
        String msg = "Inflating Layout";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        this.cursor = csr;
        View rv = LayoutInflater.from(context).inflate(
                R.layout.stocklist, parent, false
        );
        if (fromspinner) {
            ((TextView) rv.findViewById(R.id.rowind_click)).setText("");
            ((TextView) rv.findViewById(R.id.rowind_longclick)).setText("");
        }
        return rv;
    }

    @Override
    public void bindView(View view, Context context, Cursor csr) {
        view = initView(view, csr);
    }

    @Override
    public View getDropDownView(int position, View convertview, ViewGroup parent) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        View view = super.getDropDownView(position, convertview, parent);
        if (fromspinner) {
            view = View.inflate(ctxt,R.layout.stocklist,null);
            view.setBackgroundResource(R.drawable.textviewborder);
            int evenrow = ActionColorCoding.setHeadingColor(ctxt,
                    callerintent,
                    ActionColorCoding.getColorsPerGroup() -1) &
                    ActionColorCoding.transparency_evenrow;
            int oddrow = evenrow & ActionColorCoding.transparency_oddrow;
            if (position % 2 == 0) {
                view.setBackgroundColor(evenrow);
            } else {
                view.setBackgroundColor(oddrow);
            }

        }
        this.cursor.moveToPosition(position);
        bindView(view, ctxt, this.cursor);
        return view;

    }

    @Override
    public View getView(int position, View convertview, ViewGroup parent) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        View view = super.getView(position, convertview, parent);

        int evenrow = ActionColorCoding.setHeadingColor(ctxt,
                callerintent,
                ActionColorCoding.getColorsPerGroup() -1) &
                ActionColorCoding.transparency_evenrow;
        int oddrow = evenrow &
                ActionColorCoding.transparency_oddrow;
        if (position % 2 == 0) {
            view.setBackgroundColor(evenrow);
        } else {
            view.setBackgroundColor(oddrow);
        }
        return view;
    }


    private View initView(View view, Cursor csr) {
        String msg = "Invoked";
        String clickablerowindicator =
                ctxt.getResources().getString(
                        R.string.clickrowindicator
                );
        String longclickablerowindicator =
                ctxt.getResources().getString(
                        R.string.longclickrowindicator
                );
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        TextView productname = view.findViewById(R.id.stocklist_productname);
        TextView productcost = view.findViewById(R.id.stocklist_cost);
        TextView productorder = view.findViewById(R.id.stocklist_order);

        productname.setText(csr.getString(product_name_offset));
        productcost.setText(NumberFormat.getCurrencyInstance().format(csr.getDouble(productusage_cost_offest)));
        productorder.setText(csr.getString(productusage_order_offset));

        if (!clickable || fromspinner) {
            clickablerowindicator = "";
        }
        if ((!longclickable || fromspinner)) {
            longclickablerowindicator = "";
        }

        ((TextView) view.findViewById(
                R.id.rowind_click)).setText(
                        clickablerowindicator);
        ((TextView) view.findViewById(
                R.id.rowind_longclick)).setText(
                        longclickablerowindicator
        );
        msg = "Set Product=" + productname.getText().toString() +
                " Cost=" + productcost.getText().toString() +
                " Order=" + productorder.getText().toString();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);

        return view;

    }

    private void setStockOffsets() {
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

        product_name_offset = cursor.getColumnIndex(DBProductsTableConstants.PRODUCTS_NAME_COL);

        aisle_name_offset = cursor.getColumnIndex(DBAislesTableConstants.AISLES_NAME_COL);
        aisle_order_offset = cursor.getColumnIndex(DBAislesTableConstants.AISLES_ORDER_COL);
        aisle_shopref_offset = cursor.getColumnIndex(DBAislesTableConstants.AISLES_SHOPREF_COL);

        shop_name_offset = cursor.getColumnIndex(DBShopsTableConstants.SHOPS_NAME_COL);
        shop_city_offset = cursor.getColumnIndex(DBShopsTableConstants.SHOPS_CITY_COL);
        shop_order_offset = cursor.getColumnIndex(DBShopsTableConstants.SHOPS_ORDER_COL);

    }

}
