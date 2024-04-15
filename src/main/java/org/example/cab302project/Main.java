package org.example.cab302project;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tab;
import javafx.stage.Stage;
import javafx.scene.control.TabPane.TabClosingPolicy;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {

        TabPane tabPane = new TabPane();

        Tab tab1 = new Tab("Home", new HomeTab());
        Tab tab2 = new Tab("Tasks"  , new TasksTab());
        Tab tab3 = new Tab("Profiles" , new ProfilesTab());
        Tab tab4 = new Tab("Notes" , new NotesTab());
        Tab tab5 = new Tab("Report" , new ReportTab());

        tabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

        tabPane.getTabs().addAll(tab1, tab2, tab3, tab4, tab5);

        Scene scene = new Scene(tabPane, 800, 600);

        primaryStage.setScene(scene);
        primaryStage.setTitle("JavaFX App");

        primaryStage.show();
    }
}