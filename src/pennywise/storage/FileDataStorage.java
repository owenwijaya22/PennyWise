package pennywise.storage;

import pennywise.interfaces.IDataStorage;
import pennywise.model.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileDataStorage implements IDataStorage {
    private final String dataDirectory;
    private static final String USERS_FILE = "users.dat";
    private static final String TRANSACTIONS_FILE = "transactions.dat";
    private static final String BUDGETS_FILE = "budgets.dat";

    public FileDataStorage(String dataDirectory) {
        this.dataDirectory = dataDirectory;
        initializeDirectory();
    }

    private void initializeDirectory() {
        File directory = new File(dataDirectory);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    @Override
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

    @Override
    public void saveData(List<User> users) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(new File(dataDirectory, USERS_FILE)))) {
            oos.writeObject(users);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public User loadUser(String userID) {
        List<User> users = loadData();
        return users.stream()
                   .filter(u -> u.getUserId().equals(userID))
                   .findFirst()
                   .orElse(null);
    }

    @Override
    public boolean saveUser(User user) {
        List<User> users = loadData();
        users.removeIf(u -> u.getUserId().equals(user.getUserId()));
        users.add(user);
        try {
            saveData(users);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

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

    @Override
    public boolean saveTransaction(String userId, Transaction transaction) {
        List<Transaction> transactions = loadTransactions(userId);
        transactions.add(transaction);
        return saveAllTransactions(userId, transactions);
    }

    @Override
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

    private boolean saveAllTransactions(String userId, List<Transaction> transactions) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(new File(dataDirectory, userId + "_" + TRANSACTIONS_FILE)))) {
            oos.writeObject(transactions);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

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

    @Override
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