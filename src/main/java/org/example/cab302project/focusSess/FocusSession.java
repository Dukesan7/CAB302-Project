package org.example.cab302project.focusSess;

import java.awt.*;
import java.util.Objects;
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

    public long CalculateTime(String[] data) {
        int hours = Integer.parseInt(data[1]);
        int minutes = Integer.parseInt(data[2]);
        return TimeUnit.HOURS.toMillis(hours) + TimeUnit.MINUTES.toMillis(minutes);
    }

    public void collectVariables(String[] data) {
        blockApp = data[3];
        wallPaper = data[4];
        breaks = data[5];
        if (Objects.equals(breaks, "true")) {
            breakInterval = TimeUnit.MINUTES.toMillis(Integer.parseInt(data[5]));
            breakLength = TimeUnit.MINUTES.toMillis(Integer.parseInt(data[6]));
            calcBreakTime();
        }
    }

    public void calcBreakTime() {
        nextBreakTime = System.currentTimeMillis() + breakInterval;
    }

    public String displayTime(long time) {

        long Hours = TimeUnit.MILLISECONDS.toHours(time);
        long Minutes = TimeUnit.MILLISECONDS.toMinutes(time) % 60;
        long Seconds = TimeUnit.MILLISECONDS.toSeconds(time) % 60;

        return String.format("%02d:%02d:%02d", Hours, Minutes, Seconds);
    }

    public double calculateProgress() {
        currentTime = System.currentTimeMillis();
        timeLeft = (int) (endTime - (long) System.currentTimeMillis());

        return 1 - (double) timeLeft / studyLength;
    }

    public long calculateEndtime() {
        long startTime = System.currentTimeMillis();
        endTime = studyLength + startTime;
        return endTime;
    }

    public void calculateBreakTimes() {
        breakEndTime = System.currentTimeMillis() + breakLength;
        breakTimeLeft = breakEndTime - System.currentTimeMillis();
    }

    public void notification() throws AWTException {

        String[] msgs = {
                "Don't forget to drink plenty of Water!",
                "Take a moment to breathe, you got this!",
                "Keep going you can do this!",
                "Make sure to have a snack every now and then, it will keep you energized!",
                "You got this!"
        };
        //Notification.notification(msgs);
    }
}
