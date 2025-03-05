package main;

import fileRead.MenuFileRead;
import fileRead.OrdersFileRead;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.HashSet;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import exception.InvalidMenuFileReadException;
import exception.InvalidOrdersFileReadException;
import main.MenuItem;
import main.Order;
import main.Basket;
import main.DiscountRuler;

public class Main extends JFrame {

    // Data management
    private static String menuFileName = "ASE-CoffeeShop/ASE coffeeshop/src/Menu.csv";
    private static String ordersFileName = "ASE-CoffeeShop/ASE coffeeshop/src/Orders.csv";
    private static String newOrdersFileName = "ASE-CoffeeShop/ASE coffeeshop/src/newOrders.csv";
    private static MenuFileRead menuFileReader;
    private static OrdersFileRead ordersFileReader;
    private static MenuItem currentListSelection;
    private static LinkedHashMap<Integer, Order> currentOrders;
    private static Basket basket;

    // GUI components
    private JButton buttonAdd, buttonRemove, buttonConfirm, buttonQuit, buttonCancel, buttonReport;
    private JList<MenuItem> orderList;
    private JScrollPane orderPane;
    private static JLabel discount, total;

    private JPanel menuPanel;
    private JButton foodButton, drinkButton, memButton;
    private JList<MenuItem> productList;
    private JScrollPane productScrollPane;

    public static void main(String[] args) throws InvalidMenuFileReadException, InvalidOrdersFileReadException {
        // Initialize file management
        menuFileReader = new MenuFileRead();
        ordersFileReader = new OrdersFileRead();

        menuFileReader.readCSVAndStoreData(menuFileName);
        ordersFileReader.readCSVAndStoreData(ordersFileName);
      
        basket = new Basket();
        currentOrders = new LinkedHashMap<>();

        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }

    public Main() {
        createView();
        initBtnActions();
        setTitle("ASE Coffee Shop");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(700, 450);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    /** GUI Code */
    private void createView() {
        JPanel panelMain = new JPanel();
        getContentPane().add(panelMain);

        JPanel panelForm = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        panelMain.add(panelForm);

        /** Menu Panel */
        menuPanel = new JPanel();
        menuPanel.setPreferredSize(new Dimension(275, 275));
        menuPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Menu",
                TitledBorder.LEFT, TitledBorder.TOP));
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(0, 0, 10, 0);
        c.gridheight = 6;
        c.gridx = 0;
        c.gridy = 0;
        panelForm.add(menuPanel, c);
        c.insets = new Insets(0, 0, 0, 0);

        // Category button
        foodButton = new JButton("Coffee");
        drinkButton = new JButton("Drinks");
        memButton = new JButton("Dessert");
        menuPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        menuPanel.add(foodButton);
        menuPanel.add(drinkButton);
        menuPanel.add(memButton);

        // Product List
        productList = new JList<>();
        productScrollPane = new JScrollPane(productList);
        productScrollPane.setPreferredSize(new Dimension(250, 200));
        menuPanel.add(productScrollPane);

        // Add button listener
        foodButton.addActionListener(e -> updateProductList("Coffee"));
        drinkButton.addActionListener(e -> updateProductList("Drinks"));
        memButton.addActionListener(e -> updateProductList("Dessert"));

        /** Order List */
        orderList = new JList<>();
        orderPane = new JScrollPane(orderList);
        orderList.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        orderList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                currentListSelection = orderList.getSelectedValue();
            }
        });

        orderPane.setPreferredSize(new Dimension(300, 200));
        orderPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Order",
                TitledBorder.LEFT, TitledBorder.TOP));
        c.anchor = GridBagConstraints.PAGE_START;
        c.insets = new Insets(0, 15, 0, 0);
        c.gridheight = 3;
        c.gridx = 3;
        c.gridy = 0;
        panelForm.add(orderPane, c);
        c.insets = new Insets(0, 0, 0, 0);

        /**DISCOUNT JLABLE*/
		discount = new JLabel("Discounts: ");
		c.gridx = 3;
		c.gridy = 3;
		c.anchor = GridBagConstraints.LINE_START;
		c.insets = new Insets(0, 15, 10, 0);
		panelForm.add(discount, c);
		c.insets = new Insets(0, 0, 0, 0);
		
		/**TOTAL JLABLE*/
		total = new JLabel("Total: ");
		c.gridx = 3;
		c.gridy = 3;
		c.anchor = GridBagConstraints.LAST_LINE_START;
		c.insets = new Insets(0, 15, 10, 0);
		panelForm.add(total, c);
		c.insets = new Insets(0, 0, 0, 0);

        /**Add BUTTON*/
		buttonAdd = new JButton("Add to Order");
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 6;
		panelForm.add(buttonAdd, c);
		c.fill = GridBagConstraints.NONE;

        /**REMOVE BUTTON*/
		buttonRemove = new JButton("     Remove Item     ");
		c.gridx = 3;
		c.gridy = 6;
		c.anchor = GridBagConstraints.LINE_START;
		c.insets = new Insets(0, 15, 0, 0);
		panelForm.add(buttonRemove, c);
		c.insets = new Insets(0, 0, 0, 0);
		
		/**CANCEL BUTTON*/
		buttonCancel = new JButton("Cancel Order");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.LAST_LINE_START;
		c.gridx = 0;
		c.gridy = 9;
		panelForm.add(buttonCancel, c);
		c.fill = GridBagConstraints.NONE;

		/** REPORT BUTTON */
		buttonReport = new JButton("     Show Report      ");
		c.gridx = 3; 
		c.gridy = 9;
		c.insets = new Insets(0, 15, 0, 0);
		panelForm.add(buttonReport, c);
		c.insets = new Insets(0, 0, 0, 0);

		/**CONFIRM BUTTON*/
		buttonConfirm = new JButton("     Confirm Order     ");
		c.anchor = GridBagConstraints.LINE_END;
		c.gridx = 3;
		c.gridy = 6;
		panelForm.add(buttonConfirm, c);

		/**QUIT BUTTON*/
		buttonQuit = new JButton("   Quit   ");
		c.gridx = 3;
		c.gridy = 9;
		c.insets = new Insets(35, 0, 0, 0);
		c.anchor = GridBagConstraints.LAST_LINE_END;
		panelForm.add(buttonQuit, c);
    }

    private void initBtnActions() {
        buttonAdd.addActionListener(e -> {
            MenuItem selectedProduct = productList.getSelectedValue();
            if (selectedProduct == null) {
                JOptionPane.showMessageDialog(null, "Please select a product");
                return;
            }
            
            basket.addItem(selectedProduct);    // Add products to Basket
            displayBasket();    // Update Order Display
            setPrice(); // Set discount and total price
        });
        
        buttonRemove.addActionListener(e -> {
            MenuItem selectedItem = orderList.getSelectedValue();
            if (selectedItem == null) {
                JOptionPane.showMessageDialog(null, "Please Select an Item to Remove");
                return;
            }

            basket.removeItem(selectedItem);    // Remove products from Basket
            displayBasket();
            setPrice();
        });
        
        buttonConfirm.addActionListener(e -> {
            if (!basket.getItems().isEmpty()) {
                basket.confirmOrder();
                JOptionPane.showMessageDialog(null, "Order Confirmed");
                displayBasket();
                setPrice();
            } else {
                JOptionPane.showMessageDialog(null, "Your basket is empty. Add items before confirming.");
            }
        });
        
        buttonCancel.addActionListener(e -> {   
            basket.clearBasket();   // Clear Basket
            displayBasket();
            setPrice();
            JOptionPane.showMessageDialog(null, "Order has been successfully cancelled");
        });        

        buttonReport.addActionListener(e -> {
            ReportGenerator.getInstance().generateReport();
            JOptionPane.showMessageDialog(null, "Report generated successfully!");
        });

        buttonQuit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Exit the program 
            }
        });
        
    }
    

    
    private void updateProductList(String category) {
        ArrayList<String> itemNames = menuFileReader.getItemNameListForSelectedCategory(category);
        DefaultListModel<MenuItem> model = new DefaultListModel<>(); // Store MenuItem object 
        
        for (String name : itemNames) {
            for (MenuItem item : menuFileReader.menuItemsHashMap.values()) {
                if (item.getItemName().equals(name)) {
                    model.addElement(item); // Add MenuItem object
                }
            }
        }
        productList.setModel(model); // JList will call the toString method of MenuItem
    }
    
    private void displayBasket() {
        ArrayList<MenuItem> itemsInBasket = basket.getItems();
        DefaultListModel<MenuItem> model = new DefaultListModel<>(); // JList stores MenuItem objects
        
        for (MenuItem item : itemsInBasket) {
            model.addElement(item); // Products are added to the model, JList will automatically call toString()
        }
        orderList.setModel(model); // Refresh GUI display
    }

    private void setPrice() {
        // Get the product list from all orders
        ArrayList<MenuItem> itemsInBasket = basket.getItems();
    
        // Format output
        DecimalFormat df = new DecimalFormat("#.##");
        discount.setText("Discount: -£" + df.format(Basket.calculateTotalPrice(itemsInBasket)-Basket.calculateDiscountedTotal(itemsInBasket)));
        total.setText("Total: £" + df.format(Basket.calculateDiscountedTotal(itemsInBasket)));
    }
    
}
