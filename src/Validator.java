import com.sun.jdi.ClassNotPreparedException;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class Validator {
    public static final String NAME_PARTS_DELIMITERS_PATTERN = "[\\s\\-'`]";
    private static final List<String> ACTOR_AWARDS = new ArrayList<>(
            Arrays.asList("Оскар", "Золотой глобус", "Сезар", "Золотой орёл", "Сатурн")
    );
    private static final List<String> ACTOR_TITLES = new ArrayList<>(
            Arrays.asList(
                    "Заслуженный артист Российской Федерации", "Народный артист Российской Федерации",
                    "Народный артист СССР", "Артист мира ЮНЕСКО"
            )
    );
    private static final String NAME_DUPLICATED_CHARS = "\\s\\-'`";

    @org.jetbrains.annotations.Contract("null, _, _, false -> fail; null, _, _, true -> null")
    public static <T, N> N validateField(T value, String type, Class<N> expectedClass, boolean isNullable) {
        if (!isNullable && value == null) {
            throw new NullPointerException("Значение " + type + " не должно ровняться null");
        }

        if (isNullable && value == null) {
            return null;
        }

        Object validatedValue = switch (type) {
            case "id" -> validateId(value);
            case "amount" -> validateAmount(value);
            case "actorAward" -> validateActorAward(value);
            case "actorTitle" -> validateActorTitle(value);
            case "workExperience" -> validateWorkExperience(value);
            case "surname" -> validateSurname((String) value);
            case "firstname" -> validateFirstname((String) value);
            case "patronymic" -> validatePatronymic((String) value);
            case "phone" -> validatePhone((String) value);
            case "actorTitles", "actorAwards" -> validateList((List<?>) value, true, false);
            case "Contract", "WorkExperience", "ActorTitle", "ActorAward" -> value;
            default -> throw new IllegalArgumentException("Значения параметра " + type + " не корректно");
        };

        try {
            return expectedClass.cast(validatedValue);
        } catch (ClassCastException _) {
            throw new RuntimeException("Поле " + type + " не может приводиться к типу " + expectedClass);
        }
    }

    private static Integer validateId(Object id) {
        return switch (id) {
            case Integer integer -> validateID(integer);
            case Double iDouble -> validateID(iDouble);
            case Long iLong -> validateID(iLong);
            default -> throw new ClassNotPreparedException("Класс объекта не соответствует возможным");
        };
    }

    private static Integer validateID(Integer id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Id должен быть больше 0");
        }

        return id;
    }

    private static Integer validateID(Double id) {
        return validateID(id.intValue());
    }

    private static Integer validateID(Long id) {
        return validateID(id.intValue());
    }

    public static @NotNull Object[] validateArray(@NotNull Object[] list, boolean canBeEmpty, boolean canContainNull) {
        if (!canBeEmpty && list.length == 0) {
            throw new IllegalArgumentException("Список/массив не может быть пустым");
        } else if (!canContainNull && CommonUtils.containNull(list)) {
            throw new IllegalArgumentException("Список/массив не может содержать null");
        }

        return list;
    }

    public static @NotNull List<?> validateList(@NotNull List<?> list, boolean canBeEmpty, boolean canContainNull) {
        Object[] validatedArray = validateArray(list.toArray(), canBeEmpty, canContainNull);

        return List.of(validatedArray);
    }

    public static @NotNull String validateNumber(@NotNull String number, String numberType) {
        if (number.isEmpty()) {
            return "0";
        }

        String preparedNumber = CommonUtils.removeDuplicatedChars(
                CommonUtils.removeChars(number.strip(), "\\+\\_\\ "), "\\.\\-"
        );
        if (Pattern.compile("[^\\d\\-.]").matcher(preparedNumber).results().findAny().isPresent()) {
            throw new IllegalArgumentException("Значение " + numberType + " содержит недопустимые символы");
        } else if (Pattern.compile("^-?\\d+(?:\\.\\d+)?$").matcher(preparedNumber).results().findAny().isEmpty()) {
            throw new NumberFormatException("Значение " + numberType + " заданно в некорректном формате");
        }

        return preparedNumber;
    }

    public static @NotNull String validateFullNamePart(@NotNull String name, String nameType) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Значение " + nameType + " не может быть пустым");
        }

        String preparedName = CommonUtils.removeDuplicatedChars(name.strip(), NAME_DUPLICATED_CHARS);
        if (Pattern.compile("[^А-ЯЁа-яё\\s\\-'`]").matcher(preparedName).results().findAny().isPresent()) {
            throw new IllegalArgumentException("Значение " + nameType + " содержит недопустимые символы");
        } else if (Pattern.compile("^[А-ЯЁа-яё]+(?:[\\s\\-'`][А-ЯЁа-яё]+)*$").matcher(preparedName).results().findAny().isEmpty()) {
            throw new IllegalArgumentException("Значение " + nameType + " не соответствует стандартному виду");
        }

        return CommonUtils.capitalize(preparedName, NAME_PARTS_DELIMITERS_PATTERN);
    }

    private static Double validateAmount(Object amount) {
        return switch (amount) {
            case String string -> validateAmount(string);
            case Integer integer -> validateAmount(integer);
            case Double aDouble -> validateAmount(aDouble);
            case Contract contract -> contract.getAmount();
            default -> throw new ClassNotPreparedException("Класс объекта не соответствует возможным");
        };
    }

    private static Double validateAmount(String amount) {
        return validateAmount(Double.valueOf(validateNumber(amount, "amount")));
    }

    private static Double validateAmount(Integer amount) {
        return validateAmount(amount.doubleValue());
    }

    private static Double validateAmount(Double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Сумма контракта должна быть положительной");
        }

        return amount;
    }

    private static String validateActorAward(Object award) {
        return switch (award) {
            case String string -> validateActorAward(string);
            case ActorAward actorAward -> actorAward.getAwardName();
            default -> throw new ClassNotPreparedException("Класс объекта не соответствует возможным");
        };
    }

    private static String validateActorAward(String actorAward) {
        if (!ACTOR_AWARDS.contains(actorAward)) {
            throw new IllegalArgumentException("Такой награды для актёров не существует");
        }

        return actorAward;
    }

    private static String validateActorTitle(Object title) {
        return switch (title) {
            case String string -> validateActorTitle(string);
            case ActorTitle actorTitle -> actorTitle.getTitleName();
            default -> throw new ClassNotPreparedException("Класс объекта не соответствует возможным");
        };
    }

    private static String validateActorTitle(String actorTitle) {
        if (!ACTOR_TITLES.contains(actorTitle)) {
            throw new IllegalArgumentException("Такого актёрского звания не существует");
        }

        return actorTitle;
    }

    private static Integer validateWorkExperience(Object object) {
        return switch (object) {
            case WorkExperience workExperience -> workExperience.getDays();
            case Integer integer -> validateWorkExperience(integer);
            case Double aDouble -> validateWorkExperience(aDouble);
            case String string -> validateWorkExperience(string);
            case Integer[] integers -> validateWorkExperience((Integer[]) validateArray(integers, false, false));
            case Double[] doubles -> validateWorkExperience((Double[]) validateArray(doubles, false, false));
            case String[] strings -> validateWorkExperience((String[]) validateArray(strings, false, false));
            case List<?> list -> validateWorkExperience(validateList(list, false, false));
            default -> throw new ClassNotPreparedException("Класс объекта не соответствует возможным");
        };
    }

    private static Integer validateWorkExperience(Integer workExperience) {
        if (workExperience < 0) {
            throw new IllegalArgumentException("Стаж не может быть отрицательным числом");
        }

        return workExperience;
    }

    private static Integer validateWorkExperience(Double workExperience) {
        return validateWorkExperience(workExperience.intValue());
    }

    private static Integer validateWorkExperience(String workExperience) {
        return validateWorkExperience(CommonUtils.convertNumberToInteger(validateNumber(workExperience, "workExperience")));
    }

    private static Integer validateWorkExperience(Integer[] workExperience) {
        if (workExperience.length != 3) {
            throw new IllegalArgumentException("В массиве должно содержаться ровно 3 элемента (количество лет, месяцев, дней)");
        }

        int years = workExperience[0], months = workExperience[1], days = workExperience[2];

        if (years < 0 || months < 0 || days < 0) {
            throw new IllegalArgumentException("Количество лет, месяцев и дней не может быть отрицательным");
        }

        return TimeUtils.convertYearsIntoDays(years) + TimeUtils.convertMonthsIntoDays(months) + days;
    }

    private static Integer validateWorkExperience(Double[] workExperience) {
        return validateWorkExperience(CommonUtils.casteInnerClass(workExperience, Integer.class).toArray(new Integer[0]));
    }

    private static Integer validateWorkExperience(String[] workExperience) {
        List<Integer> list = new ArrayList<>();
        for (String string : workExperience) {
            list.add(CommonUtils.convertNumberToInteger(validateNumber(string, "workExperience element")));
        }
        return validateWorkExperience(list.toArray(new Integer[0]));
    }

    private static Integer validateWorkExperience(List<?> workExperience) {
        return validateWorkExperience(CommonUtils.convertListToArray(workExperience));
    }

    private static String validateSurname(String surname) {
        return validateFullNamePart(surname, "surname");
    }

    private static String validateFirstname(String firstname) {
        return validateFullNamePart(firstname, "firstname");
    }

    private static String validatePatronymic(String patronymic) {
        if (patronymic == null || patronymic.isBlank()) {
            return null;
        }

        return validateFullNamePart(patronymic, "patronymic");
    }

    public static String validatePhone(String phone) {
        phone = CommonUtils.removeChars(phone, "- ()+");
        if (phone.isBlank()) {
            throw new IllegalArgumentException("Телефон не может быть пустой строкой");
        }
        if (Pattern.compile("\\D").matcher(phone).find()) {
            throw new IllegalArgumentException("Телефон содержит недопустимые символы");
        }
        if (!Pattern.compile("^[78]?\\d{10}$").matcher(phone).find()) {
            throw new IllegalArgumentException("Формат телефона не соответствует");
        }

        return (phone.length() == 10) ? phone : phone.substring(1);
    }
}
