package pennywise.model;

import java.util.ArrayList;
import java.util.List;

import pennywise.service.BudgetManager;
import pennywise.service.FinancialGoalPlanner;

public class User {
    private String userID;
    private String name;
    private List<Transaction> transactions;
    private BudgetManager budgetManager;
    private FinancialGoalPlanner goalPlanner;

    public User(String userID, String name) {
        this.userID = userID;
        this.name = name;
        this.transactions = new ArrayList<>();
        this.budgetManager = new BudgetManager();
        this.goalPlanner = new FinancialGoalPlanner();
    }

    public String getUserID() {
        return userID;
    }

    public String getName() {
        return name;
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public List<Transaction> getTransactions() {
        return new ArrayList<>(transactions);
    }
    
    public BudgetManager getBudgetManager() {
        return budgetManager;
    }

    public FinancialGoalPlanner getGoalPlanner() {
        return goalPlanner;
    }

    public float calculateTotalSpendings() {
        return (float) transactions.stream()
            .filter(t -> t instanceof Expense)
            .mapToDouble(Transaction::getAmount)
            .sum();
    }

    public float calculateTotalIncome() {
        return (float) transactions.stream()
            .filter(t -> t instanceof Income)
            .mapToDouble(Transaction::getAmount)
            .sum();
    }
}