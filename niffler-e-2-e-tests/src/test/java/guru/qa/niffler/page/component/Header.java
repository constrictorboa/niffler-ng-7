package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.EditSpendingPage;
import guru.qa.niffler.page.FriendsPage;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.PeoplePage;
import guru.qa.niffler.page.ProfilePage;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

@ParametersAreNonnullByDefault
public class Header {
    private final SelenideElement menuButton = $("button[aria-label='Menu']");
    private final SelenideElement profileButton = $("a[href='/profile']");
    private final SelenideElement friendsButton = $("a[href='/people/friends']");
    private final SelenideElement allPeopleButton = $("a[href='/people/all']");
    private final SelenideElement addSpendingButton = $("a[href='/spending']");
    private final SelenideElement homeButton = $("a[href='/main']");
    private final SelenideElement signOutButton  = $x("//li[text()='Sign out']");
    private final SelenideElement logOutButton = $x("//button[text()='Log out']");


    @Nonnull
    @Step("Перейти на страницу Friends")
    public FriendsPage toFriendsPage() {
        menuButton.click();
        friendsButton.click();
        return new FriendsPage();
    }

    @Nonnull
    @Step("Перейти на страницу All people")
    public PeoplePage toPeoplePage() {
        menuButton.click();
        allPeopleButton.click();
        return new PeoplePage();
    }

    @Nonnull
    @Step("Перейти на страницу Profile")
    public ProfilePage toProfilePage() {
        menuButton.click();
        profileButton.click();
        return new ProfilePage();
    }

    @Nonnull
    @Step("Разлогиниться")
    public LoginPage signOut() {
        menuButton.click();
        signOutButton.click();
        logOutButton.click();
        return new LoginPage();
    }

    @Nonnull
    @Step("Перейти на страницу New spending")
    public EditSpendingPage addSpendingPage() {
        addSpendingButton.click();
        return new EditSpendingPage();
    }

    @Nonnull
    @Step("Перейти на главную страницу")
    public MainPage toMainPage(){
        homeButton.click();
        return new MainPage();
    }
}
