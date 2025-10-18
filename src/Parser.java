import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class Parser {
    public static <T, N> Map<String, ?> parse(@NotNull T rawData, @NotNull String dataType, @NotNull Class<N> requiredClass) {
        return switch (rawData) {
            case String string when dataType.equals("json") -> parseJsonString(string);
            case File file when dataType.equals("json") -> parseJsonFile(file);
            case String string when dataType.equals("jsonpath") -> parseJsonFile(string);
            case Map<?, ?> map when (dataType.equals("map") || dataType.equals("dict") || dataType.equals("hashmap")) -> parseMap(map);
            case String string when (
                    (dataType.equals("string") || dataType.equals("str")) &&
                            Pattern.compile("(?:[\\w\\s]+=[^=\\n;]*[\\n;])+").matcher(string).results().toList().size() == 1
            ) -> parseMapString(string);
            default -> CommonUtils.makeMap(CommonUtils.getClassFields(requiredClass), parse(rawData, dataType));
        };
    }

    public static <T> List<Object> parse(@NotNull T rawData, @NotNull String dataType) {
        return switch (rawData) {
            case String string when (
                    (dataType.equals("string") || dataType.equals("str")) &&
                            Pattern.compile("(?:[^=\\n;]*[\\n;])+").matcher(string).results().toList().size() == 1
            ) -> parseListString(string);
            case List<?> list when dataType.equals("list") || dataType.equals("arraylist") || dataType.equals("array") -> parseList(list);
            default -> throw new IllegalArgumentException("Указан некорректный тип данных");
        };
    }

    private static Map<String, ?> parseJsonString(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, Object>>(){}.getType();

        return gson.fromJson(json, type);
    }

    private static Map<String, ?> parseJsonFile(File json) {
        return parseJsonString(CommonUtils.readFileLines(json));
    }

    private static Map<String, ?> parseJsonFile(String json) {
        return parseJsonFile(new File(json));
    }

    private static Map<String, ?> parseMap(Map<?, ?> map) {
        Map<String, Object> parsedMap = new HashMap<>();
        if (map.isEmpty()) {
            return parsedMap;
        }

        map.keySet().forEach(key -> parsedMap.put(key.toString(), map.get(key)));

        return parsedMap;
    }

    private static Map<String, ?> parseMapString(String rawData) {
        rawData = CommonUtils.prepareStringToParse(rawData);
        Map<String, Object> map = new HashMap<>();

        for (String pair : rawData.split(";")) {
            String[] pairArray = pair.split("=");
            if (pairArray.length != 1 && pairArray.length != 2) {
                continue;
            }

            String key = pairArray[0].strip();
            if (pairArray.length == 1) {
                map.put(key, "");
                continue;
            }

            String value = (pairArray[1].contains(",")) ? CommonUtils.prepareListData(pairArray[1]) : pairArray[1];

            if (value.contains(",")) {
                List<String> valuesList = new ArrayList<>();
                for (String string : value.split(",")) {
                    valuesList.add(string.strip());
                }

                map.put(key, valuesList);
            } else {
                map.put(key, value);
            }
        }

        return map;
    }

    private static List<Object> parseListString(String rawData) {
        rawData = CommonUtils.prepareStringToParse(rawData);
        List<Object> list = new ArrayList<>();

        for (String value : rawData.split(";")) {
            value = (value.contains(",")) ? CommonUtils.prepareListData(value) : value;

            if (value.contains(",")) {
                List<String> valuesList = new ArrayList<>();
                for (String string : value.split(",")) {
                    valuesList.add(string.strip());
                }

                list.add(valuesList);
            } else {
                list.add(value);
            }
        }

        return list;
    }

    private static List<Object> parseList(List<?> rawData) {
        return List.copyOf(rawData);
    }
}
