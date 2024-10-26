package pennywise.app;

import pennywise.model.*;
import pennywise.utils.DiscountManager;
import pennywise.interfaces.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Optional;

public class PennyWise {
    private List<User> users;
    private IDataStorage dataStorage;

    public PennyWise(IDataStorage dataStorage) {
        this.users = new ArrayList<>();
        this.dataStorage = dataStorage;
    }

    public String createUser(String name) {
        String userID = UUID.randomUUID().toString();
        User user = new User(userID, name);
        users.add(user);
        return userID;
    }

    public Optional<User> getUser(String userID) {
        return users.stream()
            .filter(u -> u.getUserID().equals(userID))
            .findFirst();
    }

    public void addTransaction(String userID, Transaction transaction) {
        getUser(userID).ifPresent(user -> user.addTransaction(transaction));
    }

    public void setUserBudget(String userID, float amount) {
        getUser(userID).ifPresent(user -> user.getBudgetManager().createBudget(userID, amount));
    }

    public void setUserGoal(String userID, Goal goal) {
        getUser(userID).ifPresent(user -> user.getGoalPlanner().setFinancialGoal(goal));
    }

    public List<Discount> getAvailableDiscounts() {
        return DiscountManager.getInstance().getAvailableDiscounts();
    }

    public void saveData() {
        dataStorage.saveData(users);
    }

    public void loadData() {
        Object data = dataStorage.loadData();
        if (data instanceof List<?>) {
            this.users = (List<User>) data;
        }
    }

    public float getUserTotalSpendings(String userID) {
        return getUser(userID)
            .map(User::calculateTotalSpendings)
            .orElse(0f);
    }

    public float getUserTotalIncome(String userID) {
        return getUser(userID)
            .map(User::calculateTotalIncome)
            .orElse(0f);
    }
}