package bank.notifications;

/**
 * SMSNotifier - Concrete Observer
 * Simulates sending notifications via SMS.
 */
public class SMSNotifier implements Notifier {
    
    @Override
    public void update(String message) {
        System.out.println("[SMS] Notification sent: " + message);
    }
}
