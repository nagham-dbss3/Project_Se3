package bank.interest;

import bank.accounts.Account;

/**
 * SavingInterest - Strategy implementation for Savings Accounts
 * Calculates simple interest based on balance.
 */
public class SavingInterest implements InterestStrategy {
    
    private double interestRate;

    public SavingInterest(double annualInterestRate) {
        this.interestRate = annualInterestRate;
    }

    // Default constructor with 3% rate
    public SavingInterest() {
        this(0.03);
    }

    @Override
    public double calculateInterest(Account account) {
        double monthlyInterest = account.getBalance() * interestRate / 12;
        // Logic from SavingAccount: deposit the interest
        // Note: The strategy should ideally just calculate, but existing logic deposits it.
        // I will return the amount, and let the Account call handle the deposit if needed, 
        // OR the strategy performs the action.
        // The interface returns double. 
        // In the original code:
        // public double calculateInterest() {
        //    double monthlyInterest = getBalance() * INTEREST_RATE / 12;
        //    deposit(monthlyInterest);
        //    return monthlyInterest;
        // }
        
        // I will keep it pure calculation here. The Account class will handle the deposit.
        return monthlyInterest;
    }
}
