package pennywise.model;

import java.util.Date;

public class Income extends Transaction {
    private IncomeCategory category;

    public Income(String userId, double amount, String description, IncomeCategory category) {
        super(userId, amount, description, TransactionType.INCOME);
        this.category = category;
    }

    public IncomeCategory getCategory() {
        return category;
    }

    public void setCategory(IncomeCategory category) {
        this.category = category;
    }
}