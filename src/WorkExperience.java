public class WorkExperience {
    private Integer years;
    private Integer months;
    private Integer days;

    public WorkExperience(Integer years, Integer months, Integer days) {
        Integer[] workExperience = CommonUtils.validateField(
                new Integer[]{years, months, days}, "workExperience", Integer[].class
        );

        this.years = workExperience[0];
        this.months = workExperience[1];
        this.days = workExperience[2];
    }

    public Integer getYears() {
        return years;
    }

    public void setYears(Integer years) {
        this.years = years;
    }

    public Integer getMonths() {
        return months;
    }

    public void setMonths(Integer months) {
        this.months = months;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }
}
