package main;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Order {

	private Integer orderID;
	private String customerID;
	private String itemID;
	private String itemName;
	private Float itemPrice;
	private LocalDateTime currTime;
	
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

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
	
    public void setCurrTimeFromString(String timeStr) {
        this.currTime = LocalDateTime.parse(timeStr, formatter);
    }
    public String getCurrTimeAsString() {
        return currTime.format(formatter);
    }

	
	@Override
	public String toString() {
		return "Order ID: " + orderID + ", Customer Id: " + customerID + ", Item ID: " + itemID 
				+ ", Item Name: " + itemName + ", Item Price: " + itemPrice +  ", Timestamp: " + getCurrTimeAsString();
	}	
	
	public String toCSVString() {
        return String.join(",", orderID.toString(), customerID, itemID, itemName, itemPrice.toString(), getCurrTimeAsString());
    }
	
}
