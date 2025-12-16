package bank.accounts.composite;

/**
 * AccountComponent - Component interface for Composite Pattern
 * Defines the common interface for both leaf accounts and account groups.
 * This allows treating individual accounts and groups of accounts uniformly.
 * 
 * Design Pattern: Composite Pattern
 * - Provides a tree structure for organizing accounts
 * - Allows uniform treatment of leaf nodes and composite nodes
 * - Enables recursive operations on account hierarchies
 */
public interface AccountComponent {
    
    /**
     * Gets the name of this component
     */
    String getComponentName();
    
    /**
     * Gets the total balance of this component and all children
     */
    double getTotalBalance();
    
    /**
     * Deposits amount to this component
     * For leaf accounts: deposits to that account
     * For groups: distributes deposit across all child accounts
     */
    boolean deposit(double amount);
    
    /**
     * Withdraws amount from this component
     * For leaf accounts: withdraws from that account
     * For groups: withdraws from all child accounts proportionally
     */
    boolean withdraw(double amount);
    
    /**
     * Gets detailed information about this component
     */
    String getComponentDetails();
    
    /**
     * Displays the tree structure of this component and all children
     * Used for visualizing the account hierarchy
     */
    void displayHierarchy(int depth);
}
