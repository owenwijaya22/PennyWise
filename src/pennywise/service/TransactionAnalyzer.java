package pennywise.service;

import pennywise.model.Transaction;
import pennywise.model.TransactionType;

import java.util.*;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
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
                Transaction::getDescription,
                Collectors.summingDouble(t -> Math.abs(t.getAmount()))
            ));
    }
}