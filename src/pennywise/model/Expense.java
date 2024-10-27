package pennywise.model;

public class Expense extends Transaction {
    public Expense(String userId, double amount, IncomeCategory category) {
        super(userId, amount, TransactionType.INCOME, category);
    }
}