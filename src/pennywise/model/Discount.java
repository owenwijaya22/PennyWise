package pennywise.model;

import java.util.Date;

public class Discount {
    private String code;
    private float percentage;
    private Date expiryDate;
    private String description;
    
    public Discount(String code, float percentage, Date expiryDate, String description) {
        this.code = code;
        this.percentage = percentage;
        this.expiryDate = expiryDate;
        this.description = description;
    }
    
    public String getCode() {
        return code;
    }
    
    public float getPercentage() {
        return percentage;
    }
    
    public Date getExpiryDate() {
        return expiryDate;
    }
    
    public String getDescription() {
        return description;
    }
    
    public boolean isValid() {
        return new Date().before(expiryDate)&& percentage <100 && percentage >0;
    }
}