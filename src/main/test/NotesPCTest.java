import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.example.cab302project.Controller.NotesPageController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.event.ActionEvent;

public class NotesPCTest {

    private NotesPageController controller;
    private final Path testDirectory = Paths.get("test/notes");

    @BeforeEach
    public void setUp() throws Exception {
        if (!Files.exists(testDirectory)) {
            Files.createDirectories(testDirectory);
        }
        controller = new NotesPageController();
        controller.setNotesDirectory(testDirectory);
    }

    @Test
    public void testCreateFile() {
        String testFileName = "testNote.txt";
        controller.fileNameField.setText(testFileName);
        controller.createFile(new ActionEvent());
        assertTrue(Files.exists(testDirectory.resolve(testFileName)), "File should exist after creation");
    }

    @Test
    public void testRenameFile() {
        String originalFileName = "originalNote.txt";
        String newFileName = "renamedNote.txt";
        ActionEvent mockEvent = new ActionEvent();

        Path originalFilePath = testDirectory.resolve(originalFileName);
        try {
            Files.createFile(originalFilePath);
        } catch (IOException e) {
            fail("Setup failed due to IO exception: " + e.getMessage());
        }

        controller.setFileName(originalFileName);
        controller.setNewFileName(newFileName);
        controller.renameFile(mockEvent);

        assertFalse(Files.exists(originalFilePath), "Original file should not exist after rename");
        assertTrue(Files.exists(testDirectory.resolve(newFileName)), "New file should exist after rename");
    }

    @Test
    public void testDeleteFile() {
        String fileNameToDelete = "deleteNote.txt";
        Path filePathToDelete = testDirectory.resolve(fileNameToDelete);
        try {
            Files.createFile(filePathToDelete);
        } catch (IOException e) {
            fail("Setup failed due to IO exception: " + e.getMessage());
        }

        ActionEvent mockEvent = new ActionEvent();
        controller.setFileName(fileNameToDelete);
        controller.deleteFile(mockEvent);

        assertFalse(Files.exists(filePathToDelete), "File should not exist after deletion");
    }

    @Test
    public void testFileListRefresh() {
        String fileName = "refreshTestNote.txt";
        try {
            Files.createFile(testDirectory.resolve(fileName));
        } catch (IOException e) {
            fail("Setup failed due to IO exception: " + e.getMessage());
        }
        controller.refreshFileList();
        assertTrue(controller.getFileList().contains(fileName), "File list should contain the new file");
    }
}
