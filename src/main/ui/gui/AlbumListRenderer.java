package ui.gui;

import model.Album;
import ui.gui.adapters.LibraryAdapter;

import javax.swing.*;

import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;

import java.awt.*;

// represents a custom cell renderer for displaying album items in a JList.
@ExcludeFromJacocoGeneratedReport
public class AlbumListRenderer extends DefaultListCellRenderer {

    private final LibraryAdapter adapter;

    // EFFECTS: constructs a renderer using the adapter
    public AlbumListRenderer(LibraryAdapter adapter) {
        this.adapter = adapter;
    }

    // EFFECTS: sets the label text for album or string items
    @Override
    public Component getListCellRendererComponent(
            JList<?> list, Object value, int index,
            boolean isSelected, boolean cellHasFocus) {

        JLabel lbl = (JLabel) super.getListCellRendererComponent(
                list, value, index, isSelected, cellHasFocus);

        if (value instanceof String) {
            lbl.setText((String) value);
        } else if (value instanceof Album) {
            lbl.setText(adapter.albumLabel((Album) value));
        }
        return lbl;
    }
}

