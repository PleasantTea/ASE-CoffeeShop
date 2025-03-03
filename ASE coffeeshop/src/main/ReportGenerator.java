package main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;//

import fileRead.MenuFileRead;
import fileRead.OrdersFileRead;
import main.DiscountRuler;

public class ReportGenerator {
	public static void main(String[] args) {
		MenuFileRead menuReader = new MenuFileRead();
        menuReader.readCSVAndStoreData("ASE coffeeshop/src/Menu.csv");
        
		OrdersFileRead ordersFileRead = new OrdersFileRead();
	    ordersFileRead.readCSVAndStoreData("ASE coffeeshop/src/Orders.csv");
		
	    ReportGenerator report = new ReportGenerator();
		report.generateReport();
	}

    // Singleton instance
    private static ReportGenerator instance = new ReportGenerator();

    // Private constructor
    private ReportGenerator() { }

    // Get instance method
    public static ReportGenerator getInstance() {
        return instance;
    }

    // Delimiter used in CSV file
    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";
    private FileWriter fileWriter = null;
    private static final String FILE_HEADER = "ITEM_ID,ITEM_NAME,CATEGORY,QUANTITY_SOLD,COST,INCOME_TOTAL";

    // Create an instance of DiscountRuler for discount calculation
    private DiscountRuler discountRuler = new DiscountRuler();

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
            
            // Item ID
            reportItems.add(itemId);
            
            //Name
            reportItems.add(name);
            
            // Category
            reportItems.add(category);

            // Calculate quantity sold for each item
            for (Order existingOrderEntry : OrdersFileRead.existingOrder.values()) {
                if (itemIDQuantitySoldMap != null && menuItemEntry.getKey().equals(existingOrderEntry.getItemID())) {
                    Integer quantity = existingOrderEntry.getQuantity();
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
        
      
            // Cost
            reportItems.add(cost.toString());

            reportItemsList.add(reportItems);
        }

        // Generate CSV report
        generateCsvReport(reportItemsList);
    }

    // Generate CSV file and calculate totals
    private void generateCsvReport(List<ArrayList<String>> reportItemsList) {
        try {
            // Get current timestamp for file name
            //final SimpleDateFormat sdf = new SimpleDateFormat("year.mo.da.ho.mi.se");
        	final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            String simpleDateFormat = sdf.format(timestamp);

            // Define report directory path
            String reportDirPath = System.getProperty("user.dir") + "/reports/";

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

            Double totalWithoutDiscount = 0.0;
            Double totalIncomePerItem = 0.0;

            // Iterate through the report items and write data to CSV
            for (ArrayList<String> itemList : reportItemsList) {                
                String itemId = itemList.get(0);    // ITEM_ID
                String itemName = itemList.get(1);  // ITEM_NAME
                String category = itemList.get(2);  // CATEGORY
                Integer quantitySold = "null".equals(itemList.get(3)) ? 0 : Integer.parseInt(itemList.get(3));//QUANTITY_SOLD
                Double itemPrice = Double.parseDouble(itemList.get(4));
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
            
            Double finalTotal = totalWithoutDiscount;
            fileWriter.append(COMMA_DELIMITER).append(COMMA_DELIMITER).append(COMMA_DELIMITER).append(COMMA_DELIMITER);
            fileWriter.append("Total After Discount").append(COMMA_DELIMITER).append(finalTotal.toString());
            
            
/*
            // Calculate and write discount using DiscountRuler
            Double totalDiscount = discountRuler.calculateDiscount(totalWithoutDiscount);
            fileWriter.append(COMMA_DELIMITER).append(COMMA_DELIMITER).append(COMMA_DELIMITER).append(COMMA_DELIMITER);
            fileWriter.append("Total Discount").append(COMMA_DELIMITER).append(totalDiscount.toString());
            fileWriter.append(NEW_LINE_SEPARATOR);

            // Write final total after discount
            Double finalTotal = totalWithoutDiscount - totalDiscount;
            fileWriter.append(COMMA_DELIMITER).append(COMMA_DELIMITER).append(COMMA_DELIMITER).append(COMMA_DELIMITER);
            fileWriter.append("Total After Discount").append(COMMA_DELIMITER).append(finalTotal.toString());
*/
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
