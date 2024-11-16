// Transaction.java
package pennywise.model;

import java.io.Serializable;
import java.util.Date;
import pennywise.interfaces.TransactionCategory;

public class Transaction implements Serializable {
    private final String userId;
    private final double amount;
    private final Date date;
    private final TransactionCategory category;

    public Transaction(String userId, double amount, TransactionCategory category) {
        this.userId = userId;
        this.amount = category.processAmount(amount);
        this.category = category;
        this.date = new Date();
    }

    public String getUserId() { return userId; }
    public double getAmount() { return amount; }
    public boolean isExpense() { return amount < 0; }
    public Date getDate() { return new Date(date.getTime()); }
    public TransactionCategory getCategory() { return category; }
}