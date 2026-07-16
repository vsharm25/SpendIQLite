package model;

import java.util.ArrayList;
import java.util.List;

// Represents a tracker that holds budgets for different categories

public class BudgetTracker {
    private List<Budget> budgets;

    //EFFECTS: constructs an empty budget tracker with no budget set
    public BudgetTracker() {
        // stub
    }

    //REQUIRES: limit > 0
    //MODIFIES: this
    //EFFECTS:  If a budget exists with given category,
    //          replace limit with new limit.
    // If such budget does not exist, create.

    public void setBudget(String category, double limit) {
        
    }

    
    //EFFECTS: returns the Budget set for the given category or null

    public Budget getBudget(String category) {
        return null;
    }
}
