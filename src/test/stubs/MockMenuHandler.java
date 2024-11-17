package test.stubs;

import pennywise.ui.handlers.MenuHandler;

public class MockMenuHandler extends MenuHandler {
    private boolean loginMenuCalled = false;

    public MockMenuHandler() {
        super(null, null, null, null, null, null);
    }

    @Override
    public void handleLoginMenu() {
        loginMenuCalled = true;
    }

    public boolean wasLoginMenuCalled() {
        return loginMenuCalled;
    }
} 