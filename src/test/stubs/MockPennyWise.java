package test.stubs;

import pennywise.PennyWise;
import pennywise.model.*;
import pennywise.service.*;
import pennywise.interfaces.TransactionCategory;
import java.util.*;

public class MockPennyWise extends PennyWise {
    private User currentUser;
    private boolean isLoggedIn;
    private boolean isRegistered;
    private double totalIncome = 0.0;
    private double totalExpenses = 0.0;
    private List<Transaction> transactions = new ArrayList<>();
    private TransactionAnalyzer analyzer;
    private BudgetManager budgetManager;
    private boolean logoutCalled = false;

    public MockPennyWise() {
        super(null, null, null);
        this.analyzer = new MockTransactionAnalyzer();
        this.budgetManager = new MockBudgetManager(null);
        this.isRegistered = false;
    }

    @Override
    public boolean login(String userId) {
        if (userId != null && !userId.trim().isEmpty()) {
            currentUser = new User(userId);
            isLoggedIn = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean registerUser(String userId) {
        if (userId != null && !userId.trim().isEmpty()) {
            isRegistered = true;
            return true;
        }
        return false;
    }

    @Override
    public User getCurrentUser() {
        return isLoggedIn ? currentUser : null;
    }

    @Override
    public boolean isLoggedIn() {
        return isLoggedIn && currentUser != null;
    }

    @Override
    public void logout() {
        currentUser = null;
        isLoggedIn = false;
        logoutCalled = true;
        totalIncome = 0.0;
        totalExpenses = 0.0;
        transactions.clear();
    }

    @Override
    public boolean deleteAccount() {
        if (isLoggedIn) {
            logout();
            return true;
        }
        return false;
    }

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

    @Override
    public double getTotalIncome() {
        return totalIncome;
    }

    @Override
    public double getTotalExpenses() {
        return totalExpenses;
    }

    @Override
    public double getCurrentBalance() {
        return totalIncome + totalExpenses;
    }

    @Override
    public List<Transaction> getTransactions() {
        return new ArrayList<>(transactions);
    }

    @Override
    public TransactionAnalyzer getAnalyzer() {
        return analyzer;
    }

    @Override
    public BudgetManager getBudgetManager() {
        return budgetManager;
    }

    public void setAnalyzer(TransactionAnalyzer analyzer) {
        this.analyzer = analyzer;
    }

    public void setBudgetManager(BudgetManager budgetManager) {
        this.budgetManager = budgetManager;
    }

    public boolean wasLogoutCalled() {
        return logoutCalled;
    }

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

    public boolean isRegistered() {
        return isRegistered;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.isLoggedIn = loggedIn;
        if (!loggedIn) {
            this.currentUser = null;  // Clear current user when logging out
        }
    }

    public void clearCurrentUser() {
        this.currentUser = null;
    }
} 