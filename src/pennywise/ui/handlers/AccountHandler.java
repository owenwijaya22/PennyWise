package pennywise.ui.handlers;

import pennywise.PennyWise;
import pennywise.ui.UIConstants;

public class AccountHandler {
    private final PennyWise pennywise;
    private final InputHandler inputHandler;
    private MenuHandler menuHandler;

    public AccountHandler(PennyWise pennywise, InputHandler inputHandler) {
        this.pennywise = pennywise;
        this.inputHandler = inputHandler;
    }

    public void setMenuHandler(MenuHandler menuHandler) {
        this.menuHandler = menuHandler;
    }

    public void handleLogin() {
        System.out.print(UIConstants.ENTER_USER_ID_PROMPT);
        String userId = inputHandler.readLine();
        
        if (pennywise.login(userId)) {
            System.out.println(UIConstants.LOGIN_SUCCESS_MESSAGE);
        } else {
            System.out.println(UIConstants.LOGIN_FAILED_MESSAGE);
        }
    }

    public void handleRegistration() {
        System.out.print(UIConstants.ENTER_NEW_USER_ID_PROMPT);
        String userId = inputHandler.readLine();
        
        if (pennywise.registerUser(userId)) {
            System.out.println(UIConstants.REGISTRATION_SUCCESS_MESSAGE);
        } else {
            System.out.println(UIConstants.REGISTRATION_FAILED_MESSAGE);
        }
    }

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