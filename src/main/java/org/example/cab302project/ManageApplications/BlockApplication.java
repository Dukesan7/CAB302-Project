package org.example.cab302project.ManageApplications;

import javafx.stage.FileChooser;
import org.example.cab302project.DbConnection;
import org.example.cab302project.LoginPageController;
import org.example.cab302project.PageFunctions;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BlockApplication {
    private Connection connection;
    private LoginPageController loginPage;

    public String getReturnString() {
        return returnString;
    }
    String returnString;
    private String appPath = null;

    public BlockApplication() {
        try {
            connection = DbConnection.getInstance().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the Full application path based on the users selection using FileChooser
     * Uses Regex to return a shortened version of the application path to display to the user
     * @param fileChooser Opens a window for the user to select an application
     * @return returns filePath
     */
    public String ReturnApplicationPath(FileChooser fileChooser) {
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            appPath = file.getAbsolutePath();
            Pattern pattern = Pattern.compile( "[^\\\\]*.exe");
            Matcher matcher = pattern.matcher(appPath);
            matcher.find();
            returnString = matcher.group();
        }
        return returnString;
    }

    /**
     * Displays the groupnames from the database into a list to be sent to the controller
     * @return returns a list of groups
     */
    public ArrayList<String> returnDataToGroupList() {
        ArrayList<String> returnedData = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Groups WHERE userID = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, loginPage.userID );
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    returnedData.add(rs.getString("Groupname"));
                }
            } catch (SQLException e) {
                System.err.println("Error adding blocked applications: " + e.getMessage());
            }
        } catch (NullPointerException e) {System.err.println(e.getMessage());}
        return returnedData;
    }

    /**
     * Adds an application to be blocked into the database
     * @param blockReason is the reason for the app to be blocked taken from the controller
     */
    public void addApplicationToDb(String blockReason) {
        String sql = "INSERT INTO BlackLists(blackListID, groupID, fileName, reason) VALUES(?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(2, loginPage.userID);
            pstmt.setString(3, appPath);
            pstmt.setString(4, blockReason);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error adding: " + e.getMessage());
        }
    }
}

