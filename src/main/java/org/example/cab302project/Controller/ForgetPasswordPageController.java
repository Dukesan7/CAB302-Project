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
        String hashedSecurityAnswer = hashString(securityAnswer);

        try {
            Connection conn = DbConnection.getInstance().getConnection();

            PreparedStatement verifyStmt = conn.prepareStatement(
                    "SELECT * FROM UserDetails WHERE email = ? AND securityQuestion = ? AND securityAnswer = ?"
            );
            verifyStmt.setString(1, hashedEmail);
            verifyStmt.setString(2, securityQuestion);
            verifyStmt.setString(3, hashedSecurityAnswer);
            ResultSet rs = verifyStmt.executeQuery();

            if (rs.next()) {
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

    @FXML
    void resetPassword(ActionEvent event) {
        String email = emailField.getText();
        String newPassword = newPasswordField.getText();
        String securityQuestion = securityQuestionComboBox.getValue();
        String securityAnswer = securityAnswerField.getText();

        if (email.isEmpty() || newPassword.isEmpty() || securityQuestion == null || securityQuestion.isEmpty() || securityAnswer.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please fill in all the fields!");
            return;
        }

        boolean success = resetPassword(email, newPassword, securityQuestion, securityAnswer);

        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Password reset successfully!");

            try {
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
