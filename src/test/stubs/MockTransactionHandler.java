package test.stubs;

import pennywise.ui.handlers.TransactionHandler;
import pennywise.PennyWise;
import pennywise.ui.handlers.InputHandler;

public class MockTransactionHandler extends TransactionHandler {
    private boolean addExpenseCalled;
    private boolean addIncomeCalled;
    private boolean viewTransactionsCalled;
    private boolean viewMonthlyExpensesCalled;
    private boolean viewMonthlyIncomesCalled;
    private boolean viewExpensesByCategoryCalled;
    private boolean viewIncomesByCategoryCalled;
    private boolean viewBalanceCalled;

    public MockTransactionHandler(PennyWise pennywise, InputHandler inputHandler) {
        super(pennywise, inputHandler);
    }

    @Override
    public void handleAddExpense() {
        addExpenseCalled = true;
    }

    @Override
    public void handleAddIncome() {
        addIncomeCalled = true;
    }

    @Override
    public void handleViewTransactions() {
        viewTransactionsCalled = true;
    }

    @Override
    public void handleViewMonthlyExpenses() {
        viewMonthlyExpensesCalled = true;
    }

    @Override
    public void handleViewMonthlyIncomes() {
        viewMonthlyIncomesCalled = true;
    }

    @Override
    public void handleViewExpensesByCategory() {
        viewExpensesByCategoryCalled = true;
    }

    @Override
    public void handleViewIncomesByCategory() {
        viewIncomesByCategoryCalled = true;
    }

    @Override
    public void handleViewBalance() {
        viewBalanceCalled = true;
    }

    public boolean wasAddExpenseCalled() {
        return addExpenseCalled;
    }

    public boolean wasAddIncomeCalled() {
        return addIncomeCalled;
    }

    public boolean wasViewTransactionsCalled() {
        return viewTransactionsCalled;
    }

    public boolean wasViewMonthlyExpensesCalled() {
        return viewMonthlyExpensesCalled;
    }

    public boolean wasViewMonthlyIncomesCalled() {
        return viewMonthlyIncomesCalled;
    }

    public boolean wasViewExpensesByCategoryCalled() {
        return viewExpensesByCategoryCalled;
    }

    public boolean wasViewIncomesByCategoryCalled() {
        return viewIncomesByCategoryCalled;
    }

    public boolean wasViewBalanceCalled() {
        return viewBalanceCalled;
    }
} 