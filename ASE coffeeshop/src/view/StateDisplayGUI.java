package view;

import javax.swing.*;

import model.Staff;

import java.awt.*;

public class StateDisplayGUI extends JFrame {
    private JTextArea queueTextArea, onlineQueueTextArea, staff1TextArea, staff2TextArea, staff3TextArea;
    private JSlider speedSlider;
    private JButton startButton, addStaffButton, removeStaffButton;
    private controller.StaffController staffController;

    public StateDisplayGUI() {
        setTitle("Order status");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout()); 

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(5, 5, 5, 5); 

        // ===== First: Customer queued orders =====
        JPanel queueSplitPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        queueSplitPanel.setBorder(BorderFactory.createTitledBorder("Order Queues"));

        // Normal queue
        JPanel regularQueuePanel = new JPanel(new BorderLayout());
        regularQueuePanel.setBorder(BorderFactory.createTitledBorder("In-store Orders"));
        queueTextArea = new JTextArea();
        queueTextArea.setEditable(false);
        regularQueuePanel.add(new JScrollPane(queueTextArea), BorderLayout.CENTER);

        // Online queue
        JPanel onlineQueuePanel = new JPanel(new BorderLayout());
        onlineQueuePanel.setBorder(BorderFactory.createTitledBorder("Online Orders"));
        onlineQueueTextArea = new JTextArea(); 
        onlineQueueTextArea.setEditable(false);
        onlineQueuePanel.add(new JScrollPane(onlineQueueTextArea), BorderLayout.CENTER);

        // Add to split container
        queueSplitPanel.add(regularQueuePanel);
        queueSplitPanel.add(onlineQueuePanel);

        gbc.gridy = 0;
        gbc.weighty = 0.50; 
        add(queueSplitPanel, gbc);


        // ===== Second: Employee work status =====
        JPanel staffPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        staffPanel.setBorder(BorderFactory.createTitledBorder("Employee work status"));

        staff1TextArea = createStaffTextArea("Employee 1");
        staff2TextArea = createStaffTextArea("Employee 2");
        staff3TextArea = createStaffTextArea("Employee 3");

        staffPanel.add(staff1TextArea);
        staffPanel.add(staff2TextArea);
        staffPanel.add(staff3TextArea);

        gbc.gridy = 1;
        gbc.weighty = 0.45; 
        add(staffPanel, gbc);

        // ===== Third: Speed Adjustment =====
        JPanel sliderPanel = new JPanel(new BorderLayout());
        sliderPanel.setBorder(BorderFactory.createTitledBorder("Order processing speed"));
        speedSlider = new JSlider(JSlider.HORIZONTAL, 1, 5, 3);
        speedSlider.setMajorTickSpacing(1);
        speedSlider.setPaintTicks(true);
        speedSlider.setPaintLabels(true);
        sliderPanel.add(speedSlider, BorderLayout.CENTER);

        // Add listener to speed slider to update staff processing speed
        speedSlider.addChangeListener(e -> {
            int speed = speedSlider.getValue(); // Value range 1~5
            if (staffController != null) {
                staffController.setAllStaffSpeed(speed);
                System.out.println("Slider changed: set all staff speed to " + speed);
            }
        });

        // ===== Fourth: Control button =====
        JPanel buttonPanel = new JPanel();
        startButton = new JButton("Start Simulation");
        addStaffButton = new JButton("Add Staff");
        removeStaffButton = new JButton("Remove Staff");
        buttonPanel.add(startButton);
        buttonPanel.add(addStaffButton);
        buttonPanel.add(removeStaffButton);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(sliderPanel, BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        gbc.gridy = 2;
        gbc.weighty = 0.05; 
        add(bottomPanel, gbc);
    }

    public void setStaffController(controller.StaffController staffController) {
        this.staffController = staffController;
    
        // Start Simulation 
        startButton.addActionListener(e -> {
            Staff.startSimulationForAllStaff();
            startButton.setEnabled(false);
        });
    
        // Add Staff 
        addStaffButton.addActionListener(e -> {
            staffController.addStaff();
        });
    
        // Remove Staff 
        removeStaffButton.addActionListener(e -> {
            staffController.removeStaff();
        });
    }

    private JTextArea createStaffTextArea(String title) {
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setBorder(BorderFactory.createTitledBorder(title));
        return area;
    }

    public void setQueueText(String text) {
        SwingUtilities.invokeLater(() -> queueTextArea.setText(text));
    }

    public void setOnlineQueueText(String text) {
        SwingUtilities.invokeLater(() -> onlineQueueTextArea.setText(text));
    }
    
    public void setStaffText(int staffNumber, String text) {
        SwingUtilities.invokeLater(() -> {
            switch (staffNumber) {
                case 1:
                    staff1TextArea.setText(text);
                    break;
                case 2:
                    staff2TextArea.setText(text);
                    break;
                case 3:
                    staff3TextArea.setText(text);
                    break;
                default:
                    break;
            }
        });
    }
}
