package org.example.cab302project.Controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.cab302project.SessionManager;
import org.example.cab302project.focusSess.AppBlocking;
import org.example.cab302project.focusSess.FocusSession;
import org.example.cab302project.focusSess.initialiseSess;
import org.example.cab302project.PageFunctions;
import org.w3c.dom.events.Event;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class FocusSessPageController extends java.lang.Thread {
    private long pauseStartTime;

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
    private Button hiddenDashboardButton;
    FocusSession focusSession = new FocusSession();
    @FXML
    public void initialize() {
        String[] data = new InitSessPageController().getInitSessList();
        focusSession.studyLength = focusSession.CalculateTime(data);
        focusSession.collectVariables(data);
        start();
        if (Objects.equals(focusSession.wallPaper, "True")) {
            System.out.println("WallPaper Selected!");
            //Jackson put your wallpaper run section here
        }

        if (Objects.equals(focusSession.blockApp, "True")) {
            appBlockingRun = new AppBlockingRun();
            appBlockingRun.start();
        }

        if (Objects.equals(focusSession.breaks, "True")) {

        }

        Platform.runLater(() -> {
            Stage stage = (Stage) endSess.getScene().getWindow();
            stage.setOnCloseRequest(event -> {
                handleEndSess();
                event.consume();
            });
        });
    }


    @Override
    public void run() {
        focusSession.calculateEndtime();

        while (System.currentTimeMillis() < focusSession.endTime) {
            if (!focusSession.isPaused) {
                double progress = focusSession.calculateProgress();

                Platform.runLater(() -> {
                    clock.setProgress(progress);
                    timeText.setText(focusSession.displayTime(focusSession.timeLeft));
                });
                if (Objects.equals(focusSession.breaks, "True")) {

                    if (focusSession.currentTime >= focusSession.nextBreakTime) {
                        takeBreak();
                    }
                }


                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
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

    public void takeBreak() {
        focusSession.isPaused = true;
        Platform.runLater(() -> {
            new Thread(() -> {
                while (System.currentTimeMillis() < focusSession.breakEndTime) {

                    focusSession.calculateBreakTimes();
                    Platform.runLater(() -> timeText.setText("Break: " + focusSession.displayTime(focusSession.breakTimeLeft)));
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Platform.runLater(() -> {
                    controlSess.setText("Pause");
                    focusSession.isPaused = false;
                    focusSession.nextBreakTime = System.currentTimeMillis() + focusSession.breakInterval;
                    synchronized (FocusSessPageController.this) {
                        FocusSessPageController.this.notify();
                    }
                });
            }).start();
        });
    }


    @FXML
    private void handleEndSess() {
        interrupt();
        if (appBlockingRun != null && appBlockingRun.isAlive()) {
            appBlockingRun.interrupt();
        }
        Platform.runLater(() -> {
            Stage stage = (Stage) endSess.getScene().getWindow();
            SessionManager.sessStatus = true;

            hiddenDashboardButton.setId("Dashboard");
            hiddenDashboardButton.fireEvent(new ActionEvent(hiddenDashboardButton, null));

            stage.close();
        });
    }

    @FXML
    private void handleControlSess() {
        if ("Pause".equals(controlSess.getText())) {
            controlSess.setText("Resume");
            focusSession.isPaused = true;
            pauseStartTime = System.currentTimeMillis();
            if (appBlockingRun != null) {
                appBlockingRun.pauseBlocking();
            }
        } else {
            controlSess.setText("Pause");
            focusSession.isPaused = false;
            long resumeTime = System.currentTimeMillis();
            long pauseDuration = resumeTime - pauseStartTime;
            focusSession.endTime += pauseDuration;
            if (appBlockingRun != null) {
                appBlockingRun.resumeBlocking();
            }
            synchronized (this) {
                notify();
            }
        }
    }


    private class AppBlockingRun extends Thread {
        @Override
        public void run() {
            while (!isInterrupted()) {
                synchronized (this) {
                    while (focusSession.isPaused) {
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            interrupt();
                        }
                    }
                }

                AppBlocking.appBlocker(new String[]{});
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    interrupt();
                }
            }
        }
        public synchronized void pauseBlocking() {
            focusSession.isPaused = true;
        }

        public synchronized void resumeBlocking() {
            focusSession.isPaused = false;
            notify();
        }
    }
}
