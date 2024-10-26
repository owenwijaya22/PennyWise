package pennywise.model;

import java.util.Date;

public abstract class Transaction {
	private String description;
	private float amount;
	private Date date;
	
	

	public Transaction(String description, Date date, float amount) {
		this.description = description;
		this.amount = amount;
		this.date = date;
	}


	public String getDescription() {
		return description;
	}

	public float getAmount() {
		return amount;
	}
	
	public Date getDate() {
		return date;
	}
	
	

}