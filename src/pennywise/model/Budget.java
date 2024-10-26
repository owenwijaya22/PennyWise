package pennywise.model;

public class Budget {
    private String id;
    private float amount;
    private float spent;
    
    public Budget(String id, float amount) {
        this.id = id;
        this.amount = amount;
        this.spent = 0;
    }
    
    public String getId() {
        return id;
    }
    
    public float getAmount() {
        return amount;
    }
    
    public void setAmount(float amount) {
        this.amount = amount;
    }
    
    public float getSpent() {
        return spent;
    }
    
    public void addExpense(float expense) {
        this.spent += expense;
    }
    
    public float getRemainingBudget() {
        return amount - spent;
    }
}