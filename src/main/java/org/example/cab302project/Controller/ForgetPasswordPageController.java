package org.example.cab302project.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.cab302project.DbConnection;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

public class ForgetPasswordPageController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField newPasswordField;

    public static boolean resetPassword(String email, String newPassword) {
        String hashedPassword = hashString(newPassword);
        String hashedEmail = hashString(email);

        try {
            Connection conn = DbConnection.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement("UPDATE UserDetails SET pass = ? WHERE email = ?");
            pstmt.setString(1, hashedPassword);
            pstmt.setString(2, hashedEmail);
            int rowsUpdated = pstmt.executeUpdate();
            pstmt.close();
            conn.close();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            System.err.println("Error resetting password: " + e.getMessage());
            return false;
        }
    }

    @FXML
    void resetPassword(ActionEvent event) {
        String email = emailField.getText();
        String newPassword = newPasswordField.getText();

        // Check if email and newPassword are not empty
        if (email.isEmpty() || newPassword.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Please enter both email and new password !");
            alert.showAndWait();
            return;
        }

        // Attempt to reset the password
        boolean success = resetPassword(email, newPassword);

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
                double preferredWidth = 1280.0;
                double preferredHeight = 690.0;

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
            alert.setContentText("Failed to reset password. Please try again.");
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
