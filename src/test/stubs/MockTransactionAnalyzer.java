package test.stubs;

import pennywise.model.*;
import pennywise.service.*;
import java.util.*;

public class MockTransactionAnalyzer extends TransactionAnalyzer {
    public MockTransactionAnalyzer(List<Transaction> transactions) {
        super(transactions);
    }

    private List<Transaction> transactions = new ArrayList<>();

    public void updateTransactions(List<Transaction> newTransactions) {
        this.transactions = new ArrayList<>(newTransactions);
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
}