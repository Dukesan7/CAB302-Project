package org.example.cab302project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;

public class DashboardPageController {
    PageFunctions pageFunctions = new PageFunctions();

    @FXML
    public void goToPage(ActionEvent event) {
        pageFunctions.goToPage(event);
    }
    @FXML
    HBox hBox;
    public void initialize() {
        // Optional: Any initializations for your controller
        pageFunctions.AddSideBar(hBox);
    }
}
