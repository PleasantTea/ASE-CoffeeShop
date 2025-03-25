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
                Thread.sleep(500); // 每 500ms 检查一次队列更新
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        running = false;
    }

    private void updateQueueDisplay() {
        StringBuilder sb = new StringBuilder();

        int queueSize = customerQueue.getQueueSize();
        sb.append("There are currently ").append(queueSize).append(" people waiting in the queue:\n\n");

        for (LinkedHashMap<Integer, Order> customerOrders : customerQueue.getQueue()) {
            if (customerOrders.isEmpty()) continue;
            Order firstOrder = customerOrders.values().iterator().next();
            sb.append("    Customer ").append(firstOrder.getCustomerID())
              .append(" - ").append(customerOrders.size()).append(" items\n");
        }

        stateDisplayGUI.setQueueText(sb.toString());
    }
}
