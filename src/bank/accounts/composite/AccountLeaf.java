package bank.accounts.composite;

import bank.accounts.Account;

/**
 * AccountLeaf - Leaf node in the Composite Pattern
 * Represents a single individual account.
 * This is a wrapper that allows individual accounts to be used in the composite hierarchy.
 */
public class AccountLeaf implements AccountComponent {
    
    private Account account;
    
    /**
     * Constructor
     * 
     * @param account The actual Account object to wrap
     */
    public AccountLeaf(Account account) {
        this.account = account;
    }
    
    /**
     * Gets the account object
     */
    public Account getAccount() {
        return account;
    }
    
    @Override
    public String getComponentName() {
        return account.getAccountHolder() + "'s " + account.getAccountType() + " Account";
    }
    
    @Override
    public double getTotalBalance() {
        // For loan accounts, return the absolute value to represent total portfolio value
        double balance = account.getBalance();
        return account.getAccountType().equals("LOAN") ? Math.abs(balance) : balance;
    }
    
    @Override
    public boolean deposit(double amount) {
        return account.deposit(amount);
    }
    
    @Override
    public boolean withdraw(double amount) {
        return account.withdraw(amount);
    }
    
    @Override
    public String getComponentDetails() {
        return account.getAccountDetails();
    }
    
    @Override
    public void displayHierarchy(int depth) {
        String indent = "  ".repeat(depth);
        System.out.println(indent + "├─ " + getComponentName());
        System.out.println(indent + "│  Status: " + account.getCurrentStateName());
        System.out.println(indent + "│  Balance: $" + String.format("%.2f", getTotalBalance()));
    }
    
    @Override
    public String toString() {
        return getComponentName() + " [" + account.getAccountId() + "]";
    }
}
