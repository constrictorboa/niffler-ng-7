package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.Header;
import guru.qa.niffler.page.component.SpendingTable;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import java.time.LocalDate;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class MainPage extends BasePage<MainPage> {
    private final SelenideElement statisticsBlock = $("#stat");
    private final Header header = new Header();
    private final SpendingTable spendingTable = new SpendingTable();


    @Nonnull
    public EditSpendingPage editSpending(String spendingDescription) {
        return spendingTable.editSpending(spendingDescription);
    }

    @Nonnull
    public EditSpendingPage addNewSpending() {
        return header.addSpendingPage();
    }

    public void checkThatTableContainsSpending(String spendingDescription) {
        spendingTable.checkThatTableContains(spendingDescription);
    }

    public void checkThatTableContainsRow(String category, String amount, String description, LocalDate date) {
        spendingTable.checkThatTableContains(category, amount, description, date);
    }

    @Nonnull
    public MainPage checkTableSize(int size) {
        spendingTable.checkTableSize(size);
        return this;
    }

    @Step("Проверить, что авторизация прошла успешно")
    public void checkThatLoginSuccess() {
        statisticsBlock.shouldBe(visible);
        spendingTable.checkThatHistoryOfSpendingsBlockVisible();
    }

    @Nonnull
    public FriendsPage goToFriendsPage() {
        return header.goToFriendsPage();
    }

    @Nonnull
    public ProfilePage goToProfilePage() {
        return header.goToProfilePage();
    }
}
