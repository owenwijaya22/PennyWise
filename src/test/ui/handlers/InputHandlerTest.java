package test.ui.handlers;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import pennywise.ui.handlers.InputHandler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class InputHandlerTest {
    private InputHandler inputHandler;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void testReadIntValidInput() {
        // Test Case: Verify reading valid integer input
        // Tests:
        // 1. Valid integer string is provided
        // 2. Correct integer value is returned
        
        String input = "42\n";
        provideInput(input);
        inputHandler = new InputHandler();
        
        assertEquals(42, inputHandler.readInt());
        inputHandler.close();
    }

    @Test
    void testReadIntInvalidInput() {
        // Test Case: Verify handling of invalid integer input
        // Tests:
        // 1. Non-integer string is provided
        // 2. Error value (-1) is returned
        
        String input = "not a number\n";
        provideInput(input);
        inputHandler = new InputHandler();
        
        assertEquals(-1, inputHandler.readInt());
        inputHandler.close();
    }

    @Test
    void testReadDoubleValidInput() {
        // Test Case: Verify reading valid double input
        // Tests:
        // 1. Valid double string is provided
        // 2. Correct double value is returned
        
        String input = "42.5\n";
        provideInput(input);
        inputHandler = new InputHandler();
        
        assertEquals(42.5, inputHandler.readDouble());
        inputHandler.close();
    }

    @Test
    void testReadDoubleInvalidInput() {
        // Test Case: Verify handling of invalid double input
        // Tests:
        // 1. Non-double string is provided
        // 2. Error value (-1.0) is returned
        
        String input = "not a number\n";
        provideInput(input);
        inputHandler = new InputHandler();
        
        assertEquals(-1.0, inputHandler.readDouble());
        inputHandler.close();
    }

    @Test
    void testReadFloatValidInput() {
        // Test Case: Verify reading valid float input
        // Tests:
        // 1. Valid float string is provided
        // 2. Correct float value is returned
        
        String input = "42.5\n";
        provideInput(input);
        inputHandler = new InputHandler();
        
        assertEquals(42.5f, inputHandler.readFloat());
        inputHandler.close();
    }

    @Test
    void testReadFloatInvalidInput() {
        // Test Case: Verify handling of invalid float input
        // Tests:
        // 1. Non-float string is provided
        // 2. Error value (-1.0f) is returned
        
        String input = "not a number\n";
        provideInput(input);
        inputHandler = new InputHandler();
        
        assertEquals(-1.0f, inputHandler.readFloat());
        inputHandler.close();
    }

    @Test
    void testReadLine() {
        // Test Case: Verify reading string input
        // Tests:
        // 1. String is provided
        // 2. Exact string is returned without modifications
        
        String input = "test string\n";
        provideInput(input);
        inputHandler = new InputHandler();
        
        assertEquals("test string", inputHandler.readLine());
        inputHandler.close();
    }

    @Test
    void testAskYesNoPositive() {
        // Test Case: Verify handling of positive Y/N response
        // Tests:
        // 1. Question is displayed
        // 2. User inputs 'Y'
        // 3. Returns true
        
        String input = "Y\n";
        provideInput(input);
        inputHandler = new InputHandler();
        
        assertTrue(inputHandler.askYesNo("Test question"));
        assertTrue(outputStream.toString().contains("Test question (Y/N)"));
        inputHandler.close();
    }

    @Test
    void testAskYesNoNegative() {
        // Test Case: Verify handling of negative Y/N response
        // Tests:
        // 1. Question is displayed
        // 2. User inputs 'N'
        // 3. Returns false
        
        String input = "N\n";
        provideInput(input);
        inputHandler = new InputHandler();
        
        assertFalse(inputHandler.askYesNo("Test question"));
        assertTrue(outputStream.toString().contains("Test question (Y/N)"));
        inputHandler.close();
    }

    private void provideInput(String data) {
        ByteArrayInputStream testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
    }
}
