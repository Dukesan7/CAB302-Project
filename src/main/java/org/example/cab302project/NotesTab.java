package org.example.cab302project;

import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class NotesTab extends VBox {
    public NotesTab() {
        // Adding elements to the "Planes" tab
        getChildren().add(new Label("Notes"));

        // Adding a button to the "Planes" tab
        Button button = new Button("New Note");
        getChildren().add(button);
    }
}
