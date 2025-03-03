package fileRead;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;//

//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;

import view.CoffeeShopGUI;
import main.MenuItem;

public class MenuFileRead {
	public static LinkedHashMap<String, MenuItem> menuItemsHashMap;
	public static HashSet<String> distinctCategory;
	
	//Delimiters used in the CSV file
	private static final String COMMA_DELIMITER = ",";
	
	public  void readCSVAndStoreData(String filePath){
		BufferedReader br = null;
		
		try {
				//Reading the csv file
				menuItemsHashMap = new LinkedHashMap<>();
	            br = new BufferedReader(new FileReader(filePath));
	            //Use Delimiter as COMMA
	            String line = "";
	            //Read to skip the header
	            if(br!=null) {
	            	br.readLine();	
	            }
	            
	            //Reading from the second line
	            while ((line = br.readLine()) != null){
	            	
	            	String[] menuDetails = line.split(COMMA_DELIMITER);
	                if(menuDetails.length > 0 )
	                {
	                	// Create Menu object and set fields
	                    MenuItem item = new MenuItem();
	                    item.setItemId(menuDetails[0]);      // Set Item ID
	                    item.setItemName(menuDetails[1]);   // Set Item Name
	                    item.setPrice(Float.parseFloat(menuDetails[2]));  // Set Price
	                    item.setCategory(menuDetails[3]);   // Set Category
	                    item.setDescription(menuDetails[4]); // Set Item Description
	                	
	                	// Stored in HashMap, Item ID as key
	                    menuItemsHashMap.put(item.getItemId(), item);
	                }
	            } 
	    }
        catch(Exception ee)
        {
            ee.printStackTrace();
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
