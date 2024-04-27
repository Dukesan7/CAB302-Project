package org.example.cab302project;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import java.io.IOException;

public class Main extends Application {
    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        goToPage("Home");
        primaryStage.setTitle("idk app name here");
        primaryStage.show();
    }

    public void goToPage(String pageName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(pageName + ".fxml"));
            VBox root = loader.load();
            Scene scene = new Scene(root, 800, 600);
            primaryStage.setScene(scene);

            PageController controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException error) {
            error.printStackTrace();
        }
    }

}
