package model;


import org.json.JSONObject;

import persistence.Writable;

// Represents a budget set for a specific category

public class Budget implements Writable {
    private String category;
    private double limit;

    // REQUIRES: limit > 0
    // EFFECTS: constructs a Budget with given category and limit
    public Budget(String category, double limit) {
        this.category = category;
        this.limit = limit;
    }

    // EFFECTS: returns the category for this budget
    public String getCategory() {
        return category;
    }

    // EFFECTS: returns the limit of this budget
    public double getLimit() {
        return limit;
    }

    // EFFECTS: returns this budget as a JSON object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("category", category);
        json.put("limit", limit);
        return json;
    }
}
