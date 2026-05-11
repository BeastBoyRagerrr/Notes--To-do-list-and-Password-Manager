package gui;

import core.Storable;
import javax.swing.*;
import java.awt.*;
import java.io.File;

public class FileDialogHelper {

    public static void saveToFile(Component parent, Storable store){
        FileDialog fd = new FileDialog((Frame) SwingUtilities.getWindowAncestor(parent), "Save File", FileDialog.SAVE);
        fd.setVisible(true);
        String dir = fd.getDirectory(); String file = fd.getFile();
        if(dir==null || file==null) return;
        String path = dir + File.separator + file;
        try { store.saveToFile(path); } catch(Exception e){ JOptionPane.showMessageDialog(parent, "Save failed: "+e.getMessage(), "Save Error", JOptionPane.ERROR_MESSAGE); }
    }

    public static void loadFromFile(Component parent, Storable store){
        FileDialog fd = new FileDialog((Frame) SwingUtilities.getWindowAncestor(parent), "Open File", FileDialog.LOAD);
        fd.setVisible(true);
        String dir = fd.getDirectory(); String file = fd.getFile();
        if(dir==null || file==null) return;
        String path = dir + File.separator + file;
        try { store.loadFromFile(path); } catch(Exception e){ JOptionPane.showMessageDialog(parent, "Load failed: "+e.getMessage(), "Load Error", JOptionPane.ERROR_MESSAGE); }
    }
}
