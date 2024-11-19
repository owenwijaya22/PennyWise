/*
 * 
 */
package test.stubs;

import pennywise.ui.handlers.AccountHandler;
import pennywise.PennyWise;
import pennywise.ui.handlers.InputHandler;


/**
 * The Class MockAccountHandler.
 */
public class MockAccountHandler extends AccountHandler {
    
    /** The login called. */
    private boolean loginCalled;
    
    /** The registration called. */
    private boolean registrationCalled;
    
    /** The account management called. */
    private boolean accountManagementCalled;

    /**
     * Instantiates a new mock account handler.
     *
     * @param pennywise the pennywise
     * @param inputHandler the input handler
     */
    public MockAccountHandler(PennyWise pennywise, InputHandler inputHandler) {
        super(pennywise, inputHandler);
    }

    /**
     * Handle login.
     */
    @Override
    public void handleLogin() {
        loginCalled = true;
    }

    /**
     * Handle registration.
     */
    @Override
    public void handleRegistration() {
        registrationCalled = true;
    }

    /**
     * Handle account management.
     */
    @Override
    public void handleAccountManagement() {
        accountManagementCalled = true;
    }

    /**
     * Was login called.
     *
     * @return true, if successful
     */
    public boolean wasLoginCalled() {
        return loginCalled;
    }

    /**
     * Was registration called.
     *
     * @return true, if successful
     */
    public boolean wasRegistrationCalled() {
        return registrationCalled;
    }

    /**
     * Was account management called.
     *
     * @return true, if successful
     */
    public boolean wasAccountManagementCalled() {
        return accountManagementCalled;
    }
} 