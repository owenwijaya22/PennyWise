package test.stubs;

import pennywise.model.*;
import pennywise.service.*;
import pennywise.interfaces.TransactionCategory;
import java.util.*;

public class MockTransactionAnalyzer extends TransactionAnalyzer {
    private List<Transaction> transactions = new ArrayList<>();
    private Map<TransactionCategory, Double> mockIncomesByCategory = new HashMap<>();
    private Map<TransactionCategory, Double> mockExpensesByCategory = new HashMap<>();
    private Map<String, Double> mockMonthlyIncomes = new HashMap<>();
    private Map<String, Double> mockMonthlyExpenses = new HashMap<>();

    public MockTransactionAnalyzer() {
        super(new ArrayList<>());
        this.transactions = new ArrayList<>();
    }

    public MockTransactionAnalyzer(List<Transaction> transactions) {
        super(transactions);
        this.transactions = new ArrayList<>(transactions);
    }

    public void updateTransactions(List<Transaction> newTransactions) {
        this.transactions = new ArrayList<>(newTransactions);
    }

    @Override
    public Map<TransactionCategory, Double> getIncomeByCategory() {
        return mockIncomesByCategory;
    }

    public void setIncomesByCategory(Map<TransactionCategory, Double> incomesByCategory) {
        this.mockIncomesByCategory = new HashMap<>(incomesByCategory);
    }

    @Override
    public Map<TransactionCategory, Double> getExpensesByCategory() {
        return mockExpensesByCategory;
    }

    public void setExpensesByCategory(Map<TransactionCategory, Double> expensesByCategory) {
        this.mockExpensesByCategory = new HashMap<>(expensesByCategory);
    }

    @Override
    public Map<String, Double> getMonthlyIncome() {
        return mockMonthlyIncomes;
    }

    public void setMonthlyIncomes(Map<String, Double> monthlyIncomes) {
        this.mockMonthlyIncomes = new HashMap<>(monthlyIncomes);
    }

    @Override
    public Map<String, Double> getMonthlyExpenses() {
        return mockMonthlyExpenses;
    }

    public void setMonthlyExpenses(Map<String, Double> monthlyExpenses) {
        this.mockMonthlyExpenses = new HashMap<>(monthlyExpenses);
    }

    @Override
    public double getTotalExpenses() {
        return transactions.stream()
            .filter(Transaction::isExpense)
            .mapToDouble(t -> Math.abs(t.getAmount()))
            .sum();
    }

    @Override
    public double getTotalIncome() {
        return transactions.stream()
            .filter(t -> !t.isExpense())
            .mapToDouble(Transaction::getAmount)
            .sum();
    }
}