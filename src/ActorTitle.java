public class ActorTitle {
    private final Integer titleId;
    private String titleName;

    public ActorTitle(String titleName) {
        this((Object) titleName);
    }

    public ActorTitle(ActorTitle actorTitle) {
        this((Object) actorTitle);
    }

    public ActorTitle(Object title) {
        this.titleId = CommonUtils.generateId();
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
}
