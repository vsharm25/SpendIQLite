package persistence;

// Citation: test structure modeled on JsonSerializationDemo
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo

import model.Budget;
import model.BudgetTracker;
import model.SpendingAccount;
import model.Transaction;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonReaderTest {

    @Test
    public void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            reader.readAccount();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    public void testReaderEmptyAccount() {
        JsonReader reader = new JsonReader("./data/testReaderEmpty.json");
        try {
            SpendingAccount account = reader.readAccount();
            assertEquals(0, account.getTransactions().size());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    public void testReaderEmptyBudgetTracker() {
        JsonReader reader = new JsonReader("./data/testReaderEmpty.json");
        try {
            BudgetTracker tracker = reader.readBudgetTracker();
            assertNull(tracker.getBudget("Groceries"));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    public void testReaderGeneralAccount() {
        JsonReader reader = new JsonReader("./data/testReaderGeneral.json");
        try {
            SpendingAccount account = reader.readAccount();
            List<Transaction> transactions = account.getTransactions();
            assertEquals(2, transactions.size());
            checkTransaction(45.5, "Groceries", LocalDate.of(2026, 7, 15), transactions.get(0));
            checkTransaction(12.0, "Transport", LocalDate.of(2026, 7, 16), transactions.get(1));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    public void testReaderGeneralBudgetTracker() {
        JsonReader reader = new JsonReader("./data/testReaderGeneral.json");
        try {
            BudgetTracker tracker = reader.readBudgetTracker();
            Budget groceries = tracker.getBudget("Groceries");
            Budget transport = tracker.getBudget("Transport");
            assertEquals(400.0, groceries.getLimit());
            assertEquals(150.0, transport.getLimit());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    private void checkTransaction(double amount, String category, LocalDate date, Transaction transaction) {
        assertEquals(amount, transaction.getAmount());
        assertEquals(category, transaction.getCategory());
        assertEquals(date, transaction.getDate());
    }
}