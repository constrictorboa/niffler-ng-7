package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

public class LoginWebTest extends BaseWebTest {
    @Test
    void mainPageShouldBeDisplayedAfterSuccessLogin() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("duck", "12345")
                .checkThatLoginSuccess();
    }

    @Test
    void userShouldStayOnLoginPageAfterLoginWithBadCredentials() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("duck", "bad_password");
        new LoginPage()
                .checkThatLoginFormVisible()
                .checkThatErrorBadCredentialsVisible();
    }
}
