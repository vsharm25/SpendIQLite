package ui;

import model.Budget;
import model.BudgetTracker;
import model.SpendingAccount;
import model.Transaction;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

// Represents a panel that draws a bar chart of total spending per category,
// with each category's budget limit (if one is set) overlaid as a red line.
// This is the GUI's required visual component: it visually represents the
// user's transaction data, rather than simply coloring existing widgets.

public class SpendingChartPanel extends JPanel {
    private static final int MARGIN = 50;
    private static final int BAR_WIDTH = 60;
    private static final int BAR_GAP = 30;

    private SpendingAccount account;
    private BudgetTracker budgetTracker;

    // EFFECTS: constructs a chart panel that draws data from the given account and budget tracker
    public SpendingChartPanel(SpendingAccount account, BudgetTracker budgetTracker) {
        this.account = account;
        this.budgetTracker = budgetTracker;
        setPreferredSize(new Dimension(600, 400));
        setBackground(Color.WHITE);
    }

    // MODIFIES: this
    // EFFECTS: updates the account and budget tracker this chart draws from, then repaints
    public void setData(SpendingAccount account, BudgetTracker budgetTracker) {
        this.account = account;
        this.budgetTracker = budgetTracker;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Map<String, Double> totals = totalsByCategory();
        int panelHeight = getHeight();
        int panelWidth = getWidth();
        int baseline = panelHeight - MARGIN;

        g2.setColor(Color.BLACK);
        g2.drawLine(MARGIN, baseline, panelWidth - 10, baseline);
        g2.drawLine(MARGIN, 10, MARGIN, baseline);
        g2.drawString("Spending by Category", MARGIN, 20);

        if (totals.isEmpty()) {
            g2.drawString("No transactions yet.", MARGIN + 10, baseline - 10);
            return;
        }

        double maxValue = maxOf(totals);

        int x = MARGIN + 20;
        double scale = (baseline - 40) / maxValue;

        for (Map.Entry<String, Double> entry : totals.entrySet()) {
            String category = entry.getKey();
            double total = entry.getValue();
            int barHeight = (int) (total * scale);

            g2.setColor(new Color(70, 130, 180));
            g2.fillRect(x, baseline - barHeight, BAR_WIDTH, barHeight);
            g2.setColor(Color.BLACK);
            g2.drawRect(x, baseline - barHeight, BAR_WIDTH, barHeight);

            Budget budget = budgetTracker.getBudget(category);
            if (budget != null) {
                int limitY = baseline - (int) (budget.getLimit() * scale);
                g2.setColor(Color.RED);
                g2.drawLine(x, limitY, x + BAR_WIDTH, limitY);
            }

            g2.setColor(Color.BLACK);
            g2.drawString(category, x, baseline + 15);
            g2.drawString(String.format("$%.2f", total), x, baseline - barHeight - 5);

            x += BAR_WIDTH + BAR_GAP;
        }
    }

    // EFFECTS: returns the largest value among category totals and any set budget limits
    // for those categories, so the chart's vertical scale fits everything drawn
    private double maxOf(Map<String, Double> totals) {
        double maxValue = 1.0;
        for (double v : totals.values()) {
            maxValue = Math.max(maxValue, v);
        }
        for (String category : totals.keySet()) {
            Budget b = budgetTracker.getBudget(category);
            if (b != null) {
                maxValue = Math.max(maxValue, b.getLimit());
            }
        }
        return maxValue;
    }

    // EFFECTS: returns the total amount spent per category, in the order categories were
    // first encountered in the account's transaction list
    private Map<String, Double> totalsByCategory() {
        Map<String, Double> totals = new LinkedHashMap<>();
        for (Transaction t : account.getTransactions()) {
            totals.merge(t.getCategory(), t.getAmount(), Double::sum);
        }
        return totals;
    }
}
