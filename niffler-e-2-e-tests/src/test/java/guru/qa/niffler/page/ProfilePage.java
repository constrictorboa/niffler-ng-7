package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.config.Config;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

@ParametersAreNonnullByDefault
public class ProfilePage {
    public static String url = Config.getInstance().frontUrl() + "profile";
    public static SelenideElement addNewCategoryInput = $("#category");

    private final SelenideElement showArchivedRadiobutton = $(byText("Show archived")).$x("preceding::span/input");

    @Nonnull
    @Step("Нажать на радиобаттон 'Show archived'")
    public ProfilePage clickOnShowArchivedRadiobutton() {
        showArchivedRadiobutton.scrollIntoView(false).click();
        return this;
    }

    @Nonnull
    @Step("Проверить, что категория [{categoryName}] отображается")
    public ProfilePage checkThatCategoryPresentInList(String categoryName) {
        $(byText(categoryName)).shouldBe(visible);
        return this;
    }

    @Nonnull
    @Step("Проверить, что категория [{categoryName}] не отображается")
    public ProfilePage checkThatCategoryNotPresentInList(String categoryName) {
        $(byText(categoryName)).shouldNotBe(visible);
        return this;
    }

    @Nonnull
    @Step("Архивировать категорию")
    public ProfilePage archiveCategory(String categoryName) {
        $(byText(categoryName))
                .$x("../following-sibling::div/button[@aria-label='Archive category']")
                .click();
        $x("//button[text()='Archive']")
                .click();
        return this;
    }

    @Nonnull
    @Step("Разархивировать категорию")
    public ProfilePage unarchiveCategory(String categoryName) {
        $(byText(categoryName))
                .$x("../following-sibling::span/button[@aria-label='Unarchive category']")
                .click();
        $x("//button[text()='Unarchive']")
                .click();
        return this;
    }

    @Nonnull
    @Step("Добавить категорию [{categoryName}]")
    public ProfilePage addNewCategory(String categoryName) {
        addNewCategoryInput
                .setValue(categoryName)
                .pressEnter();
        return this;
    }
}
