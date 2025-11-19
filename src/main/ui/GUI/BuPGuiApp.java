package ui.gui;

import javax.swing.SwingUtilities;
import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;

// The GUI app of Better Your Picture (collects photos and reflections).
@ExcludeFromJacocoGeneratedReport
public class BuPGuiApp {
    // MODIFIES: this
    // EFFECTS:  starts the GUI on the Swing event dispatch thread.
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new StartGui());
    }

    // Helper runnable that shows the main frame.
    @ExcludeFromJacocoGeneratedReport
    private static class StartGui implements Runnable {
        @Override
        public void run() {
            new MainFrame().setVisible(true);
        }
    }
}
