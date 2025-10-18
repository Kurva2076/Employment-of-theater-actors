public class Contract {
    private final Integer contractId;
    private Double amount;

    public Contract(Double amount) {
        this((Object) amount);
    }

    public Contract(Integer amount) {
        this((Object) amount);
    }

    public Contract(String amount) {
        this((Object) amount);
    }

    public Contract(Contract contract) {
        this((Object) contract);
    }

    public Contract(Object amount) {
        this.contractId = CommonUtils.generateId();
        this.amount = Validator.validateField(amount, "amount", Double.class, false);
    }

    public Integer getContractId() {
        return contractId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        setAmount((Object) amount);
    }

    public void setAmount(Integer amount) {
        setAmount((Object) amount);
    }

    public void setAmount(String amount) {
        setAmount((Object) amount);
    }

    public void setAmount(Contract contract) {
        setAmount((Object) contract);
    }

    public void setAmount(Object amount) {
        this.amount = Validator.validateField(amount, "amount", Double.class, false);
    }
}
