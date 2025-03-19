package guru.qa.niffler.page.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.model.FilterData;
import guru.qa.niffler.model.MonthEnum;
import guru.qa.niffler.page.EditSpendingPage;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import java.time.LocalDate;
import java.util.Objects;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class SpendingTable {
    private final ElementsCollection tableRows = $("#spendings tbody").$$("tr");
    private final SelenideElement historyOfSpendingsBlock = $("#spendings");
    private final SelenideElement deleteButton = $("button #delete");
    private final SelenideElement nextButton = $("button #page-next");
    private final SelenideElement timeFilter = $("div #period");
    private final ElementsCollection dropdownList = $$("ul[role='listbox'] li");
    private final SearchField searchField = new SearchField();

    @Nonnull
    @Step("Отредактировать spending [{spendingDescription}]")
    public EditSpendingPage editSpending(String spendingDescription) {
        tableRows.find(text(spendingDescription)).$$("td").get(5).click();
        return new EditSpendingPage();
    }

    @Step("Проверить видимость блока History of Spendings")
    public void checkThatHistoryOfSpendingsBlockVisible() {
        historyOfSpendingsBlock.shouldBe(visible);
    }

    @Nonnull
    @Step("Выбрать период {period}")
    public SpendingTable selectPeriod(FilterData period) {
        timeFilter.click();
        dropdownList.stream()
                .filter(elem -> Objects.equals(elem.getAttribute("data-value"), period.name()))
                .findFirst()
                .get()
                .click();
        return this;
    }

    @Nonnull
    @Step("Удалить spending [{description}]")
    public SpendingTable deleteSpending(String description) {
        tableRows.find(text(description)).$$("td").get(1).click();
        deleteButton.click();
        return this;
    }

    @Nonnull
    @Step("Найти spending [{description}]")
    public SpendingTable searchSpendingByDescription(String description) {
        searchField.search(description);
        return this;
    }

    @Step("Проверить, что таблица 'History of Spendings' содержит spending")
    public void checkThatTableContains(String... expectedSpends) {
        for (String spend : expectedSpends) {
            searchSpendingByDescription(spend);
            tableRows.find(text(spend)).should(visible);
        }
    }

    @Step("Проверить, что таблица 'History of Spendings' содержит spending")
    public void checkThatTableContains(String category, String amount, String description, LocalDate date) {
        ElementsCollection rowElems = tableRows.find(text(description)).$$("td");
        rowElems.get(1).shouldBe(text(category));
        rowElems.get(2).shouldBe(text(amount));
        rowElems.get(3).shouldBe(text(description));
        String day = String.valueOf(date.getDayOfMonth());
        if (day.length() < 2) {
            day = "0" + day;
        }
        rowElems.get(4)
                .shouldBe(text(
                        MonthEnum.getMonthShortName(date.getMonthValue()) + " "
                                + day + ", "
                                + date.getYear()));

    }

    @Step("Проверить размер таблицы History of Spendings")
    public void checkTableSize(int expectedSize) {
        tableRows.should(size(expectedSize));
    }

}
