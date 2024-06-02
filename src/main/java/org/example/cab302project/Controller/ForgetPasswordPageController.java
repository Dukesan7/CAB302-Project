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

/**
 * Controller class for the forget password page
 */
public class ForgetPasswordPageController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField newPasswordField;

    @FXML
    // ComboBox for selecting security questions
    private ComboBox<String> securityQuestionComboBox;

    @FXML
    private TextField securityAnswerField;

    /**
     * Initialises the controller class and use combo box to show the security questions
     */
    @FXML
    public void initialize() {
        securityQuestionComboBox.getItems().addAll(
                "What is the name of your first pet?",
                "What school did you first attend?",
                "What suburb did you first live in?",
                "What is your favourite ice cream flavour?"
        );
    }

    /**
     * Reset the user's password through email and security question
     *
     * @param email the user's email
     * @param newPassword new password that user have entered
     * @param securityQuestion user choose which security question they wanted to answer
     * @param securityAnswer enter the correct answer for the security question
     * @return true if password have been reset or show alert message
     */
    public static boolean resetPassword(String email, String newPassword, String securityQuestion, String securityAnswer) {
        // Hash the input values
        String hashedPassword = hashString(newPassword);
        String hashedEmail = hashString(email);
        String hashedSecurityAnswer = hashString(securityAnswer);

        try {
            // Make connections with database
            Connection conn = DbConnection.getInstance().getConnection();

            // Verify the provided details with database records
            PreparedStatement verifyStmt = conn.prepareStatement(
                    "SELECT * FROM UserDetails WHERE email = ? AND securityQuestion = ? AND securityAnswer = ?"
            );
            verifyStmt.setString(1, hashedEmail);
            verifyStmt.setString(2, securityQuestion);
            verifyStmt.setString(3, hashedSecurityAnswer);
            ResultSet rs = verifyStmt.executeQuery();

            if (rs.next()) {
                // Update password if details are correct
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
                verifyStmt.close();
                conn.close();
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Error resetting password: " + e.getMessage());
            return false;
        }
    }

    /**
     * Handle the action to reset the password
     * Check the input field if correct then reset password
     * Load the landing page after successfully resetting the password
     *
     * @param event the action event
     */
    @FXML
    void resetPassword(ActionEvent event) {
        // Get these input values
        String email = emailField.getText();
        String newPassword = newPasswordField.getText();
        String securityQuestion = securityQuestionComboBox.getValue();
        String securityAnswer = securityAnswerField.getText();

        // Check if any input field is empty
        if (email.isEmpty() || newPassword.isEmpty() || securityQuestion == null || securityQuestion.isEmpty() || securityAnswer.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please fill in all the fields!");
            return;
        }

        // Reset password
        boolean success = resetPassword(email, newPassword, securityQuestion, securityAnswer);

        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Password reset successfully!");

            try {
                // Load dashboard page after password has successfully been reset
                Parent dashboardPage = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/example/cab302project/Dashboard.fxml")));
                Scene dashboardScene = new Scene(dashboardPage);

                double preferredWidth = 800;
                double preferredHeight = 450;

                Stage currentStage = (Stage) emailField.getScene().getWindow();

                currentStage.setScene(dashboardScene);

                currentStage.setWidth(preferredWidth);
                currentStage.setHeight(preferredHeight);

                currentStage.show();
            } catch (IOException e) {
                System.err.println("Failed to load the dashboard page: " + e.getMessage());
            }

        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to reset password. Please ensure your details are correct and try again.");
        }
    }

    /**
     * Hash the input string using SHA-256 algorithm
     *
     * @param input hash the input string
     * @return the hashed string
     */
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

    /**
     * Displays alert messages with the specified parameters
     *
     * @param alertType different type of alert messages
     * @param title the title for the alert message
     * @param message the message for the alert
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
