// IncomeCategory.java
package pennywise.model;

import pennywise.interfaces.TransactionCategory;

public enum IncomeCategory implements TransactionCategory {
    SALARY,
    BUSINESS,
    INVESTMENT,
    GIFT,
    OTHER;

    @Override
    public String getCategoryName() {
        return this.name();
    }

    @Override
    public TransactionType getTransactionType() {
        return TransactionType.INCOME;
    }
    
    @Override
    public double processAmount(double amount) {
        return amount;  		
    }
}