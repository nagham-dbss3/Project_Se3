package bank.accounts.types;

import bank.accounts.Account;

/**
 * LoanAccount - Loan/Credit account
 * Features:
 * - Represents an outstanding loan or line of credit
 * - Negative balance represents amount owed
 * - Interest accrual on the loan amount
 * - Minimum monthly payment requirements
 * - Fixed or variable interest rates
 */
public class LoanAccount extends Account {
    
    private double interestRate; // Monthly interest rate
    private double principalAmount; // Original loan amount
    private int monthsRemaining; // Remaining payment period
    private double minimumPayment;
    
    /**
     * Constructor
     * 
     * @param accountHolder Name of the account holder
     * @param principalAmount The original loan amount
     * @param annualInterestRate Annual interest rate (e.g., 0.05 for 5%)
     * @param monthsToRepay Number of months to repay the loan
     */
    public LoanAccount(String accountHolder, double principalAmount, 
                       double annualInterestRate, int monthsToRepay) {
        super(accountHolder, "LOAN", -principalAmount); // Negative balance = amount owed
        this.principalAmount = principalAmount;
        this.interestRate = annualInterestRate / 12 / 100; // Convert to monthly rate
        this.monthsRemaining = monthsToRepay;
        this.minimumPayment = calculateMinimumPayment();
        
        // Set strategy
        setInterestStrategy(new bank.interest.LoanInterest(annualInterestRate));
    }
    
    /**
     * Calculates the minimum monthly payment using amortization formula
     */
    private double calculateMinimumPayment() {
        // Using the loan payment formula
        double rate = interestRate;
        double balance = Math.abs(getBalance());
        
        if (rate == 0) {
            return balance / monthsRemaining;
        }
        
        return (balance * rate * Math.pow(1 + rate, monthsRemaining)) / 
               (Math.pow(1 + rate, monthsRemaining) - 1);
    }
    
    /**
     * Calculates monthly interest accrual on the loan
     * Interest is added to the outstanding balance
     */
    @Override
    public double calculateInterest() {
        double monthlyInterest = super.calculateInterest();
        
        // Add interest (negative because it's a loan)
        if (monthlyInterest > 0) {
            // Decrease balance (increase debt) directly as withdraw is blocked/restricted
            double newBalance = getBalance() - monthlyInterest;
            setBalance(newBalance);
            System.out.println("Interest accrued: $" + monthlyInterest);
        }
        return monthlyInterest;
    }
    
    /**
     * Makes a payment on the loan (deposit to the account reduces debt)
     */
    @Override
    public boolean deposit(double amount) {
        if (amount <= 0) {
            System.out.println("Error: Payment amount must be positive.");
            return false;
        }
        
        double debtBefore = Math.abs(getBalance());
        boolean success = super.deposit(amount);
        
        if (success) {
            double debtAfter = Math.abs(getBalance());
            System.out.println("Loan payment received. Debt reduced from $" + debtBefore 
                             + " to $" + debtAfter);
            monthsRemaining--;
        }
        
        return success;
    }
    
    /**
     * Loan accounts don't allow regular withdrawals (you can't borrow more)
     */
    @Override
    public boolean withdraw(double amount) {
        System.out.println("Error: Cannot withdraw from a loan account. Payments must be made as deposits.");
        return false;
    }
    
    /**
     * Gets the account details
     */
    @Override
    public String getAccountDetails() {
        return String.format(
            "Loan Account Details:%n" +
            "  Account ID: %s%n" +
            "  Holder: %s%n" +
            "  Principal Amount: $%.2f%n" +
            "  Amount Owed: $%.2f%n" +
            "  Annual Interest Rate: %.2f%%%n" +
            "  Minimum Payment: $%.2f%n" +
            "  Months Remaining: %d%n" +
            "  Status: %s",
            getAccountId(),
            getAccountHolder(),
            principalAmount,
            Math.abs(getBalance()),
            interestRate * 12 * 100,
            minimumPayment,
            monthsRemaining,
            getCurrentStateName()
        );
    }
    
    /**
     * Gets the remaining balance (amount still owed)
     */
    public double getAmountOwed() {
        return Math.abs(getBalance());
    }
}
