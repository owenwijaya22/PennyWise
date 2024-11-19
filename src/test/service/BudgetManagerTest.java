/*
 * 
 */
package test.service;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.jupiter.api.*;

import pennywise.interfaces.IDataStorage;
import pennywise.storage.FileDataStorage;
import pennywise.service.BudgetManager;


/**
 * The Class BudgetManagerTest.
 */
public class BudgetManagerTest {
    
    /** The budget manager. */
    private BudgetManager budgetManager;
    
    /** The Constant TEST_DATA_DIR. */
    private static final String TEST_DATA_DIR = "./test_data";
    
    /** The Constant TEST_USER_ID. */
    private static final String TEST_USER_ID = "testUser";

    /**
     * Sets the up.
     */
    @BeforeEach
    void setUp() {
        File directory = new File(TEST_DATA_DIR);
        directory.mkdirs();
        
        IDataStorage storage = new FileDataStorage(TEST_DATA_DIR);
        budgetManager = new BudgetManager(storage);
    }
    
    /**
     * Tear down.
     */
    @AfterEach
    void tearDown() {
        File directory = new File(TEST_DATA_DIR);
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    file.delete();
                }
            }
            directory.delete();
        }
    }

    /**
     * Test create budget.
     */
    @Test
    void testCreateBudget() {
        // Test Case: Verify budget creation functionality
        // Tests:
        // 1. Creating a new budget with valid amount
        // 2. Retrieving the created budget
        
        assertTrue(budgetManager.createBudget(TEST_USER_ID, 1000.0));
        assertEquals(1000.0, budgetManager.getCurrentMonthBudget(TEST_USER_ID));
    }

    /**
     * Test invalid budget.
     */
    @Test
    void testInvalidBudget() {
        // Test Case: Verify handling of invalid budget amounts
        // Tests:
        // 1. Attempt to create budget with negative amount
        // 2. Verify budget remains at default value (0.0)
        
        assertFalse(budgetManager.createBudget(TEST_USER_ID, -100.0));
        assertEquals(0.0, budgetManager.getCurrentMonthBudget(TEST_USER_ID));
    }

    /**
     * Test update budget.
     */
    @Test
    void testUpdateBudget() {
        // Test Case: Verify budget update functionality
        // Tests:
        // 1. Creating initial budget
        // 2. Updating to new amount
        // 3. Verifying the update was successful
        
        budgetManager.createBudget(TEST_USER_ID, 1000.0);
        assertTrue(budgetManager.updateBudget(TEST_USER_ID, 1500.0));
        assertEquals(1500.0, budgetManager.getCurrentMonthBudget(TEST_USER_ID));
    }

    /**
     * Test is over budget.
     */
    @Test
    void testIsOverBudget() {
        // Test Case: Verify budget limit checking functionality
        // Tests:
        // 1. Setting up initial budget
        // 2. Testing scenarios under budget
        // 3. Testing scenarios over budget
        
        budgetManager.createBudget(TEST_USER_ID, 1000.0);
        assertFalse(budgetManager.isOverBudget(TEST_USER_ID, 500.0, 400.0));
        assertTrue(budgetManager.isOverBudget(TEST_USER_ID, 800.0, 300.0));
    }

    /**
     * Test decimal budget.
     */
    @Test
    void testDecimalBudget() {
        // Test Case: Handling of decimal budget amounts
        // Tests:
        // 1. Create budget with decimal amount
        // 2. Verify exact decimal precision
        
        double decimalAmount = 100.55;
        assertTrue(budgetManager.createBudget(TEST_USER_ID, decimalAmount));
        assertEquals(decimalAmount, budgetManager.getCurrentMonthBudget(TEST_USER_ID));
    }

    /**
     * Test multiple budget updates.
     */
    @Test
    void testMultipleBudgetUpdates() {
        // Test Case: Multiple budget updates in same month
        // Tests:
        // 1. Creating initial budget
        // 2. Multiple updates
        // 3. Verifying only latest update is retained
        
        assertTrue(budgetManager.createBudget(TEST_USER_ID, 1000.0));
        assertTrue(budgetManager.updateBudget(TEST_USER_ID, 1500.0));
        assertTrue(budgetManager.updateBudget(TEST_USER_ID, 2000.0));
        assertEquals(2000.0, budgetManager.getCurrentMonthBudget(TEST_USER_ID));
    }

    /**
     * Test large budget amount.
     */
    @Test
    void testLargeBudgetAmount() {
        // Test Case: Handling of large budget amounts
        // Tests:
        // 1. Creating budget with large amount
        // 2. Verifying precision maintenance
        
        double largeAmount = 999999999.99;
        assertTrue(budgetManager.createBudget(TEST_USER_ID, largeAmount));
        assertEquals(largeAmount, budgetManager.getCurrentMonthBudget(TEST_USER_ID));
    }

    /**
     * Test budget overflow scenarios.
     */
    @Test
    void testBudgetOverflowScenarios() {
        // Test Case: Handling complex over-budget scenarios
        // Tests:
        // 1. Set up initial budget
        // 2. Test multiple expense combinations
        // 3. Edge cases for over-budget calculation
        
        budgetManager.createBudget(TEST_USER_ID, 1000.0);
        
        // Test budget amounts
        assertFalse(budgetManager.isOverBudget(TEST_USER_ID, 900.0, 100.0));  // exactly at budget
        assertTrue(budgetManager.isOverBudget(TEST_USER_ID, 900.0, 100.1));   // slightly over budget
        assertFalse(budgetManager.isOverBudget(TEST_USER_ID, 900.0, 99.9));   // slightly under budget
    }
    
    /**
     * Test update without initial budget.
     */
    @Test
    void testUpdateWithoutInitialBudget() {
        // Test Case: Updating budget without creating one first
        assertTrue(budgetManager.updateBudget(TEST_USER_ID, 1500.0));
        assertEquals(1500.0, budgetManager.getCurrentMonthBudget(TEST_USER_ID));
    }
    
    /**
     * Test double precision edge cases.
     */
    @Test
    void testDoublePrecisionEdgeCases() {
    	// Test Case: Handling edge cases for double precision
    	// Tests:
    	// 1. Set up initial budget
    	// 2. Test on small decimal differences
    	// 3. Test on max double precision
    	
        double budget = 1000.0;
        assertTrue(budgetManager.createBudget(TEST_USER_ID, budget));
        
        // Test very small decimal differences
        assertFalse(budgetManager.isOverBudget(TEST_USER_ID, 999.999999, 0.000001));
        assertTrue(budgetManager.isOverBudget(TEST_USER_ID, 999.999999, 0.000002));
        
        // Test with maximum double precision
        double maxPrecision = 999.999999999999;
        assertTrue(budgetManager.createBudget(TEST_USER_ID, maxPrecision));
        assertEquals(maxPrecision, budgetManager.getCurrentMonthBudget(TEST_USER_ID));
    }

    /**
     * Test update budget with negative amount.
     */
    @Test
    void testUpdateBudgetWithNegativeAmount() {
        // Test Case: Verify update budget with negative amount
        // Tests:
        // 1. Initial valid budget creation
        // 2. Attempt to update with negative amount
        // 3. Verify budget remains unchanged
        
        budgetManager.createBudget(TEST_USER_ID, 1000.0);
        assertFalse(budgetManager.updateBudget(TEST_USER_ID, -500.0));
        assertEquals(1000.0, budgetManager.getCurrentMonthBudget(TEST_USER_ID));
    }

    /**
     * Test is over budget with zero budget.
     */
    @Test
    void testIsOverBudgetWithZeroBudget() {
        // Test Case: Verify over-budget behavior with zero budget
        // Tests:
        // 1. Set up zero budget
        // 2. Verify any expense amount results in over-budget
        
        budgetManager.createBudget(TEST_USER_ID, 0.0);
        assertTrue(budgetManager.isOverBudget(TEST_USER_ID, 0.0, 0.1));
        assertTrue(budgetManager.isOverBudget(TEST_USER_ID, 100.0, 100.0));
        assertTrue(budgetManager.isOverBudget(TEST_USER_ID, 0.0, 0.0));
    }

    /**
     * Test set up with existing empty directory.
     */
    @Test
    void testSetUpWithExistingEmptyDirectory() {
        // Test Case: Setup with an existing but empty directory
        File directory = new File(TEST_DATA_DIR);
        directory.mkdirs();
        
        IDataStorage storage = new FileDataStorage(TEST_DATA_DIR);
        BudgetManager testManager = new BudgetManager(storage);
        
        // Verify manager works with empty directory
        assertTrue(testManager.createBudget(TEST_USER_ID, 100.0));
    }

    /**
     * Test tear down with missing files.
     */
    @Test
    void testTearDownWithMissingFiles() {
        // Test Case: TearDown with missing files
        File directory = new File(TEST_DATA_DIR);
        directory.mkdirs();
        
        // Create and immediately delete a file to test handling of missing files
        File testFile = new File(directory, "test.txt");
        testFile.delete();
        
    }

    /**
     * Test corrupted budget file.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    void testCorruptedBudgetFile() throws IOException {
        // Test Case: Corrupted budget file
        File budgetFile = new File(TEST_DATA_DIR, TEST_USER_ID + "_budgets.dat");
        budgetFile.getParentFile().mkdirs();
        try (FileOutputStream fos = new FileOutputStream(budgetFile)) {
            fos.write("corrupted data".getBytes());
        }

        // Verify the manager handles corrupted file gracefully
        assertEquals(0.0, budgetManager.getCurrentMonthBudget(TEST_USER_ID));
    }
}