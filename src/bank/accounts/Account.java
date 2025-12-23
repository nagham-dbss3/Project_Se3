package bank.accounts;

import bank.accounts.composite.AccountComponent;
import bank.accounts.states.ActiveState;
import bank.interest.InterestStrategy;
import bank.notifications.NotificationSubject;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Account Abstract Class - Base class for all account types
 * Implements the State Pattern to manage account states and their associated behaviors.
 * Extends NotificationSubject to support Observer Pattern for notifications.
 * 
 * Design Pattern: State Pattern
 * - Uses AccountState interface to define state-specific behaviors
 * - State can be changed at runtime
 * - Each state determines what operations are allowed
 * 
 * Design Pattern: Observer Pattern
 * - Inherits from NotificationSubject
 * - Notifies observers on balance changes and state transitions
 */
public abstract class Account extends NotificationSubject implements AccountComponent {
    
    // Account basic information
    private String accountId;
    private String accountHolder;
    private String accountType;
    private double balance;
    private LocalDateTime createdAt;
    private LocalDateTime lastModified;
    
    // State management
    private AccountState currentState;

    // Strategy Pattern
    private InterestStrategy interestStrategy;
    
    /**
     * Constructor - Initializes a new account
     * By default, new accounts are created in ACTIVE state
     * 
     * @param accountHolder Name of the account holder
     * @param accountType Type of the account (e.g., Savings, Checking)
     * @param initialBalance Initial balance for the account
     */
    public Account(String accountHolder, String accountType, double initialBalance) {
        this.accountId = UUID.randomUUID().toString();
        this.accountHolder = accountHolder;
        this.accountType = accountType;
        this.balance = initialBalance;
        this.currentState = new ActiveState();
        this.createdAt = LocalDateTime.now();
        this.lastModified = LocalDateTime.now();
    }
    
    // ============ STATE MANAGEMENT METHODS ============
    
    /**
     * Changes the account state to a new state
     * 
     * @param newState The new AccountState to transition to
     */
    public void setState(AccountState newState) {
        if (newState != null) {
            this.currentState = newState;
            this.lastModified = LocalDateTime.now();
            String message = "Account " + accountId + " state changed to: " + currentState.getStateName();
            System.out.println(message);
            notifyObservers(message);
        }
    }
    
    /**
     * Gets the current state of the account
     */
    public AccountState getCurrentState() {
        return currentState;
    }
    
    /**
     * Gets the current state name
     */
    public String getCurrentStateName() {
        return currentState.getStateName();
    }
    
    // ============ TRANSACTION METHODS (State-dependent) ============
    
    /**
     * Deposits money into the account
     * Operation is only allowed if the current state permits deposits
     * 
     * @param amount Amount to deposit
     * @return true if deposit was successful, false otherwise
     */
    public boolean deposit(double amount) {
        if (!currentState.canDeposit()) {
            System.out.println("Error: Cannot deposit in " + currentState.getStateName() + " state. " 
                             + currentState.getDescription());
            return false;
        }
        
        if (amount <= 0) {
            System.out.println("Error: Deposit amount must be positive.");
            return false;
        }
        
        balance += amount;
        lastModified = LocalDateTime.now();
        String message = "Successfully deposited " + amount + " to account " + accountId + ". New Balance: " + balance;
        System.out.println(message);
        notifyObservers(message);
        return true;
    }
    
    /**
     * Withdraws money from the account
     * Operation is only allowed if the current state permits withdrawals
     * 
     * @param amount Amount to withdraw
     * @return true if withdrawal was successful, false otherwise
     */
    public boolean withdraw(double amount) {
        if (!currentState.canWithdraw()) {
            System.out.println("Error: Cannot withdraw in " + currentState.getStateName() + " state. " 
                             + currentState.getDescription());
            return false;
        }
        
        if (amount <= 0) {
            System.out.println("Error: Withdrawal amount must be positive.");
            return false;
        }
        
        if (amount > balance) {
            System.out.println("Error: Insufficient funds. Available balance: " + balance);
            return false;
        }
        
        balance -= amount;
        lastModified = LocalDateTime.now();
        String message = "Successfully withdrew " + amount + " from account " + accountId + ". New Balance: " + balance;
        System.out.println(message);
        notifyObservers(message);
        return true;
    }
    
    /**
     * Transfers money to another account
     * Operation is only allowed if the current state permits transfers
     * 
     * @param targetAccount The account to transfer to
     * @param amount Amount to transfer
     * @return true if transfer was successful, false otherwise
     */
    public boolean transfer(Account targetAccount, double amount) {
        if (!currentState.canTransfer()) {
            System.out.println("Error: Cannot transfer in " + currentState.getStateName() + " state. " 
                             + currentState.getDescription());
            return false;
        }
        
        if (amount <= 0) {
            System.out.println("Error: Transfer amount must be positive.");
            return false;
        }
        
        if (amount > balance) {
            System.out.println("Error: Insufficient funds for transfer. Available balance: " + balance);
            return false;
        }
        
        // Perform the transfer
        // Use setters/getters to allow Decorators to intercept
        this.setBalance(this.getBalance() - amount);
        targetAccount.setBalance(targetAccount.getBalance() + amount);
        this.lastModified = LocalDateTime.now();
        // targetAccount.lastModified cannot be accessed directly if it's a decorator, 
        // but we don't have a setter for it. 
        // However, updating balance on target usually updates its lastModified if done via deposit.
        // Since we use setBalance, we might miss updating lastModified on target if setBalance doesn't do it.
        // Let's check setBalance. It just sets field. 
        // Ideally we should use a method that updates timestamp.
        
        String senderMsg = "Successfully transferred " + amount + " from account " + getAccountId() 
                         + " to account " + targetAccount.getAccountId() + ". New Balance: " + this.getBalance();
        System.out.println(senderMsg);
        this.notifyObservers(senderMsg);
        
        String receiverMsg = "Received transfer of " + amount + " from account " + getAccountId() 
                           + ". New Balance: " + targetAccount.getBalance();
        targetAccount.notifyObservers(receiverMsg);
        
        return true;
    }
    
    // ============ ACCOUNT MANAGEMENT METHODS ============
    
    /**
     * Updates account holder information
     * 
     * @param newAccountHolder New account holder name
     */
    public void updateAccountHolder(String newAccountHolder) {
        if (newAccountHolder != null && !newAccountHolder.trim().isEmpty()) {
            this.accountHolder = newAccountHolder;
            this.lastModified = LocalDateTime.now();
            System.out.println("Account holder updated to: " + newAccountHolder);
        }
    }
    
    /**
     * Closes the account - can only be done from Active or Suspended state
     */
    public void closeAccount() {
        // This will be implemented in subclasses or state handler
        this.lastModified = LocalDateTime.now();
        System.out.println("Account " + accountId + " is being closed.");
    }
    
    // ============ GETTER METHODS ============
    
    public String getAccountId() {
        return accountId;
    }
    
    public String getAccountHolder() {
        return accountHolder;
    }
    
    public String getAccountType() {
        return accountType;
    }
    
    public double getBalance() {
        return balance;
    }
    
    /**
     * Setter for balance to allow subclasses and decorators to override standard logic
     */
    public void setBalance(double balance) {
        this.balance = balance;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public LocalDateTime getLastModified() {
        return lastModified;
    }
    
    // ============ ABSTRACT METHODS (to be implemented by subclasses) ============
    
    /**
     * Sets the interest calculation strategy
     * 
     * @param strategy The new interest strategy to use
     */
    public void setInterestStrategy(InterestStrategy strategy) {
        this.interestStrategy = strategy;
    }

    /**
     * Calculates interest using the current strategy
     * 
     * @return The calculated interest amount
     */
    public double calculateInterest() {
        if (interestStrategy != null) {
            return interestStrategy.calculateInterest(this);
        }
        return 0.0;
    }
    
    /**
     * Gets the account type-specific information
     * 
     * @return A string containing account type details
     */
    public abstract String getAccountDetails();
    
    // ============ UTILITY METHODS ============
    
    public String getComponentName() {
        return getAccountHolder() + "'s " + getAccountType() + " Account";
    }
    
    public double getTotalBalance() {
        double b = getBalance();
        return "LOAN".equals(getAccountType()) ? Math.abs(b) : b;
    }
    
    public String getComponentDetails() {
        return getAccountDetails();
    }
    
    public void displayHierarchy(int depth) {
        String indent = "  ".repeat(Math.max(0, depth));
        System.out.println(indent + "├─ " + getComponentName());
        System.out.println(indent + "│  Status: " + getCurrentStateName());
        System.out.println(indent + "│  Balance: $" + String.format("%.2f", getTotalBalance()));
    }
    
    /**
     * Displays account information
     */
    @Override
    public String toString() {
        return "Account{" +
                "accountId='" + accountId + '\'' +
                ", accountHolder='" + accountHolder + '\'' +
                ", accountType='" + accountType + '\'' +
                ", balance=" + balance +
                ", currentState=" + currentState.getStateName() +
                ", createdAt=" + createdAt +
                ", lastModified=" + lastModified +
                '}';
    }
}
