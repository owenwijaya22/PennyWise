package pennywise.service;

import java.util.ArrayList;
import java.util.List;
import pennywise.model.Goal;

public class FinancialGoalPlanner {
    private List<Goal> goals;

    public FinancialGoalPlanner() {
        this.goals = new ArrayList<>();
    }

    public boolean setFinancialGoal(Goal goal) {
        return goals.add(goal);
    }

    public List<Goal> getAllGoals() {
        return new ArrayList<>(goals);
    }

    public boolean removeGoal(Goal goal) {
        return goals.remove(goal);
    }

    public Goal getGoalByDescription(String description) {
        return goals.stream()
                   .filter(g -> g.getDescription().equals(description))
                   .findFirst()
                   .orElse(null);
    }

    public void updateGoalProgress(Goal goal, double currentAmount) {
        goal.updateProgress(currentAmount);
    }
}
