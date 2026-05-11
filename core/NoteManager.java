package core;

import exceptions.DataNotFoundException;
import exceptions.SaveFailedException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class NoteManager implements Storable {

    private List<Note> notes = new ArrayList<>();
    private final String defaultFile = "notes.ser";

    public void addNote(Note note) {
        notes.add(note);
    }

    public Note getNote(int index) throws DataNotFoundException {
        if (index < 0 || index >= notes.size()) {
            throw new DataNotFoundException("Invalid note index!");
        }
        return notes.get(index);
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void updateNote(int index, Note updatedNote) throws DataNotFoundException {
        if (index < 0 || index >= notes.size()) {
            throw new DataNotFoundException("Invalid note index!");
        }
        notes.set(index, updatedNote);
    }

    public void deleteNote(int index) throws DataNotFoundException {
        if (index < 0 || index >= notes.size()) {
            throw new DataNotFoundException("Invalid note index!");
        }
        notes.remove(index);
    }

    public int size() {
        return notes.size();
    }

    @Override
    public void saveToFile(String filePath) throws IOException, SaveFailedException {
        if (filePath == null || filePath.isEmpty()) {
            filePath = defaultFile;
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(notes);
        } catch (IOException e) {
            throw new SaveFailedException("Failed to save notes: " + e.getMessage());
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
                notes = (List<Note>) obj;
            }
        }
    }
}
