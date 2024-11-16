// TransactionAnalyzer.java
package pennywise.service;

import pennywise.model.*;
import pennywise.interfaces.TransactionCategory;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.stream.Collectors;

public class TransactionAnalyzer {
    private List<Transaction> transactions;

    public TransactionAnalyzer(List<Transaction> transactions) {
        this.transactions = new ArrayList<>(transactions);
    }

    public void updateTransactions(List<Transaction> newTransactions) {
        this.transactions = new ArrayList<>(newTransactions);
    }

    public Map<String, Double> getMonthlyExpenses() {
        return transactions.stream()
            .filter(Transaction::isExpense)
            .collect(Collectors.groupingBy(
                t -> new SimpleDateFormat("MMMM yyyy").format(t.getDate()),
                Collectors.summingDouble(t -> Math.abs(t.getAmount()))
            ));
    }

    public Map<TransactionCategory, Double> getExpensesByCategory() {
        return transactions.stream()
            .filter(Transaction::isExpense)
            .collect(Collectors.groupingBy(
                Transaction::getCategory,
                Collectors.summingDouble(t -> Math.abs(t.getAmount()))
            ));
    }

    public Map<TransactionCategory, Double> getIncomeByCategory() {
        return transactions.stream()
            .filter(t -> !t.isExpense())
            .collect(Collectors.groupingBy(
                Transaction::getCategory,
                Collectors.summingDouble(Transaction::getAmount)
            ));
    }

    public Map<String, Double> getMonthlyIncome() {
        return transactions.stream()
            .filter(t -> !t.isExpense())
            .collect(Collectors.groupingBy(
                t -> new SimpleDateFormat("MMMM yyyy").format(t.getDate()),
                Collectors.summingDouble(Transaction::getAmount)
            ));
    }

    public double getTotalExpenses() {
        return transactions.stream()
            .filter(Transaction::isExpense)
            .mapToDouble(t -> Math.abs(t.getAmount()))
            .sum();
    }

    public double getTotalIncome() {
        return transactions.stream()
            .filter(t -> !t.isExpense())
            .mapToDouble(Transaction::getAmount)
            .sum();
    }

    public double getNetAmount() {
        return transactions.stream()
            .mapToDouble(Transaction::getAmount)
            .sum();
    }

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