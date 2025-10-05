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
        this.surname = surname;
        this.firstname = firstname;
        this.patronymic = patronymic;
        this.workExperience = workExperience;
        this.contract = contract;
        this.actorTitles = actorTitles;
        this.actorAwards = actorAwards;
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
        this.surname = surname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public WorkExperience getWorkExperience() {
        return workExperience;
    }

    public void setWorkExperience(WorkExperience workExperience) {
        this.workExperience = workExperience;
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public List<ActorTitle> getActorTitles() {
        return actorTitles;
    }

    public void setActorTitles(List<ActorTitle> actorTitles) {
        this.actorTitles = actorTitles;
    }

    public List<ActorAward> getActorAwards() {
        return actorAwards;
    }

    public void setActorAwards(List<ActorAward> actorAwards) {
        this.actorAwards = actorAwards;
    }
}
