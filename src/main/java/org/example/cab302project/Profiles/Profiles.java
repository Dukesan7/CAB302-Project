package org.example.cab302project.Profiles;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import org.example.cab302project.Controller.DashboardPageController;
import org.example.cab302project.DbConnection;
import org.example.cab302project.LoginPageController;
import org.example.cab302project.PageFunctions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Profiles {

    private LoginPageController loginPage = new LoginPageController();
    private Connection connection;

    public ObservableList<String> getPotentialQuestions() {
        return potentialQuestions;
    }

    ObservableList<String> potentialQuestions = FXCollections.observableArrayList(
            "What is the name of your first pet?",
            "What school did you first attend?",
            "What suburb did you first live in?",
            "What is your favourite ice cream flavour?"
    );

    public Profiles() {
        try {
            connection = DbConnection.getInstance().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public String returnDefaultSecurityQuestion() {
        String defaultQuestion = null;
        try {
            String sql = "SELECT (securityQuestion) FROM UserDetails WHERE userID = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, loginPage.userID);
                ResultSet rs = pstmt.executeQuery();
                defaultQuestion = rs.getString("securityQuestion");
            } catch (NullPointerException e) {
                System.err.println(e.getMessage());
            }
        } catch (SQLException e) {
            System.err.println("Error adding: " + e.getMessage());
        }
        System.out.println(defaultQuestion);
        return defaultQuestion;
    }


    public boolean saveSecurityQuestionToDB(String question, String answer) {
        String hashAnswer = loginPage.hashString(answer);
        String sql = "UPDATE UserDetails SET securityQuestion = ?, securityAnswer = ? WHERE UserID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            int userID = loginPage.userID;
            pstmt.setString(1, question);
            pstmt.setString(2, hashAnswer);
            pstmt.setInt(3, userID);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }



}
