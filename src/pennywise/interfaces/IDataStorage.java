/*
 * 
 */
package pennywise.interfaces;

import java.util.List;
import pennywise.model.User;
import pennywise.model.Budget;
import pennywise.model.Discount;
import pennywise.model.Transaction;


/**
 * The Interface IDataStorage.
 */
public interface IDataStorage {
    
    /**
     * Save data.
     *
     * @param users the users
     */
    void saveData(List<User> users);
    
    /**
     * Load data.
     *
     * @return the list
     */
    List<User> loadData();
    
    /**
     * Save user.
     *
     * @param user the user
     * @return true, if successful
     */
    boolean saveUser(User user);
    
    /**
     * Load user.
     *
     * @param userID the user ID
     * @return the user
     */
    User loadUser(String userID);
    
    /**
     * Save transaction.
     *
     * @param userID the user ID
     * @param transaction the transaction
     * @return true, if successful
     */
    boolean saveTransaction(String userID, Transaction transaction);
    
    /**
     * Load transactions.
     *
     * @param userID the user ID
     * @return the list
     */
    List<Transaction> loadTransactions(String userID);
    
    /**
     * Delete user.
     *
     * @param userID the user ID
     * @return true, if successful
     */
    boolean deleteUser(String userID);
    
    /**
     * Clear all data.
     *
     * @return true, if successful
     */
    boolean clearAllData();
    
    /**
     * Save budget.
     *
     * @param userId the user id
     * @param budget the budget
     * @return true, if successful
     */
    boolean saveBudget(String userId, Budget budget);
    
    /**
     * Load budgets.
     *
     * @param userId the user id
     * @return the list
     */
    List<Budget> loadBudgets(String userId);
	
}