// ExpenseCategory.java
package pennywise.model;

import pennywise.interfaces.TransactionCategory;


/**
 * The Enum ExpenseCategory.
 */
public enum ExpenseCategory implements TransactionCategory {
    
    /** The food. */
    FOOD, 
 /** The transportation. */
 TRANSPORTATION, 
 /** The utilities. */
 UTILITIES, 
 /** The shopping. */
 SHOPPING, 
 /** The bills. */
 BILLS, 
 /** The entertainment. */
 ENTERTAINMENT, 
 /** The health. */
 HEALTH, 
 /** The other. */
 OTHER;

    /**
     * Gets the category name.
     *
     * @return the category name
     */
    @Override
    public String getCategoryName() {
        return this.name();
    }

    /**
     * Process amount.
     *
     * @param amount the amount
     * @return the double
     */
    @Override
    public double processAmount(double amount) {
        return -Math.abs(amount);  // Expense is always negative
    }
    
    /**
     * Checks if is expense category.
     *
     * @return true, if is expense category
     */
    @Override
    public boolean isExpenseCategory() {
        return true;
    }		
}