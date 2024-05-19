package org.example.cab302project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.text.MessageFormat;
import java.util.ArrayList;

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
    TableView applicationTable;
    @FXML
    TableColumn<DisplayObject, String> fileNameColumn;
    @FXML
    TableColumn<DisplayObject, String> reasonColumn;


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

    public void exampleApps() {
        String sql = "INSERT INTO BlackLists(blackListID, userID, fileName, reason) VALUES(?, ?, ?, ?)";
        var fileNames = new String[] {"Steam.exe", "Chrome.exe"};
        var reasons = new String[] {"Games", "Internet"};
        var userID = new int[] {2, 1};

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for(int i = 0; i < 2; i++){
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


    static class DisplayObject {
        private String name = null; private String reason = null;
        public String getName() {
            return name;
        }

        public String getReason() {
            return reason;
        }

    public DisplayObject(String name, String reason)  {
        this.name = name; this.reason = reason;
    } }

    public void populateApplicationList() {
        try {
            System.out.println("popApps Selected");
            String sql = "SELECT fileName, reason FROM BlackLists WHERE userID = ?";

            try (Connection conn = connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, loginPage.userID );
                ResultSet rs = pstmt.executeQuery();


                fileNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
                reasonColumn.setCellValueFactory(new PropertyValueFactory<>("reason"));


                while (rs.next()) {
                    System.out.printf("%-5s%-25s",
                            rs.getString("fileName"),
                            rs.getString("reason")
                    );

                    DisplayObject displayObject  = new DisplayObject(rs.getString("fileName"), rs.getString("reason"));
                    System.out.println(displayObject.name + displayObject.reason);
                    applicationTable.getItems().add(new DisplayObject(rs.getString("fileName"), rs.getString("reason")));
                }

            } catch (SQLException e) {
                System.err.println("Error adding blocked applications: " + e.getMessage());
            }
        } catch (NullPointerException e) {System.err.println(e.getMessage());}
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
            stage.setScene(new Scene(root, 1280, 690));
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
        exampleApps();
        populateApplicationList();

    }
}
