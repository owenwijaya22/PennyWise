package pennywise.model;

import java.util.Date;

public class Expense extends Transaction {
    private ExpenseCategory category;

    public Expense(String userId, double amount, String description, ExpenseCategory category) {
        super(userId, amount, description, TransactionType.EXPENSE);
        this.category = category;
    }

    public ExpenseCategory getCategory() {
        return category;
    }

    public void setCategory(ExpenseCategory category) {
        this.category = category;
    }
}