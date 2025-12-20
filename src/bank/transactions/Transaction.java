package bank.transactions;

import bank.accounts.Account;
import bank.users.Role;
import java.time.LocalDateTime;
import java.util.UUID;

public class Transaction {
    private final String id;
    private final TransactionType type;
    private final Account sourceAccount;
    private final Account targetAccount;
    private final double amount;
    private final LocalDateTime timestamp;
    private final String initiatedBy;
    private final Role initiatedByRole;

    public Transaction(TransactionType type,
                       Account sourceAccount,
                       Account targetAccount,
                       double amount,
                       String initiatedBy,
                       Role initiatedByRole) {
        this.id = UUID.randomUUID().toString();
        this.type = type;
        this.sourceAccount = sourceAccount;
        this.targetAccount = targetAccount;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
        this.initiatedBy = initiatedBy;
        this.initiatedByRole = initiatedByRole;
    }

    public String getId() {
        return id;
    }

    public TransactionType getType() {
        return type;
    }

    public Account getSourceAccount() {
        return sourceAccount;
    }

    public Account getTargetAccount() {
        return targetAccount;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getInitiatedBy() {
        return initiatedBy;
    }

    public Role getInitiatedByRole() {
        return initiatedByRole;
    }
}

