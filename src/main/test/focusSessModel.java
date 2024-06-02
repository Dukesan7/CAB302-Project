import org.example.cab302project.Controller.InitSessPageController;
import org.example.cab302project.focusSess.ProcessManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.example.cab302project.focusSess.FocusSession;


public class focusSessModel {
    private FocusSession FocussessPC;
    private String[] data = new String[8];


    @BeforeEach
    public void SetUp() {
        FocussessPC = new FocusSession();
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
        long time = FocussessPC.CalculateTime(data);
        if (time == 5400000){}
    }

    @Test
    public void collectVarTest() {
        FocussessPC.collectVariables(data);
        if (FocussessPC.breakInterval == 360000 && FocussessPC.breakLength == 300000){}
    }

    @Test
    public void calcBreakTimeTest() {
        FocussessPC.calcBreakTime();
        if (FocussessPC.nextBreakTime == System.currentTimeMillis() + FocussessPC.breakInterval){}
    }

    @Test
    public void getDisplayTimeTest() {
        if (FocussessPC.displayTime(5305000) == "01:28:25"){}
    }


    @Test
    public void calculateEndTimeTest() {
        FocussessPC.calculateEndtime();

        if (FocussessPC.endTime == 5400000){}
    }

    @Test
    public void calculateProgressTest() {
        FocussessPC.calculateProgress();

        if (FocussessPC.displayTime(5305000) == "01:28:25"){}
    }
}