/*
 * 
 */
package pennywise;

import pennywise.interfaces.IDataStorage;
import pennywise.interfaces.TransactionCategory;
import pennywise.model.*;
import pennywise.storage.*;
import pennywise.service.*;
import pennywise.ui.ConsoleUI;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;


/**
 * The Class PennyWise.
 */
public class PennyWise {
    
    /** The storage. */
    private final IDataStorage storage;
    
    /** The auth service. */
    private final AuthenticationService authService;
    
    /** The budget manager. */
    private final BudgetManager budgetManager;
    
    /** The transaction manager. */
    private final TransactionManager transactionManager;
    
    /** The analyzer. */
    private TransactionAnalyzer analyzer;
    
    /** The ui. */
    protected ConsoleUI ui;

    /**
     * Instantiates a new penny wise.
     *
     * @param dataDirectory the data directory
     */
    public PennyWise(String dataDirectory) {
        this.storage = new FileDataStorage(dataDirectory);
        this.authService = new AuthenticationService(storage);
        this.budgetManager = new BudgetManager(storage);
        this.transactionManager = new TransactionManager(storage);
        this.analyzer = new TransactionAnalyzer(new ArrayList<>());
        this.ui = new ConsoleUI(this);
    }

    /**
     * Instantiates a new penny wise.
     *
     * @param storage the storage
     * @param budgetManager the budget manager
     * @param analyzer the analyzer
     */
    public PennyWise(IDataStorage storage, BudgetManager budgetManager, TransactionAnalyzer analyzer) {
        this.storage = storage;
        this.authService = new AuthenticationService(storage);
        this.budgetManager = budgetManager;
        this.transactionManager = new TransactionManager(storage);
        this.analyzer = analyzer;
    }

    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
        PennyWise pennywise = new PennyWise("./pennywise_data");
        pennywise.ui.start();
    }

    /**
     * Login.
     *
     * @param userId the user id
     * @return true, if successful
     */
    // Authentication methods remain unchanged
    public boolean login(String userId) {
        return authService.login(userId);
    }

    /**
     * Register user.
     *
     * @param userId the user id
     * @return true, if successful
     */
    public boolean registerUser(String userId) {
        return authService.register(userId);
    }

    /**
     * Logout.
     */
    public void logout() {
        authService.logout();
    }

    /**
     * Checks if is logged in.
     *
     * @return true, if is logged in
     */
    public boolean isLoggedIn() {
        return authService.getCurrentUser() != null;
    }

    /**
     * Adds the transaction.
     *
     * @param amount the amount
     * @param category the category
     * @return true, if successful
     */
    public  boolean addTransaction(double amount, TransactionCategory category) {
        User currentUser = authService.getCurrentUser();
        if (currentUser == null || amount <= 0) {
            return false;
        }
        // OLD CODE, removed to implement strategy design pattern
        // if (category.getTransactionType() == TransactionType.EXPENSE) {
        if (category.isExpenseCategory()) {
            double monthlyBudget = budgetManager.getCurrentMonthBudget(currentUser.getUserId());
            if (monthlyBudget > 0) {
                double currentMonthExpenses = getTotalExpenses();
                if (budgetManager.isOverBudget(currentUser.getUserId(), currentMonthExpenses, amount)) {
                    return false;
                }
            }
        }
        
        return transactionManager.addTransaction(currentUser.getUserId(), amount, category);
    }

    /**
     * Gets the transactions.
     *
     * @return the transactions
     */
    public List<Transaction> getTransactions() {
        User currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            return List.of();
        }
        return transactionManager.getTransactions(currentUser.getUserId());
    }

    /**
     * Creates the budget.
     *
     * @param amount the amount
     * @return true, if successful
     */
    public boolean createBudget(double amount) {
        User currentUser = authService.getCurrentUser();
        if (currentUser == null || amount < 0) {
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
    public boolean updateBudget(double amount) {
        User currentUser = authService.getCurrentUser();
        if (currentUser == null || amount < 0) {
            return false;
        }
        return budgetManager.updateBudget(currentUser.getUserId(), amount);
    }

    /**
     * Gets the analyzer.
     *
     * @return the analyzer
     */
    // Modified Analysis methods
    public TransactionAnalyzer getAnalyzer() {
        User currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            return null;
        }
        List<Transaction> currentTransactions = getTransactions();
        analyzer.updateTransactions(currentTransactions);
        return analyzer;
    }

    /**
     * Gets the total income.
     *
     * @return the total income
     */
    public double getTotalIncome() {
        User currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            return 0.0;
        }
        return transactionManager.getTransactions(currentUser.getUserId()).stream()
                .filter(t -> t.getAmount() > 0)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    /**
     * Gets the total expenses.
     *
     * @return the total expenses
     */
    public double getTotalExpenses() {
        User currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            return 0.0;
        }
        YearMonth currentMonth = YearMonth.now();
        return transactionManager.getTransactions(currentUser.getUserId()).stream()
//        		OLD CODE, removed to implement strategy design pattern
//        		.filter(t -> t.getType() == TransactionType.EXPENSE)
        		.filter(t -> t.getAmount() < 0)
            .filter(t -> {
                YearMonth transactionMonth = YearMonth.from(t.getDate().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate());
                return transactionMonth.equals(currentMonth);
            })
            .mapToDouble(t -> Math.abs(t.getAmount()))
            .sum();
    }

    /**
     * Gets the current balance.
     *
     * @return the current balance
     */
    public double getCurrentBalance() {
        User currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            return 0.0;
        }
        return transactionManager.getTransactions(currentUser.getUserId()).stream()
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    /**
     * Delete account.
     *
     * @return true, if successful
     */
    // Account management methods remain unchanged
    public boolean deleteAccount() {
        User currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            return false;
        }
        boolean deleted = storage.deleteUser(currentUser.getUserId());
        if (deleted) {
            logout();
        }
        return deleted;
    }

    /**
     * Clear all user data.
     *
     * @return true, if successful
     */
    public boolean clearAllUserData() {
        User currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            return false;
        }
        String userId = currentUser.getUserId();
        logout();
        return storage.deleteUser(userId);
    }

    /**
     * Reset application.
     *
     * @param dataDirectory the data directory
     * @return true, if successful
     */
    public static boolean resetApplication(String dataDirectory) {
        IDataStorage storage = new FileDataStorage(dataDirectory);
        return storage.clearAllData();
    }

    /**
     * Gets the current user.
     *
     * @return the current user
     */
    // Getter methods for testing
    public User getCurrentUser() {
        return authService.getCurrentUser();
    }

    /**
     * Gets the budget manager.
     *
     * @return the budget manager
     */
    public BudgetManager getBudgetManager() {
        return budgetManager;
    }
}