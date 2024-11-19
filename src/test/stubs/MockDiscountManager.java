/*
 * 
 */
package test.stubs;

import pennywise.model.Discount;
import pennywise.utils.DiscountManager;
import java.lang.reflect.Field;
import java.util.*;


/**
 * The Class MockDiscountManager.
 */
public class MockDiscountManager extends DiscountManager {
    
    /** The discounts. */
    private List<Discount> discounts = new ArrayList<>();
    
    /** The instance. */
    private static MockDiscountManager instance;

    /**
     * Instantiates a new mock discount manager.
     */
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
    
    /**
     * Gets the single instance of MockDiscountManager.
     *
     * @return single instance of MockDiscountManager
     */
    public static MockDiscountManager getInstance() {
        if (instance == null) {
            instance = new MockDiscountManager();
        }
        return instance;
    }
    
    /**
     * Adds the discount.
     *
     * @param discount the discount
     */
    @Override
    public void addDiscount(Discount discount) {
        if (discount != null && !hasDiscountCode(discount.getCode())) {
            discounts.add(discount);
            System.out.println("Discount added successfully!");
        } else {
            System.out.println("Discount code already exists. Please use a unique code.");
        }
    }
    
    /**
     * Gets the available discounts.
     *
     * @return the available discounts
     */
    @Override
    public List<Discount> getAvailableDiscounts() {
        return new ArrayList<>(discounts);
    }
    
    /**
     * Find discount by code.
     *
     * @param code the code
     * @return the discount
     */
    @Override
    public Discount findDiscountByCode(String code) {
        if (code == null) return null;
        return discounts.stream()
            .filter(d -> d.getCode().equalsIgnoreCase(code) && d.isValid())
            .findFirst()
            .orElse(null);
    }
    
    /**
     * Checks for discount code.
     *
     * @param code the code
     * @return true, if successful
     */
    private boolean hasDiscountCode(String code) {
        if (code == null) return false;
        return discounts.stream()
            .anyMatch(d -> d.getCode().equalsIgnoreCase(code));
    }
    
    /**
     * Clear discounts.
     */
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
