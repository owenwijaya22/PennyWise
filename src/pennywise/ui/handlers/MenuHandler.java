package pennywise.ui.handlers;

import pennywise.PennyWise;
import pennywise.ui.*;

public class MenuHandler {
    private final PennyWise pennywise;
    private final InputHandler inputHandler;
    private final TransactionHandler transactionHandler;
    private final BudgetHandler budgetHandler;
    private final DiscountHandler discountHandler;
    private final AccountHandler accountHandler;

    public MenuHandler(PennyWise pennywise, InputHandler inputHandler, 
                      TransactionHandler transactionHandler,
                      BudgetHandler budgetHandler,
                      DiscountHandler discountHandler,
                      AccountHandler accountHandler) {
        this.pennywise = pennywise;
        this.inputHandler = inputHandler;
        this.transactionHandler = transactionHandler;
        this.budgetHandler = budgetHandler;
        this.discountHandler = discountHandler;
        this.accountHandler = accountHandler;
    }

    public void handleLoginMenu() {
        System.out.println(UIConstants.LOGIN_MENU_TITLE);
        for (String option : UIConstants.LOGIN_MENU_OPTIONS) {
            System.out.println(option);
        }
        System.out.print(UIConstants.CHOOSE_OPTION_PROMPT);

        int choice = inputHandler.readInt();
        if (choice < UIConstants.MIN_MENU_OPTION || choice > UIConstants.MAX_LOGIN_MENU_OPTION) {
            System.out.println(UIConstants.INVALID_INPUT_MESSAGE);
            return;
        }

        switch (choice) {
            case 1:
                accountHandler.handleLogin();
                break;
            case 2:
                accountHandler.handleRegistration();
                break;
            case 3:
                System.out.println(UIConstants.LOGOUT_MESSAGE);
                inputHandler.close();
                System.exit(0);
                break;
            default:
                System.out.println(UIConstants.INVALID_OPTION_MESSAGE);
        }
    }

    public void handleMainMenu() {
        System.out.println(UIConstants.MAIN_MENU_TITLE);
        for (String option : UIConstants.MAIN_MENU_OPTIONS) {
            System.out.println(option);
        }
        System.out.print(UIConstants.CHOOSE_OPTION_PROMPT);

        int choice = inputHandler.readInt();
        if (choice < UIConstants.MIN_MENU_OPTION || choice > UIConstants.MAX_MAIN_MENU_OPTION) {
            System.out.println(UIConstants.INVALID_INPUT_MESSAGE);
            return;
        }

        switch (choice) {
            case 1:
                transactionHandler.handleAddExpense();
                break;
            case 2:
                transactionHandler.handleAddIncome();
                break;
            case 3:
                transactionHandler.handleViewTransactions();
                break;
            case 4:
                transactionHandler.handleViewMonthlyExpenses();
                break;
            case 5:
                transactionHandler.handleViewMonthlyIncomes();
                break;
            case 6:
                transactionHandler.handleViewExpensesByCategory();
                break;
            case 7:
                transactionHandler.handleViewIncomesByCategory();
                break;
            case 8:
                budgetHandler.handleCreateBudget();
                break;
            case 9:
                budgetHandler.handleEditBudget();
                break;
            case 10:
                budgetHandler.handleViewBudgets();
                break;
            case 11:
                transactionHandler.handleViewBalance();
                break;
            case 12:
                discountHandler.handleDiscountVisualization();
                break;
            case 13:
                accountHandler.handleAccountManagement();
                break;
            case 14:
                pennywise.logout();
                break;
            default:
                System.out.println(UIConstants.INVALID_OPTION_MESSAGE);
        }
    }
} 