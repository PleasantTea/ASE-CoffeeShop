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
import main.MenuItem;
import main.Order;

public class main extends JFrame {

    // 数据管理
    private static String menuFileName = "ASE coffeeshop/src/Menu.csv";
    private static String ordersFileName = "ASE coffeeshop/src/Orders.csv";
    private static MenuFileRead menuFileReader;
    private static OrdersFileRead ordersFileReader;
    private static MenuItem currentListSelection;
    private static LinkedHashMap<Integer, Order> currentOrders;

    // GUI 组件
    private JButton buttonAdd, buttonRemove, buttonConfirm, buttonQuit, buttonCancel, buttonReport;
    private JList<MenuItem> orderList;
    private JScrollPane orderPane;
    private static JLabel discount, total;

    private JPanel menuPanel;
    private JButton foodButton, drinkButton, memButton;
    private JList<MenuItem> productList;
    private JScrollPane productScrollPane;

    public static void main(String[] args) {
        // 初始化文件管理
        menuFileReader = new MenuFileRead();
        ordersFileReader = new OrdersFileRead();

        menuFileReader.readCSVAndStoreData(menuFileName);
        ordersFileReader.readCSVAndStoreData(ordersFileName);

        

        System.out.println("Menu items loaded:");
        if (menuFileReader.menuItemsHashMap.isEmpty()) {    //看这里读取数据对不对
            System.out.println("Error: No menu items loaded. Check CSV file path and format.");
        } else {
            for (MenuItem item : menuFileReader.menuItemsHashMap.values()) {
                System.out.println(item);
            }
        }

        currentOrders = new LinkedHashMap<>();

        SwingUtilities.invokeLater(() -> new main().setVisible(true));
    }

    public main() {
        createView();
        initBtnActions();
        setTitle("ASE Coffee Shop");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(700, 450);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    /** GUI 代码 */
    private void createView() {
        JPanel panelMain = new JPanel();
        getContentPane().add(panelMain);

        JPanel panelForm = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        panelMain.add(panelForm);

        /** 菜单面板 */
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

        // 按钮
        foodButton = new JButton("Coffee");
        drinkButton = new JButton("Drinks");
        memButton = new JButton("Dessert");
        menuPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        menuPanel.add(foodButton);
        menuPanel.add(drinkButton);
        menuPanel.add(memButton);

        // 产品列表
        productList = new JList<>();
        productScrollPane = new JScrollPane(productList);
        productScrollPane.setPreferredSize(new Dimension(250, 200));
        menuPanel.add(productScrollPane);

        // 添加按钮监听
        foodButton.addActionListener(e -> updateProductList("Coffee"));
        drinkButton.addActionListener(e -> updateProductList("Drinks"));
        memButton.addActionListener(e -> updateProductList("Dessert"));

        /** 订单列表 */
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
		
		/**CANCEL BUTTON*/
		buttonCancel = new JButton("Cancel Order");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.LAST_LINE_START;
		c.gridx = 0;
		c.gridy = 9;
		//c.gridwidth = 1;
		panelForm.add(buttonCancel, c);
		c.fill = GridBagConstraints.NONE;

		/** SHOW REPORT BUTTON */
		buttonReport = new JButton("     Show Report      ");
		c.gridx = 3; // 放在 Cancel Order 右边
		c.gridy = 9;
		//c.gridwidth = 1; // 
		panelForm.add(buttonReport, c);
		c.insets = new Insets(0, 15, 0, 0);
		panelForm.add(buttonReport, c);
		c.insets = new Insets(0, 0, 0, 0);

		/**CONFIRM BUTTON*/
		buttonConfirm = new JButton("     Confirm Order     ");
		c.anchor = GridBagConstraints.LINE_END;
		c.gridx = 3;
		c.gridy = 6;
		panelForm.add(buttonConfirm, c);

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
            Order order = new Order();
            order.setItemID(selectedProduct.getItemId());
            order.setItemName(selectedProduct.getItemName());
            order.setItemPrice(Math.round(selectedProduct.getPrice()));
            order.setQuantity(1);
            currentOrders.put(currentOrders.size() + 1, order);
            //setDiscountAndTotal();
            //displayBasket();
        });

        buttonRemove.addActionListener(e -> {
            if (currentListSelection == null) {
                JOptionPane.showMessageDialog(null, "Please Select an Item to Remove");
                return;
            }
            currentOrders.remove(currentListSelection.getItemId());
            //setDiscountAndTotal();
            //displayBasket();
        });

        buttonConfirm.addActionListener(e -> {
            if (!currentOrders.isEmpty()) {
                ordersFileReader.saveNewOrdersInExistingOrders(currentOrders);
                ordersFileReader.writeOrdersToCSV();
                JOptionPane.showMessageDialog(null, "Order Confirmed");
                currentOrders.clear();
                //setDiscountAndTotal();
                //displayBasket();
            }
        });

        buttonCancel.addActionListener(e -> {   //clear,重新计算折扣和总价,displayBasket
            currentOrders.clear();
            //setDiscountAndTotal();
            //displayBasket();
            JOptionPane.showMessageDialog(null, "Order has been successfully cancelled");
        });        

        buttonReport.addActionListener(e -> {
            ReportGenerator.getInstance().generateReport();
            JOptionPane.showMessageDialog(null, "Report generated successfully!");
        });

        buttonQuit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // 立即退出程序
            }
        });
        
    }

    private void updateProductList(String category) {
        ArrayList<String> itemNames = menuFileReader.getItemNameListForSelectedCategory(category);
        DefaultListModel<MenuItem> model = new DefaultListModel<>();
        for (String name : itemNames) {
            for (MenuItem item : menuFileReader.menuItemsHashMap.values()) {
                if (item.getItemName().equals(name)) model.addElement(item);
            }
        }
        productList.setModel(model);
    }

    
}
