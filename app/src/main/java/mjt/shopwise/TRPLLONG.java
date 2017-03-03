package mjt.shopwise;

/**
 * TRPLLONG 3 longs
 */
public class TRPLLONG {
    private long long1;
    private long long2;
    private long long3;


    /**
     * Instantiates a new Trpllong.
     */
    public TRPLLONG() {
        setTRPLLONG(0,0,0);
    }

    /**
     * Instantiates a new Trpllong.
     *
     * @param v1 the v 1
     * @param v2 the v 2
     * @param v3 the v 3
     */
    public TRPLLONG(@SuppressWarnings("SameParameterValue") long v1, @SuppressWarnings("SameParameterValue") long v2, @SuppressWarnings("SameParameterValue") long v3) {
        setTRPLLONG(v1,v2,v3);
    }

    /**
     * Sets trpllong.
     *
     * @param v1 the v 1
     * @param v2 the v 2
     * @param v3 the v 3
     */
    public void setTRPLLONG(long v1, long v2, long v3) {
        long1 = v1;
        long2 = v2;
        long3 = v3;
    }

    /**
     * Gets long 1.
     *
     * @return the long 1
     */
    public long getLong1() { return long1;}

    /**
     * Gets long 2.
     *
     * @return the long 2
     */
    public long getLong2() { return long2;}

    /**
     * Gets long 3.
     *
     * @return the long 3
     */
    public long getLong3() { return long3;}
}
