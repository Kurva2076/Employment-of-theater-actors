public class TimeUtils {
    public static final Integer DAYS_PER_MONTH = 30;
    public static final Integer DAYS_PER_YEAR = 30 * 12;
    public static final Integer MONTHS_PER_YEAR = 12;

    public static Integer convertDaysIntoMonths(Integer days) {
        return days / DAYS_PER_MONTH;
    }

    public static Integer convertDaysIntoYears(Integer days) {
        return days / DAYS_PER_YEAR;
    }

    public static Integer convertMonthsIntoYears(Integer months) {
        return months / MONTHS_PER_YEAR;
    }

    public static Integer convertYearsIntoMonths(Integer years) {
        return years * MONTHS_PER_YEAR;
    }

    public static Integer convertYearsIntoDays(Integer years) {
        return years * DAYS_PER_YEAR;
    }

    public static Integer convertMonthsIntoDays(Integer months) {
        return months * MONTHS_PER_YEAR;
    }
}
