package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import pennywise.model.*;
import pennywise.service.*;
import pennywise.interfaces.*;
import test.stubs.*;
import java.util.*;
import pennywise.PennyWise;

public class PennyWiseIntegrationTest {
    private PennyWise pennywise;
    private IDataStorage mockStorage;
    private BudgetManager mockBudgetManager;
    private TransactionAnalyzer mockAnalyzer;
    private static final String TEST_USER_ID = "testUser";

    @BeforeEach
    void setUp() {
        mockStorage = new MockDataStorage();
        mockBudgetManager = new MockBudgetManager(mockStorage);
        mockAnalyzer = new TransactionAnalyzer(new ArrayList<>());
        pennywise = new PennyWise(mockStorage, mockBudgetManager, mockAnalyzer);
    }

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
}