package pennywise.ui.handlers;

import pennywise.model.Discount;
import pennywise.ui.UIConstants;
import pennywise.utils.DiscountManager;
import java.util.*;

public class DiscountHandler {
    private final DiscountManager discountManager;
    private final InputHandler inputHandler;

    public DiscountHandler(DiscountManager discountManager, InputHandler inputHandler) {
        this.discountManager = discountManager;
        this.inputHandler = inputHandler;
    }

    public void handleDiscountVisualization() {
        DiscountManager discountManager = DiscountManager.getInstance();
        
        while (true) {
            System.out.println(UIConstants.DISCOUNT_MENU_TITLE);
            for (String option : UIConstants.DISCOUNT_MENU_OPTIONS) {
                System.out.println(option);
            }
            System.out.println(UIConstants.CHOOSE_OPTION_PROMPT);
            
            int choice = inputHandler.readInt();
            
            switch (choice) {
                case 1:
                    displayDiscounts(discountManager.getAvailableDiscounts());
                    break;
                case 2:
                    addCustomDiscount(discountManager);
                    break;
                case 3:
                    addPredeterminedDiscount(discountManager);
                    break;
                case 4:
                    return;
                default:
                    System.out.println(UIConstants.INVALID_OPTION_MESSAGE);
            }
        }
    }

    public void handleDiscountCode(double amount) {
        System.out.print(UIConstants.ENTER_DISCOUNT_CODE_PROMPT);
        String discountCode = inputHandler.readLine().trim().toUpperCase();
        
        Discount discount = discountManager.findDiscountByCode(discountCode);
        
        if (discount != null && discount.isValid()) {
            double discountAmount = amount * (discount.getPercentage() / 100);
            amount -= discountAmount;
            System.out.printf(UIConstants.DISCOUNT_APPLIED_FORMAT, discountAmount, discount.getPercentage());
        } else {
            System.out.println(UIConstants.INVALID_DISCOUNT_MESSAGE);
        }
    }

    public void addCustomDiscount(DiscountManager discountManager) {
        System.out.println(UIConstants.CUSTOM_DISCOUNT_TITLE);
        Discount newDiscount;
        while(true) {
        System.out.print(UIConstants.ENTER_DISCOUNT_CODE_PROMPT);
        String code = inputHandler.readLine().trim().toUpperCase();
        
        System.out.print(UIConstants.ENTER_DISCOUNT_PERCENTAGE_PROMPT);
        float percentage = inputHandler.readFloat();
        
        System.out.print(UIConstants.ENTER_DESCRIPTION_PROMPT);
        String description = inputHandler.readLine();
        
        System.out.println(UIConstants.EXPIRY_DATE_FORMAT);
        System.out.print(UIConstants.ENTER_DAYS_PROMPT);
        int days = inputHandler.readInt();
        
        Date expiryDate = new Date(System.currentTimeMillis() + (long)days * 24 * 60 * 60 * 1000);
        
        newDiscount = new Discount(code, percentage, expiryDate, description);
        if(newDiscount.isValid()) {
        	break;
        }
        System.out.println(UIConstants.INVALID_DISCOUNT_MESSAGE);
        }
        discountManager.addDiscount(newDiscount);
        
    }

    private void addPredeterminedDiscount(DiscountManager discountManager) {
        System.out.println(UIConstants.PREDETERMINED_DISCOUNT_TITLE);
        for (String option : UIConstants.DISCOUNT_OPTIONS) {
            System.out.println(option);
        }
        System.out.println(UIConstants.CHOOSE_OPTION_PROMPT);
        
        int choice = inputHandler.readInt();
        
        Date defaultExpiry = new Date(System.currentTimeMillis() + 365L * 24 * 60 * 60 * 1000);
        
        Discount selectedDiscount = null;
        switch (choice) {
            case 1:
                selectedDiscount = new Discount(
                    "APPLEEDU",
                    10.0f,
                    defaultExpiry,
                    "Apple Education Store - Up to 10% off on Mac, iPad, and accessories"
                );
                break;
            case 2:
                selectedDiscount = new Discount(
                    "OCTOPUSSTUDENT",
                    20.0f,
                    defaultExpiry,
                    "Octopus Student Status - 20% off on transportation fares"
                );
                break;
            case 3:
                selectedDiscount = new Discount(
                    "GITEDU",
                    100.0f,
                    defaultExpiry,
                    "GitHub Education Pack - Free Pro features for verified teachers/staff"
                );
                break;
            case 4:
                selectedDiscount = new Discount(
                    "SPOTIFYEDU",
                    50.0f,
                    defaultExpiry,
                    "Spotify Premium Student - 50% off monthly subscription"
                );
                break;
            case 5:
                selectedDiscount = new Discount(
                    "MS365EDU",
                    100.0f,
                    defaultExpiry,
                    "Free Microsoft 365 Apps for Education with valid school email"
                );
                break;
            case 6:
                selectedDiscount = new Discount(
                    "ADOBEEDU",
                    60.0f,
                    defaultExpiry,
                    "Adobe Creative Cloud - 60% off for students and teachers"
                );
                break;
            case 7:
                return;
            default:
                System.out.println(UIConstants.INVALID_OPTION_MESSAGE);
                return;
        }
        
        if (selectedDiscount != null) {
            discountManager.addDiscount(selectedDiscount);
            System.out.println(UIConstants.PREDETERMINED_DISCOUNT_SUCCESS);
        }
    }

    private void displayDiscounts(List<Discount> discounts) {
        if (discounts.isEmpty()) {
            System.out.println(UIConstants.NO_DISCOUNTS_MESSAGE);
            return;
        }

        System.out.println(UIConstants.DISCOUNT_BORDER_TOP);
        
        for (Discount discount : discounts) {
            if (discount.getPercentage() < 0 || discount.getPercentage() > 100) {
                continue; 
            }
            boolean isValid = discount.isValid();
            String status = isValid ? UIConstants.ACTIVE_DISCOUNT_SYMBOL : UIConstants.EXPIRED_DISCOUNT_SYMBOL;
            
            long diffInMillies = discount.getExpiryDate().getTime() - System.currentTimeMillis();
            long diffInDays = diffInMillies / (24 * 60 * 60 * 1000);
            
            String timeInfo = isValid ? 
                String.format(UIConstants.DAYS_REMAINING_FORMAT, diffInDays) :
                String.format(UIConstants.DAYS_EXPIRED_FORMAT, Math.abs(diffInDays));

            int filledBars = (int)((discount.getPercentage() / 100) * UIConstants.PROGRESS_BAR_LENGTH);
            String percentageBar = UIConstants.PROGRESS_BAR_FILLED.repeat(Math.min(filledBars, UIConstants.PROGRESS_BAR_LENGTH)) + 
                                 UIConstants.PROGRESS_BAR_EMPTY.repeat(Math.max(0, UIConstants.PROGRESS_BAR_LENGTH - filledBars));

            System.out.println(UIConstants.DISCOUNT_BORDER_MIDDLE);
            System.out.printf(UIConstants.DISCOUNT_CODE_FORMAT, discount.getCode(), status);
            System.out.printf(UIConstants.DISCOUNT_BAR_FORMAT, percentageBar, discount.getPercentage());
            System.out.printf(UIConstants.DISCOUNT_DESCRIPTION_FORMAT, discount.getDescription());
            System.out.printf(UIConstants.DISCOUNT_EXPIRY_FORMAT, discount.getExpiryDate().toString());
            System.out.printf(UIConstants.DISCOUNT_STATUS_FORMAT, timeInfo);
        }

        System.out.println(UIConstants.DISCOUNT_BORDER_BOTTOM);
        
        long activeCount = discounts.stream().filter(Discount::isValid).count();
        double avgDiscount = discounts.stream()
            .filter(Discount::isValid)
            .mapToDouble(Discount::getPercentage)
            .average()
            .orElse(0.0);

        System.out.println(UIConstants.QUICK_STATS_HEADER);
        System.out.printf(UIConstants.ACTIVE_DISCOUNTS_FORMAT, activeCount, discounts.size());
        System.out.printf(UIConstants.AVERAGE_DISCOUNT_FORMAT, avgDiscount);
    }
}