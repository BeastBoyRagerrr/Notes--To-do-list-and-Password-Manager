package gui.launch;

import core.NoteManager;
import core.PasswordManager;
import core.TodoManager;
import gui.HomeScreen;

import javax.swing.*;

public class MainLauncher {
    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> {
            NoteManager noteManager = new NoteManager();
            PasswordManager passwordManager = new PasswordManager();
            TodoManager todoManager = new TodoManager();

            // try load default files (silent)
            try { noteManager.loadFromFile(null); } catch (Exception ignored) {}
            try { passwordManager.loadFromFile(null); } catch (Exception ignored) {}
            try { todoManager.loadFromFile(null); } catch (Exception ignored) {}

            HomeScreen home = new HomeScreen(noteManager, passwordManager, todoManager);
            home.setVisible(true);
        });
    }
}
