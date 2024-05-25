package org.example.cab302project;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Objects;

public class LoginPageController {

    // DOM HAS MADE CHANGES TO SET THE USER ID AFTER LOGIN VERIFIED
    // I will comment everywhere I make a change
    //Dom edit \/
    public static int userID;
    public static String nameOfUser;

    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    
    @FXML
    private void loginUser() {
        String email = emailField.getText().trim();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert(AlertType.ERROR, "Login Error", "Please fill in the blanks !");
            return;
        }

        if (verifyUserCredentials(email, password)) {
            showAlert(AlertType.INFORMATION, "Login Successful", "You have successfully logged in.");

            // Switch to the dashboard page
            try {
                Parent dashboardPage = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Dashboard.fxml")));
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
            showAlert(AlertType.ERROR, "Login Failed", "Invalid email or password.");
        }
    }

    @FXML
    private void goToRegisterPage() {
        try {
            // Load the register page from fxml file
            Parent registerPage = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Register.fxml")));

            // Create a new scene with the registration page
            Scene registerScene = new Scene(registerPage);

            // Get the preferred width and height from the registration page
            double preferredWidth = 800;
            double preferredHeight = 450;

            // Get the current page
            Stage currentPage = (Stage) emailField.getScene().getWindow();

            // Set the new scene to the page
            currentPage.setScene(registerScene);

            // Set window size for the page
            currentPage.setWidth(preferredWidth);
            currentPage.setHeight(preferredHeight);

            // Show window with new scene
            currentPage.show();
        }
        catch (IOException e) {
            System.err.println("Failed to load registration page: " + e.getMessage());
        }
    }

    @FXML
    private void goToForgetPasswordPage() {
        try {
            // Load the register page from fxml file
            Parent forgetPasswordPage = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("ForgetPassword.fxml")));

            // Create a new scene with the registration page
            Scene forgetPasswordScene = new Scene(forgetPasswordPage);

            // Get the preferred width and height from the registration page
            double preferredWidth = 800;
            double preferredHeight = 450;

            // Get the current page
            Stage currentPage = (Stage) emailField.getScene().getWindow();

            // Set the new scene to the page
            currentPage.setScene(forgetPasswordScene);

            // Set window size for the page
            currentPage.setWidth(preferredWidth);
            currentPage.setHeight(preferredHeight);

            // Show window with new scene
            currentPage.show();
        }
        catch (IOException e) {
            System.err.println("Failed to load forget password page: " + e.getMessage());
        }
    }

    private boolean verifyUserCredentials(String email, String password) {
        String hashedEmail = hashString(email);
        String hashedPassword = hashString(password);

        String sql = "SELECT * FROM UserDetails WHERE email = ? AND pass = ?";

        try (Connection conn = DbConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, hashedEmail);
            pstmt.setString(2, hashedPassword);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                //Dom edit \/
                userID = rs.getInt("UserID");
                nameOfUser = rs.getString("fName");
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Error verifying user credentials: " + e.getMessage());
            return false;
        }
    }

    private String hashString(String input) {
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

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null); // No header
        alert.setContentText(message);
        alert.showAndWait();
    }

    // FXML initialization method
    @FXML
    public void initialize() {
        // Optional: Any initializations for your controller
    }
}

