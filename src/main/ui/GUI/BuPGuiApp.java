package ui.GUI;

import javax.swing.SwingUtilities;

public class BuPGuiApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}
