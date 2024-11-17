package pennywise.model;

import java.io.Serializable;

public class User implements Serializable {
	// Ensure that a loaded class corresponds exactly to a serialized object
	private static final long serialVersionUID = 1L;
    private final String userId;
//OLD CODE, extracted to BudgetManager.java
//    private final List<Budget> budgets;

    public User(String userId) {
        this.userId = userId;
//OLD CODE, extracted to BudgetManager.java
//        this.budgets = new ArrayList<>();
    }

    public String getUserId() {
        return userId;
    }
//OLD CODE, extracted to BudgetManager.java
//    public List<Budget> getBudgets() {
//        return new ArrayList<>(budgets);
//    }
//
//    public void addBudget(Budget budget) {
//        if (budget != null) {
//            budgets.add(budget);
//        }
//    }
}