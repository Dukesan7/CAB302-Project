package org.example.cab302project.focusSess;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 */
public class ProcessManager {
    /**
     *
     * @param filePaths
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
     *
     * @param filePath
     * @return
     */
    public static boolean isProcessRunning(String filePath) {
        Path path = FileSystems.getDefault().getPath(filePath);
        return ProcessHandle.allProcesses()
                .map(ProcessHandle::info)
                .map(info -> info.command().orElse(""))
                .anyMatch(command -> command.contains(path.toString()));
    }

    /**
     *
     * @param filePaths
     * @return
     */
    public static List<String> checkIfFilePathsRunning(List<String> filePaths) {
        return filePaths.stream()
                .filter(ProcessManager::isProcessRunning)
                .collect(Collectors.toList());
    }
}
