package org.example.cab302project.Controller;

import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import org.example.cab302project.DbConnection;
import org.example.cab302project.LoginPageController;
import org.example.cab302project.PageFunctions;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class BlockApplicationController {

    private PageFunctions pageFunctions = new PageFunctions();
    private Connection connection;
    private LoginPageController loginPage;
    String returnString;
    private String appPath = null;

    @FXML
    ChoiceBox<String> groupSelection;
    @FXML
    TextArea blockReason;
    @FXML
    Button appPathButton;
    @FXML
    Label filePathLabel;


    @FXML
    private void FindApplicationPath() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Application Path");
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            appPath = file.getAbsolutePath();
            Pattern pattern = Pattern.compile( "[^\\\\]*.exe");
            Matcher matcher = pattern.matcher(appPath);
            matcher.find();
            returnString = matcher.group();
            filePathLabel.setText("File Name: " + returnString);

        }
    }

    private void populateGroupList() {
        try {
            groupSelection.getItems().clear();
            String sql = "SELECT * FROM Groups WHERE userID = ?";

            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, loginPage.userID );
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    groupSelection.getItems().add(rs.getString("Groupname"));
                }

            } catch (SQLException e) {
                System.err.println("Error adding blocked applications: " + e.getMessage());
            }
        } catch (NullPointerException e) {System.err.println(e.getMessage());}
    }



    public void AddApplications() {
        String sql = "INSERT INTO BlackLists(blackListID, groupID, fileName, reason) VALUES(?, ?, ?, ?)";

        if (returnString == null || blockReason.getText() == null) { return; }
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(2, loginPage.userID);
            pstmt.setString(3, appPath);
            pstmt.setString(4, blockReason.getText());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error adding: " + e.getMessage());
        }
    }


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
        try {
            connection = DbConnection.getInstance().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        populateGroupList();

    }

}
