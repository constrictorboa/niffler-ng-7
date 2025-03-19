package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

public class ProfileWebTest extends BaseWebTest {
    @User(
            username = "duck",
            categories = @Category(
                    archived = false
            )
    )
    @Test
    void archivedCategoryShouldPresentInCategoriesList(CategoryJson categoryJson) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("duck", "12345")
                .goToProfilePage()
                .checkThatCategoryPresentInList(categoryJson.name())
                .archiveCategory(categoryJson.name())
                .checkThatCategoryNotPresentInList(categoryJson.name())
                .clickOnShowArchivedRadiobutton()
                .checkThatCategoryPresentInList(categoryJson.name());
    }

    @User(
            username = "duck",
            categories = @Category(
                    archived = true
            )
    )
    @Test
    void activeCategoryShouldPresentInCategoriesList(CategoryJson categoryJson) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("duck", "12345")
                .goToProfilePage()
                .checkThatCategoryNotPresentInList(categoryJson.name())
                .clickOnShowArchivedRadiobutton()
                .checkThatCategoryPresentInList(categoryJson.name())
                .unarchiveCategory(categoryJson.name())
                .clickOnShowArchivedRadiobutton()
                .checkThatCategoryPresentInList(categoryJson.name());
    }

    @User
    @Test
    void newUserCanAddNewCategory(UserJson user) {
        String newCategory = "new";
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .goToProfilePage()
                .addNewCategory(newCategory)
                .checkThatCategoryPresentInList(newCategory);
    }
}
