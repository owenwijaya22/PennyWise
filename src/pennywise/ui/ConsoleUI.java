package pennywise.ui;

import pennywise.PennyWise;
import pennywise.ui.handlers.*;
import pennywise.utils.DiscountManager;

public class ConsoleUI {
    private final PennyWise pennywise;
    private final InputHandler inputHandler;
    private final MenuHandler menuHandler;

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