package model;

import java.time.LocalDate;
import org.json.JSONObject;

import persistence.Writable;

// Represents a transation with amount, category and date of
// transaction

public class Transaction implements Writable {

    private double amount;
    private String category;
    private LocalDate date;

    // EFFECTS: returns this transation as JSON object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("amount", amount);
        json.put("category", category);
        json.put("date", date.toString());
        return json;
     }


    // REQUIRES: amount > 0, Category cannot be null or empty,
    // date cannot be null
    // EFFECTS: constructs a Transaction with the given amount, category,
    // and date

    public Transaction(double amount, String category, LocalDate date) {
        this.amount = amount;
        this.category = category;
        this.date = date;

    }

    // EFFECTS : returns the amount of transaction

    public double getAmount() {
        return amount;
    }

    // EFFECTS : returns the category of transaction

    public String getCategory() {
        return category;
    }

    // EFFECTS : returns the date of transaction

    public LocalDate getDate() {
        return date;
    }
}