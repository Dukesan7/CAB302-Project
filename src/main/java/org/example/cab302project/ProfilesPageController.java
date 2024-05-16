package org.example.cab302project;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.w3c.dom.events.MouseEvent;

import java.io.IOException;
import java.sql.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Objects;

public class ProfilesPageController {
    private LoginPageController loginPage;

    public ArrayList<String> getProfileName() {
        return profileName;
    }

    public void setProfileName(ArrayList<String> profileName) { this.profileName = profileName; }
    private ArrayList<String> profileName = new ArrayList<>();

    private String password;
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    private ArrayList<String> smName = new ArrayList<>();
    private int smIndex;
    private String selectedQuestion;
    ObservableList<String> potentialQuestions = FXCollections.observableArrayList(
            "What is the name of your first pet?",
            "What school did you first attend?",
            "What suburb did you first live in?",
            "What is your favourite ice cream flavour?"
    );

//    public ProfilesPageController(String profileName, String password, String smName, String selectedQuestion ) {
//        this.profileName.add(profileName);
//        this.password = password;
//        this.smName.add(smName);
//        this.selectedQuestion = selectedQuestion;
//    }


    // ~*~*~*~*~*~*~*~ DATABASE STUFF ~*~*~*~*~*~*~*~
    private static final String DB_FILE_PATH = "src/main/resources/org/example/cab302project/ToDo.db";
    private Connection connect() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:" + DB_FILE_PATH);
    }




    public void AddNewProfile(String newProfileName) {
        this.profileName.add(newProfileName);
    }

    public String ChangePassword(String oldPw, String newPw, String confirmNewPw) {
        if (oldPw.equals(newPw)) { return "Please enter a different password"; }
        if (!newPw.equals(confirmNewPw)) {return "Ensure the confirmed password is correct"; }

        //change shit in db();
        return "Password has successfully been changed!";
    }


    public String AppendStudyMode(String nameofMode) {
        int count = smName.size();

        //checks for duplicates
        for (String s : smName) {
            if (s.equals(nameofMode)) {
                return "Please try a different name for the study mode."; }
        }
        smName.add(nameofMode);
        // checks to see if string is successfully added
        if (smName.size() <= count) {
            return "The Study Mode was unable to be added, please try again."; }

        return MessageFormat.format("Study Mode {0} Successfully added.", smName.get(count));
    }


    @FXML
    TextField addStudyModeInput;
    @FXML
    ComboBox<String> changeSecurityQuestion;
    @FXML
    Label profileDisplayName;
    @FXML
    TextArea applicationList;

    @FXML
    private void returnTextAndAppend() {
        String addedStudyModeName = addStudyModeInput.getText();
        String returnString = AppendStudyMode(addedStudyModeName);
        System.out.println(returnString);
    }
    @FXML
    private void populateSecurityQuestions() {
        try {changeSecurityQuestion.getItems().addAll(potentialQuestions); }
        catch (NullPointerException e) {System.err.println(e.getMessage());}
    }
    @FXML
    private void getSelectedQuestion() { selectedQuestion = changeSecurityQuestion.getValue(); }
    @FXML
    private void populateProfileDisplayName() {
        try {profileDisplayName.setText("Current Profile: " + loginPage.nameOfUser); }
        catch (NullPointerException e) {System.err.println(e.getMessage());}
    }

    @FXML
    public void changeButtonLabel(ActionEvent event) {
        ToggleButton tButton = (ToggleButton) event.getSource();
        if (tButton.getText().equals("OFF")) {
            tButton.setText("ON");
        }
        else { tButton.setText("OFF");}
    }


    public void populateApplicationList() {
        String sql = "SELECT * FROM BlackLists WHERE userID = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, loginPage.userID );
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                System.out.println(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error verifying user credentials: " + e.getMessage());
        }
    }




    @FXML
    public void handleBackButtonAction() {
        return;
    }

    @FXML
    public void goToPage(ActionEvent event) {
        Button button = (Button) event.getSource();
        String pageName = button.getId();

        try {
            Parent root = FXMLLoader.load(getClass().getResource(pageName + ".fxml"));
            Stage stage = (Stage) button.getScene().getWindow();
            stage.setScene(new Scene(root, 600, 400));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        // Optional: Any initializations for your controller
        populateSecurityQuestions();
        populateProfileDisplayName();
        populateApplicationList();

    }
}
