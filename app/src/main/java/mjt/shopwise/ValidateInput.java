package mjt.shopwise;

/**
 * validate Input Class for the validation of input
 */

@SuppressWarnings("WeakerAccess")
public class ValidateInput {

    /**
     * validateInteger
     * @param   integertocheck      The integere to check for validity
     * @return  an Emsg object (boolean, int, String)
     */
    public static Emsg validateInteger(String integertocheck) {
        Emsg retmsg = new Emsg(false,0,"");
        try {
            @SuppressWarnings("unused") int givenint = Integer.parseInt(integertocheck);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            retmsg.setAll(true, 1, "Invalid Integer - Must be nnn.");
        }
        return retmsg;
    }


    /**
     * validateInteger within a specified range
     * @param   integertocheck      The integere to check for validity
     * @param   lowest              The lowest acceptable value
     * @param   highest             The highest acceptable value
     * @return  an Emsg object (boolean, int, String)
     */
    public static Emsg validateInteger(String integertocheck,int lowest, int highest) {
        int value = 0;
        Emsg retmsg = validateInteger(integertocheck);
        if (retmsg.getErrorIndicator()) {
            return  retmsg;
        }
        retmsg.setAll(false,0,"");
        //noinspection EmptyCatchBlock
        try {
            value = Integer.parseInt(integertocheck);
        } catch (NumberFormatException a) {
        }
        if (value < lowest || value > highest) {
            retmsg.setAll(true,2,
                    "Invalid Integer - Out of Range (" +
                            Integer.toString(lowest) +
                            " to " +
                            Integer.toString(highest) +
                            ")"
            );
        }
        return retmsg;
    }

    /**
     *
     * @param   monetarytocheck     the String holding the monetary value to check
     *                              for validity
     * @return  an Emsg object (boolean, int, String)
     */
    public static Emsg validateMonetary(String monetarytocheck) {
        Emsg retmsg = new Emsg(false, 0, "");

        String amnts[] = monetarytocheck.split(".");
        if(amnts.length > 1) {
            retmsg.setAll(true, 1, "Invalid Monetary Number - More than 1 decimal point.");
            return retmsg;
        }

        try {
            @SuppressWarnings("unused") Float monetaryfloat = Float.parseFloat(monetarytocheck);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            retmsg.setAll(true,2,"Invalid Monetary Number - Should be nnn.nn or nnn");
            return retmsg;
        }
        return retmsg;
    }

    /**
     *
     * @param datetocheck       the String holding the date to be checked for
     *                          validity.
     * @return                  true if a valid date, esle false.
     */
    public static Emsg validateDate(String datetocheck) {
        Emsg retmsg = new Emsg(false, 0, "");
        // Valid Length Check ie must be a minimum of 8 characters in Length and a maximum of 10
        if(datetocheck.length() < 8 | datetocheck.length() > 10)  {
            retmsg.setAll(true, 1, "Invalid Length (must be 8-10) it was " + datetocheck.length());
            return retmsg;
        }
        String day = "";
        String month = "";
        String year = "";
        int dayasint;
        int monthasint;
        int yearasint;

        int partcount = 0;
        String dateparts[] = datetocheck.split("/");
        for(String cpart: dateparts) {
            partcount++;
            switch (partcount) {
                case 1:
                    day = cpart;
                    break;
                case 2:
                    month = cpart;
                    break;
                case 3:
                    year = cpart;
                    break;
            }
        }
        if(partcount != 3) {
            retmsg.setAll(true, 2, "Invalid Format - " +
                    "Must have 3 parts seperated by 2 /'s (dd/MM/yyyy).");
            return retmsg;
        }
        if(day.length() < 1 | day.length() > 2) {
            retmsg.setAll(true, 3, "Invalid Day - " +
                    "Must be 1 or 2 numerics.");
            return retmsg;
        }
        if(month.length() < 1 | month.length() > 2 ) {
            retmsg.setAll(true, 4, "Invalid Month - " +
                    "Must be 1 or 2 numerics.");
            return retmsg;
        }
        if(year.length() != 4) {
            retmsg.setAll(true, 5, "Invalid Year - " +
                    "Must be 4 numerics.");
            return retmsg;
        }
        try {
            dayasint = Integer.parseInt(day);
        } catch (NumberFormatException e) {
            retmsg.setAll(true, 6, "Invalid Day - " +
                    "Non-numeric(s).");
            return retmsg;
        }
        try {
            monthasint = Integer.parseInt(month);
        } catch (NumberFormatException e) {
            retmsg.setAll(true, 7, "Invalid Month - " +
                    "Non-numeric(s).");
            return retmsg;
        }
        try {
            yearasint = Integer.parseInt(year);
        } catch (NumberFormatException e) {
            retmsg.setAll(true, 8, "Invalid Year - " +
                    "Non-numeric(s).");
            return retmsg;
        }


        int leapyear = 0;
        if(yearasint % 4 == 0) {
            if(yearasint % 100 == 0) {
                if(yearasint % 400 == 0) {
                    leapyear = 1;
                }

            } else {
                leapyear = 1;
            }
        }
        // Definine the Days in Each month adjusting for leap years
        int[] daysinmonth = {31,28+leapyear,31,30,31,30,31,31,30,31,30,31};

        if(monthasint < 1 | monthasint > 12) {
            retmsg.setAll(true, 10, "Invalid Month - " +
                    "Must be 1-12.");
            return retmsg;
        }

        if(dayasint < 1 | dayasint > daysinmonth[(monthasint - 1)] ) {
            retmsg.setAll(true, 9, "Invalid Day - " +
                    "Must be 1-" + daysinmonth[(monthasint - 1)]);
            return retmsg;
        }

        if(yearasint < 1970) {
            retmsg.setAll(true, 11, "Invalid Year - " +
                    "The Year must be 1970 or later.");
            return retmsg;
        }
        return retmsg;
    }
}
