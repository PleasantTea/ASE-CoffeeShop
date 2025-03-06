package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import exception.InvalidMenuFileReadException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import fileRead.MenuFileRead;

public class TestMenuFileRead {
	private MenuFileRead menuFileRead;
    private static final String TEST_CSV_PATH = "src/test/resources/test_menu.csv";

    
    @BeforeEach
    void setUp() {
        menuFileRead = new MenuFileRead();
    } 
    
    /**
     * Creates a temporary CSV test file for testing the "readCSVAndStoreData()" method.
     * This file mimics the real CSV format that the method is expected to read.
     * @throws IOException If an error occurs while creating or writing to the file.
     */
    private void createTestCSV() throws IOException {
        File file = new File(TEST_CSV_PATH);
        file.getParentFile().mkdirs();   // Make sure the directory exists
        
        // Write the CSV header
        FileWriter writer = new FileWriter(file);
        writer.write("ItemId,ItemName,Price,Category,Description\n");
        
        // Write test data rows
        writer.write("COFFEE003,Americano,18.0,Coffee,Refreshing black coffee\n");
        writer.write("COFFEE006,Coconut Latte,23.0,Coffee,Fusion of espresso and raw coconut milk\n");
        writer.write("DRINKS116,Orange Juice,12.0,Drinks,Freshly squeezed orange juice\n");
        writer.write("DESSERT224,Chocolate Mousse,15.0,Dessert,Chocolate meets light mousse\n");
        writer.flush();
        writer.close();
    }
    
    /**
     * Deletes the temporary CSV test file after test execution.
     * This helps remain the test environment clean.
     */
    private void deleteTestCSV() {
        File file = new File(TEST_CSV_PATH);
        if (file.exists()) {
            file.delete();
        }
    }
    
    /**
     * Test that whether the "readCSVAndStoreData()" method can read the CSV test file correctly.
     * @throws IOException If an error occurs while creating CSV test file.
     * @throws InvalidMenuFileReadException If an error occurs while reading and parsing the CSV file.
     */
    @Test
    void testReadCSVAndStoreData() throws IOException, InvalidMenuFileReadException {
    	// Create a CSV test file.
    	createTestCSV();
    	
    	// Call the method to read the CSV file.
        menuFileRead.readCSVAndStoreData(TEST_CSV_PATH);
        // Verify that the correct number of items were loaded into the HashMap.
        assertEquals(4, MenuFileRead.menuItemsHashMap.size());   
        
        // Validation of specific data
        assertTrue(MenuFileRead.menuItemsHashMap.containsKey("COFFEE003"));
        assertTrue(MenuFileRead.menuItemsHashMap.containsKey("DRINKS116"));
        assertEquals("Americano", MenuFileRead.menuItemsHashMap.get("COFFEE003").getItemName());
        assertEquals("Orange Juice", MenuFileRead.menuItemsHashMap.get("DRINKS116").getItemName());
        assertEquals(18.0f, MenuFileRead.menuItemsHashMap.get("COFFEE003").getPrice());
        assertEquals(12.0f, MenuFileRead.menuItemsHashMap.get("DRINKS116").getPrice());
        assertEquals("Coffee", MenuFileRead.menuItemsHashMap.get("COFFEE003").getCategory());
        assertEquals("Drinks", MenuFileRead.menuItemsHashMap.get("DRINKS116").getCategory());
        assertEquals("Refreshing black coffee", MenuFileRead.menuItemsHashMap.get("COFFEE003").getDescription());
        assertEquals("Freshly squeezed orange juice", MenuFileRead.menuItemsHashMap.get("DRINKS116").getDescription());
        
        // Delete test CSV files to keep the test environment clean.
        deleteTestCSV();
    }
    
    /**
     * Test whether the "getDistinctCategory()" method can correctly return a collection of categories.
     */
    @Test
    void testGetDistinctCategory() {
        HashSet<String> expectedCategories = new HashSet<>();
        expectedCategories.add("Coffee");
        expectedCategories.add("Drinks");
        expectedCategories.add("Dessert");
        assertEquals(expectedCategories, menuFileRead.getDistinctCategory());
    }

    /**
     * Test whether the "getItemNameListForSelectedCategory()" method can return the correct list of item names.
     */
    @Test
    void testGetItemNameListForSelectedCategory() {
        ArrayList<String> expectedCoffeeItems = new ArrayList<>();
        expectedCoffeeItems.add("Americano");
        expectedCoffeeItems.add("Coconut Latte");
        assertEquals(expectedCoffeeItems, menuFileRead.getItemNameListForSelectedCategory("Coffee"));

        ArrayList<String> expectedDrinksItems = new ArrayList<>();
        expectedDrinksItems.add("Orange Juice");
        assertEquals(expectedDrinksItems, menuFileRead.getItemNameListForSelectedCategory("Drinks"));
        
        ArrayList<String> expectedDessertItems = new ArrayList<>();
        expectedDessertItems.add("Chocolate Mousse");
        assertEquals(expectedDessertItems, menuFileRead.getItemNameListForSelectedCategory("Dessert"));
    }

    /**
     * Test the "getDescriptionForSelectedCategoryAndItem()" method.
     */
    @Test
    void testGetDescriptionForSelectedCategoryAndItem() {
        assertEquals("Refreshing black coffee", menuFileRead.getDescriptionForSelectedCategoryAndItem("Coffee", "Americano"));
        assertEquals("Freshly squeezed orange juice", menuFileRead.getDescriptionForSelectedCategoryAndItem("Drinks", "Orange Juice"));
        assertEquals("Chocolate meets light mousse", menuFileRead.getDescriptionForSelectedCategoryAndItem("Dessert", "Chocolate Mousse"));
        assertEquals("", menuFileRead.getDescriptionForSelectedCategoryAndItem("Coffee", "Mocha")); // Non-existent items should return ""
    }

    /**
     * Test the "getPriceForSelectedCategoryAndItemAndDescription()" method.
     */
    @Test
    void testGetPriceForSelectedCategoryAndItemAndDescription() {
        assertEquals("18.0", menuFileRead.getPriceForSelectedCategoryAndItemAndDescription("Coffee", "Americano", "Refreshing black coffee"));
        assertEquals("12.0", menuFileRead.getPriceForSelectedCategoryAndItemAndDescription("Drinks", "Orange Juice", "Freshly squeezed orange juice"));
        assertEquals("15.0", menuFileRead.getPriceForSelectedCategoryAndItemAndDescription("Dessert", "Chocolate Mousse", "Chocolate meets light mousse"));
        assertEquals("", menuFileRead.getPriceForSelectedCategoryAndItemAndDescription("Coffee", "Mocha", "Chocolate coffee")); // Non-existent items should return ""
    }
}
