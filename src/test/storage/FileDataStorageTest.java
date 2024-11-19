/*
 * 
 */
package test.storage;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import pennywise.storage.FileDataStorage;

import java.io.*;
import java.nio.file.*;


/**
 * The Class FileDataStorageTest.
 */
public class FileDataStorageTest {
    
    /** The Constant TEST_DATA_DIR. */
    private static final String TEST_DATA_DIR = "./test_data";
    
    /** The storage. */
    private FileDataStorage storage;
    
    /** The Constant TEST_USER_ID. */
    private static final String TEST_USER_ID = "testUser";

    /**
     * Sets the up.
     */
    @BeforeEach
    void setUp() {
        cleanTestDirectory();
        storage = new FileDataStorage(TEST_DATA_DIR);
    }

    /**
     * Tear down.
     */
    @AfterEach
    void tearDown() {
        // Make directory writable again before cleanup
        File directory = new File(TEST_DATA_DIR);
        if (directory.exists()) {
            makeWritable(directory);
        }
        cleanTestDirectory();
    }

    /**
     * Clean test directory.
     */
    private void cleanTestDirectory() {
        try {
            Path directory = Paths.get(TEST_DATA_DIR);
            if (Files.exists(directory)) {
                Files.walk(directory)
                     .sorted((a, b) -> b.compareTo(a))
                     .forEach(path -> {
                         try {
                             Files.delete(path);
                         } catch (IOException e) {
                             // Ignore deletion errors during cleanup
                         }
                     });
            }
        } catch (IOException e) {
            // Ignore cleanup errors
        }
    }

    /**
     * Make writable.
     *
     * @param file the file
     */
    private void makeWritable(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File child : files) {
                    makeWritable(child);
                }
            }
        }
        file.setWritable(true);
    }

    /**
     * Test corrupted user file.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    void testCorruptedUserFile() throws IOException {
        // Create a corrupted users file
        File usersFile = new File(TEST_DATA_DIR, "users.dat");
        usersFile.getParentFile().mkdirs();
        try (FileOutputStream fos = new FileOutputStream(usersFile)) {
            fos.write("corrupted data".getBytes());
        }

        // Verify the storage handles corrupted file gracefully
        assertTrue(storage.loadData().isEmpty());
    }

    /**
     * Test corrupted transaction file.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    void testCorruptedTransactionFile() throws IOException {
        // Create a corrupted transactions file
        File transactionsFile = new File(TEST_DATA_DIR, TEST_USER_ID + "_transactions.dat");
        transactionsFile.getParentFile().mkdirs();
        try (FileOutputStream fos = new FileOutputStream(transactionsFile)) {
            fos.write("corrupted data".getBytes());
        }

        // Verify the storage handles corrupted file gracefully
        assertTrue(storage.loadTransactions(TEST_USER_ID).isEmpty());
    }

    /**
     * Test delete non existent user.
     */
    @Test
    void testDeleteNonExistentUser() {
        // Attempt to delete a user that doesn't exist
        assertFalse(storage.deleteUser("nonexistentUser"));
    }
}