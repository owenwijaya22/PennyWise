// ExpenseCategory.java
package pennywise.model;

import pennywise.interfaces.TransactionCategory;

public enum ExpenseCategory implements TransactionCategory {
    FOOD, TRANSPORTATION, UTILITIES, SHOPPING, BILLS, ENTERTAINMENT, HEALTH, OTHER;

    @Override
    public String getCategoryName() {
        return this.name();
    }

    @Override
    public double processAmount(double amount) {
        return -Math.abs(amount);  // Expense is always negative
    }
    
    @Override
    public boolean isExpenseCategory() {
        return true;
    }		
}