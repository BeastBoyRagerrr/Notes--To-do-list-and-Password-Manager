package gui.launch;

import core.Note;
import core.NoteManager;
import gui.FileDialogHelper;
import exceptions.DataNotFoundException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class NotesScreen extends JFrame {
    private final NoteManager manager;
    private final JFrame parent;
    private DefaultListModel<String> model;
    private JList<String> list;

    public NotesScreen(NoteManager manager, JFrame parent) {
        super("Notes");
        this.manager = manager;
        this.parent = parent;
        setSize(700, 500);
        setLocationRelativeTo(null);
        init();
    }

    private void init() {
        // Create a top menu bar with Save and Load
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem saveItem = new JMenuItem("Save");
        saveItem.addActionListener(e -> FileDialogHelper.saveToFile(this, manager));
        JMenuItem loadItem = new JMenuItem("Load");
        loadItem.addActionListener(e -> {
            FileDialogHelper.loadFromFile(this, manager);
            refreshList();
        });
        fileMenu.add(saveItem);
        fileMenu.add(loadItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
        setLayout(new BorderLayout());
        model = new DefaultListModel<>();
        list = new JList<>(model);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(list), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton add = new JButton("Add");
        add.addActionListener((ActionEvent e) -> {
            String title = JOptionPane.showInputDialog(this, "Title:");
            if (title == null || title.trim().isEmpty())
                return;
            String content = JOptionPane.showInputDialog(this, "Content:");
            manager.addNote(new Note(title, content));
            refreshList();
        });

        JButton edit = new JButton("Edit");
        edit.addActionListener(e -> {
            int idx = list.getSelectedIndex();
            if (idx == -1) {
                JOptionPane.showMessageDialog(this, "Select an item to edit.");
                return;
            }
            Note orig = manager.getNotes().get(idx);
            String newTitle = JOptionPane.showInputDialog(this, "Title:", orig.getTitle());
            if (newTitle == null || newTitle.trim().isEmpty())
                return;
            String newContent = JOptionPane.showInputDialog(this, "Content:", orig.getContent());
            Note updated = new Note(newTitle, newContent);
            // perform update via manager and handle checked exception
            try {
                manager.updateNote(idx, updated);
                JOptionPane.showMessageDialog(this, "Note updated successfully!");
            } catch (DataNotFoundException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            refreshList();
        });

        JButton del = new JButton("Delete");
        del.addActionListener(e -> {
            int idx = list.getSelectedIndex();
            if (idx == -1) {
                JOptionPane.showMessageDialog(this, "Select an item to delete.");
                return;
            }
            int r = JOptionPane.showConfirmDialog(this, "Delete selected note?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (r == JOptionPane.YES_OPTION) {
                try {
                    manager.deleteNote(idx);
                    JOptionPane.showMessageDialog(this, "Note deleted successfully!");
                } catch (DataNotFoundException ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
                refreshList();
            }
        });

        // Save and Load moved to File menu on top
        JButton back = new JButton("Back");
        back.addActionListener(e -> {
            this.setVisible(false);
            parent.setVisible(true);
        });

        bottom.add(add);
        bottom.add(edit);
        bottom.add(del);
        // Save/Load are available in File menu (top)
        bottom.add(back);
        add(bottom, BorderLayout.SOUTH);
        refreshList();
    }

    private void refreshList() {
        model.clear();
        List<Note> notes = manager.getNotes();
        for (Note n : notes)
            model.addElement(n.getTitle() + "  (" + n.getCreatedAt() + ")");
    }
}
