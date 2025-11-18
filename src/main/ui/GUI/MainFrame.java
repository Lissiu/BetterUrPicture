package ui.GUI;

import model.Album;
import model.Photo;
import ui.GUI.adapters.LibraryAdapter;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.io.File;


// Main application window for the Better Your Picture GUI.
// It connects the album list, photo list, image preview, and
// reflection panel, and uses LibraryAdapter to talk to the model.

public class MainFrame extends JFrame implements ListSelectionListener {
    private final LibraryAdapter adapter = new LibraryAdapter();

    private final AlbumListPanel albumList = new AlbumListPanel();
    private final PhotoListPanel photoList = new PhotoListPanel();
    private final ImagePreviewPanel preview = new ImagePreviewPanel();
    private final ReflectionPanel reflection = new ReflectionPanel();
    private final Toolbar toolbar = new Toolbar(this, adapter);

    private Album currentAlbum = null;
    private Photo currentPhoto = null;



// MODIFIES: this
// EFFECTS:  constructs the main window, sets up layout, menus,
//          list selection listeners, loads data from disk, and
//          populates initial views.

    public MainFrame() {
        super("Better Your Picture (GUI)");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1100, 700));
        setLayout(new BorderLayout());
        add(toolbar, BorderLayout.NORTH);

        JSplitPane left = new JSplitPane(JSplitPane.VERTICAL_SPLIT, albumList, photoList);
        left.setResizeWeight(0.5);
        JSplitPane right = new JSplitPane(JSplitPane.VERTICAL_SPLIT, preview, reflection);
        right.setResizeWeight(0.65);
        JSplitPane center = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left, right);
        center.setResizeWeight(0.35);
        add(center, BorderLayout.CENTER);

        albumList.getList().addListSelectionListener(this);
        photoList.getList().addListSelectionListener(this);

        reflection.onSave(new java.util.function.Consumer<String>() {
            @Override
            public void accept(String txt) {
                if (currentAlbum != null) {
                    adapter.setAlbumReflection(currentAlbum, txt);
                    JOptionPane.showMessageDialog(MainFrame.this,
                            "Reflection saved for " + currentAlbum.getAlbumName());
                }
            }
        });

        setJMenuBar(new AppMenuBar(this, adapter));

        adapter.loadAll();
        refreshAll();
        pack();
        setLocationRelativeTo(null);
    }



// MODIFIES: this
// EFFECTS:  reloads the album list and photo list from the adapter,
//          selects the "(All Photos)" item, clears the reflection
//           text, and repaints the frame.

    public void refreshAll() {
        albumList.setAlbums(adapter.albums(), adapter);
        albumList.selectAllPhotosItem();
        photoList.setPhotos(adapter.allPhotos(), adapter);
        reflection.setText("");
        revalidate();
        repaint();
    }


// EFFECTS:  opens a file chooser for images and returns the absolute
//          path of the selected file, or null if the user cancels.

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


// EFFECTS:  returns the album currently selected in the UI, or null
//          if "(All Photos)" is selected.

    public Album getCurrentAlbum() {
        return currentAlbum;
    }



// EFFECTS:  returns the photo currently selected in the photo list,
//           or null if none is selected.

    public Photo getCurrentPhoto() {
        return currentPhoto;
    }


// REQUIRES: e is not null
// MODIFIES: this
// EFFECTS:  responds to selection changes in the album or photo list.
//           When an album is selected, updates the photo list and
//          album reflection text; when "(All Photos)" is selected,
//          shows all photos and clears the reflection; when a photo
//           is selected, updates the image preview and shows that
//          photo's reflection in read-only form.

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) {
            return;
        }

        Object src = e.getSource();

        if (src == albumList.getList()) {
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
                reflection.setText(adapter.getAlbumReflection(currentAlbum));
                reflection.setEditable(true);
            }
        } else if (src == photoList.getList()) {
            Photo p = (Photo) photoList.getList().getSelectedValue();
            currentPhoto = p;

            if (p == null) {
                preview.showImage(null);
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
    }


// REQUIRES: p is not null

// EFFECTS:  builds a human readable text summary of the photo's
//          reflection, including score, problems, and comments; if
//          there is no reflection, returns a message indicating that.

    private String renderPhotoReflection(Photo p) {
        model.Reflection r = p.getReflection();
        if (r == null) {
            return "(No reflection for this photo)";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Score: ").append(r.getScore()).append("\n");
        sb.append("Problems: ");
        if (r.getProblems().isEmpty()) {
            sb.append("(none)");
        } else {
            for (int i = 0; i < r.getProblems().size(); i++) {
                sb.append(r.getProblems().get(i).name());
                if (i < r.getProblems().size() - 1) {
                    sb.append(", ");
                }
            }
        }
        sb.append("\nComments:\n");
        if (r.getComments().isEmpty()) {
            sb.append("(none)");
        } else {
            for (String c : r.getComments()) {
                sb.append("- ").append(c).append("\n");
            }
        }
        return sb.toString();
    }
}

