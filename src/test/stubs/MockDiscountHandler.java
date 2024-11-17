package test.stubs;

import pennywise.ui.handlers.DiscountHandler;

public class MockDiscountHandler extends DiscountHandler {
    private boolean discountVisualizationCalled;

    public MockDiscountHandler() {
        super(new MockDiscountManager(), new MockInputHandler());
    }

    @Override
    public void handleDiscountVisualization() {
        discountVisualizationCalled = true;
    }

    public boolean wasDiscountVisualizationCalled() {
        return discountVisualizationCalled;
    }
} 