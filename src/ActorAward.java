public class ActorAward {
    private final Integer awardId;
    private String awardName;

    public ActorAward(String awardName) {
        this(null, awardName);
    }

    public ActorAward(ActorAward actorAward) {
        this(null, actorAward);
    }

    public ActorAward(Number awardId, Object award) {
        if (awardId == null) {
            this.awardId = CommonUtils.generateId();
        } else {
            this.awardId = Validator.validateField(awardId, "id", Integer.class, false);
        }
        this.awardName = Validator.validateField(award, "actorAward", String.class, false);
    }

    public Integer getAwardId() {
        return awardId;
    }

    public String getAwardName() {
        return awardName;
    }

    public void setAwardName(String awardName) {
        setAwardName((Object) awardName);
    }

    public void setAwardName(ActorAward actorAward) {
        setAwardName((Object) actorAward);
    }

    public void setAwardName(Object award) {
        this.awardName = Validator.validateField(award, "actorAward", String.class, false);
    }

    @Override
    public String toString() {
        return awardName;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ActorAward actorAward)) {
            return false;
        }

        return awardName.equals(actorAward.getAwardName());
    }

    @Override
    public int hashCode() {
        return awardName.hashCode();
    }
}
