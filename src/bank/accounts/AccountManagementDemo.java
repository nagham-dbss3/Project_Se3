package bank.accounts;

import bank.accounts.composite.AccountComponent;
import bank.accounts.composite.AccountGroup;
import bank.accounts.composite.AccountLeaf;
import bank.accounts.decorators.InsuranceFeature;
import bank.accounts.decorators.OverdraftProtection;
import bank.accounts.decorators.PremiumAccount;
import bank.accounts.states.*;
import bank.accounts.types.*;
import bank.interest.*;
import bank.notifications.*;
import bank.transactions.*;
import bank.users.Role;
import bank.users.User;
import bank.admin.AccessControl;
import bank.admin.Dashboard;
import bank.admin.ReportingService;

import bank.transactions.history.TransactionLog;
import bank.transactions.notification.ConsoleNotificationService;
import bank.transactions.validator.TransactionValidator;
import java.util.Arrays;
import java.util.List;

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
        
        // Admin & User Management
        demonstrateAdminSystem();
        
        // Decorator Pattern
        demonstrateDecoratorPattern();
    }
    
    /**
     * Demonstrates the Decorator Pattern for dynamic feature addition
     */
    private static void demonstrateDecoratorPattern() {
        System.out.println("\n========================================");
        System.out.println("DECORATOR PATTERN (Dynamic Features)");
        System.out.println("========================================\n");
        
        // 1. Create a basic account
        Account myAccount = new SavingAccount("Dynamic Dave", 1000.0);
        System.out.println("--- 1. Basic Account ---");
        System.out.println(myAccount.getAccountDetails());
        
        // 2. Add Overdraft Protection
        System.out.println("\n--- 2. Adding Overdraft Protection ($500 limit) ---");
        myAccount = new OverdraftProtection(myAccount, 500.0);
        System.out.println(myAccount.getAccountDetails());
        
        // Test Overdraft
        System.out.println("Attempting to withdraw $1200 (Balance: $1000)...");
        myAccount.withdraw(1200.0); // Should succeed
        
        // 3. Add Premium Features
        System.out.println("\n--- 3. Upgrading to Premium Account ---");
        myAccount = new PremiumAccount(myAccount);
        System.out.println(myAccount.getAccountDetails());
        
        // Test Premium Transfer
        Account target = new SavingAccount("Target Tom", 500.0);
        myAccount.transfer(target, 100.0);
        
        // 4. Add Insurance
        System.out.println("\n--- 4. Adding Insurance Feature ---");
        myAccount = new InsuranceFeature(myAccount);
        System.out.println(myAccount.getAccountDetails());
        
        // Test Insurance Fee
        System.out.println("Withdrawing $50 (Should charge insurance fee)...");
        myAccount.withdraw(50.0);
    }

    /**
     * Demonstrates Admin and User Management System
     */
    private static void demonstrateAdminSystem() {
        System.out.println("\n========================================");
        System.out.println("ADMIN & USER MANAGEMENT SYSTEM");
        System.out.println("========================================\n");
        
        // 1. Setup Data
        TransactionLog log = new TransactionLog();
        SavingAccount a1 = new SavingAccount("User1", 5000.0);
        SavingAccount a2 = new SavingAccount("User2", 3000.0);
        List<Account> accounts = Arrays.asList(a1, a2);
        
        // 2. Setup Admin Components
        ReportingService reportingService = new ReportingService(log);
        AccessControl accessControl = new AccessControl();
        Dashboard dashboard = new Dashboard(reportingService, accessControl);
        
        // 3. Create Users
        User adminUser = new User("U001", "Admin Alice", Role.ADMIN);
        User tellerUser = new User("U002", "Teller Bob", Role.TELLER);
        User customerUser = new User("U003", "Customer Charlie", Role.CUSTOMER);
        
        // 4. Demonstrate Dashboard Access
        System.out.println("--- 1. Admin Accessing Dashboard ---");
        dashboard.showDashboard(adminUser, accounts);
        
        System.out.println("--- 2. Teller Accessing Dashboard ---");
        // Teller might not have VIEW_REPORTS depending on permissions logic, let's check User.java
        // TELLER has PROCESS_TRANSACTION, VIEW_ACCOUNTS. Does not have VIEW_REPORTS.
        dashboard.showDashboard(tellerUser, accounts);
        
        System.out.println("--- 3. Customer Accessing Dashboard ---");
        dashboard.showDashboard(customerUser, accounts);
        
        // 5. Demonstrate Access Control on Transactions
        System.out.println("--- 4. Access Control Checks ---");
        System.out.println("Admin initiating $200,000 transaction: " + 
                           (accessControl.canPerformTransaction(adminUser, 200000.0) ? "ALLOWED" : "DENIED"));
                           
        System.out.println("Teller initiating $60,000 transaction (Limit 50k): " + 
                           (accessControl.canPerformTransaction(tellerUser, 60000.0) ? "ALLOWED" : "DENIED"));
                           
        System.out.println("Customer initiating $15,000 transaction (Limit 10k): " + 
                           (accessControl.canPerformTransaction(customerUser, 15000.0) ? "ALLOWED" : "DENIED"));
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
        service.transfer(source, dest, 500.0, "Alice", Role.CUSTOMER);

        // 2. Medium Transaction (Teller Approval)
        System.out.println("\n--- 2. Medium Transaction ($5,000) ---");
        service.transfer(source, dest, 5000.0, "Teller1", Role.TELLER);

        // 3. Large Transaction (Manager Approval)
        System.out.println("\n--- 3. Large Transaction ($50,000) ---");
        service.transfer(source, dest, 50000.0, "Manager1", Role.MANAGER);

        // 4. Very Large Transaction (Admin Approval)
        System.out.println("\n--- 4. Very Large Transaction ($120,000) ---");
        service.transfer(source, dest, 120000.0, "Admin1", Role.ADMIN);
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
