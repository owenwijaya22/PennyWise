/*
 * 
 */
package pennywise.model;

import java.io.Serializable;


/**
 * The Class User.
 */
public class User implements Serializable {
	
	/** The Constant serialVersionUID. */
	// Ensure that a loaded class corresponds exactly to a serialized object
	private static final long serialVersionUID = 1L;
    
    /** The user id. */
    private final String userId;
//OLD CODE, extracted to BudgetManager.java
//    private final List<Budget> budgets;

    /**
 * Instantiates a new user.
 *
 * @param userId the user id
 */
public User(String userId) {
        this.userId = userId;
//OLD CODE, extracted to BudgetManager.java
//        this.budgets = new ArrayList<>();
    }

    /**
     * Gets the user id.
     *
     * @return the user id
     */
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