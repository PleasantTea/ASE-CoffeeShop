package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import exception.InvalidOrdersFileReadException;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import fileRead.OrdersFileRead;
import main.Basket;
import main.MenuItem;
import main.Order;

class TestBasket {
    private Basket basket;

    @BeforeEach
    public void setUp() {
        basket = new Basket();
    }

    @Test
    @DisplayName("Test to see if the item was added to the basket correctly")
    public void testAddItem() {
        MenuItem item = new MenuItem();
        item.setItemId("COFFEE001");
        item.setItemName("Classic Latte");
        item.setPrice(24f);
        item.setCategory("Coffee");

        basket.addItem(item);
        assertEquals(1, basket.getItems().size());
        assertTrue(basket.getItems().contains(item));
    }

    @Test
    @DisplayName("Test to see if the item was removed from the basket correctly")
    public void testRemoveItem() {
        MenuItem item = new MenuItem();
        item.setItemId("COFFEE001");
        item.setItemName("Classic Latte");
        item.setPrice(24f);
        item.setCategory("Coffee");

        basket.addItem(item);
        basket.removeItem(item);
        assertEquals(0, basket.getItems().size());
    }

    @Test
    @DisplayName("Test to see if the basket was cleared")
    public void testClearBasket() {
        MenuItem item1 = new MenuItem();
        item1.setItemId("COFFEE001");
        item1.setItemName("Classic Latte");
        item1.setPrice(24f);
        item1.setCategory("Coffee");

        MenuItem item2 = new MenuItem();
        item2.setItemId("DRINKS114");
        item2.setItemName("Black Tea");
        item2.setPrice(15f);
        item2.setCategory("Drinks");

        basket.addItem(item1);
        basket.addItem(item2);
        basket.clearBasket();
        assertEquals(0, basket.getItems().size());
    }

    @Test
    @DisplayName("Test to see if the total price calculation is crrect")
    public void testCalculateTotalPrice() {
        ArrayList<MenuItem> items = new ArrayList<>();

        // Test 1: Empty Shopping Cart
        assertEquals(0, Basket.calculateTotalPrice(items));

        // Test 2: Individual item
        MenuItem item1 = new MenuItem();
        item1.setItemId("COFFEE001");
        item1.setItemName("Classic Latte");
        item1.setPrice(24f);
        item1.setCategory("Coffee");
        items.add(item1);
        assertEquals(24, Basket.calculateTotalPrice(items));

        // Test 3: Multiple items
        MenuItem item2 = new MenuItem();
        item2.setItemId("DRINKS114");
        item2.setItemName("Black Tea");
        item2.setPrice(15f);
        item2.setCategory("Drinks");
        items.add(item2);
        assertEquals(39, Basket.calculateTotalPrice(items));

        // Test 4: Item prices cannot be negative
        MenuItem item3 = new MenuItem();
        item3.setItemId("DESSERT224");
        item3.setItemName("Chocolate Mousse");
        item3.setPrice(-5f); // Negative test
        item3.setCategory("Dessert");
        items.add(item3);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            Basket.calculateTotalPrice(items);
        });

        assertTrue(exception.getMessage().contains("The item price cannot be negative"));
    }

    @Test
    @DisplayName("Test to confirm order: no order when cart is empty, order correctly added and cart emptied after confirmation")
    public void testConfirmOrder() throws InvalidOrdersFileReadException {
        // Test order confirmation when shopping cart is empty
        basket.confirmOrder();
        assertEquals(0, basket.getItems().size()); // Shopping cart is empty, order will not be added

        // Add item
        MenuItem item = new MenuItem();
        item.setItemId("COFFEE001");
        item.setItemName("Classic Latte");
        item.setPrice(24f);
        item.setCategory("Coffee");
        basket.addItem(item);

        MenuItem item2 = new MenuItem();
        item2.setItemId("DRINKS114");
        item2.setItemName("Black Tea");
        item2.setPrice(15f);
        item2.setCategory("Drinks");
        basket.addItem(item2);

        // Simulation of OrdersFileRead
        OrdersFileRead existingOrders = new OrdersFileRead();
        existingOrders.readCSVAndStoreData("ASE coffeeshop/src/Orders.csv"); // Read the order file first
        Integer lastOrderIDBefore = OrdersFileRead.getLastOrderNumber();

        // Confirmation of orders
        basket.confirmOrder();
        
        // Checks for a successful write to existingOrder
        LinkedHashMap<Integer, Order> existingOrder = OrdersFileRead.existingOrder;

        // Get the ID of the newly generated order
        Integer lastOrderID = OrdersFileRead.getLastOrderNumber();
        assertNotNull(lastOrderID);
        assertTrue(lastOrderID > 0);
        assertEquals(lastOrderIDBefore, lastOrderID-2);

        // Check if the new order exists in existingOrder
        Order newOrder1 = existingOrder.get(lastOrderID - 1); 
        Order newOrder2 = existingOrder.get(lastOrderID);     

        assertNotNull(newOrder1);
        assertNotNull(newOrder2);

        // Verify order contents
        assertEquals("COFFEE001", newOrder1.getItemID());
        assertEquals("Classic Latte", newOrder1.getItemName());
        assertEquals(24f, newOrder1.getItemPrice(), 0.001);

        assertEquals("DRINKS114", newOrder2.getItemID());
        assertEquals("Black Tea", newOrder2.getItemName());
        assertEquals(15f, newOrder2.getItemPrice(), 0.001);
        
        // Check if shopping cart is emptied
        assertEquals(0, basket.getItems().size());
    }
}
