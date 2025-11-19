package ui.gui;

import model.Photo;

import javax.swing.*;

import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;

import java.awt.*;

@ExcludeFromJacocoGeneratedReport
public class PhotoListRenderer extends DefaultListCellRenderer {

    // EFFECTS: sets the label text to the photo name
    @Override
    public Component getListCellRendererComponent(
            JList<?> list, Object value, int index,
            boolean isSelected, boolean cellHasFocus) {

        JLabel lbl = (JLabel) super.getListCellRendererComponent(
                list, value, index, isSelected, cellHasFocus);

        if (value instanceof Photo) {
            lbl.setText(((Photo) value).getPhotoname());
        }
        return lbl;
    }
}
