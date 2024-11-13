package pennywise.ui.handlers;

import pennywise.PennyWise;
import pennywise.ui.UIConstants;

public class BudgetHandler {
    private final PennyWise pennywise;
    private final InputHandler inputHandler;

    public BudgetHandler(PennyWise pennywise, InputHandler inputHandler) {
        this.pennywise = pennywise;
        this.inputHandler = inputHandler;
    }

    void handleCreateBudget() {
        System.out.println(UIConstants.SET_MONTHLY_BUDGET_TITLE);
        System.out.print(UIConstants.ENTER_BUDGET_PROMPT);        
        double amount = inputHandler.readDouble();
        if (amount < 0) {
            System.out.println(UIConstants.INVALID_NUMBER_MESSAGE);
            return;
        }

        if (pennywise.createBudget(amount)) {
            System.out.println(UIConstants.BUDGET_SET_SUCCESS_MESSAGE);
            System.out.printf(UIConstants.CURRENT_BUDGET_FORMAT, amount);
        } else {
            System.out.println(UIConstants.BUDGET_SET_FAILED_MESSAGE);
        }
    }

    void handleEditBudget() {
        double currentBudget = pennywise.getBudgetManager().getCurrentMonthBudget(pennywise.getCurrentUser().getUserId());
        System.out.println(UIConstants.EDIT_MONTHLY_BUDGET_TITLE);
        System.out.printf(UIConstants.CURRENT_BUDGET_FORMAT, currentBudget);
        System.out.print(UIConstants.ENTER_NEW_BUDGET_PROMPT);
        double newAmount = inputHandler.readDouble();
        if (newAmount < 0) {
            System.out.println(UIConstants.INVALID_NUMBER_MESSAGE);
            return;
        }

        if (pennywise.updateBudget(newAmount)) {
            System.out.println(UIConstants.BUDGET_UPDATE_SUCESS_MESSAGE);
            System.out.printf(UIConstants.NEW_BUDGET_FORMAT, newAmount);
            
            // Show updated budget status
            double currentExpenses = pennywise.getTotalExpenses();
            double remainingBudget = newAmount - currentExpenses;
            double usedPercentage = (currentExpenses / newAmount) * 100;

            System.out.println(UIConstants.UPDATED_BUDGET_TITLE);
            System.out.printf(UIConstants.CURRENT_EXPENSES_FORMAT, currentExpenses);
            System.out.printf(UIConstants.REMAINING_BUDGET_FORMAT, remainingBudget);
            System.out.printf(UIConstants.BUDGET_USED_FORMAT, usedPercentage);

            if (usedPercentage >= 90) {
                System.out.println(UIConstants.BUDGET_WARNING_MESSAGE);
            }
        } else {
            System.out.println(UIConstants.BUDGET_UPDATE_FAILED_MESSAGE);
        }
    }

    void handleViewBudgets() {
        if (pennywise.getCurrentUser() == null) {
            System.out.println(UIConstants.LOGIN_PROMPT);
            return;
        }

        double monthlyBudget = pennywise.getBudgetManager().getCurrentMonthBudget(pennywise.getCurrentUser().getUserId());
        if (monthlyBudget <= 0) {
        	if (inputHandler.askYesNo(UIConstants.ENTER_NO_SET_BUDGET_PROMPT)) {
                handleCreateBudget();
            }
            return;
        }

        double currentExpenses = pennywise.getTotalExpenses();
        double remainingBudget = monthlyBudget - currentExpenses;
        double usedPercentage = (currentExpenses / monthlyBudget) * 100;

        System.out.println(UIConstants.BUDGET_MENU_TITLE);
        System.out.printf(UIConstants.MONTHLY_BUDGET_FORMAT, monthlyBudget);
        System.out.printf(UIConstants.CURRENT_EXPENSES_FORMAT, currentExpenses);
        System.out.printf(UIConstants.REMAINING_BUDGET_FORMAT, remainingBudget);
        System.out.printf(UIConstants.BUDGET_USED_FORMAT, usedPercentage);

        int filledBars = (int)((usedPercentage / 100) * UIConstants.PROGRESS_BAR_LENGTH);
        String progressBar = UIConstants.PROGRESS_BAR_FILLED.repeat(Math.min(filledBars, UIConstants.PROGRESS_BAR_LENGTH)) + 
                            UIConstants.PROGRESS_BAR_EMPTY.repeat(Math.max(0, UIConstants.PROGRESS_BAR_LENGTH - filledBars));
        System.out.printf(UIConstants.USAGE_FORMAT, progressBar);

        if (usedPercentage >= 90) {
            System.out.println(UIConstants.BUDGET_WARNING_MESSAGE);
        }

        if (inputHandler.askYesNo(UIConstants.EDIT_BUDGET_PROMPT)) {
            handleEditBudget();
        }
    }
}