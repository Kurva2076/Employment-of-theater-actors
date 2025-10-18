public class PublicActor {
    private String surname;
    private String firstname;
    private String patronymic;
    private String phone;

    public PublicActor(String surname, String firstname, String patronymic, String phone) {
        this.surname = Validator.validateField(surname, "surname", String.class, false);
        this.firstname = Validator.validateField(firstname, "firstname", String.class, false);
        this.patronymic = Validator.validateField(patronymic, "patronymic", String.class, true);
        this.phone = Validator.validateField(phone, "phone", String.class, false);
    }

    public PublicActor(String surname, String firstname, String phone) {
        this(surname, firstname, null, phone);
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = Validator.validateField(phone, "phone", String.class, false);
    }
}
