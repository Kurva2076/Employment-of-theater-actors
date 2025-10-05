public class Contract {
    private final Integer contractId;
    private Double amount;

    public Contract(Double amount) {
        this.contractId = CommonUtils.generateId();
        this.amount = amount;
    }

    public Contract(Integer amount) {
        this.contractId = CommonUtils.generateId();
        this.amount = Double.valueOf(amount);
    }

    public Integer getContractId() {
        return contractId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setAmount(Integer amount) {
        this.amount = Double.valueOf(amount);
    }
}
