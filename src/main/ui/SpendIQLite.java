package ui;

// Citation: modeled on JsonSerializationDemo
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo

import model.Budget;
import model.SpendingAccount;
import model.BudgetTracker;
import model.Transaction;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class SpendIQLite {

    private static final String JSON_STORE = "./data/spendIQLite.json";

    private SpendingAccount account;
    private BudgetTracker budgetTracker;
    private Scanner input;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;


    public SpendIQLite() {
        runApp();
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    private void runApp() {
        boolean keepGoing = true;
        String command;
        init();

        while (keepGoing) {
            displayMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("q")) {
                keepGoing = false;
            } else {
                processCommand(command);
            }
        }

        System.out.println("\nGoodbye!");
    }

    // MODIFIES: this
    // EFFECTS: initializes account, budget tracker, and input scanner
    private void init() {
        account = new SpendingAccount();
        budgetTracker = new BudgetTracker();
        input = new Scanner(System.in);
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
    }

    // EFFECTS: displays menu of options to the user
    private void displayMenu() {
        System.out.println("\nSelect an option:");
        System.out.println("\ta -> add transaction");
        System.out.println("\tv -> view all transactions");
        System.out.println("\tb -> set budget for category");
        System.out.println("\tc -> view spending vs. budget");
        System.out.println("\ts -> save data to file");
        System.out.println("\tl -> load data from file");
        System.out.println("\tq -> quit");
    }

    // MODIFIES: this
    // EFFECTS: processes user command
    private void processCommand(String command) {
        if (command.equals("a")) {
            doAddTransaction();
        } else if (command.equals("v")) {
            doViewTransactions();
        } else if (command.equals("b")) {
            doSetBudget();
        } else if (command.equals("c")) {
            doViewSpendingVsBudget();
        } else if (command.equals("s")) {
            doSaveData();
        } else if (command.equals("l")) {
            doLoadData();
        } else {
            System.out.println("Selection not valid, try again");
        }
    }

    // MODIFIES: this
    // EFFECTS: asks for details and adds transaction to account
    private void doAddTransaction() {

        System.out.println("Enter amount:");
        double amount = input.nextDouble();

        System.out.println("Enter category:");
        String category = input.next();

        System.out.println("Enter date (YYYY-MM-DD):");
        LocalDate date = LocalDate.parse(input.next());

        account.addTransaction(new Transaction(amount, category, date));
        System.out.println("Transaction added!");
    }

    // EFFECTS: show all transactions
    private void doViewTransactions() {
        List<Transaction> transactions = account.getTransactions();

        if (transactions.isEmpty()) {
            System.out.println("No transactions yet.");
        } else {
            for (Transaction t : transactions) {
                System.out.println(t.getDate() + " | " + t.getCategory() + " | $" + t.getAmount());
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: sets the budget with input category and limit
    private void doSetBudget() {
        System.out.println("Enter category:");
        String category = input.next();

        System.out.println("Enter budget limit:");
        double limit = input.nextDouble();

        budgetTracker.setBudget(category, limit);
        System.out.println("Budget set!");
    }

    // EFFECTS: returns TotalSpending vs Budget in a category
    private void doViewSpendingVsBudget() {
        System.out.println("Enter category:");
        String category = input.next();

        double total = account.getTotalForCategory(category);
        Budget budget = budgetTracker.getBudget(category);

        System.out.println("Total spent in " + category + ": $" + total);

        if (budget == null) {
            System.out.println("No budget set for this category.");
        } else {
            System.out.println("Budget limit: $" + budget.getLimit());
            System.out.println("Remaining: $" + (budget.getLimit() - total));
        }
    }

    // EFFECTS: saves account and budget tracker to file
    private void doSaveData() {
        try {
            jsonWriter.open();
            jsonWriter.write(account, budgetTracker);
            jsonWriter.close();
            System.out.println("Saved data to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads account and budget tracker from file
    private void doLoadData() {
        try {
            account = jsonReader.readAccount();
            budgetTracker = jsonReader.readBudgetTracker();
            System.out.println("Loaded data from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

}
