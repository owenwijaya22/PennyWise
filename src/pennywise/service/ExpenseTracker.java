package pennywise.service;

import pennywise.model.*;
import pennywise.interfaces.IDataStorage;
import pennywise.interfaces.TransactionCategory;

import java.util.List;

public class ExpenseTracker {
    private final IDataStorage storage;

    public ExpenseTracker(IDataStorage storage) {
        this.storage = storage;
    }

    public boolean addTransaction(String userId, double amount, TransactionCategory category) {
        if (amount <= 0) return false;
        Transaction transaction = new Transaction(
            userId, 
            amount, 
            category.getTransactionType(), 
            category
        );
        return storage.saveTransaction(userId, transaction);
    }

    public List<Transaction> getTransactions(String userId) {
        return storage.loadTransactions(userId);
    }
}