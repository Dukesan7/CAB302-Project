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
import java.util.ArrayList;
import java.util.List;

public class PageFunctions {

    public void AddSideBar(HBox hbox){
        VBox sidebar = new VBox();
        sidebar.setMaxWidth(200);
        sidebar.setMinWidth(200);
        sidebar.setSpacing(10);
        sidebar.setStyle("-fx-padding: 10;");
        List<Button> buttons = new ArrayList<>(List.of(
            new Button("Dashboard"),
            new Button("Tasks"),
            new Button("Notes"),
            new Button("Profiles")
        ));

        for(Button button : buttons){
            button.setId(button.getText());
            button.setMaxWidth(180);
            button.setMinWidth(180);
            button.setAlignment(javafx.geometry.Pos.CENTER);
            button.setStyle("-fx-background-color: #f5f5f5; -fx-padding: 10; -fx-font-size: 14px;");
            button.setOnAction(this::goToPage);
        }

        sidebar.getChildren().addAll(buttons);
        hbox.getChildren().add(0, sidebar);
        sidebar.getStylesheets().add(this.getClass().getResource("StyleSheets/style.css").toExternalForm());
        sidebar.setId("sidebar-background");
        sidebar.getStyleClass().add("sidebar-background");
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
            stage.setTitle("Focus Session");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
