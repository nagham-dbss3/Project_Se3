package bank.interest;

import bank.accounts.Account;

/**
 * LoanInterest - Strategy implementation for Loan Accounts
 * Calculates interest on the outstanding debt.
 */
public class LoanInterest implements InterestStrategy {
    
    private double monthlyInterestRate;

    /**
     * @param annualInterestRate Annual interest rate (e.g., 0.05 for 5%)
     */
    public LoanInterest(double annualInterestRate) {
        this.monthlyInterestRate = annualInterestRate / 12 / 100;
    }

    @Override
    public double calculateInterest(Account account) {
        // Loan balance is negative, so amount owed is absolute value
        double currentBalance = Math.abs(account.getBalance());
        double monthlyInterest = currentBalance * monthlyInterestRate;
        
        // Note: The strategy calculates the amount.
        // The application of this interest (increasing debt) should be handled by the Account.
        return monthlyInterest;
    }
}
