package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class RegisterPage {
    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement passwordSubmitInput = $("input[name='passwordSubmit']");
    private final SelenideElement submitButton = $("button[type='submit']");
    private final SelenideElement loginButton = $("a[class='form_sign-in']");

    public RegisterPage setUsername(String username) {
        usernameInput.sendKeys(username);
        return this;
    }

    public RegisterPage setPassword(String password) {
        passwordInput.sendKeys(password);
        return this;
    }

    public RegisterPage setPasswordSubmit(String password) {
        passwordSubmitInput.sendKeys(password);
        return this;
    }

    public RegisterPage clickOnSubmitButton() {
        submitButton.click();
        return this;
    }

    public LoginPage clickOnLoginButton() {
        loginButton.click();
        return new LoginPage();
    }

    public RegisterPage checkThatSuccessRegistrationFormVisible() {
        loginButton.shouldBe(visible);
        $(byText("Congratulations! You've registered!")).shouldBe(visible);
        return this;
    }

    public RegisterPage checkThatErrorUsernameExistsVisible(String username) {
        $(byText("Username `" + username + "` already exists")).shouldBe(visible);
        return this;
    }

    public RegisterPage checkThatErrorPaaswordsShouldBeEqualVisible() {
        $(byText("Passwords should be equal")).shouldBe(visible);
        return this;
    }

    public RegisterPage checkThatRegisterFormVisible() {
        usernameInput.shouldBe(visible);
        passwordInput.shouldBe(visible);
        passwordSubmitInput.shouldBe(visible);
        submitButton.shouldBe(visible);
        return this;
    }

}
