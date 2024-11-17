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

public class PennyWise {
    private final IDataStorage storage;
    private final AuthenticationService authService;
    private final BudgetManager budgetManager;
    private final TransactionManager transactionManager;
    private TransactionAnalyzer analyzer;
    protected ConsoleUI ui;

    public PennyWise(String dataDirectory) {
        this.storage = new FileDataStorage(dataDirectory);
        this.authService = new AuthenticationService(storage);
        this.budgetManager = new BudgetManager(storage);
        this.transactionManager = new TransactionManager(storage);
        this.analyzer = new TransactionAnalyzer(new ArrayList<>());
        this.ui = new ConsoleUI(this);
    }

    public PennyWise(IDataStorage storage, BudgetManager budgetManager, TransactionAnalyzer analyzer) {
        this.storage = storage;
        this.authService = new AuthenticationService(storage);
        this.budgetManager = budgetManager;
        this.transactionManager = new TransactionManager(storage);
        this.analyzer = analyzer;
    }

    public static void main(String[] args) {
        PennyWise pennywise = new PennyWise("./pennywise_data");
        pennywise.ui.start();
    }

    // Authentication methods remain unchanged
    public boolean login(String userId) {
        return authService.login(userId);
    }

    public boolean registerUser(String userId) {
        return authService.register(userId);
    }

    public void logout() {
        authService.logout();
    }

    public boolean isLoggedIn() {
        return authService.getCurrentUser() != null;
    }

    public  boolean addTransaction(double amount, TransactionCategory category) {
        User currentUser = authService.getCurrentUser();
        if (currentUser == null || amount <= 0) {
            return false;
        }

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

    public List<Transaction> getTransactions() {
        User currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            return List.of();
        }
        return transactionManager.getTransactions(currentUser.getUserId());
    }

    public boolean createBudget(double amount) {
        User currentUser = authService.getCurrentUser();
        if (currentUser == null || amount < 0) {
            return false;
        }
        return budgetManager.createBudget(currentUser.getUserId(), amount);
    }
    
    public boolean updateBudget(double amount) {
        User currentUser = authService.getCurrentUser();
        if (currentUser == null || amount < 0) {
            return false;
        }
        return budgetManager.updateBudget(currentUser.getUserId(), amount);
    }

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

    public double getTotalExpenses() {
        User currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            return 0.0;
        }
        YearMonth currentMonth = YearMonth.now();
        return transactionManager.getTransactions(currentUser.getUserId()).stream()
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

    public double getCurrentBalance() {
        User currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            return 0.0;
        }
        return transactionManager.getTransactions(currentUser.getUserId()).stream()
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

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

    public boolean clearAllUserData() {
        User currentUser = authService.getCurrentUser();
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

    // Getter methods for testing
    public User getCurrentUser() {
        return authService.getCurrentUser();
    }

    public BudgetManager getBudgetManager() {
        return budgetManager;
    }
}