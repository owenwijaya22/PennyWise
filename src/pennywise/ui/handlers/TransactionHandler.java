package pennywise.ui.handlers;

import pennywise.PennyWise;
import pennywise.model.*;
import pennywise.service.TransactionAnalyzer;
import pennywise.ui.UIConstants;
import pennywise.utils.DiscountManager;

import java.util.*;

public class TransactionHandler {
    private final PennyWise pennywise;
    private final InputHandler inputHandler;

    public TransactionHandler(PennyWise pennywise, InputHandler inputHandler) {
        this.pennywise = pennywise;
        this.inputHandler = inputHandler;
    }

    void handleAddExpense() {
        System.out.print(UIConstants.ENTER_AMOUNT_PROMPT);
        double amount = inputHandler.readDouble();
        System.out.println(amount);
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

        System.out.println(UIConstants.SELECT_CATEGORY_PROMPT);
        ExpenseCategory[] categories = ExpenseCategory.values();
        for (int i = 0; i < categories.length; i++) {
            System.out.println((i + 1) + ". " + categories[i].getCategoryName());
        }
        int categoryChoice = inputHandler.readInt() - 1;

        if (categoryChoice >= 0 && categoryChoice < categories.length) {
            if (pennywise.addTransaction(amount, categories[categoryChoice])) {
                System.out.println(UIConstants.EXPENSE_SUCCESS_MESSAGE);
            } else {
                System.out.println(UIConstants.EXPENSE_FAILED_MESSAGE);
            }
        } else {
            System.out.println(UIConstants.INVALID_CATEGORY_MESSAGE);
        }
    }

    void handleAddIncome() {
        System.out.print(UIConstants.ENTER_AMOUNT_PROMPT);
        double amount = inputHandler.readDouble();
        if (amount < 0) {
            System.out.println(UIConstants.INVALID_NUMBER_MESSAGE);
            return;
        }
        
        System.out.println(UIConstants.SELECT_CATEGORY_PROMPT);
        IncomeCategory[] categories = IncomeCategory.values();
        for (int i = 0; i < categories.length; i++) {
            System.out.println((i + 1) + ". " + categories[i].getCategoryName());
        }
        int categoryChoice = inputHandler.readInt() - 1;

        if (categoryChoice >= 0 && categoryChoice < categories.length) {
            if (pennywise.addTransaction(amount, categories[categoryChoice])) {
                System.out.println(UIConstants.INCOME_SUCCESS_MESSAGE);
            } else {
                System.out.println(UIConstants.INCOME_FAILED_MESSAGE);
            }
        } else {
            System.out.println(UIConstants.INVALID_CATEGORY_MESSAGE);
        }
    }

    void handleViewTransactions() {
        List<Transaction> transactions = pennywise.getTransactions();
        if (transactions.isEmpty()) {
            System.out.println(UIConstants.NO_TRANSACTIONS_MESSAGE);
            return;
        }

        System.out.println(UIConstants.TRANSACTIONS_TITLE);
        for (Transaction t : transactions) {
            System.out.printf(UIConstants.TRANSACTION_FORMAT,
                    t.getType(), Math.abs(t.getAmount()), 
                    t.getCategory().getCategoryName(), t.getDate());
        }
    }

    void handleViewMonthlyExpenses() {
        TransactionAnalyzer analyzer = pennywise.getAnalyzer();
        if (analyzer == null) {
            System.out.println(UIConstants.LOGIN_PROMPT_MONTHLY_EXPENSES);
            return;
        }

        Map<Object, Double> monthlyExpenses = analyzer.getMonthlyExpenses();
        System.out.println(UIConstants.MONTHLY_EXPENSES_TITLE);
        if (monthlyExpenses.isEmpty()) {
            System.out.println(UIConstants.NO_EXPENSES_MESSAGE);
        } else {
            monthlyExpenses.forEach((month, amount) -> 
                System.out.printf(UIConstants.CATEGORY_AMOUNT_FORMAT, month, amount));
        }
    }

    void handleViewMonthlyIncomes() {
		TransactionAnalyzer analyzer = pennywise.getAnalyzer();
		if (analyzer == null) {
			System.out.println(UIConstants.LOGIN_PROMPT_MONTHLY_EXPENSES);
			return;
		}

		Map<Object, Double> monthlyIncomes = analyzer.getMonthlyIncome();
		System.out.println(UIConstants.MONTHLY_INCOMES_TITLE);
		if (monthlyIncomes.isEmpty()) {
			System.out.println(UIConstants.NO_INCOMES_MESSAGE);
		} else {
			monthlyIncomes.forEach((month, amount) -> System.out.printf(UIConstants.CATEGORY_AMOUNT_FORMAT, month, amount));
		}
    }

    void handleViewExpensesByCategory() {
        TransactionAnalyzer analyzer = pennywise.getAnalyzer();
        if (analyzer == null) {
            System.out.println(UIConstants.LOGIN_PROMPT_EXPENSES_CATEGORY);
            return;
        }

        Map<Object, Double> categoryExpenses = analyzer.getExpensesByCategory();
        System.out.println(UIConstants.EXPENSES_CATEGORY_TITLE);
        if (categoryExpenses.isEmpty()) {
            System.out.println(UIConstants.NO_EXPENSES_MESSAGE);
        } else {
            categoryExpenses.forEach((category, amount) -> 
                System.out.printf(UIConstants.CATEGORY_AMOUNT_FORMAT, category, amount));
        }
    }

    void handleViewIncomesByCategory() {
    	TransactionAnalyzer analyzer = pennywise.getAnalyzer();
        if (analyzer == null) {
            System.out.println(UIConstants.LOGIN_PROMPT_INCOMES_CATEGORY);
            return;
        }

        Map<Object, Double> categoryIncomes = analyzer.getIncomeByCategory();
        System.out.println(UIConstants.MONTHLY_INCOMES_TITLE);
        if (categoryIncomes.isEmpty()) {
            System.out.println(UIConstants.NO_INCOMES_MESSAGE);
        } else {
        	categoryIncomes.forEach((category, amount) -> 
                System.out.printf(UIConstants.CATEGORY_AMOUNT_FORMAT, category, amount));
        }
    }

    void handleViewBalance() {
        System.out.println(UIConstants.FINANCIAL_SUMMARY_TITLE);
        System.out.printf(UIConstants.TOTAL_INCOME_FORMAT, pennywise.getTotalIncome());
        System.out.printf(UIConstants.TOTAL_EXPENSES_FORMAT, pennywise.getTotalExpenses());
        System.out.printf(UIConstants.CURRENT_BALANCE_FORMAT, pennywise.getCurrentBalance());
    }
}