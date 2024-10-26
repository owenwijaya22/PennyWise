package pennywise.model;

import java.io.Serializable;
import java.util.Date;

public class Transaction implements Serializable {
    private final String userId;
    private final double amount;
    private final String description;
    private final TransactionType type;
    private final Date date;

    public Transaction(String userId, double amount, String description, TransactionType type) {
        this.userId = userId;
        this.amount = amount;
        this.description = description;
        this.type = type;
        this.date = new Date();
    }

    public String getUserId() { return userId; }
    
    public double getAmount() { return amount; }
    public String getDescription() { return description; }
    public TransactionType getType() { return type; }
    public Date getDate() { return new Date(date.getTime()); }
}
