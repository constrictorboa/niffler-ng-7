package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class RegisterPage {
    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement passwordSubmitInput = $("input[name='passwordSubmit']");
    private final SelenideElement submitButton = $("button[type='submit']");
    private final SelenideElement loginButton = $("a[class='form_sign-in']");

    @Nonnull
    @Step("Ввести логин [{username}]")
    public RegisterPage setUsername(String username) {
        usernameInput.sendKeys(username);
        return this;
    }

    @Nonnull
    @Step("Ввести пароль")
    public RegisterPage setPassword(String password) {
        passwordInput.sendKeys(password);
        return this;
    }

    @Nonnull
    @Step("Ввести повторно пароль")
    public RegisterPage setPasswordSubmit(String password) {
        passwordSubmitInput.sendKeys(password);
        return this;
    }

    @Nonnull
    @Step("Нажать кнопку подтверждения")
    public RegisterPage clickOnSubmitButton() {
        submitButton.click();
        return this;
    }

    @Nonnull
    @Step("Залогиниться")
    public LoginPage clickOnLoginButton() {
        loginButton.click();
        return new LoginPage();
    }

    @Nonnull
    @Step("Проверить уведомление об успешной регистрации")
    public RegisterPage checkThatSuccessRegistrationFormVisible() {
        loginButton.shouldBe(visible);
        $(byText("Congratulations! You've registered!")).shouldBe(visible);
        return this;
    }

    @Nonnull
    @Step("Проверить отображение ошибки, что пользователь существует")
    public RegisterPage checkThatErrorUsernameExistsVisible(String username) {
        $(byText("Username `" + username + "` already exists")).shouldBe(visible);
        return this;
    }

    @Nonnull
    @Step("Проверить отображени ошибки, что пароли не совпадают")
    public RegisterPage checkThatErrorPasswordsShouldBeEqualVisible() {
        $(byText("Passwords should be equal")).shouldBe(visible);
        return this;
    }

    @Nonnull
    @Step("Проверить отображение формы регистрации")
    public RegisterPage checkThatRegisterFormVisible() {
        usernameInput.shouldBe(visible);
        passwordInput.shouldBe(visible);
        passwordSubmitInput.shouldBe(visible);
        submitButton.shouldBe(visible);
        return this;
    }

}
