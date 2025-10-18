import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Actor {
    private final Integer actorId;
    private String surname;
    private String firstname;
    private String patronymic;
    private WorkExperience workExperience;
    private Contract contract;
    private List<ActorTitle> actorTitles;
    private List<ActorAward> actorAwards;

    public Actor(String surname, String firstname, String patronymic, WorkExperience workExperience,
                 Contract contract, List<?> actorTitles, List<?> actorAwards) {
        this.actorId = CommonUtils.generateId();
        this.surname = Validator.validateField(surname, "surname", String.class, false);
        this.firstname = Validator.validateField(firstname, "firstname", String.class, false);
        this.patronymic = Validator.validateField(patronymic, "patronymic", String.class, true);
        this.workExperience = Validator.validateField(workExperience, "WorkExperience", WorkExperience.class, false);
        this.contract = Validator.validateField(contract, "Contract", Contract.class, false);
        this.actorTitles = CommonUtils.casteInnerClass(
                Validator.validateField(actorTitles, "actorTitles", List.class, false), ActorTitle.class
        );
        this.actorAwards = CommonUtils.casteInnerClass(
                Validator.validateField(actorAwards, "actorAwards", List.class, false), ActorAward.class
        );
    }

    public Actor(String surname, String firstname, WorkExperience workExperience, Contract contract,
                 List<?> actorTitles, List<?> actorAwards) {
        this(surname, firstname, null, workExperience, contract, actorTitles, actorAwards);
    }

    public Actor(Map<String, ?> map) {
        this(
                (String) map.get("surname"),
                (String) map.get("firstname"),
                (String) map.get("patronymic"),
                new WorkExperience(map.get("workExperience")),
                new Contract(map.get("contract")),
                (map.get("actorTitles") == null) ? new ArrayList<>() : (
                        (map.get("actorTitles") instanceof String) ? List.of(map.get("actorTitles")) :
                        (List<?>) map.get("actorTitles")
                ),
                (map.get("actorAwards") == null) ? new ArrayList<>() : (
                        (map.get("actorAwards") instanceof String) ? List.of(map.get("actorAwards")) :
                        (List<?>) map.get("actorAwards")
                )
        );
    }

    public Actor(Object object, String type) {
        this(Parser.parse(object, type, Actor.class));
    }

    public Actor(Actor actor) {
        this(
                actor.getSurname(), actor.getFirstname(), actor.getPatronymic(), actor.getWorkExperience(),
                actor.getContract(), actor.getActorTitles(), actor.getActorAwards()
        );
    }

    public Integer getActorId() {
        return actorId;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = Validator.validateField(surname, "surname", String.class, false);
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = Validator.validateField(firstname, "firstname", String.class, false);
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = Validator.validateField(patronymic, "patronymic", String.class, true);
    }

    public WorkExperience getWorkExperience() {
        return workExperience;
    }

    public void setWorkExperience(WorkExperience workExperience) {
        this.workExperience = Validator.validateField(workExperience, "WorkExperience", WorkExperience.class, false);
    }

    public void setWorkExperience(Object workExperience) {
        this.workExperience = new WorkExperience(workExperience);
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = Validator.validateField(contract, "Contract", Contract.class, false);
    }

    public void setContract(Object amount) {
        this.contract = new Contract(amount);
    }

    public List<ActorTitle> getActorTitles() {
        return actorTitles;
    }

    public void setActorTitles(List<?> actorTitles) {
        this.actorTitles = CommonUtils.casteInnerClass(
                Validator.validateField(actorTitles, "actorTitles", List.class, false), ActorTitle.class
        );
    }

    public List<ActorAward> getActorAwards() {
        return actorAwards;
    }

    public void setActorAwards(List<?> actorAwards) {
        this.actorAwards = CommonUtils.casteInnerClass(
                Validator.validateField(actorAwards, "actorAwards", List.class, false), ActorAward.class
        );
    }

    @Override
    public String toString() {
        return "Фамилия: " + surname + "\n" +
                "Имя: " + firstname + "\n" +
                ((patronymic != null) ? "Отчество: " + patronymic + "\n": "") +
                "Стаж: " + workExperience + "\n" +
                "Контракт: " + contract + "\n" +
                ((actorTitles.isEmpty()) ? "" : "Звания: " + actorTitles + "\n") +
                ((actorAwards.isEmpty()) ? "" : "Награды: " + actorAwards + "\n");
    }

    public String shortString() {
        return String.join(" ",surname, firstname, ((patronymic != null) ? patronymic : ""));
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Actor actor)) {
            return false;
        }

        boolean equalPatronymic = patronymic == null && actor.getPatronymic() != null && actor.getPatronymic().equals(patronymic) ||
                patronymic != null && actor.getPatronymic() == null && patronymic.equals(actor.getPatronymic()) ||
                patronymic != null && actor.getPatronymic() != null && patronymic.equals(actor.getPatronymic()) ||
                patronymic == null && actor.getPatronymic() == null;

        return surname.equals(actor.getSurname()) &&
                firstname.equals(actor.getFirstname()) &&
                equalPatronymic &&
                workExperience.equals(actor.getWorkExperience()) &&
                contract.equals(actor.getContract()) &&
                actorTitles.containsAll(actor.getActorTitles()) &&
                actorAwards.containsAll(actor.getActorAwards());
    }
}
