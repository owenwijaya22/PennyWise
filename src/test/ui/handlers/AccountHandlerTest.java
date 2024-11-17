package test.ui.handlers;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import pennywise.ui.UIConstants;
import pennywise.ui.handlers.AccountHandler;
import test.stubs.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class AccountHandlerTest {
    private AccountHandler accountHandler;
    private MockPennyWise mockPennywise;
    private MockInputHandler mockInputHandler;
    private MockMenuHandler mockMenuHandler;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        mockPennywise = new MockPennyWise();
        mockInputHandler = new MockInputHandler();
        mockMenuHandler = new MockMenuHandler();
        accountHandler = new AccountHandler(mockPennywise, mockInputHandler);
        accountHandler.setMenuHandler(mockMenuHandler);
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void testLoginSuccess() {
        // Test Case: Verify successful login
        // Tests:
        // 1. Valid user ID is provided
        // 2. Login is successful
        // 3. Success message is displayed
        
        mockInputHandler.queueString("validUser");
        accountHandler.handleLogin();
        
        assertTrue(outputStream.toString().contains(UIConstants.LOGIN_SUCCESS_MESSAGE));
        assertTrue(mockPennywise.isLoggedIn());
        assertEquals("validUser", mockPennywise.getCurrentUser().getUserId());
    }

    @Test
    void testLoginFailure() {
        // Test Case: Verify failed login
        // Tests:
        // 1. Invalid/empty user ID is provided
        // 2. Login fails
        // 3. Failure message is displayed
        
        mockInputHandler.queueString("");
        accountHandler.handleLogin();
        
        assertTrue(outputStream.toString().contains(UIConstants.LOGIN_FAILED_MESSAGE));
        assertFalse(mockPennywise.isLoggedIn());
    }

    @Test
    void testRegistrationSuccess() {
        // Test Case: Verify successful registration
        // Tests:
        // 1. Valid user ID is provided
        // 2. Registration is successful
        // 3. Success message is displayed
        
        mockInputHandler.queueString("newUser");
        accountHandler.handleRegistration();
        
        assertTrue(outputStream.toString().contains(UIConstants.REGISTRATION_SUCCESS_MESSAGE));
    }

    @Test
    void testRegistrationFailure() {
        // Test Case: Verify failed registration
        // Tests:
        // 1. Invalid/empty user ID is provided
        // 2. Registration fails
        // 3. Failure message is displayed
        
        mockInputHandler.queueString("");
        accountHandler.handleRegistration();
        
        assertTrue(outputStream.toString().contains(UIConstants.REGISTRATION_FAILED_MESSAGE));
        assertFalse(mockPennywise.isRegistered());
    }

    @Test
    void testAccountManagementClearDataCancel() {
        // Test Case: Verify cancelled data clearing
        // Tests:
        // 1. User is logged in
        // 2. Option 3 (Clear Data) is selected
        // 3. User cancels clearing
        // 4. Data remains intact
        
        mockPennywise.login("testUser");
        mockInputHandler.queueInt(3);
        mockInputHandler.queueBoolean(false); // Cancel clearing
        
        accountHandler.handleAccountManagement();
        
        assertFalse(outputStream.toString().contains(UIConstants.CLEAR_DATA_SUCCESS_MESSAGE));
        assertFalse(mockMenuHandler.wasLoginMenuCalled());
    }

    @Test
    void testAccountManagementClearDataSuccess() {
        // Test Case: Verify successful data clearing
        // Tests:
        // 1. User is logged in
        // 2. Option 3 (Clear Data) is selected
        // 3. User confirms clearing
        // 4. Clearing operation succeeds
        // 5. Success message is displayed and returns to login menu
        
        mockPennywise.login("testUser");
        mockInputHandler.queueInt(3);
        mockInputHandler.queueBoolean(true); // Confirm clearing
        // MockPennyWise.clearAllUserData() will return true when user is logged in
        
        accountHandler.handleAccountManagement();
        
        assertTrue(outputStream.toString().contains(UIConstants.CLEAR_DATA_SUCCESS_MESSAGE));
        assertTrue(mockMenuHandler.wasLoginMenuCalled());
    }

    @Test
    void testAccountManagementClearDataFailure() {
        // Test Case: Verify failed data clearing
        // Tests:
        // 1. User is not logged in
        // 2. Option 3 (Clear Data) is selected
        // 3. User confirms clearing
        // 4. Clearing operation fails
        // 5. Failure message is displayed
        
        mockPennywise.setLoggedIn(false); // This will cause clearAllUserData to fail
        mockInputHandler.queueInt(3);
        mockInputHandler.queueBoolean(true); // Confirm clearing
        
        accountHandler.handleAccountManagement();
        
        assertTrue(outputStream.toString().contains(UIConstants.CLEAR_DATA_FAILED_MESSAGE));
        assertFalse(mockMenuHandler.wasLoginMenuCalled());
    }

    @Test
    void testAccountManagementDeleteAccountSuccess() {
        // Test Case: Verify successful account deletion
        // Tests:
        // 1. User is logged in
        // 2. Option 2 (Delete Account) is selected
        // 3. User confirms deletion
        // 4. Deletion operation succeeds
        // 5. Success message is displayed and returns to login menu
        
        mockPennywise.login("testUser");
        mockInputHandler.queueInt(2);
        mockInputHandler.queueBoolean(true); // Confirm deletion
        // MockPennyWise.deleteAccount() will return true when user is logged in
        
        accountHandler.handleAccountManagement();
        
        assertTrue(outputStream.toString().contains(UIConstants.DELETE_ACC_SUCCESS_MESSAGE));
        assertTrue(mockMenuHandler.wasLoginMenuCalled());
    }

    @Test
    void testAccountManagementDeleteAccountFailure() {
        // Test Case: Verify failed account deletion
        // Tests:
        // 1. User is not logged in
        // 2. Option 2 (Delete Account) is selected
        // 3. User confirms deletion
        // 4. Deletion operation fails
        // 5. Failure message is displayed
        
        mockPennywise.setLoggedIn(false); // This will cause deleteAccount to fail
        mockInputHandler.queueInt(2);
        mockInputHandler.queueBoolean(true); // Confirm deletion
        
        accountHandler.handleAccountManagement();
        
        assertTrue(outputStream.toString().contains(UIConstants.DELETE_ACC_FAILED_MESSAGE));
        assertFalse(mockMenuHandler.wasLoginMenuCalled());
    }

    @Test
    void testAccountManagementReturnOption() {
        // Test Case: Verify return option
        // Tests:
        // 1. User is logged in
        // 2. Option 4 (Return) is selected
        // 3. Method returns without any action
        
        mockPennywise.login("testUser");
        mockInputHandler.queueInt(4);
        
        accountHandler.handleAccountManagement();
        
        // Verify no messages were displayed and no actions were taken
        assertFalse(mockMenuHandler.wasLoginMenuCalled());
        assertTrue(mockPennywise.isLoggedIn());
    }

    @Test
    void testAccountManagementViewUserIdSuccess() {
        // Test Case: Verify viewing user ID
        // Tests:
        // 1. User is logged in
        // 2. Option 1 (View User ID) is selected
        // 3. User ID is displayed correctly
        
        String testUserId = "testUser123";
        mockPennywise.login(testUserId); // This creates a new User with testUserId
        mockInputHandler.queueInt(1);
        
        accountHandler.handleAccountManagement();
        
        assertTrue(outputStream.toString().contains(testUserId));
        assertNotNull(mockPennywise.getCurrentUser());
        assertEquals(testUserId, mockPennywise.getCurrentUser().getUserId());
    }

    @Test
    void testAccountManagementInvalidOption() {
        // Test Case: Verify invalid option handling
        // Tests:
        // 1. User is logged in
        // 2. Invalid option (not 1-4) is selected
        // 3. Invalid option message is displayed
        
        mockPennywise.login("testUser"); // Ensure user is logged in
        mockInputHandler.queueInt(99); // Queue an invalid option number
        
        accountHandler.handleAccountManagement();
        
        assertTrue(outputStream.toString().contains(UIConstants.INVALID_OPTION_MESSAGE));
    }

    @Test
    void testAccountManagementNegativeOption() {
        // Test Case: Verify negative option handling
        // Tests:
        // 1. User is logged in
        // 2. Negative option number is selected
        // 3. Invalid option message is displayed
        
        mockPennywise.login("testUser");
        mockInputHandler.queueInt(-1); // Queue a negative option number
        
        accountHandler.handleAccountManagement();
        
        assertTrue(outputStream.toString().contains(UIConstants.INVALID_OPTION_MESSAGE));
    }
}
