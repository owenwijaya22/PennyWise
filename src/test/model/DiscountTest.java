package test.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.util.*;
import pennywise.model.Discount;
public class DiscountTest {
    private static final String TEST_CODE = "SAVE20";
    private static final float TEST_PERCENTAGE = 20.0f;
    private static final String TEST_DESCRIPTION = "Test discount";

    @Test
    void testValidDiscount() {
        // Test Case: Verify valid discount creation and validation
        // Tests:
        // 1. Creating discount with valid parameters
        // 2. Verifying discount properties
        // 3. Checking validity status
        
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 7); // Set expiry to 7 days from now
        Date futureDate = cal.getTime();
        
        Discount discount = new Discount(TEST_CODE, TEST_PERCENTAGE, futureDate, TEST_DESCRIPTION);
        
        assertTrue(discount.isValid());
        assertEquals(TEST_CODE, discount.getCode());
        assertEquals(TEST_PERCENTAGE, discount.getPercentage());
        assertEquals(TEST_DESCRIPTION, discount.getDescription());
        assertEquals(futureDate, discount.getExpiryDate());
    }

    @Test
    void testExpiredDiscount() {
        // Test Case: Verify expired discount handling
        // Tests:
        // 1. Creating discount with past date
        // 2. Verifying invalid status
        
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -1); // Set expiry to yesterday
        Date pastDate = cal.getTime();
        
        Discount discount = new Discount(TEST_CODE, TEST_PERCENTAGE, pastDate, TEST_DESCRIPTION);
        assertFalse(discount.isValid());
    }

    @Test
    void testInvalidPercentages() {
        // Test Case: Verify percentage validation
        // Tests:
        // 1. Testing negative percentage
        // 2. Testing zero percentage
        // 3. Testing 100% and above
        
        Date futureDate = new Date(System.currentTimeMillis() + 86400000); // tomorrow
        
        Discount negativeDiscount = new Discount(TEST_CODE, -10.0f, futureDate, TEST_DESCRIPTION);
        assertFalse(negativeDiscount.isValid());
        
        Discount zeroDiscount = new Discount(TEST_CODE, 0.0f, futureDate, TEST_DESCRIPTION);
        assertFalse(zeroDiscount.isValid());
        
        Discount hundredDiscount = new Discount(TEST_CODE, 100.0f, futureDate, TEST_DESCRIPTION);
        assertFalse(hundredDiscount.isValid());
        
        Discount overHundredDiscount = new Discount(TEST_CODE, 150.0f, futureDate, TEST_DESCRIPTION);
        assertFalse(overHundredDiscount.isValid());
    }

    @Test
    void testBoundaryPercentages() {
        // Test Case: Verify percentage boundary conditions
        // Tests:
        // 1. Testing just above 0%
        // 2. Testing just below 100%
        
        Date futureDate = new Date(System.currentTimeMillis() + 86400000);
        
        Discount lowBoundary = new Discount(TEST_CODE, 0.1f, futureDate, TEST_DESCRIPTION);
        assertTrue(lowBoundary.isValid());
        
        Discount highBoundary = new Discount(TEST_CODE, 99.9f, futureDate, TEST_DESCRIPTION);
        assertTrue(highBoundary.isValid());
    }
}
