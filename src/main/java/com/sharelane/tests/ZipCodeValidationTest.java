package com.sharelane.tests;

import com.sharelane.pages.RegistrationPage;
import com.sharelane.pages.ZipCodePage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ZipCodeValidationTest extends BaseTest {

    @Test
    @DisplayName("Валидный 5-значный ZIP код -> переход на страницу 2")
    void validZipCode_proceedsToNextPage() {
        ZipCodePage zipPage = new ZipCodePage(driver);
        zipPage.open(BASE_URL);
        zipPage.enterZipCode("94118");
        zipPage.clickContinue();

        RegistrationPage regPage = new RegistrationPage(driver);
        assertTrue(regPage.isRegistrationFormDisplayed(),
                "После валидного ZIP-кода должна открыться форма с First Name / Email / Password");
    }

    @Test
    @DisplayName("Пустой ZIP код -> сообщение об ошибке")
    void emptyZipCode_showsError() {
        ZipCodePage zipPage = new ZipCodePage(driver);
        zipPage.open(BASE_URL);
        zipPage.enterZipCode("");
        zipPage.clickContinue();

        assertTrue(zipPage.isErrorDisplayed(), "Пустой ZIP код должен приводить к ошибке");
    }

    @ParameterizedTest(name = "ZIP код \"{0}\" с буквами/символами -> ошибка")
    @ValueSource(strings = {"abcde", "941a8", "9411!"})
    void nonNumericZipCode_showsError(String zipCode) {
        ZipCodePage zipPage = new ZipCodePage(driver);
        zipPage.open(BASE_URL);
        zipPage.enterZipCode(zipCode);
        zipPage.clickContinue();

        assertTrue(zipPage.isErrorDisplayed(),
                "ZIP код с недопустимыми символами должен приводить к ошибке: " + zipCode);
    }

    @Test
    @DisplayName("ZIP код короче 5 цифр -> ошибка")
    void tooShortZipCode_showsError() {
        ZipCodePage zipPage = new ZipCodePage(driver);
        zipPage.open(BASE_URL);
        zipPage.enterZipCode("9411");
        zipPage.clickContinue();

        assertTrue(zipPage.isErrorDisplayed(), "ZIP код короче 5 цифр должен приводить к ошибке");
    }

    // BUG #1 в исходнике: сайт проверяет "len < 5", а должен "len != 5".
    // Поэтому ZIP длиннее 5 цифр НЕПРАВИЛЬНО проходит. Этот тест написан
    // "как должно быть" и должен УПАСТЬ на реальном сайте — так мы документируем баг.
    @Test
    @DisplayName("BUG #1: ZIP код длиннее 5 цифр должен давать ошибку, но не даёт")
    void zipCodeLongerThanFiveDigits_shouldShowErrorPerSpec() {
        ZipCodePage zipPage = new ZipCodePage(driver);
        zipPage.open(BASE_URL);
        zipPage.enterZipCode("941188"); // 6 цифр
        zipPage.clickContinue();

        assertTrue(zipPage.isErrorDisplayed(),
                "По спецификации ZIP код должен быть ровно 5 цифр (известный баг сайта)");
    }
}