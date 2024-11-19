/*
 * 
 */
package test.stubs;

import pennywise.ui.handlers.TransactionHandler;
import pennywise.PennyWise;
import pennywise.ui.handlers.InputHandler;


/**
 * The Class MockTransactionHandler.
 */
public class MockTransactionHandler extends TransactionHandler {
    
    /** The add expense called. */
    private boolean addExpenseCalled;
    
    /** The add income called. */
    private boolean addIncomeCalled;
    
    /** The view transactions called. */
    private boolean viewTransactionsCalled;
    
    /** The view monthly expenses called. */
    private boolean viewMonthlyExpensesCalled;
    
    /** The view monthly incomes called. */
    private boolean viewMonthlyIncomesCalled;
    
    /** The view expenses by category called. */
    private boolean viewExpensesByCategoryCalled;
    
    /** The view incomes by category called. */
    private boolean viewIncomesByCategoryCalled;
    
    /** The view balance called. */
    private boolean viewBalanceCalled;

    /**
     * Instantiates a new mock transaction handler.
     *
     * @param pennywise the pennywise
     * @param inputHandler the input handler
     */
    public MockTransactionHandler(PennyWise pennywise, InputHandler inputHandler) {
        super(pennywise, inputHandler);
    }

    /**
     * Handle add expense.
     */
    @Override
    public void handleAddExpense() {
        addExpenseCalled = true;
    }

    /**
     * Handle add income.
     */
    @Override
    public void handleAddIncome() {
        addIncomeCalled = true;
    }

    /**
     * Handle view transactions.
     */
    @Override
    public void handleViewTransactions() {
        viewTransactionsCalled = true;
    }

    /**
     * Handle view monthly expenses.
     */
    @Override
    public void handleViewMonthlyExpenses() {
        viewMonthlyExpensesCalled = true;
    }

    /**
     * Handle view monthly incomes.
     */
    @Override
    public void handleViewMonthlyIncomes() {
        viewMonthlyIncomesCalled = true;
    }

    /**
     * Handle view expenses by category.
     */
    @Override
    public void handleViewExpensesByCategory() {
        viewExpensesByCategoryCalled = true;
    }

    /**
     * Handle view incomes by category.
     */
    @Override
    public void handleViewIncomesByCategory() {
        viewIncomesByCategoryCalled = true;
    }

    /**
     * Handle view balance.
     */
    @Override
    public void handleViewBalance() {
        viewBalanceCalled = true;
    }

    /**
     * Was add expense called.
     *
     * @return true, if successful
     */
    public boolean wasAddExpenseCalled() {
        return addExpenseCalled;
    }

    /**
     * Was add income called.
     *
     * @return true, if successful
     */
    public boolean wasAddIncomeCalled() {
        return addIncomeCalled;
    }

    /**
     * Was view transactions called.
     *
     * @return true, if successful
     */
    public boolean wasViewTransactionsCalled() {
        return viewTransactionsCalled;
    }

    /**
     * Was view monthly expenses called.
     *
     * @return true, if successful
     */
    public boolean wasViewMonthlyExpensesCalled() {
        return viewMonthlyExpensesCalled;
    }

    /**
     * Was view monthly incomes called.
     *
     * @return true, if successful
     */
    public boolean wasViewMonthlyIncomesCalled() {
        return viewMonthlyIncomesCalled;
    }

    /**
     * Was view expenses by category called.
     *
     * @return true, if successful
     */
    public boolean wasViewExpensesByCategoryCalled() {
        return viewExpensesByCategoryCalled;
    }

    /**
     * Was view incomes by category called.
     *
     * @return true, if successful
     */
    public boolean wasViewIncomesByCategoryCalled() {
        return viewIncomesByCategoryCalled;
    }

    /**
     * Was view balance called.
     *
     * @return true, if successful
     */
    public boolean wasViewBalanceCalled() {
        return viewBalanceCalled;
    }
} 