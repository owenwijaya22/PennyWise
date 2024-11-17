package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import pennywise.PennyWise;
import pennywise.model.ExpenseCategory;

import java.io.File;

public class PennyWiseTest {
    private PennyWise pennywise;
    private static final String TEST_DATA_DIR = "./test_data";
    private static final String TEST_USER_ID = "testUser";

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
    void testGetCurrentUserWhenLoggedOut() {
        // Test Case: Verify getCurrentUser behavior when logged out
        // Tests:
        // 1. Get current user when no one is logged in
        // 2. Get current user after login
        // 3. Get current user after logout
        
        assertNull(pennywise.getCurrentUser());
        
        assertTrue(pennywise.registerUser(TEST_USER_ID));
        assertTrue(pennywise.login(TEST_USER_ID));
        assertNotNull(pennywise.getCurrentUser());
        assertEquals(TEST_USER_ID, pennywise.getCurrentUser().getUserId());
        
        pennywise.logout();
        assertNull(pennywise.getCurrentUser());
    }

    @Test
    void testDeleteAccountWhenNotLoggedIn() {
        // Test Case: Verify delete account behavior when not logged in
        // Tests:
        // 1. Delete account with no active session
        // 2. Delete account after logging out
        
        assertFalse(pennywise.deleteAccount());
        
        assertTrue(pennywise.registerUser(TEST_USER_ID));
        assertTrue(pennywise.login(TEST_USER_ID));
        pennywise.logout();
        assertFalse(pennywise.deleteAccount());
    }

    @Test
    void testClearUserDataWhenNotLoggedIn() {
        // Test Case: Verify clear user data behavior when not logged in
        // Tests:
        // 1. Clear data with no active session
        // 2. Clear data after logging out
        
        assertFalse(pennywise.clearAllUserData());
        
        assertTrue(pennywise.registerUser(TEST_USER_ID));
        assertTrue(pennywise.login(TEST_USER_ID));
        pennywise.logout();
        assertFalse(pennywise.clearAllUserData());
    }

    @Test
    void testConsoleUIIntegration() {
        // Test Case: Verify PennyWise integration with ConsoleUI
        // Tests:
        // 1. Verify PennyWise instance creates ConsoleUI successfully
        // 2. Verify basic user operations through PennyWise affect UI state
        // 3. Verify UI reflects correct authentication state
        
        PennyWise testPennyWise = new PennyWise(TEST_DATA_DIR);
        
        // Test registration and login through PennyWise
        assertTrue(testPennyWise.registerUser(TEST_USER_ID));
        assertTrue(testPennyWise.login(TEST_USER_ID));
        
        // Verify UI state through PennyWise
        assertTrue(testPennyWise.isLoggedIn());
        assertEquals(TEST_USER_ID, testPennyWise.getCurrentUser().getUserId());
        
        // Test logout affects UI state
        testPennyWise.logout();
        assertFalse(testPennyWise.isLoggedIn());
        assertNull(testPennyWise.getCurrentUser());
    }

    @Test
    void testInvalidLogin() {
        // Test Case: Verify system handling of invalid login attempts
        // Tests:
        // 1. Login with null user ID
        // 2. Login with empty user ID
        // 3. Login with whitespace-only user ID
        // 4. Login with non-existent user ID
        
        assertFalse(pennywise.login(null));
        assertFalse(pennywise.login(""));
        assertFalse(pennywise.login("   "));
        assertFalse(pennywise.login("nonexistentUser"));
        assertFalse(pennywise.isLoggedIn());
    }

    @Test
    void testNegativeAmountOperations() {
        // Test Case: Verify system handling of negative amounts
        // Tests:
        // 1. Create budget with negative amount
        // 2. Update budget with negative amount
        
        assertTrue(pennywise.registerUser(TEST_USER_ID));
        assertTrue(pennywise.login(TEST_USER_ID));
        
        assertFalse(pennywise.createBudget(-100.0));
        assertFalse(pennywise.updateBudget(-50.0));
        
        // Verify valid amounts still work
        assertTrue(pennywise.createBudget(100.0));
        assertTrue(pennywise.updateBudget(150.0));
    }

    @Test
    void testTransactionOperationsWhenLoggedOut() {
        // Test Case: Verify transaction operations when not logged in
        // Tests:
        // 1. Get total income when logged out
        // 2. Get total expenses when logged out
        // 3. Get current balance when logged out
        
        assertEquals(0.0, pennywise.getTotalIncome());
        assertEquals(0.0, pennywise.getTotalExpenses());
        assertEquals(0.0, pennywise.getCurrentBalance());
        
        // Verify same methods work when logged in
        assertTrue(pennywise.registerUser(TEST_USER_ID));
        assertTrue(pennywise.login(TEST_USER_ID));
        
        assertEquals(0.0, pennywise.getTotalIncome());  // Should still be 0 but now because of no transactions
        assertEquals(0.0, pennywise.getTotalExpenses());
        assertEquals(0.0, pennywise.getCurrentBalance());
    }

    @Test
    void testDeleteAccountCascadingEffects() {
        // Test Case: Verify all effects of account deletion
        // Tests:
        // 1. Verify user is logged out after deletion
        // 2. Verify user data is actually removed
        // 3. Verify cannot perform operations after deletion
        
        assertTrue(pennywise.registerUser(TEST_USER_ID));
        assertTrue(pennywise.login(TEST_USER_ID));
        
        // Create some data
        assertTrue(pennywise.createBudget(1000.0));
        
        // Delete account
        assertTrue(pennywise.deleteAccount());
        
        // Verify logged out
        assertFalse(pennywise.isLoggedIn());
        assertNull(pennywise.getCurrentUser());
        
        // Verify cannot log back in
        assertFalse(pennywise.login(TEST_USER_ID));
        
        // Verify cannot perform operations
        assertEquals(0.0, pennywise.getTotalIncome());
        assertEquals(0.0, pennywise.getTotalExpenses());
        assertEquals(0.0, pennywise.getCurrentBalance());
    }

    @Test
    void testDeleteAccountLogoutBehavior() {
        // Test Case: Verify logout behavior when account is deleted
        // Tests:
        // 1. Verify user is logged in before deletion
        // 2. Verify deletion triggers logout
        // 3. Verify login state after deletion
        
        assertTrue(pennywise.registerUser(TEST_USER_ID));
        assertTrue(pennywise.login(TEST_USER_ID));
        assertTrue(pennywise.isLoggedIn());
        
        // Delete account and verify it triggers logout
        assertTrue(pennywise.deleteAccount());
        assertFalse(pennywise.isLoggedIn());
        assertNull(pennywise.getCurrentUser());
        
        // Verify cannot log back in after deletion
        assertFalse(pennywise.login(TEST_USER_ID));
    }

    @Test
    void testTransactionValidation() {
        // Test Case: Verify transaction amount validation
        // Tests:
        // 1. Transaction with negative amount
        // 2. Transaction with zero amount
        // 3. Transaction when user is not logged in
        
        // Test when not logged in
        assertFalse(pennywise.addTransaction(100.0, ExpenseCategory.FOOD));
        
        // Login and test invalid amounts
        assertTrue(pennywise.registerUser(TEST_USER_ID));
        assertTrue(pennywise.login(TEST_USER_ID));
        
        assertFalse(pennywise.addTransaction(-100.0, ExpenseCategory.FOOD));
        assertFalse(pennywise.addTransaction(0.0, ExpenseCategory.FOOD));
        
        // Verify valid transaction still works
        assertTrue(pennywise.addTransaction(100.0, ExpenseCategory.FOOD));
    }

    @Test
    void testBudgetValidation() {
        // Test Case: Verify budget amount validation
        // Tests:
        // 1. Budget with negative amount
        // 2. Budget when user is not logged in
        
        // Test when not logged in
        assertFalse(pennywise.createBudget(1000.0));
        assertFalse(pennywise.updateBudget(1000.0));
        
        // Login and test invalid amounts
        assertTrue(pennywise.registerUser(TEST_USER_ID));
        assertTrue(pennywise.login(TEST_USER_ID));
        
        assertFalse(pennywise.createBudget(-1000.0));
        assertFalse(pennywise.updateBudget(-1000.0));
        
        // Verify valid amounts still work
        assertTrue(pennywise.createBudget(1000.0));
        assertTrue(pennywise.updateBudget(1500.0));
    }
}

