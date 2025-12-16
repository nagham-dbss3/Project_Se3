package bank.notifications;

import java.util.ArrayList;
import java.util.List;

/**
 * NotificationSubject - Observer Pattern
 * Represents the Subject that is observed by Notifiers.
 * Maintains a list of observers and notifies them of changes.
 */
public class NotificationSubject {
    
    private final List<Notifier> observers = new ArrayList<>();
    
    /**
     * Attaches an observer to the subject
     * 
     * @param observer The observer to attach
     */
    public void attach(Notifier observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }
    
    /**
     * Detaches an observer from the subject
     * 
     * @param observer The observer to detach
     */
    public void detach(Notifier observer) {
        observers.remove(observer);
    }
    
    /**
     * Notifies all observers with a message
     * 
     * @param message The message to send to observers
     */
    public void notifyObservers(String message) {
        for (Notifier observer : observers) {
            observer.update(message);
        }
    }
}
