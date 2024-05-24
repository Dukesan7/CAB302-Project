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
import org.example.cab302project.PageFunctions;

import java.awt.*;
import java.util.Objects;

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
        focusSession.setRandomMsgTime();
        System.out.println("breakInterval: " + focusSession.breakInterval);
        start();

        if (Objects.equals(focusSession.wallPaper, "True")) {
            System.out.println("WallPaper Selected!");
        }

        if (Objects.equals(focusSession.blockApp, "True")) {
            appBlockingRun = new AppBlockingRun();
            appBlockingRun.start();
        }

        if (Objects.equals(focusSession.breaks, "True")) {
            focusSession.calcBreakTime();
        }

        Platform.runLater(() -> {
            Stage stage = (Stage) endSess.getScene().getWindow();
            stage.setOnCloseRequest(event -> {
                handleEndSess(new ActionEvent(hiddenDashboardButton, null));
                event.consume();
            });
        });
    }

    @Override
    public void run() {
        focusSession.calculateEndtime();

        while (System.currentTimeMillis() < focusSession.endTime) {
            if (!focusSession.isPaused) {
                focusSession.calculateProgress();

                Platform.runLater(() -> {
                    clock.setProgress(focusSession.progress);
                    timeText.setText(focusSession.displayTime(focusSession.timeLeft));
                });

                if (focusSession.breaks.equals("True")) {
                    long timeUntilNextBreak = focusSession.nextBreakTime - System.currentTimeMillis();
                    if (timeUntilNextBreak <= 0) {
                        System.out.println("Break time reached at: " + System.currentTimeMillis());
                        try {
                            String msg = "Your " + (focusSession.breakLength / 60000) + " minute break starts now. Make sure to get up and stretch, relax and enjoy the break, you've earned this!";
                            focusSession.getBreakMsg(msg);
                        } catch (AWTException e) {
                            throw new RuntimeException(e);
                        }
                        takeBreak();
                    } else {
                        System.out.println("Time left for next break: " + timeUntilNextBreak / 1000 + " seconds");
                    }
                }

                if (System.currentTimeMillis() >= focusSession.nextRandomMsgTime) {
                    try {
                        focusSession.getRandMsg();
                    } catch (AWTException e) {
                        e.printStackTrace();
                    }
                    focusSession.setRandomMsgTime();
                }

                try {
                    Thread.sleep(1000);
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
        System.out.println("Taking a break at: " + System.currentTimeMillis());
        focusSession.isPaused = true;
        focusSession.breakEndTime = System.currentTimeMillis() + focusSession.breakLength;

        new Thread(() -> {
            while (System.currentTimeMillis() < focusSession.breakEndTime) {
                focusSession.breakTimeLeft = focusSession.breakEndTime - System.currentTimeMillis();
                Platform.runLater(() -> timeText.setText("Break: " + focusSession.displayTime(focusSession.breakTimeLeft)));
                System.out.println("Break time left: " + (focusSession.breakTimeLeft / 1000) + " seconds");

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Platform.runLater(() -> {
                System.out.println("Break ended at: " + System.currentTimeMillis());
                controlSess.setText("Pause");
                focusSession.isPaused = false;
                focusSession.calcBreakTime();
                System.out.println("Next break scheduled for: " + focusSession.nextBreakTime);
                String msg = "Your break has ended, time to work hard now, We believe in you!";
                try {
                    focusSession.getBreakMsg(msg);
                } catch (AWTException e) {
                    throw new RuntimeException(e);
                }
                synchronized (FocusSessPageController.this) {
                    FocusSessPageController.this.notify();
                }
            });
        }).start();
    }

    @FXML
    private void handleEndSess(ActionEvent event) {
        interrupt();
        if (appBlockingRun != null && appBlockingRun.isAlive()) {
            appBlockingRun.interrupt();
        }
        focusSession.endSession();

        Platform.runLater(() -> {
            Stage stage = (Stage) endSess.getScene().getWindow();
            SessionManager.sessStatus = true;


            String subgroup = new InitSessPageController().getInitSessList()[0];
            String breakLength = String.valueOf(focusSession.breakLength / 60000) + " minutes";
            String date = java.time.LocalDate.now().toString();

            focusSession.getSessionData(subgroup, breakLength, date);
            hiddenDashboardButton.setId("Dashboard");
            hiddenDashboardButton.fireEvent(event);

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
