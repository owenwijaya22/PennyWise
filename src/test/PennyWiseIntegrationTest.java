/*
 * 
 */
package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import pennywise.model.*;
import pennywise.service.*;
import pennywise.interfaces.*;
import java.util.*;
import pennywise.PennyWise;
import java.io.File;


/**
 * The Class PennyWiseIntegrationTest.
 */
public class PennyWiseIntegrationTest {
    
    /** The pennywise. */
    private PennyWise pennywise;
    
    /** The Constant TEST_DATA_DIR. */
    private static final String TEST_DATA_DIR = "./test_integration_data";
    
    /** The Constant TEST_USER_ID. */
    private static final String TEST_USER_ID = "testUser";

    /**
     * Sets the up.
     */
    @BeforeEach
    void setUp() {
        // Clean test directory before creating PennyWise instance
        File directory = new File(TEST_DATA_DIR);
        if (directory.exists()) {
            for (File file : directory.listFiles()) {
                file.delete();
            }
            directory.delete();
        }
        
        // Initialize PennyWise - FileDataStorage constructor will create directory
        pennywise = new PennyWise(TEST_DATA_DIR);
    }

    /**
     * Tear down.
     */
    @AfterEach
    void tearDown() {
        // Clean up test data after each test
        File directory = new File(TEST_DATA_DIR);
        if (directory.exists()) {
            for (File file : directory.listFiles()) {
                file.delete();
            }
            directory.delete();
        }
    }

    /**
     * Test budget and transaction integration.
     */
    @Test
    void testBudgetAndTransactionIntegration() {
        // Test Case: Budget and Transaction component integration
        // Tests:
        // 1. Budget creation and validation
        // 2. Transaction processing within budget limits
        // 3. Storage consistency across components
        
        assertTrue(pennywise.registerUser(TEST_USER_ID));
        assertTrue(pennywise.login(TEST_USER_ID));
        
        assertTrue(pennywise.createBudget(1000.0));
        // Note: We pass positive amounts, the system will convert them to negative for expenses
        assertTrue(pennywise.addTransaction(500.0, ExpenseCategory.FOOD));
        assertFalse(pennywise.addTransaction(600.0, ExpenseCategory.ENTERTAINMENT));
        
        assertEquals(500.0, Math.abs(pennywise.getTotalExpenses()));
        assertEquals(1000.0, pennywise.getBudgetManager().getCurrentMonthBudget(TEST_USER_ID));
    }

    /**
     * Test storage and analyzer integration.
     */
    @Test
    void testStorageAndAnalyzerIntegration() {
        // Test Case: Storage and Analyzer component integration
        // Tests:
        // 1. Transaction storage persistence
        // 2. Analyzer calculations from stored data
        // 3. Cross-component data consistency
        
        assertTrue(pennywise.registerUser(TEST_USER_ID));
        assertTrue(pennywise.login(TEST_USER_ID));
        
        pennywise.addTransaction(1000.0, IncomeCategory.SALARY);
        pennywise.addTransaction(300.0, ExpenseCategory.FOOD);
        pennywise.addTransaction(200.0, ExpenseCategory.UTILITIES);
        
        TransactionAnalyzer analyzer = pennywise.getAnalyzer();
        assertNotNull(analyzer);
        assertEquals(500.0, Math.abs(analyzer.getTotalExpenses()));
        assertEquals(1000.0, analyzer.getTotalIncome());
    }

    /**
     * Test complete component integration.
     */
    @Test
    void testCompleteComponentIntegration() {
        // Test Case: Full system component integration
        // Tests:
        // 1. User authentication with budget system
        // 2. Transaction processing across components
        // 3. Analysis and reporting integration
        // 4. Cross-component data consistency
        
        assertTrue(pennywise.registerUser(TEST_USER_ID));
        assertTrue(pennywise.login(TEST_USER_ID));
        
        assertTrue(pennywise.createBudget(3000.0));
        assertTrue(pennywise.addTransaction(2000.0, IncomeCategory.SALARY));
        assertTrue(pennywise.addTransaction(500.0, ExpenseCategory.FOOD));
        assertTrue(pennywise.addTransaction(300.0, ExpenseCategory.TRANSPORTATION));
        
        assertEquals(2000.0, pennywise.getTotalIncome());
        assertEquals(800.0, pennywise.getTotalExpenses()); // Now negative
        assertEquals(1200.0, pennywise.getCurrentBalance());
        assertEquals(3000.0, pennywise.getBudgetManager().getCurrentMonthBudget(TEST_USER_ID));
        
        TransactionAnalyzer analyzer = pennywise.getAnalyzer();
        Map<TransactionCategory, Double> expensesByCategory = analyzer.getExpensesByCategory();
        assertEquals(500.0, expensesByCategory.get(ExpenseCategory.FOOD)); // Now negative
        assertEquals(300.0, expensesByCategory.get(ExpenseCategory.TRANSPORTATION)); // Now negative
    }

    /**
     * Test user data management.
     */
    @Test
    void testUserDataManagement() {
        // Test user data clearing and application reset
        assertTrue(pennywise.registerUser(TEST_USER_ID));
        assertTrue(pennywise.login(TEST_USER_ID));
        
        // Add some data
        pennywise.createBudget(1000.0);
        pennywise.addTransaction(500.0, IncomeCategory.SALARY);
        
        // Test clearAllUserData
        assertTrue(pennywise.clearAllUserData());
        assertFalse(pennywise.isLoggedIn());
        
        // Try to login after clearing data
        assertFalse(pennywise.login(TEST_USER_ID));
        
        // Test resetApplication
        assertTrue(pennywise.registerUser("anotherUser"));
        assertTrue(PennyWise.resetApplication(TEST_DATA_DIR));
        assertFalse(pennywise.login("anotherUser"));
    }

    /**
     * Test transaction analysis.
     */
    @Test
    void testTransactionAnalysis() {
        assertTrue(pennywise.registerUser(TEST_USER_ID));
        assertTrue(pennywise.login(TEST_USER_ID));
        
        // Add mixed transactions
        assertTrue(pennywise.addTransaction(1000.0, IncomeCategory.SALARY));
        assertTrue(pennywise.addTransaction(200.0, ExpenseCategory.FOOD));
        assertTrue(pennywise.addTransaction(500.0, IncomeCategory.INVESTMENT));
        assertTrue(pennywise.addTransaction(300.0, ExpenseCategory.UTILITIES));
        
        // Test getTotalIncome
        assertEquals(1500.0, pennywise.getTotalIncome());
        
        // Test getAnalyzer
        TransactionAnalyzer analyzer = pennywise.getAnalyzer();
        assertNotNull(analyzer);
        assertEquals(1500.0, analyzer.getTotalIncome());
        assertEquals(500.0, analyzer.getTotalExpenses());
        
        // Test when not logged in
        pennywise.logout();
        assertNull(pennywise.getAnalyzer());
        assertEquals(0.0, pennywise.getTotalIncome());
    }

    /**
     * Test budget management.
     */
    @Test
    void testBudgetManagement() {
        assertTrue(pennywise.registerUser(TEST_USER_ID));
        assertTrue(pennywise.login(TEST_USER_ID));
        
        // Test budget creation
        assertTrue(pennywise.createBudget(1000.0));
        assertEquals(1000.0, pennywise.getBudgetManager().getCurrentMonthBudget(TEST_USER_ID));
        
        // Test budget update
        assertTrue(pennywise.updateBudget(1500.0));
        assertEquals(1500.0, pennywise.getBudgetManager().getCurrentMonthBudget(TEST_USER_ID));
        
        // Test invalid budget amounts
        assertFalse(pennywise.createBudget(-100.0));
        assertFalse(pennywise.updateBudget(-50.0));
    }

    /**
     * Test transaction management.
     */
    @Test
    void testTransactionManagement() {
        assertTrue(pennywise.registerUser(TEST_USER_ID));
        assertTrue(pennywise.login(TEST_USER_ID));
        
        // Set up budget
        assertTrue(pennywise.createBudget(1000.0));
        
        // Test valid transactions
        assertTrue(pennywise.addTransaction(500.0, IncomeCategory.SALARY));
        assertTrue(pennywise.addTransaction(300.0, ExpenseCategory.FOOD));
        
        // Test invalid transactions
        assertFalse(pennywise.addTransaction(-100.0, ExpenseCategory.FOOD)); // Negative amount
        assertFalse(pennywise.addTransaction(0.0, ExpenseCategory.FOOD)); // Zero amount
        
        // Test budget limit
        assertFalse(pennywise.addTransaction(800.0, ExpenseCategory.ENTERTAINMENT)); // Would exceed budget
        
        // Verify transactions
        List<Transaction> transactions = pennywise.getTransactions();
        assertEquals(2, transactions.size());
        
        // Test when not logged in
        pennywise.logout();
        assertTrue(pennywise.getTransactions().isEmpty());
        assertFalse(pennywise.addTransaction(100.0, ExpenseCategory.FOOD));
    }

    /**
     * Test main method initialization.
     */
    @Test
    void testMainMethodInitialization() {
        // Test Case: Verify main method initialization
        // Tests:
        // 1. PennyWise instance creation with default data directory
        // 2. Data directory creation and management
        
        // Create a test directory path similar to main method
        String testMainDir = "./pennywise_data_test_main";
        
        try {
            // Create a new PennyWise instance similar to main method
            PennyWise testPennyWise = new PennyWise(testMainDir);
            
            // Verify the instance is properly initialized
            assertNotNull(testPennyWise);
            
            // Verify data directory is created
            File dataDir = new File(testMainDir);
            assertTrue(dataDir.exists());
            assertTrue(dataDir.isDirectory());
            
            // Verify basic functionality works
            assertTrue(testPennyWise.registerUser("testUser"));
            assertTrue(testPennyWise.login("testUser"));
            assertTrue(testPennyWise.isLoggedIn());
            
        } finally {
            // Clean up test directory
            File dataDir = new File(testMainDir);
            if (dataDir.exists()) {
                for (File file : dataDir.listFiles()) {
                    file.delete();
                }
                dataDir.delete();
            }
        }
    }
}