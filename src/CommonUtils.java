import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    public static @NotNull String removeChars(@NotNull String string, @NotNull String chars) {
        return Pattern.compile("([" + chars + "])+").matcher(string).replaceAll(_ -> "");
    }

    public static @NotNull String readFileLines(@NotNull File file) {
        FileReader fileReader;

        try {
            fileReader = new FileReader(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        StringBuilder string = new StringBuilder();
        try {
            int integerChar;

            while ((integerChar = fileReader.read()) != -1) {
                string.append((char) integerChar);
            }

            fileReader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return string.toString();
    }

    public static @NotNull String prepareStringToParse(@NotNull String rawSting) {
        return rawSting.replace("\n", ";");
    }

    public static @NotNull String prepareListData(@NotNull String listData) {
        return listData.replace("[", "").replace("]", "").strip();
    }

    public static <T> @NotNull List<String> getClassFields(@NotNull Class<T> tClass) {
        List<String> fieldNames = new ArrayList<>();
        for (Field field : tClass.getDeclaredFields()) {
            if (!field.getName().toLowerCase().endsWith("id")) {
                fieldNames.add(field.getName());
            }
        }
        return fieldNames;
    }

    public static @NotNull Map<String, Object> makeMap(@NotNull List<String> keys, @NotNull List<?> values) {
        System.out.println(keys);
        System.out.println(values);
        if (keys.size() != values.size()) {
            throw new IllegalArgumentException("Списки имеют разный размер");
        }

        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < keys.size(); i++) {
            map.put(keys.get(i), values.get(i));
        }

        return map;
    }

    public static <T> @NotNull Object convertListToArray(@NotNull List<T> list) {
        if (list.isEmpty()) {
            throw new IllegalArgumentException("Список для преобразования в массив не может быть пустым");
        }
        Object firstElement = list.getFirst();
        Class<?> elementClass = firstElement.getClass();

        Object array = java.lang.reflect.Array.newInstance(elementClass, list.size());

        for (int i = 0; i < list.size(); i++) {
            Object element = list.get(i);
            if (element != null && !elementClass.isInstance(element)) {
                throw new IllegalArgumentException(
                        "Элемент " + element + " не соответствует типу " + elementClass.getSimpleName()
                );
            }
            java.lang.reflect.Array.set(array, i, element);
        }

        return array;
    }

    public static <T> @NotNull List<T> casteInnerClass(Object[] originalArray, Class<T> castedClass) {
        List<T> castedList = new ArrayList<>();
        if (castedClass == Integer.class && originalArray instanceof String[]) {
            for (Object o : originalArray) {
                castedList.add(castedClass.cast(convertNumberToInteger((String) o)));
            }
        } else if (castedClass == Double.class && originalArray instanceof String[]) {
            for (Object o : originalArray) {
                castedList.add(castedClass.cast(Double.valueOf((String) o)));
            }
        } else if (castedClass == Integer.class && originalArray instanceof Double[]) {
            for (Object o : originalArray) {
                castedList.add(castedClass.cast(((Double) o).intValue()));
            }
        } else if (castedClass == ActorTitle.class && !(originalArray instanceof ActorTitle[])) {
            for (Object o : originalArray) {
                castedList.add(castedClass.cast(new ActorTitle(o)));
            }
        } else if (castedClass == ActorAward.class && !(originalArray instanceof ActorAward[])) {
            for (Object o : originalArray) {
                castedList.add(castedClass.cast(new ActorAward(o)));
            }
        } else {
            for (Object o : originalArray) {
                castedList.add(castedClass.cast(o));
            }
        }

        return castedList;
    }

    public static @NotNull <T> List<T> casteInnerClass(List<?> originalList, Class<T> castedClass) {
        return casteInnerClass(originalList.toArray(), castedClass);
    }

    public static Integer convertNumberToInteger(String number) {
        int dotIndex = number.indexOf('.');

        String integerNumber;
        if (dotIndex == -1) {
            integerNumber = number;
        } else {
            integerNumber = number.substring(0, dotIndex);
        }

        return Integer.valueOf(integerNumber);
    }

    public static boolean containNull(Object[] objects) {
        for (Object object : objects) {
            if (object == null) {
                return true;
            }
        }

        return false;
    }

    public static boolean containNull(List<?> objects) {
        return containNull(objects.toArray());
    }
}
