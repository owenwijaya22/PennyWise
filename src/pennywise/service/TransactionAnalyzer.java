// TransactionAnalyzer.java
package pennywise.service;

import pennywise.model.*;
import pennywise.interfaces.TransactionCategory;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.stream.Collectors;


/**
 * The Class TransactionAnalyzer.
 */
public class TransactionAnalyzer {
    
    /** The transactions. */
    private List<Transaction> transactions;

    /**
     * Instantiates a new transaction analyzer.
     *
     * @param transactions the transactions
     */
    public TransactionAnalyzer(List<Transaction> transactions) {
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
     * Gets the monthly expenses.
     *
     * @return the monthly expenses
     */
    public Map<String, Double> getMonthlyExpenses() {
        return transactions.stream()
            .filter(Transaction::isExpense)
            .collect(Collectors.groupingBy(
                t -> new SimpleDateFormat("MMMM yyyy").format(t.getDate()),
                Collectors.summingDouble(t -> Math.abs(t.getAmount()))
            ));
    }

    /**
     * Gets the expenses by category.
     *
     * @return the expenses by category
     */
    public Map<TransactionCategory, Double> getExpensesByCategory() {
        return transactions.stream()
            .filter(Transaction::isExpense)
            .collect(Collectors.groupingBy(
                Transaction::getCategory,
                Collectors.summingDouble(t -> Math.abs(t.getAmount()))
            ));
    }

    /**
     * Gets the income by category.
     *
     * @return the income by category
     */
    public Map<TransactionCategory, Double> getIncomeByCategory() {
        return transactions.stream()
            .filter(t -> !t.isExpense())
            .collect(Collectors.groupingBy(
                Transaction::getCategory,
                Collectors.summingDouble(Transaction::getAmount)
            ));
    }

    /**
     * Gets the monthly income.
     *
     * @return the monthly income
     */
    public Map<String, Double> getMonthlyIncome() {
        return transactions.stream()
            .filter(t -> !t.isExpense())
            .collect(Collectors.groupingBy(
                t -> new SimpleDateFormat("MMMM yyyy").format(t.getDate()),
                Collectors.summingDouble(Transaction::getAmount)
            ));
    }

    /**
     * Gets the total expenses.
     *
     * @return the total expenses
     */
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
    public double getTotalIncome() {
        return transactions.stream()
            .filter(t -> !t.isExpense())
            .mapToDouble(Transaction::getAmount)
            .sum();
    }

    /**
     * Gets the net amount.
     *
     * @return the net amount
     */
    public double getNetAmount() {
        return transactions.stream()
            .mapToDouble(Transaction::getAmount)
            .sum();
    }

    /**
     * Gets the category percentages.
     *
     * @param isExpense the is expense
     * @return the category percentages
     */
    public Map<TransactionCategory, Double> getCategoryPercentages(boolean isExpense) {
        double total = isExpense ? getTotalExpenses() : getTotalIncome();
        if (total == 0) return new HashMap<>();
        
        Map<TransactionCategory, Double> categoryAmounts = isExpense ? 
            getExpensesByCategory() : getIncomeByCategory();

        return categoryAmounts.entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                e -> (e.getValue() / total) * 100
            ));
    }
}