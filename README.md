# Smart Productivity Manager

A Java-based desktop productivity application that helps users manage notes, passwords, and daily tasks through an interactive GUI interface.

---

## Features

- Notes management system
- Password storage and organization
- To-do task management
- Java Swing based GUI
- File handling and data persistence
- Custom exception handling
- Modular project architecture
- Simple and user-friendly interface

---

## Project Structure

```bash
src/
├── core/
│   ├── Item.java
│   ├── Note.java
│   ├── NoteManager.java
│   ├── PasswordItem.java
│   ├── PasswordManager.java
│   ├── TodoItem.java
│   └── TodoManager.java
│
├── exceptions/
│   ├── DataNotFoundException.java
│   ├── InvalidChoiceException.java
│   └── SaveFailedException.java
│
├── gui/
│   ├── FileDialogHelper.java
│   ├── HomeScreen.java
│   └── launch/
│       ├── MainLauncher.java
│       ├── NotesScreen.java
│       ├── PasswordScreen.java
│       └── TodoScreen.java
│
└── README.md
```

---

## Technologies Used

- Java
- Java Swing
- File I/O
- Exception Handling
- Object-Oriented Programming

---

## Requirements

- Java JDK 17+ (or higher)
- Eclipse / IntelliJ IDEA / VS Code

---

## Building & Running

### Compile Project

```bash
javac src/gui/launch/MainLauncher.java
```

### Run Application

```bash
java src.gui.launch.MainLauncher
```

---

## Core Modules

### Notes Manager
Create, edit, save, and manage notes.

### Password Manager
Store and organize password information securely.

### Todo Manager
Manage daily tasks with status tracking.

### Exception Handling
Handles invalid operations and file-related errors gracefully.

---

## Future Enhancements

- Database integration
- User authentication
- Encryption for passwords
- Cloud synchronization
- Dark mode UI
- Search and filter support

---

## Author

Developed as a Java desktop application project for productivity and task management.

---

## License

Open Source – Free to use and modify.
