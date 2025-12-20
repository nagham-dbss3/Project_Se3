package bank.admin;

import bank.users.User;

public class AccessControl {
    
    public boolean checkPermission(User user, String requiredPermission) {
        if (user == null) return false;
        return user.hasPermission(requiredPermission);
    }
    
    public boolean canPerformTransaction(User user, double amount) {
        // Simple rule-based access control based on role and amount
        switch (user.getRole()) {
            case ADMIN: return true;
            case MANAGER: return amount <= 100000.0;
            case TELLER: return amount <= 50000.0;
            case CUSTOMER: return amount <= 10000.0;
            default: return false;
        }
    }
}
