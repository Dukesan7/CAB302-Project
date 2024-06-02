package org.example.cab302project;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Controller class for the registration page
 */
public class RegisterPageController {
    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;

    /**
     * Handle the action for the registration button
     * Validate the input field and register for an account
     * Load the login page after successfully creating an account
     */
    @FXML
    private void registerUser() {
        // Get and trim input fields
        String fullName = nameField.getText().trim();
        String email = emailField.getText().trim();
        String pass = passwordField.getText();
        String confirmPass = confirmPasswordField.getText();

        // Check if both password matches
        if (!pass.equals(confirmPass)) {
            showAlert(AlertType.ERROR, "Registration Error", "Password does not match!");
            return;
        }

        // Check if any input field is empty
        if (fullName.isEmpty() || email.isEmpty() || pass.isEmpty()) {
            showAlert(AlertType.ERROR, "Registration Error", "Fill in the blanks!");
            return;
        }

        // Check if email format is valid
        if (!isValidEmail(email)) {
            showAlert(AlertType.ERROR, "Registration Error", "Invalid email format!");
            return;
        }

        // Split full name into first name and last name
        String[] names = fullName.split(" ", 2);
        String fName = names.length > 0 ? names[0] : "";
        String lName = names.length > 1 ? names[1] : "";

        // Register user in database
        if (registerUserInDatabase(fName, lName, email, pass)) {
            showAlert(AlertType.INFORMATION, "Registration Successfully", "You have successfully registered.");

            try{
                // Load the login page
                Parent loginPage = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Login.fxml")));
                Scene loginScene = new Scene(loginPage);

                double preferredWidth = 800;
                double preferredHeight = 450;

                Stage currentStage = (Stage) emailField.getScene().getWindow();

                currentStage.setScene(loginScene);

                currentStage.setWidth(preferredWidth);
                currentStage.setHeight(preferredHeight);

                currentStage.show();
            }
            catch (IOException e) {
                System.err.println("Failed to load the login page:" + e.getMessage());
            }
        } else {
            showAlert(AlertType.ERROR, "Registration Failed", "User might already exist.");
        }
    }

    /**
     * Check for correct email format
     *
     * @param email validate the email address
     * @return true if email format is correct or else return false with an alert
     */
    public boolean isValidEmail(String email) {
        // Regular expression for validating email format
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * Register the user information into the database
     * @return true if the registration is successful, else return error message
     */
    public boolean registerUserInDatabase(String fName, String lName, String email, String pass) {
        // Hash email and password
        String hashedEmail = hashString(email);
        String hashedPass = hashString(pass);

        String sql = "INSERT INTO UserDetails(fName, lName, email, pass) VALUES(?,?,?,?)";

        try (Connection conn = DbConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, fName);
            pstmt.setString(2, lName);
            pstmt.setString(3, hashedEmail);
            pstmt.setString(4, hashedPass);
            pstmt.executeUpdate();
            return true;
        }

        catch (SQLException e)
        {
            System.err.println("Error registering user: " + e.getMessage());
            return false;
        }
    }

    /**
     * Hash the input string using SHA-256 algorithm
     *
     * @param input hash the input string
     * @return the hashed string
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
    public void showAlert(AlertType alertType, String title, String message){
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
