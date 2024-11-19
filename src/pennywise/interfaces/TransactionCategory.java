// TransactionCategory.java
package pennywise.interfaces;


/**
 * The Interface TransactionCategory.
 */
public interface TransactionCategory {
    
    /**
     * Gets the category name.
     *
     * @return the category name
     */
    String getCategoryName();
    
    /**
     * Process amount.
     *
     * @param amount the amount
     * @return the double
     */
    double processAmount(double amount);
    
    /**
     * Checks if is expense category.
     *
     * @return true, if is expense category
     */
    boolean isExpenseCategory(); // To check if transaction type is expense and over budget, don't add it}
//  OLD CODE, refactored to removed hard coded instance checking, violating SOLID principles
//  TransactionType getTransactionType();

} 