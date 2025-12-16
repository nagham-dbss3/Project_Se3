package bank.accounts.types;

import bank.accounts.Account;

/**
 * CheckingAccount - Checking account for daily transactions
 * Features:
 * - Designed for frequent transactions
 * - May include overdraft protection
 * - Check writing capability
 * - No interest accrual
 * - Low or no minimum balance requirements
 */
public class CheckingAccount extends Account {
    
    private boolean overdraftProtectionEnabled;
    private static final double OVERDRAFT_LIMIT = 500.0;
    private double overdraftUsed;
    
    /**
     * Constructor
     * 
     * @param accountHolder Name of the account holder
     * @param initialBalance Initial balance
     */
    public CheckingAccount(String accountHolder, double initialBalance) {
        super(accountHolder, "CHECKING", initialBalance);
        this.overdraftProtectionEnabled = true;
        this.overdraftUsed = 0.0;
    }
    
    /**
     * Calculates interest for checking account
     * Most checking accounts don't earn interest, so returns 0
     */
    @Override
    public double calculateInterest() {
        // Checking accounts typically don't earn interest
        return 0.0;
    }
    
    /**
     * Gets the account details
     */
    @Override
    public String getAccountDetails() {
        return String.format(
            "Checking Account Details:%n" +
            "  Account ID: %s%n" +
            "  Holder: %s%n" +
            "  Balance: $%.2f%n" +
            "  Overdraft Protection: %s%n" +
            "  Overdraft Limit: $%.2f%n" +
            "  Overdraft Used: $%.2f%n" +
            "  Status: %s",
            getAccountId(),
            getAccountHolder(),
            getBalance(),
            overdraftProtectionEnabled ? "Enabled" : "Disabled",
            OVERDRAFT_LIMIT,
            overdraftUsed,
            getCurrentStateName()
        );
    }
    
    /**
     * Enables overdraft protection for this checking account
     */
    public void enableOverdraftProtection() {
        this.overdraftProtectionEnabled = true;
        System.out.println("Overdraft protection enabled for account " + getAccountId());
    }
    
    /**
     * Disables overdraft protection for this checking account
     */
    public void disableOverdraftProtection() {
        this.overdraftProtectionEnabled = false;
        System.out.println("Overdraft protection disabled for account " + getAccountId());
    }
    
    /**
     * Overrides withdraw to allow overdraft if protection is enabled
     */
    @Override
    public boolean withdraw(double amount) {
        if (!getCurrentState().canWithdraw()) {
            System.out.println("Error: Cannot withdraw in " + getCurrentStateName() + " state. " 
                             + getCurrentState().getDescription());
            return false;
        }
        
        if (amount <= 0) {
            System.out.println("Error: Withdrawal amount must be positive.");
            return false;
        }
        
        // Check if withdrawal can be covered by balance
        if (amount <= getBalance()) {
            return super.withdraw(amount);
        }
        
        // Check if overdraft can cover the difference
        if (overdraftProtectionEnabled) {
            double shortfall = amount - getBalance();
            if (overdraftUsed + shortfall <= OVERDRAFT_LIMIT) {
                // Withdraw what we can from balance first
                double fromBalance = getBalance();
                super.withdraw(fromBalance);
                
                // Use overdraft for the rest
                overdraftUsed += shortfall;
                System.out.println("Withdrawal of " + amount + " completed using overdraft. " 
                                 + "Overdraft used: $" + overdraftUsed);
                return true;
            } else {
                System.out.println("Error: Withdrawal exceeds available balance and overdraft limit.");
                return false;
            }
        } else {
            System.out.println("Error: Insufficient funds and overdraft protection is disabled.");
            return false;
        }
    }
    
    /**
     * Repays overdraft
     */
    public boolean repayOverdraft(double amount) {
        if (amount <= 0) {
            System.out.println("Error: Repayment amount must be positive.");
            return false;
        }
        
        double previousOverdraft = overdraftUsed;
        overdraftUsed = Math.max(0, overdraftUsed - amount);
        System.out.println("Overdraft repaid. Previous overdraft: $" + previousOverdraft 
                         + ", Remaining: $" + overdraftUsed);
        return true;
    }
}
