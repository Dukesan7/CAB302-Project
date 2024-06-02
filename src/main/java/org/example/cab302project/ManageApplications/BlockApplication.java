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
    private PageFunctions pageFunctions = new PageFunctions();
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

