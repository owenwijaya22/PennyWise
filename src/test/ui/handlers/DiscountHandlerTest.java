package test.ui.handlers;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import pennywise.model.Discount;
import pennywise.ui.UIConstants;
import pennywise.ui.handlers.DiscountHandler;
import test.stubs.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;

public class DiscountHandlerTest {
    private DiscountHandler discountHandler;
    private MockInputHandler mockInputHandler;
    private MockDiscountManager mockDiscountManager;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        mockInputHandler = new MockInputHandler();
        mockDiscountManager = new MockDiscountManager();
        discountHandler = new DiscountHandler(mockDiscountManager, mockInputHandler);
        System.setOut(new PrintStream(outputStream));
        outputStream.reset();
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        mockDiscountManager.clearDiscounts();
    }

    @Test
    void testDisplayDiscountsEmpty() {
        // Test Case: Verify display of empty discount list
        // Tests:
        // 1. No discounts exist
        // 2. View discounts option selected
        // 3. No discounts message is displayed
        
        mockInputHandler.queueInt(1); // View discounts
        mockInputHandler.queueInt(4); // Exit
        
        discountHandler.handleDiscountVisualization();
        
        assertTrue(outputStream.toString().contains(UIConstants.NO_DISCOUNTS_MESSAGE));
    }

    @Test
    void testDisplayDiscountsWithValidDiscounts() {
        // Test Case: Verify display of existing discounts
        // Tests:
        // 1. Valid discount exists
        // 2. View discounts option selected
        // 3. Discount details are correctly displayed
        
        Date futureDate = new Date(System.currentTimeMillis() + 86400000);
        Discount testDiscount = new Discount("TEST10", 10.0f, futureDate, "Test Discount");
        mockDiscountManager.addDiscount(testDiscount);
        
        mockInputHandler.queueInt(1); // View discounts
        mockInputHandler.queueInt(4); // Exit
        
        discountHandler.handleDiscountVisualization();
        
        String output = outputStream.toString();
        assertTrue(output.contains("TEST10"), "Should display discount code");
        assertTrue(output.contains("Test Discount"), "Should display discount description");
    }

    @Test
    void testAddCustomDiscountSuccess() {
        // Test Case: Verify successful addition of custom discount
        // Tests:
        // 1. Select add custom discount option
        // 2. Enter valid discount details:
        //    - Unique code
        //    - Valid percentage
        //    - Description
        //    - Valid duration
        // 3. Discount is added successfully
        // 4. All discount properties match input values
        
        mockInputHandler.queueInt(2);                    // Add custom discount
        mockInputHandler.queueString("TEST20");          // Code
        mockInputHandler.queueFloat(20.0f);              // Percentage
        mockInputHandler.queueString("Test Description"); // Description
        mockInputHandler.queueInt(7);                    // Days
        mockInputHandler.queueInt(4);                    // Exit
        
        discountHandler.handleDiscountVisualization();
        
        Discount addedDiscount = mockDiscountManager.findDiscountByCode("TEST20");
        assertNotNull(addedDiscount, "Custom discount should be added successfully");
        assertEquals(20.0f, addedDiscount.getPercentage(), "Discount percentage should match input");
        assertEquals("Test Description", addedDiscount.getDescription(), "Discount description should match input");
    }

    @Test
    void testAddPredeterminedDiscount() {
        // Test Case: Verify adding a predetermined discount
        // Tests:
        // 1. Select predetermined discount option
        // 2. Choose specific discount (Apple Student)
        // 3. Discount is added successfully
        // 4. Discount properties are correct
        
        mockInputHandler.queueInt(3); // Add predetermined discount
        mockInputHandler.queueInt(1); // Choose Apple Student Discount
        mockInputHandler.queueInt(4); // Exit
        
        discountHandler.handleDiscountVisualization();
        
        Discount addedDiscount = mockDiscountManager.findDiscountByCode("APPLEEDU");
        assertNotNull(addedDiscount, "Predetermined discount should be added");
        assertEquals(10.0f, addedDiscount.getPercentage(), "Discount percentage should match");
    }

    @Test
    void testInvalidMenuOption() {
        // Test Case: Verify handling of invalid menu options
        // Tests:
        // 1. User selects invalid menu option
        // 2. Error message is displayed
        // 3. Program continues to run
        
        mockInputHandler.queueInt(99); // Invalid option
        mockInputHandler.queueInt(4); // Exit
        
        discountHandler.handleDiscountVisualization();
        
        assertTrue(outputStream.toString().contains(UIConstants.INVALID_OPTION_MESSAGE), 
                   "Should display invalid option message");
    }

    @Test
    void testExitOption() {
        // Test Case: Verify menu exit functionality
        // Tests:
        // 1. User selects exit option
        // 2. Menu title is displayed
        // 3. Program exits cleanly
        
        mockInputHandler.queueInt(4); // Exit immediately
        
        discountHandler.handleDiscountVisualization();
        
        assertTrue(outputStream.toString().contains(UIConstants.DISCOUNT_MENU_TITLE), 
                   "Should display menu title before exit");
    }

    @Test
    void testAddDuplicateDiscount() {
        // Test Case: Verify handling of duplicate discount codes
        // Tests:
        // 1. Initial discount exists
        // 2. Attempt to add duplicate discount code
        // 3. Original discount remains unchanged
        // 4. No new discount is added
        // 5. Total number of discounts remains the same
        
        // Setup existing discount
        Date futureDate = new Date(System.currentTimeMillis() + 86400000);
        Discount testDiscount = new Discount("TEST10", 10.0f, futureDate, "Test Discount");
        mockDiscountManager.addDiscount(testDiscount);
        
        // Queue inputs for adding duplicate discount
        mockInputHandler.queueInt(2);                    // Add custom discount
        mockInputHandler.queueString("TEST10");          // Duplicate code
        mockInputHandler.queueFloat(20.0f);              // Different percentage
        mockInputHandler.queueString("New Description"); // Description
        mockInputHandler.queueInt(7);                    // Days until expiry
        mockInputHandler.queueInt(4);                    // Exit
        
        discountHandler.handleDiscountVisualization();
        
        // Verify original discount remains unchanged
        Discount originalDiscount = mockDiscountManager.findDiscountByCode("TEST10");
        assertNotNull(originalDiscount, "Original discount should still exist");
        assertEquals(10.0f, originalDiscount.getPercentage(), "Original discount percentage should be unchanged");
        assertEquals("Test Discount", originalDiscount.getDescription(), "Original discount description should be unchanged");
        
        // Verify no new discount was added
        assertEquals(1, mockDiscountManager.getAvailableDiscounts().size(), "Should only have one discount");
    }

    @Test
    void testHandleDiscountCodeValid() {
        // Test Case: Verify applying a valid discount code
        // Tests:
        // 1. Valid discount exists and is not expired
        // 2. Correct discount amount is calculated
        // 3. Success message is displayed with correct values
        
        Date futureDate = new Date(System.currentTimeMillis() + 86400000);
        Discount testDiscount = new Discount("TEST25", 25.0f, futureDate, "Test Discount");
        mockDiscountManager.addDiscount(testDiscount);
        
        mockInputHandler.queueString("TEST25");  // Input discount code
        
        discountHandler.handleDiscountCode(100.0);  // Test with $100 amount
        
        String output = outputStream.toString();
        assertTrue(output.contains("$25.00"), "Should display correct discount amount");
    }

    @Test
    void testHandleDiscountCodeInvalid() {
        // Test Case: Verify handling invalid discount code
        // Tests:
        // 1. Non-existent discount code
        // 2. Error message is displayed
        
        mockInputHandler.queueString("INVALID");  // Input invalid discount code
        
        discountHandler.handleDiscountCode(100.0);
        
        assertTrue(outputStream.toString().contains(UIConstants.INVALID_DISCOUNT_MESSAGE));
    }

    @Test
    void testHandleDiscountCodeExpired() {
        // Test Case: Verify handling expired discount code
        // Tests:
        // 1. Discount exists but is expired
        // 2. Error message is displayed
        
        Date pastDate = new Date(System.currentTimeMillis() - 86400000); // Yesterday
        Discount expiredDiscount = new Discount("EXPIRED", 10.0f, pastDate, "Expired Discount");
        mockDiscountManager.addDiscount(expiredDiscount);
        
        mockInputHandler.queueString("EXPIRED");  // Input expired discount code
        
        discountHandler.handleDiscountCode(100.0);
        
        assertTrue(outputStream.toString().contains(UIConstants.INVALID_DISCOUNT_MESSAGE));
    }
} 