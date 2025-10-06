import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtils {
    public static int generateId() {
        int minValue = 1, maxValue = Integer.MAX_VALUE;
        Random random = new Random();

        return random.nextInt(maxValue - minValue) + minValue;
    }

    public static @NotNull String capitalize(@NotNull String string) {
        if (string.isEmpty()) {
            return "";
        }

        return string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase();
    }

    public static @NotNull String capitalize(@NotNull String string, @NotNull String delimiters) {
        Pattern delimitersPattern = Pattern.compile(delimiters, Pattern.CASE_INSENSITIVE|Pattern.MULTILINE);
        Matcher matcher = delimitersPattern.matcher(string);
        String[] stringParts = delimitersPattern.split(string);
        List<String> capitalizedParts = new ArrayList<>();
        int index = 0;

        while (matcher.find()) {
            capitalizedParts.add(capitalize(stringParts[index]) + string.substring(matcher.start(), matcher.end()));
            index++;
        }
        capitalizedParts.add(capitalize(stringParts[index]));

        return String.join("", capitalizedParts);
    }

    public static @NotNull String removeDuplicatedChars(@NotNull String string, @NotNull String chars) {
        return Pattern.compile("([" + chars + "])\\1+")
                .matcher(string)
                .replaceAll(matchResult -> string.substring(matchResult.start(), matchResult.start() + 1));
    }
}
