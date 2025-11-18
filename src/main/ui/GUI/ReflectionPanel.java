package ui.gui;

import javax.swing.*;

import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;

import java.awt.*;
import java.util.function.Consumer;


// Panel that shows and optionally edits a single block of text for
// reflections.
@ExcludeFromJacocoGeneratedReport
public class ReflectionPanel extends JPanel {
    private final JTextArea area = new JTextArea(8, 40);
    private final JButton save = new JButton("Save Reflection");
    private Consumer<String> onSave = t -> {
    };


// MODIFIES: this
// EFFECTS:  constructs the reflection panel with a text area and a
//          save button; by default the save handler does nothing.

    public ReflectionPanel() {
        setLayout(new BorderLayout());
        add(new JLabel("Reflection"), BorderLayout.NORTH);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        add(new JScrollPane(area), BorderLayout.CENTER);
        save.addActionListener(e -> onSave.accept(area.getText()));
        add(save, BorderLayout.SOUTH);
    }


// MODIFIES: this
// EFFECTS:  sets the text of the reflection area; null becomes empty.

    public void setText(String t) {
        area.setText(t == null ? "" : t);
    }



// MODIFIES: this
// EFFECTS:  sets whether the reflection area is editable and whether
//          the save button is enabled.

    public void setEditable(boolean editable) {
        area.setEditable(editable);
        save.setEnabled(editable);
    }



// MODIFIES: this
// EFFECTS:  installs a callback that is invoked when the save button
//          is clicked, passing the current text content.

    public void onSave(Consumer<String> c) {
        this.onSave = c;
    }
}
