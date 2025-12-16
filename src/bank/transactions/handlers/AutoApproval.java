package bank.transactions.handlers;

import bank.transactions.Transaction;

public class AutoApproval extends ApprovalHandler {
    protected boolean approve(Transaction tx) {
        return tx.getAmount() < 1000.0;
    }
    protected ApprovalLevel getLevel() {
        return ApprovalLevel.AUTO;
    }
}

