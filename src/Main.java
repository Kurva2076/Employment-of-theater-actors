import java.util.ArrayList;
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
    }
}