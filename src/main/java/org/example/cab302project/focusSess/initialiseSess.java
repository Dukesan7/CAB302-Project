package org.example.cab302project.focusSess;

import javafx.scene.control.CheckBox;
import org.example.cab302project.DbConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;

import static org.example.cab302project.LoginPageController.userID;

public class initialiseSess {

    public  int totalMinutes;
    public int breakInterval;

    public int GroupID;

    public int sliderVal;




    public Hashtable<Integer, String> getSubGroupDB() throws SQLException {
        //ArrayList<String> subGroups = new ArrayList<>();
        Hashtable<Integer, String> subGroups = new Hashtable<Integer, String>();
        String sql = "SELECT * FROM SubGroup WHERE groupID = ?";
        try {
            Connection connection = DbConnection.getInstance().getConnection();
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {

                pstmt.setInt(1, GroupID);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    subGroups.put(rs.getInt("subGroupID"), rs.getString("name"));
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

    public String getSubGroupID(String groupName) {
        String sql = "SELECT subGroupID FROM SubGroup WHERE name = ? AND groupID = ?";

        try {
            Connection connection = DbConnection.getInstance().getConnection();
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, groupName);
                pstmt.setInt(2, GroupID);

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        int subGroupID = rs.getInt("subGroupID");
                        return Integer.toString(subGroupID);
                    }
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }




}
