package ui;

// Citation: event-handling pattern modeled on the LabelChanger example from
// https://stackoverflow.com/questions/6578205/swing-jlabel-text-change-on-the-running-application

import model.Budget;
import model.BudgetTracker;
import model.SpendingAccount;
import model.Transaction;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import model.EventLog;
import model.Event;

// Represents the main window of the SpendIQ Lite graphical user interface.
// Displays all transactions (Xs) that have been added to the spending account (Y),
// and lets the user add transactions, view/filter transactions by category
// alongside their budget, save application state to file, and load it back.
// As UI code, this class is not unit tested.
public class SpendIQLiteGUI extends JFrame {
    private static final String JSON_STORE = "./data/spendIQLite.json";

    private SpendingAccount account;
    private BudgetTracker budgetTracker;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    private TransactionTableModel tableModel;
    private SpendingChartPanel chartPanel;

    // EFFECTS: constructs the SpendIQ Lite GUI: initializes an empty account and
    // budget
    // tracker, lays out the window, optionally loads saved data, then displays the
    // window
    public SpendIQLiteGUI() {
        super("SpendIQ Lite");
        account = new SpendingAccount();
        budgetTracker = new BudgetTracker();
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                promptSaveOnExit();
            }
        });

        setLayout(new BorderLayout());
        add(buildToolbar(), BorderLayout.NORTH);
        add(buildCenterPanel(), BorderLayout.CENTER);

        setSize(780, 520);
        setLocationRelativeTo(null);

        promptLoadOnStartup();

        setVisible(true);
    }

    // EFFECTS: builds the toolbar containing all the buttons used to trigger GUI
    // actions
    private JToolBar buildToolbar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);

        JButton addButton = new JButton("Add Transaction");
        addButton.addActionListener(this::onAddTransaction);

        JButton viewButton = new JButton("View by Category");
        viewButton.addActionListener(this::onViewByCategory);

        JButton showAllButton = new JButton("Show All");
        showAllButton.addActionListener(this::onShowAll);

        JButton budgetButton = new JButton("Set Budget");
        budgetButton.addActionListener(this::onSetBudget);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(this::onSave);

        JButton loadButton = new JButton("Load");
        loadButton.addActionListener(this::onLoad);

        toolBar.add(addButton);
        toolBar.add(viewButton);
        toolBar.add(showAllButton);
        toolBar.add(budgetButton);
        toolBar.addSeparator();
        toolBar.add(saveButton);
        toolBar.add(loadButton);

        return toolBar;
    }

    // EFFECTS: builds the tabbed panel showing the transaction table (the panel
    // that
    // displays all Xs added to Y) and the spending chart (the required visual
    // component)
    private JTabbedPane buildCenterPanel() {
        JTabbedPane tabs = new JTabbedPane();

        tableModel = new TransactionTableModel(account.getTransactions());
        JTable table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        tabs.addTab("Transactions", new JScrollPane(table));

        chartPanel = new SpendingChartPanel(account, budgetTracker);
        tabs.addTab("Spending Chart", chartPanel);

        return tabs;
    }

    // MODIFIES: this
    // EFFECTS: prompts the user for the details of a new transaction and, if valid,
    // adds it to the account; shows an error dialog and adds nothing if input is
    // invalid.
    // This is the first of the two required actions related to adding Xs to Y.
    private void onAddTransaction(ActionEvent e) {
        try {
            Transaction t = readTransactionFromUser();
            if (t == null) {
                return;
            }
            account.addTransaction(t);
            onShowAll(e);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Amount must be a number.",
                    "Invalid input", JOptionPane.ERROR_MESSAGE);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Date must be in YYYY-MM-DD format.",
                    "Invalid input", JOptionPane.ERROR_MESSAGE);
        }
    }

    // EFFECTS: prompts the user for an amount, category, and date via input dialogs
    // and
    // returns a new Transaction built from them; returns null if the user cancels
    // any
    // prompt; throws NumberFormatException if amount isn't a number,
    // DateTimeParseException
    // if date isn't in YYYY-MM-DD format
    private Transaction readTransactionFromUser() {
        String amountStr = JOptionPane.showInputDialog(this, "Enter amount:");
        if (amountStr == null) {
            return null;
        }
        double amount = Double.parseDouble(amountStr);

        String category = JOptionPane.showInputDialog(this, "Enter category:");
        if (category == null || category.isEmpty()) {
            return null;
        }

        String dateStr = JOptionPane.showInputDialog(this, "Enter date (YYYY-MM-DD):");
        if (dateStr == null) {
            return null;
        }
        LocalDate date = LocalDate.parse(dateStr);

        return new Transaction(amount, category, date);
    }

    // EFFECTS: prompts the user for a category, filters the transaction table down
    // to the
    // subset of transactions in that category, and displays that category's total
    // spending
    // versus its budget limit (if one is set). This is the second of the two
    // required
    // actions: it displays a subset of Xs satisfying a category specified
    // by the user.
    private void onViewByCategory(ActionEvent e) {
        String category = JOptionPane.showInputDialog(this, "Enter category to view:");
        if (category == null || category.isEmpty()) {
            return;
        }

        tableModel.setTransactions(filterByCategory(category));

        JOptionPane.showMessageDialog(this, buildSpendingMessage(category),
                "Spending vs Budget", JOptionPane.INFORMATION_MESSAGE);
    }

    // EFFECTS: returns the subset of the account's transactions matching the given
    // category
    private List<Transaction> filterByCategory(String category) {
        List<Transaction> subset = new ArrayList<>();
        for (Transaction t : account.getTransactions()) {
            if (t.getCategory().equals(category)) {
                subset.add(t);
            }
        }
        return subset;
    }

    // EFFECTS: returns a message summarizing total spending in the given category
    // versus
    // its budget limit, if one is set
    private String buildSpendingMessage(String category) {
        double total = account.getTotalForCategory(category);
        Budget budget = budgetTracker.getBudget(category);

        StringBuilder message = new StringBuilder();
        message.append("Total spent in ").append(category).append(": $")
                .append(String.format("%.2f", total));
        if (budget == null) {
            message.append("\nNo budget set for this category.");
        } else {
            message.append("\nBudget limit: $").append(String.format("%.2f", budget.getLimit()));
            message.append("\nRemaining: $").append(String.format("%.2f", budget.getLimit() - total));
        }
        return message.toString();
    }

    // EFFECTS: resets the transaction table to show every transaction in the
    // account
    private void onShowAll(ActionEvent e) {
        tableModel.setTransactions(account.getTransactions());
    }

    // MODIFIES: this
    // EFFECTS: prompts the user for a category and a budget limit, and sets that
    // budget
    // in the budget tracker; refreshes the chart so the new limit line is visible
    private void onSetBudget(ActionEvent e) {
        String category = JOptionPane.showInputDialog(this, "Enter category:");
        if (category == null || category.isEmpty()) {
            return;
        }
        try {
            String limitStr = JOptionPane.showInputDialog(this, "Enter budget limit:");
            if (limitStr == null) {
                return;
            }
            double limit = Double.parseDouble(limitStr);
            budgetTracker.setBudget(category, limit);
            chartPanel.setData(account, budgetTracker);
            JOptionPane.showMessageDialog(this, "Budget set!");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Limit must be a number.",
                    "Invalid input", JOptionPane.ERROR_MESSAGE);
        }
    }

    // EFFECTS: saves the current account and budget tracker to file
    private void onSave(ActionEvent e) {
        saveData();
    }

    // MODIFIES: this
    // EFFECTS: loads the account and budget tracker from file, refreshing the views
    private void onLoad(ActionEvent e) {
        loadData();
    }

    // EFFECTS: writes the current account and budget tracker to JSON_STORE, showing
    // a
    // confirmation dialog on success or an error dialog on failure
    private void saveData() {
        try {
            jsonWriter.open();
            jsonWriter.write(account, budgetTracker);
            jsonWriter.close();
            JOptionPane.showMessageDialog(this, "Saved data to " + JSON_STORE);
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Unable to write to file: " + JSON_STORE,
                    "Save failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    // MODIFIES: this
    // EFFECTS: reads the account and budget tracker from JSON_STORE, refreshes the
    // table
    // and chart, and shows a confirmation dialog on success or an error dialog on
    // failure
    private void loadData() {
        try {
            account = jsonReader.readAccount();
            budgetTracker = jsonReader.readBudgetTracker();
            onShowAll(null);
            chartPanel.setData(account, budgetTracker);
            JOptionPane.showMessageDialog(this, "Loaded data from " + JSON_STORE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Unable to read from file: " + JSON_STORE,
                    "Load failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    // MODIFIES: this
    // EFFECTS: on startup, asks the user (Yes/No) whether to load saved data from
    // file,
    // and loads it if they choose Yes. Implements the "prompted to load data when
    // the
    // application starts" user story.
    private void promptLoadOnStartup() {
        int choice = JOptionPane.showConfirmDialog(this,
                "Load saved data from file?", "Load data",
                JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            loadData();
        }
    }

    // MODIFIES: this
    // EFFECTS: on exit, asks the user (Yes/No) whether to save data to file, saves
    // if they
    // choose Yes, prints all logged events to console, then closes the application
    private void promptSaveOnExit() {
        int choice = JOptionPane.showConfirmDialog(this,
                "Save data to file before exiting?", "Save data",
                JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            saveData();
        }
        printEventLog();
        dispose();
        System.exit(0);
    }

    // EFFECTS: prints every event logged during this run to the console
    private void printEventLog() {
        for (Event e : EventLog.getInstance()) {
            System.out.println(e.toString());
            System.out.println();
        }
    }
}