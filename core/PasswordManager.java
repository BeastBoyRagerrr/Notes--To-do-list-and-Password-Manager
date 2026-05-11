package core;

import exceptions.DataNotFoundException;
import exceptions.SaveFailedException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PasswordManager implements Storable {

    private List<PasswordItem> passwords = new ArrayList<>();
    private final String defaultFile = "passwords.ser";

    public void addPassword(PasswordItem item) {
        passwords.add(item);
    }

    public PasswordItem getPassword(int index) throws DataNotFoundException {
        if (index < 0 || index >= passwords.size()) {
            throw new DataNotFoundException("Invalid password index!");
        }
        return passwords.get(index);
    }

    public List<PasswordItem> getPasswords() {
        return passwords;
    }

    public void updatePassword(int index, PasswordItem updatedItem) throws DataNotFoundException {
        if (index < 0 || index >= passwords.size()) {
            throw new DataNotFoundException("Invalid password index!");
        }
        passwords.set(index, updatedItem);
    }

    public void deletePassword(int index) throws DataNotFoundException {
        if (index < 0 || index >= passwords.size()) {
            throw new DataNotFoundException("Invalid password index!");
        }
        passwords.remove(index);
    }

    public int size() {
        return passwords.size();
    }

    @Override
    public void saveToFile(String filePath) throws IOException, SaveFailedException {
        if (filePath == null || filePath.isEmpty()) {
            filePath = defaultFile;
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(passwords);
        } catch (IOException e) {
            throw new SaveFailedException("Failed to save passwords: " + e.getMessage());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void loadFromFile(String filePath) throws IOException, ClassNotFoundException {
        if (filePath == null || filePath.isEmpty()) {
            filePath = defaultFile;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            Object obj = ois.readObject();
            if (obj instanceof List<?>) {
                passwords = (List<PasswordItem>) obj;
            }
        }
    }
}
