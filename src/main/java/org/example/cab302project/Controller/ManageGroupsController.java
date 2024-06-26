package org.example.cab302project.Controller;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.cab302project.DbConnection;
import org.example.cab302project.LoginPageController;
import org.example.cab302project.PageFunctions;

import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Dictionary;
import java.util.Hashtable;

public class ManageGroupsController {
    private PageFunctions pageFunctions = new PageFunctions();
    private Connection connection;
    LoginPageController loginPage;
    DashboardPageController dashboard = new DashboardPageController();
    private String currentGroup;
    Dictionary<String, Integer> groupPairing = new Hashtable<>();
    //update test

    public class DisplayObject {
        private String groupName = null;
        public void setGroupName(String groupName) { this.groupName = groupName; }
        public String getGroupName() {
            return groupName;
        }
        public DisplayObject(String groupName)  {
            this.groupName = groupName;
        } }

    @FXML
    TextField studyModeTextField;
    @FXML
    TextField subGroupTextField;
    @FXML
    TableView<DisplayObject> groupTable;
    @FXML
    TableColumn<DisplayObject, String> groupNameColumn;
    @FXML
    Label groupName;
    @FXML
    TableView<DisplayObject> subGroupTable;
    @FXML
    TableColumn<DisplayObject, String> subGroupColumn;

    /**
     * Populates the groupTable based upon the database values
     */
    @FXML
    public void populateGroupTable() {
        try {
            groupTable.getItems().clear();
            String sql = "SELECT * FROM Groups WHERE userID = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, loginPage.userID );
                ResultSet rs = pstmt.executeQuery();
                groupNameColumn.setCellValueFactory(new PropertyValueFactory<>("groupName"));

                while (rs.next()) {
                    DisplayObject displayObject  = new DisplayObject(rs.getString("Groupname"));
                    groupTable.getItems().add(displayObject);
                    groupPairing.put(rs.getString("groupName"), rs.getInt("GroupID"));
                }
            } catch (SQLException e) {
                System.err.println("Error adding blocked applications: " + e.getMessage());
            }
        } catch (NullPointerException e) {System.err.println(e.getMessage());}
    }

    //Adds a new group to the database
    @FXML
    private void AddGrouptoDB() {
        String userInput = studyModeTextField.getText();
        String sql = "INSERT INTO Groups(GroupID, Groupname, userID) VALUES(?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(2, userInput);
            pstmt.setInt(3, loginPage.userID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error adding: " + e.getMessage());
        }
        populateGroupTable();
        populateSubGroupTable();
    }

    /**
     * Populates the subGroup Table based upon the values stored in the database
     */
    public void populateSubGroupTable() {
        try {
            subGroupTable.getItems().clear();
            String sql = "SELECT * FROM SubGroup WHERE groupID = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, groupPairing.get(currentGroup) );
                ResultSet rs = pstmt.executeQuery();
                subGroupColumn.setCellValueFactory(new PropertyValueFactory<>("groupName"));

                while (rs.next()) {
                    DisplayObject displayObject  = new DisplayObject(rs.getString("name"));
                    subGroupTable.getItems().add(displayObject);
                    groupPairing.put(rs.getString("name"), rs.getInt("GroupID"));
                }
            } catch (SQLException e) {
                System.err.println("Error adding blocked applications: " + e.getMessage());
            }
        } catch (NullPointerException e) {System.err.println(e.getMessage());}
    }

    //Adds a subGroup to the database
    @FXML
    private void AddSubGrouptoDB() {
        if (currentGroup == null) {
            dashboard.showAlert(Alert.AlertType.ERROR, "Select a Group!", "Select A group First");
            return;
        }
        String userInput = subGroupTextField.getText();
        Integer groupID = groupPairing.get(currentGroup);

        String sql = "INSERT INTO SubGroup(subGroupID, name, groupID) VALUES(?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(2, userInput);
            pstmt.setInt(3, groupID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error adding: " + e.getMessage());
        }
        populateGroupTable();
        populateSubGroupTable();
    }

    /**
     * Sets the selected Group to the item selected in the TableView
     * @param mouseEvent waits for the user to select an item on the TableView
     */
    @FXML
    public void GetSelectedItem(Event mouseEvent) {
        try {
            TableView<DisplayObject> selectedTable = (TableView) mouseEvent.getSource();
            DisplayObject selectedItem = selectedTable.getSelectionModel().getSelectedItem();
            populateSubGroupTable();
            if (selectedTable.getId().equals("groupTable")) {
                currentGroup = selectedItem.groupName;
                groupName.setText(currentGroup);
            }
        } catch (NullPointerException e) {System.err.println(e.getMessage());}
    }

    /**
     * Deletes the selected group in the dataBase with the Selected group in the tableview
     */
    @FXML
    public void DeleteGroup() {
        DisplayObject result = groupTable.getSelectionModel().getSelectedItem();
        try {
            String query = "DELETE FROM Groups WHERE Groupname = ?";
            try ( PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setString(1, result.groupName );
                pstmt.execute();
                populateGroupTable();
            } catch (SQLException e) {
                System.err.println("Error deleting blocked applications: " + e.getMessage());
            }
        } catch (NullPointerException e) {System.err.println(e.getMessage());}
    }

    /**
     * Deletes the selected SubGroup from the databased based on the selected TableView Item
     */
    @FXML
    public void DeleteSubgroup() {
        DisplayObject result = subGroupTable.getSelectionModel().getSelectedItem();
        try {
            String query = "DELETE FROM SubGroup WHERE name = ?";
            try ( PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setString(1, result.groupName );
                pstmt.execute();
                populateSubGroupTable();
            } catch (SQLException e) {
                System.err.println("Error deleting blocked applications: " + e.getMessage());
            }
        } catch (NullPointerException e) {System.err.println(e.getMessage());}
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
        try {
            connection = DbConnection.getInstance().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        populateGroupTable();
        populateSubGroupTable();
    }
}
