/*
 * 
 */
package pennywise.ui.handlers;

import pennywise.PennyWise;
import pennywise.ui.*;


/**
 * The Class MenuHandler.
 */
public class MenuHandler {
    
    /** The pennywise. */
    private final PennyWise pennywise;
    
    /** The input handler. */
    private final InputHandler inputHandler;
    
    /** The transaction handler. */
    private final TransactionHandler transactionHandler;
    
    /** The budget handler. */
    private final BudgetHandler budgetHandler;
    
    /** The discount handler. */
    private final DiscountHandler discountHandler;
    
    /** The account handler. */
    private final AccountHandler accountHandler;

    /**
     * Instantiates a new menu handler.
     *
     * @param pennywise the pennywise
     * @param inputHandler the input handler
     * @param transactionHandler the transaction handler
     * @param budgetHandler the budget handler
     * @param discountHandler the discount handler
     * @param accountHandler the account handler
     */
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

    /**
     * Handle login menu.
     */
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

    /**
     * Handle main menu.
     */
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