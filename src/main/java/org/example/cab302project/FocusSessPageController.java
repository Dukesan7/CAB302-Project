package org.example.cab302project;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.text.Text;

import java.util.concurrent.TimeUnit;

import org.example.cab302project.AppBlocking;
public class FocusSessPageController extends java.lang.Thread {

    long studyLength;

    @FXML
    ProgressIndicator clock;

    @FXML
    Text timeText;

    @FXML
    public void initialize() {
        // Optional: Any initializations for your controller
        String[] data = new InitSessPageController().getInitSessList();
        studyLength = CalculateTime(data);
        start();

        AppBlocking appBlocking = new AppBlocking();
        appBlocking.start();
    }

    @Override
    public void run(){
        long startTime =System.currentTimeMillis();
        long endTime = studyLength + startTime;

        while (System.currentTimeMillis() < endTime){
            long currentTime = System.currentTimeMillis();
            int timeLeft = (int)(endTime - currentTime);

            double progress = 1 - (double) timeLeft / studyLength;
            clock.setProgress(progress);

            long hours = TimeUnit.MILLISECONDS.toHours(timeLeft);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(timeLeft) % 60;
            long seconds = TimeUnit.MILLISECONDS.toSeconds(timeLeft) % 60;
            String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
            timeText.setText(timeString);

            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        timeText.setText("00:00:00");
        clock.setProgress(1);
    }


    private long CalculateTime(String[] data){
        int hours = Integer.parseInt(data[2]);
        int minutes  = Integer.parseInt(data[3]);
        return TimeUnit.HOURS.toMillis(hours) + TimeUnit.MINUTES.toMillis(minutes);
    }

    // Inner class for a second thread
    private class AppBlocking extends Thread {
        @Override
        public void run() {
            //Alex, put your app blocking here instead of the other run because it sleeps constantly
        }
    }
}
