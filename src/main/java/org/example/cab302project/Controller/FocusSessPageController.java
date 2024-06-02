package org.example.cab302project.Controller;
//imports
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
import java.awt.*;
import java.util.Objects;

/**
 * this controller class handles the initialising and displaying of fields to the application window for the focus session.
 * it bases the majority of its outputs on the variables collected in a list from the initialise session page
 * sets up the focus session countdown and handles any pausing and resuming of the session
 * organises break tracking to maintain continous breaks per the users break variables from the init sess.
 * organises Appblocking and the use of the random and set motivational notifactions through Appblocking and notification class
 * utilises threading to run each functionality of the focus session at the same time
 */
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

    /**
     *this method initialises the new focus window with the necessary fields and sets up the app blocking/ break parameters if are selected
     */
    @FXML
    public void initialize() {
        //initiate session
        focusSession.startSession();
        //gets initialisation variables from init sess
        String[] data = new InitSessPageController().getInitSessList();
        focusSession.studyLength = focusSession.CalculateTime(data);
        focusSession.collectVariables(data);
        focusSession.setRandomMsgTime();
        start();
        //if app blocking selected, start its thread
        if (Objects.equals(focusSession.blockApp, "True")) {
            appBlockingRun = new AppBlockingRun();
            appBlockingRun.start();
        }

        if (Objects.equals(focusSession.breaks, "True")) {
            focusSession.calcBreakTime();
        }
        // construct window
        Platform.runLater(() -> {
            Stage stage = (Stage) endSess.getScene().getWindow();
            stage.setWidth(600);
            stage.setHeight(500);
            stage.setOnCloseRequest(event -> {
                handleEndSess(new ActionEvent(hiddenDashboardButton, null));
                event.consume();
            });
        });
    }

    /**
     * this method handles the running of the countdown of the session timer. checks for the session end.
     * also checks when the break should start if breaks are selected
     * manages when random msg's should trigger
     */
    @Override
    public void run() {
        focusSession.calculateEndtime();
        // while the session end time hasnt been reached
        while (System.currentTimeMillis() < focusSession.endTime && !focusSession.sessionEnded) {
            // and if the focus session isnt paused, update the values of the session
            if (!focusSession.isPaused) {
                focusSession.calculateProgress();
                Platform.runLater(() -> {
                    clock.setProgress(focusSession.progress);
                    timeText.setText(focusSession.displayTime(focusSession.timeLeft));
                });
                //if breaks was included by the user
                if (focusSession.breaks.equals("True")) {
                    //calculate the time until the break and if the time is passed it pauses the countdown
                    long timeUntilNextBreak = focusSession.nextBreakTime - System.currentTimeMillis();
                    if (timeUntilNextBreak <= 0) {
                        try {
                            // displays notification and moves to counting down the break
                            String msg = "Your " + (focusSession.breakLength / 60000) + " minute break starts now. Make sure to get up and stretch, relax and enjoy the break, you've earned this!";
                            focusSession.getBreakMsg(msg);
                        } catch (AWTException e) {
                            throw new RuntimeException(e);
                        }
                        takeBreak();
                    }
                }
                // if the random msg time is reached then get a randome msg from a list of MSG's and send the notification
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

    /**
     * This method handles the duration of a break and counts down the break in a new thread.
     * pauses the old timer and displays the new break timer with that counting down
     * utilises multiple break public variables to manage the break
     */
    public void takeBreak() {
        focusSession.breakCount++;
        focusSession.isPaused = true;
        focusSession.breakEndTime = System.currentTimeMillis() + focusSession.breakLength;
        //starts new thread for break
        new Thread(() -> {
            //while the break end time isnt reached
            while (System.currentTimeMillis() < focusSession.breakEndTime) {
                focusSession.breakTimeLeft = focusSession.breakEndTime - System.currentTimeMillis();
                Platform.runLater(() -> timeText.setText("Break: " + focusSession.displayTime(focusSession.breakTimeLeft)));

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //end use of break thread Thread
            Platform.runLater(() -> {
                //update the values for the next break and reallow notifications
                controlSess.setText("Pause");
                focusSession.isPaused = false;
                focusSession.calcBreakTime();
                focusSession.setRandomMsgTime();
                synchronized (FocusSessPageController.this) {
                    FocusSessPageController.this.notify();
                }
            });
        }).start();
    }

    // ends the session when called. is called when the window is clossed, the end session button event or the session timer ends
    @FXML
    private void handleEndSess(ActionEvent event) {
        focusSession.sessionEnded = true;
        interrupt();
        //stopping all threads ends counters
        if (appBlockingRun != null && appBlockingRun.isAlive()) {
            appBlockingRun.interrupt();
        }
        focusSession.endSession();
        //closes the window
        Platform.runLater(() -> {
            Stage stage = (Stage) endSess.getScene().getWindow();
            SessionManager.sessStatus = true; //re enables init sess
            //gets report data and stores it in db
            String date = java.time.LocalDate.now().toString();
            focusSession.getSessionData(date);
            //returns to dashboard
            hiddenDashboardButton.setId("Dashboard");
            hiddenDashboardButton.fireEvent(event);

            stage.close();
        });
    }

    //handles the pause/play functionality
    @FXML
    private void handleControlSess() {
        //if the button text is Pause then change it to resume and pause the application
        if ("Pause".equals(controlSess.getText())) {
            controlSess.setText("Resume");
            focusSession.isPaused = true;
            pauseStartTime = System.currentTimeMillis();
            if (appBlockingRun != null) {
                appBlockingRun.pauseBlocking();
            }
        } else {
            //change the text to pause and resume session
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

    //class for app blocking thread
    private class AppBlockingRun extends Thread {
        @Override
        public void run() {
            //while the session is counting down and not in break or paused
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

        /**
         *update pause status method for when the pause button is pressed or when a break starts
         */
        public synchronized void pauseBlocking() {
            focusSession.isPaused = true;
        }

        /**
         * method same as pauseblocking except the opposite
         */
        public synchronized void resumeBlocking() {
            focusSession.isPaused = false;
            //allow notifications again
            notify();
        }
    }
}

