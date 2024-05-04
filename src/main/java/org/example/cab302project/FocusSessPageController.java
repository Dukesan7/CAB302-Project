package org.example.cab302project;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ProgressIndicator;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class FocusSessPageController extends java.lang.Thread {

    InitSessPageController initSessPageController = new InitSessPageController();
    String[] data;
    long studyLength;

    @FXML
    ProgressIndicator clock;

    @FXML
    public void initialize() {
        // Optional: Any initializations for your controller
        data = GetData();
        studyLength = CalculateTime(data);
        System.out.println(studyLength);
        start();
    }

    @Override
    public void run(){
        int i = 0;
        long startTime =System.currentTimeMillis();
        long endTime = studyLength + startTime;
        UpdateTimer(endTime, startTime);
        System.out.println("Start: " + new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new java.util.Date (startTime)));
        System.out.println("End: " +new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new java.util.Date (System.currentTimeMillis())));
    }

    private void UpdateTimer(long endTime, long startTime){
        double normalized = 0;
        while (normalized < 1){
            long currentTime = System.currentTimeMillis();
            int timeLeft = (int)(endTime - currentTime);

            normalized =1 - (double) timeLeft / studyLength;
            clock.setProgress(normalized);
            try{
                sleep(10L);
            }catch (InterruptedException e){
                System.out.println(e.getMessage());
            }

        }
    }

    private String[] GetData(){

        return initSessPageController.getInitSessList();
    }

    private long CalculateTime(String[] data){
        int hours = Integer.parseInt(data[2]);
        int mins = Integer.parseInt(data[3]);
        long timeInMillis = ((hours * 60L) + mins) * 60000;

        return timeInMillis;
    }
}
