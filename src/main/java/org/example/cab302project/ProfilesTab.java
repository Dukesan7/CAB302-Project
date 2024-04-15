package org.example.cab302project;

import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class ProfilesTab extends VBox {
    public ProfilesTab() {
        // Adding elements to the "Planes" tab
        getChildren().add(new Label("Profiles"));

        // Adding a button to the "Planes" tab
        Button button = new Button("Change Profile");
        getChildren().add(button);
    }
}
