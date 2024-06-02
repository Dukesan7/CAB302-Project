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
import org.example.cab302project.ManageApplications.BlockApplication;
import org.example.cab302project.PageFunctions;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class BlockApplicationController {

    BlockApplication blockApplication = new BlockApplication();
    private PageFunctions pageFunctions = new PageFunctions();
    private Connection connection;
    private LoginPageController loginPage;
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
        blockApplication.ReturnApplicationPath(fileChooser);
        filePathLabel.setText("File Name: " + blockApplication.getReturnString());
    }

    private void populateGroupList() {
        groupSelection.getItems().clear();
        groupSelection.getItems().addAll(blockApplication.returnDataToGroupList());
    }

    public void AddApplications() {
        if (blockApplication.getReturnString() == null || blockReason.getText() == null) { return; }
        blockApplication.addApplicationToDb(blockReason.getText());
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
