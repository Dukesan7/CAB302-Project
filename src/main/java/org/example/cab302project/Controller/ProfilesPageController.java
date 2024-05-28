package org.example.cab302project.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import org.example.cab302project.LoginPageController;
import org.example.cab302project.PageFunctions;
import java.sql.*;
import java.text.MessageFormat;
import java.util.ArrayList;

public class ProfilesPageController {
    private LoginPageController loginPage;
    private PageFunctions pageFunctions = new PageFunctions();
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
    TextField securityQuestionAnswer;
    @FXML
    Label profileDisplayName;


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
        try {profileDisplayName.setText("Current Profile: " + LoginPageController.nameOfUser); }
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

    public void exampleApps() {
        String sql = "INSERT INTO BlackLists(blackListID, userID, fileName, reason) VALUES(?, ?, ?, ?)";
        var fileNames = new String[] {"Steam.exe", "Chrome.exe", "Amazon.com", "EpicGames.exe", "LeagueofLegends.exe", "SchoolWork.exe"};
        var reasons = new String[] {"Games", "Internet", "Shopping", "Gaming", "Too Distracting", "Toobad"};
        var userID = new int[] {2, 1, 2, 2, 2, 1};

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for(int i = 0; i < 6; i++){
                pstmt.setInt(1, i);
                pstmt.setInt(2, userID[i]);
                pstmt.setString(3, fileNames[i]);
                pstmt.setString(4, reasons[i]);
                pstmt.executeUpdate();
            }

        } catch (SQLException e) {
            System.err.println("Error adding: " + e.getMessage());
        }
    }

    @FXML
    public void handleBackButtonAction() {
        return;
    }

    @FXML
    public void goToPage(ActionEvent event) {
        pageFunctions.goToPage(event);
    }

    @FXML
    public void initialize() {
        // Optional: Any initializations for your controller
        populateSecurityQuestions();
        populateProfileDisplayName();
        exampleApps();
    }

    @FXML
    public void saveSecurityQuestion() {
        String question = selectedQuestion;
        String answer = securityQuestionAnswer.getText();

        if (question == null || question.isEmpty() || answer == null || answer.isEmpty()) {
            System.out.println("Security question or answer is empty.");
            return;
        }

        String sql = "UPDATE UserDetails SET securityQuestion = ?, securityAnswer = ? WHERE UserID = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            int userID = LoginPageController.userID;

            pstmt.setString(1, question);
            pstmt.setString(2, answer);
            pstmt.setInt(3, userID);
            pstmt.executeUpdate();
            System.out.println("Security question and answer saved successfully.");

        } catch (SQLException e) {
            System.err.println("Error saving security question and answer: " + e.getMessage());
        }
    }
}