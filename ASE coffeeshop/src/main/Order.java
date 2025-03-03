package main;

import java.util.Set;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Order {

	private Integer orderID;
	private String customerID;
	private Set<Integer> orderIDs;
	private String itemID;
	private String itemName;
	private Integer itemPrice;
	private Integer quantity;
	private LocalDateTime currTime;
	private String discount;
	
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
	
	public String getDiscount() {
		return discount;
	}
	public void setDiscount(String discount) {
		this.discount = discount;
	}

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
	
	public Set<Integer> getOrderIDs() {
		return orderIDs;
	}
	public void setOrderIDs(Set<Integer> orderIDs) {
		this.orderIDs = orderIDs;
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

	public Integer getItemPrice() {
		return itemPrice;
	}
	public void setItemPrice(Integer itemPrice) {
		this.itemPrice = itemPrice;
	}
	
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
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
		return "Order ID: " + orderID + ", Customer Id: " + customerID + "Order List: " + orderIDs + ", Item ID: " + itemID 
				+ ", Item Name: " + itemName + ", Item Price: " + itemPrice + ", Quantity:  " + quantity + ", Timestamp: " 
				+ getCurrTimeAsString();
	}	
	
	public String toCSVString() {
        return String.join(",", orderID.toString(), customerID, itemID, itemName, itemPrice.toString(), 
        		quantity.toString(), getCurrTimeAsString());
    }//
	
}
