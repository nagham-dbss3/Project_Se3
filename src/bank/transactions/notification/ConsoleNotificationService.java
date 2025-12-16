package bank.transactions.notification;

public class ConsoleNotificationService implements NotificationService {
    public void notify(String message) {
        System.out.println("NOTIFY: " + message);
    }
}

