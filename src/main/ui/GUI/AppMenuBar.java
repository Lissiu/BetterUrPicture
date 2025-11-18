package ui.gui;

import model.Album;
import model.Photo;
import model.ProblemType;
import model.Reflection;
import ui.gui.adapters.LibraryAdapter;

import javax.swing.*;

import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;

import java.awt.*;
import java.io.File;

import java.nio.file.Files;
import java.time.LocalDate;


// Menu bar for the Better Your Picture GUI.
//Provides File, Photo, and Album menus with actions for
// loading/saving data, importing photos, and managing albums.
@ExcludeFromJacocoGeneratedReport
public class AppMenuBar extends JMenuBar {
    private final MainFrame frame;
    private final LibraryAdapter adapter;



// MODIFIES: this
// EFFECTS:  constructs the menu bar and adds the File, Photo,
//           and Album menus.

    public AppMenuBar(MainFrame frame, LibraryAdapter adapter) {
        this.frame = frame;
        this.adapter = adapter;

        add(fileMenu());
        add(photoMenu());
        add(albumMenu());
    }


// EFFECTS:  returns a File menu with items for load, save, and exit.

    private JMenu fileMenu() {
        JMenu m = new JMenu("File");
        m.add(menuItem("Load...", this::doLoad,
                KeyStroke.getKeyStroke('O', menuMask())));
        m.add(menuItem("Save...", e -> {
            adapter.saveAll();
            info("Saved.");
        }, KeyStroke.getKeyStroke('S', menuMask())));
        m.addSeparator();
        m.add(menuItem("Exit", e -> System.exit(0),
                KeyStroke.getKeyStroke('Q', menuMask())));
        return m;
    }


// EFFECTS:  returns a Photo menu with items to import photos,
//           delete a photo, edit reflection, add a photo to an
//          album, and remove it from an album.
    private JMenu photoMenu() {
        JMenu m = new JMenu("Photo");
        m.add(menuItem("Import...", this::doImport,
                KeyStroke.getKeyStroke('I', menuMask())));
        m.add(menuItem("Delete Photo", this::doDeletePhoto,
                KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_DELETE, 0)));

        m.add(menuItem("Edit Reflection...", this::doEditReflection,
                KeyStroke.getKeyStroke('E', menuMask())));

        m.addSeparator();
        m.add(menuItem("Add to Album", this::doAddToAlbum,
                KeyStroke.getKeyStroke('A', menuMask())));
        m.add(menuItem("Remove from Album", this::doRemoveFromAlbum,
                KeyStroke.getKeyStroke('R', menuMask())));
        return m;
    }



// EFFECTS:  returns an Album menu with items to create, rename,
//          and delete albums.

    private JMenu albumMenu() {
        JMenu m = new JMenu("Album");
        m.add(menuItem("New Album", this::doNewAlbum,
                KeyStroke.getKeyStroke('N', menuMask())));
        m.add(menuItem("Rename Album", this::doRenameAlbum,
                KeyStroke.getKeyStroke('M', menuMask())));
        m.add(menuItem("Delete Album", this::doDeleteAlbum, null));
        return m;
    }


// EFFECTS:  creates a JMenuItem with the given text and action
//           listener; sets the accelerator key if ks is not null.

    private JMenuItem menuItem(String text, java.awt.event.ActionListener act, KeyStroke ks) {
        JMenuItem it = new JMenuItem(text);
        it.addActionListener(act);
        if (ks != null) {
            it.setAccelerator(ks);
        }
        return it;
    }


// EFFECTS:  returns the standard menu shortcut mask for the platform
//           (Command on macOS, Control on Windows/Linux).

    private int menuMask() {
        return Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();
    }


// EFFECTS:  shows an information dialog with the given message.

    private void info(String s) {
        JOptionPane.showMessageDialog(frame, s);
    }



// EFFECTS:  shows an error dialog with the given message.

    private void error(String s) {
        JOptionPane.showMessageDialog(frame, s, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // ====================== Actions ======================



//MODIFIES: adapter, frame
// EFFECTS:  loads data from disk using the adapter, refreshes the
//          main frame, and shows an information dialog.

    private void doLoad(java.awt.event.ActionEvent e) {
        adapter.loadAll();
        frame.refreshAll();
        info("Loaded.");
    }


// MODIFIES: adapter, filesystem, frame
// EFFECTS:  opens a file chooser to select image files, copies them
//           into a fixed library directory, creates Photo objects in
//          the adapter, and refreshes the main frame; shows a dialog
//         with the number of imported photos.

    private void doImport(java.awt.event.ActionEvent e) {
        JFileChooser fc = new JFileChooser();
        fc.setMultiSelectionEnabled(true);
        fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Images", "jpg", "jpeg", "png", "gif", "webp"));
        int res = fc.showOpenDialog(frame);
        if (res != JFileChooser.APPROVE_OPTION) {
            return;
        }

        String defCamera = "Unknown";
        int defISO = 100;
        double defAperture = 2.8;
        double defShutter = 0.005;
        LocalDate defDate = LocalDate.now();

        File libDir = new File(System.getProperty("user.home"), "BetterYourPictureLibrary");
        libDir.mkdirs();

        int ok = 0;
        for (File src : fc.getSelectedFiles()) {
            try {
                File dst = uniqueName(libDir, src.getName());
                Files.copy(src.toPath(), dst.toPath());

                String camera = defCamera;
                int iso = defISO;
                double aperture = defAperture;
                double shutter = defShutter;
                LocalDate date = defDate;

                String photoname = stripExt(src.getName());
                adapter.addImportedPhoto(photoname, dst.getAbsolutePath(),
                        camera, iso, aperture, shutter, date);
                ok++;
            } catch (Exception ex) {
                System.err.println("Import failed: " + ex.getMessage());
            }
        }
        frame.refreshAll();
        info("Imported " + ok + " photo(s).");
    }



// MODIFIES: adapter, frame
// EFFECTS:  if a photo is selected in the main frame, asks the user
//          for confirmation and removes the photo from the library
//          and all albums; then refreshes the main frame and shows
//          a confirmation dialog.

    private void doDeletePhoto(java.awt.event.ActionEvent e) {
        Photo p = frame.getCurrentPhoto();
        if (p == null) {
            info("Select a photo first.");
            return;
        }
        int ok = JOptionPane.showConfirmDialog(frame,
                "Delete photo \"" + p.getPhotoname() + "\" from library (and all albums)?",
                "Confirm", JOptionPane.YES_NO_OPTION);
        if (ok != JOptionPane.YES_OPTION) {
            return;
        }
        adapter.removePhotoFromLibrary(p);
        frame.refreshAll();
        info("Deleted.");
    }


// MODIFIES: current photo in frame
// EFFECTS:  if a photo is selected, shows a dialog that lets the user
//           choose a problem type, enter a comment, and set a score.
//          When the user confirms, writes a new Reflection to the
//           photo and updates the reflection panel in the main frame.
//          If no photo is selected, shows an information dialog.
    private void doEditReflection(java.awt.event.ActionEvent e) {
        Photo p = frame.getCurrentPhoto();
        if (p == null) {
            info("Select a photo first.");
            return;
        }

        // --- build dialog UI ---
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Problem type
        JPanel typeRow = new JPanel();
        typeRow.setLayout(new BoxLayout(typeRow, BoxLayout.X_AXIS));
        JLabel typeLabel = new JLabel("Problem type: ");
        JComboBox<ProblemType> typeBox = new JComboBox<>(ProblemType.values());
        typeRow.add(typeLabel);
        typeRow.add(typeBox);
        panel.add(typeRow);

        panel.add(Box.createVerticalStrut(8));

        // Comment
        JLabel commentLabel = new JLabel("Comment (optional):");
        JTextArea commentArea = new JTextArea(5, 30);
        commentArea.setLineWrap(true);
        commentArea.setWrapStyleWord(true);
        JScrollPane commentScroll = new JScrollPane(commentArea);
        panel.add(commentLabel);
        panel.add(commentScroll);

        panel.add(Box.createVerticalStrut(8));

        // Score
        JPanel scoreRow = new JPanel();
        scoreRow.setLayout(new BoxLayout(scoreRow, BoxLayout.X_AXIS));
        JLabel scoreLabel = new JLabel("Score (0-100): ");
        JTextField scoreField = new JTextField("0", 5);
        scoreRow.add(scoreLabel);
        scoreRow.add(scoreField);
        panel.add(scoreRow);

        // --- show dialog ---
        int result = JOptionPane.showConfirmDialog(
                frame,
                panel,
                "Edit Reflection for \"" + p.getPhotoname() + "\"",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        // --- build Reflection object ---
        int score;
        try {
            score = Integer.parseInt(scoreField.getText().trim());
            if (score < 0 || score > 100) {
                throw new NumberFormatException("Score out of range");
            }
        } catch (NumberFormatException ex) {
            error("Score must be an integer between 0 and 100.");
            return;
        }

        Reflection r = new Reflection();
        ProblemType pt = (ProblemType) typeBox.getSelectedItem();
        if (pt != null) {
            r.addProblemType(pt);
        }

        String comment = commentArea.getText().trim();
        if (!comment.isEmpty()) {
            r.addComment(comment);
        }

        r.setScore(score);
        p.setReflection(r);

        // update right panel text
        frame.refreshReflectionForPhoto(p);
        info("Reflection saved for \"" + p.getPhotoname() + "\".");
    }




// MODIFIES: current album in frame
//EFFECTS:  if both an album and a photo are selected in the main
//         frame, adds the photo to the album; then refreshes the
//          main frame and shows a confirmation dialog; if not,
//          shows an informational dialog.

    private void doAddToAlbum(java.awt.event.ActionEvent e) {
        Album a = frame.getCurrentAlbum();
        Photo p = frame.getCurrentPhoto();
        if (a == null || p == null) {
            info("Select an album and a photo first.");
            return;
        }
        a.addPhoto(p);
        frame.refreshAll();
        info("Added to album.");
    }



// MODIFIES: adapter, current album in frame
// EFFECTS:  if both an album and a photo are selected, removes the
//          photo from that album using the adapter, refreshes the
//          main frame, and shows a confirmation dialog; if not,
//          shows an informational dialog.

    private void doRemoveFromAlbum(java.awt.event.ActionEvent e) {
        Album a = frame.getCurrentAlbum();
        Photo p = frame.getCurrentPhoto();
        if (a == null || p == null) {
            info("Select an album and a photo first.");
            return;
        }
        adapter.removePhotoFromAlbum(a, p);
        frame.refreshAll();
        info("Removed from album.");
    }


// MODIFIES: adapter, frame
// EFFECTS:  prompts the user for a new album name, creates the album
//          through the adapter, and refreshes the frame; if the name
//          is invalid or already exists, shows an error dialog.

    private void doNewAlbum(java.awt.event.ActionEvent e) {
        String name = JOptionPane.showInputDialog(frame, "New album name:");
        if (name == null || name.isBlank()) {
            return;
        }
        try {
            adapter.createAlbum(name.trim());
            frame.refreshAll();
            info("Album created.");
        } catch (IllegalArgumentException ex) {
            error(ex.getMessage());
        }
    }



// MODIFIES: adapter, frame
// EFFECTS:  if an album is selected, prompts the user for a new name,
//          renames the album via the adapter, and refreshes the
//          frame; if the new name is invalid or already exists,
//          shows an error dialog; if no album is selected, shows an
//          informational dialog.

    private void doRenameAlbum(java.awt.event.ActionEvent e) {
        Album a = frame.getCurrentAlbum();
        if (a == null) {
            info("Select an album first.");
            return;
        }
        String newName = JOptionPane.showInputDialog(frame, "Rename to:", a.getAlbumName());
        if (newName == null || newName.isBlank()) {
            return;
        }
        try {
            adapter.renameAlbum(a, newName.trim());
            frame.refreshAll();
            info("Renamed.");
        } catch (IllegalArgumentException ex) {
            error(ex.getMessage());
        }
    }



// MODIFIES: adapter, frame
// EFFECTS:  if an album is selected, asks for confirmation and then
//           removes it via the adapter and refreshes the frame; if
//          none is selected, shows an informational dialog.

    private void doDeleteAlbum(java.awt.event.ActionEvent e) {
        Album a = frame.getCurrentAlbum();
        if (a == null) {
            info("Select an album first.");
            return;
        }
        int ok = JOptionPane.showConfirmDialog(frame,
                "Delete album \"" + a.getAlbumName() + "\" ?",
                "Confirm", JOptionPane.YES_NO_OPTION);
        if (ok != JOptionPane.YES_OPTION) {
            return;
        }
        adapter.removeAlbum(a);
        frame.refreshAll();
        info("Deleted.");
    }



// EFFECTS:  returns a File object in the given directory that is
//          unique by appending "(k)" before the extension if needed.

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



// EFFECTS:  returns n without its extension if it has one, otherwise
//          returns n unchanged.

    private String stripExt(String n) {
        int d = n.lastIndexOf('.');
        return d >= 0 ? n.substring(0, d) : n;
    }
}
