package bank.transactions.handlers;

import bank.transactions.Transaction;

public abstract class ApprovalHandler {
    private ApprovalHandler next;

    public ApprovalHandler setNext(ApprovalHandler next) {
        this.next = next;
        return next;
    }

    public ApprovalResult handle(Transaction tx) {
        if (approve(tx)) {
            return new ApprovalResult(true, getLevel());
        }
        if (next != null) {
            return next.handle(tx);
        }
        return new ApprovalResult(false, null);
    }

    protected abstract boolean approve(Transaction tx);
    protected abstract ApprovalLevel getLevel();
}

