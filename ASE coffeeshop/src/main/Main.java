package main;

import fileRead.MenuFileRead;
import fileRead.OrdersFileRead;
import model.CustomerQueue;
import exception.InvalidMenuFileReadException;
import exception.InvalidOrdersFileReadException;

import javax.swing.SwingUtilities;
import view.CoffeeShopGUI;

public class Main {
    // Data management
    private static String menuFileName = "ASE coffeeshop/src/Menu.csv";
    private static String ordersFileName = "ASE coffeeshop/src/Orders.csv";
    private static MenuFileRead menuFileReader;
    private static OrdersFileRead ordersFileReader;
    private static CustomerQueue customerQueue;
    private static Basket basket;

    public static void main(String[] args) throws InvalidMenuFileReadException, InvalidOrdersFileReadException {
        // Initialize file management
        menuFileReader = new MenuFileRead();
        ordersFileReader = new OrdersFileRead();
        customerQueue = CustomerQueue.getInstance();
        
        menuFileReader.readCSVAndStoreData(menuFileName);
        ordersFileReader.readCSVAndStoreData(ordersFileName);
        customerQueue.addExistingCustomer();
        
        basket = new Basket();
        
        // Launch GUI
        SwingUtilities.invokeLater(() -> new CoffeeShopGUI(menuFileReader, basket).setVisible(true));
    }
}
