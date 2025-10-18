import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class TimeUtils {
    public static final Integer DAYS_PER_MONTH = 30;
    public static final Integer DAYS_PER_YEAR = 30 * 12;
    public static final Integer MONTHS_PER_YEAR = 12;
    private static final Map<String, String[]> TIME_UNION_NAMES = new HashMap<>(
            Map.of(
                    "day", new String[]{"день", "дня", "дней"},
                    "month", new String[]{"месяц", "месяца", "месяцев"},
                    "year", new String[]{"год", "года", "лет"}
            )
    );

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

    public static Integer[] getFullDateMark(Integer fullDays) {
        int years = convertDaysIntoYears(fullDays);
        int months = convertDaysIntoMonths(fullDays) - convertYearsIntoMonths(years);
        int days = fullDays - convertYearsIntoDays(years) - convertMonthsIntoDays(months);

        return new Integer[]{years, months, days};
    }

    public static String getTimeUnionName(Integer timeUnion, String type) {
        String timeUnionName;
        if (timeUnion % 10 == 1 && timeUnion % 100 != 11) {
            timeUnionName = TIME_UNION_NAMES.get(type)[0];
        } else if (1 < timeUnion % 10 && timeUnion % 10 < 5 && (timeUnion % 100 < 11 || timeUnion % 100 > 14)) {
            timeUnionName = TIME_UNION_NAMES.get(type)[1];
        } else {
            timeUnionName = TIME_UNION_NAMES.get(type)[2];
        }

        return timeUnionName;
    }
}
