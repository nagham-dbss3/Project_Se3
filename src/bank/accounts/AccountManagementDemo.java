package bank.accounts;

import bank.accounts.composite.AccountComponent;
import bank.accounts.composite.AccountGroup;
import bank.accounts.composite.AccountLeaf;
import bank.accounts.states.*;
import bank.accounts.types.*;
import bank.interest.*;
import bank.notifications.*;
import bank.transactions.*;

import bank.transactions.history.TransactionLog;
import bank.transactions.notification.ConsoleNotificationService;
import bank.transactions.validator.TransactionValidator;

/**
 * AccountManagementDemo - Comprehensive Demonstration of Advanced Banking System
 * 
 * This class demonstrates the implementation of:
 * 1. Multiple Account Types (FR1)
 * 2. Account Lifecycle Management (FR2)
 * 3. Hierarchical Account Structure - Composite Pattern (FR3)
 * 4. Account State Transitions - State Pattern (FR4)
 * 5. Notification System - Observer Pattern
 * 6. Interest Calculation - Strategy Pattern
 * 7. Transaction Processing - Chain of Responsibility
 */
public class AccountManagementDemo {
    
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("ADVANCED BANKING SYSTEM DEMO");
        System.out.println("========================================\n");
        
        // FR1: Support Multiple Account Types
        demonstrateMultipleAccountTypes();
        
        // FR4: Account State Transitions
        demonstrateStateTransitions();
        
        // FR3: Hierarchical Account Structure (Composite Pattern)
        demonstrateCompositePattern();
        
        // FR2: Account Lifecycle Management
        demonstrateAccountLifecycle();

        // Notification System (Observer Pattern)
        demonstrateNotificationSystem();

        // Interest Calculation (Strategy Pattern)
        demonstrateInterestStrategy();

        // Transaction Processing (Chain of Responsibility)
        demonstrateTransactionProcessing();
    }
    
    /**
     * Demonstrates Notification System using Observer Pattern
     */
    private static void demonstrateNotificationSystem() {
        System.out.println("\n========================================");
        System.out.println("NOTIFICATION SYSTEM (Observer Pattern)");
        System.out.println("========================================\n");

        SavingAccount account = new SavingAccount("Sarah Connor", 5000.0);
        
        // Register Observers
        System.out.println("--- Registering Observers ---");
        // Notifiers don't take arguments in their current implementation
        account.attach(new EmailNotifier());
        account.attach(new SMSNotifier());
        account.attach(new AppNotifier());

        // Trigger notifications via transactions
        System.out.println("\n--- Triggering Notifications ---");
        account.deposit(1000.0);
        account.withdraw(500.0);
        
        // Trigger notification via state change
        System.out.println("\n--- State Change Notification ---");
        account.setState(new FrozenState());
    }

    /**
     * Demonstrates Interest Calculation using Strategy Pattern
     */
    private static void demonstrateInterestStrategy() {
        System.out.println("\n========================================");
        System.out.println("INTEREST CALCULATION (Strategy Pattern)");
        System.out.println("========================================\n");

        // 1. Savings Account (Default Strategy)
        SavingAccount savingAccount = new SavingAccount("John Doe", 1000.0);
        System.out.println("--- Savings Account Interest ---");
        System.out.println("Initial Balance: $" + savingAccount.getBalance());
        double interest = savingAccount.calculateInterest();
        System.out.println("Interest Applied: $" + interest);
        System.out.println("New Balance: $" + savingAccount.getBalance());

        // 2. Loan Account (Loan Strategy)
        LoanAccount loanAccount = new LoanAccount("Jane Smith", 5000.0, 5.0, 12);
        System.out.println("\n--- Loan Account Interest ---");
        System.out.println("Initial Loan Balance (Debt): $" + Math.abs(loanAccount.getBalance()));
        double loanInterest = loanAccount.calculateInterest();
        System.out.println("Interest Accrued: $" + loanInterest);
        System.out.println("New Loan Balance: $" + Math.abs(loanAccount.getBalance()));

        // 3. Runtime Strategy Switching
        System.out.println("\n--- Runtime Strategy Switching ---");
        System.out.println("Current Savings Interest Rate: 3% (Default)");
        
        // Change to a higher interest rate strategy (e.g., Promotional 5%)
        System.out.println("Applying Promotional Interest Rate (5%)...");
        savingAccount.setInterestStrategy(new SavingInterest(0.05));
        
        double newInterest = savingAccount.calculateInterest();
        System.out.println("New Interest Applied: $" + newInterest);
        System.out.println("Final Balance: $" + savingAccount.getBalance());
    }

    /**
     * Demonstrates Transaction Processing using Chain of Responsibility
     */
    private static void demonstrateTransactionProcessing() {
        System.out.println("\n========================================");
        System.out.println("TRANSACTION PROCESSING (Chain of Responsibility)");
        System.out.println("========================================\n");

        // Initialize dependencies
        TransactionLog log = new TransactionLog();
        TransactionValidator validator = new TransactionValidator(100000.0, 100000.0);
        ConsoleNotificationService notificationService = new ConsoleNotificationService();
        
        TransactionService service = new TransactionService(validator, log, notificationService);
        
        SavingAccount source = new SavingAccount("Alice", 200000.0); // Sufficient funds
        SavingAccount dest = new SavingAccount("Bob", 100.0);

        // 1. Small Transaction (Auto Approval)
        System.out.println("--- 1. Small Transaction ($500) ---");
        // transfer(from, to, amount, user, role)
        service.transfer(source, dest, 500.0, "Alice", UserRole.CUSTOMER);

        // 2. Medium Transaction (Teller Approval)
        System.out.println("\n--- 2. Medium Transaction ($5,000) ---");
        service.transfer(source, dest, 5000.0, "Teller1", UserRole.TELLER);

        // 3. Large Transaction (Manager Approval)
        System.out.println("\n--- 3. Large Transaction ($50,000) ---");
        service.transfer(source, dest, 50000.0, "Manager1", UserRole.MANAGER);

        // 4. Very Large Transaction (Admin Approval)
        System.out.println("\n--- 4. Very Large Transaction ($120,000) ---");
        service.transfer(source, dest, 120000.0, "Admin1", UserRole.ADMIN);
    }
    
    /**
     * FR1 - Demonstrates support for multiple account types
     */
    private static void demonstrateMultipleAccountTypes() {
        System.out.println("\n========================================");
        System.out.println("FR1: MULTIPLE ACCOUNT TYPES");
        System.out.println("========================================\n");
        
        // Create different account types
        SavingAccount savingsAccount = new SavingAccount("Ahmed Ali", 5000.0);
        CheckingAccount checkingAccount = new CheckingAccount("Fatima Hassan", 2000.0);
        InvestmentAccount investmentAccount = new InvestmentAccount("Omar Khalid", 10000.0);
        LoanAccount loanAccount = new LoanAccount("Layla Samir", 50000.0, 5.0, 60);
        
        System.out.println("\n--- Savings Account ---");
        System.out.println(savingsAccount.getAccountDetails());
        
        System.out.println("\n--- Checking Account ---");
        System.out.println(checkingAccount.getAccountDetails());
        
        System.out.println("\n--- Investment Account ---");
        System.out.println(investmentAccount.getAccountDetails());
        
        System.out.println("\n--- Loan Account ---");
        System.out.println(loanAccount.getAccountDetails());
    }
    
    /**
     * FR4 - Demonstrates account state transitions and state-specific behaviors
     */
    private static void demonstrateStateTransitions() {
        System.out.println("\n========================================");
        System.out.println("FR4: ACCOUNT STATE TRANSITIONS");
        System.out.println("========================================\n");
        
        // Create an account
        SavingAccount account = new SavingAccount("Mohammed Yaseen", 3000.0);
        
        System.out.println("Initial State: " + account.getCurrentStateName());
        System.out.println("Description: " + account.getCurrentState().getDescription());
        
        // Test operations in ACTIVE state
        System.out.println("\n--- ACTIVE State (All operations allowed) ---");
        account.deposit(500.0);
        account.withdraw(200.0);
        
        // Transition to FROZEN state
        System.out.println("\n--- Transitioning to FROZEN State ---");
        account.setState(new FrozenState());
        
        System.out.println("Current State: " + account.getCurrentStateName());
        System.out.println("Description: " + account.getCurrentState().getDescription());
        
        // Try operations in FROZEN state (should fail)
        System.out.println("\nTrying operations in FROZEN state:");
        account.deposit(500.0);
        account.withdraw(200.0);
        
        // Transition to SUSPENDED state
        System.out.println("\n--- Transitioning to SUSPENDED State ---");
        account.setState(new SuspendedState());
        
        System.out.println("Current State: " + account.getCurrentStateName());
        System.out.println("Description: " + account.getCurrentState().getDescription());
        
        // Try operations in SUSPENDED state
        System.out.println("\nOperations in SUSPENDED state:");
        account.deposit(500.0); // Should succeed
        account.withdraw(200.0); // Should fail
        
        // Transition back to ACTIVE
        System.out.println("\n--- Transitioning back to ACTIVE State ---");
        account.setState(new ActiveState());
        account.withdraw(200.0); // Should succeed
        
        // Transition to CLOSED state
        System.out.println("\n--- Transitioning to CLOSED State ---");
        account.setState(new ClosedState());
        account.withdraw(100.0); // Should fail
    }
    
    /**
     * FR3 - Demonstrates hierarchical account structure using Composite Pattern
     */
    private static void demonstrateCompositePattern() {
        System.out.println("\n========================================");
        System.out.println("FR3: HIERARCHICAL ACCOUNT STRUCTURE");
        System.out.println("        (Composite Pattern)");
        System.out.println("========================================\n");
        
        // Create individual accounts
        SavingAccount fatherSavings = new SavingAccount("Father", 10000.0);
        SavingAccount motherSavings = new SavingAccount("Mother", 8000.0);
        CheckingAccount sonChecking = new CheckingAccount("Son", 2000.0);
        CheckingAccount daughterChecking = new CheckingAccount("Daughter", 3000.0);
        
        // Create leaves for these accounts
        AccountLeaf fatherLeaf = new AccountLeaf(fatherSavings);
        AccountLeaf motherLeaf = new AccountLeaf(motherSavings);
        AccountLeaf sonLeaf = new AccountLeaf(sonChecking);
        AccountLeaf daughterLeaf = new AccountLeaf(daughterChecking);
        
        // Create family account group
        AccountGroup familyGroup = new AccountGroup("Family Accounts");
        
        // Create sub-groups
        AccountGroup parentsGroup = new AccountGroup("Parents' Accounts");
        AccountGroup childrenGroup = new AccountGroup("Children's Accounts");
        
        // Build the hierarchy
        parentsGroup.addComponent(fatherLeaf);
        parentsGroup.addComponent(motherLeaf);
        
        childrenGroup.addComponent(sonLeaf);
        childrenGroup.addComponent(daughterLeaf);
        
        familyGroup.addComponent(parentsGroup);
        familyGroup.addComponent(childrenGroup);
        
        // Display the hierarchy
        System.out.println("--- Family Account Hierarchy ---\n");
        familyGroup.displayHierarchy(0);
        
        System.out.println("\n--- Group Details ---");
        System.out.println(familyGroup.getComponentDetails());
        
        // Perform operations on the group
        System.out.println("\n--- Operations on Account Groups ---");
        System.out.println("Depositing $500 to family group (distributed equally):");
        familyGroup.deposit(500.0);
        
        System.out.println("\nTotal family balance after deposit: $" + 
                          String.format("%.2f", familyGroup.getTotalBalance()));
        
        System.out.println("\nWithdrawing $1000 from family group (proportionally distributed):");
        familyGroup.withdraw(1000.0);
        
        System.out.println("\nTotal family balance after withdrawal: $" + 
                          String.format("%.2f", familyGroup.getTotalBalance()));
    }
    
    /**
     * FR2 - Demonstrates account lifecycle (create, modify, close)
     */
    private static void demonstrateAccountLifecycle() {
        System.out.println("\n========================================");
        System.out.println("FR2: ACCOUNT LIFECYCLE MANAGEMENT");
        System.out.println("========================================\n");
        
        // 1. CREATE: Create a new account
        System.out.println("--- 1. Creating Account ---");
        SavingAccount account = new SavingAccount("Noor Khalifa", 5000.0);
        System.out.println("Account Created:");
        System.out.println(account.getAccountDetails());
        
        // 2. MODIFY: Modify account information
        System.out.println("\n--- 2. Modifying Account ---");
        System.out.println("Updating account holder name...");
        account.updateAccountHolder("Dr. Noor Khalifa");
        System.out.println("Updated Account Holder: " + account.getAccountHolder());
        
        // Perform transactions
        System.out.println("\nPerforming transactions...");
        account.deposit(1500.0);
        account.withdraw(500.0);
        
        System.out.println("Account Details After Transactions:");
        System.out.println(account.getAccountDetails());
        
        // 3. CLOSE: Close the account
        System.out.println("\n--- 3. Closing Account ---");
        account.setState(new ClosedState());
        System.out.println("Account State: " + account.getCurrentStateName());
        System.out.println(account.getAccountDetails());
        
        System.out.println("\nAttempting transaction on closed account:");
        account.withdraw(100.0); // Should fail
    }
}
