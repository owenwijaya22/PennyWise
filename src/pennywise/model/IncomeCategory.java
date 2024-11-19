// IncomeCategory.java
package pennywise.model;

import pennywise.interfaces.TransactionCategory;


/**
 * The Enum IncomeCategory.
 */
public enum IncomeCategory implements TransactionCategory {
    
    /** The salary. */
    SALARY, 
 /** The business. */
 BUSINESS, 
 /** The investment. */
 INVESTMENT, 
 /** The gift. */
 GIFT, 
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
        return Math.abs(amount);  // Income is always positive
    }
    
    /**
     * Checks if is expense category.
     *
     * @return true, if is expense category
     */
    @Override
    public boolean isExpenseCategory() {
        return false;
    }		
}