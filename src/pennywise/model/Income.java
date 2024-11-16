package pennywise.model;

public class Income extends Transaction {
    public Income(String userId, double amount, IncomeCategory iCategory) {
        super(userId, amount, TransactionType.INCOME, iCategory);
    }
}