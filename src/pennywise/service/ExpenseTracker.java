package pennywise.service;

import pennywise.model.*;
import pennywise.interfaces.IDataStorage;
import java.util.List;

public class ExpenseTracker {
    private final IDataStorage storage;

    public ExpenseTracker(IDataStorage storage) {
        this.storage = storage;
    }

    public boolean addExpense(String userId, double amount, String description, ExpenseCategory category) {
        if (amount <= 0) return false;
        Transaction transaction = new Transaction(userId, -amount, description, TransactionType.EXPENSE);
        return storage.saveTransaction(userId, transaction);
    }

    public boolean addIncome(String userId, double amount, String description, IncomeCategory category) {
        if (amount <= 0) return false;
        Transaction transaction = new Transaction(userId, amount, description, TransactionType.INCOME);
        return storage.saveTransaction(userId, transaction);
    }

    public List<Transaction> getTransactions(String userId) {
        return storage.loadTransactions(userId);
    }
}