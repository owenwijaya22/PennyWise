/*
 * 
 */
package pennywise.storage;

import pennywise.interfaces.IDataStorage;
import pennywise.model.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


/**
 * The Class FileDataStorage.
 */
public class FileDataStorage implements IDataStorage {
    
    /** The data directory. */
    private final String dataDirectory;
    
    /** The Constant USERS_FILE. */
    private static final String USERS_FILE = "users.dat";
    
    /** The Constant TRANSACTIONS_FILE. */
    private static final String TRANSACTIONS_FILE = "transactions.dat";
    
    /** The Constant BUDGETS_FILE. */
    private static final String BUDGETS_FILE = "budgets.dat";

    /**
     * Instantiates a new file data storage.
     *
     * @param dataDirectory the data directory
     */
    public FileDataStorage(String dataDirectory) {
        this.dataDirectory = dataDirectory;
        initializeDirectory();
    }
    
    // OLD CODE
//    private void initializeDirectory() {
//        File directory = new File(dataDirectory);
//        if (!directory.exists()) {
//            directory.mkdirs();
//        }
//    }
    
    /**
     * Initialize directory.
     */
    // NEW CODE
    private void initializeDirectory() {
        File directory = new File(dataDirectory);
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (!created) {
                throw new RuntimeException("Failed to create directory: " + dataDirectory);
            }
        }
    }

    /**
     * Load data.
     *
     * @return the list
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<User> loadData() {
        File file = new File(dataDirectory, USERS_FILE);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<User>) ois.readObject();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
    
    // OLD CODE
//    @Override
//    public void saveData(List<User> users) {
//        try (ObjectOutputStream oos = new ObjectOutputStream(
//                new FileOutputStream(new File(dataDirectory, USERS_FILE)))) {
//            oos.writeObject(users);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    
    /**
     * Save data.
     *
     * @param users the users
     */
    // NEW CODE
    @Override
    public void saveData(List<User> users) {
        File file = new File(dataDirectory, USERS_FILE);
        File parent = file.getParentFile();
        if (!parent.exists()) {
            boolean created = parent.mkdirs();
            if (!created) {
                throw new RuntimeException("Failed to create directory: " + parent.getPath());
            }
        }
        
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(file))) {
            oos.writeObject(users);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save data: " + e.getMessage(), e);
        }
    }

    /**
     * Load user.
     *
     * @param userID the user ID
     * @return the user
     */
    @Override
    public User loadUser(String userID) {
        List<User> users = loadData();
        return users.stream()
                   .filter(u -> u.getUserId().equals(userID))
                   .findFirst()
                   .orElse(null);
    }

    /**
     * Save user.
     *
     * @param user the user
     * @return true, if successful
     */
    @Override
    public boolean saveUser(User user) {
        List<User> users = loadData();
        users.removeIf(u -> u.getUserId().equals(user.getUserId()));
        users.add(user);
        saveData(users);
        return true;
    }

    /**
     * Delete user.
     *
     * @param userID the user ID
     * @return true, if successful
     */
    @Override
    public boolean deleteUser(String userID) {
        List<User> users = loadData();
        boolean removed = users.removeIf(u -> u.getUserId().equals(userID));
        if (removed) {
            try {
                saveData(users);
                // Delete user's files
                File transactionFile = new File(dataDirectory, userID + "_" + TRANSACTIONS_FILE);
                File budgetFile = new File(dataDirectory, userID + "_" + BUDGETS_FILE);
                
                if (transactionFile.exists()) {
                    transactionFile.delete();
                }
                if (budgetFile.exists()) {
                    budgetFile.delete();
                }
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    /**
     * Clear all data.
     *
     * @return true, if successful
     */
    @Override
    public boolean clearAllData() {
        File directory = new File(dataDirectory);
        if (!directory.exists()) {
            return true;
        }

        boolean success = true;
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (!file.delete()) {
                    success = false;
                }
            }
        }
        return success;
    }

    /**
     * Save transaction.
     *
     * @param userId the user id
     * @param transaction the transaction
     * @return true, if successful
     */
    @Override
    public boolean saveTransaction(String userId, Transaction transaction) {
        List<Transaction> transactions = loadTransactions(userId);
        transactions.add(transaction);
        return saveAllTransactions(userId, transactions);
    }

    /**
     * Load transactions.
     *
     * @param userId the user id
     * @return the list
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Transaction> loadTransactions(String userId) {
        File file = new File(dataDirectory, userId + "_" + TRANSACTIONS_FILE);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Transaction>) ois.readObject();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    /**
     * Save all transactions.
     *
     * @param userId the user id
     * @param transactions the transactions
     * @return true, if successful
     */
    private boolean saveAllTransactions(String userId, List<Transaction> transactions) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(new File(dataDirectory, userId + "_" + TRANSACTIONS_FILE)))) {
            oos.writeObject(transactions);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Save budget.
     *
     * @param userId the user id
     * @param budget the budget
     * @return true, if successful
     */
    @Override
    public boolean saveBudget(String userId, Budget budget) {
        List<Budget> budgets = loadBudgets(userId);
        // Remove existing budget for the current month if exists
        budgets.removeIf(b -> b.getMonth().equals(budget.getMonth()));
        budgets.add(budget);

        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(new File(dataDirectory, userId + "_" + BUDGETS_FILE)))) {
            oos.writeObject(budgets);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Load budgets.
     *
     * @param userId the user id
     * @return the list
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Budget> loadBudgets(String userId) {
        File file = new File(dataDirectory, userId + "_" + BUDGETS_FILE);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Budget>) ois.readObject();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}