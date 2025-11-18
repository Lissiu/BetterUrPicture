package ui.gui;


import ui.gui.adapters.LibraryAdapter;

import javax.swing.*;

import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;

import java.io.File;

// Optional toolbar for quick access to common actions such as
// importing photos and managing albums.
@ExcludeFromJacocoGeneratedReport
public class Toolbar extends JToolBar {
    private final MainFrame frame;
    private final LibraryAdapter adapter;

    // MODIFIES: this
    // EFFECTS: constructs an empty, non-floatable toolbar; action
    // buttons can be added if needed.

    public Toolbar(MainFrame frame, LibraryAdapter adapter) {
        this.frame = frame;
        this.adapter = adapter;
        setFloatable(false);

    }

  
    // EFFECTS: shows an input dialog with the prompt and returns the
    // user's input, or null if cancelled.
    private String ask(String prompt) {
        return JOptionPane.showInputDialog(frame, prompt);
    }


    // EFFECTS: asks the user for an integer; if parsing fails, returns
    // the default value def.
    private int askInt(String prompt, int def) {
        try {
            String s = ask(prompt);
            if (s == null) {
                return def;
            }
            return Integer.parseInt(s);
        } catch (Exception e) {
            return def;
        }
    }

    // EFFECTS: asks the user for a double; if parsing fails, returns
    // the default value def.
    private double askDouble(String prompt, double def) {
        try {
            String s = ask(prompt);
            if (s == null) {
                return def;
            }
            return Double.parseDouble(s);
        } catch (Exception e) {
            return def;
        }
    }


    // EFFECTS: returns a File in dir whose name is base or base_(k)
    // if the original name already exists.
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


    // EFFECTS: returns the file name without extension if one exists;
    // otherwise returns the original string.
    private String stripExt(String n) {
        int d = n.lastIndexOf('.');
        return d >= 0 ? n.substring(0, d) : n;
    }


    // EFFECTS: shows a simple message dialog attached to the frame.
    private void msg(String m) {
        JOptionPane.showMessageDialog(frame, m);
    }
}
