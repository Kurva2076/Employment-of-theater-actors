public class ActorAward {
    private final Integer awardId;
    private String awardName;

    public ActorAward(String awardName) {
        this.awardId = CommonUtils.generateId();
        this.awardName = Validator.validateField(awardName, "awardName", String.class, false);
    }

    public Integer getAwardId() {
        return awardId;
    }

    public String getAwardName() {
        return awardName;
    }

    public void setAwardName(String awardName) {
        this.awardName = Validator.validateField(awardName, "awardName", String.class, false);
    }
}
