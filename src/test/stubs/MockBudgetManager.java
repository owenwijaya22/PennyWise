package test.stubs;

import pennywise.interfaces.IDataStorage;
import pennywise.service.BudgetManager;
import java.util.HashMap;
import java.util.Map;

public class MockBudgetManager extends BudgetManager {
    private final Map<String, Double> budgets = new HashMap<>();

    public MockBudgetManager(IDataStorage dataStorage) {
        super(dataStorage);
    }

    @Override
    public boolean createBudget(String userId, double amount) {
        if (userId == null || amount < 0) {
            return false;
        }
        budgets.put(userId, amount);
        return true;
    }

    @Override
    public boolean updateBudget(String userId, double amount) {
        if (userId == null || amount < 0) {
            return false;
        }
        budgets.put(userId, amount);
        return true;
    }

    @Override
    public double getCurrentMonthBudget(String userId) {
        if (userId == null || !budgets.containsKey(userId)) {
            return 0.0;
        }
        return budgets.get(userId);
    }

    public void clearAllData() {
        budgets.clear();
    }
}