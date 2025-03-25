package view;

import javax.swing.*;
import java.awt.*;

public class StateDisplayGUI extends JFrame {
    private JTextArea queueTextArea, staff1TextArea, staff2TextArea, staff3TextArea;
    private JSlider speedSlider;
    private JButton startButton, addStaffButton, removeStaffButton;

    public StateDisplayGUI() {
        setTitle("Order status");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout()); // 使用 GridBagLayout

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(5, 5, 5, 5); // 内边距

        // ===== 第一栏：顾客排队订单 =====
        JPanel queuePanel = new JPanel(new BorderLayout());
        queuePanel.setBorder(BorderFactory.createTitledBorder("Queuing orders"));
        queueTextArea = new JTextArea();
        queueTextArea.setEditable(false);
        queuePanel.add(new JScrollPane(queueTextArea), BorderLayout.CENTER);

        gbc.gridy = 0;
        gbc.weighty = 0.50; // 高度较大
        add(queuePanel, gbc);

        // ===== 第二栏：员工工作状态（3列）=====
        JPanel staffPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        staffPanel.setBorder(BorderFactory.createTitledBorder("Employee work status"));

        staff1TextArea = createStaffTextArea("Employee 1");
        staff2TextArea = createStaffTextArea("Employee 2");
        staff3TextArea = createStaffTextArea("Employee 3");

        staffPanel.add(staff1TextArea);
        staffPanel.add(staff2TextArea);
        staffPanel.add(staff3TextArea);

        gbc.gridy = 1;
        gbc.weighty = 0.45; // 高度较小
        add(staffPanel, gbc);

        // ===== 第三栏：滑块 + 控制按钮 =====
        JPanel sliderPanel = new JPanel(new BorderLayout());
        sliderPanel.setBorder(BorderFactory.createTitledBorder("Order processing speed"));
        speedSlider = new JSlider(JSlider.HORIZONTAL, 1, 5, 3);
        speedSlider.setMajorTickSpacing(1);
        speedSlider.setPaintTicks(true);
        speedSlider.setPaintLabels(true);
        sliderPanel.add(speedSlider, BorderLayout.CENTER);

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
        gbc.weighty = 0.05; // 高度中等
        add(bottomPanel, gbc);

        //setVisible(true);
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

    /*public static void main(String[] args) {
        SwingUtilities.invokeLater(StateDisplayGUI::new);
    }*/
}
