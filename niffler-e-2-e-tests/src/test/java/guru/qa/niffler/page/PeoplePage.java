package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.PeopleTable;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class PeoplePage extends BasePage<PeoplePage> {

    private final SelenideElement peopleTab = $("a[href='/people/friends']");
    private final SelenideElement allTab = $("a[href='/people/all']");
    private final PeopleTable peopleTable = new PeopleTable($("#all"));

    @Nonnull
    @Step("Проверить, что видно исходящее предложение в  друзья [{username}]")
    public PeoplePage checkInvitationSentToUser(String username) {
        peopleTable.checkThatOutcomeRequestVisible(username);
        return this;
    }
}
