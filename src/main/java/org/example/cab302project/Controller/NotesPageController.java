package org.example.cab302project.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.example.cab302project.PageFunctions;
import org.json.JSONObject;

public class NotesPageController {

    private final Path notesDirectory = Paths.get("src/main/resources/org/example/cab302project/notes");
    private final Path configPath = notesDirectory.resolve("../config.json");
    private String preferredEditor;
    @FXML
    HBox hBox;
    @FXML
    private ListView<String> fileList;
    @FXML
    private TextField fileNameField;
    @FXML
    private Label editorLabel;
    @FXML
    private TextField newFileNameField;

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

    @FXML
    private void chooseEditor(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Text Editor");
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            preferredEditor = file.getAbsolutePath();
            saveEditorPreference();
            editorLabel.setText("Editor: " + file.getName());
        }
    }

    private void refreshFileList() {
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

    private void loadEditorPreference() {
        if (Files.exists(configPath)) {
            try {
                String content = new String(Files.readAllBytes(configPath));
                JSONObject json = new JSONObject(content);
                preferredEditor = json.optString("editor", null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveEditorPreference() {
        JSONObject json = new JSONObject();
        json.put("editor", preferredEditor);
        try {
            Files.write(configPath, json.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
}
