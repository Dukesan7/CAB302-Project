package org.example.cab302project;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AppBlocking {

    private static final List<String> FILE_PATHS = Arrays.asList(
            "C:\\Program Files\\Notepad++\\notepad++.exe",
            "C:\\Program Files (x86)\\Steam\\steam.exe");

    public static void appBlocker(String[] args) {
        FilePathsRunningCheck();
        ProcessManager.killProcess(FILE_PATHS);
    }


    private static void FilePathsRunningCheck() {
        List<String> runningFilePaths = ProcessManager.checkIfFilePathsRunning(FILE_PATHS);
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
