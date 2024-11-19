/*
 * 
 */
package pennywise.ui.handlers;

import pennywise.PennyWise;
import pennywise.model.*;
import pennywise.service.TransactionAnalyzer;
import pennywise.ui.UIConstants;
import pennywise.utils.DiscountManager;
import pennywise.interfaces.TransactionCategory;

import java.util.*;


/**
 * The Class TransactionHandler.
 */
public class TransactionHandler {
    
    /** The pennywise. */
    private final PennyWise pennywise;
    
    /** The input handler. */
    private final InputHandler inputHandler;

    /**
     * Instantiates a new transaction handler.
     *
     * @param pennywise the pennywise
     * @param inputHandler the input handler
     */
    public TransactionHandler(PennyWise pennywise, InputHandler inputHandler) {
        this.pennywise = pennywise;
        this.inputHandler = inputHandler;
    }

    /**
     * Handle category selection.
     *
     * @param <T> the generic type
     * @param categoryClass the category class
     * @return the t
     */
    private <T extends Enum<T> & TransactionCategory> T handleCategorySelection(Class<T> categoryClass) {
        System.out.println(UIConstants.SELECT_CATEGORY_PROMPT);
        T[] categories = categoryClass.getEnumConstants();
        for (int i = 0; i < categories.length; i++) {
            System.out.println((i + 1) + ". " + categories[i].getCategoryName());
        }
        int categoryChoice = inputHandler.readInt() - 1;

        if (categoryChoice >= 0 && categoryChoice < categories.length) {
            return categories[categoryChoice];
        }
        return null;
    }

    /**
     * Handle transaction.
     *
     * @param amount the amount
     * @param category the category
     * @param isExpense the is expense
     * @return true, if successful
     */
    private boolean handleTransaction(double amount, TransactionCategory category, boolean isExpense) {
        if (category == null) {
            System.out.println(UIConstants.INVALID_CATEGORY_MESSAGE);
            return false;
        }

        if (pennywise.addTransaction(amount, category)) {
            System.out.println(isExpense ? UIConstants.EXPENSE_SUCCESS_MESSAGE : UIConstants.INCOME_SUCCESS_MESSAGE);
            return true;
        } else {
            System.out.println(isExpense ? UIConstants.EXPENSE_FAILED_MESSAGE : UIConstants.INCOME_FAILED_MESSAGE);
            return false;
        }
    }

    /**
     * Handle add expense.
     */
    public void handleAddExpense() {
        System.out.print(UIConstants.ENTER_AMOUNT_PROMPT);
        double amount = inputHandler.readDouble();
        if (amount < 0) {
            System.out.println(UIConstants.INVALID_NUMBER_MESSAGE);
            return;
        }

        if (inputHandler.askYesNo(UIConstants.ASK_DISCOUNT_CODE_PROMPT)) {
            System.out.print(UIConstants.ENTER_DISCOUNT_CODE_PROMPT);
            String discountCode = inputHandler.readLine().toUpperCase();
            
            DiscountManager discountManager = DiscountManager.getInstance();
            Discount discount = discountManager.findDiscountByCode(discountCode);
            
            if (discount != null && discount.isValid()) {
                double discountAmount = amount * (discount.getPercentage() / 100);
                amount -= discountAmount;
                System.out.printf(UIConstants.DISCOUNT_APPLIED_FORMAT, discountAmount, discount.getPercentage());
            } else {
                System.out.println(UIConstants.INVALID_DISCOUNT_MESSAGE);
            }
        }

        TransactionCategory category = handleCategorySelection(ExpenseCategory.class);
        handleTransaction(amount, category, true);
    }

    /**
     * Handle add income.
     */
    public void handleAddIncome() {
        System.out.print(UIConstants.ENTER_AMOUNT_PROMPT);
        double amount = inputHandler.readDouble();
        if (amount < 0) {
            System.out.println(UIConstants.INVALID_NUMBER_MESSAGE);
            return;
        }
        
        TransactionCategory category = handleCategorySelection(IncomeCategory.class);
        handleTransaction(amount, category, false);
    }

    /**
     * Handle view transactions.
     */
    public void handleViewTransactions() {
        List<Transaction> transactions = pennywise.getTransactions();
        if (transactions.isEmpty()) {
            System.out.println(UIConstants.NO_TRANSACTIONS_MESSAGE);
            return;
        }

        System.out.println(UIConstants.TRANSACTIONS_TITLE);
        for (Transaction t : transactions) {
            String transactionType = t.isExpense() ? "EXPENSE" : "INCOME";
            System.out.printf(UIConstants.TRANSACTION_FORMAT,
                    transactionType, Math.abs(t.getAmount()), 
                    t.getCategory().getCategoryName(), t.getDate());
        }
    }

    /**
     * Handle view monthly expenses.
     */
    public void handleViewMonthlyExpenses() {
        TransactionAnalyzer analyzer = pennywise.getAnalyzer();
        if (analyzer == null) {
            System.out.println(UIConstants.LOGIN_PROMPT_MONTHLY_EXPENSES);
            return;
        }

        Map<String, Double> monthlyExpenses = analyzer.getMonthlyExpenses();
        System.out.println(UIConstants.MONTHLY_EXPENSES_TITLE);
        if (monthlyExpenses.isEmpty()) {
            System.out.println(UIConstants.NO_EXPENSES_MESSAGE);
        } else {
            monthlyExpenses.forEach((month, amount) -> 
                System.out.printf(UIConstants.CATEGORY_AMOUNT_FORMAT, month, amount));
        }
    }

    /**
     * Handle view monthly incomes.
     */
    public void handleViewMonthlyIncomes() {
        TransactionAnalyzer analyzer = pennywise.getAnalyzer();
        if (analyzer == null) {
            System.out.println(UIConstants.LOGIN_PROMPT_MONTHLY_EXPENSES);
            return;
        }

        Map<String, Double> monthlyIncomes = analyzer.getMonthlyIncome();
        System.out.println(UIConstants.MONTHLY_INCOMES_TITLE);
        if (monthlyIncomes.isEmpty()) {
            System.out.println(UIConstants.NO_INCOMES_MESSAGE);
        } else {
            monthlyIncomes.forEach((month, amount) -> 
                System.out.printf(UIConstants.CATEGORY_AMOUNT_FORMAT, month, amount));
        }
    }

    /**
     * Handle view expenses by category.
     */
    public void handleViewExpensesByCategory() {
        TransactionAnalyzer analyzer = pennywise.getAnalyzer();
        if (analyzer == null) {
            System.out.println(UIConstants.LOGIN_PROMPT_EXPENSES_CATEGORY);
            return;
        }

        Map<TransactionCategory, Double> categoryExpenses = analyzer.getExpensesByCategory();
        System.out.println(UIConstants.EXPENSES_CATEGORY_TITLE);
        if (categoryExpenses.isEmpty()) {
            System.out.println(UIConstants.NO_EXPENSES_MESSAGE);
        } else {
            categoryExpenses.forEach((category, amount) -> 
                System.out.printf(UIConstants.CATEGORY_AMOUNT_FORMAT, category.getCategoryName(), amount));
        }
    }

    /**
     * Handle view incomes by category.
     */
    public void handleViewIncomesByCategory() {
        TransactionAnalyzer analyzer = pennywise.getAnalyzer();
        if (analyzer == null) {
            System.out.println(UIConstants.LOGIN_PROMPT_INCOMES_CATEGORY);
            return;
        }

        Map<TransactionCategory, Double> categoryIncomes = analyzer.getIncomeByCategory();
        System.out.println(UIConstants.MONTHLY_INCOMES_TITLE);
        if (categoryIncomes.isEmpty()) {
            System.out.println(UIConstants.NO_INCOMES_MESSAGE);
        } else {
            categoryIncomes.forEach((category, amount) -> 
                System.out.printf(UIConstants.CATEGORY_AMOUNT_FORMAT, category.getCategoryName(), amount));
        }
    }

    /**
     * Handle view balance.
     */
    public void handleViewBalance() {
        System.out.println(UIConstants.FINANCIAL_SUMMARY_TITLE);
        System.out.printf(UIConstants.TOTAL_INCOME_FORMAT, pennywise.getTotalIncome());
        System.out.printf(UIConstants.TOTAL_EXPENSES_FORMAT, pennywise.getTotalExpenses());
        System.out.printf(UIConstants.CURRENT_BALANCE_FORMAT, pennywise.getCurrentBalance());
    }
}