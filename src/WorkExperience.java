public class WorkExperience {
    private Integer workExperience;

    public WorkExperience(Integer years, Integer months, Integer days) {
        this.workExperience = Validator.validateField(
                new Integer[]{years, months, days}, "workExperience", Integer[].class, Integer.class, false
        );
    }

    public WorkExperience(Integer workExperience) {
        this(0, 0, workExperience);
    }

    public Integer getWorkExperience() {
        return workExperience;
    }

    public void setWorkExperience(Integer years, Integer months, Integer days) {
        this.workExperience = Validator.validateField(
                new Integer[]{years, months, days}, "workExperience", Integer[].class, Integer.class, false
        );
    }

    public void setWorkExperience(Integer workExperience) {
       setWorkExperience(0, 0, workExperience);
    }
}
