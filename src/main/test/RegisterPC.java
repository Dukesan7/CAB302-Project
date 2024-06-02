import org.example.cab302project.RegisterPageController;
import javafx.scene.control.Alert;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RegisterPC {

    @Test
    public void testIsValidEmail_ValidEmail() {
        RegisterPageController registerPageController = new RegisterPageController();
        assertTrue(registerPageController.isValidEmail("validEmail@gmail.com"));
    }

    @Test
    public void testIsValidEmail_InvalidEmail() {
        RegisterPageController registerPageController = new RegisterPageController();
        assertFalse(registerPageController.isValidEmail("invalidEmail@gmail"));
    }

    @Test
    public void testRegisterUserInDatabase_Success() {
        RegisterPageController registerPageController = new RegisterPageController();
        assertTrue(registerPageController.registerUserInDatabase("Kayden", "Fisher", "kaydenfisher@gmail.com", "Password.123"));
    }

    @Test
    public void testRegisterUserInDatabase_Failure() {
        RegisterPageController registerPageController = new RegisterPageController();
        assertFalse(registerPageController.registerUserInDatabase("", "", "", ""));
    }

    @Test
    public void testHashString() {
        RegisterPageController registerPageController = new RegisterPageController();
        String hashed = registerPageController.hashString("password123");
        String expectedHash = "ef92b778bafe771e89245b89ecbc51b4557b5eeea22816b47cd2c77b7f86a9da";
        assertEquals(expectedHash, hashed);
    }

    @Test
    public void testShowAlert() {
        RegisterPageController registerPageController = new RegisterPageController();
        assertDoesNotThrow(() -> registerPageController.showAlert(Alert.AlertType.INFORMATION, "Test Title", "Test Message"));
    }
}
