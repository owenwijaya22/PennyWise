package pennywise.model;

import java.io.Serializable;

public class Budget implements Serializable {
    private final String category;
    private final String userId;
    private double amount;

    public Budget(String userId, String category, double amount) {
        this.userId = userId;
        this.category = category;
        this.amount = amount;
    }

    public String getUserId() { return userId; }
    public String getCategory() { return category; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
}