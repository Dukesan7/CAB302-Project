package org.example.cab302project.focusSess;

import org.example.cab302project.Controller.ProfilesPageController;
import org.example.cab302project.DbConnection;
import org.example.cab302project.SessionManager;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.example.cab302project.SessionManager;

import static org.example.cab302project.LoginPageController.userID;
public class AppBlocking {

    static Integer GroupID = SessionManager.currentGroupID;

    static ArrayList<String> paths = new ArrayList<>();
    public static final ArrayList<String> getpaths() {


        String sql = "SELECT fileName FROM BlackLists WHERE userID = ?";
        try {
            Connection connection = DbConnection.getInstance().getConnection();
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {

                pstmt.setInt(1, userID);
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
        System.out.println(paths);
        return paths;
    }

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

class ProcessManager {

    public static void killProcess(List<String> filePaths) {
        filePaths.forEach(filePath -> {
            Path path = FileSystems.getDefault().getPath(filePath);


            ProcessHandle.allProcesses()
                    .filter(process -> process.info().command().map(cmd -> cmd.contains(path.toString())).orElse(false))
                    .forEach(process -> {
                        if (process.isAlive()) {
                            try {
                                process.destroy();
                                String blockNotify = "Blocked the use of: " + path + "... Stay on Task!";
                                Notification.notification(blockNotify);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
        });
    }

    public static boolean isProcessRunning(String filePath) {
        Path path = FileSystems.getDefault().getPath(filePath);
        return ProcessHandle.allProcesses()
                .map(ProcessHandle::info)
                .map(info -> info.command().orElse(""))
                .anyMatch(command -> command.contains(path.toString()));
    }

    public static List<String> checkIfFilePathsRunning(List<String> filePaths) {
        return filePaths.stream()
                .filter(ProcessManager::isProcessRunning)
                .collect(Collectors.toList());
    }
}
