package mjt.shopwise;

/**
 * DBLLONG two longs
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class DBLLONG {
    private long long1;
    private long long2;

    /**
     * Instantiates a new Dbllong.
     */
    public DBLLONG() {
        long1 = 0;
        long2 = 0;
    }

    /**
     * Instantiates a new Dbllong.
     *
     * @param v1 the v 1
     * @param v2 the v 2
     */
    public DBLLONG(int v1, int v2) {
        long1 = v1;
        long2 = v2;
    }

    /**
     * Sets dblint.
     *
     * @param v1 the v 1
     * @param v2 the v 2
     */
    public void setDBLINT(int v1, int v2) {
        long1 = v1;
        long2 = v2;
    }

    /**
     * Gets long 1.
     *
     * @return the long 1
     */
    public long getLong1() { return long1; }

    /**
     * Gets long 2.
     *
     * @return the long 2
     */
    public long getLong2() { return long2; }
}
