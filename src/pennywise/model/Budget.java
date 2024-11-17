package pennywise.model;

import java.io.Serializable;
import java.time.YearMonth;

public class Budget implements Serializable {
	// Ensure that a loaded class corresponds exactly to a serialized object
	private static final long serialVersionUID = 1L;
    private final String userId;
    private final double amount;
    private final YearMonth month;

    public Budget(String userId, double amount) {
        this.userId = userId;
        this.amount = amount;
        this.month = YearMonth.now();
    }

    public String getUserId() { return userId; }
    public double getAmount() { return amount; }
    public YearMonth getMonth() { return month; }
}