package test.service;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import pennywise.model.*;
import pennywise.service.TransactionHistory;
import java.util.List;

public class TransactionHistoryTest {
    private TransactionHistory history;
    private static final String TEST_USER_ID = "testUser";

    @BeforeEach
    void setUp() {
        history = new TransactionHistory();
    }

    @Test
    void testAddTransaction() {
        // Test Case: Verify single transaction addition
        // Tests:
        // 1. Adding a single transaction
        // 2. Verifying transaction count
        // 3. Confirming transaction details match
        
        Transaction transaction = new Transaction(TEST_USER_ID, 100.0, ExpenseCategory.FOOD);
        history.addTransaction(transaction);
        
        List<Transaction> transactions = history.getTransactionHistory();
        assertEquals(1, transactions.size());
        assertEquals(transaction, transactions.get(0));
    }

    @Test
    void testMultipleTransactions() {
        // Test Case: Verify multiple transaction handling
        // Tests:
        // 1. Adding multiple transactions
        // 2. Verifying transaction count
        // 3. Confirming transaction order
        
        Transaction expense = new Transaction(TEST_USER_ID, 100.0, ExpenseCategory.FOOD);
        Transaction income = new Transaction(TEST_USER_ID, 1000.0, IncomeCategory.SALARY);
        
        history.addTransaction(expense);
        history.addTransaction(income);
        
        List<Transaction> transactions = history.getTransactionHistory();
        assertEquals(2, transactions.size());
        assertEquals(expense, transactions.get(0));
        assertEquals(income, transactions.get(1));
    }

    @Test
    void testTransactionHistoryImmutability() {
        // Test Case: Verify transaction history immutability
        // Tests:
        // 1. Adding initial transaction
        // 2. Attempting to modify returned list
        // 3. Verifying original list remains unchanged
        
        Transaction transaction = new Transaction(TEST_USER_ID, 100.0, ExpenseCategory.FOOD);
        history.addTransaction(transaction);
        
        List<Transaction> transactions = history.getTransactionHistory();
        transactions.clear(); // Try to modify the returned list
        
        assertFalse(history.getTransactionHistory().isEmpty());
        assertEquals(1, history.getTransactionHistory().size());
    }
}
