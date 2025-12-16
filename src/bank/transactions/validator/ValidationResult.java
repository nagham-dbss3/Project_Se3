package bank.transactions.validator;

public class ValidationResult {
    private final boolean ok;
    private final String message;

    public ValidationResult(boolean ok, String message) {
        this.ok = ok;
        this.message = message;
    }

    public boolean isOk() {
        return ok;
    }

    public String getMessage() {
        return message;
    }
}

