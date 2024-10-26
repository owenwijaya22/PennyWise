package pennywise.service;

import pennywise.model.Budget;
import java.util.HashMap;
import java.util.Map;

public class BudgetManager {
    private Map<String, Budget> budgets;

    public BudgetManager() {
        this.budgets = new HashMap<>();
    }

    public boolean createBudget(String id, float amount) {
        if (amount < 0) {
            return false;
        }
        Budget budget = new Budget(id, amount);
        budgets.put(id, budget);
        return true;
    }

    public void editBudget(String id, float newAmount) {
        if (budgets.containsKey(id)) {
            budgets.get(id).setAmount(newAmount);
        }
    }

    public Budget viewBudget(String id) {
        return budgets.get(id);
    }

    public Map<String, Budget> getAllBudgets() {
        return new HashMap<>(budgets);
    }

    public void deleteBudget(String id) {
        budgets.remove(id);
    }

    public void recordExpense(String id, float amount) {
        Budget budget = budgets.get(id);
        if (budget != null) {
            budget.addExpense(amount);
        }
    }

    public boolean isOverBudget(String id) {
        Budget budget = budgets.get(id);
        return budget != null && budget.getSpent() > budget.getAmount();
    }
}