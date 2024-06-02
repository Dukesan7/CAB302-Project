import org.example.cab302project.LoginPageController;
import javafx.scene.control.Alert;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoginPC {

    @Test
    public void testVerifyUserCredentialsValid() {
        LoginPageController loginPageController = new LoginPageController();
        assertTrue(loginPageController.verifyUserCredentials("validEmail@gmail.com", "validPassword"));
    }

    @Test
    public void testVerifyUserCredentialsInvalid() {
        LoginPageController loginPageController = new LoginPageController();
        assertFalse(loginPageController.verifyUserCredentials("invalidEmail@gmail.com", "invalidPassword"));
    }

    @Test
    public void testHashString() {
        LoginPageController loginPageController = new LoginPageController();
        String hashed = loginPageController.hashString("password123");
        String expectedHash = "ef92b778bafe771e89245b89ecbc51b4557b5eeea22816b47cd2c77b7f86a9da";
        assertEquals(expectedHash, hashed);
    }

    @Test
    public void testShowAlert() {
        LoginPageController loginPageController = new LoginPageController();
        assertDoesNotThrow(() -> loginPageController.showAlert(Alert.AlertType.INFORMATION, "Test Title", "Test Message"));
    }
}
