package org.example.cab302project.Controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
public class DashboardPageController {
    PageFunctions pageFunctions = new PageFunctions();
    Connection connection; LoginPageController loginPage; SessionManager sessionManager;
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
        } else {
            InitSess.setDisable(false);
        }
    }


    // Following methods and @FXML variables added by Dom
    //Ranges down to Initialise method
    Dictionary<String, Integer> groupPairing = new Hashtable<>();
    @FXML
    ComboBox<String> displayGroups;
    @FXML
    Label currentGroup;
    @FXML
    public void populateGroupTable() {
        try {
            displayGroups.getItems().clear();
            String sql = "SELECT * FROM Groups WHERE userID = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, loginPage.userID );
                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    displayGroups.getItems().add(rs.getString("Groupname"));
                    groupPairing.put(rs.getString("groupName"), rs.getInt("GroupID"));
                }
            } catch (SQLException e) {
                System.err.println("Error adding Groups to ComboBox: " + e.getMessage());
            }
        } catch (NullPointerException e) {System.err.println(e.getMessage());}
    }
    @FXML
    public void GetSelectedItem() {
        currentGroup.setText("Current Group: " + displayGroups.selectionModelProperty().get().getSelectedItem());
        sessionManager.currentGroupID = groupPairing.get(displayGroups.selectionModelProperty().get().getSelectedItem());
    }



    @FXML
    public void handleInitSessButton(ActionEvent event) {
        if (displayGroups.getSelectionModel().isEmpty()) {
            System.out.println("No group selected.");

            showAlert(Alert.AlertType.ERROR, "Error", "Select A group First");
        } else {
            GetSelectedItem();
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


        checkSessStatus();
        System.out.println("Sess status: " + SessionManager.sessStatus);
        pageFunctions.AddSideBar(hBox);

        Platform.runLater(() -> {
            Stage stage = (Stage) hBox.getScene().getWindow();
            stage.addEventHandler(WindowEvent.WINDOW_SHOWN, event -> checkSessStatus());
        });

        InitSess.setOnAction(this::handleInitSessButton);
    }
}
