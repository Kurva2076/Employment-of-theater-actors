import java.util.List;

public class WorkExperience {
    private Integer workExperience;

    public WorkExperience(Integer[] workExperience) {
        this((Object) workExperience);
    }

    public WorkExperience(Double[] workExperience) {
        this((Object) workExperience);
    }

    public WorkExperience(String[] workExperience) {
        this((Object) workExperience);
    }

    public WorkExperience(Integer years, Integer months, Integer days) {
        this(new Integer[]{years, months, days});
    }

    public WorkExperience(Double years, Double months, Double days) {
        this(new Double[]{years, months, days});
    }

    public WorkExperience(String years, String months, String days) {
        this(new String[]{years, months, days});
    }

    public WorkExperience(Integer workExperience) {
        this(0, 0, workExperience);
    }

    public WorkExperience(Double workExperience) {
        this(0.0, 0.0, workExperience);
    }

    public WorkExperience(String workExperience) {
        this("0", "0", workExperience);
    }

    public WorkExperience(List<?> workExperience) {
        this((Object) workExperience);
    }

    public WorkExperience(WorkExperience workExperience) {
        this((Object) workExperience);
    }

    public WorkExperience(Object workExperience) {
        this.workExperience = Validator.validateField(workExperience, "workExperience", Integer.class, false);
    }

    public Integer getWorkExperience() {
        return workExperience;
    }

    public void setWorkExperience(Integer[] workExperience) {
        setWorkExperience((Object) workExperience);
    }

    public void setWorkExperience(Double[] workExperience) {
        setWorkExperience((Object) workExperience);
    }

    public void setWorkExperience(String[] workExperience) {
        setWorkExperience((Object) workExperience);
    }

    public void setWorkExperience(Integer years, Integer months, Integer days) {
        setWorkExperience(new Integer[]{years, months, days});
    }

    public void setWorkExperience(Double years, Double months, Double days) {
        setWorkExperience(new Double[]{years, months, days});
    }

    public void setWorkExperience(String years, String months, String days) {
        setWorkExperience(new String[]{years, months, days});
    }

    public void setWorkExperience(Integer workExperience) {
        setWorkExperience(0, 0, workExperience);
    }

    public void setWorkExperience(Double workExperience) {
        setWorkExperience(0.0, 0.0, workExperience);
    }

    public void setWorkExperience(String workExperience) {
        setWorkExperience("0", "0", workExperience);
    }

    public void setWorkExperience(List<?> workExperience) {
        setWorkExperience((Object) workExperience);
    }

    public void setWorkExperience(WorkExperience workExperience) {
        setWorkExperience((Object) workExperience);
    }

    public void setWorkExperience(Object workExperience) {
        this.workExperience = Validator.validateField(workExperience, "workExperience", Integer.class, false);
    }
}
