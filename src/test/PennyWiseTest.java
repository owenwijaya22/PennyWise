package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import pennywise.PennyWise;
import pennywise.model.*;
import pennywise.service.*;
import pennywise.interfaces.*;

import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class PennyWiseTest {
    private PennyWise pennywise;
    private static final String TEST_DATA_DIR = "./test_data";
    private static final String TEST_USER_ID = "testUser";

    @BeforeEach
    void setUp() {
        // Clean test directory and set up real components for system tests
        File directory = new File(TEST_DATA_DIR);
        if (directory.exists()) {
            for (File file : directory.listFiles()) {
                file.delete();
            }
        } else {
            directory.mkdirs();
        }
        
        // Initialize with real components for system tests
        pennywise = new PennyWise(TEST_DATA_DIR);
    }

    @AfterEach
    void tearDown() {
        // Clean up test data
        File directory = new File(TEST_DATA_DIR);
        if (directory.exists()) {
            for (File file : directory.listFiles()) {
                file.delete();
            }
            directory.delete();
        }
    }

    @Test
    void testUserRegistrationAndLogin() {
        // Test Case: Verify basic user registration and login flow
        // Tests:
        // 1. Register a new user
        // 2. Login with registered credentials
        // 3. Verify login status and user identity
        
        assertTrue(pennywise.registerUser(TEST_USER_ID));
        assertTrue(pennywise.login(TEST_USER_ID));
        assertTrue(pennywise.isLoggedIn());
        assertEquals(TEST_USER_ID, pennywise.getCurrentUser().getUserId());
    }

    @Test 
    void testInvalidRegistration() {
        // Test Case: Verify system handling of invalid registrations
        // Tests:
        // 1. Registration with null user ID
        // 2. Registration with empty user ID
        // 3. Registration with whitespace-only user ID
        
        assertFalse(pennywise.registerUser(null));
        assertFalse(pennywise.registerUser(""));
        assertFalse(pennywise.registerUser("   "));
    }

    @Test
    void testDuplicateRegistration() {
        // Test Case: Verify system prevents duplicate user registrations
        // 1. Register a user successfully
        // 2. Attempt to register the same user ID again
        assertTrue(pennywise.registerUser(TEST_USER_ID));
        assertFalse(pennywise.registerUser(TEST_USER_ID));
    }

    @Test
    void testDeleteAccount() {
        // Test Case: Verify account deletion functionality
        // 1. Register and login a user
        // 2. Delete the account
        // 3. Verify user is logged out and can't log back in
        pennywise.registerUser(TEST_USER_ID);
        pennywise.login(TEST_USER_ID);
        
        assertTrue(pennywise.deleteAccount());
        assertFalse(pennywise.isLoggedIn());
        assertFalse(pennywise.login(TEST_USER_ID));
    }

    @Test
    void testSystemErrorRecovery() {
        // Test Case: System stability during error conditions
        // Tests:
        // 1. System response to invalid budget creation
        // 2. System response to invalid transaction
        
        assertTrue(pennywise.registerUser("errorTestUser"));
        assertTrue(pennywise.login("errorTestUser"));
        
        assertFalse(pennywise.createBudget(-1000.0));
        assertFalse(pennywise.addTransaction(-500.0, null));
        
        assertEquals(0.0, pennywise.getTotalExpenses());
        assertEquals(0.0, pennywise.getCurrentBalance());
        assertTrue(pennywise.isLoggedIn());
    }

    @Test
    void testConcurrentOperations() {
        // Test Case: Handling concurrent operations
        // Tests:
        // 1. Test system's handling of rapid concurrent operations
        // 2. Simulate concurrent transactions and wait all threads to complete
        assertTrue(pennywise.registerUser("concurrentUser"));
        assertTrue(pennywise.login("concurrentUser"));
        assertTrue(pennywise.createBudget(5000.0));
        
        // Add atomic counters to track successful transactions
        AtomicInteger successfulIncomes = new AtomicInteger(0);
        AtomicInteger successfulExpenses = new AtomicInteger(0);
        
        Thread[] threads = new Thread[5];
        for (int i = 0; i < 5; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 20; j++) {
                    if (pennywise.addTransaction(100.0, IncomeCategory.SALARY)) {
                        successfulIncomes.incrementAndGet();
                    }
                    if (pennywise.addTransaction(50.0, ExpenseCategory.FOOD)) {
                        successfulExpenses.incrementAndGet();
                    }
                }
            });
            threads[i].start();
        }
        
        // Wait for all threads to complete
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                fail("Thread interrupted");
            }
        }
        
        // Verify system consistency using the actual number of successful transactions
        double expectedIncome = successfulIncomes.get() * 100.0;
        double expectedExpenses = successfulExpenses.get() * 50.0;
        assertEquals(expectedIncome, pennywise.getTotalIncome());
        assertEquals(expectedExpenses, pennywise.getTotalExpenses());
        
        // Log the actual numbers for debugging
        System.out.println("Successful income transactions: " + successfulIncomes.get());
        System.out.println("Successful expense transactions: " + successfulExpenses.get());
    }
}

