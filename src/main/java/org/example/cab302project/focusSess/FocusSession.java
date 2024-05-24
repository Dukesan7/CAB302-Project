package org.example.cab302project.focusSess;

import java.awt.*;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class FocusSession {
    public Boolean isPaused = false;
    public long breakInterval;
    public long breakLength;
    public long nextBreakTime;
    public long breakEndTime;
    public String blockApp;
    public String wallPaper;
    public String breaks;
    public long endTime;
    public long studyLength;
    public int timeLeft;
    public long currentTime;
    public long breakTimeLeft;
    public double progress;
    public long startTime;

    public long CalculateTime(String[] data) {
        int hours = Integer.parseInt(data[1]);
        int minutes = Integer.parseInt(data[2]);
        return TimeUnit.HOURS.toMillis(hours) + TimeUnit.MINUTES.toMillis(minutes);
    }

    public void collectVariables(String[] data) {
        blockApp = data[3];
        wallPaper = data[4];
        breaks = data[5];
        System.out.println("collect variables ran");
        System.out.println(breaks);
        System.out.println(wallPaper);
        System.out.println(blockApp);
        System.out.println(data[6]);
        if (breaks.equals("True")) {
            breakInterval = TimeUnit.MINUTES.toMillis(Integer.parseInt(data[6]));
            System.out.println("interval: " + breakInterval);
            breakLength = TimeUnit.MINUTES.toMillis(Integer.parseInt(data[7]));
            calcBreakTime();
        }
    }

    public void calcBreakTime() {
        nextBreakTime = System.currentTimeMillis() + breakInterval;
        System.out.println("Next break time set for: " + nextBreakTime + " (current time: " + System.currentTimeMillis() + ")");
    }

    public String displayTime(long time) {
        long Hours = TimeUnit.MILLISECONDS.toHours(time);
        long Minutes = TimeUnit.MILLISECONDS.toMinutes(time) % 60;
        long Seconds = TimeUnit.MILLISECONDS.toSeconds(time) % 60;
        return String.format("%02d:%02d:%02d", Hours, Minutes, Seconds);
    }

    public void calculateProgress() {
        currentTime = System.currentTimeMillis();
        timeLeft = (int) (endTime - currentTime);
        progress = (double) (studyLength - timeLeft) / studyLength;
    }

    public void calculateEndtime() {
        startTime = System.currentTimeMillis();
        endTime = studyLength + startTime;
        System.out.println("End time set for: " + endTime + " (current time: " + System.currentTimeMillis() + ")");
    }

    public void calculateBreakTimes() {
        breakEndTime = System.currentTimeMillis() + breakLength;
        breakTimeLeft = breakEndTime - System.currentTimeMillis();
    }

    public void getRandMsg() throws AWTException {
        String[] msgs = {
                "Don't forget to drink plenty of Water!",
                "Take a moment to breathe, you got this!",
                "Keep going you can do this!",
                "Make sure to have a snack every now and then, it will keep you energized!",
                "You got this!"
        };
        Random random = new Random();
        int randomMsg = random.nextInt(msgs.length);
        String msg = msgs[randomMsg];
        notification(msg);
    }

    public void getBreakMsg(String msg) throws AWTException {
        notification(msg);
    }

    public void notification(String msg) throws AWTException {
        Notification.notification(msg);
    }
}
