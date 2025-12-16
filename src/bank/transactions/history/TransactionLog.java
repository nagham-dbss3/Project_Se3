package bank.transactions.history;

import bank.accounts.Account;
import bank.transactions.TransactionType;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionLog {
    private final List<TransactionRecord> records = new ArrayList<>();

    public synchronized void addRecord(TransactionRecord record) {
        records.add(record);
        System.out.println("Transaction logged: " + record.getTransactionId() + " " + record.getType() + " $" + record.getAmount());
    }

    public List<TransactionRecord> getAllRecords() {
        return new ArrayList<>(records);
    }

    public double getTodaysTotalFor(Account account, TransactionType type) {
        LocalDate today = LocalDate.now();
        return records.stream()
                .filter(r -> r.isSuccess())
                .filter(r -> r.getType() == type)
                .filter(r -> r.getTimestamp().toLocalDate().equals(today))
                .filter(r -> {
                    if (type == TransactionType.DEPOSIT) return r.getTargetAccount() == account || r.getSourceAccount() == account;
                    return r.getSourceAccount() == account;
                })
                .mapToDouble(TransactionRecord::getAmount)
                .sum();
    }

    public void printDailyReport(LocalDate date) {
        List<TransactionRecord> dayRecords = records.stream()
                .filter(r -> r.getTimestamp().toLocalDate().equals(date))
                .collect(Collectors.toList());
        System.out.println("Daily Transaction Report: " + date);
        System.out.println("Count: " + dayRecords.size());
        double total = dayRecords.stream().mapToDouble(TransactionRecord::getAmount).sum();
        System.out.println("Total Amount: $" + total);
        long failures = dayRecords.stream().filter(r -> !r.isSuccess()).count();
        System.out.println("Failures: " + failures);
    }
}

