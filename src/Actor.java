import java.util.*;

public class Actor extends PublicActor {
    private final Integer actorId;
    private final String initials;
    private WorkExperience workExperience;
    private Contract contract;
    private List<ActorTitle> actorTitles;
    private List<ActorAward> actorAwards;

    public Actor(Number actorId, String surname, String firstname, String patronymic, String phone,
                 WorkExperience workExperience, Contract contract, List<?> actorTitles, List<?> actorAwards) {
        super(surname, firstname, patronymic, phone);

        if (actorId == null) {
            this.actorId = CommonUtils.generateId();
        } else {
            this.actorId = Validator.validateField(actorId, "id", Integer.class, false);
        }

        this.initials = CommonUtils.getInitials(getSurname(), getFirstname(), getPatronymic());
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
                 List<?> actorTitles, List<?> actorAwards, String phone) {
        this(null, surname, firstname, null, phone, workExperience, contract, actorTitles, actorAwards);
    }

    public Actor(Map<String, ?> map) {
        this(
                (Number) map.get("actorId"),
                (String) map.get("surname"),
                (String) map.get("firstname"),
                (String) map.get("patronymic"),
                (String) map.get("phone"),
                new WorkExperience(map.get("workExperience")),
                new Contract(null, map.get("contract")),
                (map.get("actorTitles") == null) ? new ArrayList<>() : (
                        (!(map.get("actorTitles") instanceof String)) ? (List<?>) map.get("actorTitles") : (
                                (map.get("actorTitles").toString().isBlank()) ? new ArrayList<>() :
                                        List.of(map.get("actorTitles"))
                        )
                ),
                (map.get("actorAwards") == null) ? new ArrayList<>() : (
                        (!(map.get("actorAwards") instanceof String)) ? (List<?>) map.get("actorAwards") : (
                                (map.get("actorAwards").toString().isBlank()) ? new ArrayList<>() :
                                        List.of(map.get("actorAwards"))
                        )
                )
        );
    }

    public Actor(Object object, String type) {
        this(Parser.parse(object, type, Actor.class));
    }

    public Actor(Actor actor) {
        this(
                null, actor.getSurname(), actor.getFirstname(), actor.getPatronymic(), actor.getPhone(),
                actor.getWorkExperience(), actor.getContract(), actor.getActorTitles(), actor.getActorAwards()
        );
    }

    public Integer getActorId() {
        return actorId;
    }

    public String getInitials() {
        return initials;
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
        this.contract = new Contract(null, amount);
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
        return "ID: " + actorId + "\n" +
                super.toString() +
                "Инициалы: " + initials + "\n" +
                "Стаж: " + workExperience + "\n" +
                "Контракт: " + contract + "\n" +
                ((actorTitles.isEmpty()) ? "" : "Звания: " + CommonUtils.listToString(actorTitles) + "\n") +
                ((actorAwards.isEmpty()) ? "" : "Награды: " + CommonUtils.listToString(actorAwards) + "\n");
    }

    public String shortString() {
        return String.join(" ", getSurname(), getFirstname(), ((getPatronymic() != null) ? getPatronymic() : ""));
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Actor actor)) {
            return false;
        }

        return super.equals(obj) &&
                workExperience.equals(actor.getWorkExperience()) &&
                contract.equals(actor.getContract()) &&
                new HashSet<>(actorTitles).containsAll(actor.getActorTitles()) &&
                new HashSet<>(actorAwards).containsAll(actor.getActorAwards());
    }

    public Map<String, Object> toSimpleMap() {
        Map<String, Object> map = new HashMap<>();

        map.put("actorId", actorId);
        map.put("surname", getSurname());
        map.put("firstname", getFirstname());
        map.put("patronymic", getPatronymic());
        map.put("phone", getPhone());
        map.put("contract", contract.getAmount());
        map.put("workExperience", TimeUtils.getFullDateMark(workExperience.getDays()));
        map.put("actorAwards", actorAwards.stream()
                .map(ActorAward::getAwardName)
                .toList());
        map.put("actorTitles", actorTitles.stream()
                .map(ActorTitle::getTitleName)
                .toList());

        return map;
    }
}
