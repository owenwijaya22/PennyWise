package pennywise.model;

public class Expense extends Transaction {
    public Expense(String userId, double amount, ExpenseCategory eCategory) {
        super(userId, amount, TransactionType.EXPENSE, eCategory);
    }
}