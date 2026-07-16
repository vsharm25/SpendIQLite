package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class BudgetTrackerTest {
    private BudgetTracker testTracker;

    @BeforeEach
    public void runBefore() {
        testTracker = new BudgetTracker();
    }

    @Test
    public void testGetBudgetNoneSet() {
        assertNull(testTracker.getBudget("Groceries"));
    }

    @Test
    public void testSetBudgetNew() {
        testTracker.setBudget("Groceries", 400.00);
        Budget b = testTracker.getBudget("Groceries");
        assertEquals("Groceries", b.getCategory());
        assertEquals(400.00, b.getLimit());
    }

    @Test
    public void testSetMultipleBudgets() {
        testTracker.setBudget("Groceries", 400.00);
        testTracker.setBudget("Transport", 150.00);
        assertEquals(400.00, testTracker.getBudget("Groceries").getLimit());
        assertEquals(150.00, testTracker.getBudget("Transport").getLimit());
    }

    @Test
    public void testGetBudgetNoMatch() {
        testTracker.setBudget("Transport", 2000);
        assertNull(testTracker.getBudget("Entertainment"));
    }
}