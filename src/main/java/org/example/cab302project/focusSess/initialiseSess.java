package org.example.cab302project.focusSess;

import javafx.scene.control.CheckBox;

import java.util.ArrayList;

public class initialiseSess {
    public ArrayList<String> subGroups;
    public static int totalMinutes;
    public static int breakInterval;

    public static int sliderVal;
    public ArrayList<String> getSubGroupList() {

        subGroups = new ArrayList<>();
        subGroups.add("Chemistry");
        subGroups.add("English");
        return subGroups;
    }

    public void calculateTotalMin(String SelectedHours, String SelectedMinutes) {
        totalMinutes = 0;

        if (SelectedHours != null && !SelectedHours.isEmpty()) {
            totalMinutes += Integer.parseInt(SelectedHours) * 60;
        }
        if (SelectedMinutes != null && !SelectedMinutes.isEmpty()) {
            totalMinutes += Integer.parseInt(SelectedMinutes);
        }
    }

    public String breakInterval(int sliderVal) {
        return String.valueOf(totalMinutes / sliderVal);
    }

    public String checkValue(CheckBox checkBox) {
        String checkValue;
        if (checkBox.isSelected()) {
            checkValue = "True";
        } else {
            checkValue = "False";
        }
        return checkValue;
    }

    public static String deNullifyTime(String timeValue){
        if (timeValue == null){
            timeValue = "0";
        }
        return timeValue;
    }
    public void calculateBreakValues(int breakSlider){
        sliderVal = breakSlider;
        breakInterval = totalMinutes / sliderVal;

    }
}
