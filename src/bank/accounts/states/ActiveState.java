package bank.accounts.states;

import bank.accounts.AccountState;

/**
 * ActiveState - Account is fully functional
 * In this state, the account can perform all normal banking operations.
 */
public class ActiveState implements AccountState {
    
    @Override
    public boolean canDeposit() {
        return true;
    }
    
    @Override
    public boolean canWithdraw() {
        return true;
    }
    
    @Override
    public boolean canTransfer() {
        return true;
    }
    
    @Override
    public String getStateName() {
        return "ACTIVE";
    }
    
    @Override
    public String getDescription() {
        return "Account is active and fully operational. All transactions are allowed.";
    }
}
