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

    public Map<Object, Double> getMonthlyExpenses() {
        return transactions.stream()
            .filter(t -> t.getType() == TransactionType.EXPENSE)
            .collect(Collectors.groupingBy(
                t -> new SimpleDateFormat("MMMM yyyy").format(t.getDate()),
                Collectors.summingDouble(t -> Math.abs(t.getAmount()))
            ));
    }

    public Map<Object, Double> getExpensesByCategory() {
        return transactions.stream()
            .filter(t -> t.getType() == TransactionType.EXPENSE)
            .collect(Collectors.groupingBy(
                Transaction::getCategory,
                Collectors.summingDouble(t -> Math.abs(t.getAmount()))
            ));
    }

    // Additional helper methods for analysis
    public Map<Object, Double> getIncomeByCategory() {
        return transactions.stream()
            .filter(t -> t.getType() == TransactionType.INCOME)
            .collect(Collectors.groupingBy(
                Transaction::getCategory,
                Collectors.summingDouble(Transaction::getAmount)
            ));
    }

    public Map<Object, Double> getMonthlyIncome() {
        return transactions.stream()
            .filter(t -> t.getType() == TransactionType.INCOME)
            .collect(Collectors.groupingBy(
                t -> new SimpleDateFormat("MMMM yyyy").format(t.getDate()),
                Collectors.summingDouble(Transaction::getAmount)
            ));
    }

    public double getTotalExpenses() {
        return transactions.stream()
            .filter(t -> t.getType() == TransactionType.EXPENSE)
            .mapToDouble(t -> Math.abs(t.getAmount()))
            .sum();
    }

    public double getTotalIncome() {
        return transactions.stream()
            .filter(t -> t.getType() == TransactionType.INCOME)
            .mapToDouble(Transaction::getAmount)
            .sum();
    }

    public Map<Object, Double> getCategoryPercentages(TransactionType type) {
        double total = type == TransactionType.EXPENSE ? getTotalExpenses() : getTotalIncome();
        Map<Object, Double> categoryAmounts = type == TransactionType.EXPENSE ? 
            getExpensesByCategory() : getIncomeByCategory();

        return categoryAmounts.entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                e -> (e.getValue() / total) * 100
            ));
    }
}