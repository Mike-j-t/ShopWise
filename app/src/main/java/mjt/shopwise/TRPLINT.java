package mjt.shopwise;

/**
 * TRPLINT 3 integers
 */
public class TRPLINT {

        private int int1;
        private int int2;
        private int int3;


    /**
     * Instantiates a new Trplint.
     */
    public TRPLINT() {
            setTRPLINT(0,0,0);
        }

    /**
     * Instantiates a new Trplint.
     *
     * @param v1 the v 1
     * @param v2 the v 2
     * @param v3 the v 3
     */
    @SuppressWarnings("unused")
    public TRPLINT(int v1, int v2, int v3) {
            setTRPLINT(v1,v2,v3);
        }

    /**
     * Sets trplint.
     *
     * @param v1 the v 1
     * @param v2 the v 2
     * @param v3 the v 3
     */
    public void setTRPLINT(int v1, int v2, int v3) {
            int1 = v1;
            int2 = v2;
            int3 = v3;
        }

    /**
     * Gets 1.
     *
     * @return the 1
     */
    public int getint1() { return int1;}

    /**
     * Gets 2.
     *
     * @return the 2
     */
    public int getint2() { return int2;}

    /**
     * Gets 3.
     *
     * @return the 3
     */
    public int getint3() { return int3;}
}
