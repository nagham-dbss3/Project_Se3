package bank.notifications;

/**
 * AppNotifier - Concrete Observer
 * Simulates sending notifications via In-App notification.
 */
public class AppNotifier implements Notifier {
    
    @Override
    public void update(String message) {
        System.out.println("[APP] New notification: " + message);
    }
}
