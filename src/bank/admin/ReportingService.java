package bank.admin;

import bank.accounts.Account;
import bank.transactions.history.TransactionLog;
import bank.transactions.history.TransactionRecord;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReportingService {
    
    private TransactionLog transactionLog;
    
    public ReportingService(TransactionLog log) {
        this.transactionLog = log;
    }
    
    public void generateAccountSummary(List<Account> accounts) {
        System.out.println("\n=== Account Summary Report ===");
        System.out.println("Total Accounts: " + accounts.size());
        double totalBalance = accounts.stream().mapToDouble(Account::getBalance).sum();
        System.out.println("Total Liquidity: $" + totalBalance);
        
        Map<String, Long> byType = accounts.stream()
            .collect(Collectors.groupingBy(a -> a.getClass().getSimpleName(), Collectors.counting()));
            
        byType.forEach((type, count) -> System.out.println(type + ": " + count));
        System.out.println("==============================");
    }
    
    public void generateDailyTransactionReport() {
        if (transactionLog != null) {
            transactionLog.printDailyReport(LocalDate.now());
        }
    }
    
    public void generateAuditLog() {
        System.out.println("\n=== Audit Log (Recent Activity) ===");
        if (transactionLog != null) {
            List<TransactionRecord> records = transactionLog.getAllRecords();
            records.stream()
                   .sorted((r1, r2) -> r2.getTimestamp().compareTo(r1.getTimestamp())) // Newest first
                   .limit(10)
                   .forEach(r -> System.out.println(r.getTimestamp() + " | " + r.getType() + " | " + r.getInitiatedBy() + " (" + r.getInitiatedByRole() + ") | " + (r.isSuccess() ? "SUCCESS" : "FAILED: " + r.getFailureReason())));
        }
        System.out.println("===================================");
    }
}
