package org.example.cab302project.focusSess;

import javafx.scene.control.CheckBox;
import org.example.cab302project.DbConnection;
import org.example.cab302project.SessionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import static org.example.cab302project.SessionManager.currentGroupID;

public class initialiseSess {
    public ArrayList<String> subGroups;
    public static int totalMinutes;
    public static int breakInterval;

    public static int GroupID;

    public static int sliderVal;
    private static Map<Integer, String> subGroupMap = new HashMap<>();

//    public static Map<Integer, String> getSubGroupsByGroupID(int groupID) {
//        subGroupMap.clear();
//        String sqlGetSubGroups = "SELECT subGroupID, subGroupName FROM SubGroup WHERE GroupID = ?";
//
//        try {
//            Connection connection = DbConnection.getInstance().getConnection();
//            try (PreparedStatement pstmt = connection.prepareStatement(sqlGetSubGroups)) {
//                pstmt.setInt(1, groupID);
//                ResultSet rs = pstmt.executeQuery();
//
//                while (rs.next()) {
//                    int subGroupID = rs.getInt("subGroupID");
//                    String subGroupName = rs.getString("subGroupName");
//                    subGroupMap.put(subGroupID, subGroupName);
//                    System.out.println("Retrieved SubGroup: " + subGroupID + " - " + subGroupName);
//                }
//            } catch (SQLException e) {
//                System.out.println(e.getMessage());
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//
//        return subGroupMap;
//    }

    public ArrayList<String> getSubGroupList() {

        subGroups = new ArrayList<>();
        subGroups.add("Chemistry");
        subGroups.add("English");
        return subGroups;
    }


    public static Map<Integer, String> getSubGroupMap() {
        return subGroupMap;
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
