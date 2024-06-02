package org.example.cab302project.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Pair;
import org.example.cab302project.DbConnection;
import org.example.cab302project.PageFunctions;
import org.example.cab302project.SessionManager;
import org.example.cab302project.Tasks.Tasks;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * The controller class for the tasks page
 */
public class TasksPageController {

    // FXML objects
    public TextField taskName;
    public ChoiceBox<String> selectedSubgroup;
    public Button addTask;
    public VBox root;
    public HBox hBox;

    private List<String> subGroups = new ArrayList<>();
    private List<Integer> subGroupIDs = new ArrayList<>();
    private Tasks tasks;
    private Connection connection;

    // A dictionary of subgroup name keys and subgroupID values
    private Dictionary<String, Integer> subGroupPairing = new Hashtable<>();

    /**
     * Changes the state of the task represented by the checkbox
     * @param event A task checkbox onAction event
     */
    @FXML
    public void changeState(ActionEvent event){
        CheckBox checkBox = (CheckBox) event.getSource();
        tasks.SwitchState(subGroupPairing.get(selectedSubgroup.getValue()), checkBox.getText(), checkBox.isSelected());
    }

    /**
     * Deletes the task represented by the checkbox
     * @param event A task checkbox button onAction event
     */
    @FXML
    public void deleteTask(ActionEvent event){
        Button button = (Button) event.getSource();
        CheckBox parent = (CheckBox) button.getParent();
        tasks.RemoveSubTask(subGroupPairing.get(selectedSubgroup.getValue()), parent.getText());
        root.getChildren().remove(parent);
    }

    /**
     * Displays the tasks of the selected subgroups
     */
    @FXML
    public void displayTaskList(){
        root.getChildren().clear();

        HashMap<String, Boolean> taskList = tasks.GetTaskList().get(subGroupPairing.get(selectedSubgroup.getValue()));
        List<Pair<String, Integer>> taskIDPairs = tasks.GetTaskIDPairs().get(subGroupPairing.get(selectedSubgroup.getValue()));
        for(Pair<String, Integer> pair : taskIDPairs){
            CreateCheckbox(pair.getKey(), taskList.get(pair.getKey()));
        }
    }

    /**
     * Adds a new task based on the contents of the text box
     */
    @FXML
    public void addNewTask(){
        if(subGroups.isEmpty() || Objects.equals(taskName.getText(), "")) return;
        if(!tasks.GetTaskList().get(subGroupPairing.get(selectedSubgroup.getValue())).containsKey(taskName.getText())){
            CreateCheckbox(taskName.getText(), false);
            tasks.AddSubTask(subGroupPairing.get(selectedSubgroup.getValue()), taskName.getText());
        }
    }

    /**
     * Retrieves and stores the subgroups, subgroup IDs and their pairings
     */
    public void RetrieveSubGroups() {
        try {
            // Retrieves the data from the Database
            String sql = "SELECT * FROM SubGroup WHERE groupID = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, SessionManager.currentGroupID);
                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    // Stores the data
                    subGroupPairing.put(rs.getString("name"), rs.getInt("subGroupID"));
                    subGroups.add(rs.getString("name"));
                    subGroupIDs.add(rs.getInt("subGroupID"));
                }
            } catch (SQLException e) {
                System.err.println("Error adding blocked applications: " + e.getMessage());
            }
        } catch (NullPointerException e) {System.err.println(e.getMessage());}
    }

    // Creates a new task checkbox with the given name and state
    private void CreateCheckbox(String name, Boolean state){
        // Creates the checkbox
        CheckBox checkBox = new CheckBox(name);
        checkBox.setContentDisplay(ContentDisplay.RIGHT);
        checkBox.setGraphicTextGap(10.0);
        checkBox.setStyle("-fx-background-color: c6c6c6;");
        checkBox.setOnAction(this::changeState);
        checkBox.setPadding(new Insets(4.0));
        checkBox.setFont(new Font(16.0));
        checkBox.setSelected(state);

        // Creates the close button and attaches it to the checkbox
        Button closeButton = new Button("X");
        closeButton.setAlignment(Pos.CENTER_RIGHT);
        closeButton.setContentDisplay(ContentDisplay.RIGHT);
        closeButton.setGraphicTextGap(10.0);
        closeButton.setOnAction(this::deleteTask);
        checkBox.setGraphic(closeButton);

        // Displays the checkbox
        root.getChildren().add(checkBox);
    }

    // Runs on initialization
    public void initialize() {
        // Gets a connection to the database
        try {
            connection = DbConnection.getInstance().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // Gets the subgroup data
        RetrieveSubGroups();

        // Adds the sidebar to the UI
        PageFunctions pageFunctions = new PageFunctions();
        pageFunctions.AddSideBar(hBox);

        tasks = new Tasks(subGroupIDs);

        // Populates the subgroup choice box
        selectedSubgroup.getItems().addAll(subGroups);
        if(!subGroups.isEmpty()) selectedSubgroup.setValue(subGroups.get(0));

    }
}
