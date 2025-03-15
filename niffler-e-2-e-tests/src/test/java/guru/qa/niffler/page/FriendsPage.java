package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.PeopleTable;
import guru.qa.niffler.page.component.SearchField;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class FriendsPage {
    private final PeopleTable friendsTable = new PeopleTable($("#friends"));
    private final PeopleTable requestsTable = new PeopleTable($("#requests"));
    private final SelenideElement allPeopleButton = $("a[href='/people/all']");
    private final SearchField searchField = new SearchField();
    private final PeopleTable peopleTable = new PeopleTable($("#all"));

    @Nonnull
    @Step("Открыть вкладку 'All people'")
    public FriendsPage clickOnAllFriendsButton() {
        allPeopleButton.click();
        return this;
    }

    @Nonnull
    @Step("Проверить, что таблица друзей содержит [{friendName}]")
    public FriendsPage checkThatFriendsTableContainsFriend(String friendName) {
        friendsTable.checkThatTableContains(friendName);
        return this;
    }

    @Nonnull
    @Step("Проверить, что есть запрос в друзья [{friendName}]")
    public FriendsPage checkThatRequestsTableContainsRequest(String friendName) {
        requestsTable
                .checkThatTableRowVisible(friendName);
        return this;
    }

    @Nonnull
    @Step("Проверить, что таблица пуста")
    public FriendsPage checkThatTableEmpty() {
        friendsTable.checkThatTableEmpty();
        requestsTable.checkThatTableEmpty();
        return this;
    }

    @Nonnull
    @Step("Проверить видимость кнопки 'Unfriend'")
    public FriendsPage checkThatUnfriendButtonVisible(String friendName) {
        friendsTable
                .checkThatUnfriendButtonVisible(friendName);
        return this;
    }

    @Nonnull
    @Step("Проверить видимость кнопки 'Accept'")
    public FriendsPage checkThatAcceptButtonVisible(String friendName) {
        requestsTable
                .checkThatAcceptButtonVisible(friendName);
        return this;
    }

    @Nonnull
    @Step("Проверить видимость кнопки 'Decline'")
    public FriendsPage checkThatDeclineButtonVisible(String friendName) {
        requestsTable
                .checkThatDeclineButtonVisible(friendName);
        return this;
    }

    @Nonnull
    @Step("Проверить видимость исходящего запроса в друзья [{friendName}]")
    public FriendsPage checkThatOutcomeRequestVisible(String friendName) {
        peopleTable.checkThatOutcomeRequestVisible(friendName);
        return this;
    }

    @Nonnull
    @Step("Принять заявку в друзья от [{friendName}]")
    public FriendsPage clickAcceptButton(String friendName) {
        requestsTable.clickAcceptButton(friendName);
        return this;
    }

    @Nonnull
    @Step("Отклонить заявку в друзья от [{friendName}]")
    public FriendsPage clickDeclineButton(String friendName) {
        requestsTable.clickDeclineButton(friendName);
        return this;
    }
}
