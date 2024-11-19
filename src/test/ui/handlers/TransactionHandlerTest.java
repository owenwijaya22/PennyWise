/*
 * 
 */
package test.ui.handlers;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import pennywise.interfaces.TransactionCategory;
import pennywise.model.*;
import pennywise.ui.UIConstants;
import pennywise.ui.handlers.*;
import test.stubs.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;


/**
 * The Class TransactionHandlerTest.
 */
public class TransactionHandlerTest {
    
    /** The transaction handler. */
    private TransactionHandler transactionHandler;
    
    /** The mock pennywise. */
    private MockPennyWise mockPennywise;
    
    /** The mock input handler. */
    private MockInputHandler mockInputHandler;
    
    /** The mock analyzer. */
    private MockTransactionAnalyzer mockAnalyzer;
    
    /** The mock discount manager. */
    private MockDiscountManager mockDiscountManager;
    
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
        mockAnalyzer = new MockTransactionAnalyzer(new ArrayList<>());
        mockDiscountManager = new MockDiscountManager();
        mockPennywise.setAnalyzer(mockAnalyzer);
        transactionHandler = new TransactionHandler(mockPennywise, mockInputHandler);
        System.setOut(new PrintStream(outputStream));
        outputStream.reset();
        mockPennywise.logout();
    }

    /**
     * Tear down.
     */
    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        mockDiscountManager.clearDiscounts();
    }

    /**
     * Test add expense without discount.
     */
    @Test
    void testAddExpenseWithoutDiscount() {
        // Test Case: Adding expense without applying discount
        // Tests:
        // 1. Adding valid expense amount
        // 2. Not using discount
        // 3. Verifying expense is recorded correctly
        
        mockPennywise.login("testUser");
        mockInputHandler.queueDouble(100.0);
        mockInputHandler.queueBoolean(false); // No discount
        mockInputHandler.queueInt(1); // Select FOOD category

        transactionHandler.handleAddExpense();

        assertTrue(outputStream.toString().contains("successfully"));
        assertEquals(-100.0, mockPennywise.getTotalExpenses());
    }

    /**
     * Test add expense with valid discount.
     */
    @Test
    void testAddExpenseWithValidDiscount() {
        // Test Case: Adding expense with valid discount code
        // Tests:
        // 1. Adding expense with discount
        // 2. Verifying discount calculation
        // 3. Checking final amount after discount
        
        mockPennywise.login("testUser");
        mockInputHandler.queueDouble(100.0);
        mockInputHandler.queueBoolean(true); // Use discount
        mockInputHandler.queueString("TEST10");
        mockInputHandler.queueInt(1); // Select FOOD category

        // Create a valid discount
        Date futureDate = new Date(System.currentTimeMillis() + 86400000);
        Discount testDiscount = new Discount("TEST10", 10.0f, futureDate, "Test Discount");
        mockDiscountManager.addDiscount(testDiscount);

        transactionHandler.handleAddExpense();

        assertTrue(outputStream.toString().contains(UIConstants.DISCOUNT_APPLIED_FORMAT.formatted(10.0, 10.0)));
        assertEquals(-90.0, mockPennywise.getTotalExpenses(), 0.01); // Allow small delta for floating point precision
    }

    /**
     * Test add expense with invalid amount.
     */
    @Test
    void testAddExpenseWithInvalidAmount() {
        // Test Case: Handling invalid expense amount
        // Tests:
        // 1. Attempting to add negative expense
        // 2. Verifying error message
        // 3. Ensuring no transaction is recorded
        
        mockPennywise.login("testUser");
        mockInputHandler.queueDouble(-100.0);
        
        transactionHandler.handleAddExpense();
        
        assertTrue(outputStream.toString().contains(UIConstants.INVALID_NUMBER_MESSAGE));
        assertEquals(0.0, mockPennywise.getTotalExpenses());
    }

    /**
     * Test add income.
     */
    @Test
    void testAddIncome() {
        // Test Case: Adding valid income transaction
        // Tests:
        // 1. Adding positive income amount
        // 2. Verifying success message
        // 3. Checking recorded income amount
        
        mockPennywise.login("testUser");
        mockInputHandler.queueDouble(1000.0);
        mockInputHandler.queueInt(1); // Select SALARY category

        transactionHandler.handleAddIncome();

        assertTrue(outputStream.toString().contains(UIConstants.INCOME_SUCCESS_MESSAGE));
        assertEquals(1000.0, mockPennywise.getTotalIncome());
    }

    /**
     * Test add income with invalid amount.
     */
    @Test
    void testAddIncomeWithInvalidAmount() {
        // Test Case: Handling invalid income amount
        // Tests:
        // 1. Attempting to add negative income
        // 2. Verifying error message
        // 3. Ensuring no transaction is recorded
        
        mockPennywise.login("testUser");
        mockInputHandler.queueDouble(-1000.0);
        
        transactionHandler.handleAddIncome();
        
        assertTrue(outputStream.toString().contains(UIConstants.INVALID_NUMBER_MESSAGE));
        assertEquals(0.0, mockPennywise.getTotalIncome());
    }

    /**
     * Test add income without login.
     */
    @Test
    void testAddIncomeWithoutLogin() {
        // Test Case: Adding income without user login
        // Tests:
        // 1. Attempting transaction without login
        // 2. Verifying operation fails silently
        // 3. Checking no income is recorded
        
        mockInputHandler.queueDouble(1000.0);
        mockPennywise.logout(); // Ensure logged out state
        
        transactionHandler.handleAddIncome();
        
        // Since handleAddIncome doesn't have an early login check, the operation will fail silently
        assertEquals(0.0, mockPennywise.getTotalIncome());
    }

    /**
     * Test view transactions empty.
     */
    @Test
    void testViewTransactionsEmpty() {
        // Test Case: Viewing transactions with empty history
        // Tests:
        // 1. Viewing transactions for new user
        // 2. Verifying empty transaction message
        
        mockPennywise.login("testUser");
        
        transactionHandler.handleViewTransactions();
        
        assertTrue(outputStream.toString().contains(UIConstants.NO_TRANSACTIONS_MESSAGE));
    }

    /**
     * Test view transactions with data.
     */
    @Test
    void testViewTransactionsWithData() {
        // Test Case: Viewing transactions with existing data
        // Tests:
        // 1. Adding multiple transaction types
        // 2. Verifying transaction display format
        // 3. Checking category and amount display
        
        mockPennywise.login("testUser");
        
        // Add transactions in chronological order
        mockPennywise.addTransaction(100.0, IncomeCategory.SALARY);
        mockPennywise.addTransaction(50.0, ExpenseCategory.FOOD);  // Make sure expense is negative

        transactionHandler.handleViewTransactions();

        String output = outputStream.toString();
        // Check for transaction type labels
        assertTrue(output.contains("INCOME"));
        assertTrue(output.contains("EXPENSE"));
        
        // Check for exact amount formatting using UIConstants.TRANSACTION_FORMAT
        assertTrue(output.contains(String.format("%.2f", 100.0)));
        assertTrue(output.contains(String.format("%.2f", 50.0)));
        
        // Check for category names
        assertTrue(output.contains(IncomeCategory.SALARY.getCategoryName()));
        assertTrue(output.contains(ExpenseCategory.FOOD.getCategoryName()));
    }

    /**
     * Test view monthly expenses no login.
     */
    @Test
    void testViewMonthlyExpensesNoLogin() {
        // Test Case: Viewing monthly expenses without login
        // Tests:
        // 1. Attempting to view expenses without being logged in
        // 2. Verifying login prompt message
        // 3. Checking analyzer is properly nullified
        
        mockPennywise.logout();
        mockPennywise.setAnalyzer(null); // When not logged in, analyzer should be null
        
        transactionHandler.handleViewMonthlyExpenses();
        
        assertTrue(outputStream.toString().contains(UIConstants.LOGIN_PROMPT_MONTHLY_EXPENSES));
    }

    /**
     * Test view monthly expenses empty.
     */
    @Test
    void testViewMonthlyExpensesEmpty() {
        // Test Case: Viewing monthly expenses with no data
        // Tests:
        // 1. Viewing expenses for new user
        // 2. Verifying empty expenses message
        
        mockPennywise.login("testUser");
        
        transactionHandler.handleViewMonthlyExpenses();
        
        assertTrue(outputStream.toString().contains(UIConstants.NO_EXPENSES_MESSAGE));
    }

    /**
     * Test view monthly incomes no login.
     */
    @Test
    void testViewMonthlyIncomesNoLogin() {
        // Test Case: Viewing monthly incomes without login
        // Tests:
        // 1. Attempting to view incomes without being logged in
        // 2. Verifying login prompt message
        // 3. Checking analyzer is properly nullified
        
        mockPennywise.logout();
        mockPennywise.setAnalyzer(null); // When not logged in, analyzer should be null
        
        transactionHandler.handleViewMonthlyIncomes();
        
        assertTrue(outputStream.toString().contains(UIConstants.LOGIN_PROMPT_MONTHLY_EXPENSES));
    }

    /**
     * Test view monthly incomes empty.
     */
    @Test
    void testViewMonthlyIncomesEmpty() {
        // Test Case: Viewing monthly incomes with no data
        // Tests:
        // 1. Viewing incomes for new user
        // 2. Verifying empty incomes message
        
        mockPennywise.login("testUser");
        
        transactionHandler.handleViewMonthlyIncomes();
        
        assertTrue(outputStream.toString().contains(UIConstants.NO_INCOMES_MESSAGE));
    }

    /**
     * Test view incomes by category no login.
     */
    @Test
    void testViewIncomesByCategoryNoLogin() {
        // Test Case: Viewing income categories without login
        // Tests:
        // 1. Attempting to view income categories without being logged in
        // 2. Verifying login prompt message
        // 3. Checking analyzer is properly nullified
        
        mockPennywise.setAnalyzer(null);
        
        transactionHandler.handleViewIncomesByCategory();
        assertTrue(outputStream.toString().contains(UIConstants.LOGIN_PROMPT_INCOMES_CATEGORY));
    }

    /**
     * Test view expenses by category no login.
     */
    @Test
    void testViewExpensesByCategoryNoLogin() {
        // Test Case: Viewing expense categories without login
        // Tests:
        // 1. Attempting to view expense categories without being logged in
        // 2. Verifying login prompt message
        // 3. Checking analyzer is properly nullified
        
        mockPennywise.setAnalyzer(null);
        
        transactionHandler.handleViewExpensesByCategory();
        assertTrue(outputStream.toString().contains(UIConstants.LOGIN_PROMPT_EXPENSES_CATEGORY));
    }

    /**
     * Test view expenses by category empty.
     */
    @Test
    void testViewExpensesByCategoryEmpty() {
        // Test Case: Viewing expense categories with no data
        // Tests:
        // 1. Viewing expense categories for new user
        // 2. Verifying empty expenses message
        
        mockPennywise.login("testUser");
        
        transactionHandler.handleViewExpensesByCategory();
        
        assertTrue(outputStream.toString().contains(UIConstants.NO_EXPENSES_MESSAGE));
    }

    /**
     * Test view incomes by category empty.
     */
    @Test
    void testViewIncomesByCategoryEmpty() {
        // Test Case: Viewing income categories with no data
        // Tests:
        // 1. Viewing income categories for new user
        // 2. Verifying empty incomes message
        
        mockPennywise.login("testUser");
        
        transactionHandler.handleViewIncomesByCategory();
        
        assertTrue(outputStream.toString().contains(UIConstants.NO_INCOMES_MESSAGE));
    }

    /**
     * Test view balance.
     */
    @Test
    void testViewBalance() {
        // Test Case: Viewing current balance with transactions
        // Tests:
        // 1. Adding income and expense transactions
        // 2. Calculating expected balance
        // 3. Verifying balance calculation accuracy
        
        mockPennywise.login("testUser");
        mockPennywise.addTransaction(1000.0, IncomeCategory.SALARY);
        mockPennywise.addTransaction(500.0, ExpenseCategory.FOOD);

        transactionHandler.handleViewBalance();

        assertEquals(500.0, mockPennywise.getCurrentBalance());
    }

    /**
     * Test view incomes by category with data.
     */
    @Test
    void testViewIncomesByCategoryWithData() {
        // Test Case: Viewing income categories with existing data
        // Tests:
        // 1. Adding multiple transactions across categories
        // 2. Setting up mock analyzer with expected aggregations
        // 3. Verifying category totals and display format
        // 4. Checking correct category names and amounts
        
        mockPennywise.login("testUser");
        
        // Add sample transactions
        mockPennywise.addTransaction(1000.0, IncomeCategory.SALARY);
        mockPennywise.addTransaction(500.0, IncomeCategory.INVESTMENT);
        mockPennywise.addTransaction(200.0, IncomeCategory.SALARY);

        // Set up mock analyzer with expected data
        Map<TransactionCategory, Double> mockIncomesByCategory = new HashMap<>();
        mockIncomesByCategory.put(IncomeCategory.SALARY, 1200.0);
        mockIncomesByCategory.put(IncomeCategory.INVESTMENT, 500.0);
        mockAnalyzer.setIncomesByCategory(mockIncomesByCategory);

        transactionHandler.handleViewIncomesByCategory();

        String output = outputStream.toString();
        // Verify category names and amounts are displayed
        assertTrue(output.contains(IncomeCategory.SALARY.getCategoryName()));
        assertTrue(output.contains(IncomeCategory.INVESTMENT.getCategoryName()));
        assertTrue(output.contains(String.format("%.2f", 1200.0)));
        assertTrue(output.contains(String.format("%.2f", 500.0)));
    }

    /**
     * Test view expenses by category with data.
     */
    @Test
    void testViewExpensesByCategoryWithData() {
        // Test Case: Viewing expense categories with existing data
        // Tests:
        // 1. Adding multiple transactions across categories
        // 2. Setting up mock analyzer with expected aggregations
        // 3. Verifying category totals and display format
        
        mockPennywise.login("testUser");
        
        // Add sample transactions
        mockPennywise.addTransaction(-500.0, ExpenseCategory.FOOD);
        mockPennywise.addTransaction(-300.0, ExpenseCategory.TRANSPORTATION);
        mockPennywise.addTransaction(-200.0, ExpenseCategory.FOOD);

        // Set up mock analyzer with expected data
        Map<TransactionCategory, Double> mockExpensesByCategory = new HashMap<>();
        mockExpensesByCategory.put(ExpenseCategory.FOOD, 700.0);
        mockExpensesByCategory.put(ExpenseCategory.TRANSPORTATION, 300.0);
        mockAnalyzer.setExpensesByCategory(mockExpensesByCategory);

        transactionHandler.handleViewExpensesByCategory();

        String output = outputStream.toString();
        // Verify category names and amounts are displayed
        assertTrue(output.contains(ExpenseCategory.FOOD.getCategoryName()));
        assertTrue(output.contains(ExpenseCategory.TRANSPORTATION.getCategoryName()));
        assertTrue(output.contains(String.format("%.2f", 700.0)));
        assertTrue(output.contains(String.format("%.2f", 300.0)));
    }

    /**
     * Test view monthly expenses with data.
     */
    @Test
    void testViewMonthlyExpensesWithData() {
        // Test Case: Viewing monthly expenses with existing data
        // Tests:
        // 1. Adding multiple transactions in different months
        // 2. Setting up mock analyzer with monthly aggregations
        // 3. Verifying monthly totals and display format
        
        mockPennywise.login("testUser");
        
        // Add sample transactions
        mockPennywise.addTransaction(-500.0, ExpenseCategory.FOOD);
        mockPennywise.addTransaction(-300.0, ExpenseCategory.TRANSPORTATION);

        // Set up mock analyzer with expected monthly data
        Map<String, Double> mockMonthlyExpenses = new HashMap<>();
        mockMonthlyExpenses.put("January 2024", 500.0);
        mockMonthlyExpenses.put("February 2024", 300.0);
        mockAnalyzer.setMonthlyExpenses(mockMonthlyExpenses);

        transactionHandler.handleViewMonthlyExpenses();

        String output = outputStream.toString();
        // Verify months and amounts are displayed
        assertTrue(output.contains("January 2024"));
        assertTrue(output.contains("February 2024"));
        assertTrue(output.contains(String.format("%.2f", 500.0)));
        assertTrue(output.contains(String.format("%.2f", 300.0)));
    }

    /**
     * Test view monthly incomes with data.
     */
    @Test
    void testViewMonthlyIncomesWithData() {
        // Test Case: Viewing monthly incomes with existing data
        // Tests:
        // 1. Adding multiple transactions in different months
        // 2. Setting up mock analyzer with monthly aggregations
        // 3. Verifying monthly totals and display format
        
        mockPennywise.login("testUser");
        
        // Add sample transactions
        mockPennywise.addTransaction(1000.0, IncomeCategory.SALARY);
        mockPennywise.addTransaction(500.0, IncomeCategory.INVESTMENT);

        // Set up mock analyzer with expected monthly data
        Map<String, Double> mockMonthlyIncomes = new HashMap<>();
        mockMonthlyIncomes.put("January 2024", 1000.0);
        mockMonthlyIncomes.put("February 2024", 500.0);
        mockAnalyzer.setMonthlyIncomes(mockMonthlyIncomes);

        transactionHandler.handleViewMonthlyIncomes();

        String output = outputStream.toString();
        // Verify months and amounts are displayed
        assertTrue(output.contains("January 2024"));
        assertTrue(output.contains("February 2024"));
        assertTrue(output.contains(String.format("%.2f", 1000.0)));
        assertTrue(output.contains(String.format("%.2f", 500.0)));
    }
}