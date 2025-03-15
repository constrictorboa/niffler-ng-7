package guru.qa.niffler.page.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import javax.annotation.Nonnull;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$$x;

public class PeopleTable {
    private final ElementsCollection peopleTable;
    private final SearchField searchField = new SearchField();
    private final SelenideElement declineButton = $$x("//button[text()='Decline']").get(1);

    public PeopleTable(SelenideElement self){
        this.peopleTable = self.$$("tr");
    }

    @Nonnull
    public PeopleTable checkThatOutcomeRequestVisible(String friendName) {
       peopleTable
                .find(text(friendName))
                .$x("..//parent::td/following-sibling::td")
                .find(byText("Waiting..."))
                .shouldBe(visible);
        return this;
    }

    @Nonnull
    public PeopleTable checkThatTableContains(String friendName) {
        searchField.search(friendName);
        peopleTable.find(text(friendName)).should(visible);
        return this;
    }

    @Nonnull
    public PeopleTable checkThatTableEmpty() {
        peopleTable.shouldHave(size(0));
        return this;
    }

    @Nonnull
    public PeopleTable checkThatTableRowVisible(String friendName) {
        searchField.search(friendName);
        peopleTable
                .find(text(friendName))
                .should(visible);
        return this;
    }

    @Nonnull
    public PeopleTable checkThatUnfriendButtonVisible(String friendName) {
        peopleTable
                .find(text(friendName))
                .$x("..//parent::td/following-sibling::td//button[text()='Unfriend']")
                .shouldBe(visible);
        return this;
    }

    @Nonnull
    public PeopleTable checkThatAcceptButtonVisible(String friendName) {
        peopleTable
                .find(text(friendName))
                .$x("..//parent::td/following-sibling::td//button[text()='Accept']")
                .shouldBe(visible);
        return this;
    }

    @Nonnull
    public PeopleTable checkThatDeclineButtonVisible(String friendName) {
        peopleTable
                .find(text(friendName))
                .$x("..//parent::td/following-sibling::td//button[text()='Decline']")
                .shouldBe(visible);
        return this;
    }

    @Nonnull
    public PeopleTable clickAcceptButton(String friendName) {
        peopleTable
                .find(text(friendName))
                .$x("..//parent::td/following-sibling::td//button[text()='Accept']")
                .click();
        return this;
    }

    @Nonnull
    public PeopleTable clickDeclineButton(String friendName) {
        peopleTable
                .find(text(friendName))
                .$x("..//parent::td/following-sibling::td//button[text()='Decline']")
                .click();
        declineButton.click();
        return this;
    }
}
