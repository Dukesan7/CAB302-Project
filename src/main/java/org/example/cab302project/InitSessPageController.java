package org.example.cab302project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InitSessPageController {

    @FXML
    public void handleBackButtonAction() {
        return;
    }

    ObservableList<String> Groups = FXCollections.observableArrayList(
            "School"
    );

    ObservableList<String> SubGroupSchool = FXCollections.observableArrayList(
            "Chemistry",
            "English",
            "Math"
    );




    @FXML
    ChoiceBox<String> Group;
    @FXML
    ChoiceBox<String> SubGroup;
    @FXML
    ChoiceBox<String> hours;
    @FXML
    ChoiceBox<String> minutes;

    @FXML
    public void populateGroups() {
        Group.getItems().addAll(Groups);

    }
    @FXML
    public void populateSubGroup() {
        SubGroup.getItems().addAll(SubGroupSchool);
    }

    @FXML
    public void goToPage(ActionEvent event) {
        Button button = (Button) event.getSource();
        String pageName = button.getId();

        try {
            Parent root = FXMLLoader.load(getClass().getResource(pageName + ".fxml"));
            Stage stage = (Stage) button.getScene().getWindow();
            stage.setScene(new Scene(root, 600, 400));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<String> initsessList = new ArrayList<>();
    @FXML
    private void handlestartsess() {

        String SelectedGroup = Group.getValue();
        String SelectedSubGroup = SubGroup.getValue();
        String SelectedHours = hours.getValue();
        String SelectedMinutes = minutes.getValue();


        initsessList.clear();
        initsessList.add(SelectedGroup);
        initsessList.add(SelectedSubGroup);
        initsessList.add(SelectedHours);
        initsessList.add(SelectedMinutes);

        System.out.println(initsessList);

        try {
            Parent root = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root, 600, 400));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public List<String> getInitSessList() {
        return initsessList;
    }
    @FXML
    public void initialize() {
        populateGroups();
        populateSubGroup();
    }



}
