package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class FriendsPage {
    private final ElementsCollection friendsTable = $("#friends").$$("tr");
    private final ElementsCollection requestsTable = $("#requests").$$("tr");
    private final ElementsCollection allPeopleTable = $("#all").$$("tr");
    private final SelenideElement allPeopleButton = $("a[href='/people/all']");

    public FriendsPage clickOnAllFriendsButton() {
        allPeopleButton.click();
        return this;
    }

    public FriendsPage checkThatFriendsTableContainsFriend(String friendName) {
        friendsTable.find(text(friendName)).should(visible);
        return this;
    }

    public FriendsPage checkThatRequestsTableContainsRequest(String friendName) {
        requestsTable
                .find(text(friendName))
                .should(visible);
        return this;
    }

    public FriendsPage checkThatTableEmpty() {
        friendsTable.shouldHave(size(0));
        requestsTable.shouldHave(size(0));
        return this;
    }

    public FriendsPage checkThatUnfriendButtonVisible(String friendName) {
        friendsTable
                .find(text(friendName))
                .$x("..//parent::td/following-sibling::td//button[text()='Unfriend']")
                .shouldBe(visible);
        return this;
    }

    public FriendsPage checkThatAcceptButtonVisible(String friendName) {
        requestsTable
                .find(text(friendName))
                .$x("..//parent::td/following-sibling::td//button[text()='Accept']")
                .shouldBe(visible);
        return this;
    }

    public FriendsPage checkThatDeclineButtonVisible(String friendName) {
        requestsTable
                .find(text(friendName))
                .$x("..//parent::td/following-sibling::td//button[text()='Decline']")
                .shouldBe(visible);
        return this;
    }

    public FriendsPage checkThatOutcomeRequestVisible(String friendName) {
        allPeopleTable
                .find(text(friendName))
                .$x("..//parent::td/following-sibling::td")
                .find(byText("Waiting..."))
                .shouldBe(visible);
        return this;
    }
}
