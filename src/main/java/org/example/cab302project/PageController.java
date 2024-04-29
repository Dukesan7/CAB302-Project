package org.example.cab302project;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class PageController {
    private Main mainApp;

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    public void goToPage(javafx.event.ActionEvent event) {
        Button button = (Button) event.getSource();
        String pageName = switch (button.getId()) {
            case "dashboardButton" -> "Dashboard";
            case "tasksButton" -> "Tasks";
            case "profilesButton" -> "Profiles";
            case "notesButton" -> "Notes";
            case "reportButton" -> "Report";
            default -> null;
        };

        if (pageName != null) {
            mainApp.goToPage(pageName);
        }
    }
}
