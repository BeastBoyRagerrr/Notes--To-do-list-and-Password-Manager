package gui.launch;

import core.PasswordItem;
import core.PasswordManager;
import gui.FileDialogHelper;
import exceptions.DataNotFoundException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class PasswordScreen extends JFrame {
    private final PasswordManager manager;
    private final JFrame parent;
    private DefaultListModel<String> model;
    private JList<String> list;

    public PasswordScreen(PasswordManager manager, JFrame parent) {
        super("Passwords");
        this.manager = manager;
        this.parent = parent;
        setSize(800, 500);
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
            String title = JOptionPane.showInputDialog(this, "Account title:");
            if (title == null || title.trim().isEmpty())
                return;
            String user = JOptionPane.showInputDialog(this, "Username:");
            JPasswordField passField = new JPasswordField();
            int ok = JOptionPane.showConfirmDialog(this, passField, "Enter password:", JOptionPane.OK_CANCEL_OPTION);
            String pass = "";
            if (ok == JOptionPane.OK_OPTION)
                pass = new String(passField.getPassword());
            manager.addPassword(new PasswordItem(title, user, pass));
            refreshList();
        });

        JButton edit = new JButton("Edit");
        edit.addActionListener(e -> {
            int idx = list.getSelectedIndex();
            if (idx == -1) {
                JOptionPane.showMessageDialog(this, "Select an item to edit.");
                return;
            }
            PasswordItem orig = manager.getPasswords().get(idx);
            String newTitle = JOptionPane.showInputDialog(this, "Account title:", orig.getTitle());
            if (newTitle == null || newTitle.trim().isEmpty())
                return;
            String newUser = JOptionPane.showInputDialog(this, "Username:", orig.getUsername());
            JPasswordField pf = new JPasswordField(orig.getPassword());
            int ok = JOptionPane.showConfirmDialog(this, pf, "Password:", JOptionPane.OK_CANCEL_OPTION);
            String newPass = ok == JOptionPane.OK_OPTION ? new String(pf.getPassword()) : orig.getPassword();
            PasswordItem updatedItem = new PasswordItem(newTitle, newUser, newPass);

            try {
                manager.updatePassword(idx, updatedItem);
                JOptionPane.showMessageDialog(this, "Password updated successfully!");
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
            int r = JOptionPane.showConfirmDialog(this, "Delete selected password?", "Confirm",
                    JOptionPane.YES_NO_OPTION);
            if (r == JOptionPane.YES_OPTION) {
                try {
                    manager.deletePassword(idx);
                    JOptionPane.showMessageDialog(this, "Password deleted successfully!");
                } catch (DataNotFoundException ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
                refreshList();
            }
        });

        JButton view = new JButton("View (Show/Hide)");
        view.addActionListener(e -> {
            int idx = list.getSelectedIndex();
            if (idx == -1) {
                JOptionPane.showMessageDialog(this, "Select an item to view.");
                return;
            }
            PasswordItem p = manager.getPasswords().get(idx);
            JPanel panel = new JPanel(new BorderLayout(5, 5));
            JPanel top = new JPanel(new GridLayout(2, 2));
            top.add(new JLabel("Account:"));
            top.add(new JLabel(p.getTitle()));
            top.add(new JLabel("Username:"));
            top.add(new JLabel(p.getUsername()));
            panel.add(top, BorderLayout.NORTH);
            JPasswordField pf = new JPasswordField(p.getPassword());
            pf.setEditable(false);
            pf.setEchoChar('*');
            JToggleButton toggle = new JToggleButton("Show");
            toggle.addActionListener(a -> {
                if (toggle.isSelected()) {
                    pf.setEchoChar((char) 0);
                    toggle.setText("Hide");
                } else {
                    pf.setEchoChar('*');
                    toggle.setText("Show");
                }
            });
            JPanel mid = new JPanel(new FlowLayout(FlowLayout.LEFT));
            mid.add(new JLabel("Password:"));
            mid.add(pf);
            mid.add(toggle);
            panel.add(mid, BorderLayout.CENTER);
            JOptionPane.showMessageDialog(this, panel, "View Password", JOptionPane.PLAIN_MESSAGE);
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
        bottom.add(view);
        // Save/Load are available in File menu (top)
        bottom.add(back);
        add(bottom, BorderLayout.SOUTH);
        refreshList();
    }

    private void refreshList() {
        model.clear();
        List<PasswordItem> listData = manager.getPasswords();
        for (PasswordItem p : listData)
            model.addElement(p.getTitle() + "  (" + p.getCreatedAt() + ")");
    }
}
