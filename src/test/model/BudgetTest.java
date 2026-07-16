package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BudgetTest {
    private Budget budgetTest;

    @BeforeEach
    public void runBefore() {
        budgetTest = new Budget("Groceries", 400.00);
    }

    @Test
    public void testConstructor() {
        assertEquals("Groceries", budgetTest.getCategory());
        assertEquals(400.00, budgetTest.getLimit());
    }
}
