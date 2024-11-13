package test.stubs;

import pennywise.model.Discount;
import pennywise.utils.DiscountManager;
import java.lang.reflect.Field;
import java.util.*;

public class MockDiscountManager extends DiscountManager {
    private List<Discount> discounts = new ArrayList<>();

    public MockDiscountManager() throws Exception {
        Field instanceField = DiscountManager.class.getDeclaredField("instance");
        instanceField.setAccessible(true);
        instanceField.set(null, this);
    }
    
    @Override
    public void addDiscount(Discount discount) {
        if (discount != null && !hasDiscountCode(discount.getCode())) {
            discounts.add(discount);
        }
    }
    
    @Override
    public List<Discount> getAvailableDiscounts() {
        return new ArrayList<>(discounts);
    }
    
    @Override
    public Discount findDiscountByCode(String code) {
        return discounts.stream()
            .filter(d -> d.getCode().equalsIgnoreCase(code))
            .findFirst()
            .orElse(null);
    }
    
    private boolean hasDiscountCode(String code) {
        return discounts.stream()
            .anyMatch(d -> d.getCode().equalsIgnoreCase(code));
    }
    
    public void clearDiscounts() {
        discounts.clear();
    }
}
