package fileRead;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import exception.InvalidOrdersFileReadException;
import main.Order;


public class OrdersFileRead {
	public static LinkedHashMap<Integer, Order> existingOrder;
	private static final String COMMA_DELIMITER = ",";
	
	// Read CSV file and store data into data structure
	public void readCSVAndStoreData(String fileName) throws InvalidOrdersFileReadException{
		System.out.println("Starting to read existing order CSV and store data into data structures");
		BufferedReader br = null;
		
		try {
		    existingOrder = new LinkedHashMap<>();
            br = new BufferedReader(new FileReader(fileName));   // Reading the csv file
            String line = "";   // Use Delimiter as COMMA
            // Read to skip the header
            if(br!=null) {
            	br.readLine();
            }
            
            // Reading from the second line
            while ((line = br.readLine()) != null){
            	if (line.trim().isEmpty() || line.replaceAll(",", "").trim().isEmpty()) {
                    continue;   // Skip completely empty lines
                }
            	String[] eachOrder = line.split(COMMA_DELIMITER);
            	// Verify data integrity
                if (eachOrder.length < 6) { 
                    throw new InvalidOrdersFileReadException("Invalid row: missing columns in CSV file.");
                }
                // Get the individual fields and remove the first and last spaces
                String orderIdStr = eachOrder[0].trim();
                String customerId = eachOrder[1].trim();
                String itemId = eachOrder[2].trim();
                String itemName = eachOrder[3].trim();
                String itemPriceStr = eachOrder[4].trim();
                String timestamp = eachOrder[5].trim();
                
                // Check Order ID
                int orderId;
                try {
                    orderId = Integer.parseInt(orderIdStr);
                } catch (NumberFormatException e) {
                    throw new InvalidOrdersFileReadException("Invalid Order ID format: " + orderIdStr, e);
                }
                
                // Verify Customer ID
                if (customerId.isEmpty() || customerId.matches(".*[@#$&*!/|%`^()~?+=-].*") || customerId.matches("\\d+")) {
                    throw new InvalidOrdersFileReadException("Invalid customerId in Order Item -> " + customerId);
                }
                
                // Verify Item ID
                if (itemId.isEmpty() || itemId.matches(".*[@#$&*!/|%`^()~?+=-].*") || itemId.matches("\\d+")) {
                	throw new InvalidOrdersFileReadException("Invalid itemId in Order Item -> " + itemId);
            	}
                
                // Calibration Item Price
                float itemPrice;
                try {
                    itemPrice = Float.parseFloat(itemPriceStr);
                    if (itemPrice < 0) {
                        throw new InvalidOrdersFileReadException("Invalid price for item: " + itemPriceStr);
                    }
                } catch (NumberFormatException e) {
                    throw new InvalidOrdersFileReadException("Invalid price format for item: " + itemPriceStr, e);
                }
                
                // Checksum timestamp: check the format with a regular expression
                if (timestamp.isEmpty() || timestamp.equals("0") || !timestamp.matches("^\\d{4}/\\d{1,2}/\\d{1,2} \\d{2}:\\d{2}:\\d{2}$")){
                    throw new InvalidOrdersFileReadException("Invalid timestamp in Order Item(corresponding orderIdStr) -> " + orderIdStr);
                }
                // Create new Order object and store it in HashMap
                Order order = new Order();
                order.setOrderID(orderId);
                order.setCustomerID(customerId);
                order.setItemID(itemId);
                order.setItemName(itemName);
                order.setItemPrice(itemPrice);
                order.setCurrTimeFromString(timestamp);

                existingOrder.put(orderId, order);
            }
            System.out.println("Completed Reading and Storing Existing Order CSV data into Data Structures");
	    }
        catch(IOException ee){
            // Catch IO-related exceptions and throw custom exceptions.
            throw new InvalidOrdersFileReadException("Error reading the CSV file: " + fileName, ee);
        }
        finally{
            try{
            	// Close the BufferedReader to free up resources.
                if(br!= null) {
                	br.close();
                }
            }
            catch(IOException ie){
            	System.out.println("Exception : Error occured while closing the BufferedReader");
                ie.printStackTrace();
            }
        }
	}
	
	// Save new Orders data in existing orders
	public void saveNewOrdersInExistingOrders(LinkedHashMap<Integer, Order> newOrder) {
		System.out.println("Saving new orders into existing orders");
		existingOrder.putAll(newOrder);			
	}
	
	// Get the latest order number
	public static Integer getLastOrderNumber() {
		//return existingOrder.isEmpty() ? null : existingOrder.lastKey();
		return existingOrder.keySet().stream()
	             .max(Integer::compareTo)              // Find the largest order ID
	             .orElse(null);                        // If the flow is empty, return null
	}
	
	// Get the latest customer number
	public static Integer getLastCustomerNumber() {
	    return existingOrder.values().stream()         // Obtain all existing orders
	             .map(Order::getCustomerID)	           // Get all customer IDs in the current
	             .map(id -> id.replaceAll("\\D", ""))  // Remove non numeric characters "CUS1" -> "   1"
	             .filter(id -> !id.isEmpty())          // Filter out empty strings ->"1"
	             .map(Integer::parseInt)               // Convert the remaining numerical part to an integer
	             .max(Integer::compareTo)              // Find the largest customer ID
	             .orElse(null);                        // If the flow is empty, return null
	}
	
	// Write order data back to CSV file
	public void writeOrdersToCSV(String fileName) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(fileName));
            // Write into header
            writer.write("OrderID,CustomerID,ItemID,ItemName,ItemPrice,Timestamp");
            writer.newLine();

            // Write into data
            for (Order order : existingOrder.values()) {
                writer.write(order.toCSVString()); 
                writer.newLine();
            }
            System.out.println("New orders saved successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
