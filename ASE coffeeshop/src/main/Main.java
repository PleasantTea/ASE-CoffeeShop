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
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import exception.InvalidMenuFileReadException;
import exception.InvalidOrdersFileReadException;

public class Main extends JFrame {

    // Data management
    private static String menuFileName = "ASE coffeeshop/src/Menu.csv";
    private static String ordersFileName = "ASE coffeeshop/src/Orders.csv";
    private static String newOrdersFileName = "ASE coffeeshop/src/newOrders.csv";
    private static MenuFileRead menuFileReader;
    private static OrdersFileRead ordersFileReader;
    private static MenuItem currentListSelection;
    private static Basket basket;
    private boolean isReportGenerated = false;

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
        GridBagConstraints p = new GridBagConstraints();
        panelMain.add(panelForm);

        /** Menu Panel */
        menuPanel = new JPanel();
        menuPanel.setPreferredSize(new Dimension(275, 275));
        menuPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Menu",
                TitledBorder.LEFT, TitledBorder.TOP));
        p.anchor = GridBagConstraints.LINE_START;
        p.insets = new Insets(0, 0, 10, 0);
        p.gridheight = 6;
        p.gridx = 0;
        p.gridy = 0;
        panelForm.add(menuPanel, p);
        p.insets = new Insets(0, 0, 0, 0);

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
        p.anchor = GridBagConstraints.PAGE_START;
        p.insets = new Insets(0, 15, 0, 0);
        p.gridheight = 3;
        p.gridx = 3;
        p.gridy = 0;
        panelForm.add(orderPane, p);
        p.insets = new Insets(0, 0, 0, 0);

        /**DISCOUNT JLABLE*/
		discount = new JLabel("Discounts: ");
		p.gridx = 3;
		p.gridy = 3;
		p.anchor = GridBagConstraints.LINE_START;
		p.insets = new Insets(0, 15, 10, 0);
		panelForm.add(discount, p);
		p.insets = new Insets(0, 0, 0, 0);
		
		/**TOTAL JLABLE*/
		total = new JLabel("Total: ");
		p.gridx = 3;
		p.gridy = 3;
		p.anchor = GridBagConstraints.LAST_LINE_START;
		p.insets = new Insets(0, 15, 10, 0);
		panelForm.add(total, p);
		p.insets = new Insets(0, 0, 0, 0);

        /**Add BUTTON*/
		buttonAdd = new JButton("Add to Order");
		p.anchor = GridBagConstraints.LINE_START;
		p.fill = GridBagConstraints.HORIZONTAL;
		p.gridx = 0;
		p.gridy = 6;
		panelForm.add(buttonAdd, p);
		p.fill = GridBagConstraints.NONE;

        /**REMOVE BUTTON*/
		buttonRemove = new JButton("     Remove Item     ");
		p.gridx = 3;
		p.gridy = 6;
		p.anchor = GridBagConstraints.LINE_START;
		p.insets = new Insets(0, 15, 0, 0);
		panelForm.add(buttonRemove, p);
		p.insets = new Insets(0, 0, 0, 0);
		
		/**CANCEL BUTTON*/
		buttonCancel = new JButton("Cancel Order");
		p.fill = GridBagConstraints.HORIZONTAL;
		p.anchor = GridBagConstraints.LAST_LINE_START;
		p.gridx = 0;
		p.gridy = 9;
		panelForm.add(buttonCancel, p);
		p.fill = GridBagConstraints.NONE;

		/** REPORT BUTTON */
		buttonReport = new JButton("     Show Report      ");
		p.gridx = 3; 
		p.gridy = 9;
		p.insets = new Insets(0, 15, 0, 0);
		panelForm.add(buttonReport, p);
		p.insets = new Insets(0, 0, 0, 0);

		/**CONFIRM BUTTON*/
		buttonConfirm = new JButton("     Confirm Order     ");
		p.anchor = GridBagConstraints.LINE_END;
		p.gridx = 3;
		p.gridy = 6;
		panelForm.add(buttonConfirm, p);

		/**QUIT BUTTON*/
		buttonQuit = new JButton("   Quit   ");
		p.gridx = 3;
		p.gridy = 9;
		p.insets = new Insets(35, 0, 0, 0);
		p.anchor = GridBagConstraints.LAST_LINE_END;
		panelForm.add(buttonQuit, p);
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
            isReportGenerated = true; 
            JOptionPane.showMessageDialog(null, "Report generated successfully!");
        });

        buttonQuit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Check if the report has been generated
                if (!isReportGenerated) {
                    // If the report is not generated, prompt the user
                    JOptionPane.showMessageDialog(null, "Report has not been generated yet. Please generate the report before quitting.");
                } else {
                    System.exit(0);
                }
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
