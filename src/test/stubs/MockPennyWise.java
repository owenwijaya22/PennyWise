/*
 * 
 */
package test.stubs;

import pennywise.PennyWise;
import pennywise.model.*;
import pennywise.service.*;
import pennywise.interfaces.TransactionCategory;
import java.util.*;


/**
 * The Class MockPennyWise.
 */
public class MockPennyWise extends PennyWise {
    
    /** The current user. */
    private User currentUser;
    
    /** The is logged in. */
    private boolean isLoggedIn;
    
    /** The is registered. */
    private boolean isRegistered;
    
    /** The total income. */
    private double totalIncome = 0.0;
    
    /** The total expenses. */
    private double totalExpenses = 0.0;
    
    /** The transactions. */
    private List<Transaction> transactions = new ArrayList<>();
    
    /** The analyzer. */
    private TransactionAnalyzer analyzer;
    
    /** The budget manager. */
    private BudgetManager budgetManager;
    
    /** The logout called. */
    private boolean logoutCalled = false;

    /**
     * Instantiates a new mock penny wise.
     */
    public MockPennyWise() {
        super(null, null, null);
        this.analyzer = new MockTransactionAnalyzer();
        this.budgetManager = new MockBudgetManager(null);
        this.isRegistered = false;
    }

    /**
     * Login.
     *
     * @param userId the user id
     * @return true, if successful
     */
    @Override
    public boolean login(String userId) {
        if (userId != null && !userId.trim().isEmpty()) {
            currentUser = new User(userId);
            isLoggedIn = true;
            return true;
        }
        return false;
    }

    /**
     * Register user.
     *
     * @param userId the user id
     * @return true, if successful
     */
    @Override
    public boolean registerUser(String userId) {
        if (userId != null && !userId.trim().isEmpty()) {
            isRegistered = true;
            return true;
        }
        return false;
    }

    /**
     * Gets the current user.
     *
     * @return the current user
     */
    @Override
    public User getCurrentUser() {
        return isLoggedIn ? currentUser : null;
    }

    /**
     * Checks if is logged in.
     *
     * @return true, if is logged in
     */
    @Override
    public boolean isLoggedIn() {
        return isLoggedIn && currentUser != null;
    }

    /**
     * Logout.
     */
    @Override
    public void logout() {
        currentUser = null;
        isLoggedIn = false;
        logoutCalled = true;
        totalIncome = 0.0;
        totalExpenses = 0.0;
        transactions.clear();
    }

    /**
     * Delete account.
     *
     * @return true, if successful
     */
    @Override
    public boolean deleteAccount() {
        if (isLoggedIn) {
            logout();
            return true;
        }
        return false;
    }

    /**
     * Clear all user data.
     *
     * @return true, if successful
     */
    @Override
    public boolean clearAllUserData() {
        if (isLoggedIn) {
            transactions.clear();
            totalIncome = 0.0;
            totalExpenses = 0.0;
            return true;
        }
        return false;
    }

    /**
     * Gets the total income.
     *
     * @return the total income
     */
    @Override
    public double getTotalIncome() {
        return totalIncome;
    }

    /**
     * Gets the total expenses.
     *
     * @return the total expenses
     */
    @Override
    public double getTotalExpenses() {
        return totalExpenses;
    }

    /**
     * Gets the current balance.
     *
     * @return the current balance
     */
    @Override
    public double getCurrentBalance() {
        return totalIncome + totalExpenses;
    }

    /**
     * Gets the transactions.
     *
     * @return the transactions
     */
    @Override
    public List<Transaction> getTransactions() {
        return new ArrayList<>(transactions);
    }

    /**
     * Gets the analyzer.
     *
     * @return the analyzer
     */
    @Override
    public TransactionAnalyzer getAnalyzer() {
        return analyzer;
    }

    /**
     * Gets the budget manager.
     *
     * @return the budget manager
     */
    @Override
    public BudgetManager getBudgetManager() {
        return budgetManager;
    }

    /**
     * Sets the analyzer.
     *
     * @param analyzer the new analyzer
     */
    public void setAnalyzer(TransactionAnalyzer analyzer) {
        this.analyzer = analyzer;
    }

    /**
     * Sets the budget manager.
     *
     * @param budgetManager the new budget manager
     */
    public void setBudgetManager(BudgetManager budgetManager) {
        this.budgetManager = budgetManager;
    }

    /**
     * Was logout called.
     *
     * @return true, if successful
     */
    public boolean wasLogoutCalled() {
        return logoutCalled;
    }

    /**
     * Adds the transaction.
     *
     * @param amount the amount
     * @param category the category
     * @return true, if successful
     */
    @Override
    public boolean addTransaction(double amount, TransactionCategory category) {
        if (!isLoggedIn()) {
            return false;
        }
        
        if (category instanceof ExpenseCategory) {
            totalExpenses -= Math.abs(amount);
            transactions.add(new Transaction(currentUser.getUserId(), -Math.abs(amount), category));
        } else {
            double incomeAmount = Math.abs(amount);
            totalIncome += incomeAmount;
            transactions.add(new Transaction(currentUser.getUserId(), incomeAmount, category));
        }
        return true;
    }

    /**
     * Creates the budget.
     *
     * @param amount the amount
     * @return true, if successful
     */
    @Override
    public boolean createBudget(double amount) {
        if (currentUser == null || !isLoggedIn) {
            return false;
        }
        if (amount < 0) {
            return false;
        }
        return budgetManager.createBudget(currentUser.getUserId(), amount);
    }

    /**
     * Update budget.
     *
     * @param amount the amount
     * @return true, if successful
     */
    @Override
    public boolean updateBudget(double amount) {
        if (currentUser == null || !isLoggedIn) {
            return false;
        }
        if (amount < 0) {
            return false;
        }
        return budgetManager.updateBudget(currentUser.getUserId(), amount);
    }

    /**
     * Checks if is registered.
     *
     * @return true, if is registered
     */
    public boolean isRegistered() {
        return isRegistered;
    }

    /**
     * Sets the logged in.
     *
     * @param loggedIn the new logged in
     */
    public void setLoggedIn(boolean loggedIn) {
        this.isLoggedIn = loggedIn;
        if (!loggedIn) {
            this.currentUser = null;  // Clear current user when logging out
        }
    }

    /**
     * Clear current user.
     */
    public void clearCurrentUser() {
        this.currentUser = null;
    }
} 