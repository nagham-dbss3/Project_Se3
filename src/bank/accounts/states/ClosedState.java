package bank.accounts.states;

import bank.accounts.AccountState;

/**
 * ClosedState - Account is closed
 * In this state, the account is inactive and no transactions are allowed.
 * This is typically the final state in an account's lifecycle.
 */
public class ClosedState implements AccountState {
    
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
        return "CLOSED";
    }
    
    @Override
    public String getDescription() {
        return "Account is closed. No transactions are allowed. Contact customer service for archived information.";
    }
}
