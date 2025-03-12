package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.config.Config;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

@ParametersAreNonnullByDefault
public class ProfilePage {
    public static String url = Config.getInstance().frontUrl() + "profile";

    private final SelenideElement showArchivedRadiobutton = $(byText("Show archived")).$x("preceding::span/input");

    @Nonnull
    public ProfilePage clickOnShowArchivedRadiobutton() {
        showArchivedRadiobutton.scrollIntoView(false).click();
        return this;
    }

    @Nonnull
    public ProfilePage checkThatCategoryPresentInList(String categoryName) {
        $(byText(categoryName)).shouldBe(visible);
        return this;
    }

    @Nonnull
    public ProfilePage checkThatCategoryNotPresentInList(String categoryName) {
        $(byText(categoryName)).shouldNotBe(visible);
        return this;
    }

    @Nonnull
    public ProfilePage archiveCategory(String categoryName) {
        $(byText(categoryName))
                .$x("../following-sibling::div/button[@aria-label='Archive category']")
                .click();
        $x("//button[text()='Archive']")
                .click();
        return this;
    }

    @Nonnull
    public ProfilePage unarchiveCategory(String categoryName) {
        $(byText(categoryName))
                .$x("../following-sibling::span/button[@aria-label='Unarchive category']")
                .click();
        $x("//button[text()='Unarchive']")
                .click();
        return this;
    }
}
