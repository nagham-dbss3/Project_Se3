package bank.accounts.composite;

import java.util.ArrayList;
import java.util.List;

/**
 * AccountGroup - Composite node in the Composite Pattern
 * Represents a group of accounts (can contain both leaves and other composites).
 * Examples: Family accounts, Business accounts, Joint accounts.
 * 
 * This class allows organizing accounts hierarchically while treating the entire
 * group as a single unit for operations like deposits and withdrawals.
 */
public class AccountGroup implements AccountComponent {
    
    private String groupName;
    private List<AccountComponent> children;
    
    /**
     * Constructor
     * 
     * @param groupName Name of the account group
     */
    public AccountGroup(String groupName) {
        this.groupName = groupName;
        this.children = new ArrayList<>();
    }
    
    /**
     * Adds a component (leaf or group) to this group
     * 
     * @param component The AccountComponent to add
     */
    public void addComponent(AccountComponent component) {
        if (component != null && !children.contains(component)) {
            children.add(component);
            System.out.println("Added " + component.getComponentName() + " to group " + groupName);
        }
    }
    
    /**
     * Removes a component from this group
     * 
     * @param component The AccountComponent to remove
     */
    public void removeComponent(AccountComponent component) {
        if (children.remove(component)) {
            System.out.println("Removed " + component.getComponentName() + " from group " + groupName);
        }
    }
    
    /**
     * Gets all child components
     */
    public List<AccountComponent> getChildren() {
        return new ArrayList<>(children);
    }
    
    /**
     * Gets the number of immediate children
     */
    public int getChildCount() {
        return children.size();
    }
    
    @Override
    public String getComponentName() {
        return groupName;
    }
    
    @Override
    public double getTotalBalance() {
        double total = 0.0;
        for (AccountComponent child : children) {
            total += child.getTotalBalance();
        }
        return total;
    }
    
    @Override
    public boolean deposit(double amount) {
        if (children.isEmpty()) {
            System.out.println("Error: Cannot deposit to empty group " + groupName);
            return false;
        }
        
        if (amount <= 0) {
            System.out.println("Error: Deposit amount must be positive.");
            return false;
        }
        
        // Distribute deposit equally among all children
        double amountPerChild = amount / children.size();
        boolean allSuccess = true;
        
        System.out.println("Distributing deposit of $" + amount + " across " + 
                         children.size() + " accounts...");
        
        for (AccountComponent child : children) {
            if (!child.deposit(amountPerChild)) {
                allSuccess = false;
            }
        }
        
        if (allSuccess) {
            System.out.println("Successfully deposited $" + amount + " to group " + groupName);
        }
        
        return allSuccess;
    }
    
    @Override
    public boolean withdraw(double amount) {
        if (children.isEmpty()) {
            System.out.println("Error: Cannot withdraw from empty group " + groupName);
            return false;
        }
        
        if (amount <= 0) {
            System.out.println("Error: Withdrawal amount must be positive.");
            return false;
        }
        
        // Check if group has sufficient total balance
        if (getTotalBalance() < amount) {
            System.out.println("Error: Insufficient balance in group. " +
                             "Available: $" + getTotalBalance() + 
                             ", Requested: $" + amount);
            return false;
        }
        
        // Withdraw proportionally from each child based on their balance
        boolean allSuccess = true;
        double totalBalance = getTotalBalance();
        
        System.out.println("Withdrawing $" + amount + " from group " + groupName + 
                         " (proportionally distributed)...");
        
        for (AccountComponent child : children) {
            double childBalance = child.getTotalBalance();
            double proportion = (childBalance / totalBalance);
            double childWithdrawal = amount * proportion;
            
            if (!child.withdraw(childWithdrawal)) {
                allSuccess = false;
            }
        }
        
        if (allSuccess) {
            System.out.println("Successfully withdrew $" + amount + " from group " + groupName);
        }
        
        return allSuccess;
    }
    
    @Override
    public String getComponentDetails() {
        StringBuilder sb = new StringBuilder();
        sb.append("Account Group: ").append(groupName).append("\n");
        sb.append("Total Balance: $").append(String.format("%.2f", getTotalBalance())).append("\n");
        sb.append("Number of Direct Children: ").append(children.size()).append("\n");
        sb.append("Members:\n");
        
        for (AccountComponent child : children) {
            sb.append("  - ").append(child.getComponentName()).append("\n");
        }
        
        return sb.toString();
    }
    
    @Override
    public void displayHierarchy(int depth) {
        String indent = "  ".repeat(depth);
        System.out.println(indent + "📁 " + groupName);
        System.out.println(indent + "│  Total Balance: $" + String.format("%.2f", getTotalBalance()));
        System.out.println(indent + "│  Members: " + children.size());
        
        for (int i = 0; i < children.size(); i++) {
            AccountComponent child = children.get(i);
            child.displayHierarchy(depth + 1);
        }
    }
    
    @Override
    public String toString() {
        return groupName + " [" + children.size() + " members, $" + 
               String.format("%.2f", getTotalBalance()) + "]";
    }
}
