import org.example.cab302project.Controller.DashboardPageController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DashBoardPC {
    private DashboardPageController dash;
    @BeforeEach
    public void SetUp() {
        dash = new DashboardPageController();
    }

    @Test
    public void testTimeConversion() {

        if (dash.timeConversion( 10) == "10 Seconds" && dash.timeConversion( 150) == "2.5 Minutes" && dash.timeConversion( 7200) == "2 Hours"){}
    }
}
