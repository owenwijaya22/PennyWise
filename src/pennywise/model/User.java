package pennywise.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    private final String userId;
    private final List<Budget> budgets;
    private final List<Goal> goals;

    public User(String userId) {
        this.userId = userId;
        this.budgets = new ArrayList<>();
        this.goals = new ArrayList<>();
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

    public List<Goal> getGoals() {
        return new ArrayList<>(goals);
    }

    public void addGoal(Goal goal) {
        if (goal != null) {
            goals.add(goal);
        }
    }
}