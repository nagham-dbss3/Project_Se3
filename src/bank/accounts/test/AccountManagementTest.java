package bank.accounts.test;

import bank.accounts.*;
import bank.accounts.composite.*;
import bank.accounts.states.*;
import bank.accounts.types.*;

/**
 * AccountManagementTest - Unit tests for Account Management Subsystem
 * 
 * This test suite demonstrates the testability of the Account Management system.
 * Tests verify:
 * - Account creation and basic operations
 * - State transitions and state-specific behaviors
 * - Account type-specific features
 * - Composite pattern functionality
 */
public class AccountManagementTest {
    
    private int testsPassed = 0;
    private int testsFailed = 0;
    
    public static void main(String[] args) {
        AccountManagementTest tester = new AccountManagementTest();
        tester.runAllTests();
        tester.printResults();
    }
    
    private void runAllTests() {
        System.out.println("========================================");
        System.out.println("ACCOUNT MANAGEMENT UNIT TESTS");
        System.out.println("========================================\n");
        
        // FR1: Account Types Tests
        testSavingAccountCreation();
        testSavingAccountMinimumBalance();
        testCheckingAccountOverdraft();
        testLoanAccountRepayment();
        testInvestmentAccountOperations();
        
        // FR4: State Pattern Tests
        testStateTransitions();
        testActiveState();
        testFrozenState();
        testSuspendedState();
        testClosedState();
        
        // FR3: Composite Pattern Tests
        testCompositeGroupCreation();
        testCompositeGroupDeposit();
        testCompositeGroupWithdraw();
        
        // FR2: Account Lifecycle Tests
        testAccountCreation();
        testAccountModification();
        testAccountClosure();
    }
    
    // ============ FR1: Account Type Tests ============
    
    private void testSavingAccountCreation() {
        test("Saving Account Creation", () -> {
            SavingAccount account = new SavingAccount("Test User", 500.0);
            assertTrue(account.getBalance() == 500.0, "Initial balance should be 500.0");
            assertTrue(account.getAccountType().equals("SAVINGS"), "Type should be SAVINGS");
        });
    }
    
    private void testSavingAccountMinimumBalance() {
        test("Saving Account Minimum Balance Enforcement", () -> {
            SavingAccount account = new SavingAccount("Test User", 500.0);
            boolean result = account.withdraw(500.0); // This would leave balance at -0
            assertFalse(result, "Withdrawal violating minimum balance should fail");
            assertTrue(account.getBalance() == 500.0, "Balance should remain unchanged");
        });
    }
    
    private void testCheckingAccountOverdraft() {
        test("Checking Account Overdraft Protection", () -> {
            CheckingAccount account = new CheckingAccount("Test User", 100.0);
            // Total available = 100 (balance) + 500 (overdraft) = 600
            boolean result = account.withdraw(700.0); // Exceeds total available funds
            assertFalse(result, "Withdrawal exceeding overdraft limit should fail");
            assertTrue(account.getBalance() == 100.0, "Balance should remain unchanged");
        });
    }
    
    private void testLoanAccountRepayment() {
        test("Loan Account Payment", () -> {
            LoanAccount loan = new LoanAccount("Test User", 1000.0, 5.0, 12);
            assertTrue(loan.getBalance() < 0, "Loan balance should be negative");
            assertTrue(loan.getAmountOwed() == 1000.0, "Amount owed should be 1000");
        });
    }
    
    private void testInvestmentAccountOperations() {
        test("Investment Account Operations", () -> {
            InvestmentAccount account = new InvestmentAccount("Test User", 1000.0);
            boolean invested = account.invest(500.0);
            assertTrue(invested, "Investment should succeed");
            assertTrue(account.getBalance() == 500.0, "Cash balance should be reduced");
        });
    }
    
    // ============ FR4: State Pattern Tests ============
    
    private void testStateTransitions() {
        test("Account State Transitions", () -> {
            SavingAccount account = new SavingAccount("Test User", 1000.0);
            account.setState(new FrozenState());
            assertTrue(account.getCurrentStateName().equals("FROZEN"), "State should be FROZEN");
            
            account.setState(new ActiveState());
            assertTrue(account.getCurrentStateName().equals("ACTIVE"), "State should be ACTIVE");
        });
    }
    
    private void testActiveState() {
        test("ACTIVE State - All Operations Allowed", () -> {
            SavingAccount account = new SavingAccount("Test User", 1000.0);
            account.setState(new ActiveState());
            
            boolean depositOk = account.deposit(100.0);
            boolean withdrawOk = account.withdraw(100.0);
            
            assertTrue(depositOk, "Deposit should succeed in ACTIVE state");
            assertTrue(withdrawOk, "Withdrawal should succeed in ACTIVE state");
        });
    }
    
    private void testFrozenState() {
        test("FROZEN State - No Operations Allowed", () -> {
            SavingAccount account = new SavingAccount("Test User", 1000.0);
            account.setState(new FrozenState());
            
            boolean depositOk = account.deposit(100.0);
            boolean withdrawOk = account.withdraw(100.0);
            
            assertFalse(depositOk, "Deposit should fail in FROZEN state");
            assertFalse(withdrawOk, "Withdrawal should fail in FROZEN state");
        });
    }
    
    private void testSuspendedState() {
        test("SUSPENDED State - Only Deposits Allowed", () -> {
            SavingAccount account = new SavingAccount("Test User", 1000.0);
            account.setState(new SuspendedState());
            
            boolean depositOk = account.deposit(100.0);
            boolean withdrawOk = account.withdraw(100.0);
            
            assertTrue(depositOk, "Deposit should succeed in SUSPENDED state");
            assertFalse(withdrawOk, "Withdrawal should fail in SUSPENDED state");
        });
    }
    
    private void testClosedState() {
        test("CLOSED State - No Operations Allowed", () -> {
            SavingAccount account = new SavingAccount("Test User", 1000.0);
            account.setState(new ClosedState());
            
            boolean depositOk = account.deposit(100.0);
            boolean withdrawOk = account.withdraw(100.0);
            
            assertFalse(depositOk, "Deposit should fail in CLOSED state");
            assertFalse(withdrawOk, "Withdrawal should fail in CLOSED state");
        });
    }
    
    // ============ FR3: Composite Pattern Tests ============
    
    private void testCompositeGroupCreation() {
        test("Composite Group Creation and Hierarchy", () -> {
            AccountGroup group = new AccountGroup("Test Group");
            SavingAccount account = new SavingAccount("User", 1000.0);
            AccountLeaf leaf = new AccountLeaf(account);
            
            group.addComponent(leaf);
            assertTrue(group.getChildCount() == 1, "Group should have 1 child");
        });
    }
    
    private void testCompositeGroupDeposit() {
        test("Composite Group Deposit Distribution", () -> {
            AccountGroup group = new AccountGroup("Test Group");
            SavingAccount acc1 = new SavingAccount("User1", 1000.0);
            SavingAccount acc2 = new SavingAccount("User2", 1000.0);
            
            group.addComponent(new AccountLeaf(acc1));
            group.addComponent(new AccountLeaf(acc2));
            
            group.deposit(200.0); // 100 each
            
            assertTrue(acc1.getBalance() == 1100.0, "Account1 should have 1100");
            assertTrue(acc2.getBalance() == 1100.0, "Account2 should have 1100");
        });
    }
    
    private void testCompositeGroupWithdraw() {
        test("Composite Group Proportional Withdrawal", () -> {
            AccountGroup group = new AccountGroup("Test Group");
            SavingAccount acc1 = new SavingAccount("User1", 1000.0);
            SavingAccount acc2 = new SavingAccount("User2", 2000.0);
            
            group.addComponent(new AccountLeaf(acc1));
            group.addComponent(new AccountLeaf(acc2));
            
            group.withdraw(900.0); // Proportional: 300 from acc1, 600 from acc2
            
            assertTrue(Math.abs(acc1.getBalance() - 700.0) < 1, "Account1 should have ~700");
            assertTrue(Math.abs(acc2.getBalance() - 1400.0) < 1, "Account2 should have ~1400");
        });
    }
    
    // ============ FR2: Account Lifecycle Tests ============
    
    private void testAccountCreation() {
        test("Account Creation", () -> {
            SavingAccount account = new SavingAccount("New User", 500.0);
            assertTrue(account.getAccountId() != null, "Account ID should be generated");
            assertTrue(account.getBalance() == 500.0, "Initial balance should be set");
            assertTrue(account.getCurrentStateName().equals("ACTIVE"), "New account should be ACTIVE");
        });
    }
    
    private void testAccountModification() {
        test("Account Modification", () -> {
            SavingAccount account = new SavingAccount("Original Name", 500.0);
            account.updateAccountHolder("New Name");
            assertTrue(account.getAccountHolder().equals("New Name"), "Account holder should be updated");
        });
    }
    
    private void testAccountClosure() {
        test("Account Closure", () -> {
            SavingAccount account = new SavingAccount("Test User", 500.0);
            account.setState(new ClosedState());
            assertTrue(account.getCurrentStateName().equals("CLOSED"), "Account should be CLOSED");
            
            boolean withdrawOk = account.withdraw(100.0);
            assertFalse(withdrawOk, "Withdrawal should fail on closed account");
        });
    }
    
    // ============ Test Infrastructure ============
    
    private void test(String testName, TestRunnable test) {
        try {
            System.out.println("Running: " + testName + "...");
            test.run();
            System.out.println("✓ PASSED\n");
            testsPassed++;
        } catch (AssertionError e) {
            System.out.println("✗ FAILED: " + e.getMessage() + "\n");
            testsFailed++;
        } catch (Exception e) {
            System.out.println("✗ ERROR: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }
    
    private void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
    
    private void assertFalse(boolean condition, String message) {
        if (condition) {
            throw new AssertionError(message);
        }
    }
    
    private void printResults() {
        System.out.println("\n========================================");
        System.out.println("TEST RESULTS");
        System.out.println("========================================");
        System.out.println("Passed: " + testsPassed);
        System.out.println("Failed: " + testsFailed);
        System.out.println("Total:  " + (testsPassed + testsFailed));
        System.out.println("Coverage: " + (testsPassed * 100 / (testsPassed + testsFailed)) + "%");
        System.out.println("========================================");
    }
    
    @FunctionalInterface
    interface TestRunnable {
        void run() throws Exception;
    }
}
