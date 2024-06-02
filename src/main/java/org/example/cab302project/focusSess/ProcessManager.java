package org.example.cab302project.focusSess;
//imports
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

/**
 * this class handles the proccess factors for the app blocking functionality. checks if a process is running and can kill it
 */
public class ProcessManager {
    /**
     *this method gets a list of all process, filters out unessesary and vital processes and then checks to see if blacklists files are running with the isprocessrunning method.
     * it checks agains the paths frrom the db and if they are in the list then they are destroyed
     * @param filePaths the list of file paths that are blacklisted
     */
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

    /**
     *this method checks to see if a process is running by getting the path from the blacklisted list and checks it across the file system.
     * @param filePath is the blacklisted path
     * @return returns true or false on the blacklist nature
     */
    public static boolean isProcessRunning(String filePath) {
        Path path = FileSystems.getDefault().getPath(filePath);
        return ProcessHandle.allProcesses()
                .map(ProcessHandle::info)
                .map(info -> info.command().orElse(""))
                .anyMatch(command -> command.contains(path.toString()));
    }

    /**
     * this method gets the list of all paths running and filters out system processes
     * @param filePaths is the black list
     * @return returns the list of all paths filtered
     */
    public static List<String> checkIfFilePathsRunning(List<String> filePaths) {
        return filePaths.stream()
                .filter(ProcessManager::isProcessRunning)
                .collect(Collectors.toList());
    }
}
