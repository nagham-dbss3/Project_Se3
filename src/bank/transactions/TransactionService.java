package bank.transactions;

import bank.accounts.Account;
import bank.users.Role;
import bank.transactions.handlers.*;
import bank.transactions.history.TransactionLog;
import bank.transactions.history.TransactionRecord;
import bank.transactions.notification.NotificationService;
import bank.transactions.validator.TransactionValidator;
import bank.transactions.validator.ValidationResult;

public class TransactionService {
    private final TransactionValidator validator;
    private final TransactionLog log;
    private final NotificationService notification;
    private ApprovalHandler approvalChain;

    public TransactionService(TransactionValidator validator, TransactionLog log, NotificationService notification) {
        this.validator = validator;
        this.log = log;
        this.notification = notification;
        this.approvalChain = buildChain();
    }

    private ApprovalHandler buildChain() {
        ApprovalHandler auto = new AutoApproval();
        ApprovalHandler teller = auto.setNext(new TellerApproval());
        ApprovalHandler manager = teller.setNext(new ManagerApproval());
        manager.setNext(new AdminApproval());
        return auto;
    }

    public void setApprovalChain(ApprovalHandler root) {
        if (root != null) {
            this.approvalChain = root;
        }
    }

    public boolean deposit(Account account, double amount, String user, Role role) {
        Transaction tx = new Transaction(TransactionType.DEPOSIT, account, account, amount, user, role);
        ValidationResult vr = validator.validate(account, account, TransactionType.DEPOSIT, amount, log);
        if (!vr.isOk()) {
            log.addRecord(new TransactionRecord(tx.getId(), tx.getType(), tx.getSourceAccount(), tx.getTargetAccount(),
                    tx.getTimestamp(), tx.getAmount(), tx.getInitiatedBy(), tx.getInitiatedByRole(), false, vr.getMessage(), null));
            return false;
        }
        if (!hasPrivilege(role, amount)) {
            log.addRecord(new TransactionRecord(tx.getId(), tx.getType(), tx.getSourceAccount(), tx.getTargetAccount(),
                    tx.getTimestamp(), tx.getAmount(), tx.getInitiatedBy(), tx.getInitiatedByRole(), false, "Insufficient privileges", null));
            return false;
        }
        ApprovalResult ar = approvalChain.handle(tx);
        boolean ok = account.deposit(amount);
        log.addRecord(new TransactionRecord(tx.getId(), tx.getType(), tx.getSourceAccount(), tx.getTargetAccount(),
                tx.getTimestamp(), tx.getAmount(), tx.getInitiatedBy(), tx.getInitiatedByRole(), ok, ok ? null : "Execution failed",
                ar.isApproved() ? ar.getLevel().name() : null));
        if (amount >= 20000.0) notification.notify("Large deposit $" + amount + " by " + user);
        return ok;
    }

    public boolean withdraw(Account account, double amount, String user, Role role) {
        Transaction tx = new Transaction(TransactionType.WITHDRAW, account, null, amount, user, role);
        ValidationResult vr = validator.validate(account, null, TransactionType.WITHDRAW, amount, log);
        if (!vr.isOk()) {
            log.addRecord(new TransactionRecord(tx.getId(), tx.getType(), tx.getSourceAccount(), tx.getTargetAccount(),
                    tx.getTimestamp(), tx.getAmount(), tx.getInitiatedBy(), tx.getInitiatedByRole(), false, vr.getMessage(), null));
            return false;
        }
        if (!hasPrivilege(role, amount)) {
            log.addRecord(new TransactionRecord(tx.getId(), tx.getType(), tx.getSourceAccount(), tx.getTargetAccount(),
                    tx.getTimestamp(), tx.getAmount(), tx.getInitiatedBy(), tx.getInitiatedByRole(), false, "Insufficient privileges", null));
            return false;
        }
        ApprovalResult ar = approvalChain.handle(tx);
        boolean ok = account.withdraw(amount);
        log.addRecord(new TransactionRecord(tx.getId(), tx.getType(), tx.getSourceAccount(), tx.getTargetAccount(),
                tx.getTimestamp(), tx.getAmount(), tx.getInitiatedBy(), tx.getInitiatedByRole(), ok, ok ? null : "Execution failed",
                ar.isApproved() ? ar.getLevel().name() : null));
        if (amount >= 20000.0) notification.notify("Large withdrawal $" + amount + " by " + user);
        return ok;
    }

    public boolean transfer(Account from, Account to, double amount, String user, Role role) {
        Transaction tx = new Transaction(TransactionType.TRANSFER, from, to, amount, user, role);
        ValidationResult vr = validator.validate(from, to, TransactionType.TRANSFER, amount, log);
        if (!vr.isOk()) {
            log.addRecord(new TransactionRecord(tx.getId(), tx.getType(), tx.getSourceAccount(), tx.getTargetAccount(),
                    tx.getTimestamp(), tx.getAmount(), tx.getInitiatedBy(), tx.getInitiatedByRole(), false, vr.getMessage(), null));
            return false;
        }
        if (!hasPrivilege(role, amount)) {
            log.addRecord(new TransactionRecord(tx.getId(), tx.getType(), tx.getSourceAccount(), tx.getTargetAccount(),
                    tx.getTimestamp(), tx.getAmount(), tx.getInitiatedBy(), tx.getInitiatedByRole(), false, "Insufficient privileges", null));
            return false;
        }
        ApprovalResult ar = approvalChain.handle(tx);
        boolean ok = from.transfer(to, amount);
        log.addRecord(new TransactionRecord(tx.getId(), tx.getType(), tx.getSourceAccount(), tx.getTargetAccount(),
                tx.getTimestamp(), tx.getAmount(), tx.getInitiatedBy(), tx.getInitiatedByRole(), ok, ok ? null : "Execution failed",
                ar.isApproved() ? ar.getLevel().name() : null));
        if (amount >= 20000.0) {
            String msg = "Large transfer $" + amount + " by " + user;
            notification.notify(msg);
            from.notifyObservers(msg);
        }
        return ok;
    }

    private boolean hasPrivilege(Role role, double amount) {
        switch (role) {
            case CUSTOMER: return amount <= 10000.0;
            case TELLER: return amount <= 50000.0;
            case MANAGER: return amount <= 100000.0;
            case ADMIN: return true;
            default: return false;
        }
    }
}
