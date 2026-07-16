package model;


import java.time.LocalDate;
// Represents a transation with amount, category and date of
// transaction

public class Transaction {

    private double amount;
    private String category;
    private LocalDate date;

    //REQUIRES: amount > 0, Category cannot be null or empty,
     //                    date cannot be null
     // EFFECTS: constructs a Transaction with the given amount, category,
     //          and date
     
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

     // EFFECTS :  returns the date of transaction
     
    public LocalDate getDate() {
        return date;
    }
}