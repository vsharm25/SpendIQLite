package persistence;


import model.BudgetTracker;
import model.SpendingAccount;
import model.Transaction;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.stream.Stream;

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
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseAccount(jsonObject);
    }

    // EFFECTS: reads budget tracker from file and returns it;
    // throws IOException if an error occurs reading data from file
    public BudgetTracker readBudgetTracker() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseBudgetTracker(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(contentBuilder::append);
        }

        return contentBuilder.toString();
    }

    private SpendingAccount parseAccount(JSONObject jsonObject) {
        SpendingAccount account = new SpendingAccount();
        JSONObject accountJson = jsonObject.getJSONObject("account");
        addTransactions(account, accountJson);
        return account;
    }

     private void addTransactions(SpendingAccount account, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("transactions");
        for (Object json : jsonArray) {
            JSONObject nextTransaction = (JSONObject) json;
            addTransaction(account, nextTransaction);
        }




}

// MODIFIES: account
    // EFFECTS: parses transaction from JSON object and adds it to spending account
    private void addTransaction(SpendingAccount account, JSONObject jsonObject) {
        double amount = jsonObject.getDouble("amount");
        String category = jsonObject.getString("category");
        LocalDate date = LocalDate.parse(jsonObject.getString("date"));
        Transaction transaction = new Transaction(amount, category, date);
        account.addTransaction(transaction);
    }

    // EFFECTS: parses budget tracker from JSON object and returns it
    private BudgetTracker parseBudgetTracker(JSONObject jsonObject) {
        BudgetTracker tracker = new BudgetTracker();
        JSONObject trackerJson = jsonObject.getJSONObject("budgetTracker");
        addBudgets(tracker, trackerJson);
        return tracker;
    }

    // MODIFIES: tracker
    // EFFECTS: parses budgets from JSON object and adds them to budget tracker
    private void addBudgets(BudgetTracker tracker, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("budgets");
        for (Object json : jsonArray) {
            JSONObject nextBudget = (JSONObject) json;
            addBudget(tracker, nextBudget);
        }
    }

    // MODIFIES: tracker
    // EFFECTS: parses budget from JSON object and adds it to budget tracker
    private void addBudget(BudgetTracker tracker, JSONObject jsonObject) {
        String category = jsonObject.getString("category");
        double limit = jsonObject.getDouble("limit");
        tracker.setBudget(category, limit);
    }

}
