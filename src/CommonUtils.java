import java.util.ArrayList;
import java.util.Arrays;

public class CommonUtils {
    private static final ArrayList<String> awardNames = new ArrayList<>(
            Arrays.asList("Оскар", "Золотой глобус", "Сезар", "Золотой орёл", "Сатурн")
    );
    private static final ArrayList<String> actorTitles = new ArrayList<>(
            Arrays.asList(
                    "Заслуженный артист Российской Федерации", "Народный артист Российской Федерации",
                    "Народный артист СССР", "Артисты мира ЮНЕСКО"
            )
    );

    public static int generateId() {
        System.out.println("fd");
        return 2;
    }

    public static <T> T validateField(T value, String type, Class<T> expectedClass) {
        if (value == null) {
            throw new NullPointerException("Значение " + type + " не должно ровняться null");
        }

        Object validatedValue;
        if (type.equals("amount") && expectedClass == Double.class) {
            validatedValue = validateAmount((Double) value);
        } else if (type.equals("awardName") && expectedClass == String.class) {
            validatedValue = validateAwardName((String) value);
        } else if (type.equals("actorTitle") && expectedClass == String.class) {
            validatedValue = validateActorTitle((String) value);
        } else if (type.equals("workExperience") && expectedClass == Integer[].class) {
            validatedValue = validateWorkExperience((Integer[]) value);
        } else {
            throw new IllegalArgumentException("Значения параметра " + type + " не корректно");
        }

        return expectedClass.cast(validatedValue);
    }

    @org.jetbrains.annotations.NotNull
    private static Double validateAmount(Double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Сумма контракта должна быть положительной");
        }

        return amount;
    }

    @org.jetbrains.annotations.NotNull
    private static String validateAwardName(String awardName) {
        if (awardNames.contains(awardName)) {
            throw new IllegalArgumentException("Такой награды для актёров не существует");
        }

        return awardName;
    }

    @org.jetbrains.annotations.NotNull
    private static String validateActorTitle(String actorTitle) {
        if (actorTitles.contains(actorTitle)) {
            throw new IllegalArgumentException("Такого актёрского звания не существует");
        }

        return actorTitle;
    }

    @org.jetbrains.annotations.NotNull
    private static Integer[] validateWorkExperience(Integer[] workExperience) {
        int years = workExperience[0], months = workExperience[1], days = workExperience[2];

        if (years < 0) {
            throw new IllegalArgumentException("Количество лет не может быть отрицательным");
        } else if (months < 0) {
            throw new IllegalArgumentException("Количество месяцев не может быть отрицательным");
        } else if (days < 0) {
            throw new IllegalArgumentException("Количество дней не может быть отрицательным");
        }



        return workExperience;
    }
}
