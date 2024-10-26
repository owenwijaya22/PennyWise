package pennywise.app;

import pennywise.interfaces.IDataStorage;
import pennywise.model.*;
import pennywise.storage.*;
import pennywise.service.*;

import java.util.ArrayList;
import java.util.List;
import java.lang.Math;

public class PennyWise {
    private final IDataStorage storage;
    private final BudgetManager budgetManager;
    private final ExpenseTracker expenseTracker;
    private TransactionAnalyzer analyzer;
    private User currentUser;

    public PennyWise(String dataDirectory) {
        this.storage = new FileDataStorage(dataDirectory);
        this.budgetManager = new BudgetManager(storage);
        this.expenseTracker = new ExpenseTracker(storage);
        this.analyzer = new TransactionAnalyzer(new ArrayList<>());
    }

    public boolean login(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            return false;
        }

        User user = storage.loadUser(userId);
        if (user != null) {
            currentUser = user;
            return true;
        }
        return false;
    }

    public boolean registerUser(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            return false;
        }

        // Check if user already exists
        if (storage.loadUser(userId) != null) {
            return false;
        }

        User newUser = new User(userId);
        boolean success = storage.saveUser(newUser);
        // Don't automatically log in after registration
        return success;
    }

    public boolean deleteAccount() {
        if (currentUser == null) {
            return false;
        }
        boolean deleted = storage.deleteUser(currentUser.getUserId());
        if (deleted) {
            logout();
        }
        return deleted;
    }

    public boolean addExpense(double amount, String description, ExpenseCategory category) {
        if (currentUser == null || amount <= 0 || description == null || description.trim().isEmpty()) {
            return false;
        }
        return expenseTracker.addExpense(currentUser.getUserId(), amount, description, category);
    }

    public boolean addIncome(double amount, String description, IncomeCategory category) {
        if (currentUser == null || amount <= 0 || description == null || description.trim().isEmpty()) {
            return false;
        }
        return expenseTracker.addIncome(currentUser.getUserId(), amount, description, category);
    }

    public boolean createBudget(String category, double amount) {
        if (currentUser == null || amount < 0 || category == null || category.trim().isEmpty()) {
            return false;
        }
        return budgetManager.createBudget(currentUser.getUserId(), category, amount);
    }

    public List<Transaction> getTransactions() {
        if (currentUser == null) {
            return List.of();
        }
        return expenseTracker.getTransactions(currentUser.getUserId());
    }

    public List<Budget> getBudgets() {
        if (currentUser == null) {
            return List.of();
        }
        return budgetManager.getBudgets(currentUser.getUserId());
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void logout() {
        currentUser = null;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public boolean updateUserProfile(User updatedUser) {
        if (currentUser == null || updatedUser == null || 
            !currentUser.getUserId().equals(updatedUser.getUserId())) {
            return false;
        }
        boolean success = storage.saveUser(updatedUser);
        if (success) {
            currentUser = updatedUser;
        }
        return success;
    }
 // Add this method to get the analyzer
    public TransactionAnalyzer getAnalyzer() {
        if (currentUser == null) {
            return null;
        }
        List<Transaction> currentTransactions = getTransactions();
        analyzer.updateTransactions(currentTransactions);
        return analyzer;
    }
    public double getTotalIncome() {
        if (currentUser == null) {
            return 0.0;
        }
        return expenseTracker.getTransactions(currentUser.getUserId()).stream()
                .filter(t -> t.getType() == TransactionType.INCOME)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public double getTotalExpenses() {
        if (currentUser == null) {
            return 0.0;
        }
        return expenseTracker.getTransactions(currentUser.getUserId()).stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .mapToDouble(t -> Math.abs(t.getAmount()))
                .sum();
    }

    public double getCurrentBalance() {
        if (currentUser == null) {
            return 0.0;
        }
        return expenseTracker.getTransactions(currentUser.getUserId()).stream()
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public boolean clearAllUserData() {
        if (currentUser == null) {
            return false;
        }
        String userId = currentUser.getUserId();
        logout();
        return storage.deleteUser(userId);
    }

    public static boolean resetApplication(String dataDirectory) {
        IDataStorage storage = new FileDataStorage(dataDirectory);
        return storage.clearAllData();
    }
    
}