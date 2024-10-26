package pennywise.storage;

import java.io.*;
import java.util.List;
import java.util.ArrayList;
import pennywise.interfaces.IDataStorage;
import pennywise.model.User;
import pennywise.model.Transaction;

public class FileDataStorage implements IDataStorage {
    private final String DATA_DIR = "data/";
    private final String USERS_FILE = DATA_DIR + "users.dat";
    private final String TRANSACTIONS_DIR = DATA_DIR + "transactions/";

    public FileDataStorage() {
        initializeDataDirectories();
    }

    private void initializeDataDirectories() {
        new File(DATA_DIR).mkdirs();
        new File(TRANSACTIONS_DIR).mkdirs();
    }

    @Override
    public void saveData(List<User> users) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(USERS_FILE))) {
            oos.writeObject(users);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save users data", e);
        }
    }

    @Override
    public List<User> loadData() {
        File file = new File(USERS_FILE);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(USERS_FILE))) {
            return (List<User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to load users data", e);
        }
    }

    @Override
    public boolean saveUser(User user) {
        List<User> users = loadData();
        users.removeIf(u -> u.getUserID().equals(user.getUserID()));
        users.add(user);
        saveData(users);
        return true;
    }

    @Override
    public User loadUser(String userID) {
        return loadData().stream()
                .filter(u -> u.getUserID().equals(userID))
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean saveTransaction(String userID, Transaction transaction) {
        String filename = TRANSACTIONS_DIR + userID + ".dat";
        List<Transaction> transactions = loadTransactions(userID);
        transactions.add(transaction);
        
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(filename))) {
            oos.writeObject(transactions);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public List<Transaction> loadTransactions(String userID) {
        String filename = TRANSACTIONS_DIR + userID + ".dat";
        File file = new File(filename);
        
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(filename))) {
            return (List<Transaction>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public boolean deleteUser(String userID) {
        List<User> users = loadData();
        boolean removed = users.removeIf(u -> u.getUserID().equals(userID));
        if (removed) {
            saveData(users);
            new File(TRANSACTIONS_DIR + userID + ".dat").delete();
        }
        return removed;
    }

    @Override
    public boolean clearAllData() {
        try {
            File dataDir = new File(DATA_DIR);
            deleteDirectory(dataDir);
            initializeDataDirectories();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void deleteDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
        directory.delete();
    }
}