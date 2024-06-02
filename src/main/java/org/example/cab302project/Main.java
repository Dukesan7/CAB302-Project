package org.example.cab302project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.Objects;

// The main class is the starting point for the JavaFX application
// It initialises the primary stage and the load the login fxml page
public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception{
        // Create the database if it doesn't exist
        CreateDB.createDatabase();
        // Load the fxml file for the login page
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Login.fxml")));
        primaryStage.setTitle("JavaFX Multi-page App");
        // Create a new scene with loaded FXML root node and set its size
        Scene scene = new Scene(root, 800, 450);
        // Load and add the css stylesheet to the page
        String cssPath = this.getClass().getResource("StyleSheets/style.css").toExternalForm();
        scene.getStylesheets().add(cssPath);
        primaryStage.setTitle("On-Task");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() {
    }

    public static void main(String[] args) {
        launch(args);
    }
}
