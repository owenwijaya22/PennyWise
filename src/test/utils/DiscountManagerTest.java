package test.utils;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import pennywise.model.Discount;
import java.util.*;
import java.lang.reflect.Field;
import pennywise.utils.DiscountManager;

public class DiscountManagerTest {
//    private MockDiscountManager discountManager;
	private DiscountManager discountManager;
    private static final String TEST_CODE = "TEST10";
    private static final float TEST_PERCENTAGE = 10.0f;
    private static final String TEST_DESCRIPTION = "Test Discount";

//     @BeforeEach
//     void setUp() throws Exception {
//         discountManager = new MockDiscountManager();
//     }

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() throws Exception {
        // Reset the singleton instance before each test
        Field instance = DiscountManager.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);
        
        // Get a fresh instance
        discountManager = DiscountManager.getInstance();
        
        // Clear any existing discounts
        Field discountsField = DiscountManager.class.getDeclaredField("discounts");
        discountsField.setAccessible(true);
        ((List<Discount>)discountsField.get(discountManager)).clear();
    }

    @AfterEach
    void tearDown() throws Exception {
        // Reset the singleton instance after each test
        Field instance = DiscountManager.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);
    }

    @Test
    void testAddValidDiscount() {
        // Test Case: Verify valid discount addition
        Date futureDate = new Date(System.currentTimeMillis() + 86400000); // tomorrow
        Discount discount = new Discount(TEST_CODE, TEST_PERCENTAGE, futureDate, TEST_DESCRIPTION);
        
        discountManager.addDiscount(discount);
        List<Discount> discounts = discountManager.getAvailableDiscounts();
        
        assertEquals(1, discounts.size());
        assertEquals(TEST_CODE, discounts.get(0).getCode());
    }

    @Test
    void testDuplicateDiscountCode() {
        // Test Case: Verify duplicate discount code handling
        Date futureDate = new Date(System.currentTimeMillis() + 86400000);
        Discount discount1 = new Discount(TEST_CODE, TEST_PERCENTAGE, futureDate, TEST_DESCRIPTION);
        Discount discount2 = new Discount(TEST_CODE, 20.0f, futureDate, "Different Description");
        
        discountManager.addDiscount(discount1);
        discountManager.addDiscount(discount2);
        
        List<Discount> discounts = discountManager.getAvailableDiscounts();
        assertEquals(1, discounts.size());
        assertEquals(TEST_PERCENTAGE, discounts.get(0).getPercentage());
    }

    @Test
    void testFindDiscountByCode() {
        // Test Case: Verify discount code search functionality
        Date futureDate = new Date(System.currentTimeMillis() + 86400000);
        Discount discount = new Discount(TEST_CODE, TEST_PERCENTAGE, futureDate, TEST_DESCRIPTION);
        
        discountManager.addDiscount(discount);
        
        Discount found = discountManager.findDiscountByCode(TEST_CODE);
        assertNotNull(found);
        assertEquals(TEST_CODE, found.getCode());
        
        Discount notFound = discountManager.findDiscountByCode("INVALID");
        assertNull(notFound);
    }

    @Test
    void testCaseInsensitiveSearch() {
        // Test Case: Verify case-insensitive discount code search
        Date futureDate = new Date(System.currentTimeMillis() + 86400000);
        Discount discount = new Discount(TEST_CODE, TEST_PERCENTAGE, futureDate, TEST_DESCRIPTION);
        
        discountManager.addDiscount(discount);
        
        Discount found = discountManager.findDiscountByCode(TEST_CODE.toLowerCase());
        assertNotNull(found);
        assertEquals(TEST_CODE, found.getCode());
    }

    @Test
    void testGetAvailableDiscountsImmutability() {
        // Test Case: Verify list immutability
        Date futureDate = new Date(System.currentTimeMillis() + 86400000);
        Discount discount = new Discount(TEST_CODE, TEST_PERCENTAGE, futureDate, TEST_DESCRIPTION);
        
        discountManager.addDiscount(discount);
        List<Discount> discounts = discountManager.getAvailableDiscounts();
        
        discounts.clear(); // Try to modify the returned list
        
        assertEquals(1, discountManager.getAvailableDiscounts().size());
    }

    @Test
    void testExpiredDiscount() {
        // Test Case: Verify expired discount handling
        Date pastDate = new Date(System.currentTimeMillis() - 86400000); // yesterday
        Discount expiredDiscount = new Discount(TEST_CODE, TEST_PERCENTAGE, pastDate, TEST_DESCRIPTION);
        
        discountManager.addDiscount(expiredDiscount);
        List<Discount> discounts = discountManager.getAvailableDiscounts();
        
        assertFalse(expiredDiscount.isValid());
        assertTrue(discounts.stream().noneMatch(Discount::isValid));
    }

    @Test
    void testInvalidPercentages() {
        // Test Case: Verify percentage bounds
        Date futureDate = new Date(System.currentTimeMillis() + 86400000);
        
        // Test negative percentage
        Discount negativeDiscount = new Discount("NEG10", -10.0f, futureDate, "Negative Discount");
        assertFalse(negativeDiscount.isValid());
        
        // Test zero percentage
        Discount zeroDiscount = new Discount("ZERO", 0.0f, futureDate, "Zero Discount");
        assertFalse(zeroDiscount.isValid());
        
        // Test 100% discount
        Discount hundredDiscount = new Discount("HUNDRED", 100.0f, futureDate, "100% Discount");
        assertFalse(hundredDiscount.isValid());
        
        // Test > 100% discount
        Discount overDiscount = new Discount("OVER", 150.0f, futureDate, "Over 100% Discount");
        assertFalse(overDiscount.isValid());
    }

    @Test
    void testSpecialCharactersInCode() {
        // Test Case: Verify handling of special characters in discount codes
        Date futureDate = new Date(System.currentTimeMillis() + 86400000);
        
        Discount specialCharsDiscount = new Discount("TEST@#$%", TEST_PERCENTAGE, futureDate, TEST_DESCRIPTION);
        discountManager.addDiscount(specialCharsDiscount);
        
        Discount found = discountManager.findDiscountByCode("TEST@#$%");
        assertNotNull(found);
        assertEquals("TEST@#$%", found.getCode());
    }

    @Test
    void testMultipleValidDiscounts() {
        // Test Case: Verify handling of multiple valid discounts
        Date futureDate = new Date(System.currentTimeMillis() + 86400000);
        
        // Add multiple discounts with different codes
        for (int i = 0; i < 5; i++) {
            Discount discount = new Discount(
                "CODE" + i,
                TEST_PERCENTAGE + i,
                futureDate,
                "Description " + i
            );
            discountManager.addDiscount(discount);
        }
        
        List<Discount> discounts = discountManager.getAvailableDiscounts();
        assertEquals(5, discounts.size());
        
        // Verify all discounts are retrievable
        for (int i = 0; i < 5; i++) {
            Discount found = discountManager.findDiscountByCode("CODE" + i);
            assertNotNull(found);
            assertEquals(TEST_PERCENTAGE + i, found.getPercentage());
        }
    }
}
