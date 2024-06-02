package org.example.cab302project.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.collections.ObservableList;


import org.example.cab302project.PageFunctions;

/**
 * Controller for the Notes Page, handling all UI actions and file management for notes.
 */
public class NotesPageController {

    private Path notesDirectory = Paths.get(System.getenv("APPDATA"), "On-Task", "Notes");
    private String preferredEditor;
    private String selectedFileName;

    @FXML
    private ListView<String> fileList;
    @FXML
    public TextField fileNameField;
    @FXML
    private TextField newFileNameField;
    @FXML
    HBox hBox;
    @FXML
    private Label editorLabel;
    @FXML
    private Label notesDirLabel;

    /**
     * Sets the directory where notes are stored and refreshes the list of files.
     *
     * @param newDirectory The new directory path to set.
     */
    public void setNotesDirectory(Path newDirectory) {
        this.notesDirectory = newDirectory;
        refreshFileList();
    }

    /**
     * Returns the list of file names in the current notes directory.
     *
     * @return ObservableList of file names.
     */
    public ObservableList<String> getFileList() {
        return fileList.getItems();
    }

    /**
     * Sets the name of the file currently selected in the UI.
     *
     * @param fileName The name of the file to mark as selected.
     */
    public void setSelectedFileName(String fileName) {
        this.selectedFileName = fileName;
    }

    /**
     * Sets the file name in the file name text field.
     *
     * @param fileName The file name to set.
     */
    public void setFileName(String fileName) {
        fileNameField.setText(fileName);
    }

    /**
     * Sets a new file name in the new file name text field.
     *
     * @param newFileName The new file name to set.
     */
    public void setNewFileName(String newFileName) {
        newFileNameField.setText(newFileName);
    }

    /**
     * Allows the user to choose a different directory for storing notes.
     *
     * @param event The event that triggered the directory selection.
     */
    @FXML
    public void chooseNotesDirectory(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Notes Directory");
        File selectedDirectory = directoryChooser.showDialog(null);
        if (selectedDirectory != null) {
            notesDirectory = Paths.get(selectedDirectory.getAbsolutePath());
            notesDirLabel.setText("Directory: " + selectedDirectory.getPath());
            refreshFileList();
        }
    }

    /**
     * Initializes the controller, ensures the notes directory exists, and sets up the UI components.
     */
    @FXML
    public void initialize() {
        if (!Files.exists(notesDirectory)) {
            try {
                Files.createDirectories(notesDirectory);
            } catch (IOException e) {
            }
        }
        refreshFileList();
        PageFunctions pageFunctions = new PageFunctions();
        pageFunctions.AddSideBar(hBox);
    }

    /**
     * Prompts the user to select a text editor for opening files.
     *
     * @param event The event that triggered the editor selection.
     */
    @FXML
    private void chooseEditor(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Text Editor");
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            preferredEditor = file.getAbsolutePath();
            editorLabel.setText("Editor: " + file.getName());
        }
    }

    /**
     * Refreshes the list of files shown in the file list view.
     */
    public void refreshFileList() {
        fileList.getItems().clear();
        File folder = notesDirectory.toFile();
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    fileList.getItems().add(file.getName());
                }
            }
        }
    }

    /**
     * Opens the selected file using the preferred editor.
     *
     * @param event The event that triggered the file opening.
     */
    @FXML
    public void openFile(ActionEvent event) {
        String fileName = fileList.getSelectionModel().getSelectedItem();
        if (fileName != null && preferredEditor != null) {
            try {
                ProcessBuilder builder = new ProcessBuilder(preferredEditor, notesDirectory.resolve(fileName).toString());
                builder.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Creates a new file in the notes directory with the name specified in the file name text field.
     *
     * @param event The event that triggered the file creation.
     */
    @FXML
    public void createFile(ActionEvent event) {
        String fileName = fileNameField.getText();
        Path path = notesDirectory.resolve(fileName);
        try {
            Files.createFile(path);
            refreshFileList();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Renames the selected file to the new name specified in the new file name text field.
     *
     * @param event The event that triggered the file renaming.
     */
    @FXML
    public void renameFile(ActionEvent event) {
        String oldFileName = fileList.getSelectionModel().getSelectedItem();
        String newFileName = newFileNameField.getText();
        if (oldFileName != null && newFileName != null && !newFileName.isEmpty()) {
            Path oldPath = notesDirectory.resolve(oldFileName);
            Path newPath = notesDirectory.resolve(newFileName);
            try {
                Files.move(oldPath, newPath);
                refreshFileList();
                newFileNameField.clear();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Deletes the selected file from the notes directory.
     *
     * @param event The event that triggered the file deletion.
     */
    @FXML
    public void deleteFile(ActionEvent event) {
        String fileName = fileList.getSelectionModel().getSelectedItem();
        Path path = notesDirectory.resolve(fileName);
        try {
            Files.delete(path);
            refreshFileList();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens the selected file with the default system handler for the file type.
     *
     * @param event The event that triggered the operation.
     */
    @FXML
    public void openWith(ActionEvent event) {
        String fileName = fileList.getSelectionModel().getSelectedItem();
        if (fileName != null) {
            Path filePath = notesDirectory.resolve(fileName);
            try {
                ProcessBuilder builder = new ProcessBuilder("rundll32", "url.dll,FileProtocolHandler", filePath.toAbsolutePath().toString());
                builder.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}