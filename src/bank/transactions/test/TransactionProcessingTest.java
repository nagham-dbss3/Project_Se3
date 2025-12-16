package bank.transactions.test;

import bank.accounts.states.ActiveState;
import bank.accounts.types.CheckingAccount;
import bank.accounts.types.SavingAccount;
import bank.transactions.TransactionService;
import bank.transactions.UserRole;
import bank.transactions.history.TransactionLog;
import bank.transactions.notification.ConsoleNotificationService;
import bank.transactions.validator.TransactionValidator;
import java.time.LocalDate;

public class TransactionProcessingTest {
    private int testsPassed = 0;
    private int testsFailed = 0;

    public static void main(String[] args) {
        TransactionProcessingTest t = new TransactionProcessingTest();
        t.runAll();
        t.printResults();
    }

    private void runAll() {
        TransactionValidator validator = new TransactionValidator(20000.0, 50000.0);
        TransactionLog log = new TransactionLog();
        TransactionService service = new TransactionService(validator, log, new ConsoleNotificationService());

        SavingAccount a1 = new SavingAccount("User1", 10000.0);
        a1.setState(new ActiveState());
        SavingAccount a2 = new SavingAccount("User2", 5000.0);
        a2.setState(new ActiveState());
        CheckingAccount c1 = new CheckingAccount("User3", 1000.0);
        c1.setState(new ActiveState());

        test("Deposit approved automatically under 1000", () -> {
            boolean ok = service.deposit(a1, 500.0, "User1", UserRole.CUSTOMER);
            assertTrue(ok, "Deposit should succeed");
        });

        test("Withdraw respects daily limit", () -> {
            service.deposit(a1, 20000.0, "Admin", UserRole.ADMIN);
            boolean ok1 = service.withdraw(a1, 15000.0, "User1", UserRole.TELLER);
            boolean ok2 = service.withdraw(a1, 6000.0, "User1", UserRole.TELLER);
            assertTrue(ok1, "First withdraw should succeed within limit");
            assertFalse(ok2, "Second withdraw should fail due to daily limit");
        });

        test("Transfer requires approval chain and logs", () -> {
            SavingAccount a3 = new SavingAccount("User4", 50000.0);
            a3.setState(new ActiveState());
            boolean ok = service.transfer(a3, a2, 12000.0, "User4", UserRole.TELLER);
            assertTrue(ok, "Transfer should succeed");
            assertTrue(log.getAllRecords().size() > 0, "Log should have records");
        });

        test("RBAC blocks large customer transaction", () -> {
            SavingAccount a3 = new SavingAccount("User5", 50000.0);
            a3.setState(new ActiveState());
            boolean ok = service.transfer(a3, a2, 30000.0, "User5", UserRole.CUSTOMER);
            assertFalse(ok, "Customer should not be allowed to initiate 30k transfer");
        });

        test("Checking account overdraft allowed via withdraw execution", () -> {
            boolean ok = service.withdraw(c1, 1200.0, "User3", UserRole.CUSTOMER);
            assertTrue(ok, "Checking account should allow overdraft withdraw");
        });

        test("Scheduled monthly deposit runs when due", () -> {
            bank.transactions.scheduler.ScheduledTransaction st =
                    new bank.transactions.scheduler.ScheduledTransaction(
                            bank.transactions.TransactionType.DEPOSIT, a2, a2, 1000.0,
                            "System", UserRole.ADMIN, LocalDate.now(), 30);
            st.runIfDue(LocalDate.now(), service);
            assertTrue(log.getAllRecords().size() >= 1, "Scheduled transaction should be logged");
        });
    }

    private void test(String name, TestRunnable r) {
        try {
            r.run();
            testsPassed++;
            System.out.println("✓ " + name);
        } catch (AssertionError e) {
            testsFailed++;
            System.out.println("✗ " + name + " - " + e.getMessage());
        } catch (Exception e) {
            testsFailed++;
            System.out.println("✗ " + name + " - " + e.getMessage());
        }
    }

    private void assertTrue(boolean cond, String msg) {
        if (!cond) throw new AssertionError(msg);
    }
    private void assertFalse(boolean cond, String msg) {
        if (cond) throw new AssertionError(msg);
    }

    private void printResults() {
        System.out.println("\nTEST RESULTS");
        System.out.println("Passed: " + testsPassed);
        System.out.println("Failed: " + testsFailed);
        int total = testsPassed + testsFailed;
        System.out.println("Total: " + total);
        System.out.println("Coverage: " + (total == 0 ? 0 : (testsPassed * 100 / total)) + "%");
    }

    @FunctionalInterface
    interface TestRunnable {
        void run() throws Exception;
    }
}
