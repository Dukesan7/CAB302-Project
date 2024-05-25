package org.example.cab302project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.Objects;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        CreateDB.createDatabase();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Login.fxml")));
        primaryStage.setTitle("JavaFX Multi-page App");
        primaryStage.setScene(new Scene(root, 800, 450));
        primaryStage.show();
    }

    @Override
    public void stop() {

    }

    public static void main(String[] args) {
        launch(args);
    }
}
