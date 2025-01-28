package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {
    private final ElementsCollection tableRows = $("#spendings tbody").$$("tr");
    private final SelenideElement statisticsBlock = $("#stat");
    private final SelenideElement historyOfSpendingsBlock = $("#spendings");
    private final SelenideElement menuButton = $("button[aria-label='Menu']");
    private final SelenideElement profileButton = $("a[href='/profile']");
    private final SelenideElement header = $("#root header");
    private final SelenideElement headerMenu = $("ul[role='menu']");
    private final SelenideElement statComponent = $("#stat");
    private final SelenideElement spendingTable = $("#spendings");


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

    public MainPage menuButtonClick() {
        menuButton.click();
        return this;
    }

    public ProfilePage profileButtonClick() {
        profileButton.click();
        return new ProfilePage();
    }

    public FriendsPage friendsPage() {
        header.$("button").click();
        headerMenu.$$("li").find(text("Friends")).click();
        return new FriendsPage();
    }

    public PeoplePage allPeoplesPage() {
        header.$("button").click();
        headerMenu.$$("li").find(text("All People")).click();
        return new PeoplePage();
    }

    public MainPage checkThatPageLoaded() {
        statComponent.should(visible).shouldHave(text("Statistics"));
        spendingTable.should(visible).shouldHave(text("History of Spendings"));
        return this;
    }
}
