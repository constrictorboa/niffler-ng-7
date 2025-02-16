package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.Test;

public class RegisterWebTest extends BaseWebTest {
    @Test
    void shouldRegisterNewUser() {
        String newUserName = RandomDataUtils.randomUsername();
        String newPassword = RandomDataUtils.randomPassword();

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
        String userName = RandomDataUtils.randomUsername();
        String password = RandomDataUtils.randomPassword();

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
