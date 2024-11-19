/*
 * 
 */
package pennywise.ui;

import pennywise.PennyWise;
import pennywise.ui.handlers.*;
import pennywise.utils.DiscountManager;


/**
 * The Class ConsoleUI.
 */
public class ConsoleUI {
    
    /** The pennywise. */
    private final PennyWise pennywise;
    
    /** The input handler. */
    private final InputHandler inputHandler;
    
    /** The menu handler. */
    private final MenuHandler menuHandler;

    /**
     * Instantiates a new console UI.
     *
     * @param pennywise the pennywise
     */
    public ConsoleUI(PennyWise pennywise) {
        this.pennywise = pennywise;
        this.inputHandler = new InputHandler();
        
        TransactionHandler transactionHandler = new TransactionHandler(pennywise, inputHandler);
        BudgetHandler budgetHandler = new BudgetHandler(pennywise, inputHandler);
        DiscountHandler discountHandler = new DiscountHandler(DiscountManager.getInstance(), inputHandler);
        AccountHandler accountHandler = new AccountHandler(pennywise, inputHandler);
        
        this.menuHandler = new MenuHandler(pennywise, inputHandler, transactionHandler, budgetHandler, discountHandler, accountHandler);
        
        accountHandler.setMenuHandler(menuHandler);
    }

    /**
     * Start.
     */
    public void start() {
        while (true) {
            if (!pennywise.isLoggedIn()) {
                menuHandler.handleLoginMenu();
            } else {
                menuHandler.handleMainMenu();
            }
        }
    }
}