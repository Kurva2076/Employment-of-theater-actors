package model;

import utils.CommonUtils;
import utils.Validator;

public class PublicActor {
    private final Integer id;
    private String surname;
    private String firstname;
    private String patronymic;
    private String phone;

    public PublicActor(String surname, String firstname, String patronymic, String phone) {
        this(Integer.MAX_VALUE, surname, firstname, patronymic, phone);
    }

    public PublicActor(Integer actorId, String surname, String firstname, String patronymic, String phone) {
        this.id = Validator.validateField(actorId, "id", Integer.class, false);
        this.surname = Validator.validateField(surname, "surname", String.class, false);
        this.firstname = Validator.validateField(firstname, "firstname", String.class, false);
        this.patronymic = Validator.validateField(patronymic, "patronymic", String.class, true);
        this.phone = Validator.validateField(phone, "phone", String.class, false);
    }

    public PublicActor(String surname, String firstname, String phone) {
        this(surname, firstname, null, phone);
    }

    public Integer getId() {
        return id;
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

    @Override
    public String toString() {
        return "Фамилия: " + surname + "\n" +
                "Имя: " + firstname + "\n" +
                ((patronymic != null) ? "Отчество: " + patronymic + "\n" : "") +
                "Телефон: " + CommonUtils.formatPhone(phone) + "\n";
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PublicActor publicActor)) {
            return false;
        }

        boolean equalPatronymic = patronymic != null && publicActor.patronymic != null && patronymic.equals(publicActor.patronymic) || patronymic == null && publicActor.patronymic == null;

        return surname.equals(publicActor.surname) &&
                firstname.equals(publicActor.firstname) &&
                equalPatronymic &&
                phone.equals(publicActor.phone);
    }
}
