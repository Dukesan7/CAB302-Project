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
        sidebar.setMaxWidth(200);
        sidebar.setMinWidth(200);
        sidebar.setSpacing(10);
        sidebar.setStyle("-fx-background-color: #d3d3d3; -fx-padding: 10;");

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
    }

    @FXML
    public void goToPage(ActionEvent event) {
        Button button = (Button) event.getSource();
        System.out.println(button);

        String pageName = button.getId();
        System.out.println("gotoPage");
        System.out.println(pageName);

        try {
            Parent root = FXMLLoader.load(getClass().getResource(pageName + ".fxml"));
            Stage stage = (Stage) button.getScene().getWindow();
            stage.setScene(new Scene(root, 1280, 690));
            stage.show();
            System.out.println("gotoPage try");

        } catch (IOException e) {
            System.out.println("gotoPage catch");

            e.printStackTrace();
        }
    }
}
