package pennywise.service;

import pennywise.model.*;
import pennywise.interfaces.IDataStorage;
import java.util.List;
import java.util.stream.Collectors;

public class BudgetManager {
    private final IDataStorage storage;

    public BudgetManager(IDataStorage storage) {
        this.storage = storage;
    }

    public boolean createBudget(String userId, String category, double amount) {
        if (amount < 0) return false;

        User user = findUser(userId);
        if (user == null) return false;

        Budget budget = new Budget(userId, category, amount);
        user.addBudget(budget);
        return storage.saveUser(user);
    }

    public List<Budget> getBudgets(String userId) {
        User user = findUser(userId);
        return user != null ? user.getBudgets() : List.of();
    }

    private User findUser(String userId) {
        return storage.loadData().stream()
                .filter(u -> u.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
    }
}