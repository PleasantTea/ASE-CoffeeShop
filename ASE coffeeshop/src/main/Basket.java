package main;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import fileRead.OrdersFileRead;

public class Basket {
	private ArrayList<MenuItem> menuItems;
	
	// Constructor
	public Basket() {
		menuItems = new ArrayList<>();
	}
	
	// Gets items
	public ArrayList<MenuItem> getItems() {
		return menuItems;
	}
	
	// Adds item to underlying list
	public void addItem(MenuItem item) {
		menuItems.add(item);
	}
	
	// Removes item from underlying list
	public void removeItem(MenuItem item) {
		menuItems.remove(item);
	}
	
	// Clears items list
	public void clearBasket() {
		menuItems.clear();
	}
	
	// Calculates the discounted total
	public static float calculateDiscountedTotal(ArrayList<MenuItem> itemList) {
		float result = calculateTotalPrice(itemList);
		
		DiscountRuler discountRuler = new DiscountRuler();
		result = discountRuler.applyDiscounts(result, itemList);
		
		return result;
	}

	// Calculates the total before discounts
	public static Float calculateTotalPrice(ArrayList<MenuItem> itemList) {
		Float total = (float)0;
		
		if (itemList == null || itemList.isEmpty()) {
		    return (float)0; 
		}
		
		// Total up the prices from products
		for (MenuItem menuItem: itemList) {
			Float price = menuItem.getPrice();
	        if (price < 0) {
	            throw new IllegalArgumentException("The product price cannot be negative: " + menuItem.getItemName());
	        }
	        total += price;
		}
		
		return total;
	}
	
	// Confirm order: Convert the items in the shopping cart into an order and store it in existingOrder
    public void confirmOrder() {
        if (menuItems.isEmpty()) {
            System.out.println("The shopping cart is empty, unable to submit order!");
            return;
        }

        // Get latest OrderID
        Integer lastOrderID = OrdersFileRead.getLastOrderNumber();
        int newOrderID = (lastOrderID != null) ? lastOrderID + 1 : 1;

        // Get latest CustomerID
        Integer lastCustomerNum = OrdersFileRead.getLastCustomerNumber();
        int newCustomerNum = (lastCustomerNum != null) ? lastCustomerNum + 1 : 1;
        String newCustomerID = "CUS" + newCustomerNum;

        // Get current time
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));

        // Generate an order and store it in existingOrder
        LinkedHashMap<Integer, Order> newOrders = new LinkedHashMap<>();
        for (MenuItem item : menuItems) {
            Order order = new Order();
            order.setOrderID(newOrderID++);
            order.setCustomerID(newCustomerID);
            order.setItemID(item.getItemId());
            order.setItemName(item.getItemName());
            order.setItemPrice(item.getPrice());
            order.setCurrTimeFromString(timestamp);

            newOrders.put(order.getOrderID(), order);
        }

        // Save orders
        OrdersFileRead ordersFileRead = new OrdersFileRead();
        ordersFileRead.saveNewOrdersInExistingOrders(newOrders);

        // Clear basket
        clearBasket();

        // Write into CSV
        String newOrdersFileName = "ASE-CoffeeShop/ASE coffeeshop/src/newOrders.csv";
        ordersFileRead.writeOrdersToCSV(newOrdersFileName);

        System.out.println("The order has been confirmed and saved!");
    }
}

