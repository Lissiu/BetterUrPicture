package ui.gui;

import model.Album;
import model.Photo;
import model.Reflection;
import ui.gui.adapters.LibraryAdapter;

import model.Event;
import model.EventLog;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;

import java.awt.*;
import java.io.File;

// Main application window for the Better Your Picture GUI.
// It connects the album list, photo list, image preview, and
// reflection panel, and uses LibraryAdapter to talk to the model.
@ExcludeFromJacocoGeneratedReport
public class MainFrame extends JFrame implements ListSelectionListener {
    private final LibraryAdapter adapter = new LibraryAdapter();

    private final AlbumListPanel albumList = new AlbumListPanel();
    private final PhotoListPanel photoList = new PhotoListPanel();
    private final ImagePreviewPanel preview = new ImagePreviewPanel();
    private final ReflectionPanel reflection = new ReflectionPanel();

    private Album currentAlbum = null;
    private Photo currentPhoto = null;

    // MODIFIES: this
    // EFFECTS: constructs the main window, sets up layout, menus,
    // list selection listeners, loads data from disk, and
    // populates initial views.
    public MainFrame() {
        super("Better Your Picture (GUI)");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1100, 700));
        setLayout(new BorderLayout());

        JSplitPane left = new JSplitPane(JSplitPane.VERTICAL_SPLIT, albumList, photoList);
        left.setResizeWeight(0.5);
        JSplitPane right = new JSplitPane(JSplitPane.VERTICAL_SPLIT, preview, reflection);
        right.setResizeWeight(0.65);
        JSplitPane center = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left, right);
        center.setResizeWeight(0.35);
        add(center, BorderLayout.CENTER);

        albumList.getList().addListSelectionListener(this);
        photoList.getList().addListSelectionListener(this);

        setJMenuBar(new AppMenuBar(this, adapter));

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                printLogAndExit();
            }
        });

        refreshAll();
        pack();
        setLocationRelativeTo(null);
    }

    // MODIFIES: this
    // EFFECTS: reloads the album list and photo list from the adapter,
    // selects the "(All Photos)" item, clears the reflection
    // text, and repaints the frame.
    public void refreshAll() {
        albumList.setAlbums(adapter.albums(), adapter);
        albumList.selectAllPhotosItem();
        photoList.setPhotos(adapter.allPhotos(), adapter);
        reflection.setText("");
        revalidate();
        repaint();
    }

    // EFFECTS: opens a file chooser for images and returns the absolute
    // path of the selected file, or null if the user cancels.
    private String chooseImageFile() {
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Images", "jpg", "jpeg", "png", "gif", "webp"));
        int res = fc.showOpenDialog(this);
        if (res == JFileChooser.APPROVE_OPTION) {
            return fc.getSelectedFile().getAbsolutePath();
        }
        return null;
    }

    // EFFECTS: returns the album currently selected in the UI, or null
    // if "(All Photos)" is selected.
    public Album getCurrentAlbum() {
        return currentAlbum;
    }

    // EFFECTS: returns the photo currently selected in the photo list,
    // or null if none is selected.
    public Photo getCurrentPhoto() {
        return currentPhoto;
    }

    // MODIFIES: this
    // EFFECTS: responds to selection changes in the album or photo list.
    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) {
            return;
        }
        Object src = e.getSource();
        if (src == albumList.getList()) {
            handleAlbumSelection();
        } else if (src == photoList.getList()) {
            handlePhotoSelection();
        }
    }

    // MODIFIES: this
    // EFFECTS: handles selection changes in the album list.
    private void handleAlbumSelection() {
        Object value = albumList.getList().getSelectedValue();

        if (value instanceof String
                && AlbumListPanel.ALL_PHOTOS_ITEM.equals(value)) {
            currentAlbum = null;
            photoList.setPhotos(adapter.allPhotos(), adapter);
            reflection.setText("");
            reflection.setEditable(false);
        } else if (value instanceof Album) {
            currentAlbum = (Album) value;
            photoList.setPhotos(currentAlbum.getPhotos(), adapter);
            reflection.setEditable(true);
        }
    }

    // MODIFIES: this
    // EFFECTS: handles selection changes in the photo list.
    private void handlePhotoSelection() {
        Photo p = (Photo) photoList.getList().getSelectedValue();
        currentPhoto = p;

        if (p == null) {
            preview.showImage(null);
            reflection.setText("");
            return;
        }

        String path = adapter.getPhotoPath(p);
        if (path == null || path.trim().isEmpty() || !(new File(path).exists())) {
            path = chooseImageFile();
            if (path != null && !path.trim().isEmpty()) {
                adapter.setPhotoPath(p, path);
            }
        }
        preview.showImage(path);

        reflection.setText(renderPhotoReflection(p));
        reflection.setEditable(false);
    }

    // EFFECTS: builds a human readable text summary of the photo's
    // reflection, including score, problems, and comments; if
    // there is no reflection, returns a message indicating that.
    private String renderPhotoReflection(Photo p) {
        Reflection r = p.getReflection();
        if (r == null) {
            return "(No reflection for this photo)";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(makeScoreLine(r));
        sb.append(makeProblemsLine(r));
        sb.append(makeCommentsBlock(r));

        return sb.toString();
    }

    // EFFECTS: returns a line like "Score: 85\n"
    private String makeScoreLine(Reflection r) {
        return "Score: " + r.getScore() + "\n";
    }

    // EFFECTS: returns a line like "Problems: A, B\n"
    private String makeProblemsLine(Reflection r) {
        StringBuilder sb = new StringBuilder("Problems: ");
        if (r.getProblems().isEmpty()) {
            sb.append("(none)\n");
            return sb.toString();
        }
        for (int i = 0; i < r.getProblems().size(); i++) {
            sb.append(r.getProblems().get(i).name());
            if (i < r.getProblems().size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("\n");
        return sb.toString();
    }

    // EFFECTS: returns a block:
    // Comments:
    // - xxx
    // - yyy
    private String makeCommentsBlock(Reflection r) {
        StringBuilder sb = new StringBuilder("Comments:\n");
        if (r.getComments().isEmpty()) {
            sb.append("(none)");
            return sb.toString();
        }
        for (String c : r.getComments()) {
            sb.append("- ").append(c).append("\n");
        }
        return sb.toString();
    }

    // MODIFIES: this
    // updates the reflection panel to display the reflection
    // of the given photo in read-only mode.
    public void refreshReflectionForPhoto(Photo p) {
        String text = renderPhotoReflection(p);
        reflection.setText(text);
        reflection.setEditable(false);
    }

    // EFFECTS: prints all events that have been logged since the application
    // started to the console, then exits the program.
    private void printLogAndExit() {
        for (Event ev : EventLog.getInstance()) {
            System.out.println(ev.toString());
        }
        System.exit(0);
    }

    // EFFECTS: allows "Quit" menu item to trigger the same quit behaviour.
    public void quit() {
        printLogAndExit();
    }
}
