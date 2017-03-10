package mjt.shopwise;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

@SuppressWarnings({"WeakerAccess", "CanBeFinal"})
class ExternalStoragePermissions {

    @SuppressWarnings("unused")
    public int API_VERSION = Build.VERSION.SDK_INT;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {

            //Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    public static final String THISCLASS = ExternalStoragePermissions.class.getSimpleName();
    private static final String LOGTAG = "SW_ESP";

    public ExternalStoragePermissions() {}
    // Note call this method
    public static void verifyStoragePermissions(Activity activity) {
        String msg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,THISCLASS,methodname);

        int permission = ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if(permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
}
