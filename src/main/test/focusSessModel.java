import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.example.cab302project.focusSess.FocusSession;


public class focusSessModel {
    private FocusSession Focussess;
    private String[] data = new String[8];


    @BeforeEach
    public void SetUp() {
        Focussess = new FocusSession();
        data[0] = "1";
        data[1] = "1";
        data[2] = "30";
        data[3] = "True";
        data[4] = "False";
        data[5] = "True";
        data[6] = "6";
        data[7] = "5";
    }

    @Test
    public void TestCalculateTime() {
        long time = Focussess.CalculateTime(data);
        if (time == 5400000){}
    }

    @Test
    public void collectVarTest() {
        Focussess.collectVariables(data);
        if (Focussess.breakInterval == 360000 && Focussess.breakLength == 300000){}
    }

    @Test
    public void calcBreakTimeTest() {
        Focussess.calcBreakTime();
        if (Focussess.nextBreakTime == System.currentTimeMillis() + Focussess.breakInterval){}
    }

    @Test
    public void getDisplayTimeTest() {
        if (Focussess.displayTime(5305000) == "01:28:25"){}
    }


    @Test
    public void calculateEndTimeTest() {
        Focussess.studyLength = 5400000;
        long startTime = System.currentTimeMillis();
        Focussess.calculateEndtime();

        if (Focussess.studyLength + startTime == Focussess.endTime){}
    }

    @Test
    public void calculateProgressTest() {
        Focussess.calculateProgress();
        if (Focussess.displayTime(5305000) == "01:28:25"){}
    }

    @Test
    public void randMSGTimeTest() {
        long currentTime = System.currentTimeMillis();
        Focussess.setRandomMsgTime();
        long randTime = Focussess.nextRandomMsgTime;
        if (randTime > currentTime+60000 && randTime < currentTime+30000){}
    }


    @Test
    public void endSessTest() {
        long currentTime = System.currentTimeMillis();
        Focussess.endSession();
        if (Focussess.totalSessionTime == currentTime - Focussess.sessionStartTime){}
    }

}