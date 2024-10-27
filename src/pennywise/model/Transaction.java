package pennywise.model;

import java.io.Serializable;
import java.util.Date;

import pennywise.interfaces.TransactionCategory;

public class Transaction implements Serializable {
    private final String userId;
    private final double amount;
    private final TransactionType type;
    private final Date date;
    private final TransactionCategory category;

    public Transaction(String userId, double amount, TransactionType type, TransactionCategory category) {
        this.userId = userId;
        this.amount = type == TransactionType.EXPENSE ? -Math.abs(amount) : Math.abs(amount);
        this.type = type;
        this.category = category;
        this.date = new Date();
    }

    public String getUserId() { return userId; }
    public double getAmount() { return amount; }
    public TransactionType getType() { return type; }
    public Date getDate() { return new Date(date.getTime()); }
    public TransactionCategory getCategory() { return category; }
}