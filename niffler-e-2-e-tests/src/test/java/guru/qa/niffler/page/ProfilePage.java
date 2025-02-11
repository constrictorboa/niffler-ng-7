package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class ProfilePage {
    private final SelenideElement showArchivedRadiobutton = $(byText("Show archived")).$x("preceding::span/input");

    public ProfilePage clickOnShowArchivedRadiobutton() {
        showArchivedRadiobutton.scrollIntoView(false).click();
        return this;
    }

    public ProfilePage checkThatCategoryPresentInList(String categoryName) {
        $(byText(categoryName)).shouldBe(visible);
        return this;
    }

    public ProfilePage checkThatCategoryNotPresentInList(String categoryName) {
        $(byText(categoryName)).shouldNotBe(visible);
        return this;
    }

    public ProfilePage archiveCategory(String categoryName) {
        $(byText(categoryName))
                .$x("../following-sibling::div/button[@aria-label='Archive category']")
                .click();
        $x("//button[text()='Archive']")
                .click();
        return this;
    }

    public ProfilePage unarchiveCategory(String categoryName) {
        $(byText(categoryName))
                .$x("../following-sibling::span/button[@aria-label='Unarchive category']")
                .click();
        $x("//button[text()='Unarchive']")
                .click();
        return this;
    }
}
