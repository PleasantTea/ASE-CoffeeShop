package controller;

import model.CustomerQueue;
import view.StateDisplayGUI;

import java.util.LinkedHashMap;
import java.util.Map;
import main.Order;

public class QueueController implements Runnable {
    private final CustomerQueue customerQueue;
    private final StateDisplayGUI stateDisplayGUI;
    private volatile boolean running = true;

    public QueueController(CustomerQueue queue, StateDisplayGUI gui) {
        this.customerQueue = queue;
        this.stateDisplayGUI = gui;
    }

    @Override
    public void run() {
        while (running) {
            updateQueueDisplay();

            try {
                Thread.sleep(500); // Check queue updates every 500ms
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        running = false;
    }

    private void updateQueueDisplay() {
        StringBuilder inStoreSb = new StringBuilder();
        StringBuilder onlineSb = new StringBuilder();

        // Normal queue
        int regularQueueSize = customerQueue.getQueue().size();
        inStoreSb.append("There are ").append(regularQueueSize).append(" in-store customers waiting:\n\n");
        for (LinkedHashMap<Integer, Order> customerOrders : customerQueue.getQueue()) {
            if (customerOrders.isEmpty()) continue;
            Order firstOrder = customerOrders.values().iterator().next();
            inStoreSb.append("    Customer ").append(firstOrder.getCustomerID())
                    .append(" - ").append(customerOrders.size()).append(" items\n");
        }

        // Online queue
        int priorityQueueSize = customerQueue.getPriorityQueue().size();
        onlineSb.append("There are ").append(priorityQueueSize).append(" online customers waiting:\n\n");
        for (LinkedHashMap<Integer, Order> customerOrders : customerQueue.getPriorityQueue()) {
            if (customerOrders.isEmpty()) continue;
            Order firstOrder = customerOrders.values().iterator().next();
            onlineSb.append("    Customer ").append(firstOrder.getCustomerID())
                    .append(" - ").append(customerOrders.size()).append(" items\n");
        }

        // Update GUI
        stateDisplayGUI.setQueueText(inStoreSb.toString());
        stateDisplayGUI.setOnlineQueueText(onlineSb.toString());
    }
}
