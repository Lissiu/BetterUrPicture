package ui.GUI;

import model.Photo;
import ui.GUI.adapters.LibraryAdapter;

import javax.swing.*;
import java.io.File;
import java.nio.file.Files;
import java.time.LocalDate;

// Optional toolbar for quick access to common actions such as
// importing photos and managing albums.

public class Toolbar extends JToolBar {
    private final MainFrame frame;
    private final LibraryAdapter adapter;

    // REQUIRES: frame and adapter are not null
    // MODIFIES: this
    // EFFECTS: constructs an empty, non-floatable toolbar; action
    // buttons can be added if needed.

    public Toolbar(MainFrame frame, LibraryAdapter adapter) {
        this.frame = frame;
        this.adapter = adapter;
        setFloatable(false);

  
        // add(button("Import Photo(s)...", e -> importPhotos()));
        // add(button("Add to Album", e -> addToAlbum()));
        // addSeparator();
        // add(button("Save...", e -> { adapter.saveAll(); msg("Saved."); }));
        // add(button("Load...", e -> { adapter.loadAll(); frame.refreshAll();
        // msg("Loaded."); }));
    }

    // // REQUIRES: t and l are not null
    // // MODIFIES: this
    // // EFFECTS: creates a JButton with the given text and action
    // // listener and returns it.

    // private JButton button(String t, java.awt.event.ActionListener l) {
    //     JButton b = new JButton(t);
    //     b.addActionListener(l);
    //     return b;
    // }

    // MODIFIES: adapter, frame, filesystem
    // EFFECTS: opens a file chooser, asks the user for basic metadata,
    // copies selected images into a library directory, adds
    // them as photos to the adapter, refreshes the frame, and
    // shows a dialog with the number imported.

    private void importPhotos() {
        JFileChooser fc = new JFileChooser();
        fc.setMultiSelectionEnabled(true);
        fc.setFileFilter(
                new javax.swing.filechooser.FileNameExtensionFilter(
                        "Images", "jpg", "jpeg", "png", "gif", "webp"));
        int res = fc.showOpenDialog(frame);
        if (res != JFileChooser.APPROVE_OPTION) {
            return;
        }

        String camera = ask("Camera:");
        if (camera == null) {
            return;
        }
        int iso = askInt("ISO (default 100):", 100);
        double aperture = askDouble("Aperture (default 2.8):", 2.8);
        double shutter = askDouble("Shutter (default 0.005):", 0.005);
        LocalDate date = LocalDate.now();

        File libDir = new File(System.getProperty("user.home"), "BetterYourPictureLibrary");
        libDir.mkdirs();

        int ok = 0;
        for (File src : fc.getSelectedFiles()) {
            try {
                File dst = uniqueName(libDir, src.getName());
                Files.copy(src.toPath(), dst.toPath());
                String photoname = stripExt(src.getName());
                adapter.addImportedPhoto(photoname, dst.getAbsolutePath(),
                        camera, iso, aperture, shutter, date);
                ok++;
            } catch (Exception ex) {
                System.err.println("Import failed: " + ex.getMessage());
            }
        }
        frame.refreshAll();
        msg("Imported " + ok + " photo(s).");
    }

    // MODIFIES: current album in frame
    // EFFECTS: if both an album and a photo are selected, adds the photo
    // to the album and refreshes the frame; otherwise shows a
    // message dialog.

    private void addToAlbum() {
        var album = frame.getCurrentAlbum();
        var photo = frame.getCurrentPhoto();
        if (album == null || photo == null) {
            msg("Select an album and a photo first.");
            return;
        }
        album.addPhoto(photo);
        frame.refreshAll();
        msg("Added to album.");
    }

    // MODIFIES: adapter, frame
    // EFFECTS: asks the user for a new album name, creates it through
    // the adapter, refreshes the frame, and shows a message;
    // if the name is invalid or already exists, shows an error.

    private void newAlbum() {
        String name = ask("New album name:");
        if (name == null || name.isBlank()) {
            return;
        }
        try {
            adapter.createAlbum(name.trim());
            frame.refreshAll();
            JOptionPane.showMessageDialog(frame, "Album created: " + name);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // MODIFIES: adapter, frame
    // EFFECTS: if an album is selected, asks for a new name and renames
    // it via the adapter, then refreshes the frame; if the new
    // name is invalid or already exists, shows an error; if no
    // album is selected, shows a message.

    private void renameAlbum() {
        var album = frame.getCurrentAlbum();
        if (album == null) {
            msg("Select an album first.");
            return;
        }
        String newName = ask("Rename to:");
        if (newName == null || newName.isBlank()) {
            return;
        }
        try {
            adapter.renameAlbum(album, newName.trim());
            frame.refreshAll();
            msg("Renamed.");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // MODIFIES: adapter, frame
    // EFFECTS: if an album is selected, asks for confirmation and then
    // deletes it through the adapter and refreshes the frame;
    // if no album is selected, shows a message dialog.

    private void deleteAlbum() {
        var album = frame.getCurrentAlbum();
        if (album == null) {
            msg("Select an album first.");
            return;
        }
        int ok = JOptionPane.showConfirmDialog(frame,
                "Delete album \"" + album.getAlbumName() + "\" ?",
                "Confirm", JOptionPane.YES_NO_OPTION);
        if (ok != JOptionPane.YES_OPTION) {
            return;
        }
        adapter.removeAlbum(album);
        frame.refreshAll();
        msg("Deleted.");
    }

    // REQUIRES: prompt is not null
    // EFFECTS: shows an input dialog with the prompt and returns the
    // user's input, or null if cancelled.

    private String ask(String prompt) {
        return JOptionPane.showInputDialog(frame, prompt);
    }

    // REQUIRES: prompt is not null
    // EFFECTS: asks the user for an integer; if parsing fails, returns
    // the default value def.

    private int askInt(String prompt, int def) {
        try {
            String s = ask(prompt);
            if (s == null) {
                return def;
            }
            return Integer.parseInt(s);
        } catch (Exception e) {
            return def;
        }
    }

    // REQUIRES: prompt is not null
    // EFFECTS: asks the user for a double; if parsing fails, returns
    // the default value def.

    private double askDouble(String prompt, double def) {
        try {
            String s = ask(prompt);
            if (s == null) {
                return def;
            }
            return Double.parseDouble(s);
        } catch (Exception e) {
            return def;
        }
    }

    // REQUIRES: dir and base are not null
    // EFFECTS: returns a File in dir whose name is base or base_(k)
    // if the original name already exists.

    private File uniqueName(File dir, String base) {
        File dst = new File(dir, base);
        int k = 1;
        int dot = base.lastIndexOf('.');
        String stem = dot >= 0 ? base.substring(0, dot) : base;
        String ext = dot >= 0 ? base.substring(dot) : "";
        while (dst.exists()) {
            dst = new File(dir, stem + "_(" + (k++) + ")" + ext);
        }
        return dst;
    }

    // REQUIRES: n is not null
    // EFFECTS: returns the file name without extension if one exists;
    // otherwise returns the original string.

    private String stripExt(String n) {
        int d = n.lastIndexOf('.');
        return d >= 0 ? n.substring(0, d) : n;
    }

    // REQUIRES: m is not null
    // EFFECTS: shows a simple message dialog attached to the frame.

    private void msg(String m) {
        JOptionPane.showMessageDialog(frame, m);
    }
}
