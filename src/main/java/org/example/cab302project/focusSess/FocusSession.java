package org.example.cab302project.focusSess;
//imports
import org.example.cab302project.DbConnection;
import org.example.cab302project.SessionManager;
import java.awt.*;
import java.util.concurrent.TimeUnit;
import java.util.Random;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import static org.example.cab302project.LoginPageController.userID;

/**
 * This class handles much of the calculations and sql for the focus sessions operation.
 * utilises multiple variables determined through the classes calculation methods for breaks and session timer
 */
public class FocusSession {
    //public variables initialised for cross class use
    public Boolean isPaused = false;
    public long breakInterval;
    public long breakLength;
    public long nextBreakTime;
    public long breakEndTime;
    public String blockApp;
    public String breaks;
    public long endTime;
    public long studyLength;
    public int timeLeft;
    public long currentTime;
    public long breakTimeLeft;
    public double progress;
    public long startTime;
    public long nextRandomMsgTime;
    public boolean sessionEnded = false;
    public int breakCount = 0;
    public long sessionStartTime;
    public long totalSessionTime;
    public int subGroupID;

    /**
     *This method gets the selected hours and minutes of the session initialisation and returns them combines in milliseconds
     * @param data is the array list of variables set in the init session form
     * @return returns the time in a long format
     */
    public long CalculateTime(String[] data) {
        int hours = Integer.parseInt(data[1]);
        int minutes = Integer.parseInt(data[2]);
        return TimeUnit.HOURS.toMillis(hours) + TimeUnit.MINUTES.toMillis(minutes);
    }

    /**
     *the collect variables method retrieves the sess list and gets the subgroupID and bool of if apps are to be blocked and if breaks are to occur
     * sets break interval and break length if breaks are requested
     * @param data contains the array list of the init sess variables
     */
    public void collectVariables(String[] data) {
        blockApp = data[3];
        subGroupID = Integer.parseInt(data[0]);
        breaks = data[5];
        if (breaks.equals("True")) {
            breakInterval = TimeUnit.MINUTES.toMillis(Integer.parseInt(data[6]));
            breakLength = TimeUnit.MINUTES.toMillis(Integer.parseInt(data[7]));
            calcBreakTime();
        }
    }

    /**
     *method that calculates the next break time. sets public variable for calling
     */
    public void calcBreakTime() {
        nextBreakTime = System.currentTimeMillis() + breakInterval;
    }

    /**
     *display time calculates the v
     * @param time
     * @return
     */
    public String displayTime(long time) {
        long Hours = TimeUnit.MILLISECONDS.toHours(time);
        long Minutes = TimeUnit.MILLISECONDS.toMinutes(time) % 60;
        long Seconds = TimeUnit.MILLISECONDS.toSeconds(time) % 60;
        return String.format("%02d:%02d:%02d", Hours, Minutes, Seconds);
    }

    /**
     *
     */
    public void calculateProgress() {
        currentTime = System.currentTimeMillis();
        timeLeft = (int) (endTime - currentTime);
        progress = (double) (studyLength - timeLeft) / studyLength;
    }

    /**
     *
     */
    public void calculateEndtime() {
        startTime = System.currentTimeMillis();
        endTime = studyLength + startTime;
    }

    /**
     *
     */
    public void setRandomMsgTime() {
        long minTime = TimeUnit.MINUTES.toMillis(1);
        long maxTime = 300000 - minTime;
        nextRandomMsgTime = System.currentTimeMillis() + (minTime + (long) (Math.random() * (maxTime - minTime)));
    }

    /**
     *
     * @throws AWTException
     */
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

    /**
     *
     * @param msg
     * @throws AWTException
     */
    public void getBreakMsg(String msg) throws AWTException {
        notification(msg);
    }

    /**
     *
     * @param msg
     * @throws AWTException
     */
    public void notification(String msg) throws AWTException {
        Notification.notification(msg);
    }

    /**
     *
     */
    public void startSession() {
        sessionStartTime = System.currentTimeMillis();
        sessionEnded = false;
    }

    /**
     *
     */
    public void endSession() {
        totalSessionTime = System.currentTimeMillis() - sessionStartTime;
        sessionEnded = true;
    }

    /**
     *
     * @param date
     */
    public void getSessionData(String date) {

        Integer totalSessionTimeMins = (int) totalSessionTime / 1000;

        insertSessionData(totalSessionTimeMins, date);
    }

    /**
     *
     * @param totalSessionTime
     * @param date
     */
    public void insertSessionData(int totalSessionTime, String date) {
        String sqlInsertReport = "INSERT INTO Reports (totalTime, numberOfBreaks, lengthOfBreaks, date, userID, groupID, subGroupID) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            Connection connection = DbConnection.getInstance().getConnection();
            try (PreparedStatement pstmt = connection.prepareStatement(sqlInsertReport)) {
                pstmt.setInt(1, totalSessionTime);
                pstmt.setInt(2, breakCount);
                pstmt.setLong(3, breakLength / 60000);
                pstmt.setString(4, date);
                pstmt.setInt(5, userID);
                pstmt.setInt(6, SessionManager.currentGroupID);
                pstmt.setInt(7, subGroupID);

                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }




}
