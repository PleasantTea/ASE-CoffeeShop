package fileRead;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;

import main.Order;


public class OrdersFileRead {

	public static LinkedHashMap<Integer, Order> existingOrder;
	private static final String COMMA_DELIMITER = ",";
	
	// Read CSV file and store data into data structure
	public void readCSVAndStoreData(String filePath){
		System.out.println("Starting to read existing order CSV and store data into data structures");
		BufferedReader br = null;
		
		try {
		    existingOrder = new LinkedHashMap<>();
			
			// Reading the csv file
            br = new BufferedReader(new FileReader(filePath));
			
            // Use Delimiter as COMMA
            String line = "";
            
            // Read to skip the header
            if(br!=null) {
            	br.readLine();
            }
            
            // Reading from the second line
            while ((line = br.readLine()) != null){
            	
            	String[] eachOrder = line.split(COMMA_DELIMITER);
            	
                if(eachOrder.length >= 7 ) // 8(add discount?)
                {
                	// Save the order item details in Order object
                	Order order = new Order();
                	order.setOrderID(Integer.parseInt(eachOrder[0]));
                    order.setCustomerID(eachOrder[1]);
                    order.setItemID(eachOrder[2]);
                    order.setItemName(eachOrder[3]);
                    order.setItemPrice(Integer.parseInt(eachOrder[4]));
                    order.setQuantity(Integer.parseInt(eachOrder[5]));
                    order.setCurrTimeFromString(eachOrder[6]);
                    // order.setDiscount(eachOrder[7]);

                    // Populate order in TreeMap with orderID as Key
                    existingOrder.put(order.getOrderID(), order);               	
                }
            }
            
            System.out.println("Completed Reading and Storing Existing Order CSV data into Data Structures");
	    }
        catch(Exception ee){
            ee.printStackTrace();
        }
        finally
        {
            try
            {
                if(br!= null) {
                	br.close();
                }
            }
            catch(IOException ie)
            {
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
	    return existingOrder.values().stream()
	             .map(Order::getCustomerID)
	             .map(id -> id.replaceAll("\\D", ""))  // Remove non numeric characters "CUS1" -> "   1"
	             .filter(id -> !id.isEmpty())          // Filter out empty strings "1"
	             .map(Integer::parseInt)               // Convert the remaining numerical part to an integer
	             .max(Integer::compareTo)              // Find the largest customer ID
	             .orElse(null);                        // If the flow is empty, return null
	}
	
	// Write order data back to CSV file
	public void writeOrdersToCSV() {
        BufferedWriter writer = null;

        try {
            writer = new BufferedWriter(new FileWriter("ASE coffeeshop/src/newOrders.csv"));
            // Write into header
            writer.write("OrderID,CustomerID,ItemID,ItemName,ItemPrice,Quantity,Timestamp");  // Discount?
            writer.newLine();

            // Write into data
            for (Order order : existingOrder.values()) {
                writer.write(order.toCSVString());  // Discount?
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
