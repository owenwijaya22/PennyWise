package test.stubs;

import pennywise.ui.handlers.AccountHandler;
import pennywise.PennyWise;
import pennywise.ui.handlers.InputHandler;

public class MockAccountHandler extends AccountHandler {
    private boolean loginCalled;
    private boolean registrationCalled;
    private boolean accountManagementCalled;

    public MockAccountHandler(PennyWise pennywise, InputHandler inputHandler) {
        super(pennywise, inputHandler);
    }

    @Override
    public void handleLogin() {
        loginCalled = true;
    }

    @Override
    public void handleRegistration() {
        registrationCalled = true;
    }

    @Override
    public void handleAccountManagement() {
        accountManagementCalled = true;
    }

    public boolean wasLoginCalled() {
        return loginCalled;
    }

    public boolean wasRegistrationCalled() {
        return registrationCalled;
    }

    public boolean wasAccountManagementCalled() {
        return accountManagementCalled;
    }
} 