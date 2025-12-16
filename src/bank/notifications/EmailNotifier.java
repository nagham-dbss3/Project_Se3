package bank.notifications;

/**
 * EmailNotifier - Concrete Observer
 * Simulates sending notifications via Email.
 */
public class EmailNotifier implements Notifier {
    
    @Override
    public void update(String message) {
        System.out.println("[EMAIL] Notification sent: " + message);
    }
}
