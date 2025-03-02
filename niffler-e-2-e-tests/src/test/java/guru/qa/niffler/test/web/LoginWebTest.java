package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

public class LoginWebTest extends BaseWebTest {

    @User(
            categories = {
                    @Category(name = "Магазины", archived = false),
                    @Category(name = "Бары", archived = true)
            },
            spendings = {
                    @Spending(
                            category = "Обучение",
                            description = "QA.GURU Advanced 7",
                            amount = 80000
                    )
            }
    )
    @Test
    void mainPageShouldBeDisplayedAfterSuccessLogin(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
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
