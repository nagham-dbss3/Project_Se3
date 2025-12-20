package bank.transactions.history;

import bank.accounts.Account;
import bank.transactions.TransactionType;
import bank.users.Role;
import java.time.LocalDateTime;

public class TransactionRecord {
    private final String transactionId;
    private final TransactionType type;
    private final Account sourceAccount;
    private final Account targetAccount;
    private final LocalDateTime timestamp;
    private final double amount;
    private final String initiatedBy;
    private final Role initiatedByRole;
    private final boolean success;
    private final String failureReason;
    private final String approvedByLevel;

    public TransactionRecord(String transactionId,
                             TransactionType type,
                             Account sourceAccount,
                             Account targetAccount,
                             LocalDateTime timestamp,
                             double amount,
                             String initiatedBy,
                             Role initiatedByRole,
                             boolean success,
                             String failureReason,
                             String approvedByLevel) {
        this.transactionId = transactionId;
        this.type = type;
        this.sourceAccount = sourceAccount;
        this.targetAccount = targetAccount;
        this.timestamp = timestamp;
        this.amount = amount;
        this.initiatedBy = initiatedBy;
        this.initiatedByRole = initiatedByRole;
        this.success = success;
        this.failureReason = failureReason;
        this.approvedByLevel = approvedByLevel;
    }

    public String getTransactionId() {
        return transactionId;
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

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public double getAmount() {
        return amount;
    }

    public String getInitiatedBy() {
        return initiatedBy;
    }

    public Role getInitiatedByRole() {
        return initiatedByRole;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public String getApprovedByLevel() {
        return approvedByLevel;
    }
}

