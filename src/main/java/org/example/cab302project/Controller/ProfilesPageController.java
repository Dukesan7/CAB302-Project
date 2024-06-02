package org.example.cab302project.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import org.example.cab302project.DbConnection;
import org.example.cab302project.LoginPageController;
import org.example.cab302project.PageFunctions;
import org.example.cab302project.Profiles.Profiles;

import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.util.ArrayList;

public class ProfilesPageController {
    private LoginPageController loginPage = new LoginPageController();
    private PageFunctions pageFunctions = new PageFunctions();
    private DashboardPageController dashboard = new DashboardPageController();
    private Profiles profiles = new Profiles();


    @FXML
    ComboBox<String> changeSecurityQuestion;
    @FXML
    TextField securityQuestionAnswer;
    @FXML
    Label profileDisplayName;
    @FXML
    HBox hBox;

    /**
     * A Method to display the security question on the profiles page
     * This only applies if a security question is set, otherwise the preview of the ComboBox will be displayed
     * as the default value
     * This method also updates the ComboBox to input the securityQuestions
     */
    @FXML
    private void populateSecurityQuestions() {
        String defaultSecurityQuestion = profiles.returnDefaultSecurityQuestion();

        changeSecurityQuestion.getItems().addAll(profiles.getPotentialQuestions());
        changeSecurityQuestion.setValue(defaultSecurityQuestion);

    }
    @FXML
    private String getSelectedQuestion() {
        return changeSecurityQuestion.getValue();
    }

    /**
     * Changes Profile Display name to equal the name of the user
     * LoginPageController.nameOfUser is set on the login page
     */
    @FXML
    private void populateProfileDisplayName() {
        try {profileDisplayName.setText("Current Profile: " + LoginPageController.nameOfUser); }
        catch (NullPointerException e) {System.err.println(e.getMessage());}
    }

    /**
     * Saves the security question selected along with its response to the database and hashes the answer
     * Throws a popup if required field are empty
     * Uses showAltert() method from the DashboardPageController
     */
    @FXML
    public void saveSecurityQuestion() {
        String question = getSelectedQuestion();
        String answer = securityQuestionAnswer.getText();
        boolean saveQuestionsResult =  profiles.saveSecurityQuestionToDB(question, answer);
        if (question == null || question.isEmpty() || answer == null || answer.isEmpty()) {
            dashboard.showAlert(Alert.AlertType.WARNING, "Warning", "Security question or answer is empty!");
            return;
        }
        if (saveQuestionsResult) { dashboard.showAlert(Alert.AlertType.INFORMATION, "Success", "Security question and answer saved successfully!"); }
        if (!saveQuestionsResult) { dashboard.showAlert(Alert.AlertType.ERROR,"Error", "Error saving security question and answer"); }
    }

    @FXML
    public void handleBackButtonAction() {
        return;
    }

    /**
     * Loads from pageFunctions to switch between scenes
     * @param event uses the fx:id of the button that was pressed
     */
    @FXML
    public void goToPage(ActionEvent event) {
        pageFunctions.goToPage(event);
    }

    /**
     * Runs pre-selected methods to display when the page is first loaded
     * pageFunctions.AddSideBar() loads and displays the sidebar given the node
     */
    @FXML
    public void initialize() {
        populateSecurityQuestions();
        populateProfileDisplayName();
        pageFunctions.AddSideBar(hBox);
    }

}