package test.stubs;

import pennywise.interfaces.IDataStorage;
import pennywise.model.*;
import java.util.*;

public class MockDataStorage implements IDataStorage {
    private Map<String, User> users = new HashMap<>();
    private Map<String, List<Transaction>> transactions = new HashMap<>();
    private Map<String, List<Budget>> budgets = new HashMap<>();

    @Override
    public void saveData(List<User> users) {
        users.forEach(user -> this.users.put(user.getUserId(), user));
    }

    @Override
    public List<User> loadData() {
        return new ArrayList<>(users.values());
    }

    @Override
    public boolean saveUser(User user) {
        users.put(user.getUserId(), user);
        return true;
    }

    @Override
    public User loadUser(String userID) {
        return users.get(userID);
    }

    @Override
    public boolean saveTransaction(String userID, Transaction transaction) {
        transactions.computeIfAbsent(userID, k -> new ArrayList<>()).add(transaction);
        return true;
    }

    @Override
    public List<Transaction> loadTransactions(String userID) {
        return transactions.getOrDefault(userID, new ArrayList<>());
    }

    @Override
    public boolean deleteUser(String userID) {
        users.remove(userID);
        transactions.remove(userID);
        budgets.remove(userID);
        return true;
    }

    @Override
    public boolean clearAllData() {
        users.clear();
        transactions.clear();
        budgets.clear();
        return true;
    }

    @Override
    public boolean saveBudget(String userId, Budget budget) {
        budgets.computeIfAbsent(userId, k -> new ArrayList<>()).add(budget);
        return true;
    }

    @Override
    public List<Budget> loadBudgets(String userId) {
        return budgets.getOrDefault(userId, new ArrayList<>());
    }
}