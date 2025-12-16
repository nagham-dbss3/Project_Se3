package bank.transactions.validator;

import bank.accounts.Account;
import bank.transactions.TransactionType;
import bank.transactions.history.TransactionLog;

public class TransactionValidator {
    private final double dailyWithdrawLimit;
    private final double dailyTransferLimit;

    public TransactionValidator(double dailyWithdrawLimit, double dailyTransferLimit) {
        this.dailyWithdrawLimit = dailyWithdrawLimit;
        this.dailyTransferLimit = dailyTransferLimit;
    }

    public ValidationResult validate(Account source,
                                     Account target,
                                     TransactionType type,
                                     double amount,
                                     TransactionLog log) {
        if (amount <= 0) return new ValidationResult(false, "Amount must be positive");

        switch (type) {
            case DEPOSIT:
                if (!source.getCurrentState().canDeposit()) return new ValidationResult(false, "Account state disallows deposit");
                return new ValidationResult(true, "OK");
            case WITHDRAW:
                if (!source.getCurrentState().canWithdraw()) return new ValidationResult(false, "Account state disallows withdraw");
                double withdrawnToday = log.getTodaysTotalFor(source, TransactionType.WITHDRAW);
                if (withdrawnToday + amount > dailyWithdrawLimit) return new ValidationResult(false, "Daily withdraw limit exceeded");
                return new ValidationResult(true, "OK");
            case TRANSFER:
                if (!source.getCurrentState().canTransfer()) return new ValidationResult(false, "Account state disallows transfer");
                if (target == null) return new ValidationResult(false, "Target account required");
                double transferredToday = log.getTodaysTotalFor(source, TransactionType.TRANSFER);
                if (transferredToday + amount > dailyTransferLimit) return new ValidationResult(false, "Daily transfer limit exceeded");
                return new ValidationResult(true, "OK");
            default:
                return new ValidationResult(false, "Unsupported transaction type");
        }
    }
}

