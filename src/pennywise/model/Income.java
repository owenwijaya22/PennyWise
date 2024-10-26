// Income.java
package pennywise.model;

import java.util.Date;

public class Income extends Transaction {
    private IncomeCategory category;

    public Income(Date date, float amount, IncomeCategory category) {
        super(category.name(), date, amount);
        this.category = category;
    }

    public IncomeCategory getCategory() {
        return category;
    }
}