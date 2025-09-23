public class ActorTitle {
    private final Integer titleId;
    private String titleName;

    public ActorTitle(String titleName) {
        this.titleId = CommonUtils.generateId();
        this.titleName = CommonUtils.validateField(titleName, "actorTitle", String.class);
    }

    public Integer getTitleId() {
        return titleId;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName =  CommonUtils.validateField(titleName, "actorTitle", String.class);;
    }
}
