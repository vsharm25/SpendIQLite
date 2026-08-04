package ui;

import javax.swing.SwingUtilities;

// Entry point for the SpendIQ Lite graphical user interface.
// The existing console entry point (ui.Main, running SpendIQLite) is left
// untouched so the console UI continues to work as before.
public class MainGUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(SpendIQLiteGUI::new);
    }
}