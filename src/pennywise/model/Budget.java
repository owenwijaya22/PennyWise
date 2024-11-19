/*
 * 
 */
package pennywise.model;

import java.io.Serializable;
import java.time.YearMonth;


/**
 * The Class Budget.
 */
public class Budget implements Serializable {
	
	/** The Constant serialVersionUID. */
	// Ensure that a loaded class corresponds exactly to a serialized object
	private static final long serialVersionUID = 1L;
    
    /** The user id. */
    private final String userId;
    
    /** The amount. */
    private final double amount;
    
    /** The month. */
    private final YearMonth month;

    /**
     * Instantiates a new budget.
     *
     * @param userId the user id
     * @param amount the amount
     */
    public Budget(String userId, double amount) {
        this.userId = userId;
        this.amount = amount;
        this.month = YearMonth.now();
    }

    /**
     * Gets the user id.
     *
     * @return the user id
     */
    public String getUserId() { return userId; }
    
    /**
     * Gets the amount.
     *
     * @return the amount
     */
    public double getAmount() { return amount; }
    
    /**
     * Gets the month.
     *
     * @return the month
     */
    public YearMonth getMonth() { return month; }
}