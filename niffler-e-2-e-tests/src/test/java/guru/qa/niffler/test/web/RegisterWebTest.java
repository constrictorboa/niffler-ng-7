package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;

import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

import java.util.Random;

public class RegisterWebTest extends BaseWebTest {
    @Test
    void shouldRegisterNewUser() {
        String newUserName = "newUser" + System.currentTimeMillis();
        String newPassword = "pass!1";

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickOnRegisterButton()
                .setUsername(newUserName)
                .setPassword(newPassword)
                .setPasswordSubmit(newPassword)
                .clickOnSubmitButton()
                .checkThatSuccessRegistrationFormVisible()
                .clickOnLoginButton()
                .login(newUserName, newPassword)
                .checkThatLoginSuccess();
    }

    @Test
    void shouldNotRegisterUserWithExistingUsername() {
        String userName = "duck";
        String password = "pass!1";

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickOnRegisterButton()
                .setUsername(userName)
                .setPassword(password)
                .setPasswordSubmit(password)
                .clickOnSubmitButton()
                .checkThatRegisterFormVisible()
                .checkThatErrorUsernameExistsVisible(userName);
    }

    @Test
    void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual() {
        String userName = "newUser" + System.currentTimeMillis() + new Random().nextInt(1, 100);
        String password = "pass!1";

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickOnRegisterButton()
                .setUsername(userName)
                .setPassword(password)
                .setPasswordSubmit(password + "1")
                .clickOnSubmitButton()
                .checkThatRegisterFormVisible()
                .checkThatErrorPaaswordsShouldBeEqualVisible();
    }
}
