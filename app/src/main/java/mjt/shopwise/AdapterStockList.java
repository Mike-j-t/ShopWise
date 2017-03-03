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
 * Created by Mike092015 on 16/12/2016.
 */

@SuppressWarnings({"FieldCanBeLocal", "WeakerAccess"})
public class AdapterStockList extends CursorAdapter {

    private final Intent callerintent;
    private final Context ctxt;
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

    private int product_name_offset = 0;

    private int aisle_name_offset = 0;
    private int aisle_order_offset = 0;
    private int aisle_shopref_offset = 0;

    private int shop_name_offset = 0;
    private int shop_city_offset = 0;
    private int shop_order_offset = 0;
    public static final String THISCLASS = AdapterStockList.class.getSimpleName();
    private static final String LOGTAG = "SW_ASL(CsrAdptr)";


    AdapterStockList(Context context, Cursor csr, int flags, Intent intent) {
        super(context, csr, 0);
        String msg = "Constructing";
        String methodname = "Construct";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        ctxt = context;
        callerintent = intent;
        setStockOffsets();
    }

    AdapterStockList(Context context, Cursor csr, @SuppressWarnings("SameParameterValue") int flags, Intent intent, @SuppressWarnings("SameParameterValue") boolean fromspinner) {
        super(context, csr, 0);
        String msg = "Constructing";
        String methodname = "Construct";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        ctxt = context;
        callerintent = intent;
        this.fromspinner = fromspinner;
        this.cursor = csr;
        setStockOffsets();
    }

    @Override
    public View newView(Context context, Cursor csr, ViewGroup parent) {
        String msg = "Inflating Layout";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        this.cursor = csr;
        return LayoutInflater.from(context).inflate(
                R.layout.stocklist, parent, false
        );
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
            int cpos = this.cursor.getPosition();
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
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        TextView productname = (TextView) view.findViewById(R.id.stocklist_productname);
        TextView productcost = (TextView) view.findViewById(R.id.stocklist_cost);
        TextView productorder = (TextView) view.findViewById(R.id.stocklist_order);

        productname.setText(csr.getString(product_name_offset));
        productcost.setText(NumberFormat.getCurrencyInstance().format(csr.getDouble(productusage_cost_offest)));
        productorder.setText(csr.getString(productusage_order_offset));
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
