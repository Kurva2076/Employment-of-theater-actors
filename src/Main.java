import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

        Actor actor1 = new Actor("Петров", "Пётр", "Петрович", workExperience, contract, actorTitles, actorAwards);
        Actor actor2 = new Actor("Петров", "Петрович", workExperience, contract, actorTitles, actorAwards);

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
    }
}