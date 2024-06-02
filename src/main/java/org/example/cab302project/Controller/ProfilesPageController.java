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

import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.util.ArrayList;

public class ProfilesPageController {
    private LoginPageController loginPage;
    private PageFunctions pageFunctions = new PageFunctions();
    private Connection connection;
    private String selectedQuestion;
    ObservableList<String> potentialQuestions = FXCollections.observableArrayList(
            "What is the name of your first pet?",
            "What school did you first attend?",
            "What suburb did you first live in?",
            "What is your favourite ice cream flavour?"
    );

    @FXML
    ComboBox<String> changeSecurityQuestion;
    @FXML
    TextField securityQuestionAnswer;
    @FXML
    Label profileDisplayName;
    @FXML
    HBox hBox;

    @FXML
    private void populateSecurityQuestions() {
        String questionPreview;
        try {
            String sql = "SELECT (securityQuestion) FROM UserDetails WHERE userID = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, loginPage.userID);
                ResultSet rs = pstmt.executeQuery();
                questionPreview = rs.getString("securityQuestion");
                changeSecurityQuestion.setValue(questionPreview);
            } catch (NullPointerException e) {
                System.err.println(e.getMessage());
            }
            changeSecurityQuestion.getItems().addAll(potentialQuestions);
        } catch (SQLException e) {
            System.err.println("Error adding: " + e.getMessage());
        }
    }
    @FXML
    private String getSelectedQuestion() { selectedQuestion = changeSecurityQuestion.getValue(); return selectedQuestion; }
    @FXML
    private void populateProfileDisplayName() {
        try {profileDisplayName.setText("Current Profile: " + LoginPageController.nameOfUser); }
        catch (NullPointerException e) {System.err.println(e.getMessage());}
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

        populateSecurityQuestions();
        populateProfileDisplayName();
        pageFunctions.AddSideBar(hBox);
    }

    @FXML
    public void saveSecurityQuestion() {
        String question = getSelectedQuestion();
        String answer = securityQuestionAnswer.getText();

        if (question == null || question.isEmpty() || answer == null || answer.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Security question or answer is empty!");
            return;
        }

        String hashAnswer = hashString(answer);

        String sql = "UPDATE UserDetails SET securityQuestion = ?, securityAnswer = ? WHERE UserID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {

            int userID = loginPage.userID;

            pstmt.setString(1, question);
            pstmt.setString(2, hashAnswer);
            pstmt.setInt(3, userID);
            pstmt.executeUpdate();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Security question and answer saved successfully!");

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR,"Error", "Error saving security question and answer: " + e.getMessage());
        }
    }

    private static String hashString(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}