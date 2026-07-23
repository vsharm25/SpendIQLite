package persistence;

import model.BudgetTracker;
import model.SpendingAccount;

import java.io.FileNotFoundException;

// Represents a writer that writes JSON representation of SpendingAccount and BudgetTracker to file
// Citation: modeled on JsonSerializationDemo
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
public class JsonWriter {
    private String destination;

    // EFFECTS: constructs writer to write to destination file
    public JsonWriter(String destination) {
        this.destination = destination;
    }

    // MODIFIES: this
    // EFFECTS: opens writer; throws FileNotFoundException if destination file cannot
    // be opened for writing
    public void open() throws FileNotFoundException {
        // stub
    }

    // MODIFIES: this
    // EFFECTS: writes JSON representation of account and budget tracker to file
    public void write(SpendingAccount account, BudgetTracker budgetTracker) {
        // stub
    }

    // MODIFIES: this
    // EFFECTS: closes writer
    public void close() {
        // stub
    }
}
