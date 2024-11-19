/*
 * 
 */
package pennywise.utils;

import pennywise.model.Discount;
import java.util.ArrayList;
import java.util.List;


/**
 * The Class DiscountManager.
 */
public class DiscountManager {
    
    /** The instance. */
    private static DiscountManager instance;
    
    /** The discounts. */
    private List<Discount> discounts;
    
    /**
     * Instantiates a new discount manager.
     */
    // changed from public to protected to allow mock test stubs
    protected DiscountManager() {
        discounts = new ArrayList<>();
    }
    
    /**
     * Gets the single instance of DiscountManager.
     *
     * @return single instance of DiscountManager
     */
    public static DiscountManager getInstance() {
        if (instance == null) {
            instance = new DiscountManager();
        }
        return instance;
    }
    
    /**
     * Adds the discount.
     *
     * @param discount the discount
     */
    public void addDiscount(Discount discount) {
        if (discounts.stream().noneMatch(d -> d.getCode().equals(discount.getCode()))) {
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
    public List<Discount> getAvailableDiscounts() {
        return new ArrayList<>(discounts);
    }
    
    /**
     * Find discount by code.
     *
     * @param code the code
     * @return the discount
     */
    public Discount findDiscountByCode(String code) {
        return discounts.stream()
            .filter(d -> d.getCode().equalsIgnoreCase(code))
            .findFirst()
            .orElse(null);
    }
}