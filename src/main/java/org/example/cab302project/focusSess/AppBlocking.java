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
public class AppBlocking {

    static Integer GroupID = SessionManager.currentGroupID;
//    private static final List<String> FILE_PATHS = Arrays.asList(
//            "C:\\Program Files\\Notepad++\\notepad++.exe",
//            "C:\\Program Files (x86)\\Steam\\steam.exe");
    private static List<String> getFilePaths(int groupID) {
        List<String> paths = new ArrayList<>();
        String sqlGetFileNames = "SELECT filePath FROM BlackLists WHERE groupID = ?";

        try {
            Connection connection = DbConnection.getInstance().getConnection();
            try (PreparedStatement pstmt = connection.prepareStatement(sqlGetFileNames)) {
                pstmt.setInt(1, groupID);
                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    paths.add(rs.getString("fileName"));
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return paths;
    }
    public static void appBlocker(String[] args) {
        FilePathsRunningCheck();

        ProcessManager.killProcess(getFilePaths(GroupID));
    }


    private static void FilePathsRunningCheck() {
        List<String> runningFilePaths = ProcessManager.checkIfFilePathsRunning(getFilePaths(GroupID));
        runningFilePaths.forEach(filePath -> {
            //System.out.println("The application at " + filePath + " is running.");
        });
    }
}

class ProcessManager {

    public static void killProcess(List<String> filePaths) {
        filePaths.forEach(filePath -> {
            Path path = FileSystems.getDefault().getPath(filePath);
            List<String> runningProcesses = new ArrayList<>();
            List<String> nonRunningProcesses = new ArrayList<>();

            ProcessHandle.allProcesses()
                    .filter(process -> process.info().command().map(cmd -> cmd.contains(path.toString())).orElse(false))
                    .forEach(process -> {
                        if (process.isAlive()) {
                            try {
                                process.destroy();
                                String blockNotify = "Blocked the use of: " + path.toString() + "... Stay on Task!";
                                Notification.notification(blockNotify);
                                runningProcesses.add(path.toString());
                                //System.out.println("Shut down process: " + path.toString());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            nonRunningProcesses.add(path.toString());
                            //System.out.println("Process already stopped: " + path.toString());
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
