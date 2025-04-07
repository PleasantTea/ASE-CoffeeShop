package observer;


/**
 * QueueObserver is an observer interface
 * Used to receive notifications and update the interface when the CustomerQueue changes
 */
public interface QueueObserver {
	/**
     * When the customer queue changes (a customer joins or leaves the queue)
     * CustomerQueue will call this method to notify all observers to update the display
     */
	void updateQueueDisplay();

}
