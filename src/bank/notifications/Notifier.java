package bank.notifications;

/**
 * Notifier Interface - Observer Pattern
 * Represents the Observer that receives updates from the Subject (Account).
 */
public interface Notifier {
    /**
     * Updates the observer with a message
     * 
     * @param message The notification message
     */
    void update(String message);
}
