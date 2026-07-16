package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TransactionTest {
    private Transaction testTransaction;

    @BeforeEach
    public void runBefore() {
        testTransaction = new Transaction(45.50, "Groceries", LocalDate.of(2026, 7, 15));
    }

    @Test
    public void testConstructor() {
        assertEquals(45.50, testTransaction.getAmount());
        assertEquals("Groceries", testTransaction.getCategory());
        assertEquals(LocalDate.of(2026, 7, 15), testTransaction.getDate());
    }
}
