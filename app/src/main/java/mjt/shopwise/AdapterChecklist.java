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
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.NumberFormat;

/**
 * Checklist ListView/Spinner Cursor Adapter
 */

@SuppressWarnings({"FieldCanBeLocal", "WeakerAccess"})
public class AdapterChecklist extends CursorAdapter {

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
    private int productusage_checklistflag_offset = 0;
    private int productusage_checklistcount_offset = 0;
    private int storagename_offset = 0;

    @SuppressWarnings("unused")
    private String laststoragename = "";

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

    public static final String THISCLASS = AdapterChecklist.class.getSimpleName();
    public static final String LOGTAG = "SW_ACL(CsrAdpt)";

    AdapterChecklist(Context context, Cursor csr, @SuppressWarnings("SameParameterValue") int flags, Intent intent) {
        super(context, csr, flags);
        String msg = "Constructing";
        String methodname = "Construct";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        ctxt = context;
        callerintent = intent;
        this.fromspinner = false;
        this.cursor = csr;
        setChecklistOffsets();
    }

    AdapterChecklist(Context context, Cursor csr, int flags, Intent intent,
                     boolean fromspinner) {
        super(context, csr, flags);
        String msg = "Constructing";
        String methodname = "Construct";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        ctxt = context;
        callerintent = intent;
        this.fromspinner = fromspinner;
        this.cursor = csr;
        setChecklistOffsets();
    }

    @Override
    public View newView(Context context, Cursor csr, ViewGroup parent) {
        String msg = "Inflating Layout";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        this.cursor = csr;
        return LayoutInflater.from(context).inflate(
                R.layout.checklistlist, parent, false
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
        View view = super.getView(position, convertview, parent);
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);

        TextView order1 = view.findViewById(R.id.checklist_order1_button);
        TextView less1 = view.findViewById(R.id.checklist_less1_button);
        TextView checkoff = view.findViewById(R.id.checklist_checkoff_button);
        LinearLayout storagelinearlayout =
                view.findViewById(R.id.checklist_storage_linearlayout);
        TextView storage = view.findViewById(R.id.checklist_storage);
        int primary_color = ActionColorCoding.setHeadingColor(ctxt,callerintent,0);
        int h1 = ActionColorCoding.setHeadingColor(ctxt,callerintent,1);

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

        Cursor csr = this.getCursor();
        if (position > 0) {
            csr.moveToPrevious();
            String previoustoragename = csr.getString(storagename_offset);
            csr.moveToNext();
            String currentstoragename = csr.getString(storagename_offset);
            if (previoustoragename.equals(currentstoragename)) {
                storagelinearlayout.setVisibility(View.GONE);
            } else {
                storage.setText(currentstoragename);
                storagelinearlayout.setVisibility(View.VISIBLE);
                storagelinearlayout.setBackgroundColor(h1);
            }
        } else {
            storagelinearlayout.setVisibility(View.VISIBLE);
            storagelinearlayout.setBackgroundColor(h1);
            storage.setText(csr.getString(storagename_offset));
        }
        ActionColorCoding.setActionButtonColor(order1,primary_color);
        ActionColorCoding.setActionButtonColor(checkoff,primary_color);
        ActionColorCoding.setActionButtonColor(less1,primary_color);
        return view;
    }


    private View initView(View view, Cursor csr) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        String storagename;
        TextView productname = view.findViewById(R.id.checklist_productname);
        TextView shopname = view.findViewById(R.id.checklist_shopname);
        TextView aislename = view.findViewById(R.id.checklist_aislename);
        TextView price = view.findViewById(R.id.checklist_price);
        TextView stocklevel = view.findViewById(R.id.checklist_stocklevel);
        TextView orderlevel = view.findViewById(R.id.checklist_orderlevel);
        TextView order1 = view.findViewById(R.id.checklist_order1_button);
        TextView less1 = view.findViewById(R.id.checklist_less1_button);
        TextView checkoff = view.findViewById(R.id.checklist_checkoff_button);
        @SuppressWarnings({"UnusedAssignment", "unused"}) LinearLayout storagelayout = view.findViewById(R.id.checklist_storage_linearlayout);
        TextView storage = view.findViewById(R.id.checklist_storage);

        productname.setText(csr.getString(product_name_offset));
        shopname.setText(csr.getString(shop_name_offset));
        aislename.setText(csr.getString(aisle_name_offset));
        price.setText(NumberFormat.getCurrencyInstance().format(csr.getDouble(productusage_cost_offest)));
        stocklevel.setText(csr.getString(productusage_checklistcount_offset));
        orderlevel.setText(csr.getString(calculated_orderedcount_offset));
        order1.setTag(csr.getPosition());
        less1.setTag(csr.getPosition());
        checkoff.setTag(csr.getPosition());
        storagename = csr.getString(storagename_offset);
        storage.setText(storagename);

        msg = "Setting Product name=" + productname.getText().toString() +
                " Shop=" + shopname.getText().toString() +
                " Aisle=" + aislename.getText().toString() +
                "\n\tPrice=" + price.getText().toString() +
                " Level=" + stocklevel.getText().toString() +
                " SL #=" + orderlevel.getText().toString();
        if (csr.getInt(productusage_checklistflag_offset) > 1) {
            LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,
                    msg + " Status=Checked",THISCLASS,methodname);
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
            LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,
                    msg + " Status=UnChecked",THISCLASS,methodname);
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

        storagename_offset = cursor.getColumnIndex(DBStorageTableConstants.STORAGE_NAME_COL);
    }
}
