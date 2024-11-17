// Transaction.java
package pennywise.model;

import java.io.Serializable;
import java.util.Date;
import pennywise.interfaces.TransactionCategory;

public class Transaction implements Serializable {
	// Ensure that a loaded class corresponds exactly to a serialized object
	private static final long serialVersionUID = 1L;
    private final String userId;
    private final double amount;
    private final Date date;
    private final TransactionCategory category;
// OLD CODE, refactored to implement polymorphism and strategy design pattern
//    public Transaction(String userId, double amount, TransactionType type, TransactionCategory category) {
//        this.userId = userId;
//        this.amount = type == TransactionType.EXPENSE ? -Math.abs(amount) : Math.abs(amount);
//        this.type = type;
//        this.category = category;
//        this.date = new Date();
//    }
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