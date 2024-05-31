package org.example.cab302project.Tasks;
import javafx.util.Pair;
import org.example.cab302project.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Tasks {
    private final Connection connection;

    //private HashMap<String, HashMap<String, Boolean>> taskList = new HashMap<>();
    static private Dictionary<Integer, HashMap<String, Boolean>> taskDict = new Hashtable<>();
//    public List<Pair<String, Integer>> taskIDPairs = new ArrayList<>();
    public Dictionary<Integer, List<Pair<String, Integer>>> taskIDPairs = new Hashtable<>();

    public Dictionary<Integer, HashMap<String, Boolean>> GetTaskList(){
        return taskDict;
    }

    public Tasks(List<Integer> subGroups){
        for (Integer subGroup : subGroups) {
            taskDict.put(subGroup, new HashMap<>());
        }
        try {
            connection = DbConnection.getInstance().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        RetrieveData(subGroups);
    }


    public void SwitchState(int parentID, String taskName, boolean state){
        int intState;
        if(state) intState = 1;
        else intState = 0;

        String sql = "UPDATE ToDo SET Completed = ? WHERE SubGroupID = ? AND Task = ?";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setInt(1, intState);
            pstmt.setInt(2, parentID);
            pstmt.setString(3, taskName);
            pstmt.executeUpdate();
        }catch (SQLException e){
            System.err.println("Error Updating: " + e.getMessage());
        }
        taskDict.get(parentID).replace(taskName, state);
    }

    public void AddSubTask(int parentID, String taskName) {
        String sql = "INSERT INTO ToDo(ToDoID, Task, Completed, SubGroupID) VALUES(?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(2, taskName);
            pstmt.setInt(3, 0);
            pstmt.setInt(4, parentID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error adding: " + e.getMessage());
        }
        taskDict.get(parentID).put(taskName, false);
        sql = "SELECT ToDoID FROM ToDo WHERE SubGroupID = ? AND Task = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, parentID);
            pstmt.setString(2, taskName);

            ResultSet rs = pstmt.executeQuery();
            taskIDPairs.get(parentID).add(new Pair(taskName, rs.getInt("ToDoID")));
        }catch (SQLException e) {
            System.err.println("Error adding: " + e.getMessage());
        }

    }

    public void RemoveSubTask(int parentID, String taskName){
        String sql = "SELECT ToDoID FROM ToDo WHERE SubGroupID = ? AND Task = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, parentID);
            pstmt.setString(2, taskName);

            ResultSet rs = pstmt.executeQuery();
            taskIDPairs.get(parentID).remove(new Pair(taskName, rs.getInt("ToDoID")));
        }catch (SQLException e) {
            System.err.println("Error adding: " + e.getMessage());
        }
        sql = "DELETE FROM ToDo WHERE SubGroupID = ? AND Task = ?";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setInt(1, parentID);
            pstmt.setString(2, taskName);
            pstmt.executeUpdate();
        }catch (SQLException e){
            System.err.println("Error removing: " + e.getMessage());
        }
        taskDict.get(parentID).remove(taskName);
    }

    private void RetrieveData(List<Integer> subGroupIDs){
        for(Integer subGroupId : subGroupIDs){
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
                taskDict.put(subGroupId, subGroupTasks);
                subTaskIDPairs.sort(Comparator.comparing(Pair::getValue));
                taskIDPairs.put(subGroupId, subTaskIDPairs);

            } catch (SQLException e) {
                System.err.println("Error adding blocked applications: " + e.getMessage());
            }
        }
    }
}
