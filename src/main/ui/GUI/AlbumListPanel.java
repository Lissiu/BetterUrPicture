package ui.GUI;

import model.Album;
import ui.GUI.adapters.LibraryAdapter;

import javax.swing.*;
import java.awt.*;
import java.util.List;


// Panel that displays a list of albums and an "(All Photos)" item.
// The list is used by MainFrame for selection handling.

public class AlbumListPanel extends JPanel {
    public static final String ALL_PHOTOS_ITEM = "(All Photos)";

    private final DefaultListModel<Object> model = new DefaultListModel<>();
    private final JList<Object> list = new JList<>(model);


// MODIFIES: this
// EFFECTS:  constructs the album list panel with an empty list and
//          configures basic layout.

    public AlbumListPanel() {
        setLayout(new BorderLayout());
        add(new JLabel("Albums"), BorderLayout.NORTH);

        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setCellRenderer(new DefaultListCellRenderer());

        add(new JScrollPane(list), BorderLayout.CENTER);
        setPreferredSize(new Dimension(260, 350));
    }


// REQUIRES: albums and adapter are not null
// MODIFIES: this
// EFFECTS:  fills the list model with the special "(All Photos)" item
//          followed by all albums, and sets a renderer that uses
//          adapter.albumLabel for album entries.

    public void setAlbums(List<Album> albums, LibraryAdapter adapter) {
        model.clear();
        model.addElement(ALL_PHOTOS_ITEM);
        for (Album a : albums) {
            model.addElement(a);
        }

        list.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> l, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                JLabel lbl = (JLabel) super.getListCellRendererComponent(
                        l, value, index, isSelected, cellHasFocus);
                if (value instanceof String) {
                    lbl.setText((String) value);
                } else if (value instanceof Album) {
                    lbl.setText(adapter.albumLabel((Album) value));
                }
                return lbl;
            }
        });
    }


// REQUIRES: list has at least one element
// MODIFIES: this
// EFFECTS:  selects the "(All Photos)" entry at index 0.

    public void selectAllPhotosItem() {
        list.setSelectedIndex(0);
    }



// EFFECTS:  returns the underlying JList used for album selection.

    public JList<Object> getList() {
        return list;
    }
}
