package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.DisabledByIssue;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class SpendingWebTest extends BaseWebTest {

    @User(
            username = "duck",
            spendings = @Spending(
                    category = "Обучение",
                    description = "Обучение Advanced 2.0",
                    amount = 79990
            )
    )
    @DisabledByIssue("3")
    @Test
    void categoryDescriptionShouldBeChangedFromTable(SpendJson spend) {
        final String newDescription = "Обучение Niffler Next Generation";

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("duck", "12345")
                .editSpending(spend.description())
                .setNewSpendingDescription(newDescription)
                .save();

        new MainPage().checkThatTableContainsSpending(newDescription);
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
                .checkTableSize(1)
                .checkThatTableContainsRow(category, amount, description, localDate);
    }
}

