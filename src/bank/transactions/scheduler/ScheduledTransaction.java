package bank.transactions.scheduler;

import bank.accounts.Account;
import bank.transactions.TransactionService;
import bank.transactions.TransactionType;
import bank.users.Role;
import java.time.LocalDate;

public class ScheduledTransaction {
    private final TransactionType type;
    private final Account source;
    private final Account target;
    private final double amount;
    private final String user;
    private final Role role;
    private LocalDate nextRun;
    private final int intervalDays;

    public ScheduledTransaction(TransactionType type, Account source, Account target, double amount,
                                String user, Role role, LocalDate firstRun, int intervalDays) {
        this.type = type;
        this.source = source;
        this.target = target;
        this.amount = amount;
        this.user = user;
        this.role = role;
        this.nextRun = firstRun;
        this.intervalDays = intervalDays;
    }

    public void runIfDue(LocalDate today, TransactionService service) {
        if (!today.isBefore(nextRun)) {
            switch (type) {
                case DEPOSIT: service.deposit(source, amount, user, role); break;
                case WITHDRAW: service.withdraw(source, amount, user, role); break;
                case TRANSFER: service.transfer(source, target, amount, user, role); break;
                default: break;
            }
            nextRun = nextRun.plusDays(intervalDays);
        }
    }
}

