package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class MainPage {
    private final ElementsCollection tableRows = $("#spendings tbody").$$("tr");
    private final SelenideElement statisticsBlock = $("#stat");
    private final SelenideElement historyOfSpendingsBlock = $("#spendings");
    private final SelenideElement menuButton = $("button[aria-label='Menu']");
    private final SelenideElement profileButton = $("a[href='/profile']");
    private final SelenideElement friendsButton = $("a[href='/people/friends']");


    @Nonnull
    public EditSpendingPage editSpending(String spendingDescription) {
        tableRows.find(text(spendingDescription)).$$("td").get(5).click();
        return new EditSpendingPage();
    }

    public void checkThatTableContainsSpending(String spendingDescription) {
        tableRows.find(text(spendingDescription)).should(visible);
    }

    public void checkThatLoginSuccess() {
        statisticsBlock.shouldBe(visible);
        historyOfSpendingsBlock.shouldBe(visible);
    }

    @Nonnull
    public MainPage clickOnMenuButton() {
        menuButton.click();
        return this;
    }

    @Nonnull
    public FriendsPage clickOnFriendsButton() {
        friendsButton.click();
        return new FriendsPage();
    }

    public ProfilePage clickOnProfileButton() {
        profileButton.click();
        return new ProfilePage();
    }
}
