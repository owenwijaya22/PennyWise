package pennywise.utils;

import pennywise.model.Discount;
import java.util.ArrayList;
import java.util.List;

public class DiscountManager {
    private static DiscountManager instance;
    private List<Discount> discounts;
    
    // changed from public to protected to allow mock test stubs
    protected DiscountManager() {
        discounts = new ArrayList<>();
    }
    
    public static DiscountManager getInstance() {
        if (instance == null) {
            instance = new DiscountManager();
        }
        return instance;
    }
    
    public void addDiscount(Discount discount) {
        if (discounts.stream().noneMatch(d -> d.getCode().equals(discount.getCode()))) {
            discounts.add(discount);
            System.out.println("Discount added successfully!");
        } else {
            System.out.println("Discount code already exists. Please use a unique code.");
        }
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