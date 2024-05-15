package org.example.cab302project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

public class ForgetPasswordPageController {
    private static String dbFilePath = "src/main/resources/org/example/cab302project/ToDo.db";

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField newPasswordField;

    public static boolean resetPassword(String email, String newPassword) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbFilePath);
            PreparedStatement pstmt = conn.prepareStatement("UPDATE UserDetails SET pass = ? WHERE email = ?");
            pstmt.setString(1, newPassword);
            pstmt.setString(2, email);
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
                Parent dashboardPage = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Dashboard.fxml")));
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
}
