package gui;

import core.NoteManager;
import core.PasswordManager;
import core.TodoManager;

import javax.swing.*;
import java.awt.*;

public class HomeScreen extends JFrame {
    private NoteManager noteManager;
    private PasswordManager passwordManager;
    private TodoManager todoManager;

    public HomeScreen(NoteManager nm, PasswordManager pm, TodoManager tm){
        super("Personal Manager - Home");
        this.noteManager = nm; this.passwordManager = pm; this.todoManager = tm;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420,320);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI(){
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12,12,12,12);
        gbc.gridx = 0; gbc.gridy = 0; gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel heading = new JLabel("Personal Manager"); heading.setFont(new Font("SansSerif", Font.BOLD, 18));
        add(heading, gbc);

        gbc.gridy++;
        JButton notesBtn = new JButton("Notes");
        notesBtn.addActionListener(e -> { new gui.launch.NotesScreen(noteManager, this).setVisible(true); this.setVisible(false); });
        add(notesBtn, gbc);

        gbc.gridy++;
        JButton todoBtn = new JButton("To-Do List");
        todoBtn.addActionListener(e -> { new gui.launch.TodoScreen(todoManager, this).setVisible(true); this.setVisible(false); });
        add(todoBtn, gbc);

        gbc.gridy++;
        JButton passBtn = new JButton("Passwords");
        passBtn.addActionListener(e -> { new gui.launch.PasswordScreen(passwordManager, this).setVisible(true); this.setVisible(false); });
        add(passBtn, gbc);
    }
}
