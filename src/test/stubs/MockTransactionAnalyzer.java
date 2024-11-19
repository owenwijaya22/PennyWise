/*
 * 
 */
package test.stubs;

import pennywise.model.*;
import pennywise.service.*;
import pennywise.interfaces.TransactionCategory;
import java.util.*;


/**
 * The Class MockTransactionAnalyzer.
 */
public class MockTransactionAnalyzer extends TransactionAnalyzer {
    
    /** The transactions. */
    private List<Transaction> transactions = new ArrayList<>();
    
    /** The mock incomes by category. */
    private Map<TransactionCategory, Double> mockIncomesByCategory = new HashMap<>();
    
    /** The mock expenses by category. */
    private Map<TransactionCategory, Double> mockExpensesByCategory = new HashMap<>();
    
    /** The mock monthly incomes. */
    private Map<String, Double> mockMonthlyIncomes = new HashMap<>();
    
    /** The mock monthly expenses. */
    private Map<String, Double> mockMonthlyExpenses = new HashMap<>();

    /**
     * Instantiates a new mock transaction analyzer.
     */
    public MockTransactionAnalyzer() {
        super(new ArrayList<>());
        this.transactions = new ArrayList<>();
    }

    /**
     * Instantiates a new mock transaction analyzer.
     *
     * @param transactions the transactions
     */
    public MockTransactionAnalyzer(List<Transaction> transactions) {
        super(transactions);
        this.transactions = new ArrayList<>(transactions);
    }

    /**
     * Update transactions.
     *
     * @param newTransactions the new transactions
     */
    public void updateTransactions(List<Transaction> newTransactions) {
        this.transactions = new ArrayList<>(newTransactions);
    }

    /**
     * Gets the income by category.
     *
     * @return the income by category
     */
    @Override
    public Map<TransactionCategory, Double> getIncomeByCategory() {
        return mockIncomesByCategory;
    }

    /**
     * Sets the incomes by category.
     *
     * @param incomesByCategory the incomes by category
     */
    public void setIncomesByCategory(Map<TransactionCategory, Double> incomesByCategory) {
        this.mockIncomesByCategory = new HashMap<>(incomesByCategory);
    }

    /**
     * Gets the expenses by category.
     *
     * @return the expenses by category
     */
    @Override
    public Map<TransactionCategory, Double> getExpensesByCategory() {
        return mockExpensesByCategory;
    }

    /**
     * Sets the expenses by category.
     *
     * @param expensesByCategory the expenses by category
     */
    public void setExpensesByCategory(Map<TransactionCategory, Double> expensesByCategory) {
        this.mockExpensesByCategory = new HashMap<>(expensesByCategory);
    }

    /**
     * Gets the monthly income.
     *
     * @return the monthly income
     */
    @Override
    public Map<String, Double> getMonthlyIncome() {
        return mockMonthlyIncomes;
    }

    /**
     * Sets the monthly incomes.
     *
     * @param monthlyIncomes the monthly incomes
     */
    public void setMonthlyIncomes(Map<String, Double> monthlyIncomes) {
        this.mockMonthlyIncomes = new HashMap<>(monthlyIncomes);
    }

    /**
     * Gets the monthly expenses.
     *
     * @return the monthly expenses
     */
    @Override
    public Map<String, Double> getMonthlyExpenses() {
        return mockMonthlyExpenses;
    }

    /**
     * Sets the monthly expenses.
     *
     * @param monthlyExpenses the monthly expenses
     */
    public void setMonthlyExpenses(Map<String, Double> monthlyExpenses) {
        this.mockMonthlyExpenses = new HashMap<>(monthlyExpenses);
    }

    /**
     * Gets the total expenses.
     *
     * @return the total expenses
     */
    @Override
    public double getTotalExpenses() {
        return transactions.stream()
            .filter(Transaction::isExpense)
            .mapToDouble(t -> Math.abs(t.getAmount()))
            .sum();
    }

    /**
     * Gets the total income.
     *
     * @return the total income
     */
    @Override
    public double getTotalIncome() {
        return transactions.stream()
            .filter(t -> !t.isExpense())
            .mapToDouble(Transaction::getAmount)
            .sum();
    }
}