package model;

// Represents a monthly budget set for a specific spending category

public class Budget {
    private String category;
    private double limit;

    //REQUIRES: limit > 0
    //EFFECTS:  constructs a Budget with given category and limit
    public Budget(String category, double limit) {
    }

    //EFFECTS: returns the category for this budget
    public String getCategory() {
        return null;
    }

    //EFFECTS: returns the limit of this budget
    public double getLimit() {
        return 0; 
    }
}
