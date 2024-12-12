package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
//import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.Spending;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import org.junit.jupiter.api.Test;

public class SpendingWebTest {

//    private static final Config CFG = Config.getInstance();

    @Spending(
            username = "stas",
            category = "Обучение",
            description = "Обучение Advanced 2.0",
            amount = 79990
    )
    @Test
    void categoryDescriptionShouldBeChangedFromTable(SpendJson spend) {
        final String newDescription = "Обучение Niffler Next Generation";

        Selenide.open("http://frontend.niffler.dc", LoginPage.class)
                .login("stas", "12345")
                .editSpending(spend.description())
                .setDescription(newDescription)
                .save();

        new MainPage().checkThatTableContainsSpending(newDescription);
    }
}