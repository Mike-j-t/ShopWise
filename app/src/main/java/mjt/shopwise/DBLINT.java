package mjt.shopwise;

/**
 * DBLINT two integers
 */
public class DBLINT {
    private int int1;
    private int int2;

    /**
     * Instantiates a new Dblint.
     */
    @SuppressWarnings("unused")
    public DBLINT() {
        int1 = 0;
        int2 = 0;
    }

    /**
     * Instantiates a new Dblint.
     *
     * @param v1 the v 1
     * @param v2 the v 2
     */
    public DBLINT(int v1, int v2) {
        int1 = v1;
        int2 = v2;
    }

    /**
     * Sets dblint.
     *
     * @param v1 the v 1
     * @param v2 the v 2
     */
    public void setDBLINT(int v1, int v2) {
        int1 = v1;
        int2 = v2;
    }

    /**
     * Gets int 1.
     *
     * @return the int 1
     */
    public int getInt1() { return int1; }

    /**
     * Gets int 2.
     *
     * @return the int 2
     */
    public int getInt2() { return int2; }
}
