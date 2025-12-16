package bank.transactions.handlers;

import bank.transactions.Transaction;

public class ManagerApproval extends ApprovalHandler {
    protected boolean approve(Transaction tx) {
        double a = tx.getAmount();
        return a >= 10000.0 && a < 50000.0;
    }
    protected ApprovalLevel getLevel() {
        return ApprovalLevel.MANAGER;
    }
}

