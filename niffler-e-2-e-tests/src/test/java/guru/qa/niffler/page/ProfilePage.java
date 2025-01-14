package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;

public class ProfilePage {
    private final SelenideElement showArchivedRadiobutton = $x("//span[text()='Show archived']/preceding::span/input");

    public ProfilePage showArchivedRadiobuttonClick() {
        showArchivedRadiobutton.scrollIntoView(false).click();
        return this;
    }

    public ProfilePage checkThatCategoryPresentInList(String categoryName) {
        $x("(//span[text()='" + categoryName + "'])").shouldBe(visible);
        return this;
    }

    public ProfilePage checkThatCategoryNotPresentInList(String categoryName) {
        $x("(//span[text()='" + categoryName + "'])").shouldNotBe(visible);
        return this;
    }

    public ProfilePage archiveCategory(String categoryName) {
        $x("//span[text()='" + categoryName + "']/../following-sibling::div/button[@aria-label='Archive category']").click();
        $x("//button[text()='Archive']").click();
        return this;
    }

    public ProfilePage unarchiveCategory(String categoryName) {
        $x("//span[text()='" + categoryName + "']/../following-sibling::span/button[@aria-label='Unarchive category']").click();
        $x("//button[text()='Unarchive']").click();
        return this;
    }
}
