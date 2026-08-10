package model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import persistence.Writable;

// Represents a tracker that holds budgets for different categories

public class BudgetTracker implements Writable {

    private List<Budget> budgets;

    // EFFECTS: constructs an empty budget tracker with no budget set
    public BudgetTracker() {
        budgets = new ArrayList<>();
    }

    // REQUIRES: limit > 0
    // MODIFIES: this
    // EFFECTS: If a budget already exists with given category,
    // replace limit with given limit and log the update.
    // If such budget does not exist, create it and log the creation.
    public void setBudget(String category, double limit) {
        Budget existingBudget = getBudget(category);
        if (existingBudget != null) {
            budgets.remove(existingBudget);
            budgets.add(new Budget(category, limit));
            EventLog.getInstance().logEvent(new Event("Budget updated for category: "
                    + category + " to limit $" + limit));
        } else {
            budgets.add(new Budget(category, limit));
            EventLog.getInstance().logEvent(new Event("Budget set for category: "
                    + category + " with limit $" + limit));
        }
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

    // EFFECTS: returns this budget tracker as a JSON object,
    // with all budgets as a JSON array
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("budgets", budgetsToJson());
        return json;
    }

    // EFFECTS: returns budgets in this tracker as a JSON array
    private JSONArray budgetsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Budget b : budgets) {
            jsonArray.put(b.toJson());
        }

        return jsonArray;
    }
}
