package pennywise.ui;

public final class UIConstants {
    // Menu Titles
    public static final String LOGIN_MENU_TITLE = "\n=== PennyWise Login ===";
    public static final String MAIN_MENU_TITLE = "\n=== PennyWise Main Menu ===";
    public static final String DISCOUNT_MENU_TITLE = "\n=== Discount Management ===";
    public static final String ACCOUNT_MENU_TITLE = "\n=== Account Management ===";
    public static final String BUDGET_MENU_TITLE = "\n=== Monthly Budget Status ===";
    public static final String TRANSACTIONS_TITLE = "\n=== Transactions ===";
    public static final String MONTHLY_EXPENSES_TITLE = "\n=== Monthly Expenses ===";
    public static final String MONTHLY_INCOMES_TITLE = "\n=== Monthly Incomes ===";
    public static final String EXPENSES_CATEGORY_TITLE = "\n=== Expenses by Category ===";
    public static final String INCOMES_CATEGORY_TITLE = "\n=== Incomes by Category ===";
    public static final String FINANCIAL_SUMMARY_TITLE = "\n=== Financial Summary ===";
    public static final String CUSTOM_DISCOUNT_TITLE = "\n=== Add Custom Discount ===";
    public static final String ACCOUNT_MANAGEMENT_TITLE = "\n=== Account Management ===";
    public static final String PREDETERMINED_DISCOUNT_TITLE = "\n=== Predetermined Discounts ===";
    public static final String UPDATED_BUDGET_TITLE = "\n=== Updated Budget Status ===";
    public static final String EDIT_MONTHLY_BUDGET_TITLE = "\n=== Edit Monthly Budget ===";
    public static final String SET_MONTHLY_BUDGET_TITLE = "\n=== Set Monthly Budget ===";
    
    // Messages
    public static final String INVALID_INPUT_MESSAGE = "Invalid input. Please try again.";
    public static final String INVALID_OPTION_MESSAGE = "Invalid option. Please try again.";
    public static final String INVALID_NUMBER_MESSAGE = "Invalid input. Please enter a valid number.";
    public static final String INVALID_DISCOUNT_MESSAGE = "Invalid or expired discount code.";
    public static final String INVALID_CATEGORY_MESSAGE = "Invalid category selection.";
    public static final String LOGIN_SUCCESS_MESSAGE = "Login successful!";
    public static final String LOGIN_FAILED_MESSAGE = "Login failed. User not found. Please register first.";
    public static final String REGISTRATION_SUCCESS_MESSAGE = "Registration successful! Please login to continue.";
    public static final String REGISTRATION_FAILED_MESSAGE = "Registration failed. User ID might already exist.";
    public static final String NO_TRANSACTIONS_MESSAGE = "No transactions found.";
    public static final String NO_EXPENSES_MESSAGE = "No expenses recorded yet.";
    public static final String NO_INCOMES_MESSAGE = "No incomes recorded yet.";
    public static final String NO_DISCOUNTS_MESSAGE = "\nNo discounts available currently.";
    public static final String BUDGET_UPDATE_FAILED_MESSAGE = "Failed to update budget. Please try again.";
    public static final String BUDGET_UPDATE_SUCCESS_MESSAGE = "Monthly budget updated successfully!";
    public static final String BUDGET_SET_SUCCESS_MESSAGE = "Monthly budget set successfully!";
    public static final String BUDGET_SET_FAILED_MESSAGE = "Failed to set budget. Please try again.";
    public static final String INCOME_SUCCESS_MESSAGE = "Income added successfully!";
    public static final String INCOME_FAILED_MESSAGE = "Failed to add income.";
    public static final String EXPENSE_SUCCESS_MESSAGE = "Expense added successfully!";
    public static final String EXPENSE_FAILED_MESSAGE = "Failed to add expense.";
    public static final String LOGOUT_MESSAGE = "Thank you for using PennyWise!";
    public static final String BUDGET_WARNING_MESSAGE = "\n⚠️ Warning: You're close to or exceeding your monthly budget!";
    public static final String PREDETERMINED_DISCOUNT_SUCCESS = "\nPredetermined discount added successfully!";
    public static final String DELETE_ACC_SUCCESS_MESSAGE = "Account deleted successfully.";
    public static final String DELETE_ACC_FAILED_MESSAGE = "Failed to delete account.";
    public static final String CLEAR_DATA_SUCCESS_MESSAGE = "All data cleared successfully.";
    public static final String CLEAR_DATA_FAILED_MESSAGE = "Failed to clear data.";
    
    // Prompts
    public static final String CHOOSE_OPTION_PROMPT = "Choose an option: ";
    public static final String ENTER_AMOUNT_PROMPT = "Enter amount: ";
    public static final String ENTER_USER_ID_PROMPT = "Enter user ID: ";
    public static final String ENTER_NEW_USER_ID_PROMPT = "Enter new user ID: ";
    public static final String ENTER_DISCOUNT_CODE_PROMPT = "Enter discount code: ";
    public static final String ENTER_DISCOUNT_PERCENTAGE_PROMPT = "Enter discount percentage (without % symbol): ";
    public static final String ENTER_DESCRIPTION_PROMPT = "Enter description: ";
    public static final String ENTER_DAYS_PROMPT = "Enter days from now: ";
    public static final String ENTER_BUDGET_PROMPT = "Enter monthly budget amount: $";
    public static final String ENTER_NEW_BUDGET_PROMPT = "Enter new monthly budget amount: $";
    public static final String EDIT_BUDGET_PROMPT = "Would you like to edit the budget?";
    public static final String ENTER_NO_SET_BUDGET_PROMPT = "No monthly budget set. Would you like to set one?";
    public static final String SELECT_CATEGORY_PROMPT = "\nSelect category:";
    public static final String LOGIN_PROMPT = "Please log in first.";
    public static final String LOGIN_PROMPT_MONTHLY_EXPENSES = "Please log in to view monthly incomes.";
    public static final String LOGIN_PROMPT_EXPENSES_CATEGORY = "Please log in to view expenses by category.";
    public static final String LOGIN_PROMPT_INCOMES_CATEGORY = "Please log in to view incomes by category.";
    public static final String DELETE_ACC_PROMPT = "Are you sure you want to delete your account?"; 
    public static final String CLEAR_DATA_PROMPT = "Are you sure you want to clear all your data?";
    public static final String ASK_DISCOUNT_CODE_PROMPT = "Do you have a discount code?";
    
    // Format Strings
    public static final String MONEY_FORMAT = "$%.2f";
    public static final String PERCENTAGE_FORMAT = "%.1f%%";
    public static final String TRANSACTION_FORMAT = "%s: $%.2f from %s (%s)%n";
    public static final String CATEGORY_AMOUNT_FORMAT = "%s: $%.2f%n";
    public static final String DISCOUNT_APPLIED_FORMAT = "Discount applied: -$%.2f (%.0f%%)%n";
    public static final String USAGE_FORMAT = "Usage: [%s] %n";
    public static final String TOTAL_INCOME_FORMAT = "Total Income: $%.2f%n";
    public static final String TOTAL_EXPENSES_FORMAT = "Total Expenses: $%.2f%n";
    public static final String CURRENT_BALANCE_FORMAT = "Current Balance: $%.2f%n";
    public static final String MONTHLY_BUDGET_FORMAT = "Monthly Budget: $%.2f%n";
    public static final String CURRENT_EXPENSES_FORMAT = "Current Expenses: $%.2f%n";
    public static final String REMAINING_BUDGET_FORMAT = "Remaining Budget: $%.2f%n";
    public static final String BUDGET_USED_FORMAT = "Budget Used: %.1f%%%n";
    public static final String NEW_BUDGET_FORMAT = "New monthly budget: $%.2f%n";
    public static final String CURRENT_BUDGET_FORMAT = "Current monthly budget: $%.2f%n";
    public static final String USER_ID_FORMAT = "\nUser ID: %s%n";
    public static final String EXPIRY_DATE_FORMAT = "\nExpiry date:";

    // Discount Display Constants
    public static final String DISCOUNT_BORDER_TOP = "\n╔════════════════════ Active Discounts ════════════════════╗";
    public static final String DISCOUNT_BORDER_MIDDLE = "╠═══════════════════════════════════════════════════════╣";
    public static final String DISCOUNT_BORDER_BOTTOM = "╚═══════════════════════════════════════════════════════════╝";
    public static final String DISCOUNT_CODE_FORMAT = "║ Code: \u001B[1m%s\u001B[0m %s%n";
    public static final String DISCOUNT_BAR_FORMAT = "║ Discount: [%s] %.0f%%%n";
    public static final String DISCOUNT_DESCRIPTION_FORMAT = "║ Description: %s%n";
    public static final String DISCOUNT_EXPIRY_FORMAT = "║ Expires: %s%n";
    public static final String DISCOUNT_STATUS_FORMAT = "║ Status: %s%n";
    public static final String DAYS_REMAINING_FORMAT = "\u001B[32m%d days remaining\u001B[0m";
    public static final String DAYS_EXPIRED_FORMAT = "\u001B[31mExpired %d days ago\u001B[0m";
    public static final String QUICK_STATS_HEADER = "\n📊 Quick Stats:";
    public static final String ACTIVE_DISCOUNTS_FORMAT = "Active Discounts: %d/%d%n";
    public static final String AVERAGE_DISCOUNT_FORMAT = "Average Discount: %.1f%%%n";

    // Visual Constants (move existing and add new)
    public static final String PROGRESS_BAR_FILLED = "█";
    public static final String PROGRESS_BAR_EMPTY = "░";
    public static final int PROGRESS_BAR_LENGTH = 20;
    public static final String ACTIVE_DISCOUNT_SYMBOL = "\u001B[32m▣ ACTIVE\u001B[0m";
    public static final String EXPIRED_DISCOUNT_SYMBOL = "\u001B[31m▢ EXPIRED\u001B[0m";
    
    // Menu Options
    public static final String[] LOGIN_MENU_OPTIONS = {
        "1. Login",
        "2. Register",
        "3. Exit"
    };
    
    public static final String[] MAIN_MENU_OPTIONS = {
        "1. Add Expense",
        "2. Add Income",
        "3. View Transactions",
        "4. View Monthly Expenses",
        "5. View Monthly Incomes",
        "6. View Expenses by Category",
        "7. View Incomes by Category",
        "8. Create Budget",
        "9. Edit Budget",
        "10. View Budget Status",
        "11. View Balance",
        "12. View Discounts",
        "13. Account Management",
        "14. Logout"
    };
    
    public static final String[] ACCOUNT_MENU_OPTIONS = {
        "1. View User ID",
        "2. Delete Account",
        "3. Clear All Data",
        "4. Return to Main Menu"
    };
    
    public static final String[] DISCOUNT_MENU_OPTIONS = {
        "1. View All Discounts",
        "2. Add Custom Discount",
        "3. Add Predetermined Discount",
        "4. Return to Main Menu"
    };
    
    public static final String[] DISCOUNT_OPTIONS = {
            "1. Apple Student Discount",
            "2. Octopus Student Discount",
            "3. GitHub Education (Teacher/Staff)",
            "4. Spotify Student Discount",
            "5. Microsoft Office 365 Education",
            "6. Adobe Creative Cloud (Student)",
            "7. Return"
     };
    
    // Validation Constants
    public static final int MIN_MENU_OPTION = 1;
    public static final int MAX_LOGIN_MENU_OPTION = 3;
    public static final int MAX_MAIN_MENU_OPTION = 14;
    public static final int MAX_ACCOUNT_MENU_OPTION = 4;
    public static final int MAX_DISCOUNT_MENU_OPTION = 4;
    
    // Prevent instantiation
    private UIConstants() {
        throw new AssertionError("UIConstants class should not be instantiated.");
    }
} 