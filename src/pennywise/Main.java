package pennywise;

import pennywise.app.PennyWise;
import pennywise.model.*;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static PennyWise pennywise;
    private static Scanner scanner;

    public static void main(String[] args) {
        // Initialize the application with a data directory
        pennywise = new PennyWise("./pennywise_data");
        scanner = new Scanner(System.in);

        while (true) {
            if (!pennywise.isLoggedIn()) {
                showLoginMenu();
            } else {
                showMainMenu();
            }
        }
    }

    private static void showLoginMenu() {
        System.out.println("\n=== PennyWise Login ===");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Exit");
        System.out.print("Choose an option: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                handleLogin();
                break;
            case 2:
                handleRegistration();
                break;
            case 3:
                System.out.println("Thank you for using PennyWise!");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }

    private static void showMainMenu() {
        while (true) {
            System.out.println("\n=== PennyWise Main Menu ===");
            System.out.println("1. Add Expense");
            System.out.println("2. Add Income");
            System.out.println("3. View Transactions");
            System.out.println("4. Create Budget");
            System.out.println("5. View Budgets");
            System.out.println("6. View Balance");
            System.out.println("7. Logout");
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
                    handleCreateBudget();
                    break;
                case 5:
                    handleViewBudgets();
                    break;
                case 6:
                    handleViewBalance();
                    break;
                case 7:
                    pennywise.logout();
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void handleLogin() {
        System.out.print("Enter user ID: ");
        String userId = scanner.nextLine();
        
        if (pennywise.login(userId)) {
            System.out.println("Login successful!");
        } else {
            System.out.println("Login failed. User not found. Please register first.");
        }
    }

    private static void handleRegistration() {
        System.out.print("Enter new user ID: ");
        String userId = scanner.nextLine();
        
        if (pennywise.registerUser(userId)) {
            System.out.println("Registration successful! Please login to continue.");
        } else {
            System.out.println("Registration failed. User ID might already exist.");
        }
    }

    private static void handleAddExpense() {
        System.out.print("Enter amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter description: ");
        String description = scanner.nextLine();

        System.out.println("Select category:");
        ExpenseCategory[] categories = ExpenseCategory.values();
        for (int i = 0; i < categories.length; i++) {
            System.out.println((i + 1) + ". " + categories[i]);
        }
        int categoryChoice = scanner.nextInt() - 1;
        scanner.nextLine(); // Consume newline

        if (categoryChoice >= 0 && categoryChoice < categories.length) {
            if (pennywise.addExpense(amount, description, categories[categoryChoice])) {
                System.out.println("Expense added successfully!");
            } else {
                System.out.println("Failed to add expense.");
            }
        } else {
            System.out.println("Invalid category selection.");
        }
    }

    private static void handleAddIncome() {
        System.out.print("Enter amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter description: ");
        String description = scanner.nextLine();

        System.out.println("Select category:");
        IncomeCategory[] categories = IncomeCategory.values();
        for (int i = 0; i < categories.length; i++) {
            System.out.println((i + 1) + ". " + categories[i]);
        }
        int categoryChoice = scanner.nextInt() - 1;
        scanner.nextLine(); // Consume newline

        if (categoryChoice >= 0 && categoryChoice < categories.length) {
            if (pennywise.addIncome(amount, description, categories[categoryChoice])) {
                System.out.println("Income added successfully!");
            } else {
                System.out.println("Failed to add income.");
            }
        } else {
            System.out.println("Invalid category selection.");
        }
    }

    private static void handleViewTransactions() {
        List<Transaction> transactions = pennywise.getTransactions();
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

    private static void handleCreateBudget() {
        System.out.print("Enter budget category: ");
        String category = scanner.nextLine();

        System.out.print("Enter budget amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        if (pennywise.createBudget(category, amount)) {
            System.out.println("Budget created successfully!");
        } else {
            System.out.println("Failed to create budget.");
        }
    }

    private static void handleViewBudgets() {
        List<Budget> budgets = pennywise.getBudgets();
        if (budgets.isEmpty()) {
            System.out.println("No budgets found.");
            return;
        }

        System.out.println("\n=== Budgets ===");
        for (Budget b : budgets) {
            System.out.printf("%s: $%.2f%n", b.getCategory(), b.getAmount());
        }
    }

    private static void handleViewBalance() {
        System.out.println("\n=== Financial Summary ===");
        System.out.printf("Total Income: $%.2f%n", pennywise.getTotalIncome());
        System.out.printf("Total Expenses: $%.2f%n", pennywise.getTotalExpenses());
        System.out.printf("Current Balance: $%.2f%n", pennywise.getCurrentBalance());
    }
}