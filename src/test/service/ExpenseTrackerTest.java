package test.service;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import pennywise.model.*;
import pennywise.interfaces.*;
import test.stubs.MockDataStorage;
import pennywise.service.ExpenseTracker;

import java.util.List;

public class ExpenseTrackerTest {
    private ExpenseTracker expenseTracker;
    private IDataStorage mockStorage;
    private static final String TEST_USER_ID = "testUser";

    @BeforeEach
    void setUp() {
        mockStorage = new MockDataStorage();
        expenseTracker = new ExpenseTracker(mockStorage);
    }

    @Test
    void testAddValidExpense() {
        // Test Case: Verify valid expense transaction creation
        // Tests:
        // 1. Add a valid expense transaction
        // 2. Verify transaction count
        // 3. Validate expense amount is negative
        // 4. Confirm correct expense category
        
        assertTrue(expenseTracker.addTransaction(TEST_USER_ID, 100.0, ExpenseCategory.FOOD));
        
        List<Transaction> transactions = expenseTracker.getTransactions(TEST_USER_ID);
        assertEquals(1, transactions.size());
        assertEquals(-100.0, transactions.get(0).getAmount()); // expense should be negative
        assertEquals(ExpenseCategory.FOOD, transactions.get(0).getCategory());
    }

    @Test
    void testAddValidIncome() {
        // Test Case: Verify valid income transaction creation
        // Tests:
        // 1. Add a valid income transaction
        // 2. Verify transaction count
        // 3. Validate income amount is positive
        // 4. Confirm correct income category
        
        assertTrue(expenseTracker.addTransaction(TEST_USER_ID, 1000.0, IncomeCategory.SALARY));
        
        List<Transaction> transactions = expenseTracker.getTransactions(TEST_USER_ID);
        assertEquals(1, transactions.size());
        assertEquals(1000.0, transactions.get(0).getAmount());
        assertEquals(IncomeCategory.SALARY, transactions.get(0).getCategory());
    }

    @Test
    void testAddInvalidTransaction() {
        // Test Case: Verify handling of invalid transaction amounts
        // Tests:
        // 1. Attempt to add transaction with zero amount
        // 2. Attempt to add transaction with negative amount
        // 3. Verify no transactions were stored
        
        assertFalse(expenseTracker.addTransaction(TEST_USER_ID, 0.0, ExpenseCategory.FOOD));
        assertFalse(expenseTracker.addTransaction(TEST_USER_ID, -100.0, ExpenseCategory.FOOD));
        
        List<Transaction> transactions = expenseTracker.getTransactions(TEST_USER_ID);
        assertTrue(transactions.isEmpty());
    }

    @Test
    void testMultipleTransactions() {
        // Test Case: Verify multiple transaction handling
        // Tests:
        // 1. Add multiple transactions of different types
        // 2. Verify correct transaction count
        
        expenseTracker.addTransaction(TEST_USER_ID, 100.0, ExpenseCategory.FOOD);
        expenseTracker.addTransaction(TEST_USER_ID, 1000.0, IncomeCategory.SALARY);
        expenseTracker.addTransaction(TEST_USER_ID, 50.0, ExpenseCategory.TRANSPORTATION);
        
        List<Transaction> transactions = expenseTracker.getTransactions(TEST_USER_ID);
        assertEquals(3, transactions.size());
    }
}
