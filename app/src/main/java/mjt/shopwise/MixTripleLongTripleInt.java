package mjt.shopwise;

/**
 * MixTripleLongTripleInt A triple integer and a triple long
 */
@SuppressWarnings({"WeakerAccess", "CanBeFinal"})
public class MixTripleLongTripleInt {

    private TRPLINT trint;
    private TRPLLONG trlong;

    /**
     * Instantiates a new Mix triple long triple int.
     */
    public MixTripleLongTripleInt() {
        trint = new TRPLINT();
        trlong = new TRPLLONG();
    }

    /**
     * Sets mixtrpplongint.
     *
     * @param long1 the long 1
     * @param long2 the long 2
     * @param long3 the long 3
     * @param int1  the int 1
     * @param int2  the int 2
     * @param int3  the int 3
     */
    public void setMIXTRPPLONGINT(long long1, long long2, long long3, int int1, @SuppressWarnings("SameParameterValue") int int2, @SuppressWarnings("SameParameterValue") int int3) {
        trlong.setTRPLLONG(long1, long2, long3);
        trint.setTRPLINT(int1, int2, int3);
    }

    /**
     * Gets 1.
     *
     * @return the 1
     */
    public int getint1() { return trint.getint1();}

    /**
     * Gets 2.
     *
     * @return the 2
     */
    public int getint2() { return trint.getint2();}

    /**
     * Gets 3.
     *
     * @return the 3
     */
    public int getint3() { return trint.getint3();}

    /**
     * Gets 1.
     *
     * @return the 1
     */
    public long getlong1() { return trlong.getLong1();}

    /**
     * Gets 2.
     *
     * @return the 2
     */
    public long getlong2() { return trlong.getLong2();}

    /**
     * Gets 3.
     *
     * @return the 3
     */
    public long getlong3() { return trlong.getLong3();}

    /**
     * Gets trplint.
     *
     * @return the trplint
     */
    @SuppressWarnings("unused")
    public TRPLINT getTRPLINT() { return trint;}

    /**
     * Gets trpllong.
     *
     * @return the trpllong
     */
    @SuppressWarnings("unused")
    public TRPLLONG getTRPLLONG() {return  trlong;}
}
