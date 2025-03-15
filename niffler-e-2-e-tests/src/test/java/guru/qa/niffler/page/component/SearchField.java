package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;

import static com.codeborne.selenide.Selenide.$;

public class SearchField {
    private final SelenideElement searchInput = $("input[placeholder='Search']");
    private final SelenideElement searchButton = $("button[aria-label='search']");

    @Nonnull
    @Step("Найти в поисковой строке [{query}]")
    public SearchField search(String query) {
        searchInput.setValue(query).pressEnter();
        return this;
    }

    @Nonnull
    @Step("Очистить поле поиска")
    public SearchField clearIfNotEmpty() {
        if (searchInput.getValue() == null || searchInput.getValue().isEmpty()) {
            searchInput.clear();
        }
        return this;
    }
}
