package mjt.shopwise;

import android.database.Cursor;
import android.widget.Spinner;

/**
 * moveToColumn. move the spinner (select item) to the
 * item that matches the movetovalue.
 *
 * Note this is for spinner that are based upon a cursor
 */

public class SpinnerMove {

    static void moveToColumn(Spinner spinner,
                             long movetovalue,
                             Cursor csr,
                             String column
    )
    {
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
