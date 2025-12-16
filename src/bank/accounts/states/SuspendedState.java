package bank.accounts.states;

import bank.accounts.AccountState;

/**
 * SuspendedState - Account is suspended
 * In this state, the account can only receive deposits but cannot withdraw or transfer.
 * Typically due to regulatory or compliance reasons.
 */
public class SuspendedState implements AccountState {
    
    @Override
    public boolean canDeposit() {
        return true;
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
        return "SUSPENDED";
    }
    
    @Override
    public String getDescription() {
        return "Account is suspended. Deposits are allowed, but withdrawals and transfers are prohibited.";
    }
}
