package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class SpendingWebTest extends BaseWebTest {

    @User(
            spendings = @Spending(
                    category = "Обучение",
                    description = "Обучение Advanced 2.0",
                    amount = 79990
            )
    )
    //@DisabledByIssue("3")
    @Test
    void categoryDescriptionShouldBeChangedFromTable(UserJson user) {
        final String newDescription = "Обучение Niffler Next Generation";

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .editSpending("Обучение Advanced 2.0")
                .setNewSpendingDescription(newDescription)
                .save();

        new MainPage()
                .checkAlertMessage("Spending is edited successfully")
                .checkThatTableContainsSpending(newDescription);
    }

    @User
    @Test
    void newUserCanAddSpending(UserJson user) {
        String amount = "10000";
        String category = "new category";
        String description = " new description";
        LocalDate localDate = LocalDate.of(2025, 1, 1);

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .addNewSpending()
                .setNewSpendingDescription(description)
                .setNewSpendingAmount(amount)
                .setNewSpendingCategory(category)
                .selectDateInCalendar(localDate)
                .save();

        new MainPage()
                .checkAlertMessage("New spending is successfully created")
                .checkTableSize(1)
                .checkThatTableContainsRow(category, amount, description, localDate);
    }
}

