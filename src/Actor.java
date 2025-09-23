public class Actor {
    private final Integer actorId;
    private String surname;
    private String firstname;
    private String patronymic;
    private WorkExperience workExperience;
    private Contract contract;

    public Actor(String surname, String firstname, String patronymic,
                 WorkExperience workExperience, Integer contractId) {
        this.actorId = CommonUtils.generateId();
//        this.surname =
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
}
