package bank.transactions.handlers;

import bank.transactions.Transaction;

public class TellerApproval extends ApprovalHandler {
    protected boolean approve(Transaction tx) {
        double a = tx.getAmount();
        return a >= 1000.0 && a < 10000.0;
    }
    protected ApprovalLevel getLevel() {
        return ApprovalLevel.TELLER;
    }
}

