public class Award {
    private final Integer awardId;
    private String awardName;

    public Award(String awardName) {
        this.awardId = CommonUtils.generateId();
        this.awardName = CommonUtils.validateField(awardName, "awardName", String.class);
    }

    public Integer getAwardId() {
        return awardId;
    }

    public String getAwardName() {
        return awardName;
    }

    public void setAwardName(String awardName) {
        this.awardName = CommonUtils.validateField(awardName, "awardName", String.class);
    }
}
