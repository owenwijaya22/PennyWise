// Transaction.java
package pennywise.model;

import java.io.Serializable;
import java.util.Date;
import pennywise.interfaces.TransactionCategory;


/**
 * The Class Transaction.
 */
public class Transaction implements Serializable {
	
	/** The Constant serialVersionUID. */
	// Ensure that a loaded class corresponds exactly to a serialized object
	private static final long serialVersionUID = 1L;
    
    /** The user id. */
    private final String userId;
    
    /** The amount. */
    private final double amount;
    
    /** The date. */
    private final Date date;
    
    /** The category. */
    private final TransactionCategory category;
// OLD CODE, refactored to implement polymorphism and strategy design pattern
//    public Transaction(String userId, double amount, TransactionType type, TransactionCategory category) {
//        this.userId = userId;
//        this.amount = type == TransactionType.EXPENSE ? -Math.abs(amount) : Math.abs(amount);
//        this.type = type;
//        this.category = category;
//        this.date = new Date();
/**
 * Instantiates a new transaction.
 *
 * @param userId the user id
 * @param amount the amount
 * @param category the category
 */
//    }
    public Transaction(String userId, double amount, TransactionCategory category) {
        this.userId = userId;
        this.amount = category.processAmount(amount);
        this.category = category;
        this.date = new Date();
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
     * Checks if is expense.
     *
     * @return true, if is expense
     */
    public boolean isExpense() { return amount < 0; }
    
    /**
     * Gets the date.
     *
     * @return the date
     */
    public Date getDate() { return new Date(date.getTime()); }
    
    /**
     * Gets the category.
     *
     * @return the category
     */
    public TransactionCategory getCategory() { return category; }
}