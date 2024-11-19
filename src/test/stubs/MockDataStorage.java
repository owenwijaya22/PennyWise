/*
 * 
 */
package test.stubs;

import pennywise.interfaces.IDataStorage;
import pennywise.model.*;
import java.util.*;


/**
 * The Class MockDataStorage.
 */
public class MockDataStorage implements IDataStorage {
    
    /** The users. */
    private Map<String, User> users = new HashMap<>();
    
    /** The transactions. */
    private Map<String, List<Transaction>> transactions = new HashMap<>();
    
    /** The budgets. */
    private Map<String, List<Budget>> budgets = new HashMap<>();

    /**
     * Save data.
     *
     * @param users the users
     */
    @Override
    public void saveData(List<User> users) {
        users.forEach(user -> this.users.put(user.getUserId(), user));
    }

    /**
     * Load data.
     *
     * @return the list
     */
    @Override
    public List<User> loadData() {
        return new ArrayList<>(users.values());
    }

    /**
     * Save user.
     *
     * @param user the user
     * @return true, if successful
     */
    @Override
    public boolean saveUser(User user) {
        users.put(user.getUserId(), user);
        return true;
    }

    /**
     * Load user.
     *
     * @param userID the user ID
     * @return the user
     */
    @Override
    public User loadUser(String userID) {
        return users.get(userID);
    }

    /**
     * Save transaction.
     *
     * @param userID the user ID
     * @param transaction the transaction
     * @return true, if successful
     */
    @Override
    public boolean saveTransaction(String userID, Transaction transaction) {
        transactions.computeIfAbsent(userID, k -> new ArrayList<>()).add(transaction);
        return true;
    }

    /**
     * Load transactions.
     *
     * @param userID the user ID
     * @return the list
     */
    @Override
    public List<Transaction> loadTransactions(String userID) {
        return transactions.getOrDefault(userID, new ArrayList<>());
    }

    /**
     * Delete user.
     *
     * @param userID the user ID
     * @return true, if successful
     */
    @Override
    public boolean deleteUser(String userID) {
        users.remove(userID);
        transactions.remove(userID);
        budgets.remove(userID);
        return true;
    }

    /**
     * Clear all data.
     *
     * @return true, if successful
     */
    @Override
    public boolean clearAllData() {
        users.clear();
        transactions.clear();
        budgets.clear();
        return true;
    }

    /**
     * Save budget.
     *
     * @param userId the user id
     * @param budget the budget
     * @return true, if successful
     */
    @Override
    public boolean saveBudget(String userId, Budget budget) {
        budgets.computeIfAbsent(userId, k -> new ArrayList<>()).add(budget);
        return true;
    }

    /**
     * Load budgets.
     *
     * @param userId the user id
     * @return the list
     */
    @Override
    public List<Budget> loadBudgets(String userId) {
        return budgets.getOrDefault(userId, new ArrayList<>());
    }
}