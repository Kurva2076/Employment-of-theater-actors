public class WorkExperience {
    private Integer workExperience;

    public WorkExperience(Integer workExperience) {
        this.workExperience = workExperience;
    }

    public WorkExperience(Integer years, Integer months, Integer days) {
        this.workExperience = TimeUtils.convertYearsIntoDays(years) + TimeUtils.convertMonthsIntoDays(months) + days;
    }

    public Integer getWorkExperience() {
        return workExperience;
    }

    public void setWorkExperience(Integer workExperience) {
        this.workExperience = workExperience;
    }

    public void setWorkExperience(Integer years, Integer months, Integer days) {
        this.workExperience = TimeUtils.convertYearsIntoDays(years) + TimeUtils.convertMonthsIntoDays(months) + days;
    }
}
