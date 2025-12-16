package bank.accounts;

/**
 * AccountState Interface - State Pattern
 * Defines the behavior contract for different account states.
 * Each concrete state implements specific behaviors for that state.
 */
public interface AccountState {
    
    /**
     * Checks if the account can perform deposits in this state
     */
    boolean canDeposit();
    
    /**
     * Checks if the account can perform withdrawals in this state
     */
    boolean canWithdraw();
    
    /**
     * Checks if the account can perform transfers in this state
     */
    boolean canTransfer();
    
    /**
     * Gets the state name
     */
    String getStateName();
    
    /**
     * Gets a description of the current state
     */
    String getDescription();
}
