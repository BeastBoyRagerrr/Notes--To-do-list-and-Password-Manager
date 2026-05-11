package gui.launch;

import core.TodoItem;
import core.TodoManager;
import gui.FileDialogHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TodoScreen extends JFrame {
    private TodoManager manager;
    private JFrame parent;
    private DefaultListModel<String> model;
    private JList<String> list;

    public TodoScreen(TodoManager manager, JFrame parent) {
        super("To-do List");
        this.manager = manager;
        this.parent = parent;
        setSize(900, 600);
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
            refreshList(TodoManager.Period.ALL);
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
            try {
                String title = JOptionPane.showInputDialog(this, "Task title:");
                if (title == null || title.trim().isEmpty())
                    return;
                String desc = JOptionPane.showInputDialog(this, "Description:");
                String dl = JOptionPane.showInputDialog(this,
                        "Deadline (dd-MMM-yyyy hh:mm a) e.g. 26-Nov-2025 05:30 PM:");
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
                Date deadline = sdf.parse(dl);
                String prStr = JOptionPane.showInputDialog(this, "User priority (1-5):");
                int p = Integer.parseInt(prStr);
                int forced = JOptionPane.showConfirmDialog(this, "Force HIGH priority for this task?", "Priority",
                        JOptionPane.YES_NO_OPTION);
                boolean forceHigh = forced == JOptionPane.YES_OPTION;
                manager.addTask(new TodoItem(title, desc, deadline, p, forceHigh));
                refreshList(TodoManager.Period.ALL);
            } catch (ParseException pe) {
                JOptionPane.showMessageDialog(this, "Invalid date format: " + pe.getMessage(), "Parse Error",
                        JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton edit = new JButton("Edit");
        edit.addActionListener(e -> {
            int idx = list.getSelectedIndex();
            if (idx == -1) {
                JOptionPane.showMessageDialog(this, "Select an item to edit.");
                return;
            }
            try {
                TodoItem orig = manager.getTasks().get(idx);
                String newTitle = JOptionPane.showInputDialog(this, "Task title:", orig.getTitle());
                if (newTitle == null || newTitle.trim().isEmpty())
                    return;
                String newDesc = JOptionPane.showInputDialog(this, "Description:", orig.getDescription());
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
                String dl = JOptionPane.showInputDialog(this, "Deadline (dd-MMM-yyyy hh:mm a):",
                        sdf.format(orig.getDeadline()));
                Date deadline = sdf.parse(dl);
                String prStr = JOptionPane.showInputDialog(this, "User priority (1-5):",
                        String.valueOf(orig.getUserPriority()));
                int p = Integer.parseInt(prStr);
                int forced = JOptionPane.showConfirmDialog(this, "Force HIGH priority for this task?", "Priority",
                        JOptionPane.YES_NO_OPTION);
                boolean forceHigh = forced == JOptionPane.YES_OPTION;
                manager.updateTask(idx, new TodoItem(newTitle, newDesc, deadline, p, forceHigh));
                refreshList(TodoManager.Period.ALL);
            } catch (ParseException pe) {
                JOptionPane.showMessageDialog(this, "Invalid date format: " + pe.getMessage(), "Parse Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton del = new JButton("Delete");
        del.addActionListener(e -> {
            int idx = list.getSelectedIndex();
            if (idx == -1) {
                JOptionPane.showMessageDialog(this, "Select an item to delete.");
                return;
            }
            int r = JOptionPane.showConfirmDialog(this, "Delete selected task?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (r == JOptionPane.YES_OPTION) {
                manager.deleteTask(idx);
                refreshList(TodoManager.Period.ALL);
            }
        });

        JComboBox<String> filter = new JComboBox<>(new String[] { "DAY", "WEEK", "MONTH", "ALL" });
        JButton show = new JButton("Show Due");
        show.addActionListener(e -> {
            String sel = (String) filter.getSelectedItem();
            refreshList(TodoManager.Period.valueOf(sel));
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
        bottom.add(filter);
        bottom.add(show);
        bottom.add(back);
        // Note: save/load were moved to the top File menu
        add(bottom, BorderLayout.SOUTH);
        refreshList(TodoManager.Period.ALL);
    }

    private void refreshList(TodoManager.Period p) {
        model.clear();
        java.util.List<TodoItem> listData = manager.getDueTasks(p);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
        for (TodoItem t : listData) {
            String label = String.format("%s  [%s]  Pri:%d%s  DaysLeft:%d",
                    t.getTitle(), sdf.format(t.getDeadline()), t.getUserPriority(),
                    t.isForceHighPriority() ? "(FORCED)" : "",
                    t.getDaysRemaining());
            model.addElement(label);
        }
    }
}
