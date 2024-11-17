package pennywise.service;

import pennywise.model.*;
import pennywise.interfaces.IDataStorage;
import java.time.YearMonth;
import java.util.List;

public class BudgetManager {
    private final IDataStorage storage;

    public BudgetManager(IDataStorage storage) {
        this.storage = storage;
    }
    // refactored with extract method from User.java
    public boolean createBudget(String userId, double amount) {
        if (amount < 0) return false;
        Budget budget = new Budget(userId, amount);
        return storage.saveBudget(userId, budget);
    }

    public double getCurrentMonthBudget(String userId) {
        YearMonth currentMonth = YearMonth.now();
        return getBudgets(userId).stream()
                .filter(b -> YearMonth.from(b.getMonth()).equals(currentMonth))
                .findFirst()
                .map(Budget::getAmount)
                .orElse(0.0);
    }

    public boolean isOverBudget(String userId, double currentExpenses, double proposedExpense) {
        double monthlyBudget = getCurrentMonthBudget(userId);
        return monthlyBudget <= 0 || (currentExpenses + proposedExpense) > monthlyBudget;
    }

    private List<Budget> getBudgets(String userId) {
        return storage.loadBudgets(userId);
    }
    
    public boolean updateBudget(String userId, double newAmount) {
        if (newAmount < 0) return false;
        Budget newBudget = new Budget(userId, newAmount);
        return storage.saveBudget(userId, newBudget);
    }
}
