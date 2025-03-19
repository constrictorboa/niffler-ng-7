package guru.qa.niffler.model;

public enum MonthEnum {
    January, February, March, April, May, June, July, August, September, October, November, December;

    /**
     * @param num 1 - January, 12 - December
     * @return
     */
    public static String getMonthName(int num) {
        return MonthEnum.values()[num - 1].name();
    }

    public static String getMonthShortName(int num) {
        return getMonthName(num).substring(0, 3);
    }
}
