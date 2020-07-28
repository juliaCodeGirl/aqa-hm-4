package ru.netology.delivery;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Nested;
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

    @Nested
    public class TestSendForm {
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

    @Nested
    public class TestInteraction {
        private LocalDate weekPlus = today.plusWeeks(1);
        private String dateWeekPlus = weekPlus.format(formatter);
        private int day = weekPlus.getDayOfMonth();
        private int todayDay = today.getDayOfMonth();

        @Test
        void shouldSelectedCity() {
            open("http://localhost:9999/");
            SelenideElement form = $("[action]");
            form.$("[data-test-id='city'] .input__control").setValue("Са");
            $$(".menu-item").find(Condition.exactText("Санкт-Петербург")).click();
            form.$("[data-test-id='date'] .input__control").doubleClick().sendKeys(Keys.DELETE);
            form.$("[data-test-id='date'] .input__control").setValue(date);
            form.$("[data-test-id='name'] .input__control").setValue("Иванов Иван");
            form.$("[data-test-id='phone'] .input__control").setValue("+79901112233");
            form.$("[data-test-id='agreement'] .checkbox__box").click();
            form.$(byText("Забронировать")).click();
            $(byText("Успешно!")).waitUntil(Condition.visible, 15000);
        }

        @Test
        void shouldSelectedDate() {
            open("http://localhost:9999/");
            SelenideElement form = $("[action]");
            form.$("[data-test-id='city'] .input__control").setValue("Са");
            $$(".menu-item").find(Condition.exactText("Санкт-Петербург")).click();
            form.$("[data-test-id='date'] .input__control").doubleClick().sendKeys(Keys.DELETE);
            $("[data-test-id='date'] .icon-button__content").click();

            if (todayDay > day) {
                $("[data-step='1']").click();
                $("[class='popup__container']").$$("td.calendar__day").find(Condition.exactText(String.valueOf(day))).click();
            } else {
                $("[class='popup__container']").$$("td.calendar__day").find(Condition.exactText(String.valueOf(day))).click();
            }

            form.$("[data-test-id='name'] .input__control").setValue("Иванов Иван");
            form.$("[data-test-id='phone'] .input__control").setValue("+79901112233");
            form.$("[data-test-id='agreement'] .checkbox__box").click();
            form.$(byText("Забронировать")).click();
            $(byText("Успешно!")).waitUntil(Condition.visible, 15000);
        }
    }
}
