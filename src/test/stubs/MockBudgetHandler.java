/*
 * 
 */
package test.stubs;

import pennywise.ui.handlers.BudgetHandler;
import pennywise.PennyWise;
import pennywise.ui.handlers.InputHandler;


/**
 * The Class MockBudgetHandler.
 */
public class MockBudgetHandler extends BudgetHandler {
    
    /** The create budget called. */
    private boolean createBudgetCalled;
    
    /** The edit budget called. */
    private boolean editBudgetCalled;
    
    /** The view budgets called. */
    private boolean viewBudgetsCalled;

    /**
     * Instantiates a new mock budget handler.
     *
     * @param pennywise the pennywise
     * @param inputHandler the input handler
     */
    public MockBudgetHandler(PennyWise pennywise, InputHandler inputHandler) {
        super(pennywise, inputHandler);
    }

    /**
     * Handle create budget.
     */
    @Override
    public void handleCreateBudget() {
        createBudgetCalled = true;
    }

    /**
     * Handle edit budget.
     */
    @Override
    public void handleEditBudget() {
        editBudgetCalled = true;
    }

    /**
     * Handle view budgets.
     */
    @Override
    public void handleViewBudgets() {
        viewBudgetsCalled = true;
    }

    /**
     * Was create budget called.
     *
     * @return true, if successful
     */
    public boolean wasCreateBudgetCalled() {
        return createBudgetCalled;
    }

    /**
     * Was edit budget called.
     *
     * @return true, if successful
     */
    public boolean wasEditBudgetCalled() {
        return editBudgetCalled;
    }

    /**
     * Was view budgets called.
     *
     * @return true, if successful
     */
    public boolean wasViewBudgetsCalled() {
        return viewBudgetsCalled;
    }
} 