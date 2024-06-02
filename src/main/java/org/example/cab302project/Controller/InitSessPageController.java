package org.example.cab302project.Controller;

import javafx.event.ActionEvent;
import org.example.cab302project.SessionManager;
import org.example.cab302project.focusSess.initialiseSess;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import org.example.cab302project.PageFunctions;

import java.sql.SQLException;
import java.util.*;


public class InitSessPageController {
    @FXML
    HBox hBox;
    @FXML
    public ChoiceBox<String> SubGroup;
    @FXML
    ChoiceBox<String> hours;
    @FXML
    ChoiceBox<String> minutes;
    @FXML
    CheckBox appBlock;
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
    Button Dashboard;
    initialiseSess InitialiseSess = new initialiseSess();



    public Hashtable<Integer, String> subGroups = new Hashtable<Integer, String>();
    @FXML
    public void populateSubGroup() throws SQLException {
        subGroups = InitialiseSess.getSubGroupDB();
        displaySubGroup();
    }

    public void displaySubGroup() {SubGroup.getItems().addAll(subGroups.values()); }

    @FXML
    private void handleUpdateBreakNo() {
        String selectedHours = hours.getValue();
        String selectedMinutes = minutes.getValue();
        InitialiseSess.calculateTotalMin(selectedHours, selectedMinutes);

        if (InitialiseSess.totalMinutes > 19) {
            breaksCheck.setDisable(false);

            int maxBreakVal = (InitialiseSess.totalMinutes / 5) - 2;
            int minBreaks = 1;

            if (breaksCheck.isSelected() && breakTimes.getValue() != null) {
                breakSlider.setMin(2);
                breakSlider.setMax(maxBreakVal);
                breakSlider.setValue(maxBreakVal / 2.0);

                setBreakValues();

                maxBreak.setText(String.valueOf(maxBreakVal -1));
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
    private void setBreakValues(){
        int sliderValue = (int) breakSlider.getValue();
        InitialiseSess.calculateBreakValues(sliderValue);

        breakSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            breakDisplay.setText(String.format("%d breaks (1 every %d minutes)", InitialiseSess.sliderVal - 1, InitialiseSess.breakInterval));
        });
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

    private void updateStartButtonState() {
        boolean isSubGroupSelected = SubGroup.getValue() != null && !SubGroup.getValue().isEmpty();
        boolean isHoursSelected = hours.getValue() != null && !hours.getValue().isEmpty();
        boolean isMinutesSelected = minutes.getValue() != null && !minutes.getValue().isEmpty();

        boolean enableStartButton = isSubGroupSelected && (isHoursSelected || isMinutesSelected);
        Dashboard.setDisable(!enableStartButton);
    }

    private static String[] initsessList = new String[8];
    @FXML
    private void handlestartsess(ActionEvent event) {
        String SelectedSubGroupName = SubGroup.getValue();
        String SelectedSubGroup = InitialiseSess.getSubGroupID(SelectedSubGroupName);
        String SelectedHours = hours.getValue();
        String SelectedMinutes = minutes.getValue();
        CheckBox SelectedAppBlock = appBlock;
        CheckBox SelectedBreaks = breaksCheck;
        String breakcheck = InitialiseSess.checkValue(SelectedBreaks);
        String SelectedbreakInterval = "";
        String SelectedBreakLength = "";
        if (breakcheck.equals("True")) {
            SelectedBreakLength = String.valueOf(breakTimes.getValue());
            int sliderValue = (int) breakSlider.getValue();
            SelectedbreakInterval = InitialiseSess.breakInterval(sliderValue);
        }
        String hours = initialiseSess.deNullifyTime(SelectedHours);
        String minutes = initialiseSess.deNullifyTime(SelectedMinutes);

        InitialiseSess.calculateTotalMin(SelectedHours, SelectedMinutes);

        String appBlock = InitialiseSess.checkValue(SelectedAppBlock);

        initsessList[0] = SelectedSubGroup;
        initsessList[1] = hours;
        initsessList[2] = minutes;
        initsessList[3] = appBlock;
        initsessList[4] = "False";
        initsessList[5] = breakcheck;
        initsessList[6] = SelectedbreakInterval;
        initsessList[7] = SelectedBreakLength;


        SessionManager.sessStatus = false;

        PageFunctions pageFunctions = new PageFunctions();
        pageFunctions.goToPage(event);
        pageFunctions.openFocusWin();
    }
    public String[] getInitSessList() {
        return initsessList;
    }

    @FXML
    private void initialize() throws SQLException {
        InitialiseSess.GroupID = SessionManager.currentGroupID;
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
        SubGroup.setOnAction(event -> updateStartButtonState());
        breakSlider.valueProperty().addListener((observable, oldValue, newValue) -> setBreakValues());
        breaksCheck.setDisable(true);
        breaksCheck.setOnAction(event -> handleBreaksCheck());
        breakTimes.setOnAction(event -> handleBreakTimesChange());
        updateStartButtonState();
        PageFunctions pageFunctions = new PageFunctions();
        pageFunctions.AddSideBar(hBox);
    }

}
