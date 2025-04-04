package main;

import fileRead.MenuFileRead;
import fileRead.OrdersFileRead;
import model.CustomerQueue;
import exception.InvalidMenuFileReadException;
import exception.InvalidOrdersFileReadException;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Locale;

import javax.swing.SwingUtilities;
import view.CoffeeShopGUI;
import view.StateDisplayGUI;
import view.StartGUI;
import controller.StaffController;
import controller.QueueController;

public class Main {
    // Data management
    private static String menuFileName = "ASE coffeeshop/src/Menu.csv";
    private static String ordersFileName = "ASE coffeeshop/src/Orders.csv";
    private static MenuFileRead menuFileReader;
    private static OrdersFileRead ordersFileReader;
    private static CustomerQueue customerQueue;
    private static Basket basket;

    private static QueueController queueController;

    public static void main(String[] args) throws InvalidMenuFileReadException, InvalidOrdersFileReadException {
        Locale.setDefault(Locale.ENGLISH); // Set the default language to English
        // Initialize file management
        menuFileReader = new MenuFileRead();
        ordersFileReader = new OrdersFileRead();
        customerQueue = CustomerQueue.getInstance();
        
        menuFileReader.readCSVAndStoreData(menuFileName);
        ordersFileReader.readCSVAndStoreData(ordersFileName);
        customerQueue.addExistingCustomer();
        
        basket = new Basket();
        
        // Launch GUI
        //SwingUtilities.invokeLater(() -> new CoffeeShopGUI(menuFileReader, basket).setVisible(true));
        SwingUtilities.invokeLater(() -> {
            //new CoffeeShopGUI(menuFileReader, basket).setVisible(true);
            //new StateDisplayGUI();  // 这行是新加的
            
            //CoffeeShopGUI coffeeGUI = new CoffeeShopGUI(menuFileReader, basket, orderType, startGUI);
            StartGUI startGUI = new StartGUI();
            StateDisplayGUI stateGUI = new StateDisplayGUI();

            // Get screen size
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int screenWidth = screenSize.width;
            int screenHeight = screenSize.height;

            // Set startGUI on the left side of the screen            
            startGUI.setLocation(100, (screenHeight - startGUI.getHeight()) / 2);
            startGUI.setVisible(true);

            // Set stateGUI on the right side of the screen
            stateGUI.setLocation(screenWidth - stateGUI.getWidth() - 100, (screenHeight - stateGUI.getHeight()) / 2);
            stateGUI.setVisible(true);

            queueController = new QueueController(customerQueue, stateGUI);
            new Thread(queueController).start();

            StaffController staffController = new StaffController(customerQueue, stateGUI);
            staffController.startInitialStaff();
            stateGUI.setStaffController(staffController);
        });
    }
}
