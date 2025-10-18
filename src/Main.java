import java.io.File;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        WorkExperience workExperience = new WorkExperience(2, 4, 12);
        Contract contract = new Contract(123456);
        ActorTitle actorTitle = new ActorTitle("Артист мира ЮНЕСКО");
        ActorAward actorAward = new ActorAward("Оскар");

        List<ActorTitle> actorTitles = new ArrayList<>();
        List<ActorAward> actorAwards = new ArrayList<>();
        actorTitles.add(actorTitle);
        actorAwards.add(actorAward);

        // Разные способы инициализации WorkExperience и примеры валидации
        List<Object> workExperiences1 = new ArrayList<>(
                Arrays.asList(
                        "-123.0", "+132465.123", -123, 123, "asd", 123.31,
                        new ArrayList<>(Arrays.asList(12, 13, 2)),
                        new ArrayList<>(Arrays.asList(12.13, 13.13, 2.13)),
                        new ArrayList<>(Arrays.asList("12", "13", "2")),
                        new ArrayList<>(Arrays.asList("12", "13", "2", "3")),
                        new ArrayList<>(Arrays.asList("-12", "13", "2")),
                        new Integer[]{10, 10, 10},
                        new Double[]{10.10, 10.10, 10.10},
                        new String[]{"10", "10", "10"},
                        workExperience, contract, null
                )
        );
        for (Object object : workExperiences1) {
            try {
                WorkExperience workExperience1 = new WorkExperience(object);
                System.out.println(workExperience1.getWorkExperience());
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        Integer[] workExperience2 = new Integer[]{1, 2, 3};
        Double[] workExperience3 = new Double[]{1.0, 2.0, 3.3};
        String[] workExperience4 = new String[]{"1", "2", "3"};
        System.out.println(
                (new WorkExperience(workExperience2[0], workExperience2[1], workExperience2[2])).getWorkExperience()
        );
        System.out.println(
                (new WorkExperience(workExperience3[0], workExperience3[1], workExperience3[2])).getWorkExperience()
        );
        System.out.println(
                (new WorkExperience(workExperience4[0], workExperience4[1], workExperience4[2])).getWorkExperience()
        );
        System.out.println();

        // Разные способы инициализации Contract и примеры валидации
        List<Object> contracts1 = new ArrayList<>(Arrays.asList(
                "-123.0", "+132465.123", -123, 123, -123.0, 123.0, contract, workExperience, null
        ));
        for (Object object : contracts1) {
            try {
                Contract contract1 = new Contract(object);
                System.out.println(contract1.getAmount());
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        System.out.println();

        // Разные способы инициализации ActorAward и примеры валидации
        List<Object> actorAwards1 = new ArrayList<>(Arrays.asList("", "Оскар", actorAwards.getFirst(), contract, null));
        for (Object object : actorAwards1) {
            try {
                ActorAward actorAward1 = new ActorAward(object);
                System.out.println(actorAward1.getAwardName());
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        System.out.println();

        // Разные способы инициализации ActorTitle и примеры валидации
        List<Object> actorTitles1 = new ArrayList<>(Arrays.asList(
                "", "Артист мира ЮНЕСКО", actorTitles.getFirst(), contract, null
        ));
        for (Object object : actorTitles1) {
            try {
                ActorTitle actorTitle1 = new ActorTitle(object);
                System.out.println(actorTitle1.getTitleName());
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        System.out.println();

        // Валидация Actor
        List<String[]> actors = List.of(
                new String[]{"Петров", "Пётр", "Петрович"},
                new String[]{"Петров", "Пётр", null},
                new String[]{"Д'Арк", "Жанна", null},
                new String[]{"петрова-сидорова", "Анна", null},
                new String[]{"Ivanov", "Петр", null},
                new String[]{"", "Петр", null},
                new String[]{"Иванов123", "Петр", null},
                new String[]{String.valueOf(123), "Петр", String.valueOf(123), null}
        );
        for (String[] fio : actors) {
            try {
                Actor actor1 = new Actor(fio[0], fio[1], fio[2], workExperience, contract, actorTitles, actorAwards);
                System.out.println(actor1);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        System.out.println();

        // Примеры парсинга разных форматов
        String string1 = "Петров;Пётр;;2,4,12;12000;Артист мира ЮНЕСКО,;Оскар";
        List<Object> list1 = List.of("Петров", "Пётр", "Петрович", "365", "1234", "Артист мира ЮНЕСКО", "Оскар");
        File file = new File("C:\\Users\\kotpl\\OneDrive\\Рабочий стол\\Уник\\4-ый курс\\ПИС\\Employment-of-theater-actors\\src\\data.json");
        String string2 = "C:\\Users\\kotpl\\OneDrive\\Рабочий стол\\Уник\\4-ый курс\\ПИС\\Employment-of-theater-actors\\src\\data.json";
        String string3 = "{\n" +
                "  \"surname\": \"Иванов\",\n" +
                "  \"firstname\": \"Петр\",\n" +
                "  \"patronymic\": \"Сергеевич\",\n" +
                "  \"contract\": 1234,\n" +
                "  \"workExperience\": [1, 2, 3],\n" +
                "  \"actorAwards\": \"Оскар\",\n" +
                "  \"actorTitles\": [\"Артист мира ЮНЕСКО\"]\n" +
                "}";
        Map<?, ?> map1 = Map.of(
                "surname", "Иванов",
                "firstname", "Петр",
                "contract", 1234,
                "workExperience", List.of(1, 2, 3),
                "actorAwards", "Оскар",
                "actorTitles", "Артист мира ЮНЕСКО"
        );
        String string4 = "surname=Петров;firstname=Пётр;patronymic=;workExperience=2,4,12;contract=12000;actorTitles=Артист мира ЮНЕСКО,;actorAwards=Оскар";
        System.out.println(new Actor(string1, "str"));
        System.out.println(new Actor(list1, "list"));
        System.out.println(new Actor(file, "json"));
        System.out.println(new Actor(string2, "jsonpath"));
        System.out.println(new Actor(string3, "json"));
        System.out.println(new Actor(map1, "map"));
        System.out.println(new Actor(string4, "str"));

        // Вывод короткой версии и сравнение двух актёров
        Actor actor1 = new Actor(string1, "str");
        System.out.println(actor1.shortString());
        System.out.println(actor1.equals(new Actor(string1, "str"))); // Ожидаем true
        System.out.println(actor1.equals(new Actor(string3, "json"))); // Ожидаем false
    }
}