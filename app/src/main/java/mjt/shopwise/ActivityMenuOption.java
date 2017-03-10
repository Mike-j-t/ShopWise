package mjt.shopwise;

/**
 * Activity menu Option
 */
public class ActivityMenuOption {

    private String mMenuOptionName;
    private String mMenuOptionInfo;
    private int mMenuOptionOrder;
    private static final String LOGTAG = "SW_AMO";
    public static final String THISCLASS = ActivityMenuOption.class.getSimpleName();

    /**
     * Instantiates a new Activity menu option.
     *
     * @param menuoptionname  the menuoptionname
     * @param menuoptioninfo  the menuoptioninfo
     * @param menuoptionorder the menuoptionorder
     */
    public ActivityMenuOption(String menuoptionname, String menuoptioninfo, int menuoptionorder) {
        String methodname = "Construct";
        String msg = "Construction Name=" + menuoptionname +
                " Info=" + menuoptioninfo +
                " Order=" + Integer.toString(menuoptionorder);
        LogMsg.LogMsg(LogMsg.LOGTYPE_INFORMATIONAL,LOGTAG,msg,this.getClass().getSimpleName(),methodname);
        this.mMenuOptionName = menuoptionname;
        this.mMenuOptionInfo = menuoptioninfo;
        this.mMenuOptionOrder = menuoptionorder;
    }

    /**
     * Sets menu option name.
     *
     * @param menuoptioname the menuoptioname
     */
    @SuppressWarnings("unused")
    public void setMenuOptionName(String menuoptioname) {
        this.mMenuOptionName = menuoptioname;
    }

    /**
     * Sets menu option info.
     *
     * @param menuoptioninfo the menuoptioninfo
     */
    @SuppressWarnings("unused")
    public void setMenuOptionInfo(String menuoptioninfo) {
        this.mMenuOptionInfo = menuoptioninfo;
    }

    /**
     * Sets menu option order.
     *
     * @param menuoptionorder the menuoptionorder
     */
    @SuppressWarnings("unused")
    public void setMenuOptionOrder(int menuoptionorder) {
        this.mMenuOptionOrder = menuoptionorder;
    }

    /**
     * Get menu option name string.
     *
     * @return the string
     */
    public String getMenuOptionName(){
        return this.mMenuOptionName;
    }

    /**
     * Gets menu option info.
     *
     * @return the menu option info
     */
    public String getmMenuOptionInfo() {
        return this.mMenuOptionInfo;
    }

    /**
     * Gets menu option order.
     *
     * @return the menu option order
     */
    public int getmMenuOptionOrder() {
        return this.mMenuOptionOrder;
    }
}
