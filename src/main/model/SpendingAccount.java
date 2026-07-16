package model;

import java.util.ArrayList;
import java.util.List;

// Represents a spending account having collection of transactions
public class SpendingAccount {

    private List<Transaction> transactions;

    // EFFECTS: constructs an empty spending account with no transactions
    public SpendingAccount() {
        transactions = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS: adds transaction to list of transactions
    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
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
}