package model;

import java.util.ArrayList;
import java.util.List;

// Represents a tracker that holds budgets for different categories

public class BudgetTracker {

    private List<Budget> budgets;

    // EFFECTS: constructs an empty budget tracker with no budget set
    public BudgetTracker() {
        budgets = new ArrayList<>();
    }

    // REQUIRES: limit > 0
    // MODIFIES: this
    // EFFECTS: If a budget already exists with given category,
    // replace limit with given limit.
    // If such budget does not exist, create.

    public void setBudget(String category, double limit) {
        Budget existingBudget = getBudget(category);
        if (existingBudget != null) {
            budgets.remove(existingBudget);
        }
        budgets.add(new Budget(category, limit));

    }

    // EFFECTS: returns the Budget set for the given category or null

    public Budget getBudget(String category) {
        for (Budget b : budgets) {
            if (b.getCategory().equals(category)) {
                return b;
            }
        }
        return null;
    }
}
