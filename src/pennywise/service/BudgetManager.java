/*
 * 
 */
package pennywise.service;

import pennywise.model.*;
import pennywise.interfaces.IDataStorage;
import java.time.YearMonth;
import java.util.List;


/**
 * The Class BudgetManager.
 */
public class BudgetManager {
    
    /** The storage. */
    private final IDataStorage storage;

    /**
     * Instantiates a new budget manager.
     *
     * @param storage the storage
     */
    public BudgetManager(IDataStorage storage) {
        this.storage = storage;
    }
    
    /**
     * Creates the budget.
     *
     * @param userId the user id
     * @param amount the amount
     * @return true, if successful
     */
    // refactored with extract method from User.java
    public boolean createBudget(String userId, double amount) {
        if (amount < 0) return false;
        Budget budget = new Budget(userId, amount);
        return storage.saveBudget(userId, budget);
    }

    /**
     * Gets the current month budget.
     *
     * @param userId the user id
     * @return the current month budget
     */
    public double getCurrentMonthBudget(String userId) {
        YearMonth currentMonth = YearMonth.now();
        return getBudgets(userId).stream()
                .filter(b -> YearMonth.from(b.getMonth()).equals(currentMonth))
                .findFirst()
                .map(Budget::getAmount)
                .orElse(0.0);
    }

    /**
     * Checks if is over budget.
     *
     * @param userId the user id
     * @param currentExpenses the current expenses
     * @param proposedExpense the proposed expense
     * @return true, if is over budget
     */
    public boolean isOverBudget(String userId, double currentExpenses, double proposedExpense) {
        double monthlyBudget = getCurrentMonthBudget(userId);
        return monthlyBudget <= 0 || (currentExpenses + proposedExpense) > monthlyBudget;
    }

    /**
     * Gets the budgets.
     *
     * @param userId the user id
     * @return the budgets
     */
    private List<Budget> getBudgets(String userId) {
        return storage.loadBudgets(userId);
    }
    
    /**
     * Update budget.
     *
     * @param userId the user id
     * @param newAmount the new amount
     * @return true, if successful
     */
    public boolean updateBudget(String userId, double newAmount) {
        if (newAmount < 0) return false;
        Budget newBudget = new Budget(userId, newAmount);
        return storage.saveBudget(userId, newBudget);
    }
}
