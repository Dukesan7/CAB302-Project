package org.example.cab302project.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.cab302project.PageFunctions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InitSessPageController {
    @FXML
    HBox hBox;

    private int totalMinutes;
    private int minBreaks;
    private int maxBreaks;

    private int breakInterval;

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
    Label minBreak;

    @FXML
    Label maxBreak;

    @FXML
    Label breakLengthLabel;

    @FXML
    Label numBreakLabel;

    @FXML
    Label breakMinLabel;

    @FXML
    ChoiceBox<Integer> breakTimes;

    @FXML
    CheckBox breaksCheck;

    @FXML
    public void populateGroups() {
        Group.getItems().addAll(Groups);
    }

    @FXML
    public void populateSubGroup() {
        SubGroup.getItems().addAll(SubGroupSchool);
    }

    private static String[] initsessList = new String[8];

    @FXML
    private void handleUpdateBreakNo() {
        String selectedHours = hours.getValue();
        String selectedMinutes = minutes.getValue();

        int totalMinutes = 0;
        int breakInterval = 0;

        if (selectedHours != null && !selectedHours.isEmpty()) {
            totalMinutes += Integer.parseInt(selectedHours) * 60;
        }
        if (selectedMinutes != null && !selectedMinutes.isEmpty()) {
            totalMinutes += Integer.parseInt(selectedMinutes);
        }

        if (totalMinutes > 0) {
            breaksCheck.setDisable(false);


            int maxBreaks = (totalMinutes / 5) - 2;
            int minBreaks = 1;

            if (breaksCheck.isSelected() && breakTimes.getValue() != null) {

                breakSlider.setMin(2);
                breakSlider.setMax(maxBreaks);
                breakSlider.setValue(maxBreaks / 2.0);

                int finalTotalMinutes = totalMinutes;
                breakSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
                    breakDisplay.setText(String.format("%d breaks (1 every %d minutes)", (int) breakSlider.getValue() - 1, finalTotalMinutes / ((int) breakSlider.getValue())));
                });

                maxBreak.setText(String.valueOf(maxBreaks -1));
                minBreak.setText(String.valueOf(minBreaks));
                breakSlider.setDisable(false);
            } else {
                breakSlider.setDisable(true);
            }
        } else {
            breaksCheck.setDisable(true);
            handleBreaksCheck();
        }
    }


    @FXML
    private void handleBreaksCheck() {
        boolean isSelected = breaksCheck.isSelected();
        breakLengthLabel.setVisible(isSelected);
        breakTimes.setVisible(isSelected);
        numBreakLabel.setVisible(isSelected);
        breakSlider.setVisible(isSelected);
        breakDisplay.setVisible(isSelected);
        minBreak.setVisible(isSelected);
        maxBreak.setVisible(isSelected);
        breakMinLabel.setVisible(isSelected);
        if (!isSelected) {
            breakSlider.setDisable(true);
        } else {
            handleBreakTimesChange();
        }
    }

    @FXML
    private void handleBreakTimesChange() {
        if (breakTimes.getValue() != null) {
            handleUpdateBreakNo();
        } else {
            breakSlider.setDisable(true);
        }
    }


    @FXML
    private void handlestartsess() {
        String SelectedGroup = Group.getValue();
        String SelectedSubGroup = SubGroup.getValue();
        String SelectedHours = hours.getValue();
        String SelectedMinutes = minutes.getValue();
        CheckBox SelectedAppBlock = appBlock;
        CheckBox SelectedwallPaper = wallPaper;
        int sliderValue = (int) breakSlider.getValue();
        //String SelectedbreakInterval = String.valueOf((sliderValue!= 0 ? totalMinutes / sliderValue : 0)+1);
        String SelectedBreakLength = String.valueOf(breakTimes.getValue());

        int totalMinutes = 0;

        if (SelectedHours != null && !SelectedHours.isEmpty()) {
            totalMinutes += Integer.parseInt(SelectedHours) * 60;
        }
        if (SelectedMinutes != null && !SelectedMinutes.isEmpty()) {
            totalMinutes += Integer.parseInt(SelectedMinutes);
        }
        String SelectedbreakInterval = String.valueOf(totalMinutes / ((int) breakSlider.getValue()));


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
        initsessList[6] = SelectedbreakInterval;
        initsessList[7] = SelectedBreakLength;

        System.out.println(Arrays.toString(initsessList));



        //int breakInterval = sliderValue != 0 ? totalMinutes / sliderValue : 0;

        System.out.println("Min Breaks: " + minBreaks);
        System.out.println("Max Breaks: " + maxBreaks);
        System.out.println("Selected Breaks: " + sliderValue);
        System.out.println("Break Interval: Every " + SelectedbreakInterval + " minutes");
        PageFunctions pageFunctions = new PageFunctions();
        pageFunctions.openFocusWin();


    }


    @FXML
    Button FocusSess;

    private void updateStartButtonState() {
        boolean isGroupSelected = Group.getValue() != null && !Group.getValue().isEmpty();
        boolean isSubGroupSelected = SubGroup.getValue() != null && !SubGroup.getValue().isEmpty();
        boolean isHoursSelected = hours.getValue() != null && !hours.getValue().isEmpty();
        boolean isMinutesSelected = minutes.getValue() != null && !minutes.getValue().isEmpty();

        boolean enableStartButton = isGroupSelected && isSubGroupSelected && isHoursSelected && isMinutesSelected;
        FocusSess.setDisable(!enableStartButton);
    }

    public String[] getInitSessList() {
        return initsessList;
    }

    @FXML
    private void initialize() {
        populateGroups();
        populateSubGroup();
        breakSlider.setDisable(true);
        breakSlider.setBlockIncrement(1);
        breakSlider.setMajorTickUnit(1);
        breakSlider.setMinorTickCount(0);
        breakSlider.setSnapToTicks(true);

        hours.setOnAction(event -> {
            handleUpdateBreakNo();
            updateStartButtonState();
        });
        minutes.setOnAction(event -> {
            handleUpdateBreakNo();
            updateStartButtonState();
        });

        Group.setOnAction(event -> updateStartButtonState());
        SubGroup.setOnAction(event -> updateStartButtonState());

        breaksCheck.setDisable(true);
        breaksCheck.setOnAction(event -> handleBreaksCheck());
        breakTimes.setOnAction(event -> handleBreakTimesChange());
        updateStartButtonState();
        PageFunctions pageFunctions = new PageFunctions();
        pageFunctions.AddSideBar(hBox);
    }

}
