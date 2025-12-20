package bank.accounts.decorators;

import bank.accounts.Account;

public class OverdraftProtection extends AccountDecorator {
    
    private double overdraftLimit;
    
    public OverdraftProtection(Account decoratedAccount, double overdraftLimit) {
        super(decoratedAccount);
        this.overdraftLimit = overdraftLimit;
    }
    
    @Override
    public boolean withdraw(double amount) {
        // Standard check: is amount positive?
        if (amount <= 0) {
            System.out.println("Error: Withdrawal amount must be positive.");
            return false;
        }
        
        // Standard check: state permissions
        if (!getCurrentState().canWithdraw()) {
            System.out.println("Error: Cannot withdraw in " + getCurrentStateName() + " state.");
            return false;
        }
        
        double currentBalance = getBalance();
        
        // Check if withdrawal is possible with overdraft
        if (currentBalance - amount < -overdraftLimit) {
            System.out.println("Error: Overdraft limit exceeded. Limit: " + overdraftLimit + ", Balance: " + currentBalance);
            return false;
        }
        
        // If balance is sufficient, use standard withdraw
        if (currentBalance >= amount) {
            return super.withdraw(amount);
        }
        
        // If using overdraft
        System.out.println(">>> Overdraft Protection Activated <<<");
        double newBalance = currentBalance - amount;
        setBalance(newBalance);
        System.out.println("Successfully withdrew " + amount + " with overdraft. New Balance: " + newBalance);
        notifyObservers("Overdraft withdrawal: " + amount + ". New Balance: " + newBalance);
        return true;
    }
    
    @Override
    public String getAccountDetails() {
        return super.getAccountDetails() + " [Overdraft Protection: $" + overdraftLimit + "]";
    }
}
