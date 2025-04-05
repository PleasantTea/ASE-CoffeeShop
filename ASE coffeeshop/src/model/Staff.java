package model;

import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JOptionPane;

import main.Logger;
import main.Order;
import view.StateDisplayGUI;

public class Staff extends Thread {
    private int staffNumber;  // Attendant No
    private int speed = 3;  // Used to simulate the speed at which orders are processed
    private String currentTask;
    private boolean isCancelled = false;
    private boolean shouldTerminateAfterCurrentOrder = false;
    private CustomerQueue customerQueue = CustomerQueue.getInstance();
    private LinkedHashMap<Integer, Order> currentCustomer;
    
    private StateDisplayGUI view;
    private static Logger logger = Logger.getInstance();
    
    private static boolean simulationStarted = false;
    private static final Object lock = new Object();

    private static AtomicInteger activeStaffCount = new AtomicInteger(0); // Counting the number of working staff threads
    
    /**
     * This is the constructor of the Staff class
     * @param staffNumber - this is the number of the stuff
     * @param processor - this is the costumer order processor
     * @param view - this is the the view from MVC pattern
     */
    public Staff(long staffNumber, CustomerQueue customerQueue, StateDisplayGUI view) {
        this.staffNumber = Math.round(staffNumber);
        this.customerQueue = customerQueue;
        this.view = view;
        currentTask = "Staff " + staffNumber + " is waiting for orders";
        logger.info(currentTask);
        view.setStaffText(this.staffNumber, currentTask); // Initial status display
    }

    /**
     * This method is used for getting the current costumer
     * @return the current costumer
     */
    public LinkedHashMap<Integer, Order> GetCurrentCustomer() {
        return currentCustomer;
    }
    
    /**
     * This method is used to make the staff thread run
     */
    public void run() {
        synchronized (lock) {
            while (!simulationStarted) {
                try {
                    lock.wait();  // Waiting for the user to click the start button
                } catch (InterruptedException e) {
                    //Thread.currentThread().interrupt();
                	e.printStackTrace();
                }
            }
        }
        
        if (isCancelled) {
            currentTask = staffNumber + " was removed before simulation started.";
            logger.info("STAFF MEMBER " + currentTask);
            view.setStaffText(staffNumber, "");  // Clear Panel
            return;  // Terminate the thread
        }

        
        
        activeStaffCount.incrementAndGet(); // +1 at thread startup

        while (true) {
            if (isCancelled) {
                logger.info("Staff " + staffNumber + " has been cancelled.");
                view.setStaffText(staffNumber, "");
                break;
            }
            try {
                currentCustomer = customerQueue.getNextCustomer();

                if (currentCustomer == null) {
                    logger.info("No more customers, need to close.");
                    break; // Exit thread
                }

                String customerID = currentCustomer.values().iterator().next().getCustomerID();
                StringBuilder orderDetails = new StringBuilder();
                Float total = customerQueue.getCurrentCustomerTotalAmountBeforeDiscount(currentCustomer);
                Float discount = customerQueue.getCurrentCustomerDiscount(currentCustomer);
                String discountText = (discount == 0) ? " (no discount)" : " (with " + discount + " discount)";
                total -= discount;

                for (Order order : currentCustomer.values()) {
                    orderDetails.append(order.getItemName()).append("\n");
                }

                currentTask = "Processing " + customerID + "'s order: \n" + orderDetails + "\nTotal: " + total + discountText;
                logger.info("Staff member " + staffNumber + " is " + currentTask);
                view.setStaffText(staffNumber, currentTask);

                Random random = new Random();
                int low = 6000 / speed;
                int high = 30000 / speed;
                int result = random.nextInt(high - low) + low;
                Thread.sleep(result);
                
                try {
					Thread.sleep(result);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
                
                currentTask = "Completed " + customerID + "'s order";
                logger.info("Staff member " + staffNumber + " " + currentTask);
                view.setStaffText(staffNumber, currentTask);
                
                try {
                    //result = random.nextInt(high - low) + low;
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                
                // Determine if you need to exit after completing an order
                if (shouldTerminateAfterCurrentOrder) {
                    logger.info("Staff " + staffNumber + " has completed their final order and is now being removed.");
                    view.setStaffText(staffNumber, "");
                    break;
                }

                // Putting employees back on hold
                currentTask = "Staff " + staffNumber + " is ready to take an order";
                logger.info(currentTask);
                view.setStaffText(staffNumber, currentTask);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        // -1 on thread exit
        int remainingStaff = activeStaffCount.decrementAndGet();

        // If it is currently the last staff thread, write to the log file
        if (remainingStaff == 0) {
            logger.info("All staff have finished their work. Writing log to file.");
            Logger.getInstance().printFile();
        }
    }
    
    public int getStaffNumber() {
        return staffNumber;
    }
    
    /**
     * This method sets the speed of the process
     * @param speed
     */
    public void setSpeed(int speed) {
        logger.info("Changed staff: " + staffNumber + " speed: " + speed);
        this.speed = speed;
    }
    
    /**
     * This method display staff that has finished the job
     */
    public void finish() {
        //logger.info("Staff finished: " + staffNumber);
        //this.isCancelled = true; 
    	if (currentCustomer != null) {
            String customerID = currentCustomer.values().iterator().next().getCustomerID();
            JOptionPane.showMessageDialog(null, "Staff " + staffNumber + " is currently processing customer " + customerID + "'s order. \nWill remove after completing it.");
            shouldTerminateAfterCurrentOrder = true;
        } else {
            logger.info("Staff finished: " + staffNumber);
            isCancelled = true;
        }
    }
    
    public static void startSimulationForAllStaff() {
        synchronized (lock) {
            simulationStarted = true;
            lock.notifyAll();
        }
    }
}