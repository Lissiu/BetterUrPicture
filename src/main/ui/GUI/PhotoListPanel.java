package ui.gui;

import model.Photo;
import ui.gui.adapters.LibraryAdapter;

import javax.swing.*;

import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;

import java.awt.*;
import java.util.List;


// Panel that shows the list of photos for the current view
// (all photos or photos of a selected album).
@ExcludeFromJacocoGeneratedReport
public class PhotoListPanel extends JPanel {
    private final DefaultListModel<Photo> model = new DefaultListModel<>();
    private final JList<Photo> list = new JList<>(model);


// MODIFIES: this
// EFFECTS:  constructs the photo list panel with an empty list and
//          configures the renderer to show photo names.

    public PhotoListPanel() {
        setLayout(new BorderLayout());
        add(new JLabel("Photos"), BorderLayout.NORTH);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setCellRenderer(new PhotoListRenderer());
        add(new JScrollPane(list), BorderLayout.CENTER);
        setPreferredSize(new Dimension(260, 350));
    }


// MODIFIES: this
// EFFECTS:  clears the current list and fills it with the given
//          photos.

    public void setPhotos(List<Photo> photos, LibraryAdapter adapter) {
        model.clear();
        for (Photo p : photos) {
            model.addElement(p);
        }
    }


// EFFECTS:  returns the underlying JList of photos.

    public JList<Photo> getList() {
        return list;
    }
}


