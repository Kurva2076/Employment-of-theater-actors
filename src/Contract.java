public class Contract {
    private final Integer contractId;
    private Double amount;

    public Contract(Double amount) {
        this.contractId = CommonUtils.generateId();
        this.amount = Validator.validateField(amount, "amount", Double.class, false);
    }

    public Contract(Integer amount) {
        this(Double.valueOf(amount));
    }

    public Integer getContractId() {
        return contractId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = Validator.validateField(amount, "amount", Double.class, false);
    }

    public void setAmount(Integer amount) {
        setAmount(Double.valueOf(amount));
    }
}
