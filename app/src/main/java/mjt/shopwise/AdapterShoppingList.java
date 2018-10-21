package mjt.shopwise;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.NumberFormat;

/**
 * ShoppingList Adapter
 */

@SuppressWarnings({"FieldCanBeLocal", "WeakerAccess"})
public class AdapterShoppingList extends CursorAdapter {

    private final Intent callerintent;
    private final Context ctxt;
    private Cursor cursor;

    @SuppressWarnings("unused")
    private int shoplist_productref_offset = 0;
    private int shoplist_aisleref_offset = 0;
    @SuppressWarnings("unused")
    private int shoplist_dateadded_offset = 0;
    private int shoplist_numbertoget_offset = 0;
    @SuppressWarnings("unused")
    private int shoplist_done_offset = 0;
    @SuppressWarnings("unused")
    private int shoplist_dategot_offset = 0;
    @SuppressWarnings("unused")
    private int shoplist_cost_offset = 0;

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

    private int calculated_totalcost_offset = 0;

    private int product_name_offset = 0;

    private int aisle_name_offset = 0;
    @SuppressWarnings("unused")
    private int aisle_order_offset = 0;
    private int aisle_shopref_offset = 0;

    private int shop_name_offset = 0;
    private int shop_city_offset = 0;
    @SuppressWarnings("unused")
    private int shop_order_offset = 0;
    @SuppressWarnings("unused")
    private int shop_id_offset = 0;

    private int white = 0;
    private final ColorStateList defaultcolor;
    public static final String THISCLASS = AdapterShoppingList.class.getSimpleName();
    private static final String LOGTAG = "SW_ASgL(CsrAdptr)";

    AdapterShoppingList(Context context, Cursor csr, @SuppressWarnings("SameParameterValue") int flags, Intent intent) {
        super(context, csr, flags);
        String msg = "Constructing";
        String methodname = "Construct";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        ctxt = context;
        callerintent = intent;
        this.cursor = csr;
        setShoppinglistOffsets();
        white = ContextCompat.getColor(ctxt,R.color.colorWhite);
        TextView dummy = new TextView(ctxt);
        defaultcolor = dummy.getTextColors();
    }

    @Override
    public View newView(Context context, Cursor csr, ViewGroup parent) {
        String msg = "Inflating Layout";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        this.cursor = csr;
        return LayoutInflater.from(context).inflate(
                R.layout.shoppinglist, parent, false
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

        TextView adjustbutton = view.findViewById(R.id.shoppinglist_adjustbutton);
        TextView boughtbutton = view.findViewById(R.id.shoppinglist_boughtbutton);
        TextView deletebutton = view.findViewById(R.id.shoppinglist_deletetbutton);
        LinearLayout shopinfo = view.findViewById(R.id.shoppinglist_shopinfo_linearlayout);
        LinearLayout aisleinfo = view.findViewById(R.id.shoppinglist_aisleinfo_linearlayout);
        LinearLayout productinfo = view.findViewById(R.id.shoppinglist_productinfo_linearlayout);

        int primary_color = ActionColorCoding.setHeadingColor(ctxt,callerintent,0);
        int evenrow = ActionColorCoding.setHeadingColor(ctxt,callerintent,
                ActionColorCoding.getColorsPerGroup() -1) &
                ActionColorCoding.transparency_evenrow;
        int oddrow = evenrow &
                ActionColorCoding.transparency_oddrow;

        shopinfo.setBackgroundColor(
                ActionColorCoding.setHeadingColor(ctxt,callerintent,1)
        );
        aisleinfo.setBackgroundColor(
                ActionColorCoding.setHeadingColor(ctxt,callerintent,3)
        );

        Cursor csr = this.getCursor();

        // Reduce duplicated data by only showing the shop and aisle info when
        // if first appears i.e. make the shop info and aisle info GONE
        // when it is the same as the previous.
        // NOTE gteView can be called multiple times for the same row/position.
        //  As such, the previous row is checked
        shopinfo.setVisibility(View.VISIBLE);
        aisleinfo.setVisibility(View.VISIBLE);
        if (position > 0) {
            long thisshopid = csr.getLong(aisle_shopref_offset);
            long thisaisled = csr.getLong(shoplist_aisleref_offset);
            csr.moveToPrevious();
            long previousshopid = csr.getLong(aisle_shopref_offset);
            long previousaisleid = csr.getLong(shoplist_aisleref_offset);
            csr.moveToNext();
            if (previousshopid == thisshopid) {
                shopinfo.setVisibility(View.GONE);
            }
            if (previousaisleid == thisaisled) {
                aisleinfo.setVisibility(View.GONE);
            }
        }

        // Alternate row background color
        if (position % 2 == 0) {
            productinfo.setBackgroundColor(evenrow);
        } else {
            productinfo.setBackgroundColor(oddrow);
        }

        ActionColorCoding.setActionButtonColor(adjustbutton,primary_color);
        ActionColorCoding.setActionButtonColor(boughtbutton,primary_color);
        ActionColorCoding.setActionButtonColor(deletebutton,primary_color);

        return view;
    }

    @SuppressLint("SetTextI18n")
    private View initView(View view, Cursor csr) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);

        TextView productname = view.findViewById(R.id.shoppinglist_productname);
        TextView shopname = view.findViewById(R.id.shoppinglist_shopname);
        TextView aislename = view.findViewById(R.id.shoppinglist_aislename);
        TextView shopcity = view.findViewById(R.id.shoppinglist_shopcity);
        TextView productcost = view.findViewById(R.id.shoppinglist_itemcost);
        TextView numbertoget = view.findViewById(R.id.shoppinglist_numbertoget);
        TextView totalcost = view.findViewById(R.id.shoppinglist_totalcost);
        TextView boughtbutton = view.findViewById(R.id.shoppinglist_boughtbutton);
        TextView adjustbutton = view.findViewById(R.id.shoppinglist_adjustbutton);
        TextView deletebutton = view.findViewById(R.id.shoppinglist_deletetbutton);
        //noinspection unused
        LinearLayout shopinfo = view.findViewById(R.id.shoppinglist_shopinfo_linearlayout);
        //noinspection unused
        LinearLayout aisleinfo = view.findViewById(R.id.shoppinglist_aisleinfo_linearlayout);
        //noinspection unused
        LinearLayout productinfo = view.findViewById(R.id.shoppinglist_productinfo_linearlayout);

        boughtbutton.setTag(csr.getPosition());
        adjustbutton.setTag(csr.getPosition());
        deletebutton.setTag(csr.getPosition());
        productname.setText(csr.getString(product_name_offset));
        shopname.setText(csr.getString(shop_name_offset));
        shopcity.setText(csr.getString(shop_city_offset));
        aislename.setText(csr.getString(aisle_name_offset));
        productcost.setText(NumberFormat.getCurrencyInstance().format(csr.getDouble(productusage_cost_offest)));
        int numberrequired = csr.getInt(shoplist_numbertoget_offset) - csr.getInt(shoplist_done_offset);
        numbertoget.setText(Integer.toString(numberrequired));
        totalcost.setText(NumberFormat.getCurrencyInstance().format(csr.getDouble(calculated_totalcost_offset)));

        // Checkoff if numbertoget is 0
        if (numberrequired < 1 ) {
            productname.setTextColor(white);
            productcost.setTextColor(white);
            numbertoget.setTextColor(white);
            totalcost.setTextColor(white);
            boughtbutton.setVisibility(View.INVISIBLE);
        } else {
            productname.setTextColor(ContextCompat.getColor(ctxt,R.color.colorBlack));
            productcost.setTextColor(defaultcolor);
            numbertoget.setTextColor(defaultcolor);
            totalcost.setTextColor(defaultcolor);
            boughtbutton.setVisibility(View.VISIBLE);
        }
        msg = "SET Shop=" + shopname.getText().toString() +
                " City=" + shopcity.getText().toString() +
                " Aisle=" + aislename.getText().toString() +
                "\n\tProduct=" + productname.getText().toString() +
                " ToGet=" + numbertoget.getText().toString() +
                " Price=" + productcost.getText().toString() +
                " Total+" + totalcost.getText().toString();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        return view;
    }

    private void setShoppinglistOffsets() {
        if (productusage_aisleref_offset < 0) {
            return;
        }
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        shoplist_aisleref_offset = cursor.getColumnIndex(DBShopListTableConstants.SHOPLIST_AISLEREF_COL);
        shoplist_productref_offset = cursor.getColumnIndex(DBShopListTableConstants.SHOPLIST_PRODUCTREF_COL);
        shoplist_dateadded_offset = cursor.getColumnIndex(DBShopListTableConstants.SHOPLIST_DATEADDED_COL);
        shoplist_numbertoget_offset = cursor.getColumnIndex(DBShopListTableConstants.SHOPLIST_NUMBERTOGET_COL);
        shoplist_done_offset = cursor.getColumnIndex(DBShopListTableConstants.SHOPLIST_DONE_COL);
        shoplist_dategot_offset = cursor.getColumnIndex(DBShopListTableConstants.SHOPLIST_DATEGOT_COL);
        shoplist_cost_offset = cursor.getColumnIndex(DBShopListTableConstants.SHOPLIST_COST_COL);

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

        calculated_totalcost_offset = cursor.getColumnIndex(DBConstants.CALCULATED_TOTALCOST);

        product_name_offset = cursor.getColumnIndex(DBProductsTableConstants.PRODUCTS_NAME_COL);

        aisle_name_offset = cursor.getColumnIndex(DBAislesTableConstants.AISLES_NAME_COL);
        aisle_order_offset = cursor.getColumnIndex(DBAislesTableConstants.AISLES_ORDER_COL);
        aisle_shopref_offset = cursor.getColumnIndex(DBAislesTableConstants.AISLES_SHOPREF_COL);

        shop_name_offset = cursor.getColumnIndex(DBShopsTableConstants.SHOPS_NAME_COL);
        shop_city_offset = cursor.getColumnIndex(DBShopsTableConstants.SHOPS_CITY_COL);
        shop_order_offset = cursor.getColumnIndex(DBShopsTableConstants.SHOPS_ORDER_COL);
        shop_id_offset = cursor.getColumnIndex(DBShopsTableConstants.SHOPS_ID_COL);

    }
}
