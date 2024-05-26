package org.example.cab302project.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.example.cab302project.PageFunctions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ManageGroupsController {
    private PageFunctions pageFunctions = new PageFunctions();

    @FXML
    TextField studyModeTextField;
    @FXML
    Button studyModeSaveButton;
    @FXML
    TextField subGroupTextField;
    @FXML
    Button subGroupSaveButton;





//    @FXML
//    private void AddStudyMode() {
//        String userInput = studyModeTextField.getText();
//        System.out.println(userInput);
//
//        String sql = "INSERT INTO Groups(GroupID, Groupname, userID) VALUES(?, ?, ?)";
//        try (Connection conn = connect();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//
//            pstmt.setString(2, userInput);
//            pstmt.setInt(3, loginPage.userID);
//            pstmt.executeUpdate();
//        } catch (SQLException e) {
//            System.err.println("Error adding: " + e.getMessage());
//        }
//        DisplayStudyGroups();
//    }


    @FXML
    public void handleBackButtonAction() {
        return;
    }

    @FXML
    public void goToPage(ActionEvent event) {
        pageFunctions.goToPage(event);
    }

    @FXML
    public void initialize() {
        // Optional: Any initializations for your controller
    }
}
