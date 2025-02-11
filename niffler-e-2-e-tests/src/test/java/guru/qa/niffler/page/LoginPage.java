package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.clickable;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
  private final SelenideElement usernameInput = $("input[name='username']");
  private final SelenideElement passwordInput = $("input[name='password']");
  private final SelenideElement submitButton = $("button[type='submit']");
  private final SelenideElement registerButton = $("a[href='/register']");

  public MainPage login(String username, String password) {
    usernameInput.setValue(username);
    passwordInput.setValue(password);
    submitButton.click();
    return new MainPage();
  }

  public RegisterPage clickOnRegisterButton() {
    registerButton.click();
    return new RegisterPage();
  }

  public LoginPage checkThatLoginFormVisible(){
    usernameInput.shouldBe(visible);
    passwordInput.shouldBe(visible);
    submitButton.shouldBe(clickable);
    registerButton.shouldBe(visible);
    return this;
  }

  public LoginPage checkThatErrorBadCredentialsVisible(){
    $(byText("Неверные учетные данные пользователя")).shouldBe(visible);
    return this;
  }
}
