package model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import persistence.Writable;

// Represents a spending account having collection of transactions
public class SpendingAccount implements Writable {

    private List<Transaction> transactions;

    // EFFECTS: constructs an empty spending account with no transactions
    public SpendingAccount() {
        transactions = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS: adds transaction to list of transactions
    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        EventLog.getInstance().logEvent(new Event("Transaction added: $"
                + transaction.getAmount() + " in category " + transaction.getCategory()));
    }

    // EFFECTS: returns the list of all transactions in this account
    public List<Transaction> getTransactions() {
        return transactions;
    }

    // EFFECTS: returns total amount for transations of the category
    public double getTotalForCategory(String category) {
        double total = 0;
        for (Transaction t : transactions) {
            if (t.getCategory().equals(category)) {
                total += t.getAmount();
            }
        }
        return total;
    }

    // EFFECTS: returns this spending account as a JSON object,
    // with all transactions as a JSON array
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("transactions", transactionsToJson());
        return json;
    }

    // EFFECTS: returns transactions in this account as a JSON array
    private JSONArray transactionsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Transaction t : transactions) {
            jsonArray.put(t.toJson());
        }

        return jsonArray;
    }
}