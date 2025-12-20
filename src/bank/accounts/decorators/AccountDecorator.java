package bank.accounts.decorators;

import bank.accounts.Account;
import bank.accounts.AccountState;
import bank.interest.InterestStrategy;
import java.time.LocalDateTime;

/**
 * AccountDecorator - Abstract Base Decorator
 * Implements the Decorator Pattern to add features to Account objects dynamically.
 */
public abstract class AccountDecorator extends Account {
    
    protected Account decoratedAccount;
    
    public AccountDecorator(Account decoratedAccount) {
        // Pass dummy values to super constructor, as we delegate everything
        super(decoratedAccount.getAccountHolder(), decoratedAccount.getAccountType(), decoratedAccount.getBalance());
        this.decoratedAccount = decoratedAccount;
    }
    
    @Override
    public String getAccountId() {
        return decoratedAccount.getAccountId();
    }
    
    @Override
    public String getAccountHolder() {
        return decoratedAccount.getAccountHolder();
    }
    
    @Override
    public String getAccountType() {
        return decoratedAccount.getAccountType();
    }
    
    @Override
    public double getBalance() {
        return decoratedAccount.getBalance();
    }
    
    @Override
    public void setBalance(double balance) {
        decoratedAccount.setBalance(balance); 
    }

    @Override
    public LocalDateTime getCreatedAt() {
        return decoratedAccount.getCreatedAt();
    }
    
    @Override
    public LocalDateTime getLastModified() {
        return decoratedAccount.getLastModified();
    }
    
    @Override
    public AccountState getCurrentState() {
        return decoratedAccount.getCurrentState();
    }
    
    @Override
    public String getCurrentStateName() {
        return decoratedAccount.getCurrentStateName();
    }
    
    @Override
    public void setState(AccountState newState) {
        decoratedAccount.setState(newState);
    }
    
    @Override
    public boolean deposit(double amount) {
        return decoratedAccount.deposit(amount);
    }
    
    @Override
    public boolean withdraw(double amount) {
        return decoratedAccount.withdraw(amount);
    }
    
    @Override
    public boolean transfer(Account targetAccount, double amount) {
        return decoratedAccount.transfer(targetAccount, amount);
    }
    
    @Override
    public void updateAccountHolder(String newAccountHolder) {
        decoratedAccount.updateAccountHolder(newAccountHolder);
    }
    
    @Override
    public void closeAccount() {
        decoratedAccount.closeAccount();
    }
    
    @Override
    public void setInterestStrategy(InterestStrategy strategy) {
        decoratedAccount.setInterestStrategy(strategy);
    }
    
    @Override
    public double calculateInterest() {
        return decoratedAccount.calculateInterest();
    }
    
    @Override
    public String getAccountDetails() {
        return decoratedAccount.getAccountDetails();
    }
    
    @Override
    public String toString() {
        return decoratedAccount.toString();
    }
}
