package org.example.cab302project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InitSessPageController {

    @FXML
    public void handleBackButtonAction() {
        return;
    }

    public ObservableList<String> Groups = FXCollections.observableArrayList(
            "School"
    );

    public ObservableList<String> SubGroupSchool = FXCollections.observableArrayList(
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
    CheckBox appBlock;
    @FXML
    CheckBox wallPaper;

    @FXML
    Slider breakSlider;

    @FXML
    Label breakDisplay;

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
            stage.setScene(new Scene(root, 1280, 690));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String[] initsessList = new String[6];

    @FXML
    private void handleUpdateBreakNo() {
        String selectedHours = hours.getValue();
        String selectedMinutes = minutes.getValue();

        int totalMinutes = 0;

        if (selectedHours != null && !selectedHours.isEmpty()) {
            totalMinutes += Integer.parseInt(selectedHours) * 60;
        }
        if (selectedMinutes != null && !selectedMinutes.isEmpty()) {
            totalMinutes += Integer.parseInt(selectedMinutes);
        }

        if (totalMinutes > 0) {
            int maxBreaks;
            if (totalMinutes > 10) {
                maxBreaks = totalMinutes / 5;
            } else {
                maxBreaks = totalMinutes / 4;
            }
            breakSlider.setMax(maxBreaks);
            breakSlider.setValue(maxBreaks / 2.0);
            breakDisplay.setText(String.format("%d breaks", (int) breakSlider.getValue()));
            breakSlider.setDisable(false);
        } else {
            breakSlider.setDisable(true);
        }
    }


    @FXML
    private void handlestartsess() {

        String SelectedGroup = Group.getValue();
        String  SelectedSubGroup = SubGroup.getValue();
        String SelectedHours = hours.getValue();
        String SelectedMinutes = minutes.getValue();
        CheckBox SelectedAppBlock = appBlock;
        CheckBox SelectedwallPaper = wallPaper;

        String wallPaper;
        String appBlock;
        if (SelectedwallPaper.isSelected()) {
            wallPaper = "True";
        } else {
            wallPaper = "False";
        }
        if (SelectedAppBlock.isSelected()) {
            appBlock = "True";
        } else {
            appBlock = "False";
        }
        initsessList[0] = SelectedGroup;
        initsessList[1] = SelectedSubGroup;
        initsessList[2] = SelectedHours;
        initsessList[3] = SelectedMinutes;
        initsessList[4] = appBlock;
        initsessList[5] = wallPaper;

        System.out.println(Arrays.toString(initsessList));

        try {
            Parent root = FXMLLoader.load(getClass().getResource("FocusSess.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root, 1280, 690));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String[] getInitSessList() {
        return initsessList;
    }
    @FXML
    public void initialize() {
        populateGroups();
        populateSubGroup();
        breakSlider.setDisable(true);
        breakSlider.setBlockIncrement(1);
        breakSlider.setMajorTickUnit(1);
        breakSlider.setMinorTickCount(0);
        breakSlider.setSnapToTicks(true);

        hours.setOnAction(event -> handleUpdateBreakNo());
        minutes.setOnAction(event -> handleUpdateBreakNo());

        breakSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            breakDisplay.setText(String.format("%d breaks", newValue.intValue()));
        });
    }




}
