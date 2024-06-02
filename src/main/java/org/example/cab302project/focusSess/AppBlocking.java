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
 *
 */
public class AppBlocking {

    static Integer GroupID = SessionManager.currentGroupID;

    static ArrayList<String> paths = new ArrayList<>();

    /**
     *
     * @return
     */
    public static final ArrayList<String> getpaths() {
        paths.clear();

        String sql = "SELECT fileName FROM BlackLists WHERE userID = ? AND groupID = ?";
        try {
            Connection connection = DbConnection.getInstance().getConnection();
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {

                pstmt.setInt(1, userID);
                pstmt.setInt(2, GroupID);
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
     *
     * @param args
     */
    public static void appBlocker(String[] args) {
        getpaths();
        FilePathsRunningCheck();

        ProcessManager.killProcess(paths);
    }


    private static void FilePathsRunningCheck() {
        List<String> runningFilePaths = ProcessManager.checkIfFilePathsRunning(paths);
        runningFilePaths.forEach(filePath -> {
        });
    }
}

