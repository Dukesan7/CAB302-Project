package org.example.cab302project.Controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.example.cab302project.DbConnection;
import org.example.cab302project.LoginPageController;
import org.example.cab302project.PageFunctions;
import org.example.cab302project.SessionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Dictionary;
import java.util.Hashtable;

import javafx.scene.control.Alert.AlertType;
public class DashboardPageController {
    PageFunctions pageFunctions = new PageFunctions();
    Connection connection;
    @FXML
    public void goToPage(ActionEvent event) {
        pageFunctions.goToPage(event);
    }
    @FXML
    HBox hBox;
    @FXML
    Button InitSess;
    @FXML
    public void checkSessStatus() {
        if (!SessionManager.sessStatus) {
            InitSess.setDisable(true);
            displayGroups.setDisable(true);
        } else {
            InitSess.setDisable(false);
            displayGroups.setDisable(false);
        }
    }


    //Ranges down to Initialise method
    Hashtable<String, Integer> groupPairing = new Hashtable<>();
    @FXML
    ComboBox<String> displayGroups;
    @FXML
    Label currentGroup;
    @FXML
    public void populateGroupTable() {
        try {
            String sql = "SELECT * FROM Groups WHERE userID = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, LoginPageController.userID);
                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    displayGroups.getItems().add(rs.getString("GroupName"));
                    groupPairing.put(rs.getString("GroupName"), rs.getInt("GroupID"));
                }
            } catch (SQLException e) {
                System.err.println("Error adding Groups to ComboBox: " + e.getMessage());
            }
        } catch (NullPointerException e) {System.err.println(e.getMessage());}
    }
    private String GetGroupName(int groupID) {
        try {
            String sql = "SELECT GroupName FROM Groups WHERE groupID = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, groupID );
                ResultSet rs = pstmt.executeQuery();
                return rs.getString("GroupName");
            } catch (SQLException e) {
                System.err.println("Error adding Groups to ComboBox: " + e.getMessage());
            }
        } catch (NullPointerException e) {System.err.println(e.getMessage());}
        return null;
    }
    private String GetSubGroupName(int subGroupID) {
        try {
            String sql = "SELECT Name FROM SubGroup WHERE SubGroupID = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, subGroupID );
                ResultSet rs = pstmt.executeQuery();
                return rs.getString("Name");
            } catch (SQLException e) {
                System.err.println("Error adding Groups to ComboBox: " + e.getMessage());
            }
        } catch (NullPointerException e) {System.err.println(e.getMessage());}
        return null;
    }
    @FXML
    public void GetSelectedItem() {
        currentGroup.setText("Current Group: " + displayGroups.selectionModelProperty().get().getSelectedItem());
        SessionManager.currentGroupID = groupPairing.get(displayGroups.selectionModelProperty().get().getSelectedItem());
    }

    @FXML
    TableView<DisplayObject> reportTable;
    @FXML
    TableColumn<DisplayObject, String> focusGroupColumn;
    @FXML
    TableColumn<DisplayObject, String> focusSubGroupColumn;
    @FXML
    TableColumn<DisplayObject, String> dateColumn;
    @FXML
    TableColumn<DisplayObject, String> focusTimeColumn;


    public class DisplayObject {
        private String focusGroup = null, focusSubGroup = null, date = null, focusTime = null;

        public void setFocusGroup(String focusGroup) { this.focusGroup = focusGroup; }
        public String getFocusGroup() {
            return focusGroup;
        }
        public void setFocusSubGroup(String focusSubGroup) { this.focusSubGroup = focusSubGroup; }
        public String getFocusSubGroup() {
            return focusSubGroup;
        }
        public void setDate(String date) { this.date = date; }
        public String getDate() {
            return date;
        }
        public void setFocusTime(String focusTime) { this.focusTime = focusTime; }
        public String getFocusTime() {
            return focusTime;
        }
        public DisplayObject(String focusGroup, String focusSubGroup, String date, String focusTime)  {
            this.focusGroup = focusGroup;
            this.focusSubGroup = focusSubGroup;
            this.date = date;
            this.focusTime = focusTime;
        }
    }

    public void populateReportTable() {
        try {
//            applicationTable.getItems().clear();
            String sql = "SELECT GroupID, SubGroupID, Date, TotalTime FROM Reports WHERE userID = ?";

            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, LoginPageController.userID);
                ResultSet rs = pstmt.executeQuery();
                focusGroupColumn.setCellValueFactory(new PropertyValueFactory<>("focusGroup"));
                focusSubGroupColumn.setCellValueFactory(new PropertyValueFactory<>("focusSubGroup"));
                dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
                focusTimeColumn.setCellValueFactory(new PropertyValueFactory<>("focusTime"));
                while (rs.next()) {
                    String groupName = GetGroupName(rs.getInt("GroupID"));
                    String subGroupName = GetSubGroupName(rs.getInt("SubGroupID"));
                    String time = timeConversion(rs.getInt("TotalTime"));
                    DisplayObject displayObject  = new DisplayObject(groupName, subGroupName, rs.getString("Date"), time);
                    reportTable.getItems().add(displayObject);
                }

            } catch (SQLException e) {
                System.err.println("Error adding blocked applications: " + e.getMessage());
            }
        } catch (NullPointerException e) {System.err.println(e.getMessage());}
    }

    private String timeConversion(int seconds) {
        if (seconds < 60) {
            return (seconds + " Second" + (seconds == 1 ? "" : "s"));
        } else if (seconds < 3600) {
            double minutes = seconds / 60.0;
            String minutesString = String.format(minutes % 1 == 0 ? "%.0f" : "%.2f", minutes);
            return minutesString + " Minute" + (minutesString.equals("1") ? "" : "s");
        } else {
            double hours = seconds / 3600.0;
            String hoursString = String.format(hours % 1 == 0 ? "%.0f" : "%.2f", hours);
            return hoursString + " Hour" + (hoursString.equals("1") ? "" : "s");
        }
    }

    @FXML
    public void handleInitSessButton(ActionEvent event) {
        if (displayGroups.getSelectionModel().isEmpty()) {
            System.out.println("No group selected.");

            showAlert(Alert.AlertType.ERROR, "Error", "Select A group First");
        } else {
            String selectedGroup = displayGroups.getSelectionModel().getSelectedItem();
            System.out.println("Selected group: " + selectedGroup);
            goToPage(event);
        }
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void initialize() {
        try {
            connection = DbConnection.getInstance().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        populateGroupTable();
        if(groupPairing.containsValue(SessionManager.currentGroupID))displayGroups.setValue(GetGroupName(SessionManager.currentGroupID));
        else if(!displayGroups.getItems().isEmpty())displayGroups.setValue(displayGroups.getItems().get(0));
        if(!groupPairing.isEmpty()) GetSelectedItem();
        checkSessStatus();
        pageFunctions.AddSideBar(hBox);
        populateReportTable();
        Platform.runLater(() -> {
            Stage stage = (Stage) hBox.getScene().getWindow();
            stage.addEventHandler(WindowEvent.WINDOW_SHOWN, event -> checkSessStatus());
        });
    }
}

