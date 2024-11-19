/*
 * 
 */
package pennywise.service;

import pennywise.model.*;
import pennywise.interfaces.IDataStorage;
import pennywise.interfaces.TransactionCategory;

import java.util.List;


/**
 * The Class TransactionManager.
 */
public class TransactionManager {
    
    /** The storage. */
    private final IDataStorage storage;

    /**
     * Instantiates a new transaction manager.
     *
     * @param storage the storage
     */
    public TransactionManager(IDataStorage storage) {
        this.storage = storage;
    }

    /**
     * Adds the transaction.
     *
     * @param userId the user id
     * @param amount the amount
     * @param category the category
     * @return true, if successful
     */
    public boolean addTransaction(String userId, double amount, TransactionCategory category) {
        if (amount <= 0) return false;
        Transaction transaction = new Transaction(userId, amount, category);
        return storage.saveTransaction(userId, transaction);
    }

    /**
     * Gets the transactions.
     *
     * @param userId the user id
     * @return the transactions
     */
    public List<Transaction> getTransactions(String userId) {
        return storage.loadTransactions(userId);
    }
}