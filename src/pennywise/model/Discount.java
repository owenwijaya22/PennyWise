/*
 * 
 */
package pennywise.model;

import java.util.Date;


/**
 * The Class Discount.
 */
public class Discount {
    
    /** The code. */
    private String code;
    
    /** The percentage. */
    private float percentage;
    
    /** The expiry date. */
    private Date expiryDate;
    
    /** The description. */
    private String description;
    
    /**
     * Instantiates a new discount.
     *
     * @param code the code
     * @param percentage the percentage
     * @param expiryDate the expiry date
     * @param description the description
     */
    public Discount(String code, float percentage, Date expiryDate, String description) {
        this.code = code;
        this.percentage = percentage;
        this.expiryDate = expiryDate;
        this.description = description;
    }
    
    /**
     * Gets the code.
     *
     * @return the code
     */
    public String getCode() {
        return code;
    }
    
    /**
     * Gets the percentage.
     *
     * @return the percentage
     */
    public float getPercentage() {
        return percentage;
    }
    
    /**
     * Gets the expiry date.
     *
     * @return the expiry date
     */
    public Date getExpiryDate() {
        return expiryDate;
    }
    
    /**
     * Gets the description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Checks if is valid.
     *
     * @return true, if is valid
     */
    public boolean isValid() {
        return new Date().before(expiryDate)&& percentage <100 && percentage >0;
    }
}