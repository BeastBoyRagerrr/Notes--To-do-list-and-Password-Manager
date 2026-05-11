package core;

import exceptions.SaveFailedException;
import javax.swing.*;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class TodoManager implements Storable {
    private List<TodoItem> tasks = new ArrayList<>();
    private final String defaultFile = "todos.ser";

    public enum Period { DAY, WEEK, MONTH, ALL }

    public void addTask(TodoItem t){ tasks.add(t); }
    public List<TodoItem> getTasks(){ return tasks; }
    public void updateTask(int index, TodoItem t){ if(index>=0 && index<tasks.size()) tasks.set(index,t); }
    public void deleteTask(int index){ if(index>=0 && index<tasks.size()) tasks.remove(index); }

    public List<TodoItem> getDueTasks(Period p){
        Calendar now = Calendar.getInstance();
        return tasks.stream()
                .filter(t -> {
                    Calendar d = Calendar.getInstance();
                    d.setTime(t.getDeadline());
                    switch (p){
                        case DAY:
                            return now.get(Calendar.YEAR)==d.get(Calendar.YEAR)
                                && now.get(Calendar.DAY_OF_YEAR)==d.get(Calendar.DAY_OF_YEAR);
                        case WEEK:
                            return now.get(Calendar.YEAR)==d.get(Calendar.YEAR)
                                && now.get(Calendar.WEEK_OF_YEAR)==d.get(Calendar.WEEK_OF_YEAR);
                        case MONTH:
                            return now.get(Calendar.YEAR)==d.get(Calendar.YEAR)
                                && now.get(Calendar.MONTH)==d.get(Calendar.MONTH);
                        case ALL:
                        default:
                            return true;
                    }
                })
                .sorted((a,b) -> {
                    if (a.isForceHighPriority() && !b.isForceHighPriority()) return -1;
                    if (!a.isForceHighPriority() && b.isForceHighPriority()) return 1;
                    double pa = a.getPriorityScore(); double pb = b.getPriorityScore();
                    if (pa>pb) return -1; if (pa<pb) return 1;
                    return a.getDeadline().compareTo(b.getDeadline());
                })
                .collect(Collectors.toList());
    }

    public void displayAll(){ if(tasks.isEmpty()){ System.out.println("No tasks."); return; } tasks.forEach(t-> System.out.println(t)); }

    @Override
    public void saveToFile(String filePath) throws IOException, SaveFailedException {
        String path = filePath == null ? defaultFile : filePath;
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path))){
            oos.writeObject(tasks);
            JOptionPane.showMessageDialog(null, "To-do list saved to " + path);
        } catch (IOException e){
            JOptionPane.showMessageDialog(null, "Failed to save todos: " + e.getMessage(), "Save Error", JOptionPane.ERROR_MESSAGE);
            throw e;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void loadFromFile(String filePath) throws IOException, ClassNotFoundException {
        String path = filePath == null ? defaultFile : filePath;
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path))){
            Object obj = ois.readObject();
            if (obj instanceof List){ tasks = (List<TodoItem>) obj; JOptionPane.showMessageDialog(null, "Todos loaded from " + path); }
        } catch (FileNotFoundException e){
            JOptionPane.showMessageDialog(null, "Todos file not found: " + path, "Load Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
