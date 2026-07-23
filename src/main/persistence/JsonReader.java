package persistence;

import model.BudgetTracker;
import model.SpendingAccount;

import java.io.IOException;

// Represents a reader that reads SpendingAccount and BudgetTracker from JSON data stored in file
// Citation: modeled on JsonSerializationDemo
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads spending account from file and returns it;
    // throws IOException if an error occurs reading data from file
    public SpendingAccount readAccount() throws IOException {
        return null; 
    }

    // EFFECTS: reads budget tracker from file and returns it;
    // throws IOException if an error occurs reading data from file
    public BudgetTracker readBudgetTracker() throws IOException {
        return null; 
    }
}
