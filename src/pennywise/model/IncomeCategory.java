// IncomeCategory.java
package pennywise.model;

import pennywise.interfaces.TransactionCategory;

public enum IncomeCategory implements TransactionCategory {
    SALARY, BUSINESS, INVESTMENT, GIFT, OTHER;

    @Override
    public String getCategoryName() {
        return this.name();
    }

    @Override
    public double processAmount(double amount) {
        return Math.abs(amount);  // Income is always positive
    }
    
    @Override
    public boolean isExpenseCategory() {
        return false;
    }		
}