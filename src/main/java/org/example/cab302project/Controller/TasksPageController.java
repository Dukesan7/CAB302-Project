package org.example.cab302project.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import org.example.cab302project.PageFunctions;
import org.example.cab302project.Tasks.Tasks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TasksPageController {

    public TextField taskName;
    public ChoiceBox<String> selectedSubgroup;
    public Button addTask;
    public VBox root;
    public HBox hBox;

    private List<String> subGroups = new ArrayList<>(List.of("Chemistry", "English", "Math"));
    private Tasks tasks = new Tasks(subGroups);

    @FXML
    public void changeState(ActionEvent event){
        CheckBox checkBox = (CheckBox) event.getSource();
        tasks.SwitchState(selectedSubgroup.getValue(),checkBox.getText(), checkBox.isSelected());
    }

    @FXML
    public void deleteTask(ActionEvent event){
        Button button = (Button) event.getSource();
        CheckBox parent = (CheckBox) button.getParent();
        tasks.RemoveSubTask(selectedSubgroup.getValue(), parent.getText());
        root.getChildren().remove(parent);
    }

    @FXML
    public void displayTaskList(ActionEvent event){
        root.getChildren().clear();
        HashMap<String, Boolean> taskList = tasks.GetTaskList().get(selectedSubgroup.getValue());
        for (var task : taskList.keySet()){
            CreateCheckbox(task, taskList.get(task));
        }
    }

    @FXML
    public void addNewTask(ActionEvent event){
        if(!tasks.GetTaskList().get(selectedSubgroup.getValue()).containsKey(taskName.getText())){
            CreateCheckbox(taskName.getText(), false);
            tasks.AddSubTask(selectedSubgroup.getValue(), taskName.getText());
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

        // Create and set a graphic button
        Button closeButton = new Button("X");
        closeButton.setAlignment(Pos.CENTER_RIGHT);
        closeButton.setContentDisplay(ContentDisplay.RIGHT);
        closeButton.setGraphicTextGap(10.0);
        closeButton.setOnAction(this::deleteTask);
        checkBox.setGraphic(closeButton);

        // Set margin for the CheckBox within its container
        VBox.setMargin(checkBox, new Insets(5.0));
        root.getChildren().add(checkBox);
    }

    public void initialize() {
        // Optional: Any initializations for your controller
        PageFunctions pageFunctions = new PageFunctions();
        pageFunctions.AddSideBar(hBox);
        selectedSubgroup.getItems().addAll(subGroups);
        selectedSubgroup.setValue(subGroups.get(0));

    }

}
