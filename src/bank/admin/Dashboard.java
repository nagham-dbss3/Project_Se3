package bank.admin;

import bank.accounts.Account;
import bank.users.User;
import bank.users.Role;
import java.util.List;

public class Dashboard {
    
    private ReportingService reportingService;
    private AccessControl accessControl;
    
    public Dashboard(ReportingService reportingService, AccessControl accessControl) {
        this.reportingService = reportingService;
        this.accessControl = accessControl;
    }
    
    public void showDashboard(User user, List<Account> accounts) {
        System.out.println("\n########################################");
        System.out.println("           BANKING DASHBOARD            ");
        System.out.println("########################################");
        System.out.println("Welcome, " + user.getName() + " [" + user.getRole() + "]");
        
        if (accessControl.checkPermission(user, "VIEW_REPORTS") || user.getRole() == Role.ADMIN) {
            System.out.println("\n[System Status: ONLINE]");
            System.out.println("[Alerts: None]");
            
            reportingService.generateAccountSummary(accounts);
            reportingService.generateDailyTransactionReport();
            
            System.out.println("\n[Audit Logs Access: GRANTED]");
            reportingService.generateAuditLog();
        } else {
            System.out.println("\n[Access Restricted] You do not have permission to view full dashboard reports.");
        }
        System.out.println("########################################\n");
    }
}
