package bank.accounts.decorators;

import bank.accounts.Account;

public class InsuranceFeature extends AccountDecorator {
    
    private double insuranceCostPerTransaction = 0.50;
    
    public InsuranceFeature(Account decoratedAccount) {
        super(decoratedAccount);
    }
    
    @Override
    public boolean withdraw(double amount) {
        System.out.println(">>> Insured Transaction (Fee: $" + insuranceCostPerTransaction + ") <<<");
        // Deduct insurance fee
        double currentBalance = getBalance();
        // Check if we can afford amount + fee
        // Note: This simple check doesn't account for overdraft protection if it wraps it.
        // The correct way is to just let the wrapped account handle the logic.
        // But we want to deduct the fee.
        
        // Strategy: Delegate withdraw for the main amount. If successful, deduct fee.
        boolean success = super.withdraw(amount);
        
        if (success) {
            // Deduct fee silently or explicitly
            // We use setBalance to avoid triggering another withdraw event logic
            setBalance(getBalance() - insuranceCostPerTransaction);
            System.out.println("Insurance fee deducted: $" + insuranceCostPerTransaction);
        }
        
        return success;
    }
    
    @Override
    public String getAccountDetails() {
        return super.getAccountDetails() + " [Insured]";
    }
}
