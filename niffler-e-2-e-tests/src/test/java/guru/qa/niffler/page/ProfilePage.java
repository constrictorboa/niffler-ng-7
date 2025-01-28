package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.attributeMatching;
import static com.codeborne.selenide.Condition.disabled;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.$x;

public class ProfilePage {
    private final SelenideElement showArchivedRadiobutton = $(byText("Show archived")).$x("preceding::span/input");
    private final SelenideElement avatar = $("#image__input").parent().$("img");
    private final SelenideElement userName = $("#username");
    private final SelenideElement nameInput = $("#name");
    private final SelenideElement photoInput = $("input[type='file']");
    private final SelenideElement submitButton = $("button[type='submit']");
    private final SelenideElement categoryInput = $("input[name='category']");
    private final SelenideElement archivedSwitcher = $(".MuiSwitch-input");
    private final ElementsCollection bubbles = $$(".MuiChip-filled.MuiChip-colorPrimary");
    private final ElementsCollection bubblesArchived = $$(".MuiChip-filled.MuiChip-colorDefault");

    public ProfilePage showArchivedRadiobuttonClick() {
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

    public ProfilePage setName(String name) {
        nameInput.clear();
        nameInput.setValue(name);
        return this;
    }

    public ProfilePage uploadPhotoFromClasspath(String path) {
        photoInput.uploadFromClasspath(path);
        return this;
    }

    public ProfilePage addCategory(String category) {
        categoryInput.setValue(category).pressEnter();
        return this;
    }

    public ProfilePage checkCategoryExists(String category) {
        bubbles.find(text(category)).shouldBe(visible);
        return this;
    }

    public ProfilePage checkArchivedCategoryExists(String category) {
        archivedSwitcher.click();
        bubblesArchived.find(text(category)).shouldBe(visible);
        return this;
    }

    public ProfilePage checkUsername(String username) {
        this.userName.should(value(username));
        return this;
    }

    public ProfilePage checkName(String name) {
        nameInput.shouldHave(value(name));
        return this;
    }

    public ProfilePage checkPhotoExist() {
        avatar.should(attributeMatching("src", "data:image.*"));
        return this;
    }

    public ProfilePage checkThatCategoryInputDisabled() {
        categoryInput.should(disabled);
        return this;
    }

    public ProfilePage submitProfile() {
        submitButton.click();
        return this;
    }
}
