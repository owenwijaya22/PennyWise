package test.service;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import pennywise.model.Transaction;
import pennywise.interfaces.TransactionCategory;
import pennywise.model.ExpenseCategory;
import pennywise.model.IncomeCategory;
import java.util.*;
import pennywise.service.TransactionAnalyzer;

public class TransactionAnalyzerTest {
    private TransactionAnalyzer analyzer;
    private static final String TEST_USER_ID = "testUser";
    private List<Transaction> testTransactions;

    @BeforeEach
    void setUp() {
        testTransactions = new ArrayList<>();
        analyzer = new TransactionAnalyzer(testTransactions);
    }

    @Test
    void testExpenseAnalysis() {
        // Test Case: Verify expense analysis functionality
        // Tests the analyzer's ability to:
        // 1. Track expenses by category
        // 2. Calculate total expenses
        // 3. Handle multiple transactions in the same category
        
        // Create test transactions with different expense categories
        // Note: Expenses are stored as negative values
        testTransactions.add(new Transaction(TEST_USER_ID, -100.0, ExpenseCategory.FOOD));
        testTransactions.add(new Transaction(TEST_USER_ID, -200.0, ExpenseCategory.FOOD));
        testTransactions.add(new Transaction(TEST_USER_ID, -150.0, ExpenseCategory.TRANSPORTATION));
        
        analyzer.updateTransactions(testTransactions);
        
        Map<TransactionCategory, Double> expensesByCategory = analyzer.getExpensesByCategory();
        assertEquals(300.0, expensesByCategory.get(ExpenseCategory.FOOD));          // 100 + 200
        assertEquals(150.0, expensesByCategory.get(ExpenseCategory.TRANSPORTATION)); // Single transaction
        assertEquals(450.0, analyzer.getTotalExpenses());                           // 300 + 150
    }

    @Test
    void testIncomeAnalysis() {
        // Test Case: Verify income analysis functionality
        // Tests the analyzer's ability to:
        // 1. Track income by category
        // 2. Calculate total income
        // 3. Handle different income categories
        
        // Create test transactions for different income sources
        testTransactions.add(new Transaction(TEST_USER_ID, 1000.0, IncomeCategory.SALARY));
        testTransactions.add(new Transaction(TEST_USER_ID, 500.0, IncomeCategory.BUSINESS));
        
        analyzer.updateTransactions(testTransactions);
        
        Map<TransactionCategory, Double> incomeByCategory = analyzer.getIncomeByCategory();
        assertEquals(1000.0, incomeByCategory.get(IncomeCategory.SALARY));
        assertEquals(500.0, incomeByCategory.get(IncomeCategory.BUSINESS));
        assertEquals(1500.0, analyzer.getTotalIncome());                            // 1000 + 500
    }

    @Test
    void testCategoryPercentages() {
        // Test Case: Verify percentage calculations for transaction categories
        // Tests the analyzer's ability to:
        // 1. Calculate relative percentages of expenses by category
        // 2. Handle percentage calculations with multiple categories
        
        // Create test transactions with known proportions
        testTransactions.add(new Transaction(TEST_USER_ID, -100.0, ExpenseCategory.FOOD));           // 25%
        testTransactions.add(new Transaction(TEST_USER_ID, -300.0, ExpenseCategory.TRANSPORTATION)); // 75%
        
        analyzer.updateTransactions(testTransactions);
        
        Map<TransactionCategory, Double> percentages = analyzer.getCategoryPercentages(true); // true for expenses
        assertEquals(25.0, percentages.get(ExpenseCategory.FOOD));           // 100 / 400 * 100
        assertEquals(75.0, percentages.get(ExpenseCategory.TRANSPORTATION)); // 300 / 400 * 100
    }

    @Test
    void testMixedTransactions() {
        // Test Case: Verify analyzer handles both income and expenses correctly
        testTransactions.add(new Transaction(TEST_USER_ID, 1000.0, IncomeCategory.SALARY));
        testTransactions.add(new Transaction(TEST_USER_ID, -200.0, ExpenseCategory.FOOD));
        testTransactions.add(new Transaction(TEST_USER_ID, -300.0, ExpenseCategory.TRANSPORTATION));
        
        analyzer.updateTransactions(testTransactions);
        
        assertEquals(1000.0, analyzer.getTotalIncome());
        assertEquals(500.0, analyzer.getTotalExpenses());
        assertEquals(500.0, analyzer.getNetAmount()); // 1000 - 500
    }

    @Test
    void testEmptyTransactionList() {
        // Test Case: Verify analyzer behavior with empty transaction list
        // Tests:
        // 1. Getting expenses/income with no transactions
        // 2. Verifying empty maps are returned
        // 3. Checking total calculations return zero
        
        assertTrue(analyzer.getExpensesByCategory().isEmpty());
        assertTrue(analyzer.getIncomeByCategory().isEmpty());
        assertEquals(0.0, analyzer.getTotalExpenses());
        assertEquals(0.0, analyzer.getTotalIncome());
        assertEquals(0.0, analyzer.getNetAmount());
    }

    @Test
    void testMonthlyIncome() {
        // Test Case: Verify monthly income aggregation
        testTransactions.add(new Transaction(TEST_USER_ID, 1000.0, IncomeCategory.SALARY));
        testTransactions.add(new Transaction(TEST_USER_ID, 500.0, IncomeCategory.BUSINESS));
        testTransactions.add(new Transaction(TEST_USER_ID, 1200.0, IncomeCategory.SALARY));
        
        analyzer.updateTransactions(testTransactions);
        
        Map<String, Double> monthlyIncome = analyzer.getMonthlyIncome();
        double currentMonthTotal = monthlyIncome.values().stream().findFirst().orElse(0.0);
        assertEquals(2700.0, currentMonthTotal); // 1000 + 500 + 1200
    }

    @Test
    void testMonthlyExpenses() {
        // Test Case: Verify monthly expense aggregation
        testTransactions.add(new Transaction(TEST_USER_ID, -150.0, ExpenseCategory.FOOD));
        testTransactions.add(new Transaction(TEST_USER_ID, -200.0, ExpenseCategory.TRANSPORTATION));
        testTransactions.add(new Transaction(TEST_USER_ID, -300.0, ExpenseCategory.FOOD));
        
        analyzer.updateTransactions(testTransactions);
        
        Map<String, Double> monthlyExpenses = analyzer.getMonthlyExpenses();
        double currentMonthTotal = monthlyExpenses.values().stream().findFirst().orElse(0.0);
        assertEquals(650.0, currentMonthTotal); // 150 + 200 + 300
    }

    @Test
    void testIncomeFilteringWithMixedTransactions() {
        // Test income filtering with mixed transaction types
        testTransactions.add(new Transaction(TEST_USER_ID, 1000.0, IncomeCategory.SALARY));    // Income
        testTransactions.add(new Transaction(TEST_USER_ID, -200.0, ExpenseCategory.FOOD));     // Expense
        testTransactions.add(new Transaction(TEST_USER_ID, 500.0, IncomeCategory.BUSINESS));   // Income
        testTransactions.add(new Transaction(TEST_USER_ID, -300.0, ExpenseCategory.TRANSPORTATION)); // Expense
        
        analyzer.updateTransactions(testTransactions);
        
        // Test income filtering in getMonthlyIncome
        Map<String, Double> monthlyIncome = analyzer.getMonthlyIncome();
        double totalIncome = monthlyIncome.values().stream()
            .mapToDouble(Double::doubleValue)
            .sum();
        assertEquals(1500.0, totalIncome); // Should only include income transactions (1000 + 500)
        
        // Test income filtering in getIncomeByCategory
        Map<TransactionCategory, Double> incomeByCategory = analyzer.getIncomeByCategory();
        assertEquals(2, incomeByCategory.size()); // Should only have income categories
        assertFalse(incomeByCategory.containsKey(ExpenseCategory.FOOD));
        assertFalse(incomeByCategory.containsKey(ExpenseCategory.TRANSPORTATION));
    }

    @Test
    void testCategoryPercentagesEdgeCases() {
        // Test with empty transaction list
        Map<TransactionCategory, Double> emptyPercentages = analyzer.getCategoryPercentages(true);
        assertTrue(emptyPercentages.isEmpty(), "Should return empty map when no transactions exist");
        
        // Test with single category transactions
        testTransactions.add(new Transaction(TEST_USER_ID, -100.0, ExpenseCategory.FOOD));
        analyzer.updateTransactions(testTransactions);
        
        Map<TransactionCategory, Double> singleCategoryPercentages = analyzer.getCategoryPercentages(true);
        assertEquals(1, singleCategoryPercentages.size());
        assertEquals(100.0, singleCategoryPercentages.get(ExpenseCategory.FOOD));
        
        // Test with multiple categories
        testTransactions.add(new Transaction(TEST_USER_ID, -300.0, ExpenseCategory.TRANSPORTATION));
        analyzer.updateTransactions(testTransactions);
        
        Map<TransactionCategory, Double> multiCategoryPercentages = analyzer.getCategoryPercentages(true);
        assertEquals(25.0, multiCategoryPercentages.get(ExpenseCategory.FOOD));          // 100/400 * 100
        assertEquals(75.0, multiCategoryPercentages.get(ExpenseCategory.TRANSPORTATION)); // 300/400 * 100
        
        // Test income percentages
        testTransactions.clear();
        testTransactions.add(new Transaction(TEST_USER_ID, 1000.0, IncomeCategory.SALARY));
        testTransactions.add(new Transaction(TEST_USER_ID, 500.0, IncomeCategory.BUSINESS));
        analyzer.updateTransactions(testTransactions);
        
        Map<TransactionCategory, Double> incomePercentages = analyzer.getCategoryPercentages(false);
        assertEquals(66.67, incomePercentages.get(IncomeCategory.SALARY), 0.01);   // 1000/1500 * 100
        assertEquals(33.33, incomePercentages.get(IncomeCategory.BUSINESS), 0.01); // 500/1500 * 100
    }
}