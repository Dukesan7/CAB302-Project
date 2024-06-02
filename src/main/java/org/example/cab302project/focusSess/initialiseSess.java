package org.example.cab302project.focusSess;

import javafx.scene.control.CheckBox;
import org.example.cab302project.DbConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;

/**
 * A class which handles much of the calculations relating to the initialisation of a session with multiple methods.
 * only utilised in the initsess page controller.
 */
public class initialiseSess {
    public  int totalMinutes;
    public int breakInterval;
    public int GroupID;
    public int sliderVal;

    /**
     * A crucial method that creates the hash table for storing the subgroup name and id.
     * for use in displaying the subgroup names and for identifying the chosen sub group when compiling the variables for the focus session
     * @return returns the subgroup hash table
     */
    public Hashtable<Integer, String> getSubGroupDB() {
        Hashtable<Integer, String> subGroups = new Hashtable<Integer, String>();
        String sql = "SELECT * FROM SubGroup WHERE groupID = ?";
        try {
            //makes connection to db
            Connection connection = DbConnection.getInstance().getConnection();
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {

                pstmt.setInt(1, GroupID);
                ResultSet rs = pstmt.executeQuery();
                // while the query is being recieved
                while (rs.next()) {
                    subGroups.put(rs.getInt("subGroupID"), rs.getString("name"));
                }
            //catch any errors and print them
            } catch (SQLException e) {
                System.err.println("Error getting subgroups: " + e.getMessage());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println(subGroups);
        return subGroups;
    }

    /**
     * this method recieves the  hours and minutes Selected by the user and combines the values of both into totalMinutes for easier use
     * @param SelectedHours is the Hours selected by the user in the form
     * @param SelectedMinutes is the Minutes selected by the user in the form
     */
    public void calculateTotalMin(String SelectedHours, String SelectedMinutes) {
        totalMinutes = 0;
        // checks if eeach are not null
        if (SelectedHours != null && !SelectedHours.isEmpty()) {
            totalMinutes += Integer.parseInt(SelectedHours) * 60;
        }
        if (SelectedMinutes != null && !SelectedMinutes.isEmpty()) {
            totalMinutes += Integer.parseInt(SelectedMinutes);
        }
    }

    /**
     *this small method calculates the break interval by dividing the total minutes by the slider in string form for the list
     * @param sliderVal the value of the sliders position at the time of the start session button event
     * @returns the break interval in string form
     */
    public String breakInterval(int sliderVal) {
        return String.valueOf(totalMinutes / sliderVal);
    }

    /**
     * a method that confirms if a check box value was selected when the init sess event occurs and returns the value in string form
     * @param checkBox is a checkbox fields value from the form. is used by both the appblock and breaks checking
     * @return returns the string true or false for the list. will convert to boolean in future release
     */
    public String checkValue(CheckBox checkBox) {
        String checkValue;
        if (checkBox.isSelected()) {
            checkValue = "True";
        } else {
            checkValue = "False";
        }
        return checkValue;
    }

    /**
     * a method that checks the any time value for null. if so it replaces it with 0 to remove null errors.
     * @param timeValue is either hours or minutes values from the fields in the form, depends on whos calling the method.
     * @return
     */
    public static String deNullifyTime(String timeValue){
        if (timeValue == null){
            timeValue = "0";
        }
        return timeValue;
    }

    /**
     * this method sets the break sliders value publicly for use outside of this class and calculates the integer version of break interval instead
     * used for calculating the variables for displaying on the form in the breaks section
     * @param breakSlider is the value of the break slider at the point of action on the event
     */
    public void calculateBreakValues(int breakSlider){
        sliderVal = breakSlider;
        breakInterval = totalMinutes / sliderVal;

    }

    /**
     *This method uses the selected subgroups name and finds its ID based on the name being the same and the same groupID
     * @param subGroupName is the selected name recieved from the init sess from
     * @return returns the id of the subgroup in string from for the sessions variable list or null if none selected as form of error exception handling
     */
    public String getSubGroupID(String subGroupName) {
        String sql = "SELECT subGroupID FROM SubGroup WHERE name = ? AND groupID = ?";

        try {
            Connection connection = DbConnection.getInstance().getConnection();
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, subGroupName);
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
