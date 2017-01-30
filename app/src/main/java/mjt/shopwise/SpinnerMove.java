package mjt.shopwise;

import android.database.Cursor;
import android.widget.Spinner;

/**
 * moveToColumn. move the spinner (select item) to the
 * item that matches the movetovalue.
 *
 * Note this is for spinners that are based upon a cursor
 */

public class SpinnerMove {

    public static final String THISCLASS = SpinnerMove.class.getSimpleName();
    private static final String LOGTAG = "SW_SM";

    static void moveToColumn(Spinner spinner,
                             long movetovalue,
                             Cursor csr,
                             String column
    )
    {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        int position = 0;
        if (movetovalue > 0 ) {
            csr.moveToPosition(-1);
            while (csr.moveToNext()) {
                if (csr.getLong(
                        csr.getColumnIndex(column)
                ) == movetovalue) {
                    position = csr.getPosition();
                    spinner.setSelection(position);
                    return;
                }
            }
        }
    }

    static void moveToColumn(Spinner spinner,
                             long movetovalue,
                             Cursor csr,
                             String column,
                             boolean animatespinner
    )
    {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        int position = 0;
        if (movetovalue > 0 ) {
            csr.moveToPosition(-1);
            while (csr.moveToNext()) {
                if (csr.getLong(
                        csr.getColumnIndex(column)
                ) == movetovalue) {
                    position = csr.getPosition();
                    spinner.setSelection(position,animatespinner);
                    return;
                }
            }
        }
    }
}
