package main;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * The class contains details about an order. 
 * Each order contains details such as order ID, customer ID, 
 * item ID, item name, item price, and timestamp.
 */

public class Order {

	private Integer orderID;
	private String customerID;
	private String itemID;
	private String itemName;
	private Float itemPrice;
	private LocalDateTime currTime;
	
	// Formatter for converting between LocalDateTime and String
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

	// Getter and Setter methods
	public Integer getOrderID() {
		return orderID;
	}
	public void setOrderID(Integer orderID) {
		this.orderID = orderID;
	}
	
	public String getCustomerID() {
		return customerID;
	}
	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}
	
	public String getItemID() {
		return itemID;
	}
	public void setItemID(String itemID) {
		this.itemID = itemID;
	}

	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Float getItemPrice() {
		return itemPrice;
	}
	public void setItemPrice(Float itemPrice) {
		this.itemPrice = itemPrice;
	}
	
	public LocalDateTime getCurrTime() {
		return currTime;
	}
	public void setCurrTime(LocalDateTime currTime) {
		this.currTime = currTime;
	}
	
	/**
     * Sets the current time from a formatted string.
     *
     * @param timeStr The time string in the format "yyyy/MM/dd HH:mm:ss".
     */
    public void setCurrTimeFromString(String timeStr) {
        this.currTime = LocalDateTime.parse(timeStr, formatter);
    }
    
    /**
     * Returns the current time as a formatted string.
     *
     * @return The formatted time string in the format "yyyy/MM/dd HH:mm:ss".
     */
    public String getCurrTimeAsString() {
        return currTime.format(formatter);
    }

	
	@Override
	public String toString() {
		return "Order ID: " + orderID + ", Customer Id: " + customerID + ", Item ID: " + itemID 
				+ ", Item Name: " + itemName + ", Item Price: " + itemPrice +  ", Timestamp: " + getCurrTimeAsString();
	}	
	
	// Returns a CSV-formatted string representation of the order.
	public String toCSVString() {
        return String.join(",", orderID.toString(), customerID, itemID, itemName, itemPrice.toString(), getCurrTimeAsString());
    }
	
}
