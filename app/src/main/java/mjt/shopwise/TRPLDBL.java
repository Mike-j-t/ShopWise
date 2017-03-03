package mjt.shopwise;

/**
 * TRPLDBL 3 doubles
 */

@SuppressWarnings("WeakerAccess")
public class TRPLDBL {
    private double dbl1;
    private double dbl2;
    private double dbl3;

    public TRPLDBL() { setTRPLDBL(0,0,0); }

    public void setTRPLDBL(double double1, double double2, double double3) {
        dbl1 = double1;
        dbl2 = double2;
        dbl3 = double3;
    }

    public double getdbl1() {
        return dbl1;
    }

    public double getdbl2() {
        return dbl2;
    }

    public double getdbl3() {
        return dbl3;
    }
}
