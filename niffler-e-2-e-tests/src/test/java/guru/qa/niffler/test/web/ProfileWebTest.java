package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

public class ProfileWebTest extends BaseWebTest {
    @User(
            categories = @Category(
                    archived = false
            )
    )
    @Test
    void archivedCategoryShouldPresentInCategoriesList(UserJson userJson) {
        final String categoryName = userJson.testData().categoryDescriptions()[0];

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(userJson.username(), userJson.testData().password())
                .goToProfilePage()
                .checkThatCategoryPresentInList(categoryName)
                .archiveCategory(categoryName)
                .checkAlertMessage("Category " + categoryName + " is archived")
                .checkThatCategoryNotPresentInList(categoryName)
                .clickOnShowArchivedRadiobutton()
                .checkThatCategoryPresentInList(categoryName);
    }

    @User(
            categories = @Category(
                    archived = true
            )
    )
    @Test
    void activeCategoryShouldPresentInCategoriesList(UserJson userJson) {
        final String categoryName = userJson.testData().categoryDescriptions()[0];

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(userJson.username(), userJson.testData().password())
                .goToProfilePage()
                .checkThatCategoryNotPresentInList(categoryName)
                .clickOnShowArchivedRadiobutton()
                .checkThatCategoryPresentInList(categoryName)
                .unarchiveCategory(categoryName)
                .checkAlertMessage("Category " + categoryName + " is unarchived")
                .clickOnShowArchivedRadiobutton()
                .checkThatCategoryPresentInList(categoryName);
    }

    @User
    @Test
    void newUserCanAddNewCategory(UserJson user) {
        String newCategory = "new";

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .goToProfilePage()
                .addNewCategory(newCategory)
                .checkAlertMessage("You've added new category: " + newCategory)
                .checkThatCategoryPresentInList(newCategory);
    }
}
