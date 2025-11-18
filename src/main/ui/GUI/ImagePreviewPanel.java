package ui.GUI;

import javax.swing.*;
import java.awt.*;
import java.io.File;

// Panel that shows a scaled preview of an image file, or an error
// message if the file cannot be loaded.

public class ImagePreviewPanel extends JPanel {
    private final JLabel image = new JLabel("No image", SwingConstants.CENTER);

// MODIFIES: this
// EFFECTS:  constructs the preview panel with a label placeholder.

    public ImagePreviewPanel() {
        setLayout(new BorderLayout());
        add(new JLabel("Preview"), BorderLayout.NORTH);
        add(image, BorderLayout.CENTER);
        setPreferredSize(new Dimension(680, 420));
    }

// REQUIRES: none
// MODIFIES: this
//  EFFECTS:  if path is null or blank, clears the image and shows
//            "No image"; if the file does not exist or is not a
//            valid image, shows an error message; otherwise loads and
//            scales the image to a fixed height and displays it.

    public void showImage(String path) {
        if (path == null || path.trim().isEmpty()) {
            image.setIcon(null);
            image.setText("No image");
            return;
        }
        File f = new File(path);
        if (!f.exists()) {
            image.setIcon(null);
            image.setText("File not found:\n" + path);
            return;
        }
        ImageIcon icon = new ImageIcon(path);
        if (icon.getIconHeight() <= 0) {
            image.setIcon(null);
            image.setText("Load failed:\n" + path);
            return;
        }
        int targetH = 420;
        int w = icon.getIconWidth() * targetH / icon.getIconHeight();
        Image scaled = icon.getImage().getScaledInstance(w, targetH, Image.SCALE_SMOOTH);
        image.setText("");
        image.setIcon(new ImageIcon(scaled));
        revalidate();
        repaint();
    }
}


