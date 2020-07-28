package ru.netology.delivery;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;

public class TestCardForm {
    private LocalDate today = LocalDate.now();
    private LocalDate datePlus = today.plusDays(3);
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private String date = datePlus.format(formatter);
    private String todayDate = today.format(formatter);

    @Test
    void shouldSendCorrectFrom() {
        open("http://localhost:9999/");
        SelenideElement form = $("[action]");
        form.$("[data-test-id='city'] .input__control").setValue("Москва");
        form.$("[data-test-id='date'] .input__control").doubleClick().sendKeys(Keys.DELETE);
        form.$("[data-test-id='date'] .input__control").setValue(date);
        form.$("[data-test-id='name'] .input__control").setValue("Иванов Иван");
        form.$("[data-test-id='phone'] .input__control").setValue("+79901112233");
        form.$("[data-test-id='agreement'] .checkbox__box").click();
        form.$(byText("Забронировать")).click();
        $(byText("Успешно!")).waitUntil(Condition.visible, 15000);
    }

    @Test
    void shouldBeErrorIfInvalidCity() {
        open("http://localhost:9999/");
        SelenideElement form = $("[action]");
        form.$("[data-test-id='city'] .input__control").setValue("Кругин");
        form.$("[data-test-id='date'] .input__control").doubleClick().sendKeys(Keys.DELETE);
        form.$("[data-test-id='date'] .input__control").setValue(date);
        form.$("[data-test-id='name'] .input__control").setValue("Иванов Иван");
        form.$("[data-test-id='phone'] .input__control").setValue("+79901112233");
        form.$("[data-test-id='agreement'] .checkbox__box").click();
        form.$(byText("Забронировать")).click();
        $("[data-test-id='city'] .input__sub").shouldHave(Condition.exactText("Доставка в выбранный город недоступна"));
    }

    @Test
    void shouldBeErrorIfInvalidName() {
        open("http://localhost:9999/");
        SelenideElement form = $("[action]");
        form.$("[data-test-id='city'] .input__control").setValue("Москва");
        form.$("[data-test-id='date'] .input__control").doubleClick().sendKeys(Keys.DELETE);
        form.$("[data-test-id='date'] .input__control").setValue(date);
        form.$("[data-test-id='name'] .input__control").setValue("Ivanov Ivan");
        form.$("[data-test-id='phone'] .input__control").setValue("+79901112233");
        form.$("[data-test-id='agreement'] .checkbox__box").click();
        form.$(byText("Забронировать")).click();
        $("[data-test-id='name'] .input__sub").shouldHave(Condition.exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void shouldBeErrorIfInvalidPhone() {
        open("http://localhost:9999/");
        SelenideElement form = $("[action]");
        form.$("[data-test-id='city'] .input__control").setValue("Москва");
        form.$("[data-test-id='date'] .input__control").doubleClick().sendKeys(Keys.DELETE);
        form.$("[data-test-id='date'] .input__control").setValue(date);
        form.$("[data-test-id='name'] .input__control").setValue("Иванов Иван");
        form.$("[data-test-id='phone'] .input__control").setValue("+799011");
        form.$("[data-test-id='agreement'] .checkbox__box").click();
        form.$(byText("Забронировать")).click();
        $("[data-test-id='phone'] .input__sub").shouldHave(Condition.exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void shouldBeErrorIfNotSelectedDate() {
        open("http://localhost:9999/");
        SelenideElement form = $("[action]");
        form.$("[data-test-id='city'] .input__control").setValue("Москва");
        form.$("[data-test-id='date'] .input__control").doubleClick().sendKeys(Keys.DELETE);
        form.$("[data-test-id='name'] .input__control").setValue("Иванов Иван");
        form.$("[data-test-id='phone'] .input__control").setValue("+79901112233");
        form.$("[data-test-id='agreement'] .checkbox__box").click();
        form.$(byText("Забронировать")).click();
        $("[data-test-id='date'] .input__sub").shouldHave(Condition.exactText("Неверно введена дата"));
    }

    @Test
    void shouldErrorIfUncheckedCheckbox() {
        open("http://localhost:9999/");
        SelenideElement form = $("[action]");
        form.$("[data-test-id='city'] .input__control").setValue("Москва");
        form.$("[data-test-id='date'] .input__control").doubleClick().sendKeys(Keys.DELETE);
        form.$("[data-test-id='date'] .input__control").setValue(date);
        form.$("[data-test-id='name'] .input__control").setValue("Иванов Иван");
        form.$("[data-test-id='phone'] .input__control").setValue("+79901112233");
        form.$(byText("Забронировать")).click();
        $(".input_invalid [role='presentation']").shouldHave(Condition.text("Я соглашаюсь"));
    }

    @Test
    void shouldErrorIfInvalidDate() {
        open("http://localhost:9999/");
        SelenideElement form = $("[action]");
        form.$("[data-test-id='city'] .input__control").setValue("Москва");
        form.$("[data-test-id='date'] .input__control").doubleClick().sendKeys(Keys.DELETE);
        form.$("[data-test-id='date'] .input__control").setValue(todayDate);
        form.$("[data-test-id='name'] .input__control").setValue("Иванов Иван");
        form.$("[data-test-id='phone'] .input__control").setValue("+79901112233");
        form.$(byText("Забронировать")).click();
        $("[data-test-id='date'] .input__sub").shouldHave(Condition.exactText("Заказ на выбранную дату невозможен"));
    }
}
