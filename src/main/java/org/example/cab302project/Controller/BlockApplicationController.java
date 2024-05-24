package org.example.cab302project.Controller;

import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import org.example.cab302project.DbConnection;
import org.example.cab302project.PageFunctions;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class BlockApplicationController {

    private PageFunctions pageFunctions = new PageFunctions();
    private Connection connection;

    ObservableList<String> groupList;

    @FXML
    ChoiceBox<String> groupSelection;
    @FXML
    TextArea blockReason;
    @FXML
    Button appPathButton;


    @FXML
    private void FindApplicationPath() {
        String appPath = null;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Application Path");
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            appPath = file.getAbsolutePath();
            Pattern pattern = Pattern.compile( "[^\\\\]*.exe");
            Matcher matcher = pattern.matcher(appPath);
            boolean matchFound = matcher.find();
            String returnString = matcher.group();
            System.out.println("return, " + returnString);

        }
        if (appPath != null) { System.out.println(appPath); }
    }

    private void populateGroupList() {
        //yoink from db
        //update.groupList;
    }

    private void populateSecurityQuestions() {
        try {groupSelection.getItems().addAll(groupList); }
        catch (NullPointerException e) {System.err.println(e.getMessage());}
    }


    public void AddApplications() {

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

    }

}
