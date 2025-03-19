package guru.qa.niffler.page.component;

import io.qameta.allure.Step;

import javax.annotation.Nonnull;

import static com.codeborne.selenide.Selenide.$;

public class SearchField extends BaseComponent<SearchField> {

    public SearchField() {
        super($("input[placeholder='Search']"));
    }


    @Nonnull
    @Step("Найти в поисковой строке [{query}]")
    public SearchField search(String query) {
        self.setValue(query).pressEnter();
        return this;
    }

    @Nonnull
    @Step("Очистить поле поиска")
    public SearchField clearIfNotEmpty() {
        if (self.getValue() == null || self.getValue().isEmpty()) {
            self.clear();
        }
        return this;
    }
}
