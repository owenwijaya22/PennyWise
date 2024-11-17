package test.stubs;

import pennywise.ui.handlers.BudgetHandler;
import pennywise.PennyWise;
import pennywise.ui.handlers.InputHandler;

public class MockBudgetHandler extends BudgetHandler {
    private boolean createBudgetCalled;
    private boolean editBudgetCalled;
    private boolean viewBudgetsCalled;

    public MockBudgetHandler(PennyWise pennywise, InputHandler inputHandler) {
        super(pennywise, inputHandler);
    }

    @Override
    public void handleCreateBudget() {
        createBudgetCalled = true;
    }

    @Override
    public void handleEditBudget() {
        editBudgetCalled = true;
    }

    @Override
    public void handleViewBudgets() {
        viewBudgetsCalled = true;
    }

    public boolean wasCreateBudgetCalled() {
        return createBudgetCalled;
    }

    public boolean wasEditBudgetCalled() {
        return editBudgetCalled;
    }

    public boolean wasViewBudgetsCalled() {
        return viewBudgetsCalled;
    }
} 