package org.example.cab302project.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.cab302project.DbConnection;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class ForgetPasswordPageController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private ComboBox<String> securityQuestionComboBox;

    @FXML
    private TextField securityAnswerField;

    @FXML
    public void initialize() {
        // Populate the security question combo box
        securityQuestionComboBox.getItems().addAll(
                "What is the name of your first pet?",
                "What school did you first attend?",
                "What suburb did you first live in?",
                "What is your favourite ice cream flavour?"
        );
    }

    public static boolean resetPassword(String email, String newPassword, String securityQuestion, String securityAnswer) {
        String hashedPassword = hashString(newPassword);
        String hashedEmail = hashString(email);

        try {
            Connection conn = DbConnection.getInstance().getConnection();
            // Verify security question and answer
            PreparedStatement verifyStmt = conn.prepareStatement(
                    "SELECT * FROM UserDetails WHERE email = ? AND securityQuestion = ? AND securityAnswer = ?"
            );
            verifyStmt.setString(1, hashedEmail);
            verifyStmt.setString(2, securityQuestion);
            verifyStmt.setString(3, securityAnswer);
            ResultSet rs = verifyStmt.executeQuery();

            if (rs.next()) {
                // If the security question and answer match, update the password
                PreparedStatement updateStmt = conn.prepareStatement(
                        "UPDATE UserDetails SET pass = ? WHERE email = ?"
                );
                updateStmt.setString(1, hashedPassword);
                updateStmt.setString(2, hashedEmail);
                int rowsUpdated = updateStmt.executeUpdate();
                updateStmt.close();
                verifyStmt.close();
                conn.close();
                return rowsUpdated > 0;
            } else {
                // Security question and answer do not match
                verifyStmt.close();
                conn.close();
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Error resetting password: " + e.getMessage());
            return false;
        }
    }

    @FXML
    void resetPassword(ActionEvent event) {
        String email = emailField.getText();
        String newPassword = newPasswordField.getText();
        String securityQuestion = securityQuestionComboBox.getValue();
        String securityAnswer = securityAnswerField.getText();

        // Check if email, newPassword, securityQuestion, and securityAnswer are not empty
        if (email.isEmpty() || newPassword.isEmpty() || securityQuestion == null || securityQuestion.isEmpty() || securityAnswer.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in all fields!");
            alert.showAndWait();
            return;
        }

        // Attempt to reset the password
        boolean success = resetPassword(email, newPassword, securityQuestion, securityAnswer);

        // Show result
        if (success) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Password reset successfully.");
            alert.showAndWait();

            // Switch to the dashboard page
            try {
                Parent dashboardPage = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/example/cab302project/Dashboard.fxml")));
                Scene dashboardScene = new Scene(dashboardPage);

                // Get the preferred width and height from the registration page
                double preferredWidth = 800;
                double preferredHeight = 450;

                // Switch to the dashboard page
                Stage currentStage = (Stage) emailField.getScene().getWindow();

                // Set the new scene to the stage
                currentStage.setScene(dashboardScene);

                // Set preferred width and height if desired
                currentStage.setWidth(preferredWidth);
                currentStage.setHeight(preferredHeight);

                currentStage.show();
            } catch (IOException e) {
                System.err.println("Failed to load the dashboard page: " + e.getMessage());
            }

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to reset password. Please ensure your details are correct and try again.");
            alert.showAndWait();
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
}
