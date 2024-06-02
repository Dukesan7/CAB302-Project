package org.example.cab302project.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.cab302project.DbConnection;
import org.example.cab302project.LoginPageController;
import org.example.cab302project.PageFunctions;

import java.io.IOException;
import java.sql.*;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ManageApplicationController {


    private LoginPageController loginPage;
    private PageFunctions pageFunctions = new PageFunctions();
    private Connection connection;
    private Dictionary<String, Integer> groupPairing = new Hashtable<>();

    @FXML
    TableView<DisplayObject> applicationTable;
    @FXML
    TableColumn<DisplayObject, String> fileNameColumn;
    @FXML
    TableColumn<DisplayObject, String> reasonColumn;
    @FXML
    ComboBox<String> selectGroup;


    /**
     * Creates a new Display Object class with String Values
     * This is to allow the tableView to add multiple values at once as TableView requires an Object when using its
     * .add() method.
     *
     * Attempted to Refactor this page and ManageGroups, however the displayObject class was throwing errors when
     * working between classes as it required its fields to be static which caused more problems in the code
     */
    public class DisplayObject {
        private String name = null; private String reason = null;

        public void setName(String name) { this.name = name; }
        public String getName() {
            return name;
        }
        public void setReason(String reason) { this.reason = reason; }
        public String getReason() {
            return reason;
        }
        public DisplayObject(String name, String reason)  {
            this.name = name; this.reason = reason;
        } }

    //populates the groupList based on the user ID
    private void populateGroupList() {
        try {
            selectGroup.getItems().clear();
            String sql = "SELECT * FROM Groups WHERE userID = ?";

            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, loginPage.userID );
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    selectGroup.getItems().add(rs.getString("Groupname"));
                    groupPairing.put(rs.getString("groupName"), rs.getInt("GroupID"));
                }

            } catch (SQLException e) {
                System.err.println("Error adding blocked applications: " + e.getMessage());
            }
        } catch (NullPointerException e) {System.err.println(e.getMessage());}
    }

    /**
     * populates the Blocked application list from the DB
     */
    public void populateApplicationList() {
        try {
            applicationTable.getItems().clear();
            int currentGroupID = groupPairing.get(selectGroup.getValue());
            String sql = "SELECT fileName, reason FROM BlackLists WHERE groupID = ?";

            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, currentGroupID);
                ResultSet rs = pstmt.executeQuery();

                //The setCellValueFactory preps the TableView Columns to input the "name" and "reason" variables
                fileNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
                reasonColumn.setCellValueFactory(new PropertyValueFactory<>("reason"));
                while (rs.next()) {
                    String appPath = ReturnFileShortened(rs.getString("fileName"));
                    //Creates a new Display Object based on the Database data to be inputted into the TableView
                    DisplayObject displayObject  = new DisplayObject(appPath, rs.getString("reason"));
                    applicationTable.getItems().add(displayObject);
                }

            } catch (SQLException e) {
                System.err.println("Error adding blocked applications: " + e.getMessage());
            }
        } catch (NullPointerException e) {System.err.println(e.getMessage());}
    }

    /**
     * From the shortened filepath displayed in the TableView, checks it against the stored full filepaths in the DB
     * and returns the full filepath that matches
     * ALlows for delete functionality to work properly
     * @return extended filepath
     */
    public String ReturnFileExtended() {
        String extendedPath = null;

        try {
            String sql = "SELECT fileName, reason FROM BlackLists WHERE groupID = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, loginPage.userID );
                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    DisplayObject displayObject  = new DisplayObject(rs.getString("fileName"), rs.getString("reason"));
                    if (Pattern.compile("[^\\\\]*.exe").matcher(displayObject.name).find()) { extendedPath = displayObject.name; }
                }
            } catch (SQLException e) {
                System.err.println("Error adding blocked applications: " + e.getMessage());
            }
        } catch (NullPointerException e) {System.err.println(e.getMessage());}
        return extendedPath;
    }

    //returns the shortened filepath (example.exe) based upon the full filePath using regex
    private String ReturnFileShortened(String filePath) {
        Pattern pattern = Pattern.compile( "[^\\\\]*.exe");
        Matcher matcher = pattern.matcher(filePath);
        matcher.find();
        return matcher.group();
    }


    /**
     * Deletes the application from the database where the filepath matches the shortened path
     * provided from ReturnFileShortened
     */
    public void deleteApplications() {
        DisplayObject fileShort = applicationTable.getSelectionModel().selectedItemProperty().get();
        String result = ReturnFileExtended();

        try {
            String query = "DELETE FROM BlackLists WHERE fileName = ?";

            try ( PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setString(1, result );
                pstmt.execute();
                populateApplicationList();
            } catch (SQLException e) {
                System.err.println("Error deleting blocked applications: " + e.getMessage());
            }
        } catch (NullPointerException e) {System.err.println(e.getMessage());}
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

        populateGroupList();
    }
}
