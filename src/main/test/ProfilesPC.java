
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.example.cab302project.ProfilesPageController;

public class ProfilesPC {

    private ProfilesPageController profile;
    @BeforeEach
    public void Setup() {
        profile = new ProfilesPageController("Dom", "oldpassword", "School",
                "What school did you first attend?");
    }

    @Test
    public void testChangePassword_correct() {
        if (profile.ChangePassword(profile.getPassword(), "newpw", "newpw")
                == "Password has successfully been changed!");
    }
    @Test
    public void testChangePassword_same() {
        if (profile.ChangePassword(profile.getPassword(), profile.getPassword(), profile.getPassword())
                == "Please enter a different password");
    }
    @Test
    public void testChangePassword_wrongConfirmation() {
        if (profile.ChangePassword(profile.getPassword(), "newpw", "incorrectnewpw")
                == "Ensure the confirmed password is correct");
    }

    @Test
    public void addProfile() {
        int count = profile.getProfileName().size();
        profile.AddNewProfile("NewProfile");
        if (count < profile.getProfileName().size());
    }

    @Test
    public void append_success() {
        if (profile.AppendStudyMode("NewMode") == "Study Mode NewMode Successfully added.");
    }

    @Test
    public void append_similarFail() {
        profile.AppendStudyMode("NewMode");
        if (profile.AppendStudyMode("NewMode") == "Please try a different name for the study mode.");
    }

    @Test
    public void append_noneAdded() {
        if (profile.AppendStudyMode(null) == "The Study Mode was unable to be added, please try again.");
    }
}
