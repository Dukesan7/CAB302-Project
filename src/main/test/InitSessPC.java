import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.example.cab302project.Controller.InitSessPageController;


public class InitSessPC {
    private InitSessPageController initSessPC;

    @BeforeEach
    public void SetUp() {
        initSessPC = new InitSessPageController();
    }

    @Test
    public void AddToSubGroups() {
        populateSubGroup.add("New SubGroup");
        int count = initSessPC.SubGroup.size();
        if (initSess.SubGroupSchool.get(count - 1) == "New SubGroup" );
    }

}
