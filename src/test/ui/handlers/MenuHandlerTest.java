/*
 * 
 */
package test.ui.handlers;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import pennywise.ui.UIConstants;
import pennywise.ui.handlers.*;
import test.stubs.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;


/**
 * The Class MenuHandlerTest.
 */
public class MenuHandlerTest {
    
    /** The menu handler. */
    private MenuHandler menuHandler;
    
    /** The mock pennywise. */
    private MockPennyWise mockPennywise;
    
    /** The mock input handler. */
    private MockInputHandler mockInputHandler;
    
    /** The mock transaction handler. */
    private MockTransactionHandler mockTransactionHandler;
    
    /** The mock budget handler. */
    private MockBudgetHandler mockBudgetHandler;
    
    /** The mock discount handler. */
    private MockDiscountHandler mockDiscountHandler;
    
    /** The mock account handler. */
    private MockAccountHandler mockAccountHandler;
    
    /** The output stream. */
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    
    /** The original out. */
    private final PrintStream originalOut = System.out;

    /**
     * Sets the up.
     */
    @BeforeEach
    void setUp() {
        mockPennywise = new MockPennyWise();
        mockInputHandler = new MockInputHandler();
        mockTransactionHandler = new MockTransactionHandler(mockPennywise, mockInputHandler);
        mockBudgetHandler = new MockBudgetHandler(mockPennywise, mockInputHandler);
        mockDiscountHandler = new MockDiscountHandler();
        mockAccountHandler = new MockAccountHandler(mockPennywise, mockInputHandler);
        
        menuHandler = new MenuHandler(mockPennywise, mockInputHandler, mockTransactionHandler,
                                    mockBudgetHandler, mockDiscountHandler, mockAccountHandler);
        System.setOut(new PrintStream(outputStream));
    }

    /**
     * Tear down.
     */
    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    /**
     * Test login menu valid login.
     */
    @Test
    void testLoginMenuValidLogin() {
        // Test Case: Verify successful login option
        // Tests:
        // 1. User selects login option (1)
        // 2. Login handler is called
        mockInputHandler.queueInt(1);
        menuHandler.handleLoginMenu();
        assertTrue(mockAccountHandler.wasLoginCalled());
    }

    /**
     * Test login menu valid registration.
     */
    @Test
    void testLoginMenuValidRegistration() {
        // Test Case: Verify successful registration option
        // Tests:
        // 1. User selects registration option (2)
        // 2. Registration handler is called
        mockInputHandler.queueInt(2);
        menuHandler.handleLoginMenu();
        assertTrue(mockAccountHandler.wasRegistrationCalled());
    }

    /**
     * Test login menu invalid option.
     */
    @Test
    void testLoginMenuInvalidOption() {
        // Test Case: Verify handling of invalid login menu option
        // Tests:
        // 1. User selects invalid option (99)
        // 2. Error message is displayed
        mockInputHandler.queueInt(99);
        menuHandler.handleLoginMenu();
        assertTrue(outputStream.toString().contains(UIConstants.INVALID_INPUT_MESSAGE));
    }

    /**
     * Test main menu add expense.
     */
    @Test
    void testMainMenuAddExpense() {
        // Test Case: Verify add expense option
        // Tests:
        // 1. User selects add expense option (1)
        // 2. Add expense handler is called
        mockInputHandler.queueInt(1);
        menuHandler.handleMainMenu();
        assertTrue(mockTransactionHandler.wasAddExpenseCalled());
    }

    /**
     * Test main menu add income.
     */
    @Test
    void testMainMenuAddIncome() {
        // Test Case: Verify add income option
        // Tests:
        // 1. User selects add income option (2)
        // 2. Add income handler is called
        mockInputHandler.queueInt(2);
        menuHandler.handleMainMenu();
        assertTrue(mockTransactionHandler.wasAddIncomeCalled());
    }

    /**
     * Test main menu view transactions.
     */
    @Test
    void testMainMenuViewTransactions() {
        // Test Case: Verify view transactions option
        // Tests:
        // 1. User selects view transactions option (3)
        // 2. View transactions handler is called
        mockInputHandler.queueInt(3);
        menuHandler.handleMainMenu();
        assertTrue(mockTransactionHandler.wasViewTransactionsCalled());
    }

    /**
     * Test main menu view monthly expenses.
     */
    @Test
    void testMainMenuViewMonthlyExpenses() {
        // Test Case: Verify view monthly expenses option
        // Tests:
        // 1. User selects view monthly expenses option (4)
        // 2. View monthly expenses handler is called
        mockInputHandler.queueInt(4);
        menuHandler.handleMainMenu();
        assertTrue(mockTransactionHandler.wasViewMonthlyExpensesCalled());
    }

    /**
     * Test main menu view monthly incomes.
     */
    @Test
    void testMainMenuViewMonthlyIncomes() {
        // Test Case: Verify view monthly incomes option
        // Tests:
        // 1. User selects view monthly incomes option (5)
        // 2. View monthly incomes handler is called
        mockInputHandler.queueInt(5);
        menuHandler.handleMainMenu();
        assertTrue(mockTransactionHandler.wasViewMonthlyIncomesCalled());
    }

    /**
     * Test main menu view expenses by category.
     */
    @Test
    void testMainMenuViewExpensesByCategory() {
        // Test Case: Verify view expenses by category option
        // Tests:
        // 1. User selects view expenses by category option (6)
        // 2. View expenses by category handler is called
        mockInputHandler.queueInt(6);
        menuHandler.handleMainMenu();
        assertTrue(mockTransactionHandler.wasViewExpensesByCategoryCalled());
    }

    /**
     * Test main menu view incomes by category.
     */
    @Test
    void testMainMenuViewIncomesByCategory() {
        // Test Case: Verify view incomes by category option
        // Tests:
        // 1. User selects view incomes by category option (7)
        // 2. View incomes by category handler is called
        mockInputHandler.queueInt(7);
        menuHandler.handleMainMenu();
        assertTrue(mockTransactionHandler.wasViewIncomesByCategoryCalled());
    }

    /**
     * Test main menu create budget.
     */
    @Test
    void testMainMenuCreateBudget() {
        // Test Case: Verify create budget option
        // Tests:
        // 1. User selects create budget option (8)
        // 2. Create budget handler is called
        mockInputHandler.queueInt(8);
        menuHandler.handleMainMenu();
        assertTrue(mockBudgetHandler.wasCreateBudgetCalled());
    }

    /**
     * Test main menu edit budget.
     */
    @Test
    void testMainMenuEditBudget() {
        // Test Case: Verify edit budget option
        // Tests:
        // 1. User selects edit budget option (9)
        // 2. Edit budget handler is called
        mockInputHandler.queueInt(9);
        menuHandler.handleMainMenu();
        assertTrue(mockBudgetHandler.wasEditBudgetCalled());
    }

    /**
     * Test main menu view budgets.
     */
    @Test
    void testMainMenuViewBudgets() {
        // Test Case: Verify view budgets option
        // Tests:
        // 1. User selects view budgets option (10)
        // 2. View budgets handler is called
        mockInputHandler.queueInt(10);
        menuHandler.handleMainMenu();
        assertTrue(mockBudgetHandler.wasViewBudgetsCalled());
    }

    /**
     * Test main menu view balance.
     */
    @Test
    void testMainMenuViewBalance() {
        // Test Case: Verify view balance option
        // Tests:
        // 1. User selects view balance option (11)
        // 2. View balance handler is called
        mockInputHandler.queueInt(11);
        menuHandler.handleMainMenu();
        assertTrue(mockTransactionHandler.wasViewBalanceCalled());
    }

    /**
     * Test main menu discount visualization.
     */
    @Test
    void testMainMenuDiscountVisualization() {
        // Test Case: Verify discount visualization option
        // Tests:
        // 1. User selects discount visualization option (12)
        // 2. Discount visualization handler is called
        mockInputHandler.queueInt(12);
        menuHandler.handleMainMenu();
        assertTrue(mockDiscountHandler.wasDiscountVisualizationCalled());
    }

    /**
     * Test main menu account management.
     */
    @Test
    void testMainMenuAccountManagement() {
        // Test Case: Verify account management option
        // Tests:
        // 1. User selects account management option (13)
        // 2. Account management handler is called
        mockInputHandler.queueInt(13);
        menuHandler.handleMainMenu();
        assertTrue(mockAccountHandler.wasAccountManagementCalled());
    }

    /**
     * Test main menu logout.
     */
    @Test
    void testMainMenuLogout() {
        // Test Case: Verify logout functionality
        // Tests:
        // 1. User selects logout option (14)
        // 2. Logout is called on PennyWise instance
        mockInputHandler.queueInt(14);
        menuHandler.handleMainMenu();
        assertTrue(mockPennywise.wasLogoutCalled());
    }

    /**
     * Test main menu invalid option.
     */
    @Test
    void testMainMenuInvalidOption() {
        // Test Case: Verify handling of invalid main menu option
        // Tests:
        // 1. User selects invalid option (99)
        // 2. Error message is displayed
        mockInputHandler.queueInt(99);
        menuHandler.handleMainMenu();
        assertTrue(outputStream.toString().contains(UIConstants.INVALID_INPUT_MESSAGE));
    }
}