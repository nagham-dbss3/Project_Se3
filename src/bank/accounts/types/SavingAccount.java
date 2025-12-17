package bank.accounts.types;

import bank.accounts.Account;
import bank.interest.SavingInterest;

/**
 * SavingAccount - Savings account with interest calculation
 * Features:
 * - Lower fees compared to checking accounts
 * - Interest is calculated based on balance
 * - Limited monthly withdrawals
 * - Minimum balance requirements
 */
public class SavingAccount extends Account {
    
    private static final double INTEREST_RATE = 0.03; // 3% annual interest rate
    private static final double MINIMUM_BALANCE = 100.0;
    
    /**
     * Constructor
     * 
     * @param accountHolder Name of the account holder
     * @param initialBalance Initial balance (must be >= minimum)
     */
    public SavingAccount(String accountHolder, double initialBalance) {
        super(accountHolder, "SAVINGS", initialBalance);
        
        // Set strategy
        setInterestStrategy(new SavingInterest(INTEREST_RATE));
        
        if (initialBalance < MINIMUM_BALANCE) {
            throw new IllegalArgumentException(
                "Initial balance must be at least " + MINIMUM_BALANCE + " for Savings Account");
        }
    }
    
    /**
     * Calculates simple interest for the savings account
     * Formula: Interest = Balance × Interest Rate / 12 (for monthly calculation)
     */
    @Override
    public double calculateInterest() {
        double monthlyInterest = super.calculateInterest();
        if (monthlyInterest > 0) {
            deposit(monthlyInterest);
        }
        return monthlyInterest;
    }
    
    /**
     * Gets the account details
     */
    @Override
    public String getAccountDetails() {
        return String.format(
            "Savings Account Details:%n" +
            "  Account ID: %s%n" +
            "  Holder: %s%n" +
            "  Balance: $%.2f%n" +
            "  Interest Rate: %.2f%%%n" +
            "  Minimum Balance: $%.2f%n" +
            "  Status: %s",
            getAccountId(),
            getAccountHolder(),
            getBalance(),
            INTEREST_RATE * 100,
            MINIMUM_BALANCE,
            getCurrentStateName()
        );
    }
    
    /**
     * Validates if withdrawal maintains minimum balance
     */
    @Override
    public boolean withdraw(double amount) {
        if (getBalance() - amount < MINIMUM_BALANCE) {
            System.out.println("Error: Withdrawal would violate minimum balance requirement of $" + MINIMUM_BALANCE);
            return false;
        }
        return super.withdraw(amount);
    }
}
