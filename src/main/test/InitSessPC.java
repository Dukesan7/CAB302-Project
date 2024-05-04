import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.example.cab302project.InitSessPageController;


public class InitSessPC {
    private InitSessPageController initSess;

    @BeforeEach
    public void SetUp() {
        initSess = new InitSessPageController();
    }

    @Test
    public void AddToGroups() {
        initSess.Groups.add("New Group");
        int count = initSess.Groups.size();
        if (initSess.Groups.get(count - 1) == "New Group" );
    }
    @Test
    public void AddToSubGroups() {
        initSess.SubGroupSchool.add("New SubGroup");
        int count = initSess.SubGroupSchool.size();
        if (initSess.SubGroupSchool.get(count - 1) == "New SubGroup" );
    }

}
