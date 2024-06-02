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


    public void populateApplicationList() {
        try {
            applicationTable.getItems().clear();
            int currentGroupID = groupPairing.get(selectGroup.getValue());
            String sql = "SELECT fileName, reason FROM BlackLists WHERE groupID = ?";

            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, currentGroupID);
                ResultSet rs = pstmt.executeQuery();

                fileNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
                reasonColumn.setCellValueFactory(new PropertyValueFactory<>("reason"));
                while (rs.next()) {
                    String appPath = ReturnFileShortened(rs.getString("fileName"));
                    DisplayObject displayObject  = new DisplayObject(appPath, rs.getString("reason"));
                    applicationTable.getItems().add(displayObject);
                }

            } catch (SQLException e) {
                System.err.println("Error adding blocked applications: " + e.getMessage());
            }
        } catch (NullPointerException e) {System.err.println(e.getMessage());}
    }

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

    private String ReturnFileShortened(String filePath) {
        Pattern pattern = Pattern.compile( "[^\\\\]*.exe");
        Matcher matcher = pattern.matcher(filePath);
        matcher.find();
        return matcher.group();
    }


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
