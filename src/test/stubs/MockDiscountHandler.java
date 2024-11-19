/*
 * 
 */
package test.stubs;

import pennywise.ui.handlers.DiscountHandler;


/**
 * The Class MockDiscountHandler.
 */
public class MockDiscountHandler extends DiscountHandler {
    
    /** The discount visualization called. */
    private boolean discountVisualizationCalled;

    /**
     * Instantiates a new mock discount handler.
     */
    public MockDiscountHandler() {
        super(new MockDiscountManager(), new MockInputHandler());
    }

    /**
     * Handle discount visualization.
     */
    @Override
    public void handleDiscountVisualization() {
        discountVisualizationCalled = true;
    }

    /**
     * Was discount visualization called.
     *
     * @return true, if successful
     */
    public boolean wasDiscountVisualizationCalled() {
        return discountVisualizationCalled;
    }
} 