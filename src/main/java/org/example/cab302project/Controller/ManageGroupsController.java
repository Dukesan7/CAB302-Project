package org.example.cab302project.Controller;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
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
    Button studyModeSaveButton;
    @FXML
    TextField subGroupTextField;
    @FXML
    Button subGroupSaveButton;
    @FXML
    TableView<DisplayObject> groupTable;
    @FXML
    TableColumn<DisplayObject, String> groupNameColumn;
    @FXML
    Label groupName;

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
        System.out.println(groupPairing.size());
    }

    @FXML
    private void AddGrouptoDB() {
        String userInput = studyModeTextField.getText();
        System.out.println(userInput);

        String sql = "INSERT INTO Groups(GroupID, Groupname, userID) VALUES(?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(2, userInput);
            pstmt.setInt(3, loginPage.userID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error adding: " + e.getMessage());
        }
        populateGroupTable();
    }

    @FXML
    private void AddSubGrouptoDB() {
        if (currentGroup == null) { System.out.println("Select a group!"); }
        String userInput = studyModeTextField.getText();
        Integer groupID = groupPairing.get(currentGroup);
        System.out.println(userInput);

        String sql = "INSERT INTO SubGroups(subGroupID, name, groupID) VALUES(?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(2, userInput);
            pstmt.setInt(3, groupID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error adding: " + e.getMessage());
        }
        populateGroupTable();
    }


    @FXML
    public void GetSelectedItem(Event mouseEvent) {
        TableView<DisplayObject> selectedTable = (TableView) mouseEvent.getSource();
        DisplayObject selectedItem = selectedTable.getSelectionModel().getSelectedItem();
        if (selectedTable.getId().equals("groupTable")) {
            currentGroup = selectedItem.groupName;
            groupName.setText(currentGroup);
        };
        if (selectedTable.getId().equals("subGroupTable")) { }
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
    }
}
