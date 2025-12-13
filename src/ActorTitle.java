public class ActorTitle {
    private final Integer titleId;
    private String titleName;

    public ActorTitle(String titleName) {
        this(null, titleName);
    }

    public ActorTitle(ActorTitle actorTitle) {
        this(null, actorTitle);
    }

    public ActorTitle(Number titleId, Object title) {
        if (titleId == null) {
            this.titleId = CommonUtils.generateId();
        } else {
            this.titleId = Validator.validateField(titleId, "id", Integer.class, false);
        }
        this.titleName = Validator.validateField(title, "actorTitle", String.class, false);
    }

    public Integer getTitleId() {
        return titleId;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        setTitleName((Object) titleName);
    }

    public void setTitleName(ActorTitle actorTitle) {
        setTitleName((Object) actorTitle);
    }

    public void setTitleName(Object title) {
        this.titleName = Validator.validateField(title, "actorTitle", String.class, false);
    }

    @Override
    public String toString() {
        return titleName;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ActorTitle actorTitle)) {
            return false;
        }

        return titleName.equals(actorTitle.getTitleName());
    }

    @Override
    public int hashCode() {
        return titleName.hashCode();
    }
}
