package bank.interest;

import bank.accounts.Account;
import bank.accounts.types.InvestmentAccount;

/**
 * InvestmentInterest - Strategy implementation for Investment Accounts
 * Calculates returns based on investment value and target return rate.
 */
public class InvestmentInterest implements InterestStrategy {
    
    private double annualTargetReturn;

    public InvestmentInterest(double annualTargetReturn) {
        this.annualTargetReturn = annualTargetReturn;
    }

    // Default constructor with 7% return
    public InvestmentInterest() {
        this(0.07);
    }

    @Override
    public double calculateInterest(Account account) {
        if (account instanceof InvestmentAccount) {
            InvestmentAccount invAccount = (InvestmentAccount) account;
            // Assuming getInvestmentValue() exists. If not, I'll add it.
            return invAccount.getInvestmentValue() * (annualTargetReturn / 12);
        }
        return 0.0;
    }
}
