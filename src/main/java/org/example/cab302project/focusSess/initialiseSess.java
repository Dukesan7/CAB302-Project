package org.example.cab302project.focusSess;

import javafx.scene.control.CheckBox;
import org.example.cab302project.DbConnection;
import org.example.cab302project.LoginPageController;
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

    public  int totalMinutes;
    public int breakInterval;

    public int GroupID;

    public int sliderVal;

    private static Map<Integer, String> subGroupMap = new HashMap<>();


    public ArrayList<String> getSubGroupDB() throws SQLException {
        ArrayList<String> subGroups = new ArrayList<>();
        String sql = "SELECT name FROM SubGroup WHERE groupID = ?";
        try {
            Connection connection = DbConnection.getInstance().getConnection();
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {

                pstmt.setInt(1, GroupID);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    subGroups.add(rs.getString("name"));
                }
            } catch (SQLException e) {
                System.err.println("Error getting subgroups: " + e.getMessage());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println(subGroups);
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
