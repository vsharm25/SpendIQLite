package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestBudget {
    private Budget testBudget;

    @BeforeEach
    public void runBefore() {
        testBudget = new Budget("Groceries", 400.00);
    }

    @Test
    public void testConstructor() {
        assertEquals("Groceries", testBudget.getCategory());
        assertEquals(400.00, testBudget.getLimit());
    }
}
