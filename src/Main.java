import java.io.File;
import java.util.*;

public class Main {
    private static void showActorFunctions() {
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
                System.out.println(workExperience1.getDays());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        Integer[] workExperience2 = new Integer[]{1, 2, 3};
        Double[] workExperience3 = new Double[]{1.0, 2.0, 3.3};
        String[] workExperience4 = new String[]{"1", "2", "3"};
        System.out.println(
                (new WorkExperience(workExperience2[0], workExperience2[1], workExperience2[2])).getDays()
        );
        System.out.println(
                (new WorkExperience(workExperience3[0], workExperience3[1], workExperience3[2])).getDays()
        );
        System.out.println(
                (new WorkExperience(workExperience4[0], workExperience4[1], workExperience4[2])).getDays()
        );
        System.out.println();

        // Разные способы инициализации Contract и примеры валидации
        List<Object> contracts1 = new ArrayList<>(Arrays.asList(
                "-123.0", "+132465.123", -123, 123, -123.0, 123.0, contract, workExperience, null
        ));
        for (Object object : contracts1) {
            try {
                Contract contract1 = new Contract(null, object);
                System.out.println(contract1.getAmount());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println();

        // Разные способы инициализации ActorAward и примеры валидации
        List<Object> actorAwards1 = new ArrayList<>(Arrays.asList("", "Оскар", actorAwards.getFirst(), contract, null));
        for (Object object : actorAwards1) {
            try {
                ActorAward actorAward1 = new ActorAward(null, object);
                System.out.println(actorAward1.getAwardName());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println();

        // Разные способы инициализации ActorTitle и примеры валидации
        List<Object> actorTitles1 = new ArrayList<>(Arrays.asList(
                "", "Артист мира ЮНЕСКО", actorTitles.getFirst(), contract, null
        ));
        for (Object object : actorTitles1) {
            try {
                ActorTitle actorTitle1 = new ActorTitle(null, object);
                System.out.println(actorTitle1.getTitleName());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println();

        // Валидация Actor
        List<String[]> actors = List.of(
                new String[]{"Петров", "Пётр", "Петрович", "9183288772"},
                new String[]{"Петров", "Пётр", null, "9183288772"},
                new String[]{"Д'Арк", "Жанна", null, "9183288772"},
                new String[]{"петрова-сидорова", "Анна-мАрИя", null, "9183288772"},
                new String[]{"Ivanov", "Петр", null, "9183288772"},
                new String[]{"", "Петр", null, "9183288772"},
                new String[]{"Иванов123", "Петр", null, "9183288772"},
                new String[]{String.valueOf(123), "Петр", String.valueOf(123), null, "9183288772"}
        );
        for (String[] fio : actors) {
            try {
                Actor actor1 = new Actor(null, fio[0], fio[1], fio[2], fio[3], workExperience, contract, actorTitles, actorAwards);
                System.out.println(actor1);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println();

        // Примеры парсинга разных форматов
        String string1 = "2,4,12;12000;Артист мира ЮНЕСКО,;;Петров;Пётр;;9183288772";
        List<Object> list1 = List.of("365", "1234", "", "Оскар", "Петров", "Пётр", "Петрович", "9183288772");
        File file = new File("src/data.json");
        String string2 = "src/data.json";
        String string3 = """
                {
                  "surname": "Иванов",
                  "firstname": "Петр",
                  "patronymic": "Сергеевич",
                  "phone": "9183288772",
                  "contract": 1234,
                  "workExperience": [1, 2, 3],
                  "actorAwards": "Оскар",
                  "actorTitles": ["Артист мира ЮНЕСКО"]
                }""";
        Map<?, ?> map1 = Map.of(
                "surname", "Иванов",
                "firstname", "Петр",
                "phone", "9183288772",
                "contract", 1234,
                "workExperience", List.of(1, 2, 3),
                "actorAwards", List.of("Оскар", "Золотой орёл"),
                "actorTitles", "Артист мира ЮНЕСКО"
        );
        String string4 = "surname=Петров;firstname=Пётр;patronymic=;phone=9183288772;workExperience=2,4,12;contract=12000;actorTitles=Артист мира ЮНЕСКО,;actorAwards=Оскар";
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

    private static void showActorRepJsonFunctions() {
        System.out.println("JSON:\n");
        ActorRepJson actorRepJson = new ActorRepJson("src/actors.json");

        String string1 = "2,4,12;12000.32;Артист мира ЮНЕСКО,;;Петров;Пётр;;9183288772";
        List<Object> list1 = List.of("365", "10000000.52", "", "Оскар", "Пупков", "Пуп", "Пупович", "9180888772");
        File file = new File("src/data.json");
        String string2 = "src/data.json";
        String string3 = """
                {
                  "surname": "Кален",
                  "firstname": "Эдвард",
                  "patronymic": "Карлайлович",
                  "phone": "89186482734",
                  "contract": 465312,
                  "workExperience": [1, 2, 3],
                  "actorAwards": "Сатурн",
                  "actorTitles": ["Народный артист СССР"]
                }""";
        Map<?, ?> map1 = Map.of(
                "surname", "Кален",
                "firstname", "Карлайл",
                "phone", "9187283994",
                "contract", 798456,
                "workExperience", List.of(1, 2, 3),
                "actorAwards", List.of("Оскар", "Золотой орёл"),
                "actorTitles", "Артист мира ЮНЕСКО"
        );
        String string4 = "surname=Сергеев;firstname=Сергей;patronymic=;phone=9183288662;workExperience=3,8,7;contract=52;actorTitles=Артист мира ЮНЕСКО,Народный артист СССР;actorAwards=Сезар";

        actorRepJson.writeAll(List.of(new Actor(string1, "str"), new Actor(list1, "list"), new Actor(file, "json")), true);
        actorRepJson.add(new Actor(string2, "jsonpath"));
        actorRepJson.add(new Actor(string3, "json"));
        actorRepJson.add(new Actor(map1, "map"));
        actorRepJson.add(new Actor(string4, "str"));

        System.out.println("Все актёры");
        for (Actor actor : actorRepJson.readAll()) {
            System.out.println(actor);
        }

        System.out.println("Актёр с id = 5");
        System.out.println(actorRepJson.getById(5));

        System.out.println("Актёры со 2-ой страницы по 3 актёра");
        for (PublicActor actor : actorRepJson.getKNShortList(2, 3)) {
            System.out.println(actor);
        }

        System.out.println("Отсортированные актёры по стажу");
        for (Actor actor : actorRepJson.sortBy("workExperience")) {
            System.out.println(actor);
        }

        System.out.println("Замена актёра с id = 3");
        if (actorRepJson.replaceById(3, new Actor(string1, "str"))) {
            System.out.println(actorRepJson.getById(3));
        } else {
            System.out.println("Не удалось произвести замену, так как актёра с id = 3 не существует");
        }

        System.out.println("Замена актёра с id = 10");
        if (actorRepJson.replaceById(10, new Actor(string1, "str"))) {
            System.out.println(actorRepJson.getById(10));
        } else {
            System.out.println("Не удалось произвести замену, так как актёра с id = 10 не существует");
        }

        System.out.println("Количество актёров: " + actorRepJson.getCount());
        if (actorRepJson.deleteById(7)) {
            System.out.println("Количество актёров после удаления: " + actorRepJson.getCount());
        } else {
            System.out.println("Не удалось удалить, так как актёра с id = 7 не существует");
        }
        System.out.println("\n\n");
    }

    private static void showActorRepYamlFunctions() {
        System.out.println("YAML:\n");
        ActorRepYaml actorRepYaml = new ActorRepYaml("src/actors.yaml");

        String string1 = "2,4,12;12000.32;Артист мира ЮНЕСКО,;;Петров;Пётр;;9183288772";
        List<Object> list1 = List.of("365", "10000000.52", "", "Оскар", "Пупков", "Пуп", "Пупович", "9180888772");
        File file = new File("src/data.yaml");
        String string2 = "src/data.yaml";
        String string3 = """
                surname: "Кален"
                firstname: "Эдвард"
                patronymic: "Карлайлович"
                phone: "89186482734"
                contract: 465312
                workExperience: [1, 2, 3]
                actorAwards: "Сатурн"
                actorTitles: ["Народный артист СССР"]
                """;
        Map<?, ?> map1 = Map.of(
                "surname", "Кален",
                "firstname", "Карлайл",
                "phone", "9187283994",
                "contract", 798456,
                "workExperience", List.of(1, 2, 3),
                "actorAwards", List.of("Оскар", "Золотой орёл"),
                "actorTitles", "Артист мира ЮНЕСКО"
        );
        String string4 = "surname=Сергеев;firstname=Сергей;patronymic=;phone=9183288662;workExperience=3,8,7;contract=52;actorTitles=Артист мира ЮНЕСКО,Народный артист СССР;actorAwards=Сезар";

        actorRepYaml.writeAll(List.of(new Actor(string1, "str"), new Actor(list1, "list"), new Actor(file, "yaml")), true);
        actorRepYaml.add(new Actor(string2, "yamlpath"));
        actorRepYaml.add(new Actor(string3, "yaml"));
        actorRepYaml.add(new Actor(map1, "map"));
        actorRepYaml.add(new Actor(string4, "str"));

        System.out.println("Все актёры");
        for (Actor actor : actorRepYaml.readAll()) {
            System.out.println(actor);
        }

        System.out.println("Актёр с id = 5");
        System.out.println(actorRepYaml.getById(5));

        System.out.println("Актёры со 2-ой страницы по 3 актёра");
        for (PublicActor actor : actorRepYaml.getKNShortList(2, 3)) {
            System.out.println(actor);
        }

        System.out.println("Отсортированные актёры по стажу");
        for (Actor actor : actorRepYaml.sortBy("workExperience")) {
            System.out.println(actor);
        }

        System.out.println("Замена актёра с id = 3");
        if (actorRepYaml.replaceById(3, new Actor(string1, "str"))) {
            System.out.println(actorRepYaml.getById(3));
        } else {
            System.out.println("Не удалось произвести замену, так как актёра с id = 3 не существует");
        }

        System.out.println("Замена актёра с id = 10");
        if (actorRepYaml.replaceById(10, new Actor(string1, "str"))) {
            System.out.println(actorRepYaml.getById(10));
        } else {
            System.out.println("Не удалось произвести замену, так как актёра с id = 10 не существует");
        }

        System.out.println("Количество актёров: " + actorRepYaml.getCount());
        if (actorRepYaml.deleteById(7)) {
            System.out.println("Количество актёров после удаления: " + actorRepYaml.getCount());
        } else {
            System.out.println("Не удалось удалить, так как актёра с id = 7 не существует");
        }
        System.out.println("\n\n");
    }

    private static void showActorRepDBFunctions() {
        DatabaseManager dbManager = DatabaseManager.getInstance(
                "jdbc:postgresql://localhost:5432/pis",
                "myuser",
                "1234"
        );

        ActorRepDB db = new ActorRepDB(dbManager);

        Actor actor = new Actor(
                null,
                "Иванов",
                "Иван",
                "Иванович",
                "+79998887766",
                new WorkExperience(100),
                new Contract(50000.0),
                List.of(new ActorTitle("Артист мира ЮНЕСКО"), new ActorTitle("Народный артист СССР")),
                List.of(new ActorAward("Оскар"), new ActorAward("Золотой орёл"))
        );
        String string1 = "2,4,12;12000.32;;;Петров;Пётр;;9183288772";
        List<Object> list1 = List.of("365", "10000000.52", "", "Оскар", "Пупков", "Пуп", "Пупович", "9180888772");
        File file = new File("src/data.yaml");
        String string2 = "src/data.yaml";
        String string3 = """
                surname: "Кален"
                firstname: "Эдвард"
                patronymic: "Карлайлович"
                phone: "89186482734"
                contract: 465312
                workExperience: [1, 2, 3]
                actorAwards: ["Сатурн"]
                actorTitles: ["Народный артист СССР"]
                """;
        Map<?, ?> map1 = Map.of(
                "surname", "Кален",
                "firstname", "Карлайл",
                "phone", "9187283994",
                "contract", 798456,
                "workExperience", List.of(1, 2, 3),
                "actorAwards", List.of("Оскар", "Золотой орёл"),
                "actorTitles", "Артист мира ЮНЕСКО"
        );
        String string4 = "surname=Сергеев;firstname=Сергей;patronymic=;phone=9183288662;workExperience=3,8,7;contract=52;actorTitles=Артист мира ЮНЕСКО,Народный артист СССР;actorAwards=Сезар";

        System.out.println("Добавляем актёров:");
        Actor actor1 = db.add(actor);
        Actor actor2 = db.add(new Actor(string1, "str"));
        Actor actor3 = db.add(new Actor(list1, "list"));
        Actor actor4 = db.add(new Actor(file, "yaml"));
        Actor actor5 = db.add(new Actor(string2, "yamlpath"));
        Actor actor6 = db.add(new Actor(string3, "yaml"));
        Actor actor7 = db.add(new Actor(map1, "map"));
        Actor actor8 = db.add(new Actor(string4, "str"));
        System.out.println(actor1);
        System.out.println(actor2);
        System.out.println(actor3);
        System.out.println(actor4);
        System.out.println(actor5);
        System.out.println(actor6);
        System.out.println(actor7);
        System.out.println(actor8);
        System.out.println("----------------------------");


        System.out.println("Получаем по ID = " + actor2.getActorId());
        Actor extracted = db.getById(actor2.getActorId());
        System.out.println(extracted);
        System.out.println("----------------------------");

        System.out.println("Список публичных актёров (k=1, n=3):");
        for (PublicActor p : db.getKNShortList(1, 3)) {
            System.out.println(p);
        }
        System.out.println("----------------------------");

        System.out.println("Обновляем актёра...");
        Actor updatedActor = new Actor(
                null,
                "Петров",
                "Пётр",
                "Петрович",
                "+77771112233",
                new WorkExperience(200),
                new Contract(99999.0),
                List.of(new ActorTitle("Народный артист Российской Федерации")),
                List.of(new ActorAward("Сатурн"))
        );

        db.update(actor2.getActorId(), updatedActor);

        System.out.println("После обновления:");
        System.out.println(db.getById(actor2.getActorId()));
        System.out.println("----------------------------");

        System.out.println("Количество актёров:");
        System.out.println(db.getCount());
        System.out.println("----------------------------");

        System.out.println("Удаляем актёра с ID " + actor2.getActorId());
        db.delete(actor2.getActorId());
        System.out.println("Теперь getById возвращает:");
        System.out.println(db.getById(actor2.getActorId()));
        System.out.println("----------------------------");

        System.out.println("Количество актёров после удаления:");
        System.out.println(db.getCount());
    }

    public static void main(String[] args) {
//        Main.showActorFunctions();

//        Main.showActorRepJsonFunctions();
//        Main.showActorRepYamlFunctions();

        Main.showActorRepDBFunctions();
    }
}