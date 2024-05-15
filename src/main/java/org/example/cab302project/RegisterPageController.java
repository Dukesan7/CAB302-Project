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
import java.sql.*;
import java.util.Objects;

public class RegisterPageController {

    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML

    private static final String DB_FILE_PATH = "src/main/resources/org/example/cab302project/ToDo.db";

    private Connection connect() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:" + DB_FILE_PATH);
    }

    @FXML
    private void registerUser() {
        String fullName = nameField.getText().trim();
        String email = emailField.getText().trim();
        String pass = passwordField.getText();
        String confirmPass = confirmPasswordField.getText();

        if (!pass.equals(confirmPass)) {
            showAlert(AlertType.ERROR, "Registration Error", "Password does not match !");
            return;
        }

        if (fullName.isEmpty() || email.isEmpty() || pass.isEmpty()) {
            showAlert(AlertType.ERROR, "Registration Error", "Fill in the blanks !");
            return;
        }

        String[] names = fullName.split(" ", 2);
        String fName = names.length > 0 ? names[0] : "";
        String lName = names.length > 1 ? names[1] : "";

        if (registerUserInDatabase(fName, lName, email, pass)) {
            showAlert(AlertType.INFORMATION, "Registration Successfully", "You have successfully registered.");

            // Switch to user login page
            try{
                Parent loginPage = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Login.fxml")));
                Scene loginScene = new Scene(loginPage);

                // Get the preferred width and height from the registration page
                double preferredWidth = 1280;
                double preferredHeight = 690;

                // Switch to the login page
                Stage currentStage = (Stage) emailField.getScene().getWindow();

                // Set the new scene to the stage
                currentStage.setScene(loginScene);

                // Set the preferred width and height for the stage
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

    private boolean registerUserInDatabase(String fName, String lName, String email, String pass) {
        String sql = "INSERT INTO UserDetails(fName, lName, email, pass) VALUES(?,?,?,?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, fName);
            pstmt.setString(2, lName);
            pstmt.setString(3, email);
            pstmt.setString(4, pass);
            pstmt.executeUpdate();
            return true;
        }

        catch (SQLException e)
        {
            System.err.println("Error registering user: " + e.getMessage());
            return false;
        }
    }

    private void showAlert(AlertType alertType, String title, String message){
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
