package pennywise.interfaces;

import pennywise.model.TransactionType;

public interface TransactionCategory {
    String getCategoryName();
    TransactionType getTransactionType();
    double processAmount(double amount);
}