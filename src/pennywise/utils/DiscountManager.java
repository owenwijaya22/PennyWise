package pennywise.utils;

import pennywise.model.Discount;
import java.util.ArrayList;
import java.util.List;

public class DiscountManager {
    private static DiscountManager instance;
    private List<Discount> discounts;
    
    private DiscountManager() {
        discounts = new ArrayList<>();
    }
    
    public static DiscountManager getInstance() {
        if (instance == null) {
            instance = new DiscountManager();
        }
        return instance;
    }
    
    public void addDiscount(Discount discount) {
        discounts.add(discount);
    }
    
    public List<Discount> getAvailableDiscounts() {
        return new ArrayList<>(discounts);
    }
    
    public Discount findDiscountByCode(String code) {
        return discounts.stream()
            .filter(d -> d.getCode().equalsIgnoreCase(code))
            .findFirst()
            .orElse(null);
    }
}