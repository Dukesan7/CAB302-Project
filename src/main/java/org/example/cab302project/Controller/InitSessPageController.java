package org.example.cab302project.Controller;
//imports
import javafx.event.ActionEvent;
import org.example.cab302project.SessionManager;
import org.example.cab302project.focusSess.initialiseSess;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import org.example.cab302project.PageFunctions;
import java.sql.SQLException;
import java.util.*;

/**
 * A Controller Class Which Facilitates the setting of paramaters of a focus session. the parameters are recieved and are used
 to manupulate the sessions paramaters when the session is started.
 */
public class InitSessPageController {
    //define the many variables and fields for fxml and java
    @FXML
    HBox hBox;
    @FXML
    public ChoiceBox<String> SubGroup;
    @FXML
    ChoiceBox<String> hours;
    @FXML
    ChoiceBox<String> minutes;
    @FXML
    CheckBox appBlock;
    @FXML
    Slider breakSlider;
    @FXML
    Label breakDisplay;
    @FXML
    Label minBreak;
    @FXML
    Label maxBreak;
    @FXML
    Label breakLengthLabel;
    @FXML
    Label numBreakLabel;
    @FXML
    Label breakMinLabel;
    @FXML
    ChoiceBox<Integer> breakTimes;
    @FXML
    CheckBox breaksCheck;
    @FXML
    Button Dashboard;
    //constructed new instance of initialiseSess Model for use in the controller
    initialiseSess InitialiseSess = new initialiseSess();

    private static String[] initsessList = new String[8];

    public Hashtable<Integer, String> subGroups = new Hashtable<Integer, String>();

    /**
     * populateSubGroup utilises public Variables from initialiseSess class which gets the available sub groups to display
     from the db by calling the getSubGroupDB. throws SQLException if an error occurs with the associated DB Connection
     * @throws SQLException
     */
    @FXML
    public void populateSubGroup() throws SQLException {
        subGroups = InitialiseSess.getSubGroupDB();
        displaySubGroup();
    }

    /**
     * simple method which gets the values of the hash table that is populated in initialiseSess. the values are the names of the sub groups.
     These are then added to the SubGroup ChoiceBox for selection
     */
    public void displaySubGroup() {SubGroup.getItems().addAll(subGroups.values()); }

    /**
     *A method which is called when either the minutes or hour fields are changed to recalculate the total time for a sessions and the values for the break slider widget.
     * determines if the computed values are viable for the breakslider to be modifiable.
     * calls other methods to do a majority of the calculations
     */
    @FXML
    private void handleUpdateBreakNo() {
        String selectedHours = hours.getValue();
        String selectedMinutes = minutes.getValue();
        InitialiseSess.calculateTotalMin(selectedHours, selectedMinutes);
        //20 minutes is the most before a break cannot be allowed as the length of breaks and their intervals calculates poorly
        if (InitialiseSess.totalMinutes > 19) {
            breaksCheck.setDisable(false);
            // determines the values for the break slider fields
            int maxBreakVal = (InitialiseSess.totalMinutes / 5) - 2;
            int minBreaks = 1;

            if (breaksCheck.isSelected() && breakTimes.getValue() != null) {
                breakSlider.setMin(2);
                breakSlider.setMax(maxBreakVal);
                breakSlider.setValue(maxBreakVal / 2.0);

                setBreakValues();

                maxBreak.setText(String.valueOf(maxBreakVal -1));
                minBreak.setText(String.valueOf(minBreaks));
                breakSlider.setDisable(false);
            } else {
                breakSlider.setDisable(true);
            }
        } else {
            breaksCheck.setDisable(true);
            handleBreaksCheck();
        }
    }


     //gets the value of the slider and with the use of calculateBreakValues method it displays the number of breaks and how long each will be dynamically.
    @FXML
    private void setBreakValues(){
        int sliderValue = (int) breakSlider.getValue();
        InitialiseSess.calculateBreakValues(sliderValue);
        //checks for whenever the slider is touched it updates dynamically
        breakSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            breakDisplay.setText(String.format("%d breaks (1 every %d minutes)", InitialiseSess.sliderVal - 1, InitialiseSess.breakInterval));
        });
    }


     //checks if the 'allow breaks' checkbox is selected by the user if so then all the break related fields become visible and editable
    @FXML
    private void handleBreaksCheck() {
        boolean isSelected = breaksCheck.isSelected();
        //updates the visibility of multiple break widgets on the page
        breakLengthLabel.setVisible(isSelected);
        breakTimes.setVisible(isSelected);
        numBreakLabel.setVisible(isSelected);
        breakSlider.setVisible(isSelected);
        breakDisplay.setVisible(isSelected);
        minBreak.setVisible(isSelected);
        maxBreak.setVisible(isSelected);
        breakMinLabel.setVisible(isSelected);
        //checks if the slide can be controllable or not when the check box is ticked for breaks
        if (!isSelected) {
            breakSlider.setDisable(true);
        } else {
            //updates the break times displayed and stored
            handleBreakTimesChange();
        }
    }

    // checks if breaktimes has been modified
    @FXML
    private void handleBreakTimesChange() {
        if (breakTimes.getValue() != null) {
            //if so then reupdute the calculations of the break variables
            handleUpdateBreakNo();
        } else {
            breakSlider.setDisable(true);
        }
    }

    // checks if the minimum fields are filled out to allow the user to press the start button
    private void updateStartButtonState() {
        // gets boolean statements for each parameter to see if filled or not
        boolean isSubGroupSelected = SubGroup.getValue() != null && !SubGroup.getValue().isEmpty();
        boolean isHoursSelected = hours.getValue() != null && !hours.getValue().isEmpty();
        boolean isMinutesSelected = minutes.getValue() != null && !minutes.getValue().isEmpty();
        //gets boolean to check if all are false and or if at least 1 time parameter is filled
        boolean enableStartButton = isSubGroupSelected && (isHoursSelected || isMinutesSelected);
        Dashboard.setDisable(!enableStartButton);
    }

    // handles the action of starting a session when the button is pressed
    @FXML
    private void handlestartsess(ActionEvent event) {
        //gets all the variables necessary for the focus sessions operation
        String SelectedSubGroupName = SubGroup.getValue();
        String SelectedHours = hours.getValue();
        String SelectedMinutes = minutes.getValue();
        CheckBox SelectedAppBlock = appBlock;
        CheckBox SelectedBreaks = breaksCheck;
        String breakcheck = InitialiseSess.checkValue(SelectedBreaks);
        //replaces the chosen subGroups name with its id with db select query
        String SelectedSubGroup = InitialiseSess.getSubGroupID(SelectedSubGroupName);
        //check if breaks were selected in the form
        String SelectedbreakInterval = "";
        String SelectedBreakLength = "";
        if (breakcheck.equals("True")) {
            //if so get the necesary values to calculate the break interval and break length
            SelectedBreakLength = String.valueOf(breakTimes.getValue());
            int sliderValue = (int) breakSlider.getValue();
            SelectedbreakInterval = InitialiseSess.breakInterval(sliderValue);
        }
        String hours = initialiseSess.deNullifyTime(SelectedHours);
        String minutes = initialiseSess.deNullifyTime(SelectedMinutes);
        InitialiseSess.calculateTotalMin(SelectedHours, SelectedMinutes);
        String appBlock = InitialiseSess.checkValue(SelectedAppBlock);
        //adds the variables into the list for access by the focus session
        initsessList[0] = SelectedSubGroup;
        initsessList[1] = hours;
        initsessList[2] = minutes;
        initsessList[3] = appBlock;
        initsessList[4] = "False";
        initsessList[5] = breakcheck;
        initsessList[6] = SelectedbreakInterval;
        initsessList[7] = SelectedBreakLength;
        //blocks the ability to use the initialise sess button on the dash board to block the ability to make multiple focus sessions at once
        SessionManager.sessStatus = false;
        //open new focus window and return to the dashboard
        PageFunctions pageFunctions = new PageFunctions();
        pageFunctions.goToPage(event);
        pageFunctions.openFocusWin();
    }

    /**
     * a method that gets the list of paramaters set in initsess for the focus session to work with.
     * @return
     */
    public String[] getInitSessList() {
        return initsessList;
    }

    //initilises the initialise session window by setting the state of fields, sets the 'on action' functions
    @FXML
    private void initialize() throws SQLException {
        InitialiseSess.GroupID = SessionManager.currentGroupID;
        populateSubGroup();
        breakSlider.setDisable(true);
        breakSlider.setBlockIncrement(1);
        breakSlider.setMajorTickUnit(1);
        breakSlider.setMinorTickCount(0);
        breakSlider.setSnapToTicks(true);

        hours.setOnAction(event -> {
            handleUpdateBreakNo();
            updateStartButtonState();
        });
        minutes.setOnAction(event -> {
            handleUpdateBreakNo();
            updateStartButtonState();
        });
        SubGroup.setOnAction(event -> updateStartButtonState());
        breakSlider.valueProperty().addListener((observable, oldValue, newValue) -> setBreakValues());
        breaksCheck.setDisable(true);
        breaksCheck.setOnAction(event -> handleBreaksCheck());
        breakTimes.setOnAction(event -> handleBreakTimesChange());
        updateStartButtonState();
        PageFunctions pageFunctions = new PageFunctions();
        pageFunctions.AddSideBar(hBox);
    }

}
