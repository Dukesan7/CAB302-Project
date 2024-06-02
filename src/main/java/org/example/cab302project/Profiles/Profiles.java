package org.example.cab302project.Profiles;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.cab302project.LoginPageController;
import org.example.cab302project.PageFunctions;

import java.sql.Connection;

public class Profiles {

    private LoginPageController loginPage;
    private PageFunctions pageFunctions = new PageFunctions();
    private Connection connection;
    private Profiles profiles;
    private String selectedQuestion;
    ObservableList<String> potentialQuestions = FXCollections.observableArrayList(
            "What is the name of your first pet?",
            "What school did you first attend?",
            "What suburb did you first live in?",
            "What is your favourite ice cream flavour?"
    );





}
