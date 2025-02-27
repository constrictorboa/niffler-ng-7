package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.Category;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

public class ProfileWebTest extends BaseWebTest{
    @Category(
            username = "duck",
            archived = false
    )
    @Test
    void archivedCategoryShouldPresentInCategoriesList(CategoryJson categoryJson) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("duck", "12345")
                .menuButtonClick()
                .profileButtonClick()
                .checkThatCategoryPresentInList(categoryJson.name())
                .archiveCategory(categoryJson.name())
                .checkThatCategoryNotPresentInList(categoryJson.name())
                .showArchivedRadiobuttonClick()
                .checkThatCategoryPresentInList(categoryJson.name());
    }

    @Category(
            username = "duck",
            archived = true
    )
    @Test
    void activeCategoryShouldPresentInCategoriesList(CategoryJson categoryJson) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("duck", "12345")
                .menuButtonClick()
                .profileButtonClick()
                .checkThatCategoryNotPresentInList(categoryJson.name())
                .showArchivedRadiobuttonClick()
                .checkThatCategoryPresentInList(categoryJson.name())
                .unarchiveCategory(categoryJson.name())
                .showArchivedRadiobuttonClick()
                .checkThatCategoryPresentInList(categoryJson.name());
    }
}
