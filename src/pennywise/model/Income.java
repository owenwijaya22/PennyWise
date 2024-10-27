package pennywise.model;

public class Income extends Transaction {
    public Income(String userId, double amount, IncomeCategory category) {
        super(userId, amount, TransactionType.INCOME, category);
    }
}