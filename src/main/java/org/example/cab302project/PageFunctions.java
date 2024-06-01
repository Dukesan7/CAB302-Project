package org.example.cab302project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class PageFunctions {

    public void AddSideBar(HBox hbox){
        VBox sidebar = new VBox();
//        sidebar.getStylesheets().add(this.getClass().getResource("StyleSheets/style.css").toExternalForm());
//        sidebar.setId("sidebar-background");
        sidebar.setMaxWidth(200);
        sidebar.setMinWidth(200);
        sidebar.setSpacing(10);
        sidebar.setStyle("-fx-padding: 10;");

        Button dashboardButton = new Button("Dashboard");
        dashboardButton.setId("Dashboard");
        dashboardButton.setMaxWidth(180);
        dashboardButton.setMinWidth(180);
        dashboardButton.setAlignment(javafx.geometry.Pos.CENTER);
        dashboardButton.setStyle("-fx-background-color: #f5f5f5; -fx-padding: 10; -fx-font-size: 14px;");
        dashboardButton.setOnAction(this::goToPage);

        Button tasksButton = new Button("Tasks");
        tasksButton.setId("Tasks");
        tasksButton.setMaxWidth(180);
        tasksButton.setMinWidth(180);
        tasksButton.setAlignment(javafx.geometry.Pos.CENTER);
        tasksButton.setStyle("-fx-background-color: #f5f5f5; -fx-padding: 10; -fx-font-size: 14px;");
        tasksButton.setOnAction(this::goToPage);


        Button notesButton = new Button("Notes");
        notesButton.setId("Notes");
        notesButton.setMaxWidth(180);
        notesButton.setMinWidth(180);
        notesButton.setAlignment(javafx.geometry.Pos.CENTER);
        notesButton.setStyle("-fx-background-color: #f5f5f5; -fx-padding: 10; -fx-font-size: 14px;");
        notesButton.setOnAction(this::goToPage);


        Button profilesButton = new Button("Profiles");
        profilesButton.setId("Profiles");
        profilesButton.setMaxWidth(180);
        profilesButton.setMinWidth(180);
        profilesButton.setAlignment(javafx.geometry.Pos.CENTER);
        profilesButton.setStyle("-fx-background-color: #f5f5f5; -fx-padding: 10; -fx-font-size: 14px;");
        profilesButton.setOnAction(this::goToPage);


        sidebar.getChildren().addAll(dashboardButton, tasksButton, notesButton, profilesButton);
        hbox.getChildren().add(0, sidebar);
        sidebar.getStylesheets().add(this.getClass().getResource("StyleSheets/style.css").toExternalForm());
        sidebar.setId("sidebar-background");
        sidebar.getStyleClass().add("sidebar-background");
        System.out.println("test");

    }

    @FXML
    public void goToPage(ActionEvent event) {
        Button button = (Button) event.getSource();
        String pageName = button.getId();
        try {
            Parent root = FXMLLoader.load(getClass().getResource(pageName + ".fxml"));
            Stage stage = (Stage) button.getScene().getWindow();
            Scene scene = (new Scene(root, 800, 450));
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void openFocusWin(){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("FocusSess.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root, 800, 450));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
