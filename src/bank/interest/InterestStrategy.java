package bank.interest;

import bank.accounts.Account;

/**
 * InterestStrategy Interface - Strategy Pattern
 * Defines the contract for interest calculation algorithms.
 */
public interface InterestStrategy {
    /**
     * Calculates interest for the given account
     * 
     * @param account The account to calculate interest for
     * @return The calculated interest amount
     */
    double calculateInterest(Account account);
}
