package org.example.cab302project;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import javafx.scene.Parent;
import java.io.IOException;

public class Main extends Application {
    private Stage primaryStage;

    public static void main(String[] args) {launch(args);}

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("On Task");
        goToPage("Dashboard");
        primaryStage.show();
    }

    public void goToPage(String pageName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(pageName + ".fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 800, 500);
            primaryStage.setScene(scene);

            PageController controller = loader.getController();
            controller.setMainApp(this);
        }
        catch (IOException error)
        {
            error.printStackTrace();
        }
    }
}
