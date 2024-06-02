import javafx.scene.control.CheckBox;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.example.cab302project.focusSess.initialiseSess;

public class initSessModel {
    int slidervaltest = 5;
    private initialiseSess testinitSessMod;
    @BeforeEach
    public void Setup() {
        testinitSessMod = new initialiseSess();
    }
    @Test
    public void CalculateTotalMin() {
        testinitSessMod.calculateTotalMin("1", "30" );
        if (testinitSessMod.totalMinutes == 90){}
    }
    @Test
    public void breakIntervalCheck() {

        if (testinitSessMod.breakInterval(slidervaltest) == String.valueOf((testinitSessMod.totalMinutes / slidervaltest))){}
    }
    @Test
    public void testvalueCheckingOfCheckBoxes() {
        CheckBox checkBox = new CheckBox();
        checkBox.setSelected(false);
        if (testinitSessMod.checkValue(checkBox) != "True") {
        }
    }
    @Test
    public void testIfTimeNull() {
        String time = null;
        if (testinitSessMod.deNullifyTime(time) == "0") {
        }
    }
    @Test
    public void testBreakValues() {
        int expectedBreakValue = testinitSessMod.totalMinutes / slidervaltest;
        testinitSessMod.calculateBreakValues(slidervaltest);
        if (testinitSessMod.breakInterval == expectedBreakValue) {
        }
    }
}