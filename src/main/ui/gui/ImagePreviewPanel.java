package ui.guii;

import javax.swing.*;

import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;

import java.awt.*;
import java.io.File;

// Panel that shows a scaled preview of an image file, or an error
// message if the file cannot be loaded.
@ExcludeFromJacocoGeneratedReport
public class ImagePreviewPanel extends JPanel {
    private final JLabel image = new JLabel("No image", SwingConstants.CENTER);

    // MODIFIES: this
    // EFFECTS: constructs the preview panel with a label placeholder.
    public ImagePreviewPanel() {
        setLayout(new BorderLayout());
        add(new JLabel("Preview"), BorderLayout.NORTH);
        add(image, BorderLayout.CENTER);
        setPreferredSize(new Dimension(680, 420));
    }

    // MODIFIES: this
    // EFFECTS: if path is null or blank, clears the image and shows
    // "No image"; if the file does not exist or is not a
    // valid image, shows an error message; otherwise loads and
    // scales the image to a fixed height and displays it.
    public void showImage(String path) {
        if (path == null || path.trim().isEmpty()) {
            clearImage("No image");
            return;
        }
        File f = new File(path);
        if (!f.exists()) {
            clearImage("File not found:\n" + path);
            return;
        }

        ImageIcon icon = loadIcon(path);
        if (icon == null) {
            clearImage("Load failed:\n" + path);
            return;
        }

        showLoadedIcon(icon);
    }

    // MODIFIES: this
    // EFFECTS: clears the current image and displays the given message
    // in the image label; if msg is null, shows an empty string.
    private void clearImage(String msg) {
        image.setIcon(null);
        image.setText(msg);
    }


    // EFFECTS: returns a loaded ImageIcon if the file at path is a valid image;
    // returns null if loading fails or the image has invalid dimensions.
    private ImageIcon loadIcon(String path) {
        ImageIcon icon = new ImageIcon(path);
        return icon.getIconHeight() > 0 ? icon : null;
    }



    // REQUIRES: icon has positive width and height
    // MODIFIES: this
    // EFFECTS: scales the given icon to a fixed height, displays it in the
    // preview label, and repaints the panel.
    private void showLoadedIcon(ImageIcon icon) {
        int h = 420;
        int w = icon.getIconWidth() * h / icon.getIconHeight();
        Image scaled = icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
        image.setText("");
        image.setIcon(new ImageIcon(scaled));
        revalidate();
        repaint();
    }

}
