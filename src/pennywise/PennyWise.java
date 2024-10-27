package pennywise;

import pennywise.interfaces.IDataStorage;
import pennywise.model.*;
import pennywise.storage.*;
import pennywise.service.*;
import pennywise.utils.DiscountManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class PennyWise {
    private final IDataStorage storage;
    private final BudgetManager budgetManager;
    private final ExpenseTracker expenseTracker;
    private TransactionAnalyzer analyzer;
    private User currentUser;
    private static Scanner scanner;

    public PennyWise(String dataDirectory) {
        this.storage = new FileDataStorage(dataDirectory);
        this.budgetManager = new BudgetManager(storage);
        this.expenseTracker = new ExpenseTracker(storage);
        this.analyzer = new TransactionAnalyzer(new ArrayList<>());
        scanner = new Scanner(System.in);
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

        if (storage.loadUser(userId) != null) {
            return false;
        }

        User newUser = new User(userId);
        return storage.saveUser(newUser);
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

    // Main application entry point and CLI methods
    public static void main(String[] args) {
        PennyWise pennywise = new PennyWise("./pennywise_data");

        while (true) {
            if (!pennywise.isLoggedIn()) {
                pennywise.showLoginMenu();
            } else {
                pennywise.showMainMenu();
            }
        }
    }

    private void showLoginMenu() {
        System.out.println("\n=== PennyWise Login ===");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Exit");
        System.out.print("Choose an option: ");

        String input = scanner.nextLine().trim();
        if (!input.matches("[1-3]")) {
            System.out.println("Error: Please enter a number between 1 and 3");
            return;
        }
        int choice = Integer.parseInt(input);

        switch (choice) {
            case 1:
                handleLogin();
                break;
            case 2:
                handleRegistration();
                break;
            case 3:
                System.out.println("Thank you for using PennyWise!");
                scanner.close();
                System.exit(0);
                break;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }

    private void showMainMenu() {
        while (true) {
            System.out.println("\n=== PennyWise Main Menu ===");
            System.out.println("1. Add Expense");
            System.out.println("2. Add Income");
            System.out.println("3. View Transactions");
            System.out.println("4. View Monthly Expenses");
            System.out.println("5. View Expenses by Category");
            System.out.println("6. Create Budget");
            System.out.println("7. View Budgets");
            System.out.println("8. View Balance");
            System.out.println("9. View Discounts");
            System.out.println("10. Logout");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    handleAddExpense();
                    break;
                case 2:
                    handleAddIncome();
                    break;
                case 3:
                    handleViewTransactions();
                    break;
                case 4:
                    handleViewMonthlyExpenses();
                    break;
                case 5:
                    handleViewExpensesByCategory();
                    break;
                case 6:
                    handleCreateBudget();
                    break;
                case 7:
                    handleViewBudgets();
                    break;
                case 8:
                    handleViewBalance();
                    break;
                case 9:
                    handleDiscountVisualization();
                    break;
                case 10:
                    logout();
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void handleLogin() {
        System.out.print("Enter user ID: ");
        String userId = scanner.nextLine();
        
        if (login(userId)) {
            System.out.println("Login successful!");
        } else {
            System.out.println("Login failed. User not found. Please register first.");
        }
    }

    private void handleRegistration() {
        System.out.print("Enter new user ID: ");
        String userId = scanner.nextLine();
        
        if (registerUser(userId)) {
            System.out.println("Registration successful! Please login to continue.");
        } else {
            System.out.println("Registration failed. User ID might already exist.");
        }
    }

    private void handleAddExpense() {
        System.out.print("Enter amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Enter description: ");
        String description = scanner.nextLine();

        System.out.print("Do you have a discount code? (Y/N): ");
        String hasDiscount = scanner.nextLine().trim().toUpperCase();
        
        if (hasDiscount.equals("Y")) {
            System.out.print("Enter discount code: ");
            String discountCode = scanner.nextLine().trim().toUpperCase();
            
            DiscountManager discountManager = DiscountManager.getInstance();
            Discount discount = discountManager.findDiscountByCode(discountCode);
            
            if (discount != null && discount.isValid()) {
                double discountAmount = amount * (discount.getPercentage() / 100);
                amount -= discountAmount;
                System.out.printf("Discount applied: -$%.2f (%.0f%%)%n", discountAmount, discount.getPercentage());
                description += String.format(" (Discount: %s)", discountCode);
            } else {
                System.out.println("Invalid or expired discount code.");
            }
        }

        System.out.println("Select category:");
        ExpenseCategory[] categories = ExpenseCategory.values();
        for (int i = 0; i < categories.length; i++) {
            System.out.println((i + 1) + ". " + categories[i]);
        }
        int categoryChoice = scanner.nextInt() - 1;
        scanner.nextLine();

        if (categoryChoice >= 0 && categoryChoice < categories.length) {
            if (addExpense(amount, description, categories[categoryChoice])) {
                System.out.println("Expense added successfully!");
            } else {
                System.out.println("Failed to add expense.");
            }
        } else {
            System.out.println("Invalid category selection.");
        }
    }

    private void handleAddIncome() {
        System.out.print("Enter amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Enter description: ");
        String description = scanner.nextLine();

        System.out.println("Select category:");
        IncomeCategory[] categories = IncomeCategory.values();
        for (int i = 0; i < categories.length; i++) {
            System.out.println((i + 1) + ". " + categories[i]);
        }
        int categoryChoice = scanner.nextInt() - 1;
        scanner.nextLine();

        if (categoryChoice >= 0 && categoryChoice < categories.length) {
            if (addIncome(amount, description, categories[categoryChoice])) {
                System.out.println("Income added successfully!");
            } else {
                System.out.println("Failed to add income.");
            }
        } else {
            System.out.println("Invalid category selection.");
        }
    }

    private void handleViewTransactions() {
        List<Transaction> transactions = getTransactions();
        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }

        System.out.println("\n=== Transactions ===");
        for (Transaction t : transactions) {
            System.out.printf("%s: $%.2f - %s (%s)%n",
                    t.getType(), t.getAmount(), t.getDescription(), t.getDate());
        }
    }

    private void handleCreateBudget() {
        System.out.print("Enter budget category: ");
        String category = scanner.nextLine();

        System.out.print("Enter budget amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        if (createBudget(category, amount)) {
            System.out.println("Budget created successfully!");
        } else {
            System.out.println("Failed to create budget.");
        }
    }

    private void handleViewBudgets() {
        List<Budget> budgets = getBudgets();
        if (budgets.isEmpty()) {
            System.out.println("No budgets found.");
            return;
        }

        System.out.println("\n=== Budgets ===");
        for (Budget b : budgets) {
            System.out.printf("%s: $%.2f%n", b.getCategory(), b.getAmount());
        }
    }

    private void handleViewMonthlyExpenses() {
        TransactionAnalyzer analyzer = getAnalyzer();
        if (analyzer == null) {
            System.out.println("Please log in to view monthly expenses.");
            return;
        }

        Map<Object, Double> monthlyExpenses = analyzer.getMonthlyExpenses();
        System.out.println("\n=== Monthly Expenses ===");
        if (monthlyExpenses.isEmpty()) {
            System.out.println("No expenses recorded yet.");
        } else {
            monthlyExpenses.forEach((month, amount) -> 
                System.out.printf("%s: $%.2f%n", month, amount));
        }
    }

    private void handleViewExpensesByCategory() {
        TransactionAnalyzer analyzer = getAnalyzer();
        if (analyzer == null) {
            System.out.println("Please log in to view expenses by category.");
            return;
        }

        Map<Object, Double> categoryExpenses = analyzer.getExpensesByCategory();
        System.out.println("\n=== Expenses by Category ===");
        if (categoryExpenses.isEmpty()) {
            System.out.println("No expenses recorded yet.");
        } else {
            categoryExpenses.forEach((category, amount) -> 
                System.out.printf("%s: $%.2f%n", category, amount));
        }
    }

    private void handleViewBalance() {
        System.out.println("\n=== Financial Summary ===");
        System.out.printf("Total Income: $%.2f%n", getTotalIncome());
        System.out.printf("Total Expenses: $%.2f%n", getTotalExpenses());
        System.out.printf("Current Balance: $%.2f%n", getCurrentBalance());
    }

    private void handleDiscountVisualization() {
        DiscountManager discountManager = DiscountManager.getInstance();
        
        while (true) {
            System.out.println("\n=== Discount Management ===");
            System.out.println("1. View All Discounts");
            System.out.println("2. Add Custom Discount");
            System.out.println("3. Add Predetermined Discount");
            System.out.println("4. Return to Main Menu");
            
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            switch (choice) {
                case 1:
                    displayDiscounts(discountManager.getAvailableDiscounts());
                    break;
                case 2:
                    addCustomDiscount(discountManager);
                    break;
                case 3:
                    addPredeterminedDiscount(discountManager);
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void addCustomDiscount(DiscountManager discountManager) {
        System.out.println("\n=== Add Custom Discount ===");
        
        System.out.print("Enter discount code: ");
        String code = scanner.nextLine().toUpperCase();
        
        System.out.print("Enter discount percentage (without % symbol): ");
        float percentage = scanner.nextFloat();
        scanner.nextLine();
        
        System.out.print("Enter description: ");
        String description = scanner.nextLine();
        
        System.out.println("\nExpiry date:");
        System.out.print("Enter days from now: ");
        int days = scanner.nextInt();
        scanner.nextLine();
        
        Date expiryDate = new Date(System.currentTimeMillis() + (long)days * 24 * 60 * 60 * 1000);
        
        Discount newDiscount = new Discount(code, percentage, expiryDate, description);
        discountManager.addDiscount(newDiscount);
        
        System.out.println("\nDiscount added successfully!");
    }

    private void addPredeterminedDiscount(DiscountManager discountManager) {
        System.out.println("\n=== Predetermined Discounts ===");
        System.out.println("1. Apple Student Discount");
        System.out.println("2. Octopus Student Discount");
        System.out.println("3. GitHub Education (Teacher/Staff)");
        System.out.println("4. Spotify Student Discount");
        System.out.println("5. Microsoft Office 365 Education");
        System.out.println("6. Adobe Creative Cloud (Student)");
        System.out.println("7. Return");
        
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        Date defaultExpiry = new Date(System.currentTimeMillis() + 365L * 24 * 60 * 60 * 1000);
        
        Discount selectedDiscount = null;
        switch (choice) {
            case 1:
                selectedDiscount = new Discount(
                    "APPLEEDU",
                    10.0f,
                    defaultExpiry,
                    "Apple Education Store - Up to 10% off on Mac, iPad, and accessories"
                );
                break;
            case 2:
                selectedDiscount = new Discount(
                    "OCTOPUSSTUDENT",
                    20.0f,
                    defaultExpiry,
                    "Octopus Student Status - 20% off on transportation fares"
                );
                break;
            case 3:
                selectedDiscount = new Discount(
                    "GITEDU",
                    100.0f,
                    defaultExpiry,
                    "GitHub Education Pack - Free Pro features for verified teachers/staff"
                );
                break;
            case 4:
                selectedDiscount = new Discount(
                    "SPOTIFYEDU",
                    50.0f,
                    defaultExpiry,
                    "Spotify Premium Student - 50% off monthly subscription"
                );
                break;
            case 5:
                selectedDiscount = new Discount(
                    "MS365EDU",
                    100.0f,
                    defaultExpiry,
                    "Free Microsoft 365 Apps for Education with valid school email"
                );
                break;
            case 6:
                selectedDiscount = new Discount(
                    "ADOBEEDU",
                    60.0f,
                    defaultExpiry,
                    "Adobe Creative Cloud - 60% off for students and teachers"
                );
                break;
            case 7:
                return;
            default:
                System.out.println("Invalid choice.");
                return;
        }
        
        if (selectedDiscount != null) {
            discountManager.addDiscount(selectedDiscount);
            System.out.println("\nPredetermined discount added successfully!");
        }
    }

    private void displayDiscounts(List<Discount> discounts) {
        if (discounts.isEmpty()) {
            System.out.println("\nNo discounts available currently.");
            return;
        }

        System.out.println("\n╔═══════════════════ Active Discounts ═══════════════════╗");
        
        for (Discount discount : discounts) {
            boolean isValid = discount.isValid();
            String status = isValid ? "\u001B[32m▣ ACTIVE\u001B[0m" : "\u001B[31m▢ EXPIRED\u001B[0m";
            
            long diffInMillies = discount.getExpiryDate().getTime() - System.currentTimeMillis();
            long diffInDays = diffInMillies / (24 * 60 * 60 * 1000);
            
            String timeInfo = isValid ? 
                String.format("\u001B[32m%d days remaining\u001B[0m", diffInDays) :
                String.format("\u001B[31mExpired %d days ago\u001B[0m", Math.abs(diffInDays));

            int barLength = 20;
            int filledBars = (int)((discount.getPercentage() / 100) * barLength);
            String percentageBar = "█".repeat(Math.min(filledBars, barLength)) + 
                                 "░".repeat(Math.max(0, barLength - filledBars));

            System.out.println("╠═══════════════════════════════════════════════════════╣");
            System.out.printf("║ Code: \u001B[1m%s\u001B[0m %s%n", discount.getCode(), status);
            System.out.printf("║ Discount: [%s] %.0f%%%n", percentageBar, discount.getPercentage());
            System.out.printf("║ Description: %s%n", discount.getDescription());
            System.out.printf("║ Expires: %s%n", discount.getExpiryDate().toString());
            System.out.printf("║ Status: %s%n", timeInfo);
        }

        System.out.println("╚═══════════════════════════════════════════════════════════╝");
        
        long activeCount = discounts.stream().filter(Discount::isValid).count();
        double avgDiscount = discounts.stream()
            .filter(Discount::isValid)
            .mapToDouble(Discount::getPercentage)
            .average()
            .orElse(0.0);

        System.out.println("\n📊 Quick Stats:");
        System.out.printf("Active Discounts: %d/%d%n", activeCount, discounts.size());
        System.out.printf("Average Discount: %.1f%%%n", avgDiscount);
    }
}