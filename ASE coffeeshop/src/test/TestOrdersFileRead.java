package test;

import main.Order;
import org.junit.jupiter.api.*;

import exception.InvalidOrdersFileReadException;

import java.io.*;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;

import fileRead.OrdersFileRead;

import static org.junit.jupiter.api.Assertions.*;

class TestOrdersFileRead {

    private static final String TEST_CSV_PATH = "test_orders.csv";
    private OrdersFileRead ordersFileRead;

    @BeforeEach
    public void setUp() throws IOException, InvalidOrdersFileReadException {
    	// Initialize the OrdersFileRead object.
        ordersFileRead = new OrdersFileRead();

        // Create a test CSV file
        BufferedWriter writer = new BufferedWriter(new FileWriter(TEST_CSV_PATH));
        writer.write("OrderID,CustomerID,ItemID,ItemName,ItemPrice,Timestamp\n");
        writer.write("1,CUS1,COFFEE001,Classic Latte,24,2025/03/01 14:52:05\n");
        writer.write("2,CUS2,DRINKS114,Black Tea,15,2025/03/01 14:53:10\n");
        writer.write("3,CUS3,DESSERT224,Chocolate Mousse,18,2025/03/01 15:10:30\n");
        writer.close();

        // Reading test data and store
        ordersFileRead.readCSVAndStoreData(TEST_CSV_PATH);
    }

    @AfterEach
    public void tearDown() {
        // Deleting Test CSV Files
        File file = new File(TEST_CSV_PATH);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    @DisplayName("Test whether the CSV file is read correctly")
    public void testReadCSVAndStoreData() {
        assertNotNull(OrdersFileRead.existingOrder);
        assertEquals(3, OrdersFileRead.existingOrder.size());

        // Checking existing orders
        Order order = OrdersFileRead.existingOrder.get(1);
        assertNotNull(order);
        assertEquals("CUS1", order.getCustomerID());
        assertEquals("Classic Latte", order.getItemName());
        assertEquals(24, order.getItemPrice());
        
        Order order1 = OrdersFileRead.existingOrder.get(3);
        assertNotNull(order1);
        assertEquals("DESSERT224", order1.getItemID());
        assertEquals("2025/03/01 15:10:30", order1.getCurrTimeAsString());
        assertEquals(LocalDateTime.of(2025, 3, 1, 15, 10, 30), order1.getCurrTime());
    }

    @Test
    @DisplayName("Test to get the latest order number")
    public void testGetLastOrderNumber() {
        Integer lastOrderNumber = OrdersFileRead.getLastOrderNumber();
        assertNotNull(lastOrderNumber);
        assertEquals(3, lastOrderNumber);
    }

    @Test
    @DisplayName("Test to get the latest customer number")
    public void testGetLastCustomerNumber() {
        Integer lastCustomerNumber = OrdersFileRead.getLastCustomerNumber();
        assertNotNull(lastCustomerNumber);
        assertEquals(3, lastCustomerNumber);
    }

    @Test
    @DisplayName("Test saving a new order to existingOrder")
    public void testSaveNewOrdersInExistingOrders() {
        LinkedHashMap<Integer, Order> newOrders = new LinkedHashMap<>();
        Order newOrder = new Order();
        newOrder.setOrderID(4);
        newOrder.setCustomerID("CUS4");
        newOrder.setItemID("SNACK001");
        newOrder.setItemName("French Fries");
        newOrder.setItemPrice(10f);
        newOrder.setCurrTime(LocalDateTime.of(2025, 3, 2, 12, 0, 0));

        newOrders.put(4, newOrder);

        // Performing a save operation
        ordersFileRead.saveNewOrdersInExistingOrders(newOrders);

        // assert
        assertEquals(4, OrdersFileRead.existingOrder.size());
        assertTrue(OrdersFileRead.existingOrder.containsKey(4));
        assertEquals("French Fries", OrdersFileRead.existingOrder.get(4).getItemName());
    }

    @Test
    @DisplayName("Test Write CSV File")
    public void testWriteOrdersToCSV() throws IOException {
        String outputCsvPath = "test_output_orders.csv";
        ordersFileRead.writeOrdersToCSV(outputCsvPath);

        File outputFile = new File(outputCsvPath);
        assertTrue(outputFile.exists());

        BufferedReader reader = new BufferedReader(new FileReader(outputCsvPath));
        String header = reader.readLine();
        assertEquals("OrderID,CustomerID,ItemID,ItemName,ItemPrice,Timestamp", header);

        String firstDataLine = reader.readLine();
        assertNotNull(firstDataLine);

        reader.close();
        outputFile.delete();
    }
}
