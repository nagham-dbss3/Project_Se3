package bank.accounts.states;

import bank.accounts.AccountState;

/**
 * FrozenState - Account is frozen temporarily
 * In this state, the account cannot perform any transactions.
 * Typically due to security concerns or suspicious activity.
 */
public class FrozenState implements AccountState {
    
    @Override
    public boolean canDeposit() {
        return false;
    }
    
    @Override
    public boolean canWithdraw() {
        return false;
    }
    
    @Override
    public boolean canTransfer() {
        return false;
    }
    
    @Override
    public String getStateName() {
        return "FROZEN";
    }
    
    @Override
    public String getDescription() {
        return "Account is frozen. No transactions are allowed. Contact customer service for assistance.";
    }
}
