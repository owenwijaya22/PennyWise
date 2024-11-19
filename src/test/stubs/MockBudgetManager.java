/*
 * 
 */
package test.stubs;

import pennywise.interfaces.IDataStorage;
import pennywise.service.BudgetManager;
import java.util.HashMap;
import java.util.Map;


/**
 * The Class MockBudgetManager.
 */
public class MockBudgetManager extends BudgetManager {
    
    /** The budgets. */
    private final Map<String, Double> budgets = new HashMap<>();

    /**
     * Instantiates a new mock budget manager.
     *
     * @param dataStorage the data storage
     */
    public MockBudgetManager(IDataStorage dataStorage) {
        super(dataStorage);
    }

    /**
     * Creates the budget.
     *
     * @param userId the user id
     * @param amount the amount
     * @return true, if successful
     */
    @Override
    public boolean createBudget(String userId, double amount) {
        if (userId == null || amount < 0) {
            return false;
        }
        budgets.put(userId, amount);
        return true;
    }

    /**
     * Update budget.
     *
     * @param userId the user id
     * @param amount the amount
     * @return true, if successful
     */
    @Override
    public boolean updateBudget(String userId, double amount) {
        if (userId == null || amount < 0) {
            return false;
        }
        budgets.put(userId, amount);
        return true;
    }

    /**
     * Gets the current month budget.
     *
     * @param userId the user id
     * @return the current month budget
     */
    @Override
    public double getCurrentMonthBudget(String userId) {
        if (userId == null || !budgets.containsKey(userId)) {
            return 0.0;
        }
        return budgets.get(userId);
    }

    /**
     * Clear all data.
     */
    public void clearAllData() {
        budgets.clear();
    }
}