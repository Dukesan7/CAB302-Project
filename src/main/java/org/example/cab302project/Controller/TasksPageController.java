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

public class TasksPageController {

    public TextField taskName;
    public ChoiceBox<String> selectedSubgroup;
    public Button addTask;
    public VBox root;
    public HBox hBox;

    private List<String> subGroups = new ArrayList<>();
    private List<Integer> subGroupIDs = new ArrayList<>();
    private Tasks tasks;
    private Connection connection;

    private Dictionary<String, Integer> subGroupPairing = new Hashtable<>();


    @FXML
    public void changeState(ActionEvent event){
        CheckBox checkBox = (CheckBox) event.getSource();
        tasks.SwitchState(subGroupPairing.get(selectedSubgroup.getValue()), checkBox.getText(), checkBox.isSelected());
    }

    @FXML
    public void deleteTask(ActionEvent event){
        Button button = (Button) event.getSource();
        CheckBox parent = (CheckBox) button.getParent();
        tasks.RemoveSubTask(subGroupPairing.get(selectedSubgroup.getValue()), parent.getText());
        root.getChildren().remove(parent);
    }

    @FXML
    public void displayTaskList(ActionEvent event){
        root.getChildren().clear();

        HashMap<String, Boolean> taskList = tasks.GetTaskList().get(subGroupPairing.get(selectedSubgroup.getValue()));
        List<Pair<String, Integer>> taskIDPairs = tasks.taskIDPairs.get(subGroupPairing.get(selectedSubgroup.getValue()));
        for(Pair<String, Integer> pair : taskIDPairs){
            CreateCheckbox(pair.getKey(), taskList.get(pair.getKey()));
        }
    }

    @FXML
    public void addNewTask(ActionEvent event){
        if(subGroups.isEmpty()) return;
        if(!tasks.GetTaskList().get(subGroupPairing.get(selectedSubgroup.getValue())).containsKey(taskName.getText())){
            CreateCheckbox(taskName.getText(), false);
            tasks.AddSubTask(subGroupPairing.get(selectedSubgroup.getValue()), taskName.getText());
        }
    }

    private void CreateCheckbox(String name, Boolean state){

        CheckBox checkBox = new CheckBox(name);
        checkBox.setContentDisplay(ContentDisplay.RIGHT);
        checkBox.setGraphicTextGap(10.0);
        checkBox.setStyle("-fx-background-color: c6c6c6;");
        checkBox.setWrapText(true);
        checkBox.setOnAction(this::changeState); // changeState method needs to be implemented
        checkBox.setPadding(new Insets(4.0));
        checkBox.setFont(new Font(16.0));
        checkBox.setSelected(state);

        Button closeButton = new Button("X");
        closeButton.setAlignment(Pos.CENTER_RIGHT);
        closeButton.setContentDisplay(ContentDisplay.RIGHT);
        closeButton.setGraphicTextGap(10.0);
        closeButton.setOnAction(this::deleteTask);
        checkBox.setGraphic(closeButton);

        VBox.setMargin(checkBox, new Insets(5.0));
        root.getChildren().add(checkBox);
    }

    public void initialize() {
        try {
            connection = DbConnection.getInstance().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        RetrieveSubGroups();

        PageFunctions pageFunctions = new PageFunctions();
        pageFunctions.AddSideBar(hBox);
        selectedSubgroup.getItems().addAll(subGroups);
        tasks = new Tasks(subGroupIDs);
        if(!subGroups.isEmpty()) selectedSubgroup.setValue(subGroups.get(0));
    }

    public void RetrieveSubGroups() {
        try {
            String sql = "SELECT * FROM SubGroup WHERE groupID = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, SessionManager.currentGroupID);
                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    subGroupPairing.put(rs.getString("name"), rs.getInt("subGroupID"));
                    subGroups.add(rs.getString("name"));
                    subGroupIDs.add(rs.getInt("subGroupID"));
                }
            } catch (SQLException e) {
                System.err.println("Error adding blocked applications: " + e.getMessage());
            }
        } catch (NullPointerException e) {System.err.println(e.getMessage());}
    }
}
