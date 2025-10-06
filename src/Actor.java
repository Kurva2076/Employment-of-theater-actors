import java.util.List;

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
                 Contract contract, List<ActorTitle> actorTitles, List<ActorAward> actorAwards) {
        this.actorId = CommonUtils.generateId();
        this.surname = Validator.validateField(surname, "surname", String.class, false);
        this.firstname = Validator.validateField(firstname, "firstname", String.class, false);
        this.patronymic = Validator.validateField(patronymic, "patronymic", String.class,  true);
        this.workExperience = Validator.validateField(workExperience, "workExperience", WorkExperience.class, false);
        this.contract = Validator.validateField(contract, "contract", Contract.class, false);
        this.actorTitles = Validator.validateField(actorTitles, "actorTitles", true);
        this.actorAwards = Validator.validateField(actorAwards, "actorAwards", true);
    }

    public Actor(String surname, String firstname, WorkExperience workExperience, Contract contract,
                 List<ActorTitle> actorTitles, List<ActorAward> actorAwards) {
        this(surname, firstname, null, workExperience, contract, actorTitles, actorAwards);
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
        this.patronymic = Validator.validateField(patronymic, "patronymic", String.class,  true);
    }

    public WorkExperience getWorkExperience() {
        return workExperience;
    }

    public void setWorkExperience(WorkExperience workExperience) {
        this.workExperience = Validator.validateField(workExperience, "workExperience", WorkExperience.class, false);
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = Validator.validateField(contract, "contract", Contract.class, false);
    }

    public List<ActorTitle> getActorTitles() {
        return actorTitles;
    }

    public void setActorTitles(List<ActorTitle> actorTitles) {
        this.actorTitles = Validator.validateField(actorTitles, "actorTitles", true);
    }

    public List<ActorAward> getActorAwards() {
        return actorAwards;
    }

    public void setActorAwards(List<ActorAward> actorAwards) {
        this.actorAwards = Validator.validateField(actorAwards, "actorAwards", true);
    }
}
