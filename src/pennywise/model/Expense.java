package pennywise.model;

import java.util.Date;

public class Expense extends Transaction {
    private ExpenseCategory category;

    public Expense(Date date, float amount, ExpenseCategory category) {
        super(date, amount);
        this.category = category;
    }

    public ExpenseCategory getCategory() {
        return category;
    }
}