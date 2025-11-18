package ui.GUI;

import model.Photo;
import ui.GUI.adapters.LibraryAdapter;

import javax.swing.*;
import java.awt.*;
import java.util.List;


// Panel that shows the list of photos for the current view
// (all photos or photos of a selected album).

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
        list.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> l, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                JLabel lbl = (JLabel) super.getListCellRendererComponent(
                        l, value, index, isSelected, cellHasFocus);
                if (value instanceof Photo) {
                    lbl.setText(((Photo) value).getPhotoname());
                }
                return lbl;
            }
        });
        add(new JScrollPane(list), BorderLayout.CENTER);
        setPreferredSize(new Dimension(260, 350));
    }


// REQUIRES: photos and adapter are not null
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


