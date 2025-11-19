package ui.gui;

import model.Album;
import ui.gui.adapters.LibraryAdapter;

import javax.swing.*;

import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;

import java.awt.*;
import java.util.List;

// Panel that displays a list of albums and an "(All Photos)" item.
@ExcludeFromJacocoGeneratedReport
public class AlbumListPanel extends JPanel {
    public static final String ALL_PHOTOS_ITEM = "(All Photos)";

    private final DefaultListModel<Object> model = new DefaultListModel<Object>();
    private final JList<Object> list = new JList<Object>(model);

    // MODIFIES: this
    // EFFECTS: constructs the album list panel with an empty list and
    // configures basic layout.
    public AlbumListPanel() {
        setLayout(new BorderLayout());
        add(new JLabel("Albums"), BorderLayout.NORTH);

        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(list), BorderLayout.CENTER);
        setPreferredSize(new Dimension(260, 350));
    }

    // MODIFIES: this
    // EFFECTS: fills the list model with the special "(All Photos)" item
    // followed by all albums, and sets a renderer that uses
    // adapter.albumLabel for album entries.
    public void setAlbums(List<Album> albums, LibraryAdapter adapter) {
        model.clear();
        model.addElement(ALL_PHOTOS_ITEM);
        for (Album a : albums) {
            model.addElement(a);
        }
        list.setCellRenderer(new AlbumListRenderer(adapter));
    }

    // REQUIRES: list has at least one element
    // MODIFIES: this
    // EFFECTS: selects the "(All Photos)" entry at index 0.
    public void selectAllPhotosItem() {
        list.setSelectedIndex(0);
    }

    // EFFECTS: returns the underlying JList used for album selection.
    public JList<Object> getList() {
        return list;
    }

   
}
