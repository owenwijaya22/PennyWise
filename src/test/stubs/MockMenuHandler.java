/*
 * 
 */
package test.stubs;

import pennywise.ui.handlers.MenuHandler;


/**
 * The Class MockMenuHandler.
 */
public class MockMenuHandler extends MenuHandler {
    
    /** The login menu called. */
    private boolean loginMenuCalled = false;

    /**
     * Instantiates a new mock menu handler.
     */
    public MockMenuHandler() {
        super(null, null, null, null, null, null);
    }

    /**
     * Handle login menu.
     */
    @Override
    public void handleLoginMenu() {
        loginMenuCalled = true;
    }

    /**
     * Was login menu called.
     *
     * @return true, if successful
     */
    public boolean wasLoginMenuCalled() {
        return loginMenuCalled;
    }
} 