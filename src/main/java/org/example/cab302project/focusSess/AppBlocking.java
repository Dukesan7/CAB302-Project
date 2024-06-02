package org.example.cab302project.focusSess;

import org.example.cab302project.DbConnection;
import org.example.cab302project.SessionManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import static org.example.cab302project.LoginPageController.userID;

/**
 * This class is the main appblocking model which gets the paths that are running in the file system and checks if they are blacklisted.
 * it then does the appropriate actions to destroy the instance of the black listed process
 */
public class AppBlocking {

    static Integer GroupID = SessionManager.currentGroupID;

    static ArrayList<String> paths = new ArrayList<>();

    /**
     *this method creates an array list of paths from the db where the userid and the group id are the current users values
     * @return
     */
    public static final ArrayList<String> getpaths() {
        paths.clear();

        String sql = "SELECT fileName FROM BlackLists WHERE groupID = ?";
        try {
            Connection connection = DbConnection.getInstance().getConnection();
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {

                pstmt.setInt(1, GroupID);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    paths.add(rs.getString("fileName"));
                }
            } catch (SQLException e) {
                System.err.println("Error getting subgroups: " + e.getMessage());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return paths;
    }

    /**
     * this method runs other methods to check if any blacklisted paths are running and if so kill them
     * @param args
     */
    public static void appBlocker(String[] args) {
        getpaths();
        FilePathsRunningCheck();
        ProcessManager.killProcess(paths);
    }

    //creates a list of filtered and adjusted paths
    private static void FilePathsRunningCheck() {
        List<String> runningFilePaths = ProcessManager.checkIfFilePathsRunning(paths);
        runningFilePaths.forEach(filePath -> {
        });
    }
}

