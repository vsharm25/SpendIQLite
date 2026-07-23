package persistence;
// Citation: test structure modeled on JsonSerializationDemo
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo

import model.Budget;
import model.BudgetTracker;
import model.SpendingAccount;
import model.Transaction;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonWriterTest {

    @Test
    public void testWriterInvalidFile() {
        try {
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException expected");
        } catch (FileNotFoundException e) {
            // pass
        }
    }

    @Test
    public void testWriterEmpty() {
        try {
            SpendingAccount account = new SpendingAccount();
            BudgetTracker tracker = new BudgetTracker();
            JsonWriter writer = new JsonWriter("./data/testWriterEmpty.json");
            writer.open();
            writer.write(account, tracker);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmpty.json");
            SpendingAccount readAccount = reader.readAccount();
            BudgetTracker readTracker = reader.readBudgetTracker();
            assertEquals(0, readAccount.getTransactions().size());
            assertNull(readTracker.getBudget("Groceries"));
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    public void testWriterGeneral() {
        try {
            SpendingAccount account = createSampleAccount();
            BudgetTracker tracker = createSampleBudgetTracker();

            JsonWriter writer = new JsonWriter("./data/testWriterGeneral.json");
            writer.open();
            writer.write(account, tracker);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneral.json");
            SpendingAccount readAccount = reader.readAccount();
            BudgetTracker readTracker = reader.readBudgetTracker();

            List<Transaction> transactions = readAccount.getTransactions();
            assertEquals(2, transactions.size());
            checkTransaction(45.5, "Groceries", LocalDate.of(2026, 7, 15), transactions.get(0));
            checkTransaction(12.0, "Transport", LocalDate.of(2026, 7, 16), transactions.get(1));

            Budget groceries = readTracker.getBudget("Groceries");
            Budget transport = readTracker.getBudget("Transport");
            assertEquals(400.0, groceries.getLimit());
            assertEquals(150.0, transport.getLimit());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    private SpendingAccount createSampleAccount() {
        SpendingAccount account = new SpendingAccount();
        account.addTransaction(new Transaction(45.5, "Groceries", LocalDate.of(2026, 7, 15)));
        account.addTransaction(new Transaction(12.0, "Transport", LocalDate.of(2026, 7, 16)));
        return account;
    }

    private BudgetTracker createSampleBudgetTracker() {
        BudgetTracker tracker = new BudgetTracker();
        tracker.setBudget("Groceries", 400.0);
        tracker.setBudget("Transport", 150.0);
        return tracker;
    }

    private void checkTransaction(double amount, String category, LocalDate date, Transaction transaction) {
        assertEquals(amount, transaction.getAmount());
        assertEquals(category, transaction.getCategory());
        assertEquals(date, transaction.getDate());
    }
}