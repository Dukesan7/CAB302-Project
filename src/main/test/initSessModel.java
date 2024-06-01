import org.example.cab302project.Controller.ProfilesPageController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.example.cab302project.focusSess.initialiseSess;

public class initSessModel {

    private initialiseSess testinitSessMod;

    @BeforeEach
    public void Setup() {
        testinitSessMod = new ProfilesPageController("Dom", "oldpassword", "School",
                "What school did you first attend?");
    }
}