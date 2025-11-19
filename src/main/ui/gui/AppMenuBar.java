package ui.guii;

import model.Album;
import model.Photo;
import model.ProblemType;
import model.Reflection;
import ui.guii.adapters.LibraryAdapter;

import java.util.List;

import javax.swing.*;

import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;

import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.time.LocalDate;

// Menu bar for the Better Your Picture GUI.
// Provides File, Photo, and Album menus with actions for
// loading/saving data, importing photos, and managing albums.
@ExcludeFromJacocoGeneratedReport
public class AppMenuBar extends JMenuBar {
    private final MainFrame frame;
    private final LibraryAdapter adapter;

    // MODIFIES: this
    // EFFECTS: constructs the menu bar and adds the File, Photo,
    // and Album menus.
    public AppMenuBar(MainFrame frame, LibraryAdapter adapter) {
        this.frame = frame;
        this.adapter = adapter;
        add(fileMenu());
        add(photoMenu());
        add(albumMenu());
    }

    // EFFECTS: returns a File menu with items for load, save, and exit.
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

    // EFFECTS: returns a Photo menu with items to import photos,
    // delete a photo, edit reflection, add to album,
    // and remove from album.
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

    // EFFECTS: returns an Album menu with items to create, rename,
    // and delete albums.
    private JMenu albumMenu() {
        JMenu m = new JMenu("Album");
        m.add(menuItem("New Album", this::doNewAlbum,
                KeyStroke.getKeyStroke('N', menuMask())));
        m.add(menuItem("Rename Album", this::doRenameAlbum,
                KeyStroke.getKeyStroke('M', menuMask())));
        m.add(menuItem("Delete Album", this::doDeleteAlbum,
                KeyStroke.getKeyStroke('D', menuMask())));
        return m;
    }

    // EFFECTS: creates a JMenuItem with the given text and action
    // listener; sets the accelerator key if ks is not null.
    private JMenuItem menuItem(String text, java.awt.event.ActionListener act, KeyStroke ks) {
        JMenuItem it = new JMenuItem(text);
        it.addActionListener(act);
        if (ks != null) {
            it.setAccelerator(ks);
        }
        return it;
    }

    // EFFECTS: returns the standard menu shortcut mask for the platform.
    private int menuMask() {
        return Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();
    }

    // EFFECTS: shows an information dialog with the given message.
    private void info(String s) {
        JOptionPane.showMessageDialog(frame, s);
    }

    // EFFECTS: shows an error dialog with the given message.
    private void error(String s) {
        JOptionPane.showMessageDialog(frame, s, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // ====================== Actions ======================

    // MODIFIES: adapter, frame
    // EFFECTS: loads data from disk, refreshes the main frame,
    // and shows an information dialog.
    private void doLoad(java.awt.event.ActionEvent e) {
        adapter.loadAll();
        frame.refreshAll();
        info("Loaded.");
    }

    // MODIFIES: adapter, filesystem, frame
    // EFFECTS: opens a file chooser to select image files, copies them
    // into a fixed library directory, creates Photo objects in
    // the adapter, refreshes the main frame, and shows a dialog
    // with the number of imported photos.
    private void doImport(java.awt.event.ActionEvent e) {
        JFileChooser fc = new JFileChooser();
        fc.setMultiSelectionEnabled(true);
        fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Images", "jpg", "jpeg", "png", "gif", "webp"));
        int res = fc.showOpenDialog(frame);
        if (res != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File libDir = new File(System.getProperty("user.home"), "BetterYourPictureLibrary");
        libDir.mkdirs();

        int ok = importFiles(fc.getSelectedFiles(), libDir);
        frame.refreshAll();
        info("Imported " + ok + " photo(s).");
    }

    // helper method
    private int importFiles(File[] files, File libDir) {
        int ok = 0;
        for (File src : files) {
            try {
                File dst = uniqueName(libDir, src.getName());
                Files.copy(src.toPath(), dst.toPath());
                String photoname = stripExt(src.getName());
                adapter.addImportedPhoto(
                        photoname, dst.getAbsolutePath(),
                        "Unknown", 100, 2.8, 0.005, LocalDate.now());
                ok++;
            } catch (Exception ex) {
                System.err.println("Import failed: " + ex.getMessage());
            }
        }
        return ok;
    }

    // MODIFIES: adapter, frame
    // EFFECTS: if a photo is selected, asks for confirmation and removes
    // the photo from the library and all albums; then refreshes
    // the main frame and shows a confirmation dialog.
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

    // EFFECTS: builds a human readable text summary of the photo's
    // reflection, including score, problems, and comments; if
    // there is no reflection, returns a message indicating that.
    private void doEditReflection(java.awt.event.ActionEvent e) {
        Photo p = frame.getCurrentPhoto();
        if (p == null) {
            info("Select a photo first.");
            return;
        }

        Integer score = askScore();
        if (score == null) {
            return;
        }

        ProblemType pt = askProblemType();
        String comment = askComment();

        Reflection r = new Reflection();
        r.setScore(score);
        if (pt != null) {
            r.addProblemType(pt);
        }
        if (comment != null && !comment.trim().isEmpty()) {
            r.addComment(comment.trim());
        }

        p.setReflection(r);
        frame.refreshReflectionForPhoto(p);
        info("Saved.");
    }

    // helper method
    private Integer askScore() {
        String s = JOptionPane.showInputDialog(frame, "Score (0-100):");
        if (s == null) {
            return null;
        }
        try {
            int sc = Integer.parseInt(s.trim());
            return (sc < 0 || sc > 100) ? null : sc;
        } catch (Exception ex) {
            error("Invalid score.");
            return null;
        }
    }

    // helper method
    private ProblemType askProblemType() {
        return (ProblemType) JOptionPane.showInputDialog(
                frame,
                "Select problem type:",
                "Problem Type",
                JOptionPane.PLAIN_MESSAGE,
                null,
                ProblemType.values(),
                null);
    }

    // helper method
    private String askComment() {
        return JOptionPane.showInputDialog(frame, "Comment (optional):");
    }

    // EFFECTS: Handles the action of adding the current photo to an album.
    // If successful, refreshes the frame and shows confirmation message.
    private void doAddToAlbum(java.awt.event.ActionEvent e) {
        Photo currentPhoto = frame.getCurrentPhoto();
        if (!isPhotoSelected(currentPhoto)) {
            return;
        }

        List<Album> albums = adapter.albums();
        if (!hasAlbums(albums)) {
            return;
        }

        String selectedAlbumName = showAlbumSelectionDialog(albums);
        if (selectedAlbumName == null) {
            return;
        }

        addPhotoToAlbum(currentPhoto, selectedAlbumName, albums);
        frame.refreshAll();
        info("Added to album.");
    }

    // EFFECTS: Checks if a photo is currently selected.
    // return true if photo is selected, false otherwise and shows message
    private boolean isPhotoSelected(Photo photo) {
        if (photo == null) {
            info("Select a photo first.");
            return false;
        }
        return true;
    }

    // EFFECTS: returns true if albums list is not empty, otherwise shows
    // "Create an album first." message and returns false
    private boolean hasAlbums(List<Album> albums) {
        if (albums.isEmpty()) {
            info("Create an album first.");
            return false;
        }
        return true;
    }

    // EFFECTS: returns array of album names from the albums list
    private String[] getAlbumNames(List<Album> albums) {
        String[] names = new String[albums.size()];
        for (int i = 0; i < albums.size(); i++) {
            names[i] = albums.get(i).getAlbumName();
        }
        return names;
    }

    // EFFECTS: shows dialog to select album, returns selected album name
    // or null if cancelled
    private String showAlbumSelectionDialog(List<Album> albums) {
        String[] albumNames = getAlbumNames(albums);
        String selectedAlbum = (String) JOptionPane.showInputDialog(
                frame,
                "Add to which album?",
                "Add to Album",
                JOptionPane.PLAIN_MESSAGE,
                null,
                albumNames,
                albumNames[0]);
        return selectedAlbum;
    }

    // MODIFIES: the album with matching name in albums list
    // EFFECTS: adds the photo to the album with the specified name
    private void addPhotoToAlbum(Photo photo, String albumName, List<Album> albums) {
        for (Album album : albums) {
            if (album.getAlbumName().equals(albumName)) {
                album.addPhoto(photo);
                break;
            }
        }
    }

    // MODIFIES: adapter, current album in frame
    // EFFECTS: if both an album and a photo are selected, removes the
    // photo from that album, refreshes the main frame, and
    // shows a dialog; otherwise shows an informational dialog.
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
    // EFFECTS: prompts for a new album name, creates the album through
    // the adapter, refreshes the frame, or shows an error if
    // the name is invalid or already exists.
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
    // EFFECTS: if an album is selected, prompts for a new name, renames
    // the album, refreshes the frame, or shows an error; if no
    // album is selected, shows an informational dialog.
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
    // EFFECTS: if an album is selected, asks for confirmation, removes
    // the album, refreshes the frame, and shows a dialog; if no
    // album is selected, shows an informational dialog.
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

    // EFFECTS: returns a File object in the given directory that is
    // unique by appending "(k)" before the extension if needed.
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

    // EFFECTS: returns n without its extension if it has one, otherwise
    // returns n unchanged.
    private String stripExt(String n) {
        int d = n.lastIndexOf('.');
        return d >= 0 ? n.substring(0, d) : n;
    }
}
