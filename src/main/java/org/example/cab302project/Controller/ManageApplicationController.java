package org.example.cab302project.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.cab302project.DbConnection;
import org.example.cab302project.LoginPageController;
import org.example.cab302project.PageFunctions;

import java.io.IOException;
import java.sql.*;

public class ManageApplicationController {


    private LoginPageController loginPage;
    private PageFunctions pageFunctions = new PageFunctions();
    private Connection connection;

    @FXML
    TableView<DisplayObject> applicationTable;
    @FXML
    TableColumn<DisplayObject, String> fileNameColumn;
    @FXML
    TableColumn<DisplayObject, String> reasonColumn;


    public void exampleApps() {
        String sql = "INSERT INTO BlackLists(blackListID, userID, fileName, reason) VALUES(?, ?, ?, ?)";
        var fileNames = new String[] {"Steam.exe", "Chrome.exe", "Amazon.com", "EpicGames.exe", "LeagueofLegends.exe", "SchoolWork.exe"};
        var reasons = new String[] {"Games", "Internet", "Shopping", "Gaming", "Too Distracting", "Toobad"};
        var userID = new int[] {2, 1, 2, 2, 2, 1};

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
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

    public void populateApplicationList() {
        try {
            applicationTable.getItems().clear();
            String sql = "SELECT fileName, reason FROM BlackLists WHERE userID = ?";

            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, loginPage.userID );
                ResultSet rs = pstmt.executeQuery();

                fileNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
                reasonColumn.setCellValueFactory(new PropertyValueFactory<>("reason"));

                while (rs.next()) {
                    DisplayObject displayObject  = new DisplayObject(rs.getString("fileName"), rs.getString("reason"));
                    applicationTable.getItems().add(displayObject);
                }

            } catch (SQLException e) {
                System.err.println("Error adding blocked applications: " + e.getMessage());
            }
        } catch (NullPointerException e) {System.err.println(e.getMessage());}
    }


    public void deleteApplications() {
        DisplayObject result = applicationTable.getSelectionModel().selectedItemProperty().get();

        try {
            String query = "DELETE FROM BlackLists WHERE fileName = ?";

            try ( PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setString(1, result.name );
                pstmt.execute();
                populateApplicationList();
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

        exampleApps();
        populateApplicationList();
    }
}
