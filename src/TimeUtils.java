import org.jetbrains.annotations.NotNull;

public class TimeUtils {
    public static final Integer DAYS_PER_MONTH = 30;
    public static final Integer DAYS_PER_YEAR = 30 * 12;
    public static final Integer MONTHS_PER_YEAR = 12;

    public static @NotNull Integer convertDaysIntoMonths(@NotNull Integer days) {
        return days / DAYS_PER_MONTH;
    }

    public static @NotNull Integer convertDaysIntoYears(@NotNull Integer days) {
        return days / DAYS_PER_YEAR;
    }

    public static @NotNull Integer convertMonthsIntoYears(@NotNull Integer months) {
        return months / MONTHS_PER_YEAR;
    }

    public static @NotNull Integer convertYearsIntoMonths(@NotNull Integer years) {
        return years * MONTHS_PER_YEAR;
    }

    public static @NotNull Integer convertYearsIntoDays(@NotNull Integer years) {
        return years * DAYS_PER_YEAR;
    }

    public static @NotNull Integer convertMonthsIntoDays(@NotNull Integer months) {
        return months * DAYS_PER_MONTH;
    }
}
