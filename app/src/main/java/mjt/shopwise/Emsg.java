package mjt.shopwise;

/**
 * Emsg - Class and methods for extended error messaging
 */

class Emsg {
    private boolean error_indicator;
    private int error_number;
    private String error_message;

    public Emsg() {
        this.error_indicator = true;
        this.error_number = 0;
        this.error_message = "";
    }
    Emsg(@SuppressWarnings("SameParameterValue") boolean error_indicator, @SuppressWarnings("SameParameterValue") int error_number, @SuppressWarnings("SameParameterValue") String error_message) {
        this.error_indicator = error_indicator;
        this.error_number = error_number;
        this.error_message = error_message;
    }
    boolean getErrorIndicator() {
        return this.error_indicator;
    }
    @SuppressWarnings("unused")
    public int getErrorNumber() {
        return this.error_number;
    }
    String getErrorMessage() {
        return  this.error_message;
    }
    @SuppressWarnings("unused")
    public void setErrorIndicator(boolean error_indicator) {
        this.error_indicator = error_indicator;
    }
    @SuppressWarnings("unused")
    public void  setErrorNumber(int error_number) {
        this.error_number = error_number;
    }
    @SuppressWarnings("unused")
    public void setErrorMessage(String error_message) {
        this.error_message = error_message;
    }
    public void setAll(@SuppressWarnings("SameParameterValue") boolean error_indicator, int error_number, String error_message) {
        this.error_indicator = error_indicator;
        this.error_number = error_number;
        this.error_message = error_message;
    }
}
