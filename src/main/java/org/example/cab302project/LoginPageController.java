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

/**
 * Controller class for the login page
 */
public class LoginPageController {

    /**
     * User ID for logged in user
     */
    public static int userID;
    /**
     * Name of logged in user
     */
    public static String nameOfUser;

    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;

    /**
     * Handle login button action
     * Verify user credentials and load the dashboard page if successfully logged in
     */
    @FXML
    private void loginUser() {
        String email = emailField.getText().trim();
        String password = passwordField.getText();

        // Check if email and password is empty
        if (email.isEmpty() || password.isEmpty()) {
            showAlert(AlertType.ERROR, "Login Error", "Please fill in the blanks!");
            return;
        }

        // Verify user credentials by checking email and password
        if (verifyUserCredentials(email, password)) {
            showAlert(AlertType.INFORMATION, "Login Successful", "You have successfully logged in.");

            try {
                // Load the dashboard page after successfully logging in
                Parent dashboardPage = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Dashboard.fxml")));
                Scene dashboardScene = new Scene(dashboardPage);

                double preferredWidth = 800;
                double preferredHeight = 450;

                Stage currentStage = (Stage) emailField.getScene().getWindow();

                currentStage.setScene(dashboardScene);

                currentStage.setWidth(preferredWidth);
                currentStage.setHeight(preferredHeight);

                currentStage.show();
            } catch (IOException e) {
                // Print error if dashboard can't be loaded
                System.err.println("Failed to load the dashboard page: " + e.getMessage());
            }
        } else {
            // Show login failed alert
            showAlert(AlertType.ERROR, "Login Failed", "Invalid email or password.");
        }
    }

    /**
     * Action that goes to the register page
     */
    @FXML
    private void goToRegisterPage() {
        try {
            // Load register page if user need to register for an account
            Parent registerPage = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Register.fxml")));

            Scene registerScene = new Scene(registerPage);

            double preferredWidth = 800;
            double preferredHeight = 450;

            Stage currentPage = (Stage) emailField.getScene().getWindow();

            currentPage.setScene(registerScene);

            currentPage.setWidth(preferredWidth);
            currentPage.setHeight(preferredHeight);

            currentPage.show();
        }
        catch (IOException e) {
            // Print error message if failed to load register page
            System.err.println("Failed to load registration page: " + e.getMessage());
        }
    }

    /**
     * Action that goes to the forget password page
     */
    @FXML
    private void goToForgetPasswordPage() {
        try {
            // Load forget password page if user need to reset their password for their account
            Parent forgetPasswordPage = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("ForgetPassword.fxml")));

            Scene forgetPasswordScene = new Scene(forgetPasswordPage);

            double preferredWidth = 800;
            double preferredHeight = 450;

            Stage currentPage = (Stage) emailField.getScene().getWindow();

            currentPage.setScene(forgetPasswordScene);

            currentPage.setWidth(preferredWidth);
            currentPage.setHeight(preferredHeight);

            currentPage.show();
        }
        catch (IOException e) {
            // Print error message if it fails to load the forget password page
            System.err.println("Failed to load forget password page: " + e.getMessage());
        }
    }

    /**
     * Verifies user credentials against the database
     *
     * @param email email entered by the user
     * @param password password entered by the user
     * @return returns true if the credentials are valid, else return false
     */
    public boolean verifyUserCredentials(String email, String password) {
        // Hash email and password
        String hashedEmail = hashString(email);
        String hashedPassword = hashString(password);

        String sql = "SELECT * FROM UserDetails WHERE email = ? AND pass = ?";

        try (Connection conn = DbConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, hashedEmail);
            pstmt.setString(2, hashedPassword);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // Set userID and nameOfUser for the logged in user
                userID = rs.getInt("UserID");
                nameOfUser = rs.getString("fName");
                // Return true if credentials are correct
                return true;
            }
            // Return false if credientials are wrong
            return false;
        } catch (SQLException e) {
            // Print error message if it fails the verification
            System.err.println("Error verifying user credentials: " + e.getMessage());
            return false;
        }
    }

    /**
     * Using SHA-256 to hash the given input string
     *
     * @param input hash the input string
     * @return the hashed string value
     */
    public String hashString(String input) {
        try {
            // Initialize MessageDigest with SHA-256 algorithm
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                // Convert each byte to hexadecimal and append to StringBuilder
                sb.append(String.format("%02x", b));
            }
            // Return the hashed string
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            // Throw RuntimeException if SHA-256 algorithm is not found
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
    public void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Initialises the controller class
     */
    @FXML
    public void initialize() {
    }
}

