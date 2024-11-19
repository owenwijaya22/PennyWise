/*
 * 
 */
package pennywise.ui.handlers;

import pennywise.PennyWise;
import pennywise.ui.UIConstants;


/**
 * The Class AccountHandler.
 */
public class AccountHandler {
    
    /** The pennywise. */
    private final PennyWise pennywise;
    
    /** The input handler. */
    private final InputHandler inputHandler;
    
    /** The menu handler. */
    private MenuHandler menuHandler;

    /**
     * Instantiates a new account handler.
     *
     * @param pennywise the pennywise
     * @param inputHandler the input handler
     */
    public AccountHandler(PennyWise pennywise, InputHandler inputHandler) {
        this.pennywise = pennywise;
        this.inputHandler = inputHandler;
    }

    /**
     * Sets the menu handler.
     *
     * @param menuHandler the new menu handler
     */
    public void setMenuHandler(MenuHandler menuHandler) {
        this.menuHandler = menuHandler;
    }

    /**
     * Handle login.
     */
    public void handleLogin() {
        System.out.print(UIConstants.ENTER_USER_ID_PROMPT);
        String userId = inputHandler.readLine();
        
        if (pennywise.login(userId)) {
            System.out.println(UIConstants.LOGIN_SUCCESS_MESSAGE);
        } else {
            System.out.println(UIConstants.LOGIN_FAILED_MESSAGE);
        }
    }

    /**
     * Handle registration.
     */
    public void handleRegistration() {
        System.out.print(UIConstants.ENTER_NEW_USER_ID_PROMPT);
        String userId = inputHandler.readLine();
        
        if (pennywise.registerUser(userId)) {
            System.out.println(UIConstants.REGISTRATION_SUCCESS_MESSAGE);
        } else {
            System.out.println(UIConstants.REGISTRATION_FAILED_MESSAGE);
        }
    }

    /**
     * Handle account management.
     */
    public void handleAccountManagement() {
        System.out.println(UIConstants.ACCOUNT_MANAGEMENT_TITLE);
        for (String option : UIConstants.ACCOUNT_MENU_OPTIONS) {
            System.out.println(option);
        }
        System.out.print(UIConstants.CHOOSE_OPTION_PROMPT);
        
        int choice = inputHandler.readInt();
        
        switch (choice) {
            case 1:
                System.out.printf(UIConstants.USER_ID_FORMAT, pennywise.getCurrentUser().getUserId());
                break;
            case 2:
                if (inputHandler.askYesNo(UIConstants.DELETE_ACC_PROMPT)) {
                    if (pennywise.deleteAccount()) {
                        System.out.println(UIConstants.DELETE_ACC_SUCCESS_MESSAGE);
                        menuHandler.handleLoginMenu();
                        return;
                    }
                    System.out.println(UIConstants.DELETE_ACC_FAILED_MESSAGE);
                }
                break;
            case 3:
                if (inputHandler.askYesNo(UIConstants.CLEAR_DATA_PROMPT)) {
                    if (pennywise.clearAllUserData()) {
                        System.out.println(UIConstants.CLEAR_DATA_SUCCESS_MESSAGE);
                        menuHandler.handleLoginMenu();
                        return;
                    }
                    System.out.println(UIConstants.CLEAR_DATA_FAILED_MESSAGE);
                }
                break;
            case 4:
                return;
            default:
                System.out.println(UIConstants.INVALID_OPTION_MESSAGE);
        }
    }
} 