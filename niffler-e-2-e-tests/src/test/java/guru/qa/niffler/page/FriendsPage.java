package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class FriendsPage {
    private final ElementsCollection friendsTable = $("#friends").$$("tr");
    private final ElementsCollection requestsTable = $("#requests").$$("tr");
    private final ElementsCollection allPeopleTable = $("#all").$$("tr");
    private final SelenideElement allPeopleButton = $("a[href='/people/all']");
    private final SelenideElement searchField = $("input[placeholder='Search']");

    public FriendsPage clickOnAllFriendsButton() {
        allPeopleButton.click();
        return this;
    }

    @Nonnull
    public FriendsPage checkThatFriendsTableContainsFriend(String friendName) {
        searchField.setValue(friendName).pressEnter();
        friendsTable.find(text(friendName)).should(visible);
        return this;
    }

    @Nonnull
    public FriendsPage checkThatRequestsTableContainsRequest(String friendName) {
        searchField.setValue(friendName).pressEnter();
        requestsTable
                .find(text(friendName))
                .should(visible);
        return this;
    }

    @Nonnull
    public FriendsPage checkThatTableEmpty() {
        friendsTable.shouldHave(size(0));
        requestsTable.shouldHave(size(0));
        return this;
    }

    @Nonnull
    public FriendsPage checkThatUnfriendButtonVisible(String friendName) {
        friendsTable
                .find(text(friendName))
                .$x("..//parent::td/following-sibling::td//button[text()='Unfriend']")
                .shouldBe(visible);
        return this;
    }

    @Nonnull
    public FriendsPage checkThatAcceptButtonVisible(String friendName) {
        requestsTable
                .find(text(friendName))
                .$x("..//parent::td/following-sibling::td//button[text()='Accept']")
                .shouldBe(visible);
        return this;
    }

    @Nonnull
    public FriendsPage checkThatDeclineButtonVisible(String friendName) {
        requestsTable
                .find(text(friendName))
                .$x("..//parent::td/following-sibling::td//button[text()='Decline']")
                .shouldBe(visible);
        return this;
    }

    @Nonnull
    public FriendsPage checkThatOutcomeRequestVisible(String friendName) {
        allPeopleTable
                .find(text(friendName))
                .$x("..//parent::td/following-sibling::td")
                .find(byText("Waiting..."))
                .shouldBe(visible);
        return this;
    }
}
