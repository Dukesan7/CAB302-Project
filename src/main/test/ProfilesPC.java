
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.example.cab302project.ProfilesPageController;

public class ProfilesPC {

    private ProfilesPageController testProfilePage;


    //need to enable constructor in ProfilesPageController
    @BeforeEach
    public void Setup() {
        testProfilePage = new ProfilesPageController("Dom", "oldpassword", "School",
                "What school did you first attend?");
    }

    @Test
    public void testChangePassword_correct() {
        if (testProfilePage.ChangePassword(testProfilePage.getPassword(), "newpw", "newpw")
                == "Password has successfully been changed!");
    }
    @Test
    public void testChangePassword_same() {
        if (testProfilePage.ChangePassword(testProfilePage.getPassword(), testProfilePage.getPassword(), testProfilePage.getPassword())
                == "Please enter a different password");
    }
    @Test
    public void testChangePassword_wrongConfirmation() {
        if (testProfilePage.ChangePassword(testProfilePage.getPassword(), "newpw", "incorrectnewpw")
                == "Ensure the confirmed password is correct");
    }

    @Test
    public void addProfile() {
        int count = testProfilePage.getProfileName().size();
        testProfilePage.AddNewProfile("NewProfile");
        if (count < testProfilePage.getProfileName().size());
    }

    @Test
    public void append_success() {
        if (testProfilePage.AppendStudyMode("NewMode") == "Study Mode NewMode Successfully added.");
    }

    @Test
    public void append_similarFail() {
        testProfilePage.AppendStudyMode("NewMode");
        if (testProfilePage.AppendStudyMode("NewMode") == "Please try a different name for the study mode.");
    }

    @Test
    public void append_noneAdded() {
        if (testProfilePage.AppendStudyMode(null) == "The Study Mode was unable to be added, please try again.");
    }
}
