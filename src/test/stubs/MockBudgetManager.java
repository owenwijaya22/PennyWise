package test.stubs;

import pennywise.interfaces.*;
import pennywise.model.*;
import pennywise.service.*;
import java.time.YearMonth;

public class MockBudgetManager extends BudgetManager {
    private double currentBudget = 0.0;
    
    public MockBudgetManager(IDataStorage storage) {
        super(storage);
    }

    public boolean createBudget(String userId, double amount) {
        if (amount < 0) return false;
        currentBudget = amount;
        return true;
    }

    public double getCurrentMonthBudget(String userId) {
        return currentBudget;
    }

    public boolean isOverBudget(String userId, double currentExpenses, double proposedExpense) {
        return currentBudget > 0 && (currentExpenses + proposedExpense) > currentBudget;
    }

    public boolean updateBudget(String userId, double amount) {
        if (amount < 0) return false;
        currentBudget = amount;
        return true;
    }
}