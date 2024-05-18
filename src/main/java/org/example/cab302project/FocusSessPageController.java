package org.example.cab302project;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class FocusSessPageController extends java.lang.Thread {

    private long studyLength;
    private boolean isPaused = false;
    private long pauseStartTime;
    private long endTime;
    private AppBlockingRun appBlockingRun;

    @FXML
    ProgressIndicator clock;

    @FXML
    Text timeText;

    @FXML
    Button controlSess;

    @FXML
    Button endSess;

    @FXML
    public void initialize() {
        // Optional: Any initializations for your controller
        String[] data = new InitSessPageController().getInitSessList();

        studyLength = CalculateTime(data);
        start();
        String blockApp = data[4];

        if (Objects.equals(blockApp, "True")) {
            System.out.println("True!!!");
            appBlockingRun = new AppBlockingRun();
            appBlockingRun.start();
        }
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        endTime = studyLength + startTime;

        while (System.currentTimeMillis() < endTime) {
            if (!isPaused) {
                long currentTime = System.currentTimeMillis();
                int timeLeft = (int) (endTime - currentTime);

                double progress = 1 - (double) timeLeft / studyLength;
                Platform.runLater(() -> {
                    clock.setProgress(progress);

                    long hours = TimeUnit.MILLISECONDS.toHours(timeLeft);
                    long minutes = TimeUnit.MILLISECONDS.toMinutes(timeLeft) % 60;
                    long seconds = TimeUnit.MILLISECONDS.toSeconds(timeLeft) % 60;
                    String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
                    timeText.setText(timeString);
                });

                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                // If paused, sleep until resumed
                synchronized (this) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        Platform.runLater(() -> {
            timeText.setText("00:00:00");
            clock.setProgress(1);
        });
    }

    @FXML
    private void handleEndSess() {
        System.out.println("End Session");
        // Stop the timer thread
        interrupt();

        // Stop the AppBlocking thread if it is running
        if (appBlockingRun != null && appBlockingRun.isAlive()) {
            appBlockingRun.interrupt();
        }

        // Close the JavaFX window
        Platform.runLater(() -> {
            Stage stage = (Stage) endSess.getScene().getWindow();
            stage.close();
        });
    }

    @FXML
    private void handleControlSess() {
        if ("Pause".equals(controlSess.getText())) {
            controlSess.setText("Resume");
            isPaused = true;
            pauseStartTime = System.currentTimeMillis();
        } else {
            controlSess.setText("Pause");
            isPaused = false;
            long resumeTime = System.currentTimeMillis();
            long pauseDuration = resumeTime - pauseStartTime;
            endTime += pauseDuration;

            synchronized (this) {
                notify();
            }
        }
        System.out.println("start/stop Session");
    }

    private long CalculateTime(String[] data) {
        int hours = Integer.parseInt(data[2]);
        int minutes = Integer.parseInt(data[3]);
        return TimeUnit.HOURS.toMillis(hours) + TimeUnit.MINUTES.toMillis(minutes);
    }



    // Inner class for a second thread
    private class AppBlockingRun extends Thread {
        @Override
        public void run() {
            AppBlocking.appBlocker(new String[]{});
            // Alex, put your app blocking here instead of the other run because it sleeps constantly
        }
    }
}
