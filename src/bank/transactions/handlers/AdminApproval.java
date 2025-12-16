package bank.transactions.handlers;

import bank.transactions.Transaction;

public class AdminApproval extends ApprovalHandler {
    protected boolean approve(Transaction tx) {
        return tx.getAmount() >= 50000.0;
    }
    protected ApprovalLevel getLevel() {
        return ApprovalLevel.ADMIN;
    }
}

