package test.stubs;

import pennywise.model.Discount;
import pennywise.utils.DiscountManager;
import java.lang.reflect.Field;
import java.util.*;

public class MockDiscountManager extends DiscountManager {
    private List<Discount> discounts = new ArrayList<>();
    private static MockDiscountManager instance;

    public MockDiscountManager() {
        try {
            Field instanceField = DiscountManager.class.getDeclaredField("instance");
            instanceField.setAccessible(true);
            instanceField.set(null, this);
            instance = this;
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize MockDiscountManager", e);
        }
    }
    
    public static MockDiscountManager getInstance() {
        if (instance == null) {
            instance = new MockDiscountManager();
        }
        return instance;
    }
    
    @Override
    public void addDiscount(Discount discount) {
        if (discount != null && !hasDiscountCode(discount.getCode())) {
            discounts.add(discount);
            System.out.println("Discount added successfully!");
        } else {
            System.out.println("Discount code already exists. Please use a unique code.");
        }
    }
    
    @Override
    public List<Discount> getAvailableDiscounts() {
        return new ArrayList<>(discounts);
    }
    
    @Override
    public Discount findDiscountByCode(String code) {
        if (code == null) return null;
        return discounts.stream()
            .filter(d -> d.getCode().equalsIgnoreCase(code) && d.isValid())
            .findFirst()
            .orElse(null);
    }
    
    private boolean hasDiscountCode(String code) {
        if (code == null) return false;
        return discounts.stream()
            .anyMatch(d -> d.getCode().equalsIgnoreCase(code));
    }
    
    public void clearDiscounts() {
        discounts.clear();
        try {
            Field instanceField = DiscountManager.class.getDeclaredField("instance");
            instanceField.setAccessible(true);
            instanceField.set(null, null);
            instance = null;
        } catch (Exception e) {
            throw new RuntimeException("Failed to clear MockDiscountManager", e);
        }
    }
}
