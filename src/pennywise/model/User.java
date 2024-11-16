package pennywise.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    private final String userId;
    private final List<Budget> budgets;

    public User(String userId) {
        this.userId = userId;
        this.budgets = new ArrayList<>();
    }

    public String getUserId() {
        return userId;
    }

    public List<Budget> getBudgets() {
        return new ArrayList<>(budgets);
    }

    public void addBudget(Budget budget) {
        if (budget != null) {
            budgets.add(budget);
        }
    }
}