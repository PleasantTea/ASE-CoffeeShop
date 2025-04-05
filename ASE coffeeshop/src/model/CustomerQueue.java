package model;

import java.util.LinkedList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.ArrayList;

import fileRead.MenuFileRead;
import fileRead.OrdersFileRead;
import main.Basket;
import main.MenuItem;
import main.Order;
import main.Logger;

/**
 * This class implements methods related to the customer queue, 
 * including adding waiting customers, obtaining current next customers, and so on.
 */

public class CustomerQueue {
    private static CustomerQueue instance = null;
    private final LinkedList<LinkedHashMap<Integer, Order>> queue;
    private final LinkedList<LinkedHashMap<Integer, Order>> priorityQueue;
    private static final int MAX_CAPACITY = 20;  // Limit the maximum number of customers
    private static Logger logger = Logger.getInstance();

    // Constructor function
    private CustomerQueue() {
        queue = new LinkedList<>();
        priorityQueue = new LinkedList<>();
    }

    // Get a single instance of CustomerQueue
    public static synchronized CustomerQueue getInstance() {
        if (instance == null) {
            instance = new CustomerQueue();
        }
        return instance;
    }
    
    /**
	 * Get the LinkedList of whole orders
	 * @return queue of customer orders
	 */
	public LinkedList<LinkedHashMap<Integer, Order>> getQueue() {
		return queue;
	}
	
	/**
	 * Get the LinkedList of online orders
	 * @return queue of online customer orders
	 */
	public LinkedList<LinkedHashMap<Integer, Order>> getPriorityQueue() {
		return priorityQueue;
	}
	
    /**
     * Get the size of the queue and priority queue -- the amount of the waiting customer
     * @return the size of the queue + priorityQueue
     */
    public synchronized int getQueueSize() {
        return queue.size() + priorityQueue.size();
    }

    /**
     * Add existingOrder to the queue
     */
    public synchronized void addExistingCustomer() {
    	// Retrieve the orders read from the CSV file
    	LinkedHashMap<Integer, Order> existingOrders = OrdersFileRead.existingOrder;
    	
    	// Create customer map: Map<CustomerID, Customer's Orders>
        LinkedHashMap<String, LinkedHashMap<Integer, Order>> customerOrdersMap = new LinkedHashMap<>();

        // Traverse all orders and classify them by customerID
        for (Map.Entry<Integer, Order> entry : existingOrders.entrySet()) {
            Order order = entry.getValue();
            String customerID = order.getCustomerID();

            // Retrieve the order collection of the customer, and if it does not exist, create a new LinkedHashMap
            customerOrdersMap.putIfAbsent(customerID, new LinkedHashMap<>());
            customerOrdersMap.get(customerID).put(order.getOrderID(), order);
        }

        // Add each customer's order collection (LinkedHashMap) to the queue
        for (Map.Entry<String, LinkedHashMap<Integer, Order>> entry : customerOrdersMap.entrySet()) {
        	String customerID = entry.getKey();
        	LinkedHashMap<Integer, Order> customerOrders = entry.getValue();
            
        	queue.addLast(customerOrders);
            
            logger.info("Customer " + customerID + " added to queue with " + customerOrders.size() + " orders.");
        }

        notifyAll(); // Wake up waiting threads
    }
    
    /**
     * Add customer orders(from basket) to the queue
     * @param customer orders(from basket): customerOrders (LinkedHashMap<Integer, Order>)
     */
    public synchronized void addCustomer(LinkedHashMap<Integer, Order> customerOrders) {
    	// The customer queue is full, the producer is refused and show false
        if (queue.size() + priorityQueue.size() >= MAX_CAPACITY) {
            logger.info("Customer queue is full! Order rejected.");
            throw new IllegalStateException("The customer queue is full! Please try again later.");
        }
    	
        if (customerOrders == null || customerOrders.isEmpty()) {
            throw new IllegalArgumentException("The order cannot be empty!");
        }

        queue.addLast(customerOrders);  // Add to the end of the queue
        
        String customerID = customerOrders.values().iterator().next().getCustomerID();
        logger.info("Customer " + customerID + " added to queue with " + customerOrders.size() + " orders.");
        notifyAll();  // Wake up waiting threads
    }
    
    /**
     * Add online customer orders to the priority queue
     * @param online customer orders: customerOrders (LinkedHashMap<Integer, Order>)
     */
    public synchronized void addPriorityCustomer(LinkedHashMap<Integer, Order> customerOrders) {
    	// The customer queue is full, the producer is refused and show false
        if (queue.size() + priorityQueue.size() >= MAX_CAPACITY) {
            logger.info("Customer queue is full! Order rejected.");
            throw new IllegalStateException("The customer queue is full! Please try again later.");
        }
    	
    	if (customerOrders == null || customerOrders.isEmpty()) {
             throw new IllegalArgumentException("The order cannot be empty!");
         }
    	 
    	priorityQueue.addLast(customerOrders);  // Add to priority queue 
    	
    	String customerID = customerOrders.values().iterator().next().getCustomerID();
        logger.info("Priority Customer " + customerID + " added to priority queue with " + customerOrders.size() + " orders.");
        notifyAll(); // Wake up waiting threads
    }

    /**
     * Get the next customer's order list
     * @return next customer's order list (LinkedHashMap<Integer, Order>)
     */
    public synchronized LinkedHashMap<Integer, Order> getNextCustomer() {
        while (queue.isEmpty() && priorityQueue.isEmpty()) {
            try {
            	logger.info("Customer queue is empty! Staff waiting...");
                wait(5000);  // The customer queue is empty, the consumer is waiting 5s
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            if (queue.isEmpty() && priorityQueue.isEmpty()) {
            	logger.info("Customer queue is still empty after waiting. Staff stops waiting.");
                return null;  // Return null after timeout, indicating no customer
            }
        }

        LinkedHashMap<Integer, Order> nextCustomer;
        if (!priorityQueue.isEmpty()) {
        	// Process orders in the priority queue first
        	nextCustomer = priorityQueue.removeFirst(); // Retrieve the first customer from the priority queue
            String customerID = nextCustomer.values().iterator().next().getCustomerID();

            // Recording logs
            logger.info("Processing orders for Customer " + customerID + ". Total orders: " + nextCustomer.size());
        } else {
        	// Then process regular orders
        	nextCustomer = queue.removeFirst(); // Retrieve the first customer from the queue
            String customerID = nextCustomer.values().iterator().next().getCustomerID();

            // Recording logs
            logger.info("Processing orders for Customer " + customerID + ". Total orders: " + nextCustomer.size());
        }
        return nextCustomer;
    }
    
    /**
     * Get the discount of the current customer's orders
     * @return discount of the current customer
     */
    public synchronized Float getCurrentCustomerDiscount(LinkedHashMap<Integer, Order> currentCustomer) {
    	ArrayList<MenuItem> menuItems = new ArrayList<>();
    	
    	// Traverse all orders of the current customer
        for (Map.Entry<Integer, Order> entry : currentCustomer.entrySet()) {
            Order order = entry.getValue();  // Get current order

            // Traverse the menu to retrieve the items corresponding to the current order
            for (Map.Entry<String, MenuItem> menuItemEntry : MenuFileRead.menuItemsHashMap.entrySet()) {               
                MenuItem menuItem = menuItemEntry.getValue();
                if (menuItem.getItemId().equals(order.getItemID())) {
                	menuItems.add(menuItem);
                }       
            }
        }
        
        Float customerTotalAmount = Basket.calculateTotalPrice(menuItems);  // Calculate total price of the current customer
        Float amountAfter = Basket.calculateDiscountedTotal(menuItems);  // Calculate total price after discount
        Float discount = customerTotalAmount - amountAfter;
        
    	return discount;
    }
    
    /**
     * Get the total price before discount of the current customer
     * @return total price of the current customer
     */
    public synchronized Float getCurrentCustomerTotalAmountBeforeDiscount(LinkedHashMap<Integer, Order> currentCustomer) {
    	ArrayList<MenuItem> menuItems = new ArrayList<>();
    	
    	// Traverse all orders of the current customer
        for (Map.Entry<Integer, Order> entry : currentCustomer.entrySet()) {
            Order order = entry.getValue();  // Get current order

            // Traverse the menu to retrieve the items corresponding to the current order
            for (Map.Entry<String, MenuItem> menuItemEntry : MenuFileRead.menuItemsHashMap.entrySet()) {               
                MenuItem menuItem = menuItemEntry.getValue();
                if (menuItem.getItemId().equals(order.getItemID())) {
                	menuItems.add(menuItem);
                }       
            }
        }
        
        Float customerTotalAmount = Basket.calculateTotalPrice(menuItems);  // Calculate total price of the current customer
        
    	return customerTotalAmount;
    }
}
