package pennywise.utils;

import pennywise.model.Discount;

import java.util.ArrayList;
import java.util.List;

public class DiscountManager {
    private static DiscountManager instance;
    private List<Discount> availableDiscounts;

    private DiscountManager() {
        availableDiscounts = new ArrayList<>();
    }

    public static DiscountManager getInstance() {
        if (instance == null) {
            synchronized (DiscountManager.class) {
                if (instance == null) {
                    instance = new DiscountManager();
                }
            }
        }
        return instance;
    }

    public List<Discount> getAvailableDiscounts() {
        return new ArrayList<>(availableDiscounts);
    }

    public void addDiscount(Discount discount) {
        availableDiscounts.add(discount);
    }
}