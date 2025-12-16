package bank.transactions.handlers;

public class ApprovalResult {
    private final boolean approved;
    private final ApprovalLevel level;

    public ApprovalResult(boolean approved, ApprovalLevel level) {
        this.approved = approved;
        this.level = level;
    }

    public boolean isApproved() {
        return approved;
    }

    public ApprovalLevel getLevel() {
        return level;
    }
}

