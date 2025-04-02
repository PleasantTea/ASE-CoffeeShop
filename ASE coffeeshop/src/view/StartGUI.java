package view;

import fileRead.MenuFileRead;
import main.Basket;
import main.Basket;
import javax.swing.*;
import java.awt.*;
import view.CoffeeShopGUI;

public class StartGUI extends JFrame {
    private JButton onlineOrderButton, offlineOrderButton;
    private MenuFileRead menuFileRead;
    private Basket basket;

    public StartGUI() {
    	menuFileRead = new MenuFileRead(); // 
        basket = new Basket(); // 
        
        createView();
        setTitle("Welcome to ASE Coffee Shop");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void createView() {
        JPanel panelMain = new JPanel();
        getContentPane().add(panelMain);
        panelMain.setLayout(new GridLayout(3, 1, 10, 10));

        JLabel welcomeLabel = new JLabel("Choose Your Order Type", JLabel.CENTER);
        panelMain.add(welcomeLabel);

        onlineOrderButton = new JButton("Online Order");
        offlineOrderButton = new JButton("Offline Order");

        panelMain.add(onlineOrderButton);
        panelMain.add(offlineOrderButton);

        onlineOrderButton.addActionListener(e -> openCoffeeShopGUI("online"));
        offlineOrderButton.addActionListener(e -> openCoffeeShopGUI("offline"));
    }
   

    private void openCoffeeShopGUI(String orderType) {
        CoffeeShopGUI coffeeGUI = new CoffeeShopGUI(menuFileRead, basket, orderType, this);
        //System.out.println(orderType);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        coffeeGUI.setLocation(100, (screenHeight - coffeeGUI.getHeight()) / 2);
        coffeeGUI.setVisible(true);
        dispose(); // 关闭当前窗口
    }
/*
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StartGUI startGUI = new StartGUI();
            startGUI.setVisible(true);
           

        });
    }
  */  
}