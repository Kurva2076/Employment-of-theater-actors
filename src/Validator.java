import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class Validator {
    private static final List<String> AWARD_NAMES = new ArrayList<>(
            Arrays.asList("Оскар", "Золотой глобус", "Сезар", "Золотой орёл", "Сатурн")
    );
    private static final List<String> ACTOR_TITLES = new ArrayList<>(
            Arrays.asList(
                    "Заслуженный артист Российской Федерации", "Народный артист Российской Федерации",
                    "Народный артист СССР", "Артист мира ЮНЕСКО"
            )
    );
    private static final String NAME_PARTS_DELIMITERS_PATTERN = "[\\s\\-'`]";
    private static final String NAME_DUPLICATED_CHARS = "\\s\\-'`";

    @org.jetbrains.annotations.Contract("null, _, _, _, false -> fail")
    public static <T, N> T validateField(
            N value, String type, Class<N> entranceClass, Class<T> expectedClass, boolean isNullable
    ) {
        if (!isNullable && value == null) {
            throw new NullPointerException("Значение " + type + " не должно ровняться null");
        }

        Object validatedValue;
        if (type.equals("amount") && entranceClass == Double.class) {
            validatedValue = validateAmount((Double) value);
        } else if (type.equals("awardName") && entranceClass == String.class) {
            validatedValue = validateAwardName((String) value);
        } else if (type.equals("actorTitle") && entranceClass == String.class) {
            validatedValue = validateActorTitle((String) value);
        } else if (type.equals("workExperience") && entranceClass == Integer[].class) {
            validatedValue = validateWorkExperience((Integer[]) value);
        } else if (type.equals("surname") && entranceClass == String.class) {
            validatedValue = validateSurname((String) value);
        } else if (type.equals("firstname") && entranceClass == String.class) {
            validatedValue = validateFirstname((String) value);
        } else if (type.equals("patronymic") && entranceClass == String.class) {
            validatedValue = validatePatronymic((String) value);
        } else {
            if (
                    type.equals("workExperience") && entranceClass == WorkExperience.class ||
                            type.equals("contract") && entranceClass == Contract.class
            ) {
                validatedValue = value;
            } else {
                throw new IllegalArgumentException("Значения параметра " + type + " не корректно");
            }
        }

        return expectedClass.cast(validatedValue);
    }

    @org.jetbrains.annotations.Contract("null, _, _, false -> fail")
    public static <T> T validateField(
            T value, String type, Class<T> usedClass, boolean isNullable
    ) {
        return validateField(value, type, usedClass, usedClass, isNullable);
    }

    @org.jetbrains.annotations.Contract("_, _, true -> param1")
    public static <T> @NotNull List<T> validateField(
            @NotNull List<T> list, @NotNull String type, boolean canBeEmpty
    ) {
        if (!canBeEmpty && list.isEmpty()) {
            throw new IllegalArgumentException("Список " + type + " не может быть пустым");
        }

        return list;
    }

    private static Double validateAmount(Double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Сумма контракта должна быть положительной");
        }

        return amount;
    }

    private static String validateAwardName(String awardName) {
        if (!AWARD_NAMES.contains(awardName)) {
            throw new IllegalArgumentException("Такой награды для актёров не существует");
        }

        return awardName;
    }

    private static String validateActorTitle(String actorTitle) {
        if (!ACTOR_TITLES.contains(actorTitle)) {
            throw new IllegalArgumentException("Такого актёрского звания не существует");
        }

        return actorTitle;
    }

    private static Integer validateWorkExperience(Integer[] workExperience) {
        int years = workExperience[0], months = workExperience[1], days = workExperience[2];

        if (years < 0 || months < 0 || days < 0) {
            throw new IllegalArgumentException("Количество лет, месяцев и дней не может быть отрицательным");
        }

        return TimeUtils.convertYearsIntoDays(years) + TimeUtils.convertMonthsIntoDays(months) + days;
    }

    private static String validateSurname(String surname) {
        return validateFullNamePart(surname, "surname");
    }

    private static String validateFirstname(String firstname) {
        return validateFullNamePart(firstname, "firstname");
    }

    private static String validatePatronymic(String patronymic) {
        if (patronymic == null) {
            return null;
        }

        return validateFullNamePart(patronymic, "patronymic");
    }

    private static String validateFullNamePart(String name, String nameType) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Значение " + nameType + " не может быть пустым");
        }

        String preparedName = CommonUtils.removeDuplicatedChars(name, NAME_DUPLICATED_CHARS);
        if (Pattern.compile("[^А-ЯЁа-яё\\s\\-'`]").matcher(preparedName).results().findAny().isPresent()) {
            throw new IllegalArgumentException("Значение " + nameType + " содержит недопустимые символы");
        }
        if (Pattern.compile("^[А-ЯЁа-яё]+(?:[\\s\\-'`][А-ЯЁа-яё]+)*$").matcher(preparedName).results().findAny().isEmpty()) {
            throw new IllegalArgumentException("Значение " + nameType + " не соответствует стандартному виду");
        }

        return CommonUtils.capitalize(preparedName, NAME_PARTS_DELIMITERS_PATTERN);
    }
}
