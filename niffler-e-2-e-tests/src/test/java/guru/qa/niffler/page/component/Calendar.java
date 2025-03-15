package guru.qa.niffler.page.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.NoSuchElementException;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.time.LocalDate;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;

@ParametersAreNonnullByDefault
public class Calendar {
    private final SelenideElement input = $("input[name='date']");
    private final SelenideElement viewCalendar = $x("//button[contains(@aria-label, 'Choose date')]");
    private final SelenideElement viewYear = $x("//button[contains(@aria-label, 'year')]");
    private final SelenideElement calendarHeader = $x("//div[contains(@class, 'CalendarHeader')]");
    private final SelenideElement previousMonth = $("button[aria-label='Previous month']");
    private final ElementsCollection days = $$("button[role='gridcell']");
    private final ElementsCollection years = $$x("//button[contains(@class, 'year')]");


    @Nonnull
    @Step("Выбрать в календаре дату {date}")
    public Calendar selectDateInCalendar(LocalDate localDate) {
        viewCalendar.click();
        viewYear.click();
        years
                .find(text(String.valueOf(localDate.getYear())))
                .scrollIntoView(false)
                .click();
        int monthNumber = 12;
        while (!calendarHeader.getText().contains(getMonthName(localDate.getMonthValue()))) {
            previousMonth.click();
            monthNumber--;
            if (monthNumber <= 0) {
                throw new NoSuchElementException("В календаре не найден месяц " + getMonthName(localDate.getMonthValue()));
            }
        }
        days
                .find(text(String.valueOf(localDate.getDayOfMonth())))
                .click();
        return this;
    }

    private String getMonthName(int num) {
        String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        return monthNames[num - 1];
    }
}
