package serving_staff;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import java.util.Random;
import javax.swing.JTextArea;
import main.Logger;
import model.CustomerQueue;
import main.Order;


public class Staff extends Thread {
    private int staffNumber;//Attendant No.
    private int speed = 5; // Used to simulate the speed at which orders are processed
    private boolean isFinished = false; // Used to mark whether the waiter has completed his or her work   
    private boolean closingShop = false; // Used to mark whether the coffeeshop has 
    private String currentTask;
    private LinkedList<LinkedHashMap<Integer, Order>> queueCustomer = new LinkedList<>();
    private CustomerQueue customerQueue = CustomerQueue.getInstance();//client queue
    private LinkedHashMap<Integer,Order> currentCustomer;
    
    private OrderSystemView view;
    private JTextArea panel;
    private static Logger logger = Logger.getInstance();


    /**
     * This is the constructor of the Staff class
     * @param staffNumber - this is the number of the stuff
     * @param processor - this is the costumer order processor
     * @param view - this is the the view from MVC pattern
     */
    public Staff(long staffNumber, CustomerQueue customerQueue, OrderSystemView view) {
        this.staffNumber = Math.round(staffNumber);
        this.queueCustomer = customerQueue.getQueue();
        this.customerQueue = customerQueue;
        this.view = view;
        currentTask = staffNumber + " is waiting for orders in the queue";
        logger.info("STAFF MEMBER " + currentTask);
    }
    /**
     * This method is used for getting the current costumer
     * @return the current costumer
     */
    public LinkedHashMap<Integer,Order> GetCurrentCustomer()
	{
    	return currentCustomer;
	}

    // This method is used to make the staff thread run
    public void run() {        
    	while (queueCustomer.size() == 0 && isFinished == false) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    	
    
    	while (queueCustomer.size() > 0) {
    		Random random = new Random();
    		int low = 3000 / speed;
            int high = 15000 / speed;
            int result = random.nextInt(high - low) + low;
    	                
            try {
            	currentCustomer = customerQueue.getNextCustomer();		
            	Integer customerID = currentCustomer.keySet().iterator().next(); // 获取第一个键（客户ID）
            	StringBuilder orderDetails = new StringBuilder();
            	Float total = customerQueue.getCurrentCustomerTotalAmountBeforeDiscount(currentCustomer);
            	Float discount = customerQueue.getCurrentCustomerDiscount(currentCustomer);
            	String discountText;
            	if(discount == 0) {
            		discountText = " (no discount)";
            	} else {
            		discountText =  " (with " + discount + " discount)";
            	}
            	total = total - discount;
            	// 遍历客户的所有订单
            	for (Order order : currentCustomer.values()) {
            	    // 获取订单中的物品信息，并将其添加到 orderDetails 中
            	    orderDetails.append(order.getItemName()).append("\n");
            	}

            	// 构造最终的任务描述，显示该客户的所有订单信息
            	currentTask = staffNumber + " is processing customer " + customerID + "'s order of: \n" + orderDetails.toString() + " Total: " + total + discountText;
            	logger.info("Staff member " + currentTask);
            
				
				view.UpdateAllText();

				try {
					Thread.sleep(result);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				currentTask = staffNumber + " has completed processing customer " + customerID + "'s order";	
				logger.info("Staff member " + currentTask);
				
				view.UpdateAllText();				
			} catch (NullPointerException e) {
				continue;
			}

            try {
                result = random.nextInt(high - low) + low;
                Thread.sleep(result);
            } catch (InterruptedException e) {
                e.printStackTrace();

            }
            currentTask = staffNumber + " is ready to take an order";
            logger.info("Staff member " + currentTask);
            view.UpdateAllText();
        }

        if (isFinished==false) {
            currentTask = staffNumber + " is closing the shop";
            logger.info("STAFF MEMBER " + currentTask);
            closingShop = Logger.getInstance().print();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (closingShop) {
                Logger.getInstance().printFile();
            } else {
                currentTask = staffNumber + " is cleaning up";
                logger.info("STAFF MEMBER " + currentTask);
            }
        }
    }


    /**
     * This method is used to know what the staff member is currently doing
     * @return the current task
     */
    public String GetCurrentStaffTask() {
        return currentTask;
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
        logger.info("Staff finished: " + staffNumber);
        this.isFinished = true;
    }
    @Override
    public boolean equals(Object o) {
        Staff staff = (Staff) o;
        return staffNumber == staff.staffNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(staffNumber);
    }
}



