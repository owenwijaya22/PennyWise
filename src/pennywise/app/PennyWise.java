package pennywise.app;

import pennywise.model.*;
import pennywise.utils.DiscountManager;
import pennywise.interfaces.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Optional;
import java.util.Scanner;
import java.util.Date;

public class PennyWise {
    private List<User> users;
    private IDataStorage dataStorage;
    private Scanner scanner;
    private String currentUserID;

    public PennyWise(IDataStorage dataStorage) {
        this.users = new ArrayList<>();
        this.dataStorage = dataStorage;
        this.scanner = new Scanner(System.in);
    }

    // Existing methods
    public String createUser(String name) {
        String userID = UUID.randomUUID().toString();
        User user = new User(userID, name);
        users.add(user);
        return userID;
    }

    public Optional<User> getUser(String userID) {
        return users.stream()
            .filter(u -> u.getUserID().equals(userID))
            .findFirst();
    }

    public void addTransaction(String userID, Transaction transaction) {
        getUser(userID).ifPresent(user -> user.addTransaction(transaction));
    }

    public void setUserBudget(String userID, float amount) {
        getUser(userID).ifPresent(user -> user.getBudgetManager().createBudget(userID, amount));
    }

    // Main application loop
    public void start() {
        boolean running = true;
        while (running) {
            if (currentUserID == null) {
                showLoginMenu();
            } else {
                showMainMenu();
            }
        }
    }

    private void showLoginMenu() {
        System.out.println("\n=== PennyWise ===");
        System.out.println("1. Login");
        System.out.println("2. Create Account");
        System.out.println("3. Exit");
        
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                handleLogin();
                break;
            case 2:
                handleCreateAccount();
                break;
            case 3:
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice");
        }
    }

    private void showMainMenu() {
        System.out.println("\n=== Main Menu ===");
        System.out.println("1. Add Income");
        System.out.println("2. Add Expense");
        System.out.println("3. View Balance");
        System.out.println("4. Set Budget");
        System.out.println("5. Logout");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                handleAddIncome();
                break;
            case 2:
                handleAddExpense();
                break;
            case 3:
                handleViewBalance();
                break;
            case 4:
                handleSetBudget();
                break;
            case 5:
                handleLogout();
                break;
            default:
                System.out.println("Invalid choice");
        }
    }

    private void handleLogin() {
        System.out.print("Enter user ID: ");
        String userID = scanner.nextLine();
        
        if (getUser(userID).isPresent()) {
            currentUserID = userID;
            System.out.println("Login successful!");
        } else {
            System.out.println("User not found!");
        }
    }

    private void handleCreateAccount() {
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        
        String userID = createUser(name);
        System.out.println("Account created! Your user ID is: " + userID);
        System.out.println("Please save this ID for login");
    }

    private float getUserTotalIncome(String userID) {
        Optional<User> user = getUser(userID);
        if (user.isPresent()) {
            return user.get().getTransactions().stream()
                .filter(t -> t instanceof Income)
                .map(Transaction::getAmount)
                .reduce(0f, Float::sum);
        }
        return 0f;
    }

    private float getUserTotalSpendings(String userID) {
        Optional<User> user = getUser(userID);
        if (user.isPresent()) {
            return user.get().getTransactions().stream()
                .filter(t -> t instanceof Expense)
                .map(Transaction::getAmount)
                .reduce(0f, Float::sum);
        }
        return 0f;
    }
    
    
    private void handleAddIncome() {
        System.out.print("Enter amount: ");
        float amount = scanner.nextFloat();
        scanner.nextLine(); // Consume newline
        
        Income income = new Income(new Date(), amount, IncomeCategory.OTHER);
        addTransaction(currentUserID, income);
        System.out.println("Income added successfully!");
    }

    private void handleAddExpense() {
        System.out.print("Enter amount: ");
        float amount = scanner.nextFloat();
        scanner.nextLine(); // Consume newline
        
        Expense expense = new Expense(new Date(), amount, ExpenseCategory.OTHER);
        addTransaction(currentUserID, expense);
        System.out.println("Expense added successfully!");
    }

    private void handleViewBalance() {
        float totalIncome = getUserTotalIncome(currentUserID);
        float totalExpense = getUserTotalSpendings(currentUserID);
        float balance = totalIncome - totalExpense;
        
        System.out.println("\nFinancial Summary:");
        System.out.println("Total Income: $" + totalIncome);
        System.out.println("Total Expense: $" + totalExpense);
        System.out.println("Current Balance: $" + balance);
    }

    private void handleSetBudget() {
        System.out.print("Enter budget amount: ");
        float amount = scanner.nextFloat();
        scanner.nextLine(); // Consume newline
        
        setUserBudget(currentUserID, amount);
        System.out.println("Budget set successfully!");
    }

    private void handleLogout() {
        currentUserID = null;
        System.out.println("Logged out successfully!");
    }

    // Main method
    public static void main(String[] args) {
        // Simple in-memory storage implementation
        IDataStorage storage = new IDataStorage() {
            private List<User> users = new ArrayList<>();

            public void saveData(List<User> users) {
                this.users = users;
            }

            public List<User> loadData() {
                return users;
            }

            // Implement other methods with empty returns
            public boolean saveUser(User user) { return true; }
            public User loadUser(String userID) { return null; }
            public boolean saveTransaction(String userID, Transaction transaction) { return true; }
            public List<Transaction> loadTransactions(String userID) { return new ArrayList<>(); }
            public boolean deleteUser(String userID) { return true; }
            public boolean clearAllData() { return true; }
        };

        PennyWise app = new PennyWise(storage);
        app.start();
    }
    
}