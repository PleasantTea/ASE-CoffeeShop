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
				//Reading the csv file
				menuItemsHashMap = new LinkedHashMap<>();
	            br = new BufferedReader(new FileReader(fileName));
	            //Use Delimiter as COMMA
	            String line = "";
	            //Read to skip the header
	            if(br!=null) {
	            	br.readLine();
	            }
	            
	            //Reading from the second line
	            while ((line = br.readLine()) != null){
	            	
	            	String[] menuDetails = line.split(COMMA_DELIMITER);
	            	
	            	// Checking data integrity
	                if (menuDetails.length < 5) {
	                    throw new InvalidMenuFileReadException("Invalid row: missing columns in CSV file.");
	                }
	                String itemId = menuDetails[0].trim();
	                String itemName = menuDetails[1].trim();
	                String priceStr = menuDetails[2].trim();
	                String category = menuDetails[3].trim();
	                String description = menuDetails[4].trim();
	                
	                // Verify Item ID
	                if (itemId.isEmpty()) {
	                    throw new InvalidMenuFileReadException("Invalid Menu Item: Missing Item ID.");
	                }
	                
	                // Calibration Price
	                float price;
	                try {
	                    price = Float.parseFloat(priceStr);
	                    if (price < 0) {
	                        throw new InvalidMenuFileReadException("Invalid price for item: " + itemName);
	                    }
	                } catch (NumberFormatException e) {
	                    throw new InvalidMenuFileReadException("Invalid price format for item: " + itemName, e);
	                }

	                // Check Type & Name
	                if (itemName.isEmpty() || category.isEmpty()) {
	                    throw new InvalidMenuFileReadException("Invalid Menu Item: Item Name or Category missing.");
	                }
	          
            	
                	// Stored in HashMap, Item ID as key
	                // Create Menu object and set fields
                    //menuItemsHashMap.put(item.getItemId(), item);
	                MenuItem item = new MenuItem();
	                item.setItemId(itemId);             // Set Item ID
	                item.setItemName(itemName);	        // Set Item Name
	                item.setPrice(price);               // Set Price
	                item.setCategory(category);         // Set Category
	                item.setDescription(description);   // Set Item Description

	                menuItemsHashMap.put(itemId, item);
	            } 
	    }
        catch(IOException ee)
        {
            //ee.printStackTrace();
        	throw new InvalidMenuFileReadException("Error reading the CSV file: " + fileName, ee);
        }
        finally
        {
            try{
            	if(br!= null) {
                	br.close();
                }
            }
            catch(IOException ie)
            {
                ie.printStackTrace();
            }
        }
	}

	/**
     * Get the set of all unique categories
     * @return HashSet<String> Includes all categories
     */
	public synchronized HashSet<String> getDistinctCategory() {
		distinctCategory = new HashSet<String>();
		for (MenuItem item1 : menuItemsHashMap.values()) {
            distinctCategory.add(item1.getCategory());
        }
		return distinctCategory;
	}
	/**
     * Returns the names of all products in the category according to the selected category.
     * @param selectedCategory Selected category
     * @return ArrayList<String> Contains the names of all products under the category
     */
	public synchronized ArrayList<String> getItemNameListForSelectedCategory(String selectedCategory) {
		ArrayList<String> listOfItems = new ArrayList<String>();
		for (MenuItem item2 : menuItemsHashMap.values()) {
            if (item2.getCategory().equals(selectedCategory)) {
                listOfItems.add(item2.getItemName());
            }
        }
		return listOfItems;
	}
	 /**
     * Get product descriptions for a given category and product name
     * @param selectedCategory Selected category
     * @param item Selected Product Name
     * @return String Product Description
     */
	public synchronized String getDescriptionForSelectedCategoryAndItem(String selectedCategory, String item) {
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
     * @param item Selected Product Name
     * @param description Selected Product Descriptions
     * @return String Item price
     */
	public synchronized String getPriceForSelectedCategoryAndItemAndDescription(String selectedCategory, String item, String description) {
	    for (MenuItem item4 : menuItemsHashMap.values()) {
            if (item4.getCategory().equals(selectedCategory) && item4.getItemName().equals(item) && item4.getDescription().equals(description)) {
                return String.valueOf(item4.getPrice());
            }
        }
	    return ""; // If no match is found, the empty string is returned.
	}
}
