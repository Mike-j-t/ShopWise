package mjt.shopwise;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * FileList ListView/Spinner Cursor Adapter
 */

@SuppressWarnings("WeakerAccess")
public class AdapterFileList extends ArrayAdapter<File> {

    private final Intent callerintent;
    private final Activity context;
    private final List<File> flst;

    private TextView tv_filename;
    private TextView tv_filemod;
    @SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd yyyy 'at' HH:mm");
    public static final String THISCLASS = AdapterFileList.class.getSimpleName();
    public static final String LOGTAG = "SW_AFL(CsrAdpt)";

    AdapterFileList(Activity context, @SuppressWarnings("SameParameterValue") int layout, ArrayList<File> flst, Intent intent) {
        super(context, layout, flst);
        String msg = "Constructing";
        String methodname = "Construct";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        this.context = context;
        this.flst = flst;
        callerintent = intent;
    }

    public long getItemId(int position) {
        return position;
    }

    // This is the view used for the dropdown entries
    @Override
    public View getDropDownView(int position, final View convertview, @NonNull ViewGroup parent) {
        //super.getDropDownView(position, convertview, parent);
        String msg = "Inflating Layout";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        View v;
        v = View.inflate(context,R.layout.filelist,null);
        v.setBackgroundResource(R.drawable.textviewborder);
        int evenrow = ActionColorCoding.setHeadingColor(context,
                callerintent,
                ActionColorCoding.getColorsPerGroup() -1) &
                ActionColorCoding.transparency_evenrow;
        int oddrow = evenrow & ActionColorCoding.transparency_oddrow;
        File flentry = flst.get(position);
        if(position % 2 == 0) {
            //v.setBackgroundColor(evenrow);
            ActionColorCoding.setActionButtonColor(v, evenrow);
        } else {
            //v.setBackgroundColor(oddrow);
            ActionColorCoding.setActionButtonColor(v, oddrow);
        }
        tv_filename = v.findViewById(R.id.filelist_filename);
        tv_filemod = v.findViewById(R.id.filelist_filemodifieddatetime);
        tv_filename.setText(flentry.getName());
        tv_filename.setTextColor(Color.BLACK);
        tv_filemod.setText(sdf.format(flentry.lastModified()));
        msg = "Setting File=" + tv_filename.getText().toString() +
                " Mod date=" + tv_filemod.getText().toString();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);


        /*
        v.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(context,"Delete File Here???",Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        */
        return v;
    }

    @NonNull
    @Override
    public View getView(int position, View convertview, @NonNull ViewGroup parent) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);
        View view = convertview;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(
                    R.layout.filelist, parent, false
            );
        }
        File flentry = flst.get(position);
        if (flentry != null) {
            tv_filename = view.findViewById(R.id.filelist_filename);
            tv_filemod = view.findViewById(R.id.filelist_filemodifieddatetime);

            tv_filename.setText(flentry.getName());
            tv_filename.setTextColor(Color.BLACK);
            tv_filemod.setText(sdf.format(flentry.lastModified()));
            msg = "Setting File=" + tv_filename.getText().toString() +
                    " Mod date=" + tv_filemod.getText().toString();
            LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);

        }
        int evenrow = ActionColorCoding.setHeadingColor(context, callerintent,
                 ActionColorCoding.getColorsPerGroup() -1) &
                 ActionColorCoding.transparency_evenrow;
        int oddrow = evenrow & ActionColorCoding.transparency_oddrow;
        if (position % 2 == 0) {
            view.setBackgroundColor(evenrow);
        } else {
            view.setBackgroundColor(oddrow);
        }
        return view;
    }

}
