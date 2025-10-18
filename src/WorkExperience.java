import java.util.List;
import java.util.Objects;

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

    @Override
    public String toString() {
        Integer[] styledWorkExperience = TimeUtils.getFullDateMark(workExperience);
        int years = styledWorkExperience[0], months = styledWorkExperience[1], days = styledWorkExperience[2];
        String yearsName = TimeUtils.getTimeUnionName(years, "year");
        String monthsName = TimeUtils.getTimeUnionName(months, "month");
        String daysName = TimeUtils.getTimeUnionName(days, "day");

        return String.join(" ", String.valueOf(years), yearsName, String.valueOf(months), monthsName, String.valueOf(days), daysName);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof WorkExperience workExperience1)) {
            return false;
        }
        
        return Objects.equals(workExperience, workExperience1.getWorkExperience());
    }
}
