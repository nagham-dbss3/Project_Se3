package bank.accounts.types;

import bank.accounts.Account;

/**
 * InvestmentAccount - Investment and trading account
 * Features:
 * - Designed for trading stocks, bonds, and securities
 * - Investment-specific performance metrics
 * - Potentially higher returns but with risk
 * - Transaction fees may apply
 * - Portfolio diversification tracking
 */
public class InvestmentAccount extends Account {
    
    private double investmentValue; // Current value of investments
    private double totalInvested; // Total amount invested
    private double returns; // Gains/losses on investments
    private static final double ANNUAL_TARGET_RETURN = 0.07; // 7% target annual return
    private static final double TRANSACTION_FEE_RATE = 0.001; // 0.1% transaction fee
    
    /**
     * Constructor
     * 
     * @param accountHolder Name of the account holder
     * @param initialBalance Initial cash balance
     */
    public InvestmentAccount(String accountHolder, double initialBalance) {
        super(accountHolder, "INVESTMENT", initialBalance);
        this.investmentValue = 0.0;
        this.totalInvested = 0.0;
        this.returns = 0.0;
    }
    
    /**
     * Calculates investment returns based on target return rate
     * In a real system, this would be based on actual market prices
     */
    @Override
    public double calculateInterest() {
        double monthlyReturn = investmentValue * (ANNUAL_TARGET_RETURN / 12);
        returns += monthlyReturn;
        investmentValue += monthlyReturn;
        System.out.println("Investment returns calculated: $" + monthlyReturn);
        return monthlyReturn;
    }
    
    /**
     * Invests cash into securities (simulated)
     * In a real system, this would execute actual market orders
     */
    public boolean invest(double amount) {
        if (!getCurrentState().canWithdraw()) {
            System.out.println("Error: Cannot invest in " + getCurrentStateName() + " state.");
            return false;
        }
        
        if (amount <= 0) {
            System.out.println("Error: Investment amount must be positive.");
            return false;
        }
        
        if (amount > getBalance()) {
            System.out.println("Error: Insufficient cash balance for investment.");
            return false;
        }
        
        // Calculate transaction fee
        double transactionFee = amount * TRANSACTION_FEE_RATE;
        double netInvestment = amount - transactionFee;
        
        // Move cash from balance to investments
        super.withdraw(amount);
        investmentValue += netInvestment;
        totalInvested += netInvestment;
        
        System.out.println("Investment executed: $" + netInvestment + 
                         " (fee: $" + transactionFee + ")");
        return true;
    }
    
    /**
     * Liquidates investments back to cash
     */
    public boolean liquidate(double amount) {
        if (!getCurrentState().canDeposit()) {
            System.out.println("Error: Cannot liquidate in " + getCurrentStateName() + " state.");
            return false;
        }
        
        if (amount <= 0) {
            System.out.println("Error: Liquidation amount must be positive.");
            return false;
        }
        
        if (amount > investmentValue) {
            System.out.println("Error: Cannot liquidate more than current investment value.");
            return false;
        }
        
        // Calculate transaction fee
        double transactionFee = amount * TRANSACTION_FEE_RATE;
        double netProceeds = amount - transactionFee;
        
        // Move investments back to cash balance
        investmentValue -= amount;
        super.deposit(netProceeds);
        
        System.out.println("Liquidation executed: Received $" + netProceeds + 
                         " (fee: $" + transactionFee + ")");
        return true;
    }
    
    /**
     * Gets the total portfolio value (cash + investments)
     */
    public double getPortfolioValue() {
        return getBalance() + investmentValue;
    }
    
    /**
     * Gets the current investment return percentage
     */
    public double getReturnPercentage() {
        if (totalInvested == 0) return 0.0;
        return (returns / totalInvested) * 100;
    }
    
    /**
     * Gets the account details
     */
    @Override
    public String getAccountDetails() {
        return String.format(
            "Investment Account Details:%n" +
            "  Account ID: %s%n" +
            "  Holder: %s%n" +
            "  Cash Balance: $%.2f%n" +
            "  Investment Value: $%.2f%n" +
            "  Total Portfolio Value: $%.2f%n" +
            "  Total Invested: $%.2f%n" +
            "  Returns: $%.2f (%.2f%%)%n" +
            "  Target Annual Return: %.2f%%%n" +
            "  Status: %s",
            getAccountId(),
            getAccountHolder(),
            getBalance(),
            investmentValue,
            getPortfolioValue(),
            totalInvested,
            returns,
            getReturnPercentage(),
            ANNUAL_TARGET_RETURN * 100,
            getCurrentStateName()
        );
    }
}
