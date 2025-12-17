# Advanced Banking System 🏦

A comprehensive banking software solution demonstrating advanced software engineering principles and design patterns. This system manages bank accounts, transactions, notifications, and interest calculations using a modular and extensible architecture.

## 📋 Project Overview

This project implements a core banking system with the following key subsystems:
1.  **Account Management**: Handling different account types, states, and hierarchies.
2.  **Transaction Processing**: Secure transaction handling with approval chains and validation.
3.  **Notification System**: Real-time alerts via Email, SMS, and In-App notifications.
4.  **Interest Management**: Flexible interest calculation strategies based on account type and market conditions.

## 🛠️ Architecture & Design Patterns

The system is built using the following design patterns:

### 1. **Observer Pattern** (Notifications) 🔔
-   **Purpose**: Notifies customers of account activities (deposits, withdrawals, state changes).
-   **Components**: `NotificationSubject` (Subject), `Notifier` (Observer), `EmailNotifier`, `SMSNotifier`, `AppNotifier`.
-   **Location**: `src/bank/notifications/`

### 2. **Strategy Pattern** (Interest Calculation) 📈
-   **Purpose**: Allows switching interest calculation algorithms at runtime (e.g., Savings vs. Loan vs. Investment).
-   **Components**: `InterestStrategy` (Interface), `SavingInterest`, `LoanInterest`, `InvestmentInterest`.
-   **Location**: `src/bank/interest/`

### 3. **State Pattern** (Account Lifecycle) 🔄
-   **Purpose**: Manages account behavior based on its state (Active, Frozen, Suspended, Closed).
-   **Components**: `AccountState` (Interface), `ActiveState`, `FrozenState`, `SuspendedState`, `ClosedState`.
-   **Location**: `src/bank/accounts/states/`

### 4. **Composite Pattern** (Account Hierarchy) 🌳
-   **Purpose**: Treats individual accounts and groups of accounts uniformly.
-   **Components**: `AccountComponent`, `AccountGroup`, `AccountLeaf`.
-   **Location**: `src/bank/accounts/composite/`

### 5. **Chain of Responsibility** (Transaction Approval) ⛓️
-   **Purpose**: Processes transactions through a chain of approval handlers based on amount and role.
-   **Components**: `ApprovalHandler`, `TellerApproval`, `ManagerApproval`, `AdminApproval`.
-   **Location**: `src/bank/transactions/handlers/`

---

## How to Compile and Run

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- (Optional) JUnit 4 libraries for running tests

### Compilation
To compile the project, run the following command from the project root:

```bash
mkdir bin
javac -d bin -sourcepath src src/App.java src/bank/accounts/*.java src/bank/accounts/states/*.java src/bank/accounts/types/*.java src/bank/accounts/composite/*.java src/bank/notifications/*.java src/bank/interest/*.java src/bank/transactions/*.java src/bank/transactions/handlers/*.java src/bank/transactions/history/*.java src/bank/transactions/notification/*.java src/bank/transactions/scheduler/*.java src/bank/transactions/validator/*.java
```

### Running the Demo
The `AccountManagementDemo` class provides a comprehensive demonstration of all system features (Account Management, Notifications, Interest Strategies, Transaction Processing).

```bash
java -cp bin bank.accounts.AccountManagementDemo
```

### Running Tests
To run the unit tests, you need to have the following JAR files in a `lib/` directory at the project root:
- `junit-4.13.2.jar`
- `hamcrest-core-1.3.jar`
- `mockito-core-5.11.0.jar`
- `byte-buddy-1.14.12.jar`
- `byte-buddy-agent-1.14.12.jar`
- `objenesis-3.3.jar`

**Command to run all tests:**
```bash
java -cp "bin;lib/junit-4.13.2.jar;lib/hamcrest-core-1.3.jar;lib/mockito-core-5.11.0.jar;lib/byte-buddy-1.14.12.jar;lib/byte-buddy-agent-1.14.12.jar;lib/objenesis-3.3.jar" org.junit.runner.JUnitCore bank.notifications.test.NotificationTest bank.interest.test.InterestStrategyTest bank.accounts.test.AccountManagementTest
```

## Project Structure
```
src/
├── App.java
└── bank/
    ├── accounts/           # Account Management & Composite Pattern
    │   ├── Account.java
    │   ├── AccountManagementDemo.java
    │   ├── types/          # Concrete Account Types
    │   ├── states/         # State Pattern
    │   ├── composite/      # Composite Pattern
    │   └── test/
    ├── notifications/      # Observer Pattern (Notifications)
    │   ├── NotificationSubject.java
    │   ├── Notifier.java
    │   ├── EmailNotifier.java
    │   ├── SMSNotifier.java
    │   ├── AppNotifier.java
    │   └── test/
    ├── interest/           # Strategy Pattern (Interest Calculation)
    │   ├── InterestStrategy.java
    │   ├── SavingInterest.java
    │   ├── LoanInterest.java
    │   ├── InvestmentInterest.java
    │   └── test/
    └── transactions/       # Chain of Responsibility (Processing)
        ├── TransactionService.java
        ├── handlers/       # Approval Handlers
        ├── history/        # Transaction Logging
        ├── validator/      # Validation Logic
        └── notification/   # Internal Notification Service
```
