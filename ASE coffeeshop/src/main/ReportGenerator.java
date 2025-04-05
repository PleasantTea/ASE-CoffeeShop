package main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fileRead.MenuFileRead;
import fileRead.OrdersFileRead;

public class ReportGenerator {
    private static ReportGenerator instance = new ReportGenerator();   // Singleton instance
    private ReportGenerator() { }   // Private constructor

    // Get instance method
    public static ReportGenerator getInstance() {
        return instance;
    }

    // Delimiter used in CSV file
    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";
    private FileWriter fileWriter = null;
    private static final String FILE_HEADER = "ITEM_ID,ITEM_NAME,CATEGORY,QUANTITY_SOLD,COST,INCOME_TOTAL";

    // Generate complete report including total income and discount
    public void generateReport() {
        List<ArrayList<String>> reportItemsList = new ArrayList<>();
        ArrayList<String> reportItems;
        HashMap<String, Integer> itemIDQuantitySoldMap = new HashMap<>();

        // Loop through menu items to generate the report
        for (Map.Entry<String, MenuItem> menuItemEntry : MenuFileRead.menuItemsHashMap.entrySet()) {
        	String itemId = menuItemEntry.getKey();  
            MenuItem menuItem = menuItemEntry.getValue();
            String name = menuItem.getItemName();  
            String category = menuItem.getCategory();  
            Float cost = menuItem.getPrice();  
          
            reportItems = new ArrayList<>();
            reportItems.add(itemId);    // Item ID
            reportItems.add(name);      //Name
            reportItems.add(category);  // Category

            // Calculate quantity sold for each item
            for (Order existingOrderEntry : OrdersFileRead.existingOrder.values()) {
                if (itemIDQuantitySoldMap != null && menuItemEntry.getKey().equals(existingOrderEntry.getItemID())) {
                    Integer quantity = 1;
                    Integer previousQuantity = itemIDQuantitySoldMap.get(existingOrderEntry.getItemID());

                    if (previousQuantity != null) {
                        itemIDQuantitySoldMap.put(existingOrderEntry.getItemID(), quantity + previousQuantity);
                    } else {
                        itemIDQuantitySoldMap.put(existingOrderEntry.getItemID(), quantity);
                    }
                }
            }
            // Quantity Sold
            Integer quantitySold = itemIDQuantitySoldMap.get(itemId);
            reportItems.add(String.valueOf(quantitySold));
            reportItems.add(cost.toString());     // Cost
            reportItemsList.add(reportItems);
        }
        generateCsvReport(reportItemsList);      // Generate CSV report
    }

    // Generate CSV file and calculate totals
    private void generateCsvReport(List<ArrayList<String>> reportItemsList) {
    	 
        try {
            // Get current timestamp for file name
        	final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            String simpleDateFormat = sdf.format(timestamp);

            // Define report directory path
            String reportDirPath = System.getProperty("user.dir") + "/src/reports/";

            // Create report directory if it doesn't exist
            File reportDirFile = new File(reportDirPath);
            if (!reportDirFile.exists()) {
                reportDirFile.mkdir();
            }

            // Write CSV file
            String fileName = reportDirFile + "/" + simpleDateFormat + ".csv";
            fileWriter = new FileWriter(fileName);

            // Write CSV file header
            fileWriter.append(FILE_HEADER);
            fileWriter.append(NEW_LINE_SEPARATOR);

            Float totalWithoutDiscount = 0f;
            Float totalIncomePerItem = 0f;

            // Iterate through the report items and write data to CSV
            for (ArrayList<String> itemList : reportItemsList) {                
                String itemId = itemList.get(0);    // ITEM_ID
                String itemName = itemList.get(1);  // ITEM_NAME
                String category = itemList.get(2);  // CATEGORY
                Integer quantitySold = "null".equals(itemList.get(3)) ? 0 : Integer.parseInt(itemList.get(3));//QUANTITY_SOLD
                Float itemPrice = Float.parseFloat(itemList.get(4));
                totalIncomePerItem = quantitySold * itemPrice;//COST

                // Write row data to CSV
                fileWriter.append(itemId);
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(itemName);
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(category);
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(quantitySold.toString().trim());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(itemPrice.toString());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(totalIncomePerItem.toString());
                fileWriter.append(NEW_LINE_SEPARATOR);

                totalWithoutDiscount += totalIncomePerItem;
            }

            // Write total information
            fileWriter.append(NEW_LINE_SEPARATOR);
            fileWriter.append(COMMA_DELIMITER).append(COMMA_DELIMITER).append(COMMA_DELIMITER).append(COMMA_DELIMITER);
            fileWriter.append("Total").append(COMMA_DELIMITER).append(totalWithoutDiscount.toString());
            fileWriter.append(NEW_LINE_SEPARATOR);
            

            // Create a Map to store each customer's order
            Map<String, ArrayList<MenuItem>> customerOrdersMap = new HashMap<>();

            // Iterate over existingOrder (the customer ID in each order is of type String)
            for (Map.Entry<Integer, Order> entry : OrdersFileRead.existingOrder.entrySet()) {
                Order order = entry.getValue();  // Get current order
                String customerId = order.getCustomerID();  // Get the customer ID of the current order

                // If the customer's order list does not exist, create a new ArrayList
                if (!customerOrdersMap.containsKey(customerId)) {
                    customerOrdersMap.put(customerId, new ArrayList<>());
                }

                // Add the current order to the list of orders corresponding to this customer
                for (Map.Entry<String, MenuItem> menuItemEntry : MenuFileRead.menuItemsHashMap.entrySet()) {               
                    MenuItem menuItem = menuItemEntry.getValue();
                    if (menuItem.getItemId().equals(order.getItemID())) {
                    	customerOrdersMap.get(customerId).add(menuItem);
                    }       
                }
            }

            // Iterate over customerOrdersMap to calculate the discounted price for each customer.
            Float total = 0f;
            for (Map.Entry<String, ArrayList<MenuItem>> entry : customerOrdersMap.entrySet()) {              
                ArrayList<MenuItem> menuItem = entry.getValue();  
                Float amountAfter = Basket.calculateDiscountedTotal(menuItem);  // Calculate total price after discount
                total = total + amountAfter;  // Total price after accumulating discounts for all customers
            }

            // Write the final discounted price
            fileWriter.append(COMMA_DELIMITER).append(COMMA_DELIMITER).append(COMMA_DELIMITER).append(COMMA_DELIMITER);
            fileWriter.append("Total After Discount").append(COMMA_DELIMITER).append(total.toString());
            fileWriter.append(NEW_LINE_SEPARATOR);

        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            // Ensure file writer is closed
            try {
                if (fileWriter != null) {
                    fileWriter.flush();
                    fileWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
     
    }
}
