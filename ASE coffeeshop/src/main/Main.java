package main;

import fileRead.MenuFileRead;
import fileRead.OrdersFileRead;
import model.CustomerQueue;
import exception.InvalidMenuFileReadException;
import exception.InvalidOrdersFileReadException;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.util.Locale;

import javax.swing.SwingUtilities;
import view.StateDisplayGUI;
import view.StartGUI;
import controller.StaffController;
import controller.QueueController;

public class Main {
    // Data management
    private static String menuFileName = "/Menu.csv";
    private static String ordersFileName = "/Orders.csv";
    private static MenuFileRead menuFileReader;
    private static OrdersFileRead ordersFileReader;
    private static CustomerQueue customerQueue;

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
        
        // Create data storage directory if it doesn't exist
        File generateDirFile = new File("dataFiles");
        if (!generateDirFile.exists()) {
        	generateDirFile.mkdir();
        }
        
        // Launch GUI
        SwingUtilities.invokeLater(() -> {
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
