package mjt.shopwise;

import android.app.Activity;

/**
 * RequestDialogParameters - Class and methods for passing an Activity
 * (to be started by the method called from the alertdialog) and a
 * MixTripleLonTripleInt (three longs and three ints)
 *
 * This is closely related to The RequestDialog Class.
 */
class RequestDialogParameters {

    private MixTripleLongTripleInt mixtrplli;
    private Activity passedactivity;

    /**
     * Instantiates a new Request dialog parameters.
     *
     * @param passedactivity the passedactivity
     * @param mixtrplli      the mixtrplli
     */
    RequestDialogParameters(Activity passedactivity, MixTripleLongTripleInt mixtrplli) {
        this.passedactivity = passedactivity;
        this.mixtrplli = mixtrplli;
    }

    /**
     * Gets passedactivity.
     *
     * @return the passedactivity
     */
    Activity getPassedactivity() { return this.passedactivity; }

    /**
     * Gets mix triple long triple int.
     *
     * @return the mix triple long triple int
     */
    MixTripleLongTripleInt getMixTripleLongTripleInt() {
        return this.mixtrplli;
    }

    /**
     * Gets long 1.
     *
     * @return the long 1
     */
    long getLong1() {
        return this.mixtrplli.getlong1();
    }

    /**
     * Gets long 2.
     *
     * @return the long 2
     */
    long getLong2() {
        return this.mixtrplli.getlong2();
    }

    /**
     * Gets long 3.
     *
     * @return the long 3
     */
    long getLong3() {
        return this.mixtrplli.getlong3();
    }

    /**
     * Gets int 1.
     *
     * @return the int 1
     */
    int getInt1() {
        return this.mixtrplli.getint1();
    }

    /**
     * Gets int 2.
     *
     * @return the int 2
     */
    int getInt2() {
        return this.mixtrplli.getint2();
    }

    /**
     * Gets int 3.
     *
     * @return the int 3
     */
    int getInt3() {
        return this.mixtrplli.getint3();
    }
}
