import java.math.BigDecimal;
import java.util.Objects;

public class Contract {
    private final Integer contractId;
    private Double amount;

    public Contract(Double amount) {
        this(null, amount);
    }

    public Contract(Integer amount) {
        this(null, amount);
    }

    public Contract(String amount) {
        this(null, amount);
    }

    public Contract(Contract contract) {
        this(null, contract);
    }

    public Contract(Number contractId, Object amount) {
        if (contractId == null) {
            this.contractId = CommonUtils.generateId();
        } else {
            this.contractId = Validator.validateField(contractId, "id", Integer.class, false);
        }
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

    @Override
    public String toString() {
        return BigDecimal.valueOf(amount).toPlainString() + " руб.";
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Contract contract)) {
            return false;
        }

        return Objects.equals(amount, contract.getAmount());
    }
}
