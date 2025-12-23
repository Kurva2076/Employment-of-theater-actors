package utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Actor;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.lang.reflect.Type;
import java.util.*;
import java.util.regex.Pattern;

public class Parser {
    private static final Map<Class<?>, List<String>> EXCLUDED_FIELDS = Map.of(
            Actor.class, List.of("actorId", "initials")
    );

    public static <T, N> Map<String, ?> parse(
            @NotNull T rawData,
            @NotNull String dataType,
            @NotNull Class<N> requiredClass
    ) {

        if (rawData instanceof String string) {

            if ("json".equals(dataType)) {
                return parseJsonString(string);
            }

            if ("jsonpath".equals(dataType)) {
                return parseJsonFile(string);
            }

            if ("yaml".equals(dataType)) {
                return parseYamlString(string);
            }

            if ("yamlpath".equals(dataType)) {
                return parseYamlFile(string);
            }

            if ("string".equals(dataType) || "str".equals(dataType)) {
                boolean matches =
                        Pattern.compile("(?:[\\w\\s]+=[^=\\n;]*[\\n;])+")
                                .matcher(string)
                                .results()
                                .toList()
                                .size() == 1;

                if (matches) {
                    return parseMapString(string);
                }
            }
        }

        if (rawData instanceof File file) {

            if ("json".equals(dataType)) {
                return parseJsonFile(file);
            }

            if ("yaml".equals(dataType)) {
                return parseYamlFile(file);
            }
        }

        if (rawData instanceof Map<?, ?> map) {
            if ("map".equals(dataType)
                    || "dict".equals(dataType)
                    || "hashmap".equals(dataType)) {
                return parseMap(map);
            }
        }

        /* ===== default-ветка ===== */

        List<String> fields = new ArrayList<>();
        Class<?> providedClass = requiredClass;

        while (providedClass != Object.class) {
            fields.addAll(
                    CommonUtils.getClassFields(
                            providedClass,
                            EXCLUDED_FIELDS.get(requiredClass)
                    )
            );
            providedClass = providedClass.getSuperclass();
        }

        return CommonUtils.makeMap(fields, parse(rawData, dataType));
    }

    public static <T> List<Object> parse(
            @NotNull T rawData,
            @NotNull String dataType
    ) {

        if (rawData instanceof String string) {

            if ("string".equals(dataType) || "str".equals(dataType)) {

                boolean matches =
                        Pattern.compile("(?:[^=\\n;]*[\\n;])+")
                                .matcher(string)
                                .results()
                                .toList()
                                .size() == 1;

                if (matches) {
                    return parseListString(string);
                }
            }
        }

        if (rawData instanceof List<?> list) {

            if ("list".equals(dataType)
                    || "arraylist".equals(dataType)
                    || "array".equals(dataType)) {
                return parseList(list);
            }
        }

        throw new IllegalArgumentException("Указан некорректный тип данных");
    }

    private static Map<String, ?> parseJsonString(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, Object>>() {
        }.getType();

        return gson.fromJson(json, type);
    }

    private static Map<String, ?> parseJsonFile(File json) {
        return parseJsonString(CommonUtils.readFileLines(json));
    }

    private static Map<String, ?> parseJsonFile(String json) {
        return parseJsonFile(new File(json));
    }

    @SuppressWarnings("unchecked")
    private static Map<String, ?> parseYamlString(String yamlString) {
        Yaml yaml = new Yaml();
        Object data = yaml.load(yamlString);

        if (data instanceof Map<?, ?> map) {
            return (Map<String, ?>) map;
        }

        return Collections.emptyMap();
    }

    private static Map<String, ?> parseYamlFile(File yaml) {
        return parseYamlString(CommonUtils.readFileLines(yaml));
    }

    private static Map<String, ?> parseYamlFile(String yaml) {
        return parseYamlFile(new File(yaml));
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
