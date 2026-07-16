package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SpendingAccountTest {
    private SpendingAccount testAccount;
    private Transaction testTransaction;

    @BeforeEach
    public void runBefore() {
        testAccount = new SpendingAccount();
        testTransaction = new Transaction(45.50, "Groceries", LocalDate.of(2026, 7, 15));

    }

    @Test
    public void testConstructor() {
        assertTrue(testAccount.getTransactions().isEmpty());
    }

    @Test
    public void testAddOneTransaction() {
        testAccount.addTransaction(testTransaction);
        List<Transaction> transactions = testAccount.getTransactions();
        assertEquals(1, transactions.size());
        assertEquals(testTransaction, transactions.get(0));
    }

    @Test
    public void testAddMultipleTransactions() {
        Transaction t1 = testTransaction;
        Transaction t2 = new Transaction(12.00, "Transport", LocalDate.of(2026, 7, 16));
        testAccount.addTransaction(t1);
        testAccount.addTransaction(t2);
        assertEquals(2, testAccount.getTransactions().size());
    }

    @Test
    public void testGetTotalForCategoryNoMatches() {
        testAccount.addTransaction(testTransaction);
        assertEquals(0, testAccount.getTotalForCategory("Transport"));
    }

    @Test
    public void testGetTotalForCategoryMultipleMatches() {
        Transaction t1 = new Transaction(20.00, "Groceries", LocalDate.of(2026, 7, 16));
        Transaction t2 = new Transaction(12.00, "Transport", LocalDate.of(2026, 7, 16));
        testAccount.addTransaction(testTransaction);
        testAccount.addTransaction(t1);
        testAccount.addTransaction(t2);
        assertEquals(65.50, testAccount.getTotalForCategory("Groceries"));
    }
}
