// TransactionCategory.java
package pennywise.interfaces;

public interface TransactionCategory {
    String getCategoryName();
    double processAmount(double amount);
    boolean isExpenseCategory(); // To check if transaction type is expense and over budget, don't add it}
//  OLD CODE, refactored to removed hard coded instance checking, violating SOLID principles
//  TransactionType getTransactionType();

} 