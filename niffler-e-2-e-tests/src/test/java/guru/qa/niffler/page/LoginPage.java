package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.clickable;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class LoginPage {
  private final SelenideElement usernameInput = $("input[name='username']");
  private final SelenideElement passwordInput = $("input[name='password']");
  private final SelenideElement submitButton = $("button[type='submit']");
  private final SelenideElement registerButton = $("a[href='/register']");

  @Nonnull
  public MainPage login(String username, String password) {
    usernameInput.setValue(username);
    passwordInput.setValue(password);
    submitButton.click();
    return new MainPage();
  }

  @Nonnull
  public RegisterPage clickOnRegisterButton() {
    registerButton.click();
    return new RegisterPage();
  }

  @Nonnull
  public LoginPage checkThatLoginFormVisible(){
    usernameInput.shouldBe(visible);
    passwordInput.shouldBe(visible);
    submitButton.shouldBe(clickable);
    registerButton.shouldBe(visible);
    return this;
  }

  @Nonnull
  public LoginPage checkThatErrorBadCredentialsVisible(){
    $(byText("Неверные учетные данные пользователя")).shouldBe(visible);
    return this;
  }
}
