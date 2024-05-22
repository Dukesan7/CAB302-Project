package org.example.cab302project.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import org.example.cab302project.PageFunctions;
import org.example.cab302project.SessionManager;

public class DashboardPageController {
    PageFunctions pageFunctions = new PageFunctions();

    @FXML
    public void goToPage(ActionEvent event) {
        pageFunctions.goToPage(event);
    }
    @FXML
    HBox hBox;
    @FXML
    Button InitSess;



    @FXML
    public void checkSessStatus() {
        if (!SessionManager.sessStatus) {
            InitSess.setDisable(true);
        } else {
            InitSess.setDisable(false);
        }
    }

    public void initialize() {
        // Optional: Any initializations for your controller
        checkSessStatus();
        System.out.println("Sess status: " + SessionManager.sessStatus);
        pageFunctions.AddSideBar(hBox);
    }
}
