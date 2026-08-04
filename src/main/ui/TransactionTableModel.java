package ui;

import model.Transaction;

import javax.swing.table.AbstractTableModel;
import java.util.List;

// Represents a table model that displays a list of Transactions in a JTable.

public class TransactionTableModel extends AbstractTableModel {
    private static final String[] COLUMN_NAMES = {"Date", "Category", "Amount"};

    private List<Transaction> transactions;

    // EFFECTS: constructs a table model backed by the given list of transactions
    public TransactionTableModel(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    // MODIFIES: this
    // EFFECTS: replaces the underlying list of transactions and refreshes the table
    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return transactions.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    @Override
    public String getColumnName(int col) {
        return COLUMN_NAMES[col];
    }

    @Override
    public Object getValueAt(int row, int col) {
        Transaction t = transactions.get(row);
        switch (col) {
            case 0:
                return t.getDate().toString();
            case 1:
                return t.getCategory();
            case 2:
                return String.format("$%.2f", t.getAmount());
            default:
                return null;
        }
    }
}