package fileRead;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import exception.InvalidMenuFileReadException;
import main.MenuItem;

public class MenuFileRead {
	public static LinkedHashMap<String, MenuItem> menuItemsHashMap;
	public static HashSet<String> distinctCategory;
	
	//Delimiters used in the CSV file
	private static final String COMMA_DELIMITER = ",";
	
	public  void readCSVAndStoreData(String fileName) throws InvalidMenuFileReadException {
		BufferedReader br = null;
		
		try {
			// Initialise LinkedHashMap to store menu items.
			menuItemsHashMap = new LinkedHashMap<>();
			// Reading CSV files.
            br = new BufferedReader(new FileReader(fileName));
            // Use Delimiter as COMMA
            String line = "";
            // Read to skip the header(first line).
            if(br!=null) {
            	br.readLine();
            }
            
            // Starting from the second line, read the contents of the CSV file line by line.
            while ((line = br.readLine()) != null){
            	if (line.trim().isEmpty() || line.replaceAll(",", "").trim().isEmpty()) {
                    continue;   // Skip completely empty lines
                }
            	
            	// Splitting data with comma separators
            	String[] menuDetails = line.split(COMMA_DELIMITER);
            	
            	// Checking data integrity
                if (menuDetails.length < 5) {
                    throw new InvalidMenuFileReadException("Invalid row, missing columns in CSV file.");
                }
                // Get the individual fields and remove the first and last spaces
                String itemId = menuDetails[0].trim();
                String itemName = menuDetails[1].trim();
                String priceStr = menuDetails[2].trim();
                String category = menuDetails[3].trim();
                String description = menuDetails[4].trim();
                
                // Verify Item ID
                if (itemId.isEmpty() || itemId.matches(".*[@#$&*!/|%`^()~?+=-].*") || itemId.matches("\\d+")) {
                	throw new InvalidMenuFileReadException("Invalid itemId in Menu Item -> " + itemId);
            	}
            
                // Verify Item Name
                if (itemName.isEmpty() || itemName.matches(".*[@#$&*!/|%`^()~?+=-].*") || itemName.matches("\\d+")) {
                    throw new InvalidMenuFileReadException("Invalid itemName in Menu Item -> " + itemName);
                }
                
                // Verify Price
                float price;
                try {
                    price = Float.parseFloat(priceStr);
                    if (price < 0) {
                        throw new InvalidMenuFileReadException("Invalid price for item: " + priceStr);
                    }
                } catch (NumberFormatException e) {
                    throw new InvalidMenuFileReadException("Invalid price format for item: " + priceStr, e);
                }
                
                // Verify Category
                if (category.isEmpty() || category.matches(".*[@#$&*!/|%`^()~?+=-].*") || category.matches("\\d+")) {
                    throw new InvalidMenuFileReadException("Invalid category in Menu Item -> " + category);
                }
                
                // Verify Description
                if (description.isEmpty() || description.matches(".*[@#$&*!/|%`^()~?+=-].*") || description.matches("\\d+")) {
                    throw new InvalidMenuFileReadException("Invalid description in Menu Item -> " + description);
                }
            	
                // Create a new MenuItem object and set its properties.
                MenuItem item = new MenuItem();
                item.setItemId(itemId);             // Set Item ID
                item.setItemName(itemName);	        // Set Item Name
                item.setPrice(price);               // Set Price
                item.setCategory(category);         // Set Category
                item.setDescription(description);   // Set Item Description
                
                // Stored in LinkedHashMap, itemId as key
                menuItemsHashMap.put(itemId, item);
            } 
	    }
        catch(IOException ee){
        	// Catch IO-related exceptions and throw custom exceptions.
        	throw new InvalidMenuFileReadException("Error reading the CSV file: " + fileName, ee);
        }
        finally{
            try{
            	// Close the BufferedReader to free up resources.
            	if(br!= null) {
                	br.close();
                }
            }
            catch(IOException ie){
                ie.printStackTrace();
            }
        }
	}

	/**
     * Get the set of all unique categories
     * @return HashSet<String> Includes all categories
     */
	public HashSet<String> getDistinctCategory() {
		distinctCategory = new HashSet<String>();
		for (MenuItem item1 : menuItemsHashMap.values()) {
            distinctCategory.add(item1.getCategory());
        }
		return distinctCategory;
	}
	/**
     * Returns the names of all items in the category according to the selected category.
     * @param selectedCategory Selected category
     * @return ArrayList<String> Contains the names of all items under the category
     */
	public ArrayList<String> getItemNameListForSelectedCategory(String selectedCategory) {
		ArrayList<String> listOfItems = new ArrayList<String>();
		for (MenuItem item2 : menuItemsHashMap.values()) {
            if (item2.getCategory().equals(selectedCategory)) {
                listOfItems.add(item2.getItemName());
            }
        }
		return listOfItems;
	}
	 /**
     * Get product descriptions for a given category and item name
     * @param selectedCategory Selected category
     * @param item Selected itemName
     * @return String item description
     */
	public String getDescriptionForSelectedCategoryAndItem(String selectedCategory, String item) {
	    for (MenuItem item3 : menuItemsHashMap.values()) {
            if (item3.getCategory().equals(selectedCategory) && item3.getItemName().equals(item)) {
                return item3.getDescription();  
            }
        }
	    return ""; // If no match is found, the empty string is returned.
	}
	/**
     * Get the price of a product for a specified category, product name and description.
     * @param selectedCategory Selected category
     * @param item Selected itemName
     * @param description Selected item descriptions
     * @return String Item price
     */
	public String getPriceForSelectedCategoryAndItemAndDescription(String selectedCategory, String item, String description) {
	    for (MenuItem item4 : menuItemsHashMap.values()) {
            if (item4.getCategory().equals(selectedCategory) && item4.getItemName().equals(item) && item4.getDescription().equals(description)) {
                return String.valueOf(item4.getPrice());
            }
        }
	    return ""; // If no match is found, the empty string is returned.
	}
}
