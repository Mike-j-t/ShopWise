package mjt.shopwise;

/**
 * Created by Mike092015 on 6/12/2016.
 */
public class ActivityMenuOption {

    private String mMenuOptionName;
    private String mMenuOptionInfo;
    private int mMenuOptionOrder;

    /**
     * Instantiates a new Activity menu option.
     *
     * @param menuoptionname  the menuoptionname
     * @param menuoptioninfo  the menuoptioninfo
     * @param menuoptionorder the menuoptionorder
     */
    public ActivityMenuOption(String menuoptionname, String menuoptioninfo, int menuoptionorder) {
        this.mMenuOptionName = menuoptionname;
        this.mMenuOptionInfo = menuoptioninfo;
        this.mMenuOptionOrder = menuoptionorder;
    }

    /**
     * Sets menu option name.
     *
     * @param menuoptioname the menuoptioname
     */
    public void setMenuOptionName(String menuoptioname) {
        this.mMenuOptionName = menuoptioname;
    }

    /**
     * Sets menu option info.
     *
     * @param menuoptioninfo the menuoptioninfo
     */
    public void setMenuOptionInfo(String menuoptioninfo) {
        this.mMenuOptionInfo = menuoptioninfo;
    }

    /**
     * Sets menu option order.
     *
     * @param menuoptionorder the menuoptionorder
     */
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
