package bank.accounts.decorators;

import bank.accounts.Account;

public class PremiumAccount extends AccountDecorator {
    
    public PremiumAccount(Account decoratedAccount) {
        super(decoratedAccount);
    }
    
    @Override
    public String getAccountDetails() {
        return "PREMIUM " + super.getAccountDetails() + " ⭐";
    }
    
    @Override
    public boolean transfer(Account targetAccount, double amount) {
        System.out.println(">>> Processing Premium Priority Transfer <<<");
        return super.transfer(targetAccount, amount);
    }
    
    @Override
    public double calculateInterest() {
        // Bonus interest for premium accounts
        double baseInterest = super.calculateInterest();
        return baseInterest + (baseInterest * 0.10); // 10% bonus
    }
}
