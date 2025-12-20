package bank.users;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String userId;
    private String name;
    private Role role;
    private List<String> permissions;

    public User(String userId, String name, Role role) {
        this.userId = userId;
        this.name = name;
        this.role = role;
        this.permissions = new ArrayList<>();
        assignDefaultPermissions();
    }

    private void assignDefaultPermissions() {
        switch (role) {
            case ADMIN:
                permissions.add("ALL_ACCESS");
                break;
            case MANAGER:
                permissions.add("VIEW_REPORTS");
                permissions.add("APPROVE_LARGE_TRANSACTIONS");
                permissions.add("MANAGE_USERS");
                break;
            case TELLER:
                permissions.add("PROCESS_TRANSACTION");
                permissions.add("VIEW_ACCOUNTS");
                break;
            case CUSTOMER:
                permissions.add("VIEW_OWN_ACCOUNT");
                permissions.add("INITIATE_TRANSACTION");
                break;
        }
    }

    public String getUserId() { return userId; }
    public String getName() { return name; }
    public Role getRole() { return role; }
    public List<String> getPermissions() { return permissions; }
    
    public boolean hasPermission(String permission) {
        return permissions.contains("ALL_ACCESS") || permissions.contains(permission);
    }
}
