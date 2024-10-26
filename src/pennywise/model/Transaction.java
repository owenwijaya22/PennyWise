package pennywise.model;

import java.util.Date;

public abstract class Transaction {
    protected Date date;
    protected float amount;

    public Transaction(Date date, float amount) {
        this.date = date;
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public float getAmount() {
        return amount;
    }
}