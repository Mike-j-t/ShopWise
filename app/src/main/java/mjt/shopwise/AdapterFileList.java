package mjt.shopwise;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
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
 * Created by Mike092015 on 14/01/2017.
 */

public class AdapterFileList extends ArrayAdapter<File> {

    private Intent callerintent;
    private Activity context;
    private List<File> flst;

    private TextView tv_filename;
    private TextView tv_filemod;
    @SuppressLint("SimpleDateFormat")private SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd yyyy 'at' HH:mm");

    AdapterFileList(Activity context, int layout, ArrayList<File> flst, Intent intent) {
        super(context, layout, flst);
        this.context = context;
        this.flst = flst;
        callerintent = intent;
    }

    public long getItemId(int position) {
        return position;
    }

    // This is the view used for the dropdown entries
    @Override
    public View getDropDownView(int position, final View convertview, ViewGroup parent) {
        //super.getDropDownView(position, convertview, parent);
        View v = convertview;
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
        tv_filename = (TextView) v.findViewById(R.id.filelist_filename);
        tv_filemod = (TextView) v.findViewById(R.id.filelist_filemodifieddatetime);
        tv_filename.setText(flentry.getName());
        tv_filename.setTextColor(Color.BLACK);
        tv_filemod.setText(sdf.format(flentry.lastModified()));


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

    @Override
    public View getView(int position, View convertview, ViewGroup parent) {
        View view = convertview;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(
                    R.layout.filelist, parent, false
            );
        }
        File flentry = flst.get(position);
        if (flentry != null) {
            tv_filename = (TextView) view.findViewById(R.id.filelist_filename);
            tv_filemod = (TextView) view.findViewById(R.id.filelist_filemodifieddatetime);

            tv_filename.setText(flentry.getName());
            tv_filename.setTextColor(Color.BLACK);
            tv_filemod.setText(sdf.format(flentry.lastModified()));

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