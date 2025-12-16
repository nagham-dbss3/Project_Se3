package bank.notifications.test;

import bank.accounts.Account;
import bank.accounts.states.SuspendedState;
import bank.accounts.types.SavingAccount;
import bank.notifications.Notifier;
import bank.notifications.EmailNotifier;
import bank.notifications.SMSNotifier;
import bank.notifications.AppNotifier;

import java.util.ArrayList;
import java.util.List;

public class NotificationTest {

    // Mock Notifier for testing
    static class MockNotifier implements Notifier {
        private List<String> messages = new ArrayList<>();
        private String name;

        public MockNotifier(String name) {
            this.name = name;
        }

        @Override
        public void update(String message) {
            messages.add(message);
            System.out.println("[Mock " + name + "] Received: " + message);
        }

        public List<String> getMessages() {
            return messages;
        }
        
        public void clear() {
            messages.clear();
        }
    }

    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("      Running Notification System Tests    ");
        System.out.println("==========================================");
        
        boolean allTestsPassed = true;
        
        allTestsPassed &= testSingleObserver();
        allTestsPassed &= testMultipleObservers();
        allTestsPassed &= testDetachObserver();
        allTestsPassed &= testStateChangeNotification();
        allTestsPassed &= testTransferNotification();
        
        if (allTestsPassed) {
            System.out.println("\n✅ ALL NOTIFICATION TESTS PASSED");
        } else {
            System.out.println("\n❌ SOME NOTIFICATION TESTS FAILED");
        }
    }

    private static boolean testSingleObserver() {
        System.out.println("\nTest 1: Single Observer (Deposit/Withdraw)");
        try {
            // Setup
            Account account = new SavingAccount("John Doe", 1000.0);
            MockNotifier mockNotifier = new MockNotifier("TestObserver");
            account.attach(mockNotifier);
            
            // Action 1: Deposit
            account.deposit(500.0);
            
            // Verify
            if (mockNotifier.getMessages().isEmpty()) {
                System.out.println("❌ Failed: No notification received for deposit");
                return false;
            }
            String msg = mockNotifier.getMessages().get(0);
            if (!msg.contains("deposited 500.0")) {
                System.out.println("❌ Failed: Incorrect message content: " + msg);
                return false;
            }
            
            // Action 2: Withdraw
            mockNotifier.clear();
            account.withdraw(200.0);
            
            // Verify
            if (mockNotifier.getMessages().isEmpty()) {
                System.out.println("❌ Failed: No notification received for withdraw");
                return false;
            }
            msg = mockNotifier.getMessages().get(0);
            if (!msg.contains("withdrew 200.0")) {
                System.out.println("❌ Failed: Incorrect message content: " + msg);
                return false;
            }
            
            System.out.println("✅ Test 1 Passed");
            return true;
        } catch (Exception e) {
            System.out.println("❌ Test 1 Exception: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private static boolean testMultipleObservers() {
        System.out.println("\nTest 2: Multiple Observers (Email, SMS, App)");
        try {
            Account account = new SavingAccount("Jane Doe", 2000.0);
            MockNotifier email = new MockNotifier("Email");
            MockNotifier sms = new MockNotifier("SMS");
            MockNotifier app = new MockNotifier("App");
            
            account.attach(email);
            account.attach(sms);
            account.attach(app);
            
            account.deposit(100.0);
            
            if (email.getMessages().size() == 1 && 
                sms.getMessages().size() == 1 && 
                app.getMessages().size() == 1) {
                System.out.println("✅ Test 2 Passed");
                return true;
            } else {
                System.out.println("❌ Failed: Not all observers received notification");
                return false;
            }
        } catch (Exception e) {
            System.out.println("❌ Test 2 Exception: " + e.getMessage());
            return false;
        }
    }
    
    private static boolean testDetachObserver() {
        System.out.println("\nTest 3: Detach Observer");
        try {
            Account account = new SavingAccount("Bob Smith", 500.0);
            MockNotifier observer1 = new MockNotifier("Observer1");
            MockNotifier observer2 = new MockNotifier("Observer2");
            
            account.attach(observer1);
            account.attach(observer2);
            
            account.detach(observer1);
            
            account.deposit(100.0);
            
            if (observer1.getMessages().isEmpty() && observer2.getMessages().size() == 1) {
                System.out.println("✅ Test 3 Passed");
                return true;
            } else {
                System.out.println("❌ Failed: Detached observer still received notification or attached one didn't");
                return false;
            }
        } catch (Exception e) {
            System.out.println("❌ Test 3 Exception: " + e.getMessage());
            return false;
        }
    }
    
    private static boolean testStateChangeNotification() {
        System.out.println("\nTest 4: State Change Notification");
        try {
            Account account = new SavingAccount("Alice Brown", 1000.0);
            MockNotifier observer = new MockNotifier("StateObserver");
            account.attach(observer);
            
            account.setState(new SuspendedState());
            
            if (observer.getMessages().isEmpty()) {
                System.out.println("❌ Failed: No notification for state change");
                return false;
            }
            
            String msg = observer.getMessages().get(0);
            if (msg.toUpperCase().contains("STATE CHANGED TO: SUSPENDED")) {
                System.out.println("✅ Test 4 Passed");
                return true;
            } else {
                System.out.println("❌ Failed: Incorrect message: " + msg);
                return false;
            }
        } catch (Exception e) {
            System.out.println("❌ Test 4 Exception: " + e.getMessage());
            return false;
        }
    }
    
    private static boolean testTransferNotification() {
        System.out.println("\nTest 5: Transfer Notification (Sender & Receiver)");
        try {
            Account sender = new SavingAccount("Sender", 1000.0);
            Account receiver = new SavingAccount("Receiver", 100.0);
            
            MockNotifier senderObserver = new MockNotifier("SenderObs");
            MockNotifier receiverObserver = new MockNotifier("ReceiverObs");
            
            sender.attach(senderObserver);
            receiver.attach(receiverObserver);
            
            sender.transfer(receiver, 200.0);
            
            boolean senderNotified = !senderObserver.getMessages().isEmpty() && 
                                     senderObserver.getMessages().get(0).contains("Successfully transferred");
            
            boolean receiverNotified = !receiverObserver.getMessages().isEmpty() && 
                                       receiverObserver.getMessages().get(0).contains("Received transfer");
            
            if (senderNotified && receiverNotified) {
                System.out.println("✅ Test 5 Passed");
                return true;
            } else {
                System.out.println("❌ Failed: Sender notified: " + senderNotified + ", Receiver notified: " + receiverNotified);
                return false;
            }
        } catch (Exception e) {
            System.out.println("❌ Test 5 Exception: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
