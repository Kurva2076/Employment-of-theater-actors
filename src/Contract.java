public class Contract {
    private final Integer contractId;
    private Double amount;

    public Contract(Double amount) {
        this.contractId = CommonUtils.generateId();
        this.amount = CommonUtils.validateField(amount, "amount", Double.class);
    }

    public Integer getContractId() {
        return contractId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = CommonUtils.validateField(amount, "amount", Double.class);
    }
}
