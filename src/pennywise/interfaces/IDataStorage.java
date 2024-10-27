package pennywise.interfaces;

import java.util.List;
import pennywise.model.User;
import pennywise.model.Budget;
import pennywise.model.Transaction;

public interface IDataStorage {
    void saveData(List<User> users);
    List<User> loadData();
    
    // methods buat nanti
    boolean saveUser(User user);
    User loadUser(String userID);
    boolean saveTransaction(String userID, Transaction transaction);
    List<Transaction> loadTransactions(String userID);
    boolean deleteUser(String userID);
    boolean clearAllData();
    boolean saveBudget(String userId, Budget budget);
    List<Budget> loadBudgets(String userId);
}