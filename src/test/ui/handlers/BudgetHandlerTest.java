/*
 * 
 */
package test.ui.handlers;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import pennywise.ui.UIConstants;
import pennywise.ui.handlers.BudgetHandler;
import test.stubs.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;


/**
 * The Class BudgetHandlerTest.
 */
public class BudgetHandlerTest {
    
    /** The budget handler. */
    private BudgetHandler budgetHandler;
    
    /** The mock pennywise. */
    private MockPennyWise mockPennywise;
    
    /** The mock input handler. */
    private MockInputHandler mockInputHandler;
    
    /** The mock budget manager. */
    private MockBudgetManager mockBudgetManager;
    
    /** The mock data storage. */
    private MockDataStorage mockDataStorage;
    
    /** The output stream. */
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    
    /** The original out. */
    private final PrintStream originalOut = System.out;

    /**
     * Sets the up.
     */
    @BeforeEach
    void setUp() {
        mockDataStorage = new MockDataStorage();
        mockPennywise = new MockPennyWise();
        mockInputHandler = new MockInputHandler();
        mockBudgetManager = new MockBudgetManager(mockDataStorage);
        mockPennywise.setBudgetManager(mockBudgetManager);
        budgetHandler = new BudgetHandler(mockPennywise, mockInputHandler);
        System.setOut(new PrintStream(outputStream));
    }

    /**
     * Tear down.
     */
    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        mockDataStorage.clearAllData();
    }

    /**
     * Test create budget success.
     */
    @Test
    void testCreateBudgetSuccess() {
        // Test Case: Verify successful budget creation
        // Tests:
        // 1. User is logged in
        // 2. Valid budget amount is provided
        // 3. Success message is displayed
        // 4. Budget is correctly stored
        
        mockPennywise.login("testUser");
        mockInputHandler.queueDouble(1000.0);
        
        budgetHandler.handleCreateBudget();
        
        assertTrue(outputStream.toString().contains(UIConstants.BUDGET_SET_SUCCESS_MESSAGE));
        assertEquals(1000.0, mockBudgetManager.getCurrentMonthBudget("testUser"));
    }

    /**
     * Test create budget with negative amount.
     */
    @Test
    void testCreateBudgetWithNegativeAmount() {
        // Test Case: Verify handling of negative budget amounts
        // Tests:
        // 1. User is logged in
        // 2. Negative amount is provided
        // 3. Error message is displayed
        // 4. Budget remains at default value
        
        mockPennywise.login("testUser");
        mockInputHandler.queueDouble(-100.0);
        
        budgetHandler.handleCreateBudget();
        
        assertTrue(outputStream.toString().contains(UIConstants.INVALID_NUMBER_MESSAGE));
        assertEquals(0.0, mockBudgetManager.getCurrentMonthBudget("testUser"));
    }

    /**
     * Test create budget without login.
     */
    @Test
    void testCreateBudgetWithoutLogin() {
        // Test Case: Verify budget creation without login
        // Tests:
        // 1. No user is logged in
        // 2. Attempt to create budget
        // 3. Login prompt is displayed
        mockPennywise.logout();
        mockInputHandler.queueDouble(1000.0);
        
        budgetHandler.handleCreateBudget();
        
        assertTrue(outputStream.toString().contains(UIConstants.LOGIN_PROMPT));
    }

    /**
     * Test edit budget success.
     */
    @Test
    void testEditBudgetSuccess() {
        // Test Case: Verify successful budget update
        // Tests:
        // 1. User is logged in
        // 2. Initial budget exists
        // 3. Valid new amount is provided
        // 4. Success message is displayed
        // 5. Budget is correctly updated
        
        mockPennywise.login("testUser");
        mockBudgetManager.createBudget("testUser", 1000.0);
        mockInputHandler.queueDouble(2000.0);
        
        budgetHandler.handleEditBudget();
        
        assertTrue(outputStream.toString().contains(UIConstants.BUDGET_UPDATE_SUCCESS_MESSAGE));
        assertEquals(2000.0, mockBudgetManager.getCurrentMonthBudget("testUser"));
    }

    /**
     * Test edit budget with negative amount.
     */
    @Test
    void testEditBudgetWithNegativeAmount() {
        // Test Case: Verify handling of negative budget updates
        // Tests:
        // 1. User is logged in
        // 2. Initial budget exists
        // 3. Negative amount is provided
        // 4. Error message is displayed
        // 5. Budget remains unchanged
        
        mockPennywise.login("testUser");
        mockBudgetManager.createBudget("testUser", 1000.0);
        mockInputHandler.queueDouble(-100.0);
        
        budgetHandler.handleEditBudget();
        
        assertTrue(outputStream.toString().contains(UIConstants.INVALID_NUMBER_MESSAGE));
        assertEquals(1000.0, mockBudgetManager.getCurrentMonthBudget("testUser"));
    }

    /**
     * Test edit budget without login.
     */
    @Test
    void testEditBudgetWithoutLogin() {
        // Test Case: Verify budget update without login
        // Tests:
        // 1. No user is logged in
        // 2. Attempt to edit budget
        // 3. Login prompt is displayed
        
        mockInputHandler.queueDouble(2000.0);
        
        budgetHandler.handleEditBudget();
        
        assertTrue(outputStream.toString().contains(UIConstants.LOGIN_PROMPT));
    }

    /**
     * Test view budgets with no user.
     */
    @Test
    void testViewBudgetsWithNoUser() {
        // Test Case: Verify budget view without login
        // Tests:
        // 1. No user is logged in
        // 2. Attempt to view budgets
        // 3. Login prompt is displayed
        
        budgetHandler.handleViewBudgets();
        assertTrue(outputStream.toString().contains(UIConstants.LOGIN_PROMPT));
    }

    /**
     * Test view budgets with no budget.
     */
    @Test
    void testViewBudgetsWithNoBudget() {
        // Test Case: Verify view with no existing budget
        // Tests:
        // 1. User is logged in
        // 2. No budget exists
        // 3. Prompt to create budget is displayed
        // 4. User declines to create new budget
        
        mockPennywise.login("testUser");
        mockInputHandler.queueBoolean(false); // Don't create new budget
        budgetHandler.handleViewBudgets();
        assertEquals(0.0, mockBudgetManager.getCurrentMonthBudget("testUser"));
    }

    /**
     * Test view budgets with no budget and create new.
     */
    @Test
    void testViewBudgetsWithNoBudgetAndCreateNew() {
        // Test Case: Verify budget creation from view
        // Tests:
        // 1. User is logged in
        // 2. No budget exists
        // 3. User accepts prompt to create budget
        // 4. Valid amount is provided
        // 5. Budget is created successfully
        
        mockPennywise.login("testUser");
        mockInputHandler.queueBoolean(true); // Create new budget
        mockInputHandler.queueDouble(1000.0);
        
        budgetHandler.handleViewBudgets();
        
        assertTrue(outputStream.toString().contains(UIConstants.BUDGET_SET_SUCCESS_MESSAGE));
        assertEquals(1000.0, mockBudgetManager.getCurrentMonthBudget("testUser"));
    }

    /**
     * Test view budgets and edit budget.
     */
    @Test
    void testViewBudgetsAndEditBudget() {
        // Test Case: Verify budget edit from view
        // Tests:
        // 1. User is logged in
        // 2. Initial budget exists
        // 3. User chooses to edit budget
        // 4. Valid new amount is provided
        // 5. Budget is updated successfully
        
        mockPennywise.login("testUser");
        mockBudgetManager.createBudget("testUser", 1000.0);
        mockInputHandler.queueBoolean(true); // Edit budget
        mockInputHandler.queueDouble(2000.0);
        
        budgetHandler.handleViewBudgets();
        
        assertTrue(outputStream.toString().contains(UIConstants.BUDGET_UPDATE_SUCCESS_MESSAGE));
        assertEquals(2000.0, mockBudgetManager.getCurrentMonthBudget("testUser"));
    }
}