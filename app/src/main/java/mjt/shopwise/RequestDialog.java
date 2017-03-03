package mjt.shopwise;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import java.lang.reflect.Method;


/**
 * RequestDialog - generic Build and handle standard Alert Dialog
 */
@SuppressWarnings("FieldCanBeLocal")
class RequestDialog {
    private AlertDialog.Builder rdialog;
    private Object obj = null;
    private Method posmethod = null;
    private Method negmethod = null;
    private Method neumethod = null;
    public static final String THISCLASS = RequestDialog.class.getSimpleName();
    private static final String LOGTAG = "SW_RD(generic)";
    /**
     * The Cls.
     */
    private Class<?> cls;
    private RequestDialogParameters rdp;

    /**
     * Instantiates a new Request dialog.
     */
    RequestDialog(){
        String logmsg = "Constructing";
        String methodname = "Construct";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
    }

    /**************************************************************************
     * requestDialog - Build an Alretdialog and handle the response by
     * invkoing the respective method, if specified (see notes)
     * <p>
     * Note! null or empty button text will supress the button being
     * displayed.
     * <p>
     * Note! null or empty action will supress a method being called.
     * <p>
     * Note! methods invoked MUST be in the activities class, hence
     * callingclass.
     * <p>
     * Note! values can have 3 longs and 3 ints (i.e. MixTripleLongTripleInt)
     * it is the responsibility of the invoking activity to set
     * the values. Likewise, it is up to the recipient method to
     * correctly handle the received values.
     * <p>
     * Note! values is passed along with the invoking activity to the method as
     * an instance of the RequestDialogParameters Class.
     * RequestDialogParameters has 2 members of type Activity and
     * MixTripleLongTripleInt. Actiivity for the passed activity and
     * MixTripleLongTripleInt for the discretionary values.
     *
     * @param passedactivity     // The invoking activity, will be passed
     * @param callingclass       // The class from which this was invoked
     * @param title              // The title to be displaye by the dialog
     * @param message            // The message to be displayed
     * @param positivebuttontext // Text for the positive/right button
     * @param negativebuttontext // Text for the negative/middle button
     * @param neutralbuttontext  // Text for the neutral/left button
     * @param positiveaction     // Method name invoked for positive button
     * @param negativeaction     // Method name invoked for negative button
     * @param neutralaction      // Method name invoked for neutral button
     * @param values             // data sent to the method (3 longs and 3 ints)
     */
    void requestDialog(Activity passedactivity,
                       String callingclass,
                       String title, String message,
                       String positivebuttontext, String negativebuttontext,
                       String neutralbuttontext,
                       String positiveaction, String negativeaction,
                       String neutralaction,
                       MixTripleLongTripleInt values) {
        String logmsg = "Invoked";
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);

        // Construct the RequestDialogParametes object/insatnce to be sent to
        // the invoked method (if invoked)
        rdp = new RequestDialogParameters(passedactivity, values);

        // build the 3 (up to) potential methods to be invoked
        // Note! if the name given is null or empty then method to invoke
        //          will not be built but will be left as null.
        logmsg = "Building Request Dialog";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        try {
            cls = Class.forName(callingclass);
            obj = passedactivity;
            if (!(positiveaction == null) && positiveaction.length() > 0 ) {
                posmethod =  cls.getDeclaredMethod(positiveaction,
                        RequestDialogParameters.class);
            }
            if (!(negativeaction == null) && negativeaction.length() > 0 ) {
                negmethod = cls.getDeclaredMethod(negativeaction,
                        RequestDialogParameters.class);
            }
            if (!(neutralaction == null) && neutralaction.length() > 0) {
                neumethod = cls.getDeclaredMethod(neutralaction,
                        RequestDialogParameters.class);
            }
        } catch (Exception e) { e.printStackTrace(); }

        // Build the ALertDialog
        rdialog = new AlertDialog.Builder(passedactivity);
        rdialog.setTitle(title);
        rdialog.setMessage(message);
        rdialog.setCancelable(true);
        // Setup the buttons if text is given (i.e. not null or empty)
        // and if the respective method is not null. Note that the
        // neutral button is an exception as it will often, if not always
        // not invoke a method (i.e. it is generally cancel (do nothing))
        // See setupButton method
        if (!(positivebuttontext == null) &&
                (positivebuttontext.length() > 0) &&
                !(posmethod == null)) {
            setupButton(posmethod, this.rdp, AlertDialog.BUTTON_POSITIVE,
                    positivebuttontext);
        }
        if (!(negativebuttontext == null) &&
                (negativebuttontext.length()   > 0) &&
                !(negmethod == null)) {
            setupButton(negmethod, this.rdp, AlertDialog.BUTTON_NEGATIVE,
                    negativebuttontext);
        }
        if (!(neutralbuttontext == null) && neutralbuttontext.length() > 0 ) {
            setupButton(neumethod, this.rdp, AlertDialog.BUTTON_NEUTRAL,
                    neutralbuttontext);
        }
        // Finally show the AlertDialog
        logmsg = "Displaying Built Request Dialog";
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
        rdialog.show();
    }

    /**************************************************************************
     * setupButton - Setup the button onClickListener
     * @param method        // declared mthod to be invoked
     * @param rdp           // the parameters to be passed to the method
     * @param button        // the respective button
     * @param buttontext    // the text for the button
     */
    private void setupButton(final Method method,
                                    final RequestDialogParameters rdp,
                                    int button,
                                    String buttontext) {
        String logmsg = "Setting Up Request Dialog Button=" + buttontext;
        String methodname = new Object(){}.getClass().getEnclosingMethod().getName();
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);

        if ((button == AlertDialog.BUTTON_POSITIVE) ) {
            logmsg = "Adding OnClick Listener for Positive Button=" + buttontext;
            LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
            rdialog.setPositiveButton(buttontext,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            method.invoke(obj, rdp);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            );
        }
        if (button == AlertDialog.BUTTON_NEGATIVE) {
            logmsg = "Adding OnClick Listener for Negative Button=" + buttontext;
            LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
            rdialog.setNegativeButton(buttontext,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            method.invoke(obj, rdp);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            );
        }
        if (button == AlertDialog.BUTTON_NEUTRAL) {
            logmsg = "Adding OnClick Listener for Neutral Button" + buttontext;
            LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,logmsg,THISCLASS,methodname);
            rdialog.setNeutralButton(buttontext,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (!(method == null)) {
                            try {
                                method.invoke(obj, rdp);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
        }
    }
}
