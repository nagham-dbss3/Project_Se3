package bank.interest.test;

import bank.accounts.Account;
import bank.accounts.types.InvestmentAccount;
import bank.accounts.types.LoanAccount;
import bank.accounts.types.SavingAccount;
import bank.interest.InterestStrategy;
import bank.interest.SavingInterest;

public class InterestStrategyTest {

    private static int testsPassed = 0;
    private static int testsFailed = 0;

    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("      Running Interest Strategy Tests     ");
        System.out.println("==========================================");

        testSavingInterest();
        testLoanInterest();
        testInvestmentInterest();
        testRuntimeStrategySwitch();
        testMockStrategy();

        System.out.println("\n==========================================");
        System.out.println("TEST RESULTS");
        System.out.println("Passed: " + testsPassed);
        System.out.println("Failed: " + testsFailed);
        System.out.println("==========================================");
    }

    private static void testSavingInterest() {
        System.out.println("\nTest 1: Saving Account Interest (Default Strategy)");
        try {
            SavingAccount sa = new SavingAccount("Alice", 1000.0);
            // Default rate is 3%. Monthly = 1000 * 0.03 / 12 = 2.5
            double interest = sa.calculateInterest();
            
            if (Math.abs(interest - 2.5) < 0.001) {
                System.out.println("✅ Passed: Interest calculated correctly: " + interest);
                testsPassed++;
            } else {
                System.out.println("❌ Failed: Expected 2.5, got " + interest);
                testsFailed++;
            }
        } catch (Exception e) {
            System.out.println("❌ Exception: " + e.getMessage());
            testsFailed++;
        }
    }

    private static void testLoanInterest() {
        System.out.println("\nTest 2: Loan Account Interest");
        try {
            // Loan of 1200, 5% annual interest, 12 months
            LoanAccount la = new LoanAccount("Bob", 1200.0, 5.0, 12);
            // Monthly rate = 5 / 12 / 100 = 0.0041666...
            // Interest = 1200 * 0.0041666... = 5.0
            double interest = la.calculateInterest();
            
            if (Math.abs(interest - 5.0) < 0.001) {
                System.out.println("✅ Passed: Interest calculated correctly: " + interest);
                testsPassed++;
            } else {
                System.out.println("❌ Failed: Expected 5.0, got " + interest);
                testsFailed++;
            }
        } catch (Exception e) {
            System.out.println("❌ Exception: " + e.getMessage());
            testsFailed++;
        }
    }

    private static void testInvestmentInterest() {
        System.out.println("\nTest 3: Investment Account Interest");
        try {
            InvestmentAccount ia = new InvestmentAccount("Charlie", 2000.0);
            ia.invest(1000.0); // Invest 1000 (fee 0.1% = 1.0 -> net 999.0)
            // Investment Value = 999.0
            // Default Target Return = 7%
            // Monthly Return = 999.0 * 0.07 / 12 = 5.8275
            
            double interest = ia.calculateInterest();
            
            if (Math.abs(interest - 5.8275) < 0.001) {
                System.out.println("✅ Passed: Return calculated correctly: " + interest);
                testsPassed++;
            } else {
                System.out.println("❌ Failed: Expected ~5.8275, got " + interest);
                testsFailed++;
            }
        } catch (Exception e) {
            System.out.println("❌ Exception: " + e.getMessage());
            testsFailed++;
        }
    }

    private static void testRuntimeStrategySwitch() {
        System.out.println("\nTest 4: Runtime Strategy Switch (Market Condition Change)");
        try {
            SavingAccount sa = new SavingAccount("David", 1000.0);
            // Initial (3%): 2.5
            double initialInterest = sa.calculateInterest();
            System.out.println("Initial Interest (3%): " + initialInterest);

            // Market boom! Change strategy to 6%
            System.out.println("Changing strategy to 6% interest...");
            sa.setInterestStrategy(new SavingInterest(0.06));
            
            double newInterest = sa.calculateInterest();
            // Expected: 1000 * 0.06 / 12 = 5.0 (plus slightly more due to compounded deposit from first calc)
            // Balance after first calc = 1002.5
            // New Interest = 1002.5 * 0.06 / 12 = 5.0125
            
            System.out.println("New Interest (6%): " + newInterest);
            
            if (newInterest > initialInterest && Math.abs(newInterest - 5.0125) < 0.001) {
                System.out.println("✅ Passed: Strategy switched successfully");
                testsPassed++;
            } else {
                System.out.println("❌ Failed: Strategy switch didn't reflect expected values");
                testsFailed++;
            }
        } catch (Exception e) {
            System.out.println("❌ Exception: " + e.getMessage());
            testsFailed++;
        }
    }

    private static void testMockStrategy() {
        System.out.println("\nTest 5: Mock Strategy (Simulating Promo)");
        try {
            SavingAccount sa = new SavingAccount("Eve", 1000.0);
            
            // Define a mock strategy (Lambda or Anonymous Class)
            InterestStrategy promoStrategy = new InterestStrategy() {
                @Override
                public double calculateInterest(Account account) {
                    System.out.println("[MockStrategy] Applying Flat Promo Bonus");
                    return 50.0; // Flat $50 bonus regardless of balance
                }
            };
            
            sa.setInterestStrategy(promoStrategy);
            double interest = sa.calculateInterest();
            
            if (interest == 50.0) {
                System.out.println("✅ Passed: Mock strategy applied correctly");
                testsPassed++;
            } else {
                System.out.println("❌ Failed: Expected 50.0, got " + interest);
                testsFailed++;
            }
        } catch (Exception e) {
            System.out.println("❌ Exception: " + e.getMessage());
            testsFailed++;
        }
    }
}
