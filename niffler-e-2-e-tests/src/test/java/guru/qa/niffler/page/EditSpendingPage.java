package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.Calendar;
import guru.qa.niffler.page.component.Header;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.time.LocalDate;

import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class EditSpendingPage {
    private final SelenideElement descriptionInput = $("#description");
    private final SelenideElement amountInput = $("#amount");
    private final SelenideElement categoryInput = $("#category");
    private final SelenideElement saveBtn = $("#save");
    private final Calendar calendar = new Calendar();
    private final Header header = new Header();

    @Nonnull
    @Step("Заполнить поле description значением [{description}]")
    public EditSpendingPage setNewSpendingDescription(String description) {
        descriptionInput.clear();
        descriptionInput.setValue(description);
        return this;
    }

    @Nonnull
    @Step("Заполнить поле amount значением [{amount}]")
    public EditSpendingPage setNewSpendingAmount(String amount) {
        amountInput.setValue(amount);
        return this;
    }

    @Nonnull
    @Step("Заполнить поле category значением [{category}]")
    public EditSpendingPage setNewSpendingCategory(String category) {
        categoryInput.setValue(category);
        return this;
    }

    @Nonnull
    @Step("Заполнить поле дата значением [{date}]")
    public EditSpendingPage selectDateInCalendar(LocalDate date) {
        calendar.selectDateInCalendar(date);
        return this;
    }

    @Step("Сохранить")
    public void save() {
        saveBtn.click();
    }
}
