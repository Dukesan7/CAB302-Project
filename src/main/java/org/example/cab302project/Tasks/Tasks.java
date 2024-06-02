package org.example.cab302project.Tasks;
import javafx.util.Pair;
import org.example.cab302project.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * A model class which handles the creation, removal, and updating of tasks.
 */
public class Tasks {
    // Database connection
    private final Connection connection;

    // A dictionary of SubGroupID keys and values of a HashMap relation between the task name and state
    private Dictionary<Integer, HashMap<String, Boolean>> taskDict = new Hashtable<>();

    // A dictionary of SubGroupID keys and value pairs of task names to IDs
    private Dictionary<Integer, List<Pair<String, Integer>>> taskIDPairs = new Hashtable<>();

    /**
     * Gets the task dictionary
     * @return A dictionary of tasks
     */
    public Dictionary<Integer, HashMap<String, Boolean>> GetTaskList(){
        return taskDict;
    }

    /**
     * Gets the dictionary of Task ID and name pairs
     * @return A dictionary of Task ID and name pairs
     */
    public Dictionary<Integer, List<Pair<String, Integer>>> GetTaskIDPairs(){
        return taskIDPairs;
    }

    /**
     * A constructor for the Tasks class which retrieves data from the database and stores it locally
     * @param subGroups a list of subGroupIDs
     */
    public Tasks(List<Integer> subGroups){
        // Initializes the storage for each subgroup's tasks
        for (Integer subGroup : subGroups) {
            taskDict.put(subGroup, new HashMap<>());
        }
        // Makes a connection to the database
        try {
            connection = DbConnection.getInstance().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // Retrieves the data from the database
        RetrieveData(subGroups);
    }

    /**
     * Changes the state of the given task
     * @param parentID The ID of the subgroup
     * @param taskName The name of the task
     * @param state The intended state
     */
    public void SwitchState(int parentID, String taskName, boolean state){
        int intState;
        if(state) intState = 1;
        else intState = 0;

        // Updates the given task in the database to have the new value
        String sql = "UPDATE ToDo SET Completed = ? WHERE SubGroupID = ? AND Task = ?";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setInt(1, intState);
            pstmt.setInt(2, parentID);
            pstmt.setString(3, taskName);
            pstmt.executeUpdate();
        }catch (SQLException e){
            System.err.println("Error Updating: " + e.getMessage());
        }
        // Updates the local state of the task
        taskDict.get(parentID).replace(taskName, state);
    }

    /**
     * Adds a new task to the database and local storage
     * @param parentID The ID of the subgroup
     * @param taskName The name of the task
     */
    public void AddSubTask(int parentID, String taskName) {
        // Adds a new task into the database
        String sql = "INSERT INTO ToDo(ToDoID, Task, Completed, SubGroupID) VALUES(?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(2, taskName);
            pstmt.setInt(3, 0);
            pstmt.setInt(4, parentID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error adding: " + e.getMessage());
        }
        // Stores the new task locally
        taskDict.get(parentID).put(taskName, false);
        taskIDPairs.get(parentID).add(new Pair<>(taskName, GetTaskID(parentID, taskName)));
    }

    /**
     * Removes a task from the database and local storage
     * @param parentID The ID of the subgroup
     * @param taskName The name of the task
     */
    public void RemoveSubTask(int parentID, String taskName){
        // Removes the task from the database
        String sql = "DELETE FROM ToDo WHERE SubGroupID = ? AND Task = ?";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setInt(1, parentID);
            pstmt.setString(2, taskName);
            pstmt.executeUpdate();
        }catch (SQLException e){
            System.err.println("Error removing: " + e.getMessage());
        }
        // Removes the task from the local storage
        taskDict.get(parentID).remove(taskName);
        taskIDPairs.get(parentID).remove(new Pair<>(taskName, GetTaskID(parentID, taskName)));
    }

    // Returns the ID of the given task
    private int GetTaskID(int parentID, String taskName) {
        // Selects the TaskID from the database with the given SubGroupID and TaskName
        String sql;
        sql = "SELECT ToDoID FROM ToDo WHERE SubGroupID = ? AND Task = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, parentID);
            pstmt.setString(2, taskName);
            ResultSet rs = pstmt.executeQuery();
            return rs.getInt("ToDoID");
        }catch (SQLException e) {
            System.err.println("Error adding: " + e.getMessage());
        }
        return 0;
    }

    // Gets the data from the database and saves it locally
    private void RetrieveData(List<Integer> subGroupIDs){
        // Loops through all subgroups
        for(Integer subGroupId : subGroupIDs){
            // Retrieves all tasks with the current subgroupID
            String sql = "SELECT * FROM ToDo WHERE SubGroupID = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, subGroupId);
                ResultSet rs = pstmt.executeQuery();
                HashMap<String, Boolean> subGroupTasks = new HashMap<>();
                List<Pair<String, Integer>> subTaskIDPairs = new ArrayList<>();
                while (rs.next()) {
                    boolean state = rs.getInt("Completed") == 1;
                    subGroupTasks.put(rs.getString("Task"), state);
                    subTaskIDPairs.add(new Pair<>(rs.getString("Task"), rs.getInt("ToDoID")));
                }
                // Stores the data locally
                taskDict.put(subGroupId, subGroupTasks);
                subTaskIDPairs.sort(Comparator.comparing(Pair::getValue));
                taskIDPairs.put(subGroupId, subTaskIDPairs);

            } catch (SQLException e) {
                System.err.println("Error adding blocked applications: " + e.getMessage());
            }
        }
    }
}
