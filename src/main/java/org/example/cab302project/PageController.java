package org.example.cab302project;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
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
            case "notesButton" -> "Notes";
            case "reportsButton" -> "Reports";
            case "profilesButton" -> "Profiles";
            default -> null;
        };

        if (pageName != null) {
            mainApp.goToPage(pageName);
        }
    }

    @FXML
    public void handleBackButtonAction(ActionEvent event) {
        // Navigate to the "Dashboard" page when the back button is clicked
        mainApp.goToPage("Dashboard");
    }
}
